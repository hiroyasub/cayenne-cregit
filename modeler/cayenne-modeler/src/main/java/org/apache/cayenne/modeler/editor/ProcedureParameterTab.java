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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|event
operator|.
name|ProcedureEvent
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
name|configuration
operator|.
name|event
operator|.
name|ProcedureParameterEvent
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
name|configuration
operator|.
name|event
operator|.
name|ProcedureParameterListener
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
name|Procedure
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
name|ProcedureParameter
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
name|CopyProcedureParameterAction
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
name|CreateProcedureParameterAction
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
name|CutProcedureParameterAction
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
name|RemoveProcedureParameterAction
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
name|ProcedureDisplayEvent
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
name|ProcedureDisplayListener
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
name|ProcedureParameterDisplayEvent
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
name|CayenneCellEditor
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
name|JToolBar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingConstants
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
name|BorderLayout
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
name|EventObject
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

begin_class
specifier|public
class|class
name|ProcedureParameterTab
extends|extends
name|JPanel
implements|implements
name|ProcedureParameterListener
implements|,
name|ProcedureDisplayListener
implements|,
name|ExistingSelectionProcessor
implements|,
name|ActionListener
block|{
specifier|protected
name|ProjectController
name|eventController
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
name|removeParameterButton
decl_stmt|;
specifier|protected
name|JButton
name|moveUp
decl_stmt|;
specifier|protected
name|JButton
name|moveDown
decl_stmt|;
comment|/**      * By now popup menu items are made similar to toolbar button. (i.e. all      * functionality is here) This should be probably refactored as Action.      */
specifier|protected
name|JMenuItem
name|removeParameterMenu
decl_stmt|;
specifier|protected
name|JMenuItem
name|moveUpMenu
decl_stmt|;
specifier|protected
name|JMenuItem
name|moveDownMenu
decl_stmt|;
specifier|public
name|ProcedureParameterTab
parameter_list|(
name|ProjectController
name|eventController
parameter_list|)
block|{
name|this
operator|.
name|eventController
operator|=
name|eventController
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
name|eventController
operator|.
name|addProcedureDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addProcedureParameterListener
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
name|moveDown
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|moveUp
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|moveDownMenu
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|moveUpMenu
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
block|{
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
name|toolBar
operator|.
name|setFloatable
argument_list|(
literal|false
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
name|toolBar
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateProcedureParameterAction
operator|.
name|class
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|removeParameterButton
operator|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveProcedureParameterAction
operator|.
name|class
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|removeParameterButton
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|Icon
name|up
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-up.png"
argument_list|)
decl_stmt|;
name|Icon
name|down
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-down.png"
argument_list|)
decl_stmt|;
name|moveUp
operator|=
operator|new
name|CayenneAction
operator|.
name|CayenneToolbarButton
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|moveUp
operator|.
name|setIcon
argument_list|(
name|up
argument_list|)
expr_stmt|;
name|moveUp
operator|.
name|setDisabledIcon
argument_list|(
name|FilteredIconFactory
operator|.
name|createDisabledIcon
argument_list|(
name|up
argument_list|)
argument_list|)
expr_stmt|;
name|moveUp
operator|.
name|setToolTipText
argument_list|(
literal|"Move Parameter Up"
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|moveUp
argument_list|)
expr_stmt|;
name|moveDown
operator|=
operator|new
name|CayenneAction
operator|.
name|CayenneToolbarButton
argument_list|(
literal|null
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|moveDown
operator|.
name|setIcon
argument_list|(
name|down
argument_list|)
expr_stmt|;
name|moveDown
operator|.
name|setDisabledIcon
argument_list|(
name|FilteredIconFactory
operator|.
name|createDisabledIcon
argument_list|(
name|down
argument_list|)
argument_list|)
expr_stmt|;
name|moveDown
operator|.
name|setToolTipText
argument_list|(
literal|"Move Parameter Down"
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|moveDown
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
name|actionManager
operator|.
name|getAction
argument_list|(
name|CutProcedureParameterAction
operator|.
name|class
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|CopyProcedureParameterAction
operator|.
name|class
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|toolBar
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
name|buildButton
argument_list|(
literal|3
argument_list|)
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
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"procedure/parameterTable"
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
name|removeParameterMenu
operator|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveProcedureParameterAction
operator|.
name|class
argument_list|)
operator|.
name|buildMenu
argument_list|()
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|removeParameterMenu
argument_list|)
expr_stmt|;
name|popup
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|moveUpMenu
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Move Parameter Up"
argument_list|,
name|up
argument_list|)
expr_stmt|;
name|moveDownMenu
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Move Parameter Down"
argument_list|,
name|down
argument_list|)
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|moveUpMenu
argument_list|)
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|moveDownMenu
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
name|CutProcedureParameterAction
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
name|CopyProcedureParameterAction
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
name|actionManager
operator|.
name|setupCutCopyPaste
argument_list|(
name|table
argument_list|,
name|CutProcedureParameterAction
operator|.
name|class
argument_list|,
name|CopyProcedureParameterAction
operator|.
name|class
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
name|ProcedureParameter
index|[]
name|parameters
init|=
operator|new
name|ProcedureParameter
index|[
literal|0
index|]
decl_stmt|;
name|boolean
name|enableUp
init|=
literal|false
decl_stmt|;
name|boolean
name|enableDown
init|=
literal|false
decl_stmt|;
name|boolean
name|enableRemoveButton
init|=
literal|false
decl_stmt|;
name|int
name|selectedRow
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedRow
operator|>=
literal|0
condition|)
block|{
name|enableRemoveButton
operator|=
literal|true
expr_stmt|;
name|ProcedureParameterTableModel
name|model
init|=
operator|(
name|ProcedureParameterTableModel
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
name|parameters
operator|=
operator|new
name|ProcedureParameter
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
name|parameters
index|[
name|i
index|]
operator|=
name|model
operator|.
name|getParameter
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
name|int
name|rowCount
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|rowCount
operator|>
literal|1
condition|)
block|{
if|if
condition|(
name|selectedRow
operator|>
literal|0
condition|)
block|{
name|enableUp
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|selectedRow
operator|<
operator|(
name|rowCount
operator|-
literal|1
operator|)
condition|)
block|{
name|enableDown
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
name|removeParameterButton
operator|.
name|setEnabled
argument_list|(
name|enableRemoveButton
argument_list|)
expr_stmt|;
name|moveUp
operator|.
name|setEnabled
argument_list|(
name|enableUp
argument_list|)
expr_stmt|;
name|moveDown
operator|.
name|setEnabled
argument_list|(
name|enableDown
argument_list|)
expr_stmt|;
name|syncButtons
argument_list|()
expr_stmt|;
name|ProcedureParameterDisplayEvent
name|ppde
init|=
operator|new
name|ProcedureParameterDisplayEvent
argument_list|(
name|this
argument_list|,
name|parameters
argument_list|,
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
argument_list|,
name|eventController
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|eventController
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
name|eventController
operator|.
name|fireProcedureParameterDisplayEvent
argument_list|(
name|ppde
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invoked when currently selected Procedure object is changed.      */
specifier|public
name|void
name|currentProcedureChanged
parameter_list|(
name|ProcedureDisplayEvent
name|e
parameter_list|)
block|{
name|Procedure
name|procedure
init|=
name|e
operator|.
name|getProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|!=
literal|null
operator|&&
name|e
operator|.
name|isProcedureChanged
argument_list|()
condition|)
block|{
name|rebuildTable
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Selects a specified parameters.      */
specifier|public
name|void
name|selectParameters
parameter_list|(
name|ProcedureParameter
index|[]
name|parameters
parameter_list|)
block|{
name|ModelerUtil
operator|.
name|updateActions
argument_list|(
name|parameters
operator|.
name|length
argument_list|,
name|RemoveProcedureParameterAction
operator|.
name|class
argument_list|,
name|CutProcedureParameterAction
operator|.
name|class
argument_list|,
name|CopyProcedureParameterAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|ProcedureParameterTableModel
name|model
init|=
operator|(
name|ProcedureParameterTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcedureParameter
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
name|parameters
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
name|parameters
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
name|parameters
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
specifier|protected
name|void
name|rebuildTable
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
name|ProcedureParameterTableModel
name|model
init|=
operator|new
name|ProcedureParameterTableModel
argument_list|(
name|procedure
argument_list|,
name|eventController
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
comment|// number column tweaking
name|TableColumn
name|numberColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_NUMBER
argument_list|)
decl_stmt|;
name|DefaultTableCellRenderer
name|renderer
init|=
operator|new
name|DefaultTableCellRenderer
argument_list|()
decl_stmt|;
name|renderer
operator|.
name|setHorizontalAlignment
argument_list|(
name|SwingConstants
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|numberColumn
operator|.
name|setCellRenderer
argument_list|(
name|renderer
argument_list|)
expr_stmt|;
name|TableColumn
name|typesColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_TYPE
argument_list|)
decl_stmt|;
name|JComboBox
name|typesEditor
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|TypesMapping
operator|.
name|getDatabaseTypes
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|typesEditor
argument_list|)
expr_stmt|;
name|typesColumn
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
name|typesEditor
argument_list|)
argument_list|)
expr_stmt|;
comment|// direction column tweaking
name|TableColumn
name|directionColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_DIRECTION
argument_list|)
decl_stmt|;
name|JComboBox
name|directionEditor
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_DIRECTION_NAMES
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|directionEditor
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|directionColumn
operator|.
name|setCellEditor
argument_list|(
operator|new
name|CayenneCellEditor
argument_list|(
name|directionEditor
argument_list|)
argument_list|)
expr_stmt|;
name|TableColumn
name|precisionColumn
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_PRECISION
argument_list|)
decl_stmt|;
name|LimitedTextField
name|limitedPrecisionField
init|=
operator|new
name|LimitedTextField
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|precisionColumn
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
name|limitedPrecisionField
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
name|ProcedureParameterTableModel
operator|.
name|PARAMETER_LENGTH
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
name|moveUp
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moveDown
operator|.
name|setEnabled
argument_list|(
literal|false
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
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|procedureParameterAdded
parameter_list|(
name|ProcedureParameterEvent
name|e
parameter_list|)
block|{
name|rebuildTable
argument_list|(
name|e
operator|.
name|getParameter
argument_list|()
operator|.
name|getProcedure
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getParameter
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|procedureParameterChanged
parameter_list|(
name|ProcedureParameterEvent
name|e
parameter_list|)
block|{
name|table
operator|.
name|select
argument_list|(
name|e
operator|.
name|getParameter
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|procedureParameterRemoved
parameter_list|(
name|ProcedureParameterEvent
name|e
parameter_list|)
block|{
name|ProcedureParameterTableModel
name|model
init|=
operator|(
name|ProcedureParameterTableModel
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
name|getParameter
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|removeRow
argument_list|(
name|e
operator|.
name|getParameter
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
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ProcedureParameterTableModel
name|model
init|=
operator|(
name|ProcedureParameterTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|ProcedureParameter
name|parameter
init|=
name|model
operator|.
name|getParameter
argument_list|(
name|table
operator|.
name|getSelectedRow
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|moveUp
operator|||
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|moveUpMenu
condition|)
block|{
name|index
operator|=
name|model
operator|.
name|moveRowUp
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|moveDown
operator|||
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|moveDownMenu
condition|)
block|{
name|index
operator|=
name|model
operator|.
name|moveRowDown
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|table
operator|.
name|select
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// note that 'setCallParameters' is donw by copy internally
name|parameter
operator|.
name|getProcedure
argument_list|()
operator|.
name|setCallParameters
argument_list|(
name|model
operator|.
name|getObjectList
argument_list|()
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|this
argument_list|,
name|parameter
operator|.
name|getProcedure
argument_list|()
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Synchronizes state of toolbar and popup menu buttons      */
specifier|private
name|void
name|syncButtons
parameter_list|()
block|{
name|removeParameterMenu
operator|.
name|setEnabled
argument_list|(
name|removeParameterButton
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|moveUpMenu
operator|.
name|setEnabled
argument_list|(
name|moveUp
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|moveDownMenu
operator|.
name|setEnabled
argument_list|(
name|moveDown
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

