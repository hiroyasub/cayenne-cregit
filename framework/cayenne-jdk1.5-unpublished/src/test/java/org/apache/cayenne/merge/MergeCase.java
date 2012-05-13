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
name|merge
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
name|Arrays
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|di
operator|.
name|Inject
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
name|unit
operator|.
name|UnitDbAdapter
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCaseDataSourceFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|MergeCase
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataNode
name|node
decl_stmt|;
specifier|protected
name|DataMap
name|map
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|TABLE_NAMES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"ARTIST"
argument_list|,
literal|"PAINTING"
argument_list|,
literal|"NEW_TABLE"
argument_list|,
literal|"NEW_TABLE2"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this map can't be safely modified in this test, as it is reset by DI container
comment|// on every test
name|map
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
expr_stmt|;
name|filterDataMap
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
name|execute
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DbMerger
name|createMerger
parameter_list|()
block|{
return|return
operator|new
name|DbMerger
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|includeTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
return|return
name|TABLE_NAMES
operator|.
name|contains
argument_list|(
name|tableName
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
specifier|protected
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|()
block|{
return|return
name|createMerger
argument_list|()
operator|.
name|createMergeTokens
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
return|;
block|}
comment|/**      * Remote binary pk {@link DbEntity} for {@link DbAdapter} not supporting that and so      * on.      */
specifier|private
name|void
name|filterDataMap
parameter_list|()
block|{
comment|// copied from AbstractAccessStack.dbEntitiesInInsertOrder
name|boolean
name|excludeBinPK
init|=
name|accessStackAdapter
operator|.
name|supportsBinaryPK
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|excludeBinPK
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entitiesToRemove
init|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|ent
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
for|for
control|(
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// check for BIN PK or FK to BIN Pk
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARBINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|LONGVARBINARY
condition|)
block|{
if|if
condition|(
name|attr
operator|.
name|isPrimaryKey
argument_list|()
operator|||
name|attr
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|entitiesToRemove
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
for|for
control|(
name|DbEntity
name|e
range|:
name|entitiesToRemove
control|)
block|{
name|map
operator|.
name|removeDbEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|execute
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
throws|throws
name|Exception
block|{
name|MergerContext
name|mergerContext
init|=
operator|new
name|ExecutingMergerContext
argument_list|(
name|map
argument_list|,
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|MergerToken
name|tok
range|:
name|tokens
control|)
block|{
name|tok
operator|.
name|execute
argument_list|(
name|mergerContext
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|execute
parameter_list|(
name|MergerToken
name|token
parameter_list|)
throws|throws
name|Exception
block|{
name|MergerContext
name|mergerContext
init|=
operator|new
name|ExecutingMergerContext
argument_list|(
name|map
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|token
operator|.
name|execute
argument_list|(
name|mergerContext
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|executeSql
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|Exception
block|{
name|Connection
name|conn
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
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
name|conn
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|st
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
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
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|assertTokens
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|,
name|int
name|expectedToDb
parameter_list|,
name|int
name|expectedToModel
parameter_list|)
block|{
name|int
name|actualToDb
init|=
literal|0
decl_stmt|;
name|int
name|actualToModel
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
if|if
condition|(
name|token
operator|.
name|getDirection
argument_list|()
operator|.
name|isToDb
argument_list|()
condition|)
block|{
name|actualToDb
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|token
operator|.
name|getDirection
argument_list|()
operator|.
name|isToModel
argument_list|()
condition|)
block|{
name|actualToModel
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"tokens to db"
argument_list|,
name|expectedToDb
argument_list|,
name|actualToDb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tokens to model"
argument_list|,
name|expectedToModel
argument_list|,
name|actualToModel
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertTokensAndExecute
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|int
name|expectedToDb
parameter_list|,
name|int
name|expectedToModel
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
name|assertTokens
argument_list|(
name|tokens
argument_list|,
name|expectedToDb
argument_list|,
name|expectedToModel
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|execute
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|assertTokensAndExecute
parameter_list|(
name|int
name|expectedToDb
parameter_list|,
name|int
name|expectedToModel
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
name|assertTokens
argument_list|(
name|tokens
argument_list|,
name|expectedToDb
argument_list|,
name|expectedToModel
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|execute
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|mergerFactory
argument_list|()
return|;
block|}
specifier|protected
name|void
name|dropTableIfPresent
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|String
name|tableName
parameter_list|)
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
name|AbstractToDbToken
name|t
init|=
operator|(
name|AbstractToDbToken
operator|)
name|mergerFactory
argument_list|()
operator|.
name|createDropTableToDb
argument_list|(
name|entity
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|sql
range|:
name|t
operator|.
name|createSql
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|)
control|)
block|{
name|executeSql
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
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
end_class

end_unit

