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
name|dialog
operator|.
name|ConfirmDeleteDialog
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
name|EntityListenerEvent
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
comment|/**  * Action class for removing entity listeners from a DamaMap  *  * @author Vasil Tarasevich  * @version 1.0 Oct 30, 2007  */
end_comment

begin_class
specifier|public
class|class
name|RemoveEntityListenerForDataMapAction
extends|extends
name|RemoveAction
block|{
comment|/**      * unique action name      */
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Remove entity listener for data map"
decl_stmt|;
comment|/**      * Constructor.      *      * @param application Application instance      */
specifier|public
name|RemoveEntityListenerForDataMapAction
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
comment|/**      * @return icon file name for button      */
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-remove-listener.gif"
return|;
block|}
comment|/**      * base entity listener removing logic      * @param e event      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ConfirmDeleteDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|()
decl_stmt|;
if|if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentListenerClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"entity listener"
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getCurrentListenerClass
argument_list|()
argument_list|)
condition|)
block|{
name|removeDefaultEntityListener
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|removeDefaultEntityListener
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
if|if
condition|(
name|listenerClass
operator|!=
literal|null
condition|)
block|{
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|removeDefaultEntityListener
argument_list|(
name|listenerClass
argument_list|)
expr_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireEntityListenerEvent
argument_list|(
operator|new
name|EntityListenerEvent
argument_list|(
name|RemoveEntityListenerForDataMapAction
operator|.
name|this
argument_list|,
name|listenerClass
argument_list|,
name|listenerClass
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

