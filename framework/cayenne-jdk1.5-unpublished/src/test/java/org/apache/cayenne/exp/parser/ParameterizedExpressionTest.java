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
name|Map
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
name|ExpressionException
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
name|TstTraversalHandler
import|;
end_import

begin_comment
comment|/**  * Tests parameterized expressions of the new form introduced in 1.1  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ParameterizedExpressionTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"x = null"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|TstTraversalHandler
operator|.
name|compareExps
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      *       * @throws Exception      */
specifier|public
name|void
name|testCopy1
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = 'v1' or k2 = 'v2' or k3 = 'v3'"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|TstTraversalHandler
operator|.
name|compareExps
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      */
specifier|public
name|void
name|testCopy2
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"(k1 = 'v1' and k2 = 'v2' and k3 = 'v3') or (k1 = 'v1')"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|TstTraversalHandler
operator|.
name|compareExps
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCopy3
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"(k1 / 2) = (k2 * 2)"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|TstTraversalHandler
operator|.
name|compareExps
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      */
specifier|public
name|void
name|testFailOnMissingParams
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = $test or k2 = 'v2' or k3 = 'v3'"
argument_list|)
decl_stmt|;
try|try
block|{
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Parameter was missing, but no exception was thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
specifier|public
name|void
name|testParams1
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = $test"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|map
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|EQUAL_TO
argument_list|,
name|e2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|e2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParams2
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 like $test"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|map
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|,
name|e2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|e2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoParams1
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = $test"
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// all expression nodes must be pruned
name|assertNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoParams2
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = $test1 or k2 = $test2 or k3 = $test3 or k4 = $test4"
argument_list|)
decl_stmt|;
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
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// some expression nodes must be pruned
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|k2
init|=
operator|(
name|Expression
operator|)
name|e2
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|k2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|k3
init|=
operator|(
name|Expression
operator|)
name|e2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|k3
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoParams3
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 = $test1 or k2 = $test2 or k3 = $test3 or k4 = $test4"
argument_list|)
decl_stmt|;
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
literal|"test4"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// some expression nodes must be pruned
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e2
operator|.
name|getOperandCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|e2
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"k4"
argument_list|,
operator|(
operator|(
name|Expression
operator|)
name|e2
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

