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
name|testdo
operator|.
name|mt
operator|.
name|MtTable2
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
name|CayenneContextRelationshipsIT
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
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|serverContext
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
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLostUncommittedToOneModifications_Client
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
literal|"G1"
argument_list|,
literal|"S1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"G2"
argument_list|,
literal|"S2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|33
argument_list|,
literal|1
argument_list|,
literal|"GX"
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|o
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|,
literal|33
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|r2
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
literal|2
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|r1
init|=
name|o
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|o
operator|.
name|setTable1
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r2
argument_list|,
name|o
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
comment|// see CAY-1757 - this used to reset our changes
name|assertFalse
argument_list|(
name|r1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|contains
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r2
argument_list|,
name|o
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
name|testLostUncommittedToOneModifications_Server
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
literal|"G1"
argument_list|,
literal|"S1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"G2"
argument_list|,
literal|"S2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|33
argument_list|,
literal|1
argument_list|,
literal|"GX"
argument_list|)
expr_stmt|;
name|MtTable2
name|o
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|serverContext
argument_list|,
name|MtTable2
operator|.
name|class
argument_list|,
literal|33
argument_list|)
decl_stmt|;
name|MtTable1
name|r2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|serverContext
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MtTable1
name|r1
init|=
name|o
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|o
operator|.
name|setTable1
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r2
argument_list|,
name|o
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r1
operator|.
name|getTable2Array
argument_list|()
operator|.
name|contains
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r2
argument_list|,
name|o
operator|.
name|getTable1
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

