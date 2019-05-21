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
name|dba
operator|.
name|TypesMapping
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
name|NameBuilder
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
name|Entity
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
name|event
operator|.
name|AttributeDisplayEvent
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
name|EmbeddableAttributeDisplayEvent
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
name|CreateAttributeUndoableEdit
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
name|CreateEmbAttributeUndoableEdit
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CreateAttributeAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Constructor for CreateAttributeAction.      */
specifier|public
name|CreateAttributeAction
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
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Create Attribute"
return|;
block|}
specifier|static
name|void
name|fireEmbeddableAttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|Embeddable
name|embeddable
parameter_list|,
name|EmbeddableAttribute
name|attr
parameter_list|)
block|{
name|mediator
operator|.
name|fireEmbeddableAttributeEvent
argument_list|(
operator|new
name|EmbeddableAttributeEvent
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|embeddable
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|EmbeddableAttributeDisplayEvent
name|e
init|=
operator|new
name|EmbeddableAttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|embeddable
argument_list|,
name|attr
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
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
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireEmbeddableAttributeDisplayEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fires events when an obj attribute was added      */
specifier|static
name|void
name|fireObjAttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjAttribute
name|attr
parameter_list|)
block|{
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
operator|new
name|AttributeEvent
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|objEntity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
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
name|AttributeDisplayEvent
name|ade
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|objEntity
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
name|ade
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fires events when a db attribute was added      */
specifier|static
name|void
name|fireDbAttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|DbEntity
name|dbEntity
parameter_list|,
name|DbAttribute
name|attr
parameter_list|)
block|{
name|mediator
operator|.
name|fireDbAttributeEvent
argument_list|(
operator|new
name|AttributeEvent
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|dbEntity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|AttributeDisplayEvent
name|ade
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|dbEntity
argument_list|,
name|map
argument_list|,
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
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
name|ade
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-attribute.png"
return|;
block|}
comment|/**      * Creates ObjAttribute, DbAttribute depending on context.      */
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
if|if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentEmbeddable
argument_list|()
operator|!=
literal|null
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
name|attr
init|=
operator|new
name|EmbeddableAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|attr
argument_list|,
name|embeddable
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|createEmbAttribute
argument_list|(
name|embeddable
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateEmbAttributeUndoableEdit
argument_list|(
name|embeddable
argument_list|,
operator|new
name|EmbeddableAttribute
index|[]
block|{
name|attr
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjEntity
name|objEntity
init|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attr
init|=
operator|new
name|ObjAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|attr
argument_list|,
name|objEntity
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|createObjAttribute
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|objEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateAttributeUndoableEdit
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
name|objEntity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DbEntity
name|dbEntity
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
name|DbAttribute
name|attr
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|attr
argument_list|,
name|dbEntity
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|NOT_DEFINED
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|createDbAttribute
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|dbEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateAttributeUndoableEdit
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
name|dbEntity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|createEmbAttribute
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|EmbeddableAttribute
name|attr
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|embeddable
operator|.
name|addAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|fireEmbeddableAttributeEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|embeddable
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createObjAttribute
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjAttribute
name|attr
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|objEntity
operator|.
name|addAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|fireObjAttributeEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|map
argument_list|,
name|objEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createDbAttribute
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DbEntity
name|dbEntity
parameter_list|,
name|DbAttribute
name|attr
parameter_list|)
block|{
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|fireDbAttributeEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|map
argument_list|,
name|dbEntity
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains an Entity object.      */
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
if|if
condition|(
name|object
operator|instanceof
name|Attribute
condition|)
block|{
return|return
operator|(
operator|(
name|Attribute
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|Attribute
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|instanceof
name|Entity
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

