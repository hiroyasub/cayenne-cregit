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
name|ingres
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
name|oracle
operator|.
name|OraclePkGenerator
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
comment|/**  * Ingres-specific sequence based PK generator.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|IngresPkGenerator
extends|extends
name|OraclePkGenerator
block|{
comment|/**      * @deprecated since 3.0      */
specifier|protected
name|IngresPkGenerator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|IngresPkGenerator
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
literal|".nextval"
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
comment|/**      * Generates primary key by calling Oracle sequence corresponding to the      *<code>dbEntity</code>. Executed SQL looks like this:      *       *<pre>      *     SELECT nextval(pk_table_name)      *</pre>      *       * @deprecated since 3.0      */
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
literal|".nextval"
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
annotation|@
name|Override
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
name|Statement
name|select
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|sql
init|=
literal|"select seq_name from iisequences where seq_owner != 'DBA'"
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
name|select
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
name|String
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|sequenceList
operator|.
name|add
argument_list|(
name|name
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|select
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
block|}
end_class

end_unit

