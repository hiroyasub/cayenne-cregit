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
name|event
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|GroupChat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|PacketListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|SSLXMPPConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Packet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Base64
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
name|Collections
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
comment|/**  * An EventBridge implementation based on XMPP protocol and Smack XMPP client library.  * What's good about XMPP (Extensible Messaging and Presence Protocol, an IETF standard  * protocol that grew up from Jabber IM) is that generally it has fewer or no deployment  * limitations (unlike JMS and JGroups that are generally a good solution for local  * controlled networks). Also it provides lots of additional information for free, such as  * presence, and much more.  *<p>  * This implementation is based on Smack XMPP client library from JiveSoftware.  *</p>  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|XMPPBridge
extends|extends
name|EventBridge
block|{
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_HOST_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppHost"
decl_stmt|;
comment|/**      * An optional property, port 5222 is used as default XMPP port.      */
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_PORT_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppPort"
decl_stmt|;
comment|/**      * An optional property, "conference" is used as default chat service.      */
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_CHAT_SERVICE_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppChatService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_SECURE_CONNECTION_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppSecure"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_LOGIN_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppLogin"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XMPP_PASSWORD_PROPERTY
init|=
literal|"cayenne.XMPPBridge.xmppPassword"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_CHAT_SERVICE
init|=
literal|"conference"
decl_stmt|;
specifier|static
specifier|final
name|int
name|DEFAULT_XMPP_PORT
init|=
literal|5222
decl_stmt|;
specifier|static
specifier|final
name|int
name|DEFAULT_XMPP_SECURE_PORT
init|=
literal|5223
decl_stmt|;
specifier|protected
name|boolean
name|secureConnection
decl_stmt|;
specifier|protected
name|String
name|loginId
decl_stmt|;
specifier|protected
name|String
name|password
decl_stmt|;
specifier|protected
name|String
name|xmppHost
decl_stmt|;
specifier|protected
name|int
name|xmppPort
decl_stmt|;
specifier|protected
name|String
name|chatService
decl_stmt|;
specifier|protected
name|String
name|sessionHandle
decl_stmt|;
specifier|protected
name|XMPPConnection
name|connection
decl_stmt|;
specifier|protected
name|GroupChat
name|groupChat
decl_stmt|;
specifier|protected
name|boolean
name|connected
decl_stmt|;
comment|/**      * Creates an XMPPBridge. External subject will be used as the chat group name.      */
specifier|public
name|XMPPBridge
parameter_list|(
name|EventSubject
name|localSubject
parameter_list|,
name|String
name|externalSubject
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|localSubject
argument_list|)
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates an XMPPBridge. External subject will be used as the chat group name.      */
specifier|public
name|XMPPBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|)
block|{
name|super
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
comment|// generate a unique session handle... users can override it to use a specific
comment|// handle...
name|this
operator|.
name|sessionHandle
operator|=
literal|"cayenne-xmpp-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XMPPBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
name|this
operator|.
name|chatService
operator|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_CHAT_SERVICE_PROPERTY
argument_list|)
expr_stmt|;
name|this
operator|.
name|xmppHost
operator|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_HOST_PROPERTY
argument_list|)
expr_stmt|;
name|this
operator|.
name|loginId
operator|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_LOGIN_PROPERTY
argument_list|)
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_PASSWORD_PROPERTY
argument_list|)
expr_stmt|;
name|String
name|secureConnectionString
init|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_SECURE_CONNECTION_PROPERTY
argument_list|)
decl_stmt|;
name|secureConnection
operator|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|secureConnectionString
argument_list|)
expr_stmt|;
name|String
name|portString
init|=
name|properties
operator|.
name|get
argument_list|(
name|XMPP_PORT_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|portString
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|xmppPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid port: %s"
argument_list|,
name|portString
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|String
name|getXmppHost
parameter_list|()
block|{
return|return
name|xmppHost
return|;
block|}
specifier|public
name|void
name|setXmppHost
parameter_list|(
name|String
name|xmppHost
parameter_list|)
block|{
name|this
operator|.
name|xmppHost
operator|=
name|xmppHost
expr_stmt|;
block|}
specifier|public
name|int
name|getXmppPort
parameter_list|()
block|{
return|return
name|xmppPort
return|;
block|}
specifier|public
name|void
name|setXmppPort
parameter_list|(
name|int
name|xmppPort
parameter_list|)
block|{
name|this
operator|.
name|xmppPort
operator|=
name|xmppPort
expr_stmt|;
block|}
specifier|public
name|String
name|getLoginId
parameter_list|()
block|{
return|return
name|loginId
return|;
block|}
specifier|public
name|void
name|setLoginId
parameter_list|(
name|String
name|loginId
parameter_list|)
block|{
name|this
operator|.
name|loginId
operator|=
name|loginId
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSecureConnection
parameter_list|()
block|{
return|return
name|secureConnection
return|;
block|}
specifier|public
name|void
name|setSecureConnection
parameter_list|(
name|boolean
name|secureConnection
parameter_list|)
block|{
name|this
operator|.
name|secureConnection
operator|=
name|secureConnection
expr_stmt|;
block|}
specifier|public
name|String
name|getChatService
parameter_list|()
block|{
return|return
name|chatService
return|;
block|}
specifier|public
name|void
name|setChatService
parameter_list|(
name|String
name|chatService
parameter_list|)
block|{
name|this
operator|.
name|chatService
operator|=
name|chatService
expr_stmt|;
block|}
specifier|public
name|String
name|getSessionHandle
parameter_list|()
block|{
return|return
name|sessionHandle
return|;
block|}
specifier|public
name|void
name|setSessionHandle
parameter_list|(
name|String
name|sessionHandle
parameter_list|)
block|{
name|this
operator|.
name|sessionHandle
operator|=
name|sessionHandle
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
block|{
comment|// validate settings
if|if
condition|(
name|xmppHost
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null 'xmppHost', can't start XMPPBridge"
argument_list|)
throw|;
block|}
comment|// shutdown old bridge
if|if
condition|(
name|connected
condition|)
block|{
name|shutdownExternal
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// connect and log in to chat
if|if
condition|(
name|secureConnection
condition|)
block|{
name|int
name|port
init|=
name|xmppPort
operator|>
literal|0
condition|?
name|xmppPort
else|:
name|DEFAULT_XMPP_SECURE_PORT
decl_stmt|;
name|this
operator|.
name|connection
operator|=
operator|new
name|SSLXMPPConnection
argument_list|(
name|xmppHost
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|port
init|=
name|xmppPort
operator|>
literal|0
condition|?
name|xmppPort
else|:
name|DEFAULT_XMPP_PORT
decl_stmt|;
name|this
operator|.
name|connection
operator|=
operator|new
name|XMPPConnection
argument_list|(
name|xmppHost
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|loginId
operator|!=
literal|null
condition|)
block|{
comment|// it is important to provide a (pseudo) globally unique string as the
comment|// third argument ("sessionHandle" is such string); without it same
comment|// loginId can not be reused from the same machine.
name|connection
operator|.
name|login
argument_list|(
name|loginId
argument_list|,
name|password
argument_list|,
name|sessionHandle
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|.
name|loginAnonymously
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMPPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error connecting to XMPP Server: %s"
argument_list|,
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|service
init|=
name|chatService
operator|!=
literal|null
condition|?
name|chatService
else|:
name|DEFAULT_CHAT_SERVICE
decl_stmt|;
try|try
block|{
name|groupChat
operator|=
name|connection
operator|.
name|createGroupChat
argument_list|(
name|externalSubject
operator|+
literal|'@'
operator|+
name|service
operator|+
literal|"."
operator|+
name|connection
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|groupChat
operator|.
name|join
argument_list|(
name|sessionHandle
argument_list|)
expr_stmt|;
name|groupChat
operator|.
name|addMessageListener
argument_list|(
operator|new
name|XMPPListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMPPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error setting up a group chat: %s"
argument_list|,
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|connected
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|groupChat
operator|!=
literal|null
condition|)
block|{
name|groupChat
operator|.
name|leave
argument_list|()
expr_stmt|;
name|groupChat
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
name|connection
operator|=
literal|null
expr_stmt|;
block|}
name|connected
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|sendExternalEvent
parameter_list|(
name|CayenneEvent
name|localEvent
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
name|groupChat
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|serializeToString
argument_list|(
name|localEvent
argument_list|)
argument_list|)
expr_stmt|;
comment|// set thread to our session handle to be able to discard messages from self
name|message
operator|.
name|setThread
argument_list|(
name|sessionHandle
argument_list|)
expr_stmt|;
name|groupChat
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
class|class
name|XMPPListener
implements|implements
name|PacketListener
block|{
specifier|public
name|void
name|processPacket
parameter_list|(
name|Packet
name|packet
parameter_list|)
block|{
if|if
condition|(
name|packet
operator|instanceof
name|Message
condition|)
block|{
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|packet
decl_stmt|;
comment|// filter our own messages
if|if
condition|(
name|sessionHandle
operator|.
name|equals
argument_list|(
name|message
operator|.
name|getThread
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|payload
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|event
init|=
name|deserializeFromString
argument_list|(
name|payload
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|instanceof
name|CayenneEvent
condition|)
block|{
name|onExternalEvent
argument_list|(
operator|(
name|CayenneEvent
operator|)
name|event
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore for now... need to add logging.
block|}
block|}
block|}
block|}
block|}
comment|/**      * Decodes the String (assuming it is using Base64 encoding), and then deserializes      * object from the byte array.      */
specifier|static
name|Object
name|deserializeFromString
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|string
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|byte
index|[]
name|bytes
init|=
name|Base64
operator|.
name|getDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|string
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|in
init|=
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|in
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|object
return|;
block|}
comment|/**      * Serializes object and then encodes it using Base64 encoding.      */
specifier|static
name|String
name|serializeToString
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|out
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|Base64
operator|.
name|getEncoder
argument_list|()
operator|.
name|encodeToString
argument_list|(
name|bytes
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

