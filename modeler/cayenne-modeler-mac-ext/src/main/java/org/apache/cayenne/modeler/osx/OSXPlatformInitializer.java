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
name|osx
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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Desktop
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
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|JFrame
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuBar
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
name|JPopupMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|UIManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|AbstractBorder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|Border
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
name|di
operator|.
name|Inject
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
name|AboutAction
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
name|ActionManager
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
name|ConfigurePreferencesAction
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
name|ExitAction
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
name|init
operator|.
name|platform
operator|.
name|PlatformInitializer
import|;
end_import

begin_class
specifier|public
class|class
name|OSXPlatformInitializer
implements|implements
name|PlatformInitializer
block|{
annotation|@
name|Inject
specifier|protected
name|ActionManager
name|actionManager
decl_stmt|;
specifier|public
name|void
name|initLookAndFeel
parameter_list|()
block|{
comment|// override some default styles and colors, assuming that Aqua theme will be used
name|overrideUIDefaults
argument_list|()
expr_stmt|;
name|Desktop
name|desktop
init|=
name|Desktop
operator|.
name|getDesktop
argument_list|()
decl_stmt|;
name|desktop
operator|.
name|setAboutHandler
argument_list|(
name|e
lambda|->
name|actionManager
operator|.
name|getAction
argument_list|(
name|AboutAction
operator|.
name|class
argument_list|)
operator|.
name|showAboutDialog
argument_list|()
argument_list|)
expr_stmt|;
name|desktop
operator|.
name|setPreferencesHandler
argument_list|(
name|e
lambda|->
name|actionManager
operator|.
name|getAction
argument_list|(
name|ConfigurePreferencesAction
operator|.
name|class
argument_list|)
operator|.
name|showPreferencesDialog
argument_list|()
argument_list|)
expr_stmt|;
name|desktop
operator|.
name|setQuitHandler
argument_list|(
parameter_list|(
name|e
parameter_list|,
name|r
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|actionManager
operator|.
name|getAction
argument_list|(
name|ExitAction
operator|.
name|class
argument_list|)
operator|.
name|exit
argument_list|()
condition|)
block|{
name|r
operator|.
name|cancelQuit
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|performQuit
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|overrideUIDefaults
parameter_list|()
block|{
name|Color
name|lightGrey
init|=
operator|new
name|Color
argument_list|(
literal|0xEEEEEE
argument_list|)
decl_stmt|;
name|Color
name|darkGrey
init|=
operator|new
name|Color
argument_list|(
literal|225
argument_list|,
literal|225
argument_list|,
literal|225
argument_list|)
decl_stmt|;
name|Border
name|darkBorder
init|=
name|BorderFactory
operator|.
name|createLineBorder
argument_list|(
name|darkGrey
argument_list|)
decl_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ToolBarSeparatorUI"
argument_list|,
name|OSXToolBarSeparatorUI
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"PanelUI"
argument_list|,
name|OSXPanelUI
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// next two is custom-made for Cayenne's MainToolBar
name|UIManager
operator|.
name|put
argument_list|(
literal|"ToolBar.background"
argument_list|,
name|lightGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"MainToolBar.background"
argument_list|,
name|lightGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"MainToolBar.border"
argument_list|,
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|,
literal|0
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ToolBar.border"
argument_list|,
name|darkBorder
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ScrollPane.border"
argument_list|,
name|darkBorder
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Table.scrollPaneBorder"
argument_list|,
name|darkBorder
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"SplitPane.border"
argument_list|,
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|()
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"SplitPane.background"
argument_list|,
name|darkGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Tree.rendererFillBackground"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ComboBox.background"
argument_list|,
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ComboBox.selectionBackground"
argument_list|,
name|darkGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"ComboBox.selectionForeground"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Tree.selectionForeground"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Tree.selectionBackground"
argument_list|,
name|lightGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Tree.selectionBorderColor"
argument_list|,
name|lightGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Table.selectionForeground"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Table.selectionBackground"
argument_list|,
name|lightGrey
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"Table.focusCellHighlightBorder"
argument_list|,
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|()
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"CheckBoxHeader.border"
argument_list|,
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// MacOS BigSur needs additional style tweaking for the tabs active state
name|OSXVersion
name|version
init|=
name|OSXVersion
operator|.
name|fromSystemProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|gt
argument_list|(
name|OSXVersion
operator|.
name|CATALINA
argument_list|)
condition|)
block|{
name|UIManager
operator|.
name|put
argument_list|(
literal|"TabbedPane.selectedTabTitlePressedColor"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"TabbedPane.selectedTabTitleNormalColor"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"TabbedPane.selectedTabTitleShadowDisabledColor"
argument_list|,
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"TabbedPane.selectedTabTitleShadowNormalColor"
argument_list|,
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Border
name|backgroundPainter
init|=
operator|new
name|AbstractBorder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|paintBorder
parameter_list|(
name|Component
name|c
parameter_list|,
name|Graphics
name|g
parameter_list|,
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|)
block|{
name|g
operator|.
name|setColor
argument_list|(
name|lightGrey
argument_list|)
expr_stmt|;
name|g
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
operator|-
literal|1
argument_list|,
name|height
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"MenuItem.selectedBackgroundPainter"
argument_list|,
name|backgroundPainter
argument_list|)
expr_stmt|;
name|UIManager
operator|.
name|put
argument_list|(
literal|"MenuItem.selectionForeground"
argument_list|,
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setupMenus
parameter_list|(
name|JFrame
name|frame
parameter_list|)
block|{
comment|// set additional look and feel for the window
name|frame
operator|.
name|getRootPane
argument_list|()
operator|.
name|putClientProperty
argument_list|(
literal|"apple.awt.brushMetalLook"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Action
argument_list|>
name|removeActions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|removeActions
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|ExitAction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|removeActions
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|AboutAction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|removeActions
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|ConfigurePreferencesAction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|JMenuBar
name|menuBar
init|=
name|frame
operator|.
name|getJMenuBar
argument_list|()
decl_stmt|;
for|for
control|(
name|Component
name|menu
range|:
name|menuBar
operator|.
name|getComponents
argument_list|()
control|)
block|{
if|if
condition|(
name|menu
operator|instanceof
name|JMenu
condition|)
block|{
name|JMenu
name|jMenu
init|=
operator|(
name|JMenu
operator|)
name|menu
decl_stmt|;
name|Component
index|[]
name|menuItems
init|=
name|jMenu
operator|.
name|getPopupMenu
argument_list|()
operator|.
name|getComponents
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|menuItems
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|menuItems
index|[
name|i
index|]
operator|instanceof
name|JMenuItem
condition|)
block|{
name|JMenuItem
name|jMenuItem
init|=
operator|(
name|JMenuItem
operator|)
name|menuItems
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|removeActions
operator|.
name|contains
argument_list|(
name|jMenuItem
operator|.
name|getAction
argument_list|()
argument_list|)
condition|)
block|{
name|jMenu
operator|.
name|remove
argument_list|(
name|jMenuItem
argument_list|)
expr_stmt|;
comment|// this algorithm is pretty lame, but it works for
comment|// the current (as of 08.2010) menu layout
if|if
condition|(
name|i
operator|>
literal|0
operator|&&
name|i
operator|==
name|menuItems
operator|.
name|length
operator|-
literal|1
operator|&&
name|menuItems
index|[
name|i
operator|-
literal|1
index|]
operator|instanceof
name|JPopupMenu
operator|.
name|Separator
condition|)
block|{
name|jMenu
operator|.
name|remove
argument_list|(
name|i
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

