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
name|java
operator|.
name|sql
operator|.
name|Connection
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
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
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
name|SQLAction
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * DataNode test cases.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataNodeQueriesTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCreatePkSupportForMapEntities
parameter_list|()
throws|throws
name|Exception
block|{
name|getAccessStack
argument_list|()
operator|.
name|createPKSupport
argument_list|()
expr_stmt|;
name|DataNode
name|node
init|=
name|getNode
argument_list|()
decl_stmt|;
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
name|generatePkForDbEntity
argument_list|(
name|node
argument_list|,
name|artistEnt
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
name|generatePkForDbEntity
argument_list|(
name|node
argument_list|,
name|exhibitEnt
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|bindings
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
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
name|getNode
argument_list|()
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
name|MockOperationObserver
name|checkObserver
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|SelectQuery
name|checkQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|getDomain
argument_list|()
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|checkQuery
argument_list|)
argument_list|,
name|checkObserver
argument_list|)
expr_stmt|;
name|List
name|data
init|=
name|checkObserver
operator|.
name|rowsForQuery
argument_list|(
name|checkQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|bindings
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindings
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
comment|// to compare dates we need to create the binding correctly
comment|// assertEquals(bindings.get("dob"), row.get("DATE_OF_BIRTH"));
block|}
specifier|public
name|void
name|testPerfomQueriesSelectingSQLTemplate1
parameter_list|()
throws|throws
name|Exception
block|{
name|getAccessStack
argument_list|()
operator|.
name|createTestData
argument_list|(
name|DataContextCase
operator|.
name|class
argument_list|,
literal|"testArtists"
argument_list|,
literal|null
argument_list|)
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
name|getNode
argument_list|()
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
name|DataContextCase
operator|.
name|artistCount
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
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
operator|new
name|Integer
argument_list|(
literal|33003
argument_list|)
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
specifier|public
name|void
name|testPerfomQueriesSelectingSQLTemplate2
parameter_list|()
throws|throws
name|Exception
block|{
name|getAccessStack
argument_list|()
operator|.
name|createTestData
argument_list|(
name|DataContextCase
operator|.
name|class
argument_list|,
literal|"testArtists"
argument_list|,
literal|null
argument_list|)
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
name|getSQLTemplateBuilder
argument_list|()
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
name|getNode
argument_list|()
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
name|DataContextCase
operator|.
name|artistCount
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
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
literal|"Can't find ARTIST_ID: "
operator|+
name|row
argument_list|,
literal|33003
argument_list|,
name|id
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerfomQueriesSelectingSQLTemplateAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|getAccessStack
argument_list|()
operator|.
name|createTestData
argument_list|(
name|DataContextCase
operator|.
name|class
argument_list|,
literal|"testArtists"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int' 'A') FROM ARTIST ORDER BY ARTIST_ID"
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
name|getNode
argument_list|()
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
name|DataContextCase
operator|.
name|artistCount
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
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
operator|new
name|Integer
argument_list|(
literal|33003
argument_list|)
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
name|runSQL
argument_list|(
name|templateString
argument_list|)
expr_stmt|;
block|}
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
name|runSQL
argument_list|(
name|templateString
argument_list|)
expr_stmt|;
block|}
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
name|runSQL
argument_list|(
name|templateString
argument_list|)
expr_stmt|;
block|}
comment|/**      * Testing that SQLTemplate that is entered on multiple lines can be executed. CAY-269      * shows that some databases are very picky about it.      */
specifier|private
name|void
name|runSQL
parameter_list|(
name|String
name|templateString
parameter_list|)
throws|throws
name|Exception
block|{
name|SQLTemplate
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
name|SQLAction
name|action
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|action
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

