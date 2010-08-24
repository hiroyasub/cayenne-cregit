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
operator|.
name|validator
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
name|CayenneModelerFrame
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
name|WidgetFactory
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
name|CayenneTableModel
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
name|JScrollPane
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
name|JDialog
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
name|LinkedList
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
name|FlowLayout
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

begin_comment
comment|/**  * Dialog for resolving name collision.  *   */
end_comment

begin_class
specifier|public
class|class
name|DuplicatedAttributesDialog
extends|extends
name|CayenneDialog
block|{
specifier|protected
specifier|static
name|DuplicatedAttributesDialog
name|instance
decl_stmt|;
specifier|static
specifier|final
name|String
name|DELETE_ACTION
init|=
literal|"delete"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RENAME_ACTION
init|=
literal|"rename"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CANCEL_RESULT
init|=
literal|"cancel"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROCEEDED_RESULT
init|=
literal|"proceeded"
decl_stmt|;
specifier|static
name|String
name|result
init|=
name|CANCEL_RESULT
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DuplicatedAttributeInfo
argument_list|>
name|duplicatedAttributes
decl_stmt|;
specifier|protected
name|ObjEntity
name|superEntity
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|protected
name|JTable
name|attributesTable
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|JButton
name|proceedButton
decl_stmt|;
specifier|public
specifier|static
specifier|synchronized
name|void
name|showDialog
parameter_list|(
name|CayenneModelerFrame
name|editor
parameter_list|,
name|List
argument_list|<
name|ObjAttribute
argument_list|>
name|duplicatedAttributes
parameter_list|,
name|ObjEntity
name|superEntity
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|DuplicatedAttributesDialog
argument_list|(
name|editor
argument_list|)
expr_stmt|;
name|instance
operator|.
name|centerWindow
argument_list|()
expr_stmt|;
block|}
name|instance
operator|.
name|setSuperEntity
argument_list|(
name|superEntity
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setDuplicatedAttributes
argument_list|(
name|duplicatedAttributes
argument_list|)
expr_stmt|;
name|instance
operator|.
name|updateTable
argument_list|()
expr_stmt|;
name|instance
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DuplicatedAttributesDialog
parameter_list|(
name|CayenneModelerFrame
name|editor
parameter_list|)
block|{
name|super
argument_list|(
name|editor
argument_list|,
literal|"Duplicated Attributes"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|CANCEL_RESULT
expr_stmt|;
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
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|proceedButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Continue"
argument_list|)
expr_stmt|;
name|attributesTable
operator|=
operator|new
name|JTable
argument_list|()
expr_stmt|;
comment|// assemble
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
literal|"fill:200dlu:grow"
argument_list|,
literal|"pref, 3dlu, top:40dlu:grow"
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
name|addLabel
argument_list|(
literal|"Select actions for duplicated attributes:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
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
name|attributesTable
argument_list|)
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
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|cancelButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|proceedButton
argument_list|)
expr_stmt|;
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
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
comment|// TODO: use preferences
name|setSize
argument_list|(
literal|450
argument_list|,
literal|350
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|setDefaultCloseOperation
argument_list|(
name|JDialog
operator|.
name|DISPOSE_ON_CLOSE
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
name|result
operator|=
name|CANCEL_RESULT
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|proceedButton
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
name|applyChanges
argument_list|()
expr_stmt|;
name|result
operator|=
name|PROCEEDED_RESULT
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
specifier|private
name|void
name|updateTable
parameter_list|()
block|{
name|TableColumn
name|actionColumn
init|=
name|attributesTable
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|DuplicatedAttributeTableModel
operator|.
name|ACTION
argument_list|)
decl_stmt|;
name|JComboBox
name|actionsCombo
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
operator|new
name|String
index|[]
block|{
name|DELETE_ACTION
block|,
name|RENAME_ACTION
block|}
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|actionColumn
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
name|actionsCombo
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|applyChanges
parameter_list|()
block|{
for|for
control|(
name|DuplicatedAttributeInfo
name|attributeInfo
range|:
name|duplicatedAttributes
control|)
block|{
if|if
condition|(
name|attributeInfo
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|DELETE_ACTION
argument_list|)
condition|)
block|{
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attributeInfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attributeInfo
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|RENAME_ACTION
argument_list|)
condition|)
block|{
name|ProjectUtil
operator|.
name|setAttributeName
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
name|attributeInfo
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|attributeInfo
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setDuplicatedAttributes
parameter_list|(
name|List
argument_list|<
name|ObjAttribute
argument_list|>
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|duplicatedAttributes
operator|==
literal|null
condition|)
block|{
name|duplicatedAttributes
operator|=
operator|new
name|LinkedList
argument_list|<
name|DuplicatedAttributeInfo
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|duplicatedAttributes
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjAttribute
name|attribute
range|:
name|attributes
control|)
block|{
name|DuplicatedAttributeInfo
name|attributeInfo
init|=
operator|new
name|DuplicatedAttributeInfo
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
operator|(
operator|(
name|ObjAttribute
operator|)
name|superEntity
operator|.
name|getAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|.
name|getType
argument_list|()
argument_list|,
name|DELETE_ACTION
argument_list|)
decl_stmt|;
name|duplicatedAttributes
operator|.
name|add
argument_list|(
name|attributeInfo
argument_list|)
expr_stmt|;
block|}
name|attributesTable
operator|.
name|setModel
argument_list|(
operator|new
name|DuplicatedAttributeTableModel
argument_list|(
name|getMediator
argument_list|()
argument_list|,
name|this
argument_list|,
name|duplicatedAttributes
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSuperEntity
parameter_list|(
name|ObjEntity
name|superEntity
parameter_list|)
block|{
name|this
operator|.
name|superEntity
operator|=
name|superEntity
expr_stmt|;
block|}
specifier|public
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
class|class
name|DuplicatedAttributeTableModel
extends|extends
name|CayenneTableModel
block|{
specifier|static
specifier|final
name|int
name|ATTRIBUTE_NAME
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|PARENT_TYPE
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|TYPE
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|ACTION
init|=
literal|3
decl_stmt|;
comment|/**          * Constructor for CayenneTableModel.          */
specifier|public
name|DuplicatedAttributeTableModel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|Object
name|eventSource
parameter_list|,
name|List
name|objectList
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|,
name|eventSource
argument_list|,
name|objectList
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|newValue
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|DuplicatedAttributeInfo
name|attributeInfo
init|=
name|duplicatedAttributes
operator|.
name|get
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
name|ATTRIBUTE_NAME
condition|)
block|{
name|attributeInfo
operator|.
name|setNewName
argument_list|(
name|newValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|attributeInfo
operator|.
name|setAction
argument_list|(
name|RENAME_ACTION
argument_list|)
expr_stmt|;
comment|//TODO: add warn if new valuew equals the old one or name equals to another attribute name.
name|this
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|column
operator|==
name|ACTION
condition|)
block|{
name|attributeInfo
operator|.
name|setAction
argument_list|(
name|newValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getElementsClass
parameter_list|()
block|{
return|return
name|DuplicatedAttributeInfo
operator|.
name|class
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|4
return|;
block|}
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
name|DuplicatedAttributeInfo
name|attributeInfo
init|=
name|duplicatedAttributes
operator|.
name|get
argument_list|(
name|row
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|col
condition|)
block|{
case|case
name|ATTRIBUTE_NAME
case|:
return|return
name|attributeInfo
operator|.
name|getNewName
argument_list|()
return|;
case|case
name|PARENT_TYPE
case|:
return|return
name|attributeInfo
operator|.
name|getParentType
argument_list|()
return|;
case|case
name|TYPE
case|:
return|return
name|attributeInfo
operator|.
name|getType
argument_list|()
return|;
case|case
name|ACTION
case|:
return|return
name|attributeInfo
operator|.
name|getAction
argument_list|()
return|;
block|}
return|return
literal|""
return|;
block|}
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
if|if
condition|(
name|column
operator|==
name|ACTION
operator|||
name|column
operator|==
name|ATTRIBUTE_NAME
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
switch|switch
condition|(
name|column
condition|)
block|{
case|case
name|ATTRIBUTE_NAME
case|:
return|return
literal|"Name"
return|;
case|case
name|PARENT_TYPE
case|:
return|return
literal|"Type in super entity"
return|;
case|case
name|TYPE
case|:
return|return
literal|"Type"
return|;
case|case
name|ACTION
case|:
return|return
literal|"Action"
return|;
block|}
return|return
literal|" "
return|;
block|}
specifier|public
name|Class
name|getColumnClass
parameter_list|(
name|int
name|column
parameter_list|)
block|{
return|return
name|String
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isColumnSortable
parameter_list|(
name|int
name|sortCol
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sortByColumn
parameter_list|(
name|int
name|sortCol
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
block|{
block|}
block|}
specifier|public
class|class
name|DuplicatedAttributeInfo
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|newName
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|parentType
decl_stmt|;
specifier|private
name|String
name|action
decl_stmt|;
name|DuplicatedAttributeInfo
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|parentType
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|parentType
operator|=
name|parentType
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getParentType
parameter_list|()
block|{
return|return
name|parentType
return|;
block|}
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
name|newName
return|;
block|}
specifier|public
name|void
name|setNewName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

