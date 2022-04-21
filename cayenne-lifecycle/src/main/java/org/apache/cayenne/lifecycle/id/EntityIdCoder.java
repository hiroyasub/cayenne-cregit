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
name|lifecycle
operator|.
name|id
package|;
end_package

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
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|CayenneRuntimeException
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
name|ObjectId
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
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|IDUtil
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * An object to encode/decode ObjectIds for a single mapped entity.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|EntityIdCoder
block|{
specifier|static
specifier|final
name|String
name|ID_SEPARATOR
init|=
literal|":"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TEMP_ID_PREFIX
init|=
literal|"."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TEMP_PREFIX_LENGTH
init|=
name|TEMP_ID_PREFIX
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|private
name|String
name|entityName
decl_stmt|;
specifier|private
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Converter
argument_list|<
name|?
argument_list|>
argument_list|>
name|converters
decl_stmt|;
specifier|private
name|int
name|idSize
decl_stmt|;
specifier|public
specifier|static
name|String
name|getEntityName
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|int
name|separator
init|=
name|id
operator|.
name|indexOf
argument_list|(
name|ID_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|separator
operator|<=
literal|0
operator|||
name|separator
operator|==
name|id
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid String id: "
operator|+
name|id
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|id
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|separator
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|TEMP_ID_PREFIX
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|TEMP_PREFIX_LENGTH
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
name|EntityIdCoder
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entity
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|converters
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjAttribute
name|attribute
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|converters
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getDbAttributeName
argument_list|()
argument_list|,
name|create
argument_list|(
name|attribute
operator|.
name|getJavaClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|converters
operator|.
name|containsKey
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
try|try
block|{
name|converters
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|create
argument_list|(
name|Util
operator|.
name|getJavaClass
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't instantiate class "
operator|+
name|type
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|converters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Entity has no PK defined: "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|idSize
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|converters
operator|.
name|size
argument_list|()
operator|/
literal|0.75d
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a consistent String representation of the ObjectId      */
specifier|public
name|String
name|toStringId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
comment|// deal with temp that have attached replacement ID as permanent IDs...
comment|// AuditableFilter, etc. all rely on the ability to find the temp object
comment|// after the transaction end
comment|// TODO: support encoding format for temp+replacement
if|if
condition|(
name|id
operator|.
name|isTemporary
argument_list|()
operator|&&
operator|!
name|id
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
return|return
name|toTempIdString
argument_list|(
name|id
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toPermIdString
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|toTempIdString
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|TEMP_ID_PREFIX
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ID_SEPARATOR
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|id
operator|.
name|getKey
argument_list|()
control|)
block|{
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|toPermIdString
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idValues
init|=
name|id
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Converter
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|converters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|idValues
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ID_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toUuid
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|ObjectId
name|toObjectId
parameter_list|(
name|String
name|stringId
parameter_list|)
block|{
if|if
condition|(
name|stringId
operator|.
name|startsWith
argument_list|(
name|TEMP_ID_PREFIX
argument_list|)
condition|)
block|{
name|String
name|idValues
init|=
name|stringId
operator|.
name|substring
argument_list|(
name|entityName
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|+
name|TEMP_PREFIX_LENGTH
argument_list|)
decl_stmt|;
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|entityName
argument_list|,
name|decodeTemp
argument_list|(
name|idValues
argument_list|)
argument_list|)
return|;
block|}
name|String
name|idValues
init|=
name|stringId
operator|.
name|substring
argument_list|(
name|entityName
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|converters
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Entry
argument_list|<
name|String
argument_list|,
name|Converter
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
init|=
name|converters
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|decoded
decl_stmt|;
try|try
block|{
name|decoded
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|idValues
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// unexpected
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|entityName
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|fromStringId
argument_list|(
name|decoded
argument_list|)
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|idSize
argument_list|)
decl_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|idValues
argument_list|,
name|ID_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|toks
operator|.
name|countTokens
argument_list|()
operator|!=
name|converters
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid String ID for entity "
operator|+
name|entityName
operator|+
literal|": "
operator|+
name|idValues
argument_list|)
throw|;
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Converter
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|converters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|decoded
decl_stmt|;
try|try
block|{
name|decoded
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// unexpected
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|idMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|fromStringId
argument_list|(
name|decoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|entityName
argument_list|,
name|idMap
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|decodeTemp
parameter_list|(
name|String
name|byteString
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|byteString
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|index
init|=
name|i
operator|*
literal|2
decl_stmt|;
comment|// this is better than Byte.parseByte which can't parse values>=
comment|// 128 as negative bytes
name|int
name|c1
init|=
name|byteString
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|int
name|c2
init|=
name|byteString
operator|.
name|charAt
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|bytes
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
operator|(
operator|(
name|Character
operator|.
name|digit
argument_list|(
name|c1
argument_list|,
literal|16
argument_list|)
operator|<<
literal|4
operator|)
operator|+
name|Character
operator|.
name|digit
argument_list|(
name|c2
argument_list|,
literal|16
argument_list|)
operator|)
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
specifier|private
name|Converter
argument_list|<
name|?
argument_list|>
name|create
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null type"
argument_list|)
throw|;
block|}
if|if
condition|(
name|Long
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Long
operator|::
name|valueOf
return|;
block|}
if|else if
condition|(
name|Integer
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|::
name|valueOf
return|;
block|}
if|else if
condition|(
name|String
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|stringId
lambda|->
name|stringId
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported ID type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
interface|interface
name|Converter
parameter_list|<
name|T
parameter_list|>
block|{
specifier|default
name|String
name|toUuid
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|URLEncoder
operator|.
name|encode
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// unexpected
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|T
name|fromStringId
parameter_list|(
name|String
name|stringId
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

