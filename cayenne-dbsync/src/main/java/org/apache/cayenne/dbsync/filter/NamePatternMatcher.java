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
name|dbsync
operator|.
name|filter
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
name|util
operator|.
name|CayenneMapEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|PatternSyntaxException
import|;
end_import

begin_comment
comment|/**  * Provides name pattern matching functionality.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|NamePatternMatcher
implements|implements
name|NameFilter
block|{
specifier|public
specifier|static
specifier|final
name|NameFilter
name|EXCLUDE_ALL
init|=
operator|new
name|NameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|obj
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EMPTY_ARRAY
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|COMMA
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|","
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Pattern
index|[]
name|itemIncludeFilters
decl_stmt|;
specifier|private
specifier|final
name|Pattern
index|[]
name|itemExcludeFilters
decl_stmt|;
specifier|public
specifier|static
name|NamePatternMatcher
name|build
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|String
name|includePattern
parameter_list|,
name|String
name|excludePattern
parameter_list|)
block|{
return|return
operator|new
name|NamePatternMatcher
argument_list|(
name|createPatterns
argument_list|(
name|logger
argument_list|,
name|includePattern
argument_list|)
argument_list|,
name|createPatterns
argument_list|(
name|logger
argument_list|,
name|excludePattern
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|NamePatternMatcher
parameter_list|(
name|Pattern
index|[]
name|itemIncludeFilters
parameter_list|,
name|Pattern
index|[]
name|itemExcludeFilters
parameter_list|)
block|{
name|this
operator|.
name|itemIncludeFilters
operator|=
name|itemIncludeFilters
expr_stmt|;
name|this
operator|.
name|itemExcludeFilters
operator|=
name|itemExcludeFilters
expr_stmt|;
block|}
comment|/**      * Returns an array of Patterns. Takes a comma-separated list of patterns, attempting      * to convert them to the java.util.regex.Pattern syntax. E.g.      *<p>      *<code>"billing_*,user?"</code> will become an array of two expressions:      *<p>      *<code>^billing_.*$</code><br>      *<code>^user.?$</code><br>      */
specifier|public
specifier|static
name|Pattern
index|[]
name|createPatterns
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|String
name|patternString
parameter_list|)
block|{
if|if
condition|(
name|patternString
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Pattern
index|[
literal|0
index|]
return|;
block|}
name|String
index|[]
name|patternStrings
init|=
name|tokenizePattern
argument_list|(
name|patternString
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Pattern
argument_list|>
name|patterns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|patternStrings
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|patternString1
range|:
name|patternStrings
control|)
block|{
comment|// test the pattern
try|try
block|{
name|patterns
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|patternString1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Ignoring invalid pattern ["
operator|+
name|patternString1
operator|+
literal|"], reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|patterns
operator|.
name|toArray
argument_list|(
operator|new
name|Pattern
index|[
name|patterns
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Returns an array of valid regular expressions. Takes a comma-separated list of      * patterns, attempting to convert them to the java.util.regex.Pattern syntax. E.g.      *<p>      *<code>"billing_*,user?"</code> will become an array of two expressions:      *<p>      *<code>^billing_.*$</code><br>      *<code>^user.?$</code><br>      */
specifier|public
specifier|static
name|String
index|[]
name|tokenizePattern
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
operator|||
name|pattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|EMPTY_ARRAY
return|;
block|}
name|String
index|[]
name|patterns
init|=
name|COMMA
operator|.
name|split
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
if|if
condition|(
name|patterns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|EMPTY_ARRAY
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|patterns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// convert * into regex syntax
comment|// e.g. abc*x becomes ^abc.*x$
comment|// or abc?x becomes ^abc.?x$
name|patterns
index|[
name|i
index|]
operator|=
literal|"^"
operator|+
name|patterns
index|[
name|i
index|]
operator|.
name|replaceAll
argument_list|(
literal|"[*?]"
argument_list|,
literal|".$0"
argument_list|)
operator|+
literal|"$"
expr_stmt|;
block|}
return|return
name|patterns
return|;
block|}
comment|/**      * Returns true if a given object property satisfies the include/exclude patterns.      *       * @since 3.0      */
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|passedIncludeFilter
argument_list|(
name|string
argument_list|)
operator|&&
name|passedExcludeFilter
argument_list|(
name|string
argument_list|)
return|;
block|}
comment|/**      * Returns true if an object matches any one of the "include" patterns, or if there is      * no "include" patterns defined.      *       * @since 3.0      */
specifier|private
name|boolean
name|passedIncludeFilter
parameter_list|(
name|String
name|item
parameter_list|)
block|{
if|if
condition|(
name|itemIncludeFilters
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Pattern
name|itemIncludeFilter
range|:
name|itemIncludeFilters
control|)
block|{
if|if
condition|(
name|itemIncludeFilter
operator|.
name|matcher
argument_list|(
name|item
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if an object does not match any one of the "exclude" patterns, or if      * there is no "exclude" patterns defined.      *       * @since 3.0      */
specifier|private
name|boolean
name|passedExcludeFilter
parameter_list|(
name|String
name|item
parameter_list|)
block|{
if|if
condition|(
name|itemExcludeFilters
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Pattern
name|itemExcludeFilter
range|:
name|itemExcludeFilters
control|)
block|{
if|if
condition|(
name|itemExcludeFilter
operator|.
name|matcher
argument_list|(
name|item
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

