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
name|ShortBufferException
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
name|crypto
operator|.
name|CayenneCryptoException
import|;
end_import

begin_comment
comment|/**  * A {@link BytesDecryptor} that decrypts the provided bytes that were encrypted  * by the complimentary {@link CbcEncryptor}. The object is stateful and is not  * thread-safe.  *   * @since 4.0  */
end_comment

begin_class
class|class
name|CbcDecryptor
implements|implements
name|BytesDecryptor
block|{
specifier|private
name|Cipher
name|cipher
decl_stmt|;
specifier|private
name|int
name|blockSize
decl_stmt|;
name|CbcDecryptor
parameter_list|(
name|Cipher
name|cipher
parameter_list|)
block|{
name|this
operator|.
name|cipher
operator|=
name|cipher
expr_stmt|;
name|this
operator|.
name|blockSize
operator|=
name|cipher
operator|.
name|getBlockSize
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|decrypt
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|inputOffset
parameter_list|,
name|Key
name|key
parameter_list|)
block|{
try|try
block|{
return|return
name|doDecrypt
argument_list|(
name|input
argument_list|,
name|inputOffset
argument_list|,
name|key
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
name|CayenneCryptoException
argument_list|(
literal|"Error on decryption"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|byte
index|[]
name|doDecrypt
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|inputOffset
parameter_list|,
name|Key
name|key
parameter_list|)
throws|throws
name|InvalidKeyException
throws|,
name|InvalidAlgorithmParameterException
throws|,
name|ShortBufferException
throws|,
name|IllegalBlockSizeException
throws|,
name|BadPaddingException
block|{
name|IvParameterSpec
name|iv
init|=
name|iv
argument_list|(
name|input
argument_list|,
name|inputOffset
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|,
name|iv
argument_list|)
expr_stmt|;
name|int
name|offset
init|=
name|inputOffset
operator|+
name|blockSize
decl_stmt|;
return|return
name|cipher
operator|.
name|doFinal
argument_list|(
name|input
argument_list|,
name|offset
argument_list|,
name|input
operator|.
name|length
operator|-
name|offset
argument_list|)
return|;
block|}
name|IvParameterSpec
name|iv
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|inputOffset
parameter_list|)
block|{
return|return
operator|new
name|IvParameterSpec
argument_list|(
name|input
argument_list|,
name|inputOffset
argument_list|,
name|blockSize
argument_list|)
return|;
block|}
block|}
end_class

end_unit

