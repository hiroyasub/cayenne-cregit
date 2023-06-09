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
name|action
package|;
end_package

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
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|editor
operator|.
name|dbimport
operator|.
name|DatabaseSchemaLoader
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
name|dbimport
operator|.
name|DbImportModel
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
name|dbimport
operator|.
name|DbImportView
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
name|dbimport
operator|.
name|DraggableTreePanel
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
name|dbimport
operator|.
name|PrintColumnsBiFunction
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
name|dbimport
operator|.
name|PrintTablesBiFunction
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
name|DBConnectionInfo
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|LoadDbSchemaAction
extends|extends
name|DBConnectionAwareAction
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LoadDbSchemaAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ICON_NAME
init|=
literal|"icon-dbi-refresh.png"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Refresh Db Schema"
decl_stmt|;
specifier|private
name|DraggableTreePanel
name|draggableTreePanel
decl_stmt|;
name|LoadDbSchemaAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
name|ICON_NAME
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|performAction
argument_list|(
name|e
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|,
name|TreePath
name|tablePath
parameter_list|)
block|{
specifier|final
name|DbImportView
name|rootParent
init|=
operator|(
operator|(
name|DbImportView
operator|)
name|draggableTreePanel
operator|.
name|getParent
argument_list|()
operator|.
name|getParent
argument_list|()
operator|)
decl_stmt|;
name|rootParent
operator|.
name|getLoadDbSchemaProgress
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rootParent
operator|.
name|getLoadDbSchemaButton
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
name|LoadDbSchemaAction
operator|.
name|this
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rootParent
operator|.
name|lockToolbarButtons
argument_list|()
expr_stmt|;
name|draggableTreePanel
operator|.
name|getMoveButton
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|draggableTreePanel
operator|.
name|getMoveInvertButton
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|DBConnectionInfo
name|connectionInfo
init|=
name|getConnectionInfo
argument_list|(
literal|"Load Db Schema"
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|tablePath
operator|!=
literal|null
condition|)
block|{
name|Object
name|userObject
init|=
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|tablePath
operator|.
name|getLastPathComponent
argument_list|()
operator|)
operator|.
name|getUserObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|userObject
operator|instanceof
name|Catalog
condition|)
block|{
name|Catalog
name|catalog
init|=
operator|(
name|Catalog
operator|)
name|userObject
decl_stmt|;
if|if
condition|(
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|loadTables
argument_list|(
name|connectionInfo
argument_list|,
name|tablePath
argument_list|,
name|rootParent
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|userObject
operator|instanceof
name|Schema
condition|)
block|{
name|loadTables
argument_list|(
name|connectionInfo
argument_list|,
name|tablePath
argument_list|,
name|rootParent
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|userObject
operator|instanceof
name|IncludeTable
condition|)
block|{
name|loadColumns
argument_list|(
name|connectionInfo
argument_list|,
name|tablePath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|loadTables
argument_list|(
name|connectionInfo
argument_list|,
name|tablePath
argument_list|,
name|rootParent
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|loadDataBase
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Error loading db schema"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error loading db schema"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|rootParent
operator|.
name|getLoadDbSchemaButton
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rootParent
operator|.
name|getLoadDbSchemaProgress
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rootParent
operator|.
name|unlockToolbarButtons
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|loadDataBase
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|)
throws|throws
name|Exception
block|{
name|ReverseEngineering
name|databaseReverseEngineering
init|=
operator|new
name|DatabaseSchemaLoader
argument_list|()
operator|.
name|load
argument_list|(
name|connectionInfo
argument_list|,
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
decl_stmt|;
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
operator|.
name|translateReverseEngineeringToTree
argument_list|(
name|databaseReverseEngineering
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|draggableTreePanel
operator|.
name|bindReverseEngineeringToDatamap
argument_list|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|databaseReverseEngineering
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DbImportModel
operator|)
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|reload
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|loadTables
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|TreePath
name|tablePath
parameter_list|,
name|DbImportView
name|rootParent
parameter_list|)
throws|throws
name|SQLException
block|{
name|ReverseEngineering
name|databaseReverseEngineering
init|=
operator|new
name|DatabaseSchemaLoader
argument_list|()
operator|.
name|loadTables
argument_list|(
name|connectionInfo
argument_list|,
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|,
name|tablePath
argument_list|,
name|rootParent
operator|.
name|getTableTypes
argument_list|()
argument_list|)
decl_stmt|;
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
operator|.
name|update
argument_list|(
name|databaseReverseEngineering
argument_list|,
operator|new
name|PrintTablesBiFunction
argument_list|(
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadColumns
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|TreePath
name|tablePath
parameter_list|)
throws|throws
name|SQLException
block|{
name|ReverseEngineering
name|databaseReverseEngineering
init|=
operator|new
name|DatabaseSchemaLoader
argument_list|()
operator|.
name|loadColumns
argument_list|(
name|connectionInfo
argument_list|,
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|,
name|tablePath
argument_list|)
decl_stmt|;
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
operator|.
name|update
argument_list|(
name|databaseReverseEngineering
argument_list|,
operator|new
name|PrintColumnsBiFunction
argument_list|(
name|draggableTreePanel
operator|.
name|getSourceTree
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDraggableTreePanel
parameter_list|(
name|DraggableTreePanel
name|draggableTreePanel
parameter_list|)
block|{
name|this
operator|.
name|draggableTreePanel
operator|=
name|draggableTreePanel
expr_stmt|;
block|}
block|}
end_class

end_unit

