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
name|remote
operator|.
name|hessian
operator|.
name|service
package|;
end_package

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|services
operator|.
name|server
operator|.
name|ServiceContext
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
name|MockHttpSession
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
name|event
operator|.
name|MockEventBridgeFactory
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
import|;
end_import

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

begin_class
specifier|public
class|class
name|HessianServiceTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetSession
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
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVER_ROP_EVENT_BRIDGE_FACTORY_PROPERTY
argument_list|,
name|MockEventBridgeFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
literal|null
return|;
block|}
specifier|public
name|ObjectContext
name|createContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|HessianService
name|service
init|=
operator|new
name|HessianService
argument_list|(
name|factory
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|MockHttpServletRequest
name|request
init|=
operator|new
name|MockHttpServletRequest
argument_list|()
decl_stmt|;
name|HttpSession
name|session
init|=
operator|new
name|MockHttpSession
argument_list|()
decl_stmt|;
name|request
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
comment|// for some reason need to call this to get session activated in the
comment|// mock request
name|request
operator|.
name|getSession
argument_list|()
expr_stmt|;
try|try
block|{
name|ServiceContext
operator|.
name|begin
argument_list|(
name|request
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|session
argument_list|,
name|service
operator|.
name|getSession
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ServiceContext
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

