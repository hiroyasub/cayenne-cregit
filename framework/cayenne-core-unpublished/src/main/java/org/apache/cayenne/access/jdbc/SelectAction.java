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
name|trans
operator|.
name|SelectTranslator
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
name|PrefetchProcessor
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
name|PrefetchTreeNode
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * A SQLAction that handles SelectQuery execution.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SelectAction
extends|extends
name|BaseSQLAction
block|{
specifier|protected
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
decl_stmt|;
specifier|public
name|SelectAction
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
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
specifier|protected
name|SelectTranslator
name|createTranslator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|SelectTranslator
name|translator
init|=
operator|new
name|SelectTranslator
argument_list|()
decl_stmt|;
name|translator
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setAdapter
argument_list|(
name|adapter
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
name|translator
operator|.
name|setJdbcEventLogger
argument_list|(
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
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
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|SelectTranslator
name|translator
init|=
name|createTranslator
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|PreparedStatement
name|prepStmt
init|=
name|translator
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|ResultSet
name|rs
decl_stmt|;
comment|// need to run in try-catch block to close statement properly if exception happens
try|try
block|{
name|rs
operator|=
name|prepStmt
operator|.
name|executeQuery
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|prepStmt
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
name|QueryMetadata
name|md
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|RowDescriptor
name|descriptor
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
operator|.
name|setColumns
argument_list|(
name|translator
operator|.
name|getResultColumns
argument_list|()
argument_list|)
operator|.
name|getDescriptor
argument_list|(
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
argument_list|)
decl_stmt|;
name|JDBCResultIterator
name|workerIterator
init|=
operator|new
name|JDBCResultIterator
argument_list|(
name|connection
argument_list|,
name|prepStmt
argument_list|,
name|rs
argument_list|,
name|descriptor
argument_list|,
name|md
argument_list|)
decl_stmt|;
name|workerIterator
operator|.
name|setPostProcessor
argument_list|(
name|DataRowPostProcessor
operator|.
name|createPostProcessor
argument_list|(
name|translator
argument_list|)
argument_list|)
expr_stmt|;
name|ResultIterator
name|it
init|=
name|workerIterator
decl_stmt|;
comment|// wrap result iterator if distinct has to be suppressed
if|if
condition|(
name|translator
operator|.
name|isSuppressingDistinct
argument_list|()
condition|)
block|{
comment|// a joint prefetch warrants full row compare
specifier|final
name|boolean
index|[]
name|compareFullRows
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|PrefetchTreeNode
name|rootPrefetch
init|=
name|md
operator|.
name|getPrefetchTree
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootPrefetch
operator|!=
literal|null
condition|)
block|{
name|rootPrefetch
operator|.
name|traverse
argument_list|(
operator|new
name|PrefetchProcessor
argument_list|()
block|{
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// continue to children only if we are at root
return|return
name|rootPrefetch
operator|==
name|node
return|;
block|}
specifier|public
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// continue to children only if we are at root
return|return
name|rootPrefetch
operator|==
name|node
return|;
block|}
specifier|public
name|boolean
name|startUnknownPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// continue to children only if we are at root
return|return
name|rootPrefetch
operator|==
name|node
return|;
block|}
specifier|public
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|rootPrefetch
operator|!=
name|node
condition|)
block|{
name|compareFullRows
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|it
operator|=
operator|new
name|DistinctResultIterator
argument_list|(
name|workerIterator
argument_list|,
name|translator
operator|.
name|getRootDbEntity
argument_list|()
argument_list|,
name|compareFullRows
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|// wrap iterator in a fetch limit checker ... there are a few cases when in-memory
comment|// fetch limit is a noop, however in a general case this is needed, as the SQL
comment|// result count does not directly correspond to the number of objects returned
comment|// from Cayenne.
name|int
name|fetchLimit
init|=
name|query
operator|.
name|getFetchLimit
argument_list|()
decl_stmt|;
name|int
name|offset
init|=
name|translator
operator|.
name|isSuppressingDistinct
argument_list|()
condition|?
name|query
operator|.
name|getFetchOffset
argument_list|()
else|:
name|getInMemoryOffset
argument_list|(
name|query
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fetchLimit
operator|>
literal|0
operator|||
name|offset
operator|>
literal|0
condition|)
block|{
name|it
operator|=
operator|new
name|LimitResultIterator
argument_list|(
name|it
argument_list|,
name|offset
argument_list|,
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Should do something about closing ResultSet and PreparedStatement in this
comment|// method, instead of relying on DefaultResultIterator to do that later
if|if
condition|(
operator|!
name|observer
operator|.
name|isIteratedResult
argument_list|()
condition|)
block|{
comment|// note that we don't need to close ResultIterator
comment|// since "dataRows" will do it internally
name|List
argument_list|<
name|DataRow
argument_list|>
name|resultRows
decl_stmt|;
try|try
block|{
name|resultRows
operator|=
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
expr_stmt|;
block|}
finally|finally
block|{
name|it
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|adapter
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
argument_list|,
name|translator
operator|.
name|createSqlString
argument_list|()
argument_list|)
expr_stmt|;
name|observer
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
name|workerIterator
operator|.
name|setClosingConnection
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|observer
operator|.
name|nextRows
argument_list|(
name|translator
operator|.
name|getQuery
argument_list|()
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
block|}
end_class

end_unit
