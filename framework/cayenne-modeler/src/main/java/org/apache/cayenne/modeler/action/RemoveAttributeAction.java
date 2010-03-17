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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNode
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
name|map
operator|.
name|Attribute
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
name|map
operator|.
name|event
operator|.
name|AttributeEvent
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
name|EmbeddableAttributeEvent
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
name|ConfirmRemoveDialog
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
name|RemoveAttributeUndoableEdit
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
name|ProjectUtil
import|;
end_import

begin_comment
comment|/**  * Removes currently selected attribute from either the DbEntity or ObjEntity.  *   */
end_comment

begin_class
specifier|public
class|class
name|RemoveAttributeAction
extends|extends
name|RemoveAction
implements|implements
name|MultipleObjectsAction
block|{
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Remove Attribute"
decl_stmt|;
comment|/**      * Name of action if multiple rels are selected      */
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME_MULTIPLE
init|=
literal|"Remove Attributes"
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
specifier|public
name|String
name|getActionName
parameter_list|(
name|boolean
name|multiple
parameter_list|)
block|{
return|return
name|multiple
condition|?
name|ACTION_NAME_MULTIPLE
else|:
name|ACTION_NAME
return|;
block|}
specifier|public
name|RemoveAttributeAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable      * attribute.      */
annotation|@
name|Override
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|object
operator|instanceof
name|Attribute
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|,
name|boolean
name|allowAsking
parameter_list|)
block|{
name|ConfirmRemoveDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|(
name|allowAsking
argument_list|)
decl_stmt|;
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|EmbeddableAttribute
index|[]
name|embAttrs
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentEmbAttrs
argument_list|()
decl_stmt|;
name|ObjAttribute
index|[]
name|attrs
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|embAttrs
operator|!=
literal|null
operator|&&
name|embAttrs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|embAttrs
operator|.
name|length
operator|==
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"Embeddable Attribute"
argument_list|,
name|embAttrs
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|embAttrs
operator|.
name|length
operator|>
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"selected EmbAttributes"
argument_list|)
operator|)
condition|)
block|{
name|Embeddable
name|embeddable
init|=
name|mediator
operator|.
name|getCurrentEmbeddable
argument_list|()
decl_stmt|;
name|EmbeddableAttribute
index|[]
name|eAttrs
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentEmbAttrs
argument_list|()
decl_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveAttributeUndoableEdit
argument_list|(
name|embeddable
argument_list|,
name|eAttrs
argument_list|)
argument_list|)
expr_stmt|;
name|removeEmbeddableAttributes
argument_list|(
name|embeddable
argument_list|,
name|eAttrs
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|attrs
operator|!=
literal|null
operator|&&
name|attrs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|attrs
operator|.
name|length
operator|==
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"ObjAttribute"
argument_list|,
name|attrs
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|attrs
operator|.
name|length
operator|>
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"selected ObjAttributes"
argument_list|)
operator|)
condition|)
block|{
name|ObjEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
name|ObjAttribute
index|[]
name|attribs
init|=
name|mediator
operator|.
name|getCurrentObjAttributes
argument_list|()
decl_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveAttributeUndoableEdit
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
argument_list|,
name|entity
argument_list|,
name|attribs
argument_list|)
argument_list|)
expr_stmt|;
name|removeObjAttributes
argument_list|(
name|entity
argument_list|,
name|attribs
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|DbAttribute
index|[]
name|dbAttrs
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttrs
operator|!=
literal|null
operator|&&
name|dbAttrs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|dbAttrs
operator|.
name|length
operator|==
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"DbAttribute"
argument_list|,
name|dbAttrs
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|dbAttrs
operator|.
name|length
operator|>
literal|1
operator|&&
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"selected DbAttributes"
argument_list|)
operator|)
condition|)
block|{
name|DbEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
name|DbAttribute
index|[]
name|attribs
init|=
name|mediator
operator|.
name|getCurrentDbAttributes
argument_list|()
decl_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveAttributeUndoableEdit
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
argument_list|,
name|entity
argument_list|,
name|attribs
argument_list|)
argument_list|)
expr_stmt|;
name|removeDbAttributes
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|entity
argument_list|,
name|attribs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|removeDbAttributes
parameter_list|(
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
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attrib
range|:
name|attribs
control|)
block|{
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrib
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeEvent
name|e
init|=
operator|new
name|AttributeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|attrib
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbAttributeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|ProjectUtil
operator|.
name|cleanObjMappings
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeObjAttributes
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ObjAttribute
index|[]
name|attribs
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|attrib
range|:
name|attribs
control|)
block|{
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrib
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeEvent
name|e
init|=
operator|new
name|AttributeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|attrib
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeEmbeddableAttributes
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|EmbeddableAttribute
index|[]
name|attrs
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
for|for
control|(
name|EmbeddableAttribute
name|attrib
range|:
name|attrs
control|)
block|{
name|embeddable
operator|.
name|removeAttribute
argument_list|(
name|attrib
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddableAttributeEvent
name|e
init|=
operator|new
name|EmbeddableAttributeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|attrib
argument_list|,
name|embeddable
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireEmbeddableAttributeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

