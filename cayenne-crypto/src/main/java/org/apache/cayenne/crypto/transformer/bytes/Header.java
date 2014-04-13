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
name|bytes
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

begin_comment
comment|/**  * Represents a header with metadata about the encrypted data. A header is  * prependend to each encrypted value, and itself is not encrypted.  *   * @since 3.2  */
end_comment

begin_class
class|class
name|Header
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEY_NAME_CHARSET
init|=
literal|"UTF-8"
decl_stmt|;
comment|/**      * The size of a header byte[] block.      */
specifier|static
specifier|final
name|int
name|HEADER_SIZE
init|=
literal|16
decl_stmt|;
comment|/**      * The size of a key name within the header block.      */
specifier|static
specifier|final
name|int
name|KEY_NAME_SIZE
init|=
literal|8
decl_stmt|;
comment|/**      * Position of the key name within the header block.      */
specifier|static
specifier|final
name|int
name|KEY_NAME_OFFSET
init|=
literal|8
decl_stmt|;
comment|/**      * Position of the "flags" byte in the header.      */
specifier|static
specifier|final
name|int
name|FLAGS_OFFSET
init|=
literal|0
decl_stmt|;
specifier|private
name|byte
index|[]
name|data
decl_stmt|;
specifier|static
name|Header
name|create
parameter_list|(
name|String
name|keyName
parameter_list|)
block|{
name|byte
index|[]
name|keyNameBytes
decl_stmt|;
try|try
block|{
name|keyNameBytes
operator|=
name|keyName
operator|.
name|getBytes
argument_list|(
name|KEY_NAME_CHARSET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Can't encode in "
operator|+
name|KEY_NAME_CHARSET
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|HEADER_SIZE
index|]
decl_stmt|;
if|if
condition|(
name|keyNameBytes
operator|.
name|length
operator|<=
name|KEY_NAME_SIZE
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|keyNameBytes
argument_list|,
literal|0
argument_list|,
name|data
argument_list|,
name|KEY_NAME_OFFSET
argument_list|,
name|keyNameBytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Key name '"
operator|+
name|keyName
operator|+
literal|"' is too long. Its UTF8-encoded form should not exceed "
operator|+
name|KEY_NAME_SIZE
operator|+
literal|" bytes"
argument_list|)
throw|;
block|}
return|return
name|create
argument_list|(
name|data
argument_list|)
return|;
block|}
specifier|static
name|Header
name|create
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|length
operator|!=
name|HEADER_SIZE
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Unexpected header data size: "
operator|+
name|data
operator|.
name|length
operator|+
literal|", expected size is "
operator|+
name|HEADER_SIZE
argument_list|)
throw|;
block|}
name|Header
name|h
init|=
operator|new
name|Header
argument_list|()
decl_stmt|;
name|h
operator|.
name|data
operator|=
name|data
expr_stmt|;
return|return
name|h
return|;
block|}
comment|// private constructor... construction is done via factory methods...
specifier|private
name|Header
parameter_list|()
block|{
block|}
name|byte
index|[]
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

