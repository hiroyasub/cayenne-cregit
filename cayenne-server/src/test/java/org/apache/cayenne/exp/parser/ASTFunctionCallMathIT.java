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
operator|.
name|parser
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
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
name|query
operator|.
name|ObjectSelect
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

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TABLE_PRIMITIVES_PROJECT
argument_list|)
specifier|public
class|class
name|ASTFunctionCallMathIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
specifier|private
name|TablePrimitives
name|createPrimitives
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|TablePrimitives
name|primitives
init|=
name|context
operator|.
name|newObject
argument_list|(
name|TablePrimitives
operator|.
name|class
argument_list|)
decl_stmt|;
name|primitives
operator|.
name|setIntColumn
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
return|return
name|primitives
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTAbs
parameter_list|()
throws|throws
name|Exception
block|{
name|TablePrimitives
name|p1
init|=
name|createPrimitives
argument_list|(
operator|-
literal|10
argument_list|)
decl_stmt|;
name|TablePrimitives
name|p2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|TablePrimitives
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|TablePrimitives
operator|.
name|INT_COLUMN
operator|.
name|abs
argument_list|()
operator|.
name|eq
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTSqrt
parameter_list|()
throws|throws
name|Exception
block|{
name|TablePrimitives
name|p1
init|=
name|createPrimitives
argument_list|(
literal|9
argument_list|)
decl_stmt|;
name|TablePrimitives
name|p2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|TablePrimitives
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|TablePrimitives
operator|.
name|INT_COLUMN
operator|.
name|sqrt
argument_list|()
operator|.
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTMod
parameter_list|()
throws|throws
name|Exception
block|{
name|TablePrimitives
name|p1
init|=
name|createPrimitives
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|TablePrimitives
name|p2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|TablePrimitives
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|TablePrimitives
operator|.
name|INT_COLUMN
operator|.
name|mod
argument_list|(
literal|3
argument_list|)
operator|.
name|eq
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTAbsParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"abs(-3)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3.0
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTSqrtParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"sqrt(16)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4.0
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTModParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"mod(11,2)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"10 - mod(sqrt(abs(-9)), 2)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|9L
argument_list|)
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

