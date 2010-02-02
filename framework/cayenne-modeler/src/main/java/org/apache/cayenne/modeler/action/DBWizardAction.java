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
name|util
operator|.
name|Collection
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
name|conf
operator|.
name|DriverDataSourceFactory
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
name|modeler
operator|.
name|pref
operator|.
name|DataNodeDefaults
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
name|CayenneAction
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|DBWizardAction
extends|extends
name|CayenneAction
block|{
specifier|public
name|DBWizardAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|// ==== Guessing user preferences... *****
specifier|protected
name|DataNodeDescriptor
name|getPreferredNode
parameter_list|()
block|{
name|ProjectController
name|projectController
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataNodeDescriptor
name|node
init|=
name|projectController
operator|.
name|getCurrentDataNode
argument_list|()
decl_stmt|;
comment|// try a node that belongs to the current DataMap ...
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|DataMap
name|map
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodes
init|=
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|projectController
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
operator|)
operator|.
name|getNodeDescriptors
argument_list|()
decl_stmt|;
for|for
control|(
name|DataNodeDescriptor
name|n
range|:
name|nodes
control|)
block|{
if|if
condition|(
name|n
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|contains
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|node
operator|=
name|n
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
return|return
name|node
return|;
block|}
specifier|protected
name|String
name|preferredDataSourceLabel
parameter_list|(
name|DBConnectionInfo
name|nodeInfo
parameter_list|)
block|{
if|if
condition|(
name|nodeInfo
operator|==
literal|null
condition|)
block|{
comment|// only driver nodes have meaningful connection info set
name|DataNodeDescriptor
name|node
init|=
name|getPreferredNode
argument_list|()
decl_stmt|;
return|return
operator|(
name|node
operator|!=
literal|null
operator|&&
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|)
operator|)
condition|?
literal|"DataNode Connection Info"
else|:
literal|null
return|;
block|}
return|return
name|nodeInfo
operator|.
name|getNodeName
argument_list|()
return|;
block|}
comment|/**      * Determines the most reasonable default DataSource choice.      */
specifier|protected
name|DBConnectionInfo
name|preferredDataSource
parameter_list|()
block|{
name|DataNodeDescriptor
name|node
init|=
name|getPreferredNode
argument_list|()
decl_stmt|;
comment|// no current node...
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// if node has local DS set, use it
name|DataNodeDefaults
name|nodeDefaults
init|=
operator|(
name|DataNodeDefaults
operator|)
name|getApplication
argument_list|()
operator|.
name|getCayenneProjectPreferences
argument_list|()
operator|.
name|getProjectDetailObject
argument_list|(
name|DataNodeDefaults
operator|.
name|class
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getPreferenceForDataDomain
argument_list|()
operator|.
name|node
argument_list|(
literal|"DataNode"
argument_list|)
operator|.
name|node
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|key
init|=
operator|(
name|nodeDefaults
operator|!=
literal|null
operator|)
condition|?
name|nodeDefaults
operator|.
name|getLocalDataSource
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|DBConnectionInfo
name|info
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
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
return|;
block|}
block|}
comment|// extract data from the node
if|if
condition|(
operator|!
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// create transient object..
name|DBConnectionInfo
name|nodeInfo
init|=
operator|new
name|DBConnectionInfo
argument_list|()
decl_stmt|;
name|nodeInfo
operator|.
name|copyFrom
argument_list|(
name|node
operator|.
name|getDataSourceDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|nodeInfo
operator|.
name|setDbAdapter
argument_list|(
name|node
operator|.
name|getAdapterType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|nodeInfo
return|;
block|}
block|}
end_class

end_unit

