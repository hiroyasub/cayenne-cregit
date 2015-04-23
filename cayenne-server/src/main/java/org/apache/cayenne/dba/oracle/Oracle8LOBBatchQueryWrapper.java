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
name|dba
operator|.
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|query
operator|.
name|BatchQuery
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
name|query
operator|.
name|BatchQueryRow
import|;
end_import

begin_comment
comment|/**  * Helper class to extract the information from BatchQueries, essential for LOB  * columns processing.  *   */
end_comment

begin_class
class|class
name|Oracle8LOBBatchQueryWrapper
block|{
specifier|protected
name|BatchQuery
name|query
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
decl_stmt|;
comment|// attribute list decoders
specifier|protected
name|boolean
index|[]
name|qualifierAttributes
decl_stmt|;
specifier|protected
name|boolean
index|[]
name|allLOBAttributes
decl_stmt|;
specifier|protected
name|Object
index|[]
name|updatedLOBAttributes
decl_stmt|;
name|Oracle8LOBBatchQueryWrapper
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|dbAttributes
operator|=
name|query
operator|.
name|getDbAttributes
argument_list|()
expr_stmt|;
name|int
name|len
init|=
name|dbAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|this
operator|.
name|qualifierAttributes
operator|=
operator|new
name|boolean
index|[
name|len
index|]
expr_stmt|;
name|this
operator|.
name|allLOBAttributes
operator|=
operator|new
name|boolean
index|[
name|len
index|]
expr_stmt|;
name|this
operator|.
name|updatedLOBAttributes
operator|=
operator|new
name|Object
index|[
name|len
index|]
expr_stmt|;
name|indexQualifierAttributes
argument_list|()
expr_stmt|;
block|}
comment|/**      * Indexes attributes      */
specifier|protected
name|void
name|indexQualifierAttributes
parameter_list|()
block|{
name|int
name|len
init|=
name|this
operator|.
name|dbAttributes
operator|.
name|size
argument_list|()
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|attribute
init|=
name|this
operator|.
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|type
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
name|qualifierAttributes
index|[
name|i
index|]
operator|=
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
expr_stmt|;
name|allLOBAttributes
index|[
name|i
index|]
operator|=
name|type
operator|==
name|Types
operator|.
name|BLOB
operator|||
name|type
operator|==
name|Types
operator|.
name|CLOB
expr_stmt|;
block|}
block|}
comment|/**      * Indexes attributes      */
name|void
name|indexLOBAttributes
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
block|{
name|int
name|len
init|=
name|updatedLOBAttributes
operator|.
name|length
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|updatedLOBAttributes
index|[
name|i
index|]
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|allLOBAttributes
index|[
name|i
index|]
condition|)
block|{
comment|// skip null and empty LOBs
name|Object
name|value
init|=
name|row
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|updatedLOBAttributes
index|[
name|i
index|]
operator|=
name|convertToBlobValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updatedLOBAttributes
index|[
name|i
index|]
operator|=
name|convertToClobValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Converts value to byte[] if possible.      */
specifier|protected
name|byte
index|[]
name|convertToBlobValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|(
name|byte
index|[]
operator|)
name|value
decl_stmt|;
return|return
name|bytes
operator|.
name|length
operator|==
literal|0
condition|?
literal|null
else|:
name|bytes
return|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Serializable
condition|)
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|ObjectOutputStream
name|out
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error serializing object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|bytes
operator|.
name|toByteArray
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Converts to char[] or String. Both are acceptable when writing CLOBs.      */
specifier|protected
name|Object
name|convertToClobValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|char
index|[]
condition|)
block|{
name|char
index|[]
name|chars
init|=
operator|(
name|char
index|[]
operator|)
name|value
decl_stmt|;
return|return
name|chars
operator|.
name|length
operator|==
literal|0
condition|?
literal|null
else|:
name|chars
return|;
block|}
else|else
block|{
name|String
name|strValue
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|strValue
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|strValue
return|;
block|}
block|}
comment|/**      * Returns a list of DbAttributes used in the qualifier of the query that      * selects a LOB row for LOB update.      */
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getDbAttributesForLOBSelectQualifier
parameter_list|()
block|{
name|int
name|len
init|=
name|qualifierAttributes
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|qualifierAttributes
index|[
name|i
index|]
condition|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|this
operator|.
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|attributes
return|;
block|}
comment|/**      * Returns a list of DbAttributes that correspond to the LOB columns updated      * in the current row in the batch query. The list will not include LOB      * attributes that are null or empty.      */
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getDbAttributesForUpdatedLOBColumns
parameter_list|()
block|{
name|int
name|len
init|=
name|updatedLOBAttributes
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|updatedLOBAttributes
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|this
operator|.
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|attributes
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|getValuesForLOBSelectQualifier
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
block|{
name|int
name|len
init|=
name|this
operator|.
name|qualifierAttributes
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|qualifierAttributes
index|[
name|i
index|]
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|row
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|getValuesForUpdatedLOBColumns
parameter_list|()
block|{
name|int
name|len
init|=
name|this
operator|.
name|updatedLOBAttributes
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|this
operator|.
name|updatedLOBAttributes
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|this
operator|.
name|updatedLOBAttributes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

