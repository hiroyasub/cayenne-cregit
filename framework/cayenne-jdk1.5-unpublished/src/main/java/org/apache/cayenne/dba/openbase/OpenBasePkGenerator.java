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
name|openbase
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
name|ResultSet
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
name|Iterator
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
name|dba
operator|.
name|QuotingStrategy
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
name|util
operator|.
name|IDUtil
import|;
end_import

begin_comment
comment|/**  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|OpenBasePkGenerator
extends|extends
name|JdbcPkGenerator
block|{
specifier|protected
name|OpenBasePkGenerator
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a non-repeating primary key for a given PK attribute. Since      * OpenBase-specific mechanism is used, key caching is disabled. Instead a database      * operation is performed on every call.      *       * @since 3.0      */
annotation|@
name|Override
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
name|long
name|value
init|=
name|longPkFromDatabase
argument_list|(
name|node
argument_list|,
name|entity
argument_list|)
decl_stmt|;
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
comment|/**      * Generates new (unique and non-repeating) primary key for specified DbEntity.      * Executed SQL looks like this:      *       *<pre>      *  NEWID FOR Table Column      *</pre>      *       * COLUMN must be marked as UNIQUE in order for this to work properly.      *       * @since 3.0      */
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
name|String
name|sql
init|=
name|newIDString
argument_list|(
name|entity
argument_list|)
decl_stmt|;
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
name|st
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|ResultSet
name|rs
init|=
name|st
operator|.
name|executeQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Object pk = null;
if|if
condition|(
operator|!
name|rs
operator|.
name|next
argument_list|()
condition|)
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
return|return
name|rs
operator|.
name|getLong
argument_list|(
literal|1
argument_list|)
return|;
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
finally|finally
block|{
name|st
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
comment|/**      * Returns SQL string that can generate new (unique and non-repeating) primary key for      * specified DbEntity. No actual database operations are performed.      *       * @since 1.2      */
specifier|protected
name|String
name|newIDString
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
if|if
condition|(
operator|(
literal|null
operator|==
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
operator|)
operator|||
operator|(
literal|1
operator|!=
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|)
condition|)
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
literal|": pk must be single attribute"
argument_list|)
throw|;
block|}
name|DbAttribute
name|primaryKeyAttribute
init|=
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"NEWID FOR "
argument_list|)
decl_stmt|;
name|buf
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
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|primaryKeyAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
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
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
comment|// looks like generating a PK on top of an existing one does not
comment|// result in errors...
comment|// create needed sequences
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|dbEntities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// the caller must take care of giving us the right entities
comment|// but lets check anyway
if|if
condition|(
operator|!
name|canCreatePK
argument_list|(
name|entity
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|runUpdate
argument_list|(
name|node
argument_list|,
name|createPKString
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|runUpdate
argument_list|(
name|node
argument_list|,
name|createUniquePKIndexString
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *       */
annotation|@
name|Override
specifier|public
name|List
name|createAutoPkStatements
parameter_list|(
name|List
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
literal|2
operator|*
name|dbEntities
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|dbEntities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// the caller must take care of giving us the right entities
comment|// but lets check anyway
if|if
condition|(
operator|!
name|canCreatePK
argument_list|(
name|entity
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|list
operator|.
name|add
argument_list|(
name|createPKString
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|createUniquePKIndexString
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|protected
name|boolean
name|canCreatePK
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      *       */
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
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
comment|// there is no simple way to do that... probably requires
comment|// editing metadata tables...
comment|// Good thing is that it doesn't matter, since PK support
comment|// is attached to the table itself, so if a table is dropped,
comment|// it will be dropped as well
block|}
comment|/**      * Returns an empty list, since OpenBase doesn't support this operation.      */
annotation|@
name|Override
specifier|public
name|List
name|dropAutoPkStatements
parameter_list|(
name|List
name|dbEntities
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
comment|/**      * Returns a String to create PK support for an entity.      */
specifier|protected
name|String
name|createPKString
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
operator|==
literal|null
operator|||
name|pk
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
literal|"Entity '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"' has no PK defined."
argument_list|)
throw|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"CREATE PRIMARY KEY "
argument_list|)
expr_stmt|;
name|QuotingStrategy
name|context
init|=
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedIdentifier
argument_list|(
name|entity
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|pk
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// at this point we know that there is at least on PK column
name|DbAttribute
name|firstColumn
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|firstColumn
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|column
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|column
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a String to create a unique index on table primary key columns per OpenBase      * recommendations.      */
specifier|protected
name|String
name|createUniquePKIndexString
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
operator|==
literal|null
operator|||
name|pk
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
literal|"Entity '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"' has no PK defined."
argument_list|)
throw|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// compound PK doesn't work well with UNIQUE index...
comment|// create a regular one in this case
name|buffer
operator|.
name|append
argument_list|(
name|pk
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
literal|"CREATE UNIQUE INDEX "
else|:
literal|"CREATE INDEX "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedIdentifier
argument_list|(
name|entity
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|pk
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// at this point we know that there is at least on PK column
name|DbAttribute
name|firstColumn
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|firstColumn
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|column
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|column
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|// noop
block|}
comment|/**      * Returns zero, since PK caching is not feasible with OpenBase PK generation      * mechanism.      */
annotation|@
name|Override
specifier|public
name|int
name|getPkCacheSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPkCacheSize
parameter_list|(
name|int
name|pkCacheSize
parameter_list|)
block|{
comment|// noop, no PK caching
block|}
block|}
end_class

end_unit

