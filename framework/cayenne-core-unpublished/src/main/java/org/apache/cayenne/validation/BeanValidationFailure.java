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
name|validation
package|;
end_package

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
name|reflect
operator|.
name|PropertyUtils
import|;
end_import

begin_comment
comment|/**  * ValidationFailure implementation that described a failure of a single named property of  * a Java Bean object.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|BeanValidationFailure
extends|extends
name|SimpleValidationFailure
block|{
specifier|protected
name|String
name|property
decl_stmt|;
specifier|private
specifier|static
name|String
name|validationMessage
parameter_list|(
name|String
name|attribute
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
operator|.
name|length
argument_list|()
operator|+
name|attribute
operator|.
name|length
argument_list|()
operator|+
literal|5
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
operator|.
name|append
argument_list|(
name|attribute
argument_list|)
operator|.
name|append
argument_list|(
literal|"\" "
argument_list|)
operator|.
name|append
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a ValidationFailure if a collection attribute of an object is null or      * empty.      */
specifier|public
specifier|static
name|ValidationFailure
name|validateNotEmpty
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|,
name|Collection
argument_list|<
name|?
argument_list|>
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
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" is required."
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" can not be empty."
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|ValidationFailure
name|validateMandatory
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|validateNotEmpty
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
return|return
name|validateNotEmpty
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
return|;
block|}
return|return
name|validateNotNull
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ValidationFailure
name|validateMandatory
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null bean."
argument_list|)
throw|;
block|}
try|try
block|{
name|Object
name|result
init|=
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|)
decl_stmt|;
return|return
name|validateMandatory
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error validationg bean property: "
operator|+
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|attribute
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|ValidationFailure
name|validateNotNull
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|,
name|Object
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
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" is required."
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A utility method that returns a ValidationFailure if a string is either null or has      * a length of zero; otherwise returns null.      */
specifier|public
specifier|static
name|ValidationFailure
name|validateNotEmpty
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" is a required field."
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A utility method that checks that a given string is a valid Java full class name,      * returning a non-null ValidationFailure if this is not so.       *       * Special case: primitive arrays like byte[] are also handled as a valid java       * class name.      *       * @since 1.2      */
specifier|public
specifier|static
name|ValidationFailure
name|validateJavaClassName
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|attribute
parameter_list|,
name|String
name|identifier
parameter_list|)
block|{
name|ValidationFailure
name|emptyFailure
init|=
name|validateNotEmpty
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|identifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyFailure
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyFailure
return|;
block|}
name|char
name|c
init|=
name|identifier
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" starts with invalid character: "
operator|+
name|c
argument_list|)
argument_list|)
return|;
block|}
comment|// handle arrays
if|if
condition|(
name|identifier
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|)
block|{
name|identifier
operator|=
name|identifier
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|identifier
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
name|boolean
name|wasDot
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|identifier
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|c
operator|=
name|identifier
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'.'
condition|)
block|{
if|if
condition|(
name|wasDot
operator|||
name|i
operator|+
literal|1
operator|==
name|identifier
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" is not a valid Java Class Name: "
operator|+
name|identifier
argument_list|)
argument_list|)
return|;
block|}
name|wasDot
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|bean
argument_list|,
name|attribute
argument_list|,
name|validationMessage
argument_list|(
name|attribute
argument_list|,
literal|" contains invalid character: "
operator|+
name|c
argument_list|)
argument_list|)
return|;
block|}
name|wasDot
operator|=
literal|false
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Creates new BeanValidationFailure.      */
specifier|public
name|BeanValidationFailure
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|error
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|error
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
operator|&&
name|property
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ValidationFailure cannot have 'property' when 'source' is null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|property
operator|=
name|property
expr_stmt|;
block|}
comment|/**      * Returns a failed property of the failure source object.      */
specifier|public
name|String
name|getProperty
parameter_list|()
block|{
return|return
name|property
return|;
block|}
comment|/**      * Returns a String representation of the failure.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"Validation failure for "
argument_list|)
expr_stmt|;
name|Object
name|source
init|=
name|getSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"[General]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|property
init|=
name|getProperty
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|source
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
operator|(
name|property
operator|==
literal|null
condition|?
literal|"[General]"
else|:
name|property
operator|)
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
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
