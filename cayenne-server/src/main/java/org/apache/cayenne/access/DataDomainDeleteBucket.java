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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|DeleteBatchQuery
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

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|DataDomainDeleteBucket
extends|extends
name|DataDomainSyncBucket
block|{
name|DataDomainDeleteBucket
parameter_list|(
name|DataDomainFlushAction
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|void
name|postprocess
parameter_list|()
block|{
if|if
condition|(
operator|!
name|objectsByDescriptor
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|deletedIds
init|=
name|parent
operator|.
name|getResultDeletedIds
argument_list|()
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
range|:
name|objectsByDescriptor
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|Persistent
name|object
range|:
name|objects
control|)
block|{
name|deletedIds
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
block|}
block|}
block|}
annotation|@
name|Override
name|void
name|appendQueriesInternal
parameter_list|(
name|Collection
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|)
block|{
name|DataNodeSyncQualifierDescriptor
name|qualifierBuilder
init|=
operator|new
name|DataNodeSyncQualifierDescriptor
argument_list|()
decl_stmt|;
name|EntitySorter
name|sorter
init|=
name|parent
operator|.
name|getDomain
argument_list|()
operator|.
name|getEntitySorter
argument_list|()
decl_stmt|;
name|sorter
operator|.
name|sortDbEntities
argument_list|(
name|dbEntities
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|DbEntity
name|dbEntity
range|:
name|dbEntities
control|)
block|{
name|Collection
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
name|descriptors
init|=
name|descriptorsByDbEntity
operator|.
name|get
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Query
argument_list|>
name|batches
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntityClassDescriptor
name|descriptor
range|:
name|descriptors
control|)
block|{
name|qualifierBuilder
operator|.
name|reset
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|boolean
name|isRootDbEntity
init|=
name|descriptor
operator|.
name|isMaster
argument_list|()
decl_stmt|;
comment|// remove object set for dependent entity, so that it does not show up
comment|// on post processing
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
init|=
name|objectsByDescriptor
operator|.
name|get
argument_list|(
name|descriptor
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|checkReadOnly
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRootDbEntity
condition|)
block|{
name|sorter
operator|.
name|sortObjectsForEntity
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|,
name|objects
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Persistent
name|o
range|:
name|objects
control|)
block|{
name|ObjectDiff
name|diff
init|=
name|parent
operator|.
name|objectDiff
argument_list|(
name|o
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|qualifierSnapshot
init|=
name|qualifierBuilder
operator|.
name|createQualifierSnapshot
argument_list|(
name|diff
argument_list|)
decl_stmt|;
comment|// organize batches by the nulls in qualifier
name|Set
argument_list|<
name|String
argument_list|>
name|nullQualifierNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|qualifierSnapshot
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|nullQualifierNames
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
name|batchKey
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|nullQualifierNames
argument_list|)
decl_stmt|;
name|DeleteBatchQuery
name|batch
init|=
operator|(
name|DeleteBatchQuery
operator|)
name|batches
operator|.
name|get
argument_list|(
name|batchKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|batch
operator|==
literal|null
condition|)
block|{
name|batch
operator|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|dbEntity
argument_list|,
name|qualifierBuilder
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|nullQualifierNames
argument_list|,
literal|27
argument_list|)
expr_stmt|;
name|batch
operator|.
name|setUsingOptimisticLocking
argument_list|(
name|qualifierBuilder
operator|.
name|isUsingOptimisticLocking
argument_list|()
argument_list|)
expr_stmt|;
name|batches
operator|.
name|put
argument_list|(
name|batchKey
argument_list|,
name|batch
argument_list|)
expr_stmt|;
block|}
name|batch
operator|.
name|add
argument_list|(
name|qualifierSnapshot
argument_list|)
expr_stmt|;
block|}
block|}
name|queries
operator|.
name|addAll
argument_list|(
name|batches
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

