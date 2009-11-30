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
name|List
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
name|di
operator|.
name|DIException
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

begin_comment
comment|/**  * @since 3.1  */
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
name|String
argument_list|,
name|Provider
argument_list|<
name|?
argument_list|>
argument_list|>
name|bindings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MapProvider
argument_list|>
name|mapConfigurations
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ListProvider
argument_list|>
name|listConfigurations
decl_stmt|;
specifier|private
name|InjectionStack
name|injectionStack
decl_stmt|;
specifier|public
name|DefaultInjector
parameter_list|(
name|Module
modifier|...
name|modules
parameter_list|)
throws|throws
name|DIException
block|{
name|this
operator|.
name|bindings
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|mapConfigurations
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MapProvider
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|listConfigurations
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ListProvider
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
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|?
argument_list|>
argument_list|>
name|getBindings
parameter_list|()
block|{
return|return
name|bindings
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|MapProvider
argument_list|>
name|getMapConfigurations
parameter_list|()
block|{
return|return
name|mapConfigurations
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|ListProvider
argument_list|>
name|getListConfigurations
parameter_list|()
block|{
return|return
name|listConfigurations
return|;
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
name|DIException
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
name|List
argument_list|<
name|?
argument_list|>
name|getListConfiguration
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null type"
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|DIUtil
operator|.
name|toKey
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|ListProvider
name|provider
init|=
name|listConfigurations
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIException
argument_list|(
literal|"Type '%s' has no bound list configuration in the DI container."
operator|+
literal|" Injection stack: %s"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|injectionStack
argument_list|)
throw|;
block|}
return|return
name|provider
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|getMapConfiguration
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null type"
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|DIUtil
operator|.
name|toKey
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|MapProvider
name|provider
init|=
name|mapConfigurations
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIException
argument_list|(
literal|"Type '%s' has no bound map configuration in the DI container."
operator|+
literal|" Injection stack: %s"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|injectionStack
argument_list|)
throw|;
block|}
return|return
name|provider
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
name|DIException
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null type"
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|DIUtil
operator|.
name|toKey
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
init|=
operator|(
name|Provider
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
name|provider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIException
argument_list|(
literal|"Type '%s' is not bound in the DI container."
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|provider
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
name|DIUtil
operator|.
name|toKey
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

