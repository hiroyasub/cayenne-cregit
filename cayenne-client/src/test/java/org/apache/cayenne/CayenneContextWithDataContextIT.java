begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|access
operator|.
name|DataContext
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
name|ObjectSelect
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
name|reflect
operator|.
name|LifecycleCallbackRegistry
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|test
operator|.
name|parallel
operator|.
name|ParallelTestContainer
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
name|di
operator|.
name|DataChannelInterceptor
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
name|junit
operator|.
name|Before
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
name|java
operator|.
name|sql
operator|.
name|Types
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
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|CayenneContextWithDataContextIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|clientContext
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
argument_list|(
name|ClientCase
operator|.
name|ROP_CLIENT_KEY
argument_list|)
specifier|private
name|DataChannelInterceptor
name|clientServerInterceptor
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ClientServerChannel
name|clientServerChannel
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|setColumns
argument_list|(
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"SERVER_ATTRIBUTE1"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|setColumns
argument_list|(
literal|"TABLE2_ID"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoMtTable1sAnd2sDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"g2"
argument_list|,
literal|"s2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"g1"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createEightMtTable1s
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"g"
operator|+
name|i
argument_list|,
literal|"s"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalCacheStaysLocal
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|ClientMtTable1
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|query
operator|.
name|select
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|results
argument_list|,
name|clientContext
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|query
operator|.
name|getMetaData
argument_list|(
name|clientContext
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddToList
parameter_list|()
block|{
name|ClientMtTable1
name|t1
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|t2
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|addToTable2Array
argument_list|(
name|t2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|t2
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
comment|// do it again to make sure action can handle series of changes
name|ClientMtTable1
name|t3
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|t4
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t3
operator|.
name|addToTable2Array
argument_list|(
name|t4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t3
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t3
argument_list|,
name|t4
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetValueHolder
parameter_list|()
block|{
name|ClientMtTable1
name|t1
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|t2
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t2
operator|.
name|setTable1
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|t2
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostAddCallback
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|callbackRegistry
init|=
name|clientServerChannel
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
specifier|final
name|boolean
index|[]
name|flag
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
try|try
block|{
name|callbackRegistry
operator|.
name|addListener
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|LifecycleListener
argument_list|()
block|{
specifier|public
name|void
name|postLoad
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postPersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postAdd
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|flag
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|preRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|preUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|prePersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|flag
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|flag
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callbackRegistry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostAddOnObjectCallback
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataContext
name|serverContext
init|=
operator|(
name|DataContext
operator|)
name|clientServerChannel
operator|.
name|getParentChannel
argument_list|()
decl_stmt|;
name|LifecycleCallbackRegistry
name|callbackRegistry
init|=
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
try|try
block|{
name|callbackRegistry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
literal|"prePersistMethod"
argument_list|)
expr_stmt|;
specifier|final
name|Persistent
name|clientObject
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
operator|new
name|ParallelTestContainer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
block|{
comment|// find peer
name|MtTable1
name|peer
init|=
operator|(
name|MtTable1
operator|)
name|serverContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|clientObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|peer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|peer
operator|.
name|isPrePersisted
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callbackRegistry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPreRemoveCallback
parameter_list|()
block|{
comment|// an exception was triggered within POST_LOAD callback
name|LifecycleCallbackRegistry
name|callbackRegistry
init|=
name|clientServerChannel
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
specifier|final
name|boolean
index|[]
name|flag
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
try|try
block|{
name|callbackRegistry
operator|.
name|addListener
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|LifecycleListener
argument_list|()
block|{
specifier|public
name|void
name|postLoad
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postPersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postAdd
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|preRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|flag
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|preUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
specifier|public
name|void
name|prePersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|object
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|flag
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|flag
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|flag
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callbackRegistry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRollbackChanges
parameter_list|()
block|{
name|ClientMtTable1
name|o
init|=
name|clientContext
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
literal|"1"
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|o
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clientContext
operator|.
name|modifiedObjects
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
name|testCreateFault
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|fault
init|=
name|clientContext
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
name|clientContext
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
annotation|@
name|Test
specifier|public
name|void
name|testCreateBadFault
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Object
name|fault
init|=
name|clientContext
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
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchingToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
specifier|final
name|ObjectId
name|prefetchedId
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable2
argument_list|>
name|q
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|GLOBAL_ATTRIBUTE
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable2
operator|.
name|TABLE1
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
name|results
init|=
name|q
operator|.
name|select
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
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
for|for
control|(
name|ClientMtTable2
name|o
range|:
name|results
control|)
block|{
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
name|clientContext
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
name|clientContext
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchingToOneNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|15
argument_list|,
literal|null
argument_list|,
literal|"g3"
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable2
argument_list|>
name|q
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|TABLE1
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
name|results
init|=
name|q
operator|.
name|select
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
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
name|clientContext
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchingToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|q
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|GLOBAL_ATTRIBUTE1
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|results
init|=
name|q
operator|.
name|select
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|ClientMtTable1
name|o1
init|=
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
name|clientContext
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
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
for|for
control|(
name|ClientMtTable2
name|o
range|:
name|children1
control|)
block|{
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
name|clientContext
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: fixme... reverse relationship is not connected and will
comment|// cause a fetch
comment|// assertEquals(o1, o.getTable1());
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformPaginatedQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createEightMtTable1s
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
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
argument_list|<
name|ClientMtTable1
argument_list|>
name|objects
init|=
name|query
operator|.
name|select
argument_list|(
name|clientContext
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
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchingToManyEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|q
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|GLOBAL_ATTRIBUTE1
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|results
init|=
name|q
operator|.
name|select
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|ClientMtTable1
name|o2
init|=
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
name|clientContext
argument_list|,
name|o2
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOIDQueryInterception
parameter_list|()
block|{
specifier|final
name|ClientMtTable1
name|o
init|=
name|clientContext
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
specifier|final
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
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|clientContext
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
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// fetch committed
specifier|final
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
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|clientContext
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

