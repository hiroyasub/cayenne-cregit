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
operator|.
name|trans
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
name|JoinType
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

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|final
class|class
name|JoinTreeNode
block|{
specifier|private
name|String
name|targetTableAlias
decl_stmt|;
specifier|private
name|String
name|sourceTableAlias
decl_stmt|;
specifier|private
name|DbRelationship
name|relationship
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|JoinType
name|joinType
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|JoinTreeNode
argument_list|>
name|children
decl_stmt|;
specifier|private
name|JoinStack
name|joinProcessor
decl_stmt|;
name|JoinTreeNode
parameter_list|(
name|JoinStack
name|joinProcessor
parameter_list|)
block|{
name|this
operator|.
name|joinProcessor
operator|=
name|joinProcessor
expr_stmt|;
block|}
name|JoinTreeNode
parameter_list|(
name|JoinStack
name|joinProcessor
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|this
argument_list|(
name|joinProcessor
argument_list|)
expr_stmt|;
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|joinType
operator|=
name|joinType
expr_stmt|;
block|}
name|int
name|size
parameter_list|()
block|{
name|int
name|i
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JoinTreeNode
name|child
range|:
name|children
control|)
block|{
name|i
operator|+=
name|child
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|i
return|;
block|}
name|Collection
argument_list|<
name|JoinTreeNode
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
operator|!=
literal|null
condition|?
name|children
else|:
name|Collections
operator|.
expr|<
name|JoinTreeNode
operator|>
name|emptyList
argument_list|()
return|;
block|}
name|JoinTreeNode
name|findOrCreateChild
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|ArrayList
argument_list|<
name|JoinTreeNode
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|JoinTreeNode
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|child
operator|.
name|equals
argument_list|(
name|relationship
argument_list|,
name|joinType
argument_list|,
name|alias
argument_list|)
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
block|}
name|JoinTreeNode
name|child
init|=
operator|new
name|JoinTreeNode
argument_list|(
name|joinProcessor
argument_list|,
name|relationship
argument_list|,
name|joinType
argument_list|,
name|alias
argument_list|)
decl_stmt|;
name|child
operator|.
name|setSourceTableAlias
argument_list|(
name|this
operator|.
name|targetTableAlias
argument_list|)
expr_stmt|;
name|child
operator|.
name|setTargetTableAlias
argument_list|(
name|joinProcessor
operator|.
name|newAlias
argument_list|()
argument_list|)
expr_stmt|;
name|children
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
return|return
name|child
return|;
block|}
specifier|private
name|boolean
name|equals
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
return|return
name|this
operator|.
name|relationship
operator|==
name|relationship
operator|&&
name|this
operator|.
name|joinType
operator|==
name|joinType
operator|&&
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|alias
argument_list|,
name|alias
argument_list|)
return|;
block|}
name|String
name|getTargetTableAlias
parameter_list|()
block|{
return|return
name|targetTableAlias
return|;
block|}
name|void
name|setTargetTableAlias
parameter_list|(
name|String
name|targetTableAlias
parameter_list|)
block|{
name|this
operator|.
name|targetTableAlias
operator|=
name|targetTableAlias
expr_stmt|;
block|}
name|String
name|getSourceTableAlias
parameter_list|()
block|{
return|return
name|sourceTableAlias
return|;
block|}
name|void
name|setSourceTableAlias
parameter_list|(
name|String
name|sourceTableAlias
parameter_list|)
block|{
name|this
operator|.
name|sourceTableAlias
operator|=
name|sourceTableAlias
expr_stmt|;
block|}
name|DbRelationship
name|getRelationship
parameter_list|()
block|{
return|return
name|relationship
return|;
block|}
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
name|JoinType
name|getJoinType
parameter_list|()
block|{
return|return
name|joinType
return|;
block|}
block|}
end_class

end_unit

