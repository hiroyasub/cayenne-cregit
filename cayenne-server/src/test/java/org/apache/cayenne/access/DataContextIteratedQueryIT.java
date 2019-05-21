begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|ResultBatchIterator
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
name|ResultIterator
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
name|ResultIteratorCallback
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
name|tx
operator|.
name|BaseTransaction
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
name|assertNull
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
name|DataContextIteratedQueryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
specifier|private
name|TableHelper
name|tExhibit
decl_stmt|;
specifier|private
name|TableHelper
name|tGallery
decl_stmt|;
specifier|private
name|TableHelper
name|tPainting
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
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
expr_stmt|;
block|}
specifier|private
name|void
name|createArtistsDataSet
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
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"artist4"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|"artist5"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|"artist11"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|"artist21"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createArtistsAndPaintingsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"P_artist1"
argument_list|,
literal|33001
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"P_artist2"
argument_list|,
literal|33002
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"P_artist3"
argument_list|,
literal|33003
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"P_artist4"
argument_list|,
literal|33004
argument_list|,
literal|4000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|"P_artist5"
argument_list|,
literal|33005
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|"P_artist11"
argument_list|,
literal|33006
argument_list|,
literal|11000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|"P_artist21"
argument_list|,
literal|33007
argument_list|,
literal|21000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createLargeArtistsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|tArtist
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"artist"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIterate
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|int
index|[]
name|count
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|context
operator|.
name|iterate
argument_list|(
name|q1
argument_list|,
operator|new
name|ResultIteratorCallback
argument_list|<
name|Artist
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|next
parameter_list|(
name|Artist
name|object
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|object
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|count
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIterateDataRows
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|int
index|[]
name|count
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|context
operator|.
name|iterate
argument_list|(
name|q1
argument_list|,
operator|new
name|ResultIteratorCallback
argument_list|<
name|DataRow
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|next
parameter_list|(
name|DataRow
name|object
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|object
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|count
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|Artist
argument_list|>
name|it
init|=
name|context
operator|.
name|iterator
argument_list|(
name|q1
argument_list|)
init|;
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Artist
name|a
range|:
name|it
control|)
block|{
name|count
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBatchIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createLargeArtistsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
init|(
name|ResultBatchIterator
argument_list|<
name|Artist
argument_list|>
name|it
init|=
name|context
operator|.
name|batchIterator
argument_list|(
name|q1
argument_list|,
literal|5
argument_list|)
init|;
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Artist
argument_list|>
name|artistList
range|:
name|it
control|)
block|{
name|count
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|artistList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformIteratedQuery_Count
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|context
operator|.
name|performIteratedQuery
argument_list|(
name|q1
argument_list|)
init|;
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|it
operator|.
name|nextRow
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformIteratedQuery_resolve
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsAndPaintingsDataSet
argument_list|()
expr_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|context
operator|.
name|performIteratedQuery
argument_list|(
name|SelectQuery
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
init|;
init|)
block|{
while|while
condition|(
name|it
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|it
operator|.
name|nextRow
argument_list|()
decl_stmt|;
comment|// try instantiating an object and fetching its relationships
name|Artist
name|artist
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
init|=
name|artist
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|paintings
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one painting for artist: "
operator|+
name|artist
argument_list|,
literal|1
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformIteratedQuery_CommitWithinIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsAndPaintingsDataSet
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|tPainting
operator|.
name|getRowCount
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|context
operator|.
name|performIteratedQuery
argument_list|(
name|SelectQuery
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
init|;
init|)
block|{
while|while
condition|(
name|it
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|it
operator|.
name|nextRow
argument_list|()
decl_stmt|;
name|Artist
name|artist
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|Painting
name|painting
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
name|painting
operator|.
name|setPaintingTitle
argument_list|(
literal|"P_"
operator|+
name|artist
operator|.
name|getArtistName
argument_list|()
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
block|}
block|}
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|tPainting
operator|.
name|getRowCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformIteratedQuery_Transaction
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|context
operator|.
name|performIteratedQuery
argument_list|(
name|SelectQuery
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
init|;
init|)
block|{
name|assertNull
argument_list|(
literal|"Iterator transaction was not unbound from thread"
argument_list|,
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: how do we test that transaction unbound from the thread is closed/committed at the end?
block|}
block|}
end_class

end_unit

