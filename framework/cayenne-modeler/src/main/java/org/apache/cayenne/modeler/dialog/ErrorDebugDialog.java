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
name|CayenneModelerFrame
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
name|CayenneDialog
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
name|PanelFactory
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

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|util
operator|.
name|UIStrings
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
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
name|IOException
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

begin_comment
comment|/**  * Displays CayenneModeler exceptions and warning messages.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ErrorDebugDialog
extends|extends
name|CayenneDialog
implements|implements
name|ActionListener
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
name|ErrorDebugDialog
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Sole instance of error/warning dialog to disallow showing of multiple dialogs      */
specifier|private
specifier|static
name|ErrorDebugDialog
name|instance
decl_stmt|;
specifier|protected
name|JButton
name|close
decl_stmt|;
specifier|protected
name|JButton
name|showHide
decl_stmt|;
specifier|protected
name|JTextArea
name|exText
init|=
operator|new
name|JTextArea
argument_list|()
decl_stmt|;
specifier|protected
name|JPanel
name|exPanel
decl_stmt|;
specifier|protected
name|Throwable
name|throwable
decl_stmt|;
specifier|protected
name|boolean
name|detailed
decl_stmt|;
comment|/**      * Shows an error dialog with stack trace      */
specifier|public
specifier|static
name|void
name|guiException
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
if|if
condition|(
name|th
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"CayenneModeler Error"
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
name|ErrorDebugDialog
name|dialog
init|=
operator|new
name|ErrorDebugDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"CayenneModeler Error"
argument_list|,
name|th
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|showDialog
argument_list|(
name|dialog
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows an warning dialog with stack trace      */
specifier|public
specifier|static
name|void
name|guiWarning
parameter_list|(
name|Throwable
name|th
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|th
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"CayenneModeler Warning"
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
name|WarningDialog
name|dialog
init|=
operator|new
name|WarningDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|message
argument_list|,
name|th
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|showDialog
argument_list|(
name|dialog
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows an error/warning dialog, closing existing if needed      */
specifier|private
specifier|static
name|void
name|showDialog
parameter_list|(
name|ErrorDebugDialog
name|dialog
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
name|instance
operator|=
name|dialog
expr_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ErrorDebugDialog.      */
specifier|protected
name|ErrorDebugDialog
parameter_list|(
name|CayenneModelerFrame
name|owner
parameter_list|,
name|String
name|title
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|boolean
name|detailed
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|this
argument_list|(
name|owner
argument_list|,
name|title
argument_list|,
name|throwable
argument_list|,
name|detailed
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ErrorDebugDialog, allowing to specify 'modal' property      */
specifier|protected
name|ErrorDebugDialog
parameter_list|(
name|CayenneModelerFrame
name|owner
parameter_list|,
name|String
name|title
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|boolean
name|detailed
parameter_list|,
name|boolean
name|modal
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|title
argument_list|,
name|modal
argument_list|)
expr_stmt|;
name|setThrowable
argument_list|(
name|Util
operator|.
name|unwindException
argument_list|(
name|throwable
argument_list|)
argument_list|)
expr_stmt|;
name|setDetailed
argument_list|(
name|detailed
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|setResizable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Container
name|pane
init|=
name|this
operator|.
name|getContentPane
argument_list|()
decl_stmt|;
name|pane
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
comment|// info area
name|JEditorPane
name|infoText
init|=
operator|new
name|JEditorPane
argument_list|(
literal|"text/html"
argument_list|,
name|infoHTML
argument_list|()
argument_list|)
decl_stmt|;
name|infoText
operator|.
name|setBackground
argument_list|(
name|pane
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|infoText
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// popup hyperlinks
name|infoText
operator|.
name|addHyperlinkListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|JPanel
name|infoPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|infoPanel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|infoPanel
operator|.
name|add
argument_list|(
name|infoText
argument_list|)
expr_stmt|;
name|pane
operator|.
name|add
argument_list|(
name|infoPanel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
comment|// exception area
if|if
condition|(
name|throwable
operator|!=
literal|null
condition|)
block|{
name|exText
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|exText
operator|.
name|setLineWrap
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exText
operator|.
name|setWrapStyleWord
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exText
operator|.
name|setRows
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|exText
operator|.
name|setColumns
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|JScrollPane
name|exScroll
init|=
operator|new
name|JScrollPane
argument_list|(
name|exText
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
decl_stmt|;
name|exPanel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|exPanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|exPanel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|exPanel
operator|.
name|add
argument_list|(
name|exScroll
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
comment|// buttons
name|showHide
operator|=
operator|new
name|JButton
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|showHide
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDetailed
argument_list|()
condition|)
block|{
name|showDetails
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|hideDetails
argument_list|()
expr_stmt|;
block|}
block|}
name|close
operator|=
operator|new
name|JButton
argument_list|(
literal|"Close"
argument_list|)
expr_stmt|;
name|close
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|JButton
index|[]
name|buttons
init|=
operator|(
name|showHide
operator|!=
literal|null
operator|)
condition|?
operator|new
name|JButton
index|[]
block|{
name|showHide
block|,
name|close
block|}
else|:
operator|new
name|JButton
index|[]
block|{
name|close
block|}
decl_stmt|;
name|pane
operator|.
name|add
argument_list|(
name|PanelFactory
operator|.
name|createButtonPanel
argument_list|(
name|buttons
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
comment|//add a listener to clear static variables, not to produce garbage
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
name|instance
operator|=
literal|null
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// prepare to display
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
name|this
operator|.
name|centerWindow
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|String
name|infoHTML
parameter_list|()
block|{
name|String
name|bugreportURL
init|=
name|UIStrings
operator|.
name|get
argument_list|(
literal|"cayenne.bugreport.url"
argument_list|)
decl_stmt|;
return|return
literal|"<b><font face='Arial,Helvetica' size='+1' color='red'>"
operator|+
name|getTitle
argument_list|()
operator|+
literal|"</font></b><br>"
operator|+
literal|"<font face='Arial,Helvetica' size='-1'>Please copy the message below and "
operator|+
literal|"report this error by going to<br>"
operator|+
literal|"<a href='"
operator|+
name|bugreportURL
operator|+
literal|"'>"
operator|+
name|bugreportURL
operator|+
literal|"</a></font>"
return|;
block|}
specifier|protected
name|void
name|setThrowable
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|this
operator|.
name|throwable
operator|=
name|throwable
expr_stmt|;
name|String
name|text
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|throwable
operator|!=
literal|null
condition|)
block|{
name|StringWriter
name|str
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|str
argument_list|)
decl_stmt|;
comment|// first add extra diagnostics
name|String
name|version
init|=
name|UIStrings
operator|.
name|get
argument_list|(
literal|"cayenne.version"
argument_list|)
decl_stmt|;
name|version
operator|=
operator|(
name|version
operator|!=
literal|null
operator|)
condition|?
name|version
else|:
literal|"(unknown)"
expr_stmt|;
name|String
name|buildDate
init|=
name|UIStrings
operator|.
name|get
argument_list|(
literal|"cayenne.build.date"
argument_list|)
decl_stmt|;
name|buildDate
operator|=
operator|(
name|buildDate
operator|!=
literal|null
operator|)
condition|?
name|buildDate
else|:
literal|"(unknown)"
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"CayenneModeler Info"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Version: "
operator|+
name|version
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Build Date: "
operator|+
name|buildDate
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Exception: "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"================================="
argument_list|)
expr_stmt|;
name|buildStackTrace
argument_list|(
name|out
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
try|try
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|str
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
comment|// this should never happen
block|}
name|text
operator|=
name|str
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|exText
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|buildStackTrace
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
if|if
condition|(
name|th
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|th
operator|.
name|printStackTrace
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|Throwable
name|cause
init|=
name|th
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Caused by:"
argument_list|)
expr_stmt|;
name|buildStackTrace
argument_list|(
name|out
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
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
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|close
condition|)
block|{
name|this
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|showHide
condition|)
block|{
if|if
condition|(
name|isDetailed
argument_list|()
condition|)
block|{
name|hideDetails
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|showDetails
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
name|this
operator|.
name|centerWindow
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|hideDetails
parameter_list|()
block|{
name|getContentPane
argument_list|()
operator|.
name|remove
argument_list|(
name|exPanel
argument_list|)
expr_stmt|;
name|showHide
operator|.
name|setText
argument_list|(
literal|"Show Details"
argument_list|)
expr_stmt|;
name|setDetailed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|showDetails
parameter_list|()
block|{
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|exPanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|showHide
operator|.
name|setText
argument_list|(
literal|"Hide Details"
argument_list|)
expr_stmt|;
name|setDetailed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the detailed.      * @return boolean      */
specifier|public
name|boolean
name|isDetailed
parameter_list|()
block|{
return|return
name|detailed
return|;
block|}
comment|/**      * Sets the detailed.      * @param detailed The detailed to set      */
specifier|public
name|void
name|setDetailed
parameter_list|(
name|boolean
name|detailed
parameter_list|)
block|{
name|this
operator|.
name|detailed
operator|=
name|detailed
expr_stmt|;
block|}
block|}
end_class

end_unit

