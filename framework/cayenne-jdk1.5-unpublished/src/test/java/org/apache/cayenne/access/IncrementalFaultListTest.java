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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|Ordering
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
name|Query
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|IncrementalFaultListTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|IncrementalFaultList
name|list
decl_stmt|;
specifier|protected
name|Query
name|query
decl_stmt|;
specifier|protected
name|void
name|prepareList
parameter_list|(
name|int
name|pageSize
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testArtists"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
comment|// make sure total number of objects is not divisable
comment|// by the page size, to test the last smaller page
name|q
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|Ordering
operator|.
name|ASC
argument_list|)
expr_stmt|;
name|query
operator|=
name|q
expr_stmt|;
name|list
operator|=
operator|new
name|IncrementalFaultList
argument_list|(
name|super
operator|.
name|createDataContext
argument_list|()
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSize
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataContextTest
operator|.
name|artistCount
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSmallList
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|49
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataContextTest
operator|.
name|artistCount
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnePageList
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
name|DataContextTest
operator|.
name|artistCount
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataContextTest
operator|.
name|artistCount
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Iterator
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
specifier|public
name|void
name|testNewObject
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
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
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
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:ARTIST_ID"
argument_list|,
name|Ordering
operator|.
name|DESC
argument_list|)
expr_stmt|;
name|IncrementalFaultList
name|list
init|=
operator|new
name|IncrementalFaultList
argument_list|(
name|context
argument_list|,
name|q
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
name|DataContextTest
operator|.
name|artistCount
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testListIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|ListIterator
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
specifier|public
name|void
name|testSort
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
operator|new
name|Ordering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|Ordering
operator|.
name|DESC
argument_list|)
operator|.
name|orderList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|Iterator
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
specifier|public
name|void
name|testUnfetchedObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataContextTest
operator|.
name|artistCount
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
name|DataContextTest
operator|.
name|artistCount
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
specifier|public
name|void
name|testPageIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
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
specifier|public
name|void
name|testPagesRead1
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
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
literal|0
argument_list|)
operator|instanceof
name|Map
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
name|Map
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
specifier|public
name|void
name|testGet1
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
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
literal|0
argument_list|)
operator|instanceof
name|Map
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
name|Map
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
specifier|public
name|void
name|testGet2
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SelectQuery
operator|)
name|query
operator|)
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
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
literal|0
argument_list|)
operator|instanceof
name|Map
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
name|Map
argument_list|)
expr_stmt|;
name|Object
name|a0
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a0
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
literal|0
argument_list|)
operator|instanceof
name|Artist
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
specifier|public
name|void
name|testIndexOf
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist20"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
name|artists
init|=
name|list
operator|.
name|dataContext
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
specifier|public
name|void
name|testLastIndexOf
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist20"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
name|artists
init|=
name|list
operator|.
name|dataContext
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

