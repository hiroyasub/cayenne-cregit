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
name|DatabaseMetaData
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
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
name|ObjectId
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
name|DbEntity
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
name|DbKeyGenerator
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
name|SQLTemplate
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
name|IDUtil
import|;
end_import

begin_comment
comment|/**  * Default primary key generator implementation. Uses a lookup table named  * "AUTO_PK_SUPPORT" to search and increment primary keys for tables.  */
end_comment

begin_class
specifier|public
class|class
name|JdbcPkGenerator
implements|implements
name|PkGenerator
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PK_CACHE_SIZE
init|=
literal|20
decl_stmt|;
specifier|static
specifier|final
name|long
name|DEFAULT_PK_START_VALUE
init|=
literal|200
decl_stmt|;
specifier|protected
name|JdbcAdapter
name|adapter
decl_stmt|;
specifier|protected
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Queue
argument_list|<
name|Long
argument_list|>
argument_list|>
name|pkCache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Queue
argument_list|<
name|Long
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|int
name|pkCacheSize
init|=
name|DEFAULT_PK_CACHE_SIZE
decl_stmt|;
specifier|protected
name|long
name|pkStartValue
init|=
name|DEFAULT_PK_START_VALUE
decl_stmt|;
specifier|public
name|JdbcPkGenerator
parameter_list|(
name|JdbcAdapter
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
specifier|public
name|JdbcAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|void
name|createAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
comment|// check if a table exists
comment|// create AUTO_PK_SUPPORT table
if|if
condition|(
operator|!
name|autoPkTableExists
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|pkTableCreateString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// delete any existing pk entries
if|if
condition|(
operator|!
name|dbEntities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|pkDeleteString
argument_list|(
name|dbEntities
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// insert all needed entries
for|for
control|(
name|DbEntity
name|ent
range|:
name|dbEntities
control|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|pkCreateString
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createAutoPkStatements
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|dbEntities
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|pkTableCreateString
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|pkDeleteString
argument_list|(
name|dbEntities
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DbEntity
name|ent
range|:
name|dbEntities
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|pkCreateString
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/** 	 * Drops table named "AUTO_PK_SUPPORT" if it exists in the database. 	 */
specifier|public
name|void
name|dropAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|autoPkTableExists
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|dropAutoPkString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|dropAutoPkStatements
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|dropAutoPkString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
specifier|protected
name|String
name|pkTableCreateString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"CREATE TABLE AUTO_PK_SUPPORT ("
argument_list|)
operator|.
name|append
argument_list|(
literal|"  TABLE_NAME CHAR(100) NOT NULL,"
argument_list|)
operator|.
name|append
argument_list|(
literal|"  NEXT_ID BIGINT NOT NULL,"
argument_list|)
operator|.
name|append
argument_list|(
literal|"  PRIMARY KEY(TABLE_NAME)"
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|pkDeleteString
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"DELETE FROM AUTO_PK_SUPPORT WHERE TABLE_NAME IN ("
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|dbEntities
operator|.
name|size
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbEntity
name|ent
init|=
name|dbEntities
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
operator|.
name|append
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|pkCreateString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"INSERT INTO AUTO_PK_SUPPORT"
argument_list|)
operator|.
name|append
argument_list|(
literal|" (TABLE_NAME, NEXT_ID)"
argument_list|)
operator|.
name|append
argument_list|(
literal|" VALUES ('"
argument_list|)
operator|.
name|append
argument_list|(
name|entName
argument_list|)
operator|.
name|append
argument_list|(
literal|"', "
argument_list|)
operator|.
name|append
argument_list|(
name|pkStartValue
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|pkSelectString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"SELECT NEXT_ID FROM AUTO_PK_SUPPORT WHERE TABLE_NAME = '"
argument_list|)
operator|.
name|append
argument_list|(
name|entName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|pkUpdateString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"UPDATE AUTO_PK_SUPPORT"
argument_list|)
operator|.
name|append
argument_list|(
literal|" SET NEXT_ID = NEXT_ID + "
argument_list|)
operator|.
name|append
argument_list|(
name|pkCacheSize
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE TABLE_NAME = '"
argument_list|)
operator|.
name|append
argument_list|(
name|entName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|dropAutoPkString
parameter_list|()
block|{
return|return
literal|"DROP TABLE AUTO_PK_SUPPORT"
return|;
block|}
comment|/** 	 * Checks if AUTO_PK_TABLE already exists in the database. 	 */
specifier|protected
name|boolean
name|autoPkTableExists
parameter_list|(
name|DataNode
name|node
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
block|{
name|DatabaseMetaData
name|md
init|=
name|con
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
try|try
init|(
name|ResultSet
name|tables
init|=
name|md
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"AUTO_PK_SUPPORT"
argument_list|,
literal|null
argument_list|)
init|;
init|)
block|{
return|return
name|tables
operator|.
name|next
argument_list|()
return|;
block|}
block|}
block|}
comment|/** 	 * Runs JDBC update over a Connection obtained from DataNode. Returns a 	 * number of objects returned from update. 	 *  	 * @throws SQLException 	 *             in case of query failure. 	 */
specifier|public
name|int
name|runUpdate
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
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
try|try
init|(
name|Connection
name|con
init|=
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
block|{
try|try
init|(
name|Statement
name|upd
init|=
name|con
operator|.
name|createStatement
argument_list|()
init|;
init|)
block|{
return|return
name|upd
operator|.
name|executeUpdate
argument_list|(
name|sql
argument_list|)
return|;
block|}
block|}
block|}
comment|/** 	 * Generates a unique and non-repeating primary key for specified dbEntity. 	 *<p> 	 * This implementation is naive since it does not lock the database rows 	 * when executing select and subsequent update. Adapter-specific 	 * implementations are more robust. 	 *</p> 	 *  	 * @since 3.0 	 */
specifier|public
name|Object
name|generatePk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbAttribute
name|pk
parameter_list|)
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|pk
operator|.
name|getEntity
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|pk
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|Types
operator|.
name|BINARY
case|:
case|case
name|Types
operator|.
name|VARBINARY
case|:
return|return
name|IDUtil
operator|.
name|pseudoUniqueSecureByteSequence
argument_list|(
name|pk
operator|.
name|getMaxLength
argument_list|()
argument_list|)
return|;
block|}
name|DbKeyGenerator
name|pkGenerator
init|=
name|entity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
name|long
name|cacheSize
decl_stmt|;
if|if
condition|(
name|pkGenerator
operator|!=
literal|null
operator|&&
name|pkGenerator
operator|.
name|getKeyCacheSize
argument_list|()
operator|!=
literal|null
condition|)
name|cacheSize
operator|=
name|pkGenerator
operator|.
name|getKeyCacheSize
argument_list|()
operator|.
name|intValue
argument_list|()
expr_stmt|;
else|else
name|cacheSize
operator|=
name|pkCacheSize
expr_stmt|;
name|Long
name|value
decl_stmt|;
comment|// if no caching, always generate fresh
if|if
condition|(
name|cacheSize
operator|<=
literal|1
condition|)
block|{
name|value
operator|=
name|longPkFromDatabase
argument_list|(
name|node
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Queue
argument_list|<
name|Long
argument_list|>
name|pks
init|=
name|pkCache
operator|.
name|get
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pks
operator|==
literal|null
condition|)
block|{
comment|// created exhausted LongPkRange
name|pks
operator|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|Long
argument_list|>
argument_list|()
expr_stmt|;
name|Queue
argument_list|<
name|Long
argument_list|>
name|previousPks
init|=
name|pkCache
operator|.
name|putIfAbsent
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|pks
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousPks
operator|!=
literal|null
condition|)
block|{
name|pks
operator|=
name|previousPks
expr_stmt|;
block|}
block|}
name|value
operator|=
name|pks
operator|.
name|poll
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|longPkFromDatabase
argument_list|(
name|node
argument_list|,
name|entity
argument_list|)
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
name|value
operator|+
literal|1
init|;
name|i
operator|<
name|value
operator|+
name|cacheSize
condition|;
name|i
operator|++
control|)
block|{
name|pks
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|pk
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BIGINT
condition|)
block|{
return|return
name|value
return|;
block|}
else|else
block|{
comment|// leaving it up to the user to ensure that PK does not exceed max
comment|// int...
return|return
name|value
operator|.
name|intValue
argument_list|()
return|;
block|}
block|}
comment|/** 	 * Performs primary key generation ignoring cache. Generates a range of 	 * primary keys as specified by "pkCacheSize" bean property. 	 *<p> 	 * This method is called internally from "generatePkForDbEntity" and then 	 * generated range of key values is saved in cache for performance. 	 * Subclasses that implement different primary key generation solutions 	 * should override this method, not "generatePkForDbEntity". 	 *</p> 	 *  	 * @since 3.0 	 */
specifier|protected
name|long
name|longPkFromDatabase
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|select
init|=
literal|"SELECT #result('NEXT_ID' 'long' 'NEXT_ID') "
operator|+
literal|"FROM AUTO_PK_SUPPORT "
operator|+
literal|"WHERE TABLE_NAME = '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|'\''
decl_stmt|;
comment|// run queries via DataNode to utilize its transactional behavior
name|List
argument_list|<
name|Query
argument_list|>
name|queries
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|queries
operator|.
name|add
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
name|select
argument_list|)
argument_list|)
expr_stmt|;
name|queries
operator|.
name|add
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
name|pkUpdateString
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|PkRetrieveProcessor
name|observer
init|=
operator|new
name|PkRetrieveProcessor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|queries
argument_list|,
name|observer
argument_list|)
expr_stmt|;
return|return
name|observer
operator|.
name|getId
argument_list|()
return|;
block|}
comment|/** 	 * Returns a size of the entity primary key cache. Default value is 20. If 	 * cache size is set to a value less or equals than "one", no primary key 	 * caching is done. 	 */
specifier|public
name|int
name|getPkCacheSize
parameter_list|()
block|{
return|return
name|pkCacheSize
return|;
block|}
comment|/** 	 * Sets the size of the entity primary key cache. If 	 *<code>pkCacheSize</code> parameter is less than 1, cache size is set to 	 * "one". 	 *<p> 	 *<i>Note that our tests show that setting primary key cache value to 	 * anything much bigger than 20 does not give any significant performance 	 * increase. Therefore it does not make sense to use bigger values, since 	 * this may potentially create big gaps in the database primary key 	 * sequences in cases like application crashes or restarts.</i> 	 *</p> 	 */
specifier|public
name|void
name|setPkCacheSize
parameter_list|(
name|int
name|pkCacheSize
parameter_list|)
block|{
name|this
operator|.
name|pkCacheSize
operator|=
operator|(
name|pkCacheSize
operator|<
literal|1
operator|)
condition|?
literal|1
else|:
name|pkCacheSize
expr_stmt|;
block|}
name|long
name|getPkStartValue
parameter_list|()
block|{
return|return
name|pkStartValue
return|;
block|}
name|void
name|setPkStartValue
parameter_list|(
name|long
name|startValue
parameter_list|)
block|{
name|this
operator|.
name|pkStartValue
operator|=
name|startValue
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|pkCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * OperationObserver for primary key retrieval. 	 */
specifier|final
class|class
name|PkRetrieveProcessor
implements|implements
name|OperationObserver
block|{
name|Number
name|id
decl_stmt|;
name|String
name|entityName
decl_stmt|;
name|PkRetrieveProcessor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|long
name|getId
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No key was retrieved for entity "
operator|+
name|entityName
argument_list|)
throw|;
block|}
return|return
name|id
operator|.
name|longValue
argument_list|()
return|;
block|}
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
comment|// process selected object, issue an update query
if|if
condition|(
name|dataRows
operator|==
literal|null
operator|||
name|dataRows
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK : entity not supported: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
if|if
condition|(
name|dataRows
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK : too many rows for entity: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
name|DataRow
name|lastPk
init|=
operator|(
name|DataRow
operator|)
name|dataRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|id
operator|=
operator|(
name|Number
operator|)
name|lastPk
operator|.
name|get
argument_list|(
literal|"NEXT_ID"
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|resultCount
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK for entity '"
operator|+
name|entityName
operator|+
literal|"': update count is wrong - "
operator|+
name|resultCount
argument_list|)
throw|;
block|}
block|}
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
name|keys
parameter_list|,
name|ObjectId
name|idToUpdate
parameter_list|)
block|{
block|}
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
block|}
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK for entity '"
operator|+
name|entityName
operator|+
literal|"'."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating PK for entity: "
operator|+
name|entityName
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

