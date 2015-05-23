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

begin_class
specifier|public
class|class
name|ASTNotTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate
parameter_list|()
block|{
name|Expression
name|toNegate
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"artistName = 'abc'"
argument_list|)
decl_stmt|;
name|ASTNot
name|e
init|=
operator|new
name|ASTNot
argument_list|(
operator|(
name|Node
operator|)
name|toNegate
argument_list|)
decl_stmt|;
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
literal|"abc"
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
literal|"123"
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
name|match
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

