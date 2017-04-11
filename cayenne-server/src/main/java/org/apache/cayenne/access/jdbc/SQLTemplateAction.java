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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|DataRow
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
name|ResultIterator
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
name|DataNode
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
name|OperationObserver
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
name|reader
operator|.
name|RowReader
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
name|access
operator|.
name|types
operator|.
name|ExtendedType
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
name|ExtendedTypeMap
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
name|dba
operator|.
name|DbAdapter
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
name|dba
operator|.
name|TypesMapping
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
name|map
operator|.
name|DbEntity
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
name|ObjAttribute
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
name|ObjEntity
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
name|query
operator|.
name|QueryMetadata
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
name|query
operator|.
name|SQLAction
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
name|query
operator|.
name|SQLTemplate
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
name|collections
operator|.
name|IteratorUtils
import|;
end_import

begin_comment
comment|/**  * Implements a strategy for execution of SQLTemplates.  *   * @since 1.2 replaces SQLTemplateExecutionPlan  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateAction
implements|implements
name|SQLAction
block|{
specifier|protected
name|SQLTemplate
name|query
decl_stmt|;
specifier|protected
name|QueryMetadata
name|queryMetadata
decl_stmt|;
specifier|protected
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|protected
name|DataNode
name|dataNode
decl_stmt|;
specifier|protected
name|DbAdapter
name|dbAdapter
decl_stmt|;
comment|/** 	 * @since 4.0 	 */
specifier|public
name|SQLTemplateAction
parameter_list|(
name|SQLTemplate
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|dataNode
operator|=
name|dataNode
expr_stmt|;
name|this
operator|.
name|queryMetadata
operator|=
name|query
operator|.
name|getMetaData
argument_list|(
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|queryMetadata
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
comment|// using unwrapped adapter to check for the right SQL flavor...
name|this
operator|.
name|dbAdapter
operator|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|unwrap
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Returns unwrapped DbAdapter used to find correct SQL for a given DB. 	 */
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|dbAdapter
return|;
block|}
comment|/** 	 * Runs a SQLTemplate query, collecting all results. If a callback expects 	 * an iterated result, result processing is stopped after the first 	 * ResultSet is encountered. 	 */
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|callback
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|String
name|template
init|=
name|extractTemplateString
argument_list|()
decl_stmt|;
comment|// sanity check - misconfigured templates
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"No template string configured for adapter "
operator|+
name|dbAdapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|loggable
init|=
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|isLoggable
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|counts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// bind either positional or named parameters;
comment|// for legacy reasons named parameters are processed as a batch.. this
comment|// should go away after 4.0; newer positional parameter only support a
comment|// single set of values.
if|if
condition|(
name|query
operator|.
name|getPositionalParams
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|runWithNamedParametersBatch
argument_list|(
name|connection
argument_list|,
name|callback
argument_list|,
name|template
argument_list|,
name|counts
argument_list|,
name|loggable
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|runWithPositionalParameters
argument_list|(
name|connection
argument_list|,
name|callback
argument_list|,
name|template
argument_list|,
name|counts
argument_list|,
name|loggable
argument_list|)
expr_stmt|;
block|}
comment|// notify of combined counts of all queries inside SQLTemplate
comment|// multiplied by the number of parameter sets...
name|int
index|[]
name|ints
init|=
operator|new
name|int
index|[
name|counts
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ints
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ints
index|[
name|i
index|]
operator|=
name|counts
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
name|callback
operator|.
name|nextBatchCount
argument_list|(
name|query
argument_list|,
name|ints
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runWithPositionalParameters
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|callback
parameter_list|,
name|String
name|template
parameter_list|,
name|Collection
argument_list|<
name|Number
argument_list|>
name|counts
parameter_list|,
name|boolean
name|loggable
parameter_list|)
throws|throws
name|Exception
block|{
name|SQLStatement
name|compiled
init|=
name|dataNode
operator|.
name|getSqlTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|template
argument_list|,
name|query
operator|.
name|getPositionalParams
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|loggable
condition|)
block|{
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logQuery
argument_list|(
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|compiled
operator|.
name|getBindings
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|execute
argument_list|(
name|connection
argument_list|,
name|callback
argument_list|,
name|compiled
argument_list|,
name|counts
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
name|void
name|runWithNamedParametersBatch
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|callback
parameter_list|,
name|String
name|template
parameter_list|,
name|Collection
argument_list|<
name|Number
argument_list|>
name|counts
parameter_list|,
name|boolean
name|loggable
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|size
init|=
name|query
operator|.
name|parametersSize
argument_list|()
decl_stmt|;
comment|// zero size indicates a one-shot query with no parameters
comment|// so fake a single entry batch...
name|int
name|batchSize
init|=
operator|(
name|size
operator|>
literal|0
operator|)
condition|?
name|size
else|:
literal|1
decl_stmt|;
comment|// for now supporting deprecated batch parameters...
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|it
init|=
operator|(
name|size
operator|>
literal|0
operator|)
condition|?
name|query
operator|.
name|parametersIterator
argument_list|()
else|:
name|IteratorUtils
operator|.
name|singletonIterator
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|batchSize
condition|;
name|i
operator|++
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|nextParameters
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|dataNode
operator|.
name|getSqlTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|template
argument_list|,
name|nextParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|loggable
condition|)
block|{
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logQuery
argument_list|(
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|compiled
operator|.
name|getBindings
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|execute
argument_list|(
name|connection
argument_list|,
name|callback
argument_list|,
name|compiled
argument_list|,
name|counts
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|execute
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|callback
parameter_list|,
name|SQLStatement
name|compiled
parameter_list|,
name|Collection
argument_list|<
name|Number
argument_list|>
name|updateCounts
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|iteratedResult
init|=
name|callback
operator|.
name|isIteratedResult
argument_list|()
decl_stmt|;
name|PreparedStatement
name|statement
init|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|bind
argument_list|(
name|statement
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
argument_list|)
expr_stmt|;
comment|// process a mix of results
name|boolean
name|isResultSet
init|=
name|statement
operator|.
name|execute
argument_list|()
decl_stmt|;
name|boolean
name|firstIteration
init|=
literal|true
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|firstIteration
condition|)
block|{
name|firstIteration
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|isResultSet
operator|=
name|statement
operator|.
name|getMoreResults
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isResultSet
condition|)
block|{
name|ResultSet
name|resultSet
init|=
name|statement
operator|.
name|getResultSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultSet
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|processSelectResult
argument_list|(
name|compiled
argument_list|,
name|connection
argument_list|,
name|statement
argument_list|,
name|resultSet
argument_list|,
name|callback
argument_list|,
name|t1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|iteratedResult
condition|)
block|{
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// ignore possible following update counts and bail early on iterated results
if|if
condition|(
name|iteratedResult
condition|)
block|{
break|break;
block|}
block|}
block|}
else|else
block|{
name|int
name|updateCount
init|=
name|statement
operator|.
name|getUpdateCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|updateCount
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|updateCounts
operator|.
name|add
argument_list|(
name|updateCount
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logUpdateCount
argument_list|(
name|updateCount
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|iteratedResult
condition|)
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|protected
name|void
name|processSelectResult
parameter_list|(
name|SQLStatement
name|compiled
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|Statement
name|statement
parameter_list|,
name|ResultSet
name|resultSet
parameter_list|,
name|OperationObserver
name|callback
parameter_list|,
specifier|final
name|long
name|startTime
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|iteratedResult
init|=
name|callback
operator|.
name|isIteratedResult
argument_list|()
decl_stmt|;
name|ExtendedTypeMap
name|types
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
decl_stmt|;
name|RowDescriptorBuilder
name|builder
init|=
name|configureRowDescriptorBuilder
argument_list|(
name|compiled
argument_list|,
name|resultSet
argument_list|)
decl_stmt|;
name|RowReader
argument_list|<
name|?
argument_list|>
name|rowReader
init|=
name|dataNode
operator|.
name|rowReader
argument_list|(
name|builder
operator|.
name|getDescriptor
argument_list|(
name|types
argument_list|)
argument_list|,
name|queryMetadata
argument_list|)
decl_stmt|;
name|ResultIterator
name|it
init|=
operator|new
name|JDBCResultIterator
argument_list|(
name|statement
argument_list|,
name|resultSet
argument_list|,
name|rowReader
argument_list|)
decl_stmt|;
if|if
condition|(
name|iteratedResult
condition|)
block|{
name|it
operator|=
operator|new
name|ConnectionAwareResultIterator
argument_list|(
name|it
argument_list|,
name|connection
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doClose
parameter_list|()
block|{
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logSelectCount
argument_list|(
name|rowCounter
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|startTime
argument_list|)
expr_stmt|;
name|super
operator|.
name|doClose
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
name|it
operator|=
operator|new
name|LimitResultIterator
argument_list|(
name|it
argument_list|,
name|getFetchOffset
argument_list|()
argument_list|,
name|query
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|iteratedResult
condition|)
block|{
try|try
block|{
name|callback
operator|.
name|nextRows
argument_list|(
name|query
argument_list|,
name|it
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|it
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
else|else
block|{
comment|// note that we are not closing the iterator here, relying on caller
comment|// to close the underlying ResultSet on its own... this is a hack,
comment|// maybe a cleaner flow is due here.
name|List
argument_list|<
name|DataRow
argument_list|>
name|resultRows
init|=
operator|(
name|List
argument_list|<
name|DataRow
argument_list|>
operator|)
name|it
operator|.
name|allRows
argument_list|()
decl_stmt|;
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logSelectCount
argument_list|(
name|resultRows
operator|.
name|size
argument_list|()
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|startTime
argument_list|)
expr_stmt|;
name|callback
operator|.
name|nextRows
argument_list|(
name|query
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * @since 3.0 	 */
specifier|protected
name|RowDescriptorBuilder
name|configureRowDescriptorBuilder
parameter_list|(
name|SQLStatement
name|compiled
parameter_list|,
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|SQLException
block|{
name|RowDescriptorBuilder
name|builder
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
operator|.
name|setResultSet
argument_list|(
name|resultSet
argument_list|)
operator|.
name|validateDuplicateColumnNames
argument_list|()
decl_stmt|;
comment|// SQLTemplate #result columns take precedence over other ways to determine the type
if|if
condition|(
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|setColumns
argument_list|(
name|compiled
operator|.
name|getResultColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ObjEntity
name|entity
init|=
name|queryMetadata
operator|.
name|getObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
comment|// TODO: andrus 2008/03/28 support flattened attributes with aliases...
for|for
control|(
name|ObjAttribute
name|attribute
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|String
name|column
init|=
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|column
operator|==
literal|null
operator|||
name|column
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
continue|continue;
block|}
name|builder
operator|.
name|overrideColumnType
argument_list|(
name|column
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// override numeric Java types based on JDBC defaults for DbAttributes, as Oracle
comment|// ResultSetMetadata is not very precise about NUMERIC distinctions...
comment|// (BigDecimal vs Long vs. Integer)
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|dbEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|builder
operator|.
name|isOverriden
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|overrideColumnType
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
switch|switch
condition|(
name|query
operator|.
name|getColumnNamesCapitalization
argument_list|()
condition|)
block|{
case|case
name|LOWER
case|:
name|builder
operator|.
name|useLowercaseColumnNames
argument_list|()
expr_stmt|;
break|break;
case|case
name|UPPER
case|:
name|builder
operator|.
name|useUppercaseColumnNames
argument_list|()
expr_stmt|;
break|break;
block|}
return|return
name|builder
return|;
block|}
comment|/** 	 * Extracts a template string from a SQLTemplate query. Exists mainly for 	 * the benefit of subclasses that can customize returned template. 	 *  	 * @since 1.2 	 */
specifier|protected
name|String
name|extractTemplateString
parameter_list|()
block|{
name|String
name|sql
init|=
name|query
operator|.
name|getTemplate
argument_list|(
name|dbAdapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// note that we MUST convert line breaks to spaces. On some databases (DB2)
comment|// queries with breaks simply won't run; the rest are affected by CAY-726.
return|return
name|Util
operator|.
name|stripLineBreaks
argument_list|(
name|sql
argument_list|,
literal|' '
argument_list|)
return|;
block|}
comment|/** 	 * Binds parameters to the PreparedStatement. 	 */
specifier|protected
name|void
name|bind
parameter_list|(
name|PreparedStatement
name|preparedStatement
parameter_list|,
name|SQLParameterBinding
index|[]
name|bindings
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
comment|// bind parameters
if|if
condition|(
name|bindings
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|bindings
operator|.
name|length
decl_stmt|;
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
name|Object
name|value
init|=
name|bindings
index|[
name|i
index|]
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ExtendedType
name|extendedType
init|=
name|value
operator|!=
literal|null
condition|?
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
else|:
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|ParameterBinding
name|binding
init|=
operator|new
name|ParameterBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setType
argument_list|(
name|bindings
index|[
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setStatementPosition
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setExtendedType
argument_list|(
name|extendedType
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|bindParameter
argument_list|(
name|preparedStatement
argument_list|,
name|binding
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|queryMetadata
operator|.
name|getStatementFetchSize
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|preparedStatement
operator|.
name|setFetchSize
argument_list|(
name|queryMetadata
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Returns a SQLTemplate for this action. 	 */
specifier|public
name|SQLTemplate
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|protected
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|query
operator|.
name|getFetchOffset
argument_list|()
return|;
block|}
block|}
end_class

end_unit

