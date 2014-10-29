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
name|configuration
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
name|di
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
name|ClassLoaderManager
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
name|di
operator|.
name|spi
operator|.
name|DefaultClassLoaderManager
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
name|URLResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Iterator
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|XMLDataChannelDescriptorLoaderTest
block|{
specifier|private
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
name|ClassLoaderManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultClassLoaderManager
operator|.
name|class
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
name|binder
operator|.
name|bind
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|XMLDataMapLoader
operator|.
name|class
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
block|}
block|}
decl_stmt|;
name|this
operator|.
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|testModule
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadEmpty
parameter_list|()
block|{
comment|// create and initialize loader instance to test
name|XMLDataChannelDescriptorLoader
name|loader
init|=
operator|new
name|XMLDataChannelDescriptorLoader
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|String
name|testConfigName
init|=
literal|"testConfig1"
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cayenne-"
operator|+
name|testConfigName
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|tree
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tree
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testConfigName
argument_list|,
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoad_MissingConfig
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create and initialize loader instance to test
name|XMLDataChannelDescriptorLoader
name|loader
init|=
operator|new
name|XMLDataChannelDescriptorLoader
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|loader
argument_list|)
expr_stmt|;
try|try
block|{
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file:///no_such_resource"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No exception was thrown on bad absent config name"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadDataMap
parameter_list|()
block|{
comment|// create and initialize loader instance to test
name|XMLDataChannelDescriptorLoader
name|loader
init|=
operator|new
name|XMLDataChannelDescriptorLoader
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|String
name|testConfigName
init|=
literal|"testConfig2"
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cayenne-"
operator|+
name|testConfigName
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|tree
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tree
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testConfigName
argument_list|,
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getDataMaps
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|maps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap2"
argument_list|,
name|maps
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadDataEverything
parameter_list|()
block|{
comment|// create and initialize loader instance to test
name|XMLDataChannelDescriptorLoader
name|loader
init|=
operator|new
name|XMLDataChannelDescriptorLoader
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|String
name|testConfigName
init|=
literal|"testConfig3"
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cayenne-"
operator|+
name|testConfigName
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|tree
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|DataChannelDescriptor
name|descriptor
init|=
name|tree
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testConfigName
argument_list|,
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
name|descriptor
operator|.
name|getDataMaps
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|maps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DataMap
argument_list|>
name|mapsIt
init|=
name|maps
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|DataMap
name|map1
init|=
name|mapsIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|DataMap
name|map2
init|=
name|mapsIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap3_1"
argument_list|,
name|map1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap3_2"
argument_list|,
name|map2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodes
init|=
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
name|node1
init|=
name|nodes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigNode3"
argument_list|,
name|node1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|node1
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node1
operator|.
name|getDataSourceDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node1
operator|.
name|getDataSourceDescriptor
argument_list|()
operator|.
name|getMinConnections
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node1
operator|.
name|getDataSourceDescriptor
argument_list|()
operator|.
name|getMaxConnections
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.example.test.Adapter"
argument_list|,
name|node1
operator|.
name|getAdapterType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.example.test.DataSourceFactory"
argument_list|,
name|node1
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.example.test.SchemaUpdateStartegy"
argument_list|,
name|node1
operator|.
name|getSchemaUpdateStrategyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node1
operator|.
name|getDataMapNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node1
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap3_2"
argument_list|,
name|node1
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

