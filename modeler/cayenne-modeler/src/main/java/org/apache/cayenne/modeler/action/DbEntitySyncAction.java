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
name|action
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|EntityMergeSupport
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
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|event
operator|.
name|EntityEvent
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
name|event
operator|.
name|MapEvent
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|ProjectController
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
name|modeler
operator|.
name|dialog
operator|.
name|objentity
operator|.
name|EntitySyncController
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
name|modeler
operator|.
name|undo
operator|.
name|DbEntitySyncUndoableEdit
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
name|modeler
operator|.
name|util
operator|.
name|CayenneAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Action that synchronizes all ObjEntities with the current state of the  * selected DbEntity.  */
end_comment

begin_class
specifier|public
class|class
name|DbEntitySyncAction
extends|extends
name|CayenneAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Sync Dependent ObjEntities with DbEntity"
return|;
block|}
specifier|public
name|DbEntitySyncAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-sync.gif"
return|;
block|}
comment|/** 	 * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent) 	 */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|syncDbEntity
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|syncDbEntity
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
init|=
name|dbEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getMappedEntities
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|EntityMergeSupport
name|merger
init|=
operator|new
name|EntitySyncController
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|,
name|dbEntity
argument_list|)
operator|.
name|createMerger
argument_list|()
decl_stmt|;
if|if
condition|(
name|merger
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|merger
operator|.
name|setNameGenerator
argument_list|(
operator|new
name|PreserveRelationshipNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntitySyncUndoableEdit
name|undoableEdit
init|=
operator|new
name|DbEntitySyncUndoableEdit
argument_list|(
operator|(
name|DataChannelDescriptor
operator|)
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
comment|// filter out inherited entities, as we need to add attributes only to the roots
name|filterInheritedEntities
argument_list|(
name|entities
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entities
control|)
block|{
name|DbEntitySyncUndoableEdit
operator|.
name|EntitySyncUndoableListener
name|listener
init|=
name|undoableEdit
operator|.
expr|new
name|EntitySyncUndoableListener
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|merger
operator|.
name|addEntityMergeListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
comment|// TODO: addition or removal of model objects should be reflected in listener callbacks...
comment|// we should not be trying to introspect the merger
if|if
condition|(
name|merger
operator|.
name|isRemovingMeaningfulFKs
argument_list|()
condition|)
block|{
name|undoableEdit
operator|.
name|addEdit
argument_list|(
name|undoableEdit
operator|.
expr|new
name|MeaningfulFKsUndoableEdit
argument_list|(
name|entity
argument_list|,
name|merger
operator|.
name|getMeaningfulFKs
argument_list|(
name|entity
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|merger
operator|.
name|synchronizeWithDbEntity
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|merger
operator|.
name|removeEntityMergeListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
name|undoableEdit
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * This method works only for case when all inherited entities bound to same DbEntity 	 * if this will ever change some additional checks should be performed. 	 */
specifier|private
name|void
name|filterInheritedEntities
parameter_list|(
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
parameter_list|)
block|{
comment|// entities.removeIf(c -> c.getSuperEntity() != null);
name|Iterator
argument_list|<
name|ObjEntity
argument_list|>
name|it
init|=
name|entities
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
if|if
condition|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|getSuperEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|static
class|class
name|PreserveRelationshipNameGenerator
extends|extends
name|DefaultObjectNameGenerator
block|{
annotation|@
name|Override
specifier|public
name|String
name|relationshipName
parameter_list|(
name|DbRelationship
modifier|...
name|relationshipChain
parameter_list|)
block|{
if|if
condition|(
name|relationshipChain
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|relationshipName
argument_list|(
name|relationshipChain
argument_list|)
return|;
block|}
name|DbRelationship
name|last
init|=
name|relationshipChain
index|[
name|relationshipChain
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
comment|// must be in sync with DefaultBaseNameVisitor.visitDbRelationship
if|if
condition|(
name|last
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"untitledRel"
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|relationshipName
argument_list|(
name|relationshipChain
argument_list|)
return|;
block|}
comment|// keep manually set relationship name
return|return
name|last
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

