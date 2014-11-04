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
name|UseServerRuntime
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|ObjectDetachOperationIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|serverContext
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
annotation|@
name|Test
specifier|public
name|void
name|testDetachCommitted
parameter_list|()
block|{
name|EntityResolver
name|serverResover
init|=
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|EntityResolver
name|clientResolver
init|=
name|serverResover
operator|.
name|getClientEntityResolver
argument_list|()
decl_stmt|;
name|ObjectDetachOperation
name|op
init|=
operator|new
name|ObjectDetachOperation
argument_list|(
name|clientResolver
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
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
literal|456
argument_list|)
decl_stmt|;
name|MtTable1
name|so
init|=
operator|new
name|MtTable1
argument_list|()
decl_stmt|;
name|so
operator|.
name|setObjectId
argument_list|(
name|oid
argument_list|)
expr_stmt|;
name|so
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"gx"
argument_list|)
expr_stmt|;
name|so
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
name|so
operator|.
name|setObjectContext
argument_list|(
name|serverContext
argument_list|)
expr_stmt|;
name|serverContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registerNode
argument_list|(
name|oid
argument_list|,
name|so
argument_list|)
expr_stmt|;
name|Object
name|detached
init|=
name|op
operator|.
name|detach
argument_list|(
name|so
argument_list|,
name|serverResover
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|detached
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|so
argument_list|,
name|detached
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|detached
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|co
init|=
operator|(
name|ClientMtTable1
operator|)
name|detached
decl_stmt|;
name|assertEquals
argument_list|(
name|oid
argument_list|,
name|co
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"gx"
argument_list|,
name|co
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|co
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|co
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDetachHollow
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|EntityResolver
name|serverResover
init|=
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|EntityResolver
name|clientResolver
init|=
name|serverResover
operator|.
name|getClientEntityResolver
argument_list|()
decl_stmt|;
name|ObjectDetachOperation
name|op
init|=
operator|new
name|ObjectDetachOperation
argument_list|(
name|clientResolver
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
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
literal|4
argument_list|)
decl_stmt|;
name|MtTable1
name|so
init|=
operator|new
name|MtTable1
argument_list|()
decl_stmt|;
name|so
operator|.
name|setObjectId
argument_list|(
name|oid
argument_list|)
expr_stmt|;
name|so
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
expr_stmt|;
name|so
operator|.
name|setObjectContext
argument_list|(
name|serverContext
argument_list|)
expr_stmt|;
name|serverContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registerNode
argument_list|(
name|oid
argument_list|,
name|so
argument_list|)
expr_stmt|;
name|Object
name|detached
init|=
name|op
operator|.
name|detach
argument_list|(
name|so
argument_list|,
name|serverResover
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|detached
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|so
argument_list|,
name|detached
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|detached
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|co
init|=
operator|(
name|ClientMtTable1
operator|)
name|detached
decl_stmt|;
name|assertEquals
argument_list|(
name|oid
argument_list|,
name|co
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|co
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|co
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|co
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

