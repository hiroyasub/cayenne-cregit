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
name|log
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
name|translator
operator|.
name|DbAttributeBinding
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
name|translator
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
name|configuration
operator|.
name|Constants
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
name|configuration
operator|.
name|RuntimeProperties
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
name|di
operator|.
name|Inject
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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

begin_comment
comment|/**  * A {@link JdbcEventLogger} built on top of slf4j-api logger.  *   * @since 3.1  * @since 4.0 renamed from CommonsJdbcEventLogger to Slf4jJdbcEventLogger as part of migration to SLF4J  */
end_comment

begin_class
specifier|public
class|class
name|Slf4jJdbcEventLogger
implements|implements
name|JdbcEventLogger
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JdbcEventLogger
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @deprecated since 4.0      */
specifier|private
specifier|static
specifier|final
name|int
name|TRIM_VALUES_THRESHOLD
init|=
literal|30
decl_stmt|;
specifier|protected
name|long
name|queryExecutionTimeLoggingThreshold
decl_stmt|;
specifier|public
name|Slf4jJdbcEventLogger
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|)
block|{
name|this
operator|.
name|queryExecutionTimeLoggingThreshold
operator|=
name|runtimeProperties
operator|.
name|getLong
argument_list|(
name|Constants
operator|.
name|QUERY_EXECUTION_TIME_LOGGING_THRESHOLD_PROPERTY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
name|logger
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|String
name|entity
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Generated PK: "
operator|+
name|entity
operator|+
literal|"."
operator|+
name|attribute
operator|.
name|getName
argument_list|()
operator|+
literal|" = "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
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
return|return
name|firstCharacter
operator|==
literal|'I'
operator|||
name|firstCharacter
operator|==
literal|'i'
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|sql
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|appendParameters
argument_list|(
name|buffer
argument_list|,
literal|"bind"
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|logQueryParameters
parameter_list|(
name|String
name|label
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
if|if
condition|(
name|isLoggable
argument_list|()
operator|&&
name|bindings
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendParameters
argument_list|(
name|buffer
argument_list|,
name|label
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|appendParameters
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|label
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|int
name|len
init|=
name|bindings
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|len
operator|>
literal|0
condition|)
block|{
name|boolean
name|hasIncluded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|1
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|ParameterBinding
name|b
init|=
name|bindings
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|isExcluded
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|hasIncluded
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hasIncluded
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|label
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|j
operator|++
argument_list|)
expr_stmt|;
if|if
condition|(
name|b
operator|instanceof
name|DbAttributeBinding
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
operator|(
name|DbAttributeBinding
operator|)
name|b
operator|)
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
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
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|b
operator|.
name|getExtendedType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|b
operator|.
name|getExtendedType
argument_list|()
operator|.
name|toString
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|b
operator|.
name|getValue
argument_list|()
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
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
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
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hasIncluded
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
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
name|logSelectCount
argument_list|(
name|count
argument_list|,
name|time
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|time
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
if|if
condition|(
name|isLoggable
argument_list|()
condition|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
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
name|logger
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
if|if
condition|(
name|queryExecutionTimeLoggingThreshold
operator|>
literal|0
operator|&&
name|time
operator|>
name|queryExecutionTimeLoggingThreshold
condition|)
block|{
name|String
name|message
init|=
literal|"Query time exceeded threshold ("
operator|+
name|time
operator|+
literal|" ms): "
decl_stmt|;
name|logger
operator|.
name|warn
argument_list|(
name|message
operator|+
name|sql
argument_list|,
operator|new
name|CayenneRuntimeException
argument_list|(
name|message
operator|+
literal|"%s"
argument_list|,
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
name|logger
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
name|logger
operator|.
name|info
argument_list|(
name|countStr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|logBeginTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"--- "
operator|+
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logCommitTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"+++ "
operator|+
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logRollbackTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"*** "
operator|+
name|transactionLabel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
name|logger
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
name|logger
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
annotation|@
name|Override
specifier|public
name|boolean
name|isLoggable
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
block|}
end_class

end_unit

