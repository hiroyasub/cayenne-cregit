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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ArtGroup
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
name|ArtistExhibit
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
name|art
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
name|CayenneDataObject
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
name|DataObjectUtils
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
name|ValueHolder
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
name|map
operator|.
name|ObjRelationship
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
name|Ordering
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
name|QueryMetadata
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextPrefetchTest
extends|extends
name|DataContextCase
block|{
comment|/**      * Test that a to-many relationship is initialized.      */
specifier|public
name|void
name|testPrefetchToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"name1"
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"name2"
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"artistName = $name1 or artistName = $name2"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|a1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P_"
operator|+
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|p1
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
name|toMany2
init|=
operator|(
name|List
operator|)
name|a2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany2
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|toMany2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|toMany2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P_"
operator|+
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|p2
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-many relationship is initialized.      */
specifier|public
name|void
name|testPrefetchToManyNoQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|artistCount
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artistCount
condition|;
name|i
operator|++
control|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|a
operator|.
name|readPropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid prefetched painting:"
operator|+
name|p
argument_list|,
literal|"P_"
operator|+
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-many relationship is initialized when a target entity has a compound      * PK only partially involved in relationship.      */
specifier|public
name|void
name|testPrefetchToManyOnJoinTable
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup data
name|createTestData
argument_list|(
literal|"testGalleries"
argument_list|)
expr_stmt|;
name|populateExhibits
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testArtistExhibits"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"artistExhibitArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|Ordering
operator|.
name|ASC
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|artistCount
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist1"
argument_list|,
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|a1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|artistExhibit
init|=
operator|(
name|ArtistExhibit
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|artistExhibit
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|artistExhibit
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-many relationship is initialized when there is no inverse      * relationship      */
specifier|public
name|void
name|testPrefetch3a
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|ObjEntity
name|paintingEntity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|paintingEntity
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|paintingEntity
operator|.
name|removeRelationship
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|a1
init|=
operator|(
name|CayenneDataObject
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|a1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Fix it up again, so other tests do not fail
name|paintingEntity
operator|.
name|addRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-many relationship is initialized when there is no inverse      * relationship and the root query is qualified      */
specifier|public
name|void
name|testPrefetchOneWayToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|ObjEntity
name|paintingEntity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|paintingEntity
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|paintingEntity
operator|.
name|removeRelationship
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|a1
init|=
operator|(
name|CayenneDataObject
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|a1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Fix it up again, so other tests do not fail
name|paintingEntity
operator|.
name|addRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-one relationship is initialized.      */
specifier|public
name|void
name|testPrefetch4
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|List
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|p1
init|=
operator|(
name|DataObject
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|toOnePrefetch
init|=
name|p1
operator|.
name|readNestedProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toOnePrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected DataObject, got: "
operator|+
name|toOnePrefetch
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|toOnePrefetch
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|DataObject
name|a1
init|=
operator|(
name|DataObject
operator|)
name|toOnePrefetch
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test prefetching with queries using DB_PATH.      */
specifier|public
name|void
name|testPrefetch5
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
literal|"toArtist.ARTIST_NAME"
argument_list|,
literal|"artist2"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test prefetching with queries using OBJ_PATH.      */
specifier|public
name|void
name|testPrefetch6
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toArtist.artistName"
argument_list|,
literal|"artist2"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test prefetching with the prefetch on a reflexive relationship      */
specifier|public
name|void
name|testPrefetch7
parameter_list|()
throws|throws
name|Exception
block|{
name|ArtGroup
name|parent
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
name|parent
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|ArtGroup
name|child
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
name|child
operator|.
name|setName
argument_list|(
literal|"child"
argument_list|)
expr_stmt|;
name|child
operator|.
name|setToParentGroup
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"child"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toParentGroup"
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtGroup
name|fetchedChild
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// The parent must be fully fetched, not just HOLLOW (a fault)
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|fetchedChild
operator|.
name|getToParentGroup
argument_list|()
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test prefetching with qualifier on the root query containing the path to the      * prefetch.      */
specifier|public
name|void
name|testPrefetch8
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toArtist.artistName"
argument_list|,
literal|"artist1"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|exp
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// The parent must be fully fetched, not just HOLLOW (a fault)
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Test prefetching with qualifier on the root query being the path to the prefetch.      */
specifier|public
name|void
name|testPrefetch9
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|Expression
name|artistExp
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1"
argument_list|)
decl_stmt|;
name|SelectQuery
name|artistQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|artistExp
argument_list|)
decl_stmt|;
name|Artist
name|artist1
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|artistQuery
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// find the painting not matching the artist (this is the case where such prefetch
comment|// at least makes sense)
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"toArtist"
argument_list|,
name|artist1
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|exp
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
comment|// run the query ... see that it doesn't blow
name|List
name|paintings
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// see that artists are resolved...
name|Painting
name|px
init|=
operator|(
name|Painting
operator|)
name|paintings
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Artist
name|ax
init|=
operator|(
name|Artist
operator|)
name|px
operator|.
name|readProperty
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|ax
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPrefetchOneToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintingInfos"
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"toArtist.artistName"
argument_list|,
literal|"a%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_PAINTING_INFO_PROPERTY
argument_list|)
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// testing non-null to-one target
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
name|o2
init|=
name|p2
operator|.
name|readPropertyDirectly
argument_list|(
name|Painting
operator|.
name|TO_PAINTING_INFO_PROPERTY
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|o2
operator|instanceof
name|PaintingInfo
argument_list|)
expr_stmt|;
name|PaintingInfo
name|pi2
init|=
operator|(
name|PaintingInfo
operator|)
name|o2
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|pi2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|p2
argument_list|)
argument_list|,
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|pi2
argument_list|)
argument_list|)
expr_stmt|;
comment|// testing null to-one target
name|Painting
name|p4
init|=
operator|(
name|Painting
operator|)
name|results
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|p4
operator|.
name|readPropertyDirectly
argument_list|(
name|Painting
operator|.
name|TO_PAINTING_INFO_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCAY119
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"dateOfBirth"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
comment|// prefetch with query using date in qualifier used to fail on SQL Server
comment|// see CAY-119 for details
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPrefetchingToOneNull
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|p1
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
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"aaaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
comment|// run the query ... see that it doesn't blow
name|List
name|paintings
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|paintings
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|p2
operator|.
name|readProperty
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPrefetchToOneSharedCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setName
argument_list|(
literal|"__testPrefetchToOneSharedCache__"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
comment|// per CAY-499 second run of a cached query with prefetches (i.e. when the
comment|// result is served from cache) used to throw an exception...
name|List
name|cachedResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|cachedResult
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|p1
init|=
operator|(
name|DataObject
operator|)
name|cachedResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|toOnePrefetch
init|=
name|p1
operator|.
name|readNestedProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toOnePrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected DataObject, got: "
operator|+
name|toOnePrefetch
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|toOnePrefetch
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|DataObject
name|a1
init|=
operator|(
name|DataObject
operator|)
name|toOnePrefetch
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// and just in case - run one more time...
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPrefetchToOneLocalCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setName
argument_list|(
literal|"__testPrefetchToOneLocalCache__"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
comment|// per CAY-499 second run of a cached query with prefetches (i.e. when the
comment|// result is served from cache) used to throw an exception...
name|List
name|cachedResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|cachedResult
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|p1
init|=
operator|(
name|DataObject
operator|)
name|cachedResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|toOnePrefetch
init|=
name|p1
operator|.
name|readNestedProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toOnePrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected DataObject, got: "
operator|+
name|toOnePrefetch
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|toOnePrefetch
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|DataObject
name|a1
init|=
operator|(
name|DataObject
operator|)
name|toOnePrefetch
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// and just in case - run one more time...
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

