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
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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

begin_class
specifier|public
class|class
name|HeaderEncryptorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTransform
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
name|Header
name|encryptionHeader
init|=
name|Header
operator|.
name|create
argument_list|(
literal|"mykey"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BytesEncryptor
name|delegate
init|=
name|SwapBytesTransformer
operator|.
name|encryptor
argument_list|()
decl_stmt|;
name|byte
index|[]
name|input
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
comment|// intentionally non-standard block size..
name|HeaderEncryptor
name|encryptor
init|=
operator|new
name|HeaderEncryptor
argument_list|(
name|delegate
argument_list|,
name|encryptionHeader
argument_list|)
decl_stmt|;
name|byte
index|[]
name|output1
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
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|10
block|,
literal|0
block|,
literal|'m'
block|,
literal|'y'
block|,
literal|'k'
block|,
literal|'e'
block|,
literal|'y'
block|,
literal|8
block|,
literal|7
block|,
literal|6
block|,
literal|5
block|,
literal|4
block|,
literal|3
block|,
literal|2
block|,
literal|1
block|}
argument_list|,
name|output1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|output2
init|=
name|encryptor
operator|.
name|encrypt
argument_list|(
name|input
argument_list|,
literal|1
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
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|10
block|,
literal|0
block|,
literal|'m'
block|,
literal|'y'
block|,
literal|'k'
block|,
literal|'e'
block|,
literal|'y'
block|,
literal|8
block|,
literal|7
block|,
literal|6
block|,
literal|5
block|,
literal|4
block|,
literal|3
block|,
literal|2
block|,
literal|1
block|}
argument_list|,
name|output2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

