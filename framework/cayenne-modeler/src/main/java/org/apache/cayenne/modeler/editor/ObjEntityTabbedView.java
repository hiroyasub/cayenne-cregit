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
name|Component
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTabbedPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeListener
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
name|action
operator|.
name|RemoveCallbackMethodAction
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
name|EntityDisplayEvent
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
name|ObjAttributeDisplayListener
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
name|ObjEntityDisplayListener
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
name|ObjRelationshipDisplayListener
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
name|RelationshipDisplayEvent
import|;
end_import

begin_comment
comment|/**  * Tabbed ObjEntity editor panel.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjEntityTabbedView
extends|extends
name|JTabbedPane
implements|implements
name|ObjEntityDisplayListener
implements|,
name|ObjRelationshipDisplayListener
implements|,
name|ObjAttributeDisplayListener
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|Component
name|entityPanel
decl_stmt|;
specifier|protected
name|ObjEntityRelationshipTab
name|relationshipsPanel
decl_stmt|;
specifier|protected
name|ObjEntityAttributeTab
name|attributesPanel
decl_stmt|;
comment|/**      * callback methods on ObjEntity tab      */
specifier|protected
name|AbstractCallbackMethodsTab
name|callbacksPanel
decl_stmt|;
comment|/**      * callback methods on ObjEntity's entity listeners tab      */
specifier|protected
name|JPanel
name|listenersPanel
decl_stmt|;
specifier|public
name|ObjEntityTabbedView
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
name|initView
parameter_list|()
block|{
name|setTabPlacement
argument_list|(
name|JTabbedPane
operator|.
name|TOP
argument_list|)
expr_stmt|;
comment|// add panels to tabs
comment|// note that those panels that have no internal scrollable tables
comment|// must be wrapped in a scroll pane
name|entityPanel
operator|=
operator|new
name|JScrollPane
argument_list|(
operator|new
name|ObjEntityTab
argument_list|(
name|mediator
argument_list|)
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"Entity"
argument_list|,
name|entityPanel
argument_list|)
expr_stmt|;
name|attributesPanel
operator|=
operator|new
name|ObjEntityAttributeTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"Attributes"
argument_list|,
name|attributesPanel
argument_list|)
expr_stmt|;
name|relationshipsPanel
operator|=
operator|new
name|ObjEntityRelationshipTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"Relationships"
argument_list|,
name|relationshipsPanel
argument_list|)
expr_stmt|;
name|callbacksPanel
operator|=
operator|new
name|ObjEntityCallbackMethodsTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"Callbacks"
argument_list|,
name|callbacksPanel
argument_list|)
expr_stmt|;
name|listenersPanel
operator|=
operator|new
name|ObjEntityCallbackListenersTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"Listeners"
argument_list|,
name|listenersPanel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addObjEntityDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjAttributeDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjRelationshipDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|addChangeListener
argument_list|(
operator|new
name|ChangeListener
argument_list|()
block|{
specifier|public
name|void
name|stateChanged
parameter_list|(
name|ChangeEvent
name|e
parameter_list|)
block|{
name|resetRemoveButtons
argument_list|()
expr_stmt|;
name|Component
name|selected
init|=
name|getSelectedComponent
argument_list|()
decl_stmt|;
while|while
condition|(
name|selected
operator|instanceof
name|JScrollPane
condition|)
block|{
name|selected
operator|=
operator|(
operator|(
name|JScrollPane
operator|)
name|selected
operator|)
operator|.
name|getViewport
argument_list|()
operator|.
name|getView
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|selected
operator|instanceof
name|ExistingSelectionProcessor
condition|)
block|{
operator|(
operator|(
name|ExistingSelectionProcessor
operator|)
name|selected
operator|)
operator|.
name|processExistingSelection
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/** Reset the remove buttons */
specifier|private
name|void
name|resetRemoveButtons
parameter_list|()
block|{
name|Application
name|app
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|app
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|app
operator|.
name|getAction
argument_list|(
name|RemoveRelationshipAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|app
operator|.
name|getAction
argument_list|(
name|RemoveCallbackMethodAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentObjEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
name|Entity
name|entity
init|=
name|e
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|isMainTabFocus
argument_list|()
operator|&&
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|getSelectedComponent
argument_list|()
operator|!=
name|entityPanel
condition|)
block|{
name|setSelectedComponent
argument_list|(
name|entityPanel
argument_list|)
expr_stmt|;
name|entityPanel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|resetRemoveButtons
argument_list|()
expr_stmt|;
name|setVisible
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentObjRelationshipChanged
parameter_list|(
name|RelationshipDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// update relationship selection
name|Relationship
index|[]
name|rels
init|=
name|e
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|ObjRelationship
index|[]
name|objRels
init|=
operator|new
name|ObjRelationship
index|[
name|rels
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|rels
argument_list|,
literal|0
argument_list|,
name|objRels
argument_list|,
literal|0
argument_list|,
name|rels
operator|.
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSelectedComponent
argument_list|()
operator|!=
name|relationshipsPanel
operator|&&
name|objRels
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|setSelectedComponent
argument_list|(
name|relationshipsPanel
argument_list|)
expr_stmt|;
name|relationshipsPanel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|relationshipsPanel
operator|.
name|selectRelationships
argument_list|(
name|objRels
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentObjAttributeChanged
parameter_list|(
name|AttributeDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
condition|)
return|return;
comment|// update relationship selection
name|Attribute
index|[]
name|attrs
init|=
name|e
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|ObjAttribute
index|[]
name|objAttrs
init|=
operator|new
name|ObjAttribute
index|[
name|attrs
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|attrs
argument_list|,
literal|0
argument_list|,
name|objAttrs
argument_list|,
literal|0
argument_list|,
name|attrs
operator|.
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSelectedComponent
argument_list|()
operator|!=
name|attributesPanel
operator|&&
name|objAttrs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|setSelectedComponent
argument_list|(
name|attributesPanel
argument_list|)
expr_stmt|;
name|attributesPanel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|attributesPanel
operator|.
name|selectAttributes
argument_list|(
name|objAttrs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

