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
name|HashMap
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
name|Properties
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
name|com
operator|.
name|opensymphony
operator|.
name|oscache
operator|.
name|base
operator|.
name|CacheEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|oscache
operator|.
name|base
operator|.
name|NeedsRefreshException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|oscache
operator|.
name|general
operator|.
name|GeneralCacheAdministrator
import|;
end_import

begin_comment
comment|/**  * A {@link QueryCache} implementation based on OpenSymphony OSCache. Query cache  * parameters are initialized from "/oscache.properties" file per<a  * href="http://www.opensymphony.com/oscache/wiki/Configuration.html">OSCache</a>  * documentation. In addition to the standard OSCache parameters, Cayenne provider allows  * to setup global cache expiration parameters, and parameters matching the main query  * cache group (i.e. the cache groups specified first). A sample oscache.properties may  * look like this:  *   *<pre>  * # OSCache configuration file  *                          * # OSCache standard configuration per  * #     http://www.opensymphony.com/oscache/wiki/Configuration.html  * # ---------------------------------------------------------------  *                          * #cache.memory=true  * #cache.blocking=false  * cache.capacity=5000  * cache.algorithm=com.opensymphony.oscache.base.algorithm.LRUCache  *                          * # Cayenne specific properties  * # ---------------------------------------------------------------  *                          * # Default refresh period in seconds:  * cayenne.default.refresh = 60  *                          * # Default expiry specified as cron expressions per  * #    http://www.opensymphony.com/oscache/wiki/Cron%20Expressions.html  * # expire entries every hour on the 10's minute  * cayenne.default.cron = 10 * * * *  *                          * # Same parameters can be overriden per query  * cayenne.group.xyz.refresh = 120  * cayenne.group.xyz.cron = 10 1 * * *  *</pre>  *   * Further extension of OSQueryCache is possible by using OSCache listener API.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|OSQueryCache
implements|implements
name|QueryCache
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_REFRESH_PERIOD
init|=
name|CacheEntry
operator|.
name|INDEFINITE_EXPIRY
decl_stmt|;
specifier|static
name|String
name|DEFAULT_REFRESH_KEY
init|=
literal|"cayenne.default.refresh"
decl_stmt|;
specifier|static
name|String
name|DEFAULT_CRON_KEY
init|=
literal|"cayenne.default.cron"
decl_stmt|;
specifier|static
name|String
name|GROUP_PREFIX
init|=
literal|"cayenne.group."
decl_stmt|;
specifier|static
name|String
name|REFRESH_SUFFIX
init|=
literal|".refresh"
decl_stmt|;
specifier|static
name|String
name|CRON_SUFFIX
init|=
literal|".cron"
decl_stmt|;
specifier|protected
name|GeneralCacheAdministrator
name|osCache
decl_stmt|;
name|RefreshSpecification
name|defaultRefreshSpecification
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|RefreshSpecification
argument_list|>
name|refreshSpecifications
decl_stmt|;
name|Properties
name|properties
decl_stmt|;
specifier|public
name|OSQueryCache
parameter_list|()
block|{
name|OSCacheAdministrator
name|admin
init|=
operator|new
name|OSCacheAdministrator
argument_list|()
decl_stmt|;
name|init
argument_list|(
name|admin
argument_list|,
name|admin
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OSQueryCache
parameter_list|(
name|GeneralCacheAdministrator
name|cache
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|init
argument_list|(
name|cache
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a collection of group names that have been configured explicitly via      * properties.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Collection
name|getGroupNames
parameter_list|()
block|{
return|return
name|refreshSpecifications
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|refreshSpecifications
operator|.
name|keySet
argument_list|()
argument_list|)
else|:
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
specifier|public
name|String
name|getCronExpression
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|RefreshSpecification
name|spec
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|refreshSpecifications
operator|!=
literal|null
condition|)
block|{
name|spec
operator|=
name|refreshSpecifications
operator|.
name|get
argument_list|(
name|groupName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|==
literal|null
condition|)
block|{
name|spec
operator|=
name|defaultRefreshSpecification
expr_stmt|;
block|}
return|return
name|spec
operator|.
name|cronExpression
return|;
block|}
specifier|public
name|int
name|getRrefreshPeriod
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|RefreshSpecification
name|spec
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|refreshSpecifications
operator|!=
literal|null
condition|)
block|{
name|spec
operator|=
name|refreshSpecifications
operator|.
name|get
argument_list|(
name|groupName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|==
literal|null
condition|)
block|{
name|spec
operator|=
name|defaultRefreshSpecification
expr_stmt|;
block|}
return|return
name|spec
operator|.
name|refreshPeriod
return|;
block|}
comment|/**      * Returns the underlying OSCache manager object.      */
specifier|public
name|GeneralCacheAdministrator
name|getOsCache
parameter_list|()
block|{
return|return
name|osCache
return|;
block|}
comment|/**      * Returns configuration properties. Usually this is the contents of      * "oscache.properties" file.      */
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
name|void
name|init
parameter_list|(
name|GeneralCacheAdministrator
name|cache
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|osCache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|defaultRefreshSpecification
operator|=
operator|new
name|RefreshSpecification
argument_list|()
expr_stmt|;
comment|// load defaults and per-query settings
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
comment|// first extract defaults...
name|String
name|defaultRefresh
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|DEFAULT_REFRESH_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultRefresh
operator|!=
literal|null
condition|)
block|{
name|defaultRefreshSpecification
operator|.
name|setRefreshPeriod
argument_list|(
name|defaultRefresh
argument_list|)
expr_stmt|;
block|}
name|String
name|defaultCron
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|DEFAULT_CRON_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultCron
operator|!=
literal|null
condition|)
block|{
name|defaultRefreshSpecification
operator|.
name|cronExpression
operator|=
name|defaultCron
expr_stmt|;
block|}
comment|// now check for per-query settings
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|==
literal|null
operator|||
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
name|REFRESH_SUFFIX
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|key
operator|.
name|substring
argument_list|(
name|GROUP_PREFIX
operator|.
name|length
argument_list|()
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
name|REFRESH_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|initRefreshPolicy
argument_list|(
name|name
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
name|CRON_SUFFIX
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|key
operator|.
name|substring
argument_list|(
name|GROUP_PREFIX
operator|.
name|length
argument_list|()
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
name|CRON_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|initCronPolicy
argument_list|(
name|name
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Called internally for each group that is configured with cron policy in the      * properties. Exposed mainly for the benefit of subclasses. When overriding, call      * 'super'.      */
specifier|protected
name|void
name|initCronPolicy
parameter_list|(
name|String
name|groupName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|nonNullSpec
argument_list|(
name|groupName
argument_list|)
operator|.
name|cronExpression
operator|=
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
comment|/**      * Called internally for each group that is configured with refresh policy in the      * properties. Exposed mainly for the benefit of subclasses. When overriding, call      * 'super'.      */
specifier|protected
name|void
name|initRefreshPolicy
parameter_list|(
name|String
name|groupName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|nonNullSpec
argument_list|(
name|groupName
argument_list|)
operator|.
name|setRefreshPeriod
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|RefreshSpecification
name|nonNullSpec
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|refreshSpecifications
operator|==
literal|null
condition|)
block|{
name|refreshSpecifications
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|RefreshSpecification
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|RefreshSpecification
name|spec
init|=
name|refreshSpecifications
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|spec
operator|==
literal|null
condition|)
block|{
name|spec
operator|=
operator|new
name|RefreshSpecification
argument_list|()
expr_stmt|;
name|spec
operator|.
name|cronExpression
operator|=
name|defaultRefreshSpecification
operator|.
name|cronExpression
expr_stmt|;
name|spec
operator|.
name|refreshPeriod
operator|=
name|defaultRefreshSpecification
operator|.
name|refreshPeriod
expr_stmt|;
name|refreshSpecifications
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
return|return
name|spec
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
name|RefreshSpecification
name|refresh
init|=
name|getRefreshSpecification
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|List
operator|)
name|osCache
operator|.
name|getFromCache
argument_list|(
name|key
argument_list|,
name|refresh
operator|.
name|refreshPeriod
argument_list|,
name|refresh
operator|.
name|cronExpression
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NeedsRefreshException
name|e
parameter_list|)
block|{
name|osCache
operator|.
name|cancelUpdate
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns a non-null cached value. If it is not present in the cache, it is obtained      * by calling {@link QueryCacheEntryFactory#createObject()}. Whether the cache      * provider will block on the entry update or not is controlled by "cache.blocking"      * configuration property and is "false" by default.      */
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
return|return
literal|null
return|;
block|}
name|RefreshSpecification
name|refresh
init|=
name|getRefreshSpecification
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|List
operator|)
name|osCache
operator|.
name|getFromCache
argument_list|(
name|key
argument_list|,
name|refresh
operator|.
name|refreshPeriod
argument_list|,
name|refresh
operator|.
name|cronExpression
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NeedsRefreshException
name|e
parameter_list|)
block|{
name|boolean
name|updated
init|=
literal|false
decl_stmt|;
try|try
block|{
name|Object
name|result
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
name|result
operator|instanceof
name|List
operator|)
condition|)
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null on cache rebuilding: "
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
name|result
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
name|List
name|list
init|=
operator|(
name|List
operator|)
name|result
decl_stmt|;
name|put
argument_list|(
name|metadata
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
return|return
name|list
return|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|updated
condition|)
block|{
comment|// It is essential that cancelUpdate is called if the
comment|// cached content could not be rebuilt
name|osCache
operator|.
name|cancelUpdate
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Returns non-null RefreshSpecification for the QueryMetadata.      */
name|RefreshSpecification
name|getRefreshSpecification
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
name|RefreshSpecification
name|refresh
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|refreshSpecifications
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|groups
init|=
name|metadata
operator|.
name|getCacheGroups
argument_list|()
decl_stmt|;
if|if
condition|(
name|groups
operator|!=
literal|null
operator|&&
name|groups
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|refresh
operator|=
name|refreshSpecifications
operator|.
name|get
argument_list|(
name|groups
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|refresh
operator|!=
literal|null
condition|?
name|refresh
else|:
name|defaultRefreshSpecification
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
name|osCache
operator|.
name|putInCache
argument_list|(
name|key
argument_list|,
name|results
argument_list|,
name|metadata
operator|.
name|getCacheGroups
argument_list|()
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
name|osCache
operator|.
name|removeEntry
argument_list|(
name|key
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
parameter_list|)
block|{
if|if
condition|(
name|groupKey
operator|!=
literal|null
condition|)
block|{
name|osCache
operator|.
name|flushGroup
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|osCache
operator|.
name|flushAll
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|osCache
operator|.
name|getCache
argument_list|()
operator|.
name|getSize
argument_list|()
return|;
block|}
specifier|public
name|int
name|capacity
parameter_list|()
block|{
return|return
name|osCache
operator|.
name|getCache
argument_list|()
operator|.
name|getCapacity
argument_list|()
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
name|osCache
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|final
specifier|static
class|class
name|RefreshSpecification
block|{
name|int
name|refreshPeriod
decl_stmt|;
name|String
name|cronExpression
decl_stmt|;
name|RefreshSpecification
parameter_list|()
block|{
name|this
operator|.
name|refreshPeriod
operator|=
name|DEFAULT_REFRESH_PERIOD
expr_stmt|;
block|}
name|RefreshSpecification
parameter_list|(
name|int
name|refrehsPeriod
parameter_list|,
name|String
name|cronExpression
parameter_list|)
block|{
name|this
operator|.
name|refreshPeriod
operator|=
name|refrehsPeriod
expr_stmt|;
name|this
operator|.
name|cronExpression
operator|=
name|cronExpression
expr_stmt|;
block|}
name|void
name|setRefreshPeriod
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|refreshPeriod
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// ignore...
block|}
block|}
block|}
specifier|final
specifier|static
class|class
name|OSCacheAdministrator
extends|extends
name|GeneralCacheAdministrator
block|{
name|OSCacheAdministrator
parameter_list|()
block|{
block|}
name|OSCacheAdministrator
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|super
operator|.
name|config
operator|.
name|getProperties
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
