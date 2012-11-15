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
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * A sequence-based PK generator used by {@link DB2Adapter}.  */
end_comment

begin_class
specifier|public
class|class
name|DB2PkGenerator
extends|extends
name|JdbcPkGenerator
block|{
name|DB2PkGenerator
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
specifier|private
specifier|static
specifier|final
name|String
name|_SEQUENCE_PREFIX
init|=
literal|"S_"
decl_stmt|;
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
name|String
name|pkGeneratingSequenceName
init|=
name|sequenceName
argument_list|(
name|entity
argument_list|)
decl_stmt|;
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
name|String
name|sql
init|=
literal|"SELECT NEXTVAL FOR "
operator|+
name|pkGeneratingSequenceName
operator|+
literal|" FROM SYSIBM.SYSDUMMY1"
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
name|Collection
argument_list|<
name|String
argument_list|>
name|sequences
init|=
name|getExistingSequences
argument_list|(
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|dbEntities
control|)
block|{
if|if
condition|(
operator|!
name|sequences
operator|.
name|contains
argument_list|(
name|sequenceName
argument_list|(
name|entity
argument_list|)
argument_list|)
condition|)
block|{
name|this
operator|.
name|runUpdate
argument_list|(
name|node
argument_list|,
name|createSequenceString
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates a list of CREATE SEQUENCE statements for the list of DbEntities.      */
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
argument_list|)
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|dbEntities
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|createSequenceString
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
comment|/**      * Drops PK sequences for all specified DbEntities.      */
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
name|Collection
argument_list|<
name|String
argument_list|>
name|sequences
init|=
name|getExistingSequences
argument_list|(
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|DbEntity
name|ent
range|:
name|dbEntities
control|)
block|{
name|String
name|name
decl_stmt|;
if|if
condition|(
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
condition|)
block|{
name|DbEntity
name|tempEnt
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|DataMap
name|dm
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dm
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tempEnt
operator|.
name|setDataMap
argument_list|(
name|dm
argument_list|)
expr_stmt|;
name|tempEnt
operator|.
name|setName
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
name|sequenceName
argument_list|(
name|tempEnt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|sequenceName
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sequences
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|dropSequenceString
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates a list of DROP SEQUENCE statements for the list of DbEntities.      */
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
argument_list|(
name|dbEntities
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|dbEntities
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|dropSequenceString
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
comment|/**      * Fetches a list of existing sequences that might match Cayenne generated      * ones.      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getExistingSequences
parameter_list|(
name|DataNode
name|node
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// check existing sequences
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
name|sel
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
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
literal|"SELECT SEQNAME FROM SYSCAT.SEQUENCES "
argument_list|)
operator|.
name|append
argument_list|(
literal|"WHERE SEQNAME LIKE '"
argument_list|)
operator|.
name|append
argument_list|(
name|_SEQUENCE_PREFIX
argument_list|)
operator|.
name|append
argument_list|(
literal|"%'"
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|buffer
operator|.
name|toString
argument_list|()
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
name|ResultSet
name|rs
init|=
name|sel
operator|.
name|executeQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|sequenceList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|sequenceList
operator|.
name|add
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sequenceList
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
name|sel
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
comment|/**      * Returns default sequence name for DbEntity.      */
specifier|protected
name|String
name|sequenceName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|boolean
name|status
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
condition|)
block|{
name|status
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|status
operator|=
literal|false
expr_stmt|;
block|}
name|QuotingStrategy
name|context
init|=
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|String
name|entName
init|=
name|entity
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|seqName
init|=
name|_SEQUENCE_PREFIX
operator|+
name|entName
decl_stmt|;
return|return
name|context
operator|.
name|quotedIdentifier
argument_list|(
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|seqName
argument_list|)
return|;
block|}
comment|/**      * Returns DROP SEQUENCE statement.      */
specifier|protected
name|String
name|dropSequenceString
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
literal|"DROP SEQUENCE "
operator|+
name|sequenceName
argument_list|(
name|entity
argument_list|)
operator|+
literal|" RESTRICT "
return|;
block|}
comment|/**      * Returns CREATE SEQUENCE statement for entity.      */
specifier|protected
name|String
name|createSequenceString
parameter_list|(
name|DbEntity
name|entity
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
literal|"CREATE SEQUENCE "
argument_list|)
operator|.
name|append
argument_list|(
name|sequenceName
argument_list|(
name|entity
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" START WITH 200"
argument_list|)
operator|.
name|append
argument_list|(
literal|" INCREMENT BY "
argument_list|)
operator|.
name|append
argument_list|(
name|getPkCacheSize
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" NO MAXVALUE "
argument_list|)
operator|.
name|append
argument_list|(
literal|" NO CYCLE "
argument_list|)
operator|.
name|append
argument_list|(
literal|" CACHE "
argument_list|)
operator|.
name|append
argument_list|(
name|getPkCacheSize
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
block|}
end_class

end_unit

