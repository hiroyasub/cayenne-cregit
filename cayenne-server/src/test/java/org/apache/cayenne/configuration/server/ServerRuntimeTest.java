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
name|QueryResponse
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
name|ObjectContextFactory
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
name|graph
operator|.
name|GraphDiff
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
name|EntityResolver
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
name|query
operator|.
name|Query
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
name|BaseTransaction
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
name|TransactionDescriptor
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
name|TransactionalOperation
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
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|ServerRuntimeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPerformInTransaction
parameter_list|()
block|{
specifier|final
name|BaseTransaction
name|tx
init|=
name|mock
argument_list|(
name|BaseTransaction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|TransactionFactory
name|txFactory
init|=
name|mock
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|txFactory
operator|.
name|createTransaction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tx
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|txFactory
operator|.
name|createTransaction
argument_list|(
name|any
argument_list|(
name|TransactionDescriptor
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tx
argument_list|)
expr_stmt|;
name|Module
name|module
init|=
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|txFactory
argument_list|)
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
name|ServerRuntime
operator|.
name|builder
argument_list|()
operator|.
name|addConfig
argument_list|(
literal|"xxxx"
argument_list|)
operator|.
name|addModule
argument_list|(
name|module
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|Object
name|expectedResult
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|runtime
operator|.
name|performInTransaction
argument_list|(
operator|new
name|TransactionalOperation
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|perform
parameter_list|()
block|{
name|assertSame
argument_list|(
name|tx
argument_list|,
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|expectedResult
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|expectedResult
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor_Modules
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|configured
init|=
operator|new
name|boolean
index|[
literal|2
index|]
decl_stmt|;
name|Module
name|m1
init|=
name|binder
lambda|->
name|configured
index|[
literal|0
index|]
operator|=
literal|true
decl_stmt|;
name|Module
name|m2
init|=
name|binder
lambda|->
name|configured
index|[
literal|1
index|]
operator|=
literal|true
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
name|asList
argument_list|(
name|m1
argument_list|,
name|m2
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|runtime
operator|.
name|getModules
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|modules
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configured
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configured
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDataChannel_CustomModule
parameter_list|()
block|{
specifier|final
name|DataChannel
name|channel
init|=
operator|new
name|DataChannel
argument_list|()
block|{
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Module
name|module
init|=
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|module
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|channel
argument_list|,
name|runtime
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetObjectContext_CustomModule
parameter_list|()
block|{
specifier|final
name|ObjectContext
name|context
init|=
operator|new
name|DataContext
argument_list|()
decl_stmt|;
specifier|final
name|ObjectContextFactory
name|factory
init|=
operator|new
name|ObjectContextFactory
argument_list|()
block|{
specifier|public
name|ObjectContext
name|createContext
parameter_list|(
name|DataChannel
name|parent
parameter_list|)
block|{
return|return
name|context
return|;
block|}
specifier|public
name|ObjectContext
name|createContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
decl_stmt|;
name|Module
name|module
init|=
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|module
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|runtime
operator|.
name|newContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|runtime
operator|.
name|newContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

