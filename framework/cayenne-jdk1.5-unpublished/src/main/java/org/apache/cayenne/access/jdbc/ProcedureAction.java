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
name|Collections
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
name|QueryLogger
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
name|map
operator|.
name|Procedure
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
name|ProcedureParameter
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

begin_comment
comment|/**  * A SQLAction that runs a stored procedure. Note that ProcedureAction has internal state  * and is not thread-safe.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureAction
extends|extends
name|BaseSQLAction
block|{
specifier|protected
name|ProcedureQuery
name|query
decl_stmt|;
comment|/**      * Holds a number of ResultSets processed by the action. This value is reset to zero      * on every "performAction" call.      */
specifier|protected
name|int
name|processedResultSets
decl_stmt|;
specifier|public
name|ProcedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
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
name|processedResultSets
operator|=
literal|0
expr_stmt|;
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
comment|// TODO: andrus, 4/2/2007 - according to the docs we should store the boolean
comment|// return value of this method and avoid calling 'getMoreResults' if it is
comment|// true.
comment|// some db's handle this well, some don't (MySQL).
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// read out parameters
name|readProcedureOutParameters
argument_list|(
name|statement
argument_list|,
name|observer
argument_list|)
expr_stmt|;
comment|// read the rest of the query
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|statement
operator|.
name|getMoreResults
argument_list|()
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
name|observer
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
name|QueryLogger
operator|.
name|logUpdateCount
argument_list|(
name|updateCount
argument_list|)
expr_stmt|;
name|observer
operator|.
name|nextCount
argument_list|(
name|query
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
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
name|SQLException
name|ex
parameter_list|)
block|{
block|}
block|}
block|}
comment|/**      * Returns the ProcedureTranslator to use for this ProcedureAction.      *       * @param connection JDBC connection      */
specifier|protected
name|ProcedureTranslator
name|createTranslator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|ProcedureTranslator
name|translator
init|=
operator|new
name|ProcedureTranslator
argument_list|()
decl_stmt|;
name|translator
operator|.
name|setAdapter
argument_list|(
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setEntityResolver
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
comment|/**      * Creates a RowDescriptor for result set.      *       * @param resultSet JDBC ResultSet      * @param setIndex a zero-based index of the ResultSet in the query results.      */
specifier|protected
name|RowDescriptor
name|describeResultSet
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|,
name|int
name|setIndex
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|setIndex
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected a non-negative result set index. Got: "
operator|+
name|setIndex
argument_list|)
throw|;
block|}
name|RowDescriptorBuilder
name|builder
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ColumnDescriptor
index|[]
argument_list|>
name|descriptors
init|=
name|query
operator|.
name|getResultDescriptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|descriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|setResultSet
argument_list|(
name|resultSet
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if one result is described, all of them must be present...
if|if
condition|(
name|setIndex
operator|>=
name|descriptors
operator|.
name|size
argument_list|()
operator|||
name|descriptors
operator|.
name|get
argument_list|(
name|setIndex
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No descriptor for result set at index '"
operator|+
name|setIndex
operator|+
literal|"' configured."
argument_list|)
throw|;
block|}
name|ColumnDescriptor
index|[]
name|columns
init|=
name|descriptors
operator|.
name|get
argument_list|(
name|setIndex
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setColumns
argument_list|(
name|columns
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|getDescriptor
argument_list|(
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns stored procedure for an internal query.      */
specifier|protected
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|getEntityResolver
argument_list|()
operator|.
name|lookupProcedure
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**      * Helper method that reads OUT parameters of a CallableStatement.      */
specifier|protected
name|void
name|readProcedureOutParameters
parameter_list|(
name|CallableStatement
name|statement
parameter_list|,
name|OperationObserver
name|delegate
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
comment|// build result row...
name|DataRow
name|result
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|parameters
init|=
name|getProcedure
argument_list|()
operator|.
name|getCallParameters
argument_list|()
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
name|parameters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ProcedureParameter
name|parameter
init|=
name|parameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|parameter
operator|.
name|isOutParam
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|DataRow
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|ColumnDescriptor
name|descriptor
init|=
operator|new
name|ColumnDescriptor
argument_list|(
name|parameter
argument_list|)
decl_stmt|;
name|ExtendedType
name|type
init|=
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|descriptor
operator|.
name|getJavaClass
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|type
operator|.
name|materializeObject
argument_list|(
name|statement
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|descriptor
operator|.
name|getJdbcType
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|descriptor
operator|.
name|getLabel
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// treat out parameters as a separate data row set
name|QueryLogger
operator|.
name|logSelectCount
argument_list|(
literal|1
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t1
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|nextDataRows
argument_list|(
name|query
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

