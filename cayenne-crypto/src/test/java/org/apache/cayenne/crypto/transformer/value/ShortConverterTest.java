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
name|ShortConverterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromBytes_InByteRange
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|6
argument_list|)
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|6
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromBytes_InShortRange
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|1287
argument_list|)
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|7
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFromBytes_TooLong
parameter_list|()
block|{
name|ShortConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|6
block|,
literal|5
block|,
literal|4
block|}
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
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|127
block|,
operator|-
literal|2
block|}
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
operator|.
name|toBytes
argument_list|(
operator|(
name|short
operator|)
operator|(
name|Short
operator|.
name|MAX_VALUE
operator|-
literal|1
operator|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|-
literal|7
block|}
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
operator|.
name|toBytes
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|7
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

