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
name|dba
operator|.
name|frontbase
operator|.
name|FrontBaseAdapter
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
name|openbase
operator|.
name|OpenBaseAdapter
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
name|exp
operator|.
name|ExpressionFactory
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
name|map
operator|.
name|SQLResult
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
name|EJBQLQuery
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
name|ObjectIdQuery
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
name|testdo
operator|.
name|testmap
operator|.
name|CharPkTestEntity
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
name|CompoundPkTestEntity
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
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
decl_stmt|;
specifier|protected
name|TableHelper
name|tCompoundPKTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCharPKTest
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
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
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
argument_list|)
expr_stmt|;
name|tCompoundPKTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundPKTest
operator|.
name|setColumns
argument_list|(
literal|"KEY1"
argument_list|,
literal|"KEY2"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tCharPKTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
name|tCharPKTest
operator|.
name|setColumns
argument_list|(
literal|"PK_COL"
argument_list|,
literal|"OTHER_COL"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneCompoundPK
parameter_list|()
throws|throws
name|Exception
block|{
name|tCompoundPKTest
operator|.
name|insert
argument_list|(
literal|"PK1"
argument_list|,
literal|"PK2"
argument_list|,
literal|"BBB"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneCharPK
parameter_list|()
throws|throws
name|Exception
block|{
name|tCharPKTest
operator|.
name|insert
argument_list|(
literal|"CPK"
argument_list|,
literal|"AAAA"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneArtist
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtists
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadNestedProperty_ToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|titles
init|=
operator|(
name|Collection
argument_list|<
name|String
argument_list|>
operator|)
name|Cayenne
operator|.
name|readNestedProperty
argument_list|(
name|a
argument_list|,
literal|"paintingArray.paintingTitle"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|titles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|titles
operator|.
name|contains
argument_list|(
literal|"a1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|titles
operator|.
name|contains
argument_list|(
literal|"a2"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|size
init|=
operator|(
name|Integer
operator|)
name|Cayenne
operator|.
name|readNestedProperty
argument_list|(
name|a
argument_list|,
literal|"paintingArray.@size"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScalarObjectForQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|String
name|sql
init|=
literal|"SELECT count(1) AS X FROM ARTIST"
decl_stmt|;
name|DataMap
name|map
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"tstmap"
argument_list|)
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|map
argument_list|,
name|sql
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"SELECT COUNT(ARTIST_ID) AS X FROM ARTIST"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"SELECT COUNT(ARTIST_ID) AS X FROM ARTIST"
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
name|SQLResult
name|rsMap
init|=
operator|new
name|SQLResult
argument_list|()
decl_stmt|;
name|rsMap
operator|.
name|addColumnResult
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setResult
argument_list|(
name|rsMap
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Number
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|object
operator|)
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
name|testScalarObjectForQuery2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT count(a) from Artist a"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Object class: "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|object
operator|instanceof
name|Number
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|object
operator|)
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
name|testMakePath
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Cayenne
operator|.
name|makePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|Cayenne
operator|.
name|makePath
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b"
argument_list|,
name|Cayenne
operator|.
name|makePath
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|query
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"artist2"
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|object
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForQueryNoObject
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|44001
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoObjectForPK
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
comment|// use bogus non-existent PK
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|44001
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKTemporary
parameter_list|()
throws|throws
name|Exception
block|{
name|Persistent
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Persistent
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o2
argument_list|,
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|o2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKObjectId
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKClassInt
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKEntityInt
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
literal|"Artist"
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKClassMap
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|pk
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKEntityMapCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pk
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
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"PK1"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"PK2"
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|CompoundPkTestEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BBB"
argument_list|,
operator|(
operator|(
name|CompoundPkTestEntity
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompoundPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pk
init|=
name|Cayenne
operator|.
name|compoundPKForObject
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK1"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK2"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for compound key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectFailureForNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCharPK
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for non-numeric key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"pkForObject must fail for compound key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|33002
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|33002
argument_list|)
argument_list|,
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCharPK
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CPK"
argument_list|,
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

