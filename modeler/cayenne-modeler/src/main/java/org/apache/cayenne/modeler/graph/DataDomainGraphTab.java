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
name|graph
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
name|awt
operator|.
name|FlowLayout
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
name|JDialog
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
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|DomainDisplayEvent
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
name|DomainDisplayListener
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
name|graph
operator|.
name|action
operator|.
name|RebuildGraphAction
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
name|graph
operator|.
name|action
operator|.
name|SaveAsImageAction
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
name|graph
operator|.
name|action
operator|.
name|ZoomInAction
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
name|graph
operator|.
name|action
operator|.
name|ZoomOutAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|JGraph
import|;
end_import

begin_comment
comment|/**  * Tab for editing graphical representation of a dataDomain  */
end_comment

begin_class
specifier|public
class|class
name|DataDomainGraphTab
extends|extends
name|JPanel
implements|implements
name|DomainDisplayListener
implements|,
name|ItemListener
block|{
comment|/**      * mediator instance      */
name|ProjectController
name|mediator
decl_stmt|;
comment|/**      * Diagram selection combo      */
name|JComboBox
argument_list|<
name|String
argument_list|>
name|diagramCombo
decl_stmt|;
comment|/**      * Scrollpane that the graph will be added to      */
name|JScrollPane
name|scrollPane
decl_stmt|;
comment|/**      * Current graph      */
name|JGraph
name|graph
decl_stmt|;
comment|/**      * Current domain      */
name|DataChannelDescriptor
name|domain
decl_stmt|;
comment|/**      * True to invoke rebuild next time component becomes visible      */
name|boolean
name|needRebuild
decl_stmt|;
name|GraphRegistry
name|graphRegistry
decl_stmt|;
specifier|public
name|DataDomainGraphTab
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
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|needRebuild
operator|=
literal|true
expr_stmt|;
name|mediator
operator|.
name|addDomainDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|JToolBar
name|toolbar
init|=
operator|new
name|JToolBar
argument_list|()
decl_stmt|;
name|toolbar
operator|.
name|setFloatable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|setLayout
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEFT
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|GraphType
index|[]
name|types
init|=
name|GraphType
operator|.
name|values
argument_list|()
decl_stmt|;
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[
name|types
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|names
index|[
name|i
index|]
operator|=
name|types
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|diagramCombo
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|names
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|diagramCombo
operator|.
name|addItemListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
operator|new
name|RebuildGraphAction
argument_list|(
name|this
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
operator|new
name|SaveAsImageAction
argument_list|(
name|this
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
operator|new
name|ZoomInAction
argument_list|(
name|this
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
operator|new
name|ZoomOutAction
argument_list|(
name|this
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|buildButton
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"Diagram: "
argument_list|)
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
name|diagramCombo
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|toolbar
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|scrollPane
operator|=
operator|new
name|JScrollPane
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|scrollPane
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDomainChanged
parameter_list|(
name|DomainDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|EntityDisplayEvent
condition|)
block|{
comment|// selecting an event
comment|// choose type of diagram
name|Entity
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|entity
init|=
operator|(
operator|(
name|EntityDisplayEvent
operator|)
name|e
operator|)
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|diagramCombo
operator|.
name|setSelectedIndex
argument_list|(
name|entity
operator|instanceof
name|ObjEntity
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
name|refresh
argument_list|()
expr_stmt|;
name|GraphBuilder
name|builder
init|=
name|getGraphRegistry
argument_list|()
operator|.
name|getGraphMap
argument_list|(
name|domain
argument_list|)
operator|.
name|get
argument_list|(
name|getSelectedType
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|cell
init|=
name|builder
operator|.
name|getEntityCell
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cell
operator|!=
literal|null
condition|)
block|{
name|graph
operator|.
name|setSelectionCell
argument_list|(
name|cell
argument_list|)
expr_stmt|;
name|graph
operator|.
name|scrollCellToVisible
argument_list|(
name|cell
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|domain
operator|!=
name|e
operator|.
name|getDomain
argument_list|()
condition|)
block|{
name|needRebuild
operator|=
literal|true
expr_stmt|;
name|domain
operator|=
name|e
operator|.
name|getDomain
argument_list|()
expr_stmt|;
if|if
condition|(
name|isVisible
argument_list|()
condition|)
block|{
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Rebuilds graph from a domain, if it is not yet built Otherwise, takes it      * from cache      */
specifier|public
name|void
name|refresh
parameter_list|()
block|{
if|if
condition|(
name|needRebuild
operator|&&
name|domain
operator|!=
literal|null
condition|)
block|{
name|graph
operator|=
name|getGraphRegistry
argument_list|()
operator|.
name|loadGraph
argument_list|(
name|mediator
argument_list|,
name|domain
argument_list|,
name|getSelectedType
argument_list|()
argument_list|)
expr_stmt|;
name|scrollPane
operator|.
name|setViewportView
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|needRebuild
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
name|GraphType
name|getSelectedType
parameter_list|()
block|{
return|return
name|GraphType
operator|.
name|values
argument_list|()
index|[
name|diagramCombo
operator|.
name|getSelectedIndex
argument_list|()
index|]
return|;
block|}
comment|/**      * Rebuilds graph, deleting existing if needed      */
specifier|public
name|void
name|rebuild
parameter_list|()
block|{
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|JOptionPane
name|pane
init|=
operator|new
name|JOptionPane
argument_list|(
literal|"Rebuilding graph from domain will cause all user"
operator|+
literal|" changes to be lost. Continue?"
argument_list|,
name|JOptionPane
operator|.
name|QUESTION_MESSAGE
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|)
decl_stmt|;
name|JDialog
name|dialog
init|=
name|pane
operator|.
name|createDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Confirm Rebuild"
argument_list|)
decl_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|pane
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|JOptionPane
operator|.
name|YES_OPTION
argument_list|)
condition|)
block|{
name|getGraphRegistry
argument_list|()
operator|.
name|getGraphMap
argument_list|(
name|domain
argument_list|)
operator|.
name|remove
argument_list|(
name|getSelectedType
argument_list|()
argument_list|)
expr_stmt|;
name|itemStateChanged
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|itemStateChanged
parameter_list|(
name|ItemEvent
name|e
parameter_list|)
block|{
name|needRebuild
operator|=
literal|true
expr_stmt|;
name|refresh
argument_list|()
expr_stmt|;
block|}
specifier|public
name|JGraph
name|getGraph
parameter_list|()
block|{
return|return
name|graph
return|;
block|}
name|GraphRegistry
name|getGraphRegistry
parameter_list|()
block|{
name|graphRegistry
operator|=
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|domain
argument_list|,
name|GraphRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|graphRegistry
operator|==
literal|null
condition|)
block|{
name|graphRegistry
operator|=
operator|new
name|GraphRegistry
argument_list|()
expr_stmt|;
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|add
argument_list|(
name|domain
argument_list|,
name|graphRegistry
argument_list|)
expr_stmt|;
block|}
return|return
name|graphRegistry
return|;
block|}
block|}
end_class

end_unit

