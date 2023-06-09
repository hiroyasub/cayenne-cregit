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
name|GetDbConnectionAction
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
name|AddPatternParamAction
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
name|DeleteNodeAction
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
name|EditNodeAction
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
name|JToolBar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|EmptyBorder
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
class|class
name|TreeToolbarPanel
extends|extends
name|JToolBar
block|{
specifier|private
name|JButton
name|schemaButton
decl_stmt|;
specifier|private
name|JButton
name|catalogButton
decl_stmt|;
specifier|private
name|JButton
name|includeTableButton
decl_stmt|;
specifier|private
name|JButton
name|excludeTableButton
decl_stmt|;
specifier|private
name|JButton
name|includeColumnButton
decl_stmt|;
specifier|private
name|JButton
name|excludeColumnButton
decl_stmt|;
specifier|private
name|JButton
name|includeProcedureButton
decl_stmt|;
specifier|private
name|JButton
name|excludeProcedureButton
decl_stmt|;
specifier|private
name|JButton
name|editButton
decl_stmt|;
specifier|private
name|JButton
name|deleteButton
decl_stmt|;
specifier|private
name|JButton
name|configureButton
decl_stmt|;
specifier|private
name|DbImportTree
name|reverseEngineeringTree
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|List
argument_list|<
name|JButton
argument_list|>
argument_list|>
name|levels
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
name|TreeToolbarPanel
parameter_list|(
name|ProjectController
name|projectController
parameter_list|,
name|DbImportTree
name|reverseEngineeringTree
parameter_list|,
name|DraggableTreePanel
name|treePanel
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|reverseEngineeringTree
operator|=
name|reverseEngineeringTree
expr_stmt|;
name|createButtons
argument_list|(
name|treePanel
argument_list|)
expr_stmt|;
name|initLevels
argument_list|()
expr_stmt|;
name|addButtons
argument_list|()
expr_stmt|;
name|this
operator|.
name|setBorder
argument_list|(
operator|new
name|EmptyBorder
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|unlockButtons
parameter_list|()
block|{
name|changeToolbarButtonsState
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isLabelSelected
parameter_list|()
block|{
name|DbImportTreeNode
name|selectedNode
init|=
name|reverseEngineeringTree
operator|.
name|getSelectedNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
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
name|void
name|lockButtons
parameter_list|()
block|{
if|if
condition|(
operator|(
name|reverseEngineeringTree
operator|.
name|getLastSelectedPathComponent
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|isLabelSelected
argument_list|()
operator|)
condition|)
block|{
name|DbImportTreeNode
name|selectedNode
init|=
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|reverseEngineeringTree
operator|.
name|getLastSelectedPathComponent
argument_list|()
operator|)
decl_stmt|;
name|DbImportTreeNode
name|parentNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|selectedNode
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentNode
operator|!=
literal|null
condition|)
block|{
name|lockButtons
argument_list|(
name|parentNode
operator|.
name|getUserObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|unlockButtons
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|changeToolbarButtonsState
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|changeToolbarButtonsState
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
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
name|List
argument_list|<
name|JButton
argument_list|>
name|rootLevelButtons
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|catalogButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|schemaButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|includeTableButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|excludeTableButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|includeColumnButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|excludeColumnButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|includeProcedureButton
argument_list|)
expr_stmt|;
name|rootLevelButtons
operator|.
name|add
argument_list|(
name|excludeProcedureButton
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JButton
argument_list|>
name|catalogLevelButtons
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|schemaButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|includeTableButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|excludeTableButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|includeColumnButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|excludeColumnButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|includeProcedureButton
argument_list|)
expr_stmt|;
name|catalogLevelButtons
operator|.
name|add
argument_list|(
name|excludeProcedureButton
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JButton
argument_list|>
name|schemaLevelButtons
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|includeTableButton
argument_list|)
expr_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|excludeTableButton
argument_list|)
expr_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|includeColumnButton
argument_list|)
expr_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|excludeColumnButton
argument_list|)
expr_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|includeProcedureButton
argument_list|)
expr_stmt|;
name|schemaLevelButtons
operator|.
name|add
argument_list|(
name|excludeProcedureButton
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JButton
argument_list|>
name|includeTableLevelButtons
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|includeTableLevelButtons
operator|.
name|add
argument_list|(
name|includeColumnButton
argument_list|)
expr_stmt|;
name|includeTableLevelButtons
operator|.
name|add
argument_list|(
name|excludeColumnButton
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ReverseEngineering
operator|.
name|class
argument_list|,
name|rootLevelButtons
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
name|catalogLevelButtons
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
name|schemaLevelButtons
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
name|includeTableLevelButtons
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addButtons
parameter_list|()
block|{
name|this
operator|.
name|setFloatable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|catalogButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|schemaButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|includeTableButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|excludeTableButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|includeColumnButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|excludeColumnButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|includeProcedureButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|excludeProcedureButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|editButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|deleteButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|configureButton
argument_list|)
expr_stmt|;
block|}
name|void
name|changeToolbarButtonsState
parameter_list|(
name|boolean
name|state
parameter_list|)
block|{
name|schemaButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|catalogButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|includeTableButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|excludeTableButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|includeColumnButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|excludeColumnButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|includeProcedureButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|excludeProcedureButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|editButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|lockButtons
parameter_list|(
name|Object
name|userObject
parameter_list|)
block|{
name|changeToolbarButtonsState
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JButton
argument_list|>
name|buttons
init|=
name|levels
operator|.
name|get
argument_list|(
name|userObject
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|JButton
name|button
range|:
name|buttons
control|)
block|{
name|button
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|editButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
extends|extends
name|TreeManipulationAction
parameter_list|>
name|JButton
name|createButton
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|actionClass
parameter_list|,
name|int
name|position
parameter_list|)
block|{
name|TreeManipulationAction
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
name|actionClass
argument_list|)
decl_stmt|;
name|action
operator|.
name|setTree
argument_list|(
name|reverseEngineeringTree
argument_list|)
expr_stmt|;
return|return
name|action
operator|.
name|buildButton
argument_list|(
name|position
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
extends|extends
name|AddPatternParamAction
parameter_list|>
name|JButton
name|createButton
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|actionClass
parameter_list|,
name|int
name|position
parameter_list|,
name|Class
name|paramClass
parameter_list|)
block|{
name|AddPatternParamAction
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
name|actionClass
argument_list|)
decl_stmt|;
name|action
operator|.
name|setTree
argument_list|(
name|reverseEngineeringTree
argument_list|)
expr_stmt|;
name|action
operator|.
name|setParamClass
argument_list|(
name|paramClass
argument_list|)
expr_stmt|;
return|return
name|action
operator|.
name|buildButton
argument_list|(
name|position
argument_list|)
return|;
block|}
specifier|private
name|void
name|createButtons
parameter_list|(
name|DraggableTreePanel
name|panel
parameter_list|)
block|{
name|schemaButton
operator|=
name|createButton
argument_list|(
name|AddSchemaAction
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|catalogButton
operator|=
name|createButton
argument_list|(
name|AddCatalogAction
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|includeTableButton
operator|=
name|createButton
argument_list|(
name|AddIncludeTableAction
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|excludeTableButton
operator|=
name|createButton
argument_list|(
name|AddExcludeTableAction
operator|.
name|class
argument_list|,
literal|2
argument_list|,
name|ExcludeTable
operator|.
name|class
argument_list|)
expr_stmt|;
name|includeColumnButton
operator|=
name|createButton
argument_list|(
name|AddIncludeColumnAction
operator|.
name|class
argument_list|,
literal|2
argument_list|,
name|IncludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|excludeColumnButton
operator|=
name|createButton
argument_list|(
name|AddExcludeColumnAction
operator|.
name|class
argument_list|,
literal|2
argument_list|,
name|ExcludeColumn
operator|.
name|class
argument_list|)
expr_stmt|;
name|includeProcedureButton
operator|=
name|createButton
argument_list|(
name|AddIncludeProcedureAction
operator|.
name|class
argument_list|,
literal|2
argument_list|,
name|IncludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|excludeProcedureButton
operator|=
name|createButton
argument_list|(
name|AddExcludeProcedureAction
operator|.
name|class
argument_list|,
literal|3
argument_list|,
name|ExcludeProcedure
operator|.
name|class
argument_list|)
expr_stmt|;
name|editButton
operator|=
name|createButton
argument_list|(
name|EditNodeAction
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|DeleteNodeAction
name|deleteNodeAction
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
name|DeleteNodeAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|deleteNodeAction
operator|.
name|setTree
argument_list|(
name|reverseEngineeringTree
argument_list|)
expr_stmt|;
name|deleteNodeAction
operator|.
name|setPanel
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|deleteButton
operator|=
name|deleteNodeAction
operator|.
name|buildButton
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|GetDbConnectionAction
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
name|GetDbConnectionAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|configureButton
operator|=
name|action
operator|.
name|buildButton
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

