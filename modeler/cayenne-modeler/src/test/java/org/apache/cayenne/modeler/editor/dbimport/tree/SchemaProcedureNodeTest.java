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
name|SchemaProcedureNodeTest
extends|extends
name|BaseNodeTest
block|{
specifier|private
name|SchemaProcedureNode
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
name|node
operator|=
operator|new
name|SchemaProcedureNode
argument_list|(
literal|"procedure1"
argument_list|,
name|schemaNode
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
name|rootInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeProcedure
argument_list|(
literal|"procedure1"
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
name|rootNoInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeProcedure
argument_list|(
literal|"procedure2"
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
name|rootExclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|excludeProcedure
argument_list|(
literal|"procedure1"
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
name|schemaInclude
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
name|includeProcedure
argument_list|(
literal|"procedure1"
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
name|schemaNoInclude
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
name|includeProcedure
argument_list|(
literal|"procedure2"
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
name|schemaExclude
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
name|excludeProcedure
argument_list|(
literal|"procedure1"
argument_list|)
operator|.
name|includeProcedure
argument_list|(
literal|"procedure2"
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
name|schemaIncludeRootNoInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeProcedure
argument_list|(
literal|"procedure2"
argument_list|)
operator|.
name|schema
argument_list|(
name|schema
argument_list|(
literal|"schema"
argument_list|)
operator|.
name|includeProcedure
argument_list|(
literal|"procedure1"
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
name|schemaNoIncludeRootNoInclude
parameter_list|()
block|{
name|config
operator|=
name|config
argument_list|()
operator|.
name|includeProcedure
argument_list|(
literal|"procedure2"
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
block|}
end_class

end_unit

