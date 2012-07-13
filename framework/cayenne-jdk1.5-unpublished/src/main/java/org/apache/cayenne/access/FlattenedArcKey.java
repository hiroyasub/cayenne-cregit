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
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|access
operator|.
name|DataDomainSyncBucket
operator|.
name|PropagatedValueFactory
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
name|util
operator|.
name|DefaultOperationObserver
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
name|dba
operator|.
name|DbAdapter
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
name|DbAttribute
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
name|DbJoin
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
name|ObjRelationship
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
name|Query
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
name|SQLTemplate
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
name|Util
import|;
end_import

begin_comment
comment|/**  * A holder of flattened relationship modification data.  *   * @since 1.2  */
end_comment

begin_class
specifier|final
class|class
name|FlattenedArcKey
block|{
name|ObjectId
name|sourceId
decl_stmt|;
name|ObjectId
name|destinationId
decl_stmt|;
name|ObjRelationship
name|relationship
decl_stmt|;
name|ObjRelationship
name|reverseRelationship
decl_stmt|;
name|String
name|compareToken
decl_stmt|;
name|FlattenedArcKey
parameter_list|(
name|ObjectId
name|sourceId
parameter_list|,
name|ObjectId
name|destinationId
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|this
operator|.
name|sourceId
operator|=
name|sourceId
expr_stmt|;
name|this
operator|.
name|destinationId
operator|=
name|destinationId
expr_stmt|;
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|reverseRelationship
operator|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
expr_stmt|;
comment|// build a string token to make comparison (or at least hashcode) indepent from
comment|// direction
name|String
name|relName1
init|=
name|relationship
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseRelationship
operator|!=
literal|null
condition|)
block|{
name|String
name|relName2
init|=
name|reverseRelationship
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Find the lexically lesser name and use it as the name of the source, then
comment|// use the second.
comment|// If equal (the same name), it doesn't matter which order...
if|if
condition|(
name|relName1
operator|.
name|compareTo
argument_list|(
name|relName2
argument_list|)
operator|<=
literal|0
condition|)
block|{
name|this
operator|.
name|compareToken
operator|=
name|relName1
operator|+
literal|"."
operator|+
name|relName2
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|compareToken
operator|=
name|relName2
operator|+
literal|"."
operator|+
name|relName1
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|compareToken
operator|=
name|relName1
expr_stmt|;
block|}
block|}
comment|/**      * Returns a join DbEntity for the single-step flattened relationship.      */
name|DbEntity
name|getJoinEntity
parameter_list|()
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relList
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|relList
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only single-step flattened relationships are supported in this operation: "
operator|+
name|relationship
argument_list|)
throw|;
block|}
name|DbRelationship
name|firstDbRel
init|=
name|relList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
operator|(
name|DbEntity
operator|)
name|firstDbRel
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
comment|/**      * Returns a snapshot for join record for the single-step flattened relationship,      * generating value for the primary key column if it is not propagated via the      * relationships.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|buildJoinSnapshotForInsert
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
name|lazyJoinSnapshot
argument_list|()
decl_stmt|;
name|boolean
name|autoPkDone
init|=
literal|false
decl_stmt|;
name|DbEntity
name|joinEntity
init|=
name|getJoinEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|dbAttr
range|:
name|joinEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|String
name|dbAttrName
init|=
name|dbAttr
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|snapshot
operator|.
name|containsKey
argument_list|(
name|dbAttrName
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
comment|// skip db-generated... looks like we don't care about the actual PK value
comment|// here, so no need to retrieve db-generated pk back to Java.
if|if
condition|(
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
operator|&&
name|dbAttr
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|autoPkDone
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Primary Key autogeneration only works for a single attribute."
argument_list|)
throw|;
block|}
comment|// finally, use database generation mechanism
try|try
block|{
name|Object
name|pkValue
init|=
name|adapter
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|dbAttr
argument_list|)
decl_stmt|;
name|snapshot
operator|.
name|put
argument_list|(
name|dbAttrName
argument_list|,
name|pkValue
argument_list|)
expr_stmt|;
name|autoPkDone
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|snapshot
return|;
block|}
comment|/**      * Returns pk snapshots for join records for the single-step flattened relationship.      * Multiple joins between the same pair of objects are theoretically possible, so the      * return value is a list.      */
name|List
name|buildJoinSnapshotsForDelete
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|Map
name|snapshot
init|=
name|eagerJoinSnapshot
argument_list|()
decl_stmt|;
name|DbEntity
name|joinEntity
init|=
name|getJoinEntity
argument_list|()
decl_stmt|;
name|boolean
name|fetchKey
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|dbAttr
range|:
name|joinEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|String
name|dbAttrName
init|=
name|dbAttr
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|snapshot
operator|.
name|containsKey
argument_list|(
name|dbAttrName
argument_list|)
condition|)
block|{
name|fetchKey
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|fetchKey
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|snapshot
argument_list|)
return|;
block|}
comment|// ok, the key is not included in snapshot, must do the fetch...
comment|// TODO: this should be optimized in the future, but now DeleteBatchQuery
comment|// expects a PK snapshot, so we must provide it.
name|StringBuilder
name|sql
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"SELECT "
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|joinEntity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|int
name|i
init|=
name|pk
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|joinEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|"#result('"
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|"')"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|--
name|i
operator|>
literal|0
condition|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|sql
operator|.
name|append
argument_list|(
literal|" FROM "
argument_list|)
operator|.
name|append
argument_list|(
name|joinEntity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|i
operator|=
name|snapshot
operator|.
name|size
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|snapshot
operator|.
name|keySet
argument_list|()
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|" #bindEqual($"
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|--
name|i
operator|>
literal|0
condition|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
block|}
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|joinEntity
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|sql
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|snapshot
argument_list|)
expr_stmt|;
specifier|final
name|List
index|[]
name|result
init|=
operator|new
name|List
index|[
literal|1
index|]
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
operator|new
name|DefaultOperationObserver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
name|dataRows
parameter_list|)
block|{
name|result
index|[
literal|0
index|]
operator|=
name|dataRows
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextQueryException
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Raising from query exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Raising from underlyingQueryEngine exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|result
index|[
literal|0
index|]
return|;
block|}
name|boolean
name|isBidirectional
parameter_list|()
block|{
return|return
name|reverseRelationship
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// TODO: use hashcode builder to make a better hashcode.
return|return
name|sourceId
operator|.
name|hashCode
argument_list|()
operator|+
name|destinationId
operator|.
name|hashCode
argument_list|()
operator|+
name|compareToken
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * Defines equal based on whether the relationship is bidirectional.      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|FlattenedArcKey
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FlattenedArcKey
name|update
init|=
operator|(
name|FlattenedArcKey
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|compareToken
operator|.
name|equals
argument_list|(
name|update
operator|.
name|compareToken
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|bidi
init|=
name|isBidirectional
argument_list|()
decl_stmt|;
if|if
condition|(
name|bidi
operator|!=
name|update
operator|.
name|isBidirectional
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
name|bidi
operator|)
condition|?
name|bidiEquals
argument_list|(
name|update
argument_list|)
else|:
name|uniEquals
argument_list|(
name|update
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|bidiEquals
parameter_list|(
name|FlattenedArcKey
name|update
parameter_list|)
block|{
return|return
operator|(
name|sourceId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|sourceId
argument_list|)
operator|&&
name|destinationId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|destinationId
argument_list|)
operator|)
operator|||
operator|(
name|this
operator|.
name|sourceId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|destinationId
argument_list|)
operator|&&
name|this
operator|.
name|destinationId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|sourceId
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|uniEquals
parameter_list|(
name|FlattenedArcKey
name|update
parameter_list|)
block|{
return|return
operator|(
name|this
operator|.
name|sourceId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|sourceId
argument_list|)
operator|&&
name|this
operator|.
name|destinationId
operator|.
name|equals
argument_list|(
name|update
operator|.
name|destinationId
argument_list|)
operator|)
return|;
block|}
specifier|private
name|Map
name|eagerJoinSnapshot
parameter_list|()
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relList
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|relList
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only single-step flattened relationships are supported in this operation: "
operator|+
name|relationship
argument_list|)
throw|;
block|}
name|DbRelationship
name|firstDbRel
init|=
name|relList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DbRelationship
name|secondDbRel
init|=
name|relList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sourceId
init|=
name|this
operator|.
name|sourceId
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|destinationId
init|=
name|this
operator|.
name|destinationId
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|sourceId
operator|.
name|size
argument_list|()
operator|+
name|destinationId
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|firstDbRel
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|snapshot
operator|.
name|put
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|sourceId
operator|.
name|get
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DbJoin
name|join
range|:
name|secondDbRel
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|snapshot
operator|.
name|put
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|,
name|destinationId
operator|.
name|get
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|snapshot
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|lazyJoinSnapshot
parameter_list|()
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relList
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|relList
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only single-step flattened relationships are supported in this operation: "
operator|+
name|relationship
argument_list|)
throw|;
block|}
name|DbRelationship
name|firstDbRel
init|=
name|relList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DbRelationship
name|secondDbRel
init|=
name|relList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|fromSourceJoins
init|=
name|firstDbRel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|toTargetJoins
init|=
name|secondDbRel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|fromSourceJoins
operator|.
name|size
argument_list|()
operator|+
name|toTargetJoins
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|numJoins
init|=
name|fromSourceJoins
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|numJoins
condition|;
name|i
operator|++
control|)
block|{
name|DbJoin
name|join
init|=
name|fromSourceJoins
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
operator|new
name|PropagatedValueFactory
argument_list|(
name|sourceId
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
name|snapshot
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|numJoins
init|=
name|toTargetJoins
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|numJoins
condition|;
name|i
operator|++
control|)
block|{
name|DbJoin
name|join
init|=
name|toTargetJoins
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
operator|new
name|PropagatedValueFactory
argument_list|(
name|destinationId
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
decl_stmt|;
name|snapshot
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
return|return
name|snapshot
return|;
block|}
block|}
end_class

end_unit

