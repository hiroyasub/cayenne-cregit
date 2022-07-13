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
name|query
package|;
end_package

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
name|Arrays
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
name|ObjectId
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
name|EntityResolver
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
name|DataChannelInterceptor
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
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonMap
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
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
name|*
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
name|SelectById_RunIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|interceptor
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
specifier|private
name|TableHelper
name|tPainting
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
name|EntityResolver
name|resolver
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
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING_TITLE"
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
literal|2
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Artist
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
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
name|testIntPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPkCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPk_SelectFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Artist
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
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
name|testMapPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Artist
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
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
name|testMapPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id2
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id3
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|id2
argument_list|,
name|id3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectIdPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|ObjectId
name|oid3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|Artist
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|oid3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectId
name|oid2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Artist
name|a2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|oid2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
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
name|testObjectIdPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|ObjectId
name|oid2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|oid3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|oid2
argument_list|,
name|oid3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowIntPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|DataRow
name|a3
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|DataRow
name|a2
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowMapPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id3
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|DataRow
name|a3
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|id3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id2
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|DataRow
name|a2
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|id2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowObjectIdPk
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|ObjectId
name|oid3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|DataRow
name|a3
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|oid3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectId
name|oid2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|DataRow
name|a2
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|oid2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|a2
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowIntPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|DataRow
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowMapPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|ObjectId
name|oid2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|oid3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|oid2
argument_list|,
name|oid3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|DataRow
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataRowObjectIdPkMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id2
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|id3
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|artists
init|=
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|id2
argument_list|,
name|id3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|DataRow
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMetadataCacheKey
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectById
argument_list|<
name|Painting
argument_list|>
name|q1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|4
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|QueryMetadata
name|md1
init|=
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|SelectById
argument_list|<
name|Painting
argument_list|>
name|q2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|singletonMap
argument_list|(
name|Painting
operator|.
name|PAINTING_ID_PK_COLUMN
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|QueryMetadata
name|md2
init|=
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md2
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md2
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
comment|// this query is just a different form of q1, so should hit the same
comment|// cache entry
name|assertEquals
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|md2
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|SelectById
argument_list|<
name|Painting
argument_list|>
name|q3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|5
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|QueryMetadata
name|md3
init|=
name|q3
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md3
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md3
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|md3
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|SelectById
argument_list|<
name|Artist
argument_list|>
name|q4
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|4
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|QueryMetadata
name|md4
init|=
name|q4
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md4
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md4
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|md4
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|SelectById
argument_list|<
name|Painting
argument_list|>
name|q5
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Painting"
argument_list|,
name|Painting
operator|.
name|PAINTING_ID_PK_COLUMN
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|QueryMetadata
name|md5
init|=
name|q5
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md5
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md5
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
comment|// this query is just a different form of q1, so should hit the same cache entry
name|assertEquals
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|md5
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
specifier|final
name|Artist
index|[]
name|a3
init|=
operator|new
name|Artist
index|[
literal|1
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|interceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
name|a3
index|[
literal|0
index|]
operator|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|a3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
index|[
literal|0
index|]
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|Artist
name|a3cached
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|a3
index|[
literal|0
index|]
argument_list|,
name|a3cached
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|(
literal|"g1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|interceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|45
argument_list|,
literal|3
argument_list|,
literal|"One"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|48
argument_list|,
literal|3
argument_list|,
literal|"Two"
argument_list|)
expr_stmt|;
specifier|final
name|Artist
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
block|{
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|a3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|a3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaintingTitle
argument_list|()
expr_stmt|;
name|a3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPaintingTitle
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

