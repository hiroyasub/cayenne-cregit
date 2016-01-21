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
name|util
operator|.
name|List
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
name|JOptionPane
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
name|access
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
name|DataSourceController
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
name|DbMigrateOptionsDialog
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
name|MergerOptions
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

begin_comment
comment|/**  * Action that alter database schema to match a DataMap.  */
end_comment

begin_class
specifier|public
class|class
name|MigrateAction
extends|extends
name|DBWizardAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Migrate Database Schema"
return|;
block|}
specifier|public
name|MigrateAction
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
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|DBConnectionInfo
name|nodeInfo
init|=
name|preferredDataSource
argument_list|()
decl_stmt|;
name|String
name|nodeKey
init|=
name|preferredDataSourceLabel
argument_list|(
name|nodeInfo
argument_list|)
decl_stmt|;
name|DataSourceController
name|connectWizard
init|=
operator|new
name|DataSourceController
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
literal|"Migrate DB Schema: Connect to Database"
argument_list|,
name|nodeKey
argument_list|,
name|nodeInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|connectWizard
operator|.
name|startupAction
argument_list|()
condition|)
block|{
comment|// canceled
return|return;
block|}
name|DataMap
name|map
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
comment|//migarte options
comment|// sanity check
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No current DataMap selected."
argument_list|)
throw|;
block|}
comment|//showOptions dialog
name|String
name|selectedSchema
init|=
literal|null
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|schemas
init|=
name|getSchemas
argument_list|(
name|connectWizard
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemas
operator|!=
literal|null
operator|&&
operator|!
name|schemas
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DbMigrateOptionsDialog
name|optionsDialog
init|=
operator|new
name|DbMigrateOptionsDialog
argument_list|(
name|schemas
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|)
decl_stmt|;
name|optionsDialog
operator|.
name|showDialog
argument_list|()
expr_stmt|;
if|if
condition|(
name|optionsDialog
operator|.
name|getChoice
argument_list|()
operator|==
name|DbMigrateOptionsDialog
operator|.
name|SELECT
condition|)
block|{
name|selectedSchema
operator|=
name|optionsDialog
operator|.
name|getSelectedSchema
argument_list|()
expr_stmt|;
block|}
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
literal|"Error loading schemas dialog"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
comment|// ... show dialog...
operator|new
name|MergerOptions
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
literal|"Migrate DB Schema: Options"
argument_list|,
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
argument_list|,
name|map
argument_list|,
name|selectedSchema
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getSchemas
parameter_list|(
name|DataSourceController
name|connectWizard
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapter
name|dbAdapter
init|=
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|makeAdapter
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
decl_stmt|;
name|DataSource
name|dataSource
init|=
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
operator|.
name|makeDataSource
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|DbLoader
argument_list|(
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|,
name|dbAdapter
argument_list|,
literal|null
argument_list|)
operator|.
name|getSchemas
argument_list|()
return|;
block|}
block|}
end_class

end_unit

