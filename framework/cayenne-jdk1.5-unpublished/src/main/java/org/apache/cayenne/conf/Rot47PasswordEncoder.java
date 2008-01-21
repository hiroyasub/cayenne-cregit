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
name|conf
package|;
end_package

begin_comment
comment|/**  * The ROT-47 password encoder passes the text of the database password  * through a simple Caesar cipher to obscure the password text.  The ROT-47  * cipher is similar to the ROT-13 cipher, but processes numbers and symbols  * as well. See the Wikipedia entry on  *<a href="http://en.wikipedia.org/wiki/Rot-13">ROT13</a>  * for more information on this topic.  *  * @since 3.0  * @author Michael Gentry  */
end_comment

begin_class
specifier|public
class|class
name|Rot47PasswordEncoder
implements|implements
name|PasswordEncoding
block|{
comment|/* (non-Javadoc)    * @see org.apache.cayenne.conf.PasswordEncoding#decodePassword(java.lang.String, java.lang.String)    */
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
comment|/* (non-Javadoc)    * @see org.apache.cayenne.conf.PasswordEncoding#encodePassword(java.lang.String, java.lang.String)    */
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
comment|/**    * Applies a ROT-47 Caesar cipher to the supplied value.  Each letter in    * the supplied value is substituted with a new value rotated by 47 places.    * See<a href="http://en.wikipedia.org/wiki/ROT13">ROT13</a> for more    * information (there is a subsection for ROT-47).    *<p>    * A Unix command to perform a ROT-47 cipher is:    *<pre>tr '!-~' 'P-~!-O'</pre>    *       * @param value The text to be rotated.    * @return The rotated text.    */
specifier|public
name|String
name|rotate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|int
name|length
init|=
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuffer
name|result
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
comment|// Range of printable characters is ! (33) to ~ (126).  A value
comment|// of 127 (just above ~) would therefore get rotated down to a
comment|// 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
comment|// therefore the value that needs to be subtracted from the
comment|// non-printable character to put it into the correct printable
comment|// range.
if|if
condition|(
name|c
operator|>
literal|'~'
condition|)
name|c
operator|-=
literal|94
expr_stmt|;
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
comment|/**    * Small test program to run text through the ROT-47 cipher.  This program    * can also be run by hand to encode/decode values manually.  The values    * passed on the command line are printed to standard out.    *       * @param args The array of text values (on the command-line) to be run    *             through the ROT-47 cipher.    */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Rot47PasswordEncoder
name|encoder
init|=
operator|new
name|Rot47PasswordEncoder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|string
range|:
name|args
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|encoder
operator|.
name|rotate
argument_list|(
name|string
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

