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
name|access
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
name|DbEntity
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
name|query
operator|.
name|SQLTemplate
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
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|util
operator|.
name|SQLTemplateCustomizer
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
name|Date
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|DataNodeQueriesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|SQLTemplateCustomizer
name|sqlTemplateCustomizer
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
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
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createFourArtists
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|201
argument_list|,
literal|"artist4"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|3001
argument_list|,
literal|"artist5"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatePkSupportForMapEntities
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|artistEnt
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|artistEnt
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DbEntity
name|exhibitEnt
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"EXHIBIT"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|exhibitEnt
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerfomQueriesSQLTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"VALUES (#bind($id), #bind($name), #bind($dob 'DATE'))"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|1l
argument_list|)
expr_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|observer
operator|.
name|countsForQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|observer
operator|.
name|countsForQuery
argument_list|(
name|query
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// check the data
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tArtist
operator|.
name|getRowCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|tArtist
operator|.
name|getLong
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a1"
argument_list|,
name|tArtist
operator|.
name|getString
argument_list|(
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
name|testPerfomQueriesSelectingSQLTemplate1
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
expr_stmt|;
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|data
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|201
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerfomQueriesSelectingSQLTemplate2
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
expr_stmt|;
name|String
name|template
init|=
literal|"SELECT * FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|sqlTemplateCustomizer
operator|.
name|updateSQLTemplate
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|data
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|id
init|=
operator|(
name|Number
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|201
argument_list|,
name|id
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerfomQueriesSelectingSQLTemplateAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
expr_stmt|;
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int' 'A') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|Query
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|data
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|201
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRunMultiLineSQLTemplateUNIX
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|templateString
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME)"
operator|+
literal|"\n"
operator|+
literal|"VALUES (1, 'A')"
decl_stmt|;
name|Query
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|templateString
argument_list|)
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|template
argument_list|)
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRunMultiLineSQLTemplateWindows
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|templateString
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME)"
operator|+
literal|"\r\n"
operator|+
literal|"VALUES (1, 'A')"
decl_stmt|;
name|Query
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|templateString
argument_list|)
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|template
argument_list|)
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRunMultiLineSQLTemplateMac
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|templateString
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME)"
operator|+
literal|"\r"
operator|+
literal|"VALUES (1, 'A')"
decl_stmt|;
name|Query
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|templateString
argument_list|)
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|template
argument_list|)
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

