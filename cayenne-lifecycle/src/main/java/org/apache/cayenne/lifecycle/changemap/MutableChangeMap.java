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
name|lifecycle
operator|.
name|changemap
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
name|HashSet
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

begin_comment
comment|/**  * A mutable implementation of {@link ChangeMap}.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|MutableChangeMap
implements|implements
name|ChangeMap
block|{
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|MutableObjectChange
argument_list|>
name|changes
decl_stmt|;
specifier|public
name|MutableObjectChange
name|getOrCreate
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|ObjectChangeType
name|type
parameter_list|)
block|{
name|MutableObjectChange
name|changeSet
init|=
name|getOrCreate
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|changeSet
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|changeSet
return|;
block|}
specifier|private
name|MutableObjectChange
name|getOrCreate
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|MutableObjectChange
name|objectChange
init|=
name|changes
operator|!=
literal|null
condition|?
name|changes
operator|.
name|get
argument_list|(
name|id
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|objectChange
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|changes
operator|==
literal|null
condition|)
block|{
name|changes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|objectChange
operator|=
operator|new
name|MutableObjectChange
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|changes
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|objectChange
argument_list|)
expr_stmt|;
block|}
return|return
name|objectChange
return|;
block|}
specifier|public
name|MutableObjectChange
name|aliasId
parameter_list|(
name|ObjectId
name|preCommitId
parameter_list|,
name|ObjectId
name|postCommitId
parameter_list|)
block|{
name|MutableObjectChange
name|changeSet
init|=
name|getOrCreate
argument_list|(
name|preCommitId
argument_list|)
decl_stmt|;
name|changeSet
operator|.
name|setPostCommitId
argument_list|(
name|postCommitId
argument_list|)
expr_stmt|;
name|changes
operator|.
name|put
argument_list|(
name|postCommitId
argument_list|,
name|changeSet
argument_list|)
expr_stmt|;
return|return
name|changeSet
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|?
extends|extends
name|ObjectChange
argument_list|>
name|getUniqueChanges
parameter_list|()
block|{
comment|// ensure distinct change set
return|return
name|changes
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|ObjectChange
operator|>
name|emptySet
argument_list|()
else|:
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|changes
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|?
extends|extends
name|ObjectChange
argument_list|>
name|getChanges
parameter_list|()
block|{
return|return
name|changes
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|ObjectId
operator|,
name|ObjectChange
operator|>
name|emptyMap
argument_list|()
operator|:
name|changes
return|;
block|}
block|}
end_class

end_unit

