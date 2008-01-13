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
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|validation
operator|.
name|BeanValidationFailure
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * Handles<code>java.lang.String</code>, mapping it as either of JDBC types - CLOB or  * (VAR)CHAR. Can be configured to trim trailing spaces.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CharType
extends|extends
name|AbstractType
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
name|trimingChars
parameter_list|,
name|boolean
name|usingClobs
parameter_list|)
block|{
name|this
operator|.
name|trimmingChars
operator|=
name|trimingChars
expr_stmt|;
name|this
operator|.
name|usingClobs
operator|=
name|usingClobs
expr_stmt|;
block|}
comment|/**      * Returns "java.lang.String".      */
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
comment|/**      * Validates string property.      *       * @since 1.1      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
annotation|@
name|Override
specifier|public
name|boolean
name|validateProperty
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|value
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|String
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|dbAttribute
operator|.
name|getMaxLength
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|string
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|>
name|dbAttribute
operator|.
name|getMaxLength
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"\""
operator|+
name|property
operator|+
literal|"\" exceeds maximum allowed length ("
operator|+
name|dbAttribute
operator|.
name|getMaxLength
argument_list|()
operator|+
literal|" chars): "
operator|+
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
name|validationResult
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|source
argument_list|,
name|property
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
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
name|String
name|val
init|=
literal|null
decl_stmt|;
comment|// CLOB handling
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|val
operator|=
operator|(
name|isUsingClobs
argument_list|()
operator|)
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
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// trim CHAR type
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
name|type
operator|==
name|Types
operator|.
name|CHAR
operator|&&
name|isTrimmingChars
argument_list|()
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|val
return|;
block|}
comment|/** Return trimmed string. */
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
name|String
name|val
init|=
literal|null
decl_stmt|;
comment|// CLOB handling
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
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
name|val
operator|=
name|readClob
argument_list|(
name|cs
operator|.
name|getClob
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|cs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// trim CHAR type
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
name|type
operator|==
name|Types
operator|.
name|CHAR
operator|&&
name|isTrimmingChars
argument_list|()
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|val
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
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
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
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setJdbcObject
argument_list|(
name|st
argument_list|,
name|val
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|precision
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
name|int
name|bufSize
init|=
operator|(
name|size
operator|<
name|BUF_SIZE
operator|)
condition|?
name|size
else|:
name|BUF_SIZE
decl_stmt|;
name|Reader
name|in
init|=
name|clob
operator|.
name|getCharacterStream
argument_list|()
decl_stmt|;
return|return
operator|(
name|in
operator|!=
literal|null
operator|)
condition|?
name|readValueStream
argument_list|(
operator|new
name|BufferedReader
argument_list|(
name|in
argument_list|,
name|bufSize
argument_list|)
argument_list|,
name|size
argument_list|,
name|bufSize
argument_list|)
else|:
literal|null
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
name|Reader
name|in
init|=
name|rs
operator|.
name|getCharacterStream
argument_list|(
name|index
argument_list|)
decl_stmt|;
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
name|int
name|read
decl_stmt|;
name|StringWriter
name|out
init|=
operator|(
name|streamSize
operator|>
literal|0
operator|)
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
try|try
block|{
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
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns<code>true</code> if 'materializeObject' method should trim trailing      * spaces from the CHAR columns. This addresses an issue with some JDBC drivers (e.g.      * Oracle), that return Strings for CHAR columsn padded with spaces.      */
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

