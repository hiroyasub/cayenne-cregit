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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataContext
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
name|CayenneDataObjectInContextIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoubleRegistration
parameter_list|()
block|{
name|DataObject
name|object
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|ObjectId
name|tempID
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tempID
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tempID
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// double registration in the same context should be quietly ignored
name|context
operator|.
name|registerNewObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tempID
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|tempID
argument_list|)
argument_list|)
expr_stmt|;
comment|// registering in another context should throw an exception
name|ObjectContext
name|anotherContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
try|try
block|{
name|anotherContext
operator|.
name|registerNewObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"registerNewObject should've failed - object is already in another context"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitChangesInBatch
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
literal|"abc1"
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
literal|"abc2"
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"abc3"
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
name|artists
init|=
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
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
name|testSetObjectId
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStateTransToNew
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|o1
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
name|testStateNewToCommitted
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|o1
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
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
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
name|testStateCommittedToModified
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
name|o1
operator|.
name|getArtistName
argument_list|()
operator|+
literal|"_1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|o1
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
name|testStateModifiedToCommitted
parameter_list|()
block|{
name|Artist
name|o1
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
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"qY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
name|o1
operator|.
name|getArtistName
argument_list|()
operator|+
literal|"_1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|o1
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
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
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
name|testStateCommittedToDeleted
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|o1
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
name|testStateDeletedToTransient
parameter_list|()
block|{
name|Artist
name|o1
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
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"qY"
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
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|o1
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
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetContext
parameter_list|()
block|{
name|Artist
name|o1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchByAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"m6"
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
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"m6"
argument_list|)
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
name|Artist
name|o1
init|=
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"m6"
argument_list|,
name|o1
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
name|testUniquing
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"m6"
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
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"m6"
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
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
name|Artist
name|a2
init|=
operator|(
name|Artist
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
name|assertNotNull
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotVersion1
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
name|assertEquals
argument_list|(
name|DataObject
operator|.
name|DEFAULT_VERSION
argument_list|,
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// test versions set on commit
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|DataRow
name|cachedSnapshot
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cachedSnapshot
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cachedSnapshot
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotVersion2
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"m6"
argument_list|)
expr_stmt|;
comment|// test versions assigned on fetch... clean up domain cache
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
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|DataObject
operator|.
name|DEFAULT_VERSION
operator|==
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotVersion3
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
literal|"qY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// test versions assigned after update
name|long
name|oldVersion
init|=
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
name|artist
operator|.
name|setArtistName
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
operator|+
literal|"---"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|oldVersion
operator|==
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artist
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests a condition when user substitutes object id of a new object instead of      * setting replacement. This is demonstrated here -      * http://objectstyle.org/cayenne/lists/cayenne-user/2005/01/0210.html      */
annotation|@
name|Test
specifier|public
name|void
name|testObjectsCommittedManualOID
parameter_list|()
block|{
name|Artist
name|object
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
name|object
operator|.
name|setArtistName
argument_list|(
literal|"ABC1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// do a manual id substitution
name|object
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
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
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// refetch
name|context
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|Artist
name|object2
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
literal|3
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC1"
argument_list|,
name|object2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

