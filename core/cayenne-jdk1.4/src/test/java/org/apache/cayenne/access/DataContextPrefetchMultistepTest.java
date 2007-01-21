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
name|Exhibit
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

begin_comment
comment|/**  * Testing chained prefetches...  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextPrefetchMultistepTest
extends|extends
name|DataContextCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
block|}
specifier|public
name|void
name|testToManyToManyFirstStepUnresolved
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Check the target ArtistExhibit objects do not exist yet
name|Map
name|id1
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|id1
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33001
argument_list|)
argument_list|)
expr_stmt|;
name|id1
operator|.
name|put
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
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
name|id2
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|id2
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33003
argument_list|)
argument_list|)
expr_stmt|;
name|id2
operator|.
name|put
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
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
name|q
init|=
operator|new
name|SelectQuery
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
name|galleries
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
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
operator|(
name|Gallery
operator|)
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
name|q
init|=
operator|new
name|SelectQuery
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
name|galleries
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
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
operator|(
name|Gallery
operator|)
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
name|exhibits
init|=
operator|(
name|List
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
operator|(
name|Exhibit
operator|)
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
name|aexhibits
init|=
operator|(
name|List
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
operator|(
name|ArtistExhibit
operator|)
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
block|{
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
name|q
init|=
operator|new
name|SelectQuery
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
name|galleries
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
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
operator|(
name|Gallery
operator|)
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
name|exhibits
init|=
operator|(
name|List
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
operator|(
name|Exhibit
operator|)
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
name|aexhibits
init|=
operator|(
name|List
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
operator|(
name|ArtistExhibit
operator|)
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
block|{
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
name|q
init|=
operator|new
name|SelectQuery
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
name|galleries
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
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Gallery
name|g2
init|=
operator|(
name|Gallery
operator|)
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
name|exhibits
init|=
operator|(
name|List
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
operator|(
name|Exhibit
operator|)
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
name|aexhibits
init|=
operator|(
name|List
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
operator|(
name|ArtistExhibit
operator|)
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
block|}
end_class

end_unit

