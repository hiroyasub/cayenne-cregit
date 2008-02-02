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
comment|/**  * The ROT-13 password encoder passes the text of the database password  * through a simple Caesar cipher to obscure the password text.  The ROT-13  * cipher only processes letters -- numbers and symbols are left untouched.  * ROT-13 is also a symmetrical cipher and therefore provides no real  * encryption since applying the cipher to the encrypted text produces the  * original source text.  See the Wikipedia entry on  *<a href="http://en.wikipedia.org/wiki/Rot-13">ROT13</a>  * for more information on this topic.  *   * @since 3.0  * @author Michael Gentry  */
end_comment

begin_class
specifier|public
class|class
name|Rot13PasswordEncoder
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
comment|/**    * Applies a ROT-13 Caesar cipher to the supplied value.  Each letter in    * the supplied value is substituted with a new value rotated by 13 places    * in the alphabet.  See<a href="http://en.wikipedia.org/wiki/ROT13">ROT13</a>    * for more information.    *<p>    * A Unix command to perform a ROT-13 cipher is:    *<pre>tr "[a-m][n-z][A-M][N-Z]" "[n-z][a-m][N-Z][A-M]"</pre>    *       * @param value The text to be rotated.    * @return The rotated text.    */
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
comment|// If c is a letter, rotate it by 13.  Numbers/symbols are untouched.
if|if
condition|(
operator|(
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'m'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'M'
operator|)
condition|)
name|c
operator|+=
literal|13
expr_stmt|;
comment|// The first half of the alphabet goes forward 13 letters
if|else if
condition|(
operator|(
name|c
operator|>=
literal|'n'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|)
condition|)
name|c
operator|-=
literal|13
expr_stmt|;
comment|// The last half of the alphabet goes backward 13 letters
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
comment|/**    * Small test program to run text through the ROT-13 cipher.  This program    * can also be run by hand to encode/decode values manually.  The values    * passed on the command line are printed to standard out.    *       * @param args The array of text values (on the command-line) to be run    *             through the ROT-13 cipher.    */
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
name|Rot13PasswordEncoder
name|encoder
init|=
operator|new
name|Rot13PasswordEncoder
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

