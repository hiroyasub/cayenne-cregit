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
name|Embeddable
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
name|EmbeddableAttribute
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
name|modeler
operator|.
name|action
operator|.
name|CreateAttributeAction
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
name|RemoveAttributeAction
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
name|event
operator|.
name|EmbeddableDisplayEvent
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
name|event
operator|.
name|EntityDisplayEvent
import|;
end_import

begin_class
specifier|public
class|class
name|RemoveAttributeUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|DataDomain
name|domain
decl_stmt|;
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|DbAttribute
index|[]
name|dbAttributes
decl_stmt|;
specifier|private
name|ObjAttribute
index|[]
name|objAttributes
decl_stmt|;
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|Embeddable
name|embeddable
decl_stmt|;
specifier|private
name|EmbeddableAttribute
index|[]
name|embeddableAttrs
decl_stmt|;
specifier|public
name|RemoveAttributeUndoableEdit
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|EmbeddableAttribute
index|[]
name|embeddableAttrs
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddable
operator|=
name|embeddable
expr_stmt|;
name|this
operator|.
name|embeddableAttrs
operator|=
name|embeddableAttrs
expr_stmt|;
block|}
specifier|public
name|RemoveAttributeUndoableEdit
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|ObjEntity
name|entity
parameter_list|,
name|ObjAttribute
index|[]
name|attribs
parameter_list|)
block|{
name|this
operator|.
name|objEntity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|objAttributes
operator|=
name|attribs
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
specifier|public
name|RemoveAttributeUndoableEdit
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
index|[]
name|attribs
parameter_list|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|dbAttributes
operator|=
name|attribs
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
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
name|restoreSelections
argument_list|()
expr_stmt|;
name|RemoveAttributeAction
name|action
init|=
operator|(
name|RemoveAttributeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
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
name|removeObjAttributes
argument_list|(
name|objEntity
argument_list|,
name|objAttributes
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|objEntity
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeDbAttributes
argument_list|(
name|dbEntity
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|dbEntity
argument_list|,
name|dbAttributes
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|dbEntity
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddable
operator|!=
literal|null
condition|)
block|{
name|action
operator|.
name|removeEmbeddableAttributes
argument_list|(
name|embeddable
argument_list|,
name|embeddableAttrs
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireEmbeddableDisplayEvent
argument_list|(
operator|new
name|EmbeddableDisplayEvent
argument_list|(
name|this
argument_list|,
name|embeddable
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
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
name|restoreSelections
argument_list|()
expr_stmt|;
name|CreateAttributeAction
name|action
init|=
operator|(
name|CreateAttributeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateAttributeAction
operator|.
name|getActionName
argument_list|()
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
name|ObjAttribute
name|attr
range|:
name|objAttributes
control|)
block|{
name|action
operator|.
name|createObjAttribute
argument_list|(
name|domain
argument_list|,
name|dataMap
argument_list|,
name|objEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DbAttribute
name|attr
range|:
name|dbAttributes
control|)
block|{
name|action
operator|.
name|createDbAttribute
argument_list|(
name|domain
argument_list|,
name|dataMap
argument_list|,
name|dbEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|embeddable
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EmbeddableAttribute
name|attr
range|:
name|embeddableAttrs
control|)
block|{
name|action
operator|.
name|createEmbAttribute
argument_list|(
name|embeddable
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|(
name|objAttributes
operator|.
name|length
operator|>
literal|1
operator|)
condition|?
literal|"Remove ObjAttributes"
else|:
literal|"Remove ObjAttribute"
return|;
block|}
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|dbAttributes
operator|.
name|length
operator|>
literal|1
operator|)
condition|?
literal|"Remove DbAttributes"
else|:
literal|"Remove DbAttribute"
return|;
block|}
if|if
condition|(
name|embeddableAttrs
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|embeddableAttrs
operator|.
name|length
operator|>
literal|1
operator|)
condition|?
literal|"Remove Embeddable Attributes"
else|:
literal|"Remove Embeddable Attribute"
return|;
block|}
return|return
name|super
operator|.
name|getPresentationName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

