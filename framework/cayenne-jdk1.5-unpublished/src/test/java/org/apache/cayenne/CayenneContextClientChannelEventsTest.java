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
name|configuration
operator|.
name|rop
operator|.
name|client
operator|.
name|ClientModule
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable4
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
name|ClientMtTable5
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
name|client
operator|.
name|ClientRuntimeProperty
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

begin_comment
comment|/**  * Tests peer context synchronization via ClientChannel events.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
annotation|@
name|ClientRuntimeProperty
argument_list|(
block|{
name|ClientModule
operator|.
name|CHANNEL_EVENTS
block|,
literal|"true"
block|}
argument_list|)
specifier|public
class|class
name|CayenneContextClientChannelEventsTest
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ClientRuntime
name|runtime
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable2
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable4
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable5
decl_stmt|;
specifier|private
name|TableHelper
name|tMtJoin45
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_JOIN45"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE4"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE5"
argument_list|)
expr_stmt|;
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
expr_stmt|;
name|tMtTable4
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE4"
argument_list|)
expr_stmt|;
name|tMtTable4
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|tMtTable5
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE5"
argument_list|)
expr_stmt|;
name|tMtTable5
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|tMtJoin45
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_JOIN45"
argument_list|)
expr_stmt|;
name|tMtJoin45
operator|.
name|setColumns
argument_list|(
literal|"TABLE4_ID"
argument_list|,
literal|"TABLE5_ID"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncNewObject
parameter_list|()
throws|throws
name|Exception
block|{
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|c2
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
comment|// now fetch it fresh
name|o2
operator|=
operator|(
name|ClientMtTable1
operator|)
name|c2
operator|.
name|performQuery
argument_list|(
operator|new
name|ObjectIdQuery
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|o2
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
name|o2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncNewDeletedObject
parameter_list|()
throws|throws
name|Exception
block|{
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
comment|// insert, then delete - this shouldn't propagate via an event.
name|ClientMtTable1
name|o1
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|c1
operator|.
name|deleteObjects
argument_list|(
name|o1
argument_list|)
expr_stmt|;
comment|// introduce some other change so that commit can go ahead...
name|ClientMtTable1
name|o1x
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1x
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|c2
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncNewObjectIntoDirtyContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
comment|// make sure c2 has uncommitted changes
name|c2
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|c2
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
comment|// now fetch it fresh
name|o2
operator|=
operator|(
name|ClientMtTable1
operator|)
name|c2
operator|.
name|performQuery
argument_list|(
operator|new
name|ObjectIdQuery
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|o2
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
name|o2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncSimpleProperty
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
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c2
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|o1
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|o2
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|o2
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncToOneRelationship
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
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ClientMtTable2
name|o1
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
literal|"TABLE2_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|o2
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c2
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
literal|"TABLE2_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|o1
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|o2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1r
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setTable1
argument_list|(
name|o1r
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g2"
argument_list|,
name|o2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1r
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|o2
operator|.
name|getTable1
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncToManyRelationship
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
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c2
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|o1r
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1r
operator|.
name|setGlobalAttribute
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|addToTable2Array
argument_list|(
name|o1r
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o2
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncToManyRelationship1
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
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// do not resolve objects in question in the second context and see if the merge
comment|// causes any issues...
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|o1r
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1r
operator|.
name|setGlobalAttribute
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|addToTable2Array
argument_list|(
name|o1r
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c2
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o2
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncManyToManyRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable4
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tMtTable5
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tMtTable5
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|tMtJoin45
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tMtJoin45
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ClientMtTable4
name|o1
init|=
operator|(
name|ClientMtTable4
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable4"
argument_list|,
literal|"ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable4
name|o2
init|=
operator|(
name|ClientMtTable4
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c2
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable4"
argument_list|,
literal|"ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o1
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o2
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable5
name|o1r
init|=
operator|(
name|ClientMtTable5
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|c1
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable5"
argument_list|,
literal|"ID"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|o1
operator|.
name|removeFromTable5s
argument_list|(
name|o1r
argument_list|)
expr_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSyncManyToManyRelationship1
parameter_list|()
throws|throws
name|Exception
block|{
name|CayenneContext
name|c1
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|CayenneContext
name|c2
init|=
operator|(
name|CayenneContext
operator|)
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ClientMtTable4
name|o1
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable4
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientMtTable5
name|o1r
init|=
name|c1
operator|.
name|newObject
argument_list|(
name|ClientMtTable5
operator|.
name|class
argument_list|)
decl_stmt|;
name|c1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientMtTable4
name|o2
init|=
operator|(
name|ClientMtTable4
operator|)
name|c2
operator|.
name|localObject
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ClientMtTable5
name|o2r
init|=
operator|(
name|ClientMtTable5
operator|)
name|c2
operator|.
name|localObject
argument_list|(
name|o1r
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|o2
operator|.
name|addToTable5s
argument_list|(
name|o2r
argument_list|)
expr_stmt|;
name|c2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getTable5s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c1
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

