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
name|ClientModuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testObjectContextFactory
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
name|module
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
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
name|ObjectContextFactory
name|factory
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"ObjectContextFactory must be a singleton"
argument_list|,
name|factory
argument_list|,
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataChannel
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
name|module
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
name|module
argument_list|)
decl_stmt|;
name|DataChannel
name|channel
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
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
name|assertSame
argument_list|(
literal|"DataChannel must be a singleton"
argument_list|,
name|channel
argument_list|,
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ClientChannel
name|clientChannel
init|=
operator|(
name|ClientChannel
operator|)
name|channel
decl_stmt|;
name|assertTrue
argument_list|(
name|clientChannel
operator|.
name|getConnection
argument_list|()
operator|instanceof
name|MockClientConnection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clientChannel
operator|.
name|getEventManager
argument_list|()
operator|instanceof
name|DefaultEventManager
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|clientChannel
operator|.
name|isChannelEventsEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

