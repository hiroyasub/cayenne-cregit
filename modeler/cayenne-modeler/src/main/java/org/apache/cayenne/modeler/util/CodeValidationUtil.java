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
name|modeler
operator|.
name|util
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
name|validation
operator|.
name|BeanValidationFailure
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
name|validation
operator|.
name|ValidationFailure
import|;
end_import

begin_comment
comment|// TODO, andrus 4/13/2006 - merge with BeanValidationFailure.
end_comment

begin_class
specifier|public
class|class
name|CodeValidationUtil
block|{
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
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
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
specifier|public
specifier|static
name|ValidationFailure
name|validateJavaIdentifier
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
name|BeanValidationFailure
operator|.
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
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

