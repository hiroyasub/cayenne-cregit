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
name|List
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
comment|/**  * Default primary key generator implementation. Uses a lookup table named  * "AUTO_PK_SUPPORT" to search and increment primary keys for tables.  *   * @author Andrus Adamchik  */
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
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|LongPkRange
argument_list|>
name|pkCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|LongPkRange
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|int
name|pkCacheSize
init|=
name|DEFAULT_PK_CACHE_SIZE
decl_stmt|;
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
comment|/**      * Drops table named "AUTO_PK_SUPPORT" if it exists in the database.      */
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
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
literal|"  NEXT_ID INTEGER NOT NULL,"
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
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
literal|"', 200)"
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
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
comment|/**      * Checks if AUTO_PK_TABLE already exists in the database.      */
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
decl_stmt|;
name|boolean
name|exists
init|=
literal|false
decl_stmt|;
try|try
block|{
name|DatabaseMetaData
name|md
init|=
name|con
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
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
decl_stmt|;
try|try
block|{
name|exists
operator|=
name|tables
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|tables
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// return connection to the pool
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|exists
return|;
block|}
comment|/**      * Runs JDBC update over a Connection obtained from DataNode. Returns a number of      * objects returned from update.      *       * @throws SQLException in case of query failure.      */
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
name|QueryLogger
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
decl_stmt|;
try|try
block|{
name|Statement
name|upd
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
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
finally|finally
block|{
name|upd
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Generates a unique and non-repeating primary key for specified dbEntity.      *<p>      * This implementation is naive since it does not lock the database rows when      * executing select and subsequent update. Adapter-specific implementations are more      * robust.      *</p>      *       * @since 3.0      */
specifier|public
name|Object
name|generatePkForDbEntity
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
name|long
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
synchronized|synchronized
init|(
name|pkCache
init|)
block|{
name|LongPkRange
name|r
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
name|r
operator|==
literal|null
condition|)
block|{
comment|// created exhausted LongPkRange
name|r
operator|=
operator|new
name|LongPkRange
argument_list|(
literal|1l
argument_list|,
literal|0l
argument_list|)
expr_stmt|;
name|pkCache
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|isExhausted
argument_list|()
condition|)
block|{
name|long
name|val
init|=
name|longPkFromDatabase
argument_list|(
name|node
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|r
operator|.
name|reset
argument_list|(
name|val
argument_list|,
name|val
operator|+
name|cacheSize
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|value
operator|=
name|r
operator|.
name|getNextPrimaryKey
argument_list|()
expr_stmt|;
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
name|Long
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
comment|// leaving it up to the user to ensure that PK does not exceed max int...
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|int
operator|)
name|value
argument_list|)
return|;
block|}
block|}
comment|/**      * @deprecated since 3.0      */
specifier|public
name|Object
name|generatePkForDbEntity
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|Exception
block|{
comment|// check for binary pk
name|Object
name|binPK
init|=
name|binaryPK
argument_list|(
name|ent
argument_list|)
decl_stmt|;
if|if
condition|(
name|binPK
operator|!=
literal|null
condition|)
block|{
return|return
name|binPK
return|;
block|}
name|DbKeyGenerator
name|pkGenerator
init|=
name|ent
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
name|int
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
comment|// if no caching, always generate fresh
if|if
condition|(
name|cacheSize
operator|<=
literal|1
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|pkFromDatabase
argument_list|(
name|node
argument_list|,
name|ent
argument_list|)
argument_list|)
return|;
block|}
synchronized|synchronized
init|(
name|pkCache
init|)
block|{
name|LongPkRange
name|r
init|=
name|pkCache
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
comment|// created exhausted PkRange
name|r
operator|=
operator|new
name|LongPkRange
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|pkCache
operator|.
name|put
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|isExhausted
argument_list|()
condition|)
block|{
name|int
name|val
init|=
name|pkFromDatabase
argument_list|(
name|node
argument_list|,
name|ent
argument_list|)
decl_stmt|;
name|r
operator|.
name|reset
argument_list|(
name|val
argument_list|,
name|val
operator|+
name|cacheSize
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|getNextPrimaryKey
argument_list|()
return|;
block|}
block|}
comment|/**      * @return a binary PK if DbEntity has a BINARY or VARBINARY pk, null otherwise. This      *         method will likely be deprecated in 1.1 in favor of a more generic      *         solution.      * @since 1.0.2      * @deprecated since 3.0      */
specifier|protected
name|byte
index|[]
name|binaryPK
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pkColumns
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkColumns
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbAttribute
name|pk
init|=
name|pkColumns
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
operator|.
name|getMaxLength
argument_list|()
operator|>
literal|0
operator|&&
operator|(
name|pk
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BINARY
operator|||
name|pk
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARBINARY
operator|)
condition|)
block|{
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
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Performs primary key generation ignoring cache. Generates a range of primary keys      * as specified by "pkCacheSize" bean property.      *<p>      * This method is called internally from "generatePkForDbEntity" and then generated      * range of key values is saved in cache for performance. Subclasses that implement      * different primary key generation solutions should override this method, not      * "generatePkForDbEntity".      *</p>      *       * @deprecated since 3.0 {@link #longPkFromDatabase(DataNode, DbEntity)} is used.      */
specifier|protected
name|int
name|pkFromDatabase
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|select
init|=
literal|"SELECT #result('NEXT_ID' 'int' 'NEXT_ID') "
operator|+
literal|"FROM AUTO_PK_SUPPORT "
operator|+
literal|"WHERE TABLE_NAME = '"
operator|+
name|ent
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
name|ent
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
name|ent
argument_list|,
name|pkUpdateString
argument_list|(
name|ent
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
name|ent
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
comment|/**      * Performs primary key generation ignoring cache. Generates a range of primary keys      * as specified by "pkCacheSize" bean property.      *<p>      * This method is called internally from "generatePkForDbEntity" and then generated      * range of key values is saved in cache for performance. Subclasses that implement      * different primary key generation solutions should override this method, not      * "generatePkForDbEntity".      *</p>      *       * @since 3.0      */
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
comment|/**      * Returns a size of the entity primary key cache. Default value is 20. If cache size      * is set to a value less or equals than "one", no primary key caching is done.      */
specifier|public
name|int
name|getPkCacheSize
parameter_list|()
block|{
return|return
name|pkCacheSize
return|;
block|}
comment|/**      * Sets the size of the entity primary key cache. If<code>pkCacheSize</code>      * parameter is less than 1, cache size is set to "one".      *<p>      *<i>Note that our tests show that setting primary key cache value to anything much      * bigger than 20 does not give any significant performance increase. Therefore it      * does not make sense to use bigger values, since this may potentially create big      * gaps in the database primary key sequences in cases like application crashes or      * restarts.</i>      *</p>      */
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
comment|/**      * OperationObserver for primary key retrieval.      */
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
name|int
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
name|intValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|DataRow
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
specifier|public
name|void
name|nextGeneratedDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
block|}
specifier|public
name|void
name|nextDataRows
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

