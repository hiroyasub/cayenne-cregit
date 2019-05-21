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
name|util
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
name|map
operator|.
name|DataMap
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
name|DeleteRule
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
name|ObjAttribute
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
name|map
operator|.
name|ObjRelationship
import|;
end_import

begin_comment
comment|/**  * DeleteRuleUpdater is responsible for auto-setting delete rules for object relationships  */
end_comment

begin_class
specifier|public
class|class
name|DeleteRuleUpdater
implements|implements
name|EntityMergeListener
block|{
comment|/**      * Singleton object to for defining EntityMergeListener instance      */
specifier|private
specifier|static
name|DeleteRuleUpdater
name|instance
decl_stmt|;
comment|/**      * Updates delete rules for all obj entities in a datamap      */
specifier|public
specifier|static
name|void
name|updateDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
init|=
name|map
operator|.
name|getObjEntities
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|ent
range|:
name|entities
control|)
block|{
name|updateObjEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates delete rules for all relationships in a objentity      */
specifier|public
specifier|static
name|void
name|updateObjEntity
parameter_list|(
name|ObjEntity
name|e
parameter_list|)
block|{
name|Collection
argument_list|<
name|ObjRelationship
argument_list|>
name|rels
init|=
name|e
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|rels
control|)
block|{
name|updateObjRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates delete rules for specified relationship      */
specifier|public
specifier|static
name|void
name|updateObjRelationship
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|rel
operator|.
name|setDeleteRule
argument_list|(
name|rel
operator|.
name|isToMany
argument_list|()
condition|?
name|DeleteRule
operator|.
name|DEFAULT_DELETE_RULE_TO_MANY
else|:
name|DeleteRule
operator|.
name|DEFAULT_DELETE_RULE_TO_ONE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objAttributeAdded
parameter_list|(
name|ObjAttribute
name|attr
parameter_list|)
block|{
block|}
specifier|public
name|void
name|objRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|updateObjRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns EntityMergeListener instance, which can set delete rule at relationship change      */
specifier|public
specifier|static
name|EntityMergeListener
name|getEntityMergeListener
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|DeleteRuleUpdater
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
block|}
end_class

end_unit

