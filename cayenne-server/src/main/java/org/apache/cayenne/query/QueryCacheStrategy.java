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

begin_comment
comment|/**  * Defines query result caching policy.  *   * @since 3.0  */
end_comment

begin_enum
specifier|public
enum|enum
name|QueryCacheStrategy
block|{
comment|/**      * A default cache policy stating that the query results should not be cached.      */
name|NO_CACHE
block|,
comment|/**      * A cache policy stating that query results shall be cached by the ObjectContext that      * originated the query, independent from any other ObjectContexts.      */
name|LOCAL_CACHE
block|,
comment|/**      * A cache policy stating that query results shall be cached by the ObjectContext that      * originated the query, independent from any other ObjectContexts, however the query      * that uses this policy should treat current cache state as expired, and force the      * database fetch.      */
name|LOCAL_CACHE_REFRESH
block|,
comment|/**      * A cache policy ruling that query results shall be cached in a shared location      * accessible by all ObjectContexts.      */
name|SHARED_CACHE
block|,
comment|/**      * A cache policy ruling that query results shall be cached in a shared location      * accessible by all ObjectContexts, however the query that uses this policy should      * treat current cache state as expired, and force the database fetch.      */
name|SHARED_CACHE_REFRESH
block|;
comment|/**      * Returns QueryCacheStrategy for the specified string name or default strategy for      * invalid names.      */
specifier|public
specifier|static
name|QueryCacheStrategy
name|safeValueOf
parameter_list|(
name|String
name|string
parameter_list|)
block|{
try|try
block|{
return|return
name|QueryCacheStrategy
operator|.
name|valueOf
argument_list|(
name|string
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
name|getDefaultStrategy
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the default strategy - {@link #NO_CACHE}.      */
specifier|public
specifier|static
name|QueryCacheStrategy
name|getDefaultStrategy
parameter_list|()
block|{
return|return
name|NO_CACHE
return|;
block|}
block|}
end_enum

end_unit

