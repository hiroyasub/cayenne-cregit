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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Element
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
specifier|public
class|class
name|EhCacheQueryCache
implements|implements
name|QueryCache
block|{
comment|/**      * Default cache group name.      */
specifier|static
specifier|final
name|String
name|DEFAULT_CACHE_NAME
init|=
literal|"cayenne.default.cachegroup"
decl_stmt|;
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EhCacheQueryCache
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|CacheManager
name|cacheManager
decl_stmt|;
specifier|public
name|EhCacheQueryCache
parameter_list|()
block|{
name|cacheManager
operator|=
operator|new
name|CacheManager
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EhCacheQueryCache
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|cacheManager
operator|=
operator|new
name|CacheManager
argument_list|(
name|configFile
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EhCacheQueryCache
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|)
block|{
if|if
condition|(
name|cacheManager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|cacheManager
operator|=
name|cacheManager
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"CacheManager cannot be null."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
name|DEFAULT_CACHE_NAME
argument_list|)
expr_stmt|;
block|}
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
name|String
name|cacheName
init|=
name|cacheName
argument_list|(
name|key
argument_list|,
name|metadata
operator|.
name|getCacheGroups
argument_list|()
argument_list|)
decl_stmt|;
name|Ehcache
name|cache
init|=
name|cacheManager
operator|.
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
return|return
literal|null
return|;
block|}
name|Element
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
operator|(
name|List
operator|)
name|result
operator|.
name|getObjectValue
argument_list|()
else|:
literal|null
return|;
block|}
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
comment|// TODO: we should really throw here... The method declares that it
comment|// never returns null
return|return
literal|null
return|;
block|}
name|String
name|cacheName
init|=
name|cacheName
argument_list|(
name|key
argument_list|,
name|metadata
operator|.
name|getCacheGroups
argument_list|()
argument_list|)
decl_stmt|;
comment|// create empty cache for cache group here, as we have a factory to
comment|// create an object, and should never ever return null from this
comment|// method.
name|Ehcache
name|cache
init|=
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
name|Element
name|result
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|List
operator|)
name|result
operator|.
name|getObjectValue
argument_list|()
return|;
block|}
comment|// if no result in cache locking the key to write
comment|// and putting it to the cache
name|cache
operator|.
name|acquireWriteLockOnKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
try|try
block|{
comment|// now that we locked the key, let's reread the cache again, in case
comment|// an object appeared there already
name|result
operator|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|List
operator|)
name|result
operator|.
name|getObjectValue
argument_list|()
return|;
block|}
comment|// if not succeeded in reading again putting
comment|// object to the cache ourselves
name|Object
name|object
init|=
name|factory
operator|.
name|createObject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|List
operator|)
condition|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null object created: "
operator|+
name|metadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid query result, expected List, got "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|object
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|(
name|List
operator|)
name|object
return|;
block|}
finally|finally
block|{
name|cache
operator|.
name|releaseWriteLockOnKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 3.2      */
specifier|protected
name|String
name|cacheName
parameter_list|(
name|String
name|key
parameter_list|,
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
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
if|if
condition|(
name|cacheGroups
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"multiple cache groups per key '"
operator|+
name|key
operator|+
literal|"', ignoring all but the first one: "
operator|+
name|cacheGroups
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|cacheGroups
index|[
literal|0
index|]
return|;
block|}
return|return
name|DEFAULT_CACHE_NAME
return|;
block|}
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
operator|!=
literal|null
condition|)
block|{
name|String
name|cacheName
init|=
name|cacheName
argument_list|(
name|key
argument_list|,
name|metadata
operator|.
name|getCacheGroups
argument_list|()
argument_list|)
decl_stmt|;
name|Ehcache
name|cache
init|=
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|results
argument_list|)
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
name|cacheManager
operator|.
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
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
name|Ehcache
name|cache
init|=
name|cacheManager
operator|.
name|getEhcache
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
name|removeAll
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|cacheManager
operator|.
name|removalAll
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
name|String
name|cache
range|:
name|cacheManager
operator|.
name|getCacheNames
argument_list|()
control|)
block|{
name|size
operator|+=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|cache
argument_list|)
operator|.
name|getSize
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
comment|/**      * Returns default cache group.      *       * @deprecated since 3.2 - this method is no longer in use. If you are      *             overriding it, override {@link #cacheName(String, String...)}      *             instead.      */
annotation|@
name|Deprecated
specifier|public
name|Ehcache
name|getDefaultCache
parameter_list|()
block|{
return|return
name|cacheManager
operator|.
name|getCache
argument_list|(
name|DEFAULT_CACHE_NAME
argument_list|)
return|;
block|}
comment|/**      * Shuts down EhCache CacheManager      */
annotation|@
name|BeforeScopeEnd
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|cacheManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
