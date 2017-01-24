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
name|access
operator|.
name|types
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
name|CayenneException
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
name|MemoryBlob
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Blob
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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

begin_comment
comment|/**  * Handles<code>byte[]</code>, mapping it as either of JDBC types - BLOB or  * (VAR)BINARY. Can be configured to trim trailing zero bytes.  */
end_comment

begin_class
specifier|public
class|class
name|ByteArrayType
implements|implements
name|ExtendedType
argument_list|<
name|byte
index|[]
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUF_SIZE
init|=
literal|8
operator|*
literal|1024
decl_stmt|;
specifier|protected
name|boolean
name|trimmingBytes
decl_stmt|;
specifier|protected
name|boolean
name|usingBlobs
decl_stmt|;
specifier|public
specifier|static
name|void
name|logBytes
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"< "
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|bytes
operator|.
name|length
decl_stmt|;
name|boolean
name|trimming
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|len
operator|>
name|TRIM_VALUES_THRESHOLD
condition|)
block|{
name|len
operator|=
name|TRIM_VALUES_THRESHOLD
expr_stmt|;
name|trimming
operator|=
literal|true
expr_stmt|;
block|}
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
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
name|bytes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|trimming
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Strips null bytes from the byte array, returning a potentially smaller 	 * array that contains no trailing zero bytes. 	 */
specifier|public
specifier|static
name|byte
index|[]
name|trimBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|int
name|bytesToTrim
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|bytes
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|bytes
index|[
name|i
index|]
operator|!=
literal|0
condition|)
block|{
name|bytesToTrim
operator|=
name|bytes
operator|.
name|length
operator|-
literal|1
operator|-
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|bytesToTrim
operator|==
literal|0
condition|)
block|{
return|return
name|bytes
return|;
block|}
name|byte
index|[]
name|dest
init|=
operator|new
name|byte
index|[
name|bytes
operator|.
name|length
operator|-
name|bytesToTrim
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
name|dest
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|dest
return|;
block|}
specifier|public
name|ByteArrayType
parameter_list|(
name|boolean
name|trimmingBytes
parameter_list|,
name|boolean
name|usingBlobs
parameter_list|)
block|{
name|this
operator|.
name|usingBlobs
operator|=
name|usingBlobs
expr_stmt|;
name|this
operator|.
name|trimmingBytes
operator|=
name|trimmingBytes
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"byte[]"
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|bytes
operator|=
operator|(
name|isUsingBlobs
argument_list|()
operator|)
condition|?
name|readBlob
argument_list|(
name|rs
operator|.
name|getBlob
argument_list|(
name|index
argument_list|)
argument_list|)
else|:
name|readBinaryStream
argument_list|(
name|rs
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bytes
operator|=
name|rs
operator|.
name|getBytes
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// trim BINARY type
if|if
condition|(
name|bytes
operator|!=
literal|null
operator|&&
name|type
operator|==
name|Types
operator|.
name|BINARY
operator|&&
name|isTrimmingBytes
argument_list|()
condition|)
block|{
name|bytes
operator|=
name|trimBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bytes
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|materializeObject
parameter_list|(
name|CallableStatement
name|cs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
if|if
condition|(
operator|!
name|isUsingBlobs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Binary streams are not supported in stored procedure parameters."
argument_list|)
throw|;
block|}
name|bytes
operator|=
name|readBlob
argument_list|(
name|cs
operator|.
name|getBlob
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bytes
operator|=
name|cs
operator|.
name|getBytes
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// trim BINARY type
if|if
condition|(
name|bytes
operator|!=
literal|null
operator|&&
name|type
operator|==
name|Types
operator|.
name|BINARY
operator|&&
name|isTrimmingBytes
argument_list|()
condition|)
block|{
name|bytes
operator|=
name|trimBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bytes
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|byte
index|[]
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if this is a BLOB column, set the value as "bytes"
comment|// instead. This should work with most drivers
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
if|if
condition|(
name|isUsingBlobs
argument_list|()
condition|)
block|{
name|st
operator|.
name|setBlob
argument_list|(
name|pos
argument_list|,
name|writeBlob
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setBytes
argument_list|(
name|pos
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|scale
operator|!=
operator|-
literal|1
condition|)
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|"NULL"
return|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|logBytes
argument_list|(
name|buffer
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|Blob
name|writeBlob
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
comment|// TODO: should we use Connection.createBlob() instead? (Like Oracle
comment|// ByteArrayType does)
return|return
name|bytes
operator|!=
literal|null
condition|?
operator|new
name|MemoryBlob
argument_list|(
name|bytes
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|byte
index|[]
name|readBlob
parameter_list|(
name|Blob
name|blob
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
if|if
condition|(
name|blob
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// sanity check on size
if|if
condition|(
name|blob
operator|.
name|length
argument_list|()
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"BLOB is too big to be read as byte[] in memory: "
operator|+
name|blob
operator|.
name|length
argument_list|()
argument_list|)
throw|;
block|}
name|int
name|size
init|=
operator|(
name|int
operator|)
name|blob
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|byte
index|[
literal|0
index|]
return|;
block|}
return|return
name|blob
operator|.
name|getBytes
argument_list|(
literal|1
argument_list|,
name|size
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|readBinaryStream
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|rs
operator|.
name|getBinaryStream
argument_list|(
name|index
argument_list|)
init|;
init|)
block|{
return|return
operator|(
name|in
operator|!=
literal|null
operator|)
condition|?
name|readValueStream
argument_list|(
name|in
argument_list|,
operator|-
literal|1
argument_list|,
name|BUF_SIZE
argument_list|)
else|:
literal|null
return|;
block|}
block|}
specifier|protected
name|byte
index|[]
name|readValueStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|int
name|streamSize
parameter_list|,
name|int
name|bufSize
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|bufSize
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|(
name|streamSize
operator|>
literal|0
operator|)
condition|?
operator|new
name|ByteArrayOutputStream
argument_list|(
name|streamSize
argument_list|)
else|:
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|bufSize
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/** 	 * Returns<code>true</code> if byte columns are handled as BLOBs 	 * internally. 	 */
specifier|public
name|boolean
name|isUsingBlobs
parameter_list|()
block|{
return|return
name|usingBlobs
return|;
block|}
specifier|public
name|void
name|setUsingBlobs
parameter_list|(
name|boolean
name|usingBlobs
parameter_list|)
block|{
name|this
operator|.
name|usingBlobs
operator|=
name|usingBlobs
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTrimmingBytes
parameter_list|()
block|{
return|return
name|trimmingBytes
return|;
block|}
specifier|public
name|void
name|setTrimmingBytes
parameter_list|(
name|boolean
name|trimingBytes
parameter_list|)
block|{
name|this
operator|.
name|trimmingBytes
operator|=
name|trimingBytes
expr_stmt|;
block|}
block|}
end_class

end_unit

