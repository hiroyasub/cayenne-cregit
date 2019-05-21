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
name|di
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|DIRuntimeException
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
name|di
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ConstructorInjectingDecoratorProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|DecoratorProvider
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|implementation
decl_stmt|;
specifier|private
name|DefaultInjector
name|injector
decl_stmt|;
specifier|public
name|ConstructorInjectingDecoratorProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|implementation
parameter_list|,
name|DefaultInjector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|implementation
operator|=
name|implementation
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Provider
argument_list|<
name|T
argument_list|>
name|get
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|T
argument_list|>
name|undecorated
parameter_list|)
throws|throws
name|DIRuntimeException
block|{
return|return
operator|new
name|ConstructorInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|implementation
argument_list|,
name|injector
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|value
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parameter
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|bindingName
parameter_list|,
name|InjectionStack
name|stack
parameter_list|)
block|{
comment|// delegate (possibly) injected as Provider
if|if
condition|(
name|Provider
operator|.
name|class
operator|.
name|equals
argument_list|(
name|parameter
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
init|=
name|DIUtil
operator|.
name|parameterClass
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"Constructor provider parameter %s must be "
operator|+
literal|"parameterized to be usable for injection"
argument_list|,
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|objectClass
operator|.
name|isAssignableFrom
argument_list|(
name|implementation
argument_list|)
condition|)
block|{
return|return
name|undecorated
return|;
block|}
block|}
comment|// delegate injected as value
if|else if
condition|(
name|parameter
operator|.
name|isAssignableFrom
argument_list|(
name|implementation
argument_list|)
condition|)
block|{
return|return
name|undecorated
operator|.
name|get
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|value
argument_list|(
name|parameter
argument_list|,
name|genericType
argument_list|,
name|bindingName
argument_list|,
name|stack
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

