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
name|cipher
package|;
end_package

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
name|Map
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
name|NoSuchPaddingException
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
name|di
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * Creates and returns a new {@link Cipher} configured using properties from  * {@link CryptoConstants#PROPERTIES_MAP}.  *   * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|DefaultCipherFactory
implements|implements
name|CipherFactory
block|{
specifier|protected
name|String
name|transformation
decl_stmt|;
specifier|protected
name|int
name|blockSize
decl_stmt|;
specifier|public
name|DefaultCipherFactory
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
parameter_list|)
block|{
name|String
name|algorithm
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|algorithm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Cipher algorithm is not set. Property name: "
operator|+
name|CryptoConstants
operator|.
name|CIPHER_ALGORITHM
argument_list|)
throw|;
block|}
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
name|String
name|padding
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_PADDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|padding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Cipher padding is not set. Property name: "
operator|+
name|CryptoConstants
operator|.
name|CIPHER_PADDING
argument_list|)
throw|;
block|}
name|this
operator|.
name|transformation
operator|=
name|algorithm
operator|+
literal|"/"
operator|+
name|mode
operator|+
literal|"/"
operator|+
name|padding
expr_stmt|;
name|this
operator|.
name|blockSize
operator|=
name|cipher
argument_list|()
operator|.
name|getBlockSize
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Cipher
name|cipher
parameter_list|()
block|{
try|try
block|{
return|return
name|Cipher
operator|.
name|getInstance
argument_list|(
name|transformation
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Error instantiating a cipher - no such algorithm: "
operator|+
name|transformation
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchPaddingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Error instantiating a cipher - no such padding: "
operator|+
name|transformation
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|blockSize
parameter_list|()
block|{
return|return
name|blockSize
return|;
block|}
block|}
end_class

end_unit

