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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPasswordField
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
comment|/**  * A generic panel for entering DataSource information.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DBConnectionInfoEditorView
extends|extends
name|JPanel
block|{
specifier|protected
name|JComboBox
name|adapters
decl_stmt|;
specifier|protected
name|JTextField
name|driver
decl_stmt|;
specifier|protected
name|JTextField
name|url
decl_stmt|;
specifier|protected
name|JTextField
name|userName
decl_stmt|;
specifier|protected
name|JPasswordField
name|password
decl_stmt|;
specifier|protected
name|Collection
name|labels
decl_stmt|;
specifier|public
name|DBConnectionInfoEditorView
parameter_list|()
block|{
name|adapters
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|adapters
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|driver
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|url
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|userName
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|password
operator|=
operator|new
name|JPasswordField
argument_list|()
expr_stmt|;
name|labels
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
comment|// assemble
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:pref, 3dlu, fill:160dlu:grow"
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
name|labels
operator|.
name|add
argument_list|(
name|builder
operator|.
name|append
argument_list|(
literal|"JDBC Driver:"
argument_list|,
name|driver
argument_list|)
argument_list|)
expr_stmt|;
name|labels
operator|.
name|add
argument_list|(
name|builder
operator|.
name|append
argument_list|(
literal|"DB URL:"
argument_list|,
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|labels
operator|.
name|add
argument_list|(
name|builder
operator|.
name|append
argument_list|(
literal|"User Name:"
argument_list|,
name|userName
argument_list|)
argument_list|)
expr_stmt|;
name|labels
operator|.
name|add
argument_list|(
name|builder
operator|.
name|append
argument_list|(
literal|"Password:"
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
name|labels
operator|.
name|add
argument_list|(
name|builder
operator|.
name|append
argument_list|(
literal|"Adapter (optional):"
argument_list|,
name|adapters
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|this
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
name|JTextField
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
specifier|public
name|JPasswordField
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|JTextField
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|JTextField
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
if|if
condition|(
name|isEnabled
argument_list|()
operator|!=
name|enabled
condition|)
block|{
name|super
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|labels
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Component
name|c
init|=
operator|(
name|Component
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
name|adapters
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|driver
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|url
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|userName
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|password
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

