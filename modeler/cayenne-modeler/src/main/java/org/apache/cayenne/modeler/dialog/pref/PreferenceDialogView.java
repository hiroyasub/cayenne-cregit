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
operator|.
name|pref
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
name|CardLayout
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
name|Dialog
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
name|Frame
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
name|JList
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
name|JSplitPane
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PreferenceDialogView
extends|extends
name|JDialog
block|{
specifier|protected
name|JSplitPane
name|split
decl_stmt|;
specifier|protected
name|JList
name|list
decl_stmt|;
specifier|protected
name|CardLayout
name|detailLayout
decl_stmt|;
specifier|protected
name|Container
name|detailPanel
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|JButton
name|saveButton
decl_stmt|;
specifier|public
name|PreferenceDialogView
parameter_list|(
name|Dialog
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|PreferenceDialogView
parameter_list|(
name|Frame
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|this
operator|.
name|split
operator|=
operator|new
name|JSplitPane
argument_list|(
name|JSplitPane
operator|.
name|HORIZONTAL_SPLIT
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|=
operator|new
name|JList
argument_list|()
expr_stmt|;
name|this
operator|.
name|detailLayout
operator|=
operator|new
name|CardLayout
argument_list|()
expr_stmt|;
name|this
operator|.
name|detailPanel
operator|=
operator|new
name|JPanel
argument_list|(
name|detailLayout
argument_list|)
expr_stmt|;
name|this
operator|.
name|saveButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
name|this
operator|.
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
comment|// assemble
name|Container
name|leftContainer
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
decl_stmt|;
name|leftContainer
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|list
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
name|cancelButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|saveButton
argument_list|)
expr_stmt|;
name|Container
name|rightContainer
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
decl_stmt|;
name|rightContainer
operator|.
name|add
argument_list|(
name|detailPanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|rightContainer
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
name|split
operator|.
name|setLeftComponent
argument_list|(
name|leftContainer
argument_list|)
expr_stmt|;
name|split
operator|.
name|setRightComponent
argument_list|(
name|rightContainer
argument_list|)
expr_stmt|;
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
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|split
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"Edit Preferences"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JList
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
specifier|public
name|JSplitPane
name|getSplit
parameter_list|()
block|{
return|return
name|split
return|;
block|}
specifier|public
name|Container
name|getDetailPanel
parameter_list|()
block|{
return|return
name|detailPanel
return|;
block|}
specifier|public
name|CardLayout
name|getDetailLayout
parameter_list|()
block|{
return|return
name|detailLayout
return|;
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
name|JButton
name|getSaveButton
parameter_list|()
block|{
return|return
name|saveButton
return|;
block|}
block|}
end_class

end_unit

