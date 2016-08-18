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
name|value
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
name|dba
operator|.
name|TypesMapping
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|map
operator|.
name|ObjAttribute
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
name|map
operator|.
name|ObjEntity
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
name|Collection
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
name|HashSet
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * A {@link ValueTransformerFactory} that creates encryptors/decryptors that are  * taking advantage of the JCE (Java Cryptography Extension) ciphers.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultValueTransformerFactory
implements|implements
name|ValueTransformerFactory
block|{
specifier|private
specifier|final
name|Key
name|defaultKey
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|objectToBytes
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytes
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|bytesToObject
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|bytesToDb
decl_stmt|;
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|DbAttribute
argument_list|,
name|ValueEncryptor
argument_list|>
name|encryptors
decl_stmt|;
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|DbAttribute
argument_list|,
name|ValueDecryptor
argument_list|>
name|decryptors
decl_stmt|;
specifier|public
name|DefaultValueTransformerFactory
parameter_list|(
annotation|@
name|Inject
name|KeySource
name|keySource
parameter_list|)
block|{
name|this
operator|.
name|defaultKey
operator|=
name|keySource
operator|.
name|getKey
argument_list|(
name|keySource
operator|.
name|getDefaultKeyAlias
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|encryptors
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|DbAttribute
argument_list|,
name|ValueEncryptor
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|decryptors
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|DbAttribute
argument_list|,
name|ValueDecryptor
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|objectToBytes
operator|=
name|createObjectToBytesConverters
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbToBytes
operator|=
name|createDbToBytesConverters
argument_list|()
expr_stmt|;
name|this
operator|.
name|bytesToObject
operator|=
name|createBytesToObjectConverters
argument_list|()
expr_stmt|;
name|this
operator|.
name|bytesToDb
operator|=
name|createBytesToDbConverters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ValueDecryptor
name|decryptor
parameter_list|(
name|DbAttribute
name|a
parameter_list|)
block|{
name|ValueDecryptor
name|e
init|=
name|decryptors
operator|.
name|get
argument_list|(
name|a
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|ValueDecryptor
name|newTransformer
init|=
name|createDecryptor
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|oldTransformer
init|=
name|decryptors
operator|.
name|putIfAbsent
argument_list|(
name|a
argument_list|,
name|newTransformer
argument_list|)
decl_stmt|;
name|e
operator|=
name|oldTransformer
operator|!=
literal|null
condition|?
name|oldTransformer
else|:
name|newTransformer
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
annotation|@
name|Override
specifier|public
name|ValueEncryptor
name|encryptor
parameter_list|(
name|DbAttribute
name|a
parameter_list|)
block|{
name|ValueEncryptor
name|e
init|=
name|encryptors
operator|.
name|get
argument_list|(
name|a
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|ValueEncryptor
name|newTransformer
init|=
name|createEncryptor
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|ValueEncryptor
name|oldTransformer
init|=
name|encryptors
operator|.
name|putIfAbsent
argument_list|(
name|a
argument_list|,
name|newTransformer
argument_list|)
decl_stmt|;
name|e
operator|=
name|oldTransformer
operator|!=
literal|null
condition|?
name|oldTransformer
else|:
name|newTransformer
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|createDbToBytesConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGVARBINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|CHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NCLOB
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGNVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|createBytesToDbConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGVARBINARY
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|CHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NCLOB
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|LONGNVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Types
operator|.
name|NVARCHAR
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|createObjectToBytesConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
return|return
name|map
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|createBytesToObjectConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
name|map
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
return|return
name|map
return|;
block|}
specifier|protected
name|ValueEncryptor
name|createEncryptor
parameter_list|(
name|DbAttribute
name|a
parameter_list|)
block|{
name|String
name|type
init|=
name|getJavaType
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|BytesConverter
argument_list|<
name|?
argument_list|>
name|toBytes
init|=
name|objectToBytes
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|toBytes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The type "
operator|+
name|type
operator|+
literal|" for attribute "
operator|+
name|a
operator|+
literal|" has no object-to-bytes conversion"
argument_list|)
throw|;
block|}
name|BytesConverter
argument_list|<
name|?
argument_list|>
name|fromBytes
init|=
name|bytesToDb
operator|.
name|get
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromBytes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The type "
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
operator|+
literal|" for attribute "
operator|+
name|a
operator|+
literal|" has no bytes-to-db conversion"
argument_list|)
throw|;
block|}
return|return
operator|new
name|DefaultValueEncryptor
argument_list|(
name|toBytes
argument_list|,
name|fromBytes
argument_list|)
return|;
block|}
specifier|protected
name|ValueDecryptor
name|createDecryptor
parameter_list|(
name|DbAttribute
name|a
parameter_list|)
block|{
name|BytesConverter
argument_list|<
name|?
argument_list|>
name|toBytes
init|=
name|dbToBytes
operator|.
name|get
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|toBytes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The type "
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
operator|+
literal|" for attribute "
operator|+
name|a
operator|+
literal|" has no db-to-bytes conversion"
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|getJavaType
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|BytesConverter
argument_list|<
name|?
argument_list|>
name|fromBytes
init|=
name|bytesToObject
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromBytes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The type "
operator|+
name|type
operator|+
literal|" for attribute "
operator|+
name|a
operator|+
literal|" has no bytes-to-object conversion"
argument_list|)
throw|;
block|}
return|return
operator|new
name|DefaultValueDecryptor
argument_list|(
name|toBytes
argument_list|,
name|fromBytes
argument_list|,
name|defaultKey
argument_list|)
return|;
block|}
comment|// TODO: calculating Java type of ObjAttribute may become unneeded per
comment|// CAY-1752, as DbAttribute will have it.
specifier|protected
name|String
name|getJavaType
parameter_list|(
name|DbAttribute
name|a
parameter_list|)
block|{
name|DbEntity
name|dbEntity
init|=
name|a
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|dbEntity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
init|=
name|dataMap
operator|.
name|getMappedEntities
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntities
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|javaTypes
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ObjEntity
name|objEntity
init|=
name|objEntities
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|oa
range|:
name|objEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// TODO: this won't pick up flattened attributes
if|if
condition|(
name|a
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|oa
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
condition|)
block|{
name|javaTypes
operator|.
name|add
argument_list|(
name|oa
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|javaTypes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
return|return
name|javaTypes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
end_class

end_unit

