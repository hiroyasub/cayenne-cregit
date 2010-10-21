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
name|util
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
name|BaseContext
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
name|PersistenceState
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
name|QueryCacheStrategy
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
name|reflect
operator|.
name|ArcProperty
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
comment|/**  * A helper class that implements  * {@link org.apache.cayenne.DataChannel#onQuery(ObjectContext, Query)} logic on behalf of  * an ObjectContext.  *<p>  *<i>Intended for internal use only.</i>  *</p>  *  * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ObjectContextQueryAction
block|{
specifier|protected
specifier|static
specifier|final
name|boolean
name|DONE
init|=
literal|true
decl_stmt|;
specifier|protected
name|ObjectContext
name|targetContext
decl_stmt|;
specifier|protected
name|ObjectContext
name|actingContext
decl_stmt|;
specifier|protected
name|Query
name|query
decl_stmt|;
specifier|protected
name|QueryMetadata
name|metadata
decl_stmt|;
specifier|protected
name|boolean
name|queryOriginator
decl_stmt|;
specifier|protected
specifier|transient
name|QueryResponse
name|response
decl_stmt|;
specifier|public
name|ObjectContextQueryAction
parameter_list|(
name|ObjectContext
name|actingContext
parameter_list|,
name|ObjectContext
name|targetContext
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|actingContext
operator|=
name|actingContext
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
comment|// this means that a caller must pass self as both acting context and target
comment|// context to indicate that a query originated here... null (ROP) or differing
comment|// context indicates that the query was originated elsewhere, which has
comment|// consequences in LOCAL_CACHE handling
name|this
operator|.
name|queryOriginator
operator|=
name|targetContext
operator|!=
literal|null
operator|&&
name|targetContext
operator|==
name|actingContext
expr_stmt|;
comment|// no special target context and same target context as acting context mean the
comment|// same thing. "normalize" the internal state to avoid confusion
name|this
operator|.
name|targetContext
operator|=
name|targetContext
operator|!=
name|actingContext
condition|?
name|targetContext
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|metadata
operator|=
name|query
operator|.
name|getMetaData
argument_list|(
name|actingContext
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Worker method that performs internal query.      */
specifier|public
name|QueryResponse
name|execute
parameter_list|()
block|{
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
name|interceptLocalCache
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|executePostCache
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
name|interceptObjectConversion
argument_list|()
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|private
name|void
name|executePostCache
parameter_list|()
block|{
if|if
condition|(
name|interceptInternalQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
if|if
condition|(
name|interceptPaginatedQuery
argument_list|()
operator|!=
name|DONE
condition|)
block|{
name|runQuery
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Transfers fetched objects into the target context if it is different from "acting"      * context. Note that when this method is invoked, result objects are already      * registered with acting context by the parent channel.      */
specifier|protected
name|void
name|interceptObjectConversion
parameter_list|()
block|{
if|if
condition|(
name|targetContext
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
comment|// rewrite response to contain objects from the query context
name|GenericResponse
name|childResponse
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
name|objects
init|=
name|response
operator|.
name|currentList
argument_list|()
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|childResponse
operator|.
name|addResultList
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: Andrus 1/31/2006 - IncrementalFaultList is not properly
comment|// transferred between contexts....
name|List
name|childObjects
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
name|childObjects
operator|.
name|add
argument_list|(
name|targetContext
operator|.
name|localObject
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|childResponse
operator|.
name|addResultList
argument_list|(
name|childObjects
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|childResponse
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
name|response
operator|=
name|childResponse
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|interceptInternalQuery
parameter_list|()
block|{
return|return
operator|!
name|DONE
return|;
block|}
specifier|protected
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
if|if
condition|(
operator|!
name|oidQuery
operator|.
name|isFetchMandatory
argument_list|()
operator|&&
operator|!
name|oidQuery
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
name|Object
name|object
init|=
name|actingContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|oidQuery
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
comment|// do not return hollow objects
if|if
condition|(
operator|(
operator|(
name|Persistent
operator|)
name|object
operator|)
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
block|}
block|}
return|return
operator|!
name|DONE
return|;
block|}
specifier|protected
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
operator|!
name|relationshipQuery
operator|.
name|isRefreshing
argument_list|()
condition|)
block|{
comment|// don't intercept to-many relationships if fetch is done to the same
comment|// context as the root context of this action - this will result in an
comment|// infinite loop.
if|if
condition|(
name|targetContext
operator|==
literal|null
operator|&&
name|relationshipQuery
operator|.
name|getRelationship
argument_list|(
name|actingContext
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return
operator|!
name|DONE
return|;
block|}
name|ObjectId
name|id
init|=
name|relationshipQuery
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|actingContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|actingContext
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|descriptor
operator|.
name|isFault
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|ArcProperty
name|property
init|=
operator|(
name|ArcProperty
operator|)
name|descriptor
operator|.
name|getProperty
argument_list|(
name|relationshipQuery
operator|.
name|getRelationshipName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|property
operator|.
name|isFault
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|Object
name|related
init|=
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|List
name|result
decl_stmt|;
comment|// null to-one
if|if
condition|(
name|related
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// to-many
if|else if
condition|(
name|related
operator|instanceof
name|List
condition|)
block|{
name|result
operator|=
operator|(
name|List
operator|)
name|related
expr_stmt|;
block|}
comment|// non-null to-one
else|else
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|related
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|DONE
return|;
block|}
comment|/**                          * Workaround for CAY-1183. If a Relationship query is being sent from                          * child context, we assure that local object is not NEW and relationship - unresolved                          * (this way exception will occur). This helps when faulting objects that                          * were committed to parent context (this), but not to database.                          *                          * Checking type of context's channel is the only way to ensure that we are                          * on the top level of context hierarchy (there might be more than one-level-deep                          * nested contexts).                          */
if|if
condition|(
operator|(
operator|(
name|Persistent
operator|)
name|object
operator|)
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|NEW
operator|&&
operator|!
operator|(
name|actingContext
operator|.
name|getChannel
argument_list|()
operator|instanceof
name|BaseContext
operator|)
condition|)
block|{
name|this
operator|.
name|response
operator|=
operator|new
name|ListResponse
argument_list|()
expr_stmt|;
return|return
name|DONE
return|;
block|}
block|}
block|}
block|}
block|}
return|return
operator|!
name|DONE
return|;
block|}
comment|/**      * @since 3.0      */
specifier|protected
specifier|abstract
name|boolean
name|interceptPaginatedQuery
parameter_list|()
function_decl|;
comment|/**      * @since 3.0      */
specifier|protected
specifier|abstract
name|boolean
name|interceptRefreshQuery
parameter_list|()
function_decl|;
comment|/**      * @since 3.0      */
specifier|protected
name|boolean
name|interceptLocalCache
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
comment|// ignore local cache unless this context originated the query...
if|if
condition|(
operator|!
name|queryOriginator
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
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
operator|==
name|metadata
operator|.
name|getCacheStrategy
argument_list|()
decl_stmt|;
name|boolean
name|cacheOrCacheRefresh
init|=
name|cache
operator|||
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE_REFRESH
operator|==
name|metadata
operator|.
name|getCacheStrategy
argument_list|()
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
comment|/**      * @since 3.0      */
specifier|protected
name|QueryCache
name|getQueryCache
parameter_list|()
block|{
return|return
operator|(
operator|(
name|BaseContext
operator|)
name|actingContext
operator|)
operator|.
name|getQueryCache
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|protected
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
name|executePostCache
argument_list|()
expr_stmt|;
return|return
name|response
operator|.
name|firstList
argument_list|()
return|;
block|}
block|}
return|;
block|}
comment|/**      * Fetches data from the channel.      */
specifier|protected
name|void
name|runQuery
parameter_list|()
block|{
name|this
operator|.
name|response
operator|=
name|actingContext
operator|.
name|getChannel
argument_list|()
operator|.
name|onQuery
argument_list|(
name|actingContext
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

