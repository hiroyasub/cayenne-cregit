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
operator|.
name|translator
operator|.
name|select
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|DataNode
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
name|FunctionExpressionFactory
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
name|query
operator|.
name|SortOrder
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
name|ServerCaseDataSourceFactory
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
name|Test
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
name|OrderingTranslatorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
comment|/** 	 * Tests ascending ordering on string attribute. 	 */
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart1
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"ta.ARTIST_NAME"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Tests descending ordering on string attribute. 	 */
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart2
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"ta.ARTIST_NAME DESC"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart3
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"ta.ARTIST_NAME DESC, ta.ESTIMATED_PRICE"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Tests ascending case-insensitive ordering on string attribute. 	 */
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart4
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"UPPER(ta.ARTIST_NAME)"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart5
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"UPPER(ta.ARTIST_NAME) DESC, ta.ESTIMATED_PRICE"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendPart6
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
literal|"artistName"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"UPPER(ta.ARTIST_NAME), UPPER(ta.ESTIMATED_PRICE)"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendFunctionExpression1
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
name|FunctionExpressionFactory
operator|.
name|absExp
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|)
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"ABS(ta.ESTIMATED_PRICE)"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendFunctionExpression2
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
name|FunctionExpressionFactory
operator|.
name|countExp
argument_list|(
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"dateOfBirth"
argument_list|)
argument_list|)
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
decl_stmt|;
name|Ordering
name|o2
init|=
operator|new
name|Ordering
argument_list|(
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
literal|"paintingArray.estimatedPrice"
argument_list|,
literal|3
argument_list|)
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|doTestAppendPart
argument_list|(
literal|"UPPER(COUNT(ta.DATE_OF_BIRTH)), MOD(ta.ESTIMATED_PRICE, ?) DESC"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAppendIllegalExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|Ordering
name|o1
init|=
operator|new
name|Ordering
argument_list|(
name|ExpressionFactory
operator|.
name|and
argument_list|(
name|ExpressionFactory
operator|.
name|expTrue
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|expFalse
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// should throw exception
name|doTestAppendPart
argument_list|(
literal|"TRUE AND FALSE"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestAppendPart
parameter_list|(
name|String
name|expectedSQL
parameter_list|,
name|Ordering
modifier|...
name|orderings
parameter_list|)
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|SelectQuery
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
name|addOrderings
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|orderings
argument_list|)
argument_list|)
expr_stmt|;
name|TstQueryAssembler
name|assembler
init|=
operator|new
name|TstQueryAssembler
argument_list|(
name|q
argument_list|,
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|translated
init|=
operator|new
name|OrderingTranslator
argument_list|(
name|assembler
argument_list|)
operator|.
name|appendPart
argument_list|(
name|out
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedSQL
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

