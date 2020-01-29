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
name|swing
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
name|AbstractButton
import|;
end_import

begin_class
specifier|public
class|class
name|ActionBinding
extends|extends
name|BindingBase
block|{
specifier|protected
name|Component
name|view
decl_stmt|;
specifier|public
name|ActionBinding
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|propertyExpression
parameter_list|)
block|{
name|super
argument_list|(
name|propertyExpression
argument_list|)
expr_stmt|;
name|button
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|fireAction
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|button
expr_stmt|;
block|}
specifier|public
name|ActionBinding
parameter_list|(
name|BoundComponent
name|component
parameter_list|,
name|String
name|propertyExpression
parameter_list|,
name|String
name|boundExpression
parameter_list|)
block|{
name|super
argument_list|(
name|propertyExpression
argument_list|)
expr_stmt|;
name|component
operator|.
name|addPropertyChangeListener
argument_list|(
name|boundExpression
argument_list|,
name|event
lambda|->
name|fireAction
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|component
operator|.
name|getView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
if|if
condition|(
name|view
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BindingException
argument_list|(
literal|"headless action"
argument_list|)
throw|;
block|}
return|return
name|view
return|;
block|}
specifier|public
name|void
name|updateView
parameter_list|()
block|{
comment|// noop
block|}
specifier|protected
name|void
name|fireAction
parameter_list|()
block|{
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

