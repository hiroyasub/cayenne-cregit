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
name|ResultIterator
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
name|query
operator|.
name|Query
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
name|QueryRouter
import|;
end_import

begin_comment
comment|/**  * DataDomain query action that relies on externally provided OperationObserver to process  * the results.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DataDomainLegacyQueryAction
implements|implements
name|QueryRouter
implements|,
name|OperationObserver
block|{
specifier|static
specifier|final
name|boolean
name|DONE
init|=
literal|true
decl_stmt|;
name|DataDomain
name|domain
decl_stmt|;
name|OperationObserver
name|callback
decl_stmt|;
name|Query
name|query
decl_stmt|;
name|QueryMetadata
name|metadata
decl_stmt|;
name|Map
argument_list|<
name|QueryEngine
argument_list|,
name|List
argument_list|<
name|Query
argument_list|>
argument_list|>
name|queriesByNode
decl_stmt|;
name|Map
argument_list|<
name|Query
argument_list|,
name|Query
argument_list|>
name|queriesByExecutedQueries
decl_stmt|;
name|DataDomainLegacyQueryAction
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|Query
name|query
parameter_list|,
name|OperationObserver
name|callback
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|metadata
operator|=
name|query
operator|.
name|getMetaData
argument_list|(
name|domain
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
comment|/*      * Gets response from the underlying DataNodes.      */
specifier|final
name|void
name|execute
parameter_list|()
block|{
comment|// reset
name|queriesByNode
operator|=
literal|null
expr_stmt|;
name|queriesByExecutedQueries
operator|=
literal|null
expr_stmt|;
comment|// categorize queries by node and by "executable" query...
name|query
operator|.
name|route
argument_list|(
name|this
argument_list|,
name|domain
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// run categorized queries
if|if
condition|(
name|queriesByNode
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|QueryEngine
argument_list|,
name|List
argument_list|<
name|Query
argument_list|>
argument_list|>
name|entry
range|:
name|queriesByNode
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|QueryEngine
name|nextNode
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Query
argument_list|>
name|nodeQueries
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|nextNode
operator|.
name|performQueries
argument_list|(
name|nodeQueries
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|route
parameter_list|(
name|QueryEngine
name|engine
parameter_list|,
name|Query
name|query
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|List
argument_list|<
name|Query
argument_list|>
name|queries
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|queriesByNode
operator|==
literal|null
condition|)
block|{
name|queriesByNode
operator|=
operator|new
name|HashMap
argument_list|<
name|QueryEngine
argument_list|,
name|List
argument_list|<
name|Query
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|queries
operator|=
name|queriesByNode
operator|.
name|get
argument_list|(
name|engine
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|queries
operator|==
literal|null
condition|)
block|{
name|queries
operator|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|queriesByNode
operator|.
name|put
argument_list|(
name|engine
argument_list|,
name|queries
argument_list|)
expr_stmt|;
block|}
name|queries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// handle case when routing resuled in an "exectable" query different from the
comment|// original query.
if|if
condition|(
name|substitutedQuery
operator|!=
literal|null
operator|&&
name|substitutedQuery
operator|!=
name|query
condition|)
block|{
if|if
condition|(
name|queriesByExecutedQueries
operator|==
literal|null
condition|)
block|{
name|queriesByExecutedQueries
operator|=
operator|new
name|HashMap
argument_list|<
name|Query
argument_list|,
name|Query
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|queriesByExecutedQueries
operator|.
name|put
argument_list|(
name|query
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|QueryEngine
name|engineForDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null DataMap, can't determine DataNode."
argument_list|)
throw|;
block|}
name|QueryEngine
name|node
init|=
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No DataNode exists for DataMap "
operator|+
name|map
argument_list|)
throw|;
block|}
return|return
name|node
return|;
block|}
specifier|public
name|void
name|nextCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
name|resultCount
parameter_list|)
block|{
name|callback
operator|.
name|nextCount
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|resultCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextBatchCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
index|[]
name|resultCount
parameter_list|)
block|{
name|callback
operator|.
name|nextBatchCount
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|resultCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|dataRows
parameter_list|)
block|{
name|callback
operator|.
name|nextRows
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|dataRows
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
name|callback
operator|.
name|nextRows
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|q
argument_list|)
argument_list|,
name|it
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextGeneratedRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
name|callback
operator|.
name|nextGeneratedRows
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|keysIterator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextQueryException
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|callback
operator|.
name|nextQueryException
argument_list|(
name|queryForExecutedQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|callback
operator|.
name|nextGlobalException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
name|callback
operator|.
name|isIteratedResult
argument_list|()
return|;
block|}
name|Query
name|queryForExecutedQuery
parameter_list|(
name|Query
name|executedQuery
parameter_list|)
block|{
name|Query
name|q
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|queriesByExecutedQueries
operator|!=
literal|null
condition|)
block|{
name|q
operator|=
name|queriesByExecutedQueries
operator|.
name|get
argument_list|(
name|executedQuery
argument_list|)
expr_stmt|;
block|}
return|return
name|q
operator|!=
literal|null
condition|?
name|q
else|:
name|executedQuery
return|;
block|}
block|}
end_class

end_unit
