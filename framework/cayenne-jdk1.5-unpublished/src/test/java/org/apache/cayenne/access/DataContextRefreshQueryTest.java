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
name|Cayenne
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
name|query
operator|.
name|QueryCacheStrategy
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
name|RefreshQuery
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
name|query
operator|.
name|SortOrder
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
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextRefreshQueryTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testRefreshCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
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
name|assertEquals
argument_list|(
literal|2
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|artists
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|a1
operator|.
name|readProperty
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|a2
operator|.
name|readProperty
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshCollectionToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
name|paints
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|paints
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
name|p1
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollectionToOneUpdate"
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|paints
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|p2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|a1
argument_list|,
name|p1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|p1
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshSingleObject
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
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
literal|2
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
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|a1
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|a1
operator|.
name|readProperty
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshObjectToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshObjectToMany"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
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
literal|33001l
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshObjectToManyUpdate"
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a
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
name|testRefreshQueryResultsLocalCache
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
literal|"P2"
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
name|qual
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheGroups
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|List
name|paints
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
comment|// fetch P1 separately from cached query
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
literal|33001
argument_list|)
decl_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
name|p2
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollectionToOneUpdate"
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// probably refreshed eagerly
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p2
operator|.
name|getPersistenceState
argument_list|()
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
name|assertNotSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|p1
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
literal|"b"
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshQueryResultsSharedCache
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
literal|"P2"
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
name|qual
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheGroups
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|List
name|paints
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
comment|// fetch P1 separately from cached query
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
literal|33001
argument_list|)
decl_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
name|p2
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollectionToOneUpdate"
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// probably refreshed eagerly
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p2
operator|.
name|getPersistenceState
argument_list|()
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
name|assertNotSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|p1
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
literal|"b"
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshQueryResultGroupLocal
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
literal|"P2"
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
name|qual
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q
operator|.
name|setCacheGroups
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|List
name|paints
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
comment|// fetch P1 separately from cached query
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
literal|33001
argument_list|)
decl_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|paints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
name|p2
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollectionToOneUpdate"
argument_list|)
expr_stmt|;
comment|// results are served from cache and therefore are not refreshed
name|context
operator|.
name|performQuery
argument_list|(
name|q
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
name|assertSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|p1
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
literal|"c"
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
comment|// this should invalidate results for the next query run
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
comment|// this should force a refresh
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p2
operator|.
name|getPersistenceState
argument_list|()
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
name|assertNotSame
argument_list|(
name|a1
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|p1
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
literal|"b"
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshAll
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testRefreshCollection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
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
name|Painting
name|p1
init|=
name|a1
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|p2
init|=
name|a1
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RefreshQuery
name|refresh
init|=
operator|new
name|RefreshQuery
argument_list|()
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|refresh
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|p2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|p2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

