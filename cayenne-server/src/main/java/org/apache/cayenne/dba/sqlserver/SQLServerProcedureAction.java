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
name|sqlserver
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
name|Connection
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
name|ProcedureAction
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
name|RowDescriptor
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
name|RowReaderFactory
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
name|trans
operator|.
name|ProcedureTranslator
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
name|JdbcAdapter
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
name|EntityResolver
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
name|ProcedureQuery
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
name|Query
import|;
end_import

begin_comment
comment|/**  * ProcedureAction for SQLServer MS JDBC driver. Customizes OUT parameter processing - it  * has to be done AFTER the ResultSets are read (note that jTDS driver works fine with  * normal ProcedureAction).  *<p>  *<i>See JIRA CAY-251 for details.</i>  *</p>  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerProcedureAction
extends|extends
name|ProcedureAction
block|{
comment|/**      * @since 3.2      */
specifier|public
name|SQLServerProcedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|,
name|RowReaderFactory
name|rowReaderFactory
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
argument_list|)
expr_stmt|;
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
name|ProcedureTranslator
name|transl
init|=
name|createTranslator
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|CallableStatement
name|statement
init|=
operator|(
name|CallableStatement
operator|)
name|transl
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
comment|// stored procedure may contain a mixture of update counts and result sets,
comment|// and out parameters. Read out parameters first, then
comment|// iterate until we exhaust all results
name|boolean
name|hasResultSet
init|=
name|statement
operator|.
name|execute
argument_list|()
decl_stmt|;
comment|// local observer to cache results and provide them to the external observer
comment|// in the order consistent with other adapters.
name|Observer
name|localObserver
init|=
operator|new
name|Observer
argument_list|(
name|observer
argument_list|)
decl_stmt|;
comment|// read query, using local observer
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|hasResultSet
condition|)
block|{
name|ResultSet
name|rs
init|=
name|statement
operator|.
name|getResultSet
argument_list|()
decl_stmt|;
try|try
block|{
name|RowDescriptor
name|descriptor
init|=
name|describeResultSet
argument_list|(
name|rs
argument_list|,
name|processedResultSets
operator|++
argument_list|)
decl_stmt|;
name|readResultSet
argument_list|(
name|rs
argument_list|,
name|descriptor
argument_list|,
name|query
argument_list|,
name|localObserver
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
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
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logUpdateCount
argument_list|(
name|updateCount
argument_list|)
expr_stmt|;
name|localObserver
operator|.
name|nextCount
argument_list|(
name|query
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
name|hasResultSet
operator|=
name|statement
operator|.
name|getMoreResults
argument_list|()
expr_stmt|;
block|}
comment|// read out parameters to the main observer ... AFTER the main result set
comment|// TODO: I hope SQLServer does not support ResultSets as OUT parameters,
comment|// otherwise
comment|// the order of custom result descriptors will be messed up
name|readProcedureOutParameters
argument_list|(
name|statement
argument_list|,
name|observer
argument_list|)
expr_stmt|;
comment|// add results back to main observer
name|localObserver
operator|.
name|flushResults
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|SQLException
name|ex
parameter_list|)
block|{
block|}
block|}
block|}
class|class
name|Observer
implements|implements
name|OperationObserver
block|{
name|List
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|results
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|counts
decl_stmt|;
name|OperationObserver
name|observer
decl_stmt|;
name|Observer
parameter_list|(
name|OperationObserver
name|observer
parameter_list|)
block|{
name|this
operator|.
name|observer
operator|=
name|observer
expr_stmt|;
block|}
name|void
name|flushResults
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|List
argument_list|<
name|?
argument_list|>
name|result
range|:
name|results
control|)
block|{
name|observer
operator|.
name|nextRows
argument_list|(
name|query
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
name|results
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|counts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Integer
name|count
range|:
name|counts
control|)
block|{
name|observer
operator|.
name|nextCount
argument_list|(
name|query
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|counts
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextBatchCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
index|[]
name|resultCount
parameter_list|)
block|{
name|observer
operator|.
name|nextBatchCount
argument_list|(
name|query
argument_list|,
name|resultCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
name|resultCount
parameter_list|)
block|{
comment|// does not delegate to wrapped observer
comment|// but instead caches results locally.
if|if
condition|(
name|counts
operator|==
literal|null
condition|)
block|{
name|counts
operator|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|counts
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|resultCount
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|dataRows
parameter_list|)
block|{
comment|// does not delegate to wrapped observer
comment|// but instead caches results locally.
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|ArrayList
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|results
operator|.
name|add
argument_list|(
name|dataRows
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
name|observer
operator|.
name|nextRows
argument_list|(
name|q
argument_list|,
name|it
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|observer
operator|.
name|nextGlobalException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextGeneratedRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
name|observer
operator|.
name|nextGeneratedRows
argument_list|(
name|query
argument_list|,
name|keysIterator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextQueryException
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|observer
operator|.
name|nextQueryException
argument_list|(
name|query
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
name|observer
operator|.
name|isIteratedResult
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

