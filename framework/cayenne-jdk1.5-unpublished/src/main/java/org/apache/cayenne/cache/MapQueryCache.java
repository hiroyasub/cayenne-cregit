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
name|Iterator
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
name|commons
operator|.
name|collections
operator|.
name|map
operator|.
name|LRUMap
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
literal|2000
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|CacheEntry
argument_list|>
name|map
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
name|map
operator|=
operator|new
name|LRUMap
argument_list|(
name|maxSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|CacheEntry
name|entry
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|entry
operator|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|entry
operator|!=
literal|null
operator|)
condition|?
name|entry
operator|.
name|list
else|:
literal|null
return|;
block|}
comment|/**      * Returns a non-null cached value. If it is not present in the cache, it is obtained      * by calling {@link QueryCacheEntryFactory#createObject()} without blocking the cache. As      * a result there is a potential of multiple threads to be updating cache in parallel -      * this wouldn't lead to corruption of the cache, but can be suboptimal.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|Object
name|newObject
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
name|newObject
operator|instanceof
name|List
operator|)
condition|)
block|{
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
name|newObject
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
name|result
operator|=
operator|(
name|List
operator|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|CacheEntry
name|entry
init|=
operator|new
name|CacheEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|list
operator|=
name|results
expr_stmt|;
name|entry
operator|.
name|cacheGroups
operator|=
name|metadata
operator|.
name|getCacheGroups
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
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
synchronized|synchronized
init|(
name|this
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
synchronized|synchronized
init|(
name|this
init|)
block|{
name|Iterator
argument_list|<
name|CacheEntry
argument_list|>
name|it
init|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CacheEntry
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|cacheGroups
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entry
operator|.
name|cacheGroups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|groupKey
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|cacheGroups
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
specifier|final
specifier|static
class|class
name|CacheEntry
implements|implements
name|Serializable
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
decl_stmt|;
name|String
index|[]
name|cacheGroups
decl_stmt|;
block|}
block|}
end_class

end_unit

