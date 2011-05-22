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
name|Inject
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

begin_class
specifier|public
class|class
name|ServerRuntimeProvider
implements|implements
name|Provider
argument_list|<
name|ServerRuntime
argument_list|>
block|{
specifier|private
name|ServerCaseProperties
name|properties
decl_stmt|;
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|dbAdapterProvider
decl_stmt|;
specifier|public
name|ServerRuntimeProvider
parameter_list|(
annotation|@
name|Inject
name|ServerCaseDataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|ServerCaseProperties
name|properties
parameter_list|,
annotation|@
name|Inject
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|dbAdapterProvider
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactory
operator|=
name|dataSourceFactory
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|dbAdapterProvider
operator|=
name|dbAdapterProvider
expr_stmt|;
block|}
specifier|public
name|ServerRuntime
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|String
name|configurationLocation
init|=
name|properties
operator|.
name|getConfigurationLocation
argument_list|()
decl_stmt|;
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
literal|"Null 'configurationLocation', "
operator|+
literal|"annotate your test case with @UseServerRuntime"
argument_list|)
throw|;
block|}
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
name|dbAdapterProvider
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
comment|// map DataSources for all test DataNode names
name|binder
operator|.
name|bind
argument_list|(
name|ServerCaseDataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|dataSourceFactory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

