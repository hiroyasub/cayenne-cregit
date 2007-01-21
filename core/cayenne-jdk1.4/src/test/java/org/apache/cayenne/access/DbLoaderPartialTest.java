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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DbLoaderPartialTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DbLoader
name|loader
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|loader
operator|=
operator|new
name|DbLoader
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|,
operator|new
name|DbLoaderDelegate
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
if|if
condition|(
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
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
block|}
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that FKs are properly loaded when the relationship source is not loaded. See      * CAY-479. This test will perform two reverse engineers. The second reverse engineer      * will skip two tables that share relationships with PAINTING. Relationships in      * ARTIST and GALLERY should remain unmodified, and all PAINTING relationships should      * be loaded.      */
specifier|public
name|void
name|testPartialLoad
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|boolean
name|supportsFK
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsFkConstraints
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|supportsFK
condition|)
block|{
return|return;
block|}
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
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
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
finally|finally
block|{
name|loader
operator|.
name|getCon
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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

