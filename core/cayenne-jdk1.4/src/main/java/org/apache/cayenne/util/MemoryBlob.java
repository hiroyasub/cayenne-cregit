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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|io
operator|.
name|OutputStream
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
name|SQLException
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

begin_comment
comment|/**  * A Blob implementation that stores content in memory.  *<p>  *<i>This implementation is based on jdbcBlob from HSQLDB (copyright HSQL Development  * Group).</i>  *</p>  *   * @since 1.2  * @author Andrus Adamchik, based on HSQLDB jdbcBlob.  */
end_comment

begin_class
specifier|public
class|class
name|MemoryBlob
implements|implements
name|Blob
block|{
specifier|volatile
name|byte
index|[]
name|data
decl_stmt|;
specifier|public
name|MemoryBlob
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs a new MemoryBlob instance wrapping the given octet sequence.      *       * @param data the octet sequence representing the Blob value      * @throws CayenneRuntimeException if the argument is null      */
specifier|public
name|MemoryBlob
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null data"
argument_list|)
throw|;
block|}
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
comment|/**      * Returns the number of bytes in the<code>BLOB</code> value designated by this      *<code>Blob</code> object.      *       * @return length of the<code>BLOB</code> in bytes      * @exception SQLException if there is an error accessing the length of the      *<code>BLOB</code>      */
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|data
operator|.
name|length
return|;
block|}
comment|/**      * Retrieves all or part of the<code>BLOB</code> value that this<code>Blob</code>      * object represents, as an array of bytes. This<code>byte</code> array contains up      * to<code>length</code> consecutive bytes starting at position<code>pos</code>.      *<p>      * The official specification is ambiguous in that it does not precisely indicate the      * policy to be observed when pos> this.length() - length. One policy would be to      * retrieve the octets from pos to this.length(). Another would be to throw an      * exception. This implementation observes the later policy.      *       * @param pos the ordinal position of the first byte in the<code>BLOB</code> value      *            to be extracted; the first byte is at position 1      * @param length the number of consecutive bytes to be copied      * @return a byte array containing up to<code>length</code> consecutive bytes from      *         the<code>BLOB</code> value designated by this<code>Blob</code>      *         object, starting with the byte at position<code>pos</code>      * @exception SQLException if there is an error accessing the<code>BLOB</code>      *                value      */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|(
name|long
name|pos
parameter_list|,
specifier|final
name|int
name|length
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|byte
index|[]
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|int
name|dlen
init|=
name|ldata
operator|.
name|length
decl_stmt|;
name|pos
operator|--
expr_stmt|;
if|if
condition|(
name|pos
operator|<
literal|0
operator|||
name|pos
operator|>
name|dlen
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Invalid pos: "
operator|+
operator|(
name|pos
operator|+
literal|1
operator|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|length
operator|<
literal|0
operator|||
name|length
operator|>
name|dlen
operator|-
name|pos
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"length: "
operator|+
name|length
argument_list|)
throw|;
block|}
specifier|final
name|byte
index|[]
name|out
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|ldata
argument_list|,
operator|(
name|int
operator|)
name|pos
argument_list|,
name|out
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
comment|/**      * Retrieves the<code>BLOB</code> value designated by this<code>Blob</code>      * instance as a stream.      *       * @return a stream containing the<code>BLOB</code> data      * @exception SQLException if there is an error accessing the<code>BLOB</code>      *                value      */
specifier|public
name|InputStream
name|getBinaryStream
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
comment|/**      * Retrieves the byte position at which the specified byte array<code>pattern</code>      * begins within the<code>BLOB</code> value that this<code>Blob</code> object      * represents. The search for<code>pattern</code> begins at position      *<code>start</code>.      *<p>      *       * @param pattern the byte array for which to search      * @param start the position at which to begin searching; the first position is 1      * @return the position at which the pattern appears, else -1      * @exception SQLException if there is an error accessing the<code>BLOB</code>      */
specifier|public
name|long
name|position
parameter_list|(
specifier|final
name|byte
index|[]
name|pattern
parameter_list|,
name|long
name|start
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|byte
index|[]
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|int
name|dlen
init|=
name|ldata
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|start
operator|>
name|dlen
operator|||
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|start
operator|<
literal|1
condition|)
block|{
name|start
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|start
operator|--
expr_stmt|;
block|}
specifier|final
name|int
name|plen
init|=
name|pattern
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|plen
operator|==
literal|0
operator|||
name|start
operator|>
name|dlen
operator|-
name|plen
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|final
name|int
name|stop
init|=
name|dlen
operator|-
name|plen
decl_stmt|;
specifier|final
name|byte
name|b0
init|=
name|pattern
index|[
literal|0
index|]
decl_stmt|;
name|outer_loop
label|:
for|for
control|(
name|int
name|i
init|=
operator|(
name|int
operator|)
name|start
init|;
name|i
operator|<=
name|stop
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ldata
index|[
name|i
index|]
operator|!=
name|b0
condition|)
block|{
continue|continue;
block|}
name|int
name|len
init|=
name|plen
decl_stmt|;
name|int
name|doffset
init|=
name|i
decl_stmt|;
name|int
name|poffset
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|len
operator|--
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|ldata
index|[
name|doffset
operator|++
index|]
operator|!=
name|pattern
index|[
name|poffset
operator|++
index|]
condition|)
block|{
continue|continue
name|outer_loop
continue|;
block|}
block|}
return|return
name|i
operator|+
literal|1
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**      * Retrieves the byte position in the<code>BLOB</code> value designated by this      *<code>Blob</code> object at which<code>pattern</code> begins. The search      * begins at position<code>start</code>.      *       * @param pattern the<code>Blob</code> object designating the<code>BLOB</code>      *            value for which to search      * @param start the position in the<code>BLOB</code> value at which to begin      *            searching; the first position is 1      * @return the position at which the pattern begins, else -1      * @exception SQLException if there is an error accessing the<code>BLOB</code>      *                value      */
specifier|public
name|long
name|position
parameter_list|(
specifier|final
name|Blob
name|pattern
parameter_list|,
name|long
name|start
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|byte
index|[]
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|int
name|dlen
init|=
name|ldata
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|start
operator|>
name|dlen
operator|||
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|start
operator|<
literal|1
condition|)
block|{
name|start
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|start
operator|--
expr_stmt|;
block|}
specifier|final
name|long
name|plen
init|=
name|pattern
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|plen
operator|==
literal|0
operator|||
name|start
operator|>
name|dlen
operator|-
name|plen
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// by now, we know plen<= Integer.MAX_VALUE
specifier|final
name|int
name|iplen
init|=
operator|(
name|int
operator|)
name|plen
decl_stmt|;
name|byte
index|[]
name|bap
decl_stmt|;
if|if
condition|(
name|pattern
operator|instanceof
name|MemoryBlob
condition|)
block|{
name|bap
operator|=
operator|(
operator|(
name|MemoryBlob
operator|)
name|pattern
operator|)
operator|.
name|data
expr_stmt|;
block|}
else|else
block|{
name|bap
operator|=
name|pattern
operator|.
name|getBytes
argument_list|(
literal|1
argument_list|,
name|iplen
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|stop
init|=
name|dlen
operator|-
name|iplen
decl_stmt|;
specifier|final
name|byte
name|b0
init|=
name|bap
index|[
literal|0
index|]
decl_stmt|;
name|outer_loop
label|:
for|for
control|(
name|int
name|i
init|=
operator|(
name|int
operator|)
name|start
init|;
name|i
operator|<=
name|stop
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ldata
index|[
name|i
index|]
operator|!=
name|b0
condition|)
block|{
continue|continue;
block|}
name|int
name|len
init|=
name|iplen
decl_stmt|;
name|int
name|doffset
init|=
name|i
decl_stmt|;
name|int
name|poffset
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|len
operator|--
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|ldata
index|[
name|doffset
operator|++
index|]
operator|!=
name|bap
index|[
name|poffset
operator|++
index|]
condition|)
block|{
continue|continue
name|outer_loop
continue|;
block|}
block|}
return|return
name|i
operator|+
literal|1
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**      * Always throws an exception.      */
specifier|public
name|int
name|setBytes
parameter_list|(
name|long
name|pos
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Always throws an exception.      */
specifier|public
name|int
name|setBytes
parameter_list|(
name|long
name|pos
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Always throws an exception.      */
specifier|public
name|OutputStream
name|setBinaryStream
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Truncates the<code>BLOB</code> value that this<code>Blob</code> object      * represents to be<code>len</code> bytes in length.      *       * @param len the length, in bytes, to which the<code>BLOB</code> value that this      *<code>Blob</code> object represents should be truncated      * @exception SQLException if there is an error accessing the<code>BLOB</code>      *                value      */
specifier|public
name|void
name|truncate
parameter_list|(
specifier|final
name|long
name|len
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|byte
index|[]
name|ldata
init|=
name|data
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|0
operator|||
name|len
operator|>
name|ldata
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Invalid length: "
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|len
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|len
operator|==
name|ldata
operator|.
name|length
condition|)
block|{
return|return;
block|}
name|byte
index|[]
name|newData
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|len
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|ldata
argument_list|,
literal|0
argument_list|,
name|newData
argument_list|,
literal|0
argument_list|,
operator|(
name|int
operator|)
name|len
argument_list|)
expr_stmt|;
name|data
operator|=
name|newData
expr_stmt|;
block|}
block|}
end_class

end_unit

