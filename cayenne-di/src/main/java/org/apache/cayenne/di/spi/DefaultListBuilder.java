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
name|ListBuilder
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
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|DefaultListBuilder
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DICollectionBuilder
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|,
name|T
argument_list|>
implements|implements
name|ListBuilder
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
specifier|static
name|AtomicLong
name|incrementer
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
name|DefaultListBuilder
parameter_list|(
name|Key
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|bindingKey
parameter_list|,
name|DefaultInjector
name|injector
parameter_list|)
block|{
name|super
argument_list|(
name|bindingKey
argument_list|,
name|injector
argument_list|)
expr_stmt|;
comment|// trigger initialization of the ListProvider right away, as we need to bind an
comment|// empty list even if the user never calls 'put'
name|findOrCreateListProvider
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|add
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|interfaceType
parameter_list|)
block|{
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
init|=
name|createTypeProvider
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|add
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|interfaceType
argument_list|)
argument_list|,
name|provider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|addAfter
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|afterType
parameter_list|)
block|{
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
init|=
name|createTypeProvider
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|addAfter
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|interfaceType
argument_list|)
argument_list|,
name|provider
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|afterType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|insertBefore
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|beforeType
parameter_list|)
block|{
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
init|=
name|createTypeProvider
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|insertBefore
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|interfaceType
argument_list|)
argument_list|,
name|provider
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|beforeType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|add
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
init|=
name|Key
operator|.
name|get
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|)
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|incrementer
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|add
argument_list|(
name|key
argument_list|,
name|createInstanceProvider
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|addAfter
parameter_list|(
name|T
name|value
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|afterType
parameter_list|)
block|{
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
init|=
name|Key
operator|.
name|get
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|)
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|incrementer
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|addAfter
argument_list|(
name|key
argument_list|,
name|createInstanceProvider
argument_list|(
name|value
argument_list|)
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|afterType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|insertBefore
parameter_list|(
name|T
name|value
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|beforeType
parameter_list|)
block|{
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
init|=
name|Key
operator|.
name|get
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|)
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|incrementer
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|findOrCreateListProvider
argument_list|()
operator|.
name|insertBefore
argument_list|(
name|key
argument_list|,
name|createInstanceProvider
argument_list|(
name|value
argument_list|)
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|beforeType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|values
parameter_list|)
block|{
name|findOrCreateListProvider
argument_list|()
operator|.
name|addAll
argument_list|(
name|createProviderMap
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|addAllAfter
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|values
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|afterType
parameter_list|)
block|{
name|findOrCreateListProvider
argument_list|()
operator|.
name|addAllAfter
argument_list|(
name|createProviderMap
argument_list|(
name|values
argument_list|)
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|afterType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|insertAllBefore
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|values
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|beforeType
parameter_list|)
block|{
name|findOrCreateListProvider
argument_list|()
operator|.
name|insertAllBefore
argument_list|(
name|createProviderMap
argument_list|(
name|values
argument_list|)
argument_list|,
name|Key
operator|.
name|get
argument_list|(
name|beforeType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|,
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|createProviderMap
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|objects
parameter_list|)
block|{
name|Map
argument_list|<
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|,
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|keyProviderMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|object
range|:
name|objects
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|objectType
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|)
name|object
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|keyProviderMap
operator|.
name|put
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|objectType
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|incrementer
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|createInstanceProvider
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keyProviderMap
return|;
block|}
specifier|private
name|ListProvider
argument_list|<
name|T
argument_list|>
name|findOrCreateListProvider
parameter_list|()
block|{
name|ListProvider
argument_list|<
name|T
argument_list|>
name|provider
decl_stmt|;
name|Binding
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|binding
init|=
name|injector
operator|.
name|getBinding
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
name|provider
operator|=
operator|new
name|ListProvider
argument_list|<>
argument_list|()
expr_stmt|;
name|injector
operator|.
name|putBinding
argument_list|(
name|bindingKey
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|provider
operator|=
operator|(
name|ListProvider
argument_list|<
name|T
argument_list|>
operator|)
name|binding
operator|.
name|getOriginal
argument_list|()
expr_stmt|;
block|}
return|return
name|provider
return|;
block|}
block|}
end_class

end_unit

