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
name|Cayenne
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
name|CayenneContext
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
name|ObjectContext
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
name|configuration
operator|.
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|UnitTestClosure
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
name|assertSame
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
name|ShallowMergeOperation_ClientIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|ClientRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
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
block|}
specifier|private
name|void
name|createMtTable1DataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|33001
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
literal|33002
argument_list|,
literal|"g2"
argument_list|,
literal|"s2"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"g3"
argument_list|,
literal|"s3"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"g4"
argument_list|,
literal|"s4"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMerge_Relationship
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|childContext
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|ShallowMergeOperation
name|op
init|=
operator|new
name|ShallowMergeOperation
argument_list|(
name|childContext
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|_new
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable2
name|_new2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|_new
operator|.
name|addToTable2Array
argument_list|(
name|_new2
argument_list|)
expr_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|ClientMtTable2
name|child2
init|=
name|op
operator|.
name|merge
argument_list|(
name|_new2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|child2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|child2
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|child2
operator|.
name|getTable1
argument_list|()
operator|.
name|getPersistenceState
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
name|testMerge_NoOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|childContext
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|ShallowMergeOperation
name|op
init|=
operator|new
name|ShallowMergeOperation
argument_list|(
name|childContext
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|modified
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
specifier|final
name|ClientMtTable1
name|peerModified
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|childContext
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|modified
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|modified
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"M1"
argument_list|)
expr_stmt|;
name|peerModified
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"M2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|peerModified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Persistent
name|peerModified2
init|=
name|op
operator|.
name|merge
argument_list|(
name|modified
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|peerModified
argument_list|,
name|peerModified2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|peerModified2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M2"
argument_list|,
name|peerModified
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M1"
argument_list|,
name|modified
operator|.
name|getGlobalAttribute1
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
name|testMerge_PersistenceStates
parameter_list|()
throws|throws
name|Exception
block|{
name|createMtTable1DataSet
argument_list|()
expr_stmt|;
specifier|final
name|ObjectContext
name|childContext
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|ShallowMergeOperation
name|op
init|=
operator|new
name|ShallowMergeOperation
argument_list|(
name|childContext
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|_new
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|hollow
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ClientMtTable1
operator|.
name|class
argument_list|,
literal|33001
argument_list|)
decl_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|hollow
argument_list|)
expr_stmt|;
specifier|final
name|ClientMtTable1
name|committed
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ClientMtTable1
operator|.
name|class
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
specifier|final
name|ClientMtTable1
name|modified
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ClientMtTable1
operator|.
name|class
argument_list|,
literal|33003
argument_list|)
decl_stmt|;
name|modified
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
specifier|final
name|ClientMtTable1
name|deleted
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ClientMtTable1
operator|.
name|class
argument_list|,
literal|33004
argument_list|)
decl_stmt|;
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
name|HOLLOW
argument_list|,
name|hollow
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|committed
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|deleted
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|_new
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Persistent
name|newPeer
init|=
name|op
operator|.
name|merge
argument_list|(
name|_new
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|_new
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|newPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|newPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|newPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|_new
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|hollowPeer
init|=
name|op
operator|.
name|merge
argument_list|(
name|hollow
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|hollowPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hollow
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|hollowPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|hollowPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|hollow
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|committedPeer
init|=
name|op
operator|.
name|merge
argument_list|(
name|committed
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|committedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|committed
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|committedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|committedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|committed
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|modifiedPeer
init|=
name|op
operator|.
name|merge
argument_list|(
name|modified
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|modifiedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|modified
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|modifiedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|modifiedPeer
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|modifiedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|modified
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|deletedPeer
init|=
name|op
operator|.
name|merge
argument_list|(
name|deleted
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|deletedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|deleted
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|deletedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|deletedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|deleted
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

