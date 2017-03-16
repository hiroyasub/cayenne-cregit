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
name|testdo
operator|.
name|table_primitives
operator|.
name|TablePrimitives
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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ASTModTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|evaluateNode
parameter_list|()
throws|throws
name|Exception
block|{
name|ASTObjPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"intColumn"
argument_list|)
decl_stmt|;
name|ASTMod
name|mod
init|=
operator|new
name|ASTMod
argument_list|(
name|path
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|3.0
argument_list|)
argument_list|)
decl_stmt|;
name|TablePrimitives
name|a
init|=
operator|new
name|TablePrimitives
argument_list|()
decl_stmt|;
name|a
operator|.
name|setIntColumn
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|Object
name|res
init|=
name|mod
operator|.
name|evaluateNode
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|res
operator|instanceof
name|Double
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|parseTest
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expString
init|=
literal|"mod(xyz , 3)"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expString
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exp
operator|instanceof
name|ASTMod
argument_list|)
expr_stmt|;
name|String
name|toString
init|=
name|exp
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expString
argument_list|,
name|toString
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

