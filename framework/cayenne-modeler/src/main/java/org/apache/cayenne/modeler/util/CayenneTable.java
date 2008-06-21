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
name|DefaultCellEditor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultListSelectionModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
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
name|event
operator|.
name|ChangeEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TableModelEvent
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
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|JTextComponent
import|;
end_import

begin_comment
comment|/**  * Common superclass of tables used in Cayenne. Contains some common configuration  * settings and utility methods.  *   * @author Michael Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CayenneTable
extends|extends
name|JTable
block|{
specifier|public
name|CayenneTable
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|setRowHeight
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|setSelectionModel
argument_list|(
operator|new
name|CayenneListSelectionModel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createDefaultEditors
parameter_list|()
block|{
name|super
operator|.
name|createDefaultEditors
argument_list|()
expr_stmt|;
name|JTextField
name|textField
init|=
name|CayenneWidgetFactory
operator|.
name|createTextField
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|DefaultCellEditor
name|textEditor
init|=
name|CayenneWidgetFactory
operator|.
name|createCellEditor
argument_list|(
name|textField
argument_list|)
decl_stmt|;
name|textEditor
operator|.
name|setClickCountToStart
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|setDefaultEditor
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|textEditor
argument_list|)
expr_stmt|;
name|setDefaultEditor
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|textEditor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneTableModel
name|getCayenneModel
parameter_list|()
block|{
return|return
operator|(
name|CayenneTableModel
operator|)
name|getModel
argument_list|()
return|;
block|}
comment|/**      * Cancels editing of any cells that maybe currently edited. This method should be      * called before updating any selections.      */
specifier|public
name|void
name|cancelEditing
parameter_list|()
block|{
name|editingCanceled
argument_list|(
operator|new
name|ChangeEvent
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|select
parameter_list|(
name|Object
name|row
parameter_list|)
block|{
if|if
condition|(
name|row
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|CayenneTableModel
name|model
init|=
name|getCayenneModel
argument_list|()
decl_stmt|;
name|int
name|ind
init|=
name|model
operator|.
name|getObjectList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|>=
literal|0
condition|)
block|{
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionInterval
argument_list|(
name|ind
argument_list|,
name|ind
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|select
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|CayenneTableModel
name|model
init|=
name|getCayenneModel
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|model
operator|.
name|getObjectList
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|index
operator|=
name|model
operator|.
name|getObjectList
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionInterval
argument_list|(
name|index
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Selects multiple rows at once. Fires not more than only one       * ListSelectionEvent      */
specifier|public
name|void
name|select
parameter_list|(
name|int
index|[]
name|rows
parameter_list|)
block|{
operator|(
operator|(
name|CayenneListSelectionModel
operator|)
name|getSelectionModel
argument_list|()
operator|)
operator|.
name|setSelection
argument_list|(
name|rows
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JTextComponent
name|getSelectedTextComponent
parameter_list|()
block|{
name|int
name|row
init|=
name|getSelectedRow
argument_list|()
decl_stmt|;
name|int
name|column
init|=
name|getSelectedColumn
argument_list|()
decl_stmt|;
if|if
condition|(
name|row
operator|<
literal|0
operator|||
name|column
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TableCellEditor
name|editor
init|=
name|this
operator|.
name|getCellEditor
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|instanceof
name|DefaultCellEditor
condition|)
block|{
name|Component
name|comp
init|=
operator|(
operator|(
name|DefaultCellEditor
operator|)
name|editor
operator|)
operator|.
name|getComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|JTextComponent
condition|)
block|{
return|return
operator|(
name|JTextComponent
operator|)
name|comp
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tableChanged
parameter_list|(
name|TableModelEvent
name|e
parameter_list|)
block|{
name|cancelEditing
argument_list|()
expr_stmt|;
name|super
operator|.
name|tableChanged
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * ListSelectionModel for Cayenne table. Has a method to set multiple      * rows selection at once.      */
class|class
name|CayenneListSelectionModel
extends|extends
name|DefaultListSelectionModel
block|{
name|boolean
name|fireForbidden
init|=
literal|false
decl_stmt|;
comment|/**          * Selects selection on multiple rows at once. Fires no more than one          * ListSelectionEvent          */
specifier|public
name|void
name|setSelection
parameter_list|(
name|int
index|[]
name|rows
parameter_list|)
block|{
comment|/**              * First check if we must do anything at all              */
name|boolean
name|selectionChanged
init|=
literal|false
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
name|rows
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|isRowSelected
argument_list|(
name|rows
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|selectionChanged
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|selectionChanged
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
name|getMinSelectionIndex
argument_list|()
init|;
name|i
operator|<
name|getMaxSelectionIndex
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|isSelectedIndex
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|boolean
name|inNewSelection
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|rows
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|rows
index|[
name|j
index|]
operator|==
name|i
condition|)
block|{
name|inNewSelection
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|inNewSelection
condition|)
block|{
name|selectionChanged
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|selectionChanged
condition|)
block|{
return|return;
block|}
name|fireForbidden
operator|=
literal|true
expr_stmt|;
name|clearSelection
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rows
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|addRowSelectionInterval
argument_list|(
name|rows
index|[
name|i
index|]
argument_list|,
name|rows
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|fireForbidden
operator|=
literal|false
expr_stmt|;
name|fireValueChanged
argument_list|(
name|getValueIsAdjusting
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fireValueChanged
parameter_list|(
name|int
name|firstIndex
parameter_list|,
name|int
name|lastIndex
parameter_list|,
name|boolean
name|isAdjusting
parameter_list|)
block|{
if|if
condition|(
operator|!
name|fireForbidden
condition|)
block|{
name|super
operator|.
name|fireValueChanged
argument_list|(
name|firstIndex
argument_list|,
name|lastIndex
argument_list|,
name|isAdjusting
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

