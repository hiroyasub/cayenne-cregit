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
name|tools
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
name|dbimport
operator|.
name|Catalog
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
name|dbimport
operator|.
name|Schema
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|FiltersConfig
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|IncludeTableFilter
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|PatternFilter
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|TableFilter
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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|testing
operator|.
name|AbstractMojoTestCase
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
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertCatalog
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertCatalogAndSchema
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertFlat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertSchemaContent
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertSkipPrimaryKeyLoading
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertSkipRelationshipsLoading
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|DefaultReverseEngineeringLoaderTest
operator|.
name|assertTableTypes
import|;
end_import

begin_class
specifier|public
class|class
name|DbImporterMojoConfigurationTest
extends|extends
name|AbstractMojoTestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLoadCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Catalog
argument_list|>
name|catalogs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Catalog
name|c
range|:
name|getCdbImport
argument_list|(
literal|"pom-catalog.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getCatalogs
argument_list|()
control|)
block|{
name|catalogs
operator|.
name|put
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalogs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Catalog
name|c3
init|=
name|catalogs
operator|.
name|get
argument_list|(
literal|"catalog-name-03"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c3
argument_list|)
expr_stmt|;
name|assertCatalog
argument_list|(
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Schema
argument_list|>
name|schemas
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Schema
name|s
range|:
name|getCdbImport
argument_list|(
literal|"pom-schema.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getSchemas
argument_list|()
control|)
block|{
name|schemas
operator|.
name|put
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|schemas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Schema
name|s3
init|=
name|schemas
operator|.
name|get
argument_list|(
literal|"schema-name-03"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s3
argument_list|)
expr_stmt|;
name|assertSchemaContent
argument_list|(
name|s3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSchema2
parameter_list|()
throws|throws
name|Exception
block|{
name|FiltersConfig
name|filters
init|=
name|getCdbImport
argument_list|(
literal|"pom-schema-2.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
operator|.
name|getDbLoaderConfig
argument_list|()
operator|.
name|getFiltersConfig
argument_list|()
decl_stmt|;
name|TreeSet
argument_list|<
name|IncludeTableFilter
argument_list|>
name|includes
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|includes
operator|.
name|add
argument_list|(
operator|new
name|IncludeTableFilter
argument_list|(
literal|null
argument_list|,
operator|new
name|PatternFilter
argument_list|()
operator|.
name|exclude
argument_list|(
literal|"^ETL_.*"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|TreeSet
argument_list|<
name|Pattern
argument_list|>
name|excludes
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|PatternFilter
operator|.
name|PATTERN_COMPARATOR
argument_list|)
decl_stmt|;
name|excludes
operator|.
name|add
argument_list|(
name|PatternFilter
operator|.
name|pattern
argument_list|(
literal|"^ETL_.*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|filters
operator|.
name|tableFilter
argument_list|(
literal|null
argument_list|,
literal|"NHL_STATS"
argument_list|)
argument_list|,
operator|new
name|TableFilter
argument_list|(
name|includes
argument_list|,
name|excludes
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadCatalogAndSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCatalogAndSchema
argument_list|(
name|getCdbImport
argument_list|(
literal|"pom-catalog-and-schema.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultPackage
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImportConfiguration
name|config
init|=
name|getCdbImport
argument_list|(
literal|"pom-default-package.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"com.example.test"
argument_list|,
name|config
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadFlat
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFlat
argument_list|(
name|getCdbImport
argument_list|(
literal|"pom-flat.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSkipRelationshipsLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSkipRelationshipsLoading
argument_list|(
name|getCdbImport
argument_list|(
literal|"pom-skip-relationships-loading.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSkipPrimaryKeyLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSkipPrimaryKeyLoading
argument_list|(
name|getCdbImport
argument_list|(
literal|"pom-skip-primary-key-loading.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTableTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTableTypes
argument_list|(
name|getCdbImport
argument_list|(
literal|"pom-table-types.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbImporterMojo
name|getCdbImport
parameter_list|(
name|String
name|pomFileName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|DbImporterMojo
operator|)
name|lookupMojo
argument_list|(
literal|"cdbimport"
argument_list|,
name|getTestFile
argument_list|(
literal|"src/test/resources/org/apache/cayenne/tools/config/"
operator|+
name|pomFileName
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

