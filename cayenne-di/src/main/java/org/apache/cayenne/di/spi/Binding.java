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
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * A binding encapsulates DI provider scoping settings and allows to change them as many  * times as needed.  *   * @since 3.1  */
end_comment

begin_class
class|class
name|Binding
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|Provider
argument_list|<
name|T
argument_list|>
name|original
decl_stmt|;
specifier|private
name|Provider
argument_list|<
name|T
argument_list|>
name|decorated
decl_stmt|;
specifier|private
name|Provider
argument_list|<
name|T
argument_list|>
name|scoped
decl_stmt|;
specifier|private
name|Scope
name|scope
decl_stmt|;
name|Binding
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|,
name|Scope
name|initialScope
parameter_list|)
block|{
name|this
operator|.
name|original
operator|=
name|provider
expr_stmt|;
name|this
operator|.
name|decorated
operator|=
name|provider
expr_stmt|;
name|changeScope
argument_list|(
name|initialScope
argument_list|)
expr_stmt|;
block|}
name|void
name|changeScope
parameter_list|(
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
name|NoScope
operator|.
name|INSTANCE
expr_stmt|;
block|}
comment|// TODO: what happens to the old scoped value? Seems like this leaks
comment|// scope event listeners and may cause unexpected events...
name|this
operator|.
name|scoped
operator|=
name|scope
operator|.
name|scope
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
name|void
name|decorate
parameter_list|(
name|Decoration
argument_list|<
name|T
argument_list|>
name|decoration
parameter_list|)
block|{
name|List
argument_list|<
name|DecoratorProvider
argument_list|<
name|T
argument_list|>
argument_list|>
name|decorators
init|=
name|decoration
operator|.
name|decorators
argument_list|()
decl_stmt|;
if|if
condition|(
name|decorators
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
init|=
name|this
operator|.
name|original
decl_stmt|;
for|for
control|(
name|DecoratorProvider
argument_list|<
name|T
argument_list|>
name|decoratorProvider
range|:
name|decorators
control|)
block|{
name|provider
operator|=
name|decoratorProvider
operator|.
name|get
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|decorated
operator|=
name|provider
expr_stmt|;
comment|// TODO: what happens to the old scoped value? Seems like this leaks
comment|// scope event listeners and may cause unexpected events...
name|this
operator|.
name|scoped
operator|=
name|scope
operator|.
name|scope
argument_list|(
name|decorated
argument_list|)
expr_stmt|;
block|}
name|Provider
argument_list|<
name|T
argument_list|>
name|getOriginal
parameter_list|()
block|{
return|return
name|original
return|;
block|}
name|Provider
argument_list|<
name|T
argument_list|>
name|getScoped
parameter_list|()
block|{
return|return
name|scoped
return|;
block|}
block|}
end_class

end_unit

