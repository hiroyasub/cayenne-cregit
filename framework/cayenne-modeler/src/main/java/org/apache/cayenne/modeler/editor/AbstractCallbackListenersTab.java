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
name|event
operator|.
name|ComponentAdapter
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
name|ComponentEvent
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
name|ItemEvent
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
name|ItemListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
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
name|EntityListener
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
name|EntityListenerEvent
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
name|EntityListenerListener
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
name|CayenneWidgetFactory
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

begin_comment
comment|/**  * Base abstract class for editing callback mapping on listener class  * Adds entity listener class processing logic  *  * @author Vasil Tarasevich  * @version 1.0 Oct 29, 2007  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCallbackListenersTab
extends|extends
name|AbstractCallbackMethodsTab
block|{
comment|/**      * listener class seiection combo      */
specifier|protected
name|JComboBox
name|listenerClassCombo
decl_stmt|;
comment|/**      * Constructor      * @param mediator mediator instance      */
specifier|protected
name|AbstractCallbackListenersTab
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return returns entity listeners list      */
specifier|protected
specifier|abstract
name|List
name|getEntityListeners
parameter_list|()
function_decl|;
comment|/**      * @return action for removing entity listeners      */
specifier|protected
specifier|abstract
name|CayenneAction
name|getRemoveEntityListenerAction
parameter_list|()
function_decl|;
comment|/**      * @return action for creating entity listeners      */
specifier|public
specifier|abstract
name|CayenneAction
name|getCreateEntityListenerAction
parameter_list|()
function_decl|;
comment|/**      * performs GUI components initialization      */
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getCreateEntityListenerAction
argument_list|()
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
name|toolBar
operator|.
name|add
argument_list|(
name|getRemoveEntityListenerAction
argument_list|()
operator|.
name|buildButton
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|EntityListener
name|getEntityListener
parameter_list|(
name|String
name|listenerClass
parameter_list|)
function_decl|;
specifier|private
name|void
name|processEditedListenerClassValue
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|String
name|prevName
init|=
name|mediator
operator|.
name|getCurrentListenerClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|getEntityListener
argument_list|(
name|newValue
argument_list|)
operator|==
literal|null
condition|)
block|{
name|EntityListener
name|listener
init|=
name|getEntityListener
argument_list|(
name|prevName
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|setClassName
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireEntityListenerEvent
argument_list|(
operator|new
name|EntityListenerEvent
argument_list|(
name|this
argument_list|,
name|prevName
argument_list|,
name|newValue
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * init listeners      */
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|super
operator|.
name|initController
argument_list|()
expr_stmt|;
name|addComponentListener
argument_list|(
operator|new
name|ComponentAdapter
argument_list|()
block|{
specifier|public
name|void
name|componentShown
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|rebuildListenerClassCombo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|setCurrentCallbackType
argument_list|(
operator|(
name|CallbackType
operator|)
name|callbackTypeCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|updateCallbackTypeCounters
argument_list|()
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|listenerClassCombo
operator|.
name|addItemListener
argument_list|(
operator|new
name|ItemListener
argument_list|()
block|{
specifier|public
name|void
name|itemStateChanged
parameter_list|(
name|ItemEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|SELECTED
operator|&&
name|isVisible
argument_list|()
condition|)
block|{
comment|//detect editing
if|if
condition|(
name|listenerClassCombo
operator|.
name|getSelectedIndex
argument_list|()
operator|==
operator|-
literal|1
operator|&&
name|listenerClassCombo
operator|.
name|getSelectedItem
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processEditedListenerClassValue
argument_list|(
operator|(
name|String
operator|)
name|listenerClassCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//just celeection changed
name|mediator
operator|.
name|setCurrentListenerClass
argument_list|(
operator|(
name|String
operator|)
name|listenerClassCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|updateCallbackTypeCounters
argument_list|()
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addEntityListenerListener
argument_list|(
operator|new
name|EntityListenerListener
argument_list|()
block|{
specifier|public
name|void
name|entityListenerAdded
parameter_list|(
name|EntityListenerEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|isVisible
argument_list|()
operator|&&
name|getCreateEntityListenerAction
argument_list|()
operator|==
name|e
operator|.
name|getSource
argument_list|()
condition|)
block|{
name|rebuildListenerClassCombo
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|entityListenerChanged
parameter_list|(
name|EntityListenerEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|isVisible
argument_list|()
operator|&&
name|e
operator|.
name|getSource
argument_list|()
operator|==
name|AbstractCallbackListenersTab
operator|.
name|this
condition|)
block|{
name|rebuildListenerClassCombo
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|entityListenerRemoved
parameter_list|(
name|EntityListenerEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|isVisible
argument_list|()
operator|&&
name|getRemoveEntityListenerAction
argument_list|()
operator|==
name|e
operator|.
name|getSource
argument_list|()
condition|)
block|{
name|rebuildListenerClassCombo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|rebuildTable
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * rebuils listener class selection dropdown content and fires selection event      *      * @param selectedListener listener to be selected after rebuild      */
specifier|protected
name|void
name|rebuildListenerClassCombo
parameter_list|(
name|String
name|selectedListener
parameter_list|)
block|{
name|List
name|entityListeners
init|=
name|getEntityListeners
argument_list|()
decl_stmt|;
name|List
name|listenerClasses
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityListeners
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|getEntityListeners
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|EntityListener
name|entityListener
init|=
operator|(
name|EntityListener
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|listenerClasses
operator|.
name|add
argument_list|(
name|entityListener
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|listenerClassCombo
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|listenerClasses
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getCreateCallbackMethodAction
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|listenerClasses
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|selectedListener
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|listenerClasses
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|listenerClassCombo
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|listenerClassCombo
operator|.
name|setSelectedItem
argument_list|(
name|selectedListener
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|setCurrentListenerClass
argument_list|(
operator|(
name|String
operator|)
name|listenerClassCombo
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|getRemoveEntityListenerAction
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|listenerClasses
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|listenerClassCombo
operator|.
name|setEnabled
argument_list|(
name|listenerClasses
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * adds listener class dropdown to filter bar      * @param builder filter forms builder      */
specifier|protected
name|void
name|buildFilter
parameter_list|(
name|DefaultFormBuilder
name|builder
parameter_list|)
block|{
name|listenerClassCombo
operator|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|listenerClassCombo
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"Listener class:"
argument_list|)
argument_list|,
name|listenerClassCombo
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|super
operator|.
name|buildFilter
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

