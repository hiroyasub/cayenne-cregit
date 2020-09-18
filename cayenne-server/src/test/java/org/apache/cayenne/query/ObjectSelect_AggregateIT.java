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
name|text
operator|.
name|DateFormat
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
name|Date
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
name|Locale
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
name|exp
operator|.
name|property
operator|.
name|BaseProperty
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
name|property
operator|.
name|NumericProperty
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
name|property
operator|.
name|PropertyFactory
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
name|After
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
name|Ignore
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
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|*
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

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
name|ObjectSelect_AggregateIT
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
comment|// Format: d/m/YY
name|DateFormat
name|dateFormat
init|=
name|DateFormat
operator|.
name|getDateInstance
argument_list|(
name|DateFormat
operator|.
name|SHORT
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
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
name|tArtist
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|DATE
argument_list|)
expr_stmt|;
name|java
operator|.
name|sql
operator|.
name|Date
index|[]
name|dates
init|=
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
index|[
literal|5
index|]
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|dates
index|[
name|i
operator|-
literal|1
index|]
operator|=
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/"
operator|+
name|i
operator|+
literal|"/17"
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|dates
index|[
name|i
operator|%
literal|5
index|]
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
argument_list|,
literal|"ESTIMATED_PRICE"
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
argument_list|,
name|i
operator|*
literal|10
argument_list|)
expr_stmt|;
block|}
name|tPaintings
operator|.
name|insert
argument_list|(
literal|21
argument_list|,
literal|"painting21"
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|clearArtistsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|table
range|:
name|Arrays
operator|.
name|asList
argument_list|(
literal|"PAINTING"
argument_list|,
literal|"ARTIST"
argument_list|,
literal|"GALLERY"
argument_list|)
control|)
block|{
name|TableHelper
name|tHelper
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
name|table
argument_list|)
decl_stmt|;
name|tHelper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCount
parameter_list|()
block|{
name|long
name|count
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
name|column
argument_list|(
name|PropertyFactory
operator|.
name|COUNT
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCountDistinct
parameter_list|()
throws|throws
name|Exception
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
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Artist
name|artist
range|:
name|artists
control|)
block|{
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"Duplicate"
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|NumericProperty
argument_list|<
name|Long
argument_list|>
name|countDistinctProp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|countDistinct
argument_list|()
decl_stmt|;
name|long
name|count
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
name|column
argument_list|(
name|countDistinctProp
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Not all databases support AVG(DATE) aggregation"
argument_list|)
specifier|public
name|void
name|testAvg
parameter_list|()
throws|throws
name|Exception
block|{
name|BaseProperty
argument_list|<
name|Date
argument_list|>
name|avgProp
init|=
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|avgExp
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|Date
name|avg
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
name|column
argument_list|(
name|avgProp
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/3/17"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|date
argument_list|,
name|avg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMin
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|avg
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
name|column
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|min
argument_list|()
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/1/17"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|date
argument_list|,
name|avg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMax
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|avg
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
name|column
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|max
argument_list|()
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/5/17"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|date
argument_list|,
name|avg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCountGroupBy
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|count
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
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|count
argument_list|()
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/2/17"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|count
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|count
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|date
argument_list|,
name|count
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectRelationshipCount
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|count
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
name|column
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|count
argument_list|()
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
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectRelationshipCountWithAnotherField
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
index|[]
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
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|count
argument_list|()
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
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByCount
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
name|orderBy
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|outer
argument_list|()
operator|.
name|count
argument_list|()
operator|.
name|desc
argument_list|()
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
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
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|17
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|18
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|19
argument_list|)
operator|.
name|getPaintingArray
argument_list|()
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
name|testOrderByAvg
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
name|orderBy
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|ESTIMATED_PRICE
argument_list|)
operator|.
name|avg
argument_list|()
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
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
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist4"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist5"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

