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
name|tutorial
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
name|configuration
operator|.
name|rop
operator|.
name|client
operator|.
name|ProtostuffModule
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
name|server
operator|.
name|ROPServerModule
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
name|server
operator|.
name|ServerRuntime
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
name|web
operator|.
name|WebConfiguration
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
name|web
operator|.
name|WebUtil
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
name|RemoteService
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
name|rop
operator|.
name|ROPSerializationService
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
name|rop
operator|.
name|ROPServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

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

begin_class
specifier|public
class|class
name|Http2ROPServlet
extends|extends
name|ROPServlet
block|{
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|configuration
parameter_list|)
throws|throws
name|ServletException
block|{
name|checkAlreadyConfigured
argument_list|(
name|configuration
operator|.
name|getServletContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletContext
operator|=
name|configuration
operator|.
name|getServletContext
argument_list|()
expr_stmt|;
name|WebConfiguration
name|configAdapter
init|=
operator|new
name|WebConfiguration
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|String
name|configurationLocation
init|=
name|configAdapter
operator|.
name|getConfigurationLocation
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeParameters
init|=
name|configAdapter
operator|.
name|getOtherParameters
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|configAdapter
operator|.
name|createModules
argument_list|(
operator|new
name|ROPServerModule
argument_list|(
name|eventBridgeParameters
argument_list|)
argument_list|,
operator|new
name|ProtostuffModule
argument_list|()
argument_list|)
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
name|ServerRuntime
operator|.
name|builder
argument_list|()
operator|.
name|addConfig
argument_list|(
name|configurationLocation
argument_list|)
operator|.
name|addModules
argument_list|(
name|modules
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|this
operator|.
name|remoteService
operator|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|RemoteService
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializationService
operator|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ROPSerializationService
operator|.
name|class
argument_list|)
expr_stmt|;
name|WebUtil
operator|.
name|setCayenneRuntime
argument_list|(
name|servletContext
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

