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
name|event
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|XMPPBridgeProviderTest
block|{
specifier|private
specifier|final
name|DataDomain
name|DOMAIN
init|=
operator|new
name|DataDomain
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|EventManager
name|EVENT_MANAGER
init|=
operator|new
name|DefaultEventManager
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|HOST_TEST
init|=
literal|"somehost.com"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|CHAT_SERVICE_TEST
init|=
literal|"conference"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|LOGIN_TEST
init|=
literal|"login"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|PASSWORD_TEST
init|=
literal|"password"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|boolean
name|SECURE_CONNECTION_TEST
init|=
literal|true
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|PORT_TEST
init|=
literal|12345
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetXMPPBridge
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|DefaultBindings
argument_list|()
argument_list|,
operator|new
name|XMPPModule
argument_list|()
argument_list|)
decl_stmt|;
name|EventBridge
name|bridge
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|EventBridge
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bridge
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bridge
operator|instanceof
name|XMPPBridge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUseProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Module
name|module
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
name|XMPPModule
operator|.
name|contributeSecureConnection
argument_list|(
name|binder
argument_list|,
name|SECURE_CONNECTION_TEST
argument_list|)
expr_stmt|;
name|XMPPModule
operator|.
name|contributeHost
argument_list|(
name|binder
argument_list|,
name|HOST_TEST
argument_list|)
expr_stmt|;
name|XMPPModule
operator|.
name|contributePort
argument_list|(
name|binder
argument_list|,
name|PORT_TEST
argument_list|)
expr_stmt|;
name|XMPPModule
operator|.
name|contributeLogin
argument_list|(
name|binder
argument_list|,
name|LOGIN_TEST
argument_list|,
name|PASSWORD_TEST
argument_list|)
expr_stmt|;
name|XMPPModule
operator|.
name|contributeChatService
argument_list|(
name|binder
argument_list|,
name|CHAT_SERVICE_TEST
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
operator|new
name|DefaultBindings
argument_list|()
argument_list|,
operator|new
name|XMPPModule
argument_list|()
argument_list|,
name|module
argument_list|)
decl_stmt|;
name|XMPPBridge
name|bridge
init|=
operator|(
name|XMPPBridge
operator|)
name|injector
operator|.
name|getInstance
argument_list|(
name|EventBridge
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HOST_TEST
argument_list|,
name|bridge
operator|.
name|getXmppHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CHAT_SERVICE_TEST
argument_list|,
name|bridge
operator|.
name|getChatService
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LOGIN_TEST
argument_list|,
name|bridge
operator|.
name|getLoginId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PASSWORD_TEST
argument_list|,
name|bridge
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SECURE_CONNECTION_TEST
argument_list|,
name|bridge
operator|.
name|isSecureConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PORT_TEST
argument_list|,
name|bridge
operator|.
name|getXmppPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUseDefaultProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|DefaultBindings
argument_list|()
argument_list|,
operator|new
name|XMPPModule
argument_list|()
argument_list|)
decl_stmt|;
name|XMPPBridge
name|bridge
init|=
operator|(
name|XMPPBridge
operator|)
name|injector
operator|.
name|getInstance
argument_list|(
name|EventBridge
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|XMPPBridge
operator|.
name|DEFAULT_CHAT_SERVICE
argument_list|,
name|bridge
operator|.
name|getChatService
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bridge
operator|.
name|getXmppPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|bridge
operator|.
name|isSecureConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|DefaultBindings
implements|implements
name|Module
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
name|binder
operator|.
name|bindMap
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|PROPERTIES_MAP
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
name|DOMAIN
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
name|EVENT_MANAGER
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
block|}
block|}
block|}
end_class

end_unit

