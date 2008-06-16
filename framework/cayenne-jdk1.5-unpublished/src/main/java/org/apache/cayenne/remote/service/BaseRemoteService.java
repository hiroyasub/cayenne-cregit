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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|DataDomain
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
name|conf
operator|.
name|Configuration
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
name|conf
operator|.
name|DefaultConfiguration
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A generic implementation of an RemoteService. Subclasses can be customized to work with  * different remoting mechanisms, such as Hessian or JAXRPC.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseRemoteService
implements|implements
name|RemoteService
block|{
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_BRIDGE_FACTORY_PROPERTY
init|=
literal|"cayenne.RemoteService.EventBridge.factory"
decl_stmt|;
comment|// keep logger non-static so that it could be garbage collected with this instance..
specifier|private
specifier|final
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BaseRemoteService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|DataDomain
name|domain
decl_stmt|;
specifier|protected
name|String
name|eventBridgeFactoryName
decl_stmt|;
specifier|protected
name|Map
name|eventBridgeParameters
decl_stmt|;
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
name|EMPTY_MAP
return|;
block|}
comment|/**      * A method that sets up a service, initializing Cayenne stack. Should be invoked by      * subclasses from their appropriate service lifecycle methods.      */
specifier|protected
name|void
name|initService
parameter_list|(
name|Map
name|properties
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
comment|// start Cayenne service
name|logObj
operator|.
name|debug
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" is starting"
argument_list|)
expr_stmt|;
name|initCayenneStack
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|initEventBridgeParameters
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|logObj
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
comment|/**      * Shuts down this service. Should be invoked by subclasses from their appropriate      * service lifecycle methods.      */
specifier|protected
name|void
name|destroyService
parameter_list|()
block|{
name|logObj
operator|.
name|debug
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" destroyed"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a DataChannel that is a parent of all session DataChannels.      */
specifier|public
name|DataChannel
name|getRootChannel
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * Creates a new ServerSession with a dedicated DataChannel.      */
specifier|protected
specifier|abstract
name|ServerSession
name|createServerSession
parameter_list|()
function_decl|;
comment|/**      * Creates a new ServerSession based on a shared DataChannel.      *       * @param name shared session name used to lookup a shared DataChannel.      */
specifier|protected
specifier|abstract
name|ServerSession
name|createServerSession
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a ServerSession object that represents Cayenne-related state associated      * with the current session. If ServerSession hasn't been previously saved, returns      * null.      */
specifier|protected
specifier|abstract
name|ServerSession
name|getServerSession
parameter_list|()
function_decl|;
specifier|public
name|RemoteSession
name|establishSession
parameter_list|()
block|{
name|logObj
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
name|logObj
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
specifier|public
name|RemoteSession
name|establishSharedSession
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|logObj
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
specifier|public
name|Object
name|processMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|Throwable
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
name|logObj
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
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
name|logObj
operator|.
name|info
argument_list|(
literal|"error processing message"
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
name|th
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|wrapperMessage
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|wrapperMessage
operator|.
name|append
argument_list|(
literal|"Exception processing message "
argument_list|)
operator|.
name|append
argument_list|(
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" of type "
argument_list|)
operator|.
name|append
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|wrapperMessage
operator|.
name|toString
argument_list|()
argument_list|,
name|cause
argument_list|)
throw|;
block|}
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
comment|/**      * Creates a server-side channel that will handle all client requests. For shared      * sessions the same channel instance is reused for the entire group of clients. For      * dedicated sessions, one channel per client is created.<p/> This implementation      * returns {@link ClientServerChannel} instance wrapping a DataContext. Subclasses may      * override the method to customize channel creation. For instance they may wrap      * channel in the custom interceptors to handle transactions or security.      */
specifier|protected
name|DataChannel
name|createChannel
parameter_list|()
block|{
return|return
operator|new
name|ClientServerChannel
argument_list|(
name|domain
argument_list|)
return|;
block|}
comment|/**      * Sets up Cayenne stack.      */
specifier|protected
name|void
name|initCayenneStack
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|Configuration
name|cayenneConfig
init|=
operator|new
name|DefaultConfiguration
argument_list|(
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
decl_stmt|;
try|try
block|{
name|cayenneConfig
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|cayenneConfig
operator|.
name|didInitialize
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error starting Cayenne"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
comment|// TODO (Andrus 10/15/2005) this assumes that mapping has a single domain...
comment|// do something about multiple domains
name|this
operator|.
name|domain
operator|=
name|cayenneConfig
operator|.
name|getDomain
argument_list|()
expr_stmt|;
block|}
comment|/**      * Initializes EventBridge parameters for remote clients peer-to-peer communications.      */
specifier|protected
name|void
name|initEventBridgeParameters
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|String
name|eventBridgeFactoryName
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|BaseRemoteService
operator|.
name|EVENT_BRIDGE_FACTORY_PROPERTY
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
name|eventBridgeParameters
init|=
operator|new
name|HashMap
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|eventBridgeParameters
operator|.
name|remove
argument_list|(
name|BaseRemoteService
operator|.
name|EVENT_BRIDGE_FACTORY_PROPERTY
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

