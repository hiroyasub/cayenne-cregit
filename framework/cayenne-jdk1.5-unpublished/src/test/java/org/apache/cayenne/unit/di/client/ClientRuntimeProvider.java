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
name|client
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
name|configuration
operator|.
name|rop
operator|.
name|client
operator|.
name|ClientLocalRuntime
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
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|Provider
import|;
end_import

begin_class
specifier|public
class|class
name|ClientRuntimeProvider
implements|implements
name|Provider
argument_list|<
name|ClientRuntime
argument_list|>
block|{
annotation|@
name|Inject
comment|// injecting provider to make this provider independent from scoping of ServerRuntime
specifier|protected
name|Provider
argument_list|<
name|ServerRuntime
argument_list|>
name|serverRuntimeProvider
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ClientCaseProperties
name|clientCaseProperties
decl_stmt|;
specifier|public
name|ClientRuntime
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|Injector
name|serverInjector
init|=
name|serverRuntimeProvider
operator|.
name|get
argument_list|()
operator|.
name|getInjector
argument_list|()
decl_stmt|;
return|return
operator|new
name|ClientLocalRuntime
argument_list|(
name|serverInjector
argument_list|,
name|clientCaseProperties
operator|.
name|getRuntimeProperties
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

