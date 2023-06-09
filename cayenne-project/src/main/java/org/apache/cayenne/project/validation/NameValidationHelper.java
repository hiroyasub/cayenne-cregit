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
name|project
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  * Defines a set of rules for validating java and db mapping identifiers.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|NameValidationHelper
block|{
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|RESERVED_JAVA_KEYWORDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"abstract"
argument_list|,
literal|"assert"
argument_list|,
literal|"default"
argument_list|,
literal|"enum"
argument_list|,
literal|"if"
argument_list|,
literal|"private"
argument_list|,
literal|"this"
argument_list|,
literal|"boolean"
argument_list|,
literal|"do"
argument_list|,
literal|"implements"
argument_list|,
literal|"protected"
argument_list|,
literal|"throw"
argument_list|,
literal|"break"
argument_list|,
literal|"double"
argument_list|,
literal|"import"
argument_list|,
literal|"public"
argument_list|,
literal|"throws"
argument_list|,
literal|"byte"
argument_list|,
literal|"else"
argument_list|,
literal|"instanceof"
argument_list|,
literal|"return"
argument_list|,
literal|"transient"
argument_list|,
literal|"case"
argument_list|,
literal|"extends"
argument_list|,
literal|"int"
argument_list|,
literal|"short"
argument_list|,
literal|"try"
argument_list|,
literal|"catch"
argument_list|,
literal|"final"
argument_list|,
literal|"interface"
argument_list|,
literal|"static"
argument_list|,
literal|"void"
argument_list|,
literal|"char"
argument_list|,
literal|"finally"
argument_list|,
literal|"long"
argument_list|,
literal|"strictfp"
argument_list|,
literal|"volatile"
argument_list|,
literal|"class"
argument_list|,
literal|"float"
argument_list|,
literal|"native"
argument_list|,
literal|"super"
argument_list|,
literal|"while"
argument_list|,
literal|"const"
argument_list|,
literal|"for"
argument_list|,
literal|"new"
argument_list|,
literal|"switch"
argument_list|,
literal|"continue"
argument_list|,
literal|"goto"
argument_list|,
literal|"package"
argument_list|,
literal|"synchronized"
argument_list|)
decl_stmt|;
specifier|public
name|boolean
name|isReservedJavaKeyword
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|RESERVED_JAVA_KEYWORDS
operator|.
name|contains
argument_list|(
name|word
argument_list|)
return|;
block|}
comment|// a property is considered invalid if there is a getter or a setter for it in
comment|// java.lang.Object or CayenneDataObject
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|INVALID_JAVA_PROPERTIES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"class"
argument_list|,
literal|"committedSnapshot"
argument_list|,
literal|"currentSnapshot"
argument_list|,
literal|"dataContext"
argument_list|,
literal|"objectId"
argument_list|,
literal|"persistenceState"
argument_list|,
literal|"snapshotVersion"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|NameValidationHelper
name|sharedInstance
init|=
operator|new
name|NameValidationHelper
argument_list|()
decl_stmt|;
comment|/**      * Returns shared instance of the validator.      */
specifier|public
specifier|static
name|NameValidationHelper
name|getInstance
parameter_list|()
block|{
return|return
name|sharedInstance
return|;
block|}
comment|/**      * This is more of a sanity check than a real validation. As different DBs allow      * different chars in identifiers, here we simply check for dots.      */
specifier|public
name|String
name|invalidCharsInDbPathComponent
parameter_list|(
name|String
name|dbPathComponent
parameter_list|)
block|{
return|return
operator|(
name|dbPathComponent
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
operator|)
condition|?
literal|"."
else|:
literal|null
return|;
block|}
comment|/**      * Scans a name of ObjAttribute or ObjRelationship for invalid characters.      */
specifier|public
name|String
name|invalidCharsInObjPathComponent
parameter_list|(
name|String
name|objPathComponent
parameter_list|)
block|{
name|String
name|invalidChars
init|=
name|validateJavaIdentifier
argument_list|(
name|objPathComponent
argument_list|,
literal|""
argument_list|)
decl_stmt|;
return|return
operator|(
name|invalidChars
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
name|invalidChars
else|:
literal|null
return|;
block|}
specifier|public
name|String
name|invalidCharsInJavaClassName
parameter_list|(
name|String
name|javaClassName
parameter_list|)
block|{
if|if
condition|(
name|javaClassName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|invalidChars
init|=
literal|""
decl_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|javaClassName
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
while|while
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|invalidChars
operator|=
name|validateJavaIdentifier
argument_list|(
name|toks
operator|.
name|nextToken
argument_list|()
argument_list|,
name|invalidChars
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|invalidChars
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
name|invalidChars
else|:
literal|null
return|;
block|}
specifier|public
name|boolean
name|invalidDataObjectClass
parameter_list|(
name|String
name|dataObjectClassFQN
parameter_list|)
block|{
if|if
condition|(
name|dataObjectClassFQN
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|dataObjectClassFQN
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
while|while
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
if|if
condition|(
name|RESERVED_JAVA_KEYWORDS
operator|.
name|contains
argument_list|(
name|toks
operator|.
name|nextToken
argument_list|()
argument_list|)
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
specifier|private
name|String
name|validateJavaIdentifier
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|invalidChars
parameter_list|)
block|{
comment|// TODO: Java spec seems to allow "$" char in identifiers...
comment|// Cayenne expressions do not, so we should probably check for this char presence...
name|int
name|len
init|=
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|id
operator|.
name|length
argument_list|()
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
name|invalidChars
return|;
block|}
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|invalidChars
operator|.
name|indexOf
argument_list|(
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|<
literal|0
condition|)
block|{
name|invalidChars
operator|=
name|invalidChars
operator|+
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
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
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|id
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|invalidChars
operator|.
name|indexOf
argument_list|(
name|id
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
operator|<
literal|0
condition|)
block|{
name|invalidChars
operator|=
name|invalidChars
operator|+
name|id
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|invalidChars
return|;
block|}
comment|/**      * Returns whether a given String is a valid DataObject property. A property is      * considered invalid if there is a getter or a setter for it in java.lang.Object or      * CayenneDataObject.      */
specifier|public
name|boolean
name|invalidDataObjectProperty
parameter_list|(
name|String
name|dataObjectProperty
parameter_list|)
block|{
return|return
name|dataObjectProperty
operator|==
literal|null
operator|||
name|INVALID_JAVA_PROPERTIES
operator|.
name|contains
argument_list|(
name|dataObjectProperty
argument_list|)
return|;
block|}
block|}
end_class

end_unit

