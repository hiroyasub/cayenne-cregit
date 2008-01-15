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
operator|.
name|jdbc
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
name|access
operator|.
name|DataContextCase
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
name|MockOperationObserver
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
name|dba
operator|.
name|DbAdapter
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
name|dba
operator|.
name|JdbcAdapter
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
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateActionTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
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
name|testProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
operator|new
name|JdbcAdapter
argument_list|()
decl_stmt|;
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
literal|"AAAAA"
argument_list|)
decl_stmt|;
name|SQLTemplateAction
name|action
init|=
operator|new
name|SQLTemplateAction
argument_list|(
name|template
argument_list|,
name|adapter
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|adapter
argument_list|,
name|action
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|template
argument_list|,
name|action
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteSelect
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
name|templateString
init|=
literal|"SELECT * FROM ARTIST WHERE ARTIST_ID = #bind($id)"
decl_stmt|;
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
name|getSQLTemplateBuilder
argument_list|()
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
name|Long
argument_list|(
literal|33005l
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
comment|// must ensure the right SQLTemplateAction is created
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|plan
operator|instanceof
name|SQLTemplateAction
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|plan
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
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
name|List
name|rows
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rows
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
name|rows
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
literal|"artist5"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectUtilDate
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
comment|// update data set to include dates....
name|setDate
argument_list|(
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
argument_list|,
literal|33006
argument_list|)
expr_stmt|;
name|String
name|templateString
init|=
literal|"SELECT #result('DATE_OF_BIRTH' 'java.util.Date' 'DOB') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
decl_stmt|;
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
name|getSQLTemplateBuilder
argument_list|()
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
literal|33006
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|plan
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
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
name|List
name|rows
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rows
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
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|java
operator|.
name|util
operator|.
name|Date
operator|.
name|class
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectSQLDate
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
comment|// update data set to include dates....
name|setDate
argument_list|(
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
argument_list|,
literal|33006
argument_list|)
expr_stmt|;
name|String
name|templateString
init|=
literal|"SELECT #result('DATE_OF_BIRTH' 'java.sql.Date' 'DOB') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
decl_stmt|;
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
name|getSQLTemplateBuilder
argument_list|()
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
literal|33006
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|plan
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
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
name|List
name|rows
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rows
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
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|java
operator|.
name|sql
operator|.
name|Date
operator|.
name|class
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectSQLTimestamp
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
comment|// update data set to include dates....
name|setDate
argument_list|(
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
argument_list|,
literal|33006
argument_list|)
expr_stmt|;
name|String
name|templateString
init|=
literal|"SELECT #result('DATE_OF_BIRTH' 'java.sql.Timestamp' 'DOB') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
decl_stmt|;
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
name|getSQLTemplateBuilder
argument_list|()
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
literal|33006
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|plan
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
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
name|List
name|rows
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rows
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
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sybase returns a Timestamp subclass... so can't test equality
name|assertTrue
argument_list|(
name|java
operator|.
name|sql
operator|.
name|Timestamp
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DOB"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|templateString
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"VALUES (#bind($id), #bind($name), #bind($dob 'DATE'))"
decl_stmt|;
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
name|Long
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
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|action
init|=
name|adapter
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
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|action
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
index|[]
name|batches
init|=
name|observer
operator|.
name|countsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|batches
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|batches
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|batches
index|[
literal|0
index|]
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
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
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
name|testExecuteUpdateNoParameters
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
literal|"delete from ARTIST where ARTIST_NAME like 'a%'"
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|action
init|=
name|adapter
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
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|action
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
index|[]
name|batches
init|=
name|observer
operator|.
name|countsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|batches
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|batches
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataContextCase
operator|.
name|artistCount
argument_list|,
name|batches
index|[
literal|0
index|]
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
specifier|public
name|void
name|testExecuteUpdateBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|templateString
init|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"VALUES (#bind($id), #bind($name), #bind($dob 'DATE'))"
decl_stmt|;
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
name|Map
name|bindings1
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|bindings1
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|bindings1
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|bindings1
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
name|Map
name|bindings2
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|bindings2
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|new
name|Long
argument_list|(
literal|33
argument_list|)
argument_list|)
expr_stmt|;
name|bindings2
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"a$$$$$"
argument_list|)
expr_stmt|;
name|bindings2
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
name|template
operator|.
name|setParameters
argument_list|(
operator|new
name|Map
index|[]
block|{
name|bindings1
block|,
name|bindings2
block|}
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|SQLAction
name|genericAction
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|genericAction
operator|instanceof
name|SQLTemplateAction
argument_list|)
expr_stmt|;
name|SQLTemplateAction
name|action
init|=
operator|(
name|SQLTemplateAction
operator|)
name|genericAction
decl_stmt|;
name|assertSame
argument_list|(
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|action
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|template
argument_list|,
name|action
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|action
operator|.
name|performAction
argument_list|(
name|c
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
index|[]
name|batches
init|=
name|observer
operator|.
name|countsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|batches
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|batches
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|batches
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|batches
index|[
literal|1
index|]
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
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getDomain
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
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|row1
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
name|bindings1
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|,
name|row1
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindings1
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|,
name|row1
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
comment|// to compare dates we need to create the binding correctly
comment|// assertEquals(bindings1.get("dob"), row.get("DATE_OF_BIRTH"));
name|Map
name|row2
init|=
operator|(
name|Map
operator|)
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|bindings2
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|,
name|row2
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindings2
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|,
name|row2
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
comment|// to compare dates we need to create the binding correctly
comment|// assertEquals(bindings2.get("dob"), row2.get("DATE_OF_BIRTH"));
block|}
specifier|public
name|void
name|testExtractTemplateString
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"A\nBC"
argument_list|)
decl_stmt|;
name|SQLTemplateAction
name|action
init|=
operator|new
name|SQLTemplateAction
argument_list|(
name|template
argument_list|,
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A BC"
argument_list|,
name|action
operator|.
name|extractTemplateString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setDate
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Date
name|date
parameter_list|,
name|int
name|artistId
parameter_list|)
block|{
name|String
name|templateString
init|=
literal|"UPDATE ARTIST SET DATE_OF_BIRTH #bindEqual($date 'DATE') "
operator|+
literal|"WHERE ARTIST_ID = #bind($id)"
decl_stmt|;
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
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
name|date
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|new
name|Integer
argument_list|(
name|artistId
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|getNode
argument_list|()
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|(
name|Query
operator|)
name|template
argument_list|)
argument_list|,
operator|new
name|QueryResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

