begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
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
name|ObjectContext
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
name|ObjectSelect
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
name|Award
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
name|unit
operator|.
name|UnitDbAdapter
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
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionFactory
operator|.
name|exp
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionFactory
operator|.
name|greaterExp
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|lengthExp
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|substringExp
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|trimExp
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
name|assertTrue
import|;
end_import

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
name|ExpressionFactoryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
comment|// CAY-416
annotation|@
name|Test
specifier|public
name|void
name|testCollectionMatch
parameter_list|()
block|{
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"artist"
argument_list|)
expr_stmt|;
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
decl_stmt|,
name|p2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|,
name|p3
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
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p3
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
comment|// changed to align with SQL
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p3
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
comment|// changed to align with SQL
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"p1"
argument_list|)
operator|.
name|match
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExpressionFactory
operator|.
name|notInExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"p3"
argument_list|)
operator|.
name|match
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIn
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
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
literal|"p1"
argument_list|)
expr_stmt|;
name|Painting
name|p2
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
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|Expression
name|in
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"paintingArray"
argument_list|,
name|p1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|match
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEscapeCharacter
parameter_list|()
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsEscapeInLike
argument_list|()
condition|)
block|{
return|return;
block|}
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"A_1"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"A_2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|ex1
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseDbExp
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"A*_1"
argument_list|,
literal|'*'
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex1
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|ex2
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"A*_2"
argument_list|,
literal|'*'
argument_list|)
decl_stmt|;
name|artists
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex2
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
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
name|testContains_Escape
parameter_list|()
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsEscapeInLike
argument_list|()
condition|)
block|{
return|return;
block|}
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"MA_1X"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"CA%2Y"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|ex1
init|=
name|ExpressionFactory
operator|.
name|containsExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|,
literal|"A_1"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex1
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|ex2
init|=
name|ExpressionFactory
operator|.
name|containsExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getName
argument_list|()
argument_list|,
literal|"A%2"
argument_list|)
decl_stmt|;
name|artists
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ex2
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
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
name|testSplitExpressions
parameter_list|()
block|{
name|Artist
name|artist1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|,
name|artist2
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
name|artist1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
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
decl_stmt|,
name|p2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|,
name|p3
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
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
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
decl_stmt|,
name|g2
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
literal|"g1"
argument_list|)
expr_stmt|;
name|g2
operator|.
name|setGalleryName
argument_list|(
literal|"g2"
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|addToPaintingArray
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|g1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|g1
operator|.
name|addToPaintingArray
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|g2
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|objArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"|paintingArray.toGallery.galleryName"
argument_list|,
literal|"g1"
argument_list|,
literal|"g2"
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|dbArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"db:|paintingArray.toGallery.GALLERY_NAME"
argument_list|,
literal|"g1"
argument_list|,
literal|"g2"
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|objArtists
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objArtists
argument_list|,
name|dbArtists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplitExpressions_EndsWithRelationship
parameter_list|()
block|{
name|Artist
name|artist1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|,
name|artist2
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
name|artist1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
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
decl_stmt|,
name|p2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|,
name|p3
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
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToPaintingArray
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|addToPaintingArray
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|objArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"|paintingArray"
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|dbArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"db:|paintingArray"
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|objArtists
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objArtists
argument_list|,
name|dbArtists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplitExpressions_EndsWithRelationship_DifferentObjDbPath
parameter_list|()
block|{
name|Artist
name|artist1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|,
name|artist2
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
name|artist1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|Award
name|aw1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Award
operator|.
name|class
argument_list|)
decl_stmt|,
name|aw2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Award
operator|.
name|class
argument_list|)
decl_stmt|,
name|aw3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Award
operator|.
name|class
argument_list|)
decl_stmt|;
name|aw1
operator|.
name|setName
argument_list|(
literal|"aw1"
argument_list|)
expr_stmt|;
name|aw2
operator|.
name|setName
argument_list|(
literal|"aw2"
argument_list|)
expr_stmt|;
name|aw3
operator|.
name|setName
argument_list|(
literal|"aw3"
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToAwardArray
argument_list|(
name|aw1
argument_list|)
expr_stmt|;
name|artist1
operator|.
name|addToAwardArray
argument_list|(
name|aw2
argument_list|)
expr_stmt|;
name|artist2
operator|.
name|addToAwardArray
argument_list|(
name|aw3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|objArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"|awardArray"
argument_list|,
name|aw1
argument_list|,
name|aw2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|dbArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"db:|artistAwardArray"
argument_list|,
name|aw1
argument_list|,
name|aw2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|objArtists
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objArtists
argument_list|,
name|dbArtists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplitExpressions_EndsWithRelationship_Flattened
parameter_list|()
block|{
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|,
name|a2
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|ArtGroup
name|ag1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|)
decl_stmt|,
name|ag2
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
name|ag1
operator|.
name|setName
argument_list|(
literal|"ag1"
argument_list|)
expr_stmt|;
name|ag2
operator|.
name|setName
argument_list|(
literal|"ag2"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|ag1
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToGroupArray
argument_list|(
name|ag1
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToGroupArray
argument_list|(
name|ag2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|objArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"|groupArray"
argument_list|,
name|ag1
argument_list|,
name|ag2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|dbArtists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"db:|artistGroupArray.toGroup"
argument_list|,
name|ag1
argument_list|,
name|ag2
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|objArtists
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objArtists
argument_list|,
name|dbArtists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDifferentExpressionAPI
parameter_list|()
block|{
name|List
argument_list|<
name|Artist
argument_list|>
name|res
decl_stmt|;
comment|// First version via expression string
name|Expression
name|exp1
init|=
name|exp
argument_list|(
literal|"length(substring(artistName, 1, 3))> length(trim(artistName))"
argument_list|)
decl_stmt|;
name|res
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|exp1
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|res
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Second version via FunctionExpressionFactory API
name|Expression
name|exp2
init|=
name|greaterExp
argument_list|(
name|lengthExp
argument_list|(
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|,
name|lengthExp
argument_list|(
name|trimExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|res
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|exp2
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|res
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Third version via Property API
name|Expression
name|exp3
init|=
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
operator|.
name|length
argument_list|()
operator|.
name|gt
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|res
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|exp3
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|res
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check that all expressions are equal
name|assertEquals
argument_list|(
name|exp1
argument_list|,
name|exp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp3
argument_list|,
name|exp3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

