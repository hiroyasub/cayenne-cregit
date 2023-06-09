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
operator|.
name|flush
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|access
operator|.
name|DataDomain
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
name|flush
operator|.
name|operation
operator|.
name|DefaultDbRowOpSorter
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
name|flush
operator|.
name|operation
operator|.
name|BaseDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpSorter
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
name|flush
operator|.
name|operation
operator|.
name|DeleteDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|InsertDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|UpdateDbRowOp
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|map
operator|.
name|EntityResolver
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
name|map
operator|.
name|EntitySorter
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDbRowOpSorterTest
block|{
specifier|private
name|EntitySorter
name|entitySorter
decl_stmt|;
specifier|private
name|DbRowOpSorter
name|sorter
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|createSorter
parameter_list|()
block|{
name|entitySorter
operator|=
name|mock
argument_list|(
name|EntitySorter
operator|.
name|class
argument_list|)
expr_stmt|;
name|EntityResolver
name|entityResolver
init|=
name|mock
argument_list|(
name|EntityResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|entitySorter
operator|.
name|getDbEntityComparator
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|DbEntity
operator|::
name|getName
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|entitySorter
operator|.
name|isReflexive
argument_list|(
name|argThat
argument_list|(
name|ent
lambda|->
name|ent
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"reflexive"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataDomain
name|dataDomain
init|=
name|mock
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|dataDomain
operator|.
name|getEntitySorter
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|entitySorter
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
name|sorter
operator|=
operator|new
name|DefaultDbRowOpSorter
argument_list|(
parameter_list|()
lambda|->
name|dataDomain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortEmptyList
parameter_list|()
block|{
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|rows
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sorted
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortByOpType
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|ObjectId
name|id4
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|ObjectId
name|id5
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|ObjectId
name|id6
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|DbEntity
name|test
init|=
name|mockEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DbRowOp
name|op1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id1
argument_list|)
argument_list|,
name|test
argument_list|,
name|id1
argument_list|)
decl_stmt|;
name|DbRowOp
name|op2
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id2
argument_list|)
argument_list|,
name|test
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|DbRowOp
name|op3
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id3
argument_list|)
argument_list|,
name|test
argument_list|,
name|id3
argument_list|)
decl_stmt|;
name|DbRowOp
name|op4
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id4
argument_list|)
argument_list|,
name|test
argument_list|,
name|id4
argument_list|)
decl_stmt|;
name|DbRowOp
name|op5
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id5
argument_list|)
argument_list|,
name|test
argument_list|,
name|id5
argument_list|)
decl_stmt|;
name|DbRowOp
name|op6
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id6
argument_list|)
argument_list|,
name|test
argument_list|,
name|id6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|rows
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|,
name|op5
argument_list|,
name|op6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op4
argument_list|,
name|op2
argument_list|,
name|op5
argument_list|,
name|op3
argument_list|,
name|op6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortByOpEntity
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test3"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|ObjectId
name|id4
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test1"
argument_list|,
literal|"id"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|DbRowOp
name|op1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id1
argument_list|)
argument_list|,
name|mockEntity
argument_list|(
literal|"test4"
argument_list|)
argument_list|,
name|id1
argument_list|)
decl_stmt|;
name|DbRowOp
name|op2
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id2
argument_list|)
argument_list|,
name|mockEntity
argument_list|(
literal|"test2"
argument_list|)
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|DbRowOp
name|op3
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id3
argument_list|)
argument_list|,
name|mockEntity
argument_list|(
literal|"test3"
argument_list|)
argument_list|,
name|id3
argument_list|)
decl_stmt|;
name|DbRowOp
name|op4
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id4
argument_list|)
argument_list|,
name|mockEntity
argument_list|(
literal|"test1"
argument_list|)
argument_list|,
name|id4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|rows
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op4
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortById
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id4
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|DbEntity
name|test
init|=
name|mockEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|InsertDbRowOp
name|op1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id1
argument_list|)
argument_list|,
name|test
argument_list|,
name|id1
argument_list|)
decl_stmt|;
name|InsertDbRowOp
name|op2
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id2
argument_list|)
argument_list|,
name|test
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|DeleteDbRowOp
name|op3
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id3
argument_list|)
argument_list|,
name|test
argument_list|,
name|id3
argument_list|)
decl_stmt|;
name|DeleteDbRowOp
name|op4
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id4
argument_list|)
argument_list|,
name|test
argument_list|,
name|id4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|rows
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortByIdDifferentEntities
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id4
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|ObjectId
name|id5
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|ObjectId
name|id6
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|ObjectId
name|id7
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|ObjectId
name|id8
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|ObjectId
name|id9
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|ObjectId
name|id10
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|,
literal|"id"
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|DbEntity
name|test
init|=
name|mockEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DbEntity
name|test2
init|=
name|mockEntity
argument_list|(
literal|"test2"
argument_list|)
decl_stmt|;
name|BaseDbRowOp
index|[]
name|op
init|=
operator|new
name|BaseDbRowOp
index|[
literal|10
index|]
decl_stmt|;
name|op
index|[
literal|0
index|]
operator|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id1
argument_list|)
argument_list|,
name|test2
argument_list|,
name|id1
argument_list|)
expr_stmt|;
name|op
index|[
literal|1
index|]
operator|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id2
argument_list|)
argument_list|,
name|test
argument_list|,
name|id2
argument_list|)
expr_stmt|;
name|op
index|[
literal|2
index|]
operator|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id3
argument_list|)
argument_list|,
name|test
argument_list|,
name|id3
argument_list|)
expr_stmt|;
name|op
index|[
literal|3
index|]
operator|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id4
argument_list|)
argument_list|,
name|test
argument_list|,
name|id4
argument_list|)
expr_stmt|;
name|op
index|[
literal|4
index|]
operator|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id5
argument_list|)
argument_list|,
name|test2
argument_list|,
name|id5
argument_list|)
expr_stmt|;
name|op
index|[
literal|5
index|]
operator|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id6
argument_list|)
argument_list|,
name|test
argument_list|,
name|id6
argument_list|)
expr_stmt|;
name|op
index|[
literal|6
index|]
operator|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id7
argument_list|)
argument_list|,
name|test
argument_list|,
name|id7
argument_list|)
expr_stmt|;
name|op
index|[
literal|7
index|]
operator|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id8
argument_list|)
argument_list|,
name|test2
argument_list|,
name|id8
argument_list|)
expr_stmt|;
name|op
index|[
literal|8
index|]
operator|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id9
argument_list|)
argument_list|,
name|test2
argument_list|,
name|id9
argument_list|)
expr_stmt|;
name|op
index|[
literal|9
index|]
operator|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id10
argument_list|)
argument_list|,
name|test
argument_list|,
name|id10
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op
index|[
literal|1
index|]
argument_list|,
name|op
index|[
literal|6
index|]
argument_list|,
name|op
index|[
literal|0
index|]
argument_list|,
name|op
index|[
literal|4
index|]
argument_list|,
name|op
index|[
literal|3
index|]
argument_list|,
name|op
index|[
literal|7
index|]
argument_list|,
name|op
index|[
literal|8
index|]
argument_list|,
name|op
index|[
literal|2
index|]
argument_list|,
name|op
index|[
literal|5
index|]
argument_list|,
name|op
index|[
literal|9
index|]
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|op
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sortReflexive
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"reflexive"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"reflexive"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ObjectId
name|id3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"reflexive"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|ObjectId
name|id4
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"reflexive"
argument_list|,
literal|"id"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|DbEntity
name|reflexive
init|=
name|mockEntity
argument_list|(
literal|"reflexive"
argument_list|)
decl_stmt|;
name|DbRowOp
name|op1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id1
argument_list|)
argument_list|,
name|reflexive
argument_list|,
name|id1
argument_list|)
decl_stmt|;
name|DbRowOp
name|op2
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id2
argument_list|)
argument_list|,
name|reflexive
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|DbRowOp
name|op3
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id3
argument_list|)
argument_list|,
name|reflexive
argument_list|,
name|id3
argument_list|)
decl_stmt|;
name|DbRowOp
name|op4
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id4
argument_list|)
argument_list|,
name|reflexive
argument_list|,
name|id4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|rows
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|op1
argument_list|,
name|op2
argument_list|,
name|op3
argument_list|,
name|op4
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sorted
init|=
name|sorter
operator|.
name|sort
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
comment|// no actual sorting is done
name|verify
argument_list|(
name|entitySorter
argument_list|)
comment|// should call entity sorter
operator|.
name|sortObjectsForEntity
argument_list|(
name|isNull
argument_list|()
argument_list|,
name|any
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Persistent
name|mockObject
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|Persistent
name|persistent
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|persistent
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|persistent
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
return|return
name|persistent
return|;
block|}
specifier|private
name|DbEntity
name|mockEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DbAttribute
name|attribute1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|attribute1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DbAttribute
name|attribute2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"attr"
argument_list|)
decl_stmt|;
name|DbEntity
name|testEntity
init|=
operator|new
name|DbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|testEntity
operator|.
name|addAttribute
argument_list|(
name|attribute1
argument_list|)
expr_stmt|;
name|testEntity
operator|.
name|addAttribute
argument_list|(
name|attribute2
argument_list|)
expr_stmt|;
return|return
name|testEntity
return|;
block|}
block|}
end_class

end_unit

