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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|ListProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
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
name|providers
decl_stmt|;
specifier|private
name|DIGraph
argument_list|<
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|graph
decl_stmt|;
specifier|public
name|ListProvider
parameter_list|()
block|{
name|this
operator|.
name|providers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|graph
operator|=
operator|new
name|DIGraph
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|get
parameter_list|()
throws|throws
name|DIRuntimeException
block|{
name|List
argument_list|<
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|insertOrder
init|=
name|graph
operator|.
name|topSort
argument_list|()
decl_stmt|;
if|if
condition|(
name|insertOrder
operator|.
name|size
argument_list|()
operator|!=
name|providers
operator|.
name|size
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|emptyKeys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
range|:
name|insertOrder
control|)
block|{
if|if
condition|(
operator|!
name|providers
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|emptyKeys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"DI list has no providers for keys: %s"
argument_list|,
name|emptyKeys
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|insertOrder
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
range|:
name|insertOrder
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|providers
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
name|void
name|add
parameter_list|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|)
block|{
name|providers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|void
name|addAfter
parameter_list|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|,
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|after
parameter_list|)
block|{
name|providers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
name|key
argument_list|,
name|after
argument_list|)
expr_stmt|;
block|}
name|void
name|insertBefore
parameter_list|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|,
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|before
parameter_list|)
block|{
name|providers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
name|before
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|void
name|addAll
parameter_list|(
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
parameter_list|)
block|{
name|providers
operator|.
name|putAll
argument_list|(
name|keyProviderMap
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addAll
argument_list|(
name|keyProviderMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|addAllAfter
parameter_list|(
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
parameter_list|,
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|after
parameter_list|)
block|{
name|providers
operator|.
name|putAll
argument_list|(
name|keyProviderMap
argument_list|)
expr_stmt|;
for|for
control|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
range|:
name|keyProviderMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|key
argument_list|,
name|after
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|insertAllBefore
parameter_list|(
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
parameter_list|,
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|before
parameter_list|)
block|{
name|providers
operator|.
name|putAll
argument_list|(
name|keyProviderMap
argument_list|)
expr_stmt|;
for|for
control|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
range|:
name|keyProviderMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|graph
operator|.
name|add
argument_list|(
name|before
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

