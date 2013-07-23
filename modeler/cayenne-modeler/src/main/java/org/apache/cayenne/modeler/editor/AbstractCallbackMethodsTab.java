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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|DataFlavor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|StringSelection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|Transferable
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
name|awt
operator|.
name|event
operator|.
name|MouseMotionListener
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
name|List
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
name|JButton
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
name|JToolBar
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
name|TransferHandler
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
name|event
operator|.
name|TableColumnModelEvent
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
name|TableColumnModelListener
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
name|JTableHeader
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
name|CallbackDescriptor
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
name|CallbackMap
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
name|LifecycleEvent
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
name|event
operator|.
name|CallbackMethodEvent
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
name|CallbackMethodListener
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
name|ModelerUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * Base abstract class for all calback methids editing tabs Contains logic for callback  * methods displaying, creating, removing, esiting, reordering  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCallbackMethodsTab
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AbstractCallbackMethodsTab
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * mediator instance      */
name|ProjectController
name|mediator
decl_stmt|;
comment|/**      * toolbar for actions      */
specifier|protected
name|JToolBar
name|toolBar
decl_stmt|;
comment|/**      * panel for displaying callback method tables      */
specifier|protected
name|JPanel
name|auxPanel
decl_stmt|;
comment|/**      * preferences for the callback methods table      */
specifier|protected
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
specifier|protected
name|CallbackType
index|[]
name|callbackTypes
init|=
block|{
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|,
literal|"PostAdd"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
literal|"PrePersist"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|,
literal|"PostPersist"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
literal|"PreUpdate"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|,
literal|"PostUpdate"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_REMOVE
argument_list|,
literal|"PreRemove"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|,
literal|"PostRemove"
argument_list|)
block|,
operator|new
name|CallbackType
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
literal|"PostLoad"
argument_list|)
block|,                     }
decl_stmt|;
specifier|protected
name|CayenneTable
index|[]
name|tables
init|=
operator|new
name|CayenneTable
index|[
name|callbackTypes
operator|.
name|length
index|]
decl_stmt|;
comment|/**      * constructor      *       * @param mediator mediator instance      */
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
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CreateCallbackMethodAction
operator|.
name|class
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
name|app
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveCallbackMethodAction
operator|.
name|class
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
name|auxPanel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
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
name|createTables
argument_list|()
expr_stmt|;
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|auxPanel
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inits the {@link TableColumnPreferences} object according to callback table name.      */
specifier|protected
specifier|abstract
name|void
name|initTablePreferences
parameter_list|()
function_decl|;
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
operator|new
name|CallbackMethodListener
argument_list|()
block|{
specifier|public
name|void
name|callbackMethodChanged
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|rebuildTables
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|callbackMethodAdded
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|rebuildTables
argument_list|()
expr_stmt|;
name|selectAdded
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|callbackMethodRemoved
parameter_list|(
name|CallbackMethodEvent
name|e
parameter_list|)
block|{
name|rebuildTables
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * rebuilds table content      */
specifier|protected
name|void
name|rebuildTables
parameter_list|()
block|{
name|CallbackMap
name|callbackMap
init|=
name|getCallbackMap
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
name|callbackTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|CallbackType
name|callbackType
init|=
name|callbackTypes
index|[
name|i
index|]
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|CallbackDescriptor
name|descriptor
init|=
literal|null
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
name|String
name|callbackMethod
range|:
name|descriptor
operator|.
name|getCallbackMethods
argument_list|()
control|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|callbackMethod
argument_list|)
expr_stmt|;
block|}
block|}
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
argument_list|,
name|callbackType
argument_list|)
decl_stmt|;
name|tables
index|[
name|i
index|]
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tables
operator|.
name|length
condition|;
name|i
operator|++
control|)
name|tablePreferences
operator|.
name|bind
argument_list|(
name|tables
index|[
name|i
index|]
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTables
parameter_list|()
block|{
name|FormLayout
name|formLayout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"left:pref"
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
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|CallbackType
name|callbackType
range|:
name|callbackTypes
control|)
block|{
name|tables
index|[
name|index
index|]
operator|=
name|createTable
argument_list|(
name|callbackType
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|createTablePanel
argument_list|(
name|tables
index|[
name|index
operator|++
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|initTablePreferences
argument_list|()
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
name|CENTER
argument_list|)
expr_stmt|;
name|validate
argument_list|()
expr_stmt|;
block|}
specifier|private
name|CayenneTable
name|createTable
parameter_list|(
name|CallbackType
name|callbackType
parameter_list|)
block|{
specifier|final
name|CayenneTable
name|cayenneTable
init|=
operator|new
name|CayenneTable
argument_list|()
decl_stmt|;
comment|// drag-and-drop initialization
name|cayenneTable
operator|.
name|setDragEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|setRowHeight
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|MULTIPLE_INTERVAL_SELECTION
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|setAutoResizeMode
argument_list|(
name|JTable
operator|.
name|AUTO_RESIZE_OFF
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|setTransferHandler
argument_list|(
operator|new
name|CallbackImportableHandler
argument_list|(
name|cayenneTable
argument_list|)
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|addListSelectionListener
argument_list|(
operator|new
name|CallbackListSelectionListener
argument_list|(
name|cayenneTable
argument_list|)
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|getColumnModel
argument_list|()
operator|.
name|addColumnModelListener
argument_list|(
operator|new
name|CallbackTableColumnModelListener
argument_list|(
name|cayenneTable
argument_list|)
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|getTableHeader
argument_list|()
operator|.
name|addMouseListener
argument_list|(
operator|new
name|CallbackMouseAdapter
argument_list|(
name|cayenneTable
argument_list|)
argument_list|)
expr_stmt|;
name|cayenneTable
operator|.
name|getTableHeader
argument_list|()
operator|.
name|addMouseMotionListener
argument_list|(
operator|new
name|CallbackMouseMotionListener
argument_list|(
name|cayenneTable
argument_list|)
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
name|getRemoveCallbackMethodAction
argument_list|()
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|TablePopupHandler
operator|.
name|install
argument_list|(
name|cayenneTable
argument_list|,
name|popup
argument_list|)
expr_stmt|;
name|addButtonAtHeader
argument_list|(
name|cayenneTable
argument_list|,
name|getCreateCallbackMethodAction
argument_list|()
operator|.
name|buildButton
argument_list|()
argument_list|,
operator|new
name|ButtonListener
argument_list|(
name|callbackType
argument_list|)
argument_list|,
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-create-method.gif"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cayenneTable
return|;
block|}
specifier|private
name|JPanel
name|createTablePanel
parameter_list|(
specifier|final
name|CayenneTable
name|cayenneTable
parameter_list|)
block|{
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|panel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|cayenneTable
operator|.
name|getTableHeader
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|cayenneTable
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
specifier|private
name|void
name|addButtonAtHeader
parameter_list|(
name|JTable
name|table
parameter_list|,
name|JButton
name|button
parameter_list|,
name|ActionListener
name|buttonListener
parameter_list|,
name|ImageIcon
name|buttonIcon
parameter_list|)
block|{
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"left:10dlu, 2dlu"
argument_list|,
literal|"center:10dlu"
argument_list|)
argument_list|)
decl_stmt|;
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|button
operator|.
name|setIcon
argument_list|(
name|buttonIcon
argument_list|)
expr_stmt|;
name|button
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setBorderPainted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setContentAreaFilled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|addActionListener
argument_list|(
name|buttonListener
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|button
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
name|JPanel
name|buttonPanel
init|=
name|builder
operator|.
name|getPanel
argument_list|()
decl_stmt|;
name|buttonPanel
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|JTableHeader
name|header
init|=
name|table
operator|.
name|getTableHeader
argument_list|()
decl_stmt|;
name|header
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|header
operator|.
name|setReorderingAllowed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|header
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|150
argument_list|,
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|header
operator|.
name|add
argument_list|(
name|buttonPanel
argument_list|,
name|BorderLayout
operator|.
name|EAST
argument_list|)
expr_stmt|;
block|}
class|class
name|ButtonListener
implements|implements
name|ActionListener
block|{
specifier|private
name|CallbackType
name|callbackType
decl_stmt|;
specifier|public
name|ButtonListener
parameter_list|(
name|CallbackType
name|callbackType
parameter_list|)
block|{
name|this
operator|.
name|callbackType
operator|=
name|callbackType
expr_stmt|;
block|}
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|mediator
operator|.
name|setCurrentCallbackType
argument_list|(
name|callbackType
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|CallbackType
name|getSelectedCallbackType
parameter_list|()
block|{
return|return
name|mediator
operator|.
name|getCurrentCallbackType
argument_list|()
return|;
block|}
specifier|private
name|void
name|selectAdded
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbackTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbackTypes
index|[
name|i
index|]
operator|==
name|mediator
operator|.
name|getCurrentCallbackType
argument_list|()
condition|)
block|{
if|if
condition|(
name|tables
index|[
name|i
index|]
operator|.
name|editCellAt
argument_list|(
name|tables
index|[
name|i
index|]
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|,
name|CallbackDescriptorTableModel
operator|.
name|METHOD_NAME
argument_list|)
operator|&&
name|tables
index|[
name|i
index|]
operator|.
name|getEditorComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tables
index|[
name|i
index|]
operator|.
name|getEditorComponent
argument_list|()
operator|.
name|requestFocus
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
class|class
name|CallbackImportableHandler
extends|extends
name|TransferHandler
block|{
specifier|private
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|CallbackImportableHandler
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
specifier|protected
name|Transferable
name|createTransferable
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
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
name|CallbackDescriptorTableModel
operator|.
name|METHOD_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|StringSelection
argument_list|(
name|result
argument_list|)
return|;
block|}
specifier|public
name|int
name|getSourceActions
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
return|return
name|COPY_OR_MOVE
return|;
block|}
specifier|public
name|boolean
name|importData
parameter_list|(
name|JComponent
name|comp
parameter_list|,
name|Transferable
name|t
parameter_list|)
block|{
if|if
condition|(
name|canImport
argument_list|(
name|comp
argument_list|,
name|t
operator|.
name|getTransferDataFlavors
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|callbackMethod
decl_stmt|;
try|try
block|{
name|callbackMethod
operator|=
operator|(
name|String
operator|)
name|t
operator|.
name|getTransferData
argument_list|(
name|DataFlavor
operator|.
name|stringFlavor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error transferring"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|int
name|rowIndex
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
name|CallbackDescriptor
name|callbackDescriptor
init|=
operator|(
operator|(
name|CallbackDescriptorTableModel
operator|)
name|table
operator|.
name|getCayenneModel
argument_list|()
operator|)
operator|.
name|getCallbackDescriptor
argument_list|()
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
name|rebuildTables
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|canImport
parameter_list|(
name|JComponent
name|comp
parameter_list|,
name|DataFlavor
index|[]
name|transferFlavors
parameter_list|)
block|{
for|for
control|(
name|DataFlavor
name|flavor
range|:
name|transferFlavors
control|)
block|{
if|if
condition|(
name|DataFlavor
operator|.
name|stringFlavor
operator|.
name|equals
argument_list|(
name|flavor
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|protected
class|class
name|CallbackListSelectionListener
implements|implements
name|ListSelectionListener
block|{
specifier|private
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|CallbackListSelectionListener
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
name|String
index|[]
name|methods
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tables
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|tables
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|table
argument_list|)
condition|)
block|{
name|tables
index|[
name|i
index|]
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|removeSelectionInterval
argument_list|(
literal|0
argument_list|,
name|tables
index|[
name|i
index|]
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|tables
index|[
name|i
index|]
operator|.
name|getCellEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tables
index|[
name|i
index|]
operator|.
name|getCellEditor
argument_list|()
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|table
operator|.
name|getSelectedRow
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
index|[]
name|sel
init|=
name|table
operator|.
name|getSelectedRows
argument_list|()
decl_stmt|;
name|methods
operator|=
operator|new
name|String
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
name|methods
index|[
name|i
index|]
operator|=
operator|(
name|String
operator|)
name|table
operator|.
name|getValueAt
argument_list|(
name|sel
index|[
name|i
index|]
argument_list|,
name|table
operator|.
name|convertColumnIndexToView
argument_list|(
name|CallbackDescriptorTableModel
operator|.
name|METHOD_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|LifecycleEvent
name|currentType
init|=
operator|(
operator|(
name|CallbackDescriptorTableModel
operator|)
name|table
operator|.
name|getCayenneModel
argument_list|()
operator|)
operator|.
name|getCallbackDescriptor
argument_list|()
operator|.
name|getCallbackType
argument_list|()
decl_stmt|;
for|for
control|(
name|CallbackType
name|callbackType
range|:
name|callbackTypes
control|)
block|{
if|if
condition|(
name|callbackType
operator|.
name|getType
argument_list|()
operator|==
name|currentType
condition|)
block|{
name|mediator
operator|.
name|setCurrentCallbackType
argument_list|(
name|callbackType
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|mediator
operator|.
name|setCurrentCallbackMethods
argument_list|(
name|methods
argument_list|)
expr_stmt|;
name|getRemoveCallbackMethodAction
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|methods
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|getRemoveCallbackMethodAction
argument_list|()
operator|.
name|setName
argument_list|(
name|getRemoveCallbackMethodAction
argument_list|()
operator|.
name|getActionName
argument_list|(
name|methods
operator|.
name|length
operator|>
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
class|class
name|CallbackTableColumnModelListener
implements|implements
name|TableColumnModelListener
block|{
specifier|private
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|CallbackTableColumnModelListener
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
specifier|public
name|void
name|columnMarginChanged
parameter_list|(
name|ChangeEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|table
operator|.
name|getColumnWidthChanged
argument_list|()
condition|)
block|{
if|if
condition|(
name|table
operator|.
name|getTableHeader
argument_list|()
operator|.
name|getResizingColumn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setColumnWidthChanged
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|columnMoved
parameter_list|(
name|TableColumnModelEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|columnAdded
parameter_list|(
name|TableColumnModelEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|columnRemoved
parameter_list|(
name|TableColumnModelEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|columnSelectionChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
block|}
block|}
specifier|private
class|class
name|CallbackMouseAdapter
extends|extends
name|MouseAdapter
block|{
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|CallbackMouseAdapter
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
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|table
operator|.
name|getColumnWidthChanged
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tables
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|tables
index|[
name|i
index|]
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
literal|0
argument_list|)
operator|.
name|setPreferredWidth
argument_list|(
name|table
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initTablePreferences
argument_list|()
expr_stmt|;
name|table
operator|.
name|setColumnWidthChanged
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|CallbackMouseMotionListener
implements|implements
name|MouseMotionListener
block|{
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|CallbackMouseMotionListener
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
specifier|public
name|void
name|mouseMoved
parameter_list|(
name|MouseEvent
name|arg0
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseDragged
parameter_list|(
name|MouseEvent
name|arg0
parameter_list|)
block|{
if|if
condition|(
name|table
operator|.
name|getColumnWidthChanged
argument_list|()
condition|)
block|{
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
name|tables
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|table
operator|.
name|equals
argument_list|(
name|tables
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|tables
index|[
name|i
index|]
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
literal|0
argument_list|)
operator|.
name|setPreferredWidth
argument_list|(
name|table
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

