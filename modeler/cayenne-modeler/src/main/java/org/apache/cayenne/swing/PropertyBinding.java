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
name|java
operator|.
name|beans
operator|.
name|PropertyChangeEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_comment
comment|/**  * A binding for connecting to children that implement BoundComponent.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PropertyBinding
extends|extends
name|BindingBase
block|{
specifier|protected
name|BoundComponent
name|boundComponent
decl_stmt|;
specifier|protected
name|String
name|boundExpression
decl_stmt|;
specifier|public
name|PropertyBinding
parameter_list|(
name|BoundComponent
name|boundComponent
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
name|this
operator|.
name|boundExpression
operator|=
name|boundExpression
expr_stmt|;
name|this
operator|.
name|boundComponent
operator|=
name|boundComponent
expr_stmt|;
name|this
operator|.
name|boundComponent
operator|.
name|addPropertyChangeListener
argument_list|(
name|boundExpression
argument_list|,
operator|new
name|PropertyChangeListener
argument_list|()
block|{
specifier|public
name|void
name|propertyChange
parameter_list|(
name|PropertyChangeEvent
name|event
parameter_list|)
block|{
name|setValue
argument_list|(
name|event
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|boundComponent
operator|.
name|getView
argument_list|()
return|;
block|}
specifier|public
name|void
name|updateView
parameter_list|()
block|{
name|boundComponent
operator|.
name|bindingUpdated
argument_list|(
name|boundExpression
argument_list|,
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

