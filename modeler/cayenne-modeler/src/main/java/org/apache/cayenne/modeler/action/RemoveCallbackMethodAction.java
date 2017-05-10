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
name|map
operator|.
name|CallbackMap
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
name|ConfirmRemoveDialog
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
name|CallbackMethodEvent
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
name|RemoveCallbackMethodUndoableEdit
import|;
end_import

begin_comment
comment|/**  * Action class for removing callback methods from ObjEntity  *  * @version 1.0 Oct 30, 2007  */
end_comment

begin_class
specifier|public
class|class
name|RemoveCallbackMethodAction
extends|extends
name|RemoveAction
block|{
comment|/**      * unique action name      */
specifier|public
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Remove Callback Method"
decl_stmt|;
comment|/**      * action name for multiple selection      */
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME_MULTIPLE
init|=
literal|"Remove Callback Methods"
decl_stmt|;
comment|/**      * Constructor.      *      * @param actionName unique action name      * @param application Application instance      */
specifier|public
name|RemoveCallbackMethodAction
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
comment|/**      * @return icon file name for button      */
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-trash.png"
return|;
block|}
comment|/**      * performs callback method removing      * @param e event      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|,
name|boolean
name|allowAsking
parameter_list|)
block|{
name|ConfirmRemoveDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|(
name|allowAsking
argument_list|)
decl_stmt|;
name|ObjCallbackMethod
index|[]
name|methods
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentCallbackMethods
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|methods
operator|.
name|length
operator|==
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"callback method"
argument_list|,
name|methods
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|methods
operator|.
name|length
operator|>
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"selected callback methods"
argument_list|)
operator|)
condition|)
block|{
name|removeCallbackMethods
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * base logic for callback method removing      * @param actionEvent event      */
specifier|private
name|void
name|removeCallbackMethods
parameter_list|(
name|ActionEvent
name|actionEvent
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|CallbackType
name|callbackType
init|=
name|mediator
operator|.
name|getCurrentCallbackType
argument_list|()
decl_stmt|;
name|ObjCallbackMethod
index|[]
name|callbackMethods
init|=
name|mediator
operator|.
name|getCurrentCallbackMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjCallbackMethod
name|callbackMethod
range|:
name|callbackMethods
control|)
block|{
name|removeCallbackMethod
argument_list|(
name|callbackType
argument_list|,
name|callbackMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveCallbackMethodUndoableEdit
argument_list|(
name|callbackType
argument_list|,
name|callbackMethods
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeCallbackMethod
parameter_list|(
name|CallbackType
name|callbackType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|getCallbackMap
argument_list|()
operator|.
name|getCallbackDescriptor
argument_list|(
name|callbackType
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|removeCallbackMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|CallbackMethodEvent
name|e
init|=
operator|new
name|CallbackMethodEvent
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|method
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireCallbackMethodEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return unique action name      */
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
comment|/**      * @return CallbackMap fom which remove callback method      */
specifier|public
name|CallbackMap
name|getCallbackMap
parameter_list|()
block|{
return|return
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjEntity
argument_list|()
operator|.
name|getCallbackMap
argument_list|()
return|;
block|}
specifier|public
name|String
name|getActionName
parameter_list|(
name|boolean
name|multiple
parameter_list|)
block|{
return|return
name|multiple
condition|?
name|ACTION_NAME_MULTIPLE
else|:
name|ACTION_NAME
return|;
block|}
block|}
end_class

end_unit

