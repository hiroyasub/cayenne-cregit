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
name|unit
operator|.
name|di
operator|.
name|server
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
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|DbGenerator
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
name|UnitTestDomain
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
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|ashwood
operator|.
name|AshwoodEntitySorter
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
name|cache
operator|.
name|MapQueryCache
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
name|event
operator|.
name|DefaultEventManager
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
name|Procedure
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
name|testdo
operator|.
name|testmap
operator|.
name|StringET1ExtendedType
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
name|AccessStackAdapter
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
name|CayenneResources
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Default implementation of the AccessStack that has a single DataNode per DataMap.  */
end_comment

begin_class
class|class
name|SchemaHelper
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SchemaHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// hardcoded dependent entities that should be excluded
comment|// if LOBs are not supported
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EXTRA_EXCLUDED_FOR_NO_LOB
init|=
operator|new
name|String
index|[]
block|{
literal|"CLOB_DETAIL"
block|}
decl_stmt|;
specifier|protected
name|CayenneResources
name|resources
decl_stmt|;
specifier|protected
name|UnitTestDomain
name|domain
decl_stmt|;
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
specifier|private
name|String
name|adapterClassName
decl_stmt|;
specifier|public
name|SchemaHelper
parameter_list|(
name|DataSource
name|dataSource
parameter_list|,
name|String
name|adapterClassName
parameter_list|,
name|CayenneResources
name|resources
parameter_list|,
name|DataMap
index|[]
name|maps
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|adapterClassName
operator|=
name|adapterClassName
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|domain
operator|=
operator|new
name|UnitTestDomain
argument_list|(
literal|"domain"
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setEventManager
argument_list|(
operator|new
name|DefaultEventManager
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setEntitySorter
argument_list|(
operator|new
name|AshwoodEntitySorter
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setQueryCache
argument_list|(
operator|new
name|MapQueryCache
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DataMap
name|map
range|:
name|maps
control|)
block|{
name|initNode
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|AccessStackAdapter
name|getAdapter
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
return|return
name|resources
operator|.
name|getAccessStackAdapter
argument_list|(
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|initNode
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|AccessStackAdapter
name|adapter
init|=
name|resources
operator|.
name|getAccessStackAdapter
argument_list|(
name|adapterClassName
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
comment|// setup test extended types
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|registerType
argument_list|(
operator|new
name|StringET1ExtendedType
argument_list|()
argument_list|)
expr_stmt|;
comment|// tweak mapping with a delegate
for|for
control|(
name|Procedure
name|proc
range|:
name|map
operator|.
name|getProcedures
argument_list|()
control|)
block|{
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|tweakProcedure
argument_list|(
name|proc
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|addDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|node
operator|.
name|setSchemaUpdateStrategy
argument_list|(
operator|new
name|SkipSchemaUpdateStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|/** Drops all test tables. */
specifier|public
name|void
name|dropSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates all test tables in the database.      */
specifier|public
name|void
name|createSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dropPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates primary key support for all node DbEntities. Will use its facilities      * provided by DbAdapter to generate any necessary database objects and data for      * primary key support.      */
specifier|public
name|void
name|createPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Helper method that orders DbEntities to satisfy referential constraints and returns      * an ordered list.      */
specifier|private
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesInInsertOrder
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
argument_list|)
decl_stmt|;
comment|// filter various unsupported tests...
comment|// LOBs
name|boolean
name|excludeLOB
init|=
operator|!
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|supportsLobs
argument_list|()
decl_stmt|;
name|boolean
name|excludeBinPK
init|=
operator|!
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|supportsBinaryPK
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludeLOB
operator|||
name|excludeBinPK
condition|)
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filtered
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
name|entities
control|)
block|{
comment|// check for LOB attributes
if|if
condition|(
name|excludeLOB
condition|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|EXTRA_EXCLUDED_FOR_NO_LOB
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
operator|>=
literal|0
condition|)
block|{
continue|continue;
block|}
name|boolean
name|hasLob
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BLOB
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|hasLob
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|hasLob
condition|)
block|{
continue|continue;
block|}
block|}
comment|// check for BIN PK
if|if
condition|(
name|excludeBinPK
condition|)
block|{
name|boolean
name|skip
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
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
name|skip
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|skip
condition|)
block|{
continue|continue;
block|}
block|}
name|filtered
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
name|entities
operator|=
name|filtered
expr_stmt|;
block|}
name|domain
operator|.
name|getEntitySorter
argument_list|()
operator|.
name|sortDbEntities
argument_list|(
name|entities
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|entities
return|;
block|}
specifier|private
name|void
name|dropSchema
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|Connection
name|conn
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|list
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
decl_stmt|;
try|try
block|{
name|DatabaseMetaData
name|md
init|=
name|conn
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
literal|"%"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|allTables
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
name|tables
operator|.
name|next
argument_list|()
condition|)
block|{
comment|// 'toUpperCase' is needed since most databases
comment|// are case insensitive, and some will convert names to lower case
comment|// (PostgreSQL)
name|String
name|name
init|=
name|tables
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
name|allTables
operator|.
name|add
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|tables
operator|.
name|close
argument_list|()
expr_stmt|;
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|willDropTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|,
name|allTables
argument_list|)
expr_stmt|;
comment|// drop all tables in the map
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|ListIterator
argument_list|<
name|DbEntity
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|allTables
operator|.
name|contains
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|String
name|dropSql
range|:
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|dropTableStatements
argument_list|(
name|ent
argument_list|)
control|)
block|{
try|try
block|{
name|logger
operator|.
name|info
argument_list|(
name|dropSql
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
name|dropSql
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqe
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't drop table "
operator|+
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|", ignoring..."
argument_list|,
name|sqe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|droppedTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
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
specifier|private
name|void
name|dropPKSupport
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filteredEntities
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|dropAutoPk
argument_list|(
name|node
argument_list|,
name|filteredEntities
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createPKSupport
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filteredEntities
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|filteredEntities
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createSchema
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|Connection
name|conn
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|willCreateTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|query
range|:
name|tableCreateQueries
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|createdTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
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
comment|/**      * Returns iterator of preprocessed table create queries.      */
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|tableCreateQueries
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|DbGenerator
name|gen
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|,
literal|null
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|orderedEnts
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|queries
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// table definitions
for|for
control|(
name|DbEntity
name|ent
range|:
name|orderedEnts
control|)
block|{
name|queries
operator|.
name|add
argument_list|(
name|adapter
operator|.
name|createTable
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// FK constraints
for|for
control|(
name|DbEntity
name|ent
range|:
name|orderedEnts
control|)
block|{
if|if
condition|(
operator|!
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|supportsFKConstraints
argument_list|(
name|ent
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|qs
init|=
name|gen
operator|.
name|createConstraintsQueries
argument_list|(
name|ent
argument_list|)
decl_stmt|;
name|queries
operator|.
name|addAll
argument_list|(
name|qs
argument_list|)
expr_stmt|;
block|}
return|return
name|queries
return|;
block|}
block|}
end_class

end_unit

