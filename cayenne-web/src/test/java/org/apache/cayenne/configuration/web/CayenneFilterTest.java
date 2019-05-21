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
name|web
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
name|MockFilterChain
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
name|MockFilterConfig
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
name|assertNull
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
name|CayenneFilterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInitWithFilterName
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
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
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"abc.xml"
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
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
name|WebConfiguration
operator|.
name|CONFIGURATION_LOCATION_PARAMETER
argument_list|,
literal|"xyz"
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"xyz"
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
name|testInitWithMultipleLocations
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
name|WebConfiguration
operator|.
name|CONFIGURATION_LOCATION_PARAMETER
argument_list|,
literal|"xyz,abc,\tdef, \n ghi"
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
literal|"xyz"
argument_list|,
literal|"abc"
argument_list|,
literal|"def"
argument_list|,
literal|"ghi"
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
name|testInitWithCustomDomainName
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
name|WebConfiguration
operator|.
name|DATA_DOMAIN_NAME_PARAMETER
argument_list|,
literal|"custom"
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|runtime
argument_list|)
expr_stmt|;
name|String
name|domainName
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
name|getMapOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|PROPERTIES_MAP
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVER_DOMAIN_NAME_PROPERTY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"custom"
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitWithStandardModules
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"cayenne-abc"
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
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
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"cayenne-abc.xml"
argument_list|)
argument_list|,
name|locations
argument_list|)
expr_stmt|;
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
literal|3
argument_list|,
name|modules
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|marray
init|=
name|modules
operator|.
name|toArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|marray
index|[
literal|0
index|]
operator|instanceof
name|ServerModule
condition|)
block|{
name|assertTrue
argument_list|(
name|marray
index|[
literal|1
index|]
operator|instanceof
name|WebModule
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|marray
index|[
literal|0
index|]
operator|instanceof
name|WebModule
argument_list|)
expr_stmt|;
block|}
name|RequestHandler
name|handler
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|RequestHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|instanceof
name|SessionContextRequestHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitWithExtraModules
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
name|WebConfiguration
operator|.
name|EXTRA_MODULES_PARAMETER
argument_list|,
name|MockModule1
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|","
operator|+
name|MockModule2
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|runtime
argument_list|)
expr_stmt|;
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
literal|5
argument_list|,
name|modules
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|marray
init|=
name|modules
operator|.
name|toArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|marray
index|[
literal|0
index|]
operator|instanceof
name|ServerModule
condition|)
block|{
name|assertTrue
argument_list|(
name|marray
index|[
literal|1
index|]
operator|instanceof
name|WebModule
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|marray
index|[
literal|0
index|]
operator|instanceof
name|WebModule
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|marray
index|[
literal|2
index|]
operator|instanceof
name|MockModule1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|marray
index|[
literal|3
index|]
operator|instanceof
name|MockModule2
argument_list|)
expr_stmt|;
name|RequestHandler
name|handler
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|RequestHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|instanceof
name|MockRequestHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|MockFilterConfig
name|config
init|=
operator|new
name|MockFilterConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFilterName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitParameter
argument_list|(
name|WebConfiguration
operator|.
name|EXTRA_MODULES_PARAMETER
argument_list|,
name|CayenneFilter_DispatchModule
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|setupServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CayenneFilter
name|filter
init|=
operator|new
name|CayenneFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CayenneFilter_DispatchRequestHandler
name|handler
init|=
operator|(
name|CayenneFilter_DispatchRequestHandler
operator|)
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|RequestHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler
operator|.
name|getStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler
operator|.
name|getEnded
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|doFilter
argument_list|(
operator|new
name|MockHttpServletRequest
argument_list|()
argument_list|,
operator|new
name|MockHttpServletResponse
argument_list|()
argument_list|,
operator|new
name|MockFilterChain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler
operator|.
name|getStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler
operator|.
name|getEnded
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|doFilter
argument_list|(
operator|new
name|MockHttpServletRequest
argument_list|()
argument_list|,
operator|new
name|MockHttpServletResponse
argument_list|()
argument_list|,
operator|new
name|MockFilterChain
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler
operator|.
name|getStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler
operator|.
name|getEnded
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

