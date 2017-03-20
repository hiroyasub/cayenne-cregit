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
name|FunctionExpressionFactory
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ASTExtractTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testYear
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"year(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|yearExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testMonth
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"month(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|monthExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testWeek
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"week(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|weekExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testDayOfYear
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"dayOfYear(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|dayOfYearExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testDay
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"day(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
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
name|testDayOfMonth
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"dayOfMonth(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|dayOfMonthExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testDayOfWeek
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"dayOfWeek(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|dayOfWeekExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testHour
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"hour(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|hourExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testMinute
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"minute(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|minuteExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
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
name|testSecond
parameter_list|()
block|{
name|String
name|expStr
init|=
literal|"second(dateColumn)"
decl_stmt|;
name|Expression
name|expParsed
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expStr
argument_list|)
decl_stmt|;
name|Expression
name|expFromFactory
init|=
name|FunctionExpressionFactory
operator|.
name|secondExp
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expParsed
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFromFactory
operator|instanceof
name|ASTExtract
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expParsed
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expStr
argument_list|,
name|expFromFactory
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

