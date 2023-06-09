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
name|cache
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|util
operator|.
name|concurrentlinkedhashmap
operator|.
name|ConcurrentLinkedHashMap
import|;
end_import

begin_comment
comment|/**  * A default implementation of the {@link QueryCache} interface that stores data in a  * non-expiring LRUMap.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|MapQueryCache
implements|implements
name|QueryCache
implements|,
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_CACHE_SIZE
init|=
literal|1000
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_CACHE_NAME
init|=
literal|"cayenne.default.cache"
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|cacheGroups
decl_stmt|;
specifier|private
name|int
name|maxSize
decl_stmt|;
specifier|public
name|MapQueryCache
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_CACHE_SIZE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MapQueryCache
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
name|this
operator|.
name|cacheGroups
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|maxSize
operator|=
name|maxSize
expr_stmt|;
block|}
specifier|public
name|List
name|get
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
name|String
name|key
init|=
name|metadata
operator|.
name|getCacheKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
name|createIfAbsent
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns a non-null cached value. If it is not present in the cache, it is obtained      * by calling {@link QueryCacheEntryFactory#createObject()} without blocking the cache. As      * a result there is a potential of multiple threads to be updating cache in parallel -      * this wouldn't lead to corruption of the cache, but can be suboptimal.      */
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
name|List
name|result
init|=
name|get
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|List
name|newObject
init|=
name|factory
operator|.
name|createObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|newObject
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null on cache rebuilding: %s"
argument_list|,
name|metadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
throw|;
block|}
name|result
operator|=
name|newObject
expr_stmt|;
name|put
argument_list|(
name|metadata
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
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
name|String
name|key
init|=
name|metadata
operator|.
name|getCacheKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
name|createIfAbsent
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
range|:
name|cacheGroups
operator|.
name|values
argument_list|()
control|)
block|{
synchronized|synchronized
init|(
name|map
init|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
if|if
condition|(
name|groupKey
operator|!=
literal|null
condition|)
block|{
name|cacheGroups
operator|.
name|remove
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
parameter_list|)
block|{
name|removeGroup
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|cacheGroups
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
range|:
name|cacheGroups
operator|.
name|values
argument_list|()
control|)
block|{
synchronized|synchronized
init|(
name|map
init|)
block|{
name|size
operator|+=
name|map
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|size
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|createIfAbsent
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
return|return
name|createIfAbsent
argument_list|(
name|cacheName
argument_list|(
name|metadata
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|createIfAbsent
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
name|cache
operator|=
name|createCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
specifier|synchronized
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|createCache
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
name|getCache
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
return|return
name|map
return|;
block|}
name|map
operator|=
operator|new
name|ConcurrentLinkedHashMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
operator|.
name|maximumWeightedCapacity
argument_list|(
name|maxSize
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|cacheGroups
operator|.
name|put
argument_list|(
name|cacheName
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|getCache
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cacheGroups
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|String
name|cacheName
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
name|String
name|cacheGroup
init|=
name|metadata
operator|.
name|getCacheGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|cacheGroup
operator|!=
literal|null
condition|)
block|{
return|return
name|cacheGroup
return|;
block|}
comment|// no explicit cache group
return|return
name|DEFAULT_CACHE_NAME
return|;
block|}
block|}
end_class

end_unit

