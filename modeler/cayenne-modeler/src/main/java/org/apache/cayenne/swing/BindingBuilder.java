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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|JTextComponent
import|;
end_import

begin_comment
comment|/**  * A builder for component bindings that delegates the creation of the binding to the  * underlying factory, and itself configures a number of binding parameters.  *   */
end_comment

begin_class
specifier|public
class|class
name|BindingBuilder
block|{
specifier|protected
name|BindingFactory
name|factory
decl_stmt|;
specifier|protected
name|BindingDelegate
name|delegate
decl_stmt|;
specifier|protected
name|Object
name|context
decl_stmt|;
specifier|protected
name|Map
name|actionsMap
decl_stmt|;
comment|/**      * Constructs BindingBuilder with a BindingFactory and a root model object (or      * context) of the binding.      */
specifier|public
name|BindingBuilder
parameter_list|(
name|BindingFactory
name|factory
parameter_list|,
name|Object
name|context
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|BindingDelegate
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|/**      * Sets BindingDelegate that will be assigned to all bindings created via this      * BindingBuilder.      */
specifier|public
name|void
name|setDelegate
parameter_list|(
name|BindingDelegate
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|Object
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Sets the context object that will be used by all bindings created via this      * BindingBuilder. Context is a root of the domain model for the given binding.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|Object
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|BindingFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
comment|/**      * Binds to an instance of BoundComponent.      *       * @since 1.2      */
specifier|public
name|ObjectBinding
name|bindToProperty
parameter_list|(
name|BoundComponent
name|component
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|boundProperty
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToProperty
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|boundProperty
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|BoundComponent
name|component
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|boundProperty
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToAction
argument_list|(
name|component
argument_list|,
name|action
argument_list|,
name|boundProperty
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToStateChange
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToStateChange
argument_list|(
name|button
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToStateChangeAndAction
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToStateChange
argument_list|(
name|button
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|getActionDelegate
argument_list|(
name|action
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|AbstractButton
name|button
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToAction
argument_list|(
name|button
argument_list|,
name|action
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToAction
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToAction
argument_list|(
name|component
argument_list|,
name|action
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToComboSelection
parameter_list|(
name|JComboBox
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToComboSelection
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToComboSelection
parameter_list|(
name|JComboBox
name|component
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|noSelectionValue
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToComboSelection
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|noSelectionValue
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToComboSelection
parameter_list|(
name|JComboBox
name|component
parameter_list|,
name|String
name|property
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|noSelectionValue
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToComboSelection
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|noSelectionValue
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|getActionDelegate
argument_list|(
name|action
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToTextArea
parameter_list|(
name|JTextArea
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToTextArea
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToTextField
parameter_list|(
name|JTextField
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToTextField
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|public
name|ObjectBinding
name|bindToCheckBox
parameter_list|(
name|JCheckBox
name|component
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|ObjectBinding
name|binding
init|=
name|factory
operator|.
name|bindToCheckBox
argument_list|(
name|component
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|initBinding
argument_list|(
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
specifier|protected
name|ObjectBinding
name|initBinding
parameter_list|(
name|ObjectBinding
name|binding
parameter_list|,
name|BindingDelegate
name|delegate
parameter_list|)
block|{
name|binding
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|protected
name|BindingDelegate
name|getActionDelegate
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|BindingDelegate
name|delegate
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|actionsMap
operator|==
literal|null
condition|)
block|{
name|actionsMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|delegate
operator|=
operator|(
name|BindingDelegate
operator|)
name|actionsMap
operator|.
name|get
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
operator|=
operator|new
name|ActionDelegate
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|actionsMap
operator|.
name|put
argument_list|(
name|action
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
return|return
name|delegate
return|;
block|}
block|}
end_class

end_unit

