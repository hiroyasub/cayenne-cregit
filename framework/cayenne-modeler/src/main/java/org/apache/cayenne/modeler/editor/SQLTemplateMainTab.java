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
name|Component
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|DefaultListCellRenderer
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
name|JList
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
name|CapsStrategy
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
name|query
operator|.
name|SQLTemplate
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
name|RowSpec
import|;
end_import

begin_comment
comment|/**  * A main panel for editing a SQLTemplate.  *   */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateMainTab
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CAPS_LABEL
init|=
literal|"Database Default"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOWER_CAPS_LABEL
init|=
literal|"Force Lower Case"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|UPPER_CAPS_LABEL
init|=
literal|"Force Upper Case"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|CapsStrategy
index|[]
name|LABEL_CAPITALIZATION
init|=
block|{
name|CapsStrategy
operator|.
name|DEFAULT
block|,
name|CapsStrategy
operator|.
name|LOWER
block|,
name|CapsStrategy
operator|.
name|UPPER
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|CapsStrategy
argument_list|,
name|String
argument_list|>
name|labelCapsLabels
init|=
operator|new
name|HashMap
argument_list|<
name|CapsStrategy
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|labelCapsLabels
operator|.
name|put
argument_list|(
name|CapsStrategy
operator|.
name|DEFAULT
argument_list|,
name|DEFAULT_CAPS_LABEL
argument_list|)
expr_stmt|;
name|labelCapsLabels
operator|.
name|put
argument_list|(
name|CapsStrategy
operator|.
name|LOWER
argument_list|,
name|LOWER_CAPS_LABEL
argument_list|)
expr_stmt|;
name|labelCapsLabels
operator|.
name|put
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|,
name|UPPER_CAPS_LABEL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|TextAdapter
name|name
decl_stmt|;
specifier|protected
name|SelectPropertiesPanel
name|properties
decl_stmt|;
specifier|public
name|SQLTemplateMainTab
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
name|properties
operator|=
operator|new
name|SQLTemplateQueryPropertiesPanel
argument_list|(
name|mediator
argument_list|)
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
literal|"p, 3dlu, p"
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
literal|"SQLTemplate Settings"
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
name|SQLTemplate
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
name|SQLTemplate
name|sqlQuery
init|=
operator|(
name|SQLTemplate
operator|)
name|query
decl_stmt|;
name|name
operator|.
name|setText
argument_list|(
name|sqlQuery
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|initFromModel
argument_list|(
name|sqlQuery
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SQLTemplate
name|getQuery
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
return|return
operator|(
name|query
operator|instanceof
name|SQLTemplate
operator|)
condition|?
operator|(
name|SQLTemplate
operator|)
name|query
else|:
literal|null
return|;
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
name|SQLTemplate
name|query
init|=
name|getQuery
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
name|SQLTemplate
name|query
parameter_list|)
block|{
return|return
name|query
operator|!=
literal|null
operator|&&
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|ObjEntity
condition|?
operator|(
name|ObjEntity
operator|)
name|query
operator|.
name|getRoot
argument_list|()
else|:
literal|null
return|;
block|}
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|SQLTemplate
name|template
init|=
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
comment|// in case of null entity, set root to DataMap
name|Object
name|root
init|=
name|entity
operator|!=
literal|null
condition|?
name|entity
else|:
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|template
operator|.
name|setRoot
argument_list|(
name|root
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
name|template
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|LabelCapsRenderer
extends|extends
name|DefaultListCellRenderer
block|{
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
name|list
parameter_list|,
name|Object
name|object
parameter_list|,
name|int
name|arg2
parameter_list|,
name|boolean
name|arg3
parameter_list|,
name|boolean
name|arg4
parameter_list|)
block|{
name|object
operator|=
name|labelCapsLabels
operator|.
name|get
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|object
argument_list|,
name|arg2
argument_list|,
name|arg3
argument_list|,
name|arg4
argument_list|)
return|;
block|}
block|}
specifier|final
class|class
name|SQLTemplateQueryPropertiesPanel
extends|extends
name|RawQueryPropertiesPanel
block|{
specifier|private
name|JComboBox
name|labelCase
decl_stmt|;
name|SQLTemplateQueryPropertiesPanel
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
specifier|protected
name|PanelBuilder
name|createPanelBuilder
parameter_list|()
block|{
name|labelCase
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableComboBox
argument_list|()
expr_stmt|;
name|labelCase
operator|.
name|setRenderer
argument_list|(
operator|new
name|LabelCapsRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|labelCase
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
name|Object
name|value
init|=
name|labelCase
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|setQueryProperty
argument_list|(
literal|"columnNamesCapitalization"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|PanelBuilder
name|builder
init|=
name|super
operator|.
name|createPanelBuilder
argument_list|()
decl_stmt|;
name|RowSpec
index|[]
name|extraRows
init|=
name|RowSpec
operator|.
name|decodeSpecs
argument_list|(
literal|"3dlu, p"
argument_list|)
decl_stmt|;
for|for
control|(
name|RowSpec
name|extraRow
range|:
name|extraRows
control|)
block|{
name|builder
operator|.
name|appendRow
argument_list|(
name|extraRow
argument_list|)
expr_stmt|;
block|}
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Row Label Case:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|17
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|labelCase
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|17
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
specifier|public
name|void
name|initFromModel
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|super
operator|.
name|initFromModel
argument_list|(
name|query
argument_list|)
expr_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|SQLTemplate
condition|)
block|{
name|SQLTemplate
name|template
init|=
operator|(
name|SQLTemplate
operator|)
name|query
decl_stmt|;
name|DefaultComboBoxModel
name|labelCaseModel
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|LABEL_CAPITALIZATION
argument_list|)
decl_stmt|;
name|labelCaseModel
operator|.
name|setSelectedItem
argument_list|(
name|template
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
name|labelCase
operator|.
name|setModel
argument_list|(
name|labelCaseModel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|SQLTemplateMainTab
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
name|SQLTemplate
condition|)
block|{
return|return
name|SQLTemplateMainTab
operator|.
name|this
operator|.
name|getEntity
argument_list|(
operator|(
name|SQLTemplate
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
empty_stmt|;
block|}
end_class

end_unit

