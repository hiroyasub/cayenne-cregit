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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|event
operator|.
name|DefaultEventManager
operator|.
name|Dispatch
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

begin_comment
comment|/**  * Stores a set of Invocation objects, organizing them by sender. Listeners have an option  * to receive events for a particular sender or to receive all events. EventManager  * creates one DispatchQueue per EventSubject. DispatchQueue is thread-safe - all methods  * that read/modify internal collections are synchronized.  *   * @since 1.1  */
end_comment

begin_class
class|class
name|DispatchQueue
block|{
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|subjectInvocations
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
argument_list|>
name|invocationsBySender
decl_stmt|;
name|DispatchQueue
parameter_list|()
block|{
name|subjectInvocations
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
comment|// TODO: need something like com.google.common.collect.MapMaker to avoid
comment|// synchronization on invocationsBySender
name|invocationsBySender
operator|=
operator|new
name|WeakHashMap
argument_list|<
name|Object
argument_list|,
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Dispatches event to all listeners in the queue that are registered for this event      * and sender.      */
name|void
name|dispatchEvent
parameter_list|(
name|Dispatch
name|dispatch
parameter_list|)
block|{
comment|// dispatch to "any sender" listeners
name|dispatchEvent
argument_list|(
name|subjectInvocations
operator|.
name|keySet
argument_list|()
argument_list|,
name|dispatch
argument_list|)
expr_stmt|;
comment|// dispatch to the given sender listeners
name|Object
name|sender
init|=
name|dispatch
operator|.
name|getSender
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|senderInvocations
init|=
name|invocationsForSender
argument_list|(
name|sender
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|senderInvocations
operator|!=
literal|null
condition|)
block|{
name|dispatchEvent
argument_list|(
name|senderInvocations
operator|.
name|keySet
argument_list|()
argument_list|,
name|dispatch
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|addInvocation
parameter_list|(
name|Invocation
name|invocation
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|invocations
decl_stmt|;
if|if
condition|(
name|sender
operator|==
literal|null
condition|)
block|{
name|invocations
operator|=
name|subjectInvocations
expr_stmt|;
block|}
else|else
block|{
name|invocations
operator|=
name|invocationsForSender
argument_list|(
name|sender
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// perform maintenance of the given invocations set, as failure to do that can
comment|// result in a memory leak per CAY-770. This seemed to happen when lots of
comment|// invocations got registered, but no events were dispatched (hence the stale
comment|// invocation removal during dispatch did not happen)
name|Iterator
argument_list|<
name|Invocation
argument_list|>
name|it
init|=
name|invocations
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Invocation
name|i
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|.
name|getTarget
argument_list|()
operator|==
literal|null
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|invocations
operator|.
name|putIfAbsent
argument_list|(
name|invocation
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|boolean
name|removeInvocations
parameter_list|(
name|Object
name|listener
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
comment|// remove only for specific sender
if|if
condition|(
name|sender
operator|!=
literal|null
condition|)
block|{
return|return
name|removeInvocations
argument_list|(
name|invocationsForSender
argument_list|(
name|sender
argument_list|,
literal|false
argument_list|)
argument_list|,
name|listener
argument_list|)
return|;
block|}
comment|// remove listener from all collections
name|boolean
name|didRemove
init|=
name|removeInvocations
argument_list|(
name|subjectInvocations
argument_list|,
name|listener
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|invocationsBySender
init|)
block|{
for|for
control|(
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|senderInvocations
range|:
name|invocationsBySender
operator|.
name|values
argument_list|()
control|)
block|{
name|didRemove
operator|=
name|removeInvocations
argument_list|(
name|senderInvocations
argument_list|,
name|listener
argument_list|)
operator|||
name|didRemove
expr_stmt|;
block|}
block|}
return|return
name|didRemove
return|;
block|}
specifier|private
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|invocationsForSender
parameter_list|(
name|Object
name|sender
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
synchronized|synchronized
init|(
name|invocationsBySender
init|)
block|{
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|senderInvocations
init|=
name|invocationsBySender
operator|.
name|get
argument_list|(
name|sender
argument_list|)
decl_stmt|;
if|if
condition|(
name|create
operator|&&
name|senderInvocations
operator|==
literal|null
condition|)
block|{
name|senderInvocations
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|invocationsBySender
operator|.
name|put
argument_list|(
name|sender
argument_list|,
name|senderInvocations
argument_list|)
expr_stmt|;
block|}
return|return
name|senderInvocations
return|;
block|}
block|}
comment|// removes all invocations for a given listener
specifier|private
name|boolean
name|removeInvocations
parameter_list|(
name|ConcurrentMap
argument_list|<
name|Invocation
argument_list|,
name|Object
argument_list|>
name|invocations
parameter_list|,
name|Object
name|listener
parameter_list|)
block|{
if|if
condition|(
name|invocations
operator|==
literal|null
operator|||
name|invocations
operator|.
name|isEmpty
argument_list|()
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
name|Iterator
argument_list|<
name|Invocation
argument_list|>
name|invocationsIt
init|=
name|invocations
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|invocationsIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Invocation
name|invocation
init|=
name|invocationsIt
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|invocation
operator|.
name|getTarget
argument_list|()
operator|==
name|listener
condition|)
block|{
name|invocationsIt
operator|.
name|remove
argument_list|()
expr_stmt|;
name|didRemove
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|didRemove
return|;
block|}
comment|// dispatches event to a list of listeners
specifier|private
name|void
name|dispatchEvent
parameter_list|(
name|Collection
argument_list|<
name|Invocation
argument_list|>
name|invocations
parameter_list|,
name|Dispatch
name|dispatch
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Invocation
argument_list|>
name|it
init|=
name|invocations
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// fire invocation, clean up GC'd invocations...
name|Invocation
name|invocation
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dispatch
operator|.
name|fire
argument_list|(
name|invocation
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

