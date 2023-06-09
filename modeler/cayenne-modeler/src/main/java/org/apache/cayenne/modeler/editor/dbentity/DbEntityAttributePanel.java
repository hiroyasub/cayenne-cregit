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
name|editor
operator|.
name|dbentity
package|;
end_package

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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPopupMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ListSelectionModel
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
name|ListSelectionEvent
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
name|ListSelectionListener
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
name|table
operator|.
name|TableColumn
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|dba
operator|.
name|TypesMapping
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
name|DbAttribute
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
name|DbEntity
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
name|event
operator|.
name|AttributeEvent
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
name|event
operator|.
name|DbAttributeListener
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
name|Application
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
name|action
operator|.
name|ActionManager
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
name|action
operator|.
name|CopyAttributeRelationshipAction
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
name|action
operator|.
name|CutAttributeRelationshipAction
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
name|action
operator|.
name|PasteAction
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
name|action
operator|.
name|RemoveAttributeRelationshipAction
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
name|event
operator|.
name|DbEntityDisplayListener
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
name|event
operator|.
name|EntityDisplayEvent
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
name|event
operator|.
name|TablePopupHandler
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
name|TableColumnPreferences
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
name|BoardTableCellRenderer
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
name|CayenneTable
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
name|PanelFactory
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
name|UIUtil
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
name|swing
operator|.
name|components
operator|.
name|LimitedTextField
import|;
end_import

begin_comment
comment|/**  * Detail view of the DbEntity attributes.  */
end_comment

