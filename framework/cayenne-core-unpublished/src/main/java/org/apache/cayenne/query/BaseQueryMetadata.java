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
name|io
operator|.
name|Serializable
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
name|StringTokenizer
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
name|map
operator|.
name|DataMap
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
name|Procedure
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
name|util
operator|.
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * Default mutable implementation of {@link QueryMetadata}.  *   * @since 1.1  */
end_comment

begin_class
class|class
name|BaseQueryMetadata
implements|implements
name|QueryMetadata
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
name|int
name|fetchLimit
init|=
name|QueryMetadata
operator|.
name|FETCH_LIMIT_DEFAULT
decl_stmt|;
name|int
name|fetchOffset
init|=
name|QueryMetadata
operator|.
name|FETCH_OFFSET_DEFAULT
decl_stmt|;
name|int
name|statementFetchSize
init|=
name|QueryMetadata
operator|.
name|FETCH_OFFSET_DEFAULT
decl_stmt|;
name|int
name|pageSize
init|=
name|QueryMetadata
operator|.
name|PAGE_SIZE_DEFAULT
decl_stmt|;
name|boolean
name|fetchingDataRows
init|=
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_DEFAULT
decl_stmt|;
name|QueryCacheStrategy
name|cacheStrategy
init|=
name|QueryCacheStrategy
operator|.
name|getDefaultStrategy
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|prefetchTree
decl_stmt|;
name|String
name|cacheKey
decl_stmt|;
name|String
index|[]
name|cacheGroups
decl_stmt|;
specifier|transient
name|List
argument_list|<
name|Object
argument_list|>
name|resultSetMapping
decl_stmt|;
specifier|transient
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|transient
name|DataMap
name|dataMap
decl_stmt|;
specifier|transient
name|Object
name|lastRoot
decl_stmt|;
specifier|transient
name|ClassDescriptor
name|classDescriptor
decl_stmt|;
specifier|transient
name|EntityResolver
name|lastEntityResolver
decl_stmt|;
comment|/**      * Copies values of another QueryMetadata object to this object.      */
name|void
name|copyFromInfo
parameter_list|(
name|QueryMetadata
name|info
parameter_list|)
block|{
name|this
operator|.
name|lastEntityResolver
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|lastRoot
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|classDescriptor
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|fetchingDataRows
operator|=
name|info
operator|.
name|isFetchingDataRows
argument_list|()
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
name|info
operator|.
name|getFetchLimit
argument_list|()
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|info
operator|.
name|getPageSize
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheStrategy
operator|=
name|info
operator|.
name|getCacheStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheKey
operator|=
name|info
operator|.
name|getCacheKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheGroups
operator|=
name|info
operator|.
name|getCacheGroups
argument_list|()
expr_stmt|;
name|this
operator|.
name|resultSetMapping
operator|=
name|info
operator|.
name|getResultSetMapping
argument_list|()
expr_stmt|;
name|setPrefetchTree
argument_list|(
name|info
operator|.
name|getPrefetchTree
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
name|String
name|cacheKey
parameter_list|)
block|{
if|if
condition|(
name|lastRoot
operator|!=
name|root
operator|||
name|lastEntityResolver
operator|!=
name|resolver
condition|)
block|{
name|this
operator|.
name|cacheKey
operator|=
name|cacheKey
expr_stmt|;
name|this
operator|.
name|classDescriptor
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
literal|null
expr_stmt|;
name|ObjEntity
name|entity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|root
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|entity
operator|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|root
argument_list|)
expr_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
comment|// entity not found, try to resolve it
comment|// with
comment|// client resolver
name|EntityResolver
name|clientResolver
init|=
name|resolver
operator|.
name|getClientEntityResolver
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientResolver
operator|!=
name|resolver
condition|)
block|{
name|ObjEntity
name|clientEntity
init|=
name|clientResolver
operator|.
name|getObjEntity
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientEntity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|clientEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|entity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|root
operator|instanceof
name|ObjEntity
condition|)
block|{
name|entity
operator|=
operator|(
name|ObjEntity
operator|)
name|root
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|entity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
name|entity
operator|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
operator|(
name|String
operator|)
name|root
argument_list|)
expr_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|entity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|root
operator|instanceof
name|DbEntity
condition|)
block|{
name|this
operator|.
name|dbEntity
operator|=
operator|(
name|DbEntity
operator|)
name|root
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dbEntity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|DataMap
condition|)
block|{
name|this
operator|.
name|dataMap
operator|=
operator|(
name|DataMap
operator|)
name|root
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Persistent
condition|)
block|{
name|entity
operator|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
operator|(
name|Persistent
operator|)
name|root
argument_list|)
expr_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|entity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|classDescriptor
operator|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|lastRoot
operator|=
name|root
expr_stmt|;
name|this
operator|.
name|lastEntityResolver
operator|=
name|resolver
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
name|void
name|initWithProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
comment|// must init defaults even if properties are empty
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
name|Object
name|fetchOffset
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_OFFSET_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|fetchLimit
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_LIMIT_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|pageSize
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|PAGE_SIZE_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|statementFetchSize
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|fetchingDataRows
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|cacheStrategy
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|cacheGroups
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_GROUPS_PROPERTY
argument_list|)
decl_stmt|;
comment|// init ivars from properties
name|this
operator|.
name|fetchOffset
operator|=
operator|(
name|fetchOffset
operator|!=
literal|null
operator|)
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|fetchOffset
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|FETCH_OFFSET_DEFAULT
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
operator|(
name|fetchLimit
operator|!=
literal|null
operator|)
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|fetchLimit
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|FETCH_LIMIT_DEFAULT
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
operator|(
name|pageSize
operator|!=
literal|null
operator|)
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|pageSize
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|PAGE_SIZE_DEFAULT
expr_stmt|;
name|this
operator|.
name|statementFetchSize
operator|=
operator|(
name|statementFetchSize
operator|!=
literal|null
operator|)
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|statementFetchSize
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_DEFAULT
expr_stmt|;
name|this
operator|.
name|fetchingDataRows
operator|=
operator|(
name|fetchingDataRows
operator|!=
literal|null
operator|)
condition|?
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|fetchingDataRows
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_DEFAULT
expr_stmt|;
name|this
operator|.
name|cacheStrategy
operator|=
operator|(
name|cacheStrategy
operator|!=
literal|null
operator|)
condition|?
name|QueryCacheStrategy
operator|.
name|safeValueOf
argument_list|(
name|cacheStrategy
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryCacheStrategy
operator|.
name|getDefaultStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheGroups
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|cacheGroups
operator|instanceof
name|String
index|[]
condition|)
block|{
name|this
operator|.
name|cacheGroups
operator|=
operator|(
name|String
index|[]
operator|)
name|cacheGroups
expr_stmt|;
block|}
if|else if
condition|(
name|cacheGroups
operator|instanceof
name|String
condition|)
block|{
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|cacheGroups
operator|.
name|toString
argument_list|()
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|this
operator|.
name|cacheGroups
operator|=
operator|new
name|String
index|[
name|toks
operator|.
name|countTokens
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|this
operator|.
name|cacheGroups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|cacheGroups
index|[
name|i
index|]
operator|=
name|toks
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
if|if
condition|(
name|fetchingDataRows
operator|!=
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_PROPERTY
argument_list|,
name|fetchingDataRows
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fetchOffset
operator|!=
name|QueryMetadata
operator|.
name|FETCH_OFFSET_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_OFFSET_PROPERTY
argument_list|,
name|fetchOffset
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fetchLimit
operator|!=
name|QueryMetadata
operator|.
name|FETCH_LIMIT_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_LIMIT_PROPERTY
argument_list|,
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pageSize
operator|!=
name|QueryMetadata
operator|.
name|PAGE_SIZE_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|PAGE_SIZE_PROPERTY
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheStrategy
operator|!=
literal|null
operator|&&
name|QueryCacheStrategy
operator|.
name|getDefaultStrategy
argument_list|()
operator|!=
name|cacheStrategy
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|,
name|cacheStrategy
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statementFetchSize
operator|!=
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_PROPERTY
argument_list|,
name|statementFetchSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prefetchTree
operator|!=
literal|null
condition|)
block|{
name|prefetchTree
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheGroups
operator|!=
literal|null
operator|&&
name|cacheGroups
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|cacheGroups
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|cacheGroups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
name|cacheGroups
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_GROUPS_PROPERTY
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
name|cacheKey
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
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
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|classDescriptor
operator|!=
literal|null
condition|?
name|classDescriptor
operator|.
name|getEntity
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|classDescriptor
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResultSetMapping
parameter_list|()
block|{
return|return
name|resultSetMapping
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|prefetchTree
return|;
block|}
name|void
name|setPrefetchTree
parameter_list|(
name|PrefetchTreeNode
name|prefetchTree
parameter_list|)
block|{
name|this
operator|.
name|prefetchTree
operator|=
name|prefetchTree
operator|!=
literal|null
condition|?
name|deepClone
argument_list|(
name|prefetchTree
argument_list|,
literal|null
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
specifier|private
name|PrefetchTreeNode
name|deepClone
parameter_list|(
name|PrefetchTreeNode
name|source
parameter_list|,
name|PrefetchTreeNode
name|targetParent
parameter_list|)
block|{
name|PrefetchTreeNode
name|target
init|=
operator|new
name|PrefetchTreeNode
argument_list|(
name|targetParent
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|target
operator|.
name|setEjbqlPathEntityId
argument_list|(
name|source
operator|.
name|getEjbqlPathEntityId
argument_list|()
argument_list|)
expr_stmt|;
name|target
operator|.
name|setEntityName
argument_list|(
name|source
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|target
operator|.
name|setPhantom
argument_list|(
name|source
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|target
operator|.
name|setSemantics
argument_list|(
name|source
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PrefetchTreeNode
name|child
range|:
name|source
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|target
operator|.
name|addChild
argument_list|(
name|deepClone
argument_list|(
name|child
argument_list|,
name|target
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|target
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|cacheStrategy
return|;
block|}
comment|/**      * @since 3.0      */
name|void
name|setCacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|cacheStrategy
parameter_list|)
block|{
name|this
operator|.
name|cacheStrategy
operator|=
name|cacheStrategy
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|cacheGroups
return|;
block|}
comment|/**      * @since 3.0      */
name|void
name|setCacheGroups
parameter_list|(
name|String
modifier|...
name|groups
parameter_list|)
block|{
name|this
operator|.
name|cacheGroups
operator|=
name|groups
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|fetchingDataRows
return|;
block|}
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|fetchLimit
return|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
specifier|public
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|fetchOffset
return|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
name|void
name|setFetchingDataRows
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|fetchingDataRows
operator|=
name|b
expr_stmt|;
block|}
name|void
name|setFetchLimit
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|fetchLimit
operator|=
name|i
expr_stmt|;
block|}
name|void
name|setFetchOffset
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|fetchOffset
operator|=
name|i
expr_stmt|;
block|}
name|void
name|setPageSize
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|pageSize
operator|=
name|i
expr_stmt|;
block|}
comment|/**      * Sets statement's fetch size (0 for no default size)      *       * @since 3.0      */
name|void
name|setStatementFetchSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|statementFetchSize
operator|=
name|size
expr_stmt|;
block|}
comment|/**      * @return statement's fetch size      * @since 3.0      */
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|statementFetchSize
return|;
block|}
comment|/**      * Adds a joint prefetch.      *       * @since 1.2      */
name|PrefetchTreeNode
name|addPrefetch
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
if|if
condition|(
name|prefetchTree
operator|==
literal|null
condition|)
block|{
name|prefetchTree
operator|=
operator|new
name|PrefetchTreeNode
argument_list|()
expr_stmt|;
block|}
name|PrefetchTreeNode
name|node
init|=
name|prefetchTree
operator|.
name|addPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|semantics
argument_list|)
expr_stmt|;
name|node
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * Adds all prefetches from a provided collection.      *       * @since 1.2      */
name|void
name|addPrefetches
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|prefetches
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
if|if
condition|(
name|prefetches
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|prefetch
range|:
name|prefetches
control|)
block|{
name|addPrefetch
argument_list|(
name|prefetch
argument_list|,
name|semantics
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Clears all joint prefetches.      *       * @since 1.2      */
name|void
name|clearPrefetches
parameter_list|()
block|{
name|prefetchTree
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Removes joint prefetch.      *       * @since 1.2      */
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetch
parameter_list|)
block|{
if|if
condition|(
name|prefetchTree
operator|!=
literal|null
condition|)
block|{
name|prefetchTree
operator|.
name|removePath
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
