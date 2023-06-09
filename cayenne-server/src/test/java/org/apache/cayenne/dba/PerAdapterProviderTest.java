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
name|dba
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
name|types
operator|.
name|ExtendedType
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
name|ExtendedTypeFactory
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
name|derby
operator|.
name|DerbyAdapter
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
name|DIRuntimeException
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
name|Map
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
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
specifier|public
class|class
name|PerAdapterProviderTest
block|{
specifier|private
name|OracleAdapter
name|oracleAdapter
decl_stmt|;
specifier|private
name|DerbyAdapter
name|derbyAdapter
decl_stmt|;
specifier|private
name|AutoAdapter
name|autoDerbyAdapter
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|ResourceLocator
name|locator
init|=
operator|new
name|ClassLoaderResourceLocator
argument_list|(
operator|new
name|DefaultClassLoaderManager
argument_list|()
argument_list|)
decl_stmt|;
name|RuntimeProperties
name|runtimeProperties
init|=
name|mock
argument_list|(
name|RuntimeProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|ValueObjectTypeRegistry
name|valueObjectTypeRegistry
init|=
name|mock
argument_list|(
name|ValueObjectTypeRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|oracleAdapter
operator|=
operator|new
name|OracleAdapter
argument_list|(
name|runtimeProperties
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedType
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedType
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedTypeFactory
operator|>
name|emptyList
argument_list|()
argument_list|,
name|locator
argument_list|,
name|valueObjectTypeRegistry
argument_list|)
expr_stmt|;
name|this
operator|.
name|derbyAdapter
operator|=
operator|new
name|DerbyAdapter
argument_list|(
name|runtimeProperties
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedType
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedType
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ExtendedTypeFactory
operator|>
name|emptyList
argument_list|()
argument_list|,
name|locator
argument_list|,
name|valueObjectTypeRegistry
argument_list|)
expr_stmt|;
name|this
operator|.
name|autoDerbyAdapter
operator|=
operator|new
name|AutoAdapter
argument_list|(
operator|new
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|DbAdapter
name|get
parameter_list|()
throws|throws
name|DIRuntimeException
block|{
return|return
name|derbyAdapter
return|;
block|}
block|}
argument_list|,
operator|new
name|Slf4jJdbcEventLogger
argument_list|(
name|runtimeProperties
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|DerbyAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"x"
argument_list|)
decl_stmt|;
name|PerAdapterProvider
argument_list|<
name|String
argument_list|>
name|provider
init|=
operator|new
name|PerAdapterProvider
argument_list|<>
argument_list|(
name|map
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|provider
operator|.
name|get
argument_list|(
name|oracleAdapter
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|provider
operator|.
name|get
argument_list|(
name|derbyAdapter
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|provider
operator|.
name|get
argument_list|(
name|autoDerbyAdapter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

