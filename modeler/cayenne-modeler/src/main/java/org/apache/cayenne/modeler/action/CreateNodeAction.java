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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|ConfigurationNode
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
name|configuration
operator|.
name|event
operator|.
name|DataNodeEvent
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
name|server
operator|.
name|XMLPoolingDataSourceFactory
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
name|conn
operator|.
name|DataSourceInfo
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
name|naming
operator|.
name|NameBuilder
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
name|event
operator|.
name|MapEvent
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
name|event
operator|.
name|DataNodeDisplayEvent
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
name|undo
operator|.
name|CreateNodeUndoableEdit
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

begin_class
specifier|public
class|class
name|CreateNodeAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Constructor for CreateNodeAction.      *      * @param application      */
specifier|public
name|CreateNodeAction
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
literal|"Create DataNode"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-node.png"
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
name|DataNodeDescriptor
name|node
init|=
name|buildDataNode
argument_list|()
decl_stmt|;
name|createDataNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateNodeUndoableEdit
argument_list|(
name|application
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createDataNode
parameter_list|(
name|DataNodeDescriptor
name|node
parameter_list|)
block|{
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|this
argument_list|,
name|node
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireDataNodeDisplayEvent
argument_list|(
operator|new
name|DataNodeDisplayEvent
argument_list|(
name|this
argument_list|,
name|domain
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains a DataDomain object.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|DataNodeDescriptor
operator|)
name|object
operator|)
operator|.
name|getDataChannelDescriptor
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**      * Creates a new DataNode, adding to the current domain, but doesn't send      * any events.      */
specifier|public
name|DataNodeDescriptor
name|buildDataNode
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|DataNodeDescriptor
name|node
init|=
name|buildDataNode
argument_list|(
name|domain
argument_list|)
decl_stmt|;
name|DataSourceInfo
name|src
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|node
operator|.
name|setDataSourceDescriptor
argument_list|(
name|src
argument_list|)
expr_stmt|;
comment|// by default create JDBC Node
name|node
operator|.
name|setDataSourceFactoryType
argument_list|(
name|XMLPoolingDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setSchemaUpdateStrategyType
argument_list|(
name|SkipSchemaUpdateStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * A factory method that makes a new DataNode.      */
name|DataNodeDescriptor
name|buildDataNode
parameter_list|(
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|)
block|{
name|DataNodeDescriptor
name|node
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|node
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|node
argument_list|,
name|dataChannelDescriptor
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setDataChannelDescriptor
argument_list|(
name|dataChannelDescriptor
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
block|}
end_class

end_unit

