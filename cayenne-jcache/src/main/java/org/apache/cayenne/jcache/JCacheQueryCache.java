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
name|jcache
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
name|cache
operator|.
name|QueryCacheEntryFactory
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
name|BeforeScopeEnd
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
name|query
operator|.
name|QueryMetadata
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|CacheException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|CacheManager
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
name|Objects
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|JCacheQueryCache
implements|implements
name|QueryCache
block|{
annotation|@
name|Inject
specifier|protected
name|CacheManager
name|cacheManager
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JCacheConfigurationFactory
name|configurationFactory
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|seenCacheNames
init|=
name|Collections
operator|.
name|newSetFromMap
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
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
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|metadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
decl_stmt|;
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|cache
init|=
name|createIfAbsent
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
return|return
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|String
name|key
init|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|metadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
decl_stmt|;
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|cache
init|=
name|createIfAbsent
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|result
operator|!=
literal|null
condition|?
name|result
else|:
name|cache
operator|.
name|invoke
argument_list|(
name|key
argument_list|,
operator|new
name|JCacheEntryLoader
argument_list|(
name|factory
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|metadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
decl_stmt|;
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|cache
init|=
name|createIfAbsent
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|cache
range|:
name|cacheManager
operator|.
name|getCacheNames
argument_list|()
control|)
block|{
name|getCache
argument_list|(
name|cache
argument_list|)
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|groupKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|seenCacheNames
control|)
block|{
name|getCache
argument_list|(
name|name
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|protected
name|Cache
argument_list|<
name|String
argument_list|,
name|List
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|createIfAbsent
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|Cache
argument_list|<
name|String
argument_list|,
name|List
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
try|try
block|{
name|cache
operator|=
name|createCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CacheException
name|e
parameter_list|)
block|{
comment|// someone else just created this cache?
name|cache
operator|=
name|getCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
comment|// giving up... the error was about something else...
throw|throw
name|e
throw|;
block|}
block|}
name|seenCacheNames
operator|.
name|add
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
specifier|protected
name|Cache
name|createCache
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
return|return
name|cacheManager
operator|.
name|createCache
argument_list|(
name|cacheName
argument_list|,
name|configurationFactory
operator|.
name|create
argument_list|(
name|cacheName
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
name|getCache
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cacheManager
operator|.
name|getCache
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
name|JCacheConstants
operator|.
name|DEFAULT_CACHE_NAME
return|;
block|}
annotation|@
name|BeforeScopeEnd
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|cacheManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

