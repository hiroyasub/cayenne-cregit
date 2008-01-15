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
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * QueryLogger is intended to log special events that happen whenever Cayenne interacts  * with a database. This includes execution of generated SQL statements, result counts,  * connection events, etc. Normally QueryLogger methods are not invoked directly by the  * . Rather it is a single logging point used by the framework.  *<p>  * Internally QueryLogger uses commons-logging at the "info" level.  *</p>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|QueryLogger
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|QueryLogger
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TRIM_VALUES_THRESHOLD
init|=
literal|30
decl_stmt|;
comment|/**      * @since 1.2      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|static
name|ThreadLocal
name|logLevel
init|=
operator|new
name|ThreadLocal
argument_list|()
decl_stmt|;
comment|/**      * Appends SQL literal for the specified object to the buffer. This is a utility      * method and is not intended to build SQL queries, rather this is used in logging      * routines. In particular it will trim large values to avoid flooding the logs.      *</p>      *       * @param buffer buffer to append value      * @param object object to be transformed to SQL literal.      */
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
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"Connecting. JNDI path: "
operator|+
name|dataSource
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"Opening connection: "
argument_list|)
decl_stmt|;
comment|// append URL on the same line to make log somewhat grep-friendly
name|buf
operator|.
name|append
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tLogin: "
argument_list|)
operator|.
name|append
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tPassword: *******"
argument_list|)
expr_stmt|;
name|logObj
operator|.
name|info
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"Created connection pool: "
argument_list|)
decl_stmt|;
if|if
condition|(
name|dsi
operator|!=
literal|null
condition|)
block|{
comment|// append URL on the same line to make log somewhat grep-friendly
name|buf
operator|.
name|append
argument_list|(
name|dsi
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dsi
operator|.
name|getAdapterClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tCayenne DbAdapter: "
argument_list|)
operator|.
name|append
argument_list|(
name|dsi
operator|.
name|getAdapterClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tDriver class: "
argument_list|)
operator|.
name|append
argument_list|(
name|dsi
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dsi
operator|.
name|getMinConnections
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tMin. connections in the pool: "
argument_list|)
operator|.
name|append
argument_list|(
name|dsi
operator|.
name|getMinConnections
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dsi
operator|.
name|getMaxConnections
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n\tMax. connections in the pool: "
argument_list|)
operator|.
name|append
argument_list|(
name|dsi
operator|.
name|getMaxConnections
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" pool information unavailable"
argument_list|)
expr_stmt|;
block|}
name|logObj
operator|.
name|info
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|static
name|void
name|logConnectSuccess
parameter_list|()
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"+++ Connecting: SUCCESS."
argument_list|)
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
name|logObj
operator|.
name|info
argument_list|(
literal|"*** Connecting: FAILURE."
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|buildLog
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|postfix
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|isInserting
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
name|parameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|DbAttribute
name|attribute
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|attributeIterator
init|=
literal|null
decl_stmt|;
name|int
name|position
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
name|attributeIterator
operator|=
name|attributes
operator|.
name|iterator
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|parameter
range|:
name|parameters
control|)
block|{
comment|// If at the beginning, output the prefix, otherwise a separator.
if|if
condition|(
name|position
operator|++
operator|==
literal|0
condition|)
name|buffer
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
else|else
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
comment|// Find the next attribute and SKIP generated attributes.  Only
comment|// skip when logging inserts, though.  Should show previously
comment|// generated keys on DELETE, UPDATE, or SELECT.
while|while
condition|(
name|attributeIterator
operator|!=
literal|null
operator|&&
name|attributeIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|attribute
operator|=
name|attributeIterator
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|isInserting
operator|==
literal|false
operator|||
name|attribute
operator|.
name|isGenerated
argument_list|()
operator|==
literal|false
condition|)
break|break;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|position
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|sqlLiteralForObject
argument_list|(
name|buffer
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|postfix
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isInserting
parameter_list|(
name|String
name|query
parameter_list|)
block|{
if|if
condition|(
name|query
operator|==
literal|null
operator|||
name|query
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
return|return
literal|false
return|;
name|char
name|firstCharacter
init|=
name|query
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstCharacter
operator|==
literal|'I'
operator|||
name|firstCharacter
operator|==
literal|'i'
condition|)
return|return
literal|true
return|;
else|else
return|return
literal|false
return|;
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
name|logQuery
argument_list|(
name|queryStr
argument_list|,
literal|null
argument_list|,
name|params
argument_list|,
operator|-
literal|1
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
name|queryStr
argument_list|)
decl_stmt|;
name|buildLog
argument_list|(
name|buf
argument_list|,
literal|" [bind: "
argument_list|,
literal|"]"
argument_list|,
name|attrs
argument_list|,
name|params
argument_list|,
name|isInserting
argument_list|(
name|queryStr
argument_list|)
argument_list|)
expr_stmt|;
comment|// log preparation time only if it is something significant
if|if
condition|(
name|time
operator|>
literal|5
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" - prepared in "
argument_list|)
operator|.
name|append
argument_list|(
name|time
argument_list|)
operator|.
name|append
argument_list|(
literal|" ms."
argument_list|)
expr_stmt|;
block|}
name|logObj
operator|.
name|info
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|String
name|prefix
init|=
literal|"["
operator|+
name|label
operator|+
literal|": "
decl_stmt|;
if|if
condition|(
name|isLoggable
argument_list|()
operator|&&
name|parameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buildLog
argument_list|(
name|buf
argument_list|,
name|prefix
argument_list|,
literal|"]"
argument_list|,
name|attrs
argument_list|,
name|parameters
argument_list|,
name|isInserting
argument_list|)
expr_stmt|;
name|logObj
operator|.
name|info
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"=== returned 1 row."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"=== returned "
argument_list|)
operator|.
name|append
argument_list|(
name|count
argument_list|)
operator|.
name|append
argument_list|(
literal|" rows."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|time
operator|>=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" - took "
argument_list|)
operator|.
name|append
argument_list|(
name|time
argument_list|)
operator|.
name|append
argument_list|(
literal|" ms."
argument_list|)
expr_stmt|;
block|}
name|logObj
operator|.
name|info
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
if|if
condition|(
name|count
operator|<
literal|0
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"=== updated ? rows"
argument_list|)
expr_stmt|;
block|}
else|else
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
literal|"=== updated 1 row."
else|:
literal|"=== updated "
operator|+
name|count
operator|+
literal|" rows."
decl_stmt|;
name|logObj
operator|.
name|info
argument_list|(
name|countStr
argument_list|)
expr_stmt|;
block|}
block|}
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
name|logObj
operator|.
name|info
argument_list|(
literal|"--- "
operator|+
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
name|logObj
operator|.
name|info
argument_list|(
literal|"+++ "
operator|+
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
name|logObj
operator|.
name|info
argument_list|(
literal|"*** "
operator|+
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
if|if
condition|(
name|th
operator|!=
literal|null
condition|)
block|{
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
name|logObj
operator|.
name|info
argument_list|(
literal|"*** error."
argument_list|,
name|th
argument_list|)
expr_stmt|;
if|if
condition|(
name|th
operator|instanceof
name|SQLException
condition|)
block|{
name|SQLException
name|sqlException
init|=
operator|(
operator|(
name|SQLException
operator|)
name|th
operator|)
operator|.
name|getNextException
argument_list|()
decl_stmt|;
while|while
condition|(
name|sqlException
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"*** nested SQL error."
argument_list|,
name|sqlException
argument_list|)
expr_stmt|;
name|sqlException
operator|=
name|sqlException
operator|.
name|getNextException
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
name|logObj
operator|.
name|info
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
name|logObj
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
block|}
end_class

end_unit

