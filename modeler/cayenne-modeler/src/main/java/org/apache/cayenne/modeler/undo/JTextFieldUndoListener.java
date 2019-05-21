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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|FocusAdapter
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
name|KeyAdapter
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
name|event
operator|.
name|DocumentEvent
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
name|UndoableEditEvent
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
name|UndoableEditListener
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
name|AbstractDocument
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
name|JTextComponent
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
name|CompoundEdit
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
name|util
operator|.
name|TextAdapter
import|;
end_import

begin_class
specifier|public
class|class
name|JTextFieldUndoListener
implements|implements
name|UndoableEditListener
block|{
specifier|public
name|CompoundEdit
name|compoundEdit
decl_stmt|;
specifier|private
name|TextAdapter
name|adapter
decl_stmt|;
specifier|private
name|JTextComponent
name|editor
decl_stmt|;
specifier|private
name|int
name|lastOffset
decl_stmt|;
specifier|private
name|int
name|lastLength
decl_stmt|;
specifier|private
name|boolean
name|isKeyEdit
decl_stmt|;
specifier|public
name|JTextFieldUndoListener
parameter_list|(
name|TextAdapter
name|adapter
parameter_list|)
block|{
name|this
argument_list|(
name|adapter
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
specifier|public
name|JTextFieldUndoListener
parameter_list|(
name|JTextComponent
name|editor
parameter_list|)
block|{
name|this
operator|.
name|editor
operator|=
name|editor
expr_stmt|;
name|this
operator|.
name|editor
operator|.
name|addFocusListener
argument_list|(
operator|new
name|FocusAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|focusLost
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
name|finishCurrentEdit
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|editor
operator|.
name|addKeyListener
argument_list|(
operator|new
name|KeyAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
name|isKeyEdit
operator|=
literal|true
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|undoableEditHappened
parameter_list|(
name|UndoableEditEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|compoundEdit
operator|==
literal|null
operator|||
operator|!
name|compoundEdit
operator|.
name|canUndo
argument_list|()
condition|)
block|{
name|compoundEdit
operator|=
name|startCompoundEdit
argument_list|(
name|e
operator|.
name|getEdit
argument_list|()
argument_list|)
expr_stmt|;
name|lastLength
operator|=
name|editor
operator|.
name|getDocument
argument_list|()
operator|.
name|getLength
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// See AbstractDocument.DefaultDocumentEvent.getPresentationName() method
if|if
condition|(
literal|"AbstractDocument.styleChangeText"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getEdit
argument_list|()
operator|.
name|getPresentationName
argument_list|()
argument_list|)
condition|)
block|{
name|compoundEdit
operator|.
name|addEdit
argument_list|(
name|e
operator|.
name|getEdit
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|offsetChange
init|=
name|editor
operator|.
name|getCaretPosition
argument_list|()
operator|-
name|lastOffset
decl_stmt|;
name|int
name|lengthChange
init|=
name|editor
operator|.
name|getDocument
argument_list|()
operator|.
name|getLength
argument_list|()
operator|-
name|lastLength
decl_stmt|;
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|offsetChange
argument_list|)
operator|==
literal|1
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|lengthChange
argument_list|)
operator|==
literal|1
condition|)
block|{
name|compoundEdit
operator|.
name|addEdit
argument_list|(
name|e
operator|.
name|getEdit
argument_list|()
argument_list|)
expr_stmt|;
name|lastOffset
operator|=
name|editor
operator|.
name|getCaretPosition
argument_list|()
expr_stmt|;
name|lastLength
operator|=
name|editor
operator|.
name|getDocument
argument_list|()
operator|.
name|getLength
argument_list|()
expr_stmt|;
return|return;
block|}
name|compoundEdit
operator|.
name|end
argument_list|()
expr_stmt|;
name|compoundEdit
operator|=
name|startCompoundEdit
argument_list|(
name|e
operator|.
name|getEdit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CompoundEdit
name|startCompoundEdit
parameter_list|(
name|UndoableEdit
name|anEdit
parameter_list|)
block|{
name|lastOffset
operator|=
name|editor
operator|.
name|getCaretPosition
argument_list|()
expr_stmt|;
name|lastLength
operator|=
name|editor
operator|.
name|getDocument
argument_list|()
operator|.
name|getLength
argument_list|()
expr_stmt|;
name|compoundEdit
operator|=
operator|(
name|this
operator|.
name|adapter
operator|!=
literal|null
operator|)
condition|?
operator|new
name|TextCompoundEdit
argument_list|(
name|adapter
argument_list|,
name|this
argument_list|)
else|:
operator|new
name|TextCompoundEdit
argument_list|(
name|editor
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|isKeyEdit
condition|)
block|{
name|compoundEdit
operator|.
name|addEdit
argument_list|(
name|anEdit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
name|compoundEdit
argument_list|)
expr_stmt|;
return|return
name|compoundEdit
return|;
block|}
specifier|public
name|void
name|finishCurrentEdit
parameter_list|()
block|{
if|if
condition|(
name|compoundEdit
operator|!=
literal|null
condition|)
block|{
name|compoundEdit
operator|.
name|end
argument_list|()
expr_stmt|;
name|compoundEdit
operator|=
literal|null
expr_stmt|;
name|isKeyEdit
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

