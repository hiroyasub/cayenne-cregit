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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|access
operator|.
name|types
operator|.
name|ByteArrayTypeTest
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|testdo
operator|.
name|testmap
operator|.
name|ArtistExhibit
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
name|testdo
operator|.
name|testmap
operator|.
name|Exhibit
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
name|testdo
operator|.
name|testmap
operator|.
name|Gallery
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
name|testdo
operator|.
name|testmap
operator|.
name|Painting
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
name|testdo
operator|.
name|testmap
operator|.
name|PaintingInfo
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
literal|"cayenne-small-testmap.xml"
argument_list|)
specifier|public
class|class
name|CDOOneDep2OneTest
extends|extends
name|CayenneDOTestBase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context1
decl_stmt|;
specifier|public
name|void
name|testNewAdd1
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|newArtist
argument_list|()
decl_stmt|;
name|PaintingInfo
name|pi1
init|=
name|newPaintingInfo
argument_list|()
decl_stmt|;
name|Painting
name|p1
init|=
name|newPainting
argument_list|()
decl_stmt|;
comment|// needed to save without errors
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
comment|// *** TESTING THIS ***
name|pi1
operator|.
name|setPainting
argument_list|(
name|p1
argument_list|)
expr_stmt|;
comment|// test before save
name|assertSame
argument_list|(
name|pi1
argument_list|,
name|p1
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p1
argument_list|,
name|pi1
operator|.
name|getPainting
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|context1
expr_stmt|;
comment|// test database data
name|PaintingInfo
name|pi2
init|=
name|fetchPaintingInfo
argument_list|()
decl_stmt|;
name|Painting
name|p2
init|=
name|pi2
operator|.
name|getPainting
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|paintingName
argument_list|,
name|p2
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Tests how primary key is propagated from one new object to another. */
specifier|public
name|void
name|testNewAdd2
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|this
operator|.
name|newArtist
argument_list|()
decl_stmt|;
name|Gallery
name|g1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|g1
operator|.
name|setGalleryName
argument_list|(
name|galleryName
argument_list|)
expr_stmt|;
name|Exhibit
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setOpeningDate
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setClosingDate
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setToGallery
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|ae1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ArtistExhibit
operator|.
name|class
argument_list|)
decl_stmt|;
name|ae1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|ae1
operator|.
name|setToExhibit
argument_list|(
name|e1
argument_list|)
expr_stmt|;
comment|// *** TESTING THIS ***
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testReplace
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|altPaintingName
init|=
literal|"alt painting"
decl_stmt|;
name|PaintingInfo
name|pi1
init|=
name|newPaintingInfo
argument_list|()
decl_stmt|;
name|Painting
name|p1
init|=
name|newPainting
argument_list|()
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
name|altPaintingName
argument_list|)
expr_stmt|;
name|pi1
operator|.
name|setPainting
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|context1
expr_stmt|;
comment|// test database data
name|PaintingInfo
name|pi2
init|=
name|fetchPaintingInfo
argument_list|()
decl_stmt|;
name|Painting
name|p21
init|=
name|pi2
operator|.
name|getPainting
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p21
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|altPaintingName
argument_list|,
name|p21
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pi2
argument_list|,
name|p21
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayTypeTest
operator|.
name|assertByteArraysEqual
argument_list|(
name|paintingImage
argument_list|,
name|p21
operator|.
name|getToPaintingInfo
argument_list|()
operator|.
name|getImageBlob
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p22
init|=
name|newPainting
argument_list|()
decl_stmt|;
comment|// *** TESTING THIS ***
name|pi2
operator|.
name|setPainting
argument_list|(
name|p22
argument_list|)
expr_stmt|;
comment|// test before save
name|assertNull
argument_list|(
name|p21
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pi2
argument_list|,
name|p22
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p22
argument_list|,
name|pi2
operator|.
name|getPainting
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|pi2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save II
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectId
name|pi2oid
init|=
name|pi2
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|context
operator|=
name|context1
expr_stmt|;
name|PaintingInfo
name|pi3
init|=
name|fetchPaintingInfo
argument_list|()
decl_stmt|;
name|Painting
name|p3
init|=
name|pi3
operator|.
name|getPainting
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|paintingName
argument_list|,
name|p3
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pi3
argument_list|,
name|p3
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that object id was updated.
name|assertEquals
argument_list|(
name|pi2oid
argument_list|,
name|pi3
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
