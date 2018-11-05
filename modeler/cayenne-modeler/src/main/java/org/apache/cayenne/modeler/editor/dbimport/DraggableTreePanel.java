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
name|dbimport
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeColumn
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
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeProcedure
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
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeTable
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
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeProcedure
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
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeTable
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
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|reverse
operator|.
name|dbimport
operator|.
name|Schema
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
name|dbimport
operator|.
name|AddCatalogAction
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
name|dbimport
operator|.
name|AddExcludeColumnAction
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
name|dbimport
operator|.
name|AddExcludeProcedureAction
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
name|dbimport
operator|.
name|AddExcludeTableAction
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
name|dbimport
operator|.
name|AddIncludeColumnAction
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
name|dbimport
operator|.
name|AddIncludeProcedureAction
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
name|dbimport
operator|.
name|AddIncludeTableAction
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
name|dbimport
operator|.
name|AddSchemaAction
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
name|dbimport
operator|.
name|MoveImportNodeAction
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
name|dbimport
operator|.
name|MoveInvertNodeAction
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
name|dbimport
operator|.
name|TreeManipulationAction
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
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|db
operator|.
name|load
operator|.
name|TransferableNode
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
name|javax
operator|.
name|swing
operator|.
name|DropMode
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTree
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
name|TreeSelectionEvent
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
name|TreeSelectionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultTreeCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|Transferable
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
name|UnsupportedFlavorException
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
name|KeyEvent
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
name|KeyListener
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
name|io
operator|.
name|IOException
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
name|HashMap
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DraggableTreePanel
extends|extends
name|JScrollPane
block|{
specifier|private
specifier|static
specifier|final
name|int
name|ROOT_LEVEL
init|=
literal|14
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FIRST_LEVEL
init|=
literal|11
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|SECOND_LEVEL
init|=
literal|8
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|THIRD_LEVEL
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FOURTH_LEVEL
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FIFTH_LEVEL
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MOVE_BUTTON_LABEL
init|=
literal|"Include"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MOVE_INV_BUTTON_LABEL
init|=
literal|"Exclude"
decl_stmt|;
specifier|private
name|DbImportTree
name|sourceTree
decl_stmt|;
specifier|private
name|DbImportTree
name|targetTree
decl_stmt|;
specifier|private
name|CayenneAction
operator|.
name|CayenneToolbarButton
name|moveButton
decl_stmt|;
specifier|private
name|CayenneAction
operator|.
name|CayenneToolbarButton
name|moveInvertButton
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|DataMap
argument_list|,
name|ReverseEngineering
argument_list|>
name|databaseStructures
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Integer
argument_list|>
name|levels
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|List
argument_list|<
name|Class
argument_list|>
argument_list|>
name|insertableLevels
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Class
argument_list|>
name|actions
decl_stmt|;
specifier|public
name|DraggableTreePanel
parameter_list|(
name|ProjectController
name|projectController
parameter_list|,
name|DbImportTree
name|sourceTree
parameter_list|,
name|DbImportTree
name|targetTree
parameter_list|)
block|{
name|super
argument_list|(
name|sourceTree
argument_list|)
expr_stmt|;
name|this
operator|.
name|targetTree
operator|=
name|targetTree
expr_stmt|;
name|this
operator|.
name|sourceTree
operator|=
name|sourceTree
expr_stmt|;
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|databaseStructures
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|initLevels
argument_list|()
expr_stmt|;
name|initElement
argument_list|()
expr_stmt|;
name|initActions
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initActions
parameter_list|()
block|{
name|actions
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
name|AddCatalogAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
name|AddSchemaAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
name|AddIncludeTableAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|,
name|AddExcludeTableAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|,
name|AddIncludeColumnAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|,
name|AddExcludeColumnAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|,
name|AddIncludeProcedureAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|,
name|AddExcludeProcedureAction
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updateTree
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|sourceTree
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|reload
argument_list|()
expr_stmt|;
if|if
condition|(
name|databaseStructures
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|sourceTree
operator|.
name|setReverseEngineering
argument_list|(
name|databaseStructures
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|translateReverseEngineeringToTree
argument_list|(
name|databaseStructures
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sourceTree
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|sourceTree
operator|.
name|addKeyListener
argument_list|(
operator|new
name|SourceTreeKeyListener
argument_list|()
argument_list|)
expr_stmt|;
name|targetTree
operator|.
name|addKeyListener
argument_list|(
operator|new
name|TargetTreeKeyListener
argument_list|()
argument_list|)
expr_stmt|;
name|targetTree
operator|.
name|addTreeSelectionListener
argument_list|(
operator|new
name|TargetTreeSelectionListener
argument_list|()
argument_list|)
expr_stmt|;
name|targetTree
operator|.
name|setTransferHandler
argument_list|(
operator|new
name|TargetTreeTransferHandler
argument_list|()
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|setTransferHandler
argument_list|(
operator|new
name|SourceTreeTransferHandler
argument_list|()
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|addTreeSelectionListener
argument_list|(
operator|new
name|SourceTreeSelectionListener
argument_list|()
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|addMouseListener
argument_list|(
operator|new
name|ResetFocusMouseAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|targetTree
operator|.
name|setDragEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|canBeInverted
parameter_list|()
block|{
if|if
condition|(
name|sourceTree
operator|.
name|getSelectionPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DbImportTreeNode
name|selectedElement
init|=
name|sourceTree
operator|.
name|getSelectedNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedElement
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|levels
operator|.
name|get
argument_list|(
name|selectedElement
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
operator|<
name|SECOND_LEVEL
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
specifier|private
name|void
name|initElement
parameter_list|()
block|{
name|sourceTree
operator|.
name|setDragEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|ColorTreeRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|sourceTree
operator|.
name|setDropMode
argument_list|(
name|DropMode
operator|.
name|INSERT
argument_list|)
expr_stmt|;
name|MoveImportNodeAction
name|action
init|=
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|MoveImportNodeAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|setPanel
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSourceTree
argument_list|(
name|sourceTree
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetTree
argument_list|(
name|targetTree
argument_list|)
expr_stmt|;
name|moveButton
operator|=
operator|(
name|CayenneAction
operator|.
name|CayenneToolbarButton
operator|)
name|action
operator|.
name|buildButton
argument_list|()
expr_stmt|;
name|moveButton
operator|.
name|setShowingText
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|moveButton
operator|.
name|setText
argument_list|(
name|MOVE_BUTTON_LABEL
argument_list|)
expr_stmt|;
name|MoveInvertNodeAction
name|actionInv
init|=
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|MoveInvertNodeAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|actionInv
operator|.
name|setPanel
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|actionInv
operator|.
name|setSourceTree
argument_list|(
name|sourceTree
argument_list|)
expr_stmt|;
name|actionInv
operator|.
name|setTargetTree
argument_list|(
name|targetTree
argument_list|)
expr_stmt|;
name|moveInvertButton
operator|=
operator|(
name|CayenneAction
operator|.
name|CayenneToolbarButton
operator|)
name|actionInv
operator|.
name|buildButton
argument_list|()
expr_stmt|;
name|moveInvertButton
operator|.
name|setShowingText
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|moveInvertButton
operator|.
name|setText
argument_list|(
name|MOVE_INV_BUTTON_LABEL
argument_list|)
expr_stmt|;
name|DefaultTreeCellRenderer
name|renderer
init|=
operator|(
name|DefaultTreeCellRenderer
operator|)
name|sourceTree
operator|.
name|getCellRenderer
argument_list|()
decl_stmt|;
name|renderer
operator|.
name|setLeafIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|renderer
operator|.
name|setClosedIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|renderer
operator|.
name|setOpenIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initLevels
parameter_list|()
block|{
name|levels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ReverseEngineering
operator|.
name|class
argument_list|,
name|ROOT_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
name|FIRST_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
name|SECOND_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
name|THIRD_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|,
name|FOURTH_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|,
name|FOURTH_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|,
name|FIFTH_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|,
name|FIFTH_LEVEL
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|,
name|FIFTH_LEVEL
argument_list|)
expr_stmt|;
name|insertableLevels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|rootLevelClasses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|Catalog
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|Schema
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|rootLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|catalogLevelClasses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|Schema
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalogLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|schemaLevelClasses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|includeTableLevelClasses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|includeTableLevelClasses
operator|.
name|add
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|includeTableLevelClasses
operator|.
name|add
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|insertableLevels
operator|.
name|put
argument_list|(
name|ReverseEngineering
operator|.
name|class
argument_list|,
name|rootLevelClasses
argument_list|)
expr_stmt|;
name|insertableLevels
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
name|catalogLevelClasses
argument_list|)
expr_stmt|;
name|insertableLevels
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
name|schemaLevelClasses
argument_list|)
expr_stmt|;
name|insertableLevels
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
name|includeTableLevelClasses
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|canBeMoved
parameter_list|()
block|{
if|if
condition|(
name|sourceTree
operator|.
name|getSelectionPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DbImportTreeNode
name|selectedElement
init|=
name|sourceTree
operator|.
name|getSelectedNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedElement
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|selectedElement
operator|.
name|isIncludeColumn
argument_list|()
operator|||
name|selectedElement
operator|.
name|isExcludeColumn
argument_list|()
condition|)
block|{
name|DbImportTreeNode
name|node
init|=
name|targetTree
operator|.
name|findNode
argument_list|(
name|targetTree
operator|.
name|getRootNode
argument_list|()
argument_list|,
operator|(
name|DbImportTreeNode
operator|)
name|selectedElement
operator|.
name|getParent
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
operator|&&
name|node
operator|.
name|isExcludeTable
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|Class
name|draggableElementClass
init|=
name|selectedElement
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Class
name|reverseEngineeringElementClass
decl_stmt|;
if|if
condition|(
name|targetTree
operator|.
name|getSelectionPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|selectedElement
operator|=
name|targetTree
operator|.
name|getSelectedNode
argument_list|()
expr_stmt|;
name|DbImportTreeNode
name|parent
init|=
operator|(
name|DbImportTreeNode
operator|)
name|selectedElement
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|reverseEngineeringElementClass
operator|=
name|parent
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|reverseEngineeringElementClass
operator|=
name|selectedElement
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|reverseEngineeringElementClass
operator|=
name|ReverseEngineering
operator|.
name|class
expr_stmt|;
block|}
name|List
argument_list|<
name|Class
argument_list|>
name|containsList
init|=
name|insertableLevels
operator|.
name|get
argument_list|(
name|reverseEngineeringElementClass
argument_list|)
decl_stmt|;
return|return
name|containsList
operator|.
name|contains
argument_list|(
name|draggableElementClass
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|JButton
name|getMoveButton
parameter_list|()
block|{
return|return
name|moveButton
return|;
block|}
specifier|public
name|JButton
name|getMoveInvertButton
parameter_list|()
block|{
return|return
name|moveInvertButton
return|;
block|}
specifier|public
name|TreeManipulationAction
name|getActionByNodeType
parameter_list|(
name|Class
name|nodeType
parameter_list|)
block|{
name|Class
name|actionClass
init|=
name|actions
operator|.
name|get
argument_list|(
name|nodeType
argument_list|)
decl_stmt|;
if|if
condition|(
name|actionClass
operator|!=
literal|null
condition|)
block|{
name|TreeManipulationAction
name|action
init|=
operator|(
name|TreeManipulationAction
operator|)
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|actionClass
argument_list|)
decl_stmt|;
return|return
name|action
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|bindReverseEngineeringToDatamap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|databaseStructures
operator|.
name|put
argument_list|(
name|dataMap
argument_list|,
name|reverseEngineering
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbImportTree
name|getSourceTree
parameter_list|()
block|{
return|return
name|sourceTree
return|;
block|}
specifier|private
specifier|static
class|class
name|SourceTreeTransferHandler
extends|extends
name|TransferHandler
block|{
annotation|@
name|Override
specifier|public
name|int
name|getSourceActions
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
return|return
name|COPY
return|;
block|}
annotation|@
name|Override
specifier|public
name|Transferable
name|createTransferable
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
name|JTree
name|tree
init|=
operator|(
name|JTree
operator|)
name|c
decl_stmt|;
name|TreePath
index|[]
name|paths
init|=
name|tree
operator|.
name|getSelectionPaths
argument_list|()
decl_stmt|;
name|DbImportTreeNode
index|[]
name|nodes
init|=
operator|new
name|DbImportTreeNode
index|[
name|paths
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
name|paths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|nodes
index|[
name|i
index|]
operator|=
operator|(
name|DbImportTreeNode
operator|)
name|paths
index|[
name|i
index|]
operator|.
name|getLastPathComponent
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|Transferable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|DataFlavor
index|[]
name|getTransferDataFlavors
parameter_list|()
block|{
return|return
name|TransferableNode
operator|.
name|flavors
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDataFlavorSupported
parameter_list|(
name|DataFlavor
name|flavor
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getTransferData
parameter_list|(
name|DataFlavor
name|flavor
parameter_list|)
throws|throws
name|UnsupportedFlavorException
throws|,
name|IOException
block|{
return|return
name|nodes
return|;
block|}
block|}
return|;
block|}
block|}
specifier|private
class|class
name|SourceTreeKeyListener
implements|implements
name|KeyListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|keyTyped
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_ESCAPE
condition|)
block|{
name|sourceTree
operator|.
name|setSelectionRow
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
block|}
specifier|private
class|class
name|TargetTreeKeyListener
implements|implements
name|KeyListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|keyTyped
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_ESCAPE
condition|)
block|{
name|targetTree
operator|.
name|setSelectionRow
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
block|}
specifier|private
class|class
name|TargetTreeSelectionListener
implements|implements
name|TreeSelectionListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|valueChanged
parameter_list|(
name|TreeSelectionEvent
name|e
parameter_list|)
block|{
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|sourceTree
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DbImportTreeNode
name|root
init|=
operator|(
name|DbImportTreeNode
operator|)
name|model
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|sourceTree
operator|.
name|repaint
argument_list|()
expr_stmt|;
if|if
condition|(
name|root
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|model
operator|.
name|nodesChanged
argument_list|(
name|root
argument_list|,
operator|new
name|int
index|[]
block|{
name|root
operator|.
name|getChildCount
argument_list|()
operator|-
literal|1
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|canBeMoved
argument_list|()
condition|)
block|{
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|canBeInverted
argument_list|()
condition|)
block|{
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|moveInvertButton
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
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|TargetTreeTransferHandler
extends|extends
name|TransferHandler
block|{
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|boolean
name|canImport
parameter_list|(
name|TransferSupport
name|support
parameter_list|)
block|{
if|if
condition|(
operator|!
name|support
operator|.
name|isDrop
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|importData
parameter_list|(
name|TransferSupport
name|support
parameter_list|)
block|{
if|if
condition|(
operator|!
name|canImport
argument_list|(
name|support
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|canBeMoved
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Transferable
name|transferable
init|=
name|support
operator|.
name|getTransferable
argument_list|()
decl_stmt|;
name|DbImportTreeNode
index|[]
name|transferData
init|=
literal|null
decl_stmt|;
try|try
block|{
for|for
control|(
name|DataFlavor
name|dataFlavor
range|:
name|transferable
operator|.
name|getTransferDataFlavors
argument_list|()
control|)
block|{
name|transferData
operator|=
operator|(
name|DbImportTreeNode
index|[]
operator|)
name|transferable
operator|.
name|getTransferData
argument_list|(
name|dataFlavor
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|UnsupportedFlavorException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|transferData
operator|!=
literal|null
condition|)
block|{
name|MoveImportNodeAction
name|action
init|=
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|MoveImportNodeAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|setSourceTree
argument_list|(
name|sourceTree
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetTree
argument_list|(
name|targetTree
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPanel
argument_list|(
name|DraggableTreePanel
operator|.
name|this
argument_list|)
expr_stmt|;
name|action
operator|.
name|performAction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|private
class|class
name|SourceTreeSelectionListener
implements|implements
name|TreeSelectionListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|valueChanged
parameter_list|(
name|TreeSelectionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|sourceTree
operator|.
name|getLastSelectedPathComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|canBeMoved
argument_list|()
condition|)
block|{
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|canBeInverted
argument_list|()
condition|)
block|{
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|moveInvertButton
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
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
class|class
name|ResetFocusMouseAdapter
extends|extends
name|MouseAdapter
block|{
annotation|@
name|Override
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|sourceTree
operator|.
name|getRowForLocation
argument_list|(
name|e
operator|.
name|getX
argument_list|()
argument_list|,
name|e
operator|.
name|getY
argument_list|()
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|sourceTree
operator|.
name|setSelectionRow
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|moveInvertButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|moveButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

