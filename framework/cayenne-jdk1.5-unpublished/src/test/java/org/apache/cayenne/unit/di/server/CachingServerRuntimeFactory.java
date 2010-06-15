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
name|unit
operator|.
name|di
operator|.
name|server
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|access
operator|.
name|DataDomain
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|dba
operator|.
name|DbAdapter
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
name|Binder
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
name|spi
operator|.
name|DefaultScope
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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|CachingServerRuntimeFactory
implements|implements
name|ServerRuntimeFactory
block|{
specifier|protected
name|CayenneResources
name|resources
decl_stmt|;
specifier|protected
name|DefaultScope
name|testScope
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|ServerRuntime
argument_list|>
name|cache
decl_stmt|;
specifier|public
name|CachingServerRuntimeFactory
parameter_list|(
name|CayenneResources
name|resources
parameter_list|,
name|DefaultScope
name|testScope
parameter_list|)
block|{
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|testScope
operator|=
name|testScope
expr_stmt|;
name|this
operator|.
name|cache
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ServerRuntime
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ServerRuntime
name|get
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
if|if
condition|(
name|configurationLocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null 'configurationLocation'"
argument_list|)
throw|;
block|}
name|ServerRuntime
name|runtime
init|=
name|cache
operator|.
name|get
argument_list|(
name|configurationLocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|runtime
operator|==
literal|null
condition|)
block|{
name|runtime
operator|=
name|create
argument_list|(
name|configurationLocation
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|configurationLocation
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
block|}
return|return
name|runtime
return|;
block|}
specifier|protected
name|ServerRuntime
name|create
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
return|return
operator|new
name|ServerRuntime
argument_list|(
name|configurationLocation
argument_list|,
operator|new
name|ServerExtraModule
argument_list|()
argument_list|)
return|;
block|}
class|class
name|ServerExtraModule
implements|implements
name|Module
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
comment|// these are the objects overriding standard ServerModule definitions or
comment|// dependencies needed by such overrides
name|binder
operator|.
name|bind
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|CayenneResourcesDbAdapterProvider
argument_list|(
name|resources
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataDomainProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|CayenneResourcesDataSourceProvider
argument_list|(
name|resources
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

