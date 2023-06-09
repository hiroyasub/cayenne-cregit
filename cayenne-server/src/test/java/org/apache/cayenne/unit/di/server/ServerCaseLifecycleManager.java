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
name|di
operator|.
name|DefaultUnitTestLifecycleManager
import|;
end_import

begin_class
specifier|public
class|class
name|ServerCaseLifecycleManager
extends|extends
name|DefaultUnitTestLifecycleManager
block|{
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|ServerCaseProperties
argument_list|>
name|propertiesProvider
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|ServerCaseExtraModules
argument_list|>
name|extraModulesProvider
decl_stmt|;
specifier|public
name|ServerCaseLifecycleManager
parameter_list|(
name|DefaultScope
name|scope
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setUp
parameter_list|(
name|T
name|testCase
parameter_list|)
block|{
comment|// init current runtime
name|UseServerRuntime
name|runtimeName
init|=
name|testCase
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|UseServerRuntime
operator|.
name|class
argument_list|)
decl_stmt|;
name|ExtraModules
name|extraModules
init|=
name|testCase
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ExtraModules
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|location
init|=
name|runtimeName
operator|!=
literal|null
condition|?
name|runtimeName
operator|.
name|value
argument_list|()
else|:
literal|null
decl_stmt|;
name|propertiesProvider
operator|.
name|get
argument_list|()
operator|.
name|setConfigurationLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|modules
init|=
name|extraModules
operator|!=
literal|null
condition|?
name|extraModules
operator|.
name|value
argument_list|()
else|:
operator|new
name|Class
index|[]
block|{}
decl_stmt|;
name|extraModulesProvider
operator|.
name|get
argument_list|()
operator|.
name|setExtraModules
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|(
name|testCase
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

