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
name|swing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * A factory for a number of common bindings.  *   */
end_comment

begin_class
specifier|public
class|class
name|BindingFactory
block|{
specifier|protected
name|boolean
name|usingNullForEmptyStrings
decl_stmt|;
specifier|protected
name|boolean
name|checkingForValueChange
decl_stmt|;
specifier|public
name|BindingFactory
parameter_list|()
block|{
comment|// init defaults...
name|usingNullForEmptyStrings
operator|=
literal|true
expr_stmt|;
name|checkingForValueChange
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|ObjectBinding
name|bindToTable
parameter_list|(
name|JTable
name|table
parameter_list|,
name|String
name|listBinding
parameter_list|,
name|String
index|[]
name|headers
parameter_list|,
name|BindingExpression
index|[]
name|columns
parameter_list|,
name|Class
index|[]
name|columnClass
parameter_list|,
name|boolean
index|[]
name|editableState
parameter_list|,
name|Object
index|[]
name|sampleLongValues
parameter_list|)
block|{
name|TableBinding
name|binding
init|=
operator|new
name|TableBinding
argument_list|(
name|table
argument_list|,
name|listBinding
argument_list|,
name|headers
argument_list|,
name|columns
argument_list|,
name|columnClass
argument_list|,
name|editableState
argument_list|,
name|sampleLongValues
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToProperty
parameter_list|(
name|BoundComponent
name|component
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|boundProperty
parameter_list|)
block|{
name|PropertyBinding
name|binding
init|=
operator|new
name|PropertyBinding
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|boundProperty
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
comment|/**      * Binds to AbstractButton item state change events. Most common AbstractButton      * subclasses are JButton, JCheckBox, JRadioButton.      */
specifier|public
name|ObjectBinding
name|bindToStateChange
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ItemEventBinding
name|binding
init|=
operator|new
name|ItemEventBinding
argument_list|(
name|button
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
comment|/**      * Binds to AbstractButton action events. Most common AbstractButton subclasses are      * JButton, JCheckBox, JRadioButton.      */
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|ActionBinding
name|binding
init|=
operator|new
name|ActionBinding
argument_list|(
name|button
argument_list|,
name|action
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
comment|/**      * Binds to a generic component. Action events support is discovered via      * introspection. If component class does not define action events, an exception is      * thrown.      */
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|BeanActionBinding
name|binding
init|=
operator|new
name|BeanActionBinding
argument_list|(
name|component
argument_list|,
name|action
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|BoundComponent
name|component
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|boundExpression
parameter_list|)
block|{
name|ActionBinding
name|binding
init|=
operator|new
name|ActionBinding
argument_list|(
name|component
argument_list|,
name|action
argument_list|,
name|boundExpression
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToComboSelection
parameter_list|(
name|JComboBox
name|component
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|noSelectionValue
parameter_list|)
block|{
name|ComboSelectionBinding
name|binding
init|=
operator|new
name|ComboSelectionBinding
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|noSelectionValue
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToTextArea
parameter_list|(
name|JTextArea
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|TextBinding
name|binding
init|=
operator|new
name|TextBinding
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
comment|/**      * Creates a binding that updates a property on text field text changes.      */
specifier|public
name|ObjectBinding
name|bindToTextField
parameter_list|(
name|JTextField
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|TextBinding
name|binding
init|=
operator|new
name|TextBinding
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToCheckBox
parameter_list|(
name|JCheckBox
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|CheckBoxBinding
name|binding
init|=
operator|new
name|CheckBoxBinding
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|prepareBinding
argument_list|(
name|binding
argument_list|)
return|;
block|}
comment|/**      * Configures binding with factory default settings.      */
specifier|protected
name|ObjectBinding
name|prepareBinding
parameter_list|(
name|BindingBase
name|binding
parameter_list|)
block|{
name|binding
operator|.
name|setUsingNullForEmptyStrings
argument_list|(
name|isUsingNullForEmptyStrings
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setCheckingForValueChange
argument_list|(
name|isCheckingForValueChange
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|public
name|boolean
name|isCheckingForValueChange
parameter_list|()
block|{
return|return
name|checkingForValueChange
return|;
block|}
specifier|public
name|void
name|setCheckingForValueChange
parameter_list|(
name|boolean
name|callingSetForEqual
parameter_list|)
block|{
name|this
operator|.
name|checkingForValueChange
operator|=
name|callingSetForEqual
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsingNullForEmptyStrings
parameter_list|()
block|{
return|return
name|usingNullForEmptyStrings
return|;
block|}
specifier|public
name|void
name|setUsingNullForEmptyStrings
parameter_list|(
name|boolean
name|usingNullForEmptyStrings
parameter_list|)
block|{
name|this
operator|.
name|usingNullForEmptyStrings
operator|=
name|usingNullForEmptyStrings
expr_stmt|;
block|}
block|}
end_class

end_unit

