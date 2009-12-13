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
name|runtime
package|;
end_package

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
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|DataChannel
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
name|access
operator|.
name|dbsync
operator|.
name|SchemaUpdateStrategy
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
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|dbsync
operator|.
name|ThrowOnPartialOrCreateSchemaStrategy
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
name|AdhocObjectFactory
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
name|DataChannelDescriptor
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
name|DataChannelDescriptorLoader
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
name|DataNodeDescriptor
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
name|DataSourceFactoryLoader
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
name|DbAdapterFactory
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
name|DefaultAdhocObjectFactory
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
name|DefaultConfigurationNameMapper
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
name|DefaultRuntimeProperties
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
name|ConfigurationNameMapper
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
name|RuntimeProperties
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
name|mock
operator|.
name|MockDataSourceFactory
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
name|mock
operator|.
name|MockDataSourceFactoryLoader
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
name|dba
operator|.
name|MockDbAdapter
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
name|oracle
operator|.
name|OracleAdapter
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
name|map
operator|.
name|DataMap
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
name|resource
operator|.
name|Resource
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
name|resource
operator|.
name|ResourceLocator
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
name|resource
operator|.
name|mock
operator|.
name|MockResource
import|;
end_import

begin_class
specifier|public
class|class
name|DataDomainProviderTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGet
parameter_list|()
block|{
comment|// create dependencies
specifier|final
name|String
name|testConfigName
init|=
literal|"testConfig"
decl_stmt|;
specifier|final
name|DataChannelDescriptor
name|testDescriptor
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|DataMap
name|map1
init|=
operator|new
name|DataMap
argument_list|(
literal|"map1"
argument_list|)
decl_stmt|;
name|testDescriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
name|map1
argument_list|)
expr_stmt|;
name|DataMap
name|map2
init|=
operator|new
name|DataMap
argument_list|(
literal|"map2"
argument_list|)
decl_stmt|;
name|testDescriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
name|map2
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor1
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor1
operator|.
name|setName
argument_list|(
literal|"node1"
argument_list|)
expr_stmt|;
name|nodeDescriptor1
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"map1"
argument_list|)
expr_stmt|;
name|nodeDescriptor1
operator|.
name|setAdapterType
argument_list|(
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nodeDescriptor1
operator|.
name|setDataSourceFactoryType
argument_list|(
name|MockDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nodeDescriptor1
operator|.
name|setParameters
argument_list|(
literal|"jdbc/testDataNode1"
argument_list|)
expr_stmt|;
name|nodeDescriptor1
operator|.
name|setSchemaUpdateStrategyType
argument_list|(
name|ThrowOnPartialOrCreateSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|testDescriptor
operator|.
name|getDataNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|nodeDescriptor1
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor2
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor2
operator|.
name|setName
argument_list|(
literal|"node2"
argument_list|)
expr_stmt|;
name|nodeDescriptor2
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"map2"
argument_list|)
expr_stmt|;
name|nodeDescriptor2
operator|.
name|setParameters
argument_list|(
literal|"testDataNode2.driver.xml"
argument_list|)
expr_stmt|;
name|testDescriptor
operator|.
name|getDataNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|nodeDescriptor2
argument_list|)
expr_stmt|;
specifier|final
name|ResourceLocator
name|locator
init|=
operator|new
name|ResourceLocator
argument_list|()
block|{
specifier|public
name|Collection
argument_list|<
name|Resource
argument_list|>
name|findResources
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"cayenne-"
operator|+
name|testConfigName
operator|+
literal|".xml"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
expr|<
name|Resource
operator|>
name|singleton
argument_list|(
operator|new
name|MockResource
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|DataChannelDescriptorLoader
name|testLoader
init|=
operator|new
name|DataChannelDescriptorLoader
argument_list|()
block|{
specifier|public
name|DataChannelDescriptor
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|ConfigurationException
block|{
return|return
name|testDescriptor
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|DefaultRuntimeProperties
name|testProperties
init|=
operator|new
name|DefaultRuntimeProperties
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|RuntimeProperties
operator|.
name|CAYENNE_RUNTIME_NAME
argument_list|,
name|testConfigName
argument_list|)
argument_list|)
decl_stmt|;
name|Module
name|testModule
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
name|binder
operator|.
name|bind
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|locator
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|RuntimeProperties
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|testProperties
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNameMapper
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelDescriptorLoader
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|testLoader
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SchemaUpdateStrategy
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|SkipSchemaUpdateStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|DbAdapterFactory
argument_list|()
block|{
specifier|public
name|DbAdapter
name|createAdapter
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|nodeDescriptor
operator|.
name|getAdapterType
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DbAdapter
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|nodeDescriptor
operator|.
name|getAdapterType
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
return|return
operator|new
name|MockDbAdapter
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataSourceFactoryLoader
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|MockDataSourceFactoryLoader
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultAdhocObjectFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|testModule
argument_list|)
decl_stmt|;
comment|// create and initialize provide instance to test
name|DataDomainProvider
name|provider
init|=
operator|new
name|DataDomainProvider
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|DataChannel
name|channel
init|=
name|provider
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|channel
operator|instanceof
name|DataDomain
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"DataDomainProvider must return the same instance of DataChannel on subsequent calls"
argument_list|,
name|channel
argument_list|,
name|provider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|channel
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|domain
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|getDataMaps
argument_list|()
operator|.
name|contains
argument_list|(
name|map1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|getDataMaps
argument_list|()
operator|.
name|contains
argument_list|(
name|map2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataNode
name|node1
init|=
name|domain
operator|.
name|getNode
argument_list|(
literal|"node1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node1
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map1
argument_list|,
name|node1
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|node1
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|map1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nodeDescriptor1
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|,
name|node1
operator|.
name|getDataSourceFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node1
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nodeDescriptor1
operator|.
name|getParameters
argument_list|()
argument_list|,
name|node1
operator|.
name|getDataSourceLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nodeDescriptor1
operator|.
name|getSchemaUpdateStrategyType
argument_list|()
argument_list|,
name|node1
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node1
operator|.
name|getSchemaUpdateStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nodeDescriptor1
operator|.
name|getSchemaUpdateStrategyType
argument_list|()
argument_list|,
name|node1
operator|.
name|getSchemaUpdateStrategy
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node1
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|OracleAdapter
operator|.
name|class
argument_list|,
name|node1
operator|.
name|getAdapter
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|DataNode
name|node2
init|=
name|domain
operator|.
name|getNode
argument_list|(
literal|"node2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node2
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map2
argument_list|,
name|node2
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|node2
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|map2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|node2
operator|.
name|getDataSourceFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node2
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nodeDescriptor2
operator|.
name|getParameters
argument_list|()
argument_list|,
name|node2
operator|.
name|getDataSourceLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SkipSchemaUpdateStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|node2
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node2
operator|.
name|getSchemaUpdateStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SkipSchemaUpdateStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|node2
operator|.
name|getSchemaUpdateStrategy
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node2
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MockDbAdapter
operator|.
name|class
argument_list|,
name|node2
operator|.
name|getAdapter
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

