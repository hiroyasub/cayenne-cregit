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
name|dialog
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
name|Dimension
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
name|JComponent
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
name|javax
operator|.
name|swing
operator|.
name|JTextArea
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ScrollPaneConstants
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|PanelBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|CellConstraints
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ValidationResultBrowserView
extends|extends
name|JDialog
block|{
specifier|protected
name|JTextArea
name|messageLabel
decl_stmt|;
specifier|protected
name|JTextArea
name|errorsDisplay
decl_stmt|;
specifier|protected
name|JButton
name|closeButton
decl_stmt|;
specifier|public
name|ValidationResultBrowserView
parameter_list|()
block|{
name|this
operator|.
name|closeButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Close"
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageLabel
operator|=
operator|new
name|JTextArea
argument_list|()
expr_stmt|;
name|messageLabel
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|messageLabel
operator|.
name|setLineWrap
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|messageLabel
operator|.
name|setWrapStyleWord
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|errorsDisplay
operator|=
operator|new
name|JTextArea
argument_list|()
expr_stmt|;
name|errorsDisplay
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|errorsDisplay
operator|.
name|setLineWrap
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|errorsDisplay
operator|.
name|setWrapStyleWord
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// assemble
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"fill:min(50dlu;pref):grow"
argument_list|,
literal|"fill:20dlu, 9dlu, p, 3dlu, fill:40dlu:grow"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|messageLabel
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"Details"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|errorsDisplay
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|JPanel
name|buttons
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|closeButton
argument_list|)
expr_stmt|;
name|JComponent
name|container
init|=
operator|(
name|JComponent
operator|)
name|getContentPane
argument_list|()
decl_stmt|;
name|container
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|container
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|container
operator|.
name|add
argument_list|(
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
comment|// update top label bg
name|messageLabel
operator|.
name|setBackground
argument_list|(
name|container
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
comment|// we need the right preferred size so that dialog "pack()" produces decent
comment|// default size...
name|container
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|450
argument_list|,
literal|270
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JButton
name|getCloseButton
parameter_list|()
block|{
return|return
name|closeButton
return|;
block|}
specifier|public
name|JTextArea
name|getErrorsDisplay
parameter_list|()
block|{
return|return
name|errorsDisplay
return|;
block|}
specifier|public
name|JTextArea
name|getMessageLabel
parameter_list|()
block|{
return|return
name|messageLabel
return|;
block|}
block|}
end_class

end_unit

