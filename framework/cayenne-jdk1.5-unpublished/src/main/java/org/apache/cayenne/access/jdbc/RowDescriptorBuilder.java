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
name|access
operator|.
name|jdbc
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|types
operator|.
name|ExtendedType
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
name|types
operator|.
name|ExtendedTypeMap
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
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * A builder class that helps to assemble {@link RowDescriptor} instances from various  * types of inputs.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|RowDescriptorBuilder
block|{
specifier|private
specifier|static
specifier|final
name|Transformer
name|UPPERCASE_TRANSFORMER
init|=
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
return|return
name|input
operator|!=
literal|null
condition|?
name|input
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
else|:
literal|null
return|;
block|}
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Transformer
name|LOWERCASE_TRANSFORMER
init|=
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
return|return
name|input
operator|!=
literal|null
condition|?
name|input
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|null
return|;
block|}
block|}
decl_stmt|;
specifier|protected
name|ColumnDescriptor
index|[]
name|columns
decl_stmt|;
specifier|protected
name|ResultSetMetaData
name|resultSetMetadata
decl_stmt|;
specifier|protected
name|Transformer
name|caseTransformer
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeOverrides
decl_stmt|;
comment|/**      * Returns a RowDescriptor built based on the builder internal state.      */
specifier|public
name|RowDescriptor
name|getDescriptor
parameter_list|(
name|ExtendedTypeMap
name|typeMap
parameter_list|)
throws|throws
name|SQLException
throws|,
name|IllegalStateException
block|{
name|ColumnDescriptor
index|[]
name|columnsForRD
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|resultSetMetadata
operator|!=
literal|null
condition|)
block|{
comment|// do merge between explicitly-set columns and ResultSetMetadata
comment|// explicitly-set columns take precedence
name|columnsForRD
operator|=
name|mergeResultSetAndPresetColumns
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|this
operator|.
name|columns
operator|!=
literal|null
condition|)
block|{
comment|// use explicitly-set columns
name|columnsForRD
operator|=
name|this
operator|.
name|columns
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't build RowDescriptor, both 'columns' and 'resultSetMetadata' are null"
argument_list|)
throw|;
block|}
name|performTransformAndTypeOverride
argument_list|(
name|columnsForRD
argument_list|)
expr_stmt|;
name|ExtendedType
index|[]
name|converters
init|=
operator|new
name|ExtendedType
index|[
name|columnsForRD
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|columnsForRD
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|converters
index|[
name|i
index|]
operator|=
name|typeMap
operator|.
name|getRegisteredType
argument_list|(
name|columnsForRD
index|[
name|i
index|]
operator|.
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RowDescriptor
argument_list|(
name|columnsForRD
argument_list|,
name|converters
argument_list|)
return|;
block|}
comment|/**      * @return array of columns for ResultSet with overriding ColumnDescriptors from      *         'columns' Note: column will be overlooked, if column name is empty      */
specifier|protected
name|ColumnDescriptor
index|[]
name|mergeResultSetAndPresetColumns
parameter_list|()
throws|throws
name|SQLException
block|{
name|int
name|rsLen
init|=
name|resultSetMetadata
operator|.
name|getColumnCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|rsLen
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'ResultSetMetadata' is empty."
argument_list|)
throw|;
block|}
name|int
name|columnLen
init|=
operator|(
name|columns
operator|!=
literal|null
operator|)
condition|?
name|columns
operator|.
name|length
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|rsLen
operator|<
name|columnLen
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'ResultSetMetadata' has less elements then 'columns'."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|rsLen
operator|==
name|columnLen
condition|)
block|{
comment|// 'columns' contains ColumnDescriptor for every column
comment|// in resultSetMetadata. This return is for optimization.
return|return
name|columns
return|;
block|}
name|ColumnDescriptor
index|[]
name|rsColumns
init|=
operator|new
name|ColumnDescriptor
index|[
name|rsLen
index|]
decl_stmt|;
name|int
name|outputLen
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rsLen
condition|;
name|i
operator|++
control|)
block|{
name|String
name|rowkey
init|=
name|resolveDataRowKeyFromResultSet
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|rowkey
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// escape this ColumnDescriptor, cause column name is empty
continue|continue;
block|}
comment|// resolve column descriptor from 'columns' or create new
name|rsColumns
index|[
name|outputLen
index|]
operator|=
name|getColumnDescriptor
argument_list|(
name|rowkey
argument_list|,
name|columns
argument_list|,
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|outputLen
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|outputLen
operator|<
name|rsLen
condition|)
block|{
comment|// cut ColumnDescriptor array
name|ColumnDescriptor
index|[]
name|rsColumnsCut
init|=
operator|new
name|ColumnDescriptor
index|[
name|outputLen
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|rsColumns
argument_list|,
literal|0
argument_list|,
name|rsColumnsCut
argument_list|,
literal|0
argument_list|,
name|outputLen
argument_list|)
expr_stmt|;
return|return
name|rsColumnsCut
return|;
block|}
return|return
name|rsColumns
return|;
block|}
comment|/**      * @return ColumnDescriptor from columnArray, if columnArray contains descriptor for      *         this column, or new ColumnDescriptor.      */
specifier|private
name|ColumnDescriptor
name|getColumnDescriptor
parameter_list|(
name|String
name|rowKey
parameter_list|,
name|ColumnDescriptor
index|[]
name|columnArray
parameter_list|,
name|int
name|position
parameter_list|)
throws|throws
name|SQLException
block|{
name|int
name|len
init|=
operator|(
name|columnArray
operator|!=
literal|null
operator|)
condition|?
name|columnArray
operator|.
name|length
else|:
literal|0
decl_stmt|;
comment|// go through columnArray to find ColumnDescriptor for specified column
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|columnArray
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|String
name|columnRowKey
init|=
name|columnArray
index|[
name|i
index|]
operator|.
name|getDataRowKey
argument_list|()
decl_stmt|;
comment|// TODO: andrus, 10/14/2009 - 'equalsIgnoreCase' check can result in
comment|// subtle bugs in DBs with case-sensitive column names (or when quotes are
comment|// used to force case sensitivity). Alternatively using 'equals' may miss
comment|// columns in case-insensitive situations.
if|if
condition|(
name|columnRowKey
operator|!=
literal|null
operator|&&
name|columnRowKey
operator|.
name|equalsIgnoreCase
argument_list|(
name|rowKey
argument_list|)
condition|)
block|{
return|return
name|columnArray
index|[
name|i
index|]
return|;
block|}
block|}
block|}
comment|// columnArray doesn't contain ColumnDescriptor for specified column
return|return
operator|new
name|ColumnDescriptor
argument_list|(
name|rowKey
argument_list|,
name|resultSetMetadata
argument_list|,
name|position
argument_list|)
return|;
block|}
comment|/**      * Return not empty string with ColumnLabel or ColumnName or "column_" + position for      * for specified (by it's position) column in ResultSetMetaData.      */
specifier|private
name|String
name|resolveDataRowKeyFromResultSet
parameter_list|(
name|int
name|position
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|name
init|=
name|resultSetMetadata
operator|.
name|getColumnLabel
argument_list|(
name|position
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
name|resultSetMetadata
operator|.
name|getColumnName
argument_list|(
name|position
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|""
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
specifier|private
name|void
name|performTransformAndTypeOverride
parameter_list|(
name|ColumnDescriptor
index|[]
name|columnArray
parameter_list|)
block|{
name|int
name|len
init|=
name|columnArray
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|caseTransformer
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|columnArray
index|[
name|i
index|]
operator|.
name|setDataRowKey
argument_list|(
operator|(
name|String
operator|)
name|caseTransformer
operator|.
name|transform
argument_list|(
name|columnArray
index|[
name|i
index|]
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|columnArray
index|[
name|i
index|]
operator|.
name|setName
argument_list|(
operator|(
name|String
operator|)
name|caseTransformer
operator|.
name|transform
argument_list|(
name|columnArray
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|typeOverrides
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|String
name|type
init|=
name|typeOverrides
operator|.
name|get
argument_list|(
name|columnArray
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|columnArray
index|[
name|i
index|]
operator|.
name|setJavaClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Sets an explicit set of columns. Note that the array passed as an argument can      * later be modified by the build to enforce column capitalization policy and columns      * Java types overrides.      */
specifier|public
name|RowDescriptorBuilder
name|setColumns
parameter_list|(
name|ColumnDescriptor
index|[]
name|columns
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|RowDescriptorBuilder
name|setResultSet
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
operator|.
name|resultSetMetadata
operator|=
name|resultSet
operator|.
name|getMetaData
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|RowDescriptorBuilder
name|useLowercaseColumnNames
parameter_list|()
block|{
name|this
operator|.
name|caseTransformer
operator|=
name|LOWERCASE_TRANSFORMER
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|RowDescriptorBuilder
name|useUppercaseColumnNames
parameter_list|()
block|{
name|this
operator|.
name|caseTransformer
operator|=
name|UPPERCASE_TRANSFORMER
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|RowDescriptorBuilder
name|overrideColumnType
parameter_list|(
name|String
name|columnName
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|typeOverrides
operator|==
literal|null
condition|)
block|{
name|typeOverrides
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|typeOverrides
operator|.
name|put
argument_list|(
name|columnName
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isOverriden
parameter_list|(
name|String
name|columnName
parameter_list|)
block|{
return|return
name|typeOverrides
operator|!=
literal|null
operator|&&
name|typeOverrides
operator|.
name|containsKey
argument_list|(
name|columnName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

