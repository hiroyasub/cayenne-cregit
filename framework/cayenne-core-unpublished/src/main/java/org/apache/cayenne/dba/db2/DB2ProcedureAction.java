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
name|db2
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

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|DB2ProcedureAction
extends|extends
name|ProcedureAction
block|{
name|DB2ProcedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
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
comment|// cloned from super except for result processing consistent with
comment|// CAY-1874
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
name|initStatement
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|boolean
name|hasResultSet
init|=
name|statement
operator|.
name|execute
argument_list|()
decl_stmt|;
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
name|hasResultSet
operator|=
name|statement
operator|.
name|getMoreResults
argument_list|()
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
name|SQLException
name|ex
parameter_list|)
block|{
block|}
block|}
block|}
block|}
end_class

end_unit

