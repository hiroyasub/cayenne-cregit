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
name|DataRow
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
name|query
operator|.
name|EmbeddableResultSegment
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|EmbeddableRowReader
implements|implements
name|RowReader
argument_list|<
name|DataRow
argument_list|>
block|{
specifier|private
specifier|final
name|int
name|startIndex
decl_stmt|;
specifier|private
specifier|final
name|int
name|mapCapacity
decl_stmt|;
specifier|private
specifier|final
name|ExtendedType
index|[]
name|converters
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|labels
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|types
decl_stmt|;
name|EmbeddableRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|EmbeddableResultSegment
name|segment
parameter_list|)
block|{
name|int
name|segmentWidth
init|=
name|segment
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|this
operator|.
name|startIndex
operator|=
name|segment
operator|.
name|getColumnOffset
argument_list|()
expr_stmt|;
name|this
operator|.
name|converters
operator|=
operator|new
name|ExtendedType
index|[
name|segmentWidth
index|]
expr_stmt|;
name|this
operator|.
name|types
operator|=
operator|new
name|int
index|[
name|segmentWidth
index|]
expr_stmt|;
name|this
operator|.
name|labels
operator|=
operator|new
name|String
index|[
name|segmentWidth
index|]
expr_stmt|;
name|ExtendedType
index|[]
name|converters
init|=
name|descriptor
operator|.
name|getConverters
argument_list|()
decl_stmt|;
name|ColumnDescriptor
index|[]
name|columns
init|=
name|descriptor
operator|.
name|getColumns
argument_list|()
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
name|segmentWidth
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|converters
index|[
name|i
index|]
operator|=
name|converters
index|[
name|startIndex
operator|+
name|i
index|]
expr_stmt|;
name|types
index|[
name|i
index|]
operator|=
name|columns
index|[
name|startIndex
operator|+
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
expr_stmt|;
name|labels
index|[
name|i
index|]
operator|=
name|segment
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
name|columns
index|[
name|startIndex
operator|+
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|mapCapacity
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|segmentWidth
operator|/
literal|0.75
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataRow
name|readRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
block|{
try|try
block|{
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
name|mapCapacity
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|converters
operator|.
name|length
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
name|len
condition|;
name|i
operator|++
control|)
block|{
comment|// note: jdbc column indexes start from 1, not 0 as in arrays
name|Object
name|val
init|=
name|converters
index|[
name|i
index|]
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|,
name|startIndex
operator|+
name|i
operator|+
literal|1
argument_list|,
name|types
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
name|labels
index|[
name|i
index|]
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|row
return|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|cex
parameter_list|)
block|{
comment|// rethrow unmodified
throw|throw
name|cex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|otherex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Exception materializing id column."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|otherex
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

