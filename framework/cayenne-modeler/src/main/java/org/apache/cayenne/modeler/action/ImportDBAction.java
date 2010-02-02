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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
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
name|ConnectionWizard
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
name|DbLoaderHelper
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
name|project
operator|.
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * Action that imports database structure into a DataMap.  */
end_comment

begin_class
specifier|public
class|class
name|ImportDBAction
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
literal|"Reengineer Database Schema"
return|;
block|}
specifier|public
name|ImportDBAction
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
comment|/**      * Connects to DB and delegates processing to DbLoaderController, starting it      * asynchronously.      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
comment|// guess node connection
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
comment|// connect
name|ConnectionWizard
name|connectWizard
init|=
operator|new
name|ConnectionWizard
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
literal|"Reengineer DB Schema: Connect to Database"
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
name|Connection
name|connection
init|=
name|connectWizard
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|connectWizard
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|DBConnectionInfo
name|dataSourceInfo
init|=
name|connectWizard
operator|.
name|getConnectionInfo
argument_list|()
decl_stmt|;
comment|// from here pass control to DbLoaderHelper, running it from a thread separate
comment|// from EventDispatch
specifier|final
name|DbLoaderHelper
name|helper
init|=
operator|new
name|DbLoaderHelper
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
name|connection
argument_list|,
name|adapter
argument_list|,
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|)
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
name|helper
operator|.
name|execute
argument_list|()
expr_stmt|;
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
block|}
comment|/**      * Returns<code>true</code> if path contains a DataDomain object.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|DataChannelDescriptor
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

