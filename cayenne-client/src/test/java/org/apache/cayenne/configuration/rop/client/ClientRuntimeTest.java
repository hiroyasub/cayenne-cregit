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
name|rop
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneContext
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
name|server
operator|.
name|ServerModule
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
name|DefaultEventManager
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
name|remote
operator|.
name|ClientChannel
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
name|remote
operator|.
name|ClientConnection
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
name|remote
operator|.
name|MockClientConnection
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotSame
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
name|assertSame
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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
class|class
name|ClientRuntimeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetObjectContext
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ClientModule
name|extraModule
init|=
operator|new
name|ClientModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|super
operator|.
name|configure
argument_list|(
name|binder
argument_list|)
expr_stmt|;
comment|// use a noop connection to prevent startup errors...
name|binder
operator|.
name|bind
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockClientConnection
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ClientRuntime
name|runtime
init|=
name|ClientRuntime
operator|.
name|builder
argument_list|()
operator|.
name|properties
argument_list|(
name|properties
argument_list|)
operator|.
name|addModule
argument_list|(
name|extraModule
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|instanceof
name|CayenneContext
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"ObjectContext must not be a singleton"
argument_list|,
name|context
argument_list|,
name|runtime
operator|.
name|newContext
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneContext
name|clientContext
init|=
operator|(
name|CayenneContext
operator|)
name|context
decl_stmt|;
name|assertNotNull
argument_list|(
name|clientContext
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
argument_list|,
name|clientContext
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
name|testGetDataChannel
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Module
name|extraModule
init|=
name|binder
lambda|->
comment|// use a noop connection to prevent hessian startup errors...
name|binder
operator|.
name|bind
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockClientConnection
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientRuntime
name|runtime
init|=
name|ClientRuntime
operator|.
name|builder
argument_list|()
operator|.
name|properties
argument_list|(
name|properties
argument_list|)
operator|.
name|addModule
argument_list|(
name|extraModule
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|DataChannel
name|channel
init|=
name|runtime
operator|.
name|getChannel
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
name|ClientChannel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ClientRuntime
name|runtime
init|=
name|ClientRuntime
operator|.
name|builder
argument_list|()
operator|.
name|properties
argument_list|(
name|properties
argument_list|)
operator|.
name|addModule
argument_list|(
name|binder
lambda|->
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVER_CONTEXTS_SYNC_PROPERTY
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// make sure objects to be shut down are resolved
name|EventManager
name|em
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|EventManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|em
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|em
operator|instanceof
name|DefaultEventManager
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|DefaultEventManager
operator|)
name|em
operator|)
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|DefaultEventManager
operator|)
name|em
operator|)
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

