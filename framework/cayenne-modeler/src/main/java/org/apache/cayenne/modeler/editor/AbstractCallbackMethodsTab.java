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
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
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
name|LifecycleListener
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
name|*
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
name|AbstractRemoveCallbackMethodAction
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
name|CreateCallbackMethodAction
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
name|RemoveCallbackMethodAction
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
name|StringTransferHandler
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
name|*
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
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
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
name|java
operator|.
name|awt
operator|.
name|*
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
name|ItemEvent
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
name|ItemListener
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
name|EventObject
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

begin_comment
comment|/**  * Base abstract class for all calback methids editing tabs  * Contains logic for callback methods displaying, creating, removing, esiting, reordering  *  * @author Vasil Tarasevich  * @version 1.0 Oct 23, 2007  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCallbackMethodsTab
extends|extends
name|JPanel
implements|implements
name|ExistingSelectionProcessor
implements|,
name|CallbackMethodListener
implements|,
name|CallbackTypeSelectionListener
block|{
comment|/**      * mediator instance      */
name|ProjectController
name|mediator
decl_stmt|;
comment|/**      * toolbar for actions      */
specifier|protected
name|JToolBar
name|toolBar
decl_stmt|;
comment|/**      * table for displaying callback method names      */
specifier|protected
name|CayenneTable
name|table
decl_stmt|;
comment|/**      * Dropdown for callback type selection.      * Contains fixed list of 7 callback types.      */
specifier|protected
name|JComboBox
name|callbackTypeCombo
init|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|PRE_PERSIST
argument_list|,
literal|"pre-persist"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|POST_PERSIST
argument_list|,
literal|"post-persist"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|PRE_UPDATE
argument_list|,
literal|"pre-update"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|POST_UPDATE
argument_list|,
literal|"post-update"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|PRE_REMOVE
argument_list|,
literal|"pre-remove"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|POST_REMOVE
argument_list|,
literal|"post-remove"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleListener
operator|.
name|POST_LOAD
argument_list|,
literal|"post-load"
argument_list|)
block|,             }
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/**      * constructor      * @param mediator mediator instance      */
specifier|public
name|AbstractCallbackMethodsTab
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
comment|/**      * @return CallbackMap with callback methods      */
specifier|protected
specifier|abstract
name|CallbackMap
name|getCallbackMap
parameter_list|()
function_decl|;
comment|/**      * creates filter pane for filtering callback methods list      * adds callback method type dropdown      * @param builder forms builder      */
specifier|protected
name|void
name|buildFilter
parameter_list|(
name|DefaultFormBuilder
name|builder
parameter_list|)
block|{
name|JLabel
name|callbacktypeLabel
init|=
operator|new
name|JLabel
argument_list|(
literal|"Callback type:"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|callbacktypeLabel
argument_list|,
name|callbackTypeCombo
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return create callback method action      */
specifier|protected
name|CayenneAction
name|getCreateCallbackMethodAction
parameter_list|()
block|{
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
return|return
name|app
operator|.
name|getAction
argument_list|(
name|CreateCallbackMethodAction
operator|.
name|ACTION_NAME
argument_list|)
return|;
block|}
comment|/**      * @return remove callback method action      */
specifier|protected
name|AbstractRemoveCallbackMethodAction
name|getRemoveCallbackMethodAction
parameter_list|()
block|{
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
return|return
operator|(
name|AbstractRemoveCallbackMethodAction
operator|)
name|app
operator|.
name|getAction
argument_list|(
name|RemoveCallbackMethodAction
operator|.
name|ACTION_NAME
argument_list|)
return|;
block|}
comment|/**      * GUI components initialization      */
specifier|protected
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
name|toolBar
operator|=
operator|new
name|JToolBar
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getCreateCallbackMethodAction
argument_list|()
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getRemoveCallbackMethodAction
argument_list|()
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
name|JPanel
name|auxPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|auxPanel
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|auxPanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|callbackTypeCombo
operator|.
name|addItemListener
argument_list|(
operator|new
name|ItemListener
argument_list|()
block|{
specifier|public
name|void
name|itemStateChanged
parameter_list|(
name|ItemEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|SELECTED
condition|)
block|{
name|mediator
operator|.
name|fireCallbackTypeSelectionEvent
argument_list|(
operator|new
name|CallbackTypeSelectionEvent
argument_list|(
name|e
operator|.
name|getSource
argument_list|()
argument_list|,
operator|(
name|CallbackType
operator|)
name|callbackTypeCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|FormLayout
name|formLayout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:70dlu, 3dlu, fill:150dlu"
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|formLayout
argument_list|)
decl_stmt|;
name|buildFilter
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|auxPanel
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
name|StringRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getTableHeader
argument_list|()
operator|.
name|setReorderingAllowed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|//drag-and-drop initialization
name|table
operator|.
name|setDragEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|table
operator|.
name|setTransferHandler
argument_list|(
operator|new
name|StringTransferHandler
argument_list|()
block|{
specifier|protected
name|String
name|exportString
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
name|JTable
name|table
init|=
operator|(
name|JTable
operator|)
name|c
decl_stmt|;
name|int
name|rowIndex
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
name|String
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|rowIndex
operator|>=
literal|0
operator|&&
name|rowIndex
operator|<
name|table
operator|.
name|getModel
argument_list|()
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
name|result
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|table
operator|.
name|getModel
argument_list|()
operator|.
name|getValueAt
argument_list|(
name|rowIndex
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|void
name|importString
parameter_list|(
name|JComponent
name|c
parameter_list|,
name|String
name|callbackMethod
parameter_list|)
block|{
name|JTable
name|table
init|=
operator|(
name|JTable
operator|)
name|c
decl_stmt|;
name|int
name|rowIndex
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
comment|//move callback method inside of model
name|CallbackDescriptor
name|callbackDescriptor
init|=
name|getCallbackMap
argument_list|()
operator|.
name|getCallbackDescriptor
argument_list|(
name|mediator
operator|.
name|getCurrentCallbackType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|setDirty
argument_list|(
name|callbackDescriptor
operator|.
name|moveMethod
argument_list|(
name|callbackMethod
argument_list|,
name|rowIndex
argument_list|)
argument_list|)
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|cleanup
parameter_list|(
name|JComponent
name|c
parameter_list|,
name|boolean
name|remove
parameter_list|)
block|{
comment|//System.out.println("c");
block|}
block|}
argument_list|)
expr_stmt|;
name|auxPanel
operator|.
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
name|add
argument_list|(
name|auxPanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
comment|/**      * listeners initialization      */
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addCallbackMethodListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addCallbackTypeSelectionListener
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
if|if
condition|(
operator|!
name|e
operator|.
name|getValueIsAdjusting
argument_list|()
condition|)
block|{
name|processExistingSelection
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * process table selection change      * @param e event      */
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
name|String
name|method
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
name|CallbackDescriptorTableModel
name|model
init|=
operator|(
name|CallbackDescriptorTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|method
operator|=
name|model
operator|.
name|getCallbackMethod
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
name|getRemoveCallbackMethodAction
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|table
operator|.
name|getSelectedRow
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|CallbackMethodDisplayEvent
name|ev
init|=
operator|new
name|CallbackMethodDisplayEvent
argument_list|(
name|e
operator|.
name|getSource
argument_list|()
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireCallbackMethodDisplayEvent
argument_list|(
name|ev
argument_list|)
expr_stmt|;
block|}
comment|/**      * rebuilds table content      */
specifier|protected
name|void
name|rebuildTable
parameter_list|()
block|{
comment|//System.out.println("RebuildTable: " + this);
name|CallbackType
name|callbackType
init|=
name|mediator
operator|.
name|getCurrentCallbackType
argument_list|()
decl_stmt|;
name|List
name|methods
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|CallbackDescriptor
name|descriptor
init|=
literal|null
decl_stmt|;
name|CallbackMap
name|callbackMap
init|=
name|getCallbackMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackMap
operator|!=
literal|null
operator|&&
name|callbackType
operator|!=
literal|null
condition|)
block|{
name|descriptor
operator|=
name|callbackMap
operator|.
name|getCallbackDescriptor
argument_list|(
name|callbackType
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|j
init|=
name|descriptor
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|j
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|j
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|CallbackDescriptorTableModel
name|model
init|=
operator|new
name|CallbackDescriptorTableModel
argument_list|(
name|mediator
argument_list|,
name|this
argument_list|,
name|methods
argument_list|,
name|descriptor
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
name|methodNameColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|CallbackDescriptorTableModel
operator|.
name|METHOD_NAME
argument_list|)
decl_stmt|;
name|methodNameColumn
operator|.
name|setMinWidth
argument_list|(
literal|424
argument_list|)
expr_stmt|;
block|}
comment|/**      * class for renderig string values      */
class|class
name|StringRenderer
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
return|return
name|this
return|;
block|}
block|}
comment|/**      * processes change of callback method      * @param e event      */
specifier|public
name|void
name|callbackMethodChanged
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|table
operator|.
name|select
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * processes addition of new callback method - rebuilds table      * @param e event      */
specifier|public
name|void
name|callbackMethodAdded
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|rebuildTable
argument_list|()
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * processes removal of a callback method - removes a row from GUI      * @param e event      */
specifier|public
name|void
name|callbackMethodRemoved
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|CallbackDescriptorTableModel
name|model
init|=
operator|(
name|CallbackDescriptorTableModel
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
name|getNewName
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|removeRow
argument_list|(
name|e
operator|.
name|getNewName
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
comment|/**      * rebuilds table according to the newly selected callback type       * @param e event      */
specifier|public
name|void
name|callbackTypeSelected
parameter_list|(
name|CallbackTypeSelectionEvent
name|e
parameter_list|)
block|{
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

