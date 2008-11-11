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
package|;
end_package

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
name|parser
operator|.
name|ASTList
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
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ParametrizedExpressionTest
extends|extends
name|CayenneCase
block|{
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      *       * @throws Exception      */
specifier|public
name|void
name|testCopy1
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
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
decl_stmt|;
name|e1
operator|=
name|e1
operator|.
name|orExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|=
name|e1
operator|.
name|orExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k3"
argument_list|,
literal|"v3"
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|()
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
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      *       * @throws Exception      */
specifier|public
name|void
name|testCopy2
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|andExp
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
decl_stmt|;
name|andExp
operator|=
name|andExp
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
argument_list|)
expr_stmt|;
name|andExp
operator|=
name|andExp
operator|.
name|andExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k3"
argument_list|,
literal|"v3"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|exprs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|exprs
operator|.
name|add
argument_list|(
name|andExp
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|joinExp
argument_list|(
name|Expression
operator|.
name|OR
argument_list|,
name|exprs
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|()
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
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      *       * @throws Exception      */
specifier|public
name|void
name|testInParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|inExp
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 in $test"
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"k1 in ('a', 'b')"
argument_list|)
decl_stmt|;
name|TstTraversalHandler
operator|.
name|compareExps
argument_list|(
name|e1
argument_list|,
name|inExp
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"test"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests how parameter substitution algorithm works on an expression with no      * parameters.      *       * @throws Exception      */
specifier|public
name|void
name|testFailOnMissingParams
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
literal|"k1"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|e1
operator|=
name|e1
operator|.
name|orExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|=
name|e1
operator|.
name|orExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"k3"
argument_list|,
literal|"v3"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|e1
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|()
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
literal|"k1"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
name|map
init|=
operator|new
name|HashMap
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
throws|throws
name|Exception
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"k1"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|Map
name|map
init|=
operator|new
name|HashMap
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
literal|"k1"
argument_list|,
operator|new
name|ExpressionParameter
argument_list|(
literal|"test"
argument_list|)
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
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
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
name|Expression
name|e1
init|=
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
decl_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
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
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
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
name|Expression
name|e1
init|=
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
decl_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
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
name|assertTrue
argument_list|(
literal|"List expression: "
operator|+
name|e2
argument_list|,
operator|!
operator|(
name|e2
operator|instanceof
name|ASTList
operator|)
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
specifier|public
name|void
name|testNullOptionalParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 3 and x = $a"
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|e
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
comment|// $a must be pruned
name|assertEquals
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 3"
argument_list|)
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|e
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// null must be preserved
name|assertEquals
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 3 and x = null"
argument_list|)
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullRequiredParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 3 and x = $a"
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
literal|"one parameter missing....must fail.."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
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
literal|false
argument_list|)
decl_stmt|;
comment|// null must be preserved
name|assertEquals
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 3 and x = null"
argument_list|)
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

