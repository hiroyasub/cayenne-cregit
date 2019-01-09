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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ArrayUtilTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|sliceIntArray
parameter_list|()
block|{
name|int
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|int
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|int
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|int
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|int
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|int
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|int
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceLongArray
parameter_list|()
block|{
name|long
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|long
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|long
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|long
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|long
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|long
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|long
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|long
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceFloatArray
parameter_list|()
block|{
name|float
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|float
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|float
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|float
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|float
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|float
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|float
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|,
literal|0.000001f
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceDoubleArray
parameter_list|()
block|{
name|double
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|double
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|double
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|double
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|double
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|double
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|double
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|double
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|,
literal|0.000001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceCharArray
parameter_list|()
block|{
name|char
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|char
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|char
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|char
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|char
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|char
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|char
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceShortArray
parameter_list|()
block|{
name|short
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|short
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|short
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|short
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|short
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|short
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|short
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|short
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceByteArray
parameter_list|()
block|{
name|byte
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|byte
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|byte
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|byte
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|byte
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|byte
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|byte
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceBooleanArray
parameter_list|()
block|{
name|boolean
index|[]
name|array
init|=
block|{
literal|true
block|,
literal|true
block|,
literal|false
block|,
literal|true
block|,
literal|false
block|,
literal|false
block|,
literal|true
block|}
decl_stmt|;
name|boolean
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|true
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|boolean
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|boolean
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|boolean
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|boolean
index|[]
name|array2
init|=
block|{
literal|true
block|,
literal|true
block|,
literal|false
block|,
literal|true
block|,
literal|false
block|,
literal|false
block|,
literal|true
block|,
literal|false
block|}
decl_stmt|;
name|boolean
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|,
literal|false
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sliceObjectArray
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|Object
index|[]
index|[]
name|result
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|2
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|5
block|,
literal|6
block|}
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|7
block|}
argument_list|,
name|result
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
index|[]
name|result2
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
argument_list|,
name|result2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
index|[]
name|result3
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result3
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
index|[]
name|result4
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result4
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|array
argument_list|,
name|result4
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|array2
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|Object
index|[]
index|[]
name|result5
init|=
name|ArrayUtil
operator|.
name|sliceArray
argument_list|(
name|array2
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result5
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|result5
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|,
name|result5
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

