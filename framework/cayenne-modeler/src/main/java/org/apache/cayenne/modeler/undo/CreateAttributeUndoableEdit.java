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
name|map
operator|.
name|DbAttribute
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
name|ObjAttribute
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
name|action
operator|.
name|CreateAttributeAction
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
name|RemoveAttributeAction
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
name|CreateAttributeUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|canRedo
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"Create Attribute"
return|;
block|}
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|private
name|ObjAttribute
name|objAttr
decl_stmt|;
specifier|private
name|DataChannelDescriptor
name|domain
decl_stmt|;
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|DbAttribute
name|dbAttr
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|CreateAttributeAction
name|action
init|=
operator|(
name|CreateAttributeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|createObjAttribute
argument_list|(
name|dataMap
argument_list|,
name|objEntity
argument_list|,
name|objAttr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|createDbAttribute
argument_list|(
name|dataMap
argument_list|,
name|dbEntity
argument_list|,
name|dbAttr
argument_list|)
expr_stmt|;
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
name|RemoveAttributeAction
name|action
init|=
operator|(
name|RemoveAttributeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeObjAttributes
argument_list|(
name|objEntity
argument_list|,
operator|new
name|ObjAttribute
index|[]
block|{
name|objAttr
block|}
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
name|objEntity
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeDbAttributes
argument_list|(
name|dataMap
argument_list|,
name|dbEntity
argument_list|,
operator|new
name|DbAttribute
index|[]
block|{
name|dbAttr
block|}
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
name|dbEntity
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|CreateAttributeUndoableEdit
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjAttribute
name|attr
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|objEntity
operator|=
name|objEntity
expr_stmt|;
name|this
operator|.
name|objAttr
operator|=
name|attr
expr_stmt|;
block|}
specifier|public
name|CreateAttributeUndoableEdit
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|DbEntity
name|dbEntity
parameter_list|,
name|DbAttribute
name|attr
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
name|this
operator|.
name|dbAttr
operator|=
name|attr
expr_stmt|;
block|}
block|}
end_class

end_unit

