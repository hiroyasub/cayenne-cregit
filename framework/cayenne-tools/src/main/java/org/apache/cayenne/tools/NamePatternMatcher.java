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
name|tools
package|;
end_package

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
name|StringTokenizer
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_comment
comment|/**  * Provides name pattern matching functionality.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|NamePatternMatcher
block|{
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|Pattern
index|[]
name|itemIncludeFilters
decl_stmt|;
specifier|protected
name|Pattern
index|[]
name|itemExcludeFilters
decl_stmt|;
specifier|public
name|NamePatternMatcher
parameter_list|(
name|Log
name|logger
parameter_list|,
name|String
name|includePattern
parameter_list|,
name|String
name|excludePattern
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|itemIncludeFilters
operator|=
name|createPatterns
argument_list|(
name|includePattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|itemExcludeFilters
operator|=
name|createPatterns
argument_list|(
name|excludePattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * Applies preconfigured list of filters to the list, removing entities that do not      * pass the filter.      *       * @deprecated since 3.0 still used by AntDataPortDelegate, which itself should      *             probably be deprecated      */
name|List
argument_list|<
name|?
argument_list|>
name|filter
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|items
parameter_list|)
block|{
if|if
condition|(
name|items
operator|==
literal|null
operator|||
name|items
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|items
return|;
block|}
if|if
condition|(
operator|(
name|itemIncludeFilters
operator|.
name|length
operator|==
literal|0
operator|)
operator|&&
operator|(
name|itemExcludeFilters
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
return|return
name|items
return|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|items
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CayenneMapEntry
name|entity
init|=
operator|(
name|CayenneMapEntry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|passedIncludeFilter
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|passedExcludeFilter
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|items
return|;
block|}
comment|/**      * Returns true if the entity matches any one of the "include" patterns, or if there      * is no "include" patterns defined.      *       * @deprecated since 3.0. still used by AntDataPortDelegate, which itself should      *             probably be deprecated      */
specifier|private
name|boolean
name|passedIncludeFilter
parameter_list|(
name|CayenneMapEntry
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
name|String
name|itemName
init|=
name|item
operator|.
name|getName
argument_list|()
decl_stmt|;
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
name|itemName
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
comment|/**      * Returns true if the entity does not match any one of the "exclude" patterns, or if      * there is no "exclude" patterns defined.      *       * @deprecated since 3.0      */
specifier|private
name|boolean
name|passedExcludeFilter
parameter_list|(
name|CayenneMapEntry
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
name|String
name|itemName
init|=
name|item
operator|.
name|getName
argument_list|()
decl_stmt|;
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
name|itemName
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
comment|/**      * Returns an array of Patterns. Takes a comma-separated list of patterns, attempting      * to convert them to the java.util.regex.Pattern syntax. E.g.      *<p>      *<code>"billing_*,user?"</code> will become an array of two expressions:      *<p>      *<code>^billing_.*$</code><br>      *<code>^user.?$</code><br>      */
specifier|public
name|Pattern
index|[]
name|createPatterns
parameter_list|(
name|String
name|patternString
parameter_list|)
block|{
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
argument_list|<
name|Pattern
argument_list|>
argument_list|(
name|patternStrings
operator|.
name|length
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
name|patternStrings
operator|.
name|length
condition|;
name|i
operator|++
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
name|patternStrings
index|[
name|i
index|]
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
name|patternStrings
index|[
name|i
index|]
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
operator|!=
literal|null
operator|&&
name|pattern
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|pattern
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|toks
operator|.
name|countTokens
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
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|patterns
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
name|String
name|nextPattern
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
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
name|nextPattern
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
name|nextPattern
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
name|patterns
operator|.
name|add
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|patterns
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|patterns
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
block|}
comment|/**      * Returns true if a given object property satisfies the include/exclude patterns.      *       * @since 3.0      */
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
operator|(
name|itemIncludeFilters
operator|.
name|length
operator|==
literal|0
operator|)
operator|&&
operator|(
name|itemExcludeFilters
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|passedIncludeFilter
argument_list|(
name|string
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|passedExcludeFilter
argument_list|(
name|string
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if an object matches any one of the "include" patterns, or if there is      * no "include" patterns defined.      *       * @since 3.0      */
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
specifier|public
specifier|static
name|String
name|replaceWildcardInStringWithString
parameter_list|(
name|String
name|wildcard
parameter_list|,
name|String
name|pattern
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|pattern
operator|||
literal|null
operator|==
name|wildcard
condition|)
return|return
name|pattern
return|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|lastPos
init|=
literal|0
decl_stmt|;
name|int
name|wildCardPos
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
name|wildcard
argument_list|)
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|wildCardPos
condition|)
block|{
if|if
condition|(
name|lastPos
operator|!=
name|wildCardPos
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|lastPos
argument_list|,
name|wildCardPos
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|replacement
argument_list|)
expr_stmt|;
name|lastPos
operator|+=
name|wildCardPos
operator|+
name|wildcard
operator|.
name|length
argument_list|()
expr_stmt|;
name|wildCardPos
operator|=
name|pattern
operator|.
name|indexOf
argument_list|(
name|wildcard
argument_list|,
name|lastPos
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lastPos
operator|<
name|pattern
operator|.
name|length
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|lastPos
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
block|}
end_class

end_unit

