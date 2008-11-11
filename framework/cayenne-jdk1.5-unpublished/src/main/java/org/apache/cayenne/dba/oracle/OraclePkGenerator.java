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

begin_comment
comment|/**  * Sequence-based primary key generator implementation for Oracle. Uses Oracle sequences  * to generate primary key values. This approach is at least 50% faster when tested with  * Oracle compared to the lookup table approach.  *<p>  * When using Cayenne key caching mechanism, make sure that sequences in the database have  * "INCREMENT BY" greater or equal to OraclePkGenerator "pkCacheSize" property value. If  * this is not the case, you will need to adjust PkGenerator value accordingly. For  * example when sequence is incremented by 1 each time, use the following code:  *</p>  *   *<pre>  * dataNode.getAdapter().getPkGenerator().setPkCacheSize(1);  *</pre>  *   */
end_comment

begin_class
specifier|public
class|class
name|OraclePkGenerator
extends|extends
name|JdbcPkGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|_SEQUENCE_PREFIX
init|=
literal|"pk_"
decl_stmt|;
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
name|List
name|sequences
init|=
name|getExistingSequences
argument_list|(
name|node
argument_list|)
decl_stmt|;
comment|// create needed sequences
name|Iterator
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
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sequences
operator|.
name|contains
argument_list|(
name|sequenceName
argument_list|(
name|ent
argument_list|)
argument_list|)
condition|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|createSequenceString
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
argument_list|()
decl_stmt|;
name|Iterator
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
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|createSequenceString
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
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
name|List
name|sequences
init|=
name|getExistingSequences
argument_list|(
name|node
argument_list|)
decl_stmt|;
comment|// drop obsolete sequences
name|Iterator
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
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|sequences
operator|.
name|contains
argument_list|(
name|stripSchemaName
argument_list|(
name|sequenceName
argument_list|(
name|ent
argument_list|)
argument_list|)
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
name|Iterator
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
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|dropSequenceString
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|protected
name|String
name|createSequenceString
parameter_list|(
name|DbEntity
name|ent
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
name|ent
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
name|pkCacheSize
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a SQL string needed to drop any database objects associated with automatic      * primary key generation process for a specific DbEntity.      */
specifier|protected
name|String
name|dropSequenceString
parameter_list|(
name|DbEntity
name|ent
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
literal|"DROP SEQUENCE "
argument_list|)
operator|.
name|append
argument_list|(
name|sequenceName
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Generates primary key by calling Oracle sequence corresponding to the      *<code>dbEntity</code>. Executed SQL looks like this:      *       *<pre>      *   SELECT pk_table_name.nextval FROM DUAL      *</pre>      *       * @since 3.0      */
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
name|DbKeyGenerator
name|pkGenerator
init|=
name|entity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
name|String
name|pkGeneratingSequenceName
decl_stmt|;
if|if
condition|(
name|pkGenerator
operator|!=
literal|null
operator|&&
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
operator|.
name|equals
argument_list|(
name|pkGenerator
operator|.
name|getGeneratorType
argument_list|()
argument_list|)
operator|&&
name|pkGenerator
operator|.
name|getGeneratorName
argument_list|()
operator|!=
literal|null
condition|)
name|pkGeneratingSequenceName
operator|=
name|pkGenerator
operator|.
name|getGeneratorName
argument_list|()
expr_stmt|;
else|else
name|pkGeneratingSequenceName
operator|=
name|sequenceName
argument_list|(
name|entity
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
name|String
name|sql
init|=
literal|"SELECT "
operator|+
name|pkGeneratingSequenceName
operator|+
literal|".nextval FROM DUAL"
decl_stmt|;
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
comment|/**      * Generates primary key by calling Oracle sequence corresponding to the      *<code>dbEntity</code>. Executed SQL looks like this:      *       *<pre>      *   SELECT pk_table_name.nextval FROM DUAL      *</pre>      *       * @deprecated since 3.0      */
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
name|DbKeyGenerator
name|pkGenerator
init|=
name|ent
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
name|String
name|pkGeneratingSequenceName
decl_stmt|;
if|if
condition|(
name|pkGenerator
operator|!=
literal|null
operator|&&
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
operator|.
name|equals
argument_list|(
name|pkGenerator
operator|.
name|getGeneratorType
argument_list|()
argument_list|)
operator|&&
name|pkGenerator
operator|.
name|getGeneratorName
argument_list|()
operator|!=
literal|null
condition|)
name|pkGeneratingSequenceName
operator|=
name|pkGenerator
operator|.
name|getGeneratorName
argument_list|()
expr_stmt|;
else|else
name|pkGeneratingSequenceName
operator|=
name|sequenceName
argument_list|(
name|ent
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
name|String
name|sql
init|=
literal|"SELECT "
operator|+
name|pkGeneratingSequenceName
operator|+
literal|".nextval FROM DUAL"
decl_stmt|;
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
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|rs
operator|.
name|getInt
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
specifier|protected
name|int
name|pkCacheSize
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
comment|// use custom generator if possible
name|DbKeyGenerator
name|keyGenerator
init|=
name|entity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyGenerator
operator|!=
literal|null
operator|&&
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
operator|.
name|equals
argument_list|(
name|keyGenerator
operator|.
name|getGeneratorType
argument_list|()
argument_list|)
operator|&&
name|keyGenerator
operator|.
name|getGeneratorName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Integer
name|size
init|=
name|keyGenerator
operator|.
name|getKeyCacheSize
argument_list|()
decl_stmt|;
return|return
operator|(
name|size
operator|!=
literal|null
operator|&&
name|size
operator|.
name|intValue
argument_list|()
operator|>=
literal|1
operator|)
condition|?
name|size
operator|.
name|intValue
argument_list|()
else|:
name|super
operator|.
name|getPkCacheSize
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getPkCacheSize
argument_list|()
return|;
block|}
block|}
comment|/** Returns expected primary key sequence name for a DbEntity. */
specifier|protected
name|String
name|sequenceName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
comment|// use custom generator if possible
name|DbKeyGenerator
name|keyGenerator
init|=
name|entity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyGenerator
operator|!=
literal|null
operator|&&
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
operator|.
name|equals
argument_list|(
name|keyGenerator
operator|.
name|getGeneratorType
argument_list|()
argument_list|)
operator|&&
name|keyGenerator
operator|.
name|getGeneratorName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|keyGenerator
operator|.
name|getGeneratorName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
else|else
block|{
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
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getSchema
argument_list|()
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getSchema
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|seqName
operator|=
name|entity
operator|.
name|getSchema
argument_list|()
operator|+
literal|"."
operator|+
name|seqName
expr_stmt|;
block|}
return|return
name|seqName
return|;
block|}
block|}
specifier|protected
name|String
name|stripSchemaName
parameter_list|(
name|String
name|sequenceName
parameter_list|)
block|{
name|int
name|ind
init|=
name|sequenceName
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
operator|(
name|ind
operator|>=
literal|0
operator|)
condition|?
name|sequenceName
operator|.
name|substring
argument_list|(
name|ind
operator|+
literal|1
argument_list|)
else|:
name|sequenceName
return|;
block|}
comment|/**      * Fetches a list of existing sequences that might match Cayenne generated ones.      */
specifier|protected
name|List
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
name|String
name|sql
init|=
literal|"SELECT LOWER(SEQUENCE_NAME) FROM ALL_SEQUENCES"
decl_stmt|;
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
block|}
end_class

end_unit

