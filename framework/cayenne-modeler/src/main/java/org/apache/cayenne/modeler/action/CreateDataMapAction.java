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
name|undo
operator|.
name|CreateDataMapUndoableEdit
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|resource
operator|.
name|Resource
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
name|NamedObjectFactory
import|;
end_import

begin_comment
comment|/**  * Action that creates new DataMap in the project.  *   */
end_comment

begin_class
specifier|public
class|class
name|CreateDataMapAction
extends|extends
name|CayenneAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Create DataMap"
return|;
block|}
specifier|public
name|CreateDataMapAction
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
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-datamap.gif"
return|;
block|}
comment|/** Calls addDataMap() or creates new data map if no data node selected. */
specifier|public
name|void
name|createDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|mediator
operator|.
name|addDataMap
argument_list|(
name|this
argument_list|,
name|map
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
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataChannelDescriptor
name|currentDomain
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
comment|// use domain name as DataMap base, as map names must be unique across the
comment|// project...
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DataMap
operator|.
name|class
argument_list|,
name|currentDomain
argument_list|,
name|currentDomain
operator|.
name|getName
argument_list|()
operator|+
literal|"Map"
argument_list|)
decl_stmt|;
comment|// set configuration source for new dataMap
name|Resource
name|baseResource
init|=
name|currentDomain
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseResource
operator|!=
literal|null
condition|)
block|{
name|Resource
name|dataMapResource
init|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|setConfigurationSource
argument_list|(
name|dataMapResource
argument_list|)
expr_stmt|;
block|}
name|createDataMap
argument_list|(
name|map
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
name|CreateDataMapUndoableEdit
argument_list|(
name|currentDomain
argument_list|,
name|map
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
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
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
block|}
end_class

end_unit

