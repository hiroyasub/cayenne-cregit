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
name|Arrays
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|EntityResolver
import|;
end_import

begin_comment
comment|/**  * A query that allows to explicitly clear both object and list caches either via refetch  * (eager refresh) or invalidate (lazy refresh).  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|RefreshQuery
implements|implements
name|Query
block|{
specifier|protected
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
decl_stmt|;
specifier|protected
name|Query
name|query
decl_stmt|;
specifier|protected
name|String
index|[]
name|groupKeys
decl_stmt|;
comment|/**      * Creates a RefreshQuery that does full refresh of all registered objects, cascading      * refresh all the way to the shared cache.      */
specifier|public
name|RefreshQuery
parameter_list|()
block|{
block|}
comment|/**      * Creates a RefreshQuery that refreshes a collection of objects, including      * invalidation of their relationships.      */
specifier|public
name|RefreshQuery
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
name|this
operator|.
name|objects
operator|=
name|objects
expr_stmt|;
block|}
comment|/**      * Creates a RefreshQuery that refreshes a single object, including invalidation of      * its relationships.      */
specifier|public
name|RefreshQuery
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a RefreshQuery that refreshes results of a query and individual objects in      * the result.      */
specifier|public
name|RefreshQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
comment|/**      * Creates a RefreshQuery that refreshes query results identified by group keys.      */
specifier|public
name|RefreshQuery
parameter_list|(
name|String
modifier|...
name|groupKeys
parameter_list|)
block|{
name|this
operator|.
name|groupKeys
operator|=
name|groupKeys
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
return|return
operator|new
name|BaseQueryMetadata
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
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
comment|// noop
block|}
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isRefreshAll
parameter_list|()
block|{
return|return
name|objects
operator|==
literal|null
operator|&&
name|query
operator|==
literal|null
operator|&&
name|groupKeys
operator|==
literal|null
return|;
block|}
specifier|public
name|String
index|[]
name|getGroupKeys
parameter_list|()
block|{
return|return
name|groupKeys
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|?
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
name|objects
return|;
block|}
comment|/**      * Returns an internal query, overriding cache policy to force a refresh. Returns null      * if no query was set.      */
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Query
argument_list|()
block|{
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported"
argument_list|)
throw|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|QueryMetadata
name|md
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|QueryMetadataWrapper
name|wrappedMd
init|=
operator|new
name|QueryMetadataWrapper
argument_list|(
name|md
argument_list|)
decl_stmt|;
if|if
condition|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
operator|==
name|md
operator|.
name|getCacheStrategy
argument_list|()
condition|)
block|{
name|wrappedMd
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|,
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE_REFRESH
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
operator|==
name|md
operator|.
name|getCacheStrategy
argument_list|()
condition|)
block|{
name|wrappedMd
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|,
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE_REFRESH
argument_list|)
expr_stmt|;
block|}
return|return
name|wrappedMd
return|;
block|}
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
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|query
operator|.
name|getDataMap
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
