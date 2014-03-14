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
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Blob
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Clob
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
name|Types
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
name|LOBBatchQueryBuilder
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
name|LOBInsertBatchQueryBuilder
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
name|LOBUpdateBatchQueryBuilder
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
name|InsertBatchQuery
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
name|UpdateBatchQuery
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

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|OracleLOBBatchAction
implements|implements
name|SQLAction
block|{
name|BatchQuery
name|query
decl_stmt|;
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
name|OracleLOBBatchAction
parameter_list|(
name|BatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|JdbcEventLogger
name|logger
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
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
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
name|LOBBatchQueryBuilder
name|queryBuilder
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|InsertBatchQuery
condition|)
block|{
name|queryBuilder
operator|=
operator|new
name|LOBInsertBatchQueryBuilder
argument_list|(
operator|(
name|InsertBatchQuery
operator|)
name|query
argument_list|,
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|UpdateBatchQuery
condition|)
block|{
name|queryBuilder
operator|=
operator|new
name|LOBUpdateBatchQueryBuilder
argument_list|(
operator|(
name|UpdateBatchQuery
operator|)
name|query
argument_list|,
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Unsupported batch type for special LOB processing: "
operator|+
name|query
argument_list|)
throw|;
block|}
name|queryBuilder
operator|.
name|setTrimFunction
argument_list|(
name|OracleAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
expr_stmt|;
name|queryBuilder
operator|.
name|setNewBlobFunction
argument_list|(
name|OracleAdapter
operator|.
name|NEW_BLOB_FUNCTION
argument_list|)
expr_stmt|;
name|queryBuilder
operator|.
name|setNewClobFunction
argument_list|(
name|OracleAdapter
operator|.
name|NEW_CLOB_FUNCTION
argument_list|)
expr_stmt|;
comment|// no batching is done, queries are translated
comment|// for each batch set, since prepared statements
comment|// may be different depending on whether LOBs are NULL or not..
name|OracleLOBBatchQueryWrapper
name|selectQuery
init|=
operator|new
name|OracleLOBBatchQueryWrapper
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
init|=
name|selectQuery
operator|.
name|getDbAttributesForLOBSelectQualifier
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
name|query
operator|.
name|reset
argument_list|()
expr_stmt|;
while|while
condition|(
name|query
operator|.
name|next
argument_list|()
condition|)
block|{
name|selectQuery
operator|.
name|indexLOBAttributes
argument_list|()
expr_stmt|;
name|int
name|updated
init|=
literal|0
decl_stmt|;
name|String
name|updateStr
init|=
name|queryBuilder
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// 1. run row update
name|logger
operator|.
name|logQuery
argument_list|(
name|updateStr
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
name|PreparedStatement
name|statement
init|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|updateStr
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isLoggable
condition|)
block|{
name|List
name|bindings
init|=
name|queryBuilder
operator|.
name|getValuesForLOBUpdateParameters
argument_list|()
decl_stmt|;
name|logger
operator|.
name|logQueryParameters
argument_list|(
literal|"bind"
argument_list|,
literal|null
argument_list|,
name|bindings
argument_list|,
name|query
operator|instanceof
name|InsertBatchQuery
argument_list|)
expr_stmt|;
block|}
name|queryBuilder
operator|.
name|bindParameters
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|updated
operator|=
name|statement
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
name|logger
operator|.
name|logUpdateCount
argument_list|(
name|updated
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
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
comment|// 2. run row LOB update (SELECT...FOR UPDATE and writing out LOBs)
name|processLOBRow
argument_list|(
name|connection
argument_list|,
name|queryBuilder
argument_list|,
name|selectQuery
argument_list|,
name|qualifierAttributes
argument_list|)
expr_stmt|;
comment|// finally, notify delegate that the row was updated
name|observer
operator|.
name|nextCount
argument_list|(
name|query
argument_list|,
name|updated
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|processLOBRow
parameter_list|(
name|Connection
name|con
parameter_list|,
name|LOBBatchQueryBuilder
name|queryBuilder
parameter_list|,
name|OracleLOBBatchQueryWrapper
name|selectQuery
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|lobAttributes
init|=
name|selectQuery
operator|.
name|getDbAttributesForUpdatedLOBColumns
argument_list|()
decl_stmt|;
if|if
condition|(
name|lobAttributes
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|boolean
name|isLoggable
init|=
name|logger
operator|.
name|isLoggable
argument_list|()
decl_stmt|;
name|List
name|qualifierValues
init|=
name|selectQuery
operator|.
name|getValuesForLOBSelectQualifier
argument_list|()
decl_stmt|;
name|List
name|lobValues
init|=
name|selectQuery
operator|.
name|getValuesForUpdatedLOBColumns
argument_list|()
decl_stmt|;
name|int
name|parametersSize
init|=
name|qualifierValues
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|lobSize
init|=
name|lobAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|String
name|selectStr
init|=
name|queryBuilder
operator|.
name|createLOBSelectString
argument_list|(
name|lobAttributes
argument_list|,
name|qualifierAttributes
argument_list|)
decl_stmt|;
if|if
condition|(
name|isLoggable
condition|)
block|{
name|logger
operator|.
name|logQuery
argument_list|(
name|selectStr
argument_list|,
name|qualifierValues
argument_list|)
expr_stmt|;
name|logger
operator|.
name|logQueryParameters
argument_list|(
literal|"write LOB"
argument_list|,
literal|null
argument_list|,
name|lobValues
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|PreparedStatement
name|selectStatement
init|=
name|con
operator|.
name|prepareStatement
argument_list|(
name|selectStr
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parametersSize
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
name|qualifierValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|qualifierAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|adapter
operator|.
name|bindParameter
argument_list|(
name|selectStatement
argument_list|,
name|value
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ResultSet
name|result
init|=
name|selectStatement
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Missing LOB row."
argument_list|)
throw|;
block|}
comment|// read the only expected row
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lobSize
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|attribute
init|=
name|lobAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|type
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|Clob
name|clob
init|=
name|result
operator|.
name|getClob
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Object
name|clobVal
init|=
name|lobValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|clobVal
operator|instanceof
name|char
index|[]
condition|)
block|{
name|writeClob
argument_list|(
name|clob
argument_list|,
operator|(
name|char
index|[]
operator|)
name|clobVal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeClob
argument_list|(
name|clob
argument_list|,
name|clobVal
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|Blob
name|blob
init|=
name|result
operator|.
name|getBlob
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Object
name|blobVal
init|=
name|lobValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|blobVal
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|writeBlob
argument_list|(
name|blob
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|blobVal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|className
init|=
operator|(
name|blobVal
operator|!=
literal|null
operator|)
condition|?
name|blobVal
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported class of BLOB value: "
operator|+
name|className
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only BLOB or CLOB is expected here, got: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|result
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"More than one LOB row found."
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|result
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
finally|finally
block|{
try|try
block|{
name|selectStatement
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
comment|/**      * Writing of LOBs is not supported prior to JDBC 3.0 and has to be done      * using Oracle driver utilities, using reflection.      */
specifier|protected
name|void
name|writeBlob
parameter_list|(
name|Blob
name|blob
parameter_list|,
name|byte
index|[]
name|value
parameter_list|)
block|{
try|try
block|{
name|OutputStream
name|out
init|=
name|blob
operator|.
name|setBinaryStream
argument_list|(
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error processing BLOB."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Writing of LOBs is not supported prior to JDBC 3.0 and has to be done      * using Oracle driver utilities.      */
specifier|protected
name|void
name|writeClob
parameter_list|(
name|Clob
name|clob
parameter_list|,
name|char
index|[]
name|value
parameter_list|)
block|{
try|try
block|{
name|Writer
name|out
init|=
name|clob
operator|.
name|setCharacterStream
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error processing CLOB."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Writing of LOBs is not supported prior to JDBC 3.0 and has to be done      * using Oracle driver utilities.      */
specifier|protected
name|void
name|writeClob
parameter_list|(
name|Clob
name|clob
parameter_list|,
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|Writer
name|out
init|=
name|clob
operator|.
name|setCharacterStream
argument_list|(
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error processing CLOB."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

