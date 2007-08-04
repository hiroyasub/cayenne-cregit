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
name|access
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextEJBQLGroupByHavingTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupBy
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" ORDER BY p.estimatedPrice"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row1
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row1
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupByMultipleItems
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, p.paintingTitle, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice, p.paintingTitle"
operator|+
literal|" ORDER BY p.estimatedPrice, p.paintingTitle"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PX"
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row0
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row1
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PZ"
argument_list|,
name|row1
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row1
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row2
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PY"
argument_list|,
name|row2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row2
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupByIdVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT count(p), p FROM Painting p GROUP BY p"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: andrus, 8/3/2007 the rest of the unit test fails as currently Cayenne
comment|// does not allow mixed object and scalar results (see CAY-839)
comment|// assertTrue(data.get(0) instanceof Object[]);
comment|//
comment|// for(int i = 0; i< data.size(); i++) {
comment|// Object[] row = (Object[]) data.get(i);
comment|// assertEquals(new Long(1), row[0]);
comment|// }
block|}
specifier|public
name|void
name|testGroupByHavingOnColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING p.estimatedPrice> 1"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupByHavingOnAggregate
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING count(p)> 2"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupByHavingOnAggregateMultipleConditions
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING count(p)> 2 AND p.estimatedPrice< 10"
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
name|List
name|data
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

