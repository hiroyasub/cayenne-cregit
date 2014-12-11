begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|dbimport
operator|.
name|config
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
name|resource
operator|.
name|URLResource
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
name|ExcludeTable
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|assertArrayEquals
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultReverseEngineeringLoaderTest
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
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-catalog.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertCatalog
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertCatalog
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Catalog
argument_list|>
name|catalogs
init|=
name|engineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"catalog-name-01"
argument_list|,
name|catalogs
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
literal|"catalog-name-02"
argument_list|,
name|catalogs
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertCatalog
argument_list|(
name|catalogs
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertCatalog
parameter_list|(
name|Iterator
argument_list|<
name|Catalog
argument_list|>
name|catalogs
parameter_list|)
block|{
name|Catalog
name|catalog
init|=
name|catalogs
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"catalog-name-03"
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeTable
argument_list|>
name|includeTables
init|=
name|catalog
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-01"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-02"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|IncludeTable
name|includeTable
init|=
name|includeTables
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-03"
argument_list|,
name|includeTable
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeTable
argument_list|>
name|excludeTables
init|=
name|catalog
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-01"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-02"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-03"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
name|catalog
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-02"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-03"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
name|catalog
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-02"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-03"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeProcedure
argument_list|>
name|excludeProcedures
init|=
name|catalog
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-01"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-02"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-03"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeProcedure
argument_list|>
name|includeProcedures
init|=
name|catalog
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-01"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-02"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-03"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
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
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-schema.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertSchema
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertSchema
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
name|engineering
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-01"
argument_list|,
name|schemas
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
literal|"schema-name-02"
argument_list|,
name|schemas
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
name|schemas
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-03"
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSchemaContent
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertSchemaContent
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|Iterator
argument_list|<
name|IncludeTable
argument_list|>
name|includeTables
init|=
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-01"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-02"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|IncludeTable
name|includeTable
init|=
name|includeTables
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-03"
argument_list|,
name|includeTable
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeTable
argument_list|>
name|excludeTables
init|=
name|schema
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-01"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-02"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-03"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
name|schema
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-02"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-03"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
name|schema
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-02"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-03"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeProcedure
argument_list|>
name|excludeProcedures
init|=
name|schema
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-01"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-02"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-03"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeProcedure
argument_list|>
name|includeProcedures
init|=
name|schema
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-01"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-02"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-03"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
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
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-catalog-and-schema.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertCatalogAndSchema
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertCatalogAndSchema
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|Catalog
name|catalog
init|=
name|engineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"catalog-name"
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name"
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSchemaContent
argument_list|(
name|schema
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
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-flat.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFlat
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertFlat
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|Iterator
argument_list|<
name|IncludeTable
argument_list|>
name|includeTables
init|=
name|engineering
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-01"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-02"
argument_list|,
name|includeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|IncludeTable
name|includeTable
init|=
name|includeTables
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeTable-03"
argument_list|,
name|includeTable
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeTable
argument_list|>
name|excludeTables
init|=
name|engineering
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-01"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-02"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeTable-03"
argument_list|,
name|excludeTables
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
name|engineering
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-02"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-03"
argument_list|,
name|excludeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
name|engineering
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-02"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-03"
argument_list|,
name|includeColumns
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ExcludeProcedure
argument_list|>
name|excludeProcedures
init|=
name|engineering
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-01"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-02"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeProcedure-03"
argument_list|,
name|excludeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeProcedure
argument_list|>
name|includeProcedures
init|=
name|engineering
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-01"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-02"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeProcedure-03"
argument_list|,
name|includeProcedures
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSkipRelationships
parameter_list|()
throws|throws
name|Exception
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-skipRelationshipsLoading.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertSkipRelationshipsLoading
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertSkipRelationshipsLoading
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|engineering
operator|.
name|getSkipRelationshipsLoading
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
name|ReverseEngineering
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|.
name|load
argument_list|(
name|getResource
argument_list|(
literal|"reverseEngineering-tableTypes.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTableTypes
argument_list|(
name|engineering
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|assertTableTypes
parameter_list|(
name|ReverseEngineering
name|engineering
parameter_list|)
block|{
name|assertArrayEquals
argument_list|(
name|engineering
operator|.
name|getTableTypes
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"type1"
block|,
literal|"type2"
block|,
literal|"type3"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|URLResource
name|getResource
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
operator|new
name|URLResource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

