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

begin_comment
comment|/**  * Batched UPDATE query.  *   */
end_comment

begin_class
specifier|public
class|class
name|UpdateBatchQuery
extends|extends
name|BatchQuery
block|{
comment|/**      * @since 1.2      */
specifier|protected
name|List
argument_list|<
name|ObjectId
argument_list|>
name|objectIds
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Map
argument_list|>
name|qualifierSnapshots
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Map
argument_list|>
name|updateSnapshots
decl_stmt|;
specifier|protected
name|boolean
name|usingOptimisticLocking
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|updatedAttributes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|nullQualifierNames
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
decl_stmt|;
comment|/**      * Creates new UpdateBatchQuery.      *       * @param dbEntity Table or view to update.      * @param qualifierAttributes DbAttributes used in the WHERE clause.      * @param nullQualifierNames DbAttribute names in the WHERE clause that have null      *            values.      * @param updatedAttribute DbAttributes describing updated columns.      * @param batchCapacity Estimated size of the batch.      */
specifier|public
name|UpdateBatchQuery
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|updatedAttribute
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|nullQualifierNames
parameter_list|,
name|int
name|batchCapacity
parameter_list|)
block|{
name|super
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|this
operator|.
name|updatedAttributes
operator|=
name|updatedAttribute
expr_stmt|;
name|this
operator|.
name|qualifierAttributes
operator|=
name|qualifierAttributes
expr_stmt|;
name|this
operator|.
name|nullQualifierNames
operator|=
name|nullQualifierNames
operator|!=
literal|null
condition|?
name|nullQualifierNames
else|:
name|Collections
operator|.
name|EMPTY_SET
expr_stmt|;
name|qualifierSnapshots
operator|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|>
argument_list|(
name|batchCapacity
argument_list|)
expr_stmt|;
name|updateSnapshots
operator|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|>
argument_list|(
name|batchCapacity
argument_list|)
expr_stmt|;
name|objectIds
operator|=
operator|new
name|ArrayList
argument_list|<
name|ObjectId
argument_list|>
argument_list|(
name|batchCapacity
argument_list|)
expr_stmt|;
name|dbAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|(
name|updatedAttributes
operator|.
name|size
argument_list|()
operator|+
name|qualifierAttributes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|dbAttributes
operator|.
name|addAll
argument_list|(
name|updatedAttributes
argument_list|)
expr_stmt|;
name|dbAttributes
operator|.
name|addAll
argument_list|(
name|qualifierAttributes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if a given attribute always has a null value in the batch.      *       * @since 1.1      */
specifier|public
name|boolean
name|isNull
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
name|nullQualifierNames
operator|.
name|contains
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns true if the batch query uses optimistic locking.      *       * @since 1.1      */
annotation|@
name|Override
specifier|public
name|boolean
name|isUsingOptimisticLocking
parameter_list|()
block|{
return|return
name|usingOptimisticLocking
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setUsingOptimisticLocking
parameter_list|(
name|boolean
name|usingOptimisticLocking
parameter_list|)
block|{
name|this
operator|.
name|usingOptimisticLocking
operator|=
name|usingOptimisticLocking
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getValue
parameter_list|(
name|int
name|dbAttributeIndex
parameter_list|)
block|{
name|DbAttribute
name|attribute
init|=
name|dbAttributes
operator|.
name|get
argument_list|(
name|dbAttributeIndex
argument_list|)
decl_stmt|;
comment|// take value either from updated values or id's,
comment|// depending on the index
name|Object
name|snapshot
init|=
operator|(
name|dbAttributeIndex
operator|<
name|updatedAttributes
operator|.
name|size
argument_list|()
operator|)
condition|?
name|updateSnapshots
operator|.
name|get
argument_list|(
name|batchIndex
argument_list|)
else|:
name|qualifierSnapshots
operator|.
name|get
argument_list|(
name|batchIndex
argument_list|)
decl_stmt|;
return|return
name|getValue
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|snapshot
argument_list|,
name|attribute
argument_list|)
return|;
block|}
comment|/**      * Adds a parameter row to the batch.      */
specifier|public
name|void
name|add
parameter_list|(
name|Map
name|qualifierSnapshot
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|updateSnapshot
parameter_list|)
block|{
name|add
argument_list|(
name|qualifierSnapshot
argument_list|,
name|updateSnapshot
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a parameter row to the batch.      *       * @since 1.2      */
specifier|public
name|void
name|add
parameter_list|(
name|Map
name|qualifierSnapshot
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|updateSnapshot
parameter_list|,
name|ObjectId
name|id
parameter_list|)
block|{
name|qualifierSnapshots
operator|.
name|add
argument_list|(
name|qualifierSnapshot
argument_list|)
expr_stmt|;
name|updateSnapshots
operator|.
name|add
argument_list|(
name|updateSnapshot
argument_list|)
expr_stmt|;
name|objectIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|qualifierSnapshots
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getDbAttributes
parameter_list|()
block|{
return|return
name|dbAttributes
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getUpdatedAttributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|updatedAttributes
argument_list|)
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getQualifierAttributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|qualifierAttributes
argument_list|)
return|;
block|}
comment|/**      * Returns a snapshot of the current qualifier values.      *       * @since 1.1      */
specifier|public
name|Map
name|getCurrentQualifier
parameter_list|()
block|{
return|return
name|qualifierSnapshots
operator|.
name|get
argument_list|(
name|batchIndex
argument_list|)
return|;
block|}
comment|/**      * Returns an ObjectId associated with the current batch iteration. Used internally by      * Cayenne to match current iteration with a specific object and assign it generated      * keys.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|ObjectId
name|getObjectId
parameter_list|()
block|{
return|return
name|objectIds
operator|.
name|get
argument_list|(
name|batchIndex
argument_list|)
return|;
block|}
block|}
end_class

end_unit
