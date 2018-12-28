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
name|di
operator|.
name|Inject
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
name|MockEventManager
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
name|map
operator|.
name|DataMap
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
name|remote
operator|.
name|BootstrapMessage
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
name|remote
operator|.
name|ClientChannel
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
name|remote
operator|.
name|ClientConnection
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
name|remote
operator|.
name|ClientMessage
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable1
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
name|unit
operator|.
name|di
operator|.
name|client
operator|.
name|ClientCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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
name|GenericResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|serverContext
decl_stmt|;
specifier|private
name|DefaultEventManager
name|eventManager
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|cleanUp
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
name|shutdown
argument_list|()
expr_stmt|;
name|eventManager
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|()
decl_stmt|;
comment|// test default property parameters
name|assertNotNull
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|channel
argument_list|)
expr_stmt|;
name|MockDataChannel
name|channel
init|=
operator|new
name|MockDataChannel
argument_list|()
decl_stmt|;
name|context
operator|.
name|setChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|channel
argument_list|,
name|context
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testChannel
parameter_list|()
block|{
name|MockDataChannel
name|channel
init|=
operator|new
name|MockDataChannel
argument_list|()
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|channel
argument_list|,
name|context
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitUnchanged
parameter_list|()
block|{
name|MockDataChannel
name|channel
init|=
operator|new
name|MockDataChannel
argument_list|()
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
comment|// no context changes so no connector access is expected
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|channel
operator|.
name|getRequestObjects
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitCommandExecuted
parameter_list|()
block|{
name|MockDataChannel
name|channel
init|=
operator|new
name|MockDataChannel
argument_list|(
name|mock
argument_list|(
name|GraphDiff
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|channel
operator|.
name|setEntityResolver
argument_list|(
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClientEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
comment|// test that a command is being sent via connector on commit...
name|context
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|nodePropertyChanged
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|,
literal|"x"
argument_list|,
literal|"y"
argument_list|,
literal|"z"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|channel
operator|.
name|getRequestObjects
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// expect a sync/commit chain
name|Object
name|mainMessage
init|=
name|channel
operator|.
name|getRequestObjects
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|mainMessage
operator|instanceof
name|GraphDiff
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitChangesNew
parameter_list|()
block|{
specifier|final
name|CompoundDiff
name|diff
init|=
operator|new
name|CompoundDiff
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|newObjectId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"test"
argument_list|,
literal|"key"
argument_list|,
literal|"generated"
argument_list|)
decl_stmt|;
name|eventManager
operator|=
operator|new
name|DefaultEventManager
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// test that ids that are passed back are actually propagated to the
comment|// right
comment|// objects...
name|MockDataChannel
name|channel
init|=
operator|new
name|MockDataChannel
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
return|return
name|diff
return|;
block|}
comment|// must provide a channel with working event manager
annotation|@
name|Override
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|eventManager
return|;
block|}
block|}
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test_entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
name|MockPersistentObject
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|entities
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|context
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|entities
argument_list|)
argument_list|)
expr_stmt|;
name|Persistent
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MockPersistentObject
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// record change here to make it available to the anonymous connector
comment|// method..
name|diff
operator|.
name|add
argument_list|(
operator|new
name|NodeIdChangeOperation
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|newObjectId
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that a generated object ID is assigned back to the object...
name|assertNotSame
argument_list|(
name|newObjectId
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|newObjectId
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|graphManager
operator|.
name|getNode
argument_list|(
name|newObjectId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewObject
parameter_list|()
block|{
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
operator|new
name|MockDataChannel
argument_list|()
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test_entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
name|MockPersistentObject
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|entities
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|context
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|entities
argument_list|)
argument_list|)
expr_stmt|;
name|Persistent
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MockPersistentObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|MockPersistentObject
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|dirtyNodes
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|)
operator|.
name|contains
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObject
parameter_list|()
block|{
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
operator|new
name|MockDataChannel
argument_list|()
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test_entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
name|MockPersistentObject
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|entities
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|context
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|entities
argument_list|)
argument_list|)
expr_stmt|;
comment|// TRANSIENT ... should quietly ignore it
name|Persistent
name|transientObject
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|transientObject
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|transientObject
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// NEW ... should make it TRANSIENT
comment|// create via context to make sure that object store would register it
name|Persistent
name|newObject
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MockPersistentObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|newObject
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|newObject
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|newObject
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|newObject
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|dirtyNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|newObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// see CAY-547 for details...
name|assertFalse
argument_list|(
name|context
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|dirtyNodes
argument_list|()
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// COMMITTED
name|Persistent
name|committed
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|committed
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
name|committed
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"test_entity"
argument_list|,
literal|"key"
argument_list|,
literal|"value1"
argument_list|)
argument_list|)
expr_stmt|;
name|committed
operator|.
name|setObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|committed
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|committed
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// MODIFIED
name|Persistent
name|modified
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|modified
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
name|modified
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"test_entity"
argument_list|,
literal|"key"
argument_list|,
literal|"value2"
argument_list|)
argument_list|)
expr_stmt|;
name|modified
operator|.
name|setObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|modified
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// DELETED
name|Persistent
name|deleted
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|deleted
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|)
expr_stmt|;
name|deleted
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"test_entity"
argument_list|,
literal|"key"
argument_list|,
literal|"value3"
argument_list|)
argument_list|)
expr_stmt|;
name|deleted
operator|.
name|setObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|deleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|committed
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeforePropertyReadShouldInflateHollow
parameter_list|()
block|{
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|inflated
init|=
operator|new
name|ClientMtTable1
argument_list|()
decl_stmt|;
name|inflated
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
name|inflated
operator|.
name|setObjectId
argument_list|(
name|gid
argument_list|)
expr_stmt|;
name|inflated
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|ClientConnection
name|connection
init|=
name|mock
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|connection
operator|.
name|sendMessage
argument_list|(
operator|(
name|ClientMessage
operator|)
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|ClientMessage
name|arg
init|=
operator|(
name|ClientMessage
operator|)
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|arg
operator|instanceof
name|BootstrapMessage
condition|)
block|{
return|return
operator|new
name|EntityResolver
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|GenericResponse
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|inflated
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|,
literal|false
argument_list|,
operator|new
name|MockEventManager
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// check that a HOLLOW object is infalted on "beforePropertyRead"
name|ClientMtTable1
name|hollow
init|=
operator|new
name|ClientMtTable1
argument_list|()
decl_stmt|;
name|hollow
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
expr_stmt|;
name|hollow
operator|.
name|setObjectId
argument_list|(
name|gid
argument_list|)
expr_stmt|;
specifier|final
name|boolean
index|[]
name|selectExecuted
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|selectExecuted
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setEntityResolver
argument_list|(
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClientEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|graphManager
operator|.
name|registerNode
argument_list|(
name|hollow
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|hollow
argument_list|)
expr_stmt|;
comment|// testing this...
name|context
operator|.
name|prepareForAccess
argument_list|(
name|hollow
argument_list|,
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selectExecuted
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|hollow
argument_list|,
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|gid
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|inflated
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|,
name|hollow
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|hollow
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeforeHollowDeleteShouldChangeStateToCommited
parameter_list|()
block|{
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|inflated
init|=
operator|new
name|ClientMtTable1
argument_list|()
decl_stmt|;
name|inflated
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
name|inflated
operator|.
name|setObjectId
argument_list|(
name|gid
argument_list|)
expr_stmt|;
name|inflated
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|ClientConnection
name|connection
init|=
name|mock
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|connection
operator|.
name|sendMessage
argument_list|(
operator|(
name|ClientMessage
operator|)
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|ClientMessage
name|arg
init|=
operator|(
name|ClientMessage
operator|)
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|arg
operator|instanceof
name|BootstrapMessage
condition|)
block|{
return|return
operator|new
name|EntityResolver
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|GenericResponse
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|inflated
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|,
literal|false
argument_list|,
operator|new
name|MockEventManager
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|context
operator|.
name|setEntityResolver
argument_list|(
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClientEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|hollow
init|=
name|context
operator|.
name|localObject
argument_list|(
name|inflated
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|hollow
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// testing this...
name|context
operator|.
name|deleteObjects
argument_list|(
name|hollow
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|hollow
argument_list|,
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|gid
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|inflated
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|,
name|hollow
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|hollow
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

