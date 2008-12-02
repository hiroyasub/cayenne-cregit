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
comment|/**  * Tests IncrementalFaultList behavior when fetching data rows.  *   */
end_comment

begin_class
specifier|public
class|class
name|SimpleIdIncrementalFaultListDataRowsTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|SimpleIdIncrementalFaultList
name|list
decl_stmt|;
specifier|protected
name|Query
name|query
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
name|q
operator|.
name|setPageSize
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
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
name|SimpleIdIncrementalFaultList
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
name|testGet1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|rowWidth
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
name|assertEquals
argument_list|(
name|list
operator|.
name|rowWidth
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|size
argument_list|()
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
literal|19
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
literal|19
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
name|Map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|rowWidth
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|a
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist20"
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|a
operator|)
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIndexOf1
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|parallelContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|parallelContext
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
name|Map
name|row
init|=
operator|(
name|Map
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
name|DataRow
name|clone
init|=
operator|new
name|DataRow
argument_list|(
name|row
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
name|clone
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|remove
argument_list|(
literal|"ARTIST_ID"
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
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIndexOf2
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|parallelContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist2"
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
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|parallelContext
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
name|Map
name|row
init|=
operator|(
name|Map
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
literal|1
argument_list|,
name|list
operator|.
name|indexOf
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|remove
argument_list|(
literal|"ARTIST_NAME"
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
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLastIndexOf1
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|parallelContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist3"
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
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|parallelContext
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
name|Map
name|row
init|=
operator|(
name|Map
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
literal|2
argument_list|,
name|list
operator|.
name|lastIndexOf
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|remove
argument_list|(
literal|"ARTIST_NAME"
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
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLastIndexOf2
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|parallelContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|parallelContext
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
name|Map
name|row
init|=
operator|(
name|Map
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
name|row
operator|.
name|remove
argument_list|(
literal|"ARTIST_ID"
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
name|row
argument_list|)
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
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|rowWidth
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
literal|"Unexpected object class: "
operator|+
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|obj
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|rowWidth
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|obj
operator|)
operator|.
name|size
argument_list|()
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
if|if
condition|(
name|list
operator|.
name|getUnfetchedObjects
argument_list|()
operator|>=
name|list
operator|.
name|getPageSize
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
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
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
block|}
name|counter
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

