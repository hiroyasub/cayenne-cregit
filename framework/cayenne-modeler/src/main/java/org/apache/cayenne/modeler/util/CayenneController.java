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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Point
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Window
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
name|ActionListener
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
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeSupport
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
name|JComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFrame
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
name|swing
operator|.
name|BoundComponent
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
comment|/**  * A superclass of CayenneModeler controllers.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneController
implements|implements
name|BoundComponent
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
name|CayenneController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|CayenneController
name|parent
decl_stmt|;
specifier|protected
name|Application
name|application
decl_stmt|;
specifier|protected
name|PropertyChangeSupport
name|propertyChangeSupport
decl_stmt|;
specifier|public
name|CayenneController
parameter_list|(
name|CayenneController
name|parent
parameter_list|)
block|{
name|this
operator|.
name|application
operator|=
operator|(
name|parent
operator|!=
literal|null
operator|)
condition|?
name|parent
operator|.
name|getApplication
argument_list|()
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
specifier|public
name|CayenneController
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|this
operator|.
name|application
operator|=
name|application
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
specifier|public
name|CayenneController
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/**      * Returns the vie wassociated with this Controller.      */
specifier|public
specifier|abstract
name|Component
name|getView
parameter_list|()
function_decl|;
comment|/**      * Returns last file system directory visited by user for this component. If there is      * no such directory set up in the preferences, creates a new object, setting its path      * to the parent last directory or to the user HOME directory.      */
specifier|public
name|FSPath
name|getLastDirectory
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
literal|"lastDir"
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
name|String
name|pathString
init|=
operator|(
name|getParent
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getParent
argument_list|()
operator|.
name|getLastDirectory
argument_list|()
operator|.
name|getPath
argument_list|()
else|:
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
decl_stmt|;
name|path
operator|.
name|setPath
argument_list|(
name|pathString
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
comment|/**      * Returns preference for this component view.      */
specifier|protected
name|Preferences
name|getViewPreferences
parameter_list|()
block|{
if|if
condition|(
name|getApplication
argument_list|()
operator|.
name|getProject
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|getApplication
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|getView
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
return|;
block|}
name|Preferences
name|pref
init|=
name|getApplication
argument_list|()
operator|.
name|getMainPreferenceForProject
argument_list|()
decl_stmt|;
name|String
name|pathToProject
init|=
name|pref
operator|.
name|absolutePath
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|pathToProject
operator|+
literal|"/"
operator|+
name|getView
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
return|return
name|pref
operator|.
name|node
argument_list|(
name|path
argument_list|)
return|;
block|}
comment|/**      * Utility method to provide a visual indication an execution error. This      * implementation logs an error and pops up a dialog window with error message.      */
specifier|protected
name|void
name|reportError
parameter_list|(
name|String
name|title
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
name|logObj
operator|.
name|info
argument_list|(
literal|"Error in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|th
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|getView
argument_list|()
argument_list|,
name|th
operator|.
name|getMessage
argument_list|()
argument_list|,
name|title
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Centers view on parent window.      */
specifier|protected
name|void
name|centerView
parameter_list|()
block|{
name|Window
name|parentWindow
init|=
name|parent
operator|.
name|getWindow
argument_list|()
decl_stmt|;
name|Dimension
name|parentSize
init|=
name|parentWindow
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|Dimension
name|windowSize
init|=
name|getView
argument_list|()
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|Point
name|parentLocation
init|=
operator|new
name|Point
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentWindow
operator|.
name|isShowing
argument_list|()
condition|)
block|{
name|parentLocation
operator|=
name|parentWindow
operator|.
name|getLocationOnScreen
argument_list|()
expr_stmt|;
block|}
name|int
name|x
init|=
name|parentLocation
operator|.
name|x
operator|+
name|parentSize
operator|.
name|width
operator|/
literal|2
operator|-
name|windowSize
operator|.
name|width
operator|/
literal|2
decl_stmt|;
name|int
name|y
init|=
name|parentLocation
operator|.
name|y
operator|+
name|parentSize
operator|.
name|height
operator|/
literal|2
operator|-
name|windowSize
operator|.
name|height
operator|/
literal|2
decl_stmt|;
name|getView
argument_list|()
operator|.
name|setLocation
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
comment|/**      * If this view or a parent view is a JDialog, makes it closeable on ESC hit. Dialog      * "defaultCloseOperation" property is taken into account when processing ESC button      * click.      */
specifier|protected
name|void
name|makeCloseableOnEscape
parameter_list|()
block|{
name|Window
name|window
init|=
name|getWindow
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|window
operator|instanceof
name|JDialog
operator|)
condition|)
block|{
return|return;
block|}
specifier|final
name|JDialog
name|dialog
init|=
operator|(
name|JDialog
operator|)
name|window
decl_stmt|;
name|KeyStroke
name|escReleased
init|=
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_ESCAPE
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ActionListener
name|closeAction
init|=
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|dialog
operator|.
name|isVisible
argument_list|()
condition|)
block|{
switch|switch
condition|(
name|dialog
operator|.
name|getDefaultCloseOperation
argument_list|()
condition|)
block|{
case|case
name|JDialog
operator|.
name|HIDE_ON_CLOSE
case|:
name|dialog
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|JDialog
operator|.
name|DISPOSE_ON_CLOSE
case|:
name|dialog
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|dispose
argument_list|()
expr_stmt|;
break|break;
case|case
name|JDialog
operator|.
name|DO_NOTHING_ON_CLOSE
case|:
default|default:
break|break;
block|}
block|}
block|}
block|}
decl_stmt|;
name|dialog
operator|.
name|getRootPane
argument_list|()
operator|.
name|registerKeyboardAction
argument_list|(
name|closeAction
argument_list|,
name|escReleased
argument_list|,
name|JComponent
operator|.
name|WHEN_IN_FOCUSED_WINDOW
argument_list|)
expr_stmt|;
block|}
comment|/**      * Finds a Window in the view hierarchy.      */
specifier|public
name|Window
name|getWindow
parameter_list|()
block|{
name|Component
name|view
init|=
name|getView
argument_list|()
decl_stmt|;
while|while
condition|(
name|view
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|view
operator|instanceof
name|Window
condition|)
block|{
return|return
operator|(
name|Window
operator|)
name|view
return|;
block|}
name|view
operator|=
name|view
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds a JFrame in the view hierarchy.      */
specifier|public
name|JFrame
name|getFrame
parameter_list|()
block|{
name|Component
name|view
init|=
name|getView
argument_list|()
decl_stmt|;
while|while
condition|(
name|view
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|view
operator|instanceof
name|JFrame
condition|)
block|{
return|return
operator|(
name|JFrame
operator|)
name|view
return|;
block|}
name|view
operator|=
name|view
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Fires property change event. Exists for the benefit of subclasses.      */
specifier|protected
name|void
name|firePropertyChange
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|propertyChangeSupport
operator|!=
literal|null
condition|)
block|{
name|propertyChangeSupport
operator|.
name|firePropertyChange
argument_list|(
name|propertyName
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds a listener for property change events.      */
specifier|public
name|void
name|addPropertyChangeListener
parameter_list|(
name|String
name|expression
parameter_list|,
name|PropertyChangeListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|propertyChangeSupport
operator|==
literal|null
condition|)
block|{
name|propertyChangeSupport
operator|=
operator|new
name|PropertyChangeSupport
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|propertyChangeSupport
operator|.
name|addPropertyChangeListener
argument_list|(
name|expression
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default implementation is a noop. Override to handle parent binding updates.      */
specifier|public
name|void
name|bindingUpdated
parameter_list|(
name|String
name|expression
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
comment|// do nothing...
block|}
block|}
end_class

end_unit

