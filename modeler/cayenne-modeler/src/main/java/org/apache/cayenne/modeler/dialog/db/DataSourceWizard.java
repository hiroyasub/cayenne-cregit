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
name|dialog
operator|.
name|pref
operator|.
name|GeneralPreferences
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
name|event
operator|.
name|DataSourceModificationEvent
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
name|DataSourceModificationListener
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
name|DefaultComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|WindowConstants
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
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
name|DB_ADAPTER_PROPERTY
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
name|JDBC_DRIVER_PROPERTY
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
name|PASSWORD_PROPERTY
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
name|URL_PROPERTY
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
name|USER_NAME_PROPERTY
import|;
end_import

begin_comment
comment|/**  * A subclass of ConnectionWizard that tests configured DataSource, but does not  * keep an open connection.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataSourceWizard
extends|extends
name|CayenneController
block|{
specifier|private
name|DataSourceWizardView
name|view
decl_stmt|;
specifier|private
name|ObjectBinding
name|dataSourceBinding
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DBConnectionInfo
argument_list|>
name|dataSources
decl_stmt|;
specifier|private
name|String
name|dataSourceKey
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
comment|// this object is a clone of an object selected from the dropdown, as we
comment|// need to allow
comment|// local temporary modifications
specifier|private
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
specifier|private
name|boolean
name|canceled
decl_stmt|;
specifier|private
name|DataSourceModificationListener
name|dataSourceListener
decl_stmt|;
specifier|private
name|DbAdapter
name|adapter
decl_stmt|;
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
specifier|public
name|DataSourceWizard
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|createView
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionInfo
operator|=
operator|new
name|DBConnectionInfo
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|initDataSourceListener
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
index|[]
name|getLabelsForDialog
parameter_list|(
name|String
name|viewTitle
parameter_list|)
block|{
switch|switch
condition|(
name|viewTitle
condition|)
block|{
case|case
name|GetDbConnectionAction
operator|.
name|DIALOG_TITLE
case|:
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"Save"
block|,
literal|"Cancel"
block|}
return|;
block|}
default|default:
return|return
operator|new
name|String
index|[]
block|{
literal|"Continue"
block|,
literal|"Cancel"
block|}
return|;
block|}
block|}
comment|/** 	 * Creates swing dialog for this wizard 	 */
specifier|private
name|DataSourceWizardView
name|createView
parameter_list|(
name|String
name|viewTitle
parameter_list|)
block|{
name|String
index|[]
name|labels
init|=
name|getLabelsForDialog
argument_list|(
name|viewTitle
argument_list|)
decl_stmt|;
return|return
operator|new
name|DataSourceWizardView
argument_list|(
name|this
argument_list|,
name|labels
argument_list|)
return|;
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
name|getCancelButton
argument_list|()
argument_list|,
literal|"cancelAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getOkButton
argument_list|()
argument_list|,
literal|"okAction()"
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
name|initDataSourceListener
parameter_list|()
block|{
name|dataSourceListener
operator|=
operator|new
name|DataSourceModificationListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|callbackDataSourceRemoved
parameter_list|(
name|DataSourceModificationEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|callbackDataSourceAdded
parameter_list|(
name|DataSourceModificationEvent
name|e
parameter_list|)
block|{
name|setDataSourceKey
argument_list|(
name|e
operator|.
name|getDataSourceName
argument_list|()
argument_list|)
expr_stmt|;
name|refreshDataSources
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|addDataSourceModificationListener
argument_list|(
name|dataSourceListener
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initFavouriteDataSource
parameter_list|()
block|{
name|Preferences
name|pref
init|=
name|getApplication
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|GeneralPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|favouriteDataSource
init|=
name|pref
operator|.
name|get
argument_list|(
name|GeneralPreferences
operator|.
name|FAVOURITE_DATA_SOURCE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|favouriteDataSource
operator|!=
literal|null
operator|&&
name|dataSources
operator|.
name|containsKey
argument_list|(
name|favouriteDataSource
argument_list|)
condition|)
block|{
name|setDataSourceKey
argument_list|(
name|favouriteDataSource
argument_list|)
expr_stmt|;
name|dataSourceBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeDataSourceListener
parameter_list|()
block|{
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|removeDataSourceModificationListener
argument_list|(
name|dataSourceListener
argument_list|)
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
name|projectController
operator|.
name|getDataMapPreferences
argument_list|(
name|projectController
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
name|this
operator|.
name|dataSourceKey
operator|=
name|dataSourceKey
expr_stmt|;
comment|// update a clone object that will be used to obtain connection...
name|DBConnectionInfo
name|currentInfo
init|=
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
name|view
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|setConnectionInfo
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Main action method that pops up a dialog asking for user selection. 	 * Returns true if the selection was confirmed, false - if canceled. 	 */
specifier|public
name|boolean
name|startupAction
parameter_list|()
block|{
name|this
operator|.
name|canceled
operator|=
literal|true
expr_stmt|;
name|refreshDataSources
argument_list|()
expr_stmt|;
name|initFavouriteDataSource
argument_list|()
expr_stmt|;
name|DataMapDefaults
name|dataMapDefaults
init|=
name|projectController
operator|.
name|getDataMapPreferences
argument_list|(
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
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
condition|)
block|{
name|getConnectionInfoFromPreferences
argument_list|()
operator|.
name|copyTo
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|connectionInfo
operator|.
name|setConnectionInfo
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|!
name|canceled
return|;
block|}
specifier|public
name|DBConnectionInfo
name|getConnectionInfo
parameter_list|()
block|{
return|return
name|connectionInfo
return|;
block|}
comment|/** 	 * Tests that the entered information is valid and can be used to open a 	 * conneciton. Does not store the open connection. 	 */
specifier|public
name|void
name|okAction
parameter_list|()
block|{
name|DBConnectionInfo
name|info
init|=
name|getConnectionInfo
argument_list|()
decl_stmt|;
name|ClassLoadingService
name|classLoader
init|=
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
decl_stmt|;
comment|// doing connection testing...
try|try
block|{
name|this
operator|.
name|adapter
operator|=
name|info
operator|.
name|makeAdapter
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|info
operator|.
name|makeDataSource
argument_list|(
name|classLoader
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
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignore
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"Connection Error"
argument_list|,
name|ignore
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"Connection Error"
argument_list|,
name|th
argument_list|)
expr_stmt|;
return|return;
block|}
name|onClose
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|onClose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * On close handler. Introduced to remove data source listener. 	 */
specifier|protected
name|void
name|onClose
parameter_list|(
name|boolean
name|canceled
parameter_list|)
block|{
comment|// set success flag, and unblock the caller...
name|this
operator|.
name|canceled
operator|=
name|canceled
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|removeDataSourceListener
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|canceled
condition|)
block|{
name|Preferences
name|pref
init|=
name|getApplication
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|GeneralPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|pref
operator|.
name|put
argument_list|(
name|GeneralPreferences
operator|.
name|FAVOURITE_DATA_SOURCE
argument_list|,
name|getDataSourceKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Opens preferences panel to allow configuration of DataSource presets. 	 */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|refreshDataSources
parameter_list|()
block|{
name|this
operator|.
name|dataSources
operator|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|DBConnectionInfo
argument_list|>
operator|)
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
specifier|final
name|String
name|_12package
init|=
literal|"org.objectstyle.cayenne."
decl_stmt|;
for|for
control|(
name|DBConnectionInfo
name|info
range|:
name|dataSources
operator|.
name|values
argument_list|()
control|)
block|{
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
name|String
index|[]
name|keys
init|=
name|dataSources
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
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
argument_list|<>
argument_list|(
name|keys
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|key
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getDataSourceKey
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|dataSources
operator|.
name|containsKey
argument_list|(
name|getDataSourceKey
argument_list|()
argument_list|)
condition|)
block|{
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
expr_stmt|;
block|}
block|}
name|setDataSourceKey
argument_list|(
name|key
operator|!=
literal|null
condition|?
name|key
else|:
name|getDataSourceKey
argument_list|()
argument_list|)
expr_stmt|;
name|dataSourceBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
comment|/** 	 * Returns configured DbAdapter. 	 */
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
name|void
name|setProjectController
parameter_list|(
name|ProjectController
name|projectController
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
block|}
block|}
end_class

end_unit

