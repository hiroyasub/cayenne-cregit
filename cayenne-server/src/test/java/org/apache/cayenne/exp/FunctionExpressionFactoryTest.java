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
name|exp
operator|.
name|parser
operator|.
name|ASTAbs
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
name|parser
operator|.
name|ASTAsterisk
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
name|parser
operator|.
name|ASTAvg
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
name|parser
operator|.
name|ASTConcat
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
name|parser
operator|.
name|ASTCount
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
name|parser
operator|.
name|ASTCurrentDate
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
name|parser
operator|.
name|ASTCurrentTime
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
name|parser
operator|.
name|ASTCurrentTimestamp
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
name|parser
operator|.
name|ASTCustomOperator
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
name|parser
operator|.
name|ASTLength
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
name|parser
operator|.
name|ASTLocate
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
name|parser
operator|.
name|ASTLower
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
name|parser
operator|.
name|ASTMax
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
name|parser
operator|.
name|ASTMin
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
name|parser
operator|.
name|ASTMod
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
name|parser
operator|.
name|ASTObjPath
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
name|parser
operator|.
name|ASTScalar
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
name|parser
operator|.
name|ASTSqrt
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
name|parser
operator|.
name|ASTSubstring
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
name|parser
operator|.
name|ASTSum
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
name|parser
operator|.
name|ASTTrim
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
name|parser
operator|.
name|ASTUpper
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
name|FunctionExpressionFactoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|substringExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|10
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|,
literal|10
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|Expression
name|exp3
init|=
name|FunctionExpressionFactory
operator|.
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|10
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|15
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTSubstring
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp2
argument_list|,
name|exp3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|trimExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|trimExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|trimExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTTrim
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|lowerExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|lowerExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|lowerExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTLower
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|upperExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|upperExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|upperExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTUpper
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|lengthExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|lengthExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|lengthExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTLength
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|locateExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|locateExp
argument_list|(
literal|"abc"
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|locateExp
argument_list|(
literal|"abc"
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp3
init|=
name|FunctionExpressionFactory
operator|.
name|locateExp
argument_list|(
operator|new
name|ASTScalar
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTLocate
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp2
argument_list|,
name|exp3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|absExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|absExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|absExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTAbs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sqrtExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|sqrtExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|sqrtExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTSqrt
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|modExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|Expression
name|exp3
init|=
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTMod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp2
argument_list|,
name|exp3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|concatExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|concatExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTConcat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|concatExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|getName
argument_list|()
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp2
operator|instanceof
name|ASTConcat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|exp2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp2
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp2
operator|.
name|getOperand
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|countTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|countExp
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTCount
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|ASTAsterisk
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|exp2
init|=
name|FunctionExpressionFactory
operator|.
name|countExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp2
operator|instanceof
name|ASTCount
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp2
operator|.
name|getOperand
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
name|minTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|minExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTMin
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
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
name|maxTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|maxExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTMax
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
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
name|avgTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|avgExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTAvg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
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
name|sumTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp1
init|=
name|FunctionExpressionFactory
operator|.
name|sumExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp1
operator|instanceof
name|ASTSum
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exp1
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|exp1
operator|.
name|getOperand
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
name|currentDateTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|FunctionExpressionFactory
operator|.
name|currentDate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exp
operator|instanceof
name|ASTCurrentDate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|currentTimeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|FunctionExpressionFactory
operator|.
name|currentTime
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exp
operator|instanceof
name|ASTCurrentTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|currentTimestampTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|FunctionExpressionFactory
operator|.
name|currentTimestamp
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exp
operator|instanceof
name|ASTCurrentTimestamp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customOpTest
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|FunctionExpressionFactory
operator|.
name|operator
argument_list|(
literal|"==>"
argument_list|,
literal|123
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp
operator|instanceof
name|ASTCustomOperator
argument_list|)
expr_stmt|;
name|ASTCustomOperator
name|operator
init|=
operator|(
name|ASTCustomOperator
operator|)
name|exp
decl_stmt|;
name|assertEquals
argument_list|(
literal|"==>"
argument_list|,
name|operator
operator|.
name|getOperator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|operator
operator|.
name|jjtGetNumChildren
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|operator
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
name|operator
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

