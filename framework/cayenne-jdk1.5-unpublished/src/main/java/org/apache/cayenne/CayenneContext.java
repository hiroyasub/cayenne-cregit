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
name|List
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
name|access
operator|.
name|ResultIterator
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
name|access
operator|.
name|jdbc
operator|.
name|CollectionResultIterator
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
name|graph
operator|.
name|CompoundDiff
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
name|ObjEntity
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|Select
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
name|util
operator|.
name|EventUtil
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
name|validation
operator|.
name|ValidationException
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * A default generic implementation of ObjectContext suitable for accessing  * Cayenne from either an ORM or a client tiers. Communicates with Cayenne via a  * {@link org.apache.cayenne.DataChannel}.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|CayenneContext
extends|extends
name|BaseContext
block|{
name|CayenneContextGraphManager
name|graphManager
decl_stmt|;
comment|// object that merges "backdoor" changes that come from the channel.
name|CayenneContextMergeHandler
name|mergeHandler
decl_stmt|;
comment|/**      * Creates a new CayenneContext with no channel and disabled graph events.      */
specifier|public
name|CayenneContext
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new CayenneContext, initializing it with a channel instance.      * CayenneContext created using this constructor WILL NOT broadcast graph      * change events.      */
specifier|public
name|CayenneContext
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
name|this
argument_list|(
name|channel
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new CayenneContext, initializing it with a channel.      */
specifier|public
name|CayenneContext
parameter_list|(
name|DataChannel
name|channel
parameter_list|,
name|boolean
name|changeEventsEnabled
parameter_list|,
name|boolean
name|lifecyleEventsEnabled
parameter_list|)
block|{
name|graphManager
operator|=
operator|new
name|CayenneContextGraphManager
argument_list|(
name|this
argument_list|,
name|changeEventsEnabled
argument_list|,
name|lifecyleEventsEnabled
argument_list|)
expr_stmt|;
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|attachToChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 3.1      */
annotation|@
name|Override
specifier|protected
name|void
name|attachToChannel
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
name|super
operator|.
name|attachToChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
if|if
condition|(
name|mergeHandler
operator|!=
literal|null
condition|)
block|{
name|mergeHandler
operator|.
name|active
operator|=
literal|false
expr_stmt|;
name|mergeHandler
operator|=
literal|null
expr_stmt|;
block|}
name|EventManager
name|eventManager
init|=
name|channel
operator|.
name|getEventManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|mergeHandler
operator|=
operator|new
name|CayenneContextMergeHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// listen to our channel events...
comment|// note that we must reset listener on channel switch, as there is
comment|// no
comment|// guarantee that a new channel uses the same EventManager.
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
name|channel
argument_list|,
name|mergeHandler
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if this context posts individual object modification events.      * Subject used for these events is      *<code>ObjectContext.GRAPH_CHANGED_SUBJECT</code>.      */
specifier|public
name|boolean
name|isChangeEventsEnabled
parameter_list|()
block|{
return|return
name|graphManager
operator|.
name|changeEventsEnabled
return|;
block|}
comment|/**      * Returns true if this context posts lifecycle events. Subjects used for      * these events are      *<code>ObjectContext.GRAPH_COMMIT_STARTED_SUBJECT, ObjectContext.GRAPH_COMMITTED_SUBJECT,      * ObjectContext.GRAPH_COMMIT_ABORTED_SUBJECT, ObjectContext.GRAPH_ROLLEDBACK_SUBJECT.</code>      * .      */
specifier|public
name|boolean
name|isLifecycleEventsEnabled
parameter_list|()
block|{
return|return
name|graphManager
operator|.
name|lifecycleEventsEnabled
return|;
block|}
annotation|@
name|Override
specifier|public
name|GraphManager
name|getGraphManager
parameter_list|()
block|{
return|return
name|graphManager
return|;
block|}
name|CayenneContextGraphManager
name|internalGraphManager
parameter_list|()
block|{
return|return
name|graphManager
return|;
block|}
comment|/**      * Commits changes to uncommitted objects. First checks if there are changes      * in this context and if any changes are detected, sends a commit message      * to remote Cayenne service via an internal instance of CayenneConnector.      */
annotation|@
name|Override
specifier|public
name|void
name|commitChanges
parameter_list|()
block|{
name|doCommitChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|GraphDiff
name|doCommitChanges
parameter_list|(
name|boolean
name|cascade
parameter_list|)
block|{
name|int
name|syncType
init|=
name|cascade
condition|?
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
else|:
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
decl_stmt|;
name|GraphDiff
name|commitDiff
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
if|if
condition|(
name|graphManager
operator|.
name|hasChanges
argument_list|()
condition|)
block|{
if|if
condition|(
name|isValidatingObjectsOnCommit
argument_list|()
condition|)
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|graphManager
operator|.
name|dirtyNodes
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
name|Persistent
name|p
init|=
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|instanceof
name|Validating
condition|)
block|{
switch|switch
condition|(
name|p
operator|.
name|getPersistenceState
argument_list|()
condition|)
block|{
case|case
name|PersistenceState
operator|.
name|NEW
case|:
operator|(
operator|(
name|Validating
operator|)
name|p
operator|)
operator|.
name|validateForInsert
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
case|case
name|PersistenceState
operator|.
name|MODIFIED
case|:
operator|(
operator|(
name|Validating
operator|)
name|p
operator|)
operator|.
name|validateForUpdate
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
case|case
name|PersistenceState
operator|.
name|DELETED
case|:
operator|(
operator|(
name|Validating
operator|)
name|p
operator|)
operator|.
name|validateForDelete
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|result
argument_list|)
throw|;
block|}
block|}
name|graphManager
operator|.
name|graphCommitStarted
argument_list|()
expr_stmt|;
name|GraphDiff
name|changes
init|=
name|graphManager
operator|.
name|getDiffsSinceLastFlush
argument_list|()
decl_stmt|;
try|try
block|{
name|commitDiff
operator|=
name|channel
operator|.
name|onSync
argument_list|(
name|this
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|graphManager
operator|.
name|graphCommitAborted
argument_list|()
expr_stmt|;
if|if
condition|(
name|th
operator|instanceof
name|CayenneRuntimeException
condition|)
block|{
throw|throw
operator|(
name|CayenneRuntimeException
operator|)
name|th
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Commit error"
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
name|graphManager
operator|.
name|graphCommitted
argument_list|(
name|commitDiff
argument_list|)
expr_stmt|;
comment|// this event is caught by peer nested ObjectContexts to
comment|// synchronize the
comment|// state
name|fireDataChannelCommitted
argument_list|(
name|this
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|commitDiff
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|commitChangesToParent
parameter_list|()
block|{
name|doCommitChanges
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollbackChanges
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
if|if
condition|(
name|graphManager
operator|.
name|hasChanges
argument_list|()
condition|)
block|{
name|GraphDiff
name|diff
init|=
name|graphManager
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|graphManager
operator|.
name|graphReverted
argument_list|()
expr_stmt|;
name|channel
operator|.
name|onSync
argument_list|(
name|this
argument_list|,
name|diff
argument_list|,
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
argument_list|)
expr_stmt|;
name|fireDataChannelRolledback
argument_list|(
name|this
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollbackChangesLocally
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
if|if
condition|(
name|graphManager
operator|.
name|hasChanges
argument_list|()
condition|)
block|{
name|GraphDiff
name|diff
init|=
name|graphManager
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|graphManager
operator|.
name|graphReverted
argument_list|()
expr_stmt|;
name|fireDataChannelRolledback
argument_list|(
name|this
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates and registers a new Persistent object instance.      */
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newObject
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|persistentClass
parameter_list|)
block|{
if|if
condition|(
name|persistentClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Persistent class can't be null."
argument_list|)
throw|;
block|}
name|ObjEntity
name|entity
init|=
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|persistentClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No entity mapped for class: "
operator|+
name|persistentClass
argument_list|)
throw|;
block|}
name|ClassDescriptor
name|descriptor
init|=
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|T
name|object
init|=
operator|(
name|T
operator|)
name|descriptor
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|injectValueHolders
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|registerNewObject
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
return|return
name|object
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|registerNewObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"An attempt to register null object."
argument_list|)
throw|;
block|}
name|ObjEntity
name|entity
init|=
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|registerNewObject
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Runs a query, returning result as list.      */
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|List
name|result
init|=
name|onQuery
argument_list|(
name|this
argument_list|,
name|query
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
return|return
name|result
operator|!=
literal|null
condition|?
name|result
else|:
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QueryResponse
name|performGenericQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
name|onQuery
argument_list|(
name|this
argument_list|,
name|query
argument_list|)
return|;
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
return|return
operator|new
name|CayenneContextQueryAction
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
name|query
argument_list|)
operator|.
name|execute
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|?
argument_list|>
name|uncommittedObjects
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
return|return
name|graphManager
operator|.
name|dirtyNodes
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|?
argument_list|>
name|deletedObjects
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
return|return
name|graphManager
operator|.
name|dirtyNodes
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|?
argument_list|>
name|modifiedObjects
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
return|return
name|graphManager
operator|.
name|dirtyNodes
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|?
argument_list|>
name|newObjects
parameter_list|()
block|{
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
return|return
name|graphManager
operator|.
name|dirtyNodes
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|)
return|;
block|}
block|}
comment|// ****** non-public methods ******
name|void
name|registerNewObject
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|entityName
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
comment|/**          * We should create new id only if it is not set for this object. It          * could have been created, for instance, in child context          */
name|ObjectId
name|id
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
operator|new
name|ObjectId
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|injectInitialValue
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
name|Persistent
name|createFault
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|Persistent
name|object
decl_stmt|;
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
name|object
operator|=
operator|(
name|Persistent
operator|)
name|descriptor
operator|.
name|createObject
argument_list|()
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|graphManager
operator|.
name|registerNode
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
return|return
name|object
return|;
block|}
annotation|@
name|Override
specifier|protected
name|GraphDiff
name|onContextFlush
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|boolean
name|cascade
parameter_list|)
block|{
name|boolean
name|childContext
init|=
name|this
operator|!=
name|originatingContext
operator|&&
name|changes
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|childContext
condition|)
block|{
comment|// PropertyChangeProcessingStrategy oldStrategy =
comment|// getPropertyChangeProcessingStrategy();
comment|// setPropertyChangeProcessingStrategy(PropertyChangeProcessingStrategy.RECORD);
try|try
block|{
name|changes
operator|.
name|apply
argument_list|(
operator|new
name|CayenneContextChildDiffLoader
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// setPropertyChangeProcessingStrategy(oldStrategy);
block|}
name|fireDataChannelChanged
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|cascade
operator|)
condition|?
name|doCommitChanges
argument_list|(
literal|true
argument_list|)
else|:
operator|new
name|CompoundDiff
argument_list|()
return|;
block|}
comment|/**      * Returns<code>true</code> if there are any modified, deleted or new      * objects registered with this CayenneContext,<code>false</code>      * otherwise.      */
specifier|public
name|boolean
name|hasChanges
parameter_list|()
block|{
return|return
name|graphManager
operator|.
name|hasChanges
argument_list|()
return|;
block|}
comment|/**      * This method simply returns an iterator over a list of selected objects.      * There's no performance benefit of using it vs. regular "select".      *       * @since 3.2      */
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterate
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|objects
init|=
name|select
argument_list|(
name|query
argument_list|)
decl_stmt|;
return|return
operator|new
name|CollectionResultIterator
argument_list|<
name|T
argument_list|>
argument_list|(
name|objects
argument_list|)
return|;
block|}
block|}
end_class

end_unit

