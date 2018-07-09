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
name|QueryResult
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
name|UnitDbAdapter
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
name|assertTrue
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
name|Inject
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
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
name|testReturnGeneratedKeys
parameter_list|()
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
name|QueryResult
name|response
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"testmap"
argument_list|,
literal|"INSERT INTO GENERATED_COLUMN (NAME) VALUES ('Surikov')"
argument_list|)
operator|.
name|setReturnGeneratedKeys
argument_list|(
literal|true
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|response
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|QueryResult
name|response1
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"testmap"
argument_list|,
literal|"INSERT INTO GENERATED_COLUMN (NAME) VALUES ('Sidorov')"
argument_list|)
operator|.
name|setReturnGeneratedKeys
argument_list|(
literal|false
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|test_ExecuteSelect
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
name|QueryResult
name|result
init|=
name|SQLExec
operator|.
name|query
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|firstList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|result
operator|.
name|firstList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|unitDbAdapter
operator|.
name|isLowerCaseNames
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artist_id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"artist_id"
argument_list|)
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"artist_name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|55L
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

