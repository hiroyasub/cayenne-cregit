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
name|CreateObjEntityAction
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
name|CreateObjEntityUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|public
name|CreateObjEntityUndoableEdit
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ObjEntity
name|objEntity
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
name|objEntity
operator|=
name|objEntity
expr_stmt|;
block|}
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
literal|"Create ObjEntity"
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
name|CreateObjEntityAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateObjEntityAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|createObjEntity
argument_list|(
name|map
argument_list|,
name|objEntity
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
name|RemoveAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|removeObjEntity
argument_list|(
name|map
argument_list|,
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

