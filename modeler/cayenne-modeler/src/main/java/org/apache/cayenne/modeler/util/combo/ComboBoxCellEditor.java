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
name|util
operator|.
name|combo
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
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|AbstractCellEditor
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
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellEditor
import|;
end_import

begin_comment
comment|/**  * ComboBoxCellEditor class is a workaround of collision between DefaultCellEditor and   * AutoCompletion behavior. Using DefaultCellEditor will cause combo popup to close  * out of time.  *  */
end_comment

begin_class
specifier|public
class|class
name|ComboBoxCellEditor
extends|extends
name|AbstractCellEditor
implements|implements
name|ActionListener
implements|,
name|TableCellEditor
implements|,
name|Serializable
block|{
specifier|static
specifier|final
name|String
name|IS_TABLE_CELL_EDITOR_PROPERTY
init|=
literal|"JComboBox.isTableCellEditor"
decl_stmt|;
specifier|private
specifier|final
name|JComboBox
name|comboBox
decl_stmt|;
specifier|public
name|ComboBoxCellEditor
parameter_list|(
name|JComboBox
name|comboBox
parameter_list|)
block|{
name|this
operator|.
name|comboBox
operator|=
name|comboBox
expr_stmt|;
name|this
operator|.
name|comboBox
operator|.
name|putClientProperty
argument_list|(
name|IS_TABLE_CELL_EDITOR_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// hitting enter in the combo box should stop cellediting (see below)
name|this
operator|.
name|comboBox
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|//  Editing should be stopped when textfield loses its focus
comment|//  otherwise the value may get lost (e.g. see CAY-1104)
comment|//  LATER: this turned out to be the wrong fix, so I commented
comment|//  out the code in focusLost to fix CAY-1719 and fixed CAY-1104 differently.
comment|// this.comboBox.getEditor().getEditorComponent().addFocusListener(this);
comment|// remove the editor's border - the cell itself already has one
operator|(
operator|(
name|JComponent
operator|)
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|)
operator|.
name|setBorder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// Implementing ActionListener
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
comment|// Selecting an item results in an actioncommand "comboBoxChanged".
comment|// We should ignore these ones.
comment|// Hitting enter results in an actioncommand "comboBoxEdited"
if|if
condition|(
name|e
operator|.
name|getActionCommand
argument_list|()
operator|.
name|equals
argument_list|(
literal|"comboBoxEdited"
argument_list|)
condition|)
block|{
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|getCellEditorValue
parameter_list|()
block|{
return|return
name|comboBox
operator|.
name|getSelectedItem
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|stopCellEditing
parameter_list|()
block|{
if|if
condition|(
name|comboBox
operator|.
name|isEditable
argument_list|()
condition|)
block|{
comment|// Notify the combo box that editing has stopped (e.g. User pressed F2)
name|comboBox
operator|.
name|actionPerformed
argument_list|(
operator|new
name|ActionEvent
argument_list|(
name|this
argument_list|,
literal|0
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|fireEditingStopped
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|Component
name|getTableCellEditorComponent
parameter_list|(
name|javax
operator|.
name|swing
operator|.
name|JTable
name|table
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|comboBox
operator|.
name|setSelectedItem
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|EventObject
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|MouseEvent
condition|)
block|{
comment|//allow multiple selection without
name|MouseEvent
name|me
init|=
operator|(
name|MouseEvent
operator|)
name|e
decl_stmt|;
if|if
condition|(
name|me
operator|.
name|isControlDown
argument_list|()
operator|||
name|me
operator|.
name|isShiftDown
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

