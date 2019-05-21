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
name|util
operator|.
name|List
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
name|ResultBatchIterator
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
name|ResultIterator
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
name|assertThat
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
name|ObjectSelect_RunIT
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
name|Before
specifier|public
name|void
name|createArtistsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tArtist
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
decl_stmt|;
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
name|long
name|dateBase
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|tArtist
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"artist"
operator|+
name|i
argument_list|,
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|dateBase
operator|+
literal|10000
operator|*
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|TableHelper
name|tGallery
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"GALLERY"
argument_list|)
decl_stmt|;
name|tGallery
operator|.
name|setColumns
argument_list|(
literal|"GALLERY_ID"
argument_list|,
literal|"GALLERY_NAME"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"tate modern"
argument_list|)
expr_stmt|;
name|TableHelper
name|tPaintings
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|tPaintings
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"GALLERY_ID"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|tPaintings
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"painting"
operator|+
name|i
argument_list|,
name|i
operator|%
literal|5
operator|+
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectObjects
parameter_list|()
block|{
name|List
argument_list|<
name|Artist
argument_list|>
name|result
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
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
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
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|in
argument_list|(
literal|"artist14"
argument_list|,
literal|"at1"
argument_list|,
literal|"12"
argument_list|,
literal|"asdas"
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
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist14"
argument_list|,
name|a
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
name|test_Iterate
parameter_list|()
block|{
specifier|final
name|int
index|[]
name|count
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|iterate
argument_list|(
name|context
argument_list|,
name|object
lambda|->
block|{
name|assertNotNull
argument_list|(
name|object
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|count
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_Iterator
parameter_list|()
block|{
try|try
init|(
name|ResultIterator
argument_list|<
name|Artist
argument_list|>
name|it
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
name|iterator
argument_list|(
name|context
argument_list|)
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|it
operator|.
name|nextRow
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_BatchIterator
parameter_list|()
block|{
try|try
init|(
name|ResultBatchIterator
argument_list|<
name|Artist
argument_list|>
name|it
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
name|batchIterator
argument_list|(
name|context
argument_list|,
literal|5
argument_list|)
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Artist
argument_list|>
name|artistList
range|:
name|it
control|)
block|{
name|count
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|artistList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectDataRows
parameter_list|()
block|{
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
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
name|DataRow
name|a
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist14"
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
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist14"
argument_list|,
name|a
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
name|test_SelectOne
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist13"
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
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist13"
argument_list|,
name|a
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
name|test_SelectOne_NoMatch
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist33"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a
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
name|test_SelectOne_MoreThanOneMatch
parameter_list|()
block|{
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|like
argument_list|(
literal|"artist%"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectFirst
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist13"
argument_list|)
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist13"
argument_list|,
name|a
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
name|test_SelectFirstByContext
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist13"
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|selectFirst
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist13"
argument_list|,
name|a
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
name|test_SelectFirst_NoMatch
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist33"
argument_list|)
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectFirst_MoreThanOneMatch
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|like
argument_list|(
literal|"artist%"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_PROPERTY
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|a
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
name|test_SelectFirst_TrimInWhere
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|trim
argument_list|()
operator|.
name|likeIgnoreCase
argument_list|(
literal|"artist%"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_PROPERTY
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|a
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
name|test_SelectFirst_SubstringInWhere
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|eq
argument_list|(
literal|"rti"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_PROPERTY
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|a
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
name|test_Select_CustomFunction
parameter_list|()
block|{
name|Artist
name|a
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|function
argument_list|(
literal|"UPPER"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|eq
argument_list|(
literal|"ARTIST1"
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
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|a
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
name|test_Select_Having
parameter_list|()
block|{
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
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
name|having
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|count
argument_list|()
operator|.
name|gt
argument_list|(
literal|3L
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
literal|5
argument_list|,
name|artists
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
name|test_Select_Where_Having
parameter_list|()
block|{
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
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
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist1"
argument_list|)
argument_list|)
operator|.
name|having
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|count
argument_list|()
operator|.
name|gt
argument_list|(
literal|3L
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
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

