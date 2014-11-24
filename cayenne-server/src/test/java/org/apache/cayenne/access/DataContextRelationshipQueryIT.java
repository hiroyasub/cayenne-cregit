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
name|query
operator|.
name|RelationshipQuery
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
name|assertTrue
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
name|DataContextRelationshipQueryIT
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
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
specifier|private
name|TableHelper
name|tPainting
decl_stmt|;
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
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneArtistOnePaintingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"p1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnrefreshingToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtistOnePaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|p
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
literal|1
argument_list|)
decl_stmt|;
comment|// resolve artist once before running non-refreshing query, to check that we do
comment|// not refresh the object
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
literal|1
argument_list|)
decl_stmt|;
name|long
name|v
init|=
name|a
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
name|int
name|writeCalls
init|=
name|a
operator|.
name|getPropertyWrittenDirectly
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a1"
argument_list|,
name|a
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
literal|"a2"
argument_list|)
operator|.
name|where
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|1
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|RelationshipQuery
name|toOne
init|=
operator|new
name|RelationshipQuery
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|related
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|toOne
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|related
operator|.
name|contains
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a1"
argument_list|,
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|v
argument_list|,
name|a
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Looks like relationship query caused snapshot refresh"
argument_list|,
name|writeCalls
argument_list|,
name|a
operator|.
name|getPropertyWrittenDirectly
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefreshingToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtistOnePaintingDataSet
argument_list|()
expr_stmt|;
name|Painting
name|p
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
literal|1
argument_list|)
decl_stmt|;
comment|// resolve artist once before running non-refreshing query, to check that we do
comment|// not refresh the object
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
literal|1
argument_list|)
decl_stmt|;
name|long
name|v
init|=
name|a
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
name|int
name|writeCalls
init|=
name|a
operator|.
name|getPropertyWrittenDirectly
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a1"
argument_list|,
name|a
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
literal|"a2"
argument_list|)
operator|.
name|where
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|1
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
name|RelationshipQuery
name|toOne
init|=
operator|new
name|RelationshipQuery
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|related
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|toOne
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|related
operator|.
name|contains
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a2"
argument_list|,
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Looks like relationship query didn't cause a snapshot refresh"
argument_list|,
name|v
operator|<
name|a
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Looks like relationship query didn't cause a snapshot refresh"
argument_list|,
name|writeCalls
operator|<
name|a
operator|.
name|getPropertyWrittenDirectly
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

