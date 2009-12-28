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
name|Font
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
name|util
operator|.
name|ArrayList
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
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
name|JTable
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
name|ImageIcon
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
name|DefaultTableCellRenderer
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
name|ObjAttribute
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
name|ObjEntity
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
name|EntityEvent
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
name|ObjAttributeListener
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
name|ObjEntityListener
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
name|CopyAttributeAction
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
name|CutAttributeAction
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
name|ObjEntitySyncAction
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
name|dialog
operator|.
name|objentity
operator|.
name|ObjAttributeInfoDialog
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
name|ObjEntityDisplayListener
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
name|ModelerUtil
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

begin_comment
comment|/**  * Detail view of the ObjEntity attributes.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjEntityAttributeTab
extends|extends
name|JPanel
implements|implements
name|ObjEntityDisplayListener
implements|,
name|ObjEntityListener
implements|,
name|ObjAttributeListener
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
specifier|private
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
name|JButton
name|resolve
decl_stmt|;
specifier|public
name|ObjEntityAttributeTab
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
name|initController
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
name|ObjEntitySyncAction
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
name|Icon
name|ico
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-info.gif"
argument_list|)
decl_stmt|;
name|resolve
operator|=
operator|new
name|JButton
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setIcon
argument_list|(
name|ico
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setToolTipText
argument_list|(
literal|"Edit Attribute"
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|resolve
argument_list|)
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
name|CutAttributeAction
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
name|CopyAttributeAction
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
name|PasteAction
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
name|table
operator|=
operator|new
name|CayenneTable
argument_list|()
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
name|CellRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|ObjAttributeTableModel
operator|.
name|class
argument_list|,
literal|"objEntity/attributeTable"
argument_list|)
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
name|popup
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|app
operator|.
name|getAction
argument_list|(
name|CutAttributeAction
operator|.
name|getActionName
argument_list|()
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
name|app
operator|.
name|getAction
argument_list|(
name|CopyAttributeAction
operator|.
name|getActionName
argument_list|()
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
name|app
operator|.
name|getAction
argument_list|(
name|PasteAction
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
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addObjEntityDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjEntityListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjAttributeListener
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
name|ListSelectionListener
argument_list|()
block|{
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
block|}
argument_list|)
expr_stmt|;
name|ActionListener
name|resolver
init|=
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
name|int
name|row
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|row
operator|<
literal|0
condition|)
block|{
return|return;
block|}
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
comment|// ... show dialog...
operator|new
name|ObjAttributeInfoDialog
argument_list|(
name|mediator
argument_list|,
name|row
argument_list|,
name|model
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
comment|/**                  * This is required for a table to be updated properly                  */
name|table
operator|.
name|cancelEditing
argument_list|()
expr_stmt|;
comment|// need to refresh selected row... do this by unselecting/selecting the
comment|// row
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|clearSelection
argument_list|()
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|resolve
operator|.
name|addActionListener
argument_list|(
name|resolver
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
name|setupCCP
argument_list|(
name|table
argument_list|,
name|CutAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|,
name|CopyAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initComboBoxes
parameter_list|(
name|ObjAttributeTableModel
name|model
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|embeddableNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|typeNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|dataMap
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Embeddable
argument_list|>
name|embs
init|=
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|embs
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Embeddable
name|emb
init|=
operator|(
name|Embeddable
operator|)
name|embs
operator|.
name|next
argument_list|()
decl_stmt|;
name|embeddableNames
operator|.
name|add
argument_list|(
name|emb
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|String
index|[]
name|registeredTypes
init|=
name|ModelerUtil
operator|.
name|getRegisteredTypeNames
argument_list|()
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
name|registeredTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|typeNames
operator|.
name|add
argument_list|(
name|registeredTypes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|typeNames
operator|.
name|addAll
argument_list|(
name|embeddableNames
argument_list|)
expr_stmt|;
name|TableColumn
name|typeColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|ObjAttributeTableModel
operator|.
name|OBJ_ATTRIBUTE_TYPE
argument_list|)
decl_stmt|;
name|JComboBox
name|javaTypesCombo
init|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|typeNames
operator|.
name|toArray
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|javaTypesCombo
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|typeColumn
operator|.
name|setCellEditor
argument_list|(
name|CayenneWidgetFactory
operator|.
name|createCellEditor
argument_list|(
name|javaTypesCombo
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|nameAttr
init|=
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|mediator
argument_list|,
name|model
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|setCellEditor
argument_list|(
name|nameAttr
argument_list|,
name|table
argument_list|)
expr_stmt|;
name|model
operator|.
name|setComboBoxes
argument_list|(
name|nameAttr
argument_list|,
name|ObjAttributeTableModel
operator|.
name|DB_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Selects a specified attribute.      */
specifier|public
name|void
name|selectAttributes
parameter_list|(
name|ObjAttribute
index|[]
name|attrs
parameter_list|)
block|{
name|ModelerUtil
operator|.
name|updateActions
argument_list|(
name|attrs
operator|.
name|length
argument_list|,
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|,
name|CutAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|,
name|CopyAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|List
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
name|ObjAttribute
index|[]
name|attrs
init|=
operator|new
name|ObjAttribute
index|[
literal|0
index|]
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
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
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
name|ObjAttribute
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
comment|// scroll table
name|UIUtil
operator|.
name|scrollToSelectedRow
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resolve
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|resolve
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|AttributeDisplayEvent
name|ev
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|this
argument_list|,
name|attrs
argument_list|,
name|mediator
operator|.
name|getCurrentObjEntity
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
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
name|ev
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objAttributeChanged
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
name|objAttributeAdded
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|rebuildTable
argument_list|(
operator|(
name|ObjEntity
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
name|objAttributeRemoved
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
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
name|currentObjEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|this
condition|)
block|{
return|return;
block|}
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
decl_stmt|;
comment|// Important: process event even if this is the same entity,
comment|// since the inheritance structure might have changed
if|if
condition|(
name|entity
operator|!=
literal|null
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
name|ObjEntity
name|entity
parameter_list|)
block|{
name|ObjAttributeTableModel
name|model
init|=
operator|new
name|ObjAttributeTableModel
argument_list|(
name|entity
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
name|setUpTableStructure
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setUpTableStructure
parameter_list|(
name|ObjAttributeTableModel
name|model
parameter_list|)
block|{
name|int
name|inheritanceColumnWidth
init|=
literal|20
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|minSizes
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|maxSizes
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|minSizes
operator|.
name|put
argument_list|(
name|ObjAttributeTableModel
operator|.
name|INHERITED
argument_list|,
name|inheritanceColumnWidth
argument_list|)
expr_stmt|;
name|maxSizes
operator|.
name|put
argument_list|(
name|ObjAttributeTableModel
operator|.
name|INHERITED
argument_list|,
name|inheritanceColumnWidth
argument_list|)
expr_stmt|;
name|initComboBoxes
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|tablePreferences
operator|.
name|bind
argument_list|(
name|table
argument_list|,
name|minSizes
argument_list|,
name|maxSizes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Refreshes attributes view for the updated entity      */
specifier|public
name|void
name|objEntityChanged
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|this
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
operator|(
name|table
operator|.
name|getModel
argument_list|()
operator|instanceof
name|ObjAttributeTableModel
operator|)
condition|)
block|{
comment|// probably means this panel hasn't been loaded yet...
return|return;
block|}
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
if|if
condition|(
name|model
operator|.
name|getDbEntity
argument_list|()
operator|!=
operator|(
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
condition|)
block|{
name|model
operator|.
name|resetDbEntity
argument_list|()
expr_stmt|;
name|setUpTableStructure
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
block|}
comment|// custom renderer used for inherited attributes highlighting
specifier|final
class|class
name|CellRenderer
extends|extends
name|DefaultTableCellRenderer
block|{
annotation|@
name|Override
specifier|public
name|Component
name|getTableCellRendererComponent
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
name|boolean
name|hasFocus
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|super
operator|.
name|getTableCellRendererComponent
argument_list|(
name|table
argument_list|,
name|value
argument_list|,
name|isSelected
argument_list|,
name|hasFocus
argument_list|,
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|ObjAttributeTableModel
name|model
init|=
operator|(
name|ObjAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|column
operator|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|column
argument_list|)
operator|.
name|getModelIndex
argument_list|()
expr_stmt|;
name|ObjAttribute
name|attribute
init|=
name|model
operator|.
name|getAttribute
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|!=
name|ObjAttributeTableModel
operator|.
name|INHERITED
condition|)
block|{
if|if
condition|(
operator|!
name|model
operator|.
name|isCellEditable
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
condition|)
block|{
name|setForeground
argument_list|(
name|Color
operator|.
name|GRAY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setForeground
argument_list|(
name|isSelected
operator|&&
operator|!
name|hasFocus
condition|?
name|table
operator|.
name|getSelectionForeground
argument_list|()
else|:
name|table
operator|.
name|getForeground
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attribute
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|Font
name|font
init|=
name|getFont
argument_list|()
decl_stmt|;
name|Font
name|newFont
init|=
name|font
operator|.
name|deriveFont
argument_list|(
name|Font
operator|.
name|ITALIC
argument_list|)
decl_stmt|;
name|setFont
argument_list|(
name|newFont
argument_list|)
expr_stmt|;
block|}
name|setIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|attribute
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|ImageIcon
name|objEntityIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-override.gif"
argument_list|)
decl_stmt|;
name|setIcon
argument_list|(
name|objEntityIcon
argument_list|)
expr_stmt|;
block|}
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
name|setBackground
argument_list|(
name|isSelected
operator|&&
operator|!
name|hasFocus
condition|?
name|table
operator|.
name|getSelectionBackground
argument_list|()
else|:
name|table
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

