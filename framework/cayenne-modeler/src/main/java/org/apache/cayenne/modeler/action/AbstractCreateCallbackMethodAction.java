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
name|util
operator|.
name|CayenneAction
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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_comment
comment|/**  * Base class for creating callback methods  *  * @author Vasil Tarasevich  * @version 1.0 Oct 28, 2007  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCreateCallbackMethodAction
extends|extends
name|CayenneAction
block|{
comment|/**      * default name for new callback method      */
specifier|private
specifier|static
specifier|final
name|String
name|NEW_CALLBACK_METHOD
init|=
literal|"untitled"
decl_stmt|;
comment|/**      * Constructor.      *      * @param actionName unique action name      * @param application Application instance      */
specifier|public
name|AbstractCreateCallbackMethodAction
parameter_list|(
name|String
name|actionName
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|actionName
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return CallbackMap instance where to create a method      */
specifier|public
specifier|abstract
name|CallbackMap
name|getCallbackMap
parameter_list|()
function_decl|;
comment|/**      * @return icon file name for button      */
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-create-method.gif"
return|;
block|}
comment|/**      * performs adding new callback method      * @param e event      */
specifier|public
specifier|final
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|CallbackType
name|callbackType
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentCallbackType
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
name|addCallbackMethod
argument_list|(
name|NEW_CALLBACK_METHOD
argument_list|)
expr_stmt|;
name|CallbackMethodEvent
name|ce
init|=
operator|new
name|CallbackMethodEvent
argument_list|(
name|e
operator|.
name|getSource
argument_list|()
argument_list|,
literal|null
argument_list|,
name|NEW_CALLBACK_METHOD
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireCallbackMethodEvent
argument_list|(
name|ce
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

