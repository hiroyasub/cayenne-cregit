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
name|JComboBox
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
name|JTextField
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
name|DefaultFormBuilder
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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataSourceCreatorView
extends|extends
name|JDialog
block|{
specifier|protected
name|JTextField
name|dataSourceName
decl_stmt|;
specifier|protected
name|JComboBox
name|adapters
decl_stmt|;
specifier|protected
name|JButton
name|okButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|public
name|DataSourceCreatorView
parameter_list|(
name|JDialog
name|owner
parameter_list|)
block|{
name|super
argument_list|(
name|owner
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSourceName
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|adapters
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|okButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Create"
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
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:pref, 3dlu, fill:max(50dlu;pref):grow"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
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
name|append
argument_list|(
literal|"Name:"
argument_list|,
name|dataSourceName
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Adapter:"
argument_list|,
name|adapters
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
name|okButton
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
name|getContentPane
argument_list|()
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
name|setTitle
argument_list|(
literal|"Create New Local DataSource"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JComboBox
name|getAdapters
parameter_list|()
block|{
return|return
name|adapters
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
name|getOkButton
parameter_list|()
block|{
return|return
name|okButton
return|;
block|}
specifier|public
name|JTextField
name|getDataSourceName
parameter_list|()
block|{
return|return
name|dataSourceName
return|;
block|}
block|}
end_class

end_unit

