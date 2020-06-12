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
name|configuration
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockDataSource
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
name|dba
operator|.
name|AutoAdapter
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
name|JdbcAdapter
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
name|sybase
operator|.
name|SybaseAdapter
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
name|DbEntity
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
name|reflect
operator|.
name|generic
operator|.
name|ComparisionStrategyFactory
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
name|reflect
operator|.
name|generic
operator|.
name|DefaultComparisionStrategyFactory
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
name|ResourceLocator
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
name|ArrayList
import|;
end_import

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
name|ArgumentMatchers
operator|.
name|any
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
name|DefaultDbAdapterFactoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreatedAdapter_Auto
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DbAdapter
name|adapter
init|=
name|mock
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|adapter
operator|.
name|createTable
argument_list|(
name|any
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"XXXXX"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|adapter
operator|.
name|unwrap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|detectors
operator|.
name|add
argument_list|(
name|md
lambda|->
name|adapter
argument_list|)
expr_stmt|;
name|MockConnection
name|connection
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|MockDataSource
name|dataSource
init|=
operator|new
name|MockDataSource
argument_list|()
decl_stmt|;
name|dataSource
operator|.
name|setupConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributePkGenerators
argument_list|(
name|binder
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
name|toInstance
argument_list|(
name|mock
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
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
name|DefaultDbAdapterFactory
name|factory
init|=
operator|new
name|DefaultDbAdapterFactory
argument_list|(
name|detectors
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DbAdapter
name|createdAdapter
init|=
name|factory
operator|.
name|createAdapter
argument_list|(
operator|new
name|DataNodeDescriptor
argument_list|()
argument_list|,
name|dataSource
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|createdAdapter
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXXXX"
argument_list|,
name|createdAdapter
operator|.
name|createTable
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"Test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatedAdapter_Generic
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAdapterDetector
argument_list|>
argument_list|()
decl_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
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
name|ServerModule
operator|.
name|contributePkGenerators
argument_list|(
name|binder
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
name|ResourceLocator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ClassLoaderResourceLocator
operator|.
name|class
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
name|to
argument_list|(
name|ClassLoaderResourceLocator
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
name|toInstance
argument_list|(
name|mock
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
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
name|DefaultDbAdapterFactory
name|factory
init|=
operator|new
name|DefaultDbAdapterFactory
argument_list|(
name|detectors
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DbAdapter
name|createdAdapter
init|=
name|factory
operator|.
name|createAdapter
argument_list|(
operator|new
name|DataNodeDescriptor
argument_list|()
argument_list|,
operator|new
name|MockDataSource
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|createdAdapter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected class: "
operator|+
name|createdAdapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|createdAdapter
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CREATE TABLE Test ()"
argument_list|,
name|createdAdapter
operator|.
name|createTable
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"Test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatedAdapter_Custom
parameter_list|()
throws|throws
name|Exception
block|{
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
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
name|ServerModule
operator|.
name|contributePkGenerators
argument_list|(
name|binder
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
name|ResourceLocator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ClassLoaderResourceLocator
operator|.
name|class
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
name|to
argument_list|(
name|ClassLoaderResourceLocator
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
name|toInstance
argument_list|(
name|mock
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
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
name|binder
operator|.
name|bind
argument_list|(
name|ComparisionStrategyFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultComparisionStrategyFactory
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
name|DefaultDbAdapterFactory
name|factory
init|=
operator|new
name|DefaultDbAdapterFactory
argument_list|(
name|detectors
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DbAdapter
name|createdAdapter
init|=
name|factory
operator|.
name|createAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
operator|new
name|MockDataSource
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|createdAdapter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected class: "
operator|+
name|createdAdapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|createdAdapter
operator|instanceof
name|SybaseAdapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatedAdapter_AutoExplicit
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DbAdapter
name|adapter
init|=
name|mock
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|adapter
operator|.
name|createTable
argument_list|(
name|any
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"XXXXX"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|adapter
operator|.
name|unwrap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|detectors
operator|.
name|add
argument_list|(
name|md
lambda|->
name|adapter
argument_list|)
expr_stmt|;
name|MockConnection
name|connection
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|MockDataSource
name|dataSource
init|=
operator|new
name|MockDataSource
argument_list|()
decl_stmt|;
name|dataSource
operator|.
name|setupConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributePkGenerators
argument_list|(
name|binder
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
name|toInstance
argument_list|(
name|mock
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
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
name|DefaultDbAdapterFactory
name|factory
init|=
operator|new
name|DefaultDbAdapterFactory
argument_list|(
name|detectors
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|AutoAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DbAdapter
name|createdAdapter
init|=
name|factory
operator|.
name|createAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
name|dataSource
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|createdAdapter
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXXXX"
argument_list|,
name|createdAdapter
operator|.
name|createTable
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"Test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

