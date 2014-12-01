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
name|testmap
operator|.
name|Painting
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
name|SQLException
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
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SQLTemplateIT
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
specifier|private
name|TableHelper
name|tPainting
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
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ESTIMATED_PRICE"
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
name|BIGINT
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|DECIMAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateForDataMap
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
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
literal|0
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateForDataMapWithInsert
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
literal|"INSERT INTO ARTIST VALUES (15, 'Surikov', null)"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
name|sql
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|SQLTemplate
name|q2
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateForDataMapWithInsertException
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
literal|"INSERT INTO ARTIST VALUES (15, 'Surikov', null)"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
name|sql
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|SQLTemplate
name|q2
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|gotRuntimeException
init|=
literal|false
decl_stmt|;
try|try
block|{
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|gotRuntimeException
operator|=
literal|true
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"If fetchingDataRows is false and ObjectEntity not set, should be thrown exception"
argument_list|,
name|gotRuntimeException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplate_PositionalParams
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ESTIMATED_PRICE) "
operator|+
literal|"VALUES ($b, '$n', #bind($c 'INTEGER'))"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setParamsArray
argument_list|(
literal|76
argument_list|,
literal|"The Fiddler"
argument_list|,
literal|10005
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Fiddler"
argument_list|,
name|tPainting
operator|.
name|getString
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|76
argument_list|,
name|tPainting
operator|.
name|getInt
argument_list|(
literal|"PAINTING_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10005.d
argument_list|,
name|tPainting
operator|.
name|getDouble
argument_list|(
literal|"ESTIMATED_PRICE"
argument_list|)
argument_list|,
literal|0.001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplate_PositionalParams_RepeatingVars
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ESTIMATED_PRICE) "
operator|+
literal|"VALUES ($b, '$n', #bind($b 'INTEGER'))"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setParamsArray
argument_list|(
literal|11
argument_list|,
literal|"The Fiddler"
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Fiddler"
argument_list|,
name|tPainting
operator|.
name|getString
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|tPainting
operator|.
name|getInt
argument_list|(
literal|"PAINTING_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11.d
argument_list|,
name|tPainting
operator|.
name|getDouble
argument_list|(
literal|"ESTIMATED_PRICE"
argument_list|)
argument_list|,
literal|0.001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testSQLTemplate_PositionalParams_ToFewParams
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ESTIMATED_PRICE) "
operator|+
literal|"VALUES ($b, '$n', #bind($c 'INTEGER'))"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setParamsArray
argument_list|(
literal|11
argument_list|,
literal|"The Fiddler"
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplate_PositionalParams_ToManyParams
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ESTIMATED_PRICE) "
operator|+
literal|"VALUES ($b, '$n', #bind($b 'INTEGER'))"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setParamsArray
argument_list|(
literal|11
argument_list|,
literal|"The Fiddler"
argument_list|,
literal|2345
argument_list|,
literal|333
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception not thrown on parameter length mismatch"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

