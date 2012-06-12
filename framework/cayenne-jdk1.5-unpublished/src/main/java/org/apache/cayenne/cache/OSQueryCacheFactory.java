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
name|Map
import|;
end_import

begin_comment
comment|/**  * A factory for the OSCache factory. "/oscache.properties" file is read to load the  * standard OSCache properties and also extra properties  *   * @since 3.0  * @deprecated since 3.1 OSQueryCacheFactory and QueryCacheFactory are unused.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|OSQueryCacheFactory
implements|implements
name|QueryCacheFactory
block|{
comment|/**      * Creates QueryCache, ignoring provided properties, and reading data from      * "oscache.properties" file instead.      */
specifier|public
name|QueryCache
name|getQueryCache
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
return|return
operator|new
name|OSQueryCache
argument_list|()
return|;
block|}
block|}
end_class

end_unit

