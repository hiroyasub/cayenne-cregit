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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|PropertyVisitor
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
name|ToManyProperty
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
name|ToOneProperty
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
name|remote
operator|.
name|RemoteIncrementalFaultList
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
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|CayenneContextQueryAction
extends|extends
name|ObjectContextQueryAction
block|{
name|CayenneContextQueryAction
parameter_list|(
name|CayenneContext
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
annotation|@
name|Override
specifier|protected
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
name|RemoteIncrementalFaultList
argument_list|(
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|protected
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
name|CayenneContext
name|context
init|=
operator|(
name|CayenneContext
operator|)
name|actingContext
decl_stmt|;
comment|// handle 4 separate scenarios, but do not combine them as it will be
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
name|invalidateLocally
argument_list|(
name|context
operator|.
name|internalGraphManager
argument_list|()
argument_list|,
name|context
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|iterator
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
comment|// cascade
return|return
operator|!
name|DONE
return|;
block|}
comment|// 2. invalidate object collection
name|Collection
argument_list|<
name|?
argument_list|>
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
name|invalidateLocally
argument_list|(
name|context
operator|.
name|internalGraphManager
argument_list|()
argument_list|,
name|objects
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
comment|// cascade
return|return
operator|!
name|DONE
return|;
block|}
comment|// 3. refresh query - have to do it eagerly to refresh the objects involved
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
name|String
name|group
range|:
name|groups
control|)
block|{
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|removeGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
comment|// cascade group invalidation
return|return
operator|!
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
name|invalidateLocally
parameter_list|(
name|CayenneContextGraphManager
name|graphManager
parameter_list|,
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
parameter_list|)
block|{
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
name|EntityResolver
name|resolver
init|=
name|actingContext
operator|.
name|getEntityResolver
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
specifier|final
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
comment|// we don't care about NEW objects,
comment|// but we still do care about HOLLOW, since snapshot might still be
comment|// present
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|NEW
condition|)
block|{
continue|continue;
block|}
name|ObjectId
name|id
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// per CAY-1082 ROP objects (unlike CayenneDataObject) require all
comment|// relationship faults invalidation.
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|PropertyVisitor
name|arcInvalidator
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|invalidate
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|invalidate
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|descriptor
operator|.
name|visitProperties
argument_list|(
name|arcInvalidator
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
expr_stmt|;
comment|// remove cached changes
name|graphManager
operator|.
name|changeLog
operator|.
name|unregisterNode
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|graphManager
operator|.
name|stateLog
operator|.
name|unregisterNode
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
