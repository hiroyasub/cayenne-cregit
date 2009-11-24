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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
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
name|art
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
name|art
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
name|DataObject
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
name|DataRow
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
name|MockDataObject
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
name|ObjectId
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Cayenne
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ObjectStoreTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testRegisteredObjectsCount
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|o1
init|=
operator|new
name|MockDataObject
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"key1"
argument_list|,
literal|"v1"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerNode
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// test object with same id
name|DataObject
name|o2
init|=
operator|new
name|MockDataObject
argument_list|()
decl_stmt|;
name|o2
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"key1"
argument_list|,
literal|"v1"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerNode
argument_list|(
name|o2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|o2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// test new object
name|DataObject
name|o3
init|=
operator|new
name|MockDataObject
argument_list|()
decl_stmt|;
name|o3
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"key3"
argument_list|,
literal|"v3"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerNode
argument_list|(
name|o3
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|o3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectsUnregistered
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"ArtistXYZ"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|object
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// insert object into the ObjectStore
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerNode
argument_list|(
name|oid
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|objectsUnregistered
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
comment|// in the future this may not be the case
name|assertNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUnregisterThenRegister
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// Create a gallery.
name|Gallery
name|g
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
name|g
operator|.
name|setGalleryName
argument_list|(
literal|"Test Gallery"
argument_list|)
expr_stmt|;
comment|// Create an artist in the same context.
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"Test Artist"
argument_list|)
expr_stmt|;
comment|// Create a painting in the same context.
name|Painting
name|p
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"Test Painting"
argument_list|)
expr_stmt|;
comment|// Set the painting's gallery.
name|p
operator|.
name|setToGallery
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|g
argument_list|,
name|p
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
comment|// Unregister the painting from the context.
name|context
operator|.
name|unregisterObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
comment|// Make sure that even though the painting has been removed from the context's
comment|// object graph that the reference to the gallery is the same.
name|assertEquals
argument_list|(
name|g
argument_list|,
name|p
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now, set the relationship between "p"& "a." Since "p" is not registered with a
comment|// context, but "a" is, "p" should be auto-registered with the context of "a."
name|p
operator|.
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// Now commit the gallery, artist,& painting.
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Check one last time that the painting's gallery is set to what we expect.
name|assertEquals
argument_list|(
name|g
argument_list|,
name|p
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now, retrieve the same painting from the DB. Note that the gallery relationship
comment|// is null even though according to our painting, that should not be the case; a
comment|// NULL
comment|// value has been recorded to the DB for the painting's gallery_id field.
comment|//
comment|// The full object graph is not being re-registered during auto-registration
comment|// with the context.
name|Painting
name|newP
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|newP
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

