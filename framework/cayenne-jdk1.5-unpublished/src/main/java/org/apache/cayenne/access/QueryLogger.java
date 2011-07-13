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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
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
name|ExtendedEnumeration
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
name|jdbc
operator|.
name|ParameterBinding
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
name|conn
operator|.
name|DataSourceInfo
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
name|log
operator|.
name|CommonsJdbcEventLogger
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
name|log
operator|.
name|JdbcEventLogger
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
name|util
operator|.
name|IDUtil
import|;
end_import

begin_comment
comment|/**  * A static wrapper around {@link JdbcEventLogger}.  *   * @deprecated since 3.1 replaced by injectable {@link JdbcEventLogger}.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|QueryLogger
block|{
specifier|private
specifier|static
specifier|final
name|int
name|TRIM_VALUES_THRESHOLD
init|=
literal|30
decl_stmt|;
specifier|private
specifier|static
name|JdbcEventLogger
name|logger
init|=
operator|new
name|CommonsJdbcEventLogger
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|void
name|setLogger
parameter_list|(
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
specifier|public
specifier|static
name|JdbcEventLogger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
comment|/**      * Appends SQL literal for the specified object to the buffer. This is a utility      * method and is not intended to build SQL queries, rather this is used in logging      * routines. In particular it will trim large values to avoid flooding the logs.</p>      *       * @param buffer buffer to append value      * @param object object to be transformed to SQL literal.      */
specifier|public
specifier|static
name|void
name|sqlLiteralForObject
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"NULL"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|String
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
comment|// lets escape quotes
name|String
name|literal
init|=
operator|(
name|String
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|literal
operator|.
name|length
argument_list|()
operator|>
name|TRIM_VALUES_THRESHOLD
condition|)
block|{
name|literal
operator|=
name|literal
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|TRIM_VALUES_THRESHOLD
argument_list|)
operator|+
literal|"..."
expr_stmt|;
block|}
name|int
name|curPos
init|=
literal|0
decl_stmt|;
name|int
name|endPos
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|endPos
operator|=
name|literal
operator|.
name|indexOf
argument_list|(
literal|'\''
argument_list|,
name|curPos
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|literal
operator|.
name|substring
argument_list|(
name|curPos
argument_list|,
name|endPos
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|curPos
operator|=
name|endPos
operator|+
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|curPos
operator|<
name|literal
operator|.
name|length
argument_list|()
condition|)
name|buffer
operator|.
name|append
argument_list|(
name|literal
operator|.
name|substring
argument_list|(
name|curPos
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
comment|// handle byte pretty formatting
if|else if
condition|(
name|object
operator|instanceof
name|Byte
condition|)
block|{
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
operator|(
operator|(
name|Byte
operator|)
name|object
operator|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Number
condition|)
block|{
comment|// process numeric value (do something smart in the future)
name|buffer
operator|.
name|append
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|java
operator|.
name|sql
operator|.
name|Date
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
operator|.
name|append
argument_list|(
name|object
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|java
operator|.
name|sql
operator|.
name|Time
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
operator|.
name|append
argument_list|(
name|object
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|java
operator|.
name|util
operator|.
name|Date
condition|)
block|{
name|long
name|time
init|=
operator|(
operator|(
name|java
operator|.
name|util
operator|.
name|Date
operator|)
name|object
operator|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
operator|.
name|append
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
name|time
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|java
operator|.
name|util
operator|.
name|Calendar
condition|)
block|{
name|long
name|time
init|=
operator|(
operator|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|)
name|object
operator|)
operator|.
name|getTimeInMillis
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
operator|.
name|append
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
name|time
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Character
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Character
operator|)
name|object
operator|)
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Boolean
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
operator|.
name|append
argument_list|(
name|object
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Enum
condition|)
block|{
comment|// buffer.append(object.getClass().getName()).append(".");
name|buffer
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|ExtendedEnumeration
condition|)
block|{
name|Object
name|value
init|=
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|object
operator|)
operator|.
name|getDatabaseValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
name|buffer
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
name|buffer
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
name|buffer
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME -- this isn't quite
comment|// right
block|}
if|else if
condition|(
name|object
operator|instanceof
name|ParameterBinding
condition|)
block|{
name|sqlLiteralForObject
argument_list|(
name|buffer
argument_list|,
operator|(
operator|(
name|ParameterBinding
operator|)
name|object
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
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
name|Array
operator|.
name|getLength
argument_list|(
name|object
argument_list|)
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
name|sqlLiteralForObject
argument_list|(
name|buffer
argument_list|,
name|Array
operator|.
name|get
argument_list|(
name|object
argument_list|,
name|i
argument_list|)
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
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"@"
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2 logs an arbitrary message using logging level setup for QueryLogger.      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Logs database connection event using container data source.      *       * @since 1.2      */
specifier|public
specifier|static
name|void
name|logConnect
parameter_list|(
name|String
name|dataSource
parameter_list|)
block|{
name|logger
operator|.
name|logConnect
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logConnect
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|logger
operator|.
name|logConnect
argument_list|(
name|url
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * Logs database connection event.      *       * @since 1.2      */
specifier|public
specifier|static
name|void
name|logPoolCreated
parameter_list|(
name|DataSourceInfo
name|dsi
parameter_list|)
block|{
name|logger
operator|.
name|logPoolCreated
argument_list|(
name|dsi
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logConnectSuccess
parameter_list|()
block|{
name|logger
operator|.
name|logConnectSuccess
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logConnectFailure
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|logConnectFailure
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
specifier|static
name|void
name|logGeneratedKey
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|logger
operator|.
name|logGeneratedKey
argument_list|(
name|attribute
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logQuery
parameter_list|(
name|String
name|queryStr
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|params
parameter_list|)
block|{
name|logger
operator|.
name|logQuery
argument_list|(
name|queryStr
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
comment|/**      * Log query content using Log4J Category with "INFO" priority.      *       * @param queryStr Query SQL string      * @param attrs optional list of DbAttribute (can be null)      * @param params optional list of query parameters that are used when executing query      *            in prepared statement.      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logQuery
parameter_list|(
name|String
name|queryStr
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attrs
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|params
parameter_list|,
name|long
name|time
parameter_list|)
block|{
name|logger
operator|.
name|logQuery
argument_list|(
name|queryStr
argument_list|,
name|attrs
argument_list|,
name|params
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logQueryParameters
parameter_list|(
name|String
name|label
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attrs
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|isInserting
parameter_list|)
block|{
name|logger
operator|.
name|logQueryParameters
argument_list|(
name|label
argument_list|,
name|attrs
argument_list|,
name|parameters
argument_list|,
name|isInserting
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|logSelectCount
argument_list|(
name|count
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|time
parameter_list|)
block|{
name|logger
operator|.
name|logSelectCount
argument_list|(
name|count
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logUpdateCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|logger
operator|.
name|logUpdateCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logBeginTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|logBeginTransaction
argument_list|(
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logCommitTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|logCommitTransaction
argument_list|(
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logRollbackTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|logRollbackTransaction
argument_list|(
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logQueryError
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|logQueryError
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logQueryStart
parameter_list|(
name|int
name|count
parameter_list|)
block|{
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|String
name|countStr
init|=
operator|(
name|count
operator|==
literal|1
operator|)
condition|?
literal|"--- will run 1 query."
else|:
literal|"--- will run "
operator|+
name|count
operator|+
literal|" queries."
decl_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|countStr
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if current thread default log level is high enough for QueryLogger to      * generate output.      *       * @since 1.2      */
specifier|public
specifier|static
name|boolean
name|isLoggable
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isLoggable
argument_list|()
return|;
block|}
block|}
end_class

end_unit

