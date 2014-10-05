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
name|configuration
operator|.
name|rop
operator|.
name|client
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
name|ModuleCollection
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
name|Binder
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
name|Injector
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
name|Key
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
name|Module
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

begin_comment
comment|/**  * A {@link ClientRuntime} that provides an ROP stack based on a local  * connection on top of a server stack.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ClientLocalRuntime
extends|extends
name|ClientRuntime
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SERVER_CHANNEL_KEY
init|=
literal|"client-server-channel"
decl_stmt|;
specifier|private
specifier|static
name|ModuleCollection
name|mainModuleOverride
parameter_list|(
specifier|final
name|Injector
name|serverInjector
parameter_list|)
block|{
return|return
operator|new
name|ModuleCollection
argument_list|(
operator|new
name|Module
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|,
name|CLIENT_SERVER_CHANNEL_KEY
argument_list|)
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|LocalClientServerChannelProvider
argument_list|(
name|serverInjector
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|LocalConnectionProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
name|ClientLocalRuntime
parameter_list|(
name|Injector
name|serverInjector
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|Collection
argument_list|<
name|Module
argument_list|>
name|extraModules
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|mainModuleOverride
argument_list|(
name|serverInjector
argument_list|)
operator|.
name|add
argument_list|(
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientLocalRuntime
parameter_list|(
name|Injector
name|serverInjector
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|Module
modifier|...
name|extraModules
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|mainModuleOverride
argument_list|(
name|serverInjector
argument_list|)
operator|.
name|add
argument_list|(
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

