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
name|Iterator
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
name|query
operator|.
name|ObjectIdQuery
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|NestedDataContextReadTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testCreateChildDataContext
parameter_list|()
block|{
name|DataContext
name|parent
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|parent
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ObjectContext
name|child1
init|=
name|parent
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|child1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|parent
argument_list|,
name|child1
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|DataContext
operator|)
name|child1
operator|)
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ObjectContext
name|child2
init|=
name|parent
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|child2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|parent
argument_list|,
name|child2
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|DataContext
operator|)
name|child2
operator|)
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
comment|// second level of nesting
name|ObjectContext
name|child21
init|=
name|child2
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|child21
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child2
argument_list|,
name|child21
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|DataContext
operator|)
name|child2
operator|)
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLocalObjectSynchronize
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testArtists"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|childContext
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|Persistent
name|_new
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
name|Persistent
name|hollow
init|=
name|context
operator|.
name|localObject
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
literal|33001
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DataObject
name|committed
init|=
operator|(
name|DataObject
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
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
literal|33002
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|modifiedId
init|=
literal|33003
decl_stmt|;
name|Artist
name|modified
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
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
name|modifiedId
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|modified
operator|.
name|setArtistName
argument_list|(
literal|"M1"
argument_list|)
expr_stmt|;
name|DataObject
name|deleted
init|=
operator|(
name|DataObject
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
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
literal|33004
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|deleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|hollow
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
name|committed
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|deleted
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|_new
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// now check how objects in different state behave
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Persistent
name|newPeer
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|_new
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|_new
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|_new
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|newPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|newPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|newPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|_new
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|hollowPeer
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|hollow
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|hollow
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|hollowPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hollow
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|hollowPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|hollowPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|hollow
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|committedPeer
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|committed
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|committed
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|committedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|committed
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|committedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|committedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|committed
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|modifiedPeer
init|=
operator|(
name|Artist
operator|)
name|childContext
operator|.
name|localObject
argument_list|(
name|modified
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|modified
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|modifiedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|modified
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|modifiedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M1"
argument_list|,
name|modifiedPeer
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|modifiedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|modified
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Persistent
name|deletedPeer
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|deleted
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|deleted
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|deletedPeer
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|deleted
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|deletedPeer
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|childContext
argument_list|,
name|deletedPeer
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|deleted
operator|.
name|getObjectContext
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
name|testLocalObjectsNoOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testArtists"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|childContext
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|int
name|modifiedId
init|=
literal|33003
decl_stmt|;
name|Artist
name|modified
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
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
name|modifiedId
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|peerModified
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|childContext
argument_list|,
operator|new
name|ObjectIdQuery
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
name|modifiedId
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|modified
operator|.
name|setArtistName
argument_list|(
literal|"M1"
argument_list|)
expr_stmt|;
name|peerModified
operator|.
name|setArtistName
argument_list|(
literal|"M2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|peerModified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Persistent
name|peerModified2
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|modified
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|modified
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|peerModified
argument_list|,
name|peerModified2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|peerModified2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M2"
argument_list|,
name|peerModified
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M1"
argument_list|,
name|modified
operator|.
name|getArtistName
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
name|testLocalObjectRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|childContext
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|Artist
name|_new
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
name|Painting
name|_newP
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
name|_new
operator|.
name|addToPaintingArray
argument_list|(
name|_newP
argument_list|)
expr_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|childContext
operator|.
name|localObject
argument_list|(
name|_newP
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|_newP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|painting
operator|.
name|getPersistenceState
argument_list|()
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
specifier|public
name|void
name|testSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testArtists"
argument_list|)
expr_stmt|;
name|DataContext
name|parent
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|child
init|=
name|parent
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
comment|// test how different object states appear in the child on select
name|Persistent
name|_new
init|=
name|parent
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Persistent
name|hollow
init|=
name|parent
operator|.
name|localObject
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
literal|33001
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DataObject
name|committed
init|=
operator|(
name|DataObject
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|parent
argument_list|,
operator|new
name|ObjectIdQuery
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
literal|33002
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|modifiedId
init|=
literal|33003
decl_stmt|;
name|Artist
name|modified
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|parent
argument_list|,
operator|new
name|ObjectIdQuery
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
name|modifiedId
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|modified
operator|.
name|setArtistName
argument_list|(
literal|"MODDED"
argument_list|)
expr_stmt|;
name|DataObject
name|deleted
init|=
operator|(
name|DataObject
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|parent
argument_list|,
operator|new
name|ObjectIdQuery
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
literal|33004
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|parent
operator|.
name|deleteObject
argument_list|(
name|deleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|hollow
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
name|committed
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modified
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|deleted
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|_new
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|objects
init|=
name|child
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
literal|"All but NEW object must have been included"
argument_list|,
literal|4
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataObject
name|next
init|=
operator|(
name|DataObject
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|next
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|id
init|=
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
name|modifiedId
condition|)
block|{
name|assertEquals
argument_list|(
literal|"MODDED"
argument_list|,
name|next
operator|.
name|readProperty
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testReadToOneRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testReadRelationship"
argument_list|)
expr_stmt|;
name|DataContext
name|parent
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|child
init|=
name|parent
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
comment|// test how different object states appear in the child on select
name|int
name|hollowTargetSrcId
init|=
literal|33001
decl_stmt|;
name|int
name|modifiedTargetSrcId
init|=
literal|33002
decl_stmt|;
name|int
name|deletedTargetSrcId
init|=
literal|33003
decl_stmt|;
name|int
name|committedTargetSrcId
init|=
literal|33004
decl_stmt|;
name|int
name|newTargetSrcId
init|=
literal|33005
decl_stmt|;
name|Painting
name|hollowTargetSrc
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|parent
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
name|hollowTargetSrcId
argument_list|)
decl_stmt|;
name|Artist
name|hollowTarget
init|=
name|hollowTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|Painting
name|modifiedTargetSrc
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|parent
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
name|modifiedTargetSrcId
argument_list|)
decl_stmt|;
name|Artist
name|modifiedTarget
init|=
name|modifiedTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|modifiedTarget
operator|.
name|setArtistName
argument_list|(
literal|"M1"
argument_list|)
expr_stmt|;
name|Painting
name|deletedTargetSrc
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|parent
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
name|deletedTargetSrcId
argument_list|)
decl_stmt|;
name|Artist
name|deletedTarget
init|=
name|deletedTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|deletedTargetSrc
operator|.
name|setToArtist
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|parent
operator|.
name|deleteObject
argument_list|(
name|deletedTarget
argument_list|)
expr_stmt|;
name|Painting
name|committedTargetSrc
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|parent
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
name|committedTargetSrcId
argument_list|)
decl_stmt|;
name|Artist
name|committedTarget
init|=
name|committedTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|committedTarget
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|Painting
name|newTargetSrc
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|parent
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
name|newTargetSrcId
argument_list|)
decl_stmt|;
name|Artist
name|newTarget
init|=
name|parent
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|newTarget
operator|.
name|setArtistName
argument_list|(
literal|"N1"
argument_list|)
expr_stmt|;
name|newTargetSrc
operator|.
name|setToArtist
argument_list|(
name|newTarget
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|hollowTargetSrc
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
name|modifiedTargetSrc
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|deletedTargetSrc
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
name|committedTargetSrc
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|newTargetSrc
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
name|hollowTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|modifiedTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|deletedTarget
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
name|committedTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|newTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// run an ordered query, so we can address specific objects directly by index
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
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
name|childSources
init|=
name|child
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|childSources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Painting
name|childHollowTargetSrc
init|=
operator|(
name|Painting
operator|)
name|childSources
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|childHollowTargetSrc
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|childHollowTarget
init|=
name|childHollowTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|childHollowTarget
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|childHollowTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|childHollowTarget
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|childModifiedTarget
init|=
operator|(
operator|(
name|Painting
operator|)
name|childSources
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|childModifiedTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|childModifiedTarget
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"M1"
argument_list|,
name|childModifiedTarget
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|childDeletedTargetSrc
init|=
operator|(
name|Painting
operator|)
name|childSources
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// make sure we got the right object...
name|assertEquals
argument_list|(
name|deletedTargetSrc
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|childDeletedTargetSrc
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|childDeletedTarget
init|=
name|childDeletedTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|childDeletedTarget
argument_list|)
expr_stmt|;
name|Artist
name|childCommittedTarget
init|=
operator|(
operator|(
name|Painting
operator|)
name|childSources
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|childCommittedTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|childCommittedTarget
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|childNewTargetSrc
init|=
operator|(
name|Painting
operator|)
name|childSources
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
comment|// make sure we got the right object...
name|assertEquals
argument_list|(
name|newTargetSrc
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|childNewTargetSrc
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|childNewTarget
init|=
name|childNewTargetSrc
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|childNewTarget
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|childNewTarget
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|childNewTarget
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"N1"
argument_list|,
name|childNewTarget
operator|.
name|getArtistName
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
name|testPrefetchingToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|DataContext
name|parent
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|child
init|=
name|parent
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|ObjectId
name|prefetchedId
init|=
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
literal|33001
argument_list|)
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|child
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
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|results
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|o
init|=
operator|(
name|Painting
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|o1
init|=
name|o
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
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
name|assertSame
argument_list|(
name|child
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|prefetchedId
argument_list|,
name|o1
operator|.
name|getObjectId
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
specifier|public
name|void
name|testPrefetchingToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|DataContext
name|parent
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|child
init|=
name|parent
operator|.
name|createChildContext
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
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|child
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
name|Artist
name|o1
init|=
operator|(
name|Artist
operator|)
name|results
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
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children1
init|=
name|o1
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|children1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|children1
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|o
init|=
operator|(
name|Painting
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
argument_list|,
name|o
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Artist
name|o2
init|=
operator|(
name|Artist
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|child
argument_list|,
name|o2
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children2
init|=
name|o2
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|children2
operator|.
name|size
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
block|}
end_class

end_unit

