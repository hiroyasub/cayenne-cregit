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
name|MouseEvent
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
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|JComboBox
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
name|event
operator|.
name|CellEditorListener
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

begin_class
specifier|public
class|class
name|CellEditorForAttributeTable
implements|implements
name|TableCellEditor
block|{
specifier|protected
name|Hashtable
name|editors
decl_stmt|;
specifier|protected
name|TableCellEditor
name|editor
decl_stmt|,
name|defaultEditor
decl_stmt|;
name|JTable
name|table
decl_stmt|;
specifier|public
name|CellEditorForAttributeTable
parameter_list|(
name|JTable
name|table
parameter_list|,
name|JComboBox
name|combo
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|editors
operator|=
operator|new
name|Hashtable
argument_list|()
expr_stmt|;
if|if
condition|(
name|combo
operator|!=
literal|null
condition|)
block|{
name|defaultEditor
operator|=
operator|new
name|DefaultCellEditor
argument_list|(
name|combo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|defaultEditor
operator|=
operator|new
name|DefaultCellEditor
argument_list|(
operator|new
name|JComboBox
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setEditorAt
parameter_list|(
name|int
name|row
parameter_list|,
name|TableCellEditor
name|editor
parameter_list|)
block|{
name|editors
operator|.
name|put
argument_list|(
name|row
argument_list|,
name|editor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getTableCellEditorComponent
parameter_list|(
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
name|column
operator|=
name|table
operator|.
name|convertColumnIndexToView
argument_list|(
name|column
argument_list|)
expr_stmt|;
return|return
name|editor
operator|.
name|getTableCellEditorComponent
argument_list|(
name|table
argument_list|,
name|value
argument_list|,
name|isSelected
argument_list|,
name|row
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getCellEditorValue
parameter_list|()
block|{
return|return
name|editor
operator|.
name|getCellEditorValue
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|stopCellEditing
parameter_list|()
block|{
return|return
name|editor
operator|.
name|stopCellEditing
argument_list|()
return|;
block|}
specifier|public
name|void
name|cancelCellEditing
parameter_list|()
block|{
name|editor
operator|.
name|cancelCellEditing
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|EventObject
name|anEvent
parameter_list|)
block|{
name|selectEditor
argument_list|(
operator|(
name|MouseEvent
operator|)
name|anEvent
argument_list|)
expr_stmt|;
return|return
name|editor
operator|.
name|isCellEditable
argument_list|(
name|anEvent
argument_list|)
return|;
block|}
specifier|public
name|void
name|addCellEditorListener
parameter_list|(
name|CellEditorListener
name|l
parameter_list|)
block|{
name|editor
operator|.
name|addCellEditorListener
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeCellEditorListener
parameter_list|(
name|CellEditorListener
name|l
parameter_list|)
block|{
name|editor
operator|.
name|removeCellEditorListener
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|shouldSelectCell
parameter_list|(
name|EventObject
name|anEvent
parameter_list|)
block|{
name|selectEditor
argument_list|(
operator|(
name|MouseEvent
operator|)
name|anEvent
argument_list|)
expr_stmt|;
return|return
name|editor
operator|.
name|shouldSelectCell
argument_list|(
name|anEvent
argument_list|)
return|;
block|}
specifier|protected
name|void
name|selectEditor
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|int
name|row
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|row
operator|=
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|getAnchorSelectionIndex
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|row
operator|=
name|table
operator|.
name|rowAtPoint
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|editor
operator|=
operator|(
name|TableCellEditor
operator|)
name|editors
operator|.
name|get
argument_list|(
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|editor
operator|==
literal|null
condition|)
block|{
name|editor
operator|=
name|defaultEditor
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

