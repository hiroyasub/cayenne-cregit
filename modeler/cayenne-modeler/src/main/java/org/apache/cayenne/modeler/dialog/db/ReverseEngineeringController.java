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
name|db
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
name|dba
operator|.
name|DbAdapter
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
name|db
operator|.
name|DbLoader
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
name|db
operator|.
name|DbLoaderConfiguration
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
name|FiltersConfigBuilder
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
name|ClassLoadingService
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
name|dialog
operator|.
name|db
operator|.
name|model
operator|.
name|DbModel
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
name|pref
operator|.
name|PreferenceDialog
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
name|pref
operator|.
name|TreeEditor
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
name|pref
operator|.
name|XMLFileEditor
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
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|util
operator|.
name|CayenneController
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
name|BindingBuilder
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
name|ObjectBinding
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|sql
operator|.
name|Connection
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A component for performing reverse engineering. Users can choose required dataMap and execute  * reverse engineering. Also they can see tree view of db objects clicking on sync button.  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringController
extends|extends
name|CayenneController
block|{
specifier|private
specifier|static
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ReverseEngineeringController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ProjectController
name|projectController
decl_stmt|;
specifier|protected
name|ReverseEngineeringView
name|view
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DataMapViewModel
argument_list|>
name|reverseEngineeringMap
decl_stmt|;
specifier|protected
name|DbModel
name|dbModel
decl_stmt|;
specifier|protected
name|DataSource
name|dataSource
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
specifier|protected
name|ObjectBinding
name|dataSourceBinding
decl_stmt|;
specifier|protected
name|Map
name|dataSources
decl_stmt|;
specifier|protected
name|String
name|dataSourceKey
decl_stmt|;
specifier|protected
name|boolean
name|canceled
decl_stmt|;
specifier|public
name|ReverseEngineeringController
parameter_list|(
name|ProjectController
name|controller
parameter_list|,
name|ReverseEngineeringView
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|controller
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectController
operator|=
name|controller
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|connectionInfo
operator|=
operator|new
name|DBConnectionInfo
argument_list|()
expr_stmt|;
name|this
operator|.
name|reverseEngineeringMap
operator|=
name|view
operator|.
name|getReverseEngineeringViewMap
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|refreshDataSources
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|dataSourceBinding
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getDataSources
argument_list|()
argument_list|,
literal|"dataSourceKey"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getSyncButton
argument_list|()
argument_list|,
literal|"syncAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getExecuteButton
argument_list|()
argument_list|,
literal|"executeAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getConfigButton
argument_list|()
argument_list|,
literal|"dataSourceConfigAction()"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|buildDBProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassLoadingService
name|classLoader
init|=
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
decl_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|connectionInfo
operator|.
name|makeAdapter
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|syncAction
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|TreeEditor
name|treeEditor
init|=
name|view
operator|.
name|getTreeEditor
argument_list|()
decl_stmt|;
name|XMLFileEditor
name|xmlFileEditor
init|=
name|view
operator|.
name|getXmlFileEditor
argument_list|()
decl_stmt|;
name|xmlFileEditor
operator|.
name|removeAlertMessage
argument_list|()
expr_stmt|;
try|try
block|{
name|buildDBProperties
argument_list|()
expr_stmt|;
name|ReverseEngineering
name|reverseEngineering
init|=
name|xmlFileEditor
operator|.
name|convertTextIntoReverseEngineering
argument_list|()
decl_stmt|;
name|FiltersConfigBuilder
name|filtersConfigBuilder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|reverseEngineering
argument_list|)
decl_stmt|;
name|DbLoaderConfiguration
name|dbLoaderConfiguration
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|dbLoaderConfiguration
operator|.
name|setFiltersConfig
argument_list|(
name|filtersConfigBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|DbLoader
name|dbLoader
init|=
operator|new
name|ModelerDbLoader
argument_list|(
name|this
argument_list|,
name|treeEditor
argument_list|,
name|connection
argument_list|)
decl_stmt|;
comment|// TODO: counterintuitive... we never use the DataMap that we loaded...
name|dbLoader
operator|.
name|load
argument_list|(
operator|new
name|DataMap
argument_list|()
argument_list|,
name|dbLoaderConfiguration
argument_list|)
expr_stmt|;
block|}
name|String
name|mapName
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|DataMapViewModel
name|dataMapViewModel
init|=
operator|new
name|DataMapViewModel
argument_list|()
decl_stmt|;
name|dataMapViewModel
operator|.
name|setReverseEngineeringTree
argument_list|(
name|dbModel
argument_list|)
expr_stmt|;
name|dataMapViewModel
operator|.
name|setReverseEngineeringText
argument_list|(
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getEditorPane
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|reverseEngineeringMap
operator|.
name|put
argument_list|(
name|mapName
argument_list|,
name|dataMapViewModel
argument_list|)
expr_stmt|;
name|treeEditor
operator|.
name|convertTreeViewIntoTreeNode
argument_list|(
name|dbModel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|xmlFileEditor
operator|.
name|addAlertMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|executeAction
parameter_list|()
block|{
name|XMLFileEditor
name|xmlFileEditor
init|=
name|view
operator|.
name|getXmlFileEditor
argument_list|()
decl_stmt|;
name|xmlFileEditor
operator|.
name|removeAlertMessage
argument_list|()
expr_stmt|;
try|try
block|{
name|buildDBProperties
argument_list|()
expr_stmt|;
specifier|final
name|ReverseEngineering
name|reverseEngineering
init|=
name|xmlFileEditor
operator|.
name|convertTextIntoReverseEngineering
argument_list|()
decl_stmt|;
name|Thread
name|th
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
init|(
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|)
block|{
operator|new
name|DbLoaderHelper
argument_list|(
name|projectController
argument_list|,
name|connection
argument_list|,
name|adapter
argument_list|,
name|connectionInfo
argument_list|,
name|reverseEngineering
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error on execute"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|discardAllEdits
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|th
operator|.
name|start
argument_list|()
expr_stmt|;
name|view
operator|.
name|setTempDataMap
argument_list|(
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|xmlFileEditor
operator|.
name|addAlertMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns configured DbAdapter.      */
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|String
name|getDataSourceKey
parameter_list|()
block|{
return|return
name|dataSourceKey
return|;
block|}
specifier|public
name|void
name|setDataSourceKey
parameter_list|(
name|String
name|dataSourceKey
parameter_list|)
block|{
name|XMLFileEditor
name|xmlFileEditor
init|=
name|view
operator|.
name|getXmlFileEditor
argument_list|()
decl_stmt|;
name|xmlFileEditor
operator|.
name|removeAlertMessage
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataSourceKey
operator|=
name|dataSourceKey
expr_stmt|;
name|DBConnectionInfo
name|currentInfo
init|=
operator|(
name|DBConnectionInfo
operator|)
name|dataSources
operator|.
name|get
argument_list|(
name|dataSourceKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentInfo
operator|!=
literal|null
condition|)
block|{
name|currentInfo
operator|.
name|copyTo
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connectionInfo
operator|=
operator|new
name|DBConnectionInfo
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Opens preferences panel to allow configuration of DataSource presets.      */
specifier|public
name|void
name|dataSourceConfigAction
parameter_list|()
block|{
name|PreferenceDialog
name|prefs
init|=
operator|new
name|PreferenceDialog
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|prefs
operator|.
name|showDataSourceEditorAction
argument_list|(
name|dataSourceKey
argument_list|)
expr_stmt|;
name|refreshDataSources
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|void
name|refreshDataSources
parameter_list|()
block|{
name|this
operator|.
name|dataSources
operator|=
name|getApplication
argument_list|()
operator|.
name|getCayenneProjectPreferences
argument_list|()
operator|.
name|getDetailObject
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
operator|.
name|getChildrenPreferences
argument_list|()
expr_stmt|;
comment|// 1.2 migration fix - update data source adapter names
name|Iterator
name|it
init|=
name|dataSources
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|final
name|String
name|_12package
init|=
literal|"org.objectstyle.cayenne."
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DBConnectionInfo
name|info
init|=
operator|(
name|DBConnectionInfo
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getDbAdapter
argument_list|()
operator|!=
literal|null
operator|&&
name|info
operator|.
name|getDbAdapter
argument_list|()
operator|.
name|startsWith
argument_list|(
name|_12package
argument_list|)
condition|)
block|{
name|info
operator|.
name|setDbAdapter
argument_list|(
literal|"org.apache.cayenne."
operator|+
name|info
operator|.
name|getDbAdapter
argument_list|()
operator|.
name|substring
argument_list|(
name|_12package
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
index|[]
name|keys
init|=
name|dataSources
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|view
operator|.
name|getDataSources
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|keys
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataSources
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|dataSourceKey
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|key
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keys
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|key
operator|=
name|keys
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|setDataSourceKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|getDataSourceKey
argument_list|()
operator|==
literal|null
condition|)
block|{
name|dataSourceBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

