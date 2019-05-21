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
name|map
operator|.
name|ProcedureParameter
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
specifier|public
class|class
name|ProcedureColumnLoader
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
name|ProcedureColumnLoader
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
name|getProcedureColumns
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
name|boolean
name|shouldLoad
parameter_list|(
name|CatalogFilter
name|catalog
parameter_list|,
name|SchemaFilter
name|schema
parameter_list|)
block|{
name|PatternFilter
name|filter
init|=
name|config
operator|.
name|getFiltersConfig
argument_list|()
operator|.
name|proceduresFilter
argument_list|(
name|catalog
operator|.
name|name
argument_list|,
name|schema
operator|.
name|name
argument_list|)
decl_stmt|;
return|return
operator|!
name|filter
operator|.
name|isEmpty
argument_list|()
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
name|procSchema
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PROCEDURE_SCHEM"
argument_list|)
decl_stmt|;
name|String
name|procCatalog
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PROCEDURE_CAT"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PROCEDURE_NAME"
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|Procedure
operator|.
name|generateFullyQualifiedName
argument_list|(
name|procCatalog
argument_list|,
name|procSchema
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|Procedure
name|procedure
init|=
name|map
operator|.
name|getProcedure
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// should be filtered out in getResultSet() method, but check full name here too..
if|if
condition|(
name|procedure
operator|==
literal|null
operator|||
operator|!
name|key
operator|.
name|equals
argument_list|(
name|procedure
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|ProcedureParameter
name|column
init|=
name|loadProcedureParams
argument_list|(
name|rs
argument_list|,
name|key
argument_list|,
name|procedure
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|procedure
operator|.
name|addCallParameter
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ProcedureParameter
name|loadProcedureParams
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|key
parameter_list|,
name|Procedure
name|procedure
parameter_list|)
throws|throws
name|SQLException
block|{
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
comment|// skip ResultSet columns, as they are not described in Cayenne procedures yet...
name|short
name|type
init|=
name|rs
operator|.
name|getShort
argument_list|(
literal|"COLUMN_TYPE"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|DatabaseMetaData
operator|.
name|procedureColumnResult
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"skipping ResultSet column: "
operator|+
name|key
operator|+
literal|"."
operator|+
name|columnName
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|columnName
operator|==
literal|null
operator|||
name|columnName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|type
operator|==
name|DatabaseMetaData
operator|.
name|procedureColumnReturn
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"null column name, assuming result column: "
operator|+
name|key
argument_list|)
expr_stmt|;
name|columnName
operator|=
literal|"_return_value"
expr_stmt|;
name|procedure
operator|.
name|setReturningValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"invalid null column name, skipping column : "
operator|+
name|key
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
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
name|getShort
argument_list|(
literal|"SCALE"
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
name|ProcedureParameter
name|column
init|=
operator|new
name|ProcedureParameter
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
name|int
name|direction
init|=
name|getDirection
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|direction
operator|!=
operator|-
literal|1
condition|)
block|{
name|column
operator|.
name|setDirection
argument_list|(
name|direction
argument_list|)
expr_stmt|;
block|}
name|column
operator|.
name|setType
argument_list|(
name|columnType
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
literal|"LENGTH"
argument_list|)
argument_list|)
expr_stmt|;
name|column
operator|.
name|setPrecision
argument_list|(
name|decimalDigits
argument_list|)
expr_stmt|;
name|column
operator|.
name|setProcedure
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
return|return
name|column
return|;
block|}
specifier|private
name|int
name|getDirection
parameter_list|(
name|short
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|DatabaseMetaData
operator|.
name|procedureColumnIn
case|:
return|return
name|ProcedureParameter
operator|.
name|IN_PARAMETER
return|;
case|case
name|DatabaseMetaData
operator|.
name|procedureColumnInOut
case|:
return|return
name|ProcedureParameter
operator|.
name|IN_OUT_PARAMETER
return|;
case|case
name|DatabaseMetaData
operator|.
name|procedureColumnOut
case|:
return|return
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
return|;
case|case
name|DatabaseMetaData
operator|.
name|procedureColumnReturn
case|:
return|return
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
return|;
default|default:
return|return
operator|-
literal|1
return|;
block|}
block|}
block|}
end_class

end_unit

