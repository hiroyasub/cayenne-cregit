begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * https://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|QueryResult
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
comment|/**  * A query that represents a named parameterized non selecting query stored in the mapping. The  * actual query is resolved during execution.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|MappedExec
extends|extends
name|AbstractMappedQuery
block|{
specifier|public
specifier|static
name|MappedExec
name|query
parameter_list|(
name|String
name|queryName
parameter_list|)
block|{
return|return
operator|new
name|MappedExec
argument_list|(
name|queryName
argument_list|)
return|;
block|}
specifier|protected
name|MappedExec
parameter_list|(
name|String
name|queryName
parameter_list|)
block|{
name|super
argument_list|(
name|queryName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MappedExec
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
return|return
operator|(
name|MappedExec
operator|)
name|super
operator|.
name|params
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MappedExec
name|param
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
operator|(
name|MappedExec
operator|)
name|super
operator|.
name|param
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|QueryResult
name|execute
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// TODO: switch ObjectContext to QueryResult instead of QueryResponse
comment|// and create its own 'exec' method
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
name|builder
operator|.
name|build
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
name|context
operator|.
name|performGenericQuery
argument_list|(
name|this
argument_list|)
operator|.
name|firstUpdateCount
argument_list|()
return|;
block|}
block|}
end_class

end_unit

