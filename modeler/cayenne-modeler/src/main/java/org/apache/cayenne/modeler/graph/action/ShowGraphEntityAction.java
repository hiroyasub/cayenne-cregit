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
name|graph
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
name|map
operator|.
name|Entity
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
name|CayenneModelerFrame
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
name|editor
operator|.
name|EditorView
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
name|DomainDisplayEvent
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
name|EntityDisplayEvent
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
comment|/**  * Action that shows entity on the graph  */
end_comment

begin_class
specifier|public
class|class
name|ShowGraphEntityAction
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
literal|"ShowGraphEntity"
return|;
block|}
specifier|public
name|ShowGraphEntityAction
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
argument_list|,
literal|"Show on Graph"
argument_list|)
expr_stmt|;
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-save-as-image.png"
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
name|Entity
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|entity
init|=
literal|null
decl_stmt|;
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
if|if
condition|(
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|entity
operator|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|entity
operator|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|showEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
operator|instanceof
name|Entity
return|;
block|}
name|void
name|showEntity
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|entity
parameter_list|)
block|{
comment|// we're always in same domain
name|EditorView
name|editor
init|=
operator|(
operator|(
name|CayenneModelerFrame
operator|)
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getView
argument_list|()
operator|)
operator|.
name|getView
argument_list|()
decl_stmt|;
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionPath
argument_list|()
operator|.
name|getParentPath
argument_list|()
operator|.
name|getParentPath
argument_list|()
argument_list|)
expr_stmt|;
name|DomainDisplayEvent
name|event
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
name|entity
argument_list|,
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|,
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
argument_list|)
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireDomainDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

