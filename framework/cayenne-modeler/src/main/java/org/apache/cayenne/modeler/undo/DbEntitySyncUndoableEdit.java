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
name|modeler
operator|.
name|undo
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
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CompoundEdit
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
name|DataDomain
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
name|EntityMergeListener
import|;
end_import

begin_class
specifier|public
class|class
name|DbEntitySyncUndoableEdit
extends|extends
name|CompoundEdit
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isInProgress
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canUndo
parameter_list|()
block|{
return|return
operator|!
name|edits
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|private
name|DataDomain
name|domain
decl_stmt|;
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|public
name|DbEntitySyncUndoableEdit
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
specifier|public
class|class
name|EntitySyncUndoableListener
implements|implements
name|EntityMergeListener
block|{
specifier|private
name|ObjEntity
name|entity
decl_stmt|;
specifier|public
name|EntitySyncUndoableListener
parameter_list|(
name|ObjEntity
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
name|void
name|objRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|addEdit
argument_list|(
operator|new
name|CreateRelationshipUndoableEdit
argument_list|(
name|entity
argument_list|,
operator|new
name|ObjRelationship
index|[]
block|{
name|rel
block|}
argument_list|)
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
name|addEdit
argument_list|(
operator|new
name|CreateAttributeUndoableEdit
argument_list|(
name|domain
argument_list|,
name|map
argument_list|,
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|MeaningfulFKsUndoableEdit
extends|extends
name|CompoundEdit
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isInProgress
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canUndo
parameter_list|()
block|{
return|return
operator|!
name|edits
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|MeaningfulFKsUndoableEdit
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttrs
parameter_list|)
block|{
for|for
control|(
name|DbAttribute
name|da
range|:
name|dbAttrs
control|)
block|{
name|ObjAttribute
name|oa
init|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
decl_stmt|;
while|while
condition|(
name|oa
operator|!=
literal|null
condition|)
block|{
name|addEdit
argument_list|(
operator|new
name|RemoveAttributeUndoableEdit
argument_list|(
name|domain
argument_list|,
name|map
argument_list|,
name|entity
argument_list|,
operator|new
name|ObjAttribute
index|[]
block|{
name|oa
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|oa
operator|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRedoPresentationName
parameter_list|()
block|{
return|return
literal|"Redo Db Entity Sync"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getUndoPresentationName
parameter_list|()
block|{
return|return
literal|"Undo Db Entity Sync"
return|;
block|}
block|}
end_class

end_unit

