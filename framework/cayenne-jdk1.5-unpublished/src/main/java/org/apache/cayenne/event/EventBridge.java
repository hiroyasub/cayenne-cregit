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
name|util
operator|.
name|ArrayList
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
name|EventListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_comment
comment|/**  * An object that passes events between a local EventManager and some other event dispatch  * mechanism. The most common example is sending local events to remote JVMs and receiving  * remote events dispatched by those VMs. EventBridge makes possible to connect a network  * of regular EventManagers in a single "virtual" distributed EventManager.  *</p>  *<p>  * EventBridge encapsulates a transport agreed upon by all paries (such as JMS) and  * maintains an array of "local" subjects to communicate with local EventManager, and a  * single "remote" subject - to use for "external" communications that are  * transport-specific.  *<p>  * Subclasses that require special setup to listen for external events should implement  *<code>startupExternal()</code> method accordingly.  *</p>  *<p>  * This class is an example of<a  * href="http://en.wikipedia.org/wiki/Bridge_pattern">"bridge" design pattern</a>, hence  * the name.  *</p>  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_comment
comment|// TODO Andrus, 10/15/2005 - potentially big inefficiency of concrete implementations of
end_comment

begin_comment
comment|// EventBridgeFactory is that all the expensive resources are managed by the bridge
end_comment

begin_comment
comment|// itself. Scaling to a big number of bridge instances would require resource pooling to
end_comment

begin_comment
comment|// be done by the factory singleton.
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|EventBridge
implements|implements
name|EventListener
block|{
specifier|public
specifier|static
specifier|final
name|int
name|RECEIVE_LOCAL
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|RECEIVE_EXTERNAL
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|RECEIVE_LOCAL_EXTERNAL
init|=
literal|3
decl_stmt|;
specifier|protected
name|String
name|externalSubject
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
decl_stmt|;
specifier|protected
name|EventManager
name|eventManager
decl_stmt|;
specifier|protected
name|int
name|mode
decl_stmt|;
specifier|protected
name|Object
name|externalEventSource
decl_stmt|;
comment|// keeps all listeners so that they are not deallocated
name|Collection
argument_list|<
name|SubjectListener
argument_list|>
name|listeners
decl_stmt|;
comment|/**      * A utility method that performs consistent translation from an EventSubject to a      * String that can be used by external transport as subject for distributed      * communications. Substitutes all chars that can be incorrectly interpreted by      * whoever (JNDI, ...?).      */
specifier|public
specifier|static
name|String
name|convertToExternalSubject
parameter_list|(
name|EventSubject
name|localSubject
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
name|localSubject
operator|.
name|getSubjectName
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|chars
index|[
name|i
index|]
operator|==
literal|'/'
operator|||
name|chars
index|[
name|i
index|]
operator|==
literal|'.'
condition|)
block|{
name|chars
index|[
name|i
index|]
operator|=
literal|'_'
expr_stmt|;
block|}
block|}
return|return
operator|new
name|String
argument_list|(
name|chars
argument_list|)
return|;
block|}
comment|/**      * Creates an EventBridge with a single local subject.      */
specifier|public
name|EventBridge
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
comment|/**      * Creates an EventBridge with multiple local subjects and a single external subject.      *       * @since 1.2      */
specifier|public
name|EventBridge
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
name|this
operator|.
name|localSubjects
operator|=
operator|new
name|HashSet
argument_list|<
name|EventSubject
argument_list|>
argument_list|(
name|localSubjects
argument_list|)
expr_stmt|;
name|this
operator|.
name|externalSubject
operator|=
name|externalSubject
expr_stmt|;
block|}
comment|/**      * Returns a String subject used to post distributed events.      */
specifier|public
name|String
name|getExternalSubject
parameter_list|()
block|{
return|return
name|externalSubject
return|;
block|}
comment|/**      * Returns true if this bridge is active.      *       * @since 1.2      */
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|eventManager
operator|!=
literal|null
return|;
block|}
comment|/**      * Returns a Collection of local EventSubjects.      *       * @since 1.2      */
specifier|public
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|getLocalSubjects
parameter_list|()
block|{
return|return
name|localSubjects
return|;
block|}
comment|/**      * Returns local EventManager used by the bridge. Returned value will be null before      * the bridge is started and after it is shutdown.      *       * @since 1.2      */
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|eventManager
return|;
block|}
comment|/**      * Returns an object used as a source of local events posted in response to remote      * events. If externalEventSource wasn't setup during bridge startup (or if the bridge      * is not started), returns this object.      *       * @since 1.2      */
specifier|public
name|Object
name|getExternalEventSource
parameter_list|()
block|{
return|return
name|externalEventSource
operator|!=
literal|null
condition|?
name|externalEventSource
else|:
name|this
return|;
block|}
comment|/**      * Returns true if the bridge is configured to receive local events from its internal      * EventManager.      */
specifier|public
name|boolean
name|receivesLocalEvents
parameter_list|()
block|{
return|return
name|mode
operator|==
name|RECEIVE_LOCAL_EXTERNAL
operator|||
name|mode
operator|==
name|RECEIVE_LOCAL
return|;
block|}
comment|/**      * Returns true if the bridge is configured to receive external events.      */
specifier|public
name|boolean
name|receivesExternalEvents
parameter_list|()
block|{
return|return
name|mode
operator|==
name|RECEIVE_LOCAL_EXTERNAL
operator|||
name|mode
operator|==
name|RECEIVE_EXTERNAL
return|;
block|}
comment|/**      * Starts EventBridge in the specified mode and locally listening to all event sources      * that post on a preconfigured subject. Remote events reposted locally will have this      * EventBridge as their source.      *       * @param eventManager EventManager used to send and receive local events.      * @param mode One of the possible modes of operation - RECEIVE_EXTERNAL,      *            RECEIVE_LOCAL, RECEIVE_LOCAL_EXTERNAL.      */
specifier|public
name|void
name|startup
parameter_list|(
name|EventManager
name|eventManager
parameter_list|,
name|int
name|mode
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|startup
argument_list|(
name|eventManager
argument_list|,
name|mode
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts EventBridge in the specified mode and locally listening to a specified event      * source. Remote events reposted locally will have this EventBridge as their source.      *       * @param eventManager EventManager used to send and receive local events.      * @param mode One of the possible modes of operation - RECEIVE_EXTERNAL,      *            RECEIVE_LOCAL, RECEIVE_LOCAL_EXTERNAL.      * @param localEventSource If not null, only events originating from localEventSource      *            object will be processed by this bridge.      */
specifier|public
name|void
name|startup
parameter_list|(
name|EventManager
name|eventManager
parameter_list|,
name|int
name|mode
parameter_list|,
name|Object
name|localEventSource
parameter_list|)
throws|throws
name|Exception
block|{
name|startup
argument_list|(
name|eventManager
argument_list|,
name|mode
argument_list|,
name|localEventSource
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts EventBridge in the specified mode.      *       * @param eventManager EventManager used to send and receive local events.      * @param mode One of the possible modes of operation - RECEIVE_EXTERNAL,      *            RECEIVE_LOCAL, RECEIVE_LOCAL_EXTERNAL.      * @param localEventSource If not null, only events originating from localEventSource      *            object will be processed by this bridge.      * @param remoteEventSource If not null, remoteEventSource object will be used as      *            standby source of local events posted by this EventBridge in response to      *            remote events.      * @since 1.2      */
specifier|public
name|void
name|startup
parameter_list|(
name|EventManager
name|eventManager
parameter_list|,
name|int
name|mode
parameter_list|,
name|Object
name|localEventSource
parameter_list|,
name|Object
name|remoteEventSource
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|eventManager
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'eventManager' can't be null."
argument_list|)
throw|;
block|}
comment|// uninstall old event manager
if|if
condition|(
name|this
operator|.
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|externalEventSource
operator|=
name|remoteEventSource
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
if|if
condition|(
name|receivesLocalEvents
argument_list|()
operator|&&
operator|!
name|localSubjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|SubjectListener
argument_list|>
argument_list|(
name|localSubjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|EventSubject
name|subject
range|:
name|localSubjects
control|)
block|{
name|SubjectListener
name|listener
init|=
operator|new
name|SubjectListener
argument_list|(
name|subject
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addNonBlockingListener
argument_list|(
name|listener
argument_list|,
literal|"onLocalEvent"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|localEventSource
argument_list|)
expr_stmt|;
block|}
block|}
name|startupExternal
argument_list|()
expr_stmt|;
block|}
comment|/**      * Starts an external interface of the EventBridge.      */
specifier|protected
specifier|abstract
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops listening for events on both local and external interfaces.      */
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|externalEventSource
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|listeners
operator|!=
literal|null
operator|&&
name|eventManager
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SubjectListener
name|listener
range|:
name|listeners
control|)
block|{
name|eventManager
operator|.
name|removeListener
argument_list|(
name|listener
argument_list|,
name|listener
operator|.
name|subject
argument_list|)
expr_stmt|;
block|}
name|eventManager
operator|=
literal|null
expr_stmt|;
name|listeners
operator|=
literal|null
expr_stmt|;
block|}
name|shutdownExternal
argument_list|()
expr_stmt|;
block|}
comment|/**      * Shuts down the external interface of the EventBridge, cleaning up and releasing any      * resources used to communicate external events.      */
specifier|protected
specifier|abstract
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Helper method intended to be called explicitly by subclasses to asynchronously post      * an event obtained from a remote source. Subclasses do not have to use this method,      * but they probably should for consistency.      */
specifier|protected
name|void
name|onExternalEvent
parameter_list|(
name|CayenneEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|EventSubject
name|localSubject
init|=
name|event
operator|.
name|getSubject
argument_list|()
decl_stmt|;
comment|// check for valid subject
if|if
condition|(
name|localSubject
operator|==
literal|null
operator|||
operator|!
name|localSubjects
operator|.
name|contains
argument_list|(
name|localSubject
argument_list|)
condition|)
block|{
return|return;
block|}
name|event
operator|.
name|setSource
argument_list|(
name|getExternalEventSource
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setPostedBy
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// inject external eveny to the event manager queue.. leave it up to the
comment|// listeners to figure out correct synchronization.
name|eventManager
operator|.
name|postEvent
argument_list|(
name|event
argument_list|,
name|localSubject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't post events. EventBridge was not started properly. "
operator|+
literal|"EventManager is null."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sends a Cayenne event over the transport supported by this bridge.      */
specifier|protected
specifier|abstract
name|void
name|sendExternalEvent
parameter_list|(
name|CayenneEvent
name|localEvent
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|final
class|class
name|SubjectListener
block|{
name|EventSubject
name|subject
decl_stmt|;
name|SubjectListener
parameter_list|(
name|EventSubject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
name|void
name|onLocalEvent
parameter_list|(
name|CayenneEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ignore events posted by this Bridge...
if|if
condition|(
name|event
operator|.
name|getSource
argument_list|()
operator|!=
name|getExternalEventSource
argument_list|()
operator|&&
name|event
operator|.
name|getPostedBy
argument_list|()
operator|!=
name|EventBridge
operator|.
name|this
condition|)
block|{
comment|// make sure external event has the right subject, if not make a clone
comment|// with the right one...
if|if
condition|(
operator|!
name|subject
operator|.
name|equals
argument_list|(
name|event
operator|.
name|getSubject
argument_list|()
argument_list|)
condition|)
block|{
name|CayenneEvent
name|clone
init|=
operator|(
name|CayenneEvent
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|clone
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setPostedBy
argument_list|(
name|event
operator|.
name|getPostedBy
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setSource
argument_list|(
name|event
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|=
name|clone
expr_stmt|;
block|}
name|sendExternalEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

