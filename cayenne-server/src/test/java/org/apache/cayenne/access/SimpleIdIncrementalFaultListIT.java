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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|ObjectSelect
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
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
name|SimpleIdIncrementalFaultListIT
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
block|}
specifier|protected
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
literal|"artist6"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|"artist7"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33008
argument_list|,
literal|"artist8"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33009
argument_list|,
literal|"artist9"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33010
argument_list|,
literal|"artist10"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33011
argument_list|,
literal|"artist11"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33012
argument_list|,
literal|"artist12"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33013
argument_list|,
literal|"artist13"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33014
argument_list|,
literal|"artist14"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33015
argument_list|,
literal|"artist15"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33016
argument_list|,
literal|"artist16"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33017
argument_list|,
literal|"artist17"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33018
argument_list|,
literal|"artist18"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33019
argument_list|,
literal|"artist19"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33020
argument_list|,
literal|"artist20"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33021
argument_list|,
literal|"artist21"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33022
argument_list|,
literal|"artist22"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33023
argument_list|,
literal|"artist23"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33024
argument_list|,
literal|"artist24"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33025
argument_list|,
literal|"artist25"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveDeleted
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
comment|// DataContext context = createDataContext();
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|SimpleIdIncrementalFaultList
argument_list|<
name|Artist
argument_list|>
name|list
init|=
operator|new
name|SimpleIdIncrementalFaultList
argument_list|<
name|Artist
argument_list|>
argument_list|(
name|context
argument_list|,
name|query
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|list
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|prepareList
parameter_list|(
name|int
name|pageSize
parameter_list|)
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
comment|// make sure total number of objects is not divisable
comment|// by the page size, to test the last smaller page
operator|.
name|pageSize
argument_list|(
name|pageSize
argument_list|)
operator|.
name|orderBy
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
decl_stmt|;
return|return
operator|new
name|SimpleIdIncrementalFaultList
argument_list|<>
argument_list|(
name|context
argument_list|,
name|query
argument_list|,
literal|10000
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSize
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|list
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
name|testSmallList
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|49
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|list
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
name|testOnePageList
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|25
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|list
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
name|testIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|obj
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
comment|// iterator must be resolved page by page
name|int
name|expectedResolved
init|=
name|list
operator|.
name|pageIndex
argument_list|(
name|counter
argument_list|)
operator|*
name|list
operator|.
name|getPageSize
argument_list|()
operator|+
name|list
operator|.
name|getPageSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedResolved
operator|>
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
name|expectedResolved
operator|=
name|list
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
name|expectedResolved
argument_list|,
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|Artist
name|newArtist
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
name|newArtist
operator|.
name|setArtistName
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|6
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|asc
argument_list|()
argument_list|)
decl_stmt|;
name|SimpleIdIncrementalFaultList
argument_list|<
name|Artist
argument_list|>
name|list
init|=
operator|new
name|SimpleIdIncrementalFaultList
argument_list|<>
argument_list|(
name|context
argument_list|,
name|q
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|newArtist
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testListIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|ListIterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|obj
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
comment|// iterator must be resolved page by page
name|int
name|expectedResolved
init|=
name|list
operator|.
name|pageIndex
argument_list|(
name|counter
argument_list|)
operator|*
name|list
operator|.
name|getPageSize
argument_list|()
operator|+
name|list
operator|.
name|getPageSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedResolved
operator|>
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
name|expectedResolved
operator|=
name|list
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
name|expectedResolved
argument_list|,
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSort
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|desc
argument_list|()
operator|.
name|orderList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Artist
name|previousArtist
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|previousArtist
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
name|previousArtist
operator|.
name|getArtistName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnfetchedObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|get
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|25
operator|-
literal|6
argument_list|,
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|resolveAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPageIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|pageIndex
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|pageIndex
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|pageIndex
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|13
argument_list|,
name|list
operator|.
name|pageIndex
argument_list|(
literal|82
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Element index beyound array size must throw an IndexOutOfBoundsException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|ex
parameter_list|)
block|{
comment|// exception expercted
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPagesRead1
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|8
argument_list|)
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|list
operator|.
name|resolveInterval
argument_list|(
literal|5
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|list
operator|.
name|resolveAll
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|)
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGet1
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|8
argument_list|)
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|list
operator|.
name|get
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|8
argument_list|)
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIndexOf
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist20"
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|list
operator|.
name|dataContext
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
name|row
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
literal|19
argument_list|,
name|list
operator|.
name|indexOf
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|list
operator|.
name|indexOf
argument_list|(
name|list
operator|.
name|dataContext
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLastIndexOf
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleIdIncrementalFaultList
argument_list|<
name|?
argument_list|>
name|list
init|=
name|prepareList
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"artist20"
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|list
operator|.
name|dataContext
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
name|row
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
literal|19
argument_list|,
name|list
operator|.
name|lastIndexOf
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|list
operator|.
name|lastIndexOf
argument_list|(
name|list
operator|.
name|dataContext
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

