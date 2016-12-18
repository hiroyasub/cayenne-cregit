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
name|di
operator|.
name|BeforeScopeEnd
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
name|Invocation
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|WeakHashMap
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link EventManager}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultEventManager
implements|implements
name|EventManager
block|{
specifier|static
specifier|final
name|int
name|DEFAULT_DISPATCH_THREAD_COUNT
init|=
literal|5
decl_stmt|;
comment|// keeps weak references to subjects
specifier|protected
name|Map
argument_list|<
name|EventSubject
argument_list|,
name|DispatchQueue
argument_list|>
name|subjects
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Dispatch
argument_list|>
name|eventQueue
decl_stmt|;
specifier|protected
name|boolean
name|singleThread
decl_stmt|;
specifier|protected
specifier|volatile
name|boolean
name|stopped
decl_stmt|;
name|List
argument_list|<
name|DispatchThread
argument_list|>
name|dispatchThreads
decl_stmt|;
comment|/**      * Creates a multithreaded EventManager using default thread count.      */
specifier|public
name|DefaultEventManager
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_DISPATCH_THREAD_COUNT
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates an EventManager starting the specified number of threads for multithreaded      * dispatching. To create a single-threaded EventManager, use thread count of zero or      * less.      */
specifier|public
name|DefaultEventManager
parameter_list|(
name|int
name|dispatchThreadCount
parameter_list|)
block|{
name|this
operator|.
name|subjects
operator|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|WeakHashMap
argument_list|<
name|EventSubject
argument_list|,
name|DispatchQueue
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventQueue
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|LinkedList
argument_list|<
name|Dispatch
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleThread
operator|=
name|dispatchThreadCount
operator|<=
literal|0
expr_stmt|;
if|if
condition|(
operator|!
name|singleThread
condition|)
block|{
name|dispatchThreads
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dispatchThreadCount
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
literal|"cayenne-edt-"
operator|+
name|hashCode
argument_list|()
operator|+
literal|"-"
decl_stmt|;
comment|// start dispatch threads
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dispatchThreadCount
condition|;
name|i
operator|++
control|)
block|{
name|DispatchThread
name|thread
init|=
operator|new
name|DispatchThread
argument_list|(
name|prefix
operator|+
name|i
argument_list|)
decl_stmt|;
name|dispatchThreads
operator|.
name|add
argument_list|(
name|thread
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|dispatchThreads
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if the EventManager was stopped via {@link #shutdown()} method.      *       * @since 3.1      */
specifier|public
name|boolean
name|isStopped
parameter_list|()
block|{
return|return
name|stopped
return|;
block|}
comment|/**      * Returns true if this EventManager is single-threaded. If so it will throw an      * exception on any attempt to register an unblocking listener or dispatch a      * non-blocking event.      *       * @since 1.2      */
specifier|public
name|boolean
name|isSingleThreaded
parameter_list|()
block|{
return|return
name|singleThread
return|;
block|}
comment|/**      * Stops event threads. After the EventManager is stopped, it can not be restarted and      * should be discarded.      *       * @since 3.0      */
annotation|@
name|BeforeScopeEnd
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
operator|!
name|stopped
condition|)
block|{
name|this
operator|.
name|stopped
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|DispatchThread
name|thread
range|:
name|dispatchThreads
control|)
block|{
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|dispatchThreads
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Register an<code>EventListener</code> for events sent by any sender.      *       * @throws RuntimeException if<code>methodName</code> is not found      * @see #addListener(Object, String, Class, EventSubject, Object)      */
specifier|public
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|,
name|subject
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNonBlockingListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|singleThread
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"DefaultEventManager is configured to be single-threaded."
argument_list|)
throw|;
block|}
name|this
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|,
name|subject
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Register an<code>EventListener</code> for events sent by a specific sender.      *       * @param listener the object to be notified about events      * @param methodName the name of the listener method to be invoked      * @param eventParameterClass the class of the single event argument passed to      *<code>methodName</code>      * @param subject the event subject that the listener is interested in      * @param sender the object whose events the listener is interested in;      *<code>null</code> means 'any sender'.      * @throws RuntimeException if<code>methodName</code> is not found      */
specifier|public
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
name|addListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|,
name|subject
argument_list|,
name|sender
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNonBlockingListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
if|if
condition|(
name|singleThread
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"DefaultEventManager is configured to be single-threaded."
argument_list|)
throw|;
block|}
name|addListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|,
name|subject
argument_list|,
name|sender
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|,
name|boolean
name|blocking
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Listener must not be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|eventParameterClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Event class must not be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Subject must not be null."
argument_list|)
throw|;
block|}
try|try
block|{
name|Invocation
name|invocation
init|=
operator|(
name|blocking
operator|)
condition|?
operator|new
name|Invocation
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|)
else|:
operator|new
name|NonBlockingInvocation
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|eventParameterClass
argument_list|)
decl_stmt|;
name|dispatchQueueForSubject
argument_list|(
name|subject
argument_list|,
literal|true
argument_list|)
operator|.
name|addInvocation
argument_list|(
name|invocation
argument_list|,
name|sender
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|nsm
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error adding listener, method name: "
operator|+
name|methodName
argument_list|,
name|nsm
argument_list|)
throw|;
block|}
block|}
comment|/**      * Unregister the specified listener from all event subjects handled by this manager      * instance.      *       * @param listener the object to be unregistered      * @return<code>true</code> if<code>listener</code> could be removed for any      *         existing subjects, else returns<code>false</code>.      */
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|didRemove
init|=
literal|false
decl_stmt|;
synchronized|synchronized
init|(
name|subjects
init|)
block|{
if|if
condition|(
operator|!
name|subjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|EventSubject
name|subject
range|:
name|subjects
operator|.
name|keySet
argument_list|()
control|)
block|{
name|didRemove
operator||=
name|this
operator|.
name|removeListener
argument_list|(
name|listener
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|didRemove
return|;
block|}
comment|/**      * Removes all listeners for a given subject.      */
specifier|public
name|boolean
name|removeAllListeners
parameter_list|(
name|EventSubject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|subjects
init|)
block|{
return|return
name|subjects
operator|.
name|remove
argument_list|(
name|subject
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Unregister the specified listener for the events about the given subject.      *       * @param listener the object to be unregistered      * @param subject the subject from which the listener is to be unregistered      * @return<code>true</code> if<code>listener</code> could be removed for the given      *         subject, else returns<code>false</code>.      */
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
return|return
name|this
operator|.
name|removeListener
argument_list|(
name|listener
argument_list|,
name|subject
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Unregister the specified listener for the events about the given subject and the      * given sender.      *       * @param listener the object to be unregistered      * @param subject the subject from which the listener is to be unregistered      * @param sender the object whose events the listener was interested in;      *<code>null</code> means 'any sender'.      * @return<code>true</code> if<code>listener</code> could be removed for the given      *         subject, else returns<code>false</code>.      */
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
operator|||
name|subject
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DispatchQueue
name|subjectQueue
init|=
name|dispatchQueueForSubject
argument_list|(
name|subject
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectQueue
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|subjectQueue
operator|.
name|removeInvocations
argument_list|(
name|listener
argument_list|,
name|sender
argument_list|)
return|;
block|}
comment|/**      * Sends an event to all registered objects about a particular subject. Event is sent      * synchronously, so the sender thread is blocked until all the listeners finish      * processing the event.      *       * @param event the event to be posted to the observers      * @param subject the subject about which observers will be notified      * @throws IllegalArgumentException if event or subject are null      */
specifier|public
name|void
name|postEvent
parameter_list|(
name|EventObject
name|event
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
name|dispatchEvent
argument_list|(
operator|new
name|Dispatch
argument_list|(
name|event
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends an event to all registered objects about a particular subject. Event is      * queued by EventManager, releasing the sender thread, and is later dispatched in a      * separate thread.      *       * @param event the event to be posted to the observers      * @param subject the subject about which observers will be notified      * @throws IllegalArgumentException if event or subject are null      * @since 1.1      */
specifier|public
name|void
name|postNonBlockingEvent
parameter_list|(
name|EventObject
name|event
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|singleThread
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EventManager is configured to be single-threaded."
argument_list|)
throw|;
block|}
comment|// add dispatch to the queue and return
synchronized|synchronized
init|(
name|eventQueue
init|)
block|{
name|eventQueue
operator|.
name|add
argument_list|(
operator|new
name|Dispatch
argument_list|(
name|event
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
name|eventQueue
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dispatchEvent
parameter_list|(
name|Dispatch
name|dispatch
parameter_list|)
block|{
name|DispatchQueue
name|dispatchQueue
init|=
name|dispatchQueueForSubject
argument_list|(
name|dispatch
operator|.
name|subject
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|dispatchQueue
operator|!=
literal|null
condition|)
block|{
name|dispatchQueue
operator|.
name|dispatchEvent
argument_list|(
name|dispatch
argument_list|)
expr_stmt|;
block|}
block|}
comment|// returns a subject's mapping from senders to registered listener invocations
specifier|private
name|DispatchQueue
name|dispatchQueueForSubject
parameter_list|(
name|EventSubject
name|subject
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
synchronized|synchronized
init|(
name|subjects
init|)
block|{
name|DispatchQueue
name|listenersStore
init|=
name|subjects
operator|.
name|get
argument_list|(
name|subject
argument_list|)
decl_stmt|;
if|if
condition|(
name|create
operator|&&
name|listenersStore
operator|==
literal|null
condition|)
block|{
name|listenersStore
operator|=
operator|new
name|DispatchQueue
argument_list|()
expr_stmt|;
name|subjects
operator|.
name|put
argument_list|(
name|subject
argument_list|,
name|listenersStore
argument_list|)
expr_stmt|;
block|}
return|return
name|listenersStore
return|;
block|}
block|}
comment|// represents a posted event
class|class
name|Dispatch
block|{
name|EventObject
index|[]
name|eventArgument
decl_stmt|;
name|EventSubject
name|subject
decl_stmt|;
name|Dispatch
parameter_list|(
name|EventObject
name|event
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|EventObject
index|[]
block|{
name|event
block|}
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
name|Dispatch
parameter_list|(
name|EventObject
index|[]
name|eventArgument
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|eventArgument
operator|=
name|eventArgument
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
name|Object
name|getSender
parameter_list|()
block|{
return|return
name|eventArgument
index|[
literal|0
index|]
operator|.
name|getSource
argument_list|()
return|;
block|}
name|void
name|fire
parameter_list|()
block|{
name|DefaultEventManager
operator|.
name|this
operator|.
name|dispatchEvent
argument_list|(
name|Dispatch
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
name|boolean
name|fire
parameter_list|(
name|Invocation
name|invocation
parameter_list|)
block|{
if|if
condition|(
name|invocation
operator|instanceof
name|NonBlockingInvocation
condition|)
block|{
comment|// do minimal checks first...
if|if
condition|(
name|invocation
operator|.
name|getTarget
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// inject single invocation dispatch into the queue
synchronized|synchronized
init|(
name|eventQueue
init|)
block|{
name|eventQueue
operator|.
name|add
argument_list|(
operator|new
name|InvocationDispatch
argument_list|(
name|eventArgument
argument_list|,
name|subject
argument_list|,
name|invocation
argument_list|)
argument_list|)
expr_stmt|;
name|eventQueue
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|invocation
operator|.
name|fire
argument_list|(
name|eventArgument
argument_list|)
return|;
block|}
block|}
block|}
comment|// represents a posted event that should be sent to a single known listener
class|class
name|InvocationDispatch
extends|extends
name|Dispatch
block|{
name|Invocation
name|target
decl_stmt|;
name|InvocationDispatch
parameter_list|(
name|EventObject
index|[]
name|eventArgument
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Invocation
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|eventArgument
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|Override
name|void
name|fire
parameter_list|()
block|{
comment|// there is no way to kill the invocation if it is bad...
comment|// so don't check for status
name|target
operator|.
name|fire
argument_list|(
name|eventArgument
argument_list|)
expr_stmt|;
block|}
block|}
comment|// subclass exists only to tag invocations that should be
comment|// dispatched in a separate thread
specifier|final
class|class
name|NonBlockingInvocation
extends|extends
name|Invocation
block|{
specifier|public
name|NonBlockingInvocation
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|super
argument_list|(
name|target
argument_list|,
name|methodName
argument_list|,
name|parameterType
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|DispatchThread
extends|extends
name|Thread
block|{
name|DispatchThread
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
operator|!
name|stopped
condition|)
block|{
comment|// get event from the queue, if the queue
comment|// is empty, just wait
name|Dispatch
name|dispatch
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|DefaultEventManager
operator|.
name|this
operator|.
name|eventQueue
init|)
block|{
if|if
condition|(
name|DefaultEventManager
operator|.
name|this
operator|.
name|eventQueue
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|dispatch
operator|=
name|DefaultEventManager
operator|.
name|this
operator|.
name|eventQueue
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
comment|// wake up occasionally to check whether EM has been stopped
name|DefaultEventManager
operator|.
name|this
operator|.
name|eventQueue
operator|.
name|wait
argument_list|(
literal|3
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore interrupts...
block|}
block|}
block|}
comment|// dispatch outside of synchronized block
if|if
condition|(
operator|!
name|stopped
operator|&&
name|dispatch
operator|!=
literal|null
condition|)
block|{
comment|// this try/catch is needed to prevent DispatchThread
comment|// from dying on dispatch errors
try|try
block|{
name|dispatch
operator|.
name|fire
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignoring exception
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

