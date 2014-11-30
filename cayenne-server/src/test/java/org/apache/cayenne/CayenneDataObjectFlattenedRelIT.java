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
name|MockDataNode
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
name|Before
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
name|List
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
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|CayenneDataObjectFlattenedRelIT
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
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
specifier|private
name|TableHelper
name|tArtGroup
decl_stmt|;
specifier|private
name|TableHelper
name|tArtistGroup
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|cleanUpDB
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|update
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|set
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|super
operator|.
name|cleanUpDB
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
name|tArtGroup
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTGROUP"
argument_list|)
expr_stmt|;
name|tArtGroup
operator|.
name|setColumns
argument_list|(
literal|"GROUP_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tArtistGroup
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|tArtistGroup
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"GROUP_ID"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|create1Artist1ArtGroupDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtGroup
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|create1Artist2ArtGroupDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
argument_list|()
expr_stmt|;
name|tArtGroup
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|create1Artist1ArtGroup1ArtistGroupDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
argument_list|()
expr_stmt|;
name|tArtistGroup
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtGroup
argument_list|>
name|groupList
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|groupList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|groupList
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
name|testReadFlattenedRelationship2
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroup1ArtistGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtGroup
argument_list|>
name|groupList
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|groupList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|groupList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|groupList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddToFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getGroupArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"g1"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
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
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|ArtGroup
name|group
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|group
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
name|List
argument_list|<
name|?
argument_list|>
name|groupList
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
operator|(
operator|(
name|ArtGroup
operator|)
name|groupList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Ensure that the commit doesn't fail
name|a1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// and check again
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
comment|// refetch artist with a different context
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|a1
operator|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context2
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33001
argument_list|)
expr_stmt|;
name|groupList
operator|=
name|a1
operator|.
name|getGroupArray
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
operator|(
operator|(
name|ArtGroup
operator|)
name|groupList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Test case to show up a bug in committing more than once
annotation|@
name|Test
specifier|public
name|void
name|testDoubleCommitAddToFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"g1"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
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
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtGroup
name|group
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|groupList
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
operator|(
operator|(
name|ArtGroup
operator|)
name|groupList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Ensure that the commit doesn't fail
name|a1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
comment|// The bug caused the second commit to fail (the link record
comment|// was inserted again)
name|a1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have thrown an exception"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveFromFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroup1ArtistGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|ArtGroup
name|group
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|removeFromGroupArray
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtGroup
argument_list|>
name|groupList
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Ensure that the commit doesn't fail
name|a1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// and check again
name|groupList
operator|=
name|a1
operator|.
name|getGroupArray
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|groupList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Demonstrates a possible bug in ordering of deletes, when a flattened relationships
comment|// link record is deleted at the same time (same transaction) as one of the record to
comment|// which it links.
annotation|@
name|Test
specifier|public
name|void
name|testRemoveFlattenedRelationshipAndRootRecord
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroup1ArtistGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|ArtGroup
name|group
init|=
name|a1
operator|.
name|getGroupArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|removeFromGroupArray
argument_list|(
name|group
argument_list|)
expr_stmt|;
comment|// Cause the delete of the link record
name|context
operator|.
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
comment|// Cause the deletion of the artist
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have thrown the exception :"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddRemoveFlattenedRelationship1
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist1ArtGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"g1"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
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
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtGroup
name|group
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|group
operator|.
name|removeFromArtistArray
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
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
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddRemoveFlattenedRelationship2
parameter_list|()
throws|throws
name|Exception
block|{
name|create1Artist2ArtGroupDataSet
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
literal|33001
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtGroup
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
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
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtGroup
name|g1
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ArtGroup
name|g2
init|=
operator|(
name|ArtGroup
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToGroupArray
argument_list|(
name|g2
argument_list|)
expr_stmt|;
comment|// test that there is no delete query issued when a flattened join is first
comment|// added and then deleted AND there are some other changes (CAY-548)
name|a1
operator|.
name|removeFromGroupArray
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|MockDataNode
name|nodeWrapper
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
argument_list|,
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|nodeWrapper
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodeWrapper
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

