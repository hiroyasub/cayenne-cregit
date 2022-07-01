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
name|ObjectSelect
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

begin_comment
comment|/**  * A SelectQuery decorator that overrides the metadata to ensure that query  * result is cached on the server, so that subranges could be retrieved at a  * later time. Note that a special decorator that is a subclass of SelectQuery  * is needed so that {@link IncrementalFaultList} on the server-side could apply  * SelectQuery-specific optimizations.  *   * @since 3.0  * @since 4.3 this query extends ObjectSelect  */
end_comment

begin_class
class|class
name|IncrementalSelectQuery
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ObjectSelect
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|ObjectSelect
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
name|ObjectSelect
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
annotation|@
name|Override
specifier|public
name|Expression
name|getWhere
parameter_list|()
block|{
return|return
name|query
operator|.
name|getWhere
argument_list|()
return|;
block|}
comment|/** 	 * Returns a HAVING clause Expression of this query. 	 */
annotation|@
name|Override
specifier|public
name|Expression
name|getHaving
parameter_list|()
block|{
return|return
name|query
operator|.
name|getHaving
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
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
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|query
operator|.
name|getStatementFetchSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getQueryTimeout
parameter_list|()
block|{
return|return
name|query
operator|.
name|getQueryTimeout
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
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|query
operator|.
name|getLimit
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|query
operator|.
name|getOffset
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEntityType
parameter_list|()
block|{
return|return
name|query
operator|.
name|getEntityType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|query
operator|.
name|getEntityName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDbEntityName
parameter_list|()
block|{
return|return
name|query
operator|.
name|getDbEntityName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrefetchTreeNode
name|getPrefetches
parameter_list|()
block|{
return|return
name|query
operator|.
name|getPrefetches
argument_list|()
return|;
block|}
block|}
end_class

end_unit

