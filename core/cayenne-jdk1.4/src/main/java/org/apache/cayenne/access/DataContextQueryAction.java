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
name|Collection
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
name|ObjectContextQueryAction
import|;
end_import

begin_comment
comment|/**  * A DataContext-specific version of  * {@link org.apache.cayenne.util.ObjectContextQueryAction}.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// TODO: Andrus, 2/2/2006 - all these DataContext extensions should become available to
end_comment

begin_comment
comment|// CayenneContext as well....
end_comment

begin_class
class|class
name|DataContextQueryAction
extends|extends
name|ObjectContextQueryAction
block|{
specifier|public
name|DataContextQueryAction
parameter_list|(
name|DataContext
name|actingContext
parameter_list|,
name|ObjectContext
name|targetContext
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|super
argument_list|(
name|actingContext
argument_list|,
name|targetContext
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QueryResponse
name|execute
parameter_list|()
block|{
if|if
condition|(
name|interceptPaginatedQuery
argument_list|()
operator|!=
name|DONE
condition|)
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
name|runQuery
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * Overrides super implementation to property handle data row fetches.      */
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
comment|// TODO: andrus, 10/14/2006 - obtaining a row from an object is the
comment|// only piece that makes this method different from the super
comment|// implementation. This is used in NEW objects sorting on insert. It
comment|// would be nice to implement an alternative algorithm that wouldn't
comment|// require this hack.
if|if
condition|(
name|oidQuery
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|DataContext
operator|)
name|actingContext
operator|)
operator|.
name|currentSnapshot
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
expr_stmt|;
block|}
comment|// do not return hollow objects
if|else if
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
specifier|private
name|boolean
name|interceptPaginatedQuery
parameter_list|()
block|{
if|if
condition|(
name|metadata
operator|.
name|getPageSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|response
operator|=
operator|new
name|ListResponse
argument_list|(
operator|new
name|IncrementalFaultList
argument_list|(
operator|(
name|DataContext
operator|)
name|actingContext
argument_list|,
name|query
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
specifier|protected
name|QueryCache
name|getQueryCache
parameter_list|()
block|{
return|return
operator|(
operator|(
name|DataContext
operator|)
name|actingContext
operator|)
operator|.
name|getQueryCache
argument_list|()
return|;
block|}
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
name|DataContext
name|context
init|=
operator|(
name|DataContext
operator|)
name|actingContext
decl_stmt|;
comment|// handle four separate cases, but do not combine them as it will be
comment|// unclear how to handle cascading behavior
comment|// 1. refresh all
if|if
condition|(
name|refreshQuery
operator|.
name|isRefreshAll
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|context
operator|.
name|getObjectStore
argument_list|()
init|)
block|{
name|invalidateLocally
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getObjectIterator
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// cascade
return|return
operator|!
name|DONE
return|;
block|}
comment|// 2. invalidate object collection
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
synchronized|synchronized
init|(
name|context
operator|.
name|getObjectStore
argument_list|()
init|)
block|{
name|invalidateLocally
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
argument_list|,
name|objects
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// cascade
return|return
operator|!
name|DONE
return|;
block|}
comment|// 3. refresh query - have to do it eagerly to refresh the objects involved
name|Query
name|cachedQuery
init|=
name|refreshQuery
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|cachedQuery
operator|!=
literal|null
condition|)
block|{
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
name|context
operator|.
name|performGenericQuery
argument_list|(
name|cachedQuery
argument_list|)
expr_stmt|;
comment|// do not cascade to avoid running query twice
return|return
name|DONE
return|;
block|}
comment|// 4. refresh groups...
name|String
index|[]
name|groups
init|=
name|refreshQuery
operator|.
name|getGroupKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|groups
operator|!=
literal|null
operator|&&
name|groups
operator|.
name|length
operator|>
literal|0
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
name|groups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|context
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
comment|// cascade group invalidation
return|return
operator|!
name|DONE
return|;
block|}
comment|// shouldn't ever happen
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
name|invalidateLocally
parameter_list|(
name|ObjectStore
name|objectStore
parameter_list|,
name|Iterator
name|it
parameter_list|)
block|{
name|Map
name|diffMap
init|=
name|objectStore
operator|.
name|getChangesByObjectId
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
name|int
name|state
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
comment|// we don't care about NEW objects,
comment|// but we still do care about HOLLOW, since snapshot might still
comment|// be present
if|if
condition|(
name|state
operator|==
name|PersistenceState
operator|.
name|NEW
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|state
operator|==
name|PersistenceState
operator|.
name|MODIFIED
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|DELETED
condition|)
block|{
comment|// remove cached changes
name|diffMap
operator|.
name|remove
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

