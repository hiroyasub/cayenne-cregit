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
name|graph
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
name|javax
operator|.
name|swing
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
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
name|map
operator|.
name|Entity
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
name|action
operator|.
name|FindAction
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
name|graph
operator|.
name|GraphBuilder
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
comment|/**  * Action that displays one of the objects in main tree, and then fires another action (if specified)  */
end_comment

begin_class
specifier|public
class|class
name|EntityDisplayAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Action that will be performed after selection      */
specifier|private
name|CayenneAction
name|delegate
decl_stmt|;
specifier|private
name|GraphBuilder
name|builder
decl_stmt|;
specifier|public
name|EntityDisplayAction
parameter_list|(
name|GraphBuilder
name|builder
parameter_list|)
block|{
name|super
argument_list|(
literal|"Show"
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EntityDisplayAction
parameter_list|(
name|GraphBuilder
name|builder
parameter_list|,
name|CayenneAction
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|String
operator|)
name|delegate
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|)
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Create icon manually, because at creation of super object delegate is null
name|Icon
name|icon
init|=
name|createIcon
argument_list|()
decl_stmt|;
if|if
condition|(
name|icon
operator|!=
literal|null
condition|)
block|{
name|super
operator|.
name|putValue
argument_list|(
name|Action
operator|.
name|SMALL_ICON
argument_list|,
name|icon
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|display
argument_list|()
condition|)
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|display
parameter_list|()
block|{
name|Entity
name|entity
init|=
name|builder
operator|.
name|getSelectedEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// reusing logic from FindAction
name|FindAction
operator|.
name|jumpToResult
argument_list|(
operator|new
name|FindAction
operator|.
name|SearchResultEntry
argument_list|(
name|entity
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|getIconName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
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
name|builder
operator|.
name|getSelectedEntity
argument_list|()
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

