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
name|FlowLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
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
name|javax
operator|.
name|swing
operator|.
name|Box
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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JToolBar
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
name|lang
operator|.
name|SystemUtils
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
name|map
operator|.
name|DerivedDbEntity
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
name|CreateDataMapAction
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
name|CreateDbEntityAction
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
name|CreateDerivedDbEntityAction
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
name|CreateDomainAction
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
name|CreateNodeAction
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
name|CreateObjEntityAction
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
name|CreateProcedureAction
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
name|CreateQueryAction
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
name|DerivedEntitySyncAction
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
name|action
operator|.
name|GenerateCodeAction
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
name|GenerateDBAction
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
name|ImportDBAction
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
name|ImportDataMapAction
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
name|ImportEOModelAction
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
name|NavigateBackwardAction
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
name|NavigateForwardAction
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
name|ObjEntitySyncAction
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
name|action
operator|.
name|ProjectAction
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
name|RemoveAction
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
name|RevertAction
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
name|SaveAction
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
name|SaveAsAction
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
name|ValidateAction
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
name|editor
operator|.
name|EditorView
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
name|DataMapDisplayEvent
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
name|DataMapDisplayListener
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
name|DataNodeDisplayEvent
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
name|DataNodeDisplayListener
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
name|DbEntityDisplayListener
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
name|EntityDisplayEvent
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
name|ObjEntityDisplayListener
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
name|ProcedureDisplayEvent
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
name|ProcedureDisplayListener
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
name|QueryDisplayEvent
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
name|QueryDisplayListener
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
name|CayenneAction
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
name|RecentFileMenu
import|;
end_import

begin_comment
comment|/**  * Main frame of CayenneModeler. Responsibilities include coordination of  * enabling/disabling of menu and toolbar.  */
end_comment