begin_class
specifier|public
class|class
name|DbEntityAttributePanel
extends|extends
name|JPanel
implements|implements
name|DbEntityDisplayListener
implements|,
name|DbAttributeListener
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|CayenneTable
name|table
decl_stmt|;
specifier|private
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
specifier|private
name|DbEntityAttributeRelationshipTab
name|parentPanel
decl_stmt|;
specifier|public
name|DbEntityAttributePanel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|DbEntityAttributeRelationshipTab
name|parentPanel
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|this
operator|.
name|parentPanel
operator|=
name|parentPanel
expr_stmt|;
comment|// Create and layout components
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|ActionManager
name|actionManager
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
decl_stmt|;
comment|// Create table with two columns and no rows.
name|table
operator|=
operator|new
name|CayenneTable
argument_list|()
expr_stmt|;
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|DbAttributeTableModel
operator|.
name|class
argument_list|,
literal|"attributeTable"
argument_list|)
expr_stmt|;
name|table
operator|.
name|setDefaultRenderer
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|BoardTableCellRenderer
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create and install a popup
name|JPopupMenu
name|popup
init|=
operator|new
name|JPopupMenu
argument_list|()
decl_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|CutAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|CopyAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|PasteAction
operator|.
name|class
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|TablePopupHandler
operator|.
name|install
argument_list|(
name|table
argument_list|,
name|popup
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|PanelFactory
operator|.
name|createTablePanel
argument_list|(
name|table
argument_list|,
literal|null
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addDbEntityDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addDbAttributeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|addListSelectionListener
argument_list|(
operator|new
name|DbAttributeListSelectionListener
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|setupCutCopyPaste
argument_list|(
name|table
argument_list|,
name|CutAttributeRelationshipAction
operator|.
name|class
argument_list|,
name|CopyAttributeRelationshipAction
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Selects specified attributes.      */
specifier|public
name|void
name|selectAttributes
parameter_list|(
name|DbAttribute
index|[]
name|attrs
parameter_list|)
block|{
name|DbAttributeTableModel
name|model
init|=
operator|(
name|DbAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|listAttrs
init|=
name|model
operator|.
name|getObjectList
argument_list|()
decl_stmt|;
name|int
index|[]
name|newSel
init|=
operator|new
name|int
index|[
name|attrs
operator|.
name|length
index|]
decl_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CutAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CopyAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|updateActions
argument_list|(
name|attrs
argument_list|)
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
name|attrs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|newSel
index|[
name|i
index|]
operator|=
name|listAttrs
operator|.
name|indexOf
argument_list|(
name|attrs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|select
argument_list|(
name|newSel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbAttributeChanged
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbAttributeAdded
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|rebuildTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbAttributeRemoved
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|DbAttributeTableModel
name|model
init|=
operator|(
name|DbAttributeTableModel
operator|)
name|table
operator|.
name|getModel
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
name|e
operator|.
name|getAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|removeRow
argument_list|(
operator|(
name|DbAttribute
operator|)
name|e
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|ind
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDbEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
name|e
operator|.
name|isEntityChanged
argument_list|()
condition|)
block|{
name|rebuildTable
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|// if an entity was selected on a tree, unselect currently selected row
if|if
condition|(
name|e
operator|.
name|isUnselectAttributes
argument_list|()
condition|)
block|{
name|table
operator|.
name|clearSelection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|rebuildTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
if|if
condition|(
name|table
operator|.
name|getEditingRow
argument_list|()
operator|!=
operator|-
literal|1
operator|&&
name|table
operator|.
name|getEditingColumn
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|TableCellEditor
name|cellEditor
init|=
name|table
operator|.
name|getCellEditor
argument_list|(
name|table
operator|.
name|getEditingRow
argument_list|()
argument_list|,
name|table
operator|.
name|getEditingColumn
argument_list|()
argument_list|)
decl_stmt|;
name|cellEditor
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
name|DbAttributeTableModel
name|model
init|=
operator|new
name|DbAttributeTableModel
argument_list|(
name|ent
argument_list|,
name|mediator
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|table
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowHeight
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|TableColumn
name|col
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|model
operator|.
name|typeColumnInd
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|types
init|=
name|TypesMapping
operator|.
name|getDatabaseTypes
argument_list|()
decl_stmt|;
name|JComboBox
name|comboBox
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|types
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Types.NULL makes no sense as a column type
name|comboBox
operator|.
name|removeItem
argument_list|(
literal|"NULL"
argument_list|)
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|comboBox
argument_list|)
expr_stmt|;
name|col
operator|.
name|setCellEditor
argument_list|(
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createCellEditor
argument_list|(
name|comboBox
argument_list|)
argument_list|)
expr_stmt|;
name|TableColumn
name|lengthColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|model
operator|.
name|lengthColumnId
argument_list|()
argument_list|)
decl_stmt|;
name|LimitedTextField
name|limitedLengthField
init|=
operator|new
name|LimitedTextField
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|lengthColumn
operator|.
name|setCellEditor
argument_list|(
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createCellEditor
argument_list|(
name|limitedLengthField
argument_list|)
argument_list|)
expr_stmt|;
name|TableColumn
name|scaleColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|model
operator|.
name|scaleColumnId
argument_list|()
argument_list|)
decl_stmt|;
name|LimitedTextField
name|limitedScaleField
init|=
operator|new
name|LimitedTextField
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|scaleColumn
operator|.
name|setCellEditor
argument_list|(
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createCellEditor
argument_list|(
name|limitedScaleField
argument_list|)
argument_list|)
expr_stmt|;
name|tablePreferences
operator|.
name|bind
argument_list|(
name|table
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|model
operator|.
name|nameColumnInd
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|DbAttributeListSelectionListener
implements|implements
name|ListSelectionListener
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
name|DbAttribute
index|[]
name|attrs
init|=
operator|new
name|DbAttribute
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|e
operator|.
name|getValueIsAdjusting
argument_list|()
operator|&&
operator|!
operator|(
operator|(
name|ListSelectionModel
operator|)
name|e
operator|.
name|getSource
argument_list|()
operator|)
operator|.
name|isSelectionEmpty
argument_list|()
condition|)
block|{
name|parentPanel
operator|.
name|getRelationshipPanel
argument_list|()
operator|.
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|clearSelection
argument_list|()
expr_stmt|;
if|if
condition|(
name|parentPanel
operator|.
name|getRelationshipPanel
argument_list|()
operator|.
name|table
operator|.
name|getCellEditor
argument_list|()
operator|!=
literal|null
condition|)
name|parentPanel
operator|.
name|getRelationshipPanel
argument_list|()
operator|.
name|table
operator|.
name|getCellEditor
argument_list|()
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CutAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CopyAttributeRelationshipAction
operator|.
name|class
argument_list|)
operator|.
name|setCurrentSelectedPanel
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|table
operator|.
name|getSelectedRow
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|DbAttributeTableModel
name|model
init|=
operator|(
name|DbAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|int
index|[]
name|sel
init|=
name|table
operator|.
name|getSelectedRows
argument_list|()
decl_stmt|;
name|attrs
operator|=
operator|new
name|DbAttribute
index|[
name|sel
operator|.
name|length
index|]
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
name|sel
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|attrs
index|[
name|i
index|]
operator|=
name|model
operator|.
name|getAttribute
argument_list|(
name|sel
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sel
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|UIUtil
operator|.
name|scrollToSelectedRow
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|mediator
operator|.
name|setCurrentDbAttributes
argument_list|(
name|attrs
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|updateActions
argument_list|(
name|attrs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

