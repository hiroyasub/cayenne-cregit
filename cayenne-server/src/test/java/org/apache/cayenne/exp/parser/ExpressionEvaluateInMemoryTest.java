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
name|assertTrue
import|;
end_import

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

begin_class
specifier|public
class|class
name|ExpressionEvaluateInMemoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateNOT_EQUAL_TONull
parameter_list|()
block|{
name|Expression
name|notEqualToNull
init|=
operator|new
name|ASTNotEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Expression
name|notEqualToNotNull
init|=
operator|new
name|ASTNotEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
decl_stmt|;
name|Artist
name|match
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|notEqualToNull
operator|.
name|match
argument_list|(
name|match
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notEqualToNotNull
operator|.
name|match
argument_list|(
name|match
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|noMatch
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch
operator|.
name|setArtistName
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|notEqualToNull
argument_list|,
name|notEqualToNull
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateAND
parameter_list|()
block|{
name|Expression
name|e1
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
decl_stmt|;
name|ASTAnd
name|e
init|=
operator|new
name|ASTAnd
argument_list|(
operator|new
name|Object
index|[]
block|{
name|e1
block|,
name|e2
block|}
argument_list|)
decl_stmt|;
name|Artist
name|match
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match
operator|.
name|setArtistName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|match
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|noMatch
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch
operator|.
name|setArtistName
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateOR
parameter_list|()
block|{
name|Expression
name|e1
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"xyz"
argument_list|)
decl_stmt|;
name|ASTOr
name|e
init|=
operator|new
name|ASTOr
argument_list|(
operator|new
name|Object
index|[]
block|{
name|e1
block|,
name|e2
block|}
argument_list|)
decl_stmt|;
name|Artist
name|match1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match1
operator|.
name|setArtistName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match2
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match2
operator|.
name|setArtistName
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|noMatch
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch
operator|.
name|setArtistName
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateIN
parameter_list|()
block|{
name|Expression
name|in
init|=
operator|new
name|ASTIn
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|ASTList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|BigDecimal
argument_list|(
literal|"10"
argument_list|)
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"20"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|notIn
init|=
operator|new
name|ASTNotIn
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|ASTList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|BigDecimal
argument_list|(
literal|"10"
argument_list|)
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"20"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|Painting
name|noMatch1
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|noMatch1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"21"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notIn
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|noMatch2
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|noMatch2
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"11"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|in
argument_list|,
name|in
operator|.
name|match
argument_list|(
name|noMatch2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|notIn
argument_list|,
name|notIn
operator|.
name|match
argument_list|(
name|noMatch2
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|match1
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|match1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"20"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|notIn
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|match2
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|match2
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|in
argument_list|,
name|in
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notIn
argument_list|,
name|notIn
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateIN_Null
parameter_list|()
block|{
name|Expression
name|in
init|=
operator|new
name|ASTIn
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|ASTList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|BigDecimal
argument_list|(
literal|"10"
argument_list|)
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"20"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|notIn
init|=
operator|new
name|ASTNotIn
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|ASTList
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|BigDecimal
argument_list|(
literal|"10"
argument_list|)
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"20"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|Painting
name|noMatch
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|notIn
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateADD
parameter_list|()
block|{
name|Expression
name|add
init|=
operator|new
name|ASTAdd
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
block|,
operator|new
name|Double
argument_list|(
literal|5.5
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6.5
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|add
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateSubtract
parameter_list|()
block|{
name|Expression
name|subtract
init|=
operator|new
name|ASTSubtract
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
block|,
operator|new
name|Double
argument_list|(
literal|0.1
argument_list|)
block|,
operator|new
name|Double
argument_list|(
literal|0.2
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0.7
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|subtract
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateMultiply
parameter_list|()
block|{
name|Expression
name|multiply
init|=
operator|new
name|ASTMultiply
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
block|,
operator|new
name|Double
argument_list|(
literal|3.5
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|multiply
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateDivide
parameter_list|()
block|{
name|Expression
name|divide
init|=
operator|new
name|ASTDivide
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|BigDecimal
argument_list|(
literal|"7.0"
argument_list|)
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"2.0"
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3.5
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|divide
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateNegate
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|3
argument_list|,
operator|(
operator|(
name|Number
operator|)
operator|new
name|ASTNegate
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
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
name|Number
operator|)
operator|new
name|ASTNegate
argument_list|(
operator|new
name|Integer
argument_list|(
operator|-
literal|5
argument_list|)
argument_list|)
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateTrue
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
operator|new
name|ASTTrue
argument_list|()
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateFalse
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
operator|new
name|ASTFalse
argument_list|()
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

