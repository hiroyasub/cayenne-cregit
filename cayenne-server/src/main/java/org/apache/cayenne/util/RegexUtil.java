begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * A collection of utility methods related to regular expressions processing.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|RegexUtil
block|{
specifier|static
specifier|final
name|Pattern
name|BACKSLASH
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\\\"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Pattern
name|DOT
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
comment|/**      * Replaces all backslashes "\" with forward slashes "/". Convenience method to      * convert path Strings to URI format.      */
specifier|static
name|String
name|substBackslashes
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Matcher
name|matcher
init|=
name|BACKSLASH
operator|.
name|matcher
argument_list|(
name|string
argument_list|)
decl_stmt|;
return|return
name|matcher
operator|.
name|find
argument_list|()
condition|?
name|matcher
operator|.
name|replaceAll
argument_list|(
literal|"\\/"
argument_list|)
else|:
name|string
return|;
block|}
comment|/**      * Returns package name for the Java class as a path separated with forward slash      * ("/"). Method is used to lookup resources that are located in package      * subdirectories. For example, a String "a/b/c" will be returned for class name      * "a.b.c.ClassName".      */
specifier|static
name|String
name|getPackagePath
parameter_list|(
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|Matcher
name|matcher
init|=
name|DOT
operator|.
name|matcher
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|path
init|=
name|matcher
operator|.
name|replaceAll
argument_list|(
literal|"\\/"
argument_list|)
decl_stmt|;
return|return
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
comment|/**      * Converts a SQL-style pattern to a valid Perl regular expression. E.g.:      *<p>      *<code>"billing_%"</code> will become<code>^billing_.*$</code>      *<p>      *<code>"user?"</code> will become<code>^user.?$</code>      */
specifier|static
name|String
name|sqlPatternToRegex
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null pattern."
argument_list|)
throw|;
block|}
if|if
condition|(
name|pattern
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty pattern."
argument_list|)
throw|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// convert * into regex syntax
comment|// e.g. abc*x becomes ^abc.*x$
comment|// or abc?x becomes ^abc.?x$
name|buffer
operator|.
name|append
argument_list|(
literal|"^"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|pattern
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|char
name|nextChar
init|=
name|pattern
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextChar
operator|==
literal|'%'
condition|)
block|{
name|nextChar
operator|=
literal|'*'
expr_stmt|;
block|}
if|if
condition|(
name|nextChar
operator|==
literal|'*'
operator|||
name|nextChar
operator|==
literal|'?'
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
comment|// escape special chars
if|else if
condition|(
name|nextChar
operator|==
literal|'.'
operator|||
name|nextChar
operator|==
literal|'/'
operator|||
name|nextChar
operator|==
literal|'$'
operator|||
name|nextChar
operator|==
literal|'^'
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|nextChar
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"$"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|RegexUtil
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

