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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|PatternMatchNode
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
class|class
name|LikeExpressionHelper
block|{
specifier|private
specifier|static
specifier|final
name|char
name|WILDCARD_SEQUENCE
init|=
literal|'%'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|WILDCARD_ONE
init|=
literal|'_'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|ESCAPE_ALPHABET
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ESCAPE_ALPHABET_START
init|=
literal|'!'
decl_stmt|;
static|static
block|{
name|ESCAPE_ALPHABET
operator|=
operator|new
name|boolean
index|[
name|Byte
operator|.
name|MAX_VALUE
index|]
expr_stmt|;
comment|// exclude certain chars, such as unprintable ones, and ?
for|for
control|(
name|int
name|i
init|=
name|ESCAPE_ALPHABET_START
init|;
name|i
operator|<
name|Byte
operator|.
name|MAX_VALUE
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
literal|'?'
operator|&&
name|i
operator|!=
literal|'\"'
operator|&&
name|i
operator|!=
literal|'\''
operator|&&
name|i
operator|!=
name|WILDCARD_SEQUENCE
operator|&&
name|i
operator|!=
name|WILDCARD_ONE
condition|)
block|{
name|ESCAPE_ALPHABET
index|[
name|i
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
specifier|static
name|void
name|toContains
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|)
block|{
name|escape
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|wrap
argument_list|(
name|exp
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|toStartsWith
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|)
block|{
name|escape
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|wrap
argument_list|(
name|exp
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|toEndsWith
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|)
block|{
name|escape
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|wrap
argument_list|(
name|exp
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|escape
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|)
block|{
name|Object
name|pattern
init|=
name|exp
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|instanceof
name|String
condition|)
block|{
comment|// find _ or % and then attempt to escape...
name|String
name|pString
init|=
name|pattern
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|pString
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|pString
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
name|WILDCARD_SEQUENCE
operator|||
name|c
operator|==
name|WILDCARD_ONE
condition|)
block|{
name|exp
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|escapeFrom
argument_list|(
name|exp
argument_list|,
name|pString
argument_list|,
name|i
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|escapeFrom
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|,
name|String
name|pattern
parameter_list|,
name|int
name|firstWildcard
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|boolean
index|[]
name|mutableEscapeAlphabet
init|=
operator|new
name|boolean
index|[
name|Byte
operator|.
name|MAX_VALUE
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|ESCAPE_ALPHABET
argument_list|,
name|ESCAPE_ALPHABET_START
argument_list|,
name|mutableEscapeAlphabet
argument_list|,
name|ESCAPE_ALPHABET_START
argument_list|,
name|Byte
operator|.
name|MAX_VALUE
operator|-
name|ESCAPE_ALPHABET_START
argument_list|)
expr_stmt|;
comment|// can't use chars already in the pattern, so exclude the ones already
comment|// taken
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|pattern
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
name|Byte
operator|.
name|MAX_VALUE
condition|)
block|{
name|mutableEscapeAlphabet
index|[
name|c
index|]
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|// find the first available char
name|char
name|escapeChar
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|ESCAPE_ALPHABET_START
init|;
name|i
operator|<
name|Byte
operator|.
name|MAX_VALUE
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|mutableEscapeAlphabet
index|[
name|i
index|]
condition|)
block|{
name|escapeChar
operator|=
operator|(
name|char
operator|)
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|escapeChar
operator|==
literal|0
condition|)
block|{
comment|// if we start seeing this this error in the wild, I guess we'll
comment|// need to extend escape char set beyond ASCII
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Could not properly escape pattern: "
operator|+
name|pattern
argument_list|)
throw|;
block|}
name|exp
operator|.
name|setEscapeChar
argument_list|(
name|escapeChar
argument_list|)
expr_stmt|;
comment|// build escaped pattern
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|len
operator|+
literal|1
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstWildcard
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|escapeChar
argument_list|)
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|charAt
argument_list|(
name|firstWildcard
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|firstWildcard
operator|+
literal|1
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|pattern
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
name|WILDCARD_SEQUENCE
operator|||
name|c
operator|==
name|WILDCARD_ONE
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|escapeChar
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|wrap
parameter_list|(
name|PatternMatchNode
name|exp
parameter_list|,
name|boolean
name|start
parameter_list|,
name|boolean
name|end
parameter_list|)
block|{
name|Object
name|pattern
init|=
name|exp
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|instanceof
name|String
condition|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|start
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|WILDCARD_SEQUENCE
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|end
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|WILDCARD_SEQUENCE
argument_list|)
expr_stmt|;
block|}
name|exp
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

