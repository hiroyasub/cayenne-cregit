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
name|Collection
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
name|List
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|jdbc
operator|.
name|reader
operator|.
name|DataRowPostProcessor
operator|.
name|ColumnOverride
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
name|map
operator|.
name|Entity
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
name|ObjAttribute
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
name|EntityResultSegment
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
name|query
operator|.
name|ScalarResultSegment
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRowReaderFactory
implements|implements
name|RowReaderFactory
block|{
annotation|@
name|Override
specifier|public
name|RowReader
argument_list|<
name|?
argument_list|>
name|rowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
parameter_list|)
block|{
name|PostprocessorFactory
name|postProcessorFactory
init|=
operator|new
name|PostprocessorFactory
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
argument_list|,
name|attributeOverrides
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|rsMapping
init|=
name|queryMetadata
operator|.
name|getResultSetMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|rsMapping
operator|==
literal|null
condition|)
block|{
return|return
name|createFullRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|postProcessorFactory
argument_list|)
return|;
block|}
name|int
name|resultWidth
init|=
name|rsMapping
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultWidth
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Empty result descriptor"
argument_list|)
throw|;
block|}
if|if
condition|(
name|queryMetadata
operator|.
name|isSingleResultSetMapping
argument_list|()
condition|)
block|{
name|Object
name|segment
init|=
name|rsMapping
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|segment
operator|instanceof
name|EntityResultSegment
condition|)
block|{
return|return
name|createEntityRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
operator|(
name|EntityResultSegment
operator|)
name|segment
argument_list|,
name|postProcessorFactory
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createScalarRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
operator|(
name|ScalarResultSegment
operator|)
name|segment
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|CompoundRowReader
name|reader
init|=
operator|new
name|CompoundRowReader
argument_list|(
name|resultWidth
argument_list|)
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
name|resultWidth
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|segment
init|=
name|rsMapping
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|segment
operator|instanceof
name|EntityResultSegment
condition|)
block|{
name|reader
operator|.
name|addRowReader
argument_list|(
name|i
argument_list|,
name|createEntityRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
operator|(
name|EntityResultSegment
operator|)
name|segment
argument_list|,
name|postProcessorFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reader
operator|.
name|addRowReader
argument_list|(
name|i
argument_list|,
name|createScalarRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
operator|(
name|ScalarResultSegment
operator|)
name|segment
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|reader
return|;
block|}
block|}
specifier|protected
name|RowReader
argument_list|<
name|?
argument_list|>
name|createScalarRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|ScalarResultSegment
name|segment
parameter_list|)
block|{
return|return
operator|new
name|ScalarRowReader
argument_list|<
name|Object
argument_list|>
argument_list|(
name|descriptor
argument_list|,
name|segment
argument_list|)
return|;
block|}
specifier|protected
name|RowReader
argument_list|<
name|?
argument_list|>
name|createEntityRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|EntityResultSegment
name|resultMetadata
parameter_list|,
name|PostprocessorFactory
name|postProcessorFactory
parameter_list|)
block|{
if|if
condition|(
name|queryMetadata
operator|.
name|getPageSize
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|IdRowReader
argument_list|<>
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|resultMetadata
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
if|else if
condition|(
name|resultMetadata
operator|.
name|getClassDescriptor
argument_list|()
operator|!=
literal|null
operator|&&
name|resultMetadata
operator|.
name|getClassDescriptor
argument_list|()
operator|.
name|hasSubclasses
argument_list|()
condition|)
block|{
return|return
operator|new
name|InheritanceAwareEntityRowReader
argument_list|(
name|descriptor
argument_list|,
name|resultMetadata
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|EntityRowReader
argument_list|(
name|descriptor
argument_list|,
name|resultMetadata
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|protected
name|RowReader
argument_list|<
name|?
argument_list|>
name|createFullRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|PostprocessorFactory
name|postProcessorFactory
parameter_list|)
block|{
if|if
condition|(
name|queryMetadata
operator|.
name|getPageSize
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|IdRowReader
argument_list|<>
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
literal|null
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
if|else if
condition|(
name|queryMetadata
operator|.
name|getClassDescriptor
argument_list|()
operator|!=
literal|null
operator|&&
name|queryMetadata
operator|.
name|getClassDescriptor
argument_list|()
operator|.
name|hasSubclasses
argument_list|()
condition|)
block|{
return|return
operator|new
name|InheritanceAwareRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|FullRowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|postProcessorFactory
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|protected
class|class
name|PostprocessorFactory
block|{
specifier|private
name|QueryMetadata
name|queryMetadata
decl_stmt|;
specifier|private
name|ExtendedTypeMap
name|extendedTypes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
decl_stmt|;
specifier|private
name|RowDescriptor
name|rowDescriptor
decl_stmt|;
specifier|private
name|boolean
name|created
decl_stmt|;
specifier|private
name|DataRowPostProcessor
name|postProcessor
decl_stmt|;
name|PostprocessorFactory
parameter_list|(
name|RowDescriptor
name|rowDescriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|ExtendedTypeMap
name|extendedTypes
parameter_list|,
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
parameter_list|)
block|{
name|this
operator|.
name|rowDescriptor
operator|=
name|rowDescriptor
expr_stmt|;
name|this
operator|.
name|extendedTypes
operator|=
name|extendedTypes
expr_stmt|;
name|this
operator|.
name|attributeOverrides
operator|=
name|attributeOverrides
expr_stmt|;
name|this
operator|.
name|queryMetadata
operator|=
name|queryMetadata
expr_stmt|;
block|}
name|DataRowPostProcessor
name|get
parameter_list|()
block|{
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|postProcessor
operator|=
name|create
argument_list|()
expr_stmt|;
name|created
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|postProcessor
return|;
block|}
specifier|private
name|DataRowPostProcessor
name|create
parameter_list|()
block|{
if|if
condition|(
name|attributeOverrides
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ColumnDescriptor
index|[]
name|columns
init|=
name|rowDescriptor
operator|.
name|getColumns
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|ColumnOverride
argument_list|>
argument_list|>
name|columnOverrides
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|entry
range|:
name|attributeOverrides
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ObjAttribute
name|attribute
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Entity
name|entity
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|String
name|key
init|=
literal|null
decl_stmt|;
name|int
name|jdbcType
init|=
name|TypesMapping
operator|.
name|NOT_DEFINED
decl_stmt|;
name|int
name|index
init|=
operator|-
literal|1
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
name|columns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|columns
index|[
name|i
index|]
operator|==
name|entry
operator|.
name|getValue
argument_list|()
condition|)
block|{
comment|// if attribute type is the same as column, there is no
comment|// conflict
if|if
condition|(
operator|!
name|attribute
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|columns
index|[
name|i
index|]
operator|.
name|getJavaClass
argument_list|()
argument_list|)
condition|)
block|{
comment|// note that JDBC index is "1" based
name|index
operator|=
name|i
operator|+
literal|1
expr_stmt|;
name|jdbcType
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
expr_stmt|;
name|key
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getDataRowKey
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|index
operator|<
literal|1
condition|)
block|{
continue|continue;
block|}
name|ExtendedType
name|converter
init|=
name|extendedTypes
operator|.
name|getRegisteredType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ColumnOverride
argument_list|>
name|overrides
init|=
name|columnOverrides
operator|.
name|computeIfAbsent
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|k
lambda|->
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|overrides
operator|.
name|add
argument_list|(
operator|new
name|ColumnOverride
argument_list|(
name|index
argument_list|,
name|key
argument_list|,
name|converter
argument_list|,
name|jdbcType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// inject null post-processor
if|if
condition|(
name|columnOverrides
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ClassDescriptor
name|rootDescriptor
init|=
name|queryMetadata
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
return|return
operator|new
name|DataRowPostProcessor
argument_list|(
name|rootDescriptor
argument_list|,
name|columnOverrides
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

