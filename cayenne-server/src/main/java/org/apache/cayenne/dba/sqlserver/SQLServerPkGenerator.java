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
name|dba
operator|.
name|sqlserver
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
name|TypesMapping
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
name|List
import|;
end_import

begin_comment
comment|/**  * The default PK generator for MS SQL,  * which uses sequences to generate a PK for an integer key type  * and NEWID() for UNIQUEIDENTIFIER key type  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerPkGenerator
extends|extends
name|OraclePkGenerator
block|{
comment|//MS SQL function for generating GUID
specifier|private
specifier|static
specifier|final
name|String
name|SELECT_NEW_GUID
init|=
literal|"SELECT NEWID()"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SEQUENCE_PREFIX
init|=
literal|"_pk"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_LENGTH_GUID
init|=
literal|36
decl_stmt|;
specifier|public
name|SQLServerPkGenerator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|SQLServerPkGenerator
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
name|String
name|createSequenceString
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
return|return
literal|"CREATE SEQUENCE "
operator|+
name|sequenceName
argument_list|(
name|ent
argument_list|)
operator|+
literal|" AS [bigint] START WITH "
operator|+
name|pkStartValue
operator|+
literal|" INCREMENT BY "
operator|+
name|pkCacheSize
argument_list|(
name|ent
argument_list|)
operator|+
literal|" NO CACHE"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSequencePrefix
parameter_list|()
block|{
return|return
name|SEQUENCE_PREFIX
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|selectNextValQuery
parameter_list|(
name|String
name|sequenceName
parameter_list|)
block|{
return|return
literal|"SELECT NEXT VALUE FOR "
operator|+
name|sequenceName
return|;
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
operator|new
name|ArrayList
argument_list|<>
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
name|dbEntity
range|:
name|dbEntities
control|)
block|{
if|if
condition|(
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
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
name|dbEntity
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
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|pk
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|createSequenceString
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
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
argument_list|<>
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
name|dbEntity
range|:
name|dbEntities
control|)
block|{
if|if
condition|(
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
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
name|dbEntity
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
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|pk
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|dropSequenceString
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
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
name|pk
operator|.
name|getEntity
argument_list|()
decl_stmt|;
comment|//check key on UNIQUEIDENTIFIER; UNIQUEIDENTIFIER is a character with a length of 36
if|if
condition|(
name|TypesMapping
operator|.
name|isCharacter
argument_list|(
name|pk
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|pk
operator|.
name|getMaxLength
argument_list|()
operator|==
name|MAX_LENGTH_GUID
condition|)
block|{
return|return
name|guidPkFromDatabase
argument_list|(
name|node
argument_list|,
name|entity
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|pk
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|String
name|selectAllSequencesQuery
parameter_list|()
block|{
return|return
literal|"SELECT seq.name"
operator|+
literal|" FROM sys.sequences AS seq"
operator|+
literal|" JOIN sys.schemas AS sch"
operator|+
literal|" ON seq.schema_id = sch.schema_id"
return|;
block|}
annotation|@
name|Override
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
name|seqName
init|=
name|entity
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
name|getSequencePrefix
argument_list|()
decl_stmt|;
return|return
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedIdentifier
argument_list|(
name|entity
argument_list|,
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|seqName
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
name|guidPkFromDatabase
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|entity
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
init|)
block|{
try|try
init|(
name|Statement
name|st
init|=
name|con
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|log
argument_list|(
name|SELECT_NEW_GUID
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultSet
name|rs
init|=
name|st
operator|.
name|executeQuery
argument_list|(
name|SELECT_NEW_GUID
argument_list|)
init|)
block|{
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
literal|"Error generating pk for DbEntity %s"
argument_list|,
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
name|getString
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

