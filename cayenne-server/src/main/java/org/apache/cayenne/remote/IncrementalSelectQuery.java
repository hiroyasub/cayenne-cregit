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
name|remote
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
name|ObjectContext
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
name|ResultBatchIterator
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
name|ResultIterator
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
name|ResultIteratorCallback
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
name|IncrementalFaultList
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
name|Ordering
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
name|query
operator|.
name|QueryMetadataProxy
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
name|QueryRouter
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
name|SQLAction
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
name|SQLActionVisitor
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
name|SelectQuery
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
name|SortOrder
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

begin_comment
comment|/**  * A SelectQuery decorator that overrides the metadata to ensure that query  * result is cached on the server, so that subranges could be retrieved at a  * later time. Note that a special decorator that is a subclass of SelectQuery  * is needed so that {@link IncrementalFaultList} on the server-side could apply  * SelectQuery-specific optimizations.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|IncrementalSelectQuery
parameter_list|<
name|T
parameter_list|>
extends|extends
name|SelectQuery
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
decl_stmt|;
specifier|private
name|String
name|cacheKey
decl_stmt|;
name|IncrementalSelectQuery
parameter_list|(
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|cacheKey
operator|=
name|cacheKey
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
specifier|final
name|QueryMetadata
name|metadata
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
comment|// the way paginated queries work on the server is that they are never
comment|// cached
comment|// (IncrementalFaultList interception happens before cache
comment|// interception). So
comment|// overriding caching settings in the metadata will only affect
comment|// ClientServerChannel behavior
return|return
operator|new
name|QueryMetadataProxy
argument_list|(
name|metadata
argument_list|)
block|{
specifier|public
name|Query
name|getOriginatingQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
name|cacheKey
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|query
operator|.
name|addOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrdering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|SortOrder
name|order
parameter_list|)
block|{
name|query
operator|.
name|addOrdering
argument_list|(
name|sortPathSpec
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrderings
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
name|query
operator|.
name|addOrderings
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PrefetchTreeNode
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
return|return
name|query
operator|.
name|addPrefetch
argument_list|(
name|prefetchPath
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|andQualifier
parameter_list|(
name|Expression
name|e
parameter_list|)
block|{
name|query
operator|.
name|andQualifier
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearOrderings
parameter_list|()
block|{
name|query
operator|.
name|clearOrderings
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearPrefetches
parameter_list|()
block|{
name|query
operator|.
name|clearPrefetches
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|createQuery
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|query
operator|.
name|createQuery
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|query
operator|.
name|createSQLAction
argument_list|(
name|visitor
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|query
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
comment|/** 	 * @deprecated since 4.0, use {@link IncrementalSelectQuery#getCacheGroup()} 	 */
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|query
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|String
name|getCacheGroup
parameter_list|()
block|{
return|return
name|super
operator|.
name|getCacheGroup
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
name|query
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|query
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
block|{
return|return
name|query
operator|.
name|getOrderings
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
name|query
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
name|query
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|getQualifier
parameter_list|()
block|{
return|return
name|query
operator|.
name|getQualifier
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|query
operator|.
name|getRoot
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|query
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
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
name|query
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDistinct
parameter_list|()
block|{
return|return
name|query
operator|.
name|isDistinct
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
name|query
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|orQualifier
parameter_list|(
name|Expression
name|e
parameter_list|)
block|{
name|query
operator|.
name|orQualifier
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|queryWithParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|pruneMissing
parameter_list|)
block|{
return|return
name|query
operator|.
name|queryWithParameters
argument_list|(
name|parameters
argument_list|,
name|pruneMissing
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|queryWithParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|query
operator|.
name|queryWithParameters
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|query
operator|.
name|removeOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
name|query
operator|.
name|removePrefetch
argument_list|(
name|prefetchPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|query
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @deprecated since 4.0 only first cache group will be used 	 * 			   use {@link IncrementalSelectQuery#setCacheGroup(String)} 	 */
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|void
name|setCacheGroups
parameter_list|(
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
name|query
operator|.
name|setCacheGroups
argument_list|(
name|cacheGroups
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|void
name|setCacheGroup
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
name|query
operator|.
name|setCacheGroup
argument_list|(
name|cacheGroup
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDistinct
parameter_list|(
name|boolean
name|distinct
parameter_list|)
block|{
name|query
operator|.
name|setDistinct
argument_list|(
name|distinct
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFetchingDataRows
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|query
operator|.
name|setFetchingDataRows
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFetchLimit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
name|query
operator|.
name|setFetchLimit
argument_list|(
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|query
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|query
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPrefetchTree
parameter_list|(
name|PrefetchTreeNode
name|prefetchTree
parameter_list|)
block|{
name|query
operator|.
name|setPrefetchTree
argument_list|(
name|prefetchTree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setQualifier
parameter_list|(
name|Expression
name|qualifier
parameter_list|)
block|{
name|query
operator|.
name|setQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRoot
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|query
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|selectOne
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|query
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|selectFirst
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|query
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|iterate
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ResultIteratorCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
name|query
operator|.
name|iterate
argument_list|(
name|context
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|query
operator|.
name|iterator
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResultBatchIterator
argument_list|<
name|T
argument_list|>
name|batchIterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|query
operator|.
name|batchIterator
argument_list|(
name|context
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
end_class

end_unit

