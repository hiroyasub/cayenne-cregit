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
name|modeler
operator|.
name|undo
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotRedoException
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
name|CannotUndoException
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
name|map
operator|.
name|Relationship
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
name|map
operator|.
name|event
operator|.
name|RelationshipEvent
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

begin_class
specifier|public
class|class
name|RelationshipUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1864303176024098961L
decl_stmt|;
specifier|private
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|relationship
decl_stmt|;
specifier|private
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|prevRelationship
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
specifier|private
name|boolean
name|useDb
decl_stmt|;
specifier|public
name|RelationshipUndoableEdit
parameter_list|(
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|relationship
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
expr_stmt|;
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|useDb
operator|=
name|relationship
operator|instanceof
name|DbRelationship
expr_stmt|;
name|this
operator|.
name|prevRelationship
operator|=
name|copyRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|fireRelationshipEvent
argument_list|(
name|relationship
argument_list|,
name|prevRelationship
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
name|fireRelationshipEvent
argument_list|(
name|prevRelationship
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|fireRelationshipEvent
parameter_list|(
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|relToFire
parameter_list|,
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|currRel
parameter_list|)
block|{
if|if
condition|(
name|useDb
condition|)
block|{
name|fireDbRelationshipEvent
argument_list|(
operator|(
name|DbRelationship
operator|)
name|relToFire
argument_list|,
operator|(
name|DbRelationship
operator|)
name|currRel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fireObjRelationshipEvent
argument_list|(
operator|(
name|ObjRelationship
operator|)
name|relToFire
argument_list|,
operator|(
name|ObjRelationship
operator|)
name|currRel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|fireDbRelationshipEvent
parameter_list|(
name|DbRelationship
name|relToFire
parameter_list|,
name|DbRelationship
name|currRel
parameter_list|)
block|{
name|DbEntity
name|dbEntity
init|=
name|currRel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|dbEntity
operator|.
name|removeRelationship
argument_list|(
name|currRel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addRelationship
argument_list|(
name|relToFire
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|fireDbRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|this
argument_list|,
name|relToFire
argument_list|,
name|relToFire
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|fireObjRelationshipEvent
parameter_list|(
name|ObjRelationship
name|relToFire
parameter_list|,
name|ObjRelationship
name|currRel
parameter_list|)
block|{
name|ObjEntity
name|objEntity
init|=
name|currRel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|objEntity
operator|.
name|removeRelationship
argument_list|(
name|currRel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addRelationship
argument_list|(
name|relToFire
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|fireObjRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|this
argument_list|,
name|relToFire
argument_list|,
name|relToFire
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRedoPresentationName
parameter_list|()
block|{
return|return
literal|"Redo Edit relationship"
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
literal|"Undo Edit relationship"
return|;
block|}
specifier|private
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|copyRelationship
parameter_list|(
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|relationship
parameter_list|)
block|{
return|return
name|useDb
condition|?
name|getDbRelationship
argument_list|(
operator|(
name|DbRelationship
operator|)
name|relationship
argument_list|)
else|:
name|getObjRelationship
argument_list|(
operator|(
name|ObjRelationship
operator|)
name|relationship
argument_list|)
return|;
block|}
specifier|private
name|DbRelationship
name|getDbRelationship
parameter_list|(
name|DbRelationship
name|dbRelationship
parameter_list|)
block|{
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
name|dbRelationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setToDependentPK
argument_list|(
name|dbRelationship
operator|.
name|isToDependentPK
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setToMany
argument_list|(
name|dbRelationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setTargetEntityName
argument_list|(
name|dbRelationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|dbRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setJoins
argument_list|(
name|rel
operator|.
name|getJoins
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|rel
return|;
block|}
specifier|private
name|ObjRelationship
name|getObjRelationship
parameter_list|(
name|ObjRelationship
name|objRelationship
parameter_list|)
block|{
name|ObjRelationship
name|rel
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
name|objRelationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setTargetEntityName
argument_list|(
name|objRelationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|objRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setDeleteRule
argument_list|(
name|objRelationship
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setUsedForLocking
argument_list|(
name|objRelationship
operator|.
name|isUsedForLocking
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setDbRelationshipPath
argument_list|(
name|objRelationship
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setCollectionType
argument_list|(
name|objRelationship
operator|.
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setMapKey
argument_list|(
name|objRelationship
operator|.
name|getMapKey
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|rel
return|;
block|}
block|}
end_class

end_unit

