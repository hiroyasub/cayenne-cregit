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
name|action
operator|.
name|ValidateAction
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
name|validation
operator|.
name|ValidationFailure
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
name|JDialog
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
name|JTable
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
name|AbstractTableModel
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
name|MouseAdapter
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
comment|/**  * Dialog for displaying validation errors.  *   */
end_comment

begin_class
specifier|public
class|class
name|ValidatorDialog
extends|extends
name|CayenneDialog
block|{
specifier|protected
specifier|static
name|ValidatorDialog
name|instance
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Color
name|WARNING_COLOR
init|=
operator|new
name|Color
argument_list|(
literal|245
argument_list|,
literal|194
argument_list|,
literal|194
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Color
name|ERROR_COLOR
init|=
operator|new
name|Color
argument_list|(
literal|237
argument_list|,
literal|121
argument_list|,
literal|121
argument_list|)
decl_stmt|;
specifier|protected
name|JTable
name|problemsTable
decl_stmt|;
specifier|protected
name|JButton
name|closeButton
decl_stmt|;
specifier|protected
name|JButton
name|refreshButton
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ValidationFailure
argument_list|>
name|validationObjects
decl_stmt|;
specifier|public
specifier|static
name|void
name|showDialog
parameter_list|(
name|CayenneModelerFrame
name|frame
parameter_list|,
name|List
argument_list|<
name|ValidationFailure
argument_list|>
name|list
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
name|ValidatorDialog
argument_list|(
name|frame
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
name|refreshFromModel
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|instance
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|showValidationSuccess
parameter_list|(
name|CayenneModelerFrame
name|editor
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|instance
operator|=
literal|null
expr_stmt|;
block|}
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|editor
argument_list|,
literal|"Cayenne project is valid."
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ValidatorDialog
parameter_list|(
name|CayenneModelerFrame
name|editor
parameter_list|)
block|{
name|super
argument_list|(
name|editor
argument_list|,
literal|"Validation Problems"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|validationObjects
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
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
name|refreshButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Refresh"
argument_list|)
expr_stmt|;
name|closeButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Close"
argument_list|)
expr_stmt|;
name|problemsTable
operator|=
operator|new
name|JTable
argument_list|()
expr_stmt|;
name|problemsTable
operator|.
name|setRowHeight
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|problemsTable
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|problemsTable
operator|.
name|setCellSelectionEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|problemsTable
operator|.
name|setRowSelectionAllowed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|problemsTable
operator|.
name|setDefaultRenderer
argument_list|(
name|ValidationFailure
operator|.
name|class
argument_list|,
operator|new
name|ValidationRenderer
argument_list|()
argument_list|)
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
literal|"pref, 3dlu, fill:40dlu:grow"
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
literal|"Click on any row below to go to the object that has a validation problem:"
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
name|problemsTable
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
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|refreshButton
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
name|closeButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|refreshButton
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
name|problemsTable
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|addListSelectionListener
argument_list|(
name|e
lambda|->
name|showFailedObject
argument_list|()
argument_list|)
expr_stmt|;
name|closeButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dispose
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|refreshButton
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
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
name|ValidateAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|problemsTable
operator|.
name|addMouseListener
argument_list|(
operator|new
name|MouseAdapter
argument_list|()
block|{
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|int
name|row
init|=
name|problemsTable
operator|.
name|rowAtPoint
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
decl_stmt|;
comment|// if this happens to be a selected row, re-run object selection
if|if
condition|(
name|row
operator|>=
literal|0
operator|&&
name|problemsTable
operator|.
name|getSelectedRow
argument_list|()
operator|==
name|row
condition|)
block|{
name|showFailedObject
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|refreshFromModel
parameter_list|(
name|List
argument_list|<
name|ValidationFailure
argument_list|>
name|list
parameter_list|)
block|{
name|validationObjects
operator|=
name|list
expr_stmt|;
name|problemsTable
operator|.
name|setModel
argument_list|(
operator|new
name|ValidatorTableModel
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|showFailedObject
parameter_list|()
block|{
if|if
condition|(
name|problemsTable
operator|.
name|getSelectedRow
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|ValidationFailure
name|obj
init|=
operator|(
name|ValidationFailure
operator|)
name|problemsTable
operator|.
name|getModel
argument_list|()
operator|.
name|getValueAt
argument_list|(
name|problemsTable
operator|.
name|getSelectedRow
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|ValidationDisplayHandler
operator|.
name|getErrorMsg
argument_list|(
name|obj
argument_list|)
operator|.
name|displayField
argument_list|(
name|getMediator
argument_list|()
argument_list|,
name|super
operator|.
name|getParentEditor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|ValidatorTableModel
extends|extends
name|AbstractTableModel
block|{
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|validationObjects
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|1
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
return|return
name|validationObjects
operator|.
name|get
argument_list|(
name|row
argument_list|)
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
name|col
parameter_list|)
block|{
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
return|return
literal|" "
return|;
block|}
specifier|public
name|Class
name|getColumnClass
parameter_list|(
name|int
name|columnIndex
parameter_list|)
block|{
return|return
name|ValidationFailure
operator|.
name|class
return|;
block|}
block|}
comment|// a renderer for the error message
class|class
name|ValidationRenderer
extends|extends
name|DefaultTableCellRenderer
block|{
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
name|boolean
name|error
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|ValidationFailure
name|info
init|=
operator|(
name|ValidationFailure
operator|)
name|value
decl_stmt|;
name|value
operator|=
name|info
operator|.
name|getDescription
argument_list|()
expr_stmt|;
name|setToolTipText
argument_list|(
name|info
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setBackground
argument_list|(
name|error
condition|?
name|ERROR_COLOR
else|:
name|WARNING_COLOR
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
block|}
block|}
end_class

end_unit

