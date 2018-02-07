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
name|access
operator|.
name|ClientServerChannel
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
name|access
operator|.
name|DataContext
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
name|Constants
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
name|ObjectContextFactory
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A generic implementation of an RemoteService. Can be subclassed to work with  * different remoting mechanisms, such as Hessian or JAXRPC.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseRemoteService
implements|implements
name|RemoteService
block|{
comment|// keep logger non-static so that it could be garbage collected with this
comment|// instance.
specifier|protected
specifier|final
name|Logger
name|logger
decl_stmt|;
specifier|protected
name|ObjectContextFactory
name|contextFactory
decl_stmt|;
specifier|protected
name|String
name|eventBridgeFactoryName
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeParameters
decl_stmt|;
comment|/** 	 * @since 3.1 	 */
specifier|public
name|BaseRemoteService
parameter_list|(
name|ObjectContextFactory
name|contextFactory
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeProperties
parameter_list|)
block|{
name|logger
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// start Cayenne service
name|logger
operator|.
name|debug
argument_list|(
literal|"ROP service is starting"
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextFactory
operator|=
name|contextFactory
expr_stmt|;
name|initEventBridgeParameters
argument_list|(
name|eventBridgeProperties
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" started"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getEventBridgeFactoryName
parameter_list|()
block|{
return|return
name|eventBridgeFactoryName
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getEventBridgeParameters
parameter_list|()
block|{
return|return
name|eventBridgeParameters
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|eventBridgeParameters
argument_list|)
else|:
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/** 	 * Creates a new ServerSession with a dedicated DataChannel. 	 */
specifier|protected
specifier|abstract
name|ServerSession
name|createServerSession
parameter_list|()
function_decl|;
comment|/** 	 * Creates a new ServerSession based on a shared DataChannel. 	 *  	 * @param name 	 *            shared session name used to lookup a shared DataChannel. 	 */
specifier|protected
specifier|abstract
name|ServerSession
name|createServerSession
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** 	 * Returns a ServerSession object that represents Cayenne-related state 	 * associated with the current session. If ServerSession hasn't been 	 * previously saved, returns null. 	 */
specifier|protected
specifier|abstract
name|ServerSession
name|getServerSession
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
name|RemoteSession
name|establishSession
parameter_list|()
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Session requested by client"
argument_list|)
expr_stmt|;
name|RemoteSession
name|session
init|=
name|createServerSession
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Established client session: "
operator|+
name|session
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteSession
name|establishSharedSession
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Shared session requested by client. Group name: "
operator|+
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid null shared session name"
argument_list|)
throw|;
block|}
return|return
name|createServerSession
argument_list|(
name|name
argument_list|)
operator|.
name|getSession
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|processMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null client message."
argument_list|)
throw|;
block|}
name|ServerSession
name|handler
init|=
name|getServerSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingSessionException
argument_list|(
literal|"No session associated with request."
argument_list|)
throw|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"processMessage, sessionId: "
operator|+
name|handler
operator|.
name|getSession
argument_list|()
operator|.
name|getSessionId
argument_list|()
argument_list|)
expr_stmt|;
comment|// intercept and log exceptions
try|try
block|{
return|return
name|DispatchHelper
operator|.
name|dispatch
argument_list|(
name|handler
operator|.
name|getChannel
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|String
name|wrapperMessageString
init|=
literal|"Exception processing message "
operator|+
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" of type "
operator|+
name|message
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|wrapperMessageString
argument_list|,
name|th
argument_list|)
expr_stmt|;
comment|// This exception will probably be propagated to the client.
comment|// Recast the exception to a serializable form.
name|Exception
name|cause
init|=
operator|new
name|Exception
argument_list|(
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|wrapperMessageString
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|RemoteException
block|{
block|}
specifier|protected
name|RemoteSession
name|createRemoteSession
parameter_list|(
name|String
name|sessionId
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|enableEvents
parameter_list|)
block|{
name|RemoteSession
name|session
init|=
operator|(
name|enableEvents
operator|)
condition|?
operator|new
name|RemoteSession
argument_list|(
name|sessionId
argument_list|,
name|eventBridgeFactoryName
argument_list|,
name|eventBridgeParameters
argument_list|)
else|:
operator|new
name|RemoteSession
argument_list|(
name|sessionId
argument_list|)
decl_stmt|;
name|session
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
comment|/** 	 * Creates a server-side channel that will handle all client requests. For 	 * shared sessions the same channel instance is reused for the entire group 	 * of clients. For dedicated sessions, one channel per client is created. 	 *<p> 	 * This implementation returns {@link ClientServerChannel} instance wrapping 	 * a DataContext. Subclasses may override the method to customize channel 	 * creation. For instance they may wrap channel in the custom interceptors 	 * to handle transactions or security. 	 */
specifier|protected
name|DataChannel
name|createChannel
parameter_list|()
block|{
return|return
operator|new
name|ClientServerChannel
argument_list|(
operator|(
name|DataContext
operator|)
name|contextFactory
operator|.
name|createContext
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Initializes EventBridge parameters for remote clients peer-to-peer 	 * communications. 	 */
specifier|protected
name|void
name|initEventBridgeParameters
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
name|String
name|eventBridgeFactoryName
init|=
name|properties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVER_ROP_EVENT_BRIDGE_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventBridgeFactoryName
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|eventBridgeParameters
operator|.
name|remove
argument_list|(
name|Constants
operator|.
name|SERVER_ROP_EVENT_BRIDGE_FACTORY_PROPERTY
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventBridgeFactoryName
operator|=
name|eventBridgeFactoryName
expr_stmt|;
name|this
operator|.
name|eventBridgeParameters
operator|=
name|eventBridgeParameters
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

