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
name|ArrayList
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|util
operator|.
name|TestBean
import|;
end_import

begin_class
specifier|public
class|class
name|OrderingTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testPathSpec1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pathSpec
init|=
literal|"a.b.c"
decl_stmt|;
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ord
operator|.
name|getSortSpec
argument_list|()
argument_list|)
expr_stmt|;
name|ord
operator|.
name|setSortSpecString
argument_list|(
name|pathSpec
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pathSpec
argument_list|,
name|ord
operator|.
name|getSortSpec
argument_list|()
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPathSpec3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pathSpec
init|=
literal|"a.b.c"
decl_stmt|;
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|(
name|pathSpec
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pathSpec
argument_list|,
name|ord
operator|.
name|getSortSpec
argument_list|()
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAscending1
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setAscending
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAscending2
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAscending3
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDescending1
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setDescending
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDescending2
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDescending3
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|()
decl_stmt|;
name|ord
operator|.
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isDescending
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCaseInsensitive3
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|(
literal|"M"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCaseInsensitive4
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|(
literal|"N"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCaseInsensitive5
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|(
literal|"M"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ord
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCaseInsensitive6
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|ord
init|=
operator|new
name|Ordering
argument_list|(
literal|"N"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ord
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompare3
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|p1
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|p1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1000.00
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|p2
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|p2
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2000.00
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|p3
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|p3
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2000.00
argument_list|)
argument_list|)
expr_stmt|;
name|Ordering
name|ordering
init|=
operator|new
name|Ordering
argument_list|(
literal|"estimatedPrice"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|p2
argument_list|,
name|p1
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|p2
argument_list|,
name|p3
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompare4
parameter_list|()
throws|throws
name|Exception
block|{
comment|// compare on non-persistent property
name|TestBean
name|t1
init|=
operator|new
name|TestBean
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|TestBean
name|t2
init|=
operator|new
name|TestBean
argument_list|(
literal|2000
argument_list|)
decl_stmt|;
name|TestBean
name|t3
init|=
operator|new
name|TestBean
argument_list|(
literal|2000
argument_list|)
decl_stmt|;
name|Ordering
name|ordering
init|=
operator|new
name|Ordering
argument_list|(
literal|"integer"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|t1
argument_list|,
name|t2
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|t2
argument_list|,
name|t1
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ordering
operator|.
name|compare
argument_list|(
name|t2
argument_list|,
name|t3
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOrderList2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// compare on non-persistent property
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|Ordering
argument_list|(
literal|"integer"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
operator|.
name|orderList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|TestBean
operator|)
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getInteger
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
operator|(
operator|(
name|TestBean
operator|)
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getInteger
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
operator|(
operator|(
name|TestBean
operator|)
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|getInteger
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOrderListWithMultipleOrderings2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// compare on non-persistent property
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"c"
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"a"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"b"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|TestBean
argument_list|(
literal|"b"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|orderings
init|=
operator|new
name|ArrayList
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|orderings
operator|.
name|add
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"string"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|)
expr_stmt|;
name|orderings
operator|.
name|add
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"integer"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
argument_list|)
expr_stmt|;
comment|// clone list and then order
name|List
name|orderedList
init|=
operator|new
name|ArrayList
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|Ordering
operator|.
name|orderList
argument_list|(
name|orderedList
argument_list|,
name|orderings
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|orderedList
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

