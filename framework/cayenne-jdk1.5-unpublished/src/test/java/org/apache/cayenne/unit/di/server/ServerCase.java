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
name|ObjectContext
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
name|DataNode
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
name|DIBootstrap
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|AccessStackAdapter
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
name|DICase
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
name|UnitTestLifecycleManager
import|;
end_import

begin_class
specifier|public
class|class
name|ServerCase
extends|extends
name|DICase
block|{
comment|// known runtimes... unit tests may reuse these with @UseServerRuntime annotation or
comment|// can define their own on the fly (TODO: how would that work with the global schema
comment|// setup?)
specifier|public
specifier|static
specifier|final
name|String
name|INHERTITANCE_SINGLE_TABLE1_PROJECT
init|=
literal|"cayenne-inheritance-single-table1.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERTITANCE_VERTICAL_PROJECT
init|=
literal|"cayenne-inheritance-vertical.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUOTED_IDENTIFIERS_PROJECT
init|=
literal|"cayenne-quoted-identifiers.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TESTMAP_PROJECT
init|=
literal|"cayenne-testmap.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Injector
name|injector
decl_stmt|;
static|static
block|{
comment|// TODO: andrus 6/14/2010 - this should probably also be DI driven
specifier|final
name|CayenneResources
name|resources
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|DefaultScope
name|testScope
init|=
operator|new
name|DefaultScope
argument_list|()
decl_stmt|;
comment|// these are the objects injectable in unit tests that subclass from
comment|// ServerCase. Server runtime extensions are configured in
comment|// CachingServerRuntimeFactory. There is some overlap between the two
comment|// registries (some services declared in both), as cayenne-di does not
comment|// support registry inheritance.
comment|// singleton objects
name|binder
operator|.
name|bind
argument_list|(
name|UnitTestLifecycleManager
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|ServerCaseLifecycleManager
argument_list|(
name|testScope
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ServerRuntimeFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|CachingServerRuntimeFactory
argument_list|(
name|resources
argument_list|,
name|testScope
argument_list|)
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
name|AccessStackAdapter
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|CayenneResourcesAccessStackAdapterProvider
argument_list|(
name|resources
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataNode
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataNodeProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelQueryBlocker
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerCaseDataChannelQueryBlocker
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// test-scoped objects
name|binder
operator|.
name|bind
argument_list|(
name|ServerCaseProperties
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerCaseProperties
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataContextProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DBHelper
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FlavoredDBHelperProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Injector
name|getUnitTestInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
block|}
end_class

end_unit

