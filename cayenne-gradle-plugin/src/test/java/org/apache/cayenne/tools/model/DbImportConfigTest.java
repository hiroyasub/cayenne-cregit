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
operator|.
name|model
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportConfigTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|toEmptyReverseEngineering
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImportConfig
name|config
init|=
operator|new
name|DbImportConfig
argument_list|()
decl_stmt|;
name|ReverseEngineering
name|rr
init|=
name|config
operator|.
name|toReverseEngineering
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getSchemas
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rr
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rr
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cayenne.dbsync.naming.DefaultObjectNameGenerator"
argument_list|,
name|rr
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|rr
operator|.
name|getStripFromTableNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|rr
operator|.
name|getTableTypes
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|isForceDataMapCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|isForceDataMapSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|isUseJava7Types
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|isEmptyContainer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|toReverseEngineering
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImportConfig
name|config
init|=
operator|new
name|DbImportConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|catalog
argument_list|(
literal|"catalog1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|schema
argument_list|(
literal|"schema1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDefaultPackage
argument_list|(
literal|"com.example.package"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMeaningfulPkTables
argument_list|(
literal|"pk_tables"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setNamingStrategy
argument_list|(
literal|"com.example.naming"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSkipRelationshipsLoading
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setStripFromTableNames
argument_list|(
literal|"strip"
argument_list|)
expr_stmt|;
name|config
operator|.
name|tableType
argument_list|(
literal|"table"
argument_list|)
expr_stmt|;
name|config
operator|.
name|tableTypes
argument_list|(
literal|"view"
argument_list|,
literal|"alias"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapCatalog
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapSchema
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseJava7Types
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsePrimitives
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ReverseEngineering
name|rr
init|=
name|config
operator|.
name|toReverseEngineering
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rr
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"catalog1"
argument_list|,
name|rr
operator|.
name|getCatalogs
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rr
operator|.
name|getSchemas
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"schema1"
argument_list|,
name|rr
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rr
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com.example.package"
argument_list|,
name|rr
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pk_tables"
argument_list|,
name|rr
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com.example.naming"
argument_list|,
name|rr
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"strip"
argument_list|,
name|rr
operator|.
name|getStripFromTableNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"table"
block|,
literal|"view"
block|,
literal|"alias"
block|}
argument_list|,
name|rr
operator|.
name|getTableTypes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|isForceDataMapCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|isForceDataMapSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rr
operator|.
name|isUseJava7Types
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rr
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

