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
name|util
operator|.
name|Map
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
name|CryptoConstants
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * A {@link BytesTransformerFactory} that creates transformers depending on the  * encryption mode specified via properties.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultBytesTransformerFactory
implements|implements
name|BytesTransformerFactory
block|{
specifier|private
name|BytesTransformerFactory
name|delegate
decl_stmt|;
specifier|static
name|Header
name|createEncryptionHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|KeySource
name|keySource
parameter_list|)
block|{
name|boolean
name|compressed
init|=
literal|"true"
operator|.
name|equals
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|COMPRESSION
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Header
operator|.
name|create
argument_list|(
name|keySource
operator|.
name|getDefaultKeyAlias
argument_list|()
argument_list|,
name|compressed
argument_list|)
return|;
block|}
specifier|public
name|DefaultBytesTransformerFactory
parameter_list|(
annotation|@
name|Inject
argument_list|(
name|CryptoConstants
operator|.
name|PROPERTIES_MAP
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
annotation|@
name|Inject
name|CipherFactory
name|cipherFactory
parameter_list|,
annotation|@
name|Inject
name|KeySource
name|keySource
parameter_list|)
block|{
name|Header
name|encryptionHeader
init|=
name|createEncryptionHeader
argument_list|(
name|properties
argument_list|,
name|keySource
argument_list|)
decl_stmt|;
name|String
name|mode
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Cipher mode is not set. Property name: "
operator|+
name|CryptoConstants
operator|.
name|CIPHER_MODE
argument_list|)
throw|;
block|}
if|if
condition|(
literal|"CBC"
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|CbcBytesTransformerFactory
argument_list|(
name|cipherFactory
argument_list|,
name|keySource
argument_list|,
name|encryptionHeader
argument_list|)
expr_stmt|;
block|}
comment|// TODO: ECB and other modes...
else|else
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Unsupported mode: "
operator|+
name|mode
operator|+
literal|". The following modes are currently supported:  CBC"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|BytesEncryptor
name|encryptor
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|encryptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|BytesDecryptor
name|decryptor
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|decryptor
argument_list|()
return|;
block|}
block|}
end_class

end_unit

