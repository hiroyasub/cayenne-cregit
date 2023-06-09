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
name|undo
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|DataMap
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
name|ObjEntity
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
name|query
operator|.
name|SQLTemplate
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
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultMutableTreeNode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|AbstractUndoableEdit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotRedoException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotUndoException
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

begin_class
specifier|public
class|class
name|JUndoableCheckBoxEdit
extends|extends
name|AbstractUndoableEdit
block|{
specifier|private
name|JCheckBox
name|checkBox
decl_stmt|;
specifier|private
name|ActionListener
name|actionListener
decl_stmt|;
specifier|private
name|JTabbedPane
name|tabbedPane
decl_stmt|;
specifier|private
name|TreePath
name|treePath
decl_stmt|;
specifier|private
name|Object
name|targetObject
decl_stmt|;
specifier|private
name|EditorView
name|editorView
decl_stmt|;
specifier|private
name|int
name|selectedTabIndex
decl_stmt|;
specifier|private
name|boolean
name|isSelected
decl_stmt|;
name|JUndoableCheckBoxEdit
parameter_list|(
name|JCheckBox
name|checkBox
parameter_list|,
name|ActionListener
name|actionListener
parameter_list|)
block|{
name|this
operator|.
name|checkBox
operator|=
name|checkBox
expr_stmt|;
name|this
operator|.
name|actionListener
operator|=
name|actionListener
expr_stmt|;
name|this
operator|.
name|isSelected
operator|=
name|checkBox
operator|.
name|isSelected
argument_list|()
expr_stmt|;
name|editorView
operator|=
operator|(
operator|(
name|CayenneModelerFrame
operator|)
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getView
argument_list|()
operator|)
operator|.
name|getView
argument_list|()
expr_stmt|;
name|treePath
operator|=
name|editorView
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|treePath
operator|!=
literal|null
condition|)
block|{
name|DefaultMutableTreeNode
name|newPath
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|treePath
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
name|targetObject
operator|=
name|newPath
operator|.
name|getUserObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|targetObject
operator|instanceof
name|DataChannelDescriptor
condition|)
block|{
name|tabbedPane
operator|=
name|editorView
operator|.
name|getDataDomainView
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|targetObject
operator|instanceof
name|DataMap
condition|)
block|{
name|tabbedPane
operator|=
name|editorView
operator|.
name|getDataMapView
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|targetObject
operator|instanceof
name|ObjEntity
condition|)
block|{
name|tabbedPane
operator|=
name|editorView
operator|.
name|getObjDetailView
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|targetObject
operator|instanceof
name|SQLTemplate
condition|)
block|{
name|tabbedPane
operator|=
name|editorView
operator|.
name|getSqlTemplateView
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|tabbedPane
operator|!=
literal|null
condition|)
block|{
name|selectedTabIndex
operator|=
name|tabbedPane
operator|.
name|getSelectedIndex
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|restoreSelections
parameter_list|()
block|{
name|editorView
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|treePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|tabbedPane
operator|!=
literal|null
condition|)
block|{
name|tabbedPane
operator|.
name|setSelectedIndex
argument_list|(
name|selectedTabIndex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"CheckBox Change"
return|;
block|}
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|super
operator|.
name|redo
argument_list|()
expr_stmt|;
name|restoreSelections
argument_list|()
expr_stmt|;
name|checkBox
operator|.
name|removeActionListener
argument_list|(
name|actionListener
argument_list|)
expr_stmt|;
try|try
block|{
name|checkBox
operator|.
name|setSelected
argument_list|(
name|isSelected
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|checkBox
operator|.
name|addActionListener
argument_list|(
name|actionListener
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
name|super
operator|.
name|undo
argument_list|()
expr_stmt|;
name|restoreSelections
argument_list|()
expr_stmt|;
name|checkBox
operator|.
name|removeActionListener
argument_list|(
name|actionListener
argument_list|)
expr_stmt|;
try|try
block|{
name|checkBox
operator|.
name|setSelected
argument_list|(
operator|!
name|isSelected
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|checkBox
operator|.
name|addActionListener
argument_list|(
name|actionListener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

