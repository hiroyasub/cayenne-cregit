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
name|rop
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
name|CayenneRuntime
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
name|ClientMessage
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
name|remote
operator|.
name|RemoteSession
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
name|ServletContext
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ROPServlet
extends|extends
name|HttpServlet
block|{
specifier|protected
name|ServletContext
name|servletContext
decl_stmt|;
specifier|protected
name|RemoteService
name|remoteService
decl_stmt|;
specifier|protected
name|ROPSerializationService
name|serializationService
decl_stmt|;
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
name|super
operator|.
name|init
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|checkAlreadyConfigured
parameter_list|(
name|ServletContext
name|context
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// sanity check
if|if
condition|(
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"CayenneRuntime is already configured in the servlet environment"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|servletContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|runtime
operator|!=
literal|null
condition|)
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
try|try
block|{
name|String
name|serviceId
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|String
name|objectId
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectId
operator|==
literal|null
condition|)
block|{
name|objectId
operator|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"ejbid"
argument_list|)
expr_stmt|;
block|}
name|ROPRequestContext
operator|.
name|start
argument_list|(
name|serviceId
argument_list|,
name|objectId
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
name|String
name|operation
init|=
name|req
operator|.
name|getParameter
argument_list|(
name|ROPConstants
operator|.
name|OPERATION_PARAMETER
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|ROPConstants
operator|.
name|ESTABLISH_SESSION_OPERATION
case|:
name|RemoteSession
name|session
init|=
name|remoteService
operator|.
name|establishSession
argument_list|()
decl_stmt|;
name|serializationService
operator|.
name|serialize
argument_list|(
name|session
argument_list|,
name|resp
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|ROPConstants
operator|.
name|ESTABLISH_SHARED_SESSION_OPERATION
case|:
name|String
name|sessionName
init|=
name|req
operator|.
name|getParameter
argument_list|(
name|ROPConstants
operator|.
name|SESSION_NAME_PARAMETER
argument_list|)
decl_stmt|;
name|RemoteSession
name|sharedSession
init|=
name|remoteService
operator|.
name|establishSharedSession
argument_list|(
name|sessionName
argument_list|)
decl_stmt|;
name|serializationService
operator|.
name|serialize
argument_list|(
name|sharedSession
argument_list|,
name|resp
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unknown operation: "
operator|+
name|operation
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|Object
name|response
init|=
name|remoteService
operator|.
name|processMessage
argument_list|(
name|serializationService
operator|.
name|deserialize
argument_list|(
name|req
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|ClientMessage
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|serializationService
operator|.
name|serialize
argument_list|(
name|response
argument_list|,
name|resp
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
decl||
name|ServletException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ROPRequestContext
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

