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
name|Arrays
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
name|Property
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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ColumnSelectTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|query
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryWithOneColumn
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|String
argument_list|>
name|q
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
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|singleColumn
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryWithOneColumn2
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|String
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
name|column
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|singleColumn
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryWithOneColumn3
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Object
index|[]
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
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|singleColumn
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryWithMultipleColumns
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|q
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
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|singleColumn
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryCount
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Long
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
name|count
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|PropertyFactory
operator|.
name|COUNT
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryCountWithProperty
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Long
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
name|count
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|count
argument_list|()
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|queryMinWithProperty
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|BigDecimal
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
name|min
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
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
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
name|min
argument_list|()
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|columns
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|=
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
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|havingExpression
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expTrue
init|=
name|ExpressionFactory
operator|.
name|expTrue
argument_list|()
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expTrue
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expFalse
init|=
name|ExpressionFactory
operator|.
name|expFalse
argument_list|()
decl_stmt|;
name|q
operator|.
name|having
argument_list|(
name|expFalse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expFalse
argument_list|,
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expTrue
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|havingString
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expTrue
init|=
name|ExpressionFactory
operator|.
name|expTrue
argument_list|()
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expTrue
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expFalse
init|=
name|ExpressionFactory
operator|.
name|expFalse
argument_list|()
decl_stmt|;
name|q
operator|.
name|having
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expFalse
argument_list|,
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expTrue
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|and
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expTrue
init|=
name|ExpressionFactory
operator|.
name|expTrue
argument_list|()
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"true and true"
argument_list|)
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expFalse
init|=
name|ExpressionFactory
operator|.
name|expFalse
argument_list|()
decl_stmt|;
name|q
operator|.
name|having
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|(
name|expFalse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"false and false"
argument_list|)
argument_list|,
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"true and true"
argument_list|)
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|or
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expTrue
init|=
name|ExpressionFactory
operator|.
name|expTrue
argument_list|()
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|q
operator|.
name|or
argument_list|(
name|expTrue
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"true or true"
argument_list|)
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|expFalse
init|=
name|ExpressionFactory
operator|.
name|expFalse
argument_list|()
decl_stmt|;
name|q
operator|.
name|having
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|q
operator|.
name|or
argument_list|(
name|expFalse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"false or false"
argument_list|)
argument_list|,
name|q
operator|.
name|getHaving
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"true or true"
argument_list|)
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnsAddByOne
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|properties
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|properties
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnsAddAll
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
expr_stmt|;
name|q
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|properties
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
decl_stmt|;
comment|// should it be Set instead of List?
name|assertEquals
argument_list|(
name|properties
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnAddByOne
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|column
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
expr_stmt|;
name|q
operator|.
name|column
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|)
expr_stmt|;
name|q
operator|.
name|column
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|properties
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|properties
argument_list|,
name|q
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistinct
parameter_list|()
block|{
name|ColumnSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
operator|new
name|ColumnSelect
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|distinct
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|metaData
operator|.
name|isSuppressingDistinct
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|distinct
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|distinct
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|metaData
operator|.
name|isSuppressingDistinct
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|suppressDistinct
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|distinct
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|metaData
operator|.
name|isSuppressingDistinct
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOffsetCopyFromObjectSelect
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|select
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
name|offset
argument_list|(
literal|10
argument_list|)
operator|.
name|limit
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|select
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|select
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|ColumnSelect
argument_list|<
name|String
argument_list|>
name|columnSelect
init|=
name|select
operator|.
name|column
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|columnSelect
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|columnSelect
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

