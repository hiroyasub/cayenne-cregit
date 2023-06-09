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
name|ExpressionCollectionEvaluationIT
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
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|,
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
expr_stmt|;
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
literal|3
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
name|i
operator|+
literal|"painting"
operator|+
operator|(
name|Math
operator|.
name|pow
argument_list|(
literal|10
argument_list|,
name|i
argument_list|)
operator|)
argument_list|,
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
literal|4
argument_list|,
literal|"4painting"
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
comment|//        tPaintings.insert(5, "5painting", 1, 1, null);
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstringWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"substring(paintingArray.paintingTitle, 1, 1)"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTrimWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"trim(paintingArray.paintingTitle)"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpperWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"upper(paintingArray.paintingTitle)"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLowerWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"lower(paintingArray.paintingTitle)"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLengthWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"length(paintingArray.paintingTitle)"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConcatWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"concat(paintingArray.paintingTitle, ' ', 'xyz')"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMathWithCollection
parameter_list|()
block|{
name|testExpression
argument_list|(
literal|"paintingArray.estimatedPrice + 2"
argument_list|,
name|BigDecimal
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
extends|extends
name|Comparable
argument_list|<
name|T
argument_list|>
parameter_list|>
name|void
name|testExpression
parameter_list|(
name|String
name|expStr
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|)
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Object
name|res
init|=
name|exp
operator|.
name|evaluate
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
name|selectOne
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|T
argument_list|>
name|sqlResult
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
name|createBase
argument_list|(
name|exp
argument_list|,
name|tClass
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
literal|"db:paintingArray.PAINTING_ID"
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
operator|(
name|List
argument_list|<
name|T
argument_list|>
operator|)
name|res
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|sqlResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|sqlResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|res
argument_list|,
name|sqlResult
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

