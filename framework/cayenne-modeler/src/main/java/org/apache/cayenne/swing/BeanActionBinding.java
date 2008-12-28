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
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|BeanInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|EventSetDescriptor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|Introspector
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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * A binding that registers for action events of an arbitrary Component JavaBean that  * provides a way to add an ActionListener via BeanDescriptor.  *   */
end_comment

begin_class
specifier|public
class|class
name|BeanActionBinding
extends|extends
name|BindingBase
block|{
specifier|protected
name|Component
name|view
decl_stmt|;
specifier|public
name|BeanActionBinding
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|actionExpression
parameter_list|)
block|{
name|super
argument_list|(
name|actionExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|component
expr_stmt|;
name|boolean
name|foundActionEvents
init|=
literal|false
decl_stmt|;
try|try
block|{
name|BeanInfo
name|info
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|component
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|EventSetDescriptor
index|[]
name|events
init|=
name|info
operator|.
name|getEventSetDescriptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|events
operator|!=
literal|null
operator|&&
name|events
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|EventSetDescriptor
name|event
range|:
name|events
control|)
block|{
if|if
condition|(
name|ActionListener
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|event
operator|.
name|getListenerType
argument_list|()
argument_list|)
condition|)
block|{
name|event
operator|.
name|getAddListenerMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|component
argument_list|,
operator|new
name|Object
index|[]
block|{
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|fireAction
argument_list|()
expr_stmt|;
block_content|}
block|}                         }
block|)
empty_stmt|;
name|foundActionEvents
operator|=
literal|true
argument_list|;
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error binding to component"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|foundActionEvents
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Component does not define action events: "
operator|+
name|component
argument_list|)
throw|;
block|}
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
comment|// TODO: catch exceptions...
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

