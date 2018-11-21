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
name|action
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
name|dialog
operator|.
name|db
operator|.
name|DataSourceWizard
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
name|DbActionOptionsDialog
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
name|DbLoadResultDialog
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
name|DbLoaderContext
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
name|LoadDataMapTask
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
name|GlobalDbImportController
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
name|pref
operator|.
name|DataMapDefaults
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import static
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
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Action that imports database structure into a DataMap.  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringAction
extends|extends
name|DBWizardAction
argument_list|<
name|DbActionOptionsDialog
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Reengineer Database Schema"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ICON_NAME
init|=
literal|"icon-dbi-runImport.png"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DIALOG_TITLE
init|=
literal|"Reengineer DB Schema: Connect to Database"
decl_stmt|;
specifier|private
name|DbImportView
name|view
decl_stmt|;
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
name|ICON_NAME
return|;
block|}
name|ReverseEngineeringAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
specifier|public
name|void
name|performAction
parameter_list|()
block|{
specifier|final
name|DbLoaderContext
name|context
init|=
operator|new
name|DbLoaderContext
argument_list|(
name|application
operator|.
name|getMetaData
argument_list|()
argument_list|)
decl_stmt|;
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
if|if
condition|(
operator|!
name|datamapPreferencesExist
argument_list|()
condition|)
block|{
specifier|final
name|DataSourceWizard
name|connectWizard
init|=
name|dataSourceWizardDialog
argument_list|(
name|DIALOG_TITLE
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectWizard
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|connectionInfo
operator|=
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
expr_stmt|;
name|saveConnectionInfo
argument_list|(
name|connectWizard
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connectionInfo
operator|=
name|getConnectionInfoFromPreferences
argument_list|()
expr_stmt|;
block|}
name|context
operator|.
name|setProjectController
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|setConnection
argument_list|(
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
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
literal|"Error loading schemas dialog"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
return|return;
block|}
name|GlobalDbImportController
name|dbImportController
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getGlobalDbImportController
argument_list|()
decl_stmt|;
name|DbLoadResultDialog
name|dbLoadResultDialog
init|=
name|dbImportController
operator|.
name|createDialog
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dbLoadResultDialog
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|dbImportController
operator|.
name|showDialog
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|context
operator|.
name|buildConfig
argument_list|(
name|connectionInfo
argument_list|,
name|view
argument_list|)
condition|)
block|{
try|try
block|{
name|context
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
block|}
return|return;
block|}
name|runLoaderInThread
argument_list|(
name|context
argument_list|,
parameter_list|()
lambda|->
block|{
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|discardAllEdits
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Connects to DB and delegates processing to DbLoaderController, starting it asynchronously.      */
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
name|performAction
argument_list|()
expr_stmt|;
block|}
specifier|private
name|DBConnectionInfo
name|getConnectionInfoFromPreferences
parameter_list|()
block|{
name|DBConnectionInfo
name|connectionInfo
init|=
operator|new
name|DBConnectionInfo
argument_list|()
decl_stmt|;
name|DataMapDefaults
name|dataMapDefaults
init|=
name|getProjectController
argument_list|()
operator|.
name|getDataMapPreferences
argument_list|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
name|connectionInfo
operator|.
name|setDbAdapter
argument_list|(
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|DB_ADAPTER_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setUrl
argument_list|(
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|URL_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setUserName
argument_list|(
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|USER_NAME_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setPassword
argument_list|(
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|PASSWORD_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setJdbcDriver
argument_list|(
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|JDBC_DRIVER_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|connectionInfo
return|;
block|}
specifier|private
name|void
name|saveConnectionInfo
parameter_list|(
name|DataSourceWizard
name|connectWizard
parameter_list|)
block|{
name|DataMapDefaults
name|dataMapDefaults
init|=
name|getProjectController
argument_list|()
operator|.
name|getDataMapPreferences
argument_list|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|DB_ADAPTER_PROPERTY
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getDbAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|URL_PROPERTY
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|USER_NAME_PROPERTY
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|PASSWORD_PROPERTY
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|JDBC_DRIVER_PROPERTY
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|datamapPreferencesExist
parameter_list|()
block|{
name|DataMapDefaults
name|dataMapDefaults
init|=
name|getProjectController
argument_list|()
operator|.
name|getDataMapPreferences
argument_list|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|dataMapDefaults
operator|.
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|DB_ADAPTER_PROPERTY
argument_list|,
literal|null
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|void
name|runLoaderInThread
parameter_list|(
specifier|final
name|DbLoaderContext
name|context
parameter_list|,
specifier|final
name|Runnable
name|callback
parameter_list|)
block|{
name|Thread
name|th
init|=
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
name|LoadDataMapTask
name|task
init|=
operator|new
name|LoadDataMapTask
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Reengineering DB"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|task
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|th
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|DbActionOptionsDialog
name|createDialog
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|catalogs
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|schemas
parameter_list|,
name|String
name|currentCatalog
parameter_list|,
name|String
name|currentSchema
parameter_list|,
name|int
name|command
parameter_list|)
block|{
comment|// NOOP
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setView
parameter_list|(
name|DbImportView
name|view
parameter_list|)
block|{
name|this
operator|.
name|view
operator|=
name|view
expr_stmt|;
block|}
block|}
end_class

end_unit

