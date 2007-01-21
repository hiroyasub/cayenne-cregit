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
name|dialog
operator|.
name|ErrorDebugDialog
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
name|ApplicationProject
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
name|ProjectException
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
name|swing
operator|.
name|control
operator|.
name|FileMenuItem
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
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|OpenProjectAction
extends|extends
name|ProjectAction
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|OpenProjectAction
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"Open Project"
return|;
block|}
comment|/**      * Constructor for OpenProjectAction.      */
specifier|public
name|OpenProjectAction
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
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-open.gif"
return|;
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
name|VK_O
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
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
comment|// Save and close (if needed) currently open project.
if|if
condition|(
name|getProjectController
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|checkSaveOnClose
argument_list|()
condition|)
block|{
return|return;
block|}
name|File
name|f
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|instanceof
name|FileMenuItem
condition|)
block|{
name|FileMenuItem
name|menu
init|=
operator|(
name|FileMenuItem
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|f
operator|=
name|menu
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// Get the project file name (always cayenne.xml)
name|f
operator|=
name|fileChooser
operator|.
name|openProjectFile
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error loading project file."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
comment|// by now if the project is unsaved, this has been a user choice...
if|if
condition|(
name|getProjectController
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|closeProject
argument_list|(
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
name|openProject
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Opens specified project file. File must already exist. */
specifier|public
name|void
name|openProject
parameter_list|(
name|File
name|file
parameter_list|)
block|{
try|try
block|{
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|addToLastProjListAction
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|buildProjectConfiguration
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Project
name|project
init|=
operator|new
name|ApplicationProject
argument_list|(
name|file
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
comment|// if upgrade was canceled
name|int
name|upgradeStatus
init|=
name|project
operator|.
name|getUpgradeStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|upgradeStatus
operator|>
literal|0
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
literal|"Can't open project - it was created using a newer version of the Modeler"
argument_list|,
literal|"Can't Open Project"
argument_list|,
name|JOptionPane
operator|.
name|OK_OPTION
argument_list|)
expr_stmt|;
name|closeProject
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|upgradeStatus
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|processUpgrades
argument_list|(
name|project
argument_list|)
condition|)
block|{
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|projectOpenedAction
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|closeProject
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|projectOpenedAction
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error loading project file."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|ErrorDebugDialog
operator|.
name|guiWarning
argument_list|(
name|ex
argument_list|,
literal|"Error loading project"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|processUpgrades
parameter_list|(
name|Project
name|project
parameter_list|)
throws|throws
name|ProjectException
block|{
comment|// must really concat all messages, this is a temp hack...
name|String
name|msg
init|=
operator|(
name|String
operator|)
name|project
operator|.
name|getUpgradeMessages
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// need an upgrade
name|int
name|returnCode
init|=
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Project needs an upgrade to a newer version. "
operator|+
name|msg
operator|+
literal|". Upgrade?"
argument_list|,
literal|"Upgrade Needed"
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnCode
operator|==
name|JOptionPane
operator|.
name|NO_OPTION
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// perform upgrade
name|logObj
operator|.
name|info
argument_list|(
literal|"Will upgrade project "
operator|+
name|project
operator|.
name|getMainFile
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|upgrade
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

