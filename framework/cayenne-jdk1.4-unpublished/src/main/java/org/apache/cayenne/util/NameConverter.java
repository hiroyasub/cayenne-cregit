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
name|util
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * Utility class to convert from different naming styles to Java convention. For example  * names like "ABCD_EFG" can be converted to "abcdEfg".  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|NameConverter
block|{
specifier|private
specifier|static
specifier|final
name|Map
name|SPECIAL_CHAR_TO_JAVA_MAPPING
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|SPECIAL_CHAR_TO_JAVA_MAPPING
operator|.
name|put
argument_list|(
literal|"#"
argument_list|,
literal|"pound"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Converts a String name to a String forllowing java convention for the static final      * variables. E.g. "abcXyz" will be converted to "ABC_XYZ".      *       * @since 1.0.3      */
specifier|public
specifier|static
name|String
name|javaToUnderscored
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// clear of non-java chars. While the method name implies that a passed identifier
comment|// is pure Java, it is used to build pk columns names and such, so extra safety
comment|// check is a good idea
name|name
operator|=
name|specialCharsToJava
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|char
name|charArray
index|[]
init|=
name|name
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
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
name|charArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|charArray
index|[
name|i
index|]
argument_list|)
operator|)
operator|&&
operator|(
name|i
operator|!=
literal|0
operator|)
condition|)
block|{
name|char
name|prevChar
init|=
name|charArray
index|[
name|i
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|Character
operator|.
name|isLowerCase
argument_list|(
name|prevChar
argument_list|)
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|charArray
index|[
name|i
index|]
argument_list|)
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
comment|/**      * Converts names like "ABCD_EFG_123" to Java-style names like "abcdEfg123". If      *<code>capitalize</code> is true, returned name is capitalized (for instance if      * this is a class name).      *       * @since 1.2      */
specifier|public
specifier|static
name|String
name|underscoredToJava
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|capitalize
parameter_list|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|name
argument_list|,
literal|"_"
argument_list|)
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|token
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
comment|// clear of non-java chars
name|token
operator|=
name|specialCharsToJava
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|token
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
comment|// sniff mixed case vs. single case styles
name|boolean
name|hasLowerCase
init|=
literal|false
decl_stmt|;
name|boolean
name|hasUpperCase
init|=
literal|false
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
operator|&&
operator|!
operator|(
name|hasUpperCase
operator|&&
name|hasLowerCase
operator|)
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|token
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|hasUpperCase
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|Character
operator|.
name|isLowerCase
argument_list|(
name|token
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|hasLowerCase
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// if mixed case, preserve it, if all upper, convert to lower
if|if
condition|(
name|hasUpperCase
operator|&&
operator|!
name|hasLowerCase
condition|)
block|{
name|token
operator|=
name|token
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|first
condition|)
block|{
comment|// apply explicit capitalization rules, if this is the first token
name|first
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|capitalize
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|token
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toLowerCase
argument_list|(
name|token
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|token
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
literal|1
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|token
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Replaces special chars with human-readable and Java-id-compatible symbols.      */
specifier|static
name|String
name|specialCharsToJava
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|int
name|len
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
name|string
return|;
block|}
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
name|len
argument_list|)
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
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|word
init|=
name|SPECIAL_CHAR_TO_JAVA_MAPPING
operator|.
name|get
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|word
operator|!=
literal|null
condition|?
name|word
else|:
literal|"_"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

