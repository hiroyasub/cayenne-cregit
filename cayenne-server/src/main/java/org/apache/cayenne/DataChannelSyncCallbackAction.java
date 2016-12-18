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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|LifecycleEvent
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
name|reflect
operator|.
name|LifecycleCallbackRegistry
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
name|Collection
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
comment|/**  * @since 3.1  */
end_comment

begin_comment
comment|// note: made public in 3.1 to be used in all tiers
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|DataChannelSyncCallbackAction
implements|implements
name|GraphChangeHandler
block|{
specifier|static
enum|enum
name|Op
block|{
name|INSERT
block|,
name|UPDATE
block|,
name|DELETE
block|}
specifier|public
specifier|static
name|DataChannelSyncCallbackAction
name|getCallbackAction
parameter_list|(
name|LifecycleCallbackRegistry
name|callbackRegistry
parameter_list|,
name|GraphManager
name|graphManager
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
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
return|return
operator|new
name|FlushCallbackAction
argument_list|(
name|callbackRegistry
argument_list|,
name|graphManager
argument_list|,
name|changes
argument_list|)
return|;
case|case
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
case|:
return|return
operator|new
name|RollbackCallbackAction
argument_list|(
name|callbackRegistry
argument_list|,
name|graphManager
argument_list|,
name|changes
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported sync type: "
operator|+
name|syncType
argument_list|)
throw|;
block|}
block|}
name|LifecycleCallbackRegistry
name|callbackRegistry
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
name|Map
argument_list|<
name|Object
argument_list|,
name|Op
argument_list|>
name|seenIds
decl_stmt|;
specifier|private
name|GraphManager
name|graphManager
decl_stmt|;
name|DataChannelSyncCallbackAction
parameter_list|(
name|LifecycleCallbackRegistry
name|callbackRegistry
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
name|callbackRegistry
operator|=
name|callbackRegistry
expr_stmt|;
name|this
operator|.
name|graphManager
operator|=
name|graphManager
expr_stmt|;
if|if
condition|(
name|hasListeners
argument_list|()
condition|)
block|{
name|this
operator|.
name|seenIds
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Op
argument_list|>
argument_list|()
expr_stmt|;
name|changes
operator|.
name|apply
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|boolean
name|hasListeners
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|applyPreCommit
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|applyPostCommit
parameter_list|()
function_decl|;
name|void
name|apply
parameter_list|(
name|LifecycleEvent
name|callbackType
parameter_list|,
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
if|if
condition|(
name|seenIds
operator|!=
literal|null
operator|&&
name|objects
operator|!=
literal|null
condition|)
block|{
name|callbackRegistry
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
name|Op
name|op
init|=
name|seenIds
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|Op
operator|.
name|INSERT
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
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
argument_list|<>
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
name|Op
name|op
init|=
name|seenIds
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|Op
operator|.
name|DELETE
argument_list|)
decl_stmt|;
comment|// the node may have been updated prior to delete
if|if
condition|(
name|op
operator|!=
name|Op
operator|.
name|DELETE
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
argument_list|<>
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
if|if
condition|(
name|op
operator|==
name|Op
operator|.
name|UPDATE
condition|)
block|{
name|updated
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|// don't care about preceding Op.INSERT, as NEW -> DELETED objects are
comment|// purged from the change log upstream and we don't see them here
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
name|Op
name|op
init|=
name|seenIds
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|Op
operator|.
name|UPDATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
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
argument_list|<>
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
specifier|static
class|class
name|FlushCallbackAction
extends|extends
name|DataChannelSyncCallbackAction
block|{
name|FlushCallbackAction
parameter_list|(
name|LifecycleCallbackRegistry
name|callbackRegistry
parameter_list|,
name|GraphManager
name|graphManager
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|super
argument_list|(
name|callbackRegistry
argument_list|,
name|graphManager
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|hasListeners
parameter_list|()
block|{
return|return
operator|!
operator|(
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|)
operator|&&
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|)
operator|&&
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|)
operator|&&
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|)
operator|&&
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|applyPreCommit
parameter_list|()
block|{
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|persisted
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
name|updated
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|applyPostCommit
parameter_list|()
block|{
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|,
name|updated
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|,
name|removed
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|,
name|persisted
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|RollbackCallbackAction
extends|extends
name|DataChannelSyncCallbackAction
block|{
name|RollbackCallbackAction
parameter_list|(
name|LifecycleCallbackRegistry
name|callbackRegistry
parameter_list|,
name|GraphManager
name|graphManager
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|super
argument_list|(
name|callbackRegistry
argument_list|,
name|graphManager
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|hasListeners
parameter_list|()
block|{
return|return
operator|!
name|callbackRegistry
operator|.
name|isEmpty
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|applyPreCommit
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
specifier|public
name|void
name|applyPostCommit
parameter_list|()
block|{
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|updated
argument_list|)
expr_stmt|;
name|apply
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|removed
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

