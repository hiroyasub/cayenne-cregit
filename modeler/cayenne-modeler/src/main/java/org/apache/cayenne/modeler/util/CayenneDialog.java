begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|Dialog
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Frame
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GraphicsConfiguration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|HeadlessException
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
name|awt
operator|.
name|event
operator|.
name|WindowEvent
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
name|KeyStroke
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|HyperlinkEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|HyperlinkListener
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
name|ProjectController
import|;
end_import

begin_comment
comment|/**  * Superclass of CayenneModeler dialogs. Adds support for popping hyperlinks   * in the default system browser.  *   */
end_comment

begin_class
specifier|public
class|class
name|CayenneDialog
extends|extends
name|JDialog
implements|implements
name|HyperlinkListener
block|{
specifier|public
name|CayenneDialog
parameter_list|()
throws|throws
name|HeadlessException
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Frame
name|owner
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|super
argument_list|(
name|owner
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Frame
name|owner
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
name|modal
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Frame
name|owner
parameter_list|,
name|String
name|title
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Frame
name|owner
parameter_list|,
name|String
name|title
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
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Frame
name|owner
parameter_list|,
name|String
name|title
parameter_list|,
name|boolean
name|modal
parameter_list|,
name|GraphicsConfiguration
name|gc
parameter_list|)
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|title
argument_list|,
name|modal
argument_list|,
name|gc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Dialog
name|owner
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|super
argument_list|(
name|owner
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Dialog
name|owner
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
name|modal
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Dialog
name|owner
parameter_list|,
name|String
name|title
parameter_list|)
throws|throws
name|HeadlessException
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Dialog
name|owner
parameter_list|,
name|String
name|title
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
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|Dialog
name|owner
parameter_list|,
name|String
name|title
parameter_list|,
name|boolean
name|modal
parameter_list|,
name|GraphicsConfiguration
name|gc
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
argument_list|,
name|gc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneDialog
parameter_list|(
name|CayenneModelerFrame
name|frame
parameter_list|,
name|String
name|title
parameter_list|,
name|boolean
name|modal
parameter_list|)
block|{
name|super
argument_list|(
name|frame
argument_list|,
name|title
argument_list|,
name|modal
argument_list|)
expr_stmt|;
block|}
comment|/**      * Makes dialog closeable when ESC button is clicked.      */
specifier|protected
name|void
name|initCloseOnEscape
parameter_list|()
block|{
comment|// make dialog closable on escape
comment|// TODO: Note that if a dialog contains subcomponents
comment|// that use ESC for their own purposes (like editable JTable or JComboBox),
comment|// this code will still close the dialog  (e.g. not just an expanded
comment|// ComboBox). To fix it see this advise (Swing is Fun!!):
comment|//
comment|//   http://www.eos.dk/pipermail/swing/2001-June/000789.html
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
name|CayenneDialog
operator|.
name|this
operator|.
name|isVisible
argument_list|()
condition|)
block|{
comment|// dispatch window closing event
name|WindowEvent
name|windowClosing
init|=
operator|new
name|WindowEvent
argument_list|(
name|CayenneDialog
operator|.
name|this
argument_list|,
name|WindowEvent
operator|.
name|WINDOW_CLOSING
argument_list|)
decl_stmt|;
name|CayenneDialog
operator|.
name|super
operator|.
name|processWindowEvent
argument_list|(
name|windowClosing
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
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
comment|/**       * Centers this dialog relative to the parent Window       */
specifier|public
name|void
name|centerWindow
parameter_list|()
block|{
name|ModelerUtil
operator|.
name|centerWindow
argument_list|(
name|getParentEditor
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneModelerFrame
name|getParentEditor
parameter_list|()
block|{
return|return
operator|(
name|CayenneModelerFrame
operator|)
name|super
operator|.
name|getParent
argument_list|()
return|;
block|}
comment|/**       * Opens hyperlink in the default browser.      */
specifier|public
name|void
name|hyperlinkUpdate
parameter_list|(
name|HyperlinkEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getEventType
argument_list|()
operator|==
name|HyperlinkEvent
operator|.
name|EventType
operator|.
name|ACTIVATED
condition|)
block|{
name|BrowserControl
operator|.
name|displayURL
argument_list|(
name|event
operator|.
name|getURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns current CayenneModeler mediator.      */
specifier|public
name|ProjectController
name|getMediator
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
return|;
block|}
specifier|protected
name|void
name|dialogInit
parameter_list|()
block|{
name|super
operator|.
name|dialogInit
argument_list|()
expr_stmt|;
name|initCloseOnEscape
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

