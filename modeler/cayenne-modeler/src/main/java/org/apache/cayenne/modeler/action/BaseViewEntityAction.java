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
name|DbEntity
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
name|tree
operator|.
name|TreePath
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseViewEntityAction
extends|extends
name|CayenneAction
block|{
specifier|abstract
specifier|protected
name|Entity
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|getEntity
parameter_list|()
function_decl|;
specifier|public
name|BaseViewEntityAction
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
comment|/**      * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent)      */
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
name|viewEntity
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|viewEntity
parameter_list|()
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
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|navigateToEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|navigateToEntity
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
name|TreePath
name|path
init|=
name|buildTreePath
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|editor
argument_list|()
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|EntityDisplayEvent
name|event
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|editor
argument_list|()
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
if|if
condition|(
name|entity
operator|instanceof
name|DbEntity
condition|)
block|{
name|getProjectController
argument_list|()
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
name|getProjectController
argument_list|()
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

