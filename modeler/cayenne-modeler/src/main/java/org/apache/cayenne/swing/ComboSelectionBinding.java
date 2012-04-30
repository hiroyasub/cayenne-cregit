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
name|swing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ItemListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
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
name|dialog
operator|.
name|validator
operator|.
name|ValidatorDialog
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
name|undo
operator|.
name|JComboBoxUndoListener
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
name|ValidationException
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ComboSelectionBinding
extends|extends
name|BindingBase
block|{
specifier|protected
name|JComboBox
name|comboBox
decl_stmt|;
specifier|protected
name|Color
name|defaultBGColor
decl_stmt|;
specifier|protected
name|Color
name|errorColor
decl_stmt|;
specifier|protected
name|String
name|defaultToolTip
decl_stmt|;
specifier|protected
name|String
name|noSelectionValue
decl_stmt|;
comment|/**      * Binds to update model for a combo box selection events. For editable combo boxes      * model is updated whenever a new value is entered.      */
specifier|public
name|ComboSelectionBinding
parameter_list|(
name|JComboBox
name|comboBox
parameter_list|,
name|String
name|expression
parameter_list|,
name|String
name|noSelectionValue
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|this
operator|.
name|comboBox
operator|=
name|comboBox
expr_stmt|;
name|this
operator|.
name|noSelectionValue
operator|=
name|noSelectionValue
expr_stmt|;
comment|// insert no selection value as first item in the combobox if it is not there
if|if
condition|(
name|noSelectionValue
operator|!=
literal|null
operator|&&
operator|(
name|comboBox
operator|.
name|getItemCount
argument_list|()
operator|==
literal|0
operator|||
name|comboBox
operator|.
name|getItemAt
argument_list|(
literal|0
argument_list|)
operator|!=
name|noSelectionValue
operator|)
condition|)
block|{
name|comboBox
operator|.
name|insertItemAt
argument_list|(
name|noSelectionValue
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|comboBox
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|modelUpdateDisabled
condition|)
block|{
name|updateModel
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// init error colors
name|initComponentDefaults
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initComponentDefaults
parameter_list|()
block|{
name|this
operator|.
name|errorColor
operator|=
name|ValidatorDialog
operator|.
name|WARNING_COLOR
expr_stmt|;
if|if
condition|(
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Component
name|editor
init|=
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|editor
operator|instanceof
name|JComponent
condition|)
block|{
name|JComponent
name|jEditor
init|=
operator|(
name|JComponent
operator|)
name|editor
decl_stmt|;
name|this
operator|.
name|defaultBGColor
operator|=
name|jEditor
operator|.
name|getBackground
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultToolTip
operator|=
name|jEditor
operator|.
name|getToolTipText
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|updateView
parameter_list|()
block|{
name|Object
name|value
init|=
name|getValue
argument_list|()
decl_stmt|;
name|modelUpdateDisabled
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|clear
argument_list|()
expr_stmt|;
name|ItemListener
index|[]
name|listeners
init|=
name|comboBox
operator|.
name|getItemListeners
argument_list|()
decl_stmt|;
for|for
control|(
name|ItemListener
name|itemListener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|itemListener
operator|instanceof
name|JComboBoxUndoListener
condition|)
block|{
comment|//in order not to add event to undo list
operator|(
operator|(
name|JComboBoxUndoListener
operator|)
name|itemListener
operator|)
operator|.
name|setIsUserAction
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|comboBox
operator|.
name|setSelectedItem
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|noSelectionValue
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|comboBox
operator|.
name|setSelectedItem
argument_list|(
name|noSelectionValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|comboBox
operator|.
name|setSelectedIndex
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|modelUpdateDisabled
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|updateModel
parameter_list|()
block|{
try|try
block|{
name|Object
name|value
init|=
name|comboBox
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
if|if
condition|(
name|noSelectionValue
operator|!=
literal|null
operator|&&
name|noSelectionValue
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|vex
parameter_list|)
block|{
name|initWarning
argument_list|(
name|vex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|comboBox
return|;
block|}
specifier|protected
name|void
name|clear
parameter_list|()
block|{
if|if
condition|(
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Component
name|editor
init|=
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|editor
operator|instanceof
name|JComponent
condition|)
block|{
name|JComponent
name|jEditor
init|=
operator|(
name|JComponent
operator|)
name|editor
decl_stmt|;
name|jEditor
operator|.
name|setBackground
argument_list|(
name|defaultBGColor
argument_list|)
expr_stmt|;
name|jEditor
operator|.
name|setToolTipText
argument_list|(
name|defaultToolTip
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|initWarning
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Component
name|editor
init|=
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|editor
operator|instanceof
name|JComponent
condition|)
block|{
name|JComponent
name|jEditor
init|=
operator|(
name|JComponent
operator|)
name|editor
decl_stmt|;
name|jEditor
operator|.
name|setBackground
argument_list|(
name|errorColor
argument_list|)
expr_stmt|;
name|jEditor
operator|.
name|setToolTipText
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

