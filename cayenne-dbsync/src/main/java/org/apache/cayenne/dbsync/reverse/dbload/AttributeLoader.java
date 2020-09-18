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
name|ResultSetMetaData
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
name|model
operator|.
name|DetectedDbAttribute
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
name|AttributeLoader
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
name|boolean
name|firstRow
decl_stmt|;
specifier|private
name|boolean
name|supportAutoIncrement
decl_stmt|;
name|AttributeLoader
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
name|firstRow
operator|=
literal|true
expr_stmt|;
name|supportAutoIncrement
operator|=
literal|false
expr_stmt|;
block|}
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
name|getColumns
argument_list|(
name|catalogName
argument_list|,
name|schemaName
argument_list|,
name|WILDCARD
argument_list|,
name|WILDCARD
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
if|if
condition|(
name|firstRow
condition|)
block|{
name|supportAutoIncrement
operator|=
name|checkForAutoIncrement
argument_list|(
name|rs
argument_list|)
expr_stmt|;
name|firstRow
operator|=
literal|false
expr_stmt|;
block|}
comment|// for a reason not quiet apparent to me, Oracle sometimes
comment|// returns duplicate record sets for the same table, messing up
comment|// table names. E.g. for the system table "WK$_ATTR_MAPPING" columns
comment|// are returned twice - as "WK$_ATTR_MAPPING" and "WK$$_ATTR_MAPPING"...
comment|// Go figure
name|String
name|tableName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|DbEntity
name|entity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Filter out columns by name
name|String
name|columnName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"COLUMN_NAME"
argument_list|)
decl_stmt|;
name|PatternFilter
name|columnFilter
init|=
name|schema
operator|.
name|tables
operator|.
name|getIncludeTableColumnFilter
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
if|if
condition|(
name|columnFilter
operator|==
literal|null
operator|||
operator|!
name|columnFilter
operator|.
name|isIncluded
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOGGER
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Skip column '"
operator|+
name|tableName
operator|+
literal|"."
operator|+
name|columnName
operator|+
literal|"' (Path: "
operator|+
name|catalog
operator|.
name|name
operator|+
literal|"/"
operator|+
name|schema
operator|.
name|name
operator|+
literal|"; Filter: "
operator|+
name|columnFilter
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|DbAttribute
name|attribute
init|=
name|createDbAttribute
argument_list|(
name|rs
argument_list|)
decl_stmt|;
name|addToDbEntity
argument_list|(
name|entity
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|checkForAutoIncrement
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|ResultSetMetaData
name|rsMetaData
init|=
name|rs
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|rsMetaData
operator|.
name|getColumnCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|"IS_AUTOINCREMENT"
operator|.
name|equals
argument_list|(
name|rsMetaData
operator|.
name|getColumnLabel
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|addToDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|attribute
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// override existing attributes if it comes again
if|if
condition|(
name|entity
operator|.
name|getAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbAttribute
name|createDbAttribute
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// gets attribute's (column's) information
name|int
name|columnType
init|=
name|rs
operator|.
name|getInt
argument_list|(
literal|"DATA_TYPE"
argument_list|)
decl_stmt|;
comment|// ignore precision of non-decimal columns
name|int
name|decimalDigits
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|columnType
argument_list|)
condition|)
block|{
name|decimalDigits
operator|=
name|rs
operator|.
name|getInt
argument_list|(
literal|"DECIMAL_DIGITS"
argument_list|)
expr_stmt|;
if|if
condition|(
name|rs
operator|.
name|wasNull
argument_list|()
condition|)
block|{
name|decimalDigits
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
comment|// create attribute delegating this task to adapter
name|DetectedDbAttribute
name|detectedDbAttribute
init|=
operator|new
name|DetectedDbAttribute
argument_list|(
name|adapter
operator|.
name|buildAttribute
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"COLUMN_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"TYPE_NAME"
argument_list|)
argument_list|,
name|columnType
argument_list|,
name|rs
operator|.
name|getInt
argument_list|(
literal|"COLUMN_SIZE"
argument_list|)
argument_list|,
name|decimalDigits
argument_list|,
name|rs
operator|.
name|getBoolean
argument_list|(
literal|"NULLABLE"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// store raw type name
name|detectedDbAttribute
operator|.
name|setJdbcTypeName
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"TYPE_NAME"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportAutoIncrement
condition|)
block|{
if|if
condition|(
literal|"YES"
operator|.
name|equals
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"IS_AUTOINCREMENT"
argument_list|)
argument_list|)
condition|)
block|{
name|detectedDbAttribute
operator|.
name|setGenerated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|detectedDbAttribute
return|;
block|}
block|}
end_class

end_unit

