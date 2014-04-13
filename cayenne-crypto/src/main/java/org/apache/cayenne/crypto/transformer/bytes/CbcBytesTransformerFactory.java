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
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|cipher
operator|.
name|CipherFactory
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

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_class
class|class
name|CbcBytesTransformerFactory
implements|implements
name|BytesTransformerFactory
block|{
specifier|private
name|CipherFactory
name|cipherFactory
decl_stmt|;
specifier|private
name|Key
name|key
decl_stmt|;
specifier|private
name|Header
name|encryptionHeader
decl_stmt|;
specifier|private
name|int
name|blockSize
decl_stmt|;
specifier|private
name|KeySource
name|keySource
decl_stmt|;
specifier|private
name|Queue
argument_list|<
name|SecureRandom
argument_list|>
name|randoms
decl_stmt|;
name|CbcBytesTransformerFactory
parameter_list|(
name|CipherFactory
name|cipherFactory
parameter_list|,
name|KeySource
name|keySource
parameter_list|,
name|Header
name|encryptionHeader
parameter_list|)
block|{
name|this
operator|.
name|randoms
operator|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|SecureRandom
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|keySource
operator|=
name|keySource
expr_stmt|;
name|this
operator|.
name|cipherFactory
operator|=
name|cipherFactory
expr_stmt|;
name|this
operator|.
name|blockSize
operator|=
name|cipherFactory
operator|.
name|blockSize
argument_list|()
expr_stmt|;
name|this
operator|.
name|encryptionHeader
operator|=
name|encryptionHeader
expr_stmt|;
name|String
name|keyName
init|=
name|keySource
operator|.
name|getDefaultKeyAlias
argument_list|()
decl_stmt|;
name|this
operator|.
name|key
operator|=
name|keySource
operator|.
name|getKey
argument_list|(
name|keyName
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|byte
index|[]
name|generateSeedIv
parameter_list|()
block|{
name|byte
index|[]
name|iv
init|=
operator|new
name|byte
index|[
name|blockSize
index|]
decl_stmt|;
comment|// the idea of a queue of SecureRandoms for concurrency is taken from
comment|// Tomcat's SessionIdGenerator. Also some code...
name|SecureRandom
name|random
init|=
name|randoms
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|random
operator|==
literal|null
condition|)
block|{
name|random
operator|=
name|createSecureRandom
argument_list|()
expr_stmt|;
block|}
name|random
operator|.
name|nextBytes
argument_list|(
name|iv
argument_list|)
expr_stmt|;
name|randoms
operator|.
name|add
argument_list|(
name|random
argument_list|)
expr_stmt|;
return|return
name|iv
return|;
block|}
comment|/**      * Create a new random number generator instance we should use for      * generating session identifiers.      */
specifier|private
name|SecureRandom
name|createSecureRandom
parameter_list|()
block|{
comment|// TODO: allow to customize provider?
name|SecureRandom
name|result
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
comment|// Force seeding to take place
name|result
operator|.
name|nextInt
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|BytesEncryptor
name|encryptor
parameter_list|()
block|{
name|Cipher
name|cipher
init|=
name|cipherFactory
operator|.
name|cipher
argument_list|()
decl_stmt|;
name|BytesEncryptor
name|cbcEncryptor
init|=
operator|new
name|CbcEncryptor
argument_list|(
name|cipher
argument_list|,
name|key
argument_list|,
name|generateSeedIv
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|HeaderEncryptor
argument_list|(
name|cbcEncryptor
argument_list|,
name|encryptionHeader
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|BytesDecryptor
name|decryptor
parameter_list|()
block|{
name|Cipher
name|cipher
init|=
name|cipherFactory
operator|.
name|cipher
argument_list|()
decl_stmt|;
name|BytesDecryptor
name|cbcDecryptor
init|=
operator|new
name|CbcDecryptor
argument_list|(
name|cipher
argument_list|)
decl_stmt|;
comment|// TODO: make checking for key name an optional property
return|return
operator|new
name|HeaderDecryptor
argument_list|(
name|cbcDecryptor
argument_list|,
name|keySource
argument_list|)
return|;
block|}
block|}
end_class

end_unit

