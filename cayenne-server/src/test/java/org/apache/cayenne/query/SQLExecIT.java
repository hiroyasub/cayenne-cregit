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
name|query
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
name|ServerCase
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
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SQLExecIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|test_DataMapNameRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|inserted
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"testmap"
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME) VALUES (1, 'a')"
argument_list|)
operator|.
name|update
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inserted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_DefaultRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|inserted
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME) VALUES (1, 'a')"
argument_list|)
operator|.
name|update
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inserted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ParamsArray_Single
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|inserted
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME) VALUES (1, #bind($name))"
argument_list|)
operator|.
name|paramsArray
argument_list|(
literal|"a3"
argument_list|)
operator|.
name|update
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inserted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a3"
argument_list|,
name|dbHelper
operator|.
name|getString
argument_list|(
literal|"ARTIST"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ParamsArray_Multiple
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|inserted
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME) VALUES (#bind($id), #bind($name))"
argument_list|)
operator|.
name|paramsArray
argument_list|(
literal|55
argument_list|,
literal|"a3"
argument_list|)
operator|.
name|update
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inserted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|55l
argument_list|,
name|dbHelper
operator|.
name|getLong
argument_list|(
literal|"ARTIST"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a3"
argument_list|,
name|dbHelper
operator|.
name|getString
argument_list|(
literal|"ARTIST"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

