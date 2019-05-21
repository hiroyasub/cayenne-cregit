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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|CryptoUnitUtils
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
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_class
specifier|public
class|class
name|GzipEncryptorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGzip
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|input1
init|=
literal|"Hello Hello Hello"
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|output1
init|=
name|GzipEncryptor
operator|.
name|gzip
argument_list|(
name|input1
argument_list|)
decl_stmt|;
name|byte
index|[]
name|expectedOutput1
init|=
name|CryptoUnitUtils
operator|.
name|hexToBytes
argument_list|(
literal|"1f8b0800000000000000f348cdc9c957f0409000a91a078c11000000"
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expectedOutput1
argument_list|,
name|output1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncrypt
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
name|BytesEncryptor
name|delegate
init|=
name|mock
argument_list|(
name|BytesEncryptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|delegate
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
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
name|byte
index|[]
name|answer
init|=
operator|(
name|byte
index|[]
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|int
name|offset
init|=
operator|(
name|Integer
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|offset
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|GzipEncryptor
name|e
init|=
operator|new
name|GzipEncryptor
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|byte
index|[]
name|input1
init|=
literal|"Hello Hello Hello"
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|output1
init|=
name|e
operator|.
name|encrypt
argument_list|(
name|input1
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
name|byte
index|[]
name|expectedOutput1
init|=
name|input1
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expectedOutput1
argument_list|,
name|output1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|input2
init|=
operator|(
literal|"Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello "
operator|+
literal|"Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello AAAAA Hello"
operator|)
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|output2
init|=
name|e
operator|.
name|encrypt
argument_list|(
name|input2
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
comment|// somehow 'gzip -c' fills bytes 3..9 with values... the rest of the
comment|// gzip string is identical...
name|byte
index|[]
name|expectedOutput2
init|=
name|CryptoUnitUtils
operator|.
name|hexToBytes
argument_list|(
literal|"1f8b0800000000000000f348cdc9c957700401050f8ad9949b80c40600bbec62509b000000"
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expectedOutput2
argument_list|,
name|output2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

