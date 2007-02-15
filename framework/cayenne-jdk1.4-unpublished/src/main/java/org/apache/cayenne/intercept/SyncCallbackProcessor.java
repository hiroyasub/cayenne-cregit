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
name|intercept
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|LifecycleListener
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
name|GraphChangeHandler
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
name|GraphDiff
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
name|GraphManager
import|;
end_import

begin_class
class|class
name|SyncCallbackProcessor
implements|implements
name|GraphChangeHandler
block|{
specifier|final
name|DataChannelCallbackInterceptor
name|interceptor
decl_stmt|;
specifier|private
name|GraphManager
name|graphManager
decl_stmt|;
name|Collection
name|updated
decl_stmt|;
name|Collection
name|persisted
decl_stmt|;
name|Collection
name|removed
decl_stmt|;
specifier|private
name|Set
name|seenIds
decl_stmt|;
name|SyncCallbackProcessor
parameter_list|(
name|DataChannelCallbackInterceptor
name|interceptor
parameter_list|,
name|GraphManager
name|graphManager
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|this
operator|.
name|interceptor
operator|=
name|interceptor
expr_stmt|;
name|this
operator|.
name|seenIds
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|this
operator|.
name|graphManager
operator|=
name|graphManager
expr_stmt|;
name|changes
operator|.
name|apply
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|void
name|applyPreCommit
parameter_list|(
name|int
name|syncType
parameter_list|)
block|{
switch|switch
condition|(
name|syncType
condition|)
block|{
case|case
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
case|:
case|case
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
case|:
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|PRE_UPDATE
argument_list|,
name|updated
argument_list|)
expr_stmt|;
if|if
condition|(
name|interceptor
operator|.
name|isContextCallbacksEnabled
argument_list|()
condition|)
block|{
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|PRE_PERSIST
argument_list|,
name|persisted
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|PRE_REMOVE
argument_list|,
name|removed
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|applyPostCommit
parameter_list|(
name|int
name|syncType
parameter_list|)
block|{
switch|switch
condition|(
name|syncType
condition|)
block|{
case|case
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
case|:
case|case
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
case|:
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|POST_UPDATE
argument_list|,
name|updated
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|POST_REMOVE
argument_list|,
name|removed
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|POST_PERSIST
argument_list|,
name|persisted
argument_list|)
expr_stmt|;
break|break;
case|case
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
case|:
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|POST_LOAD
argument_list|,
name|updated
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleListener
operator|.
name|POST_LOAD
argument_list|,
name|removed
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|apply
parameter_list|(
name|int
name|callbackType
parameter_list|,
name|Collection
name|objects
parameter_list|)
block|{
if|if
condition|(
name|objects
operator|!=
literal|null
condition|)
block|{
name|interceptor
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|performCallbacks
argument_list|(
name|callbackType
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
if|if
condition|(
name|seenIds
operator|.
name|add
argument_list|(
name|nodeId
argument_list|)
condition|)
block|{
name|Object
name|node
init|=
name|graphManager
operator|.
name|getNode
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|persisted
operator|==
literal|null
condition|)
block|{
name|persisted
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
name|persisted
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
if|if
condition|(
name|seenIds
operator|.
name|add
argument_list|(
name|nodeId
argument_list|)
condition|)
block|{
name|Object
name|node
init|=
name|graphManager
operator|.
name|getNode
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|removed
operator|==
literal|null
condition|)
block|{
name|removed
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
name|removed
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// TODO: andrus, 9/21/2006 - should we register to-many relationship updates?
name|nodeUpdated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// TODO: andrus, 9/21/2006 - should we register to-many relationship updates?
name|nodeUpdated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
block|}
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|nodeUpdated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|nodeUpdated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
if|if
condition|(
name|seenIds
operator|.
name|add
argument_list|(
name|nodeId
argument_list|)
condition|)
block|{
name|Object
name|node
init|=
name|graphManager
operator|.
name|getNode
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|updated
operator|==
literal|null
condition|)
block|{
name|updated
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
name|updated
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

