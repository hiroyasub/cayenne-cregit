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
package|;
end_package

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
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CayenneRuntime
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

begin_class
specifier|public
class|class
name|BaseContextTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testUserPropertiesLazyInit
parameter_list|()
block|{
name|BaseContext
name|context
init|=
operator|new
name|MockBaseContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|userProperties
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|context
operator|.
name|getUserProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|properties
argument_list|,
name|context
operator|.
name|getUserProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAttachToRuntimeIfNeeded
parameter_list|()
block|{
specifier|final
name|DataChannel
name|channel
init|=
name|mock
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|QueryCache
name|cache
init|=
name|mock
argument_list|(
name|QueryCache
operator|.
name|class
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
name|DataChannel
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|Key
argument_list|<
name|QueryCache
argument_list|>
name|cacheKey
init|=
name|Key
operator|.
name|get
argument_list|(
name|QueryCache
operator|.
name|class
argument_list|,
name|BaseContext
operator|.
name|QUERY_CACHE_INJECTION_KEY
argument_list|)
decl_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|cacheKey
argument_list|)
operator|.
name|toInstance
argument_list|(
name|cache
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
name|cache
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
name|BaseContext
name|context
init|=
operator|new
name|MockBaseContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|channel
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|queryCache
argument_list|)
expr_stmt|;
name|Injector
name|oldInjector
init|=
name|CayenneRuntime
operator|.
name|getThreadInjector
argument_list|()
decl_stmt|;
try|try
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|attachToRuntimeIfNeeded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|channel
argument_list|,
name|context
operator|.
name|channel
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|attachToRuntimeIfNeeded
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|attachToRuntimeIfNeeded
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
name|oldInjector
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAttachToRuntimeIfNeeded_NoStack
parameter_list|()
block|{
name|BaseContext
name|context
init|=
operator|new
name|MockBaseContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|channel
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|queryCache
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|attachToRuntimeIfNeeded
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No thread stack, must have thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

