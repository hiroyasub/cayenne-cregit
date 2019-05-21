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
name|assertFalse
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ObjectSelectTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDataRowQuery
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|q
operator|.
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQuery_RootType
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
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
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
name|assertFalse
argument_list|(
name|q
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|q
operator|.
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQuery_RootType_WithQualifier
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
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|"A"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = \"A\""
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|q
operator|.
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQuery_TypeAndEntity
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
argument_list|,
literal|"Painting"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Painting"
argument_list|,
name|q
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQuery_TypeAndDbEntity
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dbQuery
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PAINTING"
argument_list|,
name|q
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWhere
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"b"
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"(a = 3) and (b = 4)"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnd_Array
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"b"
argument_list|,
literal|4
argument_list|)
argument_list|,
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"c"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"(a = 3) and (b = 4) and (c> 5)"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnd_Collection
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Expression
argument_list|>
name|exps
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"b"
argument_list|,
literal|4
argument_list|)
argument_list|,
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"c"
argument_list|,
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|and
argument_list|(
name|exps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"(a = 3) and (b = 4) and (c> 5)"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnd_ArrayNull
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnd_ArrayEmpty
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|(
operator|new
name|Expression
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnd_CollectionEmpty
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|and
argument_list|(
name|Collections
operator|.
expr|<
name|Expression
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOr_Array
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|or
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"b"
argument_list|,
literal|4
argument_list|)
argument_list|,
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"c"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"(a = 3) or (b = 4) or (c> 5)"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOr_Collection
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
decl_stmt|;
name|q
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a = 3"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Expression
argument_list|>
name|exps
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"b"
argument_list|,
literal|4
argument_list|)
argument_list|,
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"c"
argument_list|,
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|or
argument_list|(
name|exps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"(a = 3) or (b = 4) or (c> 5)"
argument_list|,
name|q
operator|.
name|getWhere
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderBy_Array
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
decl_stmt|;
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"x"
argument_list|)
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result1
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result1
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|result1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
literal|"y"
argument_list|)
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result2
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o2
argument_list|,
name|result2
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
name|testOrderBy_Collection
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
decl_stmt|;
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"x"
argument_list|)
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result1
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result1
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|result1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
literal|"y"
argument_list|)
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result2
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o2
argument_list|,
name|result2
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
name|testOrderBy_PropertyStrategy
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
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
literal|"x"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result1
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result1
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"x"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
argument_list|,
name|result1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
literal|"y"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result2
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"y"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
argument_list|,
name|result2
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
name|testOrderBy_Property
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
decl_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result1
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result1
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"x"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|,
name|result1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|q
operator|.
name|orderBy
argument_list|(
literal|"y"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result2
init|=
name|q
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"x"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"y"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|,
name|result2
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
name|testPrefetch
parameter_list|()
block|{
name|PrefetchTreeNode
name|root
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
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
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|root1
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|root1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch_Path
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
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|root1
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|root1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
literal|"a.c"
argument_list|,
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|root2
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|root2
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root2
operator|.
name|getNode
argument_list|(
literal|"a.c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root2
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch_Merge
parameter_list|()
block|{
name|PrefetchTreeNode
name|root
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
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
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|root1
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|root1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|subRoot
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b.c"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|subRoot
argument_list|)
expr_stmt|;
name|root1
operator|=
name|q
operator|.
name|getPrefetches
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b.c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|root1
operator|.
name|getNode
argument_list|(
literal|"a.b.c"
argument_list|)
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLimit
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|limit
argument_list|(
literal|3
argument_list|)
operator|.
name|limit
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|q
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOffset
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|offset
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|offset
argument_list|(
literal|3
argument_list|)
operator|.
name|offset
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|q
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStatementFetchSize
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|statementFetchSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|statementFetchSize
argument_list|(
literal|3
argument_list|)
operator|.
name|statementFetchSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|q
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCacheGroups_Collection
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|cacheGroup
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCacheStrategy
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
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
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|localCache
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|localCache
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSharedCache
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|sharedCache
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|sharedCache
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|q
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

