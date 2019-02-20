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
name|query
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
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
name|access
operator|.
name|types
operator|.
name|ValueObjectType
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
name|types
operator|.
name|ValueObjectTypeRegistry
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
name|Expression
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
name|ExpressionFactory
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
name|TraversalHandler
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
name|exp
operator|.
name|parser
operator|.
name|ASTFunctionCall
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
name|ASTScalar
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
name|property
operator|.
name|BaseProperty
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
name|map
operator|.
name|EntityResult
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
name|ObjAttribute
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
name|ObjEntity
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
name|map
operator|.
name|PathComponent
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
name|SQLResult
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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
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
name|CayenneMapEntry
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|SelectQueryMetadata
extends|extends
name|BaseQueryMetadata
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7465922769303943945L
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathSplitAliases
decl_stmt|;
specifier|private
name|boolean
name|isSingleResultSetMapping
decl_stmt|;
specifier|private
name|boolean
name|suppressingDistinct
decl_stmt|;
annotation|@
name|Override
name|void
name|copyFromInfo
parameter_list|(
name|QueryMetadata
name|info
parameter_list|)
block|{
name|super
operator|.
name|copyFromInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|this
operator|.
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|info
operator|.
name|getPathSplitAliases
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|resolve
parameter_list|(
name|Object
name|root
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|resolve
argument_list|(
name|root
argument_list|,
name|resolver
argument_list|)
condition|)
block|{
comment|// generate unique cache key, but only if we are caching..
if|if
condition|(
name|cacheStrategy
operator|!=
literal|null
operator|&&
name|cacheStrategy
operator|!=
name|QueryCacheStrategy
operator|.
name|NO_CACHE
condition|)
block|{
name|this
operator|.
name|cacheKey
operator|=
name|makeCacheKey
argument_list|(
name|query
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
name|resolveAutoAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|buildResultSetMappingForColumns
argument_list|(
name|query
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
name|isSingleResultSetMapping
operator|=
name|query
operator|.
name|canReturnScalarValue
argument_list|()
operator|&&
name|super
operator|.
name|isSingleResultSetMapping
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|makeCacheKey
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
comment|// create a unique key based on entity or columns, qualifier, ordering, fetch offset and limit
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// handler to create string out of expressions, created lazily
name|TraversalHandler
name|traversalHandler
init|=
literal|null
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|getObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|"db:"
argument_list|)
operator|.
name|append
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getColumns
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|query
operator|.
name|getColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|traversalHandler
operator|=
operator|new
name|ToCacheKeyTraversalHandler
argument_list|(
name|resolver
operator|.
name|getValueObjectTypeRegistry
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
for|for
control|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|property
range|:
name|query
operator|.
name|getColumns
argument_list|()
control|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|"/c:"
argument_list|)
expr_stmt|;
name|property
operator|.
name|getExpression
argument_list|()
operator|.
name|traverse
argument_list|(
name|traversalHandler
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|query
operator|.
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|traversalHandler
operator|==
literal|null
condition|)
block|{
name|traversalHandler
operator|=
operator|new
name|ToCacheKeyTraversalHandler
argument_list|(
name|resolver
operator|.
name|getValueObjectTypeRegistry
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|getQualifier
argument_list|()
operator|.
name|traverse
argument_list|(
name|traversalHandler
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Ordering
name|o
range|:
name|query
operator|.
name|getOrderings
argument_list|()
control|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|o
operator|.
name|getSortSpecString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|o
operator|.
name|isAscending
argument_list|()
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|":d"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|.
name|isCaseInsensitive
argument_list|()
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|":i"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|query
operator|.
name|getFetchOffset
argument_list|()
operator|>
literal|0
operator|||
name|query
operator|.
name|getFetchLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|query
operator|.
name|getFetchOffset
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'o'
argument_list|)
operator|.
name|append
argument_list|(
name|query
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getFetchLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'l'
argument_list|)
operator|.
name|append
argument_list|(
name|query
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add prefetch to cache key per CAY-2349
if|if
condition|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|traverse
argument_list|(
operator|new
name|ToCacheKeyPrefetchProcessor
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|resolveAutoAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|resolveQualifierAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|resolveColumnsAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|resolveOrderingAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|resolveHavingQualifierAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// TODO: include aliases in prefetches? flattened attributes?
block|}
specifier|private
name|void
name|resolveQualifierAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|Expression
name|qualifier
init|=
name|query
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
name|resolveAutoAliases
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|resolveColumnsAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|Collection
argument_list|<
name|BaseProperty
argument_list|<
name|?
argument_list|>
argument_list|>
name|columns
init|=
name|query
operator|.
name|getColumns
argument_list|()
decl_stmt|;
if|if
condition|(
name|columns
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|property
range|:
name|columns
control|)
block|{
name|Expression
name|propertyExpression
init|=
name|property
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyExpression
operator|!=
literal|null
condition|)
block|{
name|resolveAutoAliases
argument_list|(
name|propertyExpression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|resolveOrderingAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
init|=
name|query
operator|.
name|getOrderings
argument_list|()
decl_stmt|;
if|if
condition|(
name|orderings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Ordering
name|ordering
range|:
name|orderings
control|)
block|{
name|Expression
name|sortSpec
init|=
name|ordering
operator|.
name|getSortSpec
argument_list|()
decl_stmt|;
if|if
condition|(
name|sortSpec
operator|!=
literal|null
condition|)
block|{
name|resolveAutoAliases
argument_list|(
name|sortSpec
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|resolveHavingQualifierAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|Expression
name|havingQualifier
init|=
name|query
operator|.
name|getHavingQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|havingQualifier
operator|!=
literal|null
condition|)
block|{
name|resolveAutoAliases
argument_list|(
name|havingQualifier
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|resolveAutoAliases
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
init|=
name|expression
operator|.
name|getPathAliases
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|aliases
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|pathSplitAliases
operator|==
literal|null
condition|)
block|{
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|aliases
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|pathSplitAliases
operator|.
name|compute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
parameter_list|(
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't add the same alias to different path segments."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|len
init|=
name|expression
operator|.
name|getOperandCount
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|operand
init|=
name|expression
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|operand
operator|instanceof
name|Expression
condition|)
block|{
name|resolveAutoAliases
argument_list|(
operator|(
name|Expression
operator|)
name|operand
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * @since 3.0 	 */
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathSplitAliases
parameter_list|()
block|{
return|return
name|pathSplitAliases
operator|!=
literal|null
condition|?
name|pathSplitAliases
else|:
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|void
name|addPathSplitAliases
parameter_list|(
name|String
name|path
parameter_list|,
name|String
modifier|...
name|aliases
parameter_list|)
block|{
if|if
condition|(
name|aliases
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null aliases"
argument_list|)
throw|;
block|}
if|if
condition|(
name|aliases
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No aliases specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|pathSplitAliases
operator|==
literal|null
condition|)
block|{
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|alias
range|:
name|aliases
control|)
block|{
name|pathSplitAliases
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Build DB result descriptor, that will be used to read and convert raw result of ColumnSelect 	 * @since 4.0 	 */
specifier|private
name|void
name|buildResultSetMappingForColumns
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|query
operator|.
name|getColumns
argument_list|()
operator|==
literal|null
operator|||
name|query
operator|.
name|getColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|SQLResult
name|result
init|=
operator|new
name|SQLResult
argument_list|()
decl_stmt|;
for|for
control|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|column
range|:
name|query
operator|.
name|getColumns
argument_list|()
control|)
block|{
name|Expression
name|exp
init|=
name|column
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|column
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|?
name|exp
operator|.
name|expName
argument_list|()
else|:
name|column
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|fullObject
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|exp
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|OBJ_PATH
condition|)
block|{
comment|// check if this is toOne relation
name|Expression
name|dbPath
init|=
name|this
operator|.
name|getObjEntity
argument_list|()
operator|.
name|translateToDbPath
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|DbRelationship
name|rel
init|=
name|findRelationByPath
argument_list|(
name|dbEntity
argument_list|,
name|dbPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|rel
operator|!=
literal|null
operator|&&
operator|!
name|rel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// it this path is toOne relation, than select full object for it
name|fullObject
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|exp
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FULL_OBJECT
condition|)
block|{
name|fullObject
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|fullObject
condition|)
block|{
comment|// detected full object column
if|if
condition|(
name|getPageSize
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// for paginated queries keep only IDs
name|result
operator|.
name|addEntityResult
argument_list|(
name|buildEntityIdResultForColumn
argument_list|(
name|column
argument_list|,
name|resolver
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// will unwrap to full set of db-columns (with join prefetch optionally)
name|result
operator|.
name|addEntityResult
argument_list|(
name|buildEntityResultForColumn
argument_list|(
name|query
argument_list|,
name|column
argument_list|,
name|resolver
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// scalar column
name|result
operator|.
name|addColumnResult
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|resultSetMapping
operator|=
name|result
operator|.
name|getResolvedComponents
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Collect metadata for result with ObjectId (used for paginated queries with FullObject columns) 	 * 	 * @param column full object column 	 * @param resolver entity resolver 	 * @return Entity result 	 */
specifier|private
name|EntityResult
name|buildEntityIdResultForColumn
parameter_list|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|column
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|EntityResult
name|result
init|=
operator|new
name|EntityResult
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|DbEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|DbRelationship
name|findRelationByPath
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|Expression
name|exp
parameter_list|)
block|{
name|DbRelationship
name|r
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|component
range|:
name|entity
operator|.
name|resolvePath
argument_list|(
name|exp
argument_list|,
name|getPathSplitAliases
argument_list|()
argument_list|)
control|)
block|{
name|r
operator|=
name|component
operator|.
name|getRelationship
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** 	 * Collect metadata for column that will be unwrapped to full entity in the final SQL 	 * (possibly including joint prefetch). 	 * This information will be used to correctly create Persistent object back from raw result. 	 * 	 * @param query original query 	 * @param column full object column 	 * @param resolver entity resolver to get ObjEntity and ClassDescriptor 	 * @return Entity result 	 */
specifier|private
name|EntityResult
name|buildEntityResultForColumn
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|,
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|column
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
comment|// This method is actually repeating logic of DescriptorColumnExtractor.
comment|// Here we don't care about intermediate joins and few other things so it's shorter.
comment|// Logic of these methods should be unified and simplified, possibly to a single source of metadata,
comment|// generated only once and used everywhere.
specifier|final
name|EntityResult
name|result
init|=
operator|new
name|EntityResult
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
comment|// Collecting visitor for ObjAttributes and toOne relationships
name|PropertyVisitor
name|visitor
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|ObjAttribute
name|oa
init|=
name|property
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|oa
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CayenneMapEntry
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|pathPart
operator|.
name|getName
argument_list|()
argument_list|,
name|pathPart
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|DbRelationship
name|dbRel
init|=
name|property
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|dbRel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
if|if
condition|(
operator|!
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|ObjEntity
name|oe
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|DbEntity
name|table
init|=
name|oe
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
comment|// Additionally collect PKs
for|for
control|(
name|DbAttribute
name|dba
range|:
name|table
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|dba
operator|.
name|getName
argument_list|()
argument_list|,
name|dba
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|oe
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
comment|// Collection columns for joint prefetch
if|if
condition|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|prefetch
range|:
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|adjacentJointNodes
argument_list|()
control|)
block|{
comment|// for each prefetch add columns from the target entity
name|Expression
name|prefetchExp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|prefetch
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|ASTDbPath
name|dbPrefetch
init|=
operator|(
name|ASTDbPath
operator|)
name|oe
operator|.
name|translateToDbPath
argument_list|(
name|prefetchExp
argument_list|)
decl_stmt|;
name|DbRelationship
name|r
init|=
name|findRelationByPath
argument_list|(
name|table
argument_list|,
name|dbPrefetch
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid joint prefetch '%s' for entity: %s"
argument_list|,
name|prefetch
argument_list|,
name|oe
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// go via target OE to make sure that Java types are mapped correctly...
name|ObjRelationship
name|targetRel
init|=
operator|(
name|ObjRelationship
operator|)
name|prefetchExp
operator|.
name|evaluate
argument_list|(
name|oe
argument_list|)
decl_stmt|;
name|ObjEntity
name|targetEntity
init|=
name|targetRel
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|prefetch
operator|.
name|setEntityName
argument_list|(
name|targetRel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|labelPrefix
init|=
name|dbPrefetch
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|seenNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|oa
range|:
name|targetEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|oa
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|pathPart
decl_stmt|;
if|if
condition|(
name|seenNames
operator|.
name|add
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// append remaining target attributes such as keys
name|DbEntity
name|targetDbEntity
init|=
name|r
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|targetDbEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|seenNames
operator|.
name|add
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|addDbField
argument_list|(
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|boolean
name|isSingleResultSetMapping
parameter_list|()
block|{
return|return
name|isSingleResultSetMapping
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|boolean
name|isSuppressingDistinct
parameter_list|()
block|{
return|return
name|suppressingDistinct
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|void
name|setSuppressingDistinct
parameter_list|(
name|boolean
name|suppressingDistinct
parameter_list|)
block|{
name|this
operator|.
name|suppressingDistinct
operator|=
name|suppressingDistinct
expr_stmt|;
block|}
block|}
end_class

end_unit

