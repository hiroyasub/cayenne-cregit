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
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GridBagConstraints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GridBagLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Insets
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
name|FocusEvent
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
name|FocusListener
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
name|KeyListener
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
name|MouseEvent
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
name|MouseListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ImageIcon
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
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
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
name|ModelerUtil
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
name|LocalizedStringsHandler
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
name|scopemvc
operator|.
name|util
operator|.
name|UIStrings
import|;
end_import

begin_comment
comment|/**   * Displays the Cayenne license and build information.  */
end_comment

begin_comment
comment|// Implementation note - the data displayed here is
end_comment

begin_comment
comment|// static and very simple, so there is no need to implement complex Scope MVC
end_comment

begin_comment
comment|// triad, though it might be beneficial to use strings file
end_comment

begin_class
specifier|public
class|class
name|AboutDialog
extends|extends
name|JFrame
implements|implements
name|FocusListener
implements|,
name|KeyListener
implements|,
name|MouseListener
block|{
specifier|private
name|JLabel
name|license
decl_stmt|,
name|info
decl_stmt|;
specifier|private
specifier|static
name|String
name|infoString
decl_stmt|;
specifier|private
specifier|static
name|ImageIcon
name|logoImage
decl_stmt|;
specifier|static
specifier|synchronized
name|ImageIcon
name|getLogoImage
parameter_list|()
block|{
if|if
condition|(
name|logoImage
operator|==
literal|null
condition|)
block|{
name|logoImage
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"logo.jpg"
argument_list|)
expr_stmt|;
block|}
return|return
name|logoImage
return|;
block|}
comment|/**      * Builds and returns CayenneModeler info string.      */
specifier|static
specifier|synchronized
name|String
name|getInfoString
parameter_list|()
block|{
if|if
condition|(
name|infoString
operator|==
literal|null
condition|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<font size='-1' face='Arial,Helvetica'>"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|UIStrings
operator|.
name|get
argument_list|(
literal|"cayenne.modeler.about.info"
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"</font>"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<font size='-2' face='Arial,Helvetica'>"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<br>JVM: "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vm.name"
argument_list|)
operator|+
literal|" "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|version
init|=
name|LocalizedStringsHandler
operator|.
name|getString
argument_list|(
literal|"cayenne.version"
argument_list|)
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"<br>Version: "
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
name|String
name|buildDate
init|=
name|LocalizedStringsHandler
operator|.
name|getString
argument_list|(
literal|"cayenne.build.date"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|buildDate
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|buildDate
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"</font>"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"</html>"
argument_list|)
expr_stmt|;
name|infoString
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|infoString
return|;
block|}
specifier|public
name|AboutDialog
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
specifier|final
name|FlowLayout
name|flowLayout
init|=
operator|new
name|FlowLayout
argument_list|()
decl_stmt|;
name|getContentPane
argument_list|()
operator|.
name|setLayout
argument_list|(
name|flowLayout
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|this
operator|.
name|setUndecorated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|addMouseListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|addFocusListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|addKeyListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|setLocationRelativeTo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// centre on screen
specifier|final
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|panel
operator|.
name|setLayout
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|JLabel
name|image
init|=
operator|new
name|JLabel
argument_list|(
name|getLogoImage
argument_list|()
argument_list|)
decl_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|image
argument_list|,
operator|new
name|GridBagConstraints
argument_list|()
argument_list|)
expr_stmt|;
name|license
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
specifier|final
name|GridBagConstraints
name|gridBagConstraints_1
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|gridBagConstraints_1
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|gridBagConstraints_1
operator|.
name|anchor
operator|=
name|GridBagConstraints
operator|.
name|NORTHWEST
expr_stmt|;
name|gridBagConstraints_1
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|gridBagConstraints_1
operator|.
name|gridy
operator|=
literal|1
expr_stmt|;
name|gridBagConstraints_1
operator|.
name|insets
operator|=
operator|new
name|Insets
argument_list|(
literal|0
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|license
argument_list|,
name|gridBagConstraints_1
argument_list|)
expr_stmt|;
name|license
operator|.
name|setText
argument_list|(
literal|"<html><font size='-1' face='Arial,Helvetica'>Available under the Apache license.</font></html>"
argument_list|)
expr_stmt|;
name|info
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
specifier|final
name|GridBagConstraints
name|gridBagConstraints_2
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|gridBagConstraints_2
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|gridBagConstraints_2
operator|.
name|anchor
operator|=
name|GridBagConstraints
operator|.
name|NORTHWEST
expr_stmt|;
name|gridBagConstraints_2
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|gridBagConstraints_2
operator|.
name|gridy
operator|=
literal|2
expr_stmt|;
name|gridBagConstraints_2
operator|.
name|insets
operator|=
operator|new
name|Insets
argument_list|(
literal|6
argument_list|,
literal|12
argument_list|,
literal|12
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|info
argument_list|,
name|gridBagConstraints_2
argument_list|)
expr_stmt|;
name|info
operator|.
name|setText
argument_list|(
name|getInfoString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
name|this
operator|.
name|setLocationRelativeTo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|focusLost
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|focusGained
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|keyTyped
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

