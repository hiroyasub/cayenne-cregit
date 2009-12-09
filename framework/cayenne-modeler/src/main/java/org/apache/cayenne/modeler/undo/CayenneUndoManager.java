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
name|undo
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeSelectionEvent
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
name|TreeSelectionListener
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
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|UndoableEdit
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
name|action
operator|.
name|RedoAction
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
name|UndoAction
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
name|query
operator|.
name|EJBQLQuery
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

begin_class
specifier|public
class|class
name|CayenneUndoManager
extends|extends
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|UndoManager
implements|implements
name|TreeSelectionListener
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|TreeSelectionEvent
name|event
parameter_list|)
block|{
name|UndoableEdit
name|e
init|=
name|editToBeUndone
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|TextCompoundEdit
condition|)
block|{
name|TextCompoundEdit
name|edit
init|=
operator|(
name|TextCompoundEdit
operator|)
name|e
decl_stmt|;
if|if
condition|(
name|edit
operator|.
name|getTargetObject
argument_list|()
operator|instanceof
name|SQLTemplate
operator|||
name|edit
operator|.
name|getTargetObject
argument_list|()
operator|instanceof
name|EJBQLQuery
condition|)
block|{
name|trimEdits
argument_list|(
name|edits
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|,
name|edits
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|updateUI
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Application
name|application
decl_stmt|;
specifier|public
name|CayenneUndoManager
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
name|setLimit
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|discardAllEdits
parameter_list|()
block|{
name|super
operator|.
name|discardAllEdits
argument_list|()
expr_stmt|;
name|updateUI
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|boolean
name|addEdit
parameter_list|(
name|UndoableEdit
name|anEdit
parameter_list|)
block|{
name|boolean
name|result
init|=
name|super
operator|.
name|addEdit
argument_list|(
name|anEdit
argument_list|)
decl_stmt|;
name|updateUI
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|UndoableEdit
name|e
init|=
name|editToBeRedone
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|TextCompoundEdit
condition|)
block|{
name|TextCompoundEdit
name|edit
init|=
operator|(
name|TextCompoundEdit
operator|)
name|e
decl_stmt|;
name|edit
operator|.
name|watchCaretPosition
argument_list|()
expr_stmt|;
name|super
operator|.
name|redo
argument_list|()
expr_stmt|;
name|edit
operator|.
name|stopWatchingCaretPosition
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|redo
argument_list|()
expr_stmt|;
block|}
name|updateUI
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
name|UndoableEdit
name|e
init|=
name|editToBeUndone
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|TextCompoundEdit
condition|)
block|{
name|TextCompoundEdit
name|edit
init|=
operator|(
name|TextCompoundEdit
operator|)
name|e
decl_stmt|;
name|edit
operator|.
name|watchCaretPosition
argument_list|()
expr_stmt|;
name|super
operator|.
name|undo
argument_list|()
expr_stmt|;
name|edit
operator|.
name|stopWatchingCaretPosition
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|undo
argument_list|()
expr_stmt|;
block|}
name|updateUI
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateUI
parameter_list|()
block|{
name|CayenneAction
name|undoAction
init|=
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|UndoAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|CayenneAction
name|redoAction
init|=
name|application
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|RedoAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|undoAction
operator|.
name|setEnabled
argument_list|(
name|canUndo
argument_list|()
argument_list|)
expr_stmt|;
name|redoAction
operator|.
name|setEnabled
argument_list|(
name|canRedo
argument_list|()
argument_list|)
expr_stmt|;
name|undoAction
operator|.
name|setName
argument_list|(
name|getUndoPresentationName
argument_list|()
argument_list|)
expr_stmt|;
name|redoAction
operator|.
name|setName
argument_list|(
name|getRedoPresentationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

