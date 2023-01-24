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
name|util
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
name|access
operator|.
name|flush
operator|.
name|operation
operator|.
name|DbRowOpSorter
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
name|flush
operator|.
name|operation
operator|.
name|GraphBasedDbRowOpSorter
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
name|UnitDbAdapter
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
name|server
operator|.
name|ServerCaseDataSourceFactory
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
name|server
operator|.
name|ServerCaseProperties
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
name|server
operator|.
name|ServerRuntimeProvider
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

begin_class
specifier|public
class|class
name|CustomSorterServerRuntimeProvider
extends|extends
name|ServerRuntimeProvider
block|{
specifier|public
name|CustomSorterServerRuntimeProvider
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
parameter_list|,
annotation|@
name|Inject
name|UnitDbAdapter
name|unitDbAdapter
parameter_list|)
block|{
name|super
argument_list|(
name|dataSourceFactory
argument_list|,
name|properties
argument_list|,
name|dbAdapterProvider
argument_list|,
name|unitDbAdapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|getExtraModules
parameter_list|()
block|{
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|super
operator|.
name|getExtraModules
argument_list|()
argument_list|)
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|DbRowOpSorter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GraphBasedDbRowOpSorter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|modules
return|;
block|}
block|}
end_class

end_unit

