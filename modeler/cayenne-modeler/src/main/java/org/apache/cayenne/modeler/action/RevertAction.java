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
name|io
operator|.
name|File
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
name|CayenneRuntimeException
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
name|CayenneModelerController
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
name|project
operator|.
name|Project
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|RevertAction
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
literal|"Revert"
return|;
block|}
specifier|public
name|RevertAction
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
name|Project
name|project
init|=
name|getCurrentProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|isNew
init|=
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|==
literal|null
decl_stmt|;
name|CayenneModelerController
name|controller
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
decl_stmt|;
comment|// close ... don't use OpenProjectAction close method as it will ask for save, we
comment|// don't want that here
name|controller
operator|.
name|projectClosedAction
argument_list|()
expr_stmt|;
name|File
name|fileDirectory
init|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
comment|// reopen existing
if|if
condition|(
operator|!
name|isNew
operator|&&
name|fileDirectory
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|OpenProjectAction
name|openAction
init|=
name|controller
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|openAction
operator|.
name|openProject
argument_list|(
name|fileDirectory
argument_list|)
expr_stmt|;
block|}
comment|// create new
if|else if
condition|(
operator|!
operator|(
name|project
operator|instanceof
name|Project
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only ApplicationProjects are supported."
argument_list|)
throw|;
block|}
else|else
block|{
name|controller
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|NewProjectAction
operator|.
name|class
argument_list|)
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|discardAllEdits
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

