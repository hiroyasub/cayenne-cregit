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
operator|.
name|operation
package|;
end_package

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
name|Mockito
operator|.
name|mock
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
name|when
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|DbRowOpMergerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMergeUpdateDelete
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|UpdateDbRowOp
name|row1
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|DeleteDbRowOp
name|row2
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row1
argument_list|,
name|row2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row2
argument_list|,
name|row1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMergeInsertDelete
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|InsertDbRowOp
name|row1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|DeleteDbRowOp
name|row2
init|=
operator|new
name|DeleteDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row1
argument_list|,
name|row2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMergeUpdateInsert
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|UpdateDbRowOp
name|row1
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|InsertDbRowOp
name|row2
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row1
argument_list|,
name|row2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row2
argument_list|,
name|row1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row1
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMergeInsertInsert
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"attr1"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"attr2"
argument_list|)
decl_stmt|;
name|InsertDbRowOp
name|row1
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|row1
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attr1
argument_list|,
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|InsertDbRowOp
name|row2
init|=
operator|new
name|InsertDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|row2
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attr2
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row1
argument_list|,
name|row2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|(
operator|(
name|InsertDbRowOp
operator|)
name|row
operator|)
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row2
argument_list|,
name|row1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row1
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|(
operator|(
name|InsertDbRowOp
operator|)
name|row
operator|)
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMergeUpdateUpdate
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"attr1"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"attr2"
argument_list|)
decl_stmt|;
name|UpdateDbRowOp
name|row1
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|row1
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attr1
argument_list|,
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|UpdateDbRowOp
name|row2
init|=
operator|new
name|UpdateDbRowOp
argument_list|(
name|mockObject
argument_list|(
name|id
argument_list|)
argument_list|,
name|mockEntity
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|row2
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attr2
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row1
argument_list|,
name|row2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row2
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|(
operator|(
name|UpdateDbRowOp
operator|)
name|row
operator|)
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|{
name|DbRowOpMerger
name|merger
init|=
operator|new
name|DbRowOpMerger
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|merger
operator|.
name|apply
argument_list|(
name|row2
argument_list|,
name|row1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|row1
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
init|=
operator|(
operator|(
name|UpdateDbRowOp
operator|)
name|row
operator|)
operator|.
name|getValues
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"attr2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
parameter_list|()
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
literal|"TEST"
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

