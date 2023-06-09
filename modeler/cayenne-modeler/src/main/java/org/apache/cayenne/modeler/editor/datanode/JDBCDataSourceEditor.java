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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|DataSourceDescriptor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
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
annotation|@
name|Override
specifier|public
name|void
name|setNode
parameter_list|(
name|DataNodeDescriptor
name|node
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getNode
argument_list|()
argument_list|,
name|node
argument_list|)
condition|)
block|{
if|if
condition|(
name|node
operator|.
name|getDataSourceDescriptor
argument_list|()
operator|==
literal|null
condition|)
block|{
name|node
operator|.
name|setDataSourceDescriptor
argument_list|(
operator|new
name|DataSourceDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
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
literal|"node.dataSourceDescriptor.userName"
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
literal|"node.dataSourceDescriptor.password"
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
literal|"node.dataSourceDescriptor.dataSourceUrl"
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
literal|"node.dataSourceDescriptor.jdbcDriver"
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
literal|"node.dataSourceDescriptor.maxConnections"
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
literal|"node.dataSourceDescriptor.minConnections"
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
name|getDataSourceDescriptor
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|DataSourceDescriptor
name|projectDataSourceDescriptor
init|=
name|getNode
argument_list|()
operator|.
name|getDataSourceDescriptor
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
name|getObject
argument_list|(
name|key
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
name|projectDataSourceDescriptor
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

