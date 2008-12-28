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
name|Collections
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
name|Entity
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
name|Relationship
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
name|ProjectUtil
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
name|project
operator|.
name|NamedObjectFactory
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
specifier|public
name|ResolveDbRelationshipDialog
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
name|initWithModel
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
comment|/**      * Creates graphical components.      */
specifier|private
name|void
name|initView
parameter_list|()
block|{
comment|// create widgets
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
name|removeButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Remove"
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
literal|"p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, top:14dlu, 3dlu, top:p:grow"
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
literal|"Relationship:"
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
name|name
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
literal|"Reverse Relationship"
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
name|reverseName
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
literal|7
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
literal|9
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
name|buttons
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
name|buttons
operator|.
name|add
argument_list|(
name|addButton
argument_list|)
expr_stmt|;
name|buttons
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
name|buttons
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|5
argument_list|,
literal|9
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
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|PanelFactory
operator|.
name|createButtonPanel
argument_list|(
operator|new
name|JButton
index|[]
block|{
name|saveButton
block|,
name|cancelButton
block|}
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initWithModel
parameter_list|(
name|DbRelationship
name|aRelationship
parameter_list|)
block|{
comment|// sanity check
if|if
condition|(
name|aRelationship
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
literal|"Null source entity: "
operator|+
name|aRelationship
argument_list|)
throw|;
block|}
if|if
condition|(
name|aRelationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null target entity: "
operator|+
name|aRelationship
argument_list|)
throw|;
block|}
if|if
condition|(
name|aRelationship
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
literal|"Null DataMap: "
operator|+
name|aRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
throw|;
block|}
comment|// Once assigned, can reference relationship directly. Would it be
comment|// OK to assign relationship at the very top of this method?
name|relationship
operator|=
name|aRelationship
expr_stmt|;
name|reverseRelationship
operator|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
expr_stmt|;
comment|// init UI components
name|setTitle
argument_list|(
literal|"DbRelationship Info: "
operator|+
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
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
name|sourceColumn
operator|.
name|setMinWidth
argument_list|(
literal|150
argument_list|)
expr_stmt|;
name|JComboBox
name|comboBox
init|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|getMediator
argument_list|()
argument_list|,
operator|(
name|DbEntity
operator|)
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
name|CayenneWidgetFactory
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
name|targetColumn
operator|.
name|setMinWidth
argument_list|(
literal|150
argument_list|)
expr_stmt|;
name|comboBox
operator|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|getMediator
argument_list|()
argument_list|,
operator|(
name|DbEntity
operator|)
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
name|CayenneWidgetFactory
operator|.
name|createCellEditor
argument_list|(
name|comboBox
argument_list|)
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
name|model
operator|.
name|addRow
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|relationship
argument_list|)
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
block|}
argument_list|)
expr_stmt|;
name|removeButton
operator|.
name|addActionListener
argument_list|(
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
name|model
operator|.
name|removeRow
argument_list|(
name|model
operator|.
name|getJoin
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|saveButton
operator|.
name|addActionListener
argument_list|(
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
name|cancelPressed
operator|=
literal|false
expr_stmt|;
name|save
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|addActionListener
argument_list|(
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
comment|// extract names...
name|String
name|sourceEntityName
init|=
name|name
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|sourceEntityName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|sourceEntityName
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|sourceEntityName
operator|==
literal|null
condition|)
block|{
name|sourceEntityName
operator|=
name|NamedObjectFactory
operator|.
name|createName
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|validateName
argument_list|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|relationship
argument_list|,
name|sourceEntityName
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|targetEntityName
init|=
name|reverseName
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetEntityName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|targetEntityName
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|targetEntityName
operator|==
literal|null
condition|)
block|{
name|targetEntityName
operator|=
name|NamedObjectFactory
operator|.
name|createName
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// check if reverse name is valid
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
if|if
condition|(
name|updatingReverse
operator|&&
operator|!
name|validateName
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|,
name|reverseRelationship
argument_list|,
name|targetEntityName
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// handle name update
if|if
condition|(
operator|!
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
name|String
name|oldName
init|=
name|relationship
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ProjectUtil
operator|.
name|setRelationshipName
argument_list|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|relationship
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
argument_list|(
name|targetEntityName
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
name|setTargetEntity
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
if|else if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|targetEntityName
argument_list|,
name|reverseRelationship
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|oldName
init|=
name|reverseRelationship
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ProjectUtil
operator|.
name|setRelationshipName
argument_list|(
name|reverseRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|reverseRelationship
argument_list|,
name|targetEntityName
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
name|reverseRelationship
argument_list|,
name|reverseRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|oldName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collection
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
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|validateName
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|Relationship
name|aRelationship
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|Relationship
name|existing
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
operator|&&
operator|(
name|aRelationship
operator|==
literal|null
operator|||
name|aRelationship
operator|!=
name|existing
operator|)
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
argument_list|,
literal|"There is an existing relationship named \""
operator|+
name|newName
operator|+
literal|"\". Select a different name."
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
name|Collection
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
name|EMPTY_LIST
return|;
block|}
name|List
name|reverseJoins
init|=
operator|new
name|ArrayList
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

