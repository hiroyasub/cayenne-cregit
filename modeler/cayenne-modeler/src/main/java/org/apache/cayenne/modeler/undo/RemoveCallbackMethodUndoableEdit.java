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
name|modeler
operator|.
name|action
operator|.
name|CreateCallbackMethodAction
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
name|RemoveCallbackMethodAction
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
name|CallbackType
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
name|ObjCallbackMethod
import|;
end_import

begin_class
specifier|public
class|class
name|RemoveCallbackMethodUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|CallbackType
name|callbackType
decl_stmt|;
specifier|private
name|ObjCallbackMethod
index|[]
name|methods
decl_stmt|;
specifier|public
name|RemoveCallbackMethodUndoableEdit
parameter_list|(
name|CallbackType
name|callbackType
parameter_list|,
name|ObjCallbackMethod
index|[]
name|methods
parameter_list|)
block|{
name|this
operator|.
name|callbackType
operator|=
name|callbackType
expr_stmt|;
name|this
operator|.
name|methods
operator|=
name|methods
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
literal|"Remove Obj Callback Methods"
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
name|RemoveCallbackMethodAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveCallbackMethodAction
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjCallbackMethod
name|method
range|:
name|methods
control|)
block|{
name|action
operator|.
name|removeCallbackMethod
argument_list|(
name|callbackType
argument_list|,
name|method
operator|.
name|getName
argument_list|()
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
name|CreateCallbackMethodAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateCallbackMethodAction
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjCallbackMethod
name|method
range|:
name|methods
control|)
block|{
name|action
operator|.
name|createCallbackMethod
argument_list|(
name|callbackType
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

