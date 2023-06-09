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
name|gen
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
name|project
operator|.
name|validation
operator|.
name|NameValidationHelper
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
name|Util
import|;
end_import

begin_comment
comment|/**  * Methods for mangling strings.  */
end_comment

begin_class
specifier|public
class|class
name|StringUtils
block|{
specifier|private
specifier|static
name|StringUtils
name|sharedInstance
decl_stmt|;
specifier|public
specifier|static
name|StringUtils
name|getInstance
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|sharedInstance
condition|)
block|{
name|sharedInstance
operator|=
operator|new
name|StringUtils
argument_list|()
expr_stmt|;
block|}
return|return
name|sharedInstance
return|;
block|}
comment|/**      * Prepends underscore to variable name if necessary to remove conflict with reserved      * keywords.      */
specifier|public
name|String
name|formatVariableName
parameter_list|(
name|String
name|variableName
parameter_list|)
block|{
if|if
condition|(
name|NameValidationHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|isReservedJavaKeyword
argument_list|(
name|variableName
argument_list|)
condition|)
block|{
return|return
literal|"_"
operator|+
name|variableName
return|;
block|}
else|else
block|{
return|return
name|variableName
return|;
block|}
block|}
comment|/**      * Removes package name, leaving base name.      *       * @since 1.2      */
specifier|public
name|String
name|stripPackageName
parameter_list|(
name|String
name|fullyQualifiedClassName
parameter_list|)
block|{
return|return
name|Util
operator|.
name|stripPackageName
argument_list|(
name|fullyQualifiedClassName
argument_list|)
return|;
block|}
comment|/**      * Removes base name, leaving package name.      *       * @since 1.2      */
specifier|public
name|String
name|stripClass
parameter_list|(
name|String
name|aString
parameter_list|)
block|{
if|if
condition|(
name|aString
operator|==
literal|null
operator|||
name|aString
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|aString
return|;
block|}
name|int
name|lastDot
init|=
name|aString
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|==
name|lastDot
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|aString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastDot
argument_list|)
return|;
block|}
comment|/**      * Capitalizes the first letter of the property name.      *       * @since 1.1      */
specifier|public
name|String
name|capitalized
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Util
operator|.
name|capitalized
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns string with lowercased first letter      *       * @since 1.2      */
specifier|public
name|String
name|uncapitalized
parameter_list|(
name|String
name|aString
parameter_list|)
block|{
return|return
name|Util
operator|.
name|uncapitalized
argument_list|(
name|aString
argument_list|)
return|;
block|}
comment|/**      * Converts property name to Java constants naming convention.      *       * @since 1.1      */
specifier|public
name|String
name|capitalizedAsConstant
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
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|name
return|;
block|}
comment|// clear of non-java chars. While the method name implies that a passed identifier
comment|// is pure Java, it is used to build pk columns names and such, so extra safety
comment|// check is a good idea
name|name
operator|=
name|Util
operator|.
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
name|StringBuilder
name|buffer
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
comment|/**      * Converts entity or property name to a plural form. For example:      *<ul>      *<li>pluralize("Word") == "Words"</li>      *<li>pluralize("Status") == "Statuses"</li>      *<li>pluralize("Index") == "Indexes"</li>      *<li>pluralize("Factory") == "Factories"</li>      *</ul>      *<p>      * As of 3.1 this method is not used in bundled templates, and is present here for      * user templates convenience.      *       * @since 3.1      */
specifier|public
name|String
name|pluralize
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|str
return|;
block|}
if|else if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
operator|||
name|str
operator|.
name|endsWith
argument_list|(
literal|"x"
argument_list|)
condition|)
block|{
return|return
name|str
operator|+
literal|"es"
return|;
block|}
if|else if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"y"
argument_list|)
condition|)
block|{
return|return
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
literal|"ies"
return|;
block|}
else|else
block|{
return|return
name|str
operator|+
literal|"s"
return|;
block|}
block|}
comment|/**      *<p>      * Strip generic definition from string      *</p>      *<p>For example: List&gt;Integer&lt; == List</p>      * @since 4.0      */
specifier|public
name|String
name|stripGeneric
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|start
init|=
name|str
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|str
return|;
block|}
name|int
name|end
init|=
name|str
operator|.
name|lastIndexOf
argument_list|(
literal|'>'
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|str
return|;
block|}
if|else if
condition|(
name|end
operator|==
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|start
argument_list|)
return|;
block|}
return|return
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|start
argument_list|)
operator|+
name|str
operator|.
name|substring
argument_list|(
name|end
operator|+
literal|1
argument_list|)
return|;
block|}
specifier|public
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
name|pattern
operator|==
literal|null
operator|||
name|wildcard
operator|==
literal|null
condition|)
block|{
return|return
name|pattern
return|;
block|}
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
name|wildCardPos
operator|!=
operator|-
literal|1
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

