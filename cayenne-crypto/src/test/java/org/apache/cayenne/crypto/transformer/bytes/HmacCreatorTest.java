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
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|anyByte
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
name|doAnswer
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
name|doReturn
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|HmacCreatorTest
block|{
comment|/**      * Sample output from https://en.wikipedia.org/wiki/Hash-based_message_authentication_code      */
annotation|@
name|Test
specifier|public
name|void
name|createHmac
parameter_list|()
block|{
specifier|final
name|byte
index|[]
name|headerData
init|=
literal|"The quick"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|Header
name|header
init|=
name|mock
argument_list|(
name|Header
operator|.
name|class
argument_list|)
decl_stmt|;
name|doReturn
argument_list|(
name|headerData
operator|.
name|length
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|byte
index|[]
name|input
init|=
operator|(
name|byte
index|[]
operator|)
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|headerData
argument_list|,
literal|0
argument_list|,
name|input
argument_list|,
literal|0
argument_list|,
name|headerData
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|)
operator|.
name|store
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
name|anyByte
argument_list|()
argument_list|)
expr_stmt|;
name|Key
name|key
init|=
operator|new
name|SecretKeySpec
argument_list|(
literal|"key"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|HmacCreator
name|creator
init|=
operator|new
name|HmacCreator
argument_list|(
name|header
argument_list|,
name|key
argument_list|)
block|{}
decl_stmt|;
name|byte
index|[]
name|hmac
init|=
name|creator
operator|.
name|createHmac
argument_list|(
literal|" brown fox jumps over the lazy dog"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|hmacExpected
init|=
name|CryptoUnitUtils
operator|.
name|hexToBytes
argument_list|(
literal|"f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8"
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|hmacExpected
argument_list|,
name|hmac
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

