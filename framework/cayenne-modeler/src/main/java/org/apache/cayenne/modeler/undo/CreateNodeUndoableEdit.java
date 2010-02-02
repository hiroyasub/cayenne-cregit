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
name|undo
package|;
end_package

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
name|action
operator|.
name|CreateNodeAction
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
name|RemoveAction
import|;
end_import

begin_class
specifier|public
class|class
name|CreateNodeUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"Create DataNode"
return|;
block|}
specifier|private
name|DataNodeDescriptor
name|node
decl_stmt|;
specifier|private
name|DataChannelDescriptor
name|domain
decl_stmt|;
specifier|public
name|CreateNodeUndoableEdit
parameter_list|(
name|Application
name|application
parameter_list|,
name|DataNodeDescriptor
name|node
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
operator|(
name|DataChannelDescriptor
operator|)
name|application
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
empty_stmt|;
name|this
operator|.
name|node
operator|=
name|node
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
name|RemoveAction
name|action
init|=
operator|(
name|RemoveAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|action
operator|.
name|removeDataNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
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
name|CreateNodeAction
name|action
init|=
operator|(
name|CreateNodeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateNodeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|action
operator|.
name|createDataNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

