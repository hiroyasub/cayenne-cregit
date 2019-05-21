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
name|undo
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
name|action
operator|.
name|LinkDataMapAction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotRedoException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotUndoException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
class|class
name|LinkDataMapUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
name|DataMap
name|map
decl_stmt|;
name|DataNodeDescriptor
name|node
decl_stmt|;
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|unlinkedNodes
decl_stmt|;
name|ProjectController
name|mediator
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"Link unlinked DataMaps"
return|;
block|}
specifier|public
name|LinkDataMapUndoableEdit
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DataNodeDescriptor
name|node
parameter_list|,
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|unlinkedNodes
parameter_list|,
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|unlinkedNodes
operator|=
name|unlinkedNodes
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|LinkDataMapAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|LinkDataMapAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|linkDataMap
argument_list|(
name|map
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|remove
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|this
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|unlinkedNodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|DataNodeDescriptor
name|unlinkedNode
range|:
name|unlinkedNodes
control|)
block|{
name|unlinkedNode
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|this
argument_list|,
name|unlinkedNode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

