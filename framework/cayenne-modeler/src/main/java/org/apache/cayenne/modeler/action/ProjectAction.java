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
name|conf
operator|.
name|Configuration
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
name|dialog
operator|.
name|UnsavedChangesDialog
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
name|ProjectConfiguration
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ProjectAction
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
literal|"Close Project"
return|;
block|}
specifier|public
name|ProjectAction
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
comment|/**      * Constructor for ProjectAction.      *       * @param name      */
specifier|public
name|ProjectAction
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
comment|/**      * Closes current project.      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|closeProject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a configuration suitable for use in the Modeler. Used mainly by subclasses.      *       * @since 1.2      */
specifier|protected
name|Configuration
name|buildProjectConfiguration
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|ProjectConfiguration
name|config
init|=
operator|new
name|ModelerProjectConfiguration
argument_list|(
name|projectFile
argument_list|)
decl_stmt|;
name|config
operator|.
name|setLoaderDelegate
argument_list|(
operator|new
name|ModelerProjectLoadDelegate
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSaverDelegate
argument_list|(
operator|new
name|ModelerProjectSaveDelegate
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
comment|/** Returns true if successfully closed project, false otherwise. */
specifier|public
name|boolean
name|closeProject
parameter_list|(
name|boolean
name|checkUnsaved
parameter_list|)
block|{
comment|// check if there is a project...
if|if
condition|(
name|getProjectController
argument_list|()
operator|==
literal|null
operator|||
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|checkUnsaved
operator|&&
operator|!
name|checkSaveOnClose
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|CayenneModelerController
name|controller
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
decl_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|discardAllEdits
argument_list|()
expr_stmt|;
name|controller
operator|.
name|projectClosedAction
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Returns false if cancel closing the window, true otherwise.      */
specifier|public
name|boolean
name|checkSaveOnClose
parameter_list|()
block|{
name|ProjectController
name|projectController
init|=
name|getProjectController
argument_list|()
decl_stmt|;
if|if
condition|(
name|projectController
operator|!=
literal|null
operator|&&
name|projectController
operator|.
name|isDirty
argument_list|()
condition|)
block|{
name|UnsavedChangesDialog
name|dialog
init|=
operator|new
name|UnsavedChangesDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
decl_stmt|;
name|dialog
operator|.
name|show
argument_list|()
expr_stmt|;
if|if
condition|(
name|dialog
operator|.
name|shouldCancel
argument_list|()
condition|)
block|{
comment|// discard changes and DO NOT close
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|setQuittingApplication
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|else if
condition|(
name|dialog
operator|.
name|shouldSave
argument_list|()
condition|)
block|{
comment|// save changes and close
name|ActionEvent
name|e
init|=
operator|new
name|ActionEvent
argument_list|(
name|this
argument_list|,
name|ActionEvent
operator|.
name|ACTION_PERFORMED
argument_list|,
literal|"SaveAll"
argument_list|)
decl_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|SaveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectController
operator|.
name|isDirty
argument_list|()
condition|)
block|{
comment|// save was canceled... do not close
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Always returns true.      */
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

