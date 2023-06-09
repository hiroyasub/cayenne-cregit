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
name|util
package|;
end_package

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
name|JDialog
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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JProgressBar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingConstants
import|;
end_import

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
name|Container
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

begin_comment
comment|/**  * A dialog rendering a progress bar. It is normally controlled by a subclass of  * LongRunningTask.  *   */
end_comment

begin_class
specifier|public
class|class
name|ProgressDialog
extends|extends
name|JDialog
block|{
specifier|protected
name|JProgressBar
name|progressBar
decl_stmt|;
specifier|protected
name|JLabel
name|statusLabel
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|public
name|ProgressDialog
parameter_list|(
name|JFrame
name|parent
parameter_list|,
name|String
name|title
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|progressBar
operator|=
operator|new
name|JProgressBar
argument_list|()
expr_stmt|;
name|statusLabel
operator|=
operator|new
name|JLabel
argument_list|(
name|message
argument_list|,
name|SwingConstants
operator|.
name|LEFT
argument_list|)
expr_stmt|;
name|JLabel
name|messageLabel
init|=
operator|new
name|JLabel
argument_list|(
name|message
argument_list|,
name|SwingConstants
operator|.
name|LEFT
argument_list|)
decl_stmt|;
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
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
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"fill:max(250dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p"
argument_list|)
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
name|layout
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
name|messageLabel
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
name|add
argument_list|(
name|progressBar
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
name|statusLabel
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
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|cancelButton
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
name|cancelButton
argument_list|)
expr_stmt|;
name|Container
name|root
init|=
name|getContentPane
argument_list|()
decl_stmt|;
name|root
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|root
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
name|root
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
name|setResizable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|pack
argument_list|()
expr_stmt|;
name|ModelerUtil
operator|.
name|centerWindow
argument_list|(
name|getOwner
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JButton
name|getCancelButton
parameter_list|()
block|{
return|return
name|cancelButton
return|;
block|}
specifier|public
name|JLabel
name|getStatusLabel
parameter_list|()
block|{
return|return
name|statusLabel
return|;
block|}
specifier|public
name|JProgressBar
name|getProgressBar
parameter_list|()
block|{
return|return
name|progressBar
return|;
block|}
block|}
end_class

end_unit

