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
name|query
operator|.
name|Query
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

begin_comment
comment|/**  * A convenience superclass for SQLAction implementations.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseSQLAction
implements|implements
name|SQLAction
block|{
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|public
name|BaseSQLAction
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
block|}
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
comment|/**      * Helper method to process a ResultSet.      */
specifier|protected
name|void
name|readResultSet
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|,
name|RowDescriptor
name|descriptor
parameter_list|,
name|Query
name|query
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
name|JDBCResultIterator
name|resultReader
init|=
operator|new
name|JDBCResultIterator
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|resultSet
argument_list|,
name|descriptor
argument_list|,
name|query
operator|.
name|getMetaData
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|delegate
operator|.
name|isIteratedResult
argument_list|()
condition|)
block|{
name|List
name|resultRows
init|=
name|resultReader
operator|.
name|dataRows
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|QueryLogger
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
name|t1
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|nextDataRows
argument_list|(
name|query
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|resultReader
operator|.
name|setClosingConnection
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|nextDataRows
argument_list|(
name|query
argument_list|,
name|resultReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
try|try
block|{
name|resultReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|cex
parameter_list|)
block|{
comment|// ignore...
block|}
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

