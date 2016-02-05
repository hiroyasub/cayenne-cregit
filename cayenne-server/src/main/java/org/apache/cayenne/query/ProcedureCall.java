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
name|ObjectContext
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
name|ProcedureResult
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
name|QueryResponse
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
name|util
operator|.
name|QueryResultBuilder
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

begin_comment
comment|/**  * Fluent API for calling stored procedures.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureCall
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IndirectQuery
block|{
comment|/**      * Creates procedure call using name of stored procedure defined in the mapping file.      */
specifier|public
specifier|static
name|ProcedureCall
name|query
parameter_list|(
name|String
name|procedure
parameter_list|)
block|{
return|return
operator|new
name|ProcedureCall
argument_list|(
name|procedure
argument_list|)
return|;
block|}
comment|/**      * Creates procedure call returning data rows using name of stored procedure defined in the mapping file.      */
specifier|public
specifier|static
name|ProcedureCall
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|String
name|procedure
parameter_list|)
block|{
name|ProcedureCall
argument_list|<
name|DataRow
argument_list|>
name|procedureCall
init|=
operator|new
name|ProcedureCall
argument_list|<>
argument_list|(
name|procedure
argument_list|)
decl_stmt|;
name|procedureCall
operator|.
name|fetchingDataRows
operator|=
literal|true
expr_stmt|;
return|return
name|procedureCall
return|;
block|}
comment|/**      * Creates procedure call using name of stored procedure defined in the mapping file and specifies data type      * of the objects it should return.      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|String
name|procedure
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resultClass
parameter_list|)
block|{
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|procedureCall
init|=
operator|new
name|ProcedureCall
argument_list|<>
argument_list|(
name|procedure
argument_list|,
name|resultClass
argument_list|)
decl_stmt|;
name|procedureCall
operator|.
name|fetchingDataRows
operator|=
literal|false
expr_stmt|;
return|return
name|procedureCall
return|;
block|}
specifier|protected
name|String
name|procedureName
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|T
argument_list|>
name|resultClass
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
decl_stmt|;
specifier|protected
name|Integer
name|fetchLimit
decl_stmt|;
specifier|protected
name|Integer
name|fetchOffset
decl_stmt|;
specifier|protected
name|CapsStrategy
name|capsStrategy
decl_stmt|;
specifier|protected
name|Boolean
name|fetchingDataRows
decl_stmt|;
specifier|protected
name|ColumnDescriptor
index|[]
name|resultDescriptor
decl_stmt|;
specifier|public
name|ProcedureCall
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|procedureName
expr_stmt|;
block|}
specifier|public
name|ProcedureCall
parameter_list|(
name|String
name|procedureName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resultClass
parameter_list|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|procedureName
expr_stmt|;
name|this
operator|.
name|resultClass
operator|=
name|resultClass
expr_stmt|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|params
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|params
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|params
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
name|bareMap
init|=
name|parameters
decl_stmt|;
name|this
operator|.
name|params
operator|.
name|putAll
argument_list|(
name|bareMap
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|param
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|params
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|params
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|limit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
name|this
operator|.
name|fetchLimit
operator|=
name|fetchLimit
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|offset
parameter_list|(
name|int
name|fetchOffset
parameter_list|)
block|{
name|this
operator|.
name|fetchOffset
operator|=
name|fetchOffset
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|capsStrategy
parameter_list|(
name|CapsStrategy
name|capsStrategy
parameter_list|)
block|{
name|this
operator|.
name|capsStrategy
operator|=
name|capsStrategy
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|resultDescriptor
parameter_list|(
name|ColumnDescriptor
index|[]
name|resultDescriptor
parameter_list|)
block|{
name|this
operator|.
name|resultDescriptor
operator|=
name|resultDescriptor
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProcedureResult
argument_list|<
name|T
argument_list|>
name|call
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|QueryResponse
name|response
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|QueryResultBuilder
name|builder
init|=
name|QueryResultBuilder
operator|.
name|builder
argument_list|(
name|response
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|response
operator|.
name|reset
argument_list|()
init|;
name|response
operator|.
name|next
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|response
operator|.
name|isList
argument_list|()
condition|)
block|{
name|builder
operator|.
name|addSelectResult
argument_list|(
name|response
operator|.
name|currentList
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|addBatchUpdateResult
argument_list|(
name|response
operator|.
name|currentUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ProcedureResult
argument_list|<>
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|resultClass
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|call
argument_list|(
name|context
argument_list|)
operator|.
name|getSelectResult
argument_list|()
return|;
block|}
specifier|public
name|int
index|[]
name|update
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|call
argument_list|(
name|context
argument_list|)
operator|.
name|getUpdateResult
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|ProcedureQuery
name|procedureQuery
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|procedureName
argument_list|)
decl_stmt|;
if|if
condition|(
name|fetchingDataRows
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|setFetchingDataRows
argument_list|(
name|fetchingDataRows
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultClass
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|resultClass
operator|=
name|this
operator|.
name|resultClass
expr_stmt|;
block|}
if|if
condition|(
name|fetchLimit
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|setFetchLimit
argument_list|(
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fetchOffset
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|setFetchOffset
argument_list|(
name|fetchOffset
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultDescriptor
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|addResultDescriptor
argument_list|(
name|resultDescriptor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|capsStrategy
operator|!=
literal|null
condition|)
block|{
name|procedureQuery
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|capsStrategy
argument_list|)
expr_stmt|;
block|}
name|procedureQuery
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|procedureQuery
return|;
block|}
block|}
end_class

end_unit

