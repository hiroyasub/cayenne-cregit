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
name|dialog
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|PanelBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|CellConstraints
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
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
name|CayenneRuntimeException
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
name|dbsync
operator|.
name|naming
operator|.
name|NameBuilder
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
name|DbJoin
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
name|DbRelationship
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
name|MapEvent
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
name|undo
operator|.
name|RelationshipUndoableEdit
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
name|CayenneDialog
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
name|util
operator|.
name|Util
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
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|JScrollPane
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
name|ListSelectionModel
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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
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
name|Collections
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
comment|/**  * Editor of DbRelationship joins.  */
end_comment

begin_class
specifier|public
class|class
name|ResolveDbRelationshipDialog
extends|extends
name|CayenneDialog
block|{
specifier|protected
name|DbRelationship
name|relationship
decl_stmt|;
specifier|protected
name|DbRelationship
name|reverseRelationship
decl_stmt|;
specifier|protected
name|JLabel
name|sourceName
decl_stmt|;
specifier|protected
name|JLabel
name|targetName
decl_stmt|;
specifier|protected
name|JTextField
name|name
decl_stmt|;
specifier|protected
name|JTextField
name|reverseName
decl_stmt|;
specifier|protected
name|CayenneTable
name|table
decl_stmt|;
specifier|protected
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
specifier|protected
name|JButton
name|addButton
decl_stmt|;
specifier|protected
name|JButton
name|removeButton
decl_stmt|;
specifier|protected
name|JButton
name|saveButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|private
name|boolean
name|cancelPressed
decl_stmt|;
specifier|private
name|RelationshipUndoableEdit
name|undo
decl_stmt|;
specifier|private
name|boolean
name|editable
decl_stmt|;
specifier|public
name|ResolveDbRelationshipDialog
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|this
argument_list|(
name|relationship
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolveDbRelationshipDialog
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|boolean
name|editable
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"DbRelationship Inspector"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|editable
operator|=
name|editable
expr_stmt|;
if|if
condition|(
operator|!
name|validateAndSetRelationship
argument_list|(
name|relationship
argument_list|)
condition|)
block|{
name|this
operator|.
name|cancelPressed
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
name|initWithModel
argument_list|()
expr_stmt|;
name|this
operator|.
name|undo
operator|=
operator|new
name|RelationshipUndoableEdit
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
name|this
operator|.
name|centerWindow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setVisible
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|&&
name|cancelPressed
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|setVisible
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates graphical components.      */
specifier|private
name|void
name|initView
parameter_list|()
block|{
comment|// create widgets
name|sourceName
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|targetName
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|JTextField
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|reverseName
operator|=
operator|new
name|JTextField
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|addButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Add"
argument_list|)
expr_stmt|;
name|addButton
operator|.
name|setEnabled
argument_list|(
name|this
operator|.
name|editable
argument_list|)
expr_stmt|;
name|removeButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Remove"
argument_list|)
expr_stmt|;
name|removeButton
operator|.
name|setEnabled
argument_list|(
name|this
operator|.
name|editable
argument_list|)
expr_stmt|;
name|saveButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Done"
argument_list|)
expr_stmt|;
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|setEnabled
argument_list|(
name|this
operator|.
name|editable
argument_list|)
expr_stmt|;
name|table
operator|=
operator|new
name|AttributeTable
argument_list|()
expr_stmt|;
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|SINGLE_SELECTION
argument_list|)
expr_stmt|;
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"dbentity/dbjoinTable"
argument_list|)
expr_stmt|;
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|saveButton
argument_list|)
expr_stmt|;
comment|// assemble
name|getContentPane
argument_list|()
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"right:max(50dlu;pref), 3dlu, fill:min(150dlu;pref), 3dlu, fill:min(50dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, top:14dlu, 3dlu, top:p:grow"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"DbRelationship Information"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Source Entity:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|sourceName
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Target Entity:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|targetName
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Relationship Name:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Reverse Relationship Name:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|reverseName
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"Joins"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|11
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|table
argument_list|)
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|13
argument_list|,
literal|3
argument_list|,
literal|3
argument_list|,
literal|"fill, fill"
argument_list|)
argument_list|)
expr_stmt|;
name|JPanel
name|joinButtons
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEADING
argument_list|)
argument_list|)
decl_stmt|;
name|joinButtons
operator|.
name|add
argument_list|(
name|addButton
argument_list|)
expr_stmt|;
name|joinButtons
operator|.
name|add
argument_list|(
name|removeButton
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|joinButtons
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|5
argument_list|,
literal|13
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|JButton
index|[]
name|buttons
init|=
block|{
name|cancelButton
block|,
name|saveButton
block|}
decl_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|PanelFactory
operator|.
name|createButtonPanel
argument_list|(
name|buttons
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|validateAndSetRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|reverseRelationship
operator|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
expr_stmt|;
comment|// sanity check
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null source entity: %s"
argument_list|,
name|relationship
argument_list|)
throw|;
block|}
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null DataMap: %s"
argument_list|,
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
throw|;
block|}
comment|// warn if no target entity
if|if
condition|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
argument_list|,
literal|"Please select target DbEntity first"
argument_list|,
literal|"Select target"
argument_list|,
name|JOptionPane
operator|.
name|INFORMATION_MESSAGE
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|initWithModel
parameter_list|()
block|{
comment|// init UI components
name|sourceName
operator|.
name|setText
argument_list|(
name|relationship
operator|.
name|getSourceEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|targetName
operator|.
name|setText
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|.
name|setText
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|reverseRelationship
operator|!=
literal|null
condition|)
block|{
name|reverseName
operator|.
name|setText
argument_list|(
name|reverseRelationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setModel
argument_list|(
operator|new
name|DbJoinTableModel
argument_list|(
name|relationship
argument_list|,
name|getMediator
argument_list|()
argument_list|,
name|this
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|TableColumn
name|sourceColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|DbJoinTableModel
operator|.
name|SOURCE
argument_list|)
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
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|comboBox
argument_list|)
expr_stmt|;
name|sourceColumn
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
name|targetColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|DbJoinTableModel
operator|.
name|TARGET
argument_list|)
decl_stmt|;
name|comboBox
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|comboBox
argument_list|)
expr_stmt|;
name|targetColumn
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
name|DbJoinTableModel
operator|.
name|SOURCE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|addButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|DbJoinTableModel
name|model
init|=
operator|(
name|DbJoinTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DbJoin
name|join
init|=
operator|new
name|DbJoin
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|model
operator|.
name|addRow
argument_list|(
name|join
argument_list|)
expr_stmt|;
name|undo
operator|.
name|addDbJoinAddUndo
argument_list|(
name|join
argument_list|)
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|model
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|removeButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|DbJoinTableModel
name|model
init|=
operator|(
name|DbJoinTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|stopEditing
argument_list|()
expr_stmt|;
name|int
name|row
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
name|DbJoin
name|join
init|=
name|model
operator|.
name|getJoin
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|undo
operator|.
name|addDbJoinRemoveUndo
argument_list|(
name|join
argument_list|)
expr_stmt|;
name|model
operator|.
name|removeRow
argument_list|(
name|join
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|saveButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|cancelPressed
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|editable
condition|)
block|{
name|save
argument_list|()
expr_stmt|;
block|}
name|dispose
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|cancelPressed
operator|=
literal|true
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCancelPressed
parameter_list|()
block|{
return|return
name|cancelPressed
return|;
block|}
specifier|private
name|void
name|stopEditing
parameter_list|()
block|{
comment|// Stop whatever editing may be taking place
name|int
name|col_index
init|=
name|table
operator|.
name|getEditingColumn
argument_list|()
decl_stmt|;
if|if
condition|(
name|col_index
operator|>=
literal|0
condition|)
block|{
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
name|col_index
argument_list|)
decl_stmt|;
name|col
operator|.
name|getCellEditor
argument_list|()
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|save
parameter_list|()
block|{
name|stopEditing
argument_list|()
expr_stmt|;
name|DbJoinTableModel
name|model
init|=
operator|(
name|DbJoinTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|boolean
name|updatingReverse
init|=
name|model
operator|.
name|getObjectList
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
decl_stmt|;
comment|// handle name update
name|handleNameUpdate
argument_list|(
name|relationship
argument_list|,
name|name
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|// check "to dep pk" setting,
comment|// maybe this is no longer valid
if|if
condition|(
name|relationship
operator|.
name|isToDependentPK
argument_list|()
operator|&&
operator|!
name|relationship
operator|.
name|isValidForDepPk
argument_list|()
condition|)
block|{
name|relationship
operator|.
name|setToDependentPK
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// If new reverse DbRelationship was created, add it to the target
comment|// Don't create reverse with no joins - makes no sense...
if|if
condition|(
name|updatingReverse
condition|)
block|{
comment|// If didn't find anything, create reverseDbRel
if|if
condition|(
name|reverseRelationship
operator|==
literal|null
condition|)
block|{
name|reverseRelationship
operator|=
operator|new
name|DbRelationship
argument_list|()
expr_stmt|;
name|reverseRelationship
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|reverseRelationship
argument_list|,
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
operator|.
name|baseName
argument_list|(
name|reverseName
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|reverseRelationship
operator|.
name|setSourceEntity
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
name|reverseRelationship
operator|.
name|setTargetEntityName
argument_list|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
name|reverseRelationship
operator|.
name|setToMany
argument_list|(
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|addRelationship
argument_list|(
name|reverseRelationship
argument_list|)
expr_stmt|;
comment|// fire only if the relationship is to the same entity...
comment|// this is needed to update entity view...
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|==
name|relationship
operator|.
name|getTargetEntity
argument_list|()
condition|)
block|{
name|getMediator
argument_list|()
operator|.
name|fireDbRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|this
argument_list|,
name|reverseRelationship
argument_list|,
name|reverseRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|handleNameUpdate
argument_list|(
name|reverseRelationship
argument_list|,
name|reverseName
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|reverseJoins
init|=
name|getReverseJoins
argument_list|()
decl_stmt|;
name|reverseRelationship
operator|.
name|setJoins
argument_list|(
name|reverseJoins
argument_list|)
expr_stmt|;
comment|// check if joins map to a primary key of this entity
if|if
condition|(
operator|!
name|relationship
operator|.
name|isToDependentPK
argument_list|()
operator|&&
name|reverseRelationship
operator|.
name|isValidForDepPk
argument_list|()
condition|)
block|{
name|reverseRelationship
operator|.
name|setToDependentPK
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
name|undo
argument_list|)
expr_stmt|;
name|getMediator
argument_list|()
operator|.
name|fireDbRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|this
argument_list|,
name|relationship
argument_list|,
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleNameUpdate
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|String
name|userInputName
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|userInputName
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|sourceEntityName
init|=
name|NameBuilder
operator|.
name|builder
argument_list|(
name|relationship
argument_list|,
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
operator|.
name|baseName
argument_list|(
name|userInputName
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|sourceEntityName
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|oldName
init|=
name|relationship
operator|.
name|getName
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setName
argument_list|(
name|sourceEntityName
argument_list|)
expr_stmt|;
name|undo
operator|.
name|addNameUndo
argument_list|(
name|relationship
argument_list|,
name|oldName
argument_list|,
name|sourceEntityName
argument_list|)
expr_stmt|;
name|getMediator
argument_list|()
operator|.
name|fireDbRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|this
argument_list|,
name|relationship
argument_list|,
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|oldName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|getReverseJoins
parameter_list|()
block|{
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|relationship
operator|.
name|getJoins
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|joins
operator|==
literal|null
operator|)
operator|||
operator|(
name|joins
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|DbJoin
argument_list|>
name|reverseJoins
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|joins
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// Loop through the list of attribute pairs, create reverse pairs
comment|// and put them to the reverse list.
for|for
control|(
name|DbJoin
name|pair
range|:
name|joins
control|)
block|{
name|DbJoin
name|reverseJoin
init|=
name|pair
operator|.
name|createReverseJoin
argument_list|()
decl_stmt|;
comment|// since reverse relationship is not yet initialized,
comment|// reverse join will not have it set automatically
name|reverseJoin
operator|.
name|setRelationship
argument_list|(
name|reverseRelationship
argument_list|)
expr_stmt|;
name|reverseJoins
operator|.
name|add
argument_list|(
name|reverseJoin
argument_list|)
expr_stmt|;
block|}
return|return
name|reverseJoins
return|;
block|}
specifier|final
class|class
name|AttributeTable
extends|extends
name|CayenneTable
block|{
specifier|final
name|Dimension
name|preferredSize
init|=
operator|new
name|Dimension
argument_list|(
literal|203
argument_list|,
literal|100
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Dimension
name|getPreferredScrollableViewportSize
parameter_list|()
block|{
return|return
name|preferredSize
return|;
block|}
block|}
block|}
end_class

end_unit

