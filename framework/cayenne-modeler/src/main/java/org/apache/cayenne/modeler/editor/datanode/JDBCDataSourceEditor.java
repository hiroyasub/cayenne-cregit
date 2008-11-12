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
name|datanode
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|CayenneModelerController
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
name|ProjectDataSource
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
name|BindingDelegate
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

begin_class
specifier|public
class|class
name|JDBCDataSourceEditor
extends|extends
name|DataSourceEditor
block|{
specifier|protected
name|JDBCDataSourceView
name|view
decl_stmt|;
specifier|public
name|JDBCDataSourceEditor
parameter_list|(
name|ProjectController
name|parent
parameter_list|,
name|BindingDelegate
name|nodeChangeProcessor
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|nodeChangeProcessor
argument_list|)
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
name|prepareBindings
parameter_list|(
name|BindingBuilder
name|builder
parameter_list|)
block|{
name|this
operator|.
name|view
operator|=
operator|new
name|JDBCDataSourceView
argument_list|()
expr_stmt|;
name|fieldAdapters
operator|=
operator|new
name|ObjectBinding
index|[
literal|6
index|]
expr_stmt|;
name|fieldAdapters
index|[
literal|0
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getUserName
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.userName"
argument_list|)
expr_stmt|;
name|fieldAdapters
index|[
literal|1
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.password"
argument_list|)
expr_stmt|;
name|fieldAdapters
index|[
literal|2
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getUrl
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.dataSourceUrl"
argument_list|)
expr_stmt|;
name|fieldAdapters
index|[
literal|3
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getDriver
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.jdbcDriver"
argument_list|)
expr_stmt|;
name|fieldAdapters
index|[
literal|4
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getMaxConnections
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.maxConnections"
argument_list|)
expr_stmt|;
name|fieldAdapters
index|[
literal|5
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getMinConnections
argument_list|()
argument_list|,
literal|"node.dataSource.dataSourceInfo.minConnections"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getSyncWithLocal
argument_list|()
argument_list|,
literal|"syncDataSourceAction()"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This action is called whenever the password location is changed      * in the GUI pulldown.  It changes labels and editability of the      * password fields depending on the option that was selected.      */
specifier|public
name|void
name|syncDataSourceAction
parameter_list|()
block|{
name|CayenneModelerController
name|mainController
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
decl_stmt|;
if|if
condition|(
name|getNode
argument_list|()
operator|==
literal|null
operator|||
name|getNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ProjectDataSource
name|projectDS
init|=
operator|(
name|ProjectDataSource
operator|)
name|getNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
decl_stmt|;
name|ProjectController
name|parent
init|=
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|parent
operator|.
name|getDataNodePreferences
argument_list|()
operator|.
name|getLocalDataSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|mainController
operator|.
name|updateStatus
argument_list|(
literal|"No Local DataSource selected for node..."
argument_list|)
expr_stmt|;
return|return;
block|}
name|DBConnectionInfo
name|dataSource
init|=
operator|(
name|DBConnectionInfo
operator|)
name|parent
operator|.
name|getApplicationPreferenceDomain
argument_list|()
operator|.
name|getDetail
argument_list|(
name|key
argument_list|,
name|DBConnectionInfo
operator|.
name|class
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataSource
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dataSource
operator|.
name|copyTo
argument_list|(
name|projectDS
operator|.
name|getDataSourceInfo
argument_list|()
argument_list|)
condition|)
block|{
name|refreshView
argument_list|()
expr_stmt|;
name|super
operator|.
name|nodeChangeProcessor
operator|.
name|modelUpdated
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mainController
operator|.
name|updateStatus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mainController
operator|.
name|updateStatus
argument_list|(
literal|"DataNode is up to date..."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|mainController
operator|.
name|updateStatus
argument_list|(
literal|"Invalid Local DataSource selected for node..."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

