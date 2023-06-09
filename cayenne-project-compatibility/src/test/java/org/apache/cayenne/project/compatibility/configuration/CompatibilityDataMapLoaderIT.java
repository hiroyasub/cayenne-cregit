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
name|project
operator|.
name|compatibility
operator|.
name|configuration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|DataMapLoader
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
name|xml
operator|.
name|XMLReaderProvider
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
name|map
operator|.
name|DataMap
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
name|project
operator|.
name|compatibility
operator|.
name|CompatibilityTestModule
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
name|Resource
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
name|URLResource
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CompatibilityDataMapLoaderIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLoad
parameter_list|()
block|{
name|Injector
name|injector
init|=
name|getInjector
argument_list|()
decl_stmt|;
name|DataMapLoader
name|loader
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|loader
operator|instanceof
name|CompatibilityDataMapLoader
argument_list|)
expr_stmt|;
name|URL
name|resourceUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"../test-map-v6.map.xml"
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|resourceUrl
argument_list|)
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|loader
operator|.
name|load
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getDbEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dataMap
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Artist"
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CompatibilityTestModule
argument_list|()
argument_list|,
name|binder
lambda|->
block|{
name|binder
operator|.
name|bind
argument_list|(
name|XMLReader
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|XMLReaderProvider
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|withoutScope
argument_list|()
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|CompatibilityDataMapLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

