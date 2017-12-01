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
name|modeler
operator|.
name|action
operator|.
name|CreateRelationshipAction
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
name|action
operator|.
name|DbEntityCounterpartAction
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
name|action
operator|.
name|RemoveRelationshipAction
import|;
end_import

begin_class
specifier|public
class|class
name|RemoveRelationshipUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|private
name|ObjRelationship
index|[]
name|rels
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|DbRelationship
index|[]
name|dbRels
decl_stmt|;
specifier|public
name|RemoveRelationshipUndoableEdit
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjRelationship
index|[]
name|rels
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|objEntity
operator|=
name|objEntity
expr_stmt|;
name|this
operator|.
name|rels
operator|=
name|rels
expr_stmt|;
block|}
specifier|public
name|RemoveRelationshipUndoableEdit
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbRelationship
index|[]
name|dbRels
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
name|this
operator|.
name|dbRels
operator|=
name|dbRels
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Remove Obj Relationship"
return|;
block|}
else|else
block|{
return|return
literal|"Remove Db Relationship"
return|;
block|}
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
name|RemoveRelationshipAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveRelationshipAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeObjRelationships
argument_list|(
name|objEntity
argument_list|,
name|rels
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|action
operator|.
name|removeDbRelationships
argument_list|(
name|dbEntity
argument_list|,
name|dbRels
argument_list|)
expr_stmt|;
block|}
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
name|CreateRelationshipAction
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateRelationshipAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjRelationship
name|r
range|:
name|rels
control|)
block|{
name|action
operator|.
name|createObjRelationship
argument_list|(
name|objEntity
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
name|focusObjEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|DbRelationship
name|dr
range|:
name|dbRels
control|)
block|{
name|action
operator|.
name|createDbRelationship
argument_list|(
name|dbEntity
argument_list|,
name|dr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|focusObjEntity
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|actionManager
operator|.
name|getAction
argument_list|(
name|DbEntityCounterpartAction
operator|.
name|class
argument_list|)
operator|.
name|viewCounterpartEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

