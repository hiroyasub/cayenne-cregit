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
name|access
operator|.
name|jdbc
operator|.
name|reader
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|jdbc
operator|.
name|RowDescriptor
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
name|map
operator|.
name|ObjEntity
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
name|query
operator|.
name|QueryMetadata
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|abstract
class|class
name|BaseRowReader
parameter_list|<
name|T
parameter_list|>
implements|implements
name|RowReader
argument_list|<
name|T
argument_list|>
block|{
name|ExtendedType
index|[]
name|converters
decl_stmt|;
name|String
index|[]
name|labels
decl_stmt|;
name|int
index|[]
name|types
decl_stmt|;
name|DataRowPostProcessor
name|postProcessor
decl_stmt|;
name|String
name|entityName
decl_stmt|;
name|BaseRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|DataRowPostProcessor
name|postProcessor
parameter_list|)
block|{
name|ObjEntity
name|rootObjEntity
init|=
name|queryMetadata
operator|.
name|getObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootObjEntity
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|entityName
operator|=
name|rootObjEntity
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|postProcessor
operator|=
name|postProcessor
expr_stmt|;
name|this
operator|.
name|converters
operator|=
name|descriptor
operator|.
name|getConverters
argument_list|()
expr_stmt|;
name|ColumnDescriptor
index|[]
name|columns
init|=
name|descriptor
operator|.
name|getColumns
argument_list|()
decl_stmt|;
name|int
name|width
init|=
name|columns
operator|.
name|length
decl_stmt|;
name|this
operator|.
name|labels
operator|=
operator|new
name|String
index|[
name|width
index|]
expr_stmt|;
name|this
operator|.
name|types
operator|=
operator|new
name|int
index|[
name|width
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|width
condition|;
name|i
operator|++
control|)
block|{
name|labels
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getDataRowKey
argument_list|()
expr_stmt|;
name|types
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|T
name|readRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
function_decl|;
block|}
end_class

end_unit

