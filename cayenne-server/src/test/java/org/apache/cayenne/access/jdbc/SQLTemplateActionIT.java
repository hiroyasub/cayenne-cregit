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
name|access
operator|.
name|jdbc
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
name|access
operator|.
name|DataNode
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
name|CapsStrategy
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
name|ObjectSelect
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
name|SortOrder
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
name|ServerCaseDataSourceFactory
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SQLTemplateActionIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JdbcAdapter
name|adapter
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|objectContext
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
argument_list|,
literal|"DATE_OF_BIRTH"
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
name|Date
name|date
init|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"artist2"
argument_list|,
name|date
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|"artist3"
argument_list|,
name|date
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|201
argument_list|,
literal|"artist4"
argument_list|,
name|date
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|3001
argument_list|,
literal|"artist5"
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProperties
parameter_list|()
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
name|node
argument_list|)
decl_stmt|;
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
name|assertSame
argument_list|(
name|node
argument_list|,
name|action
operator|.
name|dataNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExecuteSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
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
name|sqlTemplateCustomizer
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|201L
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
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
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
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
name|List
argument_list|<
name|DataRow
argument_list|>
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
name|DataRow
name|row
init|=
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// In the absence of ObjEntity most DB's return a Long here, except for
comment|// Oracle
comment|// that has no BIGINT type and
comment|// returns BigDecimal, so do a Number comparison
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
operator|(
operator|(
name|Number
operator|)
name|bindings
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
operator|)
operator|.
name|longValue
argument_list|()
argument_list|,
name|id
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist4"
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
annotation|@
name|Test
specifier|public
name|void
name|selectObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
expr_stmt|;
name|String
name|templateString
init|=
literal|"SELECT * FROM ARTIST"
decl_stmt|;
name|SQLTemplate
name|sqlTemplate
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|templateString
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
name|sqlTemplate
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
operator|(
name|List
argument_list|<
name|Artist
argument_list|>
operator|)
name|objectContext
operator|.
name|performQuery
argument_list|(
name|sqlTemplate
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Artist
name|artist
range|:
name|artists
control|)
block|{
name|assertTrue
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"artist"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectUtilDate
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
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
name|sqlTemplateCustomizer
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|101
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
name|List
argument_list|<
name|DataRow
argument_list|>
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
name|DataRow
name|row
init|=
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
annotation|@
name|Test
specifier|public
name|void
name|testSelectSQLDate
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
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
name|sqlTemplateCustomizer
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|101
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
name|List
argument_list|<
name|DataRow
argument_list|>
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
name|DataRow
name|row
init|=
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
annotation|@
name|Test
specifier|public
name|void
name|testSelectSQLTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
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
name|sqlTemplateCustomizer
operator|.
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|201
argument_list|)
expr_stmt|;
name|template
operator|.
name|setParameters
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|SQLAction
name|plan
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
name|List
argument_list|<
name|DataRow
argument_list|>
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
name|DataRow
name|row
init|=
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
annotation|@
name|Test
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|1L
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
name|SQLAction
name|action
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
argument_list|)
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
literal|1L
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
name|testExecuteUpdateNoParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtists
argument_list|()
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
name|SQLAction
name|action
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
argument_list|)
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
literal|4
argument_list|,
name|batches
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings1
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|1L
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindings2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|bindings2
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|33L
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
name|SQLAction
name|genericAction
init|=
name|adapter
operator|.
name|getAction
argument_list|(
name|template
argument_list|,
name|node
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
name|node
argument_list|,
name|action
operator|.
name|dataNode
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
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
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
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|orderBy
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
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
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row1
init|=
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
name|DataRow
name|row2
init|=
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
annotation|@
name|Test
specifier|public
name|void
name|testExtractTemplateString
parameter_list|()
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
name|node
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
block|}
end_class

end_unit

