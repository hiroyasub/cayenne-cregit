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
name|xml
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
name|CayenneRuntimeException
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
name|DbAttribute
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
name|SQLTemplateDescriptor
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
name|Before
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
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|XMLDataMapLoaderTest
block|{
specifier|private
name|Injector
name|injector
decl_stmt|;
specifier|private
name|DataMapLoader
name|loader
decl_stmt|;
annotation|@
name|Before
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
name|binder
operator|.
name|bind
argument_list|(
name|HandlerFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultHandlerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|NoopDataChannelMetaData
operator|.
name|class
argument_list|)
expr_stmt|;
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
block|}
block|}
decl_stmt|;
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|testModule
argument_list|)
expr_stmt|;
name|loader
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|loadMissingConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file:/no_such_file_for_map_xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|loadEmptyConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testConfigMap2.map.xml"
argument_list|)
decl_stmt|;
name|DataMap
name|map
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
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap2"
argument_list|,
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getProcedures
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getQueryDescriptors
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|isClientSupported
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|loadFullDataMap
parameter_list|()
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testConfigMap4.map.xml"
argument_list|)
decl_stmt|;
name|DataMap
name|map
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
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConfigMap4"
argument_list|,
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// check general state
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|map
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
literal|17
argument_list|,
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|getProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|map
operator|.
name|getQueryDescriptors
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
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TEST_CATALOG"
argument_list|,
name|map
operator|.
name|getDefaultCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
name|map
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isClientSupported
argument_list|()
argument_list|)
expr_stmt|;
comment|// check some loaded content
name|assertEquals
argument_list|(
literal|"org.apache.cayenne.testdo.testmap.Artist"
argument_list|,
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"CompoundPainting"
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
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
literal|7
argument_list|,
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"ArtistCallback"
argument_list|)
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name = \"test\""
argument_list|,
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|getQualifier
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"EXHIBIT"
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
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
literal|"gallery_seq"
argument_list|,
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"GALLERY"
argument_list|)
operator|.
name|getPrimaryKeyGenerator
argument_list|()
operator|.
name|getGeneratorName
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|pk1
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"EXHIBIT"
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"EXHIBIT_ID"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|pk1
operator|.
name|isGenerated
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pk1
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|pk2
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"GENERATED_COLUMN"
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"GENERATED_COLUMN"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|pk2
operator|.
name|isGenerated
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pk2
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_out_proc"
argument_list|)
operator|.
name|isReturningValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_out_proc"
argument_list|)
operator|.
name|getCallOutParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_out_proc"
argument_list|)
operator|.
name|getCallParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|map
operator|.
name|getQueryDescriptor
argument_list|(
literal|"EjbqlQueryTest"
argument_list|)
operator|.
name|getProperty
argument_list|(
literal|"cayenne.GenericSelectQuery.fetchingDataRows"
argument_list|)
argument_list|)
expr_stmt|;
name|SQLTemplateDescriptor
name|descriptor
init|=
operator|(
name|SQLTemplateDescriptor
operator|)
name|map
operator|.
name|getQueryDescriptor
argument_list|(
literal|"NonSelectingQuery"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ESTIMATED_PRICE) "
operator|+
literal|"VALUES (512, 'No Painting Like This', 12.5)"
argument_list|,
name|descriptor
operator|.
name|getAdapterSql
argument_list|()
operator|.
name|get
argument_list|(
literal|"org.apache.cayenne.dba.db2.DB2Adapter"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TEST"
argument_list|,
name|map
operator|.
name|getEmbeddable
argument_list|(
literal|"org.apache.cayenne.testdo.Embeddable"
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"test"
argument_list|)
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

