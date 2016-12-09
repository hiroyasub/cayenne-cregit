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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|EntityMergeSupport
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
name|ObjEntity
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
name|EntityEvent
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
name|dialog
operator|.
name|objentity
operator|.
name|EntitySyncController
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyEvent
import|;
end_import

begin_comment
comment|/**  * Action that synchronizes a given ObjEntity with the current state of the underlying  * DbEntity.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjEntitySyncAction
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
literal|"Sync ObjEntity with DbEntity"
return|;
block|}
specifier|public
name|ObjEntitySyncAction
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
literal|"icon-sync.gif"
return|;
block|}
specifier|public
name|KeyStroke
name|getAcceleratorKey
parameter_list|()
block|{
return|return
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_U
argument_list|,
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getMenuShortcutKeyMask
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent)      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|synchObjEntity
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|synchObjEntity
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|EntityMergeSupport
name|merger
init|=
operator|new
name|EntitySyncController
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|,
name|entity
argument_list|)
operator|.
name|createMerger
argument_list|()
decl_stmt|;
if|if
condition|(
name|merger
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|merger
operator|.
name|synchronizeWithDbEntity
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
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
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

