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
name|modeler
operator|.
name|util
package|;
end_package

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

begin_class
specifier|public
class|class
name|CircularArrayTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testArraySize5
parameter_list|()
block|{
name|String
name|a
init|=
literal|"A"
decl_stmt|,
name|b
init|=
literal|"B"
decl_stmt|,
name|c
init|=
literal|"C"
decl_stmt|,
name|d
init|=
literal|"D"
decl_stmt|,
name|e
init|=
literal|"E"
decl_stmt|,
name|f
init|=
literal|"F"
decl_stmt|,
name|g
init|=
literal|"G"
decl_stmt|,
name|h
init|=
literal|"H"
decl_stmt|;
name|CircularArray
argument_list|<
name|String
argument_list|>
name|q
init|=
operator|new
name|CircularArray
argument_list|<>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|a
argument_list|,
literal|"[A, null, null, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|a
argument_list|,
literal|"[null, null, null, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|a
argument_list|,
literal|"[A, null, null, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|b
argument_list|,
literal|"[A, B, null, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|b
argument_list|,
literal|"[A, null, null, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|b
argument_list|,
literal|"[A, B, null, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|c
argument_list|,
literal|"[A, B, C, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|c
argument_list|,
literal|"[A, B, null, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|c
argument_list|,
literal|"[A, B, C, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|d
argument_list|,
literal|"[A, B, C, D, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|d
argument_list|,
literal|"[A, B, C, null, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|d
argument_list|,
literal|"[A, B, C, D, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|e
argument_list|,
literal|"[A, B, C, D, E]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|e
argument_list|,
literal|"[A, B, C, D, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|e
argument_list|,
literal|"[A, B, C, D, E]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|f
argument_list|,
literal|"[B, C, D, E, F]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|f
argument_list|,
literal|"[B, C, D, E, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|f
argument_list|,
literal|"[B, C, D, E, F]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|g
argument_list|,
literal|"[C, D, E, F, G]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|e
argument_list|,
literal|"[C, D, F, G, null]"
argument_list|)
expr_stmt|;
name|assertAdd
argument_list|(
name|q
argument_list|,
name|h
argument_list|,
literal|"[C, D, F, G, H]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|c
argument_list|,
literal|"[D, F, G, H, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|h
argument_list|,
literal|"[D, F, G, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|f
argument_list|,
literal|"[D, G, null, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|g
argument_list|,
literal|"[D, null, null, null, null]"
argument_list|)
expr_stmt|;
name|assertRemove
argument_list|(
name|q
argument_list|,
name|d
argument_list|,
literal|"[null, null, null, null, null]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArraySize3
parameter_list|()
block|{
name|String
name|a
init|=
literal|"A"
decl_stmt|,
name|b
init|=
literal|"B"
decl_stmt|,
name|c
init|=
literal|"C"
decl_stmt|,
name|d
init|=
literal|"D"
decl_stmt|,
name|e
init|=
literal|"E"
decl_stmt|;
name|CircularArray
argument_list|<
name|String
argument_list|>
name|q
init|=
operator|new
name|CircularArray
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|q
operator|.
name|capacity
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"A should not be in the q"
argument_list|,
name|q
operator|.
name|contains
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|d
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// should be the same after resizing
name|q
operator|.
name|resize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|q
operator|.
name|capacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|q
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|indexOf
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
argument_list|,
name|q
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|resize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|q
operator|.
name|capacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|CircularArray
argument_list|<
name|String
argument_list|>
name|a
init|=
operator|new
name|CircularArray
argument_list|<>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"[null, null, null, null, null]"
argument_list|,
name|a
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAdd
parameter_list|(
name|CircularArray
argument_list|<
name|String
argument_list|>
name|a
parameter_list|,
name|String
name|obj
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
name|a
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|a
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertRemove
parameter_list|(
name|CircularArray
argument_list|<
name|String
argument_list|>
name|a
parameter_list|,
name|String
name|obj
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
name|a
operator|.
name|indexOf
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|a
operator|.
name|remove
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|a
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

