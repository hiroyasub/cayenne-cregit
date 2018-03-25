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
name|ObjectId
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
name|Persistent
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
name|DataRowStoreFactory
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
name|DefaultDataRowStoreFactory
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
name|DefaultSchemaUpdateStrategyFactory
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
name|SchemaUpdateStrategyFactory
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
name|access
operator|.
name|jdbc
operator|.
name|SQLTemplateProcessor
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
name|jdbc
operator|.
name|reader
operator|.
name|RowReaderFactory
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
name|translator
operator|.
name|batch
operator|.
name|BatchTranslatorFactory
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
name|translator
operator|.
name|batch
operator|.
name|DefaultBatchTranslatorFactory
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
name|translator
operator|.
name|select
operator|.
name|DefaultSelectTranslatorFactory
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
name|translator
operator|.
name|select
operator|.
name|SelectTranslatorFactory
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
name|types
operator|.
name|DefaultValueObjectTypeRegistry
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
name|types
operator|.
name|ValueObjectTypeRegistry
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
name|annotation
operator|.
name|PostLoad
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
name|ashwood
operator|.
name|AshwoodEntitySorter
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
name|cache
operator|.
name|QueryCache
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
name|ConfigurationTree
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
name|Constants
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
name|DataChannelDescriptorMerger
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
name|DefaultDataChannelDescriptorMerger
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
name|dba
operator|.
name|JdbcPkGenerator
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
name|PkGenerator
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
name|db2
operator|.
name|DB2Sniffer
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
name|derby
operator|.
name|DerbySniffer
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
name|firebird
operator|.
name|FirebirdSniffer
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
name|frontbase
operator|.
name|FrontBaseSniffer
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
name|h2
operator|.
name|H2Sniffer
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
name|hsqldb
operator|.
name|HSQLDBSniffer
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
name|ingres
operator|.
name|IngresSniffer
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
name|mysql
operator|.
name|MySQLSniffer
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
name|openbase
operator|.
name|OpenBaseSniffer
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
name|dba
operator|.
name|oracle
operator|.
name|OracleSniffer
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
name|postgres
operator|.
name|PostgresSniffer
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
name|sqlite
operator|.
name|SQLiteSniffer
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
name|sqlserver
operator|.
name|SQLServerAdapter
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
name|sqlserver
operator|.
name|SQLServerSniffer
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
name|sybase
operator|.
name|SybasePkGenerator
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
name|sybase
operator|.
name|SybaseSniffer
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
name|event
operator|.
name|EventBridge
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
name|event
operator|.
name|EventManager
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
name|event
operator|.
name|MockEventManager
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
name|event
operator|.
name|NoopEventBridgeProvider
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
name|log
operator|.
name|JdbcEventLogger
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
name|log
operator|.
name|Slf4jJdbcEventLogger
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
name|map
operator|.
name|EntitySorter
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
name|LifecycleEvent
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
name|ClassLoaderResourceLocator
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
specifier|public
class|class
name|DataDomainProviderTest
block|{
annotation|@
name|Test
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
name|getNodeDescriptors
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
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|nodeDescriptor2
argument_list|)
expr_stmt|;
specifier|final
name|DataChannelDescriptorLoader
name|testLoader
init|=
operator|new
name|DataChannelDescriptorLoader
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|ConfigurationException
block|{
return|return
operator|new
name|ConfigurationTree
argument_list|<>
argument_list|(
name|testDescriptor
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|EventManager
name|eventManager
init|=
operator|new
name|MockEventManager
argument_list|()
decl_stmt|;
specifier|final
name|TestListener
name|mockListener
init|=
operator|new
name|TestListener
argument_list|()
decl_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
specifier|final
name|ClassLoaderManager
name|classLoaderManager
init|=
operator|new
name|DefaultClassLoaderManager
argument_list|()
decl_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ClassLoaderManager
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|classLoaderManager
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
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeAdapterDetectors
argument_list|(
name|binder
argument_list|)
operator|.
name|add
argument_list|(
name|FirebirdSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|OpenBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|FrontBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|IngresSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SQLiteSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|DB2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|H2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|HSQLDBSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SybaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|DerbySniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SQLServerSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|OracleSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|PostgresSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|MySQLSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeDomainFilters
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeDomainListeners
argument_list|(
name|binder
argument_list|)
operator|.
name|add
argument_list|(
name|mockListener
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeProjectLocations
argument_list|(
name|binder
argument_list|)
operator|.
name|add
argument_list|(
name|testConfigName
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|PkGenerator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|JdbcPkGenerator
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|PkGeneratorFactoryProvider
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|PkGeneratorFactoryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributePkGenerators
argument_list|(
name|binder
argument_list|)
operator|.
name|put
argument_list|(
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|SybasePkGenerator
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// configure extended types
name|ServerModule
operator|.
name|contributeDefaultTypes
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeUserTypes
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeTypeFactories
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|EventManager
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|EntitySorter
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|AshwoodEntitySorter
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SchemaUpdateStrategyFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultSchemaUpdateStrategyFactory
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|ResourceLocator
name|locator
init|=
operator|new
name|ClassLoaderResourceLocator
argument_list|(
name|classLoaderManager
argument_list|)
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
comment|// ResourceLocator also used by JdbcAdapter to locate types.xml...
comment|// if this is the request we are getting, just let it go through..
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"types.xml"
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|findResources
argument_list|(
name|name
argument_list|)
return|;
block|}
name|assertEquals
argument_list|(
name|testConfigName
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
name|Key
operator|.
name|get
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|SERVER_RESOURCE_LOCATOR
argument_list|)
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
name|DataChannelDescriptorMerger
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataChannelDescriptorMerger
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
name|DbAdapterFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDbAdapterFactory
operator|.
name|class
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
name|to
argument_list|(
name|DefaultRuntimeProperties
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultBatchTranslatorFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SelectTranslatorFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultSelectTranslatorFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|MockDataSourceFactory
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|JdbcEventLogger
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|Slf4jJdbcEventLogger
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|QueryCache
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|mock
argument_list|(
name|QueryCache
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataNodeFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataNodeFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SQLTemplateProcessor
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|mock
argument_list|(
name|SQLTemplateProcessor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|EventBridge
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|NoopEventBridgeProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataRowStoreFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataRowStoreFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeValueObjectTypes
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ValueObjectTypeRegistry
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultValueObjectTypeRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|channel
decl_stmt|;
name|assertSame
argument_list|(
name|eventManager
argument_list|,
name|domain
operator|.
name|getEventManager
argument_list|()
argument_list|)
expr_stmt|;
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
name|getDataNode
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
name|getDataNode
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
comment|// check that we have mock listener passed correctly
name|Persistent
name|mockPersistent
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectId
name|mockObjectId
init|=
name|mock
argument_list|(
name|ObjectId
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockObjectId
operator|.
name|getEntityName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"mock-entity-name"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockPersistent
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockObjectId
argument_list|)
expr_stmt|;
name|domain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|performCallbacks
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|mockPersistent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should call postLoadCallback() method"
argument_list|,
literal|1
argument_list|,
name|TestListener
operator|.
name|counter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|TestListener
block|{
specifier|static
specifier|private
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|PostLoad
specifier|public
name|void
name|postLoadCallback
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

