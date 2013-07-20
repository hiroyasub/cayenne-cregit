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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
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
name|project
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * ProjectWatchdog class is responsible for tracking changes in cayenne.xml and  * other Cayenne project files  *   */
end_comment

begin_class
specifier|public
class|class
name|ProjectFileChangeTracker
extends|extends
name|Thread
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ProjectFileChangeTracker
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The default delay between every file modification check      */
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_DELAY
init|=
literal|4000
decl_stmt|;
comment|/**      * The names of the files to observe for changes.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files
decl_stmt|;
specifier|protected
name|boolean
name|paused
decl_stmt|;
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|public
name|ProjectFileChangeTracker
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|files
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|setName
argument_list|(
literal|"cayenne-modeler-file-change-tracker"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reloads files to watch from the project. Useful when project's structure      * has changed      */
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
comment|// check if project exists and has been saved at least once.
if|if
condition|(
name|project
operator|!=
literal|null
operator|&&
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|!=
literal|null
condition|)
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
specifier|protected
name|void
name|doOnChange
parameter_list|()
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
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
comment|// Currently we are reloading all project
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
name|Application
operator|.
name|getInstance
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
operator|.
name|openProject
argument_list|(
name|fileDirectory
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|mediator
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doOnRemove
parameter_list|()
block|{
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
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
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
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|SaveAction
operator|.
name|class
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
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|projectClosedAction
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|mediator
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
comment|/**      * Adds a new file to watch      *       * @param location      *            path of file      */
specifier|public
name|void
name|addFile
parameter_list|(
name|String
name|location
parameter_list|)
block|{
try|try
block|{
name|files
operator|.
name|put
argument_list|(
name|location
argument_list|,
operator|new
name|FileInfo
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"SecurityException adding file "
operator|+
name|location
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Turns off watching for a specified file      *       * @param location      *            path of file      */
specifier|public
name|void
name|removeFile
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|files
operator|.
name|remove
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
comment|/**      * Turns off watching for all files      */
specifier|public
name|void
name|removeAllFiles
parameter_list|()
block|{
name|files
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|check
parameter_list|()
block|{
if|if
condition|(
name|paused
condition|)
block|{
return|return;
block|}
name|boolean
name|hasChanges
init|=
literal|false
decl_stmt|;
name|boolean
name|hasDeletions
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|FileInfo
argument_list|>
name|it
init|=
name|files
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|FileInfo
name|fi
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|fileExists
decl_stmt|;
try|try
block|{
name|fileExists
operator|=
name|fi
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"SecurityException checking file "
operator|+
name|fi
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// we still process with other files
continue|continue;
block|}
if|if
condition|(
name|fileExists
condition|)
block|{
comment|// this can also throw a SecurityException
name|long
name|l
init|=
name|fi
operator|.
name|getFile
argument_list|()
operator|.
name|lastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|>
name|fi
operator|.
name|getLastModified
argument_list|()
condition|)
block|{
comment|// however, if we reached this point this is very unlikely.
name|fi
operator|.
name|setLastModified
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|hasChanges
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// the file has been removed
if|else if
condition|(
name|fi
operator|.
name|getLastModified
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|hasDeletions
operator|=
literal|true
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// no point to watch the file now
block|}
block|}
if|if
condition|(
name|hasDeletions
condition|)
block|{
name|doOnRemove
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|hasChanges
condition|)
block|{
name|doOnChange
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|DEFAULT_DELAY
argument_list|)
expr_stmt|;
name|check
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// someone asked to stop
return|return;
block|}
block|}
block|}
comment|/**      * Tells watcher to pause watching for some time. Useful before changing      * files      */
specifier|public
name|void
name|pauseWatching
parameter_list|()
block|{
name|paused
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Resumes watching for files      */
specifier|public
name|void
name|resumeWatching
parameter_list|()
block|{
name|paused
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Class to store information about files (last modification time& File      * pointer)      */
specifier|protected
class|class
name|FileInfo
block|{
comment|/**          * Exact java.io.File object, may not be null          */
name|File
name|file
decl_stmt|;
comment|/**          * Time the file was modified          */
name|long
name|lastModified
decl_stmt|;
comment|/**          * Creates new object          *           * @param location          *            the file path          */
specifier|public
name|FileInfo
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|lastModified
operator|=
name|file
operator|.
name|exists
argument_list|()
condition|?
name|file
operator|.
name|lastModified
argument_list|()
else|:
operator|-
literal|1
expr_stmt|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
specifier|public
name|void
name|setLastModified
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|lastModified
operator|=
name|l
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

