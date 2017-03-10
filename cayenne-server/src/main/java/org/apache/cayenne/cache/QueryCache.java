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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Defines API of a cache that stores query results.  *  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryCache
block|{
comment|/**      * Returns a cached query result for the given QueryMetadata or null if the result is      * not cached or is expired.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|List
name|get
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
function_decl|;
comment|/**      * Returns a cached query result for the given QueryMetadata. If the result is not      * cached or is expired, cache will use provided factory to rebuild the value and      * store it in the cache. A corollary is that this method never returns null.      *<p>      * Compared to {@link #get(QueryMetadata)}, this method allows the cache to do      * appropriate synchronization when refreshing the entry, preventing multiple threads      * from running the same query when a missing entry is requested by multiple threads      * simultaneously.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|List
name|get
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|,
name|QueryCacheEntryFactory
name|factory
parameter_list|)
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|void
name|put
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|,
name|List
name|results
parameter_list|)
function_decl|;
comment|/**      * Removes a single entry from cache.      */
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Removes a group of entries identified by group key. Note that depending on      * implementation this method may either actively remove the entries belonging to the      * group or just mark them as expired, so that they are refreshed on the next access.      * In the former case the cache size would shrink, but in the later the cache size      * will not change after calling this method.      */
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|)
function_decl|;
comment|/**      * Clears all cache entries.      *      * @deprecated since 4.0. Please use implementation specific methods to perform cache management tasks.      */
annotation|@
name|Deprecated
name|void
name|clear
parameter_list|()
function_decl|;
comment|/**      * Returns the number of entries currently in the cache, including expired but not      * removed entries.      *      * @deprecated since 4.0. Please use implementation specific methods to perform cache management tasks.      */
annotation|@
name|Deprecated
name|int
name|size
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

