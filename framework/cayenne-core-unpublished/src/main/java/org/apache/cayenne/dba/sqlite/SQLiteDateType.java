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
name|sqlite
package|;
end_package

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
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
operator|.
name|UtilDateType
import|;
end_import

begin_comment
comment|/**  * Implements special date handling for SQLite. As SQLite has no native date type and the  * JDBC driver does not standardize it either.  *   * @since 3.0  */
end_comment

begin_comment
comment|// see http://www.zentus.com/sqlitejdbc/usage.html for some examples of the SQLite date
end_comment

begin_comment
comment|// handling fun.
end_comment

begin_class
class|class
name|SQLiteDateType
extends|extends
name|UtilDateType
block|{
specifier|private
name|DateFormat
name|timestampFormat
decl_stmt|;
specifier|private
name|DateFormat
name|dateFormat
decl_stmt|;
specifier|private
name|DateFormat
name|timeFormat
decl_stmt|;
specifier|public
name|SQLiteDateType
parameter_list|()
block|{
name|timestampFormat
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd kk:mm:ss"
argument_list|)
expr_stmt|;
name|dateFormat
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
expr_stmt|;
name|timeFormat
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"kk:mm:ss"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Date
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
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|string
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|long
name|ts
init|=
name|getLongTimestamp
argument_list|(
name|string
argument_list|)
decl_stmt|;
if|if
condition|(
name|ts
operator|>=
literal|0
condition|)
block|{
return|return
operator|new
name|Date
argument_list|(
name|ts
argument_list|)
return|;
block|}
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
return|return
name|getTimestamp
argument_list|(
name|string
argument_list|)
return|;
case|case
name|Types
operator|.
name|DATE
case|:
return|return
name|getDate
argument_list|(
name|string
argument_list|)
return|;
case|case
name|Types
operator|.
name|TIME
case|:
return|return
name|rs
operator|.
name|getTime
argument_list|(
name|index
argument_list|)
return|;
default|default:
return|return
name|getTimestamp
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Date
name|materializeObject
parameter_list|(
name|CallableStatement
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
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|string
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|long
name|ts
init|=
name|getLongTimestamp
argument_list|(
name|string
argument_list|)
decl_stmt|;
if|if
condition|(
name|ts
operator|>=
literal|0
condition|)
block|{
return|return
operator|new
name|Date
argument_list|(
name|ts
argument_list|)
return|;
block|}
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
return|return
name|getTimestamp
argument_list|(
name|string
argument_list|)
return|;
case|case
name|Types
operator|.
name|DATE
case|:
return|return
name|getDate
argument_list|(
name|string
argument_list|)
return|;
case|case
name|Types
operator|.
name|TIME
case|:
return|return
name|getTime
argument_list|(
name|string
argument_list|)
return|;
default|default:
return|return
name|getTimestamp
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
specifier|protected
name|Date
name|getTimestamp
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
synchronized|synchronized
init|(
name|timestampFormat
init|)
block|{
return|return
name|timestampFormat
operator|.
name|parse
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
comment|// also try date format...
try|try
block|{
synchronized|synchronized
init|(
name|dateFormat
init|)
block|{
return|return
name|dateFormat
operator|.
name|parse
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Unparsable timestamp string: "
operator|+
name|string
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|Date
name|getDate
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
synchronized|synchronized
init|(
name|dateFormat
init|)
block|{
return|return
name|dateFormat
operator|.
name|parse
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Unparsable date string: "
operator|+
name|string
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Date
name|getTime
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
synchronized|synchronized
init|(
name|timeFormat
init|)
block|{
return|return
name|timeFormat
operator|.
name|parse
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Unparsable time string: "
operator|+
name|string
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|long
name|getLongTimestamp
parameter_list|(
name|String
name|string
parameter_list|)
block|{
try|try
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|string
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
block|}
end_class

end_unit
