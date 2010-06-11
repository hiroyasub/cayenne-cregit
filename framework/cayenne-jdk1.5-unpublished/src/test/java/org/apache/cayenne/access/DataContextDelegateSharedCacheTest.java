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
name|Date
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
name|DataRow
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
name|unit
operator|.
name|MultiContextCase
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
name|util
operator|.
name|ThreadedTestHelper
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataContextDelegateSharedCacheTest
extends|extends
name|MultiContextCase
block|{
specifier|protected
name|Artist
name|artist
decl_stmt|;
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Override
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
name|context
operator|=
name|createDataContextWithSharedCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// prepare a single artist record
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"version1"
argument_list|)
expr_stmt|;
name|artist
operator|.
name|setDateOfBirth
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test case to prove that delegate method is invoked on external change of object in      * the store.      *       * @throws Exception      */
specifier|public
name|void
name|testShouldMergeChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|altContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
specifier|final
name|boolean
index|[]
name|methodInvoked
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|shouldMergeChanges
parameter_list|(
name|DataObject
name|object
parameter_list|,
name|DataRow
name|snapshotInStore
parameter_list|)
block|{
name|methodInvoked
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
comment|// make sure we have a fully resolved copy of an artist object
comment|// in the second context
name|Artist
name|altArtist
init|=
operator|(
name|Artist
operator|)
name|altContext
operator|.
name|localObject
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|altArtist
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|altArtist
argument_list|,
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|altArtist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|altArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|altContext
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// Update and save artist in peer context
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"version2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that delegate was consulted when an object store
comment|// was refreshed
name|ThreadedTestHelper
name|helper
init|=
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Delegate was not consulted"
argument_list|,
name|methodInvoked
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|helper
operator|.
name|assertWithTimeout
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case to prove that delegate method can block changes made by ObjectStore.      *       * @throws Exception      */
specifier|public
name|void
name|testBlockedShouldMergeChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|oldName
init|=
name|artist
operator|.
name|getArtistName
argument_list|()
decl_stmt|;
name|DataContext
name|altContext
init|=
name|mirrorDataContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|shouldMergeChanges
parameter_list|(
name|DataObject
name|object
parameter_list|,
name|DataRow
name|snapshotInStore
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|altContext
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// make sure we have a fully resolved copy of an artist object
comment|// in the second context
name|Artist
name|altArtist
init|=
operator|(
name|Artist
operator|)
name|altContext
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getNode
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|altArtist
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|altArtist
operator|==
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oldName
argument_list|,
name|altArtist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|altArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// Update and save artist in peer context
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"version2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that delegate was able to block the merge
name|assertEquals
argument_list|(
name|oldName
argument_list|,
name|altArtist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case to prove that delegate method is invoked on external change of object in      * the store.      *       * @throws Exception      */
specifier|public
name|void
name|testShouldProcessDeleteOnExternalChange
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|altContext
init|=
name|mirrorDataContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|boolean
index|[]
name|methodInvoked
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|shouldProcessDelete
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|methodInvoked
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|altContext
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// make sure we have a fully resolved copy of an artist object
comment|// in the second context
name|Artist
name|altArtist
init|=
operator|(
name|Artist
operator|)
name|altContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|altArtist
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|altArtist
operator|==
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|altArtist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|altArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// Update and save artist in peer context
name|context
operator|.
name|deleteObject
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that delegate was consulted when an object store
comment|// was refreshed
name|ThreadedTestHelper
name|helper
init|=
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Delegate was not consulted"
argument_list|,
name|methodInvoked
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|helper
operator|.
name|assertWithTimeout
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case to prove that delegate method is invoked on external change of object in      * the store, and is able to block further object processing.      *       * @throws Exception      */
specifier|public
name|void
name|testBlockShouldProcessDeleteOnExternalChange
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|altContext
init|=
name|mirrorDataContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|boolean
index|[]
name|methodInvoked
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|shouldProcessDelete
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|methodInvoked
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|altContext
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// make sure we have a fully resolved copy of an artist object
comment|// in the second context
name|Artist
name|altArtist
init|=
operator|(
name|Artist
operator|)
name|altContext
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getNode
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|altArtist
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|altArtist
operator|==
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|altArtist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|altArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// Update and save artist in peer context
name|context
operator|.
name|deleteObject
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that delegate was consulted when an object store
comment|// was refreshed, and actually blocked object expulsion
name|ThreadedTestHelper
name|helper
init|=
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Delegate was not consulted"
argument_list|,
name|methodInvoked
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|helper
operator|.
name|assertWithTimeout
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|altArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|altArtist
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

