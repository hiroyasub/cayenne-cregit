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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|CayenneException
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
name|access
operator|.
name|util
operator|.
name|IteratedSelectObserver
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
name|ashwood
operator|.
name|AshwoodEntitySorter
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
name|DbEntity
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
name|EntitySorter
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
name|InsertBatchQuery
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
name|SQLTemplate
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * An engine to port data between two DataNodes. These nodes can potentially  * connect to databases from different vendors. The only assumption is that all  * of the DbEntities (tables) being ported are present in both source and  * destination databases and are adequately described by Cayenne mapping.  *<p>  * DataPort implements a Cayenne-based algorithm to read data from source  * DataNode and write to destination DataNode. It uses DataPortDelegate  * interface to externalize various things, such as determining what entities to  * port (include/exclude from port based on some criteria), logging the progress  * of port operation, qualifying the queries, etc.  *</p>  *   * @since 1.2: Prior to 1.2 DataPort classes were a part of cayenne-examples  *        package.  * @deprecated since 4.0  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|DataPort
block|{
specifier|public
specifier|static
specifier|final
name|int
name|INSERT_BATCH_SIZE
init|=
literal|1000
decl_stmt|;
specifier|protected
name|DataNode
name|sourceNode
decl_stmt|;
specifier|protected
name|DataNode
name|destinationNode
decl_stmt|;
specifier|protected
name|Collection
name|entities
decl_stmt|;
specifier|protected
name|boolean
name|cleaningDestination
decl_stmt|;
specifier|protected
name|DataPortDelegate
name|delegate
decl_stmt|;
specifier|protected
name|int
name|insertBatchSize
decl_stmt|;
specifier|public
name|DataPort
parameter_list|()
block|{
name|this
operator|.
name|insertBatchSize
operator|=
name|INSERT_BATCH_SIZE
expr_stmt|;
block|}
comment|/**      * Creates a new DataPort instance, setting its delegate.      */
specifier|public
name|DataPort
parameter_list|(
name|DataPortDelegate
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
comment|/**      * Runs DataPort. The instance must be fully configured by the time this      * method is invoked, having its delegate, source and destinatio nodes, and      * a list of entities set up.      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|CayenneException
block|{
comment|// sanity check
if|if
condition|(
name|sourceNode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Can't port data, source node is null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|destinationNode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Can't port data, destination node is null."
argument_list|)
throw|;
block|}
comment|// the simple equality check may actually detect problems with
comment|// misconfigred nodes
comment|// it is not as dumb as it may look at first
if|if
condition|(
name|sourceNode
operator|==
name|destinationNode
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Can't port data, source and target nodes are the same."
argument_list|)
throw|;
block|}
if|if
condition|(
name|entities
operator|==
literal|null
operator|||
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// sort entities for insertion
name|List
name|sorted
init|=
operator|new
name|ArrayList
argument_list|(
name|entities
argument_list|)
decl_stmt|;
name|EntitySorter
name|sorter
init|=
operator|new
name|AshwoodEntitySorter
argument_list|()
decl_stmt|;
name|sorter
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|destinationNode
operator|.
name|getDataMaps
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sorter
operator|.
name|sortDbEntities
argument_list|(
name|sorted
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|cleaningDestination
condition|)
block|{
comment|// reverse insertion order for deletion
name|List
name|entitiesInDeleteOrder
init|=
operator|new
name|ArrayList
argument_list|(
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|entitiesInDeleteOrder
operator|.
name|addAll
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|entitiesInDeleteOrder
argument_list|)
expr_stmt|;
name|processDelete
argument_list|(
name|entitiesInDeleteOrder
argument_list|)
expr_stmt|;
block|}
name|processInsert
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
block|}
comment|/**      * Cleans up destination tables data.      */
specifier|protected
name|void
name|processDelete
parameter_list|(
name|List
name|entities
parameter_list|)
block|{
comment|// Allow delegate to modify the list of entities
comment|// any way it wants. For instance delegate may filter
comment|// or sort the list (though it doesn't have to, and can simply
comment|// pass through the original list).
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|entities
operator|=
name|delegate
operator|.
name|willCleanData
argument_list|(
name|this
argument_list|,
name|entities
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entities
operator|==
literal|null
operator|||
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// Using QueryResult as observer for the data cleanup.
comment|// This allows to collect query statistics and pass it to the delegate.
name|QueryResult
name|observer
init|=
operator|new
name|QueryResult
argument_list|()
decl_stmt|;
comment|// Delete data from entities one by one
name|Iterator
name|it
init|=
name|entities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
literal|"DELETE FROM "
operator|+
name|entity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
comment|// notify delegate that delete is about to happen
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|query
operator|=
name|delegate
operator|.
name|willCleanData
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
comment|// perform delete query
name|observer
operator|.
name|clear
argument_list|()
expr_stmt|;
name|destinationNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
comment|// notify delegate that delete just happened
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
comment|// observer will store query statistics
name|int
name|count
init|=
name|observer
operator|.
name|getFirstUpdateCount
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|delegate
operator|.
name|didCleanData
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Reads source data from source, saving it to destination.      */
specifier|protected
name|void
name|processInsert
parameter_list|(
name|List
name|entities
parameter_list|)
throws|throws
name|CayenneException
block|{
comment|// Allow delegate to modify the list of entities
comment|// any way it wants. For instance delegate may filter
comment|// or sort the list (though it doesn't have to, and can simply
comment|// pass through the original list).
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|entities
operator|=
name|delegate
operator|.
name|willCleanData
argument_list|(
name|this
argument_list|,
name|entities
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entities
operator|==
literal|null
operator|||
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// Create an observer for to get the iterated result
comment|// instead of getting each table as a list
name|IteratedSelectObserver
name|observer
init|=
operator|new
name|IteratedSelectObserver
argument_list|()
decl_stmt|;
comment|// Using QueryResult as observer for the data insert.
comment|// This allows to collect query statistics and pass it to the delegate.
name|QueryResult
name|insertObserver
init|=
operator|new
name|QueryResult
argument_list|()
decl_stmt|;
comment|// process ordered list of entities one by one
name|Iterator
name|it
init|=
name|entities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|insertObserver
operator|.
name|clear
argument_list|()
expr_stmt|;
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|select
init|=
operator|new
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// delegate is allowed to substitute query
name|Query
name|query
init|=
operator|(
name|delegate
operator|!=
literal|null
operator|)
condition|?
name|delegate
operator|.
name|willPortEntity
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|select
argument_list|)
else|:
name|select
decl_stmt|;
name|sourceNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|ResultIterator
name|result
init|=
name|observer
operator|.
name|getResultIterator
argument_list|()
decl_stmt|;
name|InsertBatchQuery
name|insert
init|=
operator|new
name|InsertBatchQuery
argument_list|(
name|entity
argument_list|,
name|INSERT_BATCH_SIZE
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Split insertions into the same table into batches.
comment|// This will allow to process tables of arbitrary size
comment|// and not run out of memory.
name|int
name|currentRow
init|=
literal|0
decl_stmt|;
comment|// even if we don't use intermediate batch commits, we still
comment|// need to
comment|// estimate batch insert size
name|int
name|batchSize
init|=
name|insertBatchSize
operator|>
literal|0
condition|?
name|insertBatchSize
else|:
name|INSERT_BATCH_SIZE
decl_stmt|;
while|while
condition|(
name|result
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
if|if
condition|(
name|insertBatchSize
operator|>
literal|0
operator|&&
name|currentRow
operator|>
literal|0
operator|&&
name|currentRow
operator|%
name|insertBatchSize
operator|==
literal|0
condition|)
block|{
comment|// end of the batch detected... commit and start a new
comment|// insert
comment|// query
name|destinationNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|insert
argument_list|)
argument_list|,
name|insertObserver
argument_list|)
expr_stmt|;
name|insert
operator|=
operator|new
name|InsertBatchQuery
argument_list|(
name|entity
argument_list|,
name|batchSize
argument_list|)
expr_stmt|;
name|insertObserver
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|currentRow
operator|++
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|nextRow
init|=
operator|(
name|DataRow
operator|)
name|result
operator|.
name|nextRow
argument_list|()
decl_stmt|;
name|insert
operator|.
name|add
argument_list|(
name|nextRow
argument_list|)
expr_stmt|;
block|}
comment|// commit remaining batch if needed
if|if
condition|(
name|insert
operator|.
name|getRows
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|destinationNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|insert
argument_list|)
argument_list|,
name|insertObserver
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|didPortEntity
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|currentRow
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// don't forget to close ResultIterator
name|result
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Collection
name|getEntities
parameter_list|()
block|{
return|return
name|entities
return|;
block|}
specifier|public
name|DataNode
name|getSourceNode
parameter_list|()
block|{
return|return
name|sourceNode
return|;
block|}
specifier|public
name|DataNode
name|getDestinationNode
parameter_list|()
block|{
return|return
name|destinationNode
return|;
block|}
comment|/**      * Sets the initial list of entities to process. This list can be later      * modified by the delegate.      */
specifier|public
name|void
name|setEntities
parameter_list|(
name|Collection
name|entities
parameter_list|)
block|{
name|this
operator|.
name|entities
operator|=
name|entities
expr_stmt|;
block|}
comment|/**      * Sets the DataNode serving as a source of the ported data.      */
specifier|public
name|void
name|setSourceNode
parameter_list|(
name|DataNode
name|sourceNode
parameter_list|)
block|{
name|this
operator|.
name|sourceNode
operator|=
name|sourceNode
expr_stmt|;
block|}
comment|/**      * Sets the DataNode serving as a destination of the ported data.      */
specifier|public
name|void
name|setDestinationNode
parameter_list|(
name|DataNode
name|destinationNode
parameter_list|)
block|{
name|this
operator|.
name|destinationNode
operator|=
name|destinationNode
expr_stmt|;
block|}
comment|/**      * Returns previously initialized DataPortDelegate object.      */
specifier|public
name|DataPortDelegate
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
specifier|public
name|void
name|setDelegate
parameter_list|(
name|DataPortDelegate
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
comment|/**      * Returns true if a DataPort was configured to delete all data from the      * destination tables.      */
specifier|public
name|boolean
name|isCleaningDestination
parameter_list|()
block|{
return|return
name|cleaningDestination
return|;
block|}
comment|/**      * Defines whether DataPort should delete all data from destination tables      * before doing the port.      */
specifier|public
name|void
name|setCleaningDestination
parameter_list|(
name|boolean
name|cleaningDestination
parameter_list|)
block|{
name|this
operator|.
name|cleaningDestination
operator|=
name|cleaningDestination
expr_stmt|;
block|}
specifier|public
name|int
name|getInsertBatchSize
parameter_list|()
block|{
return|return
name|insertBatchSize
return|;
block|}
comment|/**      * Sets a parameter used for tuning insert batches. If set to a value      * greater than zero, DataPort will commit every N rows. If set to value      * less or equal to zero, DataPort will commit only once at the end of the      * insert.      */
specifier|public
name|void
name|setInsertBatchSize
parameter_list|(
name|int
name|insertBatchSize
parameter_list|)
block|{
name|this
operator|.
name|insertBatchSize
operator|=
name|insertBatchSize
expr_stmt|;
block|}
block|}
end_class

end_unit

