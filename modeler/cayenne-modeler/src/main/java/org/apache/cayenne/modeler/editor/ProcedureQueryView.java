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
name|awt
operator|.
name|event
operator|.
name|ActionEvent
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
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|QueryEvent
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
name|QueryDisplayEvent
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
name|QueryDisplayListener
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
name|CellRenderers
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
name|Comparators
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
name|query
operator|.
name|AbstractQuery
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
name|query
operator|.
name|ProcedureQuery
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
name|query
operator|.
name|Query
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
name|PanelBuilder
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
name|CellConstraints
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

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureQueryView
extends|extends
name|JPanel
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|TextAdapter
name|name
decl_stmt|;
specifier|protected
name|JComboBox
name|queryRoot
decl_stmt|;
specifier|protected
name|SelectPropertiesPanel
name|properties
decl_stmt|;
specifier|public
name|ProcedureQueryView
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
comment|// create widgets
name|name
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setQueryName
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|queryRoot
operator|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|queryRoot
operator|.
name|setRenderer
argument_list|(
name|CellRenderers
operator|.
name|listRendererWithIcons
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
operator|new
name|RawQueryPropertiesPanel
argument_list|(
name|mediator
argument_list|)
block|{
specifier|protected
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|ProcedureQueryView
operator|.
name|this
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjEntity
name|getEntity
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
if|if
condition|(
name|query
operator|instanceof
name|ProcedureQuery
condition|)
block|{
return|return
name|ProcedureQueryView
operator|.
name|this
operator|.
name|getEntity
argument_list|(
operator|(
name|ProcedureQuery
operator|)
name|query
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
comment|// assemble
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:max(80dlu;pref), 3dlu, fill:max(200dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p"
argument_list|)
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
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
name|addSeparator
argument_list|(
literal|"ProcedureQuery Settings"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Query Name:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|name
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Procedure:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|queryRoot
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|properties
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|queryRoot
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
name|AbstractQuery
name|query
init|=
operator|(
name|AbstractQuery
operator|)
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|queryRoot
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|this
argument_list|,
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addQueryDisplayListener
argument_list|(
operator|new
name|QueryDisplayListener
argument_list|()
block|{
specifier|public
name|void
name|currentQueryChanged
parameter_list|(
name|QueryDisplayEvent
name|e
parameter_list|)
block|{
name|initFromModel
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates the view from the current model state. Invoked when a currently displayed      * query is changed.      */
name|void
name|initFromModel
parameter_list|()
block|{
name|Query
name|query
init|=
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|query
operator|instanceof
name|ProcedureQuery
operator|)
condition|)
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|ProcedureQuery
name|procedureQuery
init|=
operator|(
name|ProcedureQuery
operator|)
name|query
decl_stmt|;
name|properties
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|name
operator|.
name|setText
argument_list|(
name|procedureQuery
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// init root choices and title label..
comment|// - ProcedureQuery supports Procedure roots
comment|// TODO: now we only allow roots from the current map,
comment|// since query root is fully resolved during map loading,
comment|// making it impossible to reference other DataMaps.
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|Object
index|[]
name|roots
init|=
name|map
operator|.
name|getProcedures
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|roots
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|roots
argument_list|,
name|Comparators
operator|.
name|getDataMapChildrenComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DefaultComboBoxModel
name|model
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|roots
argument_list|)
decl_stmt|;
name|model
operator|.
name|setSelectedItem
argument_list|(
name|procedureQuery
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|queryRoot
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|properties
operator|.
name|initFromModel
argument_list|(
name|procedureQuery
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initializes Query name from string.      */
name|void
name|setQueryName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|newName
operator|!=
literal|null
operator|&&
name|newName
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
name|newName
operator|=
literal|null
expr_stmt|;
block|}
name|AbstractQuery
name|query
init|=
operator|(
name|AbstractQuery
operator|)
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
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
name|newName
argument_list|,
name|query
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|newName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Query name is required."
argument_list|)
throw|;
block|}
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getQuery
argument_list|(
name|newName
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// completely new name, set new name for entity
name|QueryEvent
name|e
init|=
operator|new
name|QueryEvent
argument_list|(
name|this
argument_list|,
name|query
argument_list|,
name|query
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectUtil
operator|.
name|setQueryName
argument_list|(
name|map
argument_list|,
name|query
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there is a query with the same name
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"There is another query named '"
operator|+
name|newName
operator|+
literal|"'. Use a different name."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns an entity that maps to a procedure query result class.      */
name|ObjEntity
name|getEntity
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|)
block|{
name|String
name|entityName
init|=
name|query
operator|.
name|getResultEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|map
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
return|;
block|}
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|Query
name|query
init|=
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|ProcedureQuery
condition|)
block|{
name|ProcedureQuery
name|procedureQuery
init|=
operator|(
name|ProcedureQuery
operator|)
name|query
decl_stmt|;
name|procedureQuery
operator|.
name|setResultEntityName
argument_list|(
name|entity
operator|!=
literal|null
condition|?
name|entity
operator|.
name|getName
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|this
argument_list|,
name|procedureQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

