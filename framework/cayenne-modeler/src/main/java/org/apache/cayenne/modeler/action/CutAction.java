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
name|Toolkit
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

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|KeyStroke
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
name|ConfigurationNode
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
name|util
operator|.
name|CayenneAction
import|;
end_import

begin_comment
comment|/**  * Action for cutting entities, queries etc.  */
end_comment

begin_class
specifier|public
class|class
name|CutAction
extends|extends
name|CayenneAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Cut"
return|;
block|}
comment|/**      * Constructor for CutAction      */
specifier|public
name|CutAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|this
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for descendants      */
specifier|protected
name|CutAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-cut.gif"
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyStroke
name|getAcceleratorKey
parameter_list|()
block|{
return|return
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_X
argument_list|,
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getMenuShortcutKeyMask
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Performs cutting of items      */
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CopyAction
operator|.
name|class
argument_list|)
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|class
argument_list|)
operator|.
name|performAction
argument_list|(
name|e
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable object.      */
annotation|@
name|Override
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
return|return
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CopyAction
operator|.
name|class
argument_list|)
operator|.
name|enableForPath
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

