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
name|sybase
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
name|dba
operator|.
name|JdbcPkGenerator
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

begin_comment
comment|/**  * Primary key generator implementation for Sybase. Uses a lookup table named  * "AUTO_PK_SUPPORT" and a stored procedure "auto_pk_for_table" to search and increment  * primary keys for tables.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SybasePkGenerator
extends|extends
name|JdbcPkGenerator
block|{
annotation|@
name|Override
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
literal|"  NEXT_ID DECIMAL(19,0) NOT NULL,"
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
comment|/**      * Generates database objects to provide automatic primary key support. Method will      * execute the following SQL statements:      *<p>      * 1. Executed only if a corresponding table does not exist in the database.      *</p>      *       *<pre>      *    CREATE TABLE AUTO_PK_SUPPORT (      *       TABLE_NAME VARCHAR(32) NOT NULL,      *       NEXT_ID DECIMAL(19,0) NOT NULL      *    )      *</pre>      *       *<p>      * 2. Executed under any circumstances.      *</p>      *       *<pre>      * if exists (SELECT * FROM sysobjects WHERE name = 'auto_pk_for_table')      * BEGIN      *    DROP PROCEDURE auto_pk_for_table       * END      *</pre>      *       *<p>      * 3. Executed under any circumstances.      *</p>      * CREATE PROCEDURE auto_pk_for_table      *       * @tname VARCHAR(32),      * @pkbatchsize INT AS BEGIN BEGIN TRANSACTION UPDATE AUTO_PK_SUPPORT set NEXT_ID =      *              NEXT_ID +      * @pkbatchsize WHERE TABLE_NAME =      * @tname SELECT NEXT_ID from AUTO_PK_SUPPORT where NEXT_ID =      * @tname COMMIT END      *       *</pre>      *       * @param node node that provides access to a DataSource.      */
annotation|@
name|Override
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
name|super
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|dbEntities
argument_list|)
expr_stmt|;
name|super
operator|.
name|runUpdate
argument_list|(
name|node
argument_list|,
name|safePkProcDrop
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|runUpdate
argument_list|(
name|node
argument_list|,
name|unsafePkProcCreate
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|createAutoPkStatements
argument_list|(
name|dbEntities
argument_list|)
decl_stmt|;
comment|// add stored procedure drop code
name|list
operator|.
name|add
argument_list|(
name|safePkProcDrop
argument_list|()
argument_list|)
expr_stmt|;
comment|// add stored procedure creation code
name|list
operator|.
name|add
argument_list|(
name|unsafePkProcCreate
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * Drops database objects related to automatic primary key support. Method will      * execute the following SQL statements:      *       *<pre>      * if exists (SELECT * FROM sysobjects WHERE name = 'AUTO_PK_SUPPORT')      * BEGIN      *    DROP TABLE AUTO_PK_SUPPORT      * END      *       *       * if exists (SELECT * FROM sysobjects WHERE name = 'auto_pk_for_table')      * BEGIN      *    DROP PROCEDURE auto_pk_for_table       * END      *</pre>      *       * @param node node that provides access to a DataSource.      */
annotation|@
name|Override
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
name|super
operator|.
name|runUpdate
argument_list|(
name|node
argument_list|,
name|safePkProcDrop
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|runUpdate
argument_list|(
name|node
argument_list|,
name|safePkTableDrop
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|safePkProcDrop
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|safePkTableDrop
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
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
comment|// handle CAY-588 - get connection that is separate from the connection in the
comment|// current transaction.
comment|// TODO (andrus, 7/6/2006) Note that this will still work in a pool with a single
comment|// connection, as PK generator is invoked early in the transaction, before the
comment|// connection is grabbed for commit... So maybe promote this to other adapters in
comment|// 3.0?
name|Transaction
name|transaction
init|=
name|Transaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|Connection
name|connection
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
name|CallableStatement
name|statement
init|=
name|connection
operator|.
name|prepareCall
argument_list|(
literal|"{call auto_pk_for_table(?, ?)}"
argument_list|)
decl_stmt|;
try|try
block|{
name|statement
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|statement
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|super
operator|.
name|getPkCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// can't use "executeQuery"
comment|// per http://jtds.sourceforge.net/faq.html#expectingResultSet
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
return|return
name|rs
operator|.
name|getLong
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating pk for DbEntity "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating pk for DbEntity "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|", no result set from stored procedure."
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @deprecated since 3.0      */
annotation|@
name|Override
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
comment|// handle CAY-588 - get connection that is separate from the connection in the
comment|// current transaction.
comment|// TODO (andrus, 7/6/2006) Note that this will still work in a pool with a single
comment|// connection, as PK generator is invoked early in the transaction, before the
comment|// connection is grabbed for commit... So maybe promote this to other adapters in
comment|// 3.0?
name|Transaction
name|transaction
init|=
name|Transaction
operator|.
name|getThreadTransaction
argument_list|()
decl_stmt|;
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|Connection
name|connection
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
name|CallableStatement
name|statement
init|=
name|connection
operator|.
name|prepareCall
argument_list|(
literal|"{call auto_pk_for_table(?, ?)}"
argument_list|)
decl_stmt|;
try|try
block|{
name|statement
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|statement
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|super
operator|.
name|getPkCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// can't use "executeQuery"
comment|// per http://jtds.sourceforge.net/faq.html#expectingResultSet
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating pk for DbEntity "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error generating pk for DbEntity "
operator|+
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|", no result set from stored procedure."
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|safePkTableDrop
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
literal|"if exists (SELECT * FROM sysobjects WHERE name = 'AUTO_PK_SUPPORT')"
argument_list|)
operator|.
name|append
argument_list|(
literal|" BEGIN "
argument_list|)
operator|.
name|append
argument_list|(
literal|" DROP TABLE AUTO_PK_SUPPORT"
argument_list|)
operator|.
name|append
argument_list|(
literal|" END"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|unsafePkProcCreate
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
literal|" CREATE PROCEDURE auto_pk_for_table @tname VARCHAR(32), @pkbatchsize INT AS"
argument_list|)
operator|.
name|append
argument_list|(
literal|" BEGIN"
argument_list|)
operator|.
name|append
argument_list|(
literal|" BEGIN TRANSACTION"
argument_list|)
operator|.
name|append
argument_list|(
literal|" UPDATE AUTO_PK_SUPPORT set NEXT_ID = NEXT_ID + @pkbatchsize"
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE TABLE_NAME = @tname"
argument_list|)
operator|.
name|append
argument_list|(
literal|" SELECT NEXT_ID FROM AUTO_PK_SUPPORT WHERE TABLE_NAME = @tname"
argument_list|)
operator|.
name|append
argument_list|(
literal|" COMMIT"
argument_list|)
operator|.
name|append
argument_list|(
literal|" END"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|safePkProcDrop
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
literal|"if exists (SELECT * FROM sysobjects WHERE name = 'auto_pk_for_table')"
argument_list|)
operator|.
name|append
argument_list|(
literal|" BEGIN"
argument_list|)
operator|.
name|append
argument_list|(
literal|" DROP PROCEDURE auto_pk_for_table"
argument_list|)
operator|.
name|append
argument_list|(
literal|" END"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

