begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|oracle
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
name|ColumnDescriptor
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
comment|/**  * Oracle-specific ProcedureAction that supports ResultSet OUT parameters.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|OracleProcedureAction
extends|extends
name|ProcedureAction
block|{
name|OracleProcedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Helper method that reads OUT parameters of a CallableStatement. 	 */
annotation|@
name|Override
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
comment|// ==== start Oracle-specific part
if|if
condition|(
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|OracleAdapter
operator|.
name|getOracleCursorType
argument_list|()
condition|)
block|{
try|try
init|(
name|ResultSet
name|rs
init|=
operator|(
name|ResultSet
operator|)
name|statement
operator|.
name|getObject
argument_list|(
name|i
operator|+
literal|1
argument_list|)
init|;
init|)
block|{
name|RowDescriptor
name|rsDescriptor
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
name|rsDescriptor
argument_list|,
name|query
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ==== end Oracle-specific part
else|else
block|{
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
name|dataNode
operator|.
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
name|getDataRowKey
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
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
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
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
name|nextRows
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

