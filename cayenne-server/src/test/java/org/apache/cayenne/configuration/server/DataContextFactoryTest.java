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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataContext
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
name|DefaultObjectMapRetainStrategy
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
name|ObjectMapRetainStrategy
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
name|DataDomainFlushActionFactory
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
name|DefaultDataDomainFlushActionFactory
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
name|DefaultDbRowOpSorter
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
name|MapQueryCache
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
name|DefaultObjectStoreFactory
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
name|ObjectStoreFactory
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
name|tx
operator|.
name|DefaultTransactionFactory
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
name|tx
operator|.
name|DefaultTransactionManager
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
name|tx
operator|.
name|TransactionFactory
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
name|tx
operator|.
name|TransactionManager
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
name|Collections
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

begin_class
specifier|public
class|class
name|DataContextFactoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateDataContextWithDedicatedCache
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|EventManager
name|eventManager
init|=
operator|new
name|MockEventManager
argument_list|()
decl_stmt|;
specifier|final
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|)
decl_stmt|;
name|domain
operator|.
name|setSharedCacheEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
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
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|domain
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
name|QueryCache
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|MapQueryCache
argument_list|(
literal|5
argument_list|)
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
operator|new
name|DefaultRuntimeProperties
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectMapRetainStrategy
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectMapRetainStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectStoreFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectStoreFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultTransactionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|TransactionManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultTransactionManager
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
name|binder
operator|.
name|bind
argument_list|(
name|DataDomainFlushActionFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataDomainFlushActionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DefaultDbRowOpSorter
operator|.
name|class
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
name|to
argument_list|(
name|AshwoodEntitySorter
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
name|DataContextFactory
name|factory
init|=
operator|new
name|DataContextFactory
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DataContext
name|c3
init|=
operator|(
name|DataContext
operator|)
name|factory
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|c3
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|c3
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
argument_list|,
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateDataContextValidation
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|EventManager
name|eventManager
init|=
operator|new
name|MockEventManager
argument_list|()
decl_stmt|;
specifier|final
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|)
decl_stmt|;
name|domain
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Module
name|testModule
init|=
name|binder
lambda|->
block|{
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
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|domain
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
name|QueryCache
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|MapQueryCache
argument_list|(
literal|5
argument_list|)
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
operator|new
name|DefaultRuntimeProperties
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectMapRetainStrategy
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectMapRetainStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectStoreFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectStoreFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultTransactionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|TransactionManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultTransactionManager
operator|.
name|class
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
name|binder
operator|.
name|bind
argument_list|(
name|DataDomainFlushActionFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataDomainFlushActionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DefaultDbRowOpSorter
operator|.
name|class
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
name|to
argument_list|(
name|AshwoodEntitySorter
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
name|domain
operator|.
name|setDataRowStoreFactory
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|DataRowStoreFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DataContextFactory
name|factory
init|=
operator|new
name|DataContextFactory
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|DataContext
name|c1
init|=
operator|(
name|DataContext
operator|)
name|factory
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|c1
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DataContext
name|c2
init|=
operator|(
name|DataContext
operator|)
name|factory
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|c2
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

