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
name|action
package|;
end_package

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
name|JComponent
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

begin_comment
comment|/**  * Stores a map of modeler actions, and deals with activating/deactivating those actions  * on state changes.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ActionManager
block|{
comment|/**      * Returns an action for key.      */
parameter_list|<
name|T
extends|extends
name|Action
parameter_list|>
name|T
name|getAction
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|actionClass
parameter_list|)
function_decl|;
comment|/**      * Updates actions state to reflect an open project.      */
name|void
name|projectOpened
parameter_list|()
function_decl|;
name|void
name|projectClosed
parameter_list|()
function_decl|;
comment|/**      * Updates actions state to reflect DataDomain selection.      */
name|void
name|domainSelected
parameter_list|()
function_decl|;
name|void
name|dataNodeSelected
parameter_list|()
function_decl|;
name|void
name|dataMapSelected
parameter_list|()
function_decl|;
name|void
name|objEntitySelected
parameter_list|()
function_decl|;
name|void
name|dbEntitySelected
parameter_list|()
function_decl|;
name|void
name|procedureSelected
parameter_list|()
function_decl|;
name|void
name|querySelected
parameter_list|()
function_decl|;
name|void
name|embeddableSelected
parameter_list|()
function_decl|;
comment|/**      * Invoked when several objects were selected in ProjectTree at time      */
name|void
name|multipleObjectsSelected
parameter_list|(
name|ConfigurationNode
index|[]
name|objects
parameter_list|,
name|Application
name|application
parameter_list|)
function_decl|;
comment|/**      * Replaces standard Cut, Copy and Paste action maps, so that accelerators like      * Ctrl+X, Ctrl+C, Ctrl+V would work.      */
name|void
name|setupCutCopyPaste
parameter_list|(
name|JComponent
name|comp
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|cutActionType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|copyActionType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

