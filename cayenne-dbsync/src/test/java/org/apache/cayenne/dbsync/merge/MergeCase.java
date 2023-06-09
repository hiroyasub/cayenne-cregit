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
name|dbsync
operator|.
name|merge
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
name|dbsync
operator|.
name|DbSyncModule
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergerContext
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactoryProvider
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|db
operator|.
name|AbstractToDbToken
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|db
operator|.
name|SetColumnTypeToDb
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
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|dbsync
operator|.
name|naming
operator|.
name|NoStemStemmer
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DbLoader
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DbLoaderConfiguration
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|LoggingDbLoaderDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|FiltersConfig
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|PatternFilter
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|TableFilter
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|CayenneProjects
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
name|ExtraModules
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
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|List
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
annotation|@
name|ExtraModules
argument_list|(
name|DbSyncModule
operator|.
name|class
argument_list|)
specifier|public
specifier|abstract
class|class
name|MergeCase
extends|extends
name|ServerCase
block|{
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
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MergeCase
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|protected
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
name|Override
specifier|public
name|void
name|cleanUpDB
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|update
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|set
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|super
operator|.
name|cleanUpDB
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this map can't be safely modified in this test, as it is reset by DI
comment|// container
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
name|DataMapMerger
operator|.
name|Builder
name|merger
parameter_list|()
block|{
return|return
name|DataMapMerger
operator|.
name|builder
argument_list|(
name|mergerFactory
argument_list|()
argument_list|)
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
name|createMergeTokens
argument_list|(
literal|"ARTIST|GALLERY|PAINTING|NEW_TABLE2?"
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|String
name|tableFilterInclude
parameter_list|)
block|{
name|FiltersConfig
name|filters
init|=
name|FiltersConfig
operator|.
name|create
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|TableFilter
operator|.
name|include
argument_list|(
name|tableFilterInclude
argument_list|)
argument_list|,
name|PatternFilter
operator|.
name|INCLUDE_NOTHING
argument_list|)
decl_stmt|;
name|DbLoaderConfiguration
name|loaderConfiguration
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|loaderConfiguration
operator|.
name|setFiltersConfig
argument_list|(
name|filters
argument_list|)
expr_stmt|;
name|DataMap
name|dbImport
decl_stmt|;
try|try
init|(
name|Connection
name|conn
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
name|dbImport
operator|=
operator|new
name|DbLoader
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|conn
argument_list|,
name|loaderConfiguration
argument_list|,
operator|new
name|LoggingDbLoaderDelegate
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DbLoader
operator|.
name|class
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't doLoad dataMap from db."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|merger
argument_list|()
operator|.
name|filters
argument_list|(
name|filters
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|createMergeTokens
argument_list|(
name|map
argument_list|,
name|dbImport
argument_list|)
decl_stmt|;
return|return
name|filter
argument_list|(
name|tokens
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|filter
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
return|return
name|filterEmptyTypeChange
argument_list|(
name|filterEmpty
argument_list|(
name|tokens
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Filter out tokens for db attribute type change when types is same for specific DB      */
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|filterEmptyTypeChange
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokensOut
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
operator|!
operator|(
name|token
operator|instanceof
name|SetColumnTypeToDb
operator|)
condition|)
block|{
name|tokensOut
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|SetColumnTypeToDb
name|setColumnToDb
init|=
operator|(
name|SetColumnTypeToDb
operator|)
name|token
decl_stmt|;
name|int
name|toType
init|=
name|setColumnToDb
operator|.
name|getColumnNew
argument_list|()
operator|.
name|getType
argument_list|()
decl_stmt|;
name|int
name|fromType
init|=
name|setColumnToDb
operator|.
name|getColumnOriginal
argument_list|()
operator|.
name|getType
argument_list|()
decl_stmt|;
comment|// filter out conversions between date/time types
if|if
condition|(
name|accessStackAdapter
operator|.
name|onlyGenericDateType
argument_list|()
condition|)
block|{
if|if
condition|(
name|isDateTimeType
argument_list|(
name|toType
argument_list|)
operator|&&
name|isDateTimeType
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
comment|// filter out conversions between numeric types
if|if
condition|(
name|accessStackAdapter
operator|.
name|onlyGenericNumberType
argument_list|()
condition|)
block|{
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|toType
argument_list|)
operator|&&
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
name|tokensOut
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
return|return
name|tokensOut
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isDateTimeType
parameter_list|(
name|int
name|type
parameter_list|)
block|{
return|return
name|type
operator|==
name|Types
operator|.
name|DATE
operator|||
name|type
operator|==
name|Types
operator|.
name|TIME
operator|||
name|type
operator|==
name|Types
operator|.
name|TIMESTAMP
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|filterEmpty
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokensOut
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
operator|!
name|token
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|tokensOut
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokensOut
return|;
block|}
comment|/**      * Remote binary pk {@link DbEntity} for {@link DbAdapter} not supporting      * that and so on.      */
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
block|{
name|MergerContext
name|mergerContext
init|=
name|MergerContext
operator|.
name|builder
argument_list|(
name|map
argument_list|)
operator|.
name|dataNode
argument_list|(
name|node
argument_list|)
operator|.
name|build
argument_list|()
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
name|MergerContext
operator|.
name|builder
argument_list|(
name|map
argument_list|)
operator|.
name|dataNode
argument_list|(
name|node
argument_list|)
operator|.
name|build
argument_list|()
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
try|try
init|(
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
init|;
init|)
block|{
try|try
init|(
name|Statement
name|st
init|=
name|conn
operator|.
name|createStatement
argument_list|()
init|;
init|)
block|{
name|st
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
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
name|execute
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|MergerTokenFactory
name|mergerFactory
parameter_list|()
block|{
return|return
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|MergerTokenFactoryProvider
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|dropTableIfPresent
parameter_list|(
name|String
name|tableName
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must have a dummy datamap for the dummy table for the downstream code
comment|// to work
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"dummy"
argument_list|)
decl_stmt|;
name|map
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
name|map
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
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
try|try
block|{
name|executeSql
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Exception dropping table "
operator|+
name|tableName
operator|+
literal|", probably abscent.."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

