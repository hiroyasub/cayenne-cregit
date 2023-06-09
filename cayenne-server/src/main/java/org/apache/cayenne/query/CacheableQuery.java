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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Shared functionality for cacheable queries.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CacheableQuery
implements|implements
name|Query
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CacheableQuery
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|abstract
specifier|protected
name|BaseQueryMetadata
name|getBaseMetaData
parameter_list|()
function_decl|;
comment|/**      * @since 3.0      */
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|getBaseMetaData
argument_list|()
operator|.
name|getCacheStrategy
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setCacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|strategy
parameter_list|)
block|{
name|getBaseMetaData
argument_list|()
operator|.
name|setCacheStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|String
name|getCacheGroup
parameter_list|()
block|{
return|return
name|getBaseMetaData
argument_list|()
operator|.
name|getCacheGroup
argument_list|()
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setCacheGroup
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
name|getBaseMetaData
argument_list|()
operator|.
name|setCacheGroup
argument_list|(
name|cacheGroup
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "local" cache when      * running the query. This is a short-hand notation for:      *      *<pre>      * query.setCacheStrategy(QueryCacheStrategy.LOCAL_CACHE);      *</pre>      *      * @since 4.0      */
specifier|public
name|void
name|useLocalCache
parameter_list|()
block|{
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "local" cache when      * running the query. This is a short-hand notation for:      *      *<pre>      * query.setCacheStrategy(QueryCacheStrategy.LOCAL_CACHE);      * query.setCacheGroups(&quot;group1&quot;,&quot;group2&quot;);      *</pre>      *      * @since 4.0      */
specifier|public
name|void
name|useLocalCache
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|setCacheGroup
argument_list|(
name|cacheGroup
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "shared" cache when      * running the query. This is a short-hand notation for:      *      *<pre>      * query.setCacheStrategy(QueryCacheStrategy.SHARED_CACHE);      *</pre>      *      * @since 4.0      */
specifier|public
name|void
name|useSharedCache
parameter_list|()
block|{
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "shared" cache when      * running the query. This is a short-hand notation for:      *      *<pre>      * query.setCacheStrategy(QueryCacheStrategy.SHARED_CACHE);      * query.setCacheGroups(&quot;group1&quot;,&quot;group2&quot;);      *</pre>      *      * @since 4.0      */
specifier|public
name|void
name|useSharedCache
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|setCacheGroup
argument_list|(
name|cacheGroup
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

