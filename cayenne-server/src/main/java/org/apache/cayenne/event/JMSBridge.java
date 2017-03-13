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
name|util
operator|.
name|IDUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageFormatException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ObjectMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicPublisher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicSubscriber
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_comment
comment|/**  * Implementation of EventBridge that passes and receives events via JMS (Java Messaging  * Service). JMSBridge uses "publish/subscribe" model for communication with external  * agents.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|JMSBridge
extends|extends
name|EventBridge
implements|implements
name|MessageListener
block|{
comment|// this is an OpenJMS default for the factory name. Likely it won't work with
comment|// anything else
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CONNECTION_FACTORY_DEFAULT
init|=
literal|"JmsTopicConnectionFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CONNECTION_FACTORY_PROPERTY
init|=
literal|"cayenne.JMSBridge.topic.connection.factory"
decl_stmt|;
specifier|static
specifier|final
name|String
name|VM_ID
init|=
operator|new
name|String
argument_list|(
name|IDUtil
operator|.
name|pseudoUniqueByteSequence16
argument_list|()
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|VM_ID_PROPERTY
init|=
literal|"VM_ID"
decl_stmt|;
specifier|protected
name|String
name|topicConnectionFactoryName
decl_stmt|;
specifier|protected
name|TopicConnection
name|sendConnection
decl_stmt|;
specifier|protected
name|TopicSession
name|sendSession
decl_stmt|;
specifier|protected
name|TopicConnection
name|receivedConnection
decl_stmt|;
specifier|protected
name|TopicPublisher
name|publisher
decl_stmt|;
specifier|protected
name|TopicSubscriber
name|subscriber
decl_stmt|;
specifier|public
name|JMSBridge
parameter_list|(
name|EventSubject
name|localSubject
parameter_list|,
name|String
name|externalSubject
parameter_list|)
block|{
name|super
argument_list|(
name|localSubject
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|JMSBridge
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
block|}
comment|/**      * @since 4.0      */
specifier|public
name|JMSBridge
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
name|super
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
comment|// configure properties
name|String
name|topicConnectionFactory
init|=
name|properties
operator|.
name|get
argument_list|(
name|TOPIC_CONNECTION_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
name|this
operator|.
name|topicConnectionFactoryName
operator|=
operator|(
name|topicConnectionFactory
operator|!=
literal|null
operator|)
condition|?
name|topicConnectionFactory
else|:
name|TOPIC_CONNECTION_FACTORY_DEFAULT
expr_stmt|;
block|}
comment|/**      * JMS MessageListener implementation. Injects received events to the EventManager      * local event queue.      */
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|Object
name|vmID
init|=
name|message
operator|.
name|getObjectProperty
argument_list|(
name|JMSBridge
operator|.
name|VM_ID_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|JMSBridge
operator|.
name|VM_ID
operator|.
name|equals
argument_list|(
name|vmID
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
operator|(
name|message
operator|instanceof
name|ObjectMessage
operator|)
condition|)
block|{
return|return;
block|}
name|ObjectMessage
name|objectMessage
init|=
operator|(
name|ObjectMessage
operator|)
name|message
decl_stmt|;
name|CayenneEvent
name|event
init|=
name|messageObjectToEvent
argument_list|(
name|objectMessage
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
name|onExternalEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MessageFormatException
name|mfex
parameter_list|)
block|{
comment|// TODO: Andrus, 2/8/2006 logging... Log4J was removed to make this usable on
comment|// the client
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// TODO: Andrus, 2/8/2006 logging... Log4J was removed to make this usable on
comment|// the client
block|}
block|}
comment|/**      * @return Name of javax.jms.TopicConnectionFactory accessible via JNDI.      */
specifier|public
name|String
name|getTopicConnectionFactoryName
parameter_list|()
block|{
return|return
name|topicConnectionFactoryName
return|;
block|}
specifier|public
name|void
name|setTopicConnectionFactoryName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|topicConnectionFactoryName
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Starts up JMS machinery for "publish/subscribe" model.      */
annotation|@
name|Override
specifier|protected
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|jndiContext
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|TopicConnectionFactory
name|connectionFactory
init|=
operator|(
name|TopicConnectionFactory
operator|)
name|jndiContext
operator|.
name|lookup
argument_list|(
name|topicConnectionFactoryName
argument_list|)
decl_stmt|;
name|Topic
name|topic
init|=
literal|null
decl_stmt|;
try|try
block|{
name|topic
operator|=
operator|(
name|Topic
operator|)
name|jndiContext
operator|.
name|lookup
argument_list|(
name|externalSubject
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|ex
parameter_list|)
block|{
comment|// can't find topic, try to create it
name|topic
operator|=
name|topicNotFound
argument_list|(
name|jndiContext
argument_list|,
name|ex
argument_list|)
expr_stmt|;
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
comment|// config publisher
if|if
condition|(
name|receivesLocalEvents
argument_list|()
condition|)
block|{
name|this
operator|.
name|sendConnection
operator|=
name|connectionFactory
operator|.
name|createTopicConnection
argument_list|()
expr_stmt|;
name|this
operator|.
name|sendSession
operator|=
name|sendConnection
operator|.
name|createTopicSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
name|this
operator|.
name|publisher
operator|=
name|sendSession
operator|.
name|createPublisher
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
comment|// config subscriber
if|if
condition|(
name|receivesExternalEvents
argument_list|()
condition|)
block|{
name|this
operator|.
name|receivedConnection
operator|=
name|connectionFactory
operator|.
name|createTopicConnection
argument_list|()
expr_stmt|;
name|this
operator|.
name|subscriber
operator|=
name|receivedConnection
operator|.
name|createTopicSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
operator|.
name|createSubscriber
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|this
operator|.
name|subscriber
operator|.
name|setMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|receivedConnection
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Attempts to create missing Topic. Since Topic creation is JMS-implementation      * specific, this task is left to subclasses. Current implementation simply rethrows      * the exception.      */
specifier|protected
name|Topic
name|topicNotFound
parameter_list|(
name|Context
name|jndiContext
parameter_list|,
name|NamingException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
name|ex
throw|;
block|}
comment|/**      * Closes all resources used to communicate via JMS.      */
annotation|@
name|Override
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|lastException
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|publisher
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|publisher
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|lastException
operator|=
name|ex
expr_stmt|;
block|}
block|}
if|if
condition|(
name|subscriber
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|subscriber
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|lastException
operator|=
name|ex
expr_stmt|;
block|}
block|}
if|if
condition|(
name|receivedConnection
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|receivedConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|lastException
operator|=
name|ex
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sendSession
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sendSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|lastException
operator|=
name|ex
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sendConnection
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sendConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|lastException
operator|=
name|ex
expr_stmt|;
block|}
block|}
name|publisher
operator|=
literal|null
expr_stmt|;
name|subscriber
operator|=
literal|null
expr_stmt|;
name|receivedConnection
operator|=
literal|null
expr_stmt|;
name|sendConnection
operator|=
literal|null
expr_stmt|;
name|sendSession
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|lastException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|lastException
throw|;
block|}
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
name|ObjectMessage
name|message
init|=
name|sendSession
operator|.
name|createObjectMessage
argument_list|(
name|eventToMessageObject
argument_list|(
name|localEvent
argument_list|)
argument_list|)
decl_stmt|;
name|message
operator|.
name|setObjectProperty
argument_list|(
name|JMSBridge
operator|.
name|VM_ID_PROPERTY
argument_list|,
name|JMSBridge
operator|.
name|VM_ID
argument_list|)
expr_stmt|;
name|publisher
operator|.
name|publish
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Converts CayenneEvent to a serializable object that will be sent via JMS. Default      * implementation simply returns the event, but subclasses can customize this      * behavior.      */
specifier|protected
name|Serializable
name|eventToMessageObject
parameter_list|(
name|CayenneEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|event
return|;
block|}
comment|/**      * Converts a Serializable instance to CayenneEvent. Returns null if the object is not      * supported. Default implementation simply tries to cast the object to CayenneEvent,      * but subclasses can customize this behavior.      */
specifier|protected
name|CayenneEvent
name|messageObjectToEvent
parameter_list|(
name|Serializable
name|object
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|object
operator|instanceof
name|CayenneEvent
operator|)
condition|?
operator|(
name|CayenneEvent
operator|)
name|object
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

