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
name|configuration
package|;
end_package

begin_comment
comment|/**  * The ROT-47 password encoder passes the text of the database password through a simple  * Caesar cipher to obscure the password text. The ROT-47 cipher is similar to the ROT-13  * cipher, but processes numbers and symbols as well. See the Wikipedia entry on<a  * href="http://en.wikipedia.org/wiki/Rot-13">ROT13</a> for more information on this  * topic.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|Rot47PasswordEncoder
implements|implements
name|PasswordEncoding
block|{
specifier|public
name|String
name|decodePassword
parameter_list|(
name|String
name|encodedPassword
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|rotate
argument_list|(
name|encodedPassword
argument_list|)
return|;
block|}
specifier|public
name|String
name|encodePassword
parameter_list|(
name|String
name|normalPassword
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|rotate
argument_list|(
name|normalPassword
argument_list|)
return|;
block|}
comment|/**      * Applies a ROT-47 Caesar cipher to the supplied value. Each letter in the supplied      * value is substituted with a new value rotated by 47 places. See<a      * href="http://en.wikipedia.org/wiki/ROT13">ROT13</a> for more information (there is      * a subsection for ROT-47).      *<p>      * A Unix command to perform a ROT-47 cipher is:      *       *<pre>      * tr '!-~' 'P-~!-O'      *</pre>      *       * @param value The text to be rotated.      * @return The rotated text.      */
specifier|public
name|String
name|rotate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|length
init|=
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// Process letters, numbers, and symbols -- ignore spaces.
if|if
condition|(
name|c
operator|!=
literal|' '
condition|)
block|{
comment|// Add 47 (it is ROT-47, after all).
name|c
operator|+=
literal|47
expr_stmt|;
comment|// If character is now above printable range, make it printable.
comment|// Range of printable characters is ! (33) to ~ (126). A value
comment|// of 127 (just above ~) would therefore get rotated down to a
comment|// 33 (the !). The value 94 comes from 127 - 33 = 94, which is
comment|// therefore the value that needs to be subtracted from the
comment|// non-printable character to put it into the correct printable
comment|// range.
if|if
condition|(
name|c
operator|>
literal|'~'
condition|)
block|{
name|c
operator|-=
literal|94
expr_stmt|;
block|}
block|}
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

