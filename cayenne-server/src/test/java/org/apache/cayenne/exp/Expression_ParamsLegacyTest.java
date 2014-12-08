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
name|HashMap
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

begin_comment
comment|/**  * Testing deprecated Expression.expWithParameters(..) API.  *   * @deprecated since 4.0  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|Expression_ParamsLegacyTest
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testExpWithParams_Prune
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"k1 = $test or k2 = $v2 or k3 = $v3"
argument_list|)
decl_stmt|;
name|Expression
name|ep
init|=
name|e
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
block|{
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"T"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"v2"
argument_list|,
literal|"K"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"v3"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(k1 = \"T\") or (k2 = \"K\") or (k3 = 5)"
argument_list|,
name|ep
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testExpWithParams_PrunePartial
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"k1 = $test or k2 = $v2 or k3 = $v3"
argument_list|)
decl_stmt|;
name|Expression
name|ep
init|=
name|e
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
block|{
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"T"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"v3"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(k1 = \"T\") or (k3 = 5)"
argument_list|,
name|ep
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testExpWithParams_NoPrune
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"k1 = $test or k2 = $v2 or k3 = $v3"
argument_list|)
decl_stmt|;
name|Expression
name|ep
init|=
name|e
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
block|{
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"T"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"v2"
argument_list|,
literal|"K"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"v3"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(k1 = \"T\") or (k2 = \"K\") or (k3 = 5)"
argument_list|,
name|ep
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ExpressionException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testExpWithParams_NoPrune_Partial
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"k1 = $test or k2 = $v2 or k3 = $v3"
argument_list|)
decl_stmt|;
name|e
operator|.
name|expWithParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

