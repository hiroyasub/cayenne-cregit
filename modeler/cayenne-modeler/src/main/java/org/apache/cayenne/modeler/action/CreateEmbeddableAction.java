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
name|naming
operator|.
name|DefaultUniqueNameGenerator
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
name|naming
operator|.
name|NameCheckers
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
name|undo
operator|.
name|CreateEmbeddableUndoableEdit
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

begin_class
specifier|public
class|class
name|CreateEmbeddableAction
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
literal|"Create Embeddable"
return|;
block|}
specifier|public
name|CreateEmbeddableAction
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
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-new_embeddable.gif"
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
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|Embeddable
name|embeddable
init|=
operator|new
name|Embeddable
argument_list|(
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|Embeddable
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|createEmbeddable
argument_list|(
name|dataMap
argument_list|,
name|embeddable
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
name|CreateEmbeddableUndoableEdit
argument_list|(
name|dataMap
argument_list|,
name|embeddable
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createEmbeddable
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|dataMap
operator|.
name|addEmbeddable
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
name|fireEmbeddableEvent
argument_list|(
name|this
argument_list|,
name|getProjectController
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|embeddable
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|fireEmbeddableEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|mediator
operator|.
name|fireEmbeddableEvent
argument_list|(
operator|new
name|EmbeddableEvent
argument_list|(
name|src
argument_list|,
name|embeddable
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|EmbeddableDisplayEvent
name|displayEvent
init|=
operator|new
name|EmbeddableDisplayEvent
argument_list|(
name|src
argument_list|,
name|embeddable
argument_list|,
name|dataMap
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
name|displayEvent
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireEmbeddableDisplayEvent
argument_list|(
name|displayEvent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains a DataMap object.      */
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
name|ObjEntity
condition|)
block|{
return|return
operator|(
operator|(
name|ObjEntity
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
name|ObjEntity
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|instanceof
name|DataMap
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

