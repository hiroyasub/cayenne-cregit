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
name|modeler
operator|.
name|dialog
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFrame
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|map
operator|.
name|Attribute
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|Embeddable
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
name|map
operator|.
name|EmbeddableAttribute
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
name|map
operator|.
name|Entity
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
name|map
operator|.
name|Procedure
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
name|map
operator|.
name|ProcedureParameter
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
name|map
operator|.
name|Relationship
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
name|modeler
operator|.
name|ProjectController
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
name|modeler
operator|.
name|pref
operator|.
name|DataNodeDefaults
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
name|map
operator|.
name|QueryDescriptor
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
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Superclass of CayenneModeler validation messages.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ValidationDisplayHandler
block|{
specifier|private
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ValidationDisplayHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NO_ERROR
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|WARNING
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ERROR
init|=
literal|2
decl_stmt|;
specifier|protected
name|ValidationFailure
name|validationFailure
decl_stmt|;
specifier|protected
name|DataChannelDescriptor
name|domain
decl_stmt|;
specifier|public
specifier|static
name|ValidationDisplayHandler
name|getErrorMsg
parameter_list|(
name|ValidationFailure
name|result
parameter_list|)
block|{
name|Object
name|validatedObj
init|=
name|result
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|ValidationDisplayHandler
name|msg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|validatedObj
operator|instanceof
name|Embeddable
condition|)
block|{
name|msg
operator|=
operator|new
name|EmbeddableErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|Attribute
condition|)
block|{
name|msg
operator|=
operator|new
name|AttributeErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|EmbeddableAttribute
condition|)
block|{
name|msg
operator|=
operator|new
name|EmbeddableAttributeErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|Relationship
condition|)
block|{
name|msg
operator|=
operator|new
name|RelationshipErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|Entity
condition|)
block|{
name|msg
operator|=
operator|new
name|EntityErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataNodeDefaults
condition|)
block|{
name|msg
operator|=
operator|new
name|DataNodeErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataMap
condition|)
block|{
name|msg
operator|=
operator|new
name|DataMapErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataChannelDescriptor
condition|)
block|{
name|msg
operator|=
operator|new
name|DomainErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|Procedure
condition|)
block|{
name|msg
operator|=
operator|new
name|ProcedureErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|ProcedureParameter
condition|)
block|{
name|msg
operator|=
operator|new
name|ProcedureParameterErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|QueryDescriptor
condition|)
block|{
name|msg
operator|=
operator|new
name|QueryErrorMsg
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// do nothing ... this maybe a project node that is not displayed
name|logObj
operator|.
name|info
argument_list|(
literal|"unknown project node: "
operator|+
name|validatedObj
argument_list|)
expr_stmt|;
name|msg
operator|=
operator|new
name|NullHanlder
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
return|;
block|}
specifier|public
name|ValidationDisplayHandler
parameter_list|(
name|ValidationFailure
name|validationFailure
parameter_list|)
block|{
name|this
operator|.
name|validationFailure
operator|=
name|validationFailure
expr_stmt|;
block|}
comment|/**      * Fires event to display the screen where error should be corrected.      */
specifier|public
specifier|abstract
name|void
name|displayField
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|JFrame
name|frame
parameter_list|)
function_decl|;
comment|/** Returns the text of the error message. */
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|validationFailure
operator|.
name|getDescription
argument_list|()
return|;
block|}
specifier|public
name|DataChannelDescriptor
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
specifier|public
name|void
name|setDomain
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getMessage
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|validationFailure
operator|.
name|getSource
argument_list|()
return|;
block|}
specifier|public
name|ValidationFailure
name|getValidationFailure
parameter_list|()
block|{
return|return
name|validationFailure
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|NullHanlder
extends|extends
name|ValidationDisplayHandler
block|{
name|NullHanlder
parameter_list|(
name|ValidationFailure
name|info
parameter_list|)
block|{
name|super
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|displayField
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|JFrame
name|frame
parameter_list|)
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

