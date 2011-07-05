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
name|io
operator|.
name|PrintWriter
import|;
end_import

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
name|List
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
name|parser
operator|.
name|ASTLike
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
name|ASTLikeIgnoreCase
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
name|SelectQuery
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ExpressionFactoryTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|void
name|testExpressionOfBadType
parameter_list|()
throws|throws
name|Exception
block|{
comment|// non existing type
name|int
name|badType
init|=
operator|-
literal|50
decl_stmt|;
try|try
block|{
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|badType
argument_list|)
expr_stmt|;
name|fail
argument_list|()
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
name|testBetweenExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|v2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|betweenExp
argument_list|(
literal|"abc"
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|BETWEEN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBetweenDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|v2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|betweenDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|BETWEEN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotBetweenExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|v2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|notBetweenExp
argument_list|(
literal|"abc"
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|NOT_BETWEEN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotBetweenDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|v2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|notBetweenDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|NOT_BETWEEN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGreaterExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|GREATER_THAN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGreaterDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|greaterDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|GREATER_THAN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGreaterOrEqualExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|GREATER_THAN_EQUAL_TO
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGreaterOrEqualDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|greaterOrEqualDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|GREATER_THAN_EQUAL_TO
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLessExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LESS_THAN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLessDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|lessDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LESS_THAN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLessOrEqualExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LESS_THAN_EQUAL_TO
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLessOrEqualDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|v
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|lessOrEqualDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LESS_THAN_EQUAL_TO
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInExp1
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|IN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInExp2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|v
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|v
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|v
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|IN
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInExp3
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|v
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|FALSE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeExp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeExpEscape
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"=abc"
argument_list|,
name|v
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'='
argument_list|,
operator|(
operator|(
name|ASTLike
operator|)
name|exp
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeIgnoreCaseExp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|ASTLikeIgnoreCase
operator|)
name|exp
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeIgnoreCaseExpEscape
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseExp
argument_list|(
literal|"=abc"
argument_list|,
name|v
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'='
argument_list|,
operator|(
operator|(
name|ASTLikeIgnoreCase
operator|)
name|exp
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeIgnoreCaseDbExp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseDbExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotLikeIgnoreCaseExp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|v
init|=
literal|"abc"
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|notLikeIgnoreCaseExp
argument_list|(
literal|"abc"
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// testing CAY-941 bug
specifier|public
name|void
name|testLikeExpNull
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"abc"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|,
name|exp
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|path
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|,
name|path
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exp
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// CAY-416
specifier|public
name|void
name|testCollectionMatch
parameter_list|()
block|{
name|Artist
name|artist
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Painting
name|p1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|,
name|p2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|,
name|p3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p3
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p3
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|notInExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIn
parameter_list|()
block|{
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|Painting
name|p1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|Painting
name|p2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|Expression
name|in
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|match
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEscapeCharacter
parameter_list|()
block|{
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"A_1"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"A_2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|ex1
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseDbExp
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"A*_1"
argument_list|,
literal|'*'
argument_list|)
decl_stmt|;
name|SelectQuery
name|q1
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|ex2
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"A*_2"
argument_list|,
literal|'*'
argument_list|)
decl_stmt|;
name|SelectQuery
name|q2
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex2
argument_list|)
decl_stmt|;
name|artists
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

