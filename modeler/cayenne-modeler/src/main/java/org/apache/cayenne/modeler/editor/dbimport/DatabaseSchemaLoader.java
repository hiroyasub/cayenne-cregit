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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|Comparator
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|dbimport
operator|.
name|FilterContainer
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
name|dbimport
operator|.
name|IncludeColumn
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
name|dbimport
operator|.
name|IncludeProcedure
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
name|dbimport
operator|.
name|IncludeTable
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
name|dbimport
operator|.
name|PatternParam
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
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbimport
operator|.
name|Schema
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
name|dbimport
operator|.
name|SchemaContainer
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
name|modeler
operator|.
name|ClassLoadingService
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
name|modeler
operator|.
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|modeler
operator|.
name|pref
operator|.
name|DBConnectionInfo
import|;
end_import

begin_class
specifier|public
class|class
name|DatabaseSchemaLoader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_ALL_PATTERN
init|=
literal|"%"
decl_stmt|;
specifier|private
specifier|final
name|ReverseEngineering
name|databaseReverseEngineering
decl_stmt|;
specifier|public
name|DatabaseSchemaLoader
parameter_list|()
block|{
name|databaseReverseEngineering
operator|=
operator|new
name|ReverseEngineering
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ReverseEngineering
name|load
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|ClassLoadingService
name|loadingService
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapter
name|dbAdapter
init|=
name|connectionInfo
operator|.
name|makeAdapter
argument_list|(
name|loadingService
argument_list|)
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|loadingService
argument_list|)
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|processCatalogs
argument_list|(
name|connection
argument_list|,
name|dbAdapter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|databaseReverseEngineering
operator|.
name|getSchemas
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|databaseReverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|loadTables
argument_list|(
name|connectionInfo
argument_list|,
name|loadingService
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|sort
argument_list|()
expr_stmt|;
return|return
name|databaseReverseEngineering
return|;
block|}
specifier|private
name|void
name|sort
parameter_list|()
block|{
name|databaseReverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|forEach
argument_list|(
name|catalog
lambda|->
block|{
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|sort
argument_list|)
expr_stmt|;
name|sort
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|sort
argument_list|(
name|databaseReverseEngineering
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|sort
parameter_list|(
name|FilterContainer
name|filterContainer
parameter_list|)
block|{
name|Comparator
argument_list|<
name|PatternParam
argument_list|>
name|comparator
init|=
name|Comparator
operator|.
name|comparing
argument_list|(
name|PatternParam
operator|::
name|getPattern
argument_list|)
decl_stmt|;
name|filterContainer
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|sort
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|filterContainer
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|forEach
argument_list|(
name|table
lambda|->
name|table
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|sort
argument_list|(
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
name|filterContainer
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|sort
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processCatalogs
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|DbAdapter
name|dbAdapter
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|ResultSet
name|rsCatalog
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getCatalogs
argument_list|()
init|)
block|{
name|boolean
name|hasCatalogs
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|systemCatalogs
init|=
name|dbAdapter
operator|.
name|getSystemCatalogs
argument_list|()
decl_stmt|;
while|while
condition|(
name|rsCatalog
operator|.
name|next
argument_list|()
operator|&&
name|dbAdapter
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
condition|)
block|{
name|hasCatalogs
operator|=
literal|true
expr_stmt|;
name|String
name|catalog
init|=
name|rsCatalog
operator|.
name|getString
argument_list|(
literal|"TABLE_CAT"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|systemCatalogs
operator|.
name|contains
argument_list|(
name|catalog
argument_list|)
condition|)
block|{
name|processSchemas
argument_list|(
name|connection
argument_list|,
name|catalog
argument_list|,
name|dbAdapter
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|hasCatalogs
condition|)
block|{
name|processSchemas
argument_list|(
name|connection
argument_list|,
literal|null
argument_list|,
name|dbAdapter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|processSchemas
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|String
name|catalog
parameter_list|,
name|DbAdapter
name|dbAdapter
parameter_list|)
throws|throws
name|SQLException
block|{
name|DatabaseMetaData
name|metaData
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|boolean
name|hasSchemas
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|metaData
operator|.
name|supportsSchemasInTableDefinitions
argument_list|()
condition|)
block|{
try|try
init|(
name|ResultSet
name|rsSchema
init|=
name|metaData
operator|.
name|getSchemas
argument_list|(
name|catalog
argument_list|,
literal|null
argument_list|)
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|systemSchemas
init|=
name|dbAdapter
operator|.
name|getSystemSchemas
argument_list|()
decl_stmt|;
while|while
condition|(
name|rsSchema
operator|.
name|next
argument_list|()
condition|)
block|{
name|hasSchemas
operator|=
literal|true
expr_stmt|;
name|String
name|schema
init|=
name|rsSchema
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|systemSchemas
operator|.
name|contains
argument_list|(
name|schema
argument_list|)
condition|)
block|{
name|packFilterContainer
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|catalog
operator|!=
literal|null
operator|&&
operator|!
name|hasSchemas
condition|)
block|{
name|packFilterContainer
argument_list|(
name|catalog
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ReverseEngineering
name|loadTables
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|ClassLoadingService
name|loadingService
parameter_list|,
name|TreePath
name|path
parameter_list|,
name|String
index|[]
name|tableTypesFromConfig
parameter_list|)
throws|throws
name|SQLException
block|{
name|int
name|pathIndex
init|=
literal|1
decl_stmt|;
name|String
name|catalogName
init|=
literal|null
decl_stmt|,
name|schemaName
init|=
literal|null
decl_stmt|;
name|Object
name|userObject
init|=
name|getUserObjectOrNull
argument_list|(
name|path
argument_list|,
name|pathIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|userObject
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|userObject
operator|instanceof
name|Catalog
condition|)
block|{
name|Catalog
name|catalog
init|=
operator|(
name|Catalog
operator|)
name|userObject
decl_stmt|;
name|catalogName
operator|=
name|catalog
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|userObject
operator|=
name|getUserObjectOrNull
argument_list|(
name|path
argument_list|,
operator|++
name|pathIndex
argument_list|)
expr_stmt|;
if|if
condition|(
name|userObject
operator|instanceof
name|Schema
condition|)
block|{
name|schemaName
operator|=
operator|(
operator|(
name|Schema
operator|)
name|userObject
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|userObject
operator|instanceof
name|Schema
condition|)
block|{
name|schemaName
operator|=
operator|(
operator|(
name|Schema
operator|)
name|userObject
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
try|try
init|(
name|Connection
name|connection
init|=
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|loadingService
argument_list|)
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|String
index|[]
name|types
init|=
name|tableTypesFromConfig
operator|!=
literal|null
operator|&&
name|tableTypesFromConfig
operator|.
name|length
operator|!=
literal|0
condition|?
name|tableTypesFromConfig
else|:
operator|new
name|String
index|[]
block|{
literal|"TABLE"
block|,
literal|"VIEW"
block|,
literal|"SYSTEM TABLE"
block|,
literal|"GLOBAL TEMPORARY"
block|,
literal|"LOCAL TEMPORARY"
block|,
literal|"ALIAS"
block|,
literal|"SYNONYM"
block|}
decl_stmt|;
try|try
init|(
name|ResultSet
name|resultSet
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|,
name|INCLUDE_ALL_PATTERN
argument_list|,
name|types
argument_list|)
init|)
block|{
name|boolean
name|hasTables
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|resultSet
operator|.
name|next
argument_list|()
condition|)
block|{
name|hasTables
operator|=
literal|true
expr_stmt|;
name|String
name|table
init|=
name|resultSet
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|String
name|schema
init|=
name|resultSet
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
name|String
name|catalog
init|=
name|resultSet
operator|.
name|getString
argument_list|(
literal|"TABLE_CAT"
argument_list|)
decl_stmt|;
name|packTable
argument_list|(
name|table
argument_list|,
name|catalog
operator|==
literal|null
condition|?
name|catalogName
else|:
name|catalog
argument_list|,
name|schema
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasTables
operator|&&
operator|(
name|catalogName
operator|!=
literal|null
operator|||
name|schemaName
operator|!=
literal|null
operator|)
condition|)
block|{
name|packFilterContainer
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|)
expr_stmt|;
block|}
name|packProcedures
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|databaseReverseEngineering
return|;
block|}
specifier|public
name|ReverseEngineering
name|loadColumns
parameter_list|(
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|ClassLoadingService
name|loadingService
parameter_list|,
name|TreePath
name|path
parameter_list|)
throws|throws
name|SQLException
block|{
name|int
name|pathIndex
init|=
literal|1
decl_stmt|;
name|String
name|catalogName
init|=
literal|null
decl_stmt|,
name|schemaName
init|=
literal|null
decl_stmt|;
name|Object
name|userObject
init|=
name|getUserObjectOrNull
argument_list|(
name|path
argument_list|,
name|pathIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|userObject
operator|instanceof
name|Catalog
condition|)
block|{
name|catalogName
operator|=
operator|(
operator|(
name|Catalog
operator|)
name|userObject
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|userObject
operator|=
name|getUserObjectOrNull
argument_list|(
name|path
argument_list|,
operator|++
name|pathIndex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|userObject
operator|instanceof
name|Schema
condition|)
block|{
name|schemaName
operator|=
operator|(
operator|(
name|Schema
operator|)
name|userObject
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|userObject
operator|=
name|getUserObjectOrNull
argument_list|(
name|path
argument_list|,
operator|++
name|pathIndex
argument_list|)
expr_stmt|;
block|}
name|String
name|tableName
init|=
name|processTable
argument_list|(
name|userObject
argument_list|)
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|loadingService
argument_list|)
operator|.
name|getConnection
argument_list|()
init|)
block|{
try|try
init|(
name|ResultSet
name|rs
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getColumns
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|,
name|tableName
argument_list|,
literal|null
argument_list|)
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|column
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"COLUMN_NAME"
argument_list|)
decl_stmt|;
name|packTable
argument_list|(
name|tableName
argument_list|,
name|catalogName
argument_list|,
name|schemaName
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|sort
argument_list|()
expr_stmt|;
return|return
name|databaseReverseEngineering
return|;
block|}
specifier|private
name|FilterContainer
name|packFilterContainer
parameter_list|(
name|String
name|catalogName
parameter_list|,
name|String
name|schemaName
parameter_list|)
block|{
name|SchemaContainer
name|parentCatalog
decl_stmt|;
if|if
condition|(
name|catalogName
operator|==
literal|null
condition|)
block|{
name|parentCatalog
operator|=
name|databaseReverseEngineering
expr_stmt|;
block|}
else|else
block|{
name|parentCatalog
operator|=
name|getCatalogByName
argument_list|(
name|databaseReverseEngineering
operator|.
name|getCatalogs
argument_list|()
argument_list|,
name|catalogName
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentCatalog
operator|==
literal|null
condition|)
block|{
name|parentCatalog
operator|=
operator|new
name|Catalog
argument_list|()
expr_stmt|;
name|parentCatalog
operator|.
name|setName
argument_list|(
name|catalogName
argument_list|)
expr_stmt|;
name|databaseReverseEngineering
operator|.
name|addCatalog
argument_list|(
operator|(
name|Catalog
operator|)
name|parentCatalog
argument_list|)
expr_stmt|;
block|}
block|}
name|Schema
name|parentSchema
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|schemaName
operator|!=
literal|null
condition|)
block|{
name|parentSchema
operator|=
name|getSchemaByName
argument_list|(
name|parentCatalog
operator|.
name|getSchemas
argument_list|()
argument_list|,
name|schemaName
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentSchema
operator|==
literal|null
condition|)
block|{
name|parentSchema
operator|=
operator|new
name|Schema
argument_list|()
expr_stmt|;
name|parentSchema
operator|.
name|setName
argument_list|(
name|schemaName
argument_list|)
expr_stmt|;
name|parentCatalog
operator|.
name|addSchema
argument_list|(
name|parentSchema
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|parentSchema
operator|==
literal|null
condition|?
name|parentCatalog
else|:
name|parentSchema
return|;
block|}
specifier|private
name|Object
name|getUserObjectOrNull
parameter_list|(
name|TreePath
name|path
parameter_list|,
name|int
name|pathIndex
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|path
operator|.
name|getPathComponent
argument_list|(
name|pathIndex
argument_list|)
operator|)
operator|.
name|getUserObject
argument_list|()
return|;
block|}
specifier|private
name|String
name|processTable
parameter_list|(
name|Object
name|userObject
parameter_list|)
block|{
if|if
condition|(
name|userObject
operator|instanceof
name|IncludeTable
condition|)
block|{
return|return
operator|(
operator|(
name|IncludeTable
operator|)
name|userObject
operator|)
operator|.
name|getPattern
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|packProcedures
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
init|=
name|databaseReverseEngineering
operator|.
name|getCatalogs
argument_list|()
decl_stmt|;
for|for
control|(
name|Catalog
name|catalog
range|:
name|catalogs
control|)
block|{
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
name|catalog
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|schemas
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Schema
name|schema
range|:
name|schemas
control|)
block|{
name|ResultSet
name|procResultSet
init|=
name|getProcedures
argument_list|(
name|connection
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|packProcedures
argument_list|(
name|procResultSet
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ResultSet
name|procResultSet
init|=
name|getProcedures
argument_list|(
name|connection
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|packProcedures
argument_list|(
name|procResultSet
argument_list|,
name|catalog
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
name|databaseReverseEngineering
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
for|for
control|(
name|Schema
name|schema
range|:
name|schemas
control|)
block|{
name|ResultSet
name|procResultSet
init|=
name|getProcedures
argument_list|(
name|connection
argument_list|,
literal|null
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|packProcedures
argument_list|(
name|procResultSet
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ResultSet
name|getProcedures
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getProcedures
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|,
literal|"%"
argument_list|)
return|;
block|}
specifier|private
name|void
name|packProcedures
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|,
name|FilterContainer
name|filterContainer
parameter_list|)
throws|throws
name|SQLException
block|{
while|while
condition|(
name|resultSet
operator|.
name|next
argument_list|()
condition|)
block|{
name|IncludeProcedure
name|includeProcedure
init|=
operator|new
name|IncludeProcedure
argument_list|(
name|resultSet
operator|.
name|getString
argument_list|(
literal|"PROCEDURE_NAME"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|filterContainer
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|contains
argument_list|(
name|includeProcedure
argument_list|)
condition|)
block|{
name|filterContainer
operator|.
name|addIncludeProcedure
argument_list|(
name|includeProcedure
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|packTable
parameter_list|(
name|String
name|tableName
parameter_list|,
name|String
name|catalogName
parameter_list|,
name|String
name|schemaName
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
name|IncludeTable
name|table
init|=
operator|new
name|IncludeTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|setPattern
argument_list|(
name|tableName
argument_list|)
expr_stmt|;
if|if
condition|(
name|catalogName
operator|==
literal|null
operator|&&
name|schemaName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|databaseReverseEngineering
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|contains
argument_list|(
name|table
argument_list|)
condition|)
block|{
name|databaseReverseEngineering
operator|.
name|addIncludeTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
name|addColumn
argument_list|(
literal|null
argument_list|,
name|table
argument_list|,
name|columnName
argument_list|)
expr_stmt|;
return|return;
block|}
name|FilterContainer
name|filterContainer
init|=
name|packFilterContainer
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|)
decl_stmt|;
name|addTable
argument_list|(
name|filterContainer
argument_list|,
name|table
argument_list|)
expr_stmt|;
name|addColumn
argument_list|(
name|filterContainer
argument_list|,
name|table
argument_list|,
name|columnName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addTable
parameter_list|(
name|FilterContainer
name|parentFilter
parameter_list|,
name|IncludeTable
name|table
parameter_list|)
block|{
if|if
condition|(
operator|!
name|parentFilter
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|contains
argument_list|(
name|table
argument_list|)
condition|)
block|{
name|parentFilter
operator|.
name|addIncludeTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addColumn
parameter_list|(
name|FilterContainer
name|filterContainer
parameter_list|,
name|IncludeTable
name|table
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
if|if
condition|(
name|columnName
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|filterContainer
operator|=
name|filterContainer
operator|==
literal|null
condition|?
name|databaseReverseEngineering
else|:
name|filterContainer
expr_stmt|;
name|IncludeTable
name|foundTable
init|=
name|getTableByName
argument_list|(
name|filterContainer
operator|.
name|getIncludeTables
argument_list|()
argument_list|,
name|table
operator|.
name|getPattern
argument_list|()
argument_list|)
decl_stmt|;
name|table
operator|=
name|foundTable
operator|!=
literal|null
condition|?
name|foundTable
else|:
name|table
expr_stmt|;
name|IncludeColumn
name|includeColumn
init|=
operator|new
name|IncludeColumn
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
name|table
operator|.
name|addIncludeColumn
argument_list|(
name|includeColumn
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Catalog
name|getCatalogByName
parameter_list|(
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
parameter_list|,
name|String
name|catalogName
parameter_list|)
block|{
return|return
name|catalogs
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|catalog
lambda|->
name|catalog
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|catalogName
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|private
name|IncludeTable
name|getTableByName
parameter_list|(
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|tables
parameter_list|,
name|String
name|catalogName
parameter_list|)
block|{
return|return
name|tables
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|table
lambda|->
name|table
operator|.
name|getPattern
argument_list|()
operator|.
name|equals
argument_list|(
name|catalogName
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Schema
name|getSchemaByName
parameter_list|(
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
parameter_list|,
name|String
name|schemaName
parameter_list|)
block|{
return|return
name|schemas
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|schema
lambda|->
name|schema
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|schemaName
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

