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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|GraphDiff
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
name|graph
operator|.
name|NodeCreateOperation
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
name|util
operator|.
name|Util
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

begin_class
specifier|public
class|class
name|ObjectContextChangeLogTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReset
parameter_list|()
block|{
name|ObjectContextChangeLog
name|recorder
init|=
operator|new
name|ObjectContextChangeLog
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
operator|.
name|isNoop
argument_list|()
argument_list|)
expr_stmt|;
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
operator|.
name|isNoop
argument_list|()
argument_list|)
expr_stmt|;
name|recorder
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|recorder
operator|.
name|getDiffs
argument_list|()
operator|.
name|isNoop
argument_list|()
argument_list|)
expr_stmt|;
comment|// now test that a diff stored before "clear" is not affected by 'clear'
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|GraphDiff
name|diff
init|=
name|recorder
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|diff
operator|.
name|isNoop
argument_list|()
argument_list|)
expr_stmt|;
name|recorder
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|diff
operator|.
name|isNoop
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDiffs
parameter_list|()
block|{
comment|// assert that after returning, the diffs array won't get modified by
comment|// operation
comment|// recorder
name|ObjectContextChangeLog
name|recorder
init|=
operator|new
name|ObjectContextChangeLog
argument_list|()
decl_stmt|;
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CompoundDiff
name|diff
init|=
operator|(
name|CompoundDiff
operator|)
name|recorder
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diff
operator|.
name|getDiffs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diff
operator|.
name|getDiffs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CompoundDiff
name|diff2
init|=
operator|(
name|CompoundDiff
operator|)
name|recorder
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|diff2
operator|.
name|getDiffs
argument_list|()
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
name|testGetDiffsSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContextChangeLog
name|recorder
init|=
operator|new
name|ObjectContextChangeLog
argument_list|()
decl_stmt|;
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CompoundDiff
name|diff
init|=
operator|(
name|CompoundDiff
operator|)
name|recorder
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|Object
name|clone
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|diff
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clone
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|instanceof
name|CompoundDiff
argument_list|)
expr_stmt|;
name|CompoundDiff
name|d1
init|=
operator|(
name|CompoundDiff
operator|)
name|clone
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|d1
operator|.
name|getDiffs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

