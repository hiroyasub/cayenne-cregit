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
name|crypto
operator|.
name|map
operator|.
name|ColumnMapper
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
name|map
operator|.
name|PatternColumnMapper
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
name|transformer
operator|.
name|bytes
operator|.
name|BytesTransformerFactory
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
name|transformer
operator|.
name|value
operator|.
name|BytesConverter
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
name|transformer
operator|.
name|value
operator|.
name|ValueTransformerFactory
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
name|Binder
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
name|MapBuilder
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
name|Module
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * A builder that allows to customize {@link CryptoModule} module.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CryptoModuleBuilder
block|{
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|ValueTransformerFactory
argument_list|>
name|valueTransformerFactoryType
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|BytesTransformerFactory
argument_list|>
name|bytesTransformerFactoryType
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|extraObjectToBytes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|extraDbToBytes
decl_stmt|;
specifier|private
name|String
name|columnMapperPattern
decl_stmt|;
specifier|private
name|ColumnMapper
name|columnMapper
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|ColumnMapper
argument_list|>
name|columnMapperType
decl_stmt|;
specifier|private
name|String
name|cipherAlgoritm
decl_stmt|;
specifier|private
name|String
name|cipherMode
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|CipherFactory
argument_list|>
name|cipherFactoryType
decl_stmt|;
specifier|private
name|URL
name|keyStoreUrl
decl_stmt|;
specifier|private
name|String
name|keyStoreUrlString
decl_stmt|;
specifier|private
name|File
name|keyStoreFile
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|KeySource
argument_list|>
name|keySourceType
decl_stmt|;
specifier|private
name|KeySource
name|keySource
decl_stmt|;
specifier|private
name|String
name|encryptionKeyAlias
decl_stmt|;
specifier|private
name|char
index|[]
name|keyPassword
decl_stmt|;
specifier|private
name|boolean
name|compress
decl_stmt|;
specifier|private
name|boolean
name|useHMAC
decl_stmt|;
comment|// use CryptoModule.builder() to create the builder...
specifier|protected
name|CryptoModuleBuilder
parameter_list|()
block|{
name|this
operator|.
name|extraDbToBytes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraObjectToBytes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CryptoModuleBuilder
name|cipherAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|cipherAlgoritm
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|cipherMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|cipherMode
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|mode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|cipherFactory
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|CipherFactory
argument_list|>
name|factoryType
parameter_list|)
block|{
name|this
operator|.
name|cipherFactoryType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|factoryType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|valueTransformer
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ValueTransformerFactory
argument_list|>
name|factoryType
parameter_list|)
block|{
name|this
operator|.
name|valueTransformerFactoryType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|factoryType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|CryptoModuleBuilder
name|objectToBytesConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|objectType
parameter_list|,
name|BytesConverter
argument_list|<
name|T
argument_list|>
name|converter
parameter_list|)
block|{
name|extraObjectToBytes
operator|.
name|put
argument_list|(
name|objectType
operator|.
name|getName
argument_list|()
argument_list|,
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|converter
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|dbToBytesConverter
parameter_list|(
name|int
name|sqlType
parameter_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
name|converter
parameter_list|)
block|{
name|extraDbToBytes
operator|.
name|put
argument_list|(
name|sqlType
argument_list|,
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|converter
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|bytesTransformer
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|BytesTransformerFactory
argument_list|>
name|factoryType
parameter_list|)
block|{
name|this
operator|.
name|bytesTransformerFactoryType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|factoryType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|columnMapper
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ColumnMapper
argument_list|>
name|columnMapperType
parameter_list|)
block|{
name|this
operator|.
name|columnMapperPattern
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|columnMapperType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|columnMapperType
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnMapper
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|columnMapper
parameter_list|(
name|ColumnMapper
name|columnMapper
parameter_list|)
block|{
name|this
operator|.
name|columnMapperPattern
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|columnMapperType
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|columnMapper
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|columnMapper
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|columnMapper
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|columnMapperPattern
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnMapperType
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|columnMapper
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * @param encryptionKeyAlias The name of the key in the keystore that should be used for      *                           encryption by default.      */
specifier|public
name|CryptoModuleBuilder
name|encryptionKeyAlias
parameter_list|(
name|String
name|encryptionKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|encryptionKeyAlias
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|encryptionKeyAlias
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures keystore parameters. The KeyStore must be of "jceks" type and      * contain all needed secret keys for the target database. Currently all      * keys must be protected with the same password.      *      * @param file               A file to load keystore from.      * @param passwordForAllKeys A password that unlocks all keys in the keystore.      * @param encryptionKeyAlias The name of the key in the keystore that should be used for      *                           encryption by default.      */
specifier|public
name|CryptoModuleBuilder
name|keyStore
parameter_list|(
name|File
name|file
parameter_list|,
name|char
index|[]
name|passwordForAllKeys
parameter_list|,
name|String
name|encryptionKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|encryptionKeyAlias
operator|=
name|encryptionKeyAlias
expr_stmt|;
name|this
operator|.
name|keyPassword
operator|=
name|passwordForAllKeys
expr_stmt|;
name|this
operator|.
name|keyStoreUrl
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|keyStoreUrlString
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|keyStoreFile
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures keystore parameters. The KeyStore must be of "jceks" type and      * contain all needed secret keys for the target database. Currently all      * keys must be protected with the same password.      *      * @param url                A URL to load keystore from.      * @param passwordForAllKeys A password that unlocks all keys in the keystore.      * @param encryptionKeyAlias The name of the key in the keystore that should be used for      *                           encryption by default.      */
specifier|public
name|CryptoModuleBuilder
name|keyStore
parameter_list|(
name|String
name|url
parameter_list|,
name|char
index|[]
name|passwordForAllKeys
parameter_list|,
name|String
name|encryptionKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|encryptionKeyAlias
operator|=
name|encryptionKeyAlias
expr_stmt|;
name|this
operator|.
name|keyPassword
operator|=
name|passwordForAllKeys
expr_stmt|;
name|this
operator|.
name|keyStoreUrl
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|keyStoreUrlString
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyStoreFile
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures keystore parameters. The KeyStore must be of "jceks" type and      * contain all needed secret keys for the target database. Currently all      * keys must be protected with the same password.      *      * @param url                A URL to load keystore from.      * @param passwordForAllKeys A password that unlocks all keys in the keystore.      * @param encryptionKeyAlias The name of the key in the keystore that should be used for      *                           encryption by default.      */
specifier|public
name|CryptoModuleBuilder
name|keyStore
parameter_list|(
name|URL
name|url
parameter_list|,
name|char
index|[]
name|passwordForAllKeys
parameter_list|,
name|String
name|encryptionKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|encryptionKeyAlias
operator|=
name|encryptionKeyAlias
expr_stmt|;
name|this
operator|.
name|keyPassword
operator|=
name|passwordForAllKeys
expr_stmt|;
name|this
operator|.
name|keyStoreUrl
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyStoreUrlString
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|keyStoreFile
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|keySource
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|KeySource
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|keySourceType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|keySource
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|keySource
parameter_list|(
name|KeySource
name|keySource
parameter_list|)
block|{
name|this
operator|.
name|keySourceType
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|keySource
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|keySource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CryptoModuleBuilder
name|compress
parameter_list|()
block|{
name|this
operator|.
name|compress
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enable authentication codes      */
specifier|public
name|CryptoModuleBuilder
name|useHMAC
parameter_list|()
block|{
name|this
operator|.
name|useHMAC
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Produces a module that can be used to start Cayenne runtime.      */
specifier|public
name|Module
name|build
parameter_list|()
block|{
return|return
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|MapBuilder
argument_list|<
name|String
argument_list|>
name|props
init|=
name|CryptoModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
decl_stmt|;
if|if
condition|(
name|cipherAlgoritm
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_ALGORITHM
argument_list|,
name|cipherAlgoritm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cipherMode
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_MODE
argument_list|,
name|cipherMode
argument_list|)
expr_stmt|;
block|}
name|String
name|keyStoreUrl
init|=
name|keyStoreUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyStoreUrl
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|KEYSTORE_URL
argument_list|,
name|keyStoreUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encryptionKeyAlias
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|ENCRYPTION_KEY_ALIAS
argument_list|,
name|encryptionKeyAlias
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|compress
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|COMPRESSION
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useHMAC
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|USE_HMAC
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyPassword
operator|!=
literal|null
condition|)
block|{
name|CryptoModule
operator|.
name|contributeCredentials
argument_list|(
name|binder
argument_list|)
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|KEY_PASSWORD
argument_list|,
name|keyPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cipherFactoryType
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|CipherFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|cipherFactoryType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|valueTransformerFactoryType
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ValueTransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|valueTransformerFactoryType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|extraDbToBytes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytesBinder
init|=
name|CryptoModule
operator|.
name|contributeDbToByteConverters
argument_list|(
name|binder
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|extraConverter
range|:
name|extraDbToBytes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|dbToBytesBinder
operator|.
name|put
argument_list|(
name|extraConverter
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|extraConverter
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|extraObjectToBytes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|objectToBytesBinder
init|=
name|CryptoModule
operator|.
name|contributeObjectToByteConverters
argument_list|(
name|binder
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|extraConverter
range|:
name|extraObjectToBytes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|objectToBytesBinder
operator|.
name|put
argument_list|(
name|extraConverter
operator|.
name|getKey
argument_list|()
argument_list|,
name|extraConverter
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bytesTransformerFactoryType
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|BytesTransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|bytesTransformerFactoryType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keySource
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|KeySource
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|keySource
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|keySourceType
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|KeySource
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|keySourceType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|columnMapperPattern
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ColumnMapper
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|PatternColumnMapper
argument_list|(
name|columnMapperPattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|columnMapperType
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ColumnMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|columnMapperType
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|columnMapper
operator|!=
literal|null
condition|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ColumnMapper
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|columnMapper
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
specifier|protected
name|String
name|keyStoreUrl
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|keyStoreUrl
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|keyStoreUrl
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
if|if
condition|(
name|this
operator|.
name|keyStoreUrlString
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|keyStoreUrlString
return|;
block|}
if|if
condition|(
name|keyStoreFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|keyStoreFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid keyStore file"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

