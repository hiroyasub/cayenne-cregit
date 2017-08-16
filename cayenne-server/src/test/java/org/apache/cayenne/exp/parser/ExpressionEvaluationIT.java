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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|Expression
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
name|Ordering
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Here we compare Expression evaluation in-memory vs execution in database.  * Results should be same for both cases.  * Here is primary collection of complex expressions:  *  - To-Many relationships comparisons  *  - Null comparisons  *  - Null in AND and OR expressions  *  * @since 4.0  */
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
name|ExpressionEvaluationIT
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
name|tArtist
decl_stmt|,
name|tGallery
decl_stmt|,
name|tPaintings
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
literal|6
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
name|tGallery
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"GALLERY"
argument_list|)
expr_stmt|;
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
name|tPaintings
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
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
literal|10
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
literal|100
argument_list|)
expr_stmt|;
block|}
name|tPaintings
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"painting11"
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|tPaintings
operator|.
name|insert
argument_list|(
literal|12
argument_list|,
literal|"painting12"
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleLike
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|like
argument_list|(
literal|"artist%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|6
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleNotLike
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|nlike
argument_list|(
literal|"artist%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleEqual
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist2"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleNotEqual
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|ne
argument_list|(
literal|"artist2"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLikeIgnoreCase
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|likeIgnoreCase
argument_list|(
literal|"painting%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotLikeIgnoreCase
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|likeIgnoreCase
argument_list|(
literal|"PaInTing%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLike
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotLike
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqual
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|eq
argument_list|(
literal|"painting1"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqual1
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|ne
argument_list|(
literal|"painting1"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqual2
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|ne
argument_list|(
literal|"painting11"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqual3
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|ne
argument_list|(
literal|"zyz"
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBetween
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|between
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|300
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|600
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotBetween
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|notBetweenExp
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|300
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|600
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreater
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|gt
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|799
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterEqual
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|gte
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|800
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIn
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|in
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|800
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|300
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|700
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInEmpty
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|in
argument_list|(
name|Collections
operator|.
expr|<
name|BigDecimal
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotIn
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|nin
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|800
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|200
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|300
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|400
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|700
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotInEmpty
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|nin
argument_list|(
name|Collections
operator|.
expr|<
name|BigDecimal
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|6
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLess
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|lt
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|801
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqual
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|lte
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|800
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionWithNull
parameter_list|()
block|{
name|Expression
name|exp
init|=
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
name|lt
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|200
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|exp
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|gt
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|expression1
init|=
name|expression
operator|.
name|notExp
argument_list|()
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterEqualWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|gte
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|expression1
init|=
name|expression
operator|.
name|notExp
argument_list|()
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|expression1
init|=
name|expression
operator|.
name|notExp
argument_list|()
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessEqualWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|expression
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lte
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|expression1
init|=
name|expression
operator|.
name|notExp
argument_list|()
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|expression1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"artist8"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|9
argument_list|,
literal|"artist9"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|nullExp
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|and
init|=
name|ExpressionFactory
operator|.
name|and
argument_list|(
name|nullExp
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist7"
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
operator|.
name|notExp
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndWithNull2
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"artist8"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|9
argument_list|,
literal|"artist9"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|nullExp
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|and
init|=
name|ExpressionFactory
operator|.
name|and
argument_list|(
name|nullExp
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist10"
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
operator|.
name|notExp
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrWithNull
parameter_list|()
throws|throws
name|Exception
block|{
name|tPaintings
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"artist7"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"artist8"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|9
argument_list|,
literal|"artist9"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|nullExp
init|=
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|and
init|=
name|ExpressionFactory
operator|.
name|or
argument_list|(
name|nullExp
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist7"
argument_list|)
argument_list|)
decl_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|compareSqlAndEval
argument_list|(
name|and
operator|.
name|notExp
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|compareSqlAndEval
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|int
name|expectedCount
parameter_list|)
block|{
comment|// apply exp in SQL
name|Ordering
name|ordering
init|=
operator|new
name|Ordering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|filteredInSQL
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|exp
argument_list|)
operator|.
name|orderBy
argument_list|(
name|ordering
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// apply exp to in-memory collection
name|List
argument_list|<
name|Artist
argument_list|>
name|filteredInMemory
init|=
name|exp
operator|.
name|filterObjects
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
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
argument_list|)
decl_stmt|;
name|ordering
operator|.
name|orderList
argument_list|(
name|filteredInMemory
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedCount
argument_list|,
name|filteredInMemory
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|filteredInSQL
argument_list|,
name|filteredInMemory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

