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
name|dialog
operator|.
name|LogConsole
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
name|pref
operator|.
name|CayennePreferenceForProject
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
name|project
operator|.
name|ProjectPath
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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ExitAction
extends|extends
name|ProjectAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Exit"
return|;
block|}
comment|/**      * Constructor for ExitAction.      */
specifier|public
name|ExitAction
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
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|exit
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|exit
parameter_list|()
block|{
if|if
condition|(
operator|!
name|checkSaveOnClose
argument_list|()
condition|)
block|{
return|return;
block|}
comment|//stop logging before JVM shutdown to prevent hanging
name|LogConsole
operator|.
name|getInstance
argument_list|()
operator|.
name|stopLogging
argument_list|()
expr_stmt|;
name|CayennePreferenceForProject
operator|.
name|removeNewPreferences
argument_list|()
expr_stmt|;
comment|// goodbye
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**     * Always returns true.     */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

