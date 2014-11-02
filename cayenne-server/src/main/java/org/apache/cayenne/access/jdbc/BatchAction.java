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
name|OptimisticLockException
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
name|batch
operator|.
name|BatchParameterBinding
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
name|batch
operator|.
name|BatchTranslator
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
name|query
operator|.
name|BatchQuery
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
name|BatchQueryRow
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
name|InsertBatchQuery
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|BatchAction
extends|extends
name|BaseSQLAction
block|{
specifier|protected
name|boolean
name|runningAsBatch
decl_stmt|;
specifier|protected
name|BatchQuery
name|query
decl_stmt|;
specifier|protected
name|RowDescriptor
name|keyRowDescriptor
decl_stmt|;
specifier|private
specifier|static
name|void
name|bind
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|PreparedStatement
name|statement
parameter_list|,
name|BatchParameterBinding
index|[]
name|bindings
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
for|for
control|(
name|BatchParameterBinding
name|b
range|:
name|bindings
control|)
block|{
if|if
condition|(
operator|!
name|b
operator|.
name|isExcluded
argument_list|()
condition|)
block|{
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|b
operator|.
name|getValue
argument_list|()
argument_list|,
name|b
operator|.
name|getStatementPosition
argument_list|()
argument_list|,
name|b
operator|.
name|getAttribute
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|b
operator|.
name|getAttribute
argument_list|()
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 4.0      */
specifier|public
name|BatchAction
parameter_list|(
name|BatchQuery
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|,
name|boolean
name|runningAsBatch
parameter_list|)
block|{
name|super
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|runningAsBatch
operator|=
name|runningAsBatch
expr_stmt|;
block|}
comment|/**      * @return Query which originated this action      */
specifier|public
name|BatchQuery
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
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
name|observer
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|BatchTranslator
name|translator
init|=
name|createTranslator
argument_list|()
decl_stmt|;
name|boolean
name|generatesKeys
init|=
name|hasGeneratedKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|runningAsBatch
operator|&&
operator|!
name|generatesKeys
condition|)
block|{
name|runAsBatch
argument_list|(
name|connection
argument_list|,
name|translator
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|runAsIndividualQueries
argument_list|(
name|connection
argument_list|,
name|translator
argument_list|,
name|observer
argument_list|,
name|generatesKeys
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|BatchTranslator
name|createTranslator
parameter_list|()
throws|throws
name|CayenneException
block|{
return|return
name|dataNode
operator|.
name|batchTranslator
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|void
name|runAsBatch
parameter_list|(
name|Connection
name|con
parameter_list|,
name|BatchTranslator
name|translator
parameter_list|,
name|OperationObserver
name|delegate
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|String
name|sql
init|=
name|translator
operator|.
name|getSql
argument_list|()
decl_stmt|;
name|JdbcEventLogger
name|logger
init|=
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
decl_stmt|;
name|boolean
name|isLoggable
init|=
name|logger
operator|.
name|isLoggable
argument_list|()
decl_stmt|;
comment|// log batch SQL execution
name|logger
operator|.
name|logQuery
argument_list|(
name|sql
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
comment|// run batch
name|DbAdapter
name|adapter
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|PreparedStatement
name|statement
init|=
name|con
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|BatchQueryRow
name|row
range|:
name|query
operator|.
name|getRows
argument_list|()
control|)
block|{
name|BatchParameterBinding
index|[]
name|bindings
init|=
name|translator
operator|.
name|updateBindings
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|logger
operator|.
name|logQueryParameters
argument_list|(
literal|"batch bind"
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|adapter
argument_list|,
name|statement
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|statement
operator|.
name|addBatch
argument_list|()
expr_stmt|;
block|}
comment|// execute the whole batch
name|int
index|[]
name|results
init|=
name|statement
operator|.
name|executeBatch
argument_list|()
decl_stmt|;
name|delegate
operator|.
name|nextBatchCount
argument_list|(
name|query
argument_list|,
name|results
argument_list|)
expr_stmt|;
if|if
condition|(
name|isLoggable
condition|)
block|{
name|int
name|totalUpdateCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|result
range|:
name|results
control|)
block|{
comment|// this means Statement.SUCCESS_NO_INFO or
comment|// Statement.EXECUTE_FAILED
if|if
condition|(
name|result
operator|<
literal|0
condition|)
block|{
name|totalUpdateCount
operator|=
name|Statement
operator|.
name|SUCCESS_NO_INFO
expr_stmt|;
break|break;
block|}
name|totalUpdateCount
operator|+=
name|result
expr_stmt|;
block|}
name|logger
operator|.
name|logUpdateCount
argument_list|(
name|totalUpdateCount
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
block|}
comment|/**      * Executes batch as individual queries over the same prepared statement.      */
specifier|protected
name|void
name|runAsIndividualQueries
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|BatchTranslator
name|translator
parameter_list|,
name|OperationObserver
name|delegate
parameter_list|,
name|boolean
name|generatesKeys
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|JdbcEventLogger
name|logger
init|=
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
decl_stmt|;
name|boolean
name|useOptimisticLock
init|=
name|query
operator|.
name|isUsingOptimisticLocking
argument_list|()
decl_stmt|;
name|String
name|queryStr
init|=
name|translator
operator|.
name|getSql
argument_list|()
decl_stmt|;
comment|// log batch SQL execution
name|logger
operator|.
name|logQuery
argument_list|(
name|queryStr
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
comment|// run batch queries one by one
name|DbAdapter
name|adapter
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|PreparedStatement
name|statement
init|=
operator|(
name|generatesKeys
operator|)
condition|?
name|connection
operator|.
name|prepareStatement
argument_list|(
name|queryStr
argument_list|,
name|Statement
operator|.
name|RETURN_GENERATED_KEYS
argument_list|)
else|:
name|connection
operator|.
name|prepareStatement
argument_list|(
name|queryStr
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|BatchQueryRow
name|row
range|:
name|query
operator|.
name|getRows
argument_list|()
control|)
block|{
name|BatchParameterBinding
index|[]
name|bindings
init|=
name|translator
operator|.
name|updateBindings
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|logger
operator|.
name|logQueryParameters
argument_list|(
literal|"bind"
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|adapter
argument_list|,
name|statement
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|int
name|updated
init|=
name|statement
operator|.
name|executeUpdate
argument_list|()
decl_stmt|;
if|if
condition|(
name|useOptimisticLock
operator|&&
name|updated
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|OptimisticLockException
argument_list|(
name|row
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|query
operator|.
name|getDbEntity
argument_list|()
argument_list|,
name|queryStr
argument_list|,
name|row
operator|.
name|getQualifier
argument_list|()
argument_list|)
throw|;
block|}
name|delegate
operator|.
name|nextCount
argument_list|(
name|query
argument_list|,
name|updated
argument_list|)
expr_stmt|;
if|if
condition|(
name|generatesKeys
condition|)
block|{
name|processGeneratedKeys
argument_list|(
name|statement
argument_list|,
name|delegate
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|logUpdateCount
argument_list|(
name|updated
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
block|}
comment|/**      * Returns whether BatchQuery generates any keys.      */
specifier|protected
name|boolean
name|hasGeneratedKeys
parameter_list|()
block|{
comment|// see if we are configured to support generated keys
if|if
condition|(
operator|!
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// see if the query needs them
if|if
condition|(
name|query
operator|instanceof
name|InsertBatchQuery
condition|)
block|{
comment|// see if any of the generated attributes is PK
for|for
control|(
specifier|final
name|DbAttribute
name|attr
range|:
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getGeneratedAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Implements generated keys extraction supported in JDBC 3.0 specification.      *       * @since 4.0      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|protected
name|void
name|processGeneratedKeys
parameter_list|(
name|Statement
name|statement
parameter_list|,
name|OperationObserver
name|observer
parameter_list|,
name|BatchQueryRow
name|row
parameter_list|)
throws|throws
name|SQLException
throws|,
name|CayenneException
block|{
name|ResultSet
name|keysRS
init|=
name|statement
operator|.
name|getGeneratedKeys
argument_list|()
decl_stmt|;
comment|// TODO: andrus, 7/4/2007 - (1) get the type of meaningful PK's from
comment|// their
comment|// ObjAttributes; (2) use a different form of Statement.execute -
comment|// "execute(String,String[])" to be able to map generated column names
comment|// (this way
comment|// we can support multiple columns.. although need to check how well
comment|// this works
comment|// with most common drivers)
name|RowDescriptorBuilder
name|builder
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|keyRowDescriptor
operator|==
literal|null
condition|)
block|{
comment|// attempt to figure out the right descriptor from the mapping...
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|generated
init|=
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getGeneratedAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|generated
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbAttribute
name|key
init|=
name|generated
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ColumnDescriptor
index|[]
name|columns
init|=
operator|new
name|ColumnDescriptor
index|[
literal|1
index|]
decl_stmt|;
comment|// use column name from result set, but type and Java class from
comment|// DB
comment|// attribute
name|columns
index|[
literal|0
index|]
operator|=
operator|new
name|ColumnDescriptor
argument_list|(
name|keysRS
operator|.
name|getMetaData
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|columns
index|[
literal|0
index|]
operator|.
name|setJdbcType
argument_list|(
name|key
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|columns
index|[
literal|0
index|]
operator|.
name|setJavaClass
argument_list|(
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|key
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setColumns
argument_list|(
name|columns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|setResultSet
argument_list|(
name|keysRS
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|keyRowDescriptor
operator|=
name|builder
operator|.
name|getDescriptor
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|keyRowDescriptor
argument_list|,
name|query
operator|.
name|getMetaData
argument_list|(
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|,
name|Collections
operator|.
expr|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
operator|>
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|ResultIterator
name|iterator
init|=
operator|new
name|JDBCResultIterator
argument_list|(
literal|null
argument_list|,
name|keysRS
argument_list|,
name|rowReader
argument_list|)
decl_stmt|;
name|observer
operator|.
name|nextGeneratedRows
argument_list|(
name|query
argument_list|,
name|iterator
argument_list|,
name|row
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

