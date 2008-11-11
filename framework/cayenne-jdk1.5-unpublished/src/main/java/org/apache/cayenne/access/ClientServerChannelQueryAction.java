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
name|ObjectId
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
name|Persistent
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
name|query
operator|.
name|PrefetchTreeNode
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
name|reflect
operator|.
name|ClassDescriptor
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
name|GenericResponse
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
name|IncrementalListResponse
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
name|ListResponse
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
name|ObjectDetachOperation
import|;
end_import

begin_comment
comment|/**  * A query handler used by ClientServerChannel.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|ClientServerChannelQueryAction
block|{
specifier|static
specifier|final
name|boolean
name|DONE
init|=
literal|true
decl_stmt|;
specifier|private
specifier|final
name|ClientServerChannel
name|channel
decl_stmt|;
specifier|private
name|Query
name|serverQuery
decl_stmt|;
specifier|private
name|QueryResponse
name|response
decl_stmt|;
specifier|private
specifier|final
name|QueryMetadata
name|serverMetadata
decl_stmt|;
specifier|private
specifier|final
name|EntityResolver
name|serverResolver
decl_stmt|;
name|ClientServerChannelQueryAction
parameter_list|(
name|ClientServerChannel
name|channel
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
name|this
operator|.
name|serverResolver
operator|=
name|channel
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
name|this
operator|.
name|serverQuery
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|serverMetadata
operator|=
name|serverQuery
operator|.
name|getMetaData
argument_list|(
name|serverResolver
argument_list|)
expr_stmt|;
block|}
name|QueryResponse
name|execute
parameter_list|()
block|{
if|if
condition|(
name|interceptSinglePageQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|runQuery
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|interceptIncrementalListConversion
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|interceptObjectConversion
argument_list|()
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|private
name|boolean
name|interceptSinglePageQuery
parameter_list|()
block|{
comment|// retrieve range from the previously cached list
if|if
condition|(
name|serverMetadata
operator|.
name|getFetchOffset
argument_list|()
operator|>=
literal|0
operator|&&
name|serverMetadata
operator|.
name|getFetchLimit
argument_list|()
operator|>
literal|0
operator|&&
name|serverMetadata
operator|.
name|getCacheKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
name|cachedList
init|=
name|channel
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|serverMetadata
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedList
operator|==
literal|null
condition|)
block|{
comment|// attempt to refetch... respawn the action...
name|Query
name|originatingQuery
init|=
name|serverMetadata
operator|.
name|getOrginatingQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|originatingQuery
operator|!=
literal|null
condition|)
block|{
name|ClientServerChannelQueryAction
name|subaction
init|=
operator|new
name|ClientServerChannelQueryAction
argument_list|(
name|channel
argument_list|,
name|originatingQuery
argument_list|)
decl_stmt|;
name|subaction
operator|.
name|execute
argument_list|()
expr_stmt|;
name|cachedList
operator|=
name|channel
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|serverMetadata
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cachedList
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No cached list for "
operator|+
name|serverMetadata
operator|.
name|getCacheKey
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|int
name|startIndex
init|=
name|serverMetadata
operator|.
name|getFetchOffset
argument_list|()
decl_stmt|;
name|int
name|endIndex
init|=
name|startIndex
operator|+
name|serverMetadata
operator|.
name|getFetchLimit
argument_list|()
decl_stmt|;
comment|// send back just one page... query sender will figure out where it fits in
comment|// the incremental list
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|cachedList
operator|.
name|subList
argument_list|(
name|startIndex
argument_list|,
name|endIndex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
return|return
operator|!
name|DONE
return|;
block|}
specifier|private
name|void
name|runQuery
parameter_list|()
block|{
name|this
operator|.
name|response
operator|=
name|channel
operator|.
name|getParentChannel
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|serverQuery
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|interceptIncrementalListConversion
parameter_list|()
block|{
name|int
name|pageSize
init|=
name|serverMetadata
operator|.
name|getPageSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|pageSize
operator|>
literal|0
operator|&&
name|serverMetadata
operator|.
name|getCacheKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
name|list
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|>
name|pageSize
operator|&&
name|list
operator|instanceof
name|IncrementalFaultList
condition|)
block|{
comment|// cache
name|channel
operator|.
name|getQueryCache
argument_list|()
operator|.
name|put
argument_list|(
name|serverMetadata
argument_list|,
name|list
argument_list|)
expr_stmt|;
comment|// extract and convert first page
comment|// TODO: andrus, 2008/03/05 - we no longer resolve the first page
comment|// automatically on the server... probably should not do it for the client
comment|// either... One rare case when this is completely undesirable is
comment|// subaction execution from 'interceptSinglePageQuery', as it doesn't even
comment|// care about the first page...
name|List
name|sublist
init|=
name|list
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|pageSize
argument_list|)
decl_stmt|;
name|List
name|firstPage
init|=
operator|(
name|serverMetadata
operator|.
name|isFetchingDataRows
argument_list|()
operator|)
condition|?
operator|new
name|ArrayList
argument_list|(
name|sublist
argument_list|)
else|:
name|toClientObjects
argument_list|(
name|sublist
argument_list|)
decl_stmt|;
name|this
operator|.
name|response
operator|=
operator|new
name|IncrementalListResponse
argument_list|(
name|firstPage
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
block|}
return|return
operator|!
name|DONE
return|;
block|}
specifier|private
name|void
name|interceptObjectConversion
parameter_list|()
block|{
if|if
condition|(
operator|!
name|serverMetadata
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
name|GenericResponse
name|clientResponse
init|=
operator|new
name|GenericResponse
argument_list|()
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
name|List
name|serverObjects
init|=
name|response
operator|.
name|currentList
argument_list|()
decl_stmt|;
name|clientResponse
operator|.
name|addResultList
argument_list|(
name|toClientObjects
argument_list|(
name|serverObjects
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientResponse
operator|.
name|addBatchUpdateCount
argument_list|(
name|response
operator|.
name|currentUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|response
operator|=
name|clientResponse
expr_stmt|;
block|}
block|}
specifier|private
name|List
name|toClientObjects
parameter_list|(
name|List
name|serverObjects
parameter_list|)
block|{
comment|// create a list copy even if it is empty to ensure that we have a
comment|// clean serializable list...
name|List
name|clientObjects
init|=
operator|new
name|ArrayList
argument_list|(
name|serverObjects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|serverObjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ObjectDetachOperation
name|op
init|=
operator|new
name|ObjectDetachOperation
argument_list|(
name|serverResolver
operator|.
name|getClientEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|serverObjects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|prefetchTree
init|=
name|serverMetadata
operator|.
name|getPrefetchTree
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
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ObjectId
name|id
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Server returned an object without an id: "
operator|+
name|object
argument_list|)
throw|;
block|}
comment|// have to resolve descriptor here for every object, as
comment|// often a query will not have any info indicating the
comment|// entity type
name|ClassDescriptor
name|serverDescriptor
init|=
name|serverResolver
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|clientObjects
operator|.
name|add
argument_list|(
name|op
operator|.
name|detach
argument_list|(
name|object
argument_list|,
name|serverDescriptor
argument_list|,
name|prefetchTree
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|clientObjects
return|;
block|}
block|}
end_class

end_unit

