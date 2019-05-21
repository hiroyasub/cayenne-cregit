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
name|remote
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
name|CayenneContext
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|MtTable1
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
name|client
operator|.
name|ClientCase
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
name|assertSame
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
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|RemoteIncrementalFaultListIT
extends|extends
name|ClientCase
block|{
specifier|private
specifier|static
specifier|final
name|int
name|COUNT
init|=
literal|25
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|clientContext
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tMTTable
decl_stmt|;
specifier|private
name|RemoteIncrementalFaultList
name|list
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
name|tMTTable
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|setColumns
argument_list|(
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"SERVER_ATTRIBUTE1"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createObjectsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMTTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"g2"
argument_list|,
literal|"s2"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"g3"
argument_list|,
literal|"s3"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"g4"
argument_list|,
literal|"s4"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|"g5"
argument_list|,
literal|"s5"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"g6"
argument_list|,
literal|"s6"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"g7"
argument_list|,
literal|"s7"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|"g8"
argument_list|,
literal|"s8"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|9
argument_list|,
literal|"g9"
argument_list|,
literal|"s9"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|10
argument_list|,
literal|"g10"
argument_list|,
literal|"s10"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"g11"
argument_list|,
literal|"s11"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|12
argument_list|,
literal|"g12"
argument_list|,
literal|"s12"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|13
argument_list|,
literal|"g13"
argument_list|,
literal|"s13"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|14
argument_list|,
literal|"g14"
argument_list|,
literal|"s14"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|15
argument_list|,
literal|"g15"
argument_list|,
literal|"s15"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|16
argument_list|,
literal|"g16"
argument_list|,
literal|"s16"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|17
argument_list|,
literal|"g17"
argument_list|,
literal|"s17"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|18
argument_list|,
literal|"g18"
argument_list|,
literal|"s18"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|19
argument_list|,
literal|"g19"
argument_list|,
literal|"s19"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|20
argument_list|,
literal|"g20"
argument_list|,
literal|"s20"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|21
argument_list|,
literal|"g21"
argument_list|,
literal|"s21"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|22
argument_list|,
literal|"g22"
argument_list|,
literal|"s22"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|23
argument_list|,
literal|"g23"
argument_list|,
literal|"s23"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|24
argument_list|,
literal|"g24"
argument_list|,
literal|"s24"
argument_list|)
expr_stmt|;
name|tMTTable
operator|.
name|insert
argument_list|(
literal|25
argument_list|,
literal|"g25"
argument_list|,
literal|"s25"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|prepareList
parameter_list|(
name|int
name|pageSize
parameter_list|)
throws|throws
name|Exception
block|{
name|createObjectsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// make sure total number of objects is not divisable
comment|// by the page size, to test the last smaller page
name|query
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
literal|"db:"
operator|+
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|list
operator|=
operator|new
name|RemoteIncrementalFaultList
argument_list|(
name|clientContext
argument_list|,
name|query
argument_list|)
expr_stmt|;
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
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COUNT
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
name|testIteratorPageSize1
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestIterator
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIteratorPageSize5
parameter_list|()
throws|throws
name|Exception
block|{
comment|// size divisiable by page size
name|doTestIterator
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIteratorPageSize6
parameter_list|()
throws|throws
name|Exception
block|{
comment|// size not divisable by page size
name|doTestIterator
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIteratorPageSize25
parameter_list|()
throws|throws
name|Exception
block|{
comment|// size equals to page size
name|doTestIterator
argument_list|(
name|COUNT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIteratorPageSize26
parameter_list|()
throws|throws
name|Exception
block|{
comment|// size exceeding page size
name|doTestIterator
argument_list|(
name|COUNT
operator|+
literal|1
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
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
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
name|Persistent
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
name|COUNT
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
name|get
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COUNT
operator|-
literal|12
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
annotation|@
name|Test
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
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|RemoteIncrementalFaultList
operator|.
name|PLACEHOLDER
argument_list|,
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|8
argument_list|)
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
name|ClientMtTable1
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
name|ClientMtTable1
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
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|RemoteIncrementalFaultList
operator|.
name|PLACEHOLDER
argument_list|,
name|list
operator|.
name|elements
operator|.
name|get
argument_list|(
literal|8
argument_list|)
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
name|ClientMtTable1
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
name|ClientMtTable1
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
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1
operator|.
name|eq
argument_list|(
literal|"g20"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|list
operator|.
name|context
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
name|ClientMtTable1
name|row
init|=
operator|(
name|ClientMtTable1
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
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
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
name|prepareList
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1
operator|.
name|eq
argument_list|(
literal|"g20"
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|ClientMtTable1
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|list
operator|.
name|context
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|row
init|=
operator|(
name|ClientMtTable1
operator|)
name|objects
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
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestIterator
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|Exception
block|{
name|prepareList
argument_list|(
name|size
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
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
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
name|Persistent
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
block|}
end_class

end_unit

