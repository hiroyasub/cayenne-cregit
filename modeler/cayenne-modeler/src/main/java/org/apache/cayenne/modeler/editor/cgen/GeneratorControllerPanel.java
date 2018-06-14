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
operator|.
name|cgen
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
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

begin_comment
comment|/**  * A generic panel that is a superclass of generator panels, defining common fields.  *   */
end_comment

begin_class
specifier|public
class|class
name|GeneratorControllerPanel
extends|extends
name|JPanel
block|{
specifier|protected
name|Collection
argument_list|<
name|StandardPanelComponent
argument_list|>
name|dataMapLines
decl_stmt|;
specifier|protected
name|JTextField
name|outputFolder
decl_stmt|;
specifier|protected
name|JButton
name|selectOutputFolder
decl_stmt|;
specifier|public
name|GeneratorControllerPanel
parameter_list|()
block|{
name|this
operator|.
name|dataMapLines
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|outputFolder
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectOutputFolder
operator|=
operator|new
name|JButton
argument_list|(
literal|"Select"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JTextField
name|getOutputFolder
parameter_list|()
block|{
return|return
name|outputFolder
return|;
block|}
specifier|public
name|JButton
name|getSelectOutputFolder
parameter_list|()
block|{
return|return
name|selectOutputFolder
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|StandardPanelComponent
argument_list|>
name|getDataMapLines
parameter_list|()
block|{
return|return
name|dataMapLines
return|;
block|}
block|}
end_class

end_unit

