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
name|Date
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
name|Gallery
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
comment|/**  * @since 4.2  */
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
name|ObjectSelect_SubqueryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
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
operator|-
literal|2
operator|*
literal|24
operator|*
literal|3600
operator|*
literal|1000
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
literal|24
operator|*
literal|3600
operator|*
literal|1000
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
name|selectQuery_simpleExists
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|selectCount
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
name|selectQuery_existsWithExpressionFromParentQuery
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_PROPERTY
operator|.
name|enclosing
argument_list|()
argument_list|)
operator|.
name|andExp
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
argument_list|)
operator|.
name|andExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|enclosing
argument_list|()
operator|.
name|like
argument_list|(
literal|"art%"
argument_list|)
argument_list|)
decl_stmt|;
name|ColumnSelect
argument_list|<
name|String
argument_list|>
name|subQuery
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|where
argument_list|(
name|exp
argument_list|)
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|subQuery
argument_list|)
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|selectQuery_notExistsWithExpressionFromParentQuery
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|subQuery
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_PROPERTY
operator|.
name|enclosing
argument_list|()
argument_list|)
argument_list|)
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|notExists
argument_list|(
name|subQuery
argument_list|)
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|15L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|selectQuery_twoLevelExists
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|exp
argument_list|)
argument_list|)
argument_list|)
operator|.
name|selectCount
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
name|selectQuery_twoLevelExistsWithExpressionFromParentQuery
parameter_list|()
block|{
name|Expression
name|deepNestedExp
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|enclosing
argument_list|()
operator|.
name|enclosing
argument_list|()
operator|.
name|like
argument_list|(
literal|"art%"
argument_list|)
operator|.
name|andExp
argument_list|(
name|Painting
operator|.
name|TO_GALLERY
operator|.
name|enclosing
argument_list|()
operator|.
name|eq
argument_list|(
name|Gallery
operator|.
name|SELF
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|deepNestedExp
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andExp
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|Artist
operator|.
name|SELF
operator|.
name|enclosing
argument_list|()
argument_list|)
argument_list|)
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|exp
argument_list|)
argument_list|)
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|objectSelect_twoLevelExistsWithExpressionFromParentQuery
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Gallery
argument_list|>
name|deepSubquery
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Gallery
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
name|enclosing
argument_list|()
operator|.
name|enclosing
argument_list|()
operator|.
name|like
argument_list|(
literal|"art%"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|Painting
operator|.
name|TO_GALLERY
operator|.
name|enclosing
argument_list|()
operator|.
name|eq
argument_list|(
name|Gallery
operator|.
name|SELF
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|subquery
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting%"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|Artist
operator|.
name|SELF
operator|.
name|enclosing
argument_list|()
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|deepSubquery
argument_list|)
argument_list|)
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
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|exists
argument_list|(
name|subquery
argument_list|)
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|columnSelect_simpleInSubquery
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|String
argument_list|>
name|subquery
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|count
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|dot
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
operator|.
name|in
argument_list|(
name|subquery
argument_list|)
argument_list|)
operator|.
name|selectCount
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
name|columnSelect_simpleNotInSubquery
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|String
argument_list|>
name|subquery
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|lt
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|count
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|dot
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
operator|.
name|nin
argument_list|(
name|subquery
argument_list|)
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|16L
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

