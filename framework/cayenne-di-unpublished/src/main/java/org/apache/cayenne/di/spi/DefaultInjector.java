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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ConfigurationException
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
name|Injector
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
name|Module
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
name|Scopes
import|;
end_import

begin_comment
comment|/**  * A default Cayenne implementations of a DI injector.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultInjector
implements|implements
name|Injector
block|{
specifier|private
name|Map
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|bindings
decl_stmt|;
specifier|private
name|InjectionStack
name|injectionStack
decl_stmt|;
specifier|private
name|Scope
name|defaultScope
decl_stmt|;
specifier|public
name|DefaultInjector
parameter_list|(
name|Module
modifier|...
name|modules
parameter_list|)
throws|throws
name|ConfigurationException
block|{
comment|// this is intentionally hardcoded and is not configurable
name|this
operator|.
name|defaultScope
operator|=
name|Scopes
operator|.
name|SINGLETON
expr_stmt|;
name|this
operator|.
name|bindings
operator|=
operator|new
name|HashMap
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|injectionStack
operator|=
operator|new
name|InjectionStack
argument_list|()
expr_stmt|;
name|DefaultBinder
name|binder
init|=
operator|new
name|DefaultBinder
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// bind self for injector injection...
name|binder
operator|.
name|bind
argument_list|(
name|Injector
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// bind modules
if|if
condition|(
name|modules
operator|!=
literal|null
operator|&&
name|modules
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Module
name|module
range|:
name|modules
control|)
block|{
name|module
operator|.
name|configure
argument_list|(
name|binder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|InjectionStack
name|getInjectionStack
parameter_list|()
block|{
return|return
name|injectionStack
return|;
block|}
parameter_list|<
name|T
parameter_list|>
name|Binding
argument_list|<
name|T
argument_list|>
name|getBinding
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null key"
argument_list|)
throw|;
block|}
comment|// may return null - this is intentionally allowed in this non-public method
return|return
operator|(
name|Binding
argument_list|<
name|T
argument_list|>
operator|)
name|bindings
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
parameter_list|<
name|T
parameter_list|>
name|void
name|putBinding
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|bindingKey
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|)
block|{
comment|// TODO: andrus 11/15/2009 - report overriding existing binding??
name|bindings
operator|.
name|put
argument_list|(
name|bindingKey
argument_list|,
operator|new
name|Binding
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider
argument_list|,
name|defaultScope
argument_list|)
argument_list|)
expr_stmt|;
block|}
parameter_list|<
name|T
parameter_list|>
name|void
name|changeBindingScope
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|bindingKey
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
if|if
condition|(
name|scope
operator|==
literal|null
condition|)
block|{
name|scope
operator|=
name|Scopes
operator|.
name|NO_SCOPE
expr_stmt|;
block|}
name|Binding
argument_list|<
name|?
argument_list|>
name|binding
init|=
name|bindings
operator|.
name|get
argument_list|(
name|bindingKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"No existing binding for key "
operator|+
name|bindingKey
argument_list|)
throw|;
block|}
name|binding
operator|.
name|changeScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|ConfigurationException
block|{
return|return
name|getProvider
argument_list|(
name|type
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
throws|throws
name|ConfigurationException
block|{
return|return
name|getProvider
argument_list|(
name|key
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|getProvider
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|ConfigurationException
block|{
return|return
name|getProvider
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|getProvider
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null key"
argument_list|)
throw|;
block|}
name|Binding
argument_list|<
name|T
argument_list|>
name|binding
init|=
operator|(
name|Binding
argument_list|<
name|T
argument_list|>
operator|)
name|bindings
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"DI container has no binding for key %s"
argument_list|,
name|key
argument_list|)
throw|;
block|}
return|return
name|binding
operator|.
name|getScoped
argument_list|()
return|;
block|}
specifier|public
name|void
name|injectMembers
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|Provider
argument_list|<
name|Object
argument_list|>
name|provider0
init|=
operator|new
name|InstanceProvider
argument_list|<
name|Object
argument_list|>
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|Object
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|Object
argument_list|>
argument_list|(
name|provider0
argument_list|,
name|this
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|provider1
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

