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
name|editor
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
name|DocumentListener
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
name|BadLocationException
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
name|Document
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
name|event
operator|.
name|QueryEvent
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
name|undo
operator|.
name|JTextFieldUndoListener
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
name|CayenneWidgetFactory
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
name|project
operator|.
name|validator
operator|.
name|EJBQLQueryValidator
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
name|project
operator|.
name|validator
operator|.
name|EJBQLQueryValidator
operator|.
name|PositionException
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
name|Query
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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|JCayenneTextPane
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

begin_class
specifier|public
class|class
name|EjbqlQueryScriptsTab
extends|extends
name|JPanel
implements|implements
name|DocumentListener
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|JCayenneTextPane
name|scriptArea
decl_stmt|;
specifier|private
name|boolean
name|updateDisabled
decl_stmt|;
specifier|protected
name|EJBQLQueryValidator
name|ejbqlQueryValidator
init|=
operator|new
name|EJBQLQueryValidator
argument_list|()
decl_stmt|;
specifier|public
name|EjbqlQueryScriptsTab
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
block|}
name|void
name|displayScript
parameter_list|()
block|{
name|EJBQLQuery
name|query
init|=
name|getQuery
argument_list|()
decl_stmt|;
name|scriptArea
operator|.
name|setText
argument_list|(
name|query
operator|.
name|getEjbqlStatement
argument_list|()
argument_list|)
expr_stmt|;
name|updateDisabled
operator|=
literal|false
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|scriptArea
operator|=
name|CayenneWidgetFactory
operator|.
name|createJEJBQLTextPane
argument_list|()
expr_stmt|;
name|scriptArea
operator|.
name|getPane
argument_list|()
operator|.
name|getDocument
argument_list|()
operator|.
name|addUndoableEditListener
argument_list|(
operator|new
name|JTextFieldUndoListener
argument_list|(
name|scriptArea
operator|.
name|getPane
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
operator|new
name|DocumentListener
argument_list|()
block|{
specifier|public
name|void
name|changedUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|insertUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
try|try
block|{
name|String
name|text
init|=
name|scriptArea
operator|.
name|getDocument
argument_list|()
operator|.
name|getText
argument_list|(
name|e
operator|.
name|getOffset
argument_list|()
argument_list|,
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|equals
argument_list|(
literal|" "
argument_list|)
operator|||
name|text
operator|.
name|equals
argument_list|(
literal|"\n"
argument_list|)
operator|||
name|text
operator|.
name|equals
argument_list|(
literal|"\t"
argument_list|)
condition|)
block|{
name|getQuery
argument_list|()
operator|.
name|setEjbqlStatement
argument_list|(
name|scriptArea
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|validateEJBQL
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|getQuery
argument_list|()
operator|.
name|setEjbqlStatement
argument_list|(
name|scriptArea
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|validateEJBQL
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getPane
argument_list|()
operator|.
name|addFocusListener
argument_list|(
operator|new
name|FocusListener
argument_list|()
block|{
name|EJBQLValidationThread
name|thread
decl_stmt|;
specifier|public
name|void
name|focusGained
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
name|thread
operator|=
operator|new
name|EJBQLValidationThread
argument_list|()
expr_stmt|;
name|thread
operator|.
name|start
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
name|thread
operator|.
name|terminate
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getPane
argument_list|()
operator|.
name|addKeyListener
argument_list|(
operator|new
name|KeyListener
argument_list|()
block|{
name|boolean
name|pasteOrCut
decl_stmt|;
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_END
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_HOME
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_LEFT
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_RIGHT
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_UP
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_UNDO
condition|)
block|{
name|getQuery
argument_list|()
operator|.
name|setEjbqlStatement
argument_list|(
name|scriptArea
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|validateEJBQL
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_V
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_X
operator|)
operator|&&
name|e
operator|.
name|isControlDown
argument_list|()
condition|)
block|{
name|pasteOrCut
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|(
name|pasteOrCut
operator|&&
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_CONTROL
operator|)
operator|||
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_DELETE
condition|)
block|{
name|scriptArea
operator|.
name|removeHighlightText
argument_list|()
expr_stmt|;
name|getQuery
argument_list|()
operator|.
name|setEjbqlStatement
argument_list|(
name|scriptArea
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|validateEJBQL
argument_list|()
expr_stmt|;
name|pasteOrCut
operator|=
literal|false
expr_stmt|;
block|}
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
block|}
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|scriptArea
argument_list|,
name|BorderLayout
operator|.
name|WEST
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|scriptArea
operator|.
name|getScrollPane
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initFromModel
parameter_list|()
block|{
name|Query
name|query
init|=
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|query
operator|instanceof
name|EJBQLQuery
operator|)
condition|)
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|scriptArea
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|displayScript
argument_list|()
expr_stmt|;
name|validateEJBQL
argument_list|()
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|EJBQLQuery
name|getQuery
parameter_list|()
block|{
name|Query
name|query
init|=
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
return|return
operator|(
name|query
operator|instanceof
name|EJBQLQuery
operator|)
condition|?
operator|(
name|EJBQLQuery
operator|)
name|query
else|:
literal|null
return|;
block|}
name|void
name|setEJBQL
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|Document
name|doc
init|=
name|e
operator|.
name|getDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|setEJBQL
argument_list|(
name|doc
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|doc
operator|.
name|getLength
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|setEJBQL
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|EJBQLQuery
name|query
init|=
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|testTemp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|testTemp
operator|=
name|text
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|testTemp
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|text
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// Compare the value before modifying the query - text area
comment|// will call "verify" even if no changes have occured....
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|text
argument_list|,
name|query
operator|.
name|getEjbqlStatement
argument_list|()
argument_list|)
condition|)
block|{
name|query
operator|.
name|setEjbqlStatement
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|this
argument_list|,
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|insertUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|changedUpdate
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|changedUpdate
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|changedUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|updateDisabled
condition|)
block|{
name|setEJBQL
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|validateEJBQL
parameter_list|()
block|{
name|PositionException
name|positionException
init|=
name|ejbqlQueryValidator
operator|.
name|validateEJBQL
argument_list|(
name|getQuery
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|positionException
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|positionException
operator|.
name|getBeginLine
argument_list|()
operator|!=
literal|null
operator|||
name|positionException
operator|.
name|getBeginColumn
argument_list|()
operator|!=
literal|null
operator|||
name|positionException
operator|.
name|getLength
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scriptArea
operator|.
name|setHighlightText
argument_list|(
name|positionException
operator|.
name|getBeginLine
argument_list|()
argument_list|,
name|positionException
operator|.
name|getBeginColumn
argument_list|()
argument_list|,
name|positionException
operator|.
name|getLength
argument_list|()
argument_list|,
name|positionException
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scriptArea
operator|.
name|removeHighlightText
argument_list|()
expr_stmt|;
block|}
block|}
block|}
class|class
name|EJBQLValidationThread
extends|extends
name|Thread
block|{
name|boolean
name|running
decl_stmt|;
name|Object
name|timer
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|previousCaretPosition
decl_stmt|;
name|int
name|validateCaretPosition
decl_stmt|;
specifier|static
specifier|final
name|int
name|DELAY
init|=
literal|500
decl_stmt|;
name|EJBQLValidationThread
parameter_list|()
block|{
name|super
argument_list|(
literal|"ejbql-validation-thread"
argument_list|)
expr_stmt|;
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|running
operator|=
literal|true
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
name|int
name|caretPosition
init|=
name|scriptArea
operator|.
name|getCaretPosition
argument_list|()
decl_stmt|;
if|if
condition|(
name|previousCaretPosition
operator|!=
literal|0
operator|&&
name|previousCaretPosition
operator|==
name|caretPosition
operator|&&
name|validateCaretPosition
operator|!=
name|previousCaretPosition
condition|)
block|{
name|validateEJBQL
argument_list|()
expr_stmt|;
name|validateCaretPosition
operator|=
name|caretPosition
expr_stmt|;
block|}
name|previousCaretPosition
operator|=
name|caretPosition
expr_stmt|;
synchronized|synchronized
init|(
name|timer
init|)
block|{
try|try
block|{
name|timer
operator|.
name|wait
argument_list|(
name|DELAY
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
block|}
specifier|public
name|void
name|terminate
parameter_list|()
block|{
synchronized|synchronized
init|(
name|timer
init|)
block|{
name|running
operator|=
literal|false
expr_stmt|;
name|timer
operator|.
name|notify
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

