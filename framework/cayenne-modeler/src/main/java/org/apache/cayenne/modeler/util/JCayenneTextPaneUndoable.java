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
name|util
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
name|UndoableEditListener
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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
operator|.
name|SyntaxConstant
import|;
end_import

begin_class
class|class
name|JCayenneTextPaneUndoable
extends|extends
name|JCayenneTextPane
block|{
specifier|private
name|UndoableEditListener
name|undoListener
decl_stmt|;
name|JCayenneTextPaneUndoable
parameter_list|(
name|SyntaxConstant
name|syntaxConstant
parameter_list|)
block|{
name|super
argument_list|(
name|syntaxConstant
argument_list|)
expr_stmt|;
name|this
operator|.
name|undoListener
operator|=
operator|new
name|JTextFieldUndoListener
argument_list|(
name|this
operator|.
name|getPane
argument_list|()
argument_list|)
expr_stmt|;
name|getDocument
argument_list|()
operator|.
name|addUndoableEditListener
argument_list|(
name|this
operator|.
name|undoListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|getDocument
argument_list|()
operator|.
name|removeUndoableEditListener
argument_list|(
name|this
operator|.
name|undoListener
argument_list|)
expr_stmt|;
try|try
block|{
name|super
operator|.
name|setText
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getDocument
argument_list|()
operator|.
name|addUndoableEditListener
argument_list|(
name|this
operator|.
name|undoListener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

