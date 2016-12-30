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
name|cache
package|;
end_package

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
name|query
operator|.
name|PrefetchTreeNode
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
name|QueryCacheStrategy
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
name|QueryMetadata
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

begin_comment
comment|/**  * A {@link QueryCache} wrapper that introduces a key namespace on top of a  * delegate shared cache. This way multiple cache users can share the same  * underlying cache without a possibility of key conflicts, yet refresh the  * cache groups in a coordinated fashion.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|NestedQueryCache
implements|implements
name|QueryCache
block|{
comment|// the idea is to be something short (to speed up comparisons), but clear
comment|// and unlikely to create a conflict with application cache keys...
comment|// fully-qualified class name that we used before was a bit too long
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE_PREXIX
init|=
literal|"#nested-"
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|int
name|currentId
decl_stmt|;
specifier|protected
name|QueryCache
name|delegate
decl_stmt|;
specifier|protected
name|String
name|namespace
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|nextInt
parameter_list|()
block|{
if|if
condition|(
name|currentId
operator|==
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
name|currentId
operator|=
literal|0
expr_stmt|;
block|}
return|return
name|currentId
operator|++
return|;
block|}
specifier|public
name|NestedQueryCache
parameter_list|(
name|QueryCache
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|NAMESPACE_PREXIX
operator|+
name|nextInt
argument_list|()
operator|+
literal|":"
expr_stmt|;
block|}
comment|/**      * Returns the actual implementation of the query cache that is wrapped by      * this NestedQueryCache.      */
specifier|public
name|QueryCache
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|/**      * Clears the underlying shared cache.      */
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|// seems pretty evil - it clears the keys that do not belong to our
comment|// subset of the
comment|// cache
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|List
name|get
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|,
name|QueryCacheEntryFactory
name|factory
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|get
argument_list|(
name|qualifiedMetadata
argument_list|(
name|metadata
argument_list|)
argument_list|,
name|factory
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|List
name|get
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|get
argument_list|(
name|qualifiedMetadata
argument_list|(
name|metadata
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|void
name|put
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|,
name|List
name|results
parameter_list|)
block|{
name|delegate
operator|.
name|put
argument_list|(
name|qualifiedMetadata
argument_list|(
name|metadata
argument_list|)
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes an entry for key in the current namespace.      */
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|delegate
operator|.
name|remove
argument_list|(
name|qualifiedKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invalidates a shared cache group.      */
annotation|@
name|Override
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
name|delegate
operator|.
name|removeGroup
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a shared cache size.      */
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
specifier|private
name|String
name|qualifiedKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|key
operator|!=
literal|null
condition|?
name|namespace
operator|+
name|key
else|:
literal|null
return|;
block|}
specifier|private
name|QueryMetadata
name|qualifiedMetadata
parameter_list|(
name|QueryMetadata
name|md
parameter_list|)
block|{
return|return
operator|new
name|QualifiedKeyQueryMetadata
argument_list|(
name|md
argument_list|)
return|;
block|}
specifier|final
class|class
name|QualifiedKeyQueryMetadata
implements|implements
name|QueryMetadata
block|{
specifier|private
name|QueryMetadata
name|mdDelegate
decl_stmt|;
name|QualifiedKeyQueryMetadata
parameter_list|(
name|QueryMetadata
name|mdDelegate
parameter_list|)
block|{
name|this
operator|.
name|mdDelegate
operator|=
name|mdDelegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
name|qualifiedKey
argument_list|(
name|mdDelegate
operator|.
name|getCacheKey
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getCacheStrategy
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getClassDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getFetchOffset
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getObjEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getOrginatingQuery
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getPageSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getPrefetchTree
argument_list|()
return|;
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
name|mdDelegate
operator|.
name|getPathSplitAliases
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getProcedure
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResultSetMapping
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getResultSetMapping
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getStatementFetchSize
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

