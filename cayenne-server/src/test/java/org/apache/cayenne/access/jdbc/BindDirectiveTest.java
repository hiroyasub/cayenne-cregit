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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
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
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|Date
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|dba
operator|.
name|oracle
operator|.
name|OracleAdapter
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
name|log
operator|.
name|JdbcEventLogger
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

begin_comment
comment|/**  * Tests BindDirective for passed null parameters and for not passed parameters  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|BindDirectiveTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|JdbcAdapter
name|adapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ObjectContext
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
name|JdbcEventLogger
name|logger
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
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithDOB"
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2010
argument_list|,
literal|2
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
operator|new
name|Timestamp
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// without JDBC usage
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|assertEquals
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindSQLDate
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithDOB"
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2010
argument_list|,
literal|2
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// without JDBC usage
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|assertEquals
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"dob"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindUtilDate
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithDOB"
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2010
argument_list|,
literal|2
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
comment|// without JDBC usage
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|assertEquals
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"dob"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindingForCollection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// insert 3 artists
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|new
name|Long
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Artist"
operator|+
name|i
argument_list|)
expr_stmt|;
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|true
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// now select only with names: Artist1 and Artist3
name|Set
argument_list|<
name|String
argument_list|>
name|artistNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|artistNames
operator|.
name|add
argument_list|(
literal|"Artist1"
argument_list|)
expr_stmt|;
name|artistNames
operator|.
name|add
argument_list|(
literal|"Artist3"
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME in (#bind($ARTISTNAMES))"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
comment|// customize for DB's that require trimming CHAR spaces
name|query
operator|.
name|setTemplate
argument_list|(
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"SELECT * FROM ARTIST WHERE RTRIM(ARTIST_NAME) in (#bind($ARTISTNAMES))"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ARTISTNAMES"
argument_list|,
name|artistNames
argument_list|)
argument_list|)
expr_stmt|;
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
name|query
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
block|}
specifier|public
name|void
name|testBindForPassedNullParam
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithoutDOB"
argument_list|)
expr_stmt|;
comment|// passing null in parameter
name|parameters
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// without JDBC usage
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|parameters
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
name|assertEquals
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"dob"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindWithJDBCForPassedNullParam
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithoutDOB"
argument_list|)
expr_stmt|;
comment|// passing null in parameter
name|parameters
operator|.
name|put
argument_list|(
literal|"dob"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// use JDBC
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|parameters
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
name|assertEquals
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"dob"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindForNotPassedParam
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithoutDOB"
argument_list|)
expr_stmt|;
comment|// not passing parameter parameters.put("dob", not passed!);
comment|// without JDBC usage
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|parameters
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
comment|// parameter should be passed as null
name|assertNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindWithJDBCForNotPassedParam
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"ArtistWithoutDOB"
argument_list|)
expr_stmt|;
comment|// not passing parameter parameters.put("dob", not passed!);
comment|// use JDBC
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
name|performInsertForParameters
argument_list|(
name|parameters
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameters
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
name|parameters
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
comment|// parameter should be passed as null
name|assertNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts row for given parameters      *       * @return inserted row      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|performInsertForParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|useJDBCType
parameter_list|,
name|int
name|expectedRowCount
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|templateString
decl_stmt|;
if|if
condition|(
name|useJDBCType
condition|)
block|{
name|templateString
operator|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"VALUES (#bind($id), #bind($name), #bind($dob 'DATE'))"
expr_stmt|;
block|}
else|else
block|{
name|templateString
operator|=
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"VALUES (#bind($id), #bind($name), #bind($dob))"
expr_stmt|;
block|}
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
name|template
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|SQLTemplateAction
name|action
init|=
operator|new
name|SQLTemplateAction
argument_list|(
name|template
argument_list|,
name|adapter
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
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
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|data
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedRowCount
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

