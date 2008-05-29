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
operator|new
name|DefaultCellEditor
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
block|}
end_class

end_unit

