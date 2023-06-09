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
name|dialog
operator|.
name|welcome
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
name|Paint
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
name|List
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
name|BorderFactory
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
name|JScrollPane
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
name|ModelerPreferences
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
name|BackgroundPanel
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
comment|/**  * Welcome screen (CAY-894) is a panel shown when no project is open. User can quickly  * create new project or open an existing one.  */
end_comment

begin_class
specifier|public
class|class
name|WelcomeScreen
extends|extends
name|JScrollPane
implements|implements
name|RecentFileListListener
implements|,
name|RecentFileListRenderer
operator|.
name|OnFileClickListener
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
argument_list|<
name|String
argument_list|>
name|recentProjectsList
decl_stmt|;
specifier|private
name|JPanel
name|buttonsPanel
decl_stmt|;
specifier|private
name|JPanel
name|mainPanel
decl_stmt|;
specifier|public
name|WelcomeScreen
parameter_list|()
block|{
name|initView
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates all necessary components      */
specifier|protected
name|void
name|initView
parameter_list|()
block|{
name|mainPanel
operator|=
operator|new
name|JPanel
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
block|{
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
comment|// paint gradient background
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
name|Paint
name|paint
init|=
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
decl_stmt|;
name|g2
operator|.
name|setPaint
argument_list|(
name|paint
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
block|}
expr_stmt|;
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|()
argument_list|)
expr_stmt|;
name|initButtonsPane
argument_list|()
expr_stmt|;
name|initFileListPane
argument_list|()
expr_stmt|;
name|setViewportView
argument_list|(
name|mainPanel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initFileListPane
parameter_list|()
block|{
name|JPanel
name|fileListPanel
init|=
operator|new
name|BackgroundPanel
argument_list|(
literal|"welcome/welcome-screen-right-bg.jpg"
argument_list|)
decl_stmt|;
specifier|final
name|int
name|padding
init|=
literal|20
decl_stmt|;
name|recentProjectsList
operator|=
operator|new
name|JList
argument_list|<>
argument_list|()
expr_stmt|;
name|recentProjectsList
operator|.
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|recentProjectsList
operator|.
name|setLocation
argument_list|(
name|padding
argument_list|,
name|padding
argument_list|)
expr_stmt|;
name|recentProjectsList
operator|.
name|setSize
argument_list|(
name|fileListPanel
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|,
name|fileListPanel
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|)
expr_stmt|;
name|recentProjectsList
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|RecentFileListRenderer
argument_list|(
name|recentProjectsList
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|fileListPanel
operator|.
name|add
argument_list|(
name|recentProjectsList
argument_list|)
expr_stmt|;
name|mainPanel
operator|.
name|add
argument_list|(
name|fileListPanel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initButtonsPane
parameter_list|()
block|{
specifier|final
name|int
name|padding
init|=
literal|24
decl_stmt|;
comment|// bottom padding for buttons
specifier|final
name|int
name|buttonHeight
init|=
literal|36
decl_stmt|;
name|buttonsPanel
operator|=
operator|new
name|BackgroundPanel
argument_list|(
literal|"welcome/welcome-screen-left-bg.jpg"
argument_list|)
expr_stmt|;
name|int
name|openButtonY
init|=
name|buttonsPanel
operator|.
name|getHeight
argument_list|()
operator|-
name|padding
operator|-
name|buttonHeight
decl_stmt|;
comment|// buttons layout from bottom
name|int
name|newButtonY
init|=
name|openButtonY
operator|-
literal|10
operator|-
name|buttonHeight
decl_stmt|;
comment|// 10px - space between buttons
name|initButton
argument_list|(
literal|"open"
argument_list|,
name|openButtonY
argument_list|,
name|OpenProjectAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|initButton
argument_list|(
literal|"new"
argument_list|,
name|newButtonY
argument_list|,
name|NewProjectAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|mainPanel
operator|.
name|add
argument_list|(
name|buttonsPanel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initButton
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|y
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|ImageIcon
name|icon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"welcome/welcome-screen-"
operator|+
name|name
operator|+
literal|"-btn.png"
argument_list|)
decl_stmt|;
name|ImageIcon
name|hoverIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"welcome/welcome-screen-"
operator|+
name|name
operator|+
literal|"-btn-hover.png"
argument_list|)
decl_stmt|;
name|JButton
name|button
init|=
name|createButton
argument_list|(
name|icon
argument_list|,
name|hoverIcon
argument_list|)
decl_stmt|;
name|button
operator|.
name|setLocation
argument_list|(
literal|24
argument_list|,
name|y
argument_list|)
expr_stmt|;
comment|// 24px - button left& right padding
name|button
operator|.
name|addActionListener
argument_list|(
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
name|actionClass
argument_list|)
argument_list|)
expr_stmt|;
name|buttonsPanel
operator|.
name|add
argument_list|(
name|button
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFileSelect
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|ActionEvent
name|event
init|=
operator|new
name|ActionEvent
argument_list|(
name|file
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// Fire an action with the file as source
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
name|performAction
argument_list|(
name|event
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
annotation|@
name|Override
specifier|public
name|void
name|recentFileListChanged
parameter_list|()
block|{
name|List
argument_list|<
name|File
argument_list|>
name|arr
init|=
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
decl_stmt|;
name|recentProjectsList
operator|.
name|setModel
argument_list|(
operator|new
name|RecentFileListModel
argument_list|(
name|arr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

