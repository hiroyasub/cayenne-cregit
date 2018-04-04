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
name|DeleteRule
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
name|ObjRelationship
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
name|map
operator|.
name|event
operator|.
name|ObjRelationshipListener
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
name|RelationshipEvent
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
name|dialog
operator|.
name|objentity
operator|.
name|ObjRelationshipInfo
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
name|CayenneAction
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
name|CellRenderers
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
name|Comparators
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
name|DbRelationshipPathComboBoxEditor
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
name|swing
operator|.
name|components
operator|.
name|image
operator|.
name|FilteredIconFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Icon
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
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
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
name|ListSelectionModel
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
name|List
import|;
end_import

begin_comment
comment|/**  * Displays ObjRelationships for the edited ObjEntity.  */
end_comment

begin_class
specifier|public
class|class
name|ObjEntityRelationshipPanel
extends|extends
name|JPanel
implements|implements
name|ObjEntityDisplayListener
implements|,
name|ObjEntityListener
implements|,
name|ObjRelationshipListener
block|{
specifier|private
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ObjEntityRelationshipPanel
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Object
index|[]
name|DELETE_RULES
init|=
operator|new
name|Object
index|[]
block|{
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|NO_ACTION
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|CASCADE
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
block|,     }
decl_stmt|;
specifier|private
name|ProjectController
name|mediator
decl_stmt|;
specifier|private
name|CayenneTable
name|table
decl_stmt|;
specifier|private
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
specifier|private
name|ActionListener
name|resolver
decl_stmt|;
specifier|private
name|ObjEntityAttributeRelationshipTab
name|parentPanel
decl_stmt|;
specifier|private
name|boolean
name|enabledResolve
decl_stmt|;
comment|//for JBottom "resolve" in ObjEntityAttrRelationshipTab
comment|/**      * By now popup menu item is made similar to toolbar button. (i.e. all functionality      * is here) This should be probably refactored as Action.      */
specifier|private
name|JMenuItem
name|resolveMenu
decl_stmt|;
specifier|public
name|ObjEntityRelationshipPanel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|ObjEntityAttributeRelationshipTab
name|parentPanel
parameter_list|)
block|{
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
name|init
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CayenneTable
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|void
name|setTable
parameter_list|(
name|CayenneTable
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
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
name|StringRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setDefaultRenderer
argument_list|(
name|ObjEntity
operator|.
name|class
argument_list|,
operator|new
name|EntityRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|ObjRelationshipTableModel
operator|.
name|class
argument_list|,
literal|"objEntity/relationshipTable"
argument_list|)
expr_stmt|;
comment|// Create and install a popup
name|Icon
name|ico
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-edit.png"
argument_list|)
decl_stmt|;
name|resolveMenu
operator|=
operator|new
name|CayenneAction
operator|.
name|CayenneMenuItem
argument_list|(
literal|"Database Mapping"
argument_list|,
name|ico
argument_list|)
expr_stmt|;
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
name|resolveMenu
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
name|addObjRelationshipListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|resolver
operator|=
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
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
operator|new
name|ObjRelationshipInfo
argument_list|(
name|mediator
argument_list|,
name|model
operator|.
name|getRelationship
argument_list|(
name|row
argument_list|)
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
name|enabledResolve
operator|=
literal|false
expr_stmt|;
block|}
block|}
expr_stmt|;
name|resolveMenu
operator|.
name|addActionListener
argument_list|(
name|resolver
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
name|ObjRelationshipListSelectionListener
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
comment|/**      * Selects a specified relationship in the relationships table.      */
specifier|public
name|void
name|selectRelationships
parameter_list|(
name|ObjRelationship
index|[]
name|rels
parameter_list|)
block|{
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|List
name|listRels
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
name|rels
operator|.
name|length
index|]
decl_stmt|;
name|parentPanel
operator|.
name|updateActions
argument_list|(
name|rels
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
name|rels
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
name|listRels
operator|.
name|indexOf
argument_list|(
name|rels
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
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Loads obj relationships into table.      */
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
name|ObjEntity
name|objEntity
init|=
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|objEntity
operator|.
name|getSuperEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parentPanel
operator|.
name|getToolBar
argument_list|()
operator|.
name|getComponentAtIndex
argument_list|(
literal|2
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parentPanel
operator|.
name|getToolBar
argument_list|()
operator|.
name|getComponentAtIndex
argument_list|(
literal|2
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a list of ObjEntity names.      */
specifier|private
name|Object
index|[]
name|createObjEntityComboModel
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
comment|// this actually happens per CAY-221... can't reproduce though
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"createObjEntityComboModel:: Null DataMap."
argument_list|)
expr_stmt|;
return|return
operator|new
name|Object
index|[
literal|0
index|]
return|;
block|}
if|if
condition|(
name|map
operator|.
name|getNamespace
argument_list|()
operator|==
literal|null
condition|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"createObjEntityComboModel:: Null DataMap namespace - "
operator|+
name|map
argument_list|)
expr_stmt|;
return|return
operator|new
name|Object
index|[
literal|0
index|]
return|;
block|}
return|return
name|map
operator|.
name|getNamespace
argument_list|()
operator|.
name|getObjEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
name|Comparators
operator|.
name|getDataMapChildrenComparator
argument_list|()
argument_list|)
operator|.
name|toArray
argument_list|()
return|;
block|}
specifier|public
name|void
name|objEntityChanged
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|reloadEntityList
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|reloadEntityList
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipChanged
parameter_list|(
name|RelationshipEvent
name|e
parameter_list|)
block|{
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipAdded
parameter_list|(
name|RelationshipEvent
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
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipRemoved
parameter_list|(
name|RelationshipEvent
name|e
parameter_list|)
block|{
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
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
name|getRelationship
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|removeRow
argument_list|(
operator|(
name|ObjRelationship
operator|)
name|e
operator|.
name|getRelationship
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
comment|/**      * Refresh the list of ObjEntity targets. Also refresh the table in case some      * ObjRelationships were deleted.      */
specifier|private
name|void
name|reloadEntityList
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
operator|!=
name|this
condition|)
block|{
return|return;
block|}
comment|// If current model added/removed, do nothing.
name|ObjEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
name|e
operator|.
name|getEntity
argument_list|()
operator|||
name|entity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|ObjRelationshipTableModel
operator|.
name|REL_TARGET
argument_list|)
decl_stmt|;
name|DefaultCellEditor
name|editor
init|=
operator|(
name|DefaultCellEditor
operator|)
name|col
operator|.
name|getCellEditor
argument_list|()
decl_stmt|;
name|JComboBox
name|combo
init|=
operator|(
name|JComboBox
operator|)
name|editor
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|combo
operator|.
name|setRenderer
argument_list|(
name|CellRenderers
operator|.
name|entityListRendererWithIcons
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|rebuildTable
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
specifier|final
name|ObjRelationshipTableModel
name|model
init|=
operator|new
name|ObjRelationshipTableModel
argument_list|(
name|entity
argument_list|,
name|mediator
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|model
operator|.
name|addTableModelListener
argument_list|(
name|e
lambda|->
block|{
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
name|ObjRelationship
name|rel
init|=
name|model
operator|.
name|getRelationship
argument_list|(
name|table
operator|.
name|getSelectedRow
argument_list|()
argument_list|)
decl_stmt|;
name|enabledResolve
operator|=
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|!=
literal|null
expr_stmt|;
name|resolveMenu
operator|.
name|setEnabled
argument_list|(
name|enabledResolve
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|ObjRelationshipTableModel
operator|.
name|REL_TARGET_PATH
argument_list|)
decl_stmt|;
name|col
operator|.
name|setCellEditor
argument_list|(
operator|new
name|DbRelationshipPathComboBoxEditor
argument_list|()
argument_list|)
expr_stmt|;
name|col
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|DefaultTableCellRenderer
argument_list|()
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
name|setToolTipText
argument_list|(
literal|"To choose relationship press enter two times.To choose next relationship press dot."
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
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
name|ObjRelationshipTableModel
operator|.
name|REL_DELETE_RULE
argument_list|)
expr_stmt|;
name|JComboBox
name|deleteRulesCombo
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|DELETE_RULES
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|deleteRulesCombo
operator|.
name|setFocusable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteRulesCombo
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JComponent
operator|)
name|deleteRulesCombo
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
name|deleteRulesCombo
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
name|deleteRulesCombo
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// Default to the first value
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
name|deleteRulesCombo
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
name|ObjRelationshipTableModel
operator|.
name|REL_NAME
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
class|class
name|EntityRenderer
extends|extends
name|StringRenderer
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
name|Object
name|oldValue
init|=
name|value
decl_stmt|;
name|value
operator|=
name|CellRenderers
operator|.
name|asString
argument_list|(
name|value
argument_list|)
expr_stmt|;
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
name|Icon
name|icon
init|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
name|oldValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSelected
condition|)
block|{
name|setForeground
argument_list|(
name|UIManager
operator|.
name|getColor
argument_list|(
literal|"Table.selectionForeground"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
class|class
name|StringRenderer
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
comment|// center cardinality column
name|int
name|align
init|=
name|column
operator|==
name|ObjRelationshipTableModel
operator|.
name|REL_SEMANTICS
condition|?
name|JLabel
operator|.
name|CENTER
else|:
name|JLabel
operator|.
name|LEFT
decl_stmt|;
name|super
operator|.
name|setHorizontalAlignment
argument_list|(
name|align
argument_list|)
expr_stmt|;
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
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
name|model
operator|.
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|!=
name|model
operator|.
name|getEntity
argument_list|()
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
return|return
name|this
return|;
block|}
block|}
specifier|private
class|class
name|ObjRelationshipListSelectionListener
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
name|ObjRelationship
index|[]
name|rels
init|=
operator|new
name|ObjRelationship
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
name|getAttributePanel
argument_list|()
operator|.
name|getTable
argument_list|()
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
name|getAttributePanel
argument_list|()
operator|.
name|getTable
argument_list|()
operator|.
name|getCellEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
operator|.
name|getTable
argument_list|()
operator|.
name|getCellEditor
argument_list|()
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
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
name|getRelationshipPanel
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
name|getRelationshipPanel
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
name|getRelationshipPanel
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|parentPanel
operator|.
name|getAttributePanel
argument_list|()
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|setToolTipText
argument_list|(
literal|"Edit Relationship"
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|getResolve
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
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
name|ObjRelationshipTableModel
name|model
init|=
operator|(
name|ObjRelationshipTableModel
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
name|rels
operator|=
operator|new
name|ObjRelationship
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
name|rels
index|[
name|i
index|]
operator|=
name|model
operator|.
name|getRelationship
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
name|enabledResolve
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|enabledResolve
operator|=
literal|false
expr_stmt|;
block|}
name|resolveMenu
operator|.
name|setEnabled
argument_list|(
name|enabledResolve
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|setCurrentObjRelationships
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|parentPanel
operator|.
name|updateActions
argument_list|(
name|rels
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isEnabledResolve
parameter_list|()
block|{
return|return
name|enabledResolve
return|;
block|}
specifier|public
name|ActionListener
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
block|}
end_class

end_unit

