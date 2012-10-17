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
name|Iterator
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
name|DbAttribute
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
name|UpdateBatchQuery
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|DataDomainUpdateBucket
extends|extends
name|DataDomainSyncBucket
block|{
name|DataDomainUpdateBucket
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
name|appendQueriesInternal
parameter_list|(
name|Collection
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|)
block|{
name|DataDomainDBDiffBuilder
name|diffBuilder
init|=
operator|new
name|DataDomainDBDiffBuilder
argument_list|()
decl_stmt|;
name|DataNodeSyncQualifierDescriptor
name|qualifierBuilder
init|=
operator|new
name|DataNodeSyncQualifierDescriptor
argument_list|()
decl_stmt|;
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
argument_list|<
name|Object
argument_list|,
name|Query
argument_list|>
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
name|ObjEntity
name|entity
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|diffBuilder
operator|.
name|reset
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
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
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|==
name|dbEntity
decl_stmt|;
name|Iterator
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
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|objects
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Persistent
name|o
init|=
name|objects
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|snapshot
init|=
name|diffBuilder
operator|.
name|buildDBDiff
argument_list|(
name|diff
argument_list|)
decl_stmt|;
comment|// check whether MODIFIED object has real db-level modifications
if|if
condition|(
name|snapshot
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
comment|// after we filtered out "fake" modifications, check if an
comment|// attempt is made to modify a read only entity
name|checkReadOnly
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Map
name|qualifierSnapshot
init|=
name|qualifierBuilder
operator|.
name|createQualifierSnapshot
argument_list|(
name|diff
argument_list|)
decl_stmt|;
comment|// organize batches by the updated columns + nulls in qualifier
name|Set
name|snapshotSet
init|=
name|snapshot
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|Set
name|nullQualifierNames
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|qualifierSnapshot
operator|.
name|entrySet
argument_list|()
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
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|List
name|batchKey
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|snapshotSet
argument_list|,
name|nullQualifierNames
argument_list|)
decl_stmt|;
name|UpdateBatchQuery
name|batch
init|=
operator|(
name|UpdateBatchQuery
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
name|UpdateBatchQuery
argument_list|(
name|dbEntity
argument_list|,
name|qualifierBuilder
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|updatedAttributes
argument_list|(
name|dbEntity
argument_list|,
name|snapshot
argument_list|)
argument_list|,
name|nullQualifierNames
argument_list|,
literal|10
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
argument_list|,
name|snapshot
argument_list|,
name|o
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
comment|// update replacement id with meaningful PK changes
if|if
condition|(
name|isRootDbEntity
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacementId
init|=
name|o
operator|.
name|getObjectId
argument_list|()
operator|.
name|getReplacementIdMap
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|pk
range|:
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|pk
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|snapshot
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
name|replacementId
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|replacementId
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
comment|/**      * Creates a list of DbAttributes that are updated in a snapshot      */
specifier|private
name|List
name|updatedAttributes
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|Map
name|updatedSnapshot
parameter_list|)
block|{
name|List
name|attributes
init|=
operator|new
name|ArrayList
argument_list|(
name|updatedSnapshot
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Map
name|entityAttributes
init|=
name|entity
operator|.
name|getAttributeMap
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|updatedSnapshot
operator|.
name|keySet
argument_list|()
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
name|Object
name|name
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
name|entityAttributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|attributes
return|;
block|}
block|}
end_class

end_unit

