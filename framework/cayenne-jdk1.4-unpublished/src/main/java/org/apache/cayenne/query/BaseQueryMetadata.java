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
name|Iterator
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
name|Util
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
comment|/**  * Default mutable implementation of {@link QueryMetadata}.  *   * @author Andrus Adamchik  * @since 1.1  */
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
name|boolean
name|refreshingObjects
init|=
name|QueryMetadata
operator|.
name|REFRESHING_OBJECTS_DEFAULT
decl_stmt|;
name|boolean
name|resolvingInherited
init|=
name|QueryMetadata
operator|.
name|RESOLVING_INHERITED_DEFAULT
decl_stmt|;
name|String
name|cachePolicy
init|=
name|QueryMetadata
operator|.
name|CACHE_POLICY_DEFAULT
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
name|refreshingObjects
operator|=
name|info
operator|.
name|isRefreshingObjects
argument_list|()
expr_stmt|;
name|this
operator|.
name|resolvingInherited
operator|=
name|info
operator|.
name|isResolvingInherited
argument_list|()
expr_stmt|;
name|this
operator|.
name|cachePolicy
operator|=
name|info
operator|.
name|getCachePolicy
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
condition|)
block|{
name|entity
operator|=
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
operator|(
name|Class
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
name|lookupObjEntity
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
name|refreshingObjects
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|REFRESHING_OBJECTS_PROPERTY
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
name|resolvingInherited
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|RESOLVING_INHERITED_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|cachePolicy
init|=
name|properties
operator|.
name|get
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_POLICY_PROPERTY
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
name|refreshingObjects
operator|=
operator|(
name|refreshingObjects
operator|!=
literal|null
operator|)
condition|?
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|refreshingObjects
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|REFRESHING_OBJECTS_DEFAULT
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
name|resolvingInherited
operator|=
operator|(
name|resolvingInherited
operator|!=
literal|null
operator|)
condition|?
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|resolvingInherited
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|QueryMetadata
operator|.
name|RESOLVING_INHERITED_DEFAULT
expr_stmt|;
name|this
operator|.
name|cachePolicy
operator|=
operator|(
name|cachePolicy
operator|!=
literal|null
operator|)
condition|?
name|cachePolicy
operator|.
name|toString
argument_list|()
else|:
name|QueryMetadata
operator|.
name|CACHE_POLICY_DEFAULT
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
name|refreshingObjects
operator|!=
name|QueryMetadata
operator|.
name|REFRESHING_OBJECTS_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|REFRESHING_OBJECTS_PROPERTY
argument_list|,
name|refreshingObjects
argument_list|)
expr_stmt|;
block|}
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
name|resolvingInherited
operator|!=
name|QueryMetadata
operator|.
name|RESOLVING_INHERITED_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|RESOLVING_INHERITED_PROPERTY
argument_list|,
name|resolvingInherited
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
name|cachePolicy
operator|!=
literal|null
operator|&&
operator|!
name|QueryMetadata
operator|.
name|CACHE_POLICY_DEFAULT
operator|.
name|equals
argument_list|(
name|cachePolicy
argument_list|)
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_POLICY_PROPERTY
argument_list|,
name|cachePolicy
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
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
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
name|cachePolicy
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
if|if
condition|(
name|prefetchTree
operator|!=
literal|null
condition|)
block|{
comment|// importnat: make a clone to allow modification independent from the
comment|// caller...
try|try
block|{
name|prefetchTree
operator|=
operator|(
name|PrefetchTreeNode
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|prefetchTree
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error cloning prefetch tree"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|this
operator|.
name|prefetchTree
operator|=
name|prefetchTree
expr_stmt|;
block|}
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|cachePolicy
return|;
block|}
name|void
name|setCachePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|this
operator|.
name|cachePolicy
operator|=
name|policy
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
index|[]
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
comment|/**      * Always returns -1.      */
specifier|public
name|int
name|getFetchStartIndex
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|refreshingObjects
return|;
block|}
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
name|resolvingInherited
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
name|void
name|setRefreshingObjects
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|refreshingObjects
operator|=
name|b
expr_stmt|;
block|}
name|void
name|setResolvingInherited
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|resolvingInherited
operator|=
name|b
expr_stmt|;
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
operator|&&
operator|!
name|prefetches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|prefetches
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|prefetch
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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

