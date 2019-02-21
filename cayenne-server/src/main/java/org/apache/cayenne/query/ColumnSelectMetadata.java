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
name|ObjRelationship
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|ColumnSelectMetadata
extends|extends
name|ObjectSelectMetadata
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3622675304651257963L
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
name|boolean
name|isSingleResultSetMapping
decl_stmt|;
specifier|private
name|boolean
name|suppressingDistinct
decl_stmt|;
name|boolean
name|resolve
parameter_list|(
name|Object
name|root
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|ColumnSelect
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
argument_list|)
expr_stmt|;
name|isSingleResultSetMapping
operator|=
name|query
operator|.
name|isSingleColumn
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
annotation|@
name|Override
specifier|protected
name|void
name|resolveAutoAliases
parameter_list|(
name|FluentSelect
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
name|super
operator|.
name|resolveAutoAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|resolveColumnsAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|resolveColumnsAliases
parameter_list|(
name|FluentSelect
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
comment|/** 	 * NOTE: this is a dirty logic, we calculate hollow resultSetMapping here and later in translator 	 * (see ColumnExtractorStage and extractors) discard this and calculate it with full info. 	 * 	 * This result set mapping required by paginated queries that need only result type (entity/scalar) not 	 * full info. So we can optimize this a bit and pair calculation with translation that do same thing to provide 	 * result column descriptors. 	 */
specifier|private
name|void
name|buildResultSetMappingForColumns
parameter_list|(
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|query
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

