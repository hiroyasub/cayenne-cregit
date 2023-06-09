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
name|crypto
operator|.
name|transformer
operator|.
name|value
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
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
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
name|assertArrayEquals
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

begin_class
specifier|public
class|class
name|LocalDateTimeConverterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromBytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2015
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|11
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|,
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|2
block|,
literal|64
block|,
literal|58
block|,
literal|0
block|,
literal|0
block|,
literal|36
block|,
literal|4
block|,
operator|-
literal|113
block|,
literal|36
block|,
literal|116
block|,
literal|0
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToBytes
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|toBytes
argument_list|(
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2015
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|11
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|2
block|,
literal|64
block|,
literal|58
block|,
literal|0
block|,
literal|0
block|,
literal|36
block|,
literal|4
block|,
operator|-
literal|113
block|,
literal|36
block|,
literal|116
block|,
literal|0
block|}
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToBytesBig
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|toBytes
argument_list|(
name|LocalDateTime
operator|.
name|MAX
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|8
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|85
block|,
literal|10
block|,
literal|27
block|,
literal|72
block|,
operator|-
literal|9
block|,
literal|0
block|,
literal|0
block|,
literal|78
block|,
operator|-
literal|108
block|,
operator|-
literal|111
block|,
literal|78
block|,
operator|-
literal|1
block|,
operator|-
literal|1
block|}
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromBytesBig
parameter_list|()
block|{
name|LocalDateTime
name|localDateTime
init|=
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|8
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|85
block|,
literal|10
block|,
literal|27
block|,
literal|72
block|,
operator|-
literal|9
block|,
literal|0
block|,
literal|0
block|,
literal|78
block|,
operator|-
literal|108
block|,
operator|-
literal|111
block|,
literal|78
block|,
operator|-
literal|1
block|,
operator|-
literal|1
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LocalDateTime
operator|.
name|MAX
argument_list|,
name|localDateTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToBytesSmall
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|toBytes
argument_list|(
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|4
block|,
operator|-
literal|1
block|,
operator|-
literal|11
block|,
literal|5
block|,
literal|88
block|,
literal|0
block|}
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromBytesSmall
parameter_list|()
block|{
name|LocalDateTime
name|localDateTime
init|=
name|LocalDateTimeConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|4
block|,
operator|-
literal|1
block|,
operator|-
literal|11
block|,
literal|5
block|,
literal|88
block|,
literal|0
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
name|localDateTime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

