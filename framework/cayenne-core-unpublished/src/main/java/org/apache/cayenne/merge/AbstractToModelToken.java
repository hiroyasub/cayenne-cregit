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
name|merge
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
name|HashSet
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
name|MappingNamespace
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
name|EntityMergeSupport
import|;
end_import

begin_comment
comment|/**  * Common abstract superclass for all {@link MergerToken}s going from the database to the  * model.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractToModelToken
implements|implements
name|MergerToken
block|{
specifier|public
specifier|final
name|MergeDirection
name|getDirection
parameter_list|()
block|{
return|return
name|MergeDirection
operator|.
name|TO_MODEL
return|;
block|}
specifier|protected
name|void
name|synchronizeWithObjEntity
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|objEntitiesMappedToDbEntity
argument_list|(
name|entity
argument_list|)
control|)
block|{
operator|new
name|EntityMergeSupport
argument_list|(
name|objEntity
operator|.
name|getDataMap
argument_list|()
argument_list|)
operator|.
name|synchronizeWithDbEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|objEntitiesMappedToDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Set
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
init|=
operator|new
name|HashSet
argument_list|<
name|ObjEntity
argument_list|>
argument_list|()
decl_stmt|;
name|MappingNamespace
name|mns
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|mns
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|equals
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|objEntities
operator|.
name|add
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|objEntities
return|;
block|}
specifier|protected
name|void
name|remove
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|,
name|DbRelationship
name|rel
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
if|if
condition|(
name|rel
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|reverse
condition|)
block|{
name|remove
argument_list|(
name|mergerContext
argument_list|,
name|rel
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|DbEntity
name|dbEntity
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|objEntitiesMappedToDbEntity
argument_list|(
name|dbEntity
argument_list|)
control|)
block|{
name|remove
argument_list|(
name|mergerContext
argument_list|,
name|objEntity
operator|.
name|getRelationshipForDbRelationship
argument_list|(
name|rel
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getModelMergeDelegate
argument_list|()
operator|.
name|dbRelationshipRemoved
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|remove
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|,
name|ObjRelationship
name|rel
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
if|if
condition|(
name|rel
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|reverse
condition|)
block|{
name|remove
argument_list|(
name|mergerContext
argument_list|,
name|rel
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getModelMergeDelegate
argument_list|()
operator|.
name|objRelationshipRemoved
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|ts
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getTokenName
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getTokenValue
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ts
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|abstract
specifier|static
class|class
name|Entity
extends|extends
name|AbstractToModelToken
block|{
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|public
name|Entity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
specifier|public
name|DbEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
specifier|abstract
specifier|static
class|class
name|EntityAndColumn
extends|extends
name|Entity
block|{
specifier|private
name|DbAttribute
name|column
decl_stmt|;
specifier|public
name|EntityAndColumn
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
block|}
specifier|public
name|DbAttribute
name|getColumn
parameter_list|()
block|{
return|return
name|column
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
