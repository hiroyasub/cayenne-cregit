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
name|editor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|EntityListener
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
name|action
operator|.
name|AbstractRemoveCallbackMethodAction
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
name|CreateCallbackMethodForDataMapListenerAction
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
name|CreateDataMapEntityListenerAction
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
name|RemoveCallbackMethodForDataMapListenerAction
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
name|RemoveEntityListenerForDataMapAction
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
name|DataMapDisplayEvent
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
name|DataMapDisplayListener
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
comment|/**  * Tab for editing default entity listeners of a DataMap  *  * @author Vasil Tarasevich  * @version 1.0 Oct 28, 2007  */
end_comment

begin_class
specifier|public
class|class
name|DataMapCallbackListenersTab
extends|extends
name|AbstractCallbackListenersTab
block|{
comment|/**      * Constructor      * @param mediator mediator instance      */
specifier|public
name|DataMapCallbackListenersTab
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return CallbackMap with callback methods      */
specifier|protected
name|CallbackMap
name|getCallbackMap
parameter_list|()
block|{
name|String
name|listenerClass
init|=
operator|(
name|String
operator|)
name|listenerClassCombo
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
return|return
name|listenerClass
operator|==
literal|null
condition|?
literal|null
else|:
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getDefaultEntityListener
argument_list|(
name|listenerClass
argument_list|)
operator|.
name|getCallbackMap
argument_list|()
return|;
block|}
comment|/**      * @return returns entity listeners list      */
specifier|protected
name|List
name|getEntityListeners
parameter_list|()
block|{
return|return
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getDefaultEntityListeners
argument_list|()
return|;
block|}
comment|/**      * init listeners      */
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|super
operator|.
name|initController
argument_list|()
expr_stmt|;
name|mediator
operator|.
name|addDataMapDisplayListener
argument_list|(
operator|new
name|DataMapDisplayListener
argument_list|()
block|{
comment|/**                      * process DapaMap selection                      * @param e event                      */
specifier|public
name|void
name|currentDataMapChanged
parameter_list|(
name|DataMapDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|isVisible
argument_list|()
condition|)
block|{
name|rebuildListenerClassCombo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|updateCallbackTypeCounters
argument_list|()
expr_stmt|;
name|mediator
operator|.
name|setCurrentCallbackType
argument_list|(
operator|(
name|CallbackType
operator|)
name|callbackTypeCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return create callback method action      */
specifier|protected
name|CayenneAction
name|getCreateCallbackMethodAction
parameter_list|()
block|{
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
return|return
name|app
operator|.
name|getAction
argument_list|(
name|CreateCallbackMethodForDataMapListenerAction
operator|.
name|ACTION_NAME
argument_list|)
return|;
block|}
comment|/**      * @return remove callback method action      */
specifier|protected
name|AbstractRemoveCallbackMethodAction
name|getRemoveCallbackMethodAction
parameter_list|()
block|{
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
return|return
operator|(
name|AbstractRemoveCallbackMethodAction
operator|)
name|app
operator|.
name|getAction
argument_list|(
name|RemoveCallbackMethodForDataMapListenerAction
operator|.
name|ACTION_NAME
argument_list|)
return|;
block|}
comment|/**      * @return action for removing entity listeners      */
specifier|protected
name|CayenneAction
name|getRemoveEntityListenerAction
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveEntityListenerForDataMapAction
operator|.
name|getActionName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return action for creating entity listeners      */
specifier|public
name|CayenneAction
name|getCreateEntityListenerAction
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|CreateDataMapEntityListenerAction
operator|.
name|getActionName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|EntityListener
name|getEntityListener
parameter_list|(
name|String
name|listenerClass
parameter_list|)
block|{
return|return
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getDefaultEntityListener
argument_list|(
name|listenerClass
argument_list|)
return|;
block|}
block|}
end_class

end_unit

