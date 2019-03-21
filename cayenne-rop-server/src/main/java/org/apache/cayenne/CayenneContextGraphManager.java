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
name|ArcCreateOperation
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
name|ArcDeleteOperation
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
name|ArcId
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
name|GraphEvent
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
name|GraphMap
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
name|NodeCreateOperation
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
name|NodeDeleteOperation
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
name|NodeIdChangeOperation
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
name|NodePropertyChangeOperation
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
name|EntityResolver
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
name|ArcProperty
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
name|ClassDescriptor
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
name|PropertyException
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
name|ToManyMapProperty
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
name|PersistentObjectMap
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

begin_comment
comment|/**  * A GraphMap extension that works together with {@link ObjectContext} to track persistent object  * changes and send events.  *   * @since 1.2  */
end_comment

begin_class
specifier|final
class|class
name|CayenneContextGraphManager
extends|extends
name|GraphMap
block|{
specifier|static
specifier|final
name|String
name|COMMIT_MARKER
init|=
literal|"commit"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLUSH_MARKER
init|=
literal|"flush"
decl_stmt|;
name|CayenneContext
name|context
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|deadIds
decl_stmt|;
name|boolean
name|changeEventsEnabled
decl_stmt|;
name|boolean
name|lifecycleEventsEnabled
decl_stmt|;
name|ObjectContextStateLog
name|stateLog
decl_stmt|;
name|ObjectContextChangeLog
name|changeLog
decl_stmt|;
name|CayenneContextGraphManager
parameter_list|(
name|CayenneContext
name|context
parameter_list|,
name|boolean
name|changeEventsEnabled
parameter_list|,
name|boolean
name|lifecycleEventsEnabled
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|changeEventsEnabled
operator|=
name|changeEventsEnabled
expr_stmt|;
name|this
operator|.
name|lifecycleEventsEnabled
operator|=
name|lifecycleEventsEnabled
expr_stmt|;
name|this
operator|.
name|stateLog
operator|=
operator|new
name|ObjectContextStateLog
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeLog
operator|=
operator|new
name|ObjectContextChangeLog
argument_list|()
expr_stmt|;
block|}
name|boolean
name|hasChanges
parameter_list|()
block|{
return|return
name|changeLog
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
name|boolean
name|hasChangesSinceLastFlush
parameter_list|()
block|{
name|int
name|size
init|=
name|changeLog
operator|.
name|hasMarker
argument_list|(
name|FLUSH_MARKER
argument_list|)
condition|?
name|changeLog
operator|.
name|sizeAfterMarker
argument_list|(
name|FLUSH_MARKER
argument_list|)
else|:
name|changeLog
operator|.
name|size
argument_list|()
decl_stmt|;
return|return
name|size
operator|>
literal|0
return|;
block|}
name|GraphDiff
name|getDiffs
parameter_list|()
block|{
return|return
name|changeLog
operator|.
name|getDiffs
argument_list|()
return|;
block|}
name|GraphDiff
name|getDiffsSinceLastFlush
parameter_list|()
block|{
return|return
name|changeLog
operator|.
name|hasMarker
argument_list|(
name|FLUSH_MARKER
argument_list|)
condition|?
name|changeLog
operator|.
name|getDiffsAfterMarker
argument_list|(
name|FLUSH_MARKER
argument_list|)
else|:
name|changeLog
operator|.
name|getDiffs
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|Object
argument_list|>
name|dirtyNodes
parameter_list|()
block|{
return|return
name|stateLog
operator|.
name|dirtyNodes
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|Object
argument_list|>
name|dirtyNodes
parameter_list|(
name|int
name|state
parameter_list|)
block|{
return|return
name|stateLog
operator|.
name|dirtyNodes
argument_list|(
name|state
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|Object
name|unregisterNode
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|Object
name|node
init|=
name|super
operator|.
name|unregisterNode
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
comment|// remove node from other collections...
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|stateLog
operator|.
name|unregisterNode
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|changeLog
operator|.
name|unregisterNode
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|node
decl_stmt|;
name|object
operator|.
name|setObjectContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// ****** Sync Events API *****
comment|/**      * Clears commit marker, but keeps all recorded operations.      */
name|void
name|graphCommitAborted
parameter_list|()
block|{
name|changeLog
operator|.
name|removeMarker
argument_list|(
name|COMMIT_MARKER
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets commit start marker in the change log. If events are enabled, posts commit      * start event.      */
name|void
name|graphCommitStarted
parameter_list|()
block|{
name|changeLog
operator|.
name|setMarker
argument_list|(
name|COMMIT_MARKER
argument_list|)
expr_stmt|;
block|}
name|void
name|graphCommitted
parameter_list|(
name|GraphDiff
name|parentSyncDiff
parameter_list|)
block|{
if|if
condition|(
name|parentSyncDiff
operator|!=
literal|null
condition|)
block|{
operator|new
name|CayenneContextMergeHandler
argument_list|(
name|context
argument_list|)
operator|.
name|merge
argument_list|(
name|parentSyncDiff
argument_list|)
expr_stmt|;
block|}
name|remapTargets
argument_list|()
expr_stmt|;
name|stateLog
operator|.
name|graphCommitted
argument_list|()
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|lifecycleEventsEnabled
condition|)
block|{
comment|// include all diffs after the commit start marker.
comment|// We fire event as if it was posted by parent channel, so that
comment|// nested contexts could catch it
name|context
operator|.
name|fireDataChannelCommitted
argument_list|(
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
name|parentSyncDiff
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Remaps keys in to-many map relationships that contain dirty objects with      * potentially modified properties.      */
specifier|private
name|void
name|remapTargets
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|stateLog
operator|.
name|dirtyIds
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|EntityResolver
name|resolver
init|=
name|context
operator|.
name|getEntityResolver
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
name|ObjectId
name|id
init|=
operator|(
name|ObjectId
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ArcProperty
argument_list|>
name|mapArcProperties
init|=
name|descriptor
operator|.
name|getMapArcProperties
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|mapArcProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Object
name|object
init|=
name|getNode
argument_list|(
name|id
argument_list|)
decl_stmt|;
for|for
control|(
name|ArcProperty
name|arc
range|:
name|mapArcProperties
control|)
block|{
name|ToManyMapProperty
name|reverseArc
init|=
operator|(
name|ToManyMapProperty
operator|)
name|arc
operator|.
name|getComplimentaryReverseArc
argument_list|()
decl_stmt|;
name|Object
name|source
init|=
name|arc
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
operator|&&
operator|!
name|reverseArc
operator|.
name|isFault
argument_list|(
name|source
argument_list|)
condition|)
block|{
name|remapTarget
argument_list|(
name|reverseArc
argument_list|,
name|source
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|// clone of DataDomainSyncBucket.remapTarget
specifier|private
specifier|final
name|void
name|remapTarget
parameter_list|(
name|ToManyMapProperty
name|property
parameter_list|,
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|property
operator|.
name|readProperty
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Object
name|newKey
init|=
name|property
operator|.
name|getMapKey
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|Object
name|currentValue
init|=
name|map
operator|.
name|get
argument_list|(
name|newKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentValue
operator|==
name|target
condition|)
block|{
comment|// nothing to do
return|return;
block|}
comment|// else - do not check for conflicts here (i.e. another object mapped for the same
comment|// key), as we have no control of the order in which this method is called, so
comment|// another object may be remapped later by the caller
comment|// must do a slow map scan to ensure the object is not mapped under a different
comment|// key...
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|map
operator|.
name|entrySet
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
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|e
init|=
operator|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|target
condition|)
block|{
comment|// this remove does not trigger event in PersistentObjectMap
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
comment|// TODO: (andrey, 25/11/09 - this is a hack to prevent event triggering
comment|// (and concurrent exceptions)
comment|// should find a way to get rid of type casting
operator|(
operator|(
name|PersistentObjectMap
operator|)
name|map
operator|)
operator|.
name|putDirectly
argument_list|(
name|newKey
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
name|void
name|graphFlushed
parameter_list|()
block|{
name|changeLog
operator|.
name|setMarker
argument_list|(
name|FLUSH_MARKER
argument_list|)
expr_stmt|;
block|}
name|void
name|graphReverted
parameter_list|()
block|{
name|GraphDiff
name|diff
init|=
name|changeLog
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|diff
operator|.
name|undo
argument_list|(
operator|new
name|RollbackChangeHandler
argument_list|()
argument_list|)
expr_stmt|;
name|stateLog
operator|.
name|graphReverted
argument_list|()
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|lifecycleEventsEnabled
condition|)
block|{
name|context
operator|.
name|fireDataChannelRolledback
argument_list|(
name|context
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ****** GraphChangeHandler API ******
comment|// =====================================================
annotation|@
name|Override
specifier|public
specifier|synchronized
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
name|stateLog
operator|.
name|nodeIdChanged
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|NodeIdChangeOperation
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|stateLog
operator|.
name|nodeCreated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
name|nodeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|stateLog
operator|.
name|nodeRemoved
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|NodeDeleteOperation
argument_list|(
name|nodeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
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
name|stateLog
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|NodePropertyChangeOperation
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
name|stateLog
operator|.
name|arcCreated
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|ArcCreateOperation
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
name|stateLog
operator|.
name|arcDeleted
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
name|processChange
argument_list|(
operator|new
name|ArcDeleteOperation
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ****** helper methods ******
comment|// =====================================================
specifier|private
name|void
name|processChange
parameter_list|(
name|GraphDiff
name|diff
parameter_list|)
block|{
name|changeLog
operator|.
name|addOperation
argument_list|(
name|diff
argument_list|)
expr_stmt|;
if|if
condition|(
name|changeEventsEnabled
condition|)
block|{
name|context
operator|.
name|fireDataChannelChanged
argument_list|(
name|context
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Wraps GraphDiff in a GraphEvent and sends it via EventManager with specified      * subject.      */
name|void
name|send
parameter_list|(
name|GraphDiff
name|diff
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|eventSource
parameter_list|)
block|{
name|EventManager
name|manager
init|=
operator|(
name|context
operator|.
name|getChannel
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|context
operator|.
name|getChannel
argument_list|()
operator|.
name|getEventManager
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|GraphEvent
name|e
init|=
operator|new
name|GraphEvent
argument_list|(
name|context
argument_list|,
name|eventSource
argument_list|,
name|diff
argument_list|)
decl_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|e
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|reset
parameter_list|()
block|{
name|changeLog
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|deadIds
operator|!=
literal|null
condition|)
block|{
comment|// unregister dead ids...
for|for
control|(
specifier|final
name|Object
name|deadId
range|:
name|deadIds
control|)
block|{
name|nodes
operator|.
name|remove
argument_list|(
name|deadId
argument_list|)
expr_stmt|;
block|}
name|deadIds
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|Object
argument_list|>
name|deadIds
parameter_list|()
block|{
if|if
condition|(
name|deadIds
operator|==
literal|null
condition|)
block|{
name|deadIds
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|deadIds
return|;
block|}
comment|/**      * This change handler is used to perform rollback actions for Cayenne context      */
class|class
name|RollbackChangeHandler
implements|implements
name|GraphChangeHandler
block|{
annotation|@
name|Override
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
name|ArcId
name|arcId
parameter_list|)
block|{
name|context
operator|.
name|mergeHandler
operator|.
name|arcCreated
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|arcCreated
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|ArcId
name|arcId
parameter_list|)
block|{
name|context
operator|.
name|mergeHandler
operator|.
name|arcDeleted
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|arcDeleted
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|nodeCreated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|nodeIdChanged
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
expr_stmt|;
block|}
comment|/**          * Need to write property directly to this context          */
annotation|@
name|Override
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
name|context
operator|.
name|mergeHandler
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|CayenneContextGraphManager
operator|.
name|this
operator|.
name|nodeRemoved
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

