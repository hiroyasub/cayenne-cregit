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
name|net
operator|.
name|URL
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
name|Preferences
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
name|dialog
operator|.
name|validator
operator|.
name|ValidationDisplayHandler
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
name|pref
operator|.
name|Domain
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
name|ProjectSaver
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
name|validate
operator|.
name|Validator
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
name|validate
operator|.
name|ValidationInfo
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
name|validate
operator|.
name|DefaultValidator
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
name|resource
operator|.
name|URLResource
import|;
end_import

begin_comment
comment|/**  * A "Save As" action that allows user to pick save location.  *   */
end_comment

begin_class
specifier|public
class|class
name|SaveAsAction
extends|extends
name|CayenneAction
block|{
specifier|protected
name|ProjectOpener
name|fileChooser
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Save As..."
return|;
block|}
specifier|public
name|SaveAsAction
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
specifier|protected
name|SaveAsAction
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
name|this
operator|.
name|fileChooser
operator|=
operator|new
name|ProjectOpener
argument_list|()
expr_stmt|;
block|}
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
name|VK_S
argument_list|,
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getMenuShortcutKeyMask
argument_list|()
operator||
name|ActionEvent
operator|.
name|SHIFT_MASK
argument_list|)
return|;
block|}
comment|/**      * Saves project and related files. Saving is done to temporary files, and only on      * successful save, master files are replaced with new versions.      */
specifier|protected
name|boolean
name|saveAll
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|p
init|=
name|getCurrentProject
argument_list|()
decl_stmt|;
name|String
name|oldPath
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|oldPath
operator|=
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
comment|// obtain preference object before save, when the project path may change.....
name|Domain
name|preference
init|=
name|getProjectController
argument_list|()
operator|.
name|getPreferenceDomainForProject
argument_list|()
decl_stmt|;
name|File
name|projectDir
init|=
name|fileChooser
operator|.
name|newProjectDir
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectDir
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|projectDir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|projectDir
operator|.
name|canWrite
argument_list|()
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Can't save project - unable to write to file \""
operator|+
name|projectDir
operator|.
name|getPath
argument_list|()
operator|+
literal|"\""
argument_list|,
literal|"Can't Save Project"
argument_list|,
name|JOptionPane
operator|.
name|OK_OPTION
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|getProjectController
argument_list|()
operator|.
name|getProjectWatcher
argument_list|()
operator|.
name|pauseWatching
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|projectDir
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// /!!!!!!!!!!!!!!!!!!! SAVE AS!!!!!!!!!!!!!!
name|ProjectSaver
name|saver
init|=
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ProjectSaver
operator|.
name|class
argument_list|)
decl_stmt|;
name|saver
operator|.
name|saveAs
argument_list|(
name|p
argument_list|,
name|res
argument_list|)
expr_stmt|;
comment|// update preferences domain key
name|preference
operator|.
name|rename
argument_list|(
name|projectDir
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldPath
operator|!=
literal|null
operator|&&
name|oldPath
operator|.
name|length
argument_list|()
operator|!=
literal|0
operator|&&
operator|!
name|oldPath
operator|.
name|equals
argument_list|(
name|p
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
condition|)
block|{
name|String
name|newName
init|=
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|replace
argument_list|(
literal|".xml"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|oldName
init|=
name|oldPath
operator|.
name|replace
argument_list|(
literal|".xml"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Preferences
name|oldPref
init|=
name|getProjectController
argument_list|()
operator|.
name|getPreferenceForProject
argument_list|()
decl_stmt|;
name|String
name|projPath
init|=
name|oldPref
operator|.
name|absolutePath
argument_list|()
operator|.
name|replace
argument_list|(
name|oldName
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Preferences
name|newPref
init|=
name|getProjectController
argument_list|()
operator|.
name|getPreferenceForProject
argument_list|()
operator|.
name|node
argument_list|(
name|projPath
operator|+
name|newName
argument_list|)
decl_stmt|;
name|CayennePreferenceForProject
operator|.
name|copyPreferences
argument_list|(
name|newPref
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getPreferenceForProject
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|CayennePreferenceForProject
operator|.
name|removeNewPreferences
argument_list|()
expr_stmt|;
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|addToLastProjListAction
argument_list|(
name|p
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
name|Application
operator|.
name|getFrame
argument_list|()
operator|.
name|fireRecentFileListChanged
argument_list|()
expr_stmt|;
comment|/**          * Reset the watcher now          */
name|getProjectController
argument_list|()
operator|.
name|getProjectWatcher
argument_list|()
operator|.
name|reconfigure
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * This method is synchronized to prevent problems on double-clicking "save".      */
specifier|public
specifier|synchronized
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|performAction
argument_list|(
name|ValidationDisplayHandler
operator|.
name|WARNING
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|performAction
parameter_list|(
name|int
name|warningLevel
parameter_list|)
block|{
name|Validator
name|validator
init|=
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|Validator
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|object
init|=
name|validator
operator|.
name|validate
argument_list|(
name|getCurrentProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|getCurrentProject
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|validationCode
init|=
operator|(
operator|(
name|DefaultValidator
operator|)
name|validator
operator|)
operator|.
name|getMaxSeverity
argument_list|()
decl_stmt|;
comment|// If no serious errors, perform save.
if|if
condition|(
name|validationCode
operator|<
name|ValidationDisplayHandler
operator|.
name|ERROR
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|saveAll
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error on save"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|projectSavedAction
argument_list|()
expr_stmt|;
block|}
comment|// If there were errors or warnings at validation, display them
if|if
condition|(
name|validationCode
operator|>=
name|warningLevel
condition|)
block|{
name|ValidatorDialog
operator|.
name|showDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns<code>true</code> if path contains a Project object and the project is      * modified.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Project
name|project
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|Project
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|project
operator|!=
literal|null
operator|&&
name|project
operator|.
name|isModified
argument_list|()
return|;
block|}
block|}
end_class

end_unit

