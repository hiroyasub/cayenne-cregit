begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|flush
operator|.
name|operation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataRow
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Persistent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|QueryResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataDomain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|ObjectStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|flush
operator|.
name|EffectiveOpId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|parser
operator|.
name|ASTDbPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|graph
operator|.
name|GraphManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbRelationship
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|ObjectIdQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|SingleEntryMap
import|;
end_import

begin_comment
comment|/**  * Db operation sorted that builds dependency graph and uses topological sort to get final order.  * This in general slower than {@link DefaultDbRowOpSorter} but can handle more edge cases (like multiple meaningful PKs/FKs).  *  * TODO: possible optimizations could be optional logic parts (e.g. detecting effective id intersections,  *       reflexive dependencies, etc.)  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|GraphBasedDbRowOpSorter
implements|implements
name|DbRowOpSorter
block|{
specifier|private
specifier|final
name|DbRowOpTypeVisitor
name|rowOpTypeVisitor
init|=
operator|new
name|DbRowOpTypeVisitor
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Provider
argument_list|<
name|DataDomain
argument_list|>
name|dataDomainProvider
decl_stmt|;
specifier|private
specifier|volatile
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
argument_list|>
name|relationships
decl_stmt|;
specifier|public
name|GraphBasedDbRowOpSorter
parameter_list|(
annotation|@
name|Inject
name|Provider
argument_list|<
name|DataDomain
argument_list|>
name|dataDomainProvider
parameter_list|)
block|{
name|this
operator|.
name|dataDomainProvider
operator|=
name|dataDomainProvider
expr_stmt|;
block|}
specifier|private
name|void
name|initDataSync
parameter_list|()
block|{
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
argument_list|>
name|localRelationships
init|=
name|relationships
decl_stmt|;
if|if
condition|(
name|localRelationships
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|localRelationships
operator|=
name|relationships
expr_stmt|;
if|if
condition|(
name|localRelationships
operator|==
literal|null
condition|)
block|{
name|initDataNoSync
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Init all the data we need for faster processing actual rows.      */
specifier|private
name|void
name|initDataNoSync
parameter_list|()
block|{
name|relationships
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|EntityResolver
name|resolver
init|=
name|dataDomainProvider
operator|.
name|get
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|getDbEntities
argument_list|()
operator|.
name|forEach
argument_list|(
name|entity
lambda|->
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|forEach
argument_list|(
name|dbRelationship
lambda|->
block|{
block_content|if(dbRelationship.isToMany(
argument_list|)
operator|||
operator|!
name|dbRelationship
operator|.
name|isToPK
argument_list|()
operator|||
name|dbRelationship
operator|.
name|isToDependentPK
argument_list|()
block_content|)
block|{
comment|// TODO: can we ignore all of these relationships?
return|return;
block|}
block|relationships                         .computeIfAbsent(entity
operator|,
function|e -> new ArrayList<>
parameter_list|()
block|)
operator|.
name|add
argument_list|(
name|dbRelationship
argument_list|)
expr_stmt|;
end_class

begin_empty_stmt
unit|})         )
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Override
specifier|public
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sort
parameter_list|(
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|dbRows
parameter_list|)
block|{
comment|// lazy init Cayenne model data
name|initDataSync
argument_list|()
expr_stmt|;
comment|// build index op by ID
name|Map
argument_list|<
name|EffectiveOpId
argument_list|,
name|List
argument_list|<
name|DbRowOp
argument_list|>
argument_list|>
name|indexById
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|dbRows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|dbRows
operator|.
name|forEach
argument_list|(
name|op
lambda|->
name|indexById
operator|.
name|computeIfAbsent
argument_list|(
name|effectiveIdFor
argument_list|(
name|op
argument_list|)
argument_list|,
name|id
lambda|->
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|op
argument_list|)
argument_list|)
expr_stmt|;
comment|// build ops dependency graph
name|DbRowOpGraph
name|graph
init|=
operator|new
name|DbRowOpGraph
argument_list|()
decl_stmt|;
name|dbRows
operator|.
name|forEach
argument_list|(
name|op
lambda|->
block|{
name|processRelationships
argument_list|(
name|indexById
argument_list|,
name|graph
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|processMeaningfulIds
argument_list|(
name|indexById
argument_list|,
name|graph
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
name|op
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// sort
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sortedOps
init|=
name|graph
operator|.
name|topSort
argument_list|()
decl_stmt|;
return|return
name|sortedOps
return|;
block|}
end_function

begin_function
specifier|private
name|void
name|processRelationships
parameter_list|(
name|Map
argument_list|<
name|EffectiveOpId
argument_list|,
name|List
argument_list|<
name|DbRowOp
argument_list|>
argument_list|>
name|indexByDbId
parameter_list|,
name|DbRowOpGraph
name|graph
parameter_list|,
name|DbRowOp
name|op
parameter_list|)
block|{
comment|// get graph edges for reflexive relationships
name|DbRowOpType
name|opType
init|=
name|op
operator|.
name|accept
argument_list|(
name|rowOpTypeVisitor
argument_list|)
decl_stmt|;
name|relationships
operator|.
name|getOrDefault
argument_list|(
name|op
operator|.
name|getEntity
argument_list|()
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|relationship
lambda|->
name|getParentsOpId
argument_list|(
name|op
argument_list|,
name|relationship
argument_list|)
operator|.
name|forEach
argument_list|(
name|parentOpId
lambda|->
name|indexByDbId
operator|.
name|getOrDefault
argument_list|(
name|parentOpId
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|parentOp
lambda|->
block|{
block_content|if(op == parentOp
argument_list|)
block|{
return|return;
block|}
name|DbRowOpType
name|parentOpType
init|=
name|parentOp
operator|.
name|accept
argument_list|(
name|rowOpTypeVisitor
argument_list|)
decl_stmt|;
comment|// 1. Our insert can depend on others insert or update
comment|// 2. Our update can depend on others insert or update, or others delete can depend on our update
comment|// 3. Others delete can depend on our delete
switch|switch
condition|(
name|opType
condition|)
block|{
case|case
name|INSERT
case|:
if|if
condition|(
name|parentOpType
operator|!=
name|DbRowOpType
operator|.
name|DELETE
condition|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|op
argument_list|,
name|parentOp
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|UPDATE
case|:
if|if
condition|(
name|parentOpType
operator|!=
name|DbRowOpType
operator|.
name|DELETE
condition|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|op
argument_list|,
name|parentOp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|graph
operator|.
name|add
argument_list|(
name|parentOp
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|DELETE
case|:
if|if
condition|(
name|parentOpType
operator|==
name|DbRowOpType
operator|.
name|DELETE
condition|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|parentOp
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_function

begin_empty_stmt
unit|)             )         )
empty_stmt|;
end_empty_stmt

begin_function
unit|}      private
name|void
name|processMeaningfulIds
parameter_list|(
name|Map
argument_list|<
name|EffectiveOpId
argument_list|,
name|List
argument_list|<
name|DbRowOp
argument_list|>
argument_list|>
name|indexById
parameter_list|,
name|DbRowOpGraph
name|graph
parameter_list|,
name|DbRowOp
name|op
parameter_list|)
block|{
comment|// get graph edges from same ID operations, for such operations delete depends on other operations
name|indexById
operator|.
name|get
argument_list|(
name|effectiveIdFor
argument_list|(
name|op
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|sameIdOp
lambda|->
block|{
if|if
condition|(
name|op
operator|==
name|sameIdOp
condition|)
block|{
return|return;
block|}
name|DbRowOpType
name|sameIdOpType
init|=
name|sameIdOp
operator|.
name|accept
argument_list|(
name|rowOpTypeVisitor
argument_list|)
decl_stmt|;
if|if
condition|(
name|sameIdOpType
operator|==
name|DbRowOpType
operator|.
name|DELETE
condition|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|op
argument_list|,
name|sameIdOp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|Collection
argument_list|<
name|EffectiveOpId
argument_list|>
name|getParentsOpId
parameter_list|(
name|DbRowOp
name|op
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|)
block|{
return|return
name|op
operator|.
name|accept
argument_list|(
operator|new
name|DbRowOpSnapshotVisitor
argument_list|(
name|relationship
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|snapshot
lambda|->
name|this
operator|.
name|effectiveIdFor
argument_list|(
name|relationship
argument_list|,
name|snapshot
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|EffectiveOpId
name|effectiveIdFor
parameter_list|(
name|DbRowOp
name|op
parameter_list|)
block|{
return|return
operator|new
name|EffectiveOpId
argument_list|(
name|op
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|op
operator|.
name|getChangeId
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|EffectiveOpId
name|effectiveIdFor
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|opSnapshot
parameter_list|)
block|{
name|int
name|len
init|=
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
init|=
name|len
operator|==
literal|1
condition|?
operator|new
name|SingleEntryMap
argument_list|<>
argument_list|(
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetName
argument_list|()
argument_list|)
else|:
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|len
argument_list|)
decl_stmt|;
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|forEach
argument_list|(
name|join
lambda|->
block|{
name|Object
name|value
init|=
name|opSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|idMap
operator|.
name|put
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|idMap
operator|.
name|size
argument_list|()
operator|!=
name|len
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|EffectiveOpId
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|,
name|idMap
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_class
specifier|private
specifier|static
class|class
name|DbRowOpSnapshotVisitor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|Collection
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|>
block|{
specifier|private
specifier|final
name|DbRelationship
name|relationship
decl_stmt|;
specifier|private
name|DbRowOpSnapshotVisitor
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|visitInsert
parameter_list|(
name|InsertDbRowOp
name|dbRow
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|dbRow
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|visitUpdate
parameter_list|(
name|UpdateDbRowOp
name|dbRow
parameter_list|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|result
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|updatedSnapshot
init|=
name|dbRow
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRow
operator|.
name|getChangeId
argument_list|()
operator|.
name|getEntityName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|updatedSnapshot
argument_list|)
return|;
block|}
name|result
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// get updated state from operation
name|result
operator|.
name|add
argument_list|(
name|updatedSnapshot
argument_list|)
expr_stmt|;
comment|// get previous state from cache, but only for update attributes
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cachedSnapshot
init|=
name|getCachedSnapshot
argument_list|(
name|dbRow
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
name|cachedSnapshot
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
block|{
if|if
condition|(
operator|!
name|updatedSnapshot
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|cachedSnapshot
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|visitDelete
parameter_list|(
name|DeleteDbRowOp
name|dbRow
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cachedSnapshot
init|=
name|getCachedSnapshot
argument_list|(
name|dbRow
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
comment|// check and merge flattened IDs snapshots
name|GraphManager
name|graphManager
init|=
name|dbRow
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getGraphManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|graphManager
operator|instanceof
name|ObjectStore
condition|)
block|{
name|ObjectStore
name|store
init|=
operator|(
name|ObjectStore
operator|)
name|graphManager
decl_stmt|;
name|store
operator|.
name|getFlattenedIds
argument_list|(
name|dbRow
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|flattenedId
lambda|->
block|{
comment|// map values of flattened ids from target to source
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idSnapshot
init|=
name|flattenedId
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|forEach
argument_list|(
name|join
lambda|->
block|{
name|Object
name|value
init|=
name|idSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|cachedSnapshot
operator|.
name|put
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
name|cachedSnapshot
operator|.
name|putAll
argument_list|(
name|dbRow
operator|.
name|getQualifier
argument_list|()
operator|.
name|getSnapshot
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|cachedSnapshot
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCachedSnapshot
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|true
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
name|object
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getChannel
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|DataRow
argument_list|>
operator|)
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
operator|||
name|result
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|// copy snapshot as we can modify it later
return|return
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

begin_class
specifier|private
specifier|static
class|class
name|DbRowOpTypeVisitor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|DbRowOpType
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitDelete
parameter_list|(
name|DeleteDbRowOp
name|dbRow
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|DELETE
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitInsert
parameter_list|(
name|InsertDbRowOp
name|dbRow
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|INSERT
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitUpdate
parameter_list|(
name|UpdateDbRowOp
name|dbRow
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|UPDATE
return|;
block|}
block|}
end_class

unit|}
end_unit

