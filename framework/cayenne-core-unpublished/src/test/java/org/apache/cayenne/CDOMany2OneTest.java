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
name|cayenne
operator|.
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|CapsStrategy
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
name|query
operator|.
name|SelectQuery
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|ROPainting
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
name|CDOMany2OneTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
decl_stmt|;
specifier|protected
name|TableHelper
name|tGallery
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
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GALLERY"
argument_list|)
expr_stmt|;
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"GALLERY_ID"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
name|tGallery
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"GALLERY"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|setColumns
argument_list|(
literal|"GALLERY_ID"
argument_list|,
literal|"GALLERY_NAME"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createArtistWithPaintingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"aX"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"pW"
argument_list|,
literal|8
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createArtistWithPaintingsInGalleryDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"aX"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"Ge"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"pW1"
argument_list|,
literal|8
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"pW2"
argument_list|,
literal|8
argument_list|,
literal|11
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleToOneDeletion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// was a problem per CAY-901
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
literal|"P1"
argument_list|)
expr_stmt|;
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
literal|"A1"
argument_list|)
expr_stmt|;
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
literal|"G1"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToGallery
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|p
operator|.
name|setToArtist
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToGallery
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SQLTemplate
name|q
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * from PAINTING"
argument_list|)
decl_stmt|;
name|q
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|row
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P1"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"GALLERY_ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadRO1
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistWithPaintingDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|ROPainting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|a1
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ROPainting
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ROPainting
argument_list|>
name|paints
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
name|paints
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ROPainting
name|rop1
init|=
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|rop1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadRO2
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistWithPaintingDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|ROPainting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|a1
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ROPainting
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ROPainting
argument_list|>
name|paints
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
name|paints
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ROPainting
name|rop1
init|=
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rop1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
comment|// trigger fetch
name|rop1
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|rop1
operator|.
name|getToArtist
argument_list|()
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectViaRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistWithPaintingDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|Painting
name|p1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|a1
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
name|List
argument_list|<
name|Painting
argument_list|>
name|paints
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
name|paints
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p1
argument_list|,
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectViaMultiRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistWithPaintingsInGalleryDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|Gallery
name|g1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Gallery
operator|.
name|class
argument_list|,
literal|11
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingArray.toGallery"
argument_list|,
name|g1
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
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
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
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewAdd
parameter_list|()
throws|throws
name|Exception
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
literal|"bL"
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
literal|"xa"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|p1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p1
argument_list|,
name|a1
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a1
argument_list|)
argument_list|,
name|tArtist
operator|.
name|getLong
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a1
argument_list|)
argument_list|,
name|tPainting
operator|.
name|getLong
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemove
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
literal|"xa"
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
decl_stmt|;
name|g1
operator|.
name|setGalleryName
argument_list|(
literal|"yT"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToGallery
argument_list|(
name|g1
argument_list|)
expr_stmt|;
comment|// do save
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
comment|// test database data
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context2
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Gallery
name|g2
init|=
name|p2
operator|.
name|getToGallery
argument_list|()
decl_stmt|;
name|p2
operator|.
name|setToGallery
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// test before save
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|p2
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save II
name|context2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context3
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Painting
name|p3
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context3
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|p3
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReplace
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
literal|"xa"
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
decl_stmt|;
name|g1
operator|.
name|setGalleryName
argument_list|(
literal|"yTW"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToGallery
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
comment|// test database data
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context2
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Gallery
name|g21
init|=
name|p2
operator|.
name|getToGallery
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|g21
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yTW"
argument_list|,
name|g21
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|g21
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p2
argument_list|,
name|g21
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Gallery
name|g22
init|=
name|context2
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|g22
operator|.
name|setGalleryName
argument_list|(
literal|"rE"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToGallery
argument_list|(
name|g22
argument_list|)
expr_stmt|;
comment|// test before save
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g21
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|g22
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p2
argument_list|,
name|g22
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// do save II
name|context2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context3
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Painting
name|p3
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context3
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Gallery
name|g3
init|=
name|p3
operator|.
name|getToGallery
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|g3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rE"
argument_list|,
name|g3
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|g3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p3
argument_list|,
name|g3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSavedAdd
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
literal|"xa"
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
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
comment|// test database data
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context2
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|p2
operator|.
name|getToGallery
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|context2
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|g2
operator|.
name|setGalleryName
argument_list|(
literal|"rE"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToGallery
argument_list|(
name|g2
argument_list|)
expr_stmt|;
comment|// test before save
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|g2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p2
argument_list|,
name|g2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// do save II
name|context2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context3
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Painting
name|p3
init|=
operator|(
name|Painting
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context3
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Gallery
name|g3
init|=
name|p3
operator|.
name|getToGallery
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|g3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rE"
argument_list|,
name|g3
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|g3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p3
argument_list|,
name|g3
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
