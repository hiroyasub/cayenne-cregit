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
name|Mockito
operator|.
name|mock
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
name|java
operator|.
name|security
operator|.
name|InvalidAlgorithmParameterException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidKeyException
import|;
end_import

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
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|BadPaddingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|IllegalBlockSizeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|NoSuchPaddingException
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
name|IvParameterSpec
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
name|CayenneCryptoException
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

begin_class
specifier|public
class|class
name|CbcEncryptorTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneCryptoException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|NoSuchAlgorithmException
throws|,
name|NoSuchPaddingException
block|{
name|byte
index|[]
name|iv
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
block|}
decl_stmt|;
name|Key
name|key
init|=
name|mock
argument_list|(
name|Key
operator|.
name|class
argument_list|)
decl_stmt|;
name|Cipher
name|cipher
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|cipher
operator|.
name|getBlockSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// must throw as IV sie and block size are different
operator|new
name|CbcEncryptor
argument_list|(
name|cipher
argument_list|,
name|key
argument_list|,
name|iv
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncrypt_AES
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|NoSuchPaddingException
throws|,
name|InvalidKeyException
throws|,
name|InvalidAlgorithmParameterException
throws|,
name|IllegalBlockSizeException
throws|,
name|BadPaddingException
block|{
name|byte
index|[]
name|ivBytes
init|=
name|CryptoUnitUtils
operator|.
name|hexToBytes
argument_list|(
literal|"0591849d87c93414f4405d32f4d69220"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|keyBytes
init|=
name|CryptoUnitUtils
operator|.
name|hexToBytes
argument_list|(
literal|"a4cb499fa31a6a228e16b7e4741d4fa3"
argument_list|)
decl_stmt|;
name|Key
name|key
init|=
operator|new
name|SecretKeySpec
argument_list|(
name|keyBytes
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|cipher
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES/CBC/PKCS5Padding"
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|cipher
operator|.
name|getBlockSize
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|plain
init|=
block|{
literal|21
block|,
literal|20
block|,
literal|19
block|,
literal|18
block|,
literal|17
block|,
literal|16
block|,
literal|15
block|,
literal|14
block|,
literal|13
block|,
literal|12
block|,
literal|11
block|,
literal|10
block|,
literal|9
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
decl_stmt|;
comment|// copy ivBytes, as they are reset
name|CbcEncryptor
name|encryptor
init|=
operator|new
name|CbcEncryptor
argument_list|(
name|cipher
argument_list|,
name|key
argument_list|,
name|ivBytes
argument_list|)
decl_stmt|;
name|byte
index|[]
name|encrypted
init|=
name|encryptor
operator|.
name|encrypt
argument_list|(
name|plain
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
name|assertEquals
argument_list|(
literal|16
operator|*
literal|3
argument_list|,
name|encrypted
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|ivBytes
argument_list|,
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|encrypted
argument_list|,
literal|0
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
name|Cipher
name|decCipher
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES/CBC/PKCS5Padding"
argument_list|)
decl_stmt|;
name|decCipher
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|,
operator|new
name|IvParameterSpec
argument_list|(
name|ivBytes
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|newPlain
init|=
name|decCipher
operator|.
name|doFinal
argument_list|(
name|encrypted
argument_list|,
literal|16
argument_list|,
name|encrypted
operator|.
name|length
operator|-
literal|16
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|plain
argument_list|,
name|newPlain
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

