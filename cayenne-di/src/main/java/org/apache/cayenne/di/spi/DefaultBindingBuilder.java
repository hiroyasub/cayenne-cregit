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
name|di
operator|.
name|Key
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Scope
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|DefaultBindingBuilder
parameter_list|<
name|T
parameter_list|>
implements|implements
name|BindingBuilder
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
name|DefaultInjector
name|injector
decl_stmt|;
specifier|protected
name|Key
argument_list|<
name|T
argument_list|>
name|bindingKey
decl_stmt|;
name|DefaultBindingBuilder
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|bindingKey
parameter_list|,
name|DefaultInjector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
name|this
operator|.
name|bindingKey
operator|=
name|bindingKey
expr_stmt|;
block|}
specifier|public
name|BindingBuilder
argument_list|<
name|T
argument_list|>
name|to
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|implementation
parameter_list|)
throws|throws
name|DIRuntimeException
block|{
name|Provider
argument_list|<
name|T
argument_list|>
name|provider0
init|=
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
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider0
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|injector
operator|.
name|putBinding
argument_list|(
name|bindingKey
argument_list|,
name|provider1
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|BindingBuilder
argument_list|<
name|T
argument_list|>
name|toInstance
parameter_list|(
name|T
name|instance
parameter_list|)
throws|throws
name|DIRuntimeException
block|{
name|Provider
argument_list|<
name|T
argument_list|>
name|provider0
init|=
operator|new
name|InstanceProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|instance
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider0
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|injector
operator|.
name|putBinding
argument_list|(
name|bindingKey
argument_list|,
name|provider1
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
empty_stmt|;
specifier|public
name|BindingBuilder
argument_list|<
name|T
argument_list|>
name|toProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|providerType
parameter_list|)
block|{
name|Provider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|provider0
init|=
operator|new
name|ConstructorInjectingProvider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|(
name|providerType
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|(
name|provider0
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider2
init|=
operator|new
name|CustomProvidersProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider1
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider3
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider2
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|injector
operator|.
name|putBinding
argument_list|(
name|bindingKey
argument_list|,
name|provider3
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|BindingBuilder
argument_list|<
name|T
argument_list|>
name|toProviderInstance
parameter_list|(
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|)
block|{
name|Provider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|provider0
init|=
operator|new
name|InstanceProvider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|(
name|provider
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|(
name|provider0
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider2
init|=
operator|new
name|CustomProvidersProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider1
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider3
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider2
argument_list|,
name|injector
argument_list|)
decl_stmt|;
name|injector
operator|.
name|putBinding
argument_list|(
name|bindingKey
argument_list|,
name|provider3
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|void
name|in
parameter_list|(
name|Scope
name|scope
parameter_list|)
block|{
name|injector
operator|.
name|changeBindingScope
argument_list|(
name|bindingKey
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|withoutScope
parameter_list|()
block|{
name|in
argument_list|(
name|injector
operator|.
name|getNoScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|inSingletonScope
parameter_list|()
block|{
name|in
argument_list|(
name|injector
operator|.
name|getSingletonScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

