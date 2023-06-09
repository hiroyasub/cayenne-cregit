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
name|reverse
operator|.
name|dbload
package|;
end_package

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
name|filters
operator|.
name|CatalogFilter
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
name|SchemaFilter
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
name|dbsync
operator|.
name|model
operator|.
name|DetectedDbEntity
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
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
class|class
name|EntityLoader
extends|extends
name|PerCatalogAndSchemaLoader
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DbLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|types
decl_stmt|;
name|EntityLoader
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|,
name|DbLoaderDelegate
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|,
name|config
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|types
operator|=
name|getTableTypes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ResultSet
name|getResultSet
parameter_list|(
name|String
name|catalogName
parameter_list|,
name|String
name|schemaName
parameter_list|,
name|DatabaseMetaData
name|metaData
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|metaData
operator|.
name|getTables
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|,
name|WILDCARD
argument_list|,
name|types
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processResultSetRow
parameter_list|(
name|CatalogFilter
name|catalog
parameter_list|,
name|SchemaFilter
name|schema
parameter_list|,
name|DbLoadDataStore
name|map
parameter_list|,
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|String
name|catalogName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_CAT"
argument_list|)
decl_stmt|;
name|String
name|schemaName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_TYPE"
argument_list|)
decl_stmt|;
comment|// Oracle 9i and newer has a nifty recycle bin feature...
comment|// but we don't want dropped tables to be included here;
comment|// in fact they may even result in errors on reverse engineering
comment|// as their names have special chars like "/", etc.
comment|// So skip them all together (it's about "name == null" check)
if|if
condition|(
name|name
operator|==
literal|null
operator|||
operator|!
name|schema
operator|.
name|tables
operator|.
name|isIncludeTable
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
operator|(
name|catalog
operator|.
name|name
operator|==
literal|null
operator|||
name|catalog
operator|.
name|name
operator|.
name|equals
argument_list|(
name|catalogName
argument_list|)
operator|)
operator|||
operator|!
operator|(
name|schema
operator|.
name|name
operator|==
literal|null
operator|||
name|schema
operator|.
name|name
operator|.
name|equals
argument_list|(
name|schemaName
argument_list|)
operator|)
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|catalogName
operator|+
literal|"."
operator|+
name|schema
operator|+
literal|"."
operator|+
name|schemaName
operator|+
literal|" wrongly loaded for catalog/schema : "
operator|+
name|catalog
operator|.
name|name
operator|+
literal|"."
operator|+
name|schema
operator|.
name|name
argument_list|)
expr_stmt|;
return|return;
block|}
name|DetectedDbEntity
name|table
init|=
operator|new
name|DetectedDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|table
operator|.
name|setCatalog
argument_list|(
name|catalogName
argument_list|)
expr_stmt|;
name|table
operator|.
name|setSchema
argument_list|(
name|schemaName
argument_list|)
expr_stmt|;
name|table
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|addDbEntityToMap
argument_list|(
name|table
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addDbEntityToMap
parameter_list|(
name|DetectedDbEntity
name|table
parameter_list|,
name|DbLoadDataStore
name|map
parameter_list|)
block|{
name|DbEntity
name|oldEnt
init|=
name|map
operator|.
name|addDbEntitySafe
argument_list|(
name|table
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldEnt
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Overwrite DbEntity: "
operator|+
name|oldEnt
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|dbEntityRemoved
argument_list|(
name|oldEnt
argument_list|)
expr_stmt|;
block|}
name|delegate
operator|.
name|dbEntityAdded
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
index|[]
name|getTableTypes
parameter_list|()
block|{
name|String
index|[]
name|configTypes
init|=
name|config
operator|.
name|getTableTypes
argument_list|()
decl_stmt|;
name|String
name|viewType
init|=
name|adapter
operator|.
name|tableTypeForView
argument_list|()
decl_stmt|;
name|String
name|tableType
init|=
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resultTableTypes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|configTypes
operator|==
literal|null
operator|||
name|configTypes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|addTypeToList
argument_list|(
name|viewType
argument_list|,
name|resultTableTypes
argument_list|)
expr_stmt|;
name|addTypeToList
argument_list|(
name|tableType
argument_list|,
name|resultTableTypes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|type
range|:
name|configTypes
control|)
block|{
if|if
condition|(
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TABLE"
argument_list|)
condition|)
block|{
name|addTypeToList
argument_list|(
name|tableType
argument_list|,
name|resultTableTypes
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"VIEW"
argument_list|)
condition|)
block|{
name|addTypeToList
argument_list|(
name|viewType
argument_list|,
name|resultTableTypes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addTypeToList
argument_list|(
name|type
argument_list|,
name|resultTableTypes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|resultTableTypes
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|addTypeToList
parameter_list|(
name|String
name|type
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|tableTypes
parameter_list|)
block|{
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|tableTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

