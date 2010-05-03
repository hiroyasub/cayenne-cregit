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
name|web
package|;
end_package

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
name|BaseContext
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
name|MockDataChannel
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
name|MockObjectContext
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
name|di
operator|.
name|Scopes
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
name|web
operator|.
name|MockHttpServletRequest
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
name|web
operator|.
name|MockHttpServletResponse
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
name|web
operator|.
name|MockHttpSession
import|;
end_import

begin_class
specifier|public
class|class
name|ServletContextHandlerTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testRequestStart_bindContext
parameter_list|()
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
name|binder
operator|.
name|bind
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockObjectContext
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|NO_SCOPE
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockDataChannel
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
name|SessionContextRequestHandler
name|handler
init|=
operator|new
name|SessionContextRequestHandler
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|MockHttpSession
name|session
init|=
operator|new
name|MockHttpSession
argument_list|()
decl_stmt|;
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|MockHttpServletRequest
name|request1
init|=
operator|new
name|MockHttpServletRequest
argument_list|()
decl_stmt|;
name|MockHttpServletResponse
name|response1
init|=
operator|new
name|MockHttpServletResponse
argument_list|()
decl_stmt|;
name|request1
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestStart
argument_list|(
name|request1
argument_list|,
name|response1
argument_list|)
expr_stmt|;
name|ObjectContext
name|c1
init|=
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestEnd
argument_list|(
name|request1
argument_list|,
name|response1
argument_list|)
expr_stmt|;
try|try
block|{
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"thread context not null"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|MockHttpServletRequest
name|request2
init|=
operator|new
name|MockHttpServletRequest
argument_list|()
decl_stmt|;
name|MockHttpServletResponse
name|response2
init|=
operator|new
name|MockHttpServletResponse
argument_list|()
decl_stmt|;
name|request2
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestStart
argument_list|(
name|request2
argument_list|,
name|response2
argument_list|)
expr_stmt|;
name|ObjectContext
name|c2
init|=
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestEnd
argument_list|(
name|request2
argument_list|,
name|response2
argument_list|)
expr_stmt|;
try|try
block|{
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"thread context not null"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|MockHttpServletRequest
name|request3
init|=
operator|new
name|MockHttpServletRequest
argument_list|()
decl_stmt|;
name|MockHttpServletResponse
name|response3
init|=
operator|new
name|MockHttpServletResponse
argument_list|()
decl_stmt|;
name|request3
operator|.
name|setSession
argument_list|(
operator|new
name|MockHttpSession
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestStart
argument_list|(
name|request3
argument_list|,
name|response3
argument_list|)
expr_stmt|;
name|ObjectContext
name|c3
init|=
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|c3
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c3
argument_list|)
expr_stmt|;
name|handler
operator|.
name|requestEnd
argument_list|(
name|request3
argument_list|,
name|response3
argument_list|)
expr_stmt|;
try|try
block|{
name|BaseContext
operator|.
name|getThreadObjectContext
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"thread context not null"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
finally|finally
block|{
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

