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
name|remote
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
name|access
operator|.
name|ClientServerChannel
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
name|event
operator|.
name|MockEventManager
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
name|remote
operator|.
name|service
operator|.
name|LocalConnection
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
name|AccessStack
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
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|RemoteIncrementalFaultListTest
extends|extends
name|CayenneCase
block|{
specifier|static
specifier|final
name|int
name|COUNT
init|=
literal|25
decl_stmt|;
specifier|protected
name|RemoteIncrementalFaultList
name|list
decl_stmt|;
specifier|protected
name|SelectQuery
name|query
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
return|;
block|}
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
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testObjects"
argument_list|)
expr_stmt|;
name|this
operator|.
name|query
operator|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|ClientServerChannel
name|serverChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|LocalConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
name|serverChannel
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|clientChannel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|,
literal|false
argument_list|,
operator|new
name|MockEventManager
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|this
operator|.
name|list
operator|=
operator|new
name|RemoteIncrementalFaultList
argument_list|(
operator|new
name|CayenneContext
argument_list|(
name|clientChannel
argument_list|)
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
name|COUNT
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
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"g20"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
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
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"g20"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
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