begin_class
specifier|public
class|class
name|CayenneModelerFrame
extends|extends
name|JFrame
implements|implements
name|DataNodeDisplayListener
implements|,
name|DataMapDisplayListener
implements|,
name|ObjEntityDisplayListener
implements|,
name|DbEntityDisplayListener
implements|,
name|QueryDisplayListener
implements|,
name|ProcedureDisplayListener
block|{
specifier|protected
name|EditorView
name|view
decl_stmt|;
specifier|protected
name|RecentFileMenu
name|recentFileMenu
decl_stmt|;
specifier|protected
name|ActionManager
name|actionManager
decl_stmt|;
specifier|protected
name|JLabel
name|status
decl_stmt|;
specifier|public
name|CayenneModelerFrame
parameter_list|(
name|ActionManager
name|actionManager
parameter_list|)
block|{
name|super
argument_list|(
name|ModelerConstants
operator|.
name|TITLE
argument_list|)
expr_stmt|;
name|this
operator|.
name|actionManager
operator|=
name|actionManager
expr_stmt|;
name|initMenus
argument_list|()
expr_stmt|;
name|initToolbar
argument_list|()
expr_stmt|;
name|initStatusBar
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an action object associated with the key.      */
specifier|private
name|CayenneAction
name|getAction
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|actionManager
operator|.
name|getAction
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|protected
name|void
name|initMenus
parameter_list|()
block|{
name|getContentPane
argument_list|()
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|JMenu
name|fileMenu
init|=
operator|new
name|JMenu
argument_list|(
literal|"File"
argument_list|)
decl_stmt|;
name|JMenu
name|projectMenu
init|=
operator|new
name|JMenu
argument_list|(
literal|"Project"
argument_list|)
decl_stmt|;
name|JMenu
name|toolMenu
init|=
operator|new
name|JMenu
argument_list|(
literal|"Tools"
argument_list|)
decl_stmt|;
name|JMenu
name|helpMenu
init|=
operator|new
name|JMenu
argument_list|(
literal|"Help"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|SystemUtils
operator|.
name|IS_OS_MAC_OSX
condition|)
block|{
name|fileMenu
operator|.
name|setMnemonic
argument_list|(
name|KeyEvent
operator|.
name|VK_F
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|setMnemonic
argument_list|(
name|KeyEvent
operator|.
name|VK_P
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|setMnemonic
argument_list|(
name|KeyEvent
operator|.
name|VK_T
argument_list|)
expr_stmt|;
name|helpMenu
operator|.
name|setMnemonic
argument_list|(
name|KeyEvent
operator|.
name|VK_H
argument_list|)
expr_stmt|;
block|}
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|NewProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ImportDataMapAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|SaveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|SaveAsAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|RevertAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|recentFileMenu
operator|=
operator|new
name|RecentFileMenu
argument_list|(
literal|"Recent Files"
argument_list|)
expr_stmt|;
name|recentFileMenu
operator|.
name|rebuildFromPreferences
argument_list|()
expr_stmt|;
name|recentFileMenu
operator|.
name|setEnabled
argument_list|(
name|recentFileMenu
operator|.
name|getMenuComponentCount
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|recentFileMenu
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ExitAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ValidateAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDomainAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateNodeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDataMapAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateObjEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDbEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDerivedDbEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateProcedureAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateQueryAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ObjEntitySyncAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|DerivedEntitySyncAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|projectMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|projectMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ImportDBAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ImportEOModelAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|GenerateCodeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|GenerateDBAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|toolMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|ConfigurePreferencesAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|helpMenu
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|AboutAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildMenu
argument_list|()
argument_list|)
expr_stmt|;
name|JMenuBar
name|menuBar
init|=
operator|new
name|JMenuBar
argument_list|()
decl_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|fileMenu
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|projectMenu
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|toolMenu
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|helpMenu
argument_list|)
expr_stmt|;
name|setJMenuBar
argument_list|(
name|menuBar
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initStatusBar
parameter_list|()
block|{
name|status
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|status
operator|.
name|setFont
argument_list|(
name|status
operator|.
name|getFont
argument_list|()
operator|.
name|deriveFont
argument_list|(
name|Font
operator|.
name|PLAIN
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|JPanel
name|statusBar
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEFT
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
comment|// add placeholder
name|statusBar
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createVerticalStrut
argument_list|(
literal|16
argument_list|)
argument_list|)
expr_stmt|;
name|statusBar
operator|.
name|add
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|statusBar
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
block|}
comment|/** Initializes main toolbar. */
specifier|protected
name|void
name|initToolbar
parameter_list|()
block|{
name|JToolBar
name|toolBar
init|=
operator|new
name|JToolBar
argument_list|()
decl_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|NewProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|SaveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDomainAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateNodeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDataMapAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDbEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateDerivedDbEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateProcedureAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateObjEntityAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|CreateQueryAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|NavigateBackwardAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getAction
argument_list|(
name|NavigateForwardAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|toolBar
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDataNodeChanged
parameter_list|(
name|DataNodeDisplayEvent
name|e
parameter_list|)
block|{
name|actionManager
operator|.
name|dataNodeSelected
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|currentDataMapChanged
parameter_list|(
name|DataMapDisplayEvent
name|e
parameter_list|)
block|{
name|actionManager
operator|.
name|dataMapSelected
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|currentObjEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
name|actionManager
operator|.
name|objEntitySelected
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|currentDbEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
name|boolean
name|derived
init|=
name|e
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|DerivedDbEntity
decl_stmt|;
if|if
condition|(
name|derived
condition|)
block|{
name|actionManager
operator|.
name|derivedDbEntitySelected
argument_list|()
expr_stmt|;
name|getAction
argument_list|(
name|DerivedEntitySyncAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|actionManager
operator|.
name|dbEntitySelected
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|currentQueryChanged
parameter_list|(
name|QueryDisplayEvent
name|e
parameter_list|)
block|{
name|actionManager
operator|.
name|querySelected
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|currentProcedureChanged
parameter_list|(
name|ProcedureDisplayEvent
name|e
parameter_list|)
block|{
name|actionManager
operator|.
name|procedureSelected
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the right side view panel.      *       * @return EditorView      */
specifier|public
name|EditorView
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|JLabel
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
comment|/**      * Returns the recentFileMenu.      *       * @return RecentFileMenu      */
specifier|public
name|RecentFileMenu
name|getRecentFileMenu
parameter_list|()
block|{
return|return
name|recentFileMenu
return|;
block|}
comment|/**      * Adds editor view to the frame.      */
specifier|public
name|void
name|setView
parameter_list|(
name|EditorView
name|view
parameter_list|)
block|{
name|boolean
name|change
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|view
operator|!=
literal|null
condition|)
block|{
name|getContentPane
argument_list|()
operator|.
name|remove
argument_list|(
name|this
operator|.
name|view
argument_list|)
expr_stmt|;
name|change
operator|=
literal|true
expr_stmt|;
block|}
name|this
operator|.
name|view
operator|=
name|view
expr_stmt|;
if|if
condition|(
name|view
operator|!=
literal|null
condition|)
block|{
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|view
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|change
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|change
condition|)
block|{
name|validate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

