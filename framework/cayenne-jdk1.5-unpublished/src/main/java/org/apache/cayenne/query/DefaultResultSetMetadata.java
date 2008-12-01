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
name|query
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
name|map
operator|.
name|EntityResult
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
name|SQLResult
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
comment|/**  * A SQLResultSetMetadata implementation based on {@link SQLResult}.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|DefaultResultSetMetadata
implements|implements
name|SQLResultSetMetadata
block|{
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|segments
decl_stmt|;
specifier|private
name|int
index|[]
name|scalarSegments
decl_stmt|;
specifier|private
name|int
index|[]
name|entitySegments
decl_stmt|;
name|DefaultResultSetMetadata
parameter_list|(
name|SQLResult
name|result
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|descriptors
init|=
name|result
operator|.
name|getComponents
argument_list|()
decl_stmt|;
name|this
operator|.
name|segments
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|descriptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
index|[]
name|scalarSegments
init|=
operator|new
name|int
index|[
name|descriptors
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
index|[]
name|entitySegments
init|=
operator|new
name|int
index|[
name|descriptors
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|ss
init|=
literal|0
decl_stmt|,
name|es
init|=
literal|0
decl_stmt|;
name|int
name|offset
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
name|descriptors
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|descriptor
init|=
name|descriptors
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|instanceof
name|String
condition|)
block|{
name|segments
operator|.
name|add
argument_list|(
operator|new
name|DefaultScalarResultMetadata
argument_list|(
operator|(
name|String
operator|)
name|descriptor
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|offset
operator|+
literal|1
expr_stmt|;
name|scalarSegments
index|[
name|ss
operator|++
index|]
operator|=
name|i
expr_stmt|;
block|}
if|else if
condition|(
name|descriptor
operator|instanceof
name|EntityResult
condition|)
block|{
name|EntityResult
name|entityResult
init|=
operator|(
name|EntityResult
operator|)
name|descriptor
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
init|=
name|entityResult
operator|.
name|getDbFields
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|String
name|entityName
init|=
name|entityResult
operator|.
name|getEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityName
operator|==
literal|null
condition|)
block|{
name|entityName
operator|=
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|entityResult
operator|.
name|getEntityClass
argument_list|()
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|ClassDescriptor
name|classDescriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
name|segments
operator|.
name|add
argument_list|(
operator|new
name|DefaultEntityResultMetadata
argument_list|(
name|classDescriptor
argument_list|,
name|fields
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|offset
operator|+
name|fields
operator|.
name|size
argument_list|()
expr_stmt|;
name|entitySegments
index|[
name|es
operator|++
index|]
operator|=
name|i
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported result set descriptor type: "
operator|+
name|descriptor
argument_list|)
throw|;
block|}
block|}
name|this
operator|.
name|scalarSegments
operator|=
name|trim
argument_list|(
name|scalarSegments
argument_list|,
name|ss
argument_list|)
expr_stmt|;
name|this
operator|.
name|entitySegments
operator|=
name|trim
argument_list|(
name|entitySegments
argument_list|,
name|es
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
index|[]
name|trim
parameter_list|(
name|int
index|[]
name|array
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|int
index|[]
name|trimmed
init|=
operator|new
name|int
index|[
name|size
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|trimmed
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|trimmed
return|;
block|}
specifier|public
name|EntityResultMetadata
name|getEntitySegment
parameter_list|(
name|int
name|position
parameter_list|)
block|{
name|Object
name|result
init|=
name|segments
operator|.
name|get
argument_list|(
name|position
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|EntityResultMetadata
condition|)
block|{
return|return
operator|(
name|EntityResultMetadata
operator|)
name|result
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Segment at position "
operator|+
name|position
operator|+
literal|" is not an entity segment"
argument_list|)
throw|;
block|}
specifier|public
name|int
index|[]
name|getEntitySegments
parameter_list|()
block|{
return|return
name|entitySegments
return|;
block|}
specifier|public
name|ScalarResultMetadata
name|getScalarSegment
parameter_list|(
name|int
name|position
parameter_list|)
block|{
name|Object
name|result
init|=
name|segments
operator|.
name|get
argument_list|(
name|position
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|ScalarResultMetadata
condition|)
block|{
return|return
operator|(
name|ScalarResultMetadata
operator|)
name|result
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Segment at position "
operator|+
name|position
operator|+
literal|" is not a scalar segment"
argument_list|)
throw|;
block|}
specifier|public
name|int
index|[]
name|getScalarSegments
parameter_list|()
block|{
return|return
name|scalarSegments
return|;
block|}
specifier|public
name|int
name|getSegmentsCount
parameter_list|()
block|{
return|return
name|segments
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

