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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
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
name|Collections
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
name|DataObject
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
name|DataRow
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
name|ObjectId
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
name|PersistenceState
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
name|Persistent
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
name|event
operator|.
name|SnapshotEvent
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
name|EventBridge
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
name|EventBridgeFactory
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
name|util
operator|.
name|concurrentlinkedhashmap
operator|.
name|ConcurrentLinkedHashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|ExtendedProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A fixed size cache of DataRows keyed by ObjectId.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|DataRowStore
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DataRowStore
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// property keys
specifier|public
specifier|static
specifier|final
name|String
name|SNAPSHOT_EXPIRATION_PROPERTY
init|=
literal|"cayenne.DataRowStore.snapshot.expiration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SNAPSHOT_CACHE_SIZE_PROPERTY
init|=
literal|"cayenne.DataRowStore.snapshot.size"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REMOTE_NOTIFICATION_PROPERTY
init|=
literal|"cayenne.DataRowStore.remote.notify"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_BRIDGE_FACTORY_PROPERTY
init|=
literal|"cayenne.DataRowStore.EventBridge.factory"
decl_stmt|;
comment|// default property values
comment|// default expiration time is 2 hours
specifier|public
specifier|static
specifier|final
name|long
name|SNAPSHOT_EXPIRATION_DEFAULT
init|=
literal|2
operator|*
literal|60
operator|*
literal|60
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|SNAPSHOT_CACHE_SIZE_DEFAULT
init|=
literal|10000
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|boolean
name|REMOTE_NOTIFICATION_DEFAULT
init|=
literal|false
decl_stmt|;
comment|// use String for class name, since JavaGroups may not be around,
comment|// causing CNF exceptions
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_BRIDGE_FACTORY_DEFAULT
init|=
literal|"org.apache.cayenne.event.JavaGroupsBridgeFactory"
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|maxSize
decl_stmt|;
specifier|protected
name|ConcurrentMap
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|snapshots
decl_stmt|;
specifier|protected
name|boolean
name|notifyingRemoteListeners
decl_stmt|;
specifier|protected
specifier|transient
name|EventManager
name|eventManager
decl_stmt|;
specifier|protected
specifier|transient
name|EventBridge
name|remoteNotificationsHandler
decl_stmt|;
comment|// IMPORTANT: EventSubject must be an ivar to avoid its deallocation
comment|// too early, and thus disabling events.
specifier|protected
specifier|transient
name|EventSubject
name|eventSubject
decl_stmt|;
comment|/**      * Creates new DataRowStore with a specified name and a set of properties. If no      * properties are defined, default values are used.      *       * @param name DataRowStore name. Used to idenitfy this DataRowStore in events, etc.      *            Can't be null.      * @param properties Properties map used to configure DataRowStore parameters. Can be      *            null.      * @param eventManager EventManager that should be used for posting and receiving      *            events.      * @since 1.2      */
specifier|public
name|DataRowStore
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
name|properties
parameter_list|,
name|EventManager
name|eventManager
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DataRowStore name can't be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|eventSubject
operator|=
name|createSubject
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|private
name|EventSubject
name|createSubject
parameter_list|()
block|{
return|return
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|protected
name|void
name|initWithProperties
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|ExtendedProperties
name|propertiesWrapper
init|=
operator|new
name|ExtendedProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|propertiesWrapper
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|long
name|snapshotsExpiration
init|=
name|propertiesWrapper
operator|.
name|getLong
argument_list|(
name|SNAPSHOT_EXPIRATION_PROPERTY
argument_list|,
name|SNAPSHOT_EXPIRATION_DEFAULT
argument_list|)
decl_stmt|;
name|maxSize
operator|=
name|propertiesWrapper
operator|.
name|getInt
argument_list|(
name|SNAPSHOT_CACHE_SIZE_PROPERTY
argument_list|,
name|SNAPSHOT_CACHE_SIZE_DEFAULT
argument_list|)
expr_stmt|;
name|boolean
name|notifyRemote
init|=
name|propertiesWrapper
operator|.
name|getBoolean
argument_list|(
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
decl_stmt|;
name|String
name|eventBridgeFactory
init|=
name|propertiesWrapper
operator|.
name|getString
argument_list|(
name|EVENT_BRIDGE_FACTORY_PROPERTY
argument_list|,
name|EVENT_BRIDGE_FACTORY_DEFAULT
argument_list|)
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"DataRowStore property "
operator|+
name|SNAPSHOT_EXPIRATION_PROPERTY
operator|+
literal|" = "
operator|+
name|snapshotsExpiration
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"DataRowStore property "
operator|+
name|SNAPSHOT_CACHE_SIZE_PROPERTY
operator|+
literal|" = "
operator|+
name|maxSize
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"DataRowStore property "
operator|+
name|REMOTE_NOTIFICATION_PROPERTY
operator|+
literal|" = "
operator|+
name|notifyRemote
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"DataRowStore property "
operator|+
name|EVENT_BRIDGE_FACTORY_PROPERTY
operator|+
literal|" = "
operator|+
name|eventBridgeFactory
argument_list|)
expr_stmt|;
block|}
comment|// init ivars from properties
name|this
operator|.
name|notifyingRemoteListeners
operator|=
name|notifyRemote
expr_stmt|;
name|this
operator|.
name|snapshots
operator|=
operator|new
name|ConcurrentLinkedHashMap
operator|.
name|Builder
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
argument_list|()
operator|.
name|maximumWeightedCapacity
argument_list|(
name|maxSize
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// init event bridge only if we are notifying remote listeners
if|if
condition|(
name|notifyingRemoteListeners
condition|)
block|{
try|try
block|{
name|EventBridgeFactory
name|factory
init|=
operator|(
name|EventBridgeFactory
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|eventBridgeFactory
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|subjects
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|getSnapshotEventSubject
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|externalSubject
init|=
name|EventBridge
operator|.
name|convertToExternalSubject
argument_list|(
name|getSnapshotEventSubject
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|remoteNotificationsHandler
operator|=
name|factory
operator|.
name|createEventBridge
argument_list|(
name|subjects
argument_list|,
name|externalSubject
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error initializing DataRowStore."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|startListeners
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Updates cached snapshots for the list of objects.      *       * @since 1.2      */
name|void
name|snapshotsUpdatedForObjects
parameter_list|(
name|List
name|objects
parameter_list|,
name|List
name|snapshots
parameter_list|,
name|boolean
name|refresh
parameter_list|)
block|{
name|int
name|size
init|=
name|objects
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|size
operator|!=
name|snapshots
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Counts of objects and corresponding snapshots do not match. "
operator|+
literal|"Objects count: "
operator|+
name|objects
operator|.
name|size
argument_list|()
operator|+
literal|", snapshots count: "
operator|+
name|snapshots
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|Map
name|modified
init|=
literal|null
decl_stmt|;
name|Object
name|eventPostedBy
init|=
literal|null
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// skip null objects... possible since 3.0 in some EJBQL results
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
comment|// skip HOLLOW objects as they likely were created from partial snapshots
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
continue|continue;
block|}
name|ObjectId
name|oid
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// add snapshots if refresh is forced, or if a snapshot is
comment|// missing
name|DataRow
name|cachedSnapshot
init|=
name|this
operator|.
name|snapshots
operator|.
name|get
argument_list|(
name|oid
argument_list|)
decl_stmt|;
if|if
condition|(
name|refresh
operator|||
name|cachedSnapshot
operator|==
literal|null
condition|)
block|{
name|DataRow
name|newSnapshot
init|=
operator|(
name|DataRow
operator|)
name|snapshots
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedSnapshot
operator|!=
literal|null
condition|)
block|{
comment|// use old snapshot if no changes occurred
if|if
condition|(
name|object
operator|instanceof
name|DataObject
operator|&&
name|cachedSnapshot
operator|.
name|equals
argument_list|(
name|newSnapshot
argument_list|)
condition|)
block|{
operator|(
operator|(
name|DataObject
operator|)
name|object
operator|)
operator|.
name|setSnapshotVersion
argument_list|(
name|cachedSnapshot
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|newSnapshot
operator|.
name|setReplacesVersion
argument_list|(
name|cachedSnapshot
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|modified
operator|==
literal|null
condition|)
block|{
name|modified
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|eventPostedBy
operator|=
name|object
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getGraphManager
argument_list|()
expr_stmt|;
block|}
name|modified
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|newSnapshot
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|modified
operator|!=
literal|null
condition|)
block|{
name|processSnapshotChanges
argument_list|(
name|eventPostedBy
argument_list|,
name|modified
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns current cache size.      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|snapshots
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns maximum allowed cache size.      */
specifier|public
name|int
name|maximumSize
parameter_list|()
block|{
return|return
name|maxSize
return|;
block|}
comment|/**      * Shuts down any remote notification connections, and clears internal cache.      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|stopListeners
argument_list|()
expr_stmt|;
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the name of this DataRowStore. Name allows to create EventSubjects for      * event notifications addressed to or sent from this DataRowStore.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets the name of this DataRowStore. Name allows to create EventSubjects for event      * notifications addressed to or sent from this DataRowStore.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Returns an EventManager associated with this DataRowStore.      *       * @since 1.2      */
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|eventManager
return|;
block|}
comment|/**      * Sets an EventManager associated with this DataRowStore.      *       * @since 1.2      */
specifier|public
name|void
name|setEventManager
parameter_list|(
name|EventManager
name|eventManager
parameter_list|)
block|{
if|if
condition|(
name|eventManager
operator|!=
name|this
operator|.
name|eventManager
condition|)
block|{
name|stopListeners
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
name|startListeners
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns cached snapshot or null if no snapshot is currently cached for the given      * ObjectId.      */
specifier|public
name|DataRow
name|getCachedSnapshot
parameter_list|(
name|ObjectId
name|oid
parameter_list|)
block|{
return|return
name|snapshots
operator|.
name|get
argument_list|(
name|oid
argument_list|)
return|;
block|}
comment|/**      * Returns EventSubject used by this SnapshotCache to notify of snapshot changes.      */
specifier|public
name|EventSubject
name|getSnapshotEventSubject
parameter_list|()
block|{
return|return
name|eventSubject
return|;
block|}
comment|/**      * Expires and removes all stored snapshots without sending any notification events.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|snapshots
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Evicts a snapshot from cache without generating any SnapshotEvents.      */
specifier|public
name|void
name|forgetSnapshot
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|snapshots
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Handles remote events received via EventBridge. Performs needed snapshot updates,      * and then resends the event to local listeners.      */
specifier|public
name|void
name|processRemoteEvent
parameter_list|(
name|SnapshotEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getSource
argument_list|()
operator|!=
name|remoteNotificationsHandler
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"remote event: "
operator|+
name|event
argument_list|)
expr_stmt|;
block|}
name|Collection
name|deletedSnapshotIds
init|=
name|event
operator|.
name|getDeletedIds
argument_list|()
decl_stmt|;
name|Collection
name|invalidatedSnapshotIds
init|=
name|event
operator|.
name|getInvalidatedIds
argument_list|()
decl_stmt|;
name|Map
name|diffs
init|=
name|event
operator|.
name|getModifiedDiffs
argument_list|()
decl_stmt|;
name|Collection
name|indirectlyModifiedIds
init|=
name|event
operator|.
name|getIndirectlyModifiedIds
argument_list|()
decl_stmt|;
if|if
condition|(
name|deletedSnapshotIds
operator|.
name|isEmpty
argument_list|()
operator|&&
name|invalidatedSnapshotIds
operator|.
name|isEmpty
argument_list|()
operator|&&
name|diffs
operator|.
name|isEmpty
argument_list|()
operator|&&
name|indirectlyModifiedIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"processRemoteEvent.. bogus call... no changes."
argument_list|)
expr_stmt|;
return|return;
block|}
name|processDeletedIDs
argument_list|(
name|deletedSnapshotIds
argument_list|)
expr_stmt|;
name|processInvalidatedIDs
argument_list|(
name|invalidatedSnapshotIds
argument_list|)
expr_stmt|;
name|processUpdateDiffs
argument_list|(
name|diffs
argument_list|)
expr_stmt|;
name|sendUpdateNotification
argument_list|(
name|event
operator|.
name|getPostedBy
argument_list|()
argument_list|,
name|diffs
argument_list|,
name|deletedSnapshotIds
argument_list|,
name|invalidatedSnapshotIds
argument_list|,
name|indirectlyModifiedIds
argument_list|)
expr_stmt|;
block|}
comment|/**      * Processes changes made to snapshots. Modifies internal cache state, and then sends      * the event to all listeners. Source of these changes is usually an ObjectStore.      */
specifier|public
name|void
name|processSnapshotChanges
parameter_list|(
name|Object
name|postedBy
parameter_list|,
name|Map
name|updatedSnapshots
parameter_list|,
name|Collection
name|deletedSnapshotIds
parameter_list|,
name|Collection
name|invalidatedSnapshotIds
parameter_list|,
name|Collection
name|indirectlyModifiedIds
parameter_list|)
block|{
comment|// update the internal cache, prepare snapshot event
if|if
condition|(
name|deletedSnapshotIds
operator|.
name|isEmpty
argument_list|()
operator|&&
name|invalidatedSnapshotIds
operator|.
name|isEmpty
argument_list|()
operator|&&
name|updatedSnapshots
operator|.
name|isEmpty
argument_list|()
operator|&&
name|indirectlyModifiedIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"postSnapshotsChangeEvent.. bogus call... no changes."
argument_list|)
expr_stmt|;
return|return;
block|}
name|processDeletedIDs
argument_list|(
name|deletedSnapshotIds
argument_list|)
expr_stmt|;
name|processInvalidatedIDs
argument_list|(
name|invalidatedSnapshotIds
argument_list|)
expr_stmt|;
name|Map
name|diffs
init|=
name|processUpdatedSnapshots
argument_list|(
name|updatedSnapshots
argument_list|)
decl_stmt|;
name|sendUpdateNotification
argument_list|(
name|postedBy
argument_list|,
name|diffs
argument_list|,
name|deletedSnapshotIds
argument_list|,
name|invalidatedSnapshotIds
argument_list|,
name|indirectlyModifiedIds
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processDeletedIDs
parameter_list|(
name|Collection
name|deletedSnapshotIDs
parameter_list|)
block|{
comment|// DELETED: evict deleted snapshots
if|if
condition|(
operator|!
name|deletedSnapshotIDs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|deletedSnapshotIDs
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
name|snapshots
operator|.
name|remove
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|processInvalidatedIDs
parameter_list|(
name|Collection
name|invalidatedSnapshotIds
parameter_list|)
block|{
comment|// INVALIDATED: forget snapshot, treat as expired from cache
if|if
condition|(
operator|!
name|invalidatedSnapshotIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|invalidatedSnapshotIds
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
name|snapshots
operator|.
name|remove
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
name|processUpdatedSnapshots
parameter_list|(
name|Map
name|updatedSnapshots
parameter_list|)
block|{
name|Map
name|diffs
init|=
literal|null
decl_stmt|;
comment|// MODIFIED: replace/add snapshots, generate diffs for event
if|if
condition|(
operator|!
name|updatedSnapshots
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|updatedSnapshots
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
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ObjectId
name|key
init|=
operator|(
name|ObjectId
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataRow
name|newSnapshot
init|=
operator|(
name|DataRow
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|DataRow
name|oldSnapshot
init|=
name|snapshots
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|newSnapshot
argument_list|)
decl_stmt|;
comment|// generate diff for the updated event, if this not a new
comment|// snapshot
comment|// The following cases should be handled here:
comment|// 1. There is no previously cached snapshot for a given id.
comment|// 2. There was a previously cached snapshot for a given id,
comment|// but it expired from cache and was removed. Currently
comment|// handled as (1); what are the consequences of that?
comment|// 3. There is a previously cached snapshot and it has the
comment|// *same version* as the "replacesVersion" property of the
comment|// new snapshot.
comment|// 4. There is a previously cached snapshot and it has a
comment|// *different version* from "replacesVersion" property of
comment|// the new snapshot. It means that we don't know how to merge
comment|// the two (we don't even know which one is newer due to
comment|// multithreading). Just throw out this snapshot....
if|if
condition|(
name|oldSnapshot
operator|!=
literal|null
condition|)
block|{
comment|// case 4 above... have to throw out the snapshot since
comment|// no good options exist to tell how to merge the two.
if|if
condition|(
name|oldSnapshot
operator|.
name|getVersion
argument_list|()
operator|!=
name|newSnapshot
operator|.
name|getReplacesVersion
argument_list|()
condition|)
block|{
comment|// snapshots can be huge potentially.. so print them only if the
comment|// user is expecting them to be printed
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"snapshot version changed, don't know what to do... Old: "
operator|+
name|oldSnapshot
operator|+
literal|", New: "
operator|+
name|newSnapshot
argument_list|)
expr_stmt|;
block|}
name|forgetSnapshot
argument_list|(
name|key
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Map
name|diff
init|=
name|oldSnapshot
operator|.
name|createDiff
argument_list|(
name|newSnapshot
argument_list|)
decl_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
condition|)
block|{
name|diffs
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
name|diffs
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|diffs
return|;
block|}
specifier|private
name|void
name|processUpdateDiffs
parameter_list|(
name|Map
name|diffs
parameter_list|)
block|{
comment|// apply snapshot diffs
if|if
condition|(
operator|!
name|diffs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|diffs
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
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ObjectId
name|key
init|=
operator|(
name|ObjectId
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataRow
name|oldSnapshot
init|=
name|snapshots
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldSnapshot
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|DataRow
name|newSnapshot
init|=
name|oldSnapshot
operator|.
name|applyDiff
argument_list|(
operator|(
name|DataRow
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|snapshots
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|newSnapshot
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|sendUpdateNotification
parameter_list|(
name|Object
name|postedBy
parameter_list|,
name|Map
name|diffs
parameter_list|,
name|Collection
name|deletedSnapshotIDs
parameter_list|,
name|Collection
name|invalidatedSnapshotIDs
parameter_list|,
name|Collection
name|indirectlyModifiedIds
parameter_list|)
block|{
comment|// do not send bogus events... e.g. inserted objects are not counted
if|if
condition|(
operator|(
name|diffs
operator|!=
literal|null
operator|&&
operator|!
name|diffs
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|deletedSnapshotIDs
operator|!=
literal|null
operator|&&
operator|!
name|deletedSnapshotIDs
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|invalidatedSnapshotIDs
operator|!=
literal|null
operator|&&
operator|!
name|invalidatedSnapshotIDs
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|indirectlyModifiedIds
operator|!=
literal|null
operator|&&
operator|!
name|indirectlyModifiedIds
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|SnapshotEvent
name|event
init|=
operator|new
name|SnapshotEvent
argument_list|(
name|this
argument_list|,
name|postedBy
argument_list|,
name|diffs
argument_list|,
name|deletedSnapshotIDs
argument_list|,
name|invalidatedSnapshotIDs
argument_list|,
name|indirectlyModifiedIds
argument_list|)
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"postSnapshotsChangeEvent: "
operator|+
name|event
argument_list|)
expr_stmt|;
block|}
comment|// synchronously notify listeners; leaving it up to the listeners to
comment|// register as "non-blocking" if needed.
name|eventManager
operator|.
name|postEvent
argument_list|(
name|event
argument_list|,
name|getSnapshotEventSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isNotifyingRemoteListeners
parameter_list|()
block|{
return|return
name|notifyingRemoteListeners
return|;
block|}
specifier|public
name|void
name|setNotifyingRemoteListeners
parameter_list|(
name|boolean
name|notifyingRemoteListeners
parameter_list|)
block|{
name|this
operator|.
name|notifyingRemoteListeners
operator|=
name|notifyingRemoteListeners
expr_stmt|;
block|}
comment|// deserialization support
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|in
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
comment|// restore subjects
name|this
operator|.
name|eventSubject
operator|=
name|createSubject
argument_list|()
expr_stmt|;
block|}
name|void
name|stopListeners
parameter_list|()
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remoteNotificationsHandler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|remoteNotificationsHandler
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exception shutting down EventBridge."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|remoteNotificationsHandler
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|void
name|startListeners
parameter_list|()
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|remoteNotificationsHandler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// listen to EventBridge ... must add itself as non-blocking listener
comment|// otherwise a deadlock can occur as "processRemoteEvent" will attempt
comment|// to
comment|// obtain a lock on this object when the dispatch queue is locked...
comment|// And
comment|// another commit thread may have this object locked and attempt to
comment|// lock
comment|// dispatch queue
name|eventManager
operator|.
name|addNonBlockingListener
argument_list|(
name|this
argument_list|,
literal|"processRemoteEvent"
argument_list|,
name|SnapshotEvent
operator|.
name|class
argument_list|,
name|getSnapshotEventSubject
argument_list|()
argument_list|,
name|remoteNotificationsHandler
argument_list|)
expr_stmt|;
comment|// start EventBridge - it will listen to all event sources for this
comment|// subject
name|remoteNotificationsHandler
operator|.
name|startup
argument_list|(
name|eventManager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error initializing DataRowStore."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit
