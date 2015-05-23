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
name|ASTBetweenTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate
parameter_list|()
block|{
comment|// evaluate both BETWEEN and NOT_BETWEEN
name|Expression
name|between
init|=
operator|new
name|ASTBetween
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|10d
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|20d
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|notBetween
init|=
operator|new
name|ASTNotBetween
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|10d
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|20d
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
name|noMatch
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|21
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|between
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notBetween
operator|.
name|match
argument_list|(
name|noMatch
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
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|between
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|notBetween
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
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|between
argument_list|,
name|between
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
name|notBetween
argument_list|,
name|notBetween
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|Painting
name|match3
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|match3
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|between
argument_list|,
name|between
operator|.
name|match
argument_list|(
name|match3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notBetween
argument_list|,
name|notBetween
operator|.
name|match
argument_list|(
name|match3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_Null
parameter_list|()
block|{
name|Expression
name|btNull
init|=
operator|new
name|ASTBetween
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|10d
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|20d
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|btNotNull
init|=
operator|new
name|ASTNotBetween
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"estimatedPrice"
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|10d
argument_list|)
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|20d
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
name|btNull
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|btNotNull
operator|.
name|match
argument_list|(
name|noMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

