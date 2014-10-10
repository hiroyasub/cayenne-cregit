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
name|ExitAction
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
name|dialog
operator|.
name|validator
operator|.
name|ValidatorDialog
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
name|EditorView
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
name|init
operator|.
name|platform
operator|.
name|PlatformInitializer
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
name|pref
operator|.
name|ComponentGeometry
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
name|pref
operator|.
name|FSPath
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
name|CayenneController
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
name|FileFilters
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
name|cayenne
operator|.
name|project
operator|.
name|validation
operator|.
name|ProjectValidator
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
name|validation
operator|.
name|ValidationFailure
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|WindowConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|filechooser
operator|.
name|FileFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|DataFlavor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|Transferable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|dnd
operator|.
name|DropTarget
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|dnd
operator|.
name|DropTargetAdapter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|dnd
operator|.
name|DropTargetDropEvent
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
name|WindowAdapter
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
name|WindowEvent
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|BackingStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_comment
comment|/**  * Controller of the main application frame.  */
end_comment

begin_class
specifier|public
class|class
name|CayenneModelerController
extends|extends
name|CayenneController
block|{
specifier|protected
name|ProjectController
name|projectController
decl_stmt|;
specifier|protected
name|CayenneModelerFrame
name|frame
decl_stmt|;
specifier|private
name|EditorView
name|editorView
decl_stmt|;
specifier|public
name|CayenneModelerController
parameter_list|()
block|{
block|}
specifier|public
name|CayenneModelerController
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|application
argument_list|)
expr_stmt|;
name|this
operator|.
name|frame
operator|=
operator|new
name|CayenneModelerFrame
argument_list|(
name|application
operator|.
name|getActionManager
argument_list|()
argument_list|)
expr_stmt|;
name|application
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|PlatformInitializer
operator|.
name|class
argument_list|)
operator|.
name|setupMenus
argument_list|(
name|frame
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectController
operator|=
operator|new
name|ProjectController
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|frame
return|;
block|}
specifier|public
name|ProjectController
name|getProjectController
parameter_list|()
block|{
return|return
name|projectController
return|;
block|}
specifier|public
name|FSPath
name|getLastEOModelDirectory
parameter_list|()
block|{
comment|// find start directory in preferences
name|FSPath
name|path
init|=
operator|(
name|FSPath
operator|)
name|application
operator|.
name|getCayenneProjectPreferences
argument_list|()
operator|.
name|getProjectDetailObject
argument_list|(
name|FSPath
operator|.
name|class
argument_list|,
name|getViewPreferences
argument_list|()
operator|.
name|node
argument_list|(
literal|"lastEOMDir"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|getPath
argument_list|()
operator|==
literal|null
condition|)
block|{
name|path
operator|.
name|setPath
argument_list|(
name|getLastDirectory
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|frame
operator|.
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DO_NOTHING_ON_CLOSE
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addWindowListener
argument_list|(
operator|new
name|WindowAdapter
argument_list|()
block|{
specifier|public
name|void
name|windowClosing
parameter_list|(
name|WindowEvent
name|e
parameter_list|)
block|{
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|ExitAction
operator|.
name|class
argument_list|)
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
operator|new
name|DropTarget
argument_list|(
name|frame
argument_list|,
operator|new
name|DropTargetAdapter
argument_list|()
block|{
specifier|public
name|void
name|drop
parameter_list|(
name|DropTargetDropEvent
name|dtde
parameter_list|)
block|{
name|dtde
operator|.
name|acceptDrop
argument_list|(
name|dtde
operator|.
name|getDropAction
argument_list|()
argument_list|)
expr_stmt|;
name|Transferable
name|transferable
init|=
name|dtde
operator|.
name|getTransferable
argument_list|()
decl_stmt|;
name|dtde
operator|.
name|dropComplete
argument_list|(
name|processDropAction
argument_list|(
name|transferable
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ComponentGeometry
name|geometry
init|=
operator|new
name|ComponentGeometry
argument_list|(
name|frame
operator|.
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|geometry
operator|.
name|bind
argument_list|(
name|frame
argument_list|,
literal|650
argument_list|,
literal|550
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|processDropAction
parameter_list|(
name|Transferable
name|transferable
parameter_list|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|fileList
decl_stmt|;
try|try
block|{
name|fileList
operator|=
operator|(
name|List
operator|)
name|transferable
operator|.
name|getTransferData
argument_list|(
name|DataFlavor
operator|.
name|javaFileListFlavor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|File
name|transferFile
init|=
name|fileList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|transferFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|FileFilter
name|filter
init|=
name|FileFilters
operator|.
name|getApplicationFilter
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
operator|.
name|accept
argument_list|(
name|transferFile
argument_list|)
condition|)
block|{
name|ActionEvent
name|e
init|=
operator|new
name|ActionEvent
argument_list|(
name|transferFile
argument_list|,
name|ActionEvent
operator|.
name|ACTION_PERFORMED
argument_list|,
literal|"OpenProject"
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
name|actionPerformed
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|initBindings
argument_list|()
expr_stmt|;
name|frame
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|projectModifiedAction
parameter_list|()
block|{
name|String
name|title
init|=
operator|(
name|projectController
operator|.
name|getProject
argument_list|()
operator|.
name|getConfigurationResource
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|"[New]"
else|:
name|projectController
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
decl_stmt|;
name|frame
operator|.
name|setTitle
argument_list|(
literal|"* - "
operator|+
name|ModelerConstants
operator|.
name|TITLE
operator|+
literal|" - "
operator|+
name|title
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|projectSavedAction
parameter_list|()
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|updateProjectControllerPreferences
argument_list|()
expr_stmt|;
name|updateStatus
argument_list|(
literal|"Project saved..."
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setTitle
argument_list|(
name|ModelerConstants
operator|.
name|TITLE
operator|+
literal|" - "
operator|+
name|projectController
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
expr_stmt|;
block|}
comment|/**      * Action method invoked on project closing.      */
specifier|public
name|void
name|projectClosedAction
parameter_list|()
block|{
comment|// --- update view
name|frame
operator|.
name|setView
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// repaint is needed, since sometimes there is a
comment|// trace from menu left on the screen
name|frame
operator|.
name|repaint
argument_list|()
expr_stmt|;
name|frame
operator|.
name|setTitle
argument_list|(
name|ModelerConstants
operator|.
name|TITLE
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|setProject
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|reset
argument_list|()
expr_stmt|;
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|projectClosed
argument_list|()
expr_stmt|;
name|updateStatus
argument_list|(
literal|"Project Closed..."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Handles project opening control. Updates main frame, then delegates control to      * child controllers.      */
specifier|public
name|void
name|projectOpenedAction
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|projectController
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|editorView
operator|=
operator|new
name|EditorView
argument_list|(
name|projectController
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setView
argument_list|(
name|editorView
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|projectOpened
argument_list|()
expr_stmt|;
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|projectOpened
argument_list|()
expr_stmt|;
comment|// do status update AFTER the project is actually opened...
if|if
condition|(
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|==
literal|null
condition|)
block|{
name|updateStatus
argument_list|(
literal|"New project created..."
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setTitle
argument_list|(
name|ModelerConstants
operator|.
name|TITLE
operator|+
literal|"- [New]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateStatus
argument_list|(
literal|"Project opened..."
argument_list|)
expr_stmt|;
name|frame
operator|.
name|setTitle
argument_list|(
name|ModelerConstants
operator|.
name|TITLE
operator|+
literal|" - "
operator|+
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
expr_stmt|;
block|}
comment|// update preferences
if|if
condition|(
name|project
operator|.
name|getConfigurationResource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getLastDirectory
argument_list|()
operator|.
name|setDirectory
argument_list|(
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
argument_list|)
expr_stmt|;
name|frame
operator|.
name|fireRecentFileListChanged
argument_list|()
expr_stmt|;
block|}
comment|// for validation purposes combine load failures with post-load validation (not
comment|// sure if that'll cause duplicate messages?).
name|List
argument_list|<
name|ValidationFailure
argument_list|>
name|allFailures
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationFailure
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ValidationFailure
argument_list|>
name|loadFailures
init|=
name|project
operator|.
name|getConfigurationTree
argument_list|()
operator|.
name|getLoadFailures
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|loadFailures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// mark project as unsaved
name|project
operator|.
name|setModified
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|allFailures
operator|.
name|addAll
argument_list|(
name|loadFailures
argument_list|)
expr_stmt|;
block|}
name|ProjectValidator
name|projectValidator
init|=
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ProjectValidator
operator|.
name|class
argument_list|)
decl_stmt|;
name|ValidationResult
name|validationResult
init|=
name|projectValidator
operator|.
name|validate
argument_list|(
name|project
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
name|allFailures
operator|.
name|addAll
argument_list|(
name|validationResult
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|allFailures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ValidatorDialog
operator|.
name|showDialog
argument_list|(
name|frame
argument_list|,
name|validationResult
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|EditorView
name|getEditorView
parameter_list|()
block|{
return|return
name|editorView
return|;
block|}
comment|/** Adds path to the list of last opened projects in preferences. */
specifier|public
name|void
name|addToLastProjListAction
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|Preferences
name|frefLastProjFiles
init|=
name|ModelerPreferences
operator|.
name|getLastProjFilesPref
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|arr
init|=
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
decl_stmt|;
comment|// Add proj path to the preferences
comment|// Prevent duplicate entries.
if|if
condition|(
name|arr
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|path
argument_list|)
expr_stmt|;
while|while
condition|(
name|arr
operator|.
name|size
argument_list|()
operator|>
name|ModelerPreferences
operator|.
name|LAST_PROJ_FILES_SIZE
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|arr
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|frefLastProjFiles
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BackingStoreException
name|e
parameter_list|)
block|{
comment|// ignore exception
block|}
name|int
name|size
init|=
name|arr
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|frefLastProjFiles
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|,
name|arr
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Performs status bar update with a message. Message will dissappear in 6 seconds.      */
specifier|public
name|void
name|updateStatus
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|frame
operator|.
name|getStatus
argument_list|()
operator|.
name|setText
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// start message cleanup thread that would remove the message after X seconds
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Thread
name|cleanup
init|=
operator|new
name|ExpireThread
argument_list|(
name|message
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
class|class
name|ExpireThread
extends|extends
name|Thread
block|{
specifier|protected
name|int
name|seconds
decl_stmt|;
specifier|protected
name|String
name|message
decl_stmt|;
specifier|public
name|ExpireThread
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|seconds
parameter_list|)
block|{
name|this
operator|.
name|seconds
operator|=
name|seconds
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|sleep
argument_list|(
name|seconds
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore exception
block|}
if|if
condition|(
name|message
operator|.
name|equals
argument_list|(
name|frame
operator|.
name|getStatus
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|updateStatus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|changePathInLastProjListAction
parameter_list|(
name|String
name|oldPath
parameter_list|,
name|String
name|newPath
parameter_list|)
block|{
name|Preferences
name|frefLastProjFiles
init|=
name|ModelerPreferences
operator|.
name|getLastProjFilesPref
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|arr
init|=
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
decl_stmt|;
comment|// Add proj path to the preferences
comment|// Prevent duplicate entries.
if|if
condition|(
name|arr
operator|.
name|contains
argument_list|(
name|oldPath
argument_list|)
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|oldPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arr
operator|.
name|contains
argument_list|(
name|newPath
argument_list|)
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|newPath
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|newPath
argument_list|)
expr_stmt|;
while|while
condition|(
name|arr
operator|.
name|size
argument_list|()
operator|>
name|ModelerPreferences
operator|.
name|LAST_PROJ_FILES_SIZE
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|arr
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|frefLastProjFiles
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BackingStoreException
name|e
parameter_list|)
block|{
comment|// ignore exception
block|}
name|int
name|size
init|=
name|arr
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|frefLastProjFiles
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|,
name|arr
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getLastDirectory
argument_list|()
operator|.
name|setDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|newPath
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|fireRecentFileListChanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

