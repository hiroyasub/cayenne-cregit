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
name|DataContextRollbackTest
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
name|ServerRuntime
name|serverRuntime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
block|}
specifier|public
name|void
name|testRollbackNew
parameter_list|()
block|{
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|Painting
name|p1
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
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|Painting
name|p2
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
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToArtist
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|Painting
name|p3
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
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setToArtist
argument_list|(
name|artist
argument_list|)
expr_stmt|;
comment|// before:
name|assertEquals
argument_list|(
name|artist
argument_list|,
name|p1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
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
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
comment|// after:
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
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
name|testRollbackNewObject
parameter_list|()
block|{
name|String
name|artistName
init|=
literal|"revertTestArtist"
decl_stmt|;
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
name|artistName
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// The commit should have made no changes, so
comment|// perform a fetch to ensure that this artist hasn't been persisted to the db
name|DataContext
name|freshContext
init|=
operator|(
name|DataContext
operator|)
name|serverRuntime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|freshContext
argument_list|)
expr_stmt|;
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
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|queryResults
init|=
name|freshContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queryResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Catches a bug where new objects were unregistered within an object iterator, thus
comment|// modifying the collection the iterator was iterating over
comment|// (ConcurrentModificationException)
specifier|public
name|void
name|testRollbackWithMultipleNewObjects
parameter_list|()
block|{
name|String
name|artistName
init|=
literal|"rollbackTestArtist"
decl_stmt|;
name|String
name|paintingTitle
init|=
literal|"rollbackTestPainting"
decl_stmt|;
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
name|artistName
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
name|paintingTitle
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// The commit should have made no changes, so
comment|// perform a fetch to ensure that this artist hasn't been persisted to the db
name|DataContext
name|freshContext
init|=
operator|(
name|DataContext
operator|)
name|serverRuntime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|freshContext
argument_list|)
expr_stmt|;
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
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|queryResults
init|=
name|freshContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queryResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRollbackRelationshipModification
parameter_list|()
block|{
name|String
name|artistName
init|=
literal|"relationshipModArtist"
decl_stmt|;
name|String
name|paintingTitle
init|=
literal|"relationshipTestPainting"
decl_stmt|;
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
name|artistName
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
name|paintingTitle
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
literal|null
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
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|artist
operator|.
name|getPaintingArray
argument_list|()
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
name|artist
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
name|artist
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check that the reverse relationship was handled
name|assertEquals
argument_list|(
literal|1
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
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|DataContext
name|freshContext
init|=
operator|(
name|DataContext
operator|)
name|serverRuntime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|freshContext
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingTitle"
argument_list|,
name|paintingTitle
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|queryResults
init|=
name|freshContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queryResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|queriedPainting
init|=
operator|(
name|Painting
operator|)
name|queryResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// NB: This is an easier comparison than manually fetching artist
name|assertEquals
argument_list|(
name|artistName
argument_list|,
name|queriedPainting
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
name|testRollbackDeletedObject
parameter_list|()
block|{
name|String
name|artistName
init|=
literal|"deleteTestArtist"
decl_stmt|;
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
name|artistName
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
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
comment|// Now check everything is as it should be
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
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// The commit should have made no changes, so
comment|// perform a fetch to ensure that this artist hasn't been deleted from the db
name|DataContext
name|freshContext
init|=
operator|(
name|DataContext
operator|)
name|serverRuntime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|freshContext
argument_list|)
expr_stmt|;
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
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|queryResults
init|=
name|freshContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queryResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRollbackModifiedObject
parameter_list|()
block|{
name|String
name|artistName
init|=
literal|"initialTestArtist"
decl_stmt|;
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
name|artistName
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"a new value"
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
comment|// Make sure the inmemory changes have been rolled back
name|assertEquals
argument_list|(
name|artistName
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Commit what's in memory...
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// .. and ensure that the correct data is in the db
name|DataContext
name|freshContext
init|=
operator|(
name|DataContext
operator|)
name|serverRuntime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|freshContext
argument_list|)
expr_stmt|;
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
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|queryResults
init|=
name|freshContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queryResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

