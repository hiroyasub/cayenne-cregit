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
name|rop
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
name|web
operator|.
name|MockServletConfig
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
name|MockServletContext
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
name|web
operator|.
name|*
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
name|rop
operator|.
name|ROPServlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
specifier|public
class|class
name|ROPServletTest
block|{
specifier|private
name|CayenneRuntime
name|runtime
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|shutdownRuntime
parameter_list|()
block|{
if|if
condition|(
name|runtime
operator|!=
literal|null
condition|)
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
name|testInitWithServletName
parameter_list|()
throws|throws
name|Exception
block|{
name|MockServletConfig
name|config
init|=
operator|new
name|MockServletConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setServletName
argument_list|(
literal|"cayenne-org.apache.cayenne.configuration.rop.server.test-config"
argument_list|)
expr_stmt|;
name|MockServletContext
name|context
init|=
operator|new
name|MockServletContext
argument_list|()
decl_stmt|;
name|config
operator|.
name|setServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|ROPServlet
name|servlet
init|=
operator|new
name|ROPServlet
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|runtime
operator|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|runtime
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|locations
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|getListOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|SERVER_PROJECT_LOCATIONS_LIST
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"cayenne-org.apache.cayenne.configuration.rop.server.test-config.xml"
argument_list|)
argument_list|,
name|locations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitWithLocation
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|location
init|=
literal|"cayenne-org.apache.cayenne.configuration.rop.server.test-config.xml"
decl_stmt|;
name|MockServletConfig
name|config
init|=
operator|new
name|MockServletConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setServletName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
literal|"configuration-location"
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|MockServletContext
name|context
init|=
operator|new
name|MockServletContext
argument_list|()
decl_stmt|;
name|config
operator|.
name|setServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|ROPServlet
name|servlet
init|=
operator|new
name|ROPServlet
argument_list|()
decl_stmt|;
name|servlet
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|runtime
operator|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|runtime
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|locations
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|getListOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|SERVER_PROJECT_LOCATIONS_LIST
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|location
argument_list|)
argument_list|,
name|locations
argument_list|)
expr_stmt|;
block|}
comment|//	@Test
comment|//	public void testInitWithStandardModules() throws Exception {
comment|//
comment|//		String name = "cayenne-org.apache.cayenne.configuration.rop.server.test-config";
comment|//
comment|//		MockServletConfig config = new MockServletConfig();
comment|//		config.setServletName(name);
comment|//
comment|//		MockServletContext context = new MockServletContext();
comment|//		config.setServletContext(context);
comment|//
comment|//		ROPServlet servlet = new ROPServlet();
comment|//		servlet.init(config);
comment|//
comment|//		runtime = WebUtil.getCayenneRuntime(context);
comment|//		assertNotNull(runtime);
comment|//
comment|//		List<String> locations = runtime.getInjector().getInstance(
comment|//				Key.getListOf(String.class, Constants.SERVER_PROJECT_LOCATIONS_LIST));
comment|//
comment|//		assertEquals(Arrays.asList(name + ".xml"), locations);
comment|//
comment|//		Collection<Module> modules = runtime.getModules();
comment|//		assertEquals(3, modules.size());
comment|//		Object[] marray = modules.toArray();
comment|//
comment|//		assertTrue(marray[0] instanceof ServerModule);
comment|//		// [2] is an inner class
comment|//		assertTrue(marray[1] instanceof ROPServerModule);
comment|//	}
comment|//	@Test
comment|//	public void testInitWithExtraModules() throws Exception {
comment|//
comment|//		String name = "cayenne-org.apache.cayenne.configuration.rop.server.test-config";
comment|//
comment|//		MockServletConfig config = new MockServletConfig();
comment|//		config.setServletName(name);
comment|//		config.setInitParameter("extra-modules", MockModule1.class.getName() + "," + MockModule2.class.getName());
comment|//
comment|//		MockServletContext context = new MockServletContext();
comment|//		config.setServletContext(context);
comment|//
comment|//		ROPServlet servlet = new ROPServlet();
comment|//		servlet.init(config);
comment|//
comment|//		runtime = WebUtil.getCayenneRuntime(context);
comment|//		assertNotNull(runtime);
comment|//
comment|//		Collection<Module> modules = runtime.getModules();
comment|//		assertEquals(5, modules.size());
comment|//
comment|//		Object[] marray = modules.toArray();
comment|//
comment|//		assertTrue(marray[0] instanceof ServerModule);
comment|//		// [1] is an inner class
comment|//		assertTrue(marray[1] instanceof ROPServerModule);
comment|//		assertTrue(marray[2] instanceof MockModule1);
comment|//		assertTrue(marray[3] instanceof MockModule2);
comment|//
comment|//		RequestHandler handler = runtime.getInjector().getInstance(RequestHandler.class);
comment|//		assertTrue(handler instanceof MockRequestHandler);
comment|//	}
comment|//	@Test
comment|//	public void testInitHessianService() throws Exception {
comment|//
comment|//		MockServletConfig config = new MockServletConfig();
comment|//		config.setServletName("abc");
comment|//
comment|//		MockServletContext context = new MockServletContext();
comment|//		config.setServletContext(context);
comment|//		config.setInitParameter("extra-modules", ROPHessianServlet_ConfigModule.class.getName());
comment|//
comment|//		ROPServlet servlet = new ROPServlet();
comment|//
comment|//		servlet.init(config);
comment|//		runtime = WebUtil.getCayenneRuntime(context);
comment|//		Collection<Module> modules = runtime.getModules();
comment|//		assertEquals(4, modules.size());
comment|//
comment|//		Object[] marray = modules.toArray();
comment|//
comment|//		assertTrue(marray[2] instanceof ROPHessianServlet_ConfigModule);
comment|//
comment|//		// TODO: mock servlet request to check that the right service instance
comment|//		// is invoked
comment|//	}
block|}
end_class

end_unit

