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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Collection
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
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultValueTransformerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DB_TO_BYTE_CONVERTERS_KEY
init|=
literal|"org.apache.cayenne.crypto.transformer.value.DefaultValueTransformerFactory.dbToBytes"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_TO_BYTE_CONVERTERS_KEY
init|=
literal|"org.apache.cayenne.crypto.transformer.value.DefaultValueTransformerFactory.objectToBytes"
decl_stmt|;
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
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|DB_TO_BYTE_CONVERTERS_KEY
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|OBJECT_TO_BYTE_CONVERTERS_KEY
argument_list|)
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
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|decryptors
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|objectToBytes
operator|=
name|objectToBytes
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
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
name|dbToBytes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|extraConverter
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|extraConverter
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|dbToBytes
operator|=
name|m
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
name|objectToBytes
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
name|String
argument_list|>
name|javaTypes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|dataMap
operator|.
name|getMappedEntities
argument_list|(
name|dbEntity
argument_list|)
control|)
block|{
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
name|String
name|javaType
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|String
name|attributeName
init|=
name|dbEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|a
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|msg
init|=
name|javaTypes
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|?
literal|"ObjAttributes with different java types"
else|:
literal|"No ObjAttributes"
decl_stmt|;
comment|// Warn user about this problem as there is nothing else we can do
name|logger
operator|.
name|warn
argument_list|(
name|msg
operator|+
literal|" bound to DbAttribute '"
operator|+
name|attributeName
operator|+
literal|"', "
operator|+
name|javaType
operator|+
literal|" type will be used."
argument_list|)
expr_stmt|;
return|return
name|javaType
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

