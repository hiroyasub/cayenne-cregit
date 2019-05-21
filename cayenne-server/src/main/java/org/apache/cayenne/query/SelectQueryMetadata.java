begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|DefaultEntityResultSegment
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
name|DefaultScalarResultSegment
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
specifier|static
specifier|final
name|ScalarResultSegment
name|SCALAR_RESULT_SEGMENT
init|=
operator|new
name|DefaultScalarResultSegment
argument_list|(
literal|null
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|EntityResultSegment
name|ENTITY_RESULT_SEGMENT
init|=
operator|new
name|DefaultEntityResultSegment
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|)
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
name|resultSetMapping
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|query
operator|.
name|getColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// for each column we need only to know if it's entity or scalar
name|Expression
name|exp
init|=
name|column
operator|.
name|getExpression
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
name|Object
name|rel
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|getObjEntity
argument_list|()
argument_list|)
decl_stmt|;
comment|// it this path is toOne relation, than select full object for it
name|fullObject
operator|=
name|rel
operator|instanceof
name|ObjRelationship
operator|&&
operator|!
operator|(
operator|(
name|ObjRelationship
operator|)
name|rel
operator|)
operator|.
name|isToMany
argument_list|()
expr_stmt|;
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
name|resultSetMapping
operator|.
name|add
argument_list|(
name|ENTITY_RESULT_SEGMENT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultSetMapping
operator|.
name|add
argument_list|(
name|SCALAR_RESULT_SEGMENT
argument_list|)
expr_stmt|;
block|}
block|}
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

