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
operator|.
name|invalidation
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
name|DataChannel
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
name|DataChannelFilter
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
name|DataChannelFilterChain
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
name|QueryResponse
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
name|annotation
operator|.
name|PrePersist
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
name|annotation
operator|.
name|PreRemove
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
name|annotation
operator|.
name|PreUpdate
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
name|cache
operator|.
name|QueryCache
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
name|di
operator|.
name|Inject
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
name|di
operator|.
name|Provider
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
name|graph
operator|.
name|GraphDiff
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  *<p>  * A {@link DataChannelFilter} that invalidates cache groups.  * Use custom rules for invalidation provided via DI.  *</p>  *<p>  * Default rule is based on entities' {@link CacheGroups} annotation.  *</p>  *<p>  * To add default filter:<pre>  *         ServerRuntime.builder("cayenne-project.xml")  *              .addModule(CacheInvalidationModuleBuilder.builder().build());  *</pre>  *</p>  *  * @see CacheInvalidationModuleExtender  * @see InvalidationHandler  * @since 4.0 enhanced to support custom handlers.  */
end_comment

begin_class
specifier|public
class|class
name|CacheInvalidationFilter
implements|implements
name|DataChannelFilter
block|{
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryCache
argument_list|>
name|cacheProvider
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|InvalidationHandler
argument_list|>
name|handlers
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Persistent
argument_list|>
argument_list|,
name|Function
argument_list|<
name|Persistent
argument_list|,
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
argument_list|>
name|mappedHandlers
decl_stmt|;
specifier|private
specifier|final
name|Function
argument_list|<
name|Persistent
argument_list|,
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
name|skipHandler
decl_stmt|;
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|Set
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
name|groups
decl_stmt|;
specifier|public
name|CacheInvalidationFilter
parameter_list|(
annotation|@
name|Inject
name|Provider
argument_list|<
name|QueryCache
argument_list|>
name|cacheProvider
parameter_list|,
annotation|@
name|Inject
name|List
argument_list|<
name|InvalidationHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|mappedHandlers
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|skipHandler
operator|=
name|p
lambda|->
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
name|this
operator|.
name|groups
operator|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheProvider
operator|=
name|cacheProvider
expr_stmt|;
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|,
name|DataChannelFilterChain
name|filterChain
parameter_list|)
block|{
return|return
name|filterChain
operator|.
name|onQuery
argument_list|(
name|originatingContext
argument_list|,
name|query
argument_list|)
return|;
block|}
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|,
name|DataChannelFilterChain
name|filterChain
parameter_list|)
block|{
try|try
block|{
name|GraphDiff
name|result
init|=
name|filterChain
operator|.
name|onSync
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
decl_stmt|;
comment|// no exceptions, flush...
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
name|groupSet
init|=
name|groups
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupSet
operator|!=
literal|null
operator|&&
operator|!
name|groupSet
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|QueryCache
name|cache
init|=
name|cacheProvider
operator|.
name|get
argument_list|()
decl_stmt|;
for|for
control|(
name|CacheGroupDescriptor
name|group
range|:
name|groupSet
control|)
block|{
if|if
condition|(
name|group
operator|.
name|getKeyType
argument_list|()
operator|!=
name|Void
operator|.
name|class
condition|)
block|{
name|cache
operator|.
name|removeGroup
argument_list|(
name|group
operator|.
name|getCacheGroupName
argument_list|()
argument_list|,
name|group
operator|.
name|getKeyType
argument_list|()
argument_list|,
name|group
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|.
name|removeGroup
argument_list|(
name|group
operator|.
name|getCacheGroupName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
finally|finally
block|{
name|groups
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A callback method that records cache group to flush at the end of the commit.      */
annotation|@
name|PrePersist
annotation|@
name|PreRemove
annotation|@
name|PreUpdate
specifier|protected
name|void
name|preCommit
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
comment|// TODO: for some reason we can't use Persistent as the argument type... (is it fixed in Cayenne 4.0.M4?)
name|Persistent
name|p
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
name|Function
argument_list|<
name|Persistent
argument_list|,
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
name|invalidationFunction
init|=
name|mappedHandlers
operator|.
name|get
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalidationFunction
operator|==
literal|null
condition|)
block|{
name|invalidationFunction
operator|=
name|skipHandler
expr_stmt|;
for|for
control|(
name|InvalidationHandler
name|handler
range|:
name|handlers
control|)
block|{
name|Function
argument_list|<
name|Persistent
argument_list|,
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
name|function
init|=
name|handler
operator|.
name|canHandle
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|!=
literal|null
condition|)
block|{
name|invalidationFunction
operator|=
name|function
expr_stmt|;
break|break;
block|}
block|}
name|mappedHandlers
operator|.
name|put
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
argument_list|,
name|invalidationFunction
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
name|objectGroups
init|=
name|invalidationFunction
operator|.
name|apply
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|objectGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getOrCreateTxGroups
argument_list|()
operator|.
name|addAll
argument_list|(
name|objectGroups
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Set
argument_list|<
name|CacheGroupDescriptor
argument_list|>
name|getOrCreateTxGroups
parameter_list|()
block|{
name|Set
argument_list|<
name|CacheGroupDescriptor
argument_list|>
name|txGroups
init|=
name|groups
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|txGroups
operator|==
literal|null
condition|)
block|{
name|txGroups
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|groups
operator|.
name|set
argument_list|(
name|txGroups
argument_list|)
expr_stmt|;
block|}
return|return
name|txGroups
return|;
block|}
block|}
end_class

end_unit

