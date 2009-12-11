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
name|dialog
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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
name|Clipboard
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
name|StringSelection
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Implementation for modeler log console functionality  */
end_comment

begin_class
specifier|public
class|class
name|LogConsole
extends|extends
name|CayenneController
block|{
comment|/**      * How much characters are allowed in console      */
specifier|private
specifier|static
specifier|final
name|int
name|TEXT_MAX_LENGTH
init|=
literal|500000
decl_stmt|;
comment|/**      * Property to store user preference      */
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_CONSOLE_PROPERTY
init|=
literal|"show.log.console"
decl_stmt|;
comment|/**      * Property to store 'is-docked' preference      */
specifier|public
specifier|static
specifier|final
name|String
name|DOCKED_PROPERTY
init|=
literal|"log.console.docked"
decl_stmt|;
comment|/**      * Message date format       */
specifier|private
specifier|static
specifier|final
name|DateFormat
name|FORMAT
init|=
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|()
decl_stmt|;
comment|/**      * Red color style for severe messages      */
specifier|public
specifier|static
specifier|final
name|MutableAttributeSet
name|ERROR_STYLE
decl_stmt|;
comment|/**      * Red bold color style for fatal messages      */
specifier|public
specifier|static
specifier|final
name|MutableAttributeSet
name|FATAL_STYLE
decl_stmt|;
comment|/**      * Dark red color style for warn messages      */
specifier|public
specifier|static
specifier|final
name|MutableAttributeSet
name|WARN_STYLE
decl_stmt|;
comment|/**      * Blue color style for info messages      */
specifier|public
specifier|static
specifier|final
name|MutableAttributeSet
name|INFO_STYLE
decl_stmt|;
comment|/**      * Style for debug messages      */
specifier|public
specifier|static
specifier|final
name|MutableAttributeSet
name|DEBUG_STYLE
decl_stmt|;
static|static
block|{
name|ERROR_STYLE
operator|=
operator|new
name|SimpleAttributeSet
argument_list|()
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|ERROR_STYLE
argument_list|,
name|Color
operator|.
name|RED
argument_list|)
expr_stmt|;
name|FATAL_STYLE
operator|=
operator|new
name|SimpleAttributeSet
argument_list|()
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|FATAL_STYLE
argument_list|,
name|Color
operator|.
name|RED
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setBold
argument_list|(
name|FATAL_STYLE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WARN_STYLE
operator|=
operator|new
name|SimpleAttributeSet
argument_list|()
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|WARN_STYLE
argument_list|,
name|Color
operator|.
name|RED
operator|.
name|darker
argument_list|()
argument_list|)
expr_stmt|;
name|INFO_STYLE
operator|=
operator|new
name|SimpleAttributeSet
argument_list|()
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|INFO_STYLE
argument_list|,
name|Color
operator|.
name|BLUE
argument_list|)
expr_stmt|;
name|DEBUG_STYLE
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Lone log console instance      */
specifier|private
specifier|static
name|LogConsole
name|instance
decl_stmt|;
comment|/**      * Swing console window      */
specifier|private
name|LogConsoleView
name|view
decl_stmt|;
comment|/**      * Window, which contains the console. Might be non-visible, if the console is docked      */
specifier|private
name|LogConsoleWindow
name|logWindow
decl_stmt|;
comment|/**      * Whether auto-scrolling should be enabled for the console text area.      * Currently not included in UI      */
specifier|private
name|boolean
name|autoScroll
decl_stmt|;
comment|/**      * Flag, indicating that no more logging to Swing component is appreciated.      * This is useful to prevent logging messages when they are no more needed, e.g. on      * JVM shutdown       */
specifier|private
name|boolean
name|loggingStopped
decl_stmt|;
specifier|public
name|LogConsole
parameter_list|()
block|{
name|super
argument_list|(
operator|(
name|CayenneController
operator|)
literal|null
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|LogConsoleView
argument_list|()
expr_stmt|;
name|autoScroll
operator|=
literal|true
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|view
operator|.
name|getClearButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
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
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCopyButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
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
name|copy
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getDockButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
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
comment|/**                  * Log console should be visible                  */
name|disappear
argument_list|()
expr_stmt|;
name|setConsoleProperty
argument_list|(
name|DOCKED_PROPERTY
argument_list|,
operator|!
name|getConsoleProperty
argument_list|(
name|DOCKED_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|appear
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clears the console      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|view
operator|.
name|getLogView
argument_list|()
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows the console, in separate window or in main frame       */
specifier|private
name|void
name|appear
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getConsoleProperty
argument_list|(
name|DOCKED_PROPERTY
argument_list|)
condition|)
block|{
name|view
operator|.
name|getDockButton
argument_list|()
operator|.
name|setText
argument_list|(
literal|"Dock"
argument_list|)
expr_stmt|;
if|if
condition|(
name|logWindow
operator|==
literal|null
condition|)
block|{
name|logWindow
operator|=
operator|new
name|LogConsoleWindow
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|ComponentGeometry
name|geometry
init|=
operator|new
name|ComponentGeometry
argument_list|(
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
name|logWindow
argument_list|,
literal|600
argument_list|,
literal|300
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|logWindow
operator|.
name|setContentPane
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|logWindow
operator|.
name|validate
argument_list|()
expr_stmt|;
name|logWindow
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|view
operator|.
name|getDockButton
argument_list|()
operator|.
name|setText
argument_list|(
literal|"Undock"
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getFrame
argument_list|()
operator|.
name|setDockComponent
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Hides the console       */
specifier|private
name|void
name|disappear
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getConsoleProperty
argument_list|(
name|DOCKED_PROPERTY
argument_list|)
condition|)
block|{
name|logWindow
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|logWindow
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|Application
operator|.
name|getFrame
argument_list|()
operator|.
name|setDockComponent
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Copies selected text from the console to system buffer      */
specifier|public
name|void
name|copy
parameter_list|()
block|{
name|String
name|selectedText
init|=
name|view
operator|.
name|getLogView
argument_list|()
operator|.
name|getSelectedText
argument_list|()
decl_stmt|;
comment|/**          * If nothing is selected, we copy the whole text          */
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|selectedText
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|view
operator|.
name|getLogView
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|selectedText
operator|=
name|doc
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|doc
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e
parameter_list|)
block|{
return|return;
block|}
block|}
name|Clipboard
name|sysClip
init|=
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getSystemClipboard
argument_list|()
decl_stmt|;
name|StringSelection
name|data
init|=
operator|new
name|StringSelection
argument_list|(
name|selectedText
argument_list|)
decl_stmt|;
name|sysClip
operator|.
name|setContents
argument_list|(
name|data
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows or hides the console window      */
specifier|public
name|void
name|toggle
parameter_list|()
block|{
name|boolean
name|needShow
init|=
operator|!
name|getConsoleProperty
argument_list|(
name|SHOW_CONSOLE_PROPERTY
argument_list|)
decl_stmt|;
name|setConsoleProperty
argument_list|(
name|SHOW_CONSOLE_PROPERTY
argument_list|,
name|needShow
argument_list|)
expr_stmt|;
if|if
condition|(
name|needShow
condition|)
block|{
name|appear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|disappear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Shows the console if the preference 'SHOW_CONSOLE_PROPERTY' is set to true       */
specifier|public
name|void
name|showConsoleIfNeeded
parameter_list|()
block|{
if|if
condition|(
name|getConsoleProperty
argument_list|(
name|SHOW_CONSOLE_PROPERTY
argument_list|)
condition|)
block|{
name|appear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Sets the property, depending on last user's choice      */
specifier|public
name|void
name|setConsoleProperty
parameter_list|(
name|String
name|prop
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|putBoolean
argument_list|(
name|prop
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return a boolean property      */
specifier|public
name|boolean
name|getConsoleProperty
parameter_list|(
name|String
name|prop
parameter_list|)
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|getBoolean
argument_list|(
name|prop
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Appends a message to the console.      * @param style Message font, color etc.      */
specifier|public
name|void
name|appendMessage
parameter_list|(
name|String
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|AttributeSet
name|style
parameter_list|)
block|{
name|appendMessage
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a message and (or) an exception      * @param style Message font, color etc.      */
specifier|public
name|void
name|appendMessage
parameter_list|(
name|String
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|ex
parameter_list|,
name|AttributeSet
name|style
parameter_list|)
block|{
if|if
condition|(
name|loggingStopped
condition|)
block|{
return|return;
block|}
name|Document
name|doc
init|=
name|view
operator|.
name|getLogView
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
comment|//truncate if needed
if|if
condition|(
name|doc
operator|.
name|getLength
argument_list|()
operator|>
name|TEXT_MAX_LENGTH
condition|)
block|{
name|clear
argument_list|()
expr_stmt|;
block|}
name|StringBuffer
name|newText
init|=
operator|new
name|StringBuffer
argument_list|(
name|FORMAT
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|level
operator|.
name|toUpperCase
argument_list|()
operator|+
literal|": "
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
comment|/**              * Append the message              */
name|newText
operator|.
name|append
argument_list|(
name|message
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
comment|/**              * Append the stack trace              */
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|printer
init|=
operator|new
name|PrintWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
name|printer
argument_list|)
expr_stmt|;
name|printer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|newText
operator|.
name|append
argument_list|(
name|out
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|doc
operator|.
name|insertString
argument_list|(
name|doc
operator|.
name|getLength
argument_list|()
argument_list|,
name|newText
operator|.
name|toString
argument_list|()
argument_list|,
name|style
argument_list|)
expr_stmt|;
comment|//view.getLogView().setText(view.getLogView().getText() + newText);
if|if
condition|(
name|autoScroll
condition|)
block|{
name|view
operator|.
name|getLogView
argument_list|()
operator|.
name|setCaretPosition
argument_list|(
name|doc
operator|.
name|getLength
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|ignored
parameter_list|)
block|{
comment|//should not happen
block|}
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
comment|/**      * Stop logging and don't print any more messages to text area      */
specifier|public
name|void
name|stopLogging
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getConsoleProperty
argument_list|(
name|DOCKED_PROPERTY
argument_list|)
condition|)
block|{
name|setConsoleProperty
argument_list|(
name|SHOW_CONSOLE_PROPERTY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|loggingStopped
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return lone log console instance      */
specifier|public
specifier|static
name|LogConsole
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|LogConsole
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
block|}
end_class

end_unit

