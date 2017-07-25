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
name|editor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BorderLayout
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
name|EventObject
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JToolBar
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
name|EmbeddableEvent
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
name|action
operator|.
name|ActionManager
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
name|EmbeddableDisplayListener
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
name|TextAdapter
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
name|project
operator|.
name|extension
operator|.
name|info
operator|.
name|ObjectInfo
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_class
specifier|public
class|class
name|EmbeddableTab
extends|extends
name|JPanel
implements|implements
name|EmbeddableDisplayListener
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|TextAdapter
name|className
decl_stmt|;
specifier|protected
name|TextAdapter
name|comment
decl_stmt|;
specifier|public
name|EmbeddableTab
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addEmbeddableDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|JToolBar
name|toolBar
init|=
operator|new
name|JToolBar
argument_list|()
decl_stmt|;
name|toolBar
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|setFloatable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ActionManager
name|actionManager
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
decl_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|actionManager
operator|.
name|getAction
argument_list|(
name|CreateAttributeAction
operator|.
name|class
argument_list|)
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|toolBar
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|className
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setClassName
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|comment
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setComment
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:50dlu, 3dlu, fill:150dlu, 3dlu, fill:100"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Class Name:"
argument_list|,
name|className
operator|.
name|getComponent
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Comment:"
argument_list|,
name|comment
operator|.
name|getComponent
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
name|void
name|setClassName
parameter_list|(
name|String
name|newClassName
parameter_list|)
block|{
if|if
condition|(
name|newClassName
operator|!=
literal|null
operator|&&
name|newClassName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|newClassName
operator|=
literal|null
expr_stmt|;
block|}
name|Embeddable
name|embeddable
init|=
name|mediator
operator|.
name|getCurrentEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddable
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|newClassName
argument_list|,
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|newClassName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Embeddable name is required."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|embeddable
operator|.
name|getDataMap
argument_list|()
operator|.
name|getEmbeddable
argument_list|(
name|newClassName
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// if newClassName dupliucates in other DataMaps
name|DataChannelDescriptor
name|domain
init|=
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
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DataMap
name|nextMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
if|if
condition|(
name|nextMap
operator|==
name|embeddable
operator|.
name|getDataMap
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Embeddable
name|conflictingEmbeddable
init|=
name|nextMap
operator|.
name|getEmbeddable
argument_list|(
name|newClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|conflictingEmbeddable
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Duplicate Embeddable name in another DataMap: "
operator|+
name|newClassName
operator|+
literal|"."
argument_list|)
throw|;
block|}
block|}
block|}
comment|// completely new name, set new name for embeddable
name|EmbeddableEvent
name|e
init|=
operator|new
name|EmbeddableEvent
argument_list|(
name|this
argument_list|,
name|embeddable
argument_list|,
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|oldName
init|=
name|embeddable
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|embeddable
operator|.
name|setClassName
argument_list|(
name|newClassName
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireEmbeddableEvent
argument_list|(
name|e
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
operator|(
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
operator|)
operator|.
name|getDataMaps
argument_list|()
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
name|DataMap
name|dataMap
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ObjEntity
argument_list|>
name|ent
init|=
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ent
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|attr
init|=
name|ent
operator|.
name|next
argument_list|()
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ObjAttribute
argument_list|>
name|attrIt
init|=
name|attr
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|attrIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjAttribute
name|atribute
init|=
name|attrIt
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|atribute
operator|.
name|getType
argument_list|()
operator|==
literal|null
operator|||
name|atribute
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|oldName
argument_list|)
condition|)
block|{
name|atribute
operator|.
name|setType
argument_list|(
name|newClassName
argument_list|)
expr_stmt|;
name|AttributeEvent
name|ev
init|=
operator|new
name|AttributeEvent
argument_list|(
name|this
argument_list|,
name|atribute
argument_list|,
name|atribute
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
name|ev
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
else|else
block|{
comment|// there is an embeddable with the same name
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"There is another embeddable with name '"
operator|+
name|newClassName
operator|+
literal|"'."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|currentEmbeddableChanged
parameter_list|(
name|EmbeddableDisplayEvent
name|e
parameter_list|)
block|{
name|Embeddable
name|embeddable
init|=
name|e
operator|.
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddable
operator|==
literal|null
operator|||
operator|!
name|e
operator|.
name|isEmbeddableChanged
argument_list|()
condition|)
block|{
return|return;
block|}
name|initFromModel
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initFromModel
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|className
operator|.
name|setText
argument_list|(
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|comment
operator|.
name|setText
argument_list|(
name|getComment
argument_list|(
name|embeddable
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|setComment
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
name|Embeddable
name|embeddable
init|=
name|mediator
operator|.
name|getCurrentEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddable
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ObjectInfo
operator|.
name|putToMetaData
argument_list|(
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|embeddable
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireEmbeddableEvent
argument_list|(
operator|new
name|EmbeddableEvent
argument_list|(
name|this
argument_list|,
name|embeddable
argument_list|)
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|getComment
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
return|return
name|ObjectInfo
operator|.
name|getFromMetaData
argument_list|(
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|embeddable
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

