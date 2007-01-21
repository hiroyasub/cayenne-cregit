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
name|ClientServerChannel
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
name|ObjectIdQuery
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
name|SelectQuery
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
name|RemoteIncrementalFaultList
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
name|service
operator|.
name|LocalConnection
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable2
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
name|MtTable1
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
name|AccessStack
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
name|CayenneCase
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
name|CayenneResources
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
name|UnitLocalConnection
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneContextWithDataContextTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|public
name|void
name|testCreateFault
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
comment|// must attach to the real channel...
name|ClientConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|fault
init|=
name|context
operator|.
name|createFault
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o
init|=
operator|(
name|ClientMtTable1
operator|)
name|fault
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
comment|// make sure we haven't tripped the fault yet
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// try tripping fault
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateBadFault
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
comment|// this clears domain cache
name|createDataContext
argument_list|()
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|fault
init|=
name|context
operator|.
name|createFault
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o
init|=
operator|(
name|ClientMtTable1
operator|)
name|fault
decl_stmt|;
comment|// try tripping fault
try|try
block|{
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"resolving bad fault should've thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FaultFailureException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testPrefetchingToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|ObjectId
name|prefetchedId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable2
operator|.
name|GLOBAL_ATTRIBUTE_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable2
operator|.
name|TABLE1_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|results
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
name|ClientMtTable2
name|o
init|=
operator|(
name|ClientMtTable2
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1
init|=
name|o
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|prefetchedId
argument_list|,
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPrefetchingToOneNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetchingToOneNull"
argument_list|)
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable2
operator|.
name|TABLE1_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|o
init|=
operator|(
name|ClientMtTable2
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPrefetchingToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children1
init|=
name|o1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|children1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|children1
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
name|ClientMtTable2
name|o
init|=
operator|(
name|ClientMtTable2
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: fixme...
comment|// assertEquals(o1, o.getTable1());
block|}
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPerformPaginatedQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testPerformPaginatedQuery"
argument_list|)
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|instanceof
name|RemoteIncrementalFaultList
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPrefetchingToManyEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o2
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children2
init|=
name|o2
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|children2
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|children2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testOIDQueryInterception
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
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
name|ClientMtTable1
name|o
init|=
operator|(
name|ClientMtTable1
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
comment|// fetch new
name|ObjectIdQuery
name|q1
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|o
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// fetch committed
name|ObjectIdQuery
name|q2
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|o
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|connection
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

