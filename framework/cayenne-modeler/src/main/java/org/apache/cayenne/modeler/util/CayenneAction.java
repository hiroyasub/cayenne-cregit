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
name|util
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
name|AbstractAction
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
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBoxMenuItem
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Superclass of CayenneModeler actions that implements support for some common  * functionality, exception handling, etc.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneAction
extends|extends
name|AbstractAction
block|{
specifier|protected
name|boolean
name|alwaysOn
decl_stmt|;
specifier|protected
name|Application
name|application
decl_stmt|;
comment|/**      * Creates a named CayenneAction.      */
specifier|public
name|CayenneAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|application
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a named CayenneAction.      */
specifier|public
name|CayenneAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|,
name|String
name|shortDescription
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|super
operator|.
name|putValue
argument_list|(
name|Action
operator|.
name|DEFAULT
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
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
name|KeyStroke
name|accelerator
init|=
name|getAcceleratorKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|accelerator
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
name|ACCELERATOR_KEY
argument_list|,
name|accelerator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shortDescription
operator|!=
literal|null
operator|&&
name|shortDescription
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|super
operator|.
name|putValue
argument_list|(
name|Action
operator|.
name|SHORT_DESCRIPTION
argument_list|,
name|shortDescription
argument_list|)
expr_stmt|;
block|}
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Application
name|getApplication
parameter_list|()
block|{
return|return
name|application
return|;
block|}
specifier|protected
name|Project
name|getCurrentProject
parameter_list|()
block|{
return|return
name|application
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
comment|/**      * Changes the name of this action, propagating the change to all widgets using this      * action.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|)
argument_list|,
name|newName
argument_list|)
condition|)
block|{
name|super
operator|.
name|putValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns keyboard shortcut for this action. Default implementation returns      *<code>null</code>.      */
specifier|public
name|KeyStroke
name|getAcceleratorKey
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns the name of the icon that should be used for buttons. Name will be reolved      * relative to<code>RESOURCE_PATH</code>. Default implementation returns      *<code>null</code>.      */
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Creates and returns an ImageIcon that can be used for buttons that rely on this      * action. Returns<code>null</code> if<code>getIconName</code> returns      *<code>null</code>.      */
specifier|public
name|Icon
name|createIcon
parameter_list|()
block|{
name|String
name|name
init|=
name|getIconName
argument_list|()
decl_stmt|;
return|return
operator|(
name|name
operator|!=
literal|null
operator|)
condition|?
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
name|name
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Returns the key under which this action should be stored in the ActionMap.      */
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|super
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|DEFAULT
argument_list|)
return|;
block|}
comment|/**      * Subclasses must implement this method instead of<code>actionPerformed</code> to      * allow for exception handling.      */
specifier|public
specifier|abstract
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code> if the action is enabled for the specified "project      * path" - a path on the project tree to a currently selected object. Default      * implementation simply returns<code>false</code>.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|obj
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns current project controller.      */
specifier|public
name|ProjectController
name|getProjectController
parameter_list|()
block|{
return|return
name|application
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
return|;
block|}
comment|/**      * Internally calls<code>performAction</code>. Traps exceptions that ocurred      * during action processing.      */
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
try|try
block|{
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|ErrorDebugDialog
operator|.
name|guiException
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Factory method that creates a menu item hooked up to this action.      */
specifier|public
name|JMenuItem
name|buildMenu
parameter_list|()
block|{
return|return
operator|new
name|JMenuItem
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Factory method that creates a checkbox menu item hooked up to this action.      */
specifier|public
name|JCheckBoxMenuItem
name|buildCheckBoxMenu
parameter_list|()
block|{
return|return
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Factory method that creates a button hooked up to this action.      */
specifier|public
name|JButton
name|buildButton
parameter_list|()
block|{
return|return
operator|new
name|CayenneToolbarButton
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns true if this action is always enabled.      *       * @return boolean      */
specifier|public
name|boolean
name|isAlwaysOn
parameter_list|()
block|{
return|return
name|alwaysOn
return|;
block|}
comment|/**      * Sets the alwaysOn.      *       * @param alwaysOn The alwaysOn to set      */
specifier|public
name|void
name|setAlwaysOn
parameter_list|(
name|boolean
name|alwaysOn
parameter_list|)
block|{
name|this
operator|.
name|alwaysOn
operator|=
name|alwaysOn
expr_stmt|;
if|if
condition|(
name|alwaysOn
condition|)
block|{
name|super
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Overrides super implementation to take into account "alwaysOn" flag.      */
annotation|@
name|Override
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAlwaysOn
argument_list|()
condition|)
block|{
name|super
operator|.
name|setEnabled
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * On changes in action text, will update toolbar tip instead.      */
specifier|final
class|class
name|CayenneToolbarButton
extends|extends
name|JButton
block|{
specifier|protected
name|boolean
name|showingText
decl_stmt|;
comment|/**          * Constructor for CayenneMenuItem.          */
specifier|public
name|CayenneToolbarButton
parameter_list|(
name|Action
name|a
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|setAction
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**          * Returns the showingText.          */
specifier|public
name|boolean
name|isShowingText
parameter_list|()
block|{
return|return
name|showingText
return|;
block|}
comment|/**          * Sets the showingText.          */
specifier|public
name|void
name|setShowingText
parameter_list|(
name|boolean
name|showingText
parameter_list|)
block|{
name|this
operator|.
name|showingText
operator|=
name|showingText
expr_stmt|;
block|}
comment|/**          * @see javax.swing.AbstractButton#getText()          */
annotation|@
name|Override
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
operator|(
name|showingText
operator|)
condition|?
name|super
operator|.
name|getText
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**          * @see javax.swing.AbstractButton#setText(String)          */
annotation|@
name|Override
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|showingText
condition|)
block|{
name|super
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setToolTipText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

