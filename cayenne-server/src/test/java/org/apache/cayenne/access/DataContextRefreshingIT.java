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
name|di
operator|.
name|DataChannelInterceptor
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
name|UnitTestClosure
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
name|Date
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

begin_comment
comment|/**  * Test suite covering possible scenarios of refreshing updated objects. This includes  * refreshing relationships and attributes changed outside of Cayenne with and without  * prefetching.  */
end_comment

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
name|DataContextRefreshingIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
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
literal|"ESTIMATED_PRICE"
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
name|DECIMAL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSingleArtistDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSingleArtistAndPaintingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"p"
argument_list|,
literal|5
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSingleArtistAndUnrelatedPaintingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"p"
argument_list|,
literal|null
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTwoArtistsAndPaintingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"p"
argument_list|,
literal|5
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithUpdatedAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
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
name|SelectQuery
name|queryBefore
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|nameBefore
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|queryBefore
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tArtist
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
name|nameAfter
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
comment|// fetch into the same context
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
name|queryBefore
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|queryAfter
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|nameAfter
argument_list|)
argument_list|)
decl_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|queryAfter
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithNullifiedToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithChangedToOneTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|artistBefore
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|6
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithNullToOneTargetChangedToNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndUnrelatedPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|5
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|painting
operator|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithDeletedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|delete
argument_list|()
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|PAINTING_ID_PK_COLUMN
argument_list|,
literal|4
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
annotation|@
name|Test
specifier|public
name|void
name|testRefetchRootWithAddedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|"p"
argument_list|,
literal|5
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
comment|// select without prefetch
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|query
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateRootWithUpdatedAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
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
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tArtist
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
name|nameAfter
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
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
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateRootWithNullifiedToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
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
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateRootWithChangedToOneTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|artistBefore
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|6
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|artistBefore
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateRootWithNullToOneTargetChangedToNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndUnrelatedPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|5
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|painting
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateRootWithDeletedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tPainting
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|artist
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
annotation|@
name|Test
specifier|public
name|void
name|testInvaliateRootWithAddedToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"p"
argument_list|,
literal|5
argument_list|,
literal|1000
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
name|artist
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
annotation|@
name|Test
specifier|public
name|void
name|testInvalidateThenModify
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistDataSet
argument_list|()
expr_stmt|;
specifier|final
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
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
name|artist
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
name|int
name|queries
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
comment|// this must trigger a fetch
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"new name"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queries
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
annotation|@
name|Test
specifier|public
name|void
name|testModifyHollow
parameter_list|()
throws|throws
name|Exception
block|{
name|createSingleArtistAndPaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
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
name|int
name|queries
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
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
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queries
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
block|}
end_class

end_unit

