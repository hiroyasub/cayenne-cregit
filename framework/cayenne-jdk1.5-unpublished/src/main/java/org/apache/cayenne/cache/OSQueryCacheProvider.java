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
name|ConfigurationException
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

begin_comment
comment|/**  * A provider for the OSCache factory. "/oscache.properties" file is read to load the  * standard OSCache properties and also extra properties  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|OSQueryCacheProvider
implements|implements
name|Provider
argument_list|<
name|QueryCache
argument_list|>
block|{
specifier|public
name|QueryCache
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
return|return
operator|new
name|QueryCacheLazyInitializationProxy
argument_list|(
operator|new
name|Provider
argument_list|<
name|QueryCache
argument_list|>
argument_list|()
block|{
specifier|public
name|QueryCache
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
return|return
operator|new
name|OSQueryCache
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

