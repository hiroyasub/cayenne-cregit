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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneException
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
name|dba
operator|.
name|DbAdapter
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
name|Inject
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
name|DbEntity
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCaseDataSourceFactory
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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
name|Collection
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DbLoaderPartialIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
name|DbLoader
name|loader
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|=
operator|new
name|DbLoader
argument_list|(
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|,
name|adapter
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
block|{
specifier|public
name|boolean
name|overwriteDbEntity
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|CayenneException
block|{
return|return
operator|!
operator|(
name|ent
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"ARTIST"
argument_list|)
operator|||
name|ent
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PAINTING"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDownBeforeInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests that FKs are properly loaded when the relationship source is not loaded. See      * CAY-479. This test will perform two reverse engineers. The second reverse engineer      * will skip two tables that share relationships with PAINTING. Relationships in      * ARTIST and GALLERY should remain unmodified, and all PAINTING relationships should      * be loaded.      */
annotation|@
name|Test
specifier|public
name|void
name|testPartialLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tableLabel
init|=
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|loader
operator|.
name|loadDataMapFromDB
argument_list|(
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|rels
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"ARTIST"
argument_list|)
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|int
name|artistRels
init|=
name|rels
operator|.
name|size
argument_list|()
decl_stmt|;
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"GALLERY"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|int
name|galleryRels
init|=
name|rels
operator|.
name|size
argument_list|()
decl_stmt|;
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"PAINTING"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|int
name|paintingRels
init|=
name|rels
operator|.
name|size
argument_list|()
decl_stmt|;
name|loader
operator|.
name|loadDataMapFromDB
argument_list|(
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"ARTIST"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artistRels
argument_list|,
name|rels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"GALLERY"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|galleryRels
argument_list|,
name|rels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"PAINTING"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|paintingRels
argument_list|,
name|rels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbEntity
name|getDbEntity
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbEntity
name|de
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// sometimes table names get converted to lowercase
if|if
condition|(
name|de
operator|==
literal|null
condition|)
block|{
name|de
operator|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|de
return|;
block|}
block|}
end_class

end_unit

