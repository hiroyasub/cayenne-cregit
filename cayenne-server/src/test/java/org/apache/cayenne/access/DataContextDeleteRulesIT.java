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
name|assertFalse
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
name|assertNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|PersistenceState
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
name|query
operator|.
name|SQLTemplate
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|ArtGroup
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
name|CayenneProjects
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

begin_comment
comment|// TODO: redefine all test cases in terms of entities in "relationships" map
end_comment

begin_comment
comment|// and merge this test case with DeleteRulesTst that inherits
end_comment

begin_comment
comment|// from RelationshipTestCase.
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextDeleteRulesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|cleanUpDB
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|update
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|set
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|super
operator|.
name|cleanUpDB
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullifyToOne
parameter_list|()
block|{
comment|// ArtGroup toParentGroup
name|ArtGroup
name|parentGroup
init|=
operator|(
name|ArtGroup
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup
operator|.
name|setName
argument_list|(
literal|"Parent"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup
init|=
operator|(
name|ArtGroup
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup
operator|.
name|setName
argument_list|(
literal|"Child"
argument_list|)
expr_stmt|;
name|parentGroup
operator|.
name|addToChildGroupsArray
argument_list|(
name|childGroup
argument_list|)
expr_stmt|;
comment|// Check to make sure that the relationships are both exactly correct
comment|// before starting. We're not really testing this, but it is imperative
comment|// that it is correct before testing the real details.
name|assertEquals
argument_list|(
name|parentGroup
argument_list|,
name|childGroup
operator|.
name|getToParentGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|parentGroup
operator|.
name|getChildGroupsArray
argument_list|()
operator|.
name|contains
argument_list|(
name|childGroup
argument_list|)
argument_list|)
expr_stmt|;
comment|// Always good to commit before deleting... bad things happen otherwise
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|childGroup
argument_list|)
expr_stmt|;
comment|// The things we are testing.
name|assertFalse
argument_list|(
name|parentGroup
operator|.
name|getChildGroupsArray
argument_list|()
operator|.
name|contains
argument_list|(
name|childGroup
argument_list|)
argument_list|)
expr_stmt|;
comment|// Although deleted, the property should be null (good cleanup policy)
comment|// assertNull(childGroup.getToParentGroup());
comment|// And be sure that the commit works afterwards, just for sanity
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Tests that deleting a source of a flattened relationship with CASCADE 	 * rule results in deleting a join and a target. 	 */
annotation|@
name|Test
specifier|public
name|void
name|testCascadeToManyFlattened
parameter_list|()
block|{
comment|// testing Artist.groupArray relationship
name|ArtGroup
name|aGroup
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|)
decl_stmt|;
name|aGroup
operator|.
name|setName
argument_list|(
literal|"Group Name"
argument_list|)
expr_stmt|;
name|Artist
name|anArtist
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
name|anArtist
operator|.
name|setArtistName
argument_list|(
literal|"A Name"
argument_list|)
expr_stmt|;
name|anArtist
operator|.
name|addToGroupArray
argument_list|(
name|aGroup
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|anArtist
operator|.
name|getGroupArray
argument_list|()
operator|.
name|contains
argument_list|(
name|aGroup
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SQLTemplate
name|checkQuery
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST_GROUP"
argument_list|)
decl_stmt|;
name|checkQuery
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|joins1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|checkQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|joins1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|anArtist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|aGroup
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|anArtist
operator|.
name|getGroupArray
argument_list|()
operator|.
name|contains
argument_list|(
name|aGroup
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|joins2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|checkQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|joins2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Tests that deleting a source of a flattened relationship with NULLIFY 	 * rule results in deleting a join together with the object deleted. 	 */
annotation|@
name|Test
specifier|public
name|void
name|testNullifyToManyFlattened
parameter_list|()
block|{
comment|// testing ArtGroup.artistArray relationship
name|ArtGroup
name|aGroup
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|)
decl_stmt|;
name|aGroup
operator|.
name|setName
argument_list|(
literal|"Group Name"
argument_list|)
expr_stmt|;
name|Artist
name|anArtist
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
name|anArtist
operator|.
name|setArtistName
argument_list|(
literal|"A Name"
argument_list|)
expr_stmt|;
name|aGroup
operator|.
name|addToArtistArray
argument_list|(
name|anArtist
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Preconditions
name|assertTrue
argument_list|(
name|aGroup
operator|.
name|getArtistArray
argument_list|()
operator|.
name|contains
argument_list|(
name|anArtist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|anArtist
operator|.
name|getGroupArray
argument_list|()
operator|.
name|contains
argument_list|(
name|aGroup
argument_list|)
argument_list|)
expr_stmt|;
name|SQLTemplate
name|checkQuery
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST_GROUP"
argument_list|)
decl_stmt|;
name|checkQuery
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|joins1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|checkQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|joins1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|aGroup
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|anArtist
operator|.
name|getGroupArray
argument_list|()
operator|.
name|contains
argument_list|(
name|aGroup
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|joins2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|checkQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|joins2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullifyToMany
parameter_list|()
block|{
comment|// ArtGroup childGroupsArray
name|ArtGroup
name|parentGroup
init|=
operator|(
name|ArtGroup
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup
operator|.
name|setName
argument_list|(
literal|"Parent"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup
init|=
operator|(
name|ArtGroup
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup
operator|.
name|setName
argument_list|(
literal|"Child"
argument_list|)
expr_stmt|;
name|parentGroup
operator|.
name|addToChildGroupsArray
argument_list|(
name|childGroup
argument_list|)
expr_stmt|;
comment|// Preconditions - good to check to be sure
name|assertEquals
argument_list|(
name|parentGroup
argument_list|,
name|childGroup
operator|.
name|getToParentGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|parentGroup
operator|.
name|getChildGroupsArray
argument_list|()
operator|.
name|contains
argument_list|(
name|childGroup
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
comment|// The things we are testing.
name|assertNull
argument_list|(
name|childGroup
operator|.
name|getToParentGroup
argument_list|()
argument_list|)
expr_stmt|;
comment|// Although deleted, the property should be null (good cleanup policy)
comment|// assertFalse(parentGroup.getChildGroupsArray().contains(childGroup));
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCascadeToOne
parameter_list|()
block|{
comment|// Painting toPaintingInfo
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setPaintingTitle
argument_list|(
literal|"A Title"
argument_list|)
expr_stmt|;
name|PaintingInfo
name|info
init|=
operator|(
name|PaintingInfo
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"PaintingInfo"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setToPaintingInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
comment|// Must commit before deleting.. this relationship is dependent,
comment|// and everything must be committed for certain things to work
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|painting
argument_list|)
expr_stmt|;
comment|// info must also be deleted
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|info
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|info
operator|.
name|getPainting
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToPaintingInfo
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCascadeToMany
parameter_list|()
block|{
comment|// Artist artistExhibitArray
name|Artist
name|anArtist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|anArtist
operator|.
name|setArtistName
argument_list|(
literal|"A Name"
argument_list|)
expr_stmt|;
name|Exhibit
name|anExhibit
init|=
operator|(
name|Exhibit
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Exhibit"
argument_list|)
decl_stmt|;
name|anExhibit
operator|.
name|setClosingDate
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|anExhibit
operator|.
name|setOpeningDate
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Needs a gallery... required for data integrity
name|Gallery
name|gallery
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|gallery
operator|.
name|setGalleryName
argument_list|(
literal|"A Name"
argument_list|)
expr_stmt|;
name|anExhibit
operator|.
name|setToGallery
argument_list|(
name|gallery
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|artistExhibit
init|=
operator|(
name|ArtistExhibit
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ArtistExhibit"
argument_list|)
decl_stmt|;
name|artistExhibit
operator|.
name|setToArtist
argument_list|(
name|anArtist
argument_list|)
expr_stmt|;
name|artistExhibit
operator|.
name|setToExhibit
argument_list|(
name|anExhibit
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|anArtist
argument_list|)
expr_stmt|;
comment|// Test that the link record was deleted, and removed from the
comment|// relationship
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|artistExhibit
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|anArtist
operator|.
name|getArtistExhibitArray
argument_list|()
operator|.
name|contains
argument_list|(
name|artistExhibit
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDenyToMany
parameter_list|()
block|{
comment|// Gallery paintingArray
name|Gallery
name|gallery
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|gallery
operator|.
name|setGalleryName
argument_list|(
literal|"A Name"
argument_list|)
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setPaintingTitle
argument_list|(
literal|"A Title"
argument_list|)
expr_stmt|;
name|gallery
operator|.
name|addToPaintingArray
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|deleteObjects
argument_list|(
name|gallery
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// GOOD!
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

