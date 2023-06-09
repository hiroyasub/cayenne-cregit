begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
operator|.
name|flush
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
name|access
operator|.
name|ObjectDiff
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpType
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpVisitor
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
name|flush
operator|.
name|operation
operator|.
name|DeleteDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|InsertDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|UpdateDbRowOp
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
name|graph
operator|.
name|GraphChangeHandler
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

begin_comment
comment|/**  * Visitor that runs all required actions based on operation type.  *<p>  * E.g. it creates values for insert and update, it fills optimistic lock qualifier for update and delete, etc.  *  * @since 4.2  */
end_comment

begin_class
class|class
name|RootRowOpProcessor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|Void
argument_list|>
block|{
specifier|private
specifier|final
name|DbRowOpFactory
name|dbRowOpFactory
decl_stmt|;
specifier|private
specifier|final
name|GraphChangeHandler
name|insertHandler
decl_stmt|;
specifier|private
specifier|final
name|GraphChangeHandler
name|updateHandler
decl_stmt|;
specifier|private
specifier|final
name|GraphChangeHandler
name|deleteHandler
decl_stmt|;
specifier|private
name|ObjectDiff
name|diff
decl_stmt|;
name|RootRowOpProcessor
parameter_list|(
name|DbRowOpFactory
name|dbRowOpFactory
parameter_list|)
block|{
name|this
operator|.
name|dbRowOpFactory
operator|=
name|dbRowOpFactory
expr_stmt|;
name|this
operator|.
name|insertHandler
operator|=
operator|new
name|ValuesCreationHandler
argument_list|(
name|dbRowOpFactory
argument_list|,
name|DbRowOpType
operator|.
name|INSERT
argument_list|)
expr_stmt|;
name|this
operator|.
name|updateHandler
operator|=
operator|new
name|ValuesCreationHandler
argument_list|(
name|dbRowOpFactory
argument_list|,
name|DbRowOpType
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
name|this
operator|.
name|deleteHandler
operator|=
operator|new
name|ArcValuesCreationHandler
argument_list|(
name|dbRowOpFactory
argument_list|,
name|DbRowOpType
operator|.
name|DELETE
argument_list|)
expr_stmt|;
block|}
name|void
name|setDiff
parameter_list|(
name|ObjectDiff
name|diff
parameter_list|)
block|{
name|this
operator|.
name|diff
operator|=
name|diff
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitInsert
parameter_list|(
name|InsertDbRowOp
name|dbRow
parameter_list|)
block|{
name|diff
operator|.
name|apply
argument_list|(
name|insertHandler
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitUpdate
parameter_list|(
name|UpdateDbRowOp
name|dbRow
parameter_list|)
block|{
name|diff
operator|.
name|apply
argument_list|(
name|updateHandler
argument_list|)
expr_stmt|;
if|if
condition|(
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredLockType
argument_list|()
operator|==
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
condition|)
block|{
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|visitAllProperties
argument_list|(
operator|new
name|OptimisticLockQualifierBuilder
argument_list|(
name|dbRow
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDelete
parameter_list|(
name|DeleteDbRowOp
name|dbRow
parameter_list|)
block|{
if|if
condition|(
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Attempt to modify object(s) mapped to a read-only entity: '%s'. "
operator|+
literal|"Can't commit changes."
argument_list|,
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|diff
operator|.
name|apply
argument_list|(
name|deleteHandler
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|flattenedIds
init|=
name|dbRowOpFactory
operator|.
name|getStore
argument_list|()
operator|.
name|getFlattenedIds
argument_list|(
name|dbRow
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|flattenedIds
operator|.
name|forEach
argument_list|(
name|id
lambda|->
name|dbRowOpFactory
operator|.
name|getOrCreate
argument_list|(
name|dbRowOpFactory
operator|.
name|getDbEntity
argument_list|(
name|id
argument_list|)
argument_list|,
name|id
argument_list|,
name|DbRowOpType
operator|.
name|DELETE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredLockType
argument_list|()
operator|==
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
condition|)
block|{
name|dbRowOpFactory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|visitAllProperties
argument_list|(
operator|new
name|OptimisticLockQualifierBuilder
argument_list|(
name|dbRow
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

