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
name|awt
operator|.
name|event
operator|.
name|FocusEvent
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
name|FocusListener
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
name|DefaultComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
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
name|configuration
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionException
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
name|exp
operator|.
name|parser
operator|.
name|ASTPath
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
name|ExpressionConvertor
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
name|modeler
operator|.
name|util
operator|.
name|ValidatorTextAdapter
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
name|combo
operator|.
name|AutoCompletion
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
name|SelectQuery
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
name|CayenneMapEntry
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
comment|/**  * A tabbed pane that contains editors for various SelectQuery parts.  *   */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryMainTab
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
name|TextAdapter
name|qualifier
decl_stmt|;
specifier|protected
name|JCheckBox
name|distinct
decl_stmt|;
specifier|protected
name|ObjectQueryPropertiesPanel
name|properties
decl_stmt|;
specifier|public
name|SelectQueryMainTab
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
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|queryRoot
argument_list|)
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
name|qualifier
operator|=
operator|new
name|ValidatorTextAdapter
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
name|setQueryQualifier
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|validate
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ValidationException
block|{
name|createQualifier
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|distinct
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|properties
operator|=
operator|new
name|ObjectQueryPropertiesPanel
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
literal|"right:max(80dlu;pref), 3dlu, fill:200dlu"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"
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
literal|"SelectQuery Settings"
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
literal|"Query Root:"
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
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Qualifier:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|qualifier
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
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Distinct:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|distinct
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|9
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
name|RootSelectionHandler
name|rootHandler
init|=
operator|new
name|RootSelectionHandler
argument_list|()
decl_stmt|;
name|queryRoot
operator|.
name|addActionListener
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|queryRoot
operator|.
name|addFocusListener
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|queryRoot
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|.
name|addFocusListener
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|distinct
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
name|SelectQuery
name|query
init|=
name|getQuery
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
name|setDistinct
argument_list|(
name|distinct
operator|.
name|isSelected
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
name|SelectQuery
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
name|SelectQuery
name|selectQuery
init|=
operator|(
name|SelectQuery
operator|)
name|query
decl_stmt|;
name|name
operator|.
name|setText
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|distinct
operator|.
name|setSelected
argument_list|(
name|selectQuery
operator|.
name|isDistinct
argument_list|()
argument_list|)
expr_stmt|;
name|qualifier
operator|.
name|setText
argument_list|(
name|selectQuery
operator|.
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|?
name|selectQuery
operator|.
name|getQualifier
argument_list|()
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
comment|// init root choices and title label..
comment|// - SelectQuery supports ObjEntity roots
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
name|getObjEntities
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
name|selectQuery
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
name|selectQuery
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SelectQuery
name|getQuery
parameter_list|()
block|{
return|return
operator|(
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
operator|instanceof
name|SelectQuery
operator|)
condition|?
operator|(
name|SelectQuery
operator|)
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Initializes Query qualifier from string.      */
name|void
name|setQueryQualifier
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|!=
literal|null
operator|&&
name|text
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
name|text
operator|=
literal|null
expr_stmt|;
block|}
name|Expression
name|qualifier
init|=
name|createQualifier
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
comment|//getQuery() is not null if we reached here
name|getQuery
argument_list|()
operator|.
name|setQualifier
argument_list|(
name|qualifier
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
name|getQuery
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method to create and check an expression      * @param text String to be converted as Expression      * @return Expression if a new expression was created, null otherwise.      * @throws ValidationException if<code>text</code> can't be converted        */
name|Expression
name|createQualifier
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ValidationException
block|{
name|SelectQuery
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
return|return
literal|null
return|;
block|}
name|ExpressionConvertor
name|convertor
init|=
operator|new
name|ExpressionConvertor
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|oldQualifier
init|=
name|convertor
operator|.
name|valueAsString
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldQualifier
argument_list|,
name|text
argument_list|)
condition|)
block|{
name|Expression
name|exp
init|=
operator|(
name|Expression
operator|)
name|convertor
operator|.
name|stringAsValue
argument_list|(
name|text
argument_list|)
decl_stmt|;
comment|/**                  * Advanced checking. See CAY-888 #1                  */
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|Entity
condition|)
block|{
name|checkExpression
argument_list|(
operator|(
name|Entity
operator|)
name|query
operator|.
name|getRoot
argument_list|()
argument_list|,
name|exp
argument_list|)
expr_stmt|;
block|}
return|return
name|exp
return|;
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// unparsable qualifier
throw|throw
operator|new
name|ValidationException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
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
literal|"SelectQuery name is required."
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
name|Query
name|matchingQuery
init|=
name|map
operator|.
name|getQuery
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchingQuery
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
if|else if
condition|(
name|matchingQuery
operator|!=
name|query
condition|)
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
comment|/**      * Advanced checking of an expression, needed because Expression.fromString()      * might terminate normally, but returned Expression will not be appliable      * for real Entities.      * Current implementation assures all attributes in expression are present in      * Entity        * @param root Root of a query      * @param ex Expression to check      * @throws ValidationException when something's wrong      */
specifier|static
name|void
name|checkExpression
parameter_list|(
name|Entity
name|root
parameter_list|,
name|Expression
name|ex
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
if|if
condition|(
name|ex
operator|instanceof
name|ASTPath
condition|)
block|{
comment|/**                  * Try to iterate through path, if some attributes are not present,                  * exception will be raised                  */
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|path
init|=
name|root
operator|.
name|resolvePathComponents
argument_list|(
name|ex
argument_list|)
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|path
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ex
operator|.
name|getOperandCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ex
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
operator|instanceof
name|Expression
condition|)
block|{
name|checkExpression
argument_list|(
name|root
argument_list|,
operator|(
name|Expression
operator|)
name|ex
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|eex
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|eex
operator|.
name|getUnlabeledMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Handler to user's actions with root selection combobox      */
class|class
name|RootSelectionHandler
implements|implements
name|FocusListener
implements|,
name|ActionListener
block|{
name|String
name|newName
init|=
literal|null
decl_stmt|;
name|boolean
name|needChangeName
decl_stmt|;
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|ae
parameter_list|)
block|{
name|SelectQuery
name|query
init|=
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|Entity
name|root
init|=
operator|(
name|Entity
operator|)
name|queryRoot
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
if|if
condition|(
name|needChangeName
condition|)
block|{
comment|//not changed by user
comment|/**                          * Doing auto name change, following CAY-888 #2                          */
name|String
name|newPrefix
init|=
name|root
operator|.
name|getName
argument_list|()
operator|+
literal|"Query"
decl_stmt|;
name|newName
operator|=
name|newPrefix
expr_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|long
name|postfix
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|map
operator|.
name|getQuery
argument_list|(
name|newName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|newName
operator|=
name|newPrefix
operator|+
operator|(
name|postfix
operator|++
operator|)
expr_stmt|;
block|}
name|name
operator|.
name|setText
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|focusGained
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
comment|//reset new name tracking
name|newName
operator|=
literal|null
expr_stmt|;
name|SelectQuery
name|query
init|=
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|needChangeName
operator|=
name|hasDefaultName
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|needChangeName
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|focusLost
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|newName
operator|!=
literal|null
condition|)
block|{
name|setQueryName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
name|newName
operator|=
literal|null
expr_stmt|;
name|needChangeName
operator|=
literal|false
expr_stmt|;
block|}
comment|/**          * @return whether specified's query name is 'default' i.e. Cayenne generated          * A query's name is 'default' if it starts with 'UntitledQuery' or with root name.          *           * We cannot follow user input because tab might be opened many times          */
name|boolean
name|hasDefaultName
parameter_list|(
name|SelectQuery
name|query
parameter_list|)
block|{
name|String
name|prefix
init|=
name|query
operator|.
name|getRoot
argument_list|()
operator|==
literal|null
condition|?
literal|"UntitledQuery"
else|:
name|CellRenderers
operator|.
name|asString
argument_list|(
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
operator|+
literal|"Query"
decl_stmt|;
return|return
name|name
operator|.
name|getComponent
argument_list|()
operator|.
name|getText
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

