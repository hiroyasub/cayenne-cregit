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
name|util
package|;
end_package

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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Clob
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
comment|/**  * A Clob implementation that stores contents in memory.  *<p>  *<i>This implementation is based on jdbcClob from HSQLDB (copyright HSQL Development  * Group).</i>  *</p>  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|MemoryClob
implements|implements
name|Clob
block|{
specifier|volatile
name|String
name|data
decl_stmt|;
comment|/**      * Constructs a new jdbcClob object wrapping the given character sequence.      *<p>      * This constructor is used internally to retrieve result set values as Clob objects,      * yet it must be public to allow access from other packages. As such (in the interest      * of efficiency) this object maintains a reference to the given String object rather      * than making a copy and so it is gently suggested (in the interest of effective      * memory management) that extenal clients using this constructor either take pause to      * consider the implications or at least take care to provide a String object whose      * internal character buffer is not much larger than required to represent the value.      *       * @param data the character sequence representing the Clob value      * @throws CayenneRuntimeException if the argument is null      */
specifier|public
name|MemoryClob
parameter_list|(
name|String
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
comment|/**      * Retrieves the number of characters in the<code>CLOB</code> value designated by      * this<code>Clob</code> object.      *       * @return length of the<code>CLOB</code> in characters      * @exception SQLException if there is an error accessing the length of the      *<code>CLOB</code> value      */
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|ldata
init|=
name|data
decl_stmt|;
return|return
name|ldata
operator|.
name|length
argument_list|()
return|;
block|}
comment|/**      * Retrieves a copy of the specified substring in the<code>CLOB</code> value      * designated by this<code>Clob</code> object. The substring begins at position      *<code>pos</code> and has up to<code>length</code> consecutive characters.      */
specifier|public
name|String
name|getSubString
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
name|String
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
argument_list|()
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
name|CayenneRuntimeException
argument_list|(
literal|"Invalid position: %d"
argument_list|,
operator|(
name|pos
operator|+
literal|1L
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
name|CayenneRuntimeException
argument_list|(
literal|"Invalid length: %s"
argument_list|,
name|length
argument_list|)
throw|;
block|}
if|if
condition|(
name|pos
operator|==
literal|0
operator|&&
name|length
operator|==
name|dlen
condition|)
block|{
return|return
name|ldata
return|;
block|}
return|return
name|ldata
operator|.
name|substring
argument_list|(
operator|(
name|int
operator|)
name|pos
argument_list|,
operator|(
name|int
operator|)
name|pos
operator|+
name|length
argument_list|)
return|;
block|}
comment|/**      * Retrieves the<code>CLOB</code> value designated by this<code>Clob</code>      * object as a<code>java.io.Reader</code> object (or as a stream of characters).      *       * @return a<code>java.io.Reader</code> object containing the<code>CLOB</code>      *         data      * @exception SQLException if there is an error accessing the<code>CLOB</code>      *                value      */
specifier|public
name|java
operator|.
name|io
operator|.
name|Reader
name|getCharacterStream
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|ldata
init|=
name|data
decl_stmt|;
return|return
operator|new
name|StringReader
argument_list|(
name|ldata
argument_list|)
return|;
block|}
comment|/**      * Retrieves the<code>CLOB</code> value designated by this<code>Clob</code>      * object as an ascii stream.      *       * @return a<code>java.io.InputStream</code> object containing the      *<code>CLOB</code> data      * @exception SQLException if there is an error accessing the<code>CLOB</code>      *                value      */
specifier|public
name|java
operator|.
name|io
operator|.
name|InputStream
name|getAsciiStream
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|ldata
init|=
name|data
decl_stmt|;
return|return
operator|new
name|AsciiStringInputStream
argument_list|(
name|ldata
argument_list|)
return|;
block|}
comment|/**      * Retrieves the character position at which the specified substring      *<code>searchstr</code> appears in the SQL<code>CLOB</code> value represented      * by this<code>Clob</code> object. The search begins at position      *<code>start</code>.      *       * @param searchstr the substring for which to search      * @param start the position at which to begin searching; the first position is 1      * @return the position at which the substring appears or -1 if it is not present; the      *         first position is 1      * @exception SQLException if there is an error accessing the<code>CLOB</code>      *                value      */
specifier|public
name|long
name|position
parameter_list|(
specifier|final
name|String
name|searchstr
parameter_list|,
name|long
name|start
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|searchstr
operator|==
literal|null
operator|||
name|start
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|final
name|String
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|int
name|pos
init|=
name|ldata
operator|.
name|indexOf
argument_list|(
name|searchstr
argument_list|,
operator|(
name|int
operator|)
operator|--
name|start
argument_list|)
decl_stmt|;
return|return
operator|(
name|pos
operator|<
literal|0
operator|)
condition|?
operator|-
literal|1
else|:
name|pos
operator|+
literal|1
return|;
block|}
comment|/**      * Retrieves the character position at which the specified<code>Clob</code> object      *<code>searchstr</code> appears in this<code>Clob</code> object. The search      * begins at position<code>start</code>.      *       * @param searchstr the<code>Clob</code> object for which to search      * @param start the position at which to begin searching; the first position is 1      * @return the position at which the<code>Clob</code> object appears or -1 if it is      *         not present; the first position is 1      * @exception SQLException if there is an error accessing the<code>CLOB</code>      *                value      */
specifier|public
name|long
name|position
parameter_list|(
specifier|final
name|Clob
name|searchstr
parameter_list|,
name|long
name|start
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|searchstr
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|final
name|String
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|long
name|dlen
init|=
name|ldata
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|long
name|sslen
init|=
name|searchstr
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// This is potentially much less expensive than materializing a large
comment|// substring from some other vendor's CLOB. Indeed, we should probably
comment|// do the comparison piecewise, using an in-memory buffer (or temp-files
comment|// when available), if it is detected that the input CLOB is very long.
if|if
condition|(
name|start
operator|>
name|dlen
operator|-
name|sslen
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// by now, we know sslen and start are both< Integer.MAX_VALUE
name|String
name|s
decl_stmt|;
if|if
condition|(
name|searchstr
operator|instanceof
name|MemoryClob
condition|)
block|{
name|s
operator|=
operator|(
operator|(
name|MemoryClob
operator|)
name|searchstr
operator|)
operator|.
name|data
expr_stmt|;
block|}
else|else
block|{
name|s
operator|=
name|searchstr
operator|.
name|getSubString
argument_list|(
literal|1L
argument_list|,
operator|(
name|int
operator|)
name|sslen
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|pos
init|=
name|ldata
operator|.
name|indexOf
argument_list|(
name|s
argument_list|,
operator|(
name|int
operator|)
name|start
argument_list|)
decl_stmt|;
return|return
operator|(
name|pos
operator|<
literal|0
operator|)
condition|?
operator|-
literal|1
else|:
name|pos
operator|+
literal|1
return|;
block|}
comment|/**      * Writes the given Java<code>String</code> to the<code>CLOB</code> value that      * this<code>Clob</code> object designates at the position<code>pos</code>.      * Calling this method always throws an<code>SQLException</code>.      */
specifier|public
name|int
name|setString
parameter_list|(
name|long
name|pos
parameter_list|,
name|String
name|str
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Writes<code>len</code> characters of<code>str</code>, starting at character      *<code>offset</code>, to the<code>CLOB</code> value that this      *<code>Clob</code> represents. Calling this method always throws an      *<code>SQLException</code>.      */
specifier|public
name|int
name|setString
parameter_list|(
name|long
name|pos
parameter_list|,
name|String
name|str
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
name|CayenneRuntimeException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Retrieves a stream to be used to write Ascii characters to the<code>CLOB</code>      * value that this<code>Clob</code> object represents, starting at position      *<code>pos</code>.      *<p>      * Calling this method always throws an<code>SQLException</code>.      */
specifier|public
name|java
operator|.
name|io
operator|.
name|OutputStream
name|setAsciiStream
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Retrieves a stream to be used to write a stream of Unicode characters to the      *<code>CLOB</code> value that this<code>Clob</code> object represents, at      * position<code>pos</code>.      *<p>      * Calling this method always throws an<code>SQLException</code>.      */
specifier|public
name|java
operator|.
name|io
operator|.
name|Writer
name|setCharacterStream
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
comment|/**      * Truncates the<code>CLOB</code> value that this<code>Clob</code> designates to      * have a length of<code>len</code> characters.      *<p>      */
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
name|String
name|ldata
init|=
name|data
decl_stmt|;
specifier|final
name|long
name|dlen
init|=
name|ldata
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|long
name|chars
init|=
name|len
operator|>>
literal|1
decl_stmt|;
if|if
condition|(
name|chars
operator|==
name|dlen
condition|)
block|{
comment|// nothing has changed, so there's nothing to be done
block|}
if|else if
condition|(
name|len
operator|<
literal|0
operator|||
name|chars
operator|>
name|dlen
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid length: %d"
argument_list|,
name|len
argument_list|)
throw|;
block|}
else|else
block|{
comment|// use new String() to ensure we get rid of slack
name|data
operator|=
name|ldata
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|chars
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|AsciiStringInputStream
extends|extends
name|InputStream
block|{
specifier|protected
name|int
name|strOffset
init|=
literal|0
decl_stmt|;
specifier|protected
name|int
name|charOffset
init|=
literal|0
decl_stmt|;
specifier|protected
name|int
name|available
decl_stmt|;
specifier|protected
name|String
name|str
decl_stmt|;
specifier|public
name|AsciiStringInputStream
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|str
operator|=
name|s
expr_stmt|;
name|available
operator|=
name|s
operator|.
name|length
argument_list|()
operator|*
literal|2
expr_stmt|;
block|}
specifier|public
name|int
name|doRead
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|available
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|available
operator|--
expr_stmt|;
name|char
name|c
init|=
name|str
operator|.
name|charAt
argument_list|(
name|strOffset
argument_list|)
decl_stmt|;
if|if
condition|(
name|charOffset
operator|==
literal|0
condition|)
block|{
name|charOffset
operator|=
literal|1
expr_stmt|;
return|return
operator|(
name|c
operator|&
literal|0x0000ff00
operator|)
operator|>>
literal|8
return|;
block|}
else|else
block|{
name|charOffset
operator|=
literal|0
expr_stmt|;
name|strOffset
operator|++
expr_stmt|;
return|return
name|c
operator|&
literal|0x000000ff
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|doRead
argument_list|()
expr_stmt|;
return|return
name|doRead
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|available
operator|/
literal|2
return|;
block|}
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
specifier|public
name|void
name|free
parameter_list|()
throws|throws
name|SQLException
block|{
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
specifier|public
name|Reader
name|getCharacterStream
parameter_list|(
name|long
name|pos
parameter_list|,
name|long
name|length
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

