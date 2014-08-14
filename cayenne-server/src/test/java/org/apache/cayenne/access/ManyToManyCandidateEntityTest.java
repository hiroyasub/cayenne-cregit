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
name|access
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
name|configuration
operator|.
name|ConfigurationNameMapper
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
name|ConfigurationTree
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
name|DataChannelDescriptor
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
name|DefaultConfigurationNameMapper
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
name|XMLDataChannelDescriptorLoader
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
name|XMLDataMapLoader
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
name|AdhocObjectFactory
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
name|ClassLoaderManager
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
name|spi
operator|.
name|DefaultAdhocObjectFactory
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
name|map
operator|.
name|ObjEntity
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
name|Relationship
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
name|naming
operator|.
name|BasicNameGenerator
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
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_class
specifier|public
class|class
name|ManyToManyCandidateEntityTest
extends|extends
name|TestCase
block|{
specifier|private
name|DataMap
name|map
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
name|ClassLoaderManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultClassLoaderManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultAdhocObjectFactory
operator|.
name|class
argument_list|)
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
name|XMLDataMapLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNameMapper
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
name|testModule
argument_list|)
decl_stmt|;
comment|// create and initialize loader instance to test
name|XMLDataChannelDescriptorLoader
name|loader
init|=
operator|new
name|XMLDataChannelDescriptorLoader
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|String
name|testConfigName
init|=
literal|"relationship-optimisation"
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cayenne-"
operator|+
name|testConfigName
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|tree
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|=
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getDataMap
argument_list|(
name|testConfigName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchingForManyToManyEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|manyToManyEntity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Table1Table2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ManyToManyCandidateEntity
operator|.
name|build
argument_list|(
name|manyToManyEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchingForNotManyToManyEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|entity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Table1"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|ManyToManyCandidateEntity
operator|.
name|build
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOptimisationForManyToManyEntity
parameter_list|()
block|{
name|ObjEntity
name|manyToManyEntity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Table1Table2"
argument_list|)
decl_stmt|;
name|ManyToManyCandidateEntity
operator|.
name|build
argument_list|(
name|manyToManyEntity
argument_list|)
operator|.
name|optimizeRelationships
argument_list|(
operator|new
name|BasicNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|table1Entity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Table1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|table2Entity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Table2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|table1Entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|table2Entity
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Relationship
argument_list|>
argument_list|(
name|table1Entity
operator|.
name|getRelationships
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|table2Entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|table1Entity
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Relationship
argument_list|>
argument_list|(
name|table2Entity
operator|.
name|getRelationships
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

