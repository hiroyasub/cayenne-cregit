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
name|editor
operator|.
name|dbentity
package|;
end_package

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
name|EventObject
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
name|JToolBar
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
name|TableColumn
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
name|CreateAttributeAction
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
name|CreateObjEntityAction
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
name|DbEntitySyncAction
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
name|RemoveAttributeAction
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
name|editor
operator|.
name|ExistingSelectionProcessor
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
name|AttributeDisplayEvent
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
name|CayenneWidgetFactory
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

begin_comment
comment|/**  * Detail view of the DbEntity attributes.  *   * @author Michael Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DbEntityAttributeTab
extends|extends
name|JPanel
implements|implements
name|DbEntityDisplayListener
implements|,
name|ListSelectionListener
implements|,
name|DbAttributeListener
implements|,
name|ExistingSelectionProcessor
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|DbEntityAttributeTab
parameter_list|(
name|ProjectController
name|temp_mediator
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|mediator
operator|=
name|temp_mediator
expr_stmt|;
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
comment|// Create and layout components
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
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
name|JToolBar
name|toolBar
init|=
operator|new
name|JToolBar
argument_list|()
decl_stmt|;
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|app
operator|.
name|getAction
argument_list|(
name|CreateObjEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|app
operator|.
name|getAction
argument_list|(
name|CreateAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|app
operator|.
name|getAction
argument_list|(
name|DbEntitySyncAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|app
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|toolBar
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
comment|// Create table with two columns and no rows.
name|table
operator|=
operator|new
name|CayenneTable
argument_list|()
expr_stmt|;
comment|/**          * Create and install a popup          */
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
name|app
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
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
specifier|public
name|void
name|valueChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
name|processExistingSelection
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * Selects a specified attribute.      */
specifier|public
name|void
name|selectAttribute
parameter_list|(
name|DbAttribute
name|attr
parameter_list|)
block|{
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// enable the remove button
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|java
operator|.
name|util
operator|.
name|List
name|attrs
init|=
name|model
operator|.
name|getObjectList
argument_list|()
decl_stmt|;
name|int
name|attrPos
init|=
name|attrs
operator|.
name|indexOf
argument_list|(
name|attr
argument_list|)
decl_stmt|;
if|if
condition|(
name|attrPos
operator|>=
literal|0
condition|)
block|{
name|table
operator|.
name|select
argument_list|(
name|attrPos
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|processExistingSelection
parameter_list|(
name|EventObject
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|ChangeEvent
condition|)
block|{
name|table
operator|.
name|clearSelection
argument_list|()
expr_stmt|;
block|}
name|DbAttribute
name|att
init|=
literal|null
decl_stmt|;
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
name|att
operator|=
name|model
operator|.
name|getAttribute
argument_list|(
name|table
operator|.
name|getSelectedRow
argument_list|()
argument_list|)
expr_stmt|;
comment|// scroll table
name|UIUtil
operator|.
name|scrollToSelectedRow
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|this
argument_list|,
name|att
argument_list|,
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|)
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
comment|// if an entity was selected on a tree,
comment|// unselect currently selected row
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
name|nameColumnInd
argument_list|()
argument_list|)
decl_stmt|;
name|col
operator|.
name|setMinWidth
argument_list|(
literal|150
argument_list|)
expr_stmt|;
name|col
operator|=
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
expr_stmt|;
name|col
operator|.
name|setMinWidth
argument_list|(
literal|90
argument_list|)
expr_stmt|;
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
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|types
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|col
operator|.
name|setCellEditor
argument_list|(
operator|new
name|DefaultCellEditor
argument_list|(
name|comboBox
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|addListSelectionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

