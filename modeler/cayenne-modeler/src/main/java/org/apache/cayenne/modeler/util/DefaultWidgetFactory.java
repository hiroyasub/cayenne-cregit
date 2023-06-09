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
name|modeler
operator|.
name|ModelerPreferences
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
name|modeler
operator|.
name|util
operator|.
name|combo
operator|.
name|AutoCompletion
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
name|util
operator|.
name|combo
operator|.
name|ComboBoxCellEditor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultCellEditor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
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
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|UIManager
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

begin_class
specifier|public
class|class
name|DefaultWidgetFactory
implements|implements
name|WidgetFactory
block|{
comment|/**      * Creates a new JComboBox with a collection of model objects.      */
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|createComboBox
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
block|{
return|return
name|createComboBox
argument_list|(
name|model
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|sort
argument_list|)
return|;
block|}
comment|/**      * Creates a new JComboBox with an array of model objects.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|JComboBox
argument_list|<
name|T
argument_list|>
name|createComboBox
parameter_list|(
name|T
index|[]
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
block|{
name|JComboBox
argument_list|<
name|T
argument_list|>
name|comboBox
init|=
name|createComboBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|sort
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
name|comboBox
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
comment|/**      * Creates a new JComboBox.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|JComboBox
argument_list|<
name|T
argument_list|>
name|createComboBox
parameter_list|()
block|{
name|JComboBox
argument_list|<
name|T
argument_list|>
name|comboBox
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|comboBox
operator|.
name|setFont
argument_list|(
name|UIManager
operator|.
name|getFont
argument_list|(
literal|"Label.font"
argument_list|)
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setMaximumRowCount
argument_list|(
name|ModelerPreferences
operator|.
name|COMBOBOX_MAX_VISIBLE_SIZE
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
comment|/**      * Creates undoable JComboBox.      *       */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|JComboBox
argument_list|<
name|T
argument_list|>
name|createUndoableComboBox
parameter_list|()
block|{
name|JComboBox
argument_list|<
name|T
argument_list|>
name|comboBox
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|comboBox
operator|.
name|addItemListener
argument_list|(
operator|new
name|JComboBoxUndoListener
argument_list|()
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setMaximumRowCount
argument_list|(
name|ModelerPreferences
operator|.
name|COMBOBOX_MAX_VISIBLE_SIZE
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
comment|/**      * Creates cell editor for text field      */
specifier|public
name|DefaultCellEditor
name|createCellEditor
parameter_list|(
name|JTextField
name|textField
parameter_list|)
block|{
name|textField
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|CayenneCellEditor
argument_list|(
name|textField
argument_list|)
return|;
block|}
comment|/**      * Creates cell editor for a table with combo as editor component. Type of this editor      * depends on auto-completion behavior of JComboBox      *       * @param combo JComboBox to be used as editor component      */
specifier|public
name|TableCellEditor
name|createCellEditor
parameter_list|(
name|JComboBox
argument_list|<
name|?
argument_list|>
name|combo
parameter_list|)
block|{
name|combo
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|combo
operator|.
name|getClientProperty
argument_list|(
name|AutoCompletion
operator|.
name|AUTOCOMPLETION_PROPERTY
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|ComboBoxCellEditor
argument_list|(
name|combo
argument_list|)
return|;
block|}
name|DefaultCellEditor
name|editor
init|=
operator|new
name|DefaultCellEditor
argument_list|(
name|combo
argument_list|)
decl_stmt|;
name|editor
operator|.
name|setClickCountToStart
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|editor
return|;
block|}
block|}
end_class

end_unit

