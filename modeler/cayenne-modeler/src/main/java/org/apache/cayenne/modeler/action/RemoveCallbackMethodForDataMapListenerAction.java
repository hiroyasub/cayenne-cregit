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
name|modeler
operator|.
name|Application
import|;
end_import

begin_comment
comment|/**  * Action class for removing callback methods from DataMap's entity listener  *  * @version 1.0 Oct 30, 2007  */
end_comment

begin_class
specifier|public
class|class
name|RemoveCallbackMethodForDataMapListenerAction
extends|extends
name|AbstractRemoveCallbackMethodAction
block|{
comment|/**      * unique action name      */
specifier|public
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Remove callback method for data map entity listener"
decl_stmt|;
comment|/**      * action name for multiple selection      */
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME_MULTIPLE
init|=
literal|"Remove callback methods for data map entity listener"
decl_stmt|;
comment|/**      * Constructor.      *      * @param application Application instance      */
specifier|public
name|RemoveCallbackMethodForDataMapListenerAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return CallbackMap fom which remove callback method      */
annotation|@
name|Override
specifier|public
name|CallbackMap
name|getCallbackMap
parameter_list|()
block|{
name|String
name|listenerClass
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentListenerClass
argument_list|()
decl_stmt|;
return|return
name|getProjectController
argument_list|()
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
annotation|@
name|Override
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

