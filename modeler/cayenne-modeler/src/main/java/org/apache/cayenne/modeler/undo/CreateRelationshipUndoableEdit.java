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
name|DbRelationship
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
name|ObjRelationship
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
name|CreateRelationshipAction
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
name|RemoveRelationshipAction
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

begin_class
specifier|public
class|class
name|CreateRelationshipUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|ObjEntity
name|objEnt
decl_stmt|;
specifier|private
name|ObjRelationship
index|[]
name|objectRel
decl_stmt|;
specifier|private
name|DbEntity
name|dbEnt
decl_stmt|;
specifier|private
name|DbRelationship
index|[]
name|dbRel
decl_stmt|;
specifier|public
name|CreateRelationshipUndoableEdit
parameter_list|(
name|ObjEntity
name|objEnt
parameter_list|,
name|ObjRelationship
index|[]
name|objectRel
parameter_list|)
block|{
name|this
operator|.
name|objEnt
operator|=
name|objEnt
expr_stmt|;
name|this
operator|.
name|objectRel
operator|=
name|objectRel
expr_stmt|;
block|}
specifier|public
name|CreateRelationshipUndoableEdit
parameter_list|(
name|DbEntity
name|dbEnt
parameter_list|,
name|DbRelationship
index|[]
name|dbRel
parameter_list|)
block|{
name|this
operator|.
name|dbEnt
operator|=
name|dbEnt
expr_stmt|;
name|this
operator|.
name|dbRel
operator|=
name|dbRel
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"Create Relationship"
return|;
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
name|CreateRelationshipAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateRelationshipAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEnt
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|objectRel
control|)
block|{
name|action
operator|.
name|createObjRelationship
argument_list|(
name|objEnt
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dbEnt
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DbRelationship
name|rel
range|:
name|dbRel
control|)
block|{
name|action
operator|.
name|createDbRelationship
argument_list|(
name|dbEnt
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
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
name|RemoveRelationshipAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveRelationshipAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEnt
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeObjRelationships
argument_list|(
name|objEnt
argument_list|,
name|objectRel
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|objEnt
argument_list|,
name|objEnt
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|controller
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
if|if
condition|(
name|dbEnt
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeDbRelationships
argument_list|(
name|dbEnt
argument_list|,
name|dbRel
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|dbEnt
argument_list|,
name|dbEnt
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|controller
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
end_class

end_unit

