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
name|commitlog
package|;
end_package

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
name|graph
operator|.
name|ArcId
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
name|commitlog
operator|.
name|meta
operator|.
name|CommitLogEntity
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
name|commitlog
operator|.
name|meta
operator|.
name|CommitLogEntityFactory
import|;
end_import

begin_comment
comment|/**  * Filters changes passing only auditable object changes to the underlying  * delegate.  */
end_comment

begin_class
class|class
name|DiffFilter
implements|implements
name|GraphChangeHandler
block|{
specifier|private
name|CommitLogEntityFactory
name|entityFactory
decl_stmt|;
specifier|private
name|GraphChangeHandler
name|delegate
decl_stmt|;
name|DiffFilter
parameter_list|(
name|CommitLogEntityFactory
name|entityFactory
parameter_list|,
name|GraphChangeHandler
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|entityFactory
operator|=
name|entityFactory
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
if|if
condition|(
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
operator|.
name|isIncluded
argument_list|()
condition|)
block|{
name|delegate
operator|.
name|nodeIdChanged
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
if|if
condition|(
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
operator|.
name|isIncluded
argument_list|()
condition|)
block|{
name|delegate
operator|.
name|nodeCreated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
if|if
condition|(
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
operator|.
name|isIncluded
argument_list|()
condition|)
block|{
name|delegate
operator|.
name|nodeRemoved
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|CommitLogEntity
name|entity
init|=
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|isIncluded
argument_list|(
name|property
argument_list|)
condition|)
block|{
if|if
condition|(
name|entity
operator|.
name|isConfidential
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|oldValue
operator|=
name|Confidential
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|newValue
operator|=
name|Confidential
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
name|delegate
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
if|if
condition|(
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
operator|.
name|isIncluded
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|delegate
operator|.
name|arcCreated
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
if|if
condition|(
name|entityFactory
operator|.
name|getEntity
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
operator|.
name|isIncluded
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|delegate
operator|.
name|arcDeleted
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

