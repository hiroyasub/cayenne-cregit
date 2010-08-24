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
comment|/**  * Action class for creating callback methods on ObjEntity  *  * @version 1.0 Oct 30, 2007  */
end_comment

begin_class
specifier|public
class|class
name|CreateCallbackMethodAction
extends|extends
name|AbstractCreateCallbackMethodAction
block|{
comment|/**      * unique action name      */
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Create callback method"
decl_stmt|;
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
comment|/**      * Constructor.      *      * @param application Application instance      */
specifier|public
name|CreateCallbackMethodAction
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
comment|/**      * @return CallbackMap instance where to create a method      */
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
block|}
end_class

end_unit

