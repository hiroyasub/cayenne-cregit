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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
package|;
end_package

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
name|Set
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
name|DbEntity
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

begin_class
specifier|public
class|class
name|ExportedKeyLoaderIT
extends|extends
name|BaseLoaderIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testExportedKeyLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|supportsFK
init|=
name|accessStackAdapter
operator|.
name|supportsFKConstraints
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
name|createEntity
argument_list|(
name|nameForDb
argument_list|(
literal|"ARTIST"
argument_list|)
argument_list|)
expr_stmt|;
name|createEntity
argument_list|(
name|nameForDb
argument_list|(
literal|"GALLERY"
argument_list|)
argument_list|)
expr_stmt|;
name|createEntity
argument_list|(
name|nameForDb
argument_list|(
literal|"PAINTING"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|catalog
init|=
name|connection
operator|.
name|getCatalog
argument_list|()
decl_stmt|;
name|String
name|schema
init|=
name|connection
operator|.
name|getSchema
argument_list|()
decl_stmt|;
name|DbEntity
name|artist
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|artist
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|artist
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|DbAttribute
name|artistId
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|artist
operator|.
name|addAttribute
argument_list|(
name|artistId
argument_list|)
expr_stmt|;
name|DbEntity
name|gallery
init|=
name|getDbEntity
argument_list|(
literal|"GALLERY"
argument_list|)
decl_stmt|;
name|gallery
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|gallery
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|DbAttribute
name|galleryId
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"GALLERY_ID"
argument_list|)
decl_stmt|;
name|gallery
operator|.
name|addAttribute
argument_list|(
name|galleryId
argument_list|)
expr_stmt|;
name|DbEntity
name|painting
init|=
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|DbAttribute
name|paintingId
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"PAINTING_ID"
argument_list|)
decl_stmt|;
name|DbAttribute
name|paintingArtistId
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|DbAttribute
name|paintingGalleryId
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"GALLERY_ID"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|addAttribute
argument_list|(
name|paintingId
argument_list|)
expr_stmt|;
name|painting
operator|.
name|addAttribute
argument_list|(
name|paintingArtistId
argument_list|)
expr_stmt|;
name|painting
operator|.
name|addAttribute
argument_list|(
name|paintingGalleryId
argument_list|)
expr_stmt|;
name|ExportedKeyLoader
name|loader
init|=
operator|new
name|ExportedKeyLoader
argument_list|(
name|EMPTY_CONFIG
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|store
operator|.
name|getExportedKeysEntrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ExportedKey
name|artistIdFk
init|=
name|findArtistExportedKey
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistIdFk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST"
argument_list|,
name|artistIdFk
operator|.
name|getPk
argument_list|()
operator|.
name|getTable
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|artistIdFk
operator|.
name|getPk
argument_list|()
operator|.
name|getColumn
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PAINTING"
argument_list|,
name|artistIdFk
operator|.
name|getFk
argument_list|()
operator|.
name|getTable
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|artistIdFk
operator|.
name|getFk
argument_list|()
operator|.
name|getColumn
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ExportedKey
name|findArtistExportedKey
parameter_list|()
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ExportedKey
argument_list|>
argument_list|>
name|entry
range|:
name|store
operator|.
name|getExportedKeysEntrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".ARTIST_ID"
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

