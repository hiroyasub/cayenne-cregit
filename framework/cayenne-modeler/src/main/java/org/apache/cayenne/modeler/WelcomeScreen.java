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
name|awt
operator|.
name|BorderLayout
import|;
end_import

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
name|GradientPaint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
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
name|MouseEvent
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
name|Vector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|AbstractListModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultListCellRenderer
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
name|ImageIcon
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
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JList
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
name|SwingConstants
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
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|MouseInputListener
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
name|NewProjectAction
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
name|event
operator|.
name|RecentFileListListener
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

begin_comment
comment|/**  * Welcome screen (CAY-894) is a panel shown when no project is open.  * User can quickly create new project or open an existing one.  */
end_comment

begin_class
specifier|public
class|class
name|WelcomeScreen
extends|extends
name|JPanel
implements|implements
name|RecentFileListListener
block|{
comment|/**      * Top color of gradient background      */
specifier|private
specifier|static
specifier|final
name|Color
name|TOP_GRADIENT
init|=
operator|new
name|Color
argument_list|(
literal|153
argument_list|,
literal|153
argument_list|,
literal|153
argument_list|)
decl_stmt|;
comment|/**      * Bottom color of gradient background      */
specifier|private
specifier|static
specifier|final
name|Color
name|BOTTOM_GRADIENT
init|=
operator|new
name|Color
argument_list|(
literal|230
argument_list|,
literal|230
argument_list|,
literal|230
argument_list|)
decl_stmt|;
comment|/**      * List of recent projects      */
specifier|private
name|JList
name|recentsList
decl_stmt|;
specifier|public
name|WelcomeScreen
parameter_list|()
block|{
name|initView
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates all neccesary components      */
specifier|protected
name|void
name|initView
parameter_list|()
block|{
name|setLayout
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
expr_stmt|;
name|ImageIcon
name|welcome
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"welcome.jpg"
argument_list|)
decl_stmt|;
name|JLabel
name|imageLabel
init|=
operator|new
name|JLabel
argument_list|(
name|welcome
argument_list|)
decl_stmt|;
name|ImageIcon
name|newOutIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-welcome-new.png"
argument_list|)
decl_stmt|;
name|ImageIcon
name|newOverIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-welcome-new-over.png"
argument_list|)
decl_stmt|;
name|ImageIcon
name|openOutIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-welcome-open.png"
argument_list|)
decl_stmt|;
name|ImageIcon
name|openOverIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-welcome-open-over.png"
argument_list|)
decl_stmt|;
name|JPanel
name|buttonsPane
init|=
operator|new
name|JPanel
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|buttonsPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|JButton
name|newButton
init|=
name|createButton
argument_list|(
name|newOutIcon
argument_list|,
name|newOverIcon
argument_list|)
decl_stmt|;
name|newButton
operator|.
name|addActionListener
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|NewProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|JLabel
name|newLabel
init|=
operator|new
name|JLabel
argument_list|(
name|NewProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|,
name|SwingConstants
operator|.
name|CENTER
argument_list|)
decl_stmt|;
name|JButton
name|openButton
init|=
name|createButton
argument_list|(
name|openOutIcon
argument_list|,
name|openOverIcon
argument_list|)
decl_stmt|;
name|openButton
operator|.
name|addActionListener
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|JLabel
name|openLabel
init|=
operator|new
name|JLabel
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|,
name|SwingConstants
operator|.
name|CENTER
argument_list|)
decl_stmt|;
name|imageLabel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|newButton
operator|.
name|setLocation
argument_list|(
literal|10
argument_list|,
literal|130
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|newButton
argument_list|)
expr_stmt|;
name|newLabel
operator|.
name|setLocation
argument_list|(
name|newButton
operator|.
name|getX
argument_list|()
operator|+
name|newButton
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
operator|-
name|newLabel
operator|.
name|getPreferredSize
argument_list|()
operator|.
name|width
operator|/
literal|2
argument_list|,
name|newButton
operator|.
name|getY
argument_list|()
operator|+
name|newButton
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|newLabel
operator|.
name|setSize
argument_list|(
name|newLabel
operator|.
name|getPreferredSize
argument_list|()
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|newLabel
argument_list|)
expr_stmt|;
name|openButton
operator|.
name|setLocation
argument_list|(
literal|120
argument_list|,
name|newButton
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|openButton
argument_list|)
expr_stmt|;
name|openLabel
operator|.
name|setLocation
argument_list|(
name|openButton
operator|.
name|getX
argument_list|()
operator|+
name|openButton
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
operator|-
name|openLabel
operator|.
name|getPreferredSize
argument_list|()
operator|.
name|width
operator|/
literal|2
argument_list|,
name|openButton
operator|.
name|getY
argument_list|()
operator|+
name|openButton
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|openLabel
operator|.
name|setSize
argument_list|(
name|openLabel
operator|.
name|getPreferredSize
argument_list|()
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|openLabel
argument_list|)
expr_stmt|;
name|JLabel
name|recents
init|=
operator|new
name|JLabel
argument_list|(
literal|"Recent Projects:"
argument_list|)
decl_stmt|;
name|recents
operator|.
name|setLocation
argument_list|(
literal|207
argument_list|,
name|newButton
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|recents
operator|.
name|setSize
argument_list|(
name|recents
operator|.
name|getPreferredSize
argument_list|()
argument_list|)
expr_stmt|;
name|recents
operator|.
name|setHorizontalTextPosition
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|recents
argument_list|)
expr_stmt|;
name|recentsList
operator|=
operator|new
name|JList
argument_list|()
expr_stmt|;
name|recentsList
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|recentsList
operator|.
name|setLocation
argument_list|(
name|recents
operator|.
name|getX
argument_list|()
argument_list|,
name|recents
operator|.
name|getY
argument_list|()
operator|+
literal|2
operator|*
name|recents
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|recentsList
operator|.
name|setSize
argument_list|(
name|welcome
operator|.
name|getIconWidth
argument_list|()
operator|-
name|recentsList
operator|.
name|getX
argument_list|()
operator|-
literal|1
argument_list|,
name|welcome
operator|.
name|getIconHeight
argument_list|()
operator|-
name|recentsList
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|recentsList
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|RecentFileListRenderer
argument_list|(
name|recentsList
argument_list|)
argument_list|)
expr_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|recentsList
argument_list|)
expr_stmt|;
name|imageLabel
operator|.
name|add
argument_list|(
name|buttonsPane
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|imageLabel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates welcome screen-specific button      */
specifier|private
name|JButton
name|createButton
parameter_list|(
name|Icon
name|outIcon
parameter_list|,
name|Icon
name|overIcon
parameter_list|)
block|{
name|JButton
name|button
init|=
operator|new
name|JButton
argument_list|()
decl_stmt|;
name|button
operator|.
name|setFocusPainted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setFocusable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setBorderPainted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setContentAreaFilled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|button
operator|.
name|setPressedIcon
argument_list|(
name|overIcon
argument_list|)
expr_stmt|;
name|button
operator|.
name|setRolloverIcon
argument_list|(
name|overIcon
argument_list|)
expr_stmt|;
name|button
operator|.
name|setIcon
argument_list|(
name|outIcon
argument_list|)
expr_stmt|;
name|button
operator|.
name|setSize
argument_list|(
name|outIcon
operator|.
name|getIconWidth
argument_list|()
argument_list|,
name|outIcon
operator|.
name|getIconHeight
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|button
return|;
block|}
comment|/**      * Paints gradient background      */
annotation|@
name|Override
specifier|public
name|void
name|paintComponent
parameter_list|(
name|Graphics
name|g
parameter_list|)
block|{
name|Graphics2D
name|g2
init|=
operator|(
name|Graphics2D
operator|)
name|g
operator|.
name|create
argument_list|()
decl_stmt|;
name|g2
operator|.
name|setPaint
argument_list|(
operator|new
name|GradientPaint
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|TOP_GRADIENT
argument_list|,
literal|0
argument_list|,
name|getHeight
argument_list|()
argument_list|,
name|BOTTOM_GRADIENT
argument_list|)
argument_list|)
expr_stmt|;
name|g2
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|getWidth
argument_list|()
argument_list|,
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|g2
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|recentFileListChanged
parameter_list|()
block|{
comment|// Preferences pref = ModelerPreferences.getPreferences();
specifier|final
name|Vector
argument_list|<
name|?
argument_list|>
name|arr
init|=
operator|(
name|Vector
argument_list|<
name|?
argument_list|>
operator|)
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|recentsList
operator|.
name|setModel
argument_list|(
operator|new
name|AbstractListModel
argument_list|()
block|{
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|arr
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getElementAt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|arr
operator|.
name|elementAt
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Renderer for the list of last files. Ignores the selection, instead paints      * with ROLLOVER_BACKGROUND (currently red) the row mouse is hovering over      */
class|class
name|RecentFileListRenderer
extends|extends
name|DefaultListCellRenderer
implements|implements
name|MouseInputListener
block|{
comment|/**          * Color for background of row mouse is over          */
specifier|final
name|Color
name|ROLLOVER_BACKGROUND
init|=
name|Color
operator|.
name|RED
decl_stmt|;
comment|/**          * Color for foreground of row mouse is over          */
specifier|final
name|Color
name|ROLLOVER_FOREGROUND
init|=
name|Color
operator|.
name|WHITE
decl_stmt|;
comment|/**          * List which is rendered          */
specifier|private
name|JList
name|list
decl_stmt|;
comment|/**          * Row mouse is over          */
specifier|private
name|int
name|rolloverRow
decl_stmt|;
specifier|public
name|RecentFileListRenderer
parameter_list|(
name|JList
name|list
parameter_list|)
block|{
name|list
operator|.
name|addMouseListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|list
operator|.
name|addMouseMotionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
name|setHorizontalTextPosition
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
name|list
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|cellHasFocus
parameter_list|)
block|{
comment|//selection is ignored
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|value
argument_list|,
name|index
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|rolloverRow
operator|==
name|index
condition|)
block|{
name|setOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setForeground
argument_list|(
name|ROLLOVER_FOREGROUND
argument_list|)
expr_stmt|;
name|setBackground
argument_list|(
name|ROLLOVER_BACKGROUND
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|mouseMoved
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
name|list
operator|.
name|repaint
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|SwingUtilities
operator|.
name|isLeftMouseButton
argument_list|(
name|e
argument_list|)
operator|&&
name|rolloverRow
operator|!=
operator|-
literal|1
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getElementAt
argument_list|(
name|rolloverRow
argument_list|)
argument_list|)
decl_stmt|;
comment|/**                  * Fire an action with the file as source                  */
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|performAction
argument_list|(
operator|new
name|ActionEvent
argument_list|(
name|file
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
comment|//clear selection
block|}
block|}
specifier|public
name|void
name|mouseDragged
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseMoved
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|int
name|newRow
decl_stmt|;
comment|/**              * Check that a row boundary contains the mouse point, so that rolloverRow would              * be -1 if we are below last row              */
if|if
condition|(
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|list
operator|.
name|getCellBounds
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
condition|)
block|{
name|newRow
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|newRow
operator|=
name|list
operator|.
name|locationToIndex
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rolloverRow
operator|!=
name|newRow
condition|)
block|{
name|rolloverRow
operator|=
name|newRow
expr_stmt|;
name|list
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

