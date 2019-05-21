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
name|assertFalse
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ExpressionEqualsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEquals3
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|Expression
name|e3
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e2
operator|.
name|equals
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals4
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|e3
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"aa"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e2
operator|.
name|equals
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

