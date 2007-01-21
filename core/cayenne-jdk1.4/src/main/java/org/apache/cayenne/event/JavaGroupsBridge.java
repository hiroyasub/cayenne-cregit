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
name|org
operator|.
name|jgroups
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|JChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|blocks
operator|.
name|PullPushAdapter
import|;
end_import

begin_comment
comment|/**  * Implementation of EventBridge that passes and receives events via JavaGroups  * communication software.  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|JavaGroupsBridge
extends|extends
name|EventBridge
implements|implements
name|MessageListener
block|{
comment|// TODO: Meaning of "state" in JGroups is not yet clear to me
specifier|protected
name|byte
index|[]
name|state
decl_stmt|;
specifier|protected
name|Channel
name|channel
decl_stmt|;
specifier|protected
name|PullPushAdapter
name|adapter
decl_stmt|;
specifier|protected
name|String
name|multicastAddress
decl_stmt|;
specifier|protected
name|String
name|multicastPort
decl_stmt|;
specifier|protected
name|String
name|configURL
decl_stmt|;
comment|/**      * Creates new instance of JavaGroupsBridge.      */
specifier|public
name|JavaGroupsBridge
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
name|JavaGroupsBridge
parameter_list|(
name|Collection
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
specifier|public
name|String
name|getConfigURL
parameter_list|()
block|{
return|return
name|configURL
return|;
block|}
specifier|public
name|void
name|setConfigURL
parameter_list|(
name|String
name|configURL
parameter_list|)
block|{
name|this
operator|.
name|configURL
operator|=
name|configURL
expr_stmt|;
block|}
specifier|public
name|String
name|getMulticastAddress
parameter_list|()
block|{
return|return
name|multicastAddress
return|;
block|}
specifier|public
name|void
name|setMulticastAddress
parameter_list|(
name|String
name|multicastAddress
parameter_list|)
block|{
name|this
operator|.
name|multicastAddress
operator|=
name|multicastAddress
expr_stmt|;
block|}
specifier|public
name|String
name|getMulticastPort
parameter_list|()
block|{
return|return
name|multicastPort
return|;
block|}
specifier|public
name|void
name|setMulticastPort
parameter_list|(
name|String
name|multicastPort
parameter_list|)
block|{
name|this
operator|.
name|multicastPort
operator|=
name|multicastPort
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|void
name|setState
parameter_list|(
name|byte
index|[]
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
comment|/**      * Implementation of org.javagroups.MessageListener - a callback method to process      * incoming messages.      */
specifier|public
name|void
name|receive
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|CayenneEvent
name|event
init|=
name|messageObjectToEvent
argument_list|(
operator|(
name|Serializable
operator|)
name|message
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
name|Exception
name|ex
parameter_list|)
block|{
comment|// TODO: Andrus, 2/8/2006 logging... Log4J was removed to make this usable on
comment|// the client
block|}
block|}
specifier|protected
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: need to do more research to figure out the best default transport
comment|// settings
comment|// to avoid fragmentation, etc.
comment|// if config file is set, use it, otherwise use a default
comment|// set of properties, trying to configure multicast address and port
if|if
condition|(
name|configURL
operator|!=
literal|null
condition|)
block|{
name|channel
operator|=
operator|new
name|JChannel
argument_list|(
name|configURL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|configString
init|=
name|buildConfigString
argument_list|()
decl_stmt|;
name|channel
operator|=
operator|new
name|JChannel
argument_list|(
name|configString
argument_list|)
expr_stmt|;
block|}
comment|// Important - discard messages from self
name|channel
operator|.
name|setOpt
argument_list|(
name|Channel
operator|.
name|LOCAL
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|(
name|externalSubject
argument_list|)
expr_stmt|;
if|if
condition|(
name|receivesExternalEvents
argument_list|()
condition|)
block|{
name|adapter
operator|=
operator|new
name|PullPushAdapter
argument_list|(
name|channel
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates JavaGroups configuration String, using preconfigured multicast port and      * address.      */
specifier|protected
name|String
name|buildConfigString
parameter_list|()
block|{
if|if
condition|(
name|multicastAddress
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"'multcastAddress' is not set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|multicastPort
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"'multcastPort' is not set"
argument_list|)
throw|;
block|}
return|return
literal|"UDP(mcast_addr="
operator|+
name|multicastAddress
operator|+
literal|";mcast_port="
operator|+
name|multicastPort
operator|+
literal|";ip_ttl=32):"
operator|+
literal|"PING(timeout=3000;num_initial_members=6):"
operator|+
literal|"FD(timeout=3000):"
operator|+
literal|"VERIFY_SUSPECT(timeout=1500):"
operator|+
literal|"pbcast.NAKACK(gc_lag=10;retransmit_timeout=600,1200,2400,4800):"
operator|+
literal|"pbcast.STABLE(desired_avg_gossip=10000):"
operator|+
literal|"FRAG:"
operator|+
literal|"pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;"
operator|+
literal|"shun=true;print_local_addr=false)"
return|;
block|}
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|adapter
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|channel
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|adapter
operator|=
literal|null
expr_stmt|;
name|channel
operator|=
literal|null
expr_stmt|;
block|}
block|}
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
operator|new
name|Message
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|eventToMessageObject
argument_list|(
name|localEvent
argument_list|)
argument_list|)
decl_stmt|;
name|channel
operator|.
name|send
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

