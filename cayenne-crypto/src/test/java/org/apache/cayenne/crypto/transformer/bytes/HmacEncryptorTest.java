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
name|bytes
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
name|crypto
operator|.
name|unit
operator|.
name|SwapBytesTransformer
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
name|any
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
name|anyInt
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|HmacEncryptorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|encrypt
parameter_list|()
throws|throws
name|Exception
block|{
name|HmacEncryptor
name|encryptor
init|=
name|mock
argument_list|(
name|HmacEncryptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|encryptor
operator|.
name|delegate
operator|=
name|SwapBytesTransformer
operator|.
name|encryptor
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|encryptor
operator|.
name|createHmac
argument_list|(
name|any
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0
block|,
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
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|encryptor
operator|.
name|encrypt
argument_list|(
name|any
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|anyInt
argument_list|()
argument_list|,
name|any
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenCallRealMethod
argument_list|()
expr_stmt|;
name|byte
index|[]
name|input
init|=
block|{
operator|-
literal|1
block|,
operator|-
literal|2
block|,
operator|-
literal|3
block|}
decl_stmt|;
name|byte
index|[]
name|result1
init|=
name|encryptor
operator|.
name|encrypt
argument_list|(
name|input
argument_list|,
literal|0
argument_list|,
operator|new
name|byte
index|[
literal|1
index|]
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
operator|-
literal|3
block|,
operator|-
literal|2
block|,
operator|-
literal|1
block|}
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|result2
init|=
name|encryptor
operator|.
name|encrypt
argument_list|(
name|input
argument_list|,
literal|5
argument_list|,
operator|new
name|byte
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|8
block|,
literal|0
block|,
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
operator|-
literal|3
block|,
operator|-
literal|2
block|,
operator|-
literal|1
block|}
argument_list|,
name|result2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

