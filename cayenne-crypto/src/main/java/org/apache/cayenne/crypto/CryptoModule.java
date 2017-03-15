begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|access
operator|.
name|jdbc
operator|.
name|reader
operator|.
name|RowReaderFactory
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
name|access
operator|.
name|translator
operator|.
name|batch
operator|.
name|BatchTranslatorFactory
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
name|batch
operator|.
name|CryptoBatchTranslatorFactoryDecorator
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
name|cipher
operator|.
name|DefaultCipherFactory
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
name|JceksKeySource
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
name|reader
operator|.
name|CryptoRowReaderFactoryDecorator
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
name|DefaultTransformerFactory
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
name|TransformerFactory
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
name|bytes
operator|.
name|DefaultBytesTransformerFactory
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
name|LazyBytesTransformerFactory
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
name|Base64StringConverter
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
name|BigDecimalConverter
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
name|BigIntegerConverter
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
name|BooleanConverter
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
name|ByteConverter
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
name|BytesToBytesConverter
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
name|DefaultValueTransformerFactory
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
name|DoubleConverter
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
name|FloatConverter
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
name|IntegerConverter
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
name|LazyValueTransformerFactory
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
name|LongConverter
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
name|ShortConverter
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
name|Utf8StringConverter
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
name|UtilDateConverter
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
name|math
operator|.
name|BigDecimal
import|;
end_import

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
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Contains cryptography extensions for Cayenne.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CryptoModule
implements|implements
name|Module
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CIPHER_ALGORITHM
init|=
literal|"AES"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CIPHER_MODE
init|=
literal|"CBC"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CIPHER_PADDING
init|=
literal|"PKCS5Padding"
decl_stmt|;
comment|// same as default keystore password in java...
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|DEFAULT_KEY_PASSWORD
init|=
literal|"changeit"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_COLUMN_MAPPER_PATTERN
init|=
literal|"^CRYPTO_"
decl_stmt|;
specifier|public
specifier|static
name|CryptoModuleBuilder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|CryptoModuleBuilder
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|MapBuilder
argument_list|<
name|String
argument_list|>
name|contributeProperties
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindMap
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|CryptoConstants
operator|.
name|PROPERTIES_MAP
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|MapBuilder
argument_list|<
name|char
index|[]
argument_list|>
name|contributeCredentials
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindMap
argument_list|(
name|char
index|[]
operator|.
expr|class
argument_list|,
name|CryptoConstants
operator|.
name|CREDENTIALS_MAP
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|contributeDbToByteConverters
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|MapBuilder
name|mapBuilder
init|=
name|binder
operator|.
name|bindMap
argument_list|(
name|BytesConverter
operator|.
name|class
argument_list|,
name|DefaultValueTransformerFactory
operator|.
name|DB_TO_BYTE_CONVERTERS_KEY
argument_list|)
decl_stmt|;
return|return
operator|(
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
operator|)
name|mapBuilder
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|contributeObjectToByteConverters
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|MapBuilder
name|mapBuilder
init|=
name|binder
operator|.
name|bindMap
argument_list|(
name|BytesConverter
operator|.
name|class
argument_list|,
name|DefaultValueTransformerFactory
operator|.
name|OBJECT_TO_BYTE_CONVERTERS_KEY
argument_list|)
decl_stmt|;
return|return
operator|(
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
operator|)
name|mapBuilder
return|;
block|}
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
name|contributeProperties
argument_list|(
name|binder
argument_list|)
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_ALGORITHM
argument_list|,
name|DEFAULT_CIPHER_ALGORITHM
argument_list|)
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_MODE
argument_list|,
name|DEFAULT_CIPHER_MODE
argument_list|)
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|CIPHER_PADDING
argument_list|,
name|DEFAULT_CIPHER_PADDING
argument_list|)
expr_stmt|;
comment|// credentials are stored as char[] to potentially allow wiping them clean in memory...
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
name|DEFAULT_KEY_PASSWORD
argument_list|)
expr_stmt|;
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
name|DefaultCipherFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|TransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultTransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DefaultValueTransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytesBinder
init|=
name|contributeDbToByteConverters
argument_list|(
name|binder
argument_list|)
decl_stmt|;
name|contributeDefaultDbConverters
argument_list|(
name|dbToBytesBinder
argument_list|)
expr_stmt|;
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|objectToBytesBinder
init|=
name|contributeObjectToByteConverters
argument_list|(
name|binder
argument_list|)
decl_stmt|;
name|contributeDefaultObjectConverters
argument_list|(
name|objectToBytesBinder
argument_list|)
expr_stmt|;
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
name|DefaultBytesTransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|JceksKeySource
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DEFAULT_COLUMN_MAPPER_PATTERN
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
operator|.
name|before
argument_list|(
name|CryptoBatchTranslatorFactoryDecorator
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
operator|.
name|before
argument_list|(
name|CryptoRowReaderFactoryDecorator
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// decorate Crypto's own services to allow Cayenne to operate over plaintext entities even if crypto keys are
comment|// not available.
name|binder
operator|.
name|decorate
argument_list|(
name|ValueTransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|after
argument_list|(
name|LazyValueTransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|BytesTransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|after
argument_list|(
name|LazyBytesTransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|contributeDefaultDbConverters
parameter_list|(
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapBuilder
parameter_list|)
block|{
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGVARBINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|CHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NCLOB
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGNVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|contributeDefaultObjectConverters
parameter_list|(
name|MapBuilder
argument_list|<
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapBuilder
parameter_list|)
block|{
name|mapBuilder
operator|.
name|put
argument_list|(
literal|"byte[]"
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Utf8StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DoubleConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Double
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|DoubleConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FloatConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Float
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|FloatConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|LongConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Long
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|LongConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|IntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|IntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Short
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ByteConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|ByteConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BooleanConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|BooleanConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|UtilDateConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BigIntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|BigDecimal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BigDecimalConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

