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
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|DataChannelDescriptor
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
name|DataMap
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
name|OpenProjectAction
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
name|SaveAction
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
name|FileDeletedDialog
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
name|FileWatchdog
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
name|project2
operator|.
name|Project
import|;
end_import

begin_comment
comment|/**  * ProjectWatchdog class is responsible for tracking changes in cayenne.xml and other  * Cayenne project files  *   */
end_comment

begin_class
specifier|public
class|class
name|ProjectWatchdog
extends|extends
name|FileWatchdog
block|{
comment|/**      * A project to watch      */
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
comment|/**      * Creates new watchdog for a specified project      */
specifier|public
name|ProjectWatchdog
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|setName
argument_list|(
literal|"cayenne-project-watchdog"
argument_list|)
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|setSingleNotification
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// one message is more than enough
block|}
comment|/**      * Reloads files to watch from the project. Useful when project's structure has      * changed      */
specifier|public
name|void
name|reconfigure
parameter_list|()
block|{
name|pauseWatching
argument_list|()
expr_stmt|;
name|removeAllFiles
argument_list|()
expr_stmt|;
name|Project
name|project
init|=
name|mediator
operator|.
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|project
operator|!=
literal|null
comment|// project opened
operator|&&
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|!=
literal|null
condition|)
comment|// not new project
block|{
name|String
name|projectPath
init|=
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
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|addFile
argument_list|(
name|projectPath
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DataMap
argument_list|>
name|it
init|=
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|project
operator|.
name|getRootNode
argument_list|()
operator|)
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|dm
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|addFile
argument_list|(
name|dm
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|resumeWatching
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doOnChange
parameter_list|(
name|FileInfo
name|fileInfo
parameter_list|)
block|{
if|if
condition|(
name|showConfirmation
argument_list|(
literal|"One or more project files were changed by external program. "
operator|+
literal|"Do you want to load the changes?"
argument_list|)
condition|)
block|{
comment|/**              * Currently we are reloading all project              */
if|if
condition|(
name|mediator
operator|.
name|getProject
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|File
name|fileDirectory
init|=
operator|new
name|File
argument_list|(
name|mediator
operator|.
name|getProject
argument_list|()
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
operator|(
operator|(
name|OpenProjectAction
operator|)
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|)
operator|.
name|openProject
argument_list|(
name|fileDirectory
argument_list|)
expr_stmt|;
block|}
block|}
else|else
name|mediator
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doOnRemove
parameter_list|(
name|FileInfo
name|fileInfo
parameter_list|)
block|{
if|if
condition|(
name|mediator
operator|.
name|getProject
argument_list|()
operator|!=
literal|null
comment|/*&& fileInfo.getFile().equals(mediator.getProject().getMainFile()) */
condition|)
block|{
name|FileDeletedDialog
name|dialog
init|=
operator|new
name|FileDeletedDialog
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
name|shouldSave
argument_list|()
condition|)
block|{
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
name|performAction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|dialog
operator|.
name|shouldClose
argument_list|()
condition|)
block|{
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
name|controller
operator|.
name|projectClosedAction
argument_list|()
expr_stmt|;
block|}
else|else
name|mediator
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Shows confirmation dialog      */
specifier|private
name|boolean
name|showConfirmation
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
name|JOptionPane
operator|.
name|YES_OPTION
operator|==
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|message
argument_list|,
literal|"File changed"
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|,
name|JOptionPane
operator|.
name|QUESTION_MESSAGE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

