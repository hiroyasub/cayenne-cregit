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
name|HashMap
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
name|cache
operator|.
name|QueryCache
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
name|cache
operator|.
name|QueryCacheEntryFactory
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
name|DbRelationship
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
name|map
operator|.
name|ObjRelationship
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
name|ObjectIdQuery
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
name|PrefetchSelectQuery
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
name|query
operator|.
name|QueryRouter
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
name|RefreshQuery
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
name|RelationshipQuery
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
name|SQLResultSetMapping
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
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Performs query routing and execution. During execution phase intercepts callbacks to  * the OperationObserver, remapping results to the original pre-routed queries.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|DataDomainQueryAction
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
name|DataContext
name|context
decl_stmt|;
name|DataDomain
name|domain
decl_stmt|;
name|DataRowStore
name|cache
decl_stmt|;
name|Query
name|query
decl_stmt|;
name|QueryMetadata
name|metadata
decl_stmt|;
name|QueryResponse
name|response
decl_stmt|;
name|GenericResponse
name|fullResponse
decl_stmt|;
name|Map
name|prefetchResultsByPath
decl_stmt|;
name|Map
name|queriesByNode
decl_stmt|;
name|Map
name|queriesByExecutedQueries
decl_stmt|;
name|boolean
name|noObjectConversion
decl_stmt|;
comment|/*      * A constructor for the "new" way of performing a query via 'execute' with      * QueryResponse created internally.      */
name|DataDomainQueryAction
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|DataDomain
name|domain
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|context
operator|instanceof
name|DataContext
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DataDomain can only work with DataContext. "
operator|+
literal|"Unsupported context type: "
operator|+
name|context
argument_list|)
throw|;
block|}
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
name|context
operator|=
operator|(
name|DataContext
operator|)
name|context
expr_stmt|;
comment|// cache may be shared or unique for the ObjectContext
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|cache
operator|=
name|this
operator|.
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|cache
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|cache
operator|=
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
expr_stmt|;
block|}
block|}
name|QueryResponse
name|execute
parameter_list|()
block|{
comment|// run chain...
if|if
condition|(
name|interceptOIDQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
if|if
condition|(
name|interceptRelationshipQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
if|if
condition|(
name|interceptRefreshQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
if|if
condition|(
name|interceptSharedCache
argument_list|()
operator|!=
name|DONE
condition|)
block|{
if|if
condition|(
name|interceptDataDomainQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|runQueryInTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|noObjectConversion
condition|)
block|{
if|if
condition|(
name|interceptMappedConversion
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|interceptObjectConversion
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|response
return|;
block|}
specifier|private
name|boolean
name|interceptDataDomainQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|instanceof
name|DataDomainQuery
condition|)
block|{
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|domain
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
name|boolean
name|interceptOIDQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|instanceof
name|ObjectIdQuery
condition|)
block|{
name|ObjectIdQuery
name|oidQuery
init|=
operator|(
name|ObjectIdQuery
operator|)
name|query
decl_stmt|;
name|DataRow
name|row
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|oidQuery
operator|.
name|isFetchMandatory
argument_list|()
condition|)
block|{
name|row
operator|=
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|oidQuery
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// refresh is forced or not found in cache
if|if
condition|(
name|row
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|oidQuery
operator|.
name|isFetchAllowed
argument_list|()
condition|)
block|{
name|runQueryInTransaction
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
operator|new
name|ListResponse
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|interceptRelationshipQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|instanceof
name|RelationshipQuery
condition|)
block|{
name|RelationshipQuery
name|relationshipQuery
init|=
operator|(
name|RelationshipQuery
operator|)
name|query
decl_stmt|;
if|if
condition|(
name|relationshipQuery
operator|.
name|isRefreshing
argument_list|()
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|ObjRelationship
name|relationship
init|=
name|relationshipQuery
operator|.
name|getRelationship
argument_list|(
name|domain
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
comment|// check if we can derive target PK from FK... this implies that the
comment|// relationship is to-one
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|DataRow
name|sourceRow
init|=
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|relationshipQuery
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceRow
operator|==
literal|null
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
comment|// we can assume that there is one and only one DbRelationship as
comment|// we previously checked that
comment|// "!isSourceIndependentFromTargetChange"
name|DbRelationship
name|dbRelationship
init|=
operator|(
name|DbRelationship
operator|)
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjectId
name|targetId
init|=
name|sourceRow
operator|.
name|createTargetObjectId
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|,
name|dbRelationship
argument_list|)
decl_stmt|;
comment|// null id means that FK is null...
if|if
condition|(
name|targetId
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|response
operator|=
operator|new
name|GenericResponse
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
name|DataRow
name|targetRow
init|=
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|targetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetRow
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|response
operator|=
operator|new
name|GenericResponse
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|targetRow
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
comment|// a hack to prevent passing partial snapshots to ObjectResolver ... See
comment|// CAY-724 for details.
if|else if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|domain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupInheritanceTree
argument_list|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|noObjectConversion
operator|=
literal|true
expr_stmt|;
name|Object
name|object
init|=
name|context
operator|.
name|localObject
argument_list|(
name|targetId
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|this
operator|.
name|response
operator|=
operator|new
name|GenericResponse
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|object
argument_list|)
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
comment|/**      * @since 3.0      */
specifier|private
name|boolean
name|interceptRefreshQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|instanceof
name|RefreshQuery
condition|)
block|{
name|RefreshQuery
name|refreshQuery
init|=
operator|(
name|RefreshQuery
operator|)
name|query
decl_stmt|;
if|if
condition|(
name|refreshQuery
operator|.
name|isRefreshAll
argument_list|()
condition|)
block|{
comment|// not sending any events - peer contexts will not get refreshed
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|GenericResponse
name|response
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|addUpdateCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
return|return
name|DONE
return|;
block|}
name|Collection
name|objects
init|=
name|refreshQuery
operator|.
name|getObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|objects
operator|!=
literal|null
operator|&&
operator|!
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
name|ids
init|=
operator|new
name|ArrayList
argument_list|(
name|objects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|objects
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
name|ids
operator|.
name|add
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// send an event for removed snapshots
name|domain
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|processSnapshotChanges
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|ids
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
block|}
name|GenericResponse
name|response
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|addUpdateCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
return|return
name|DONE
return|;
block|}
comment|// 3. refresh query - this shouldn't normally happen as child datacontext
comment|// usually does a cascading refresh
if|if
condition|(
name|refreshQuery
operator|.
name|getQuery
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Query
name|cachedQuery
init|=
name|refreshQuery
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|String
name|cacheKey
init|=
name|cachedQuery
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getCacheKey
argument_list|()
decl_stmt|;
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|remove
argument_list|(
name|cacheKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|domain
operator|.
name|onQuery
argument_list|(
name|context
argument_list|,
name|cachedQuery
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
comment|// 4. refresh groups...
if|if
condition|(
name|refreshQuery
operator|.
name|getGroupKeys
argument_list|()
operator|!=
literal|null
operator|&&
name|refreshQuery
operator|.
name|getGroupKeys
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|String
index|[]
name|groups
init|=
name|refreshQuery
operator|.
name|getGroupKeys
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
name|groups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|domain
operator|.
name|getQueryCache
argument_list|()
operator|.
name|removeGroup
argument_list|(
name|groups
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|GenericResponse
name|response
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|addUpdateCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
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
comment|/*      * Wraps execution in shared cache checks      */
specifier|private
specifier|final
name|boolean
name|interceptSharedCache
parameter_list|()
block|{
if|if
condition|(
name|metadata
operator|.
name|getCacheKey
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|boolean
name|cache
init|=
name|QueryMetadata
operator|.
name|SHARED_CACHE
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getCachePolicy
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|cacheOrCacheRefresh
init|=
name|cache
operator|||
name|QueryMetadata
operator|.
name|SHARED_CACHE_REFRESH
operator|.
name|equals
argument_list|(
name|metadata
operator|.
name|getCachePolicy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cacheOrCacheRefresh
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|QueryCache
name|queryCache
init|=
name|domain
operator|.
name|getQueryCache
argument_list|()
decl_stmt|;
name|QueryCacheEntryFactory
name|factory
init|=
name|getCacheObjectFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|cache
condition|)
block|{
name|List
name|cachedResults
init|=
name|queryCache
operator|.
name|get
argument_list|(
name|metadata
argument_list|,
name|factory
argument_list|)
decl_stmt|;
comment|// response may already be initialized by the factory above ... it is null if
comment|// there was a preexisting cache entry
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|cachedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cachedResults
operator|instanceof
name|ListWithPrefetches
condition|)
block|{
name|this
operator|.
name|prefetchResultsByPath
operator|=
operator|(
operator|(
name|ListWithPrefetches
operator|)
name|cachedResults
operator|)
operator|.
name|getPrefetchResultsByPath
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// on cache-refresh request, fetch without blocking and fill the cache
name|queryCache
operator|.
name|put
argument_list|(
name|metadata
argument_list|,
operator|(
name|List
operator|)
name|factory
operator|.
name|createObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|DONE
return|;
block|}
specifier|private
name|QueryCacheEntryFactory
name|getCacheObjectFactory
parameter_list|()
block|{
return|return
operator|new
name|QueryCacheEntryFactory
argument_list|()
block|{
specifier|public
name|Object
name|createObject
parameter_list|()
block|{
name|runQueryInTransaction
argument_list|()
expr_stmt|;
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
operator|!=
literal|null
condition|)
block|{
comment|// make an immutable list to make sure callers don't mess it up
name|list
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|list
argument_list|)
expr_stmt|;
comment|// include prefetches in the cached result
if|if
condition|(
name|prefetchResultsByPath
operator|!=
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ListWithPrefetches
argument_list|(
name|list
argument_list|,
name|prefetchResultsByPath
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
block|}
return|;
block|}
comment|/*      * Gets response from the underlying DataNodes.      */
name|void
name|runQueryInTransaction
parameter_list|()
block|{
name|domain
operator|.
name|runInTransaction
argument_list|(
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
name|runQuery
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runQuery
parameter_list|()
block|{
comment|// reset
name|this
operator|.
name|fullResponse
operator|=
operator|new
name|GenericResponse
argument_list|()
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|this
operator|.
name|fullResponse
expr_stmt|;
name|this
operator|.
name|queriesByNode
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|queriesByExecutedQueries
operator|=
literal|null
expr_stmt|;
comment|// whether this is null or not will driver further decisions on how to process
comment|// prefetched rows
name|this
operator|.
name|prefetchResultsByPath
operator|=
name|metadata
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|metadata
operator|.
name|isFetchingDataRows
argument_list|()
condition|?
operator|new
name|HashMap
argument_list|()
else|:
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
name|Iterator
name|nodeIt
init|=
name|queriesByNode
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodeIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|nodeIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|QueryEngine
name|nextNode
init|=
operator|(
name|QueryEngine
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
name|nodeQueries
init|=
operator|(
name|Collection
operator|)
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
specifier|private
name|void
name|interceptObjectConversion
parameter_list|()
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
operator|!
name|metadata
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
name|List
name|mainRows
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|mainRows
operator|!=
literal|null
operator|&&
operator|!
name|mainRows
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
name|objects
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|metadata
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|prefetchTree
init|=
name|metadata
operator|.
name|getPrefetchTree
argument_list|()
decl_stmt|;
comment|// take a shortcut when no prefetches exist...
if|if
condition|(
name|prefetchTree
operator|==
literal|null
condition|)
block|{
name|objects
operator|=
operator|new
name|ObjectResolver
argument_list|(
name|context
argument_list|,
name|descriptor
argument_list|,
name|metadata
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|,
name|metadata
operator|.
name|isResolvingInherited
argument_list|()
argument_list|)
operator|.
name|synchronizedObjectsFromDataRows
argument_list|(
name|mainRows
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ObjectTreeResolver
name|resolver
init|=
operator|new
name|ObjectTreeResolver
argument_list|(
name|context
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
name|objects
operator|=
name|resolver
operator|.
name|synchronizedObjectsFromDataRows
argument_list|(
name|prefetchTree
argument_list|,
name|mainRows
argument_list|,
name|prefetchResultsByPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|instanceof
name|GenericResponse
condition|)
block|{
operator|(
operator|(
name|GenericResponse
operator|)
name|response
operator|)
operator|.
name|replaceResult
argument_list|(
name|mainRows
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|response
operator|instanceof
name|ListResponse
condition|)
block|{
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown response object: "
operator|+
name|this
operator|.
name|response
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|interceptMappedConversion
parameter_list|()
block|{
name|SQLResultSetMapping
name|rsMapping
init|=
name|metadata
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
operator|!
name|DONE
return|;
block|}
name|List
name|mainRows
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|mainRows
operator|!=
literal|null
operator|&&
operator|!
name|mainRows
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
name|columns
init|=
name|rsMapping
operator|.
name|getColumnResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|columns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid result set mapping, no columns mapped."
argument_list|)
throw|;
block|}
name|Object
index|[]
name|columnsArray
init|=
name|columns
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|int
name|rowsLen
init|=
name|mainRows
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|rowWidth
init|=
name|columnsArray
operator|.
name|length
decl_stmt|;
name|List
name|objects
init|=
operator|new
name|ArrayList
argument_list|(
name|rowsLen
argument_list|)
decl_stmt|;
comment|// add scalars to the result
if|if
condition|(
name|rowWidth
operator|==
literal|1
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rowsLen
condition|;
name|i
operator|++
control|)
block|{
name|Map
name|row
init|=
operator|(
name|Map
operator|)
name|mainRows
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|objects
operator|.
name|add
argument_list|(
name|row
operator|.
name|get
argument_list|(
name|columnsArray
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add Object[]'s to the result
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rowsLen
condition|;
name|i
operator|++
control|)
block|{
name|Map
name|row
init|=
operator|(
name|Map
operator|)
name|mainRows
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
index|[]
name|rowDecoded
init|=
operator|new
name|Object
index|[
name|rowWidth
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|rowWidth
condition|;
name|j
operator|++
control|)
block|{
name|rowDecoded
index|[
name|j
index|]
operator|=
name|row
operator|.
name|get
argument_list|(
name|columnsArray
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
name|objects
operator|.
name|add
argument_list|(
name|rowDecoded
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|response
operator|instanceof
name|GenericResponse
condition|)
block|{
operator|(
operator|(
name|GenericResponse
operator|)
name|response
operator|)
operator|.
name|replaceResult
argument_list|(
name|mainRows
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|response
operator|instanceof
name|ListResponse
condition|)
block|{
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown response object: "
operator|+
name|this
operator|.
name|response
argument_list|)
throw|;
block|}
block|}
return|return
name|DONE
return|;
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
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|queries
operator|=
operator|(
name|List
operator|)
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
name|fullResponse
operator|.
name|addUpdateCount
argument_list|(
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
name|fullResponse
operator|.
name|addBatchUpdateCount
argument_list|(
name|resultCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
name|dataRows
parameter_list|)
block|{
comment|// exclude prefetched rows in the main result
if|if
condition|(
name|prefetchResultsByPath
operator|!=
literal|null
operator|&&
name|query
operator|instanceof
name|PrefetchSelectQuery
condition|)
block|{
name|PrefetchSelectQuery
name|prefetchQuery
init|=
operator|(
name|PrefetchSelectQuery
operator|)
name|query
decl_stmt|;
name|prefetchResultsByPath
operator|.
name|put
argument_list|(
name|prefetchQuery
operator|.
name|getPrefetchPath
argument_list|()
argument_list|,
name|dataRows
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fullResponse
operator|.
name|addResultList
argument_list|(
name|dataRows
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid attempt to fetch a cursor."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|nextGeneratedDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
if|if
condition|(
name|keysIterator
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|nextDataRows
argument_list|(
name|query
argument_list|,
name|keysIterator
operator|.
name|dataRows
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|ex
parameter_list|)
block|{
comment|// don't throw here....
name|nextQueryException
argument_list|(
name|query
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Query exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Global exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

