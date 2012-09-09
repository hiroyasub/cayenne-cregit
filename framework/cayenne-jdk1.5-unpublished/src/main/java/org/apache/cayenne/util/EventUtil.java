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
name|util
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
name|DataChannelListener
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
name|EventManager
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
name|EventSubject
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
name|graph
operator|.
name|GraphEvent
import|;
end_import

begin_comment
comment|/**  * Contains access stack events related utility methods.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|EventUtil
block|{
specifier|static
specifier|final
name|EventSubject
index|[]
name|CHANNEL_SUBJECTS
init|=
operator|new
name|EventSubject
index|[]
block|{
name|DataChannel
operator|.
name|GRAPH_CHANGED_SUBJECT
block|,
name|DataChannel
operator|.
name|GRAPH_FLUSHED_SUBJECT
block|,
name|DataChannel
operator|.
name|GRAPH_ROLLEDBACK_SUBJECT
block|}
decl_stmt|;
comment|/**      * Utility method that sets up a GraphChangeListener to be notified when DataChannel      * posts an event.      *       * @return false if an DataChannel doesn't have an EventManager and therefore does not      *         support events.      */
specifier|public
specifier|static
name|boolean
name|listenForChannelEvents
parameter_list|(
name|DataChannel
name|channel
parameter_list|,
name|DataChannelListener
name|listener
parameter_list|)
block|{
name|EventManager
name|manager
init|=
name|channel
operator|.
name|getEventManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|manager
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|listenForSubjects
argument_list|(
name|manager
argument_list|,
name|listener
argument_list|,
name|channel
argument_list|,
name|CHANNEL_SUBJECTS
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Listen for events from all channels that use a given EventManager.      */
specifier|public
specifier|static
name|boolean
name|listenForChannelEvents
parameter_list|(
name|EventManager
name|manager
parameter_list|,
name|DataChannelListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|manager
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|listenForSubjects
argument_list|(
name|manager
argument_list|,
name|listener
argument_list|,
literal|null
argument_list|,
name|CHANNEL_SUBJECTS
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Registers GraphEventListener for multiple subjects at once.      */
specifier|static
name|void
name|listenForSubjects
parameter_list|(
name|EventManager
name|manager
parameter_list|,
name|DataChannelListener
name|listener
parameter_list|,
name|Object
name|sender
parameter_list|,
name|EventSubject
index|[]
name|subjects
parameter_list|)
block|{
for|for
control|(
name|EventSubject
name|subject
range|:
name|subjects
control|)
block|{
comment|// assume that subject name and listener method name match
name|String
name|fqSubject
init|=
name|subject
operator|.
name|getSubjectName
argument_list|()
decl_stmt|;
name|String
name|method
init|=
name|fqSubject
operator|.
name|substring
argument_list|(
name|fqSubject
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// use non-blocking listeners for multi-threaded EM; blocking for single
comment|// threaded...
if|if
condition|(
name|manager
operator|.
name|isSingleThreaded
argument_list|()
condition|)
block|{
name|manager
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|method
argument_list|,
name|GraphEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|sender
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|manager
operator|.
name|addNonBlockingListener
argument_list|(
name|listener
argument_list|,
name|method
argument_list|,
name|GraphEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|sender
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// not for instantiation
specifier|private
name|EventUtil
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

