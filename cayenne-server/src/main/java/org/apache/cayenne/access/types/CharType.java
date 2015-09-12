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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Clob
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
comment|/**  * Handles<code>java.lang.String</code>, mapping it as either of JDBC types -  * CLOB or (VAR)CHAR. Can be configured to trim trailing spaces.  */
end_comment

begin_class
specifier|public
class|class
name|CharType
implements|implements
name|ExtendedType
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
name|trimmingChars
decl_stmt|;
specifier|protected
name|boolean
name|usingClobs
decl_stmt|;
specifier|public
name|CharType
parameter_list|(
name|boolean
name|trimmingChars
parameter_list|,
name|boolean
name|usingClobs
parameter_list|)
block|{
name|this
operator|.
name|trimmingChars
operator|=
name|trimmingChars
expr_stmt|;
name|this
operator|.
name|usingClobs
operator|=
name|usingClobs
expr_stmt|;
block|}
comment|/** 	 * Returns "java.lang.String". 	 */
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/** Return trimmed string. */
annotation|@
name|Override
specifier|public
name|Object
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
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
operator|||
name|type
operator|==
name|Types
operator|.
name|NCLOB
condition|)
block|{
return|return
name|isUsingClobs
argument_list|()
condition|?
name|readClob
argument_list|(
name|rs
operator|.
name|getClob
argument_list|(
name|index
argument_list|)
argument_list|)
else|:
name|readCharStream
argument_list|(
name|rs
argument_list|,
name|index
argument_list|)
return|;
block|}
return|return
name|handleString
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
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
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
operator|||
name|type
operator|==
name|Types
operator|.
name|NCLOB
condition|)
block|{
if|if
condition|(
operator|!
name|isUsingClobs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Character streams are not supported in stored procedure parameters."
argument_list|)
throw|;
block|}
return|return
name|readClob
argument_list|(
name|cs
operator|.
name|getClob
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
return|return
name|handleString
argument_list|(
name|cs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|private
name|Object
name|handleString
parameter_list|(
name|String
name|val
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// trim CHAR type
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
operator|(
name|type
operator|==
name|Types
operator|.
name|CHAR
operator|||
name|type
operator|==
name|Types
operator|.
name|NCHAR
operator|)
operator|&&
name|isTrimmingChars
argument_list|()
condition|)
block|{
return|return
name|rtrim
argument_list|(
name|val
argument_list|)
return|;
block|}
return|return
name|val
return|;
block|}
comment|/** Trim right spaces. */
specifier|protected
name|String
name|rtrim
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|int
name|end
init|=
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
name|int
name|count
init|=
name|end
decl_stmt|;
while|while
condition|(
name|end
operator|>=
literal|0
operator|&&
name|value
operator|.
name|charAt
argument_list|(
name|end
argument_list|)
operator|<=
literal|' '
condition|)
block|{
name|end
operator|--
expr_stmt|;
block|}
return|return
name|end
operator|==
name|count
condition|?
name|value
else|:
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|end
operator|+
literal|1
argument_list|)
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
name|Object
name|value
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
comment|// if this is a CLOB column, set the value as "String"
comment|// instead. This should work with most drivers
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
operator|||
name|type
operator|==
name|Types
operator|.
name|NCLOB
condition|)
block|{
name|st
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
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
name|value
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
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|readClob
parameter_list|(
name|Clob
name|clob
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
if|if
condition|(
name|clob
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
name|clob
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
literal|"CLOB is too big to be read as String in memory: "
operator|+
name|clob
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
name|clob
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
literal|""
return|;
block|}
return|return
name|clob
operator|.
name|getSubString
argument_list|(
literal|1
argument_list|,
name|size
argument_list|)
return|;
block|}
specifier|protected
name|String
name|readCharStream
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
name|Reader
name|in
init|=
name|rs
operator|.
name|getCharacterStream
argument_list|(
name|index
argument_list|)
init|;
init|)
block|{
return|return
name|in
operator|!=
literal|null
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
name|String
name|readValueStream
parameter_list|(
name|Reader
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
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
name|bufSize
index|]
decl_stmt|;
name|StringWriter
name|out
init|=
name|streamSize
operator|>
literal|0
condition|?
operator|new
name|StringWriter
argument_list|(
name|streamSize
argument_list|)
else|:
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|int
name|read
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
name|toString
argument_list|()
return|;
block|}
comment|/** 	 * Returns<code>true</code> if 'materializeObject' method should trim 	 * trailing spaces from the CHAR columns. This addresses an issue with some 	 * JDBC drivers (e.g. Oracle), that return Strings for CHAR columns padded 	 * with spaces. 	 */
specifier|public
name|boolean
name|isTrimmingChars
parameter_list|()
block|{
return|return
name|trimmingChars
return|;
block|}
specifier|public
name|void
name|setTrimmingChars
parameter_list|(
name|boolean
name|trimingChars
parameter_list|)
block|{
name|this
operator|.
name|trimmingChars
operator|=
name|trimingChars
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsingClobs
parameter_list|()
block|{
return|return
name|usingClobs
return|;
block|}
specifier|public
name|void
name|setUsingClobs
parameter_list|(
name|boolean
name|usingClobs
parameter_list|)
block|{
name|this
operator|.
name|usingClobs
operator|=
name|usingClobs
expr_stmt|;
block|}
block|}
end_class

end_unit

