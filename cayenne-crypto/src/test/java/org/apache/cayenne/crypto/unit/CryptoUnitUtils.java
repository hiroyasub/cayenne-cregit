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
name|unit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Cipher
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|key
operator|.
name|KeySource
import|;
end_import

begin_class
specifier|public
class|class
name|CryptoUnitUtils
block|{
specifier|public
specifier|static
name|byte
index|[]
name|hexToBytes
parameter_list|(
name|String
name|hexString
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|BigInteger
argument_list|(
name|hexString
argument_list|,
literal|16
argument_list|)
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
comment|// http://stackoverflow.com/questions/4407779/biginteger-to-byte
if|if
condition|(
name|bytes
operator|.
name|length
operator|>
literal|0
operator|&&
name|bytes
index|[
literal|0
index|]
operator|==
literal|0
condition|)
block|{
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|bytes
argument_list|,
literal|1
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|bytes
return|;
block|}
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decrypt_AES_CBC
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|ServerRuntime
name|runtime
parameter_list|)
block|{
try|try
block|{
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
name|int
name|blockSize
init|=
name|decCipher
operator|.
name|getBlockSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|keyNameBytes
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|source
argument_list|,
literal|0
argument_list|,
name|blockSize
argument_list|)
decl_stmt|;
name|byte
index|[]
name|ivBytes
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|source
argument_list|,
name|blockSize
argument_list|,
name|blockSize
operator|*
literal|2
argument_list|)
decl_stmt|;
name|byte
index|[]
name|cipherText
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|source
argument_list|,
name|blockSize
operator|*
literal|2
argument_list|,
name|source
operator|.
name|length
argument_list|)
decl_stmt|;
comment|// 'trim' is to get rid of 0 padding
name|String
name|keyName
init|=
operator|new
name|String
argument_list|(
name|keyNameBytes
argument_list|,
literal|"UTF-8"
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Key
name|key
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|KeySource
operator|.
name|class
argument_list|)
operator|.
name|getKey
argument_list|(
name|keyName
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
return|return
name|decCipher
operator|.
name|doFinal
argument_list|(
name|cipherText
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

