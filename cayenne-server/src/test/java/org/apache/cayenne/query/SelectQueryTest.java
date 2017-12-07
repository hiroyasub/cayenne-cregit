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
name|ArrayList
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
name|ExpressionParameter
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

begin_class
specifier|public
class|class
name|SelectQueryTest
block|{
specifier|private
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|this
operator|.
name|query
operator|=
operator|new
name|SelectQuery
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPrefetch
parameter_list|()
block|{
name|assertNull
argument_list|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"a.b.c"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|nonPhantomNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|getNode
argument_list|(
literal|"a.b.c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPrefetchDuplicates
parameter_list|()
block|{
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"a.b.c"
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"a.b.c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|nonPhantomNodes
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
name|testClearPrefetches
parameter_list|()
block|{
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|clearPrefetches
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPageSize
parameter_list|()
throws|throws
name|Exception
block|{
name|query
operator|.
name|setPageSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|query
operator|.
name|getPageSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddOrdering1
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
name|query
operator|.
name|addOrdering
argument_list|(
name|ord
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ord
argument_list|,
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddOrdering2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"a.b.c"
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
name|path
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Ordering
name|ord
init|=
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|path
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
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ord
operator|.
name|isAscending
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
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|query
operator|.
name|isDistinct
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDistinct
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|isDistinct
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQueryWithParams1
parameter_list|()
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDistinct
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|q1
init|=
name|query
operator|.
name|queryWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|query
operator|.
name|getRoot
argument_list|()
argument_list|,
name|q1
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|query
operator|.
name|isDistinct
argument_list|()
argument_list|,
name|q1
operator|.
name|isDistinct
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q1
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQueryWithParams2
parameter_list|()
throws|throws
name|Exception
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Expression
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k1"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k2"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k3"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test3"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k4"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|joinExp
argument_list|(
name|Expression
operator|.
name|OR
argument_list|,
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"test2"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"test3"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|q1
init|=
name|query
operator|.
name|queryWithParameters
argument_list|(
name|params
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|query
operator|.
name|getRoot
argument_list|()
argument_list|,
name|q1
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|q1
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|getQualifier
argument_list|()
operator|!=
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndQualifier
parameter_list|()
block|{
name|assertNull
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|NOT_EQUAL_TO
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|AND
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrQualifier
parameter_list|()
block|{
name|assertNull
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
decl_stmt|;
name|query
operator|.
name|orQualifier
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|NOT_EQUAL_TO
argument_list|)
decl_stmt|;
name|query
operator|.
name|orQualifier
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OR
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetQualifier
parameter_list|()
block|{
name|assertNull
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|AND
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|qual
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|qual
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

