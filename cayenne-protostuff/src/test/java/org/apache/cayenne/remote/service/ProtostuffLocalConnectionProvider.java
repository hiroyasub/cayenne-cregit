begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|remote
operator|.
name|service
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
name|configuration
operator|.
name|rop
operator|.
name|client
operator|.
name|ClientLocalRuntime
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
name|remote
operator|.
name|ClientConnection
import|;
end_import

begin_class
specifier|public
class|class
name|ProtostuffLocalConnectionProvider
implements|implements
name|Provider
argument_list|<
name|ClientConnection
argument_list|>
block|{
annotation|@
name|Inject
argument_list|(
name|ClientLocalRuntime
operator|.
name|CLIENT_SERVER_CHANNEL_KEY
argument_list|)
specifier|protected
name|Provider
argument_list|<
name|DataChannel
argument_list|>
name|clientServerChannelProvider
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ClientConnection
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|DataChannel
name|clientServerChannel
init|=
name|clientServerChannelProvider
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
operator|new
name|ProtostuffLocalConnection
argument_list|(
name|clientServerChannel
argument_list|)
return|;
block|}
block|}
end_class

end_unit

