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
name|Component
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
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
name|pref
operator|.
name|DBConnectionInfo
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
name|DbAdapterInfo
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
name|BindingBuilder
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
name|ObjectBinding
import|;
end_import

begin_comment
comment|/**  * A reusable editor for DBConnectionInfo object.  *   */
end_comment

begin_class
specifier|public
class|class
name|DBConnectionInfoEditor
extends|extends
name|CayenneController
block|{
comment|// transient placeholder to display disabled form
specifier|private
specifier|static
specifier|final
name|DBConnectionInfo
name|emptyInfo
init|=
operator|new
name|DBConnectionInfo
argument_list|()
decl_stmt|;
specifier|protected
name|DBConnectionInfoEditorView
name|view
decl_stmt|;
specifier|protected
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
specifier|protected
name|ObjectBinding
index|[]
name|bindings
decl_stmt|;
specifier|public
name|DBConnectionInfoEditor
parameter_list|(
name|CayenneController
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|DBConnectionInfoEditorView
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
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
name|this
operator|.
name|view
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DefaultComboBoxModel
name|adapterModel
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|DbAdapterInfo
operator|.
name|getStandardAdapters
argument_list|()
argument_list|)
decl_stmt|;
name|view
operator|.
name|getAdapters
argument_list|()
operator|.
name|setModel
argument_list|(
name|adapterModel
argument_list|)
expr_stmt|;
name|view
operator|.
name|getAdapters
argument_list|()
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|bindings
operator|=
operator|new
name|ObjectBinding
index|[
literal|5
index|]
expr_stmt|;
name|bindings
index|[
literal|0
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getUserName
argument_list|()
argument_list|,
literal|"connectionInfo.userName"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|1
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"connectionInfo.password"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|2
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getDriver
argument_list|()
argument_list|,
literal|"connectionInfo.jdbcDriver"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|3
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getUrl
argument_list|()
argument_list|,
literal|"connectionInfo.url"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|4
index|]
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getAdapters
argument_list|()
argument_list|,
literal|"connectionInfo.dbAdapter"
argument_list|,
literal|"Automatic"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DBConnectionInfo
name|getConnectionInfo
parameter_list|()
block|{
return|return
name|connectionInfo
operator|!=
literal|null
condition|?
name|connectionInfo
else|:
name|emptyInfo
return|;
block|}
specifier|public
name|void
name|setConnectionInfo
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|)
block|{
name|this
operator|.
name|connectionInfo
operator|=
name|connectionInfo
expr_stmt|;
name|refreshView
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|refreshView
parameter_list|()
block|{
name|getView
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|connectionInfo
operator|!=
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectBinding
name|binding
range|:
name|bindings
control|)
block|{
name|binding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

