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
name|Arrays
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
name|map
operator|.
name|DataMap
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

begin_comment
comment|/**  * A Query decorator for a collection of other queries. Note that QueryChain will always  * return DataRows (that is if it returns data), as it has no way of knowing how to  * convert the results to objects.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|QueryChain
implements|implements
name|Query
block|{
specifier|protected
name|Collection
argument_list|<
name|Query
argument_list|>
name|chain
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
comment|/**      * @since 3.1      */
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
comment|/**      * Creates an empty QueryChain.      */
specifier|public
name|QueryChain
parameter_list|()
block|{
block|}
comment|/**      * Creates a new QueryChain out of an array of queries.      */
specifier|public
name|QueryChain
parameter_list|(
name|Query
index|[]
name|queries
parameter_list|)
block|{
if|if
condition|(
name|queries
operator|!=
literal|null
operator|&&
name|queries
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|chain
operator|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|queries
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a new QueryChain with a collection of Queries.      */
specifier|public
name|QueryChain
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Query
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
name|queries
operator|!=
literal|null
operator|&&
operator|!
name|queries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|chain
operator|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
name|queries
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds a query to the chain.      */
specifier|public
name|void
name|addQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
if|if
condition|(
name|chain
operator|==
literal|null
condition|)
block|{
name|chain
operator|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a query from the chain, returning true if the query was indeed present in      * the chain and was removed.      */
specifier|public
name|boolean
name|removeQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
operator|(
name|chain
operator|!=
literal|null
operator|)
condition|?
name|chain
operator|.
name|remove
argument_list|(
name|query
argument_list|)
else|:
literal|false
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|chain
operator|==
literal|null
operator|||
name|chain
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Delegates routing to each individual query in the chain. If there is no queries,      * this method does nothing.      */
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
if|if
condition|(
name|chain
operator|!=
literal|null
operator|&&
operator|!
name|chain
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Query
name|q
range|:
name|chain
control|)
block|{
name|q
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Throws an exception as execution should've been delegated to the queries contained      * in the chain.      */
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Chain doesn't support its own execution "
operator|+
literal|"and should've been split into separate queries during routing phase."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/**      * Returns default metadata.      */
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|QueryMetadataWrapper
name|wrapper
init|=
operator|new
name|QueryMetadataWrapper
argument_list|(
name|DefaultQueryMetadata
operator|.
name|defaultMetadata
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|wrapper
return|;
block|}
block|}
end_class

end_unit

