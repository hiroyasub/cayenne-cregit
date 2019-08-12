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
name|exp
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
name|PrefetchTreeNode
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
name|ParsedExpQualifierCompatIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
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
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTwentyFiveArtists
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|25
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
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|createTwentyFiveArtistsAndPaintings
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
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
literal|25
condition|;
name|i
operator|++
control|)
block|{
name|tPainting
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"p_artist"
operator|+
name|i
argument_list|,
name|i
argument_list|,
name|i
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|execute
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
return|return
name|execute
argument_list|(
name|root
argument_list|,
name|qualifier
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|execute
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|,
name|String
name|prefetch
parameter_list|)
block|{
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|root
argument_list|,
name|qualifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefetch
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|prefetch
argument_list|(
name|prefetch
argument_list|,
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
block|}
return|return
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOr
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName='artist1' or artistName='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|parsed
operator|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName='artist1' or artistName='artist3' or artistName='artist5'"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed
argument_list|)
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
name|testAnd
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName='artist1' and artistName='artist1'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|parsed
operator|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName='artist1' and artistName='artist3'"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed
argument_list|)
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
name|testNot
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"not artistName='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"not artistName='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
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
name|testEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test with prefetch... this type of expressions should work with prefetches
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|,
literal|"paintingArray"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName=='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
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
name|testNotEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName!='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName<>'artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
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
name|testLessThan
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice< 2000.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testLessThanEqualTo
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice<= 2000.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testGreaterThan
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice> 2000"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|2
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testGreaterThanEqualTo
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice>= 2000"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|1
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testLike
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName like 'artist%2'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testLikeIgnoreCase
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName likeIgnoreCase 'artist%2'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testNotLike
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName not like 'artist%2'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testNotLikeIgnoreCase
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName not likeIgnoreCase 'artist%2'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testIn
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName in ('artist1', 'artist3', 'artist19')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testNotIn
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName not in ('artist1', 'artist3', 'artist19')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|3
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testBetween
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice between 2000.0 and 4000.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testNotBetween
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice not between 2000.0 and 4000.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|3
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist5"
argument_list|)
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName=$artistName"
argument_list|)
decl_stmt|;
name|parsed1
operator|=
name|parsed1
operator|.
name|params
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testDbExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"db:ARTIST_NAME='artist3'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testFloatExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"estimatedPrice< 2001.01"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
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
name|testNullExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtists
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName!=null"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName = null"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|execute
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
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
name|testTrueExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"(estimatedPrice< 2001.01) and true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed3
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"(estimatedPrice< 2001.01) or true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed3
argument_list|)
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
name|testFalseExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwentyFiveArtistsAndPaintings
argument_list|()
expr_stmt|;
name|Expression
name|parsed1
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"false"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed2
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"(estimatedPrice< 2001.01) and false"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|parsed3
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"(estimatedPrice< 2001.01) or false"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|execute
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|parsed3
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

