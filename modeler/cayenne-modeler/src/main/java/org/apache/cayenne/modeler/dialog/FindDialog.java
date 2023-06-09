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
name|modeler
operator|.
name|action
operator|.
name|FindAction
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
name|CayenneController
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
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
name|ActionListener
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
name|InputEvent
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * An instance of this class is responsible for displaying search results and navigating  * to the selected entity's representation.  */
end_comment

begin_class
specifier|public
class|class
name|FindDialog
extends|extends
name|CayenneController
block|{
specifier|private
name|FindDialogView
name|view
decl_stmt|;
specifier|private
name|List
argument_list|<
name|FindAction
operator|.
name|SearchResultEntry
argument_list|>
name|searchResults
decl_stmt|;
specifier|public
name|FindDialog
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|List
argument_list|<
name|FindAction
operator|.
name|SearchResultEntry
argument_list|>
name|searchResults
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|searchResults
operator|=
name|searchResults
expr_stmt|;
name|view
operator|=
operator|new
name|FindDialogView
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|view
operator|.
name|setDefaultCloseOperation
argument_list|(
name|JDialog
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|view
operator|.
name|getOkButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|view
operator|.
name|dispose
argument_list|()
argument_list|)
expr_stmt|;
name|JTable
name|table
init|=
name|view
operator|.
name|getTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|setRowHeight
argument_list|(
literal|24
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|JumpToResultActionListener
name|listener
init|=
operator|new
name|JumpToResultActionListener
argument_list|()
decl_stmt|;
name|table
operator|.
name|addKeyListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|table
operator|.
name|addMouseListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionInterval
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|JumpToResultActionListener
implements|implements
name|MouseListener
implements|,
name|KeyListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|openResult
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|!=
name|KeyEvent
operator|.
name|VK_ENTER
condition|)
block|{
return|return;
block|}
name|openResult
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|openResult
parameter_list|(
name|InputEvent
name|e
parameter_list|)
block|{
name|JTable
name|table
init|=
operator|(
name|JTable
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Integer
name|selectedLine
init|=
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|getLeadSelectionIndex
argument_list|()
decl_stmt|;
name|FindAction
operator|.
name|jumpToResult
argument_list|(
name|searchResults
operator|.
name|get
argument_list|(
name|selectedLine
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|keyTyped
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

