begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|value
operator|.
name|json
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|JsonTokenizerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNull
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NULL
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"NULL"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NULL
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  nUlL  "
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NULL
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTrue
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|TRUE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"TRUE"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|TRUE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  tRuE  "
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|TRUE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFalse
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|FALSE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"FALSE"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|FALSE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  fAlSe  "
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|FALSE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNotNull
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  nUlLs  "
argument_list|)
decl_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnknown
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  abc  "
argument_list|)
decl_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNumber
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-0"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-0"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"0.0123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0.0123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"0."
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0."
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"123.123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123.123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-0.0123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-0.0123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-123.123"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-123.123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"123e13"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123e13"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-123e13"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-123e13"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-123e-13"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-123e-13"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"-123e+13"
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-123e+13"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  -0.001e+001  "
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-0.001e+001"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testString
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"\"123\""
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"\"test\\ttest\\ntest\""
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test\\ttest\\ntest"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"\"test\\\"test\\\"test\""
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test\\\"test\\\"test"
argument_list|,
name|token
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tokenizer
operator|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"\"test\\\\test\\\\test\""
argument_list|)
expr_stmt|;
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test\\\\test\\\\test"
argument_list|,
name|token
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
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedString
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"\"1234567"
argument_list|)
decl_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyObject
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{}"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token1
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token2
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|OBJECT_START
argument_list|,
name|token1
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|OBJECT_END
argument_list|,
name|token2
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedObject
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{"
argument_list|)
decl_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedObject2
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{\"abc\""
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedObject3
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{\"abc\":"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedObject4
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{\"abc\":123"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedObject5
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"{\"abc\":123,"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObject
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"  {\"test\": 123,\n\t\"2\": \"abc\"  } "
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token1
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token2
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token3
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token4
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token5
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|OBJECT_START
argument_list|,
name|token1
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token2
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|token2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token3
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|token3
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token4
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|token4
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
argument_list|,
name|token5
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|token5
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyArray
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"[]"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token1
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token2
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|ARRAY_START
argument_list|,
name|token1
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|ARRAY_END
argument_list|,
name|token2
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedArray
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"["
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedArray2
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"[1"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedJsonException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnclosedArray3
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"[1,"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArray
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
literal|"[0,2]"
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token1
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token2
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token3
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token4
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|ARRAY_START
argument_list|,
name|token1
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token2
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
name|token2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NUMBER
argument_list|,
name|token3
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|token3
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|ARRAY_END
argument_list|,
name|token4
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFullJson
parameter_list|()
block|{
name|JsonTokenizer
name|tokenizer
init|=
operator|new
name|JsonTokenizer
argument_list|(
name|JSON
argument_list|)
decl_stmt|;
name|JsonTokenizer
operator|.
name|JsonToken
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|)
operator|.
name|getType
argument_list|()
operator|!=
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
condition|)
block|{
comment|// do nothing, just trying to read everything
block|}
name|assertEquals
argument_list|(
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|NONE
argument_list|,
name|token
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|String
name|JSON
init|=
literal|"[\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ffde690418e483588a\",\n"
operator|+
literal|"    \"index\": 0,\n"
operator|+
literal|"    \"guid\": \"e7cb7511-5b58-482b-9662-bbabc17c7999\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,019.14\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 40,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Briana Jimenez\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"VERBUS\",\n"
operator|+
literal|"    \"email\": \"brianajimenez@verbus.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (911) 471-2705\",\n"
operator|+
literal|"    \"address\": \"178 Ashland Place, Cetronia, Alaska, 7446\",\n"
operator|+
literal|"    \"about\": \"Do ullamco et nulla incididunt dolore culpa voluptate et cupidatat excepteur labore proident. Nisi exercitation tempor duis est reprehenderit exercitation aliquip velit veniam. Fugiat mollit pariatur enim qui excepteur minim officia sunt mollit sint do.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2016-12-21T04:56:36 -03:00\",\n"
operator|+
literal|"    \"latitude\": -68.436891,\n"
operator|+
literal|"    \"longitude\": -40.276385,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"voluptate\",\n"
operator|+
literal|"      \"irure\",\n"
operator|+
literal|"      \"eu\",\n"
operator|+
literal|"      \"voluptate\",\n"
operator|+
literal|"      \"do\",\n"
operator|+
literal|"      \"mollit\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Hyde Thompson\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Cathleen Mercer\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Emilia Mckenzie\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Briana Jimenez! You have 3 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"apple\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ffad699bf86a1de6f1\",\n"
operator|+
literal|"    \"index\": 1,\n"
operator|+
literal|"    \"guid\": \"7a25ff47-980f-4163-b679-5b82e6bc0693\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$3,888.34\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 20,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Finley Hawkins\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"OMATOM\",\n"
operator|+
literal|"    \"email\": \"finleyhawkins@omatom.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (904) 545-2548\",\n"
operator|+
literal|"    \"address\": \"552 Pilling Street, Roosevelt, American Samoa, 3424\",\n"
operator|+
literal|"    \"about\": \"Aliquip ad cillum minim exercitation officia proident laborum excepteur est laborum irure laboris. Nisi pariatur labore Lorem et ad exercitation. Occaecat ullamco exercitation ut in anim eiusmod sint pariatur dolor Lorem elit incididunt nulla.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2019-03-22T09:16:38 -03:00\",\n"
operator|+
literal|"    \"latitude\": 8.588498,\n"
operator|+
literal|"    \"longitude\": 140.490892,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"ex\",\n"
operator|+
literal|"      \"dolore\",\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"minim\",\n"
operator|+
literal|"      \"excepteur\",\n"
operator|+
literal|"      \"minim\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Iris Fletcher\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Moss Whitfield\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Esmeralda Christensen\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Finley Hawkins! You have 5 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ffb2a31b910a2159f1\",\n"
operator|+
literal|"    \"index\": 2,\n"
operator|+
literal|"    \"guid\": \"ab53f6a7-25e2-41e9-9744-7fbe01b16f6b\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$1,083.29\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 34,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Wendi Bowen\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"ZILLANET\",\n"
operator|+
literal|"    \"email\": \"wendibowen@zillanet.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (874) 458-3093\",\n"
operator|+
literal|"    \"address\": \"601 Fountain Avenue, Boonville, Maine, 6733\",\n"
operator|+
literal|"    \"about\": \"Eu exercitation est duis occaecat excepteur tempor sint culpa. Dolore ullamco irure pariatur reprehenderit esse qui. Exercitation tempor non duis elit exercitation cupidatat sunt ad adipisicing id. Mollit mollit reprehenderit voluptate sunt dolor nulla id. Tempor officia elit ut officia Lorem in veniam.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2017-08-26T10:33:44 -03:00\",\n"
operator|+
literal|"    \"latitude\": -85.532155,\n"
operator|+
literal|"    \"longitude\": -127.824759,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"est\",\n"
operator|+
literal|"      \"exercitation\",\n"
operator|+
literal|"      \"reprehenderit\",\n"
operator|+
literal|"      \"aliqua\",\n"
operator|+
literal|"      \"irure\",\n"
operator|+
literal|"      \"in\",\n"
operator|+
literal|"      \"non\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Bridget Todd\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Mccall Dennis\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Willis Cohen\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Wendi Bowen! You have 3 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ffb828546ee5d53781\",\n"
operator|+
literal|"    \"index\": 3,\n"
operator|+
literal|"    \"guid\": \"63d5c010-6b97-4fdd-9733-52824dfdab01\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$3,395.34\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"blue\",\n"
operator|+
literal|"    \"name\": \"Harrell Zamora\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"LUDAK\",\n"
operator|+
literal|"    \"email\": \"harrellzamora@ludak.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (839) 494-3495\",\n"
operator|+
literal|"    \"address\": \"698 Kenilworth Place, Whitmer, Hawaii, 963\",\n"
operator|+
literal|"    \"about\": \"Ex nisi minim adipisicing et amet sint sunt minim deserunt dolore. Incididunt tempor dolore tempor ipsum officia mollit non. Officia aute aute consequat amet mollit sit officia. Nostrud in laborum do duis.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2014-09-08T08:52:48 -03:00\",\n"
operator|+
literal|"    \"latitude\": 48.622813,\n"
operator|+
literal|"    \"longitude\": -52.26753,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"proident\",\n"
operator|+
literal|"      \"nulla\",\n"
operator|+
literal|"      \"enim\",\n"
operator|+
literal|"      \"nostrud\",\n"
operator|+
literal|"      \"fugiat\",\n"
operator|+
literal|"      \"qui\",\n"
operator|+
literal|"      \"dolore\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Burris Mercado\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Bertie Schroeder\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Estrada Hampton\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Harrell Zamora! You have 1 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"apple\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ff8f09e316ea28bbaf\",\n"
operator|+
literal|"    \"index\": 4,\n"
operator|+
literal|"    \"guid\": \"0fc17d32-5a40-4d30-85ac-f4f3f6d146b1\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$3,671.08\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 33,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Bernice Allison\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"INSOURCE\",\n"
operator|+
literal|"    \"email\": \"berniceallison@insource.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (816) 459-3811\",\n"
operator|+
literal|"    \"address\": \"417 Gerry Street, Lavalette, South Dakota, 7943\",\n"
operator|+
literal|"    \"about\": \"Id amet magna dolor occaecat aute dolor ullamco voluptate irure Lorem sunt. Elit aliqua ad Lorem irure aute eu. Incididunt ullamco elit dolore consectetur ipsum anim non incididunt dolor sit in consequat. Eiusmod ea ut fugiat voluptate cupidatat ullamco esse in. Ea voluptate excepteur duis labore excepteur occaecat. Tempor est ad anim eu ea. Eu irure do reprehenderit veniam velit ex eu incididunt officia eiusmod aliquip excepteur nisi.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2015-03-27T10:29:54 -03:00\",\n"
operator|+
literal|"    \"latitude\": 24.183295,\n"
operator|+
literal|"    \"longitude\": 82.144996,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"enim\",\n"
operator|+
literal|"      \"occaecat\",\n"
operator|+
literal|"      \"laborum\",\n"
operator|+
literal|"      \"velit\",\n"
operator|+
literal|"      \"fugiat\",\n"
operator|+
literal|"      \"anim\",\n"
operator|+
literal|"      \"sint\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Christine Horton\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Fowler Good\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Antoinette Cooper\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Bernice Allison! You have 9 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ff88489df59c89193c\",\n"
operator|+
literal|"    \"index\": 5,\n"
operator|+
literal|"    \"guid\": \"1b3b06d6-88d2-4b89-ade9-252de03c11b2\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$3,814.72\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 26,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Thornton Alexander\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"ASSITIA\",\n"
operator|+
literal|"    \"email\": \"thorntonalexander@assitia.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (862) 524-2047\",\n"
operator|+
literal|"    \"address\": \"247 Beach Place, Barronett, Guam, 272\",\n"
operator|+
literal|"    \"about\": \"Consequat nulla occaecat aliquip fugiat fugiat ipsum. Veniam incididunt ad est enim sit aliquip exercitation et do sint voluptate. Nostrud culpa velit cillum Lorem labore laborum id voluptate ad et.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2018-01-17T05:36:12 -03:00\",\n"
operator|+
literal|"    \"latitude\": -55.650877,\n"
operator|+
literal|"    \"longitude\": 42.279245,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"do\",\n"
operator|+
literal|"      \"qui\",\n"
operator|+
literal|"      \"id\",\n"
operator|+
literal|"      \"eiusmod\",\n"
operator|+
literal|"      \"labore\",\n"
operator|+
literal|"      \"consequat\",\n"
operator|+
literal|"      \"ullamco\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Helen Copeland\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Hall Joseph\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Ursula Mckee\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Thornton Alexander! You have 7 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"5fc4d1ff9e6d40f38aceb25f\",\n"
operator|+
literal|"    \"index\": 6,\n"
operator|+
literal|"    \"guid\": \"cd160d1b-7d66-4746-866c-2390d236e6db\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$1,385.92\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"blue\",\n"
operator|+
literal|"    \"name\": \"Ester Cooke\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"DAYCORE\",\n"
operator|+
literal|"    \"email\": \"estercooke@daycore.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (814) 450-3865\",\n"
operator|+
literal|"    \"address\": \"922 Coleman Street, Johnsonburg, Georgia, 8863\",\n"
operator|+
literal|"    \"about\": \"Commodo nisi officia deserunt pariatur cillum adipisicing incididunt. Duis pariatur duis consectetur dolor magna aute sunt. Enim occaecat mollit veniam qui voluptate. Ea id fugiat laborum eu aute esse mollit id consequat deserunt. Amet incididunt cupidatat fugiat do Lorem veniam dolor aliquip aliquip magna anim velit. Sint do commodo tempor tempor tempor irure cillum velit consequat sunt ut est.\\r\\n\",\n"
operator|+
literal|"    \"registered\": \"2014-08-23T08:53:19 -03:00\",\n"
operator|+
literal|"    \"latitude\": 26.474005,\n"
operator|+
literal|"    \"longitude\": -122.921901,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"esse\",\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"adipisicing\",\n"
operator|+
literal|"      \"sunt\",\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"esse\",\n"
operator|+
literal|"      \"quis\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Harding Sampson\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Rosario Hansen\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Larsen Black\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Ester Cooke! You have 8 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  }\n"
operator|+
literal|"]"
decl_stmt|;
block|}
end_class

end_unit

