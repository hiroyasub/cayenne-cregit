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

begin_comment
comment|/**  * A subclass of ConnectionWizard that tests configured DataSource, but does not keep an  * open connection.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataSourceWizard
extends|extends
name|CayenneController
block|{
specifier|protected
name|DataSourceWizardView
name|view
decl_stmt|;
specifier|protected
name|DBConnectionInfo
name|altDataSource
decl_stmt|;
specifier|protected
name|String
name|altDataSourceKey
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
comment|// this object is a clone of an object selected from the dropdown, as we need to allow
comment|// local temporary modifications
specifier|protected
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
specifier|protected
name|boolean
name|canceled
decl_stmt|;
specifier|public
name|DataSourceWizard
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|String
name|title
parameter_list|,
name|String
name|altDataSourceKey
parameter_list|,
name|DBConnectionInfo
name|altDataSource
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
operator|new
name|DataSourceWizardView
argument_list|(
name|this
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
name|altDataSource
operator|=
name|altDataSource
expr_stmt|;
name|this
operator|.
name|altDataSourceKey
operator|=
name|altDataSourceKey
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
comment|/**      * Main action method that pops up a dialog asking for user selection. Returns true if      * the selection was confirmed, false - if canceled.      */
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
comment|/**      * Tests that the entered information is valid and can be used to open a conneciton.      * Does not store the open connection.      */
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
comment|// try making an adapter...
try|try
block|{
name|info
operator|.
name|makeAdapter
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"DbAdapter Error"
argument_list|,
name|th
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// doing connection testing...
comment|// attempt opening the connection, and close it right away
try|try
block|{
name|Connection
name|connection
init|=
name|info
operator|.
name|makeDataSource
argument_list|(
name|classLoader
argument_list|)
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
comment|// ignore close error
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
comment|// set success flag, and unblock the caller...
name|canceled
operator|=
literal|false
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
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
name|getPreferenceDomain
argument_list|()
operator|.
name|getDetailsMap
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
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
name|info
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|altDataSourceKey
operator|!=
literal|null
operator|&&
operator|!
name|dataSources
operator|.
name|containsKey
argument_list|(
name|altDataSourceKey
argument_list|)
operator|&&
name|altDataSource
operator|!=
literal|null
condition|)
block|{
name|dataSources
operator|.
name|put
argument_list|(
name|altDataSourceKey
argument_list|,
name|altDataSource
argument_list|)
expr_stmt|;
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
name|getDataSourceKey
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|key
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|altDataSourceKey
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|altDataSourceKey
expr_stmt|;
block|}
if|else if
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

