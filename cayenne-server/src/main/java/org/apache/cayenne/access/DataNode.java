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
package|;
end_package

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
name|dbsync
operator|.
name|SchemaUpdateStrategy
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
name|SQLTemplateProcessor
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
name|jdbc
operator|.
name|reader
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
name|access
operator|.
name|translator
operator|.
name|batch
operator|.
name|BatchTranslatorFactory
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
name|select
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
name|access
operator|.
name|translator
operator|.
name|select
operator|.
name|SelectTranslatorFactory
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
name|log
operator|.
name|NoopJdbcEventLogger
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
name|DataMap
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
name|Select
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
name|tx
operator|.
name|BaseTransaction
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
name|tx
operator|.
name|Transaction
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
name|ToStringBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLFeatureNotSupportedException
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * An abstraction of a single physical data storage. This is usually a database  * server, but can potentially be some other storage type like an LDAP server,  * etc.  */
end_comment

begin_class
specifier|public
class|class
name|DataNode
implements|implements
name|QueryEngine
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|DataSource
name|dataSource
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|String
name|dataSourceFactory
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|SchemaUpdateStrategy
name|schemaUpdateStrategy
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DataMap
argument_list|>
name|dataMaps
decl_stmt|;
specifier|private
name|JdbcEventLogger
name|jdbcEventLogger
decl_stmt|;
specifier|private
name|RowReaderFactory
name|rowReaderFactory
decl_stmt|;
specifier|private
name|BatchTranslatorFactory
name|batchTranslatorFactory
decl_stmt|;
specifier|private
name|SelectTranslatorFactory
name|selectTranslatorFactory
decl_stmt|;
specifier|private
name|SQLTemplateProcessor
name|sqlTemplateProcessor
decl_stmt|;
name|TransactionDataSource
name|readThroughDataSource
decl_stmt|;
comment|/** 	 * Creates a new unnamed DataNode. 	 */
specifier|public
name|DataNode
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Creates a new DataNode, assigning it a name. 	 */
specifier|public
name|DataNode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|dataMaps
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|readThroughDataSource
operator|=
operator|new
name|TransactionDataSource
argument_list|()
expr_stmt|;
comment|// make sure logger is not null
name|this
operator|.
name|jdbcEventLogger
operator|=
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|SchemaUpdateStrategy
name|getSchemaUpdateStrategy
parameter_list|()
block|{
return|return
name|schemaUpdateStrategy
return|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|void
name|setSchemaUpdateStrategy
parameter_list|(
name|SchemaUpdateStrategy
name|schemaUpdateStrategy
parameter_list|)
block|{
name|this
operator|.
name|schemaUpdateStrategy
operator|=
name|schemaUpdateStrategy
expr_stmt|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|public
name|JdbcEventLogger
name|getJdbcEventLogger
parameter_list|()
block|{
if|if
condition|(
name|jdbcEventLogger
operator|==
literal|null
operator|&&
name|adapter
operator|instanceof
name|JdbcAdapter
condition|)
block|{
name|jdbcEventLogger
operator|=
operator|(
operator|(
name|JdbcAdapter
operator|)
name|adapter
operator|)
operator|.
name|getJdbcEventLogger
argument_list|()
expr_stmt|;
block|}
return|return
name|jdbcEventLogger
return|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|public
name|void
name|setJdbcEventLogger
parameter_list|(
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|jdbcEventLogger
operator|=
name|logger
expr_stmt|;
block|}
comment|/** 	 * Returns node name. Name is used to uniquely identify DataNode within a 	 * DataDomain. 	 */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/** 	 * Returns a name of DataSourceFactory class for this node. 	 */
specifier|public
name|String
name|getDataSourceFactory
parameter_list|()
block|{
return|return
name|dataSourceFactory
return|;
block|}
specifier|public
name|void
name|setDataSourceFactory
parameter_list|(
name|String
name|dataSourceFactory
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactory
operator|=
name|dataSourceFactory
expr_stmt|;
block|}
comment|/** 	 * Returns an unmodifiable collection of DataMaps handled by this DataNode. 	 */
specifier|public
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|getDataMaps
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|dataMaps
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Returns datamap with specified name, null if none present 	 */
specifier|public
name|DataMap
name|getDataMap
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|dataMaps
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDataMaps
parameter_list|(
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
parameter_list|)
block|{
for|for
control|(
name|DataMap
name|map
range|:
name|dataMaps
control|)
block|{
name|this
operator|.
name|dataMaps
operator|.
name|put
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Adds a DataMap to be handled by this node. 	 */
specifier|public
name|void
name|addDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|this
operator|.
name|dataMaps
operator|.
name|put
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|removeDataMap
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeDataMap
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
name|dataMaps
operator|.
name|remove
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns DataSource used by this DataNode to obtain connections. 	 */
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
operator|!=
literal|null
condition|?
name|readThroughDataSource
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
comment|/** 	 * Returns DbAdapter object. This is a plugin that handles RDBMS 	 * vendor-specific features. 	 */
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
name|void
name|setAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/** 	 * Returns a DataNode that should handle queries for all DataMap components. 	 * 	 * @since 1.1 	 */
specifier|public
name|DataNode
name|lookupDataNode
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
comment|// we don't know any better than to return ourselves...
return|return
name|this
return|;
block|}
comment|/** 	 * Runs queries using Connection obtained from internal DataSource. 	 * 	 * @since 1.1 	 */
annotation|@
name|Override
specifier|public
name|void
name|performQueries
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Query
argument_list|>
name|queries
parameter_list|,
name|OperationObserver
name|callback
parameter_list|)
block|{
name|int
name|listSize
init|=
name|queries
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|listSize
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|callback
operator|.
name|isIteratedResult
argument_list|()
operator|&&
name|listSize
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Iterated queries are not allowed in a batch. Batch size: %d"
argument_list|,
name|listSize
argument_list|)
throw|;
block|}
comment|// do this meaningless inexpensive operation to trigger AutoAdapter lazy
comment|// initialization before opening a connection. Otherwise we may end up
comment|// with two
comment|// connections open simultaneously, possibly hitting connection pool
comment|// upper limit.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
expr_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|this
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|globalEx
parameter_list|)
block|{
name|getJdbcEventLogger
argument_list|()
operator|.
name|logQueryError
argument_list|(
name|globalEx
argument_list|)
expr_stmt|;
name|Transaction
name|transaction
init|=
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
if|if
condition|(
name|transaction
operator|!=
literal|null
condition|)
block|{
name|transaction
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
block|}
name|callback
operator|.
name|nextGlobalException
argument_list|(
name|globalEx
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|DataNodeQueryAction
name|queryRunner
init|=
operator|new
name|DataNodeQueryAction
argument_list|(
name|this
argument_list|,
name|callback
argument_list|)
decl_stmt|;
for|for
control|(
name|Query
name|nextQuery
range|:
name|queries
control|)
block|{
comment|// catch exceptions for each individual query
try|try
block|{
name|queryRunner
operator|.
name|runQuery
argument_list|(
name|connection
argument_list|,
name|nextQuery
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|queryEx
parameter_list|)
block|{
name|getJdbcEventLogger
argument_list|()
operator|.
name|logQueryError
argument_list|(
name|queryEx
argument_list|)
expr_stmt|;
comment|// notify consumer of the exception,
comment|// stop running further queries
name|callback
operator|.
name|nextQueryException
argument_list|(
name|nextQuery
argument_list|,
name|queryEx
argument_list|)
expr_stmt|;
name|Transaction
name|transaction
init|=
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
if|if
condition|(
name|transaction
operator|!=
literal|null
condition|)
block|{
name|transaction
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
finally|finally
block|{
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// ignore closing exceptions...
block|}
block|}
block|}
comment|/** 	 * Returns EntityResolver that handles DataMaps of this node. 	 */
annotation|@
name|Override
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
comment|/** 	 * Sets EntityResolver. DataNode relies on externally set EntityResolver, so 	 * if the node is created outside of DataDomain stack, a valid 	 * EntityResolver must be provided explicitly. 	 * 	 * @since 1.1 	 */
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** 	 * Creates a {@link RowReader} using internal {@link RowReaderFactory}. 	 * 	 * @since 4.0 	 */
specifier|public
name|RowReader
argument_list|<
name|?
argument_list|>
name|rowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|)
block|{
return|return
name|rowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
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
return|;
block|}
comment|/** 	 * Creates a {@link RowReader} using internal {@link RowReaderFactory}. 	 * 	 * @since 4.0 	 */
specifier|public
name|RowReader
argument_list|<
name|?
argument_list|>
name|rowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
parameter_list|)
block|{
return|return
name|rowReaderFactory
operator|.
name|rowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|getAdapter
argument_list|()
argument_list|,
name|attributeOverrides
argument_list|)
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|BatchTranslator
name|batchTranslator
parameter_list|(
name|BatchQuery
name|query
parameter_list|,
name|String
name|trimFunction
parameter_list|)
block|{
return|return
name|batchTranslatorFactory
operator|.
name|translator
argument_list|(
name|query
argument_list|,
name|getAdapter
argument_list|()
argument_list|,
name|trimFunction
argument_list|)
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|SelectTranslator
name|selectTranslator
parameter_list|(
name|Select
argument_list|<
name|?
argument_list|>
name|query
parameter_list|)
block|{
return|return
name|selectTranslatorFactory
operator|.
name|translator
argument_list|(
name|query
argument_list|,
name|getAdapter
argument_list|()
argument_list|,
name|getEntityResolver
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|RowReaderFactory
name|getRowReaderFactory
parameter_list|()
block|{
return|return
name|rowReaderFactory
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|void
name|setRowReaderFactory
parameter_list|(
name|RowReaderFactory
name|rowReaderFactory
parameter_list|)
block|{
name|this
operator|.
name|rowReaderFactory
operator|=
name|rowReaderFactory
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|BatchTranslatorFactory
name|getBatchTranslatorFactory
parameter_list|()
block|{
return|return
name|batchTranslatorFactory
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|void
name|setBatchTranslatorFactory
parameter_list|(
name|BatchTranslatorFactory
name|batchTranslatorFactory
parameter_list|)
block|{
name|this
operator|.
name|batchTranslatorFactory
operator|=
name|batchTranslatorFactory
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|SQLTemplateProcessor
name|getSqlTemplateProcessor
parameter_list|()
block|{
return|return
name|sqlTemplateProcessor
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|void
name|setSqlTemplateProcessor
parameter_list|(
name|SQLTemplateProcessor
name|sqlTemplateProcessor
parameter_list|)
block|{
name|this
operator|.
name|sqlTemplateProcessor
operator|=
name|sqlTemplateProcessor
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|SelectTranslatorFactory
name|getSelectTranslatorFactory
parameter_list|()
block|{
return|return
name|selectTranslatorFactory
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|void
name|setSelectTranslatorFactory
parameter_list|(
name|SelectTranslatorFactory
name|selectTranslatorFactory
parameter_list|)
block|{
name|this
operator|.
name|selectTranslatorFactory
operator|=
name|selectTranslatorFactory
expr_stmt|;
block|}
comment|// a read-through DataSource that ensures returning the same connection
comment|// within
comment|// transaction.
specifier|final
class|class
name|TransactionDataSource
implements|implements
name|DataSource
block|{
specifier|final
name|String
name|CONNECTION_RESOURCE_PREFIX
init|=
literal|"DataNode.Connection."
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|schemaUpdateStrategy
operator|!=
literal|null
condition|)
block|{
name|schemaUpdateStrategy
operator|.
name|updateSchema
argument_list|(
name|DataNode
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
name|Transaction
name|t
init|=
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
return|return
operator|(
name|t
operator|!=
literal|null
operator|)
condition|?
name|t
operator|.
name|getOrCreateConnection
argument_list|(
name|CONNECTION_RESOURCE_PREFIX
operator|+
name|name
argument_list|,
name|dataSource
argument_list|)
else|:
name|dataSource
operator|.
name|getConnection
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|schemaUpdateStrategy
operator|!=
literal|null
condition|)
block|{
name|schemaUpdateStrategy
operator|.
name|updateSchema
argument_list|(
name|DataNode
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
name|Transaction
name|t
init|=
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
return|return
operator|(
name|t
operator|!=
literal|null
operator|)
condition|?
name|t
operator|.
name|getOrCreateConnection
argument_list|(
name|CONNECTION_RESOURCE_PREFIX
operator|+
name|name
argument_list|,
name|dataSource
argument_list|)
else|:
name|dataSource
operator|.
name|getConnection
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dataSource
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dataSource
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|SQLException
block|{
name|dataSource
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|SQLException
block|{
name|dataSource
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**          * @since 3.0          */
annotation|@
name|Override
specifier|public
name|boolean
name|isWrapperFor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|iface
operator|.
name|isAssignableFrom
argument_list|(
name|dataSource
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**          * @since 3.0          */
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
return|return
name|iface
operator|.
name|cast
argument_list|(
name|dataSource
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Not a DataSource: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**          * @since 3.1          */
annotation|@
name|Override
specifier|public
name|Logger
name|getParentLogger
parameter_list|()
throws|throws
name|SQLFeatureNotSupportedException
block|{
comment|// don't throw SQLFeatureNotSupported - this will break JDK 1.5
comment|// runtime
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

