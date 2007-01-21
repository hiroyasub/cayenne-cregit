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
comment|/**  * Tabbed ObjEntity editor panel.  *   * @author Michael Misha Shengaout  * @author Andrus Adamchik  */
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
name|ObjEntityRelationshipTab
name|relationshipsPanel
decl_stmt|;
specifier|protected
name|ObjEntityAttributeTab
name|attributesPanel
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
name|ObjEntityTab
name|entityPanel
init|=
operator|new
name|ObjEntityTab
argument_list|(
name|mediator
argument_list|)
decl_stmt|;
name|addTab
argument_list|(
literal|"Entity"
argument_list|,
operator|new
name|JScrollPane
argument_list|(
name|entityPanel
argument_list|)
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
block|}
specifier|public
name|void
name|currentObjEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
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
name|rel
init|=
name|e
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|rel
operator|instanceof
name|ObjRelationship
condition|)
block|{
if|if
condition|(
name|getSelectedComponent
argument_list|()
operator|!=
name|relationshipsPanel
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
name|selectRelationship
argument_list|(
operator|(
name|ObjRelationship
operator|)
name|rel
argument_list|)
expr_stmt|;
block|}
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
name|attr
init|=
name|e
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|attr
operator|instanceof
name|ObjAttribute
condition|)
block|{
if|if
condition|(
name|getSelectedComponent
argument_list|()
operator|!=
name|attributesPanel
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
name|selectAttribute
argument_list|(
operator|(
name|ObjAttribute
operator|)
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

