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
name|sql
operator|.
name|Timestamp
import|;
end_import

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
name|cayenne
operator|.
name|Fault
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
name|Persistent
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
name|query
operator|.
name|PrefetchTreeNode
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
name|DataContextPrefetchMultistepTest
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
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tExhibit
decl_stmt|;
specifier|protected
name|TableHelper
name|tGallery
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtistExhibit
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
name|tExhibit
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EXHIBIT"
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|setColumns
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|"GALLERY_ID"
argument_list|,
literal|"OPENING_DATE"
argument_list|,
literal|"CLOSING_DATE"
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"EXHIBIT_ID"
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
specifier|protected
name|void
name|createTwoArtistsWithExhibitsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|25
argument_list|,
literal|"gallery1"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|31
argument_list|,
literal|"gallery2"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|45
argument_list|,
literal|"gallery3"
argument_list|)
expr_stmt|;
name|Timestamp
name|now
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|25
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|31
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|45
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|25
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createGalleriesAndArtists
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|25
argument_list|,
literal|"gallery1"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|31
argument_list|,
literal|"gallery2"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|45
argument_list|,
literal|"gallery3"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyToManyFirstStepUnresolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsWithExhibitsDataSet
argument_list|()
expr_stmt|;
comment|// since objects for the phantom prefetches are not retained explicitly, they may
comment|// get garbage collected, and we won't be able to detect them
comment|// so ensure ObjectStore uses a regular map just for this test
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|objectMap
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
argument_list|()
expr_stmt|;
comment|// Check the target ArtistExhibit objects do not exist yet
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|id1
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|id1
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|id1
operator|.
name|put
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"ArtistExhibit"
argument_list|,
name|id1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|id2
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|id2
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|101
argument_list|)
expr_stmt|;
name|id2
operator|.
name|put
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"ArtistExhibit"
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid2
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"galleryName = $name"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|e
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
literal|"gallery2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray.artistExhibitArray"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship wasn't explicitly prefetched....
name|Object
name|list
init|=
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|list
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
comment|// however the target objects must be resolved
name|ArtistExhibit
name|ae1
init|=
operator|(
name|ArtistExhibit
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid1
argument_list|)
decl_stmt|;
name|ArtistExhibit
name|ae2
init|=
operator|(
name|ArtistExhibit
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|oid2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ae1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ae2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|ae1
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
name|ae2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyToManyFirstStepResolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsWithExhibitsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"galleryName = $name"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|e
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
literal|"gallery2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray.artistExhibitArray"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship should be resolved
name|assertTrue
argument_list|(
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exhibit
argument_list|>
name|exhibits
init|=
operator|(
name|List
argument_list|<
name|Exhibit
argument_list|>
operator|)
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|exhibits
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
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exhibit
name|e1
init|=
name|exhibits
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
name|e1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// this to-many must also be resolved
name|assertTrue
argument_list|(
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
name|aexhibits
init|=
operator|(
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
operator|)
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|aexhibits
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
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|ae1
init|=
name|aexhibits
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
name|ae1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMixedPrefetch1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsWithExhibitsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"galleryName = $name"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|e
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
literal|"gallery2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray"
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray.artistExhibitArray"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship should be resolved
name|assertTrue
argument_list|(
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exhibit
argument_list|>
name|exhibits
init|=
operator|(
name|List
argument_list|<
name|Exhibit
argument_list|>
operator|)
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|exhibits
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
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exhibit
name|e1
init|=
name|exhibits
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
name|e1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// this to-many must also be resolved
name|assertTrue
argument_list|(
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
name|aexhibits
init|=
operator|(
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
operator|)
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|aexhibits
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
name|aexhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|ae1
init|=
name|aexhibits
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
name|ae1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMixedPrefetch2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsWithExhibitsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"galleryName = $name"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|e
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
literal|"gallery2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// reverse the order of prefetches compared to the previous test
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"exhibitArray.artistExhibitArray"
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship should be resolved
name|assertTrue
argument_list|(
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exhibit
argument_list|>
name|exhibits
init|=
operator|(
name|List
argument_list|<
name|Exhibit
argument_list|>
operator|)
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"exhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|exhibits
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
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exhibit
name|e1
init|=
name|exhibits
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
name|e1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// this to-many must also be resolved
name|assertTrue
argument_list|(
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
name|aexhibits
init|=
operator|(
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
operator|)
name|e1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|aexhibits
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
name|aexhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtistExhibit
name|ae1
init|=
name|aexhibits
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
name|ae1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyToOne_EmptyToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createGalleriesAndArtists
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|,
name|Gallery
operator|.
name|GALLERY_NAME
operator|.
name|eq
argument_list|(
literal|"gallery2"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
argument_list|)
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship should be resolved
name|assertTrue
argument_list|(
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|exhibits
init|=
operator|(
name|List
argument_list|<
name|Painting
argument_list|>
operator|)
name|g2
operator|.
name|readPropertyDirectly
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|exhibits
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyToOne_EmptyToMany_NoRootQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createGalleriesAndArtists
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Gallery
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
argument_list|)
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
init|=
name|context
operator|.
name|select
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g
init|=
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// this relationship should be resolved
name|assertTrue
argument_list|(
name|g
operator|.
name|readPropertyDirectly
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|exhibits
init|=
operator|(
name|List
argument_list|<
name|Painting
argument_list|>
operator|)
name|g
operator|.
name|readPropertyDirectly
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|exhibits
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|exhibits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

