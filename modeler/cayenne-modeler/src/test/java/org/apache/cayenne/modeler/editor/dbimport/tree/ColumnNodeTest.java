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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
operator|.
name|tree
package|;
end_package

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

begin_class
specifier|public
class|class
name|ColumnNodeTest
extends|extends
name|BaseNodeTest
block|{
specifier|private
name|ColumnNode
name|node
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|createNode
parameter_list|()
block|{
name|CatalogNode
name|catalogNode
init|=
operator|new
name|CatalogNode
argument_list|(
literal|"catalog"
argument_list|)
decl_stmt|;
name|SchemaNode
name|schemaNode
init|=
operator|new
name|SchemaNode
argument_list|(
literal|"schema"
argument_list|,
name|catalogNode
argument_list|)
decl_stmt|;
name|SchemaTableNode
name|tableNode
init|=
operator|new
name|SchemaTableNode
argument_list|(
literal|"table1"
argument_list|,
name|schemaNode
argument_list|)
decl_stmt|;
name|node
operator|=
operator|new
name|ColumnNode
argument_list|(
literal|"id"
argument_list|,
name|tableNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootEmpty
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootNoIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
literal|"table2"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootExcludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|excludeTable
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedExplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeColumn
argument_list|(
literal|"id"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootNoIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rootExcludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|excludeColumn
argument_list|(
literal|"id"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedExplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaIncludeAll
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeTable
argument_list|(
literal|"table1"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaNoIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeTable
argument_list|(
literal|"table2"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaExcludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|excludeTable
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|includeTable
argument_list|(
literal|"table2"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedExplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaNoIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaExcludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|excludeColumn
argument_list|(
literal|"id"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedExplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaIncludeTableRootNoIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
literal|"table2"
argument_list|)
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeTable
argument_list|(
literal|"table1"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaNoIncludeTableRootNoIncludeTable
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
literal|"table2"
argument_list|)
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaIncludeColumnRootNoIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|schemaNoIncludeColumnRootNoIncludeColumn
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableIncludeAll
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableNoInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedImplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableExclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|excludeColumn
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertExcludedExplicitly
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableNoExclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|excludeColumn
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|tableIncludeExclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeTable
argument_list|(
name|table
argument_list|(
literal|"table1"
argument_list|)
operator|.
name|excludeColumn
argument_list|(
literal|"name"
argument_list|)
operator|.
name|includeColumn
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertIncluded
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

