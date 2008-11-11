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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|DeleteQuery
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
name|UpdateQuery
import|;
end_import

begin_comment
comment|/**  * Test suite covering possible scenarios of refreshing updated objects. This includes  * refreshing relationships and attributes changed outside of Cayenne with and witout  * prefetching.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataContextRefreshingTest
extends|extends
name|DataContextCase
block|{
specifier|public
name|void
name|testRefetchRootWithUpdatedAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|nameBefore
init|=
literal|"artist2"
decl_stmt|;
name|String
name|nameAfter
init|=
literal|"not an artist"
decl_stmt|;
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
name|nameBefore
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nameBefore
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_NAME"
argument_list|,
name|nameAfter
argument_list|)
expr_stmt|;
comment|// fetch into the same context
name|artist
operator|=
name|fetchArtist
argument_list|(
name|nameBefore
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|artist
operator|=
name|fetchArtist
argument_list|(
name|nameAfter
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nameAfter
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchRootWithNullifiedToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
name|fetchPainting
argument_list|(
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchRootWithChangedToOneTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|Artist
name|artistBefore
init|=
name|painting
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistBefore
argument_list|)
expr_stmt|;
name|Artist
name|artistAfter
init|=
name|fetchArtist
argument_list|(
literal|"artist3"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistAfter
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|artistBefore
argument_list|,
name|artistAfter
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|artistAfter
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
name|fetchPainting
argument_list|(
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistAfter
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchRootWithNullToOneTargetChangedToNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|artistAfter
init|=
name|fetchArtist
argument_list|(
literal|"artist3"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistAfter
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|artistAfter
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
name|fetchPainting
argument_list|(
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistAfter
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchRootWithDeletedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
name|painting
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|deleteRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|artist
operator|=
name|fetchArtist
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// select using relationship prefetching
name|artist
operator|=
name|fetchArtist
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchRootWithAddedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
literal|"artist2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"P2"
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|artist
operator|=
name|fetchArtist
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// select using relationship prefetching
name|artist
operator|=
name|fetchArtist
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateRootWithUpdatedAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|nameBefore
init|=
literal|"artist2"
decl_stmt|;
name|String
name|nameAfter
init|=
literal|"not an artist"
decl_stmt|;
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
name|nameBefore
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nameBefore
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_NAME"
argument_list|,
name|nameAfter
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nameAfter
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateRootWithNullifiedToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|painting
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateRootWithChangedToOneTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|Artist
name|artistBefore
init|=
name|painting
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistBefore
argument_list|)
expr_stmt|;
name|Artist
name|artistAfter
init|=
name|fetchArtist
argument_list|(
literal|"artist3"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistAfter
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|artistBefore
argument_list|,
name|artistAfter
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|artistAfter
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|painting
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistAfter
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateRootWithNullToOneTargetChangedToNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|artistAfter
init|=
name|fetchArtist
argument_list|(
literal|"artist3"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistAfter
argument_list|)
expr_stmt|;
comment|// update via DataNode directly
name|updateRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|artistAfter
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|painting
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistAfter
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateRootWithDeletedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|painting
init|=
name|insertPaintingInContext
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
name|painting
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|deleteRow
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvaliateRootWithAddedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
literal|"artist2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"P2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.0      */
specifier|public
name|void
name|testRefetchRootWithAddedToManyViaRefetchObject
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
literal|"artist2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"P2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|refetchObject
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidateThenModify
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|artist
init|=
name|fetchArtist
argument_list|(
literal|"artist2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// this must trigger a fetch
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"new name"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testModifyHollow
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"P2"
argument_list|)
expr_stmt|;
comment|// reset context
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
name|fetchPainting
argument_list|(
literal|"P_artist2"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
name|painting
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|artist
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistName"
argument_list|)
argument_list|)
expr_stmt|;
comment|// this must trigger a fetch
name|artist
operator|.
name|setDateOfBirth
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artist
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper method to update a single column in a database row.      */
specifier|protected
name|void
name|updateRow
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|String
name|dbAttribute
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|UpdateQuery
name|updateQuery
init|=
operator|new
name|UpdateQuery
argument_list|()
decl_stmt|;
name|updateQuery
operator|.
name|setRoot
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|updateQuery
operator|.
name|addUpdAttribute
argument_list|(
name|dbAttribute
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
comment|// set qualifier
name|updateQuery
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllDbExp
argument_list|(
name|id
operator|.
name|getIdSnapshot
argument_list|()
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
argument_list|)
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|updateQuery
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|deleteRow
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|DeleteQuery
name|deleteQuery
init|=
operator|new
name|DeleteQuery
argument_list|()
decl_stmt|;
name|deleteQuery
operator|.
name|setRoot
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|deleteQuery
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllDbExp
argument_list|(
name|id
operator|.
name|getIdSnapshot
argument_list|()
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
argument_list|)
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|deleteQuery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

