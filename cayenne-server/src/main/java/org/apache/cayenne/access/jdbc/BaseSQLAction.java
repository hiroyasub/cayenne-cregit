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

begin_comment
comment|/**  * A convenience superclass for SQLAction implementations.  *   * @since 1.2  */
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
name|DataNode
name|dataNode
decl_stmt|;
comment|/**      * @since 4.0      */
specifier|public
name|BaseSQLAction
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
name|this
operator|.
name|dataNode
operator|=
name|dataNode
expr_stmt|;
block|}
comment|/**      * Helper method to process a ResultSet.      */
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
name|QueryMetadata
name|metadata
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
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
name|descriptor
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
name|JDBCResultIterator
name|resultReader
init|=
operator|new
name|JDBCResultIterator
argument_list|(
literal|null
argument_list|,
name|resultSet
argument_list|,
name|rowReader
argument_list|)
decl_stmt|;
name|LimitResultIterator
name|it
init|=
operator|new
name|LimitResultIterator
argument_list|(
name|resultReader
argument_list|,
name|getInMemoryOffset
argument_list|(
name|metadata
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
argument_list|,
name|metadata
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
name|t1
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|nextRows
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
name|delegate
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
block|}
comment|/**      * Returns a value of the offset that will be used to rewind the ResultSet      * within the SQL action before reading the result rows. The default      * implementation returns 'queryOffset' argument. If the adapter supports      * setting offset at the SQL level, this method must be overridden to return      * zero to suppress manual offset.      *       * @since 3.0      */
specifier|protected
name|int
name|getInMemoryOffset
parameter_list|(
name|int
name|queryOffset
parameter_list|)
block|{
return|return
name|queryOffset
return|;
block|}
block|}
end_class

end_unit

