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
name|DbAdapterInfo
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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
operator|.
name|SQLSyntaxConstants
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
name|syntax
operator|.
name|jedit
operator|.
name|JEditTextArea
import|;
end_import

begin_import
import|import
name|org
operator|.
name|syntax
operator|.
name|jedit
operator|.
name|KeywordMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|syntax
operator|.
name|jedit
operator|.
name|tokenmarker
operator|.
name|PLSQLTokenMarker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|syntax
operator|.
name|jedit
operator|.
name|tokenmarker
operator|.
name|SQLTokenMarker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|syntax
operator|.
name|jedit
operator|.
name|tokenmarker
operator|.
name|Token
import|;
end_import

begin_import
import|import
name|org
operator|.
name|syntax
operator|.
name|jedit
operator|.
name|tokenmarker
operator|.
name|TokenMarker
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ListSelectionModel
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
name|DocumentEvent
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
name|DocumentListener
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
name|ListSelectionEvent
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
name|ListSelectionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|BadLocationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Document
import|;
end_import

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
name|Color
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A panel for configuring SQL scripts of a SQL template.  *   */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateScriptsTab
extends|extends
name|JPanel
implements|implements
name|DocumentListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LABEL
init|=
literal|"Default"
decl_stmt|;
comment|/**      * JEdit marker for SQL Template      */
specifier|static
specifier|final
name|TokenMarker
name|SQL_TEMPLATE_MARKER
decl_stmt|;
static|static
block|{
name|KeywordMap
name|map
init|=
name|PLSQLTokenMarker
operator|.
name|getKeywordMap
argument_list|()
decl_stmt|;
comment|//adding more keywords
name|map
operator|.
name|add
argument_list|(
literal|"FIRST"
argument_list|,
name|Token
operator|.
name|KEYWORD1
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"LIMIT"
argument_list|,
name|Token
operator|.
name|KEYWORD1
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"OFFSET"
argument_list|,
name|Token
operator|.
name|KEYWORD1
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"TOP"
argument_list|,
name|Token
operator|.
name|KEYWORD1
argument_list|)
expr_stmt|;
comment|//adding velocity template highlighing
name|map
operator|.
name|add
argument_list|(
literal|"#bind"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#bindEqual"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#bindNotEqual"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#bindObjectEqual"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#bindObjectNotEqual"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#chain"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#chunk"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#end"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"#result"
argument_list|,
name|Token
operator|.
name|KEYWORD2
argument_list|)
expr_stmt|;
name|SQL_TEMPLATE_MARKER
operator|=
operator|new
name|SQLTokenMarker
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|JList
name|scripts
decl_stmt|;
comment|/**      * JEdit text component for highlighing SQL syntax (see CAY-892)      */
specifier|protected
name|JEditTextArea
name|scriptArea
decl_stmt|;
comment|/**      * Indication that no update should be fired      */
specifier|private
name|boolean
name|updateDisabled
decl_stmt|;
specifier|protected
name|ListSelectionListener
name|scriptRefreshHandler
decl_stmt|;
specifier|public
name|SQLTemplateScriptsTab
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
specifier|protected
name|void
name|initView
parameter_list|()
block|{
comment|// create widgets, etc.
name|scriptRefreshHandler
operator|=
operator|new
name|ListSelectionListener
argument_list|()
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|getValueIsAdjusting
argument_list|()
condition|)
block|{
name|displayScript
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|scripts
operator|=
operator|new
name|JList
argument_list|()
expr_stmt|;
name|scripts
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|SINGLE_SELECTION
argument_list|)
expr_stmt|;
name|scripts
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|DbAdapterListRenderer
argument_list|(
name|DbAdapterInfo
operator|.
name|getStandardAdapterLabels
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|keys
init|=
operator|new
name|ArrayList
argument_list|(
name|DbAdapterInfo
operator|.
name|getStandardAdapters
argument_list|()
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|keys
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|DbAdapterInfo
operator|.
name|getStandardAdapters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|DEFAULT_LABEL
argument_list|)
expr_stmt|;
name|scripts
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|keys
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|scriptArea
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createJEditTextArea
argument_list|()
expr_stmt|;
name|scriptArea
operator|.
name|setTokenMarker
argument_list|(
name|SQL_TEMPLATE_MARKER
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|getPainter
argument_list|()
operator|.
name|setFont
argument_list|(
name|SQLSyntaxConstants
operator|.
name|DEFAULT_FONT
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
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"fill:100dlu, 3dlu, fill:100dlu:grow"
argument_list|,
literal|"3dlu, fill:p:grow"
argument_list|)
argument_list|)
decl_stmt|;
comment|// orderings table must grow as the panel is resized
name|builder
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|scripts
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|scriptArea
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
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
comment|// select default script.. display it bypassing the listener...
name|scripts
operator|.
name|removeListSelectionListener
argument_list|(
name|scriptRefreshHandler
argument_list|)
expr_stmt|;
name|scripts
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|displayScript
argument_list|()
expr_stmt|;
name|scripts
operator|.
name|addListSelectionListener
argument_list|(
name|scriptRefreshHandler
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns SQLTemplate text for current selection.      */
name|String
name|getSQLTemplate
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
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
return|return
literal|null
return|;
block|}
return|return
operator|(
name|key
operator|.
name|equals
argument_list|(
name|DEFAULT_LABEL
argument_list|)
operator|)
condition|?
name|query
operator|.
name|getDefaultTemplate
argument_list|()
else|:
name|query
operator|.
name|getCustomTemplate
argument_list|(
name|key
argument_list|)
return|;
block|}
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
comment|/**      * Shows selected script in the editor.      */
name|void
name|displayScript
parameter_list|()
block|{
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
name|disableEditor
argument_list|()
expr_stmt|;
return|return;
block|}
name|String
name|key
init|=
operator|(
name|String
operator|)
name|scripts
operator|.
name|getSelectedValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|disableEditor
argument_list|()
expr_stmt|;
return|return;
block|}
name|enableEditor
argument_list|()
expr_stmt|;
name|String
name|text
init|=
operator|(
name|key
operator|.
name|equals
argument_list|(
name|DEFAULT_LABEL
argument_list|)
operator|)
condition|?
name|query
operator|.
name|getDefaultTemplate
argument_list|()
else|:
name|query
operator|.
name|getCustomTemplate
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|updateDisabled
operator|=
literal|true
expr_stmt|;
name|scriptArea
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|updateDisabled
operator|=
literal|false
expr_stmt|;
block|}
name|void
name|disableEditor
parameter_list|()
block|{
name|scriptArea
operator|.
name|setText
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setBackground
argument_list|(
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|enableEditor
parameter_list|()
block|{
name|scriptArea
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|scriptArea
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
block|}
name|void
name|setSQL
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|Document
name|doc
init|=
name|e
operator|.
name|getDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|setSQL
argument_list|(
name|doc
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|doc
operator|.
name|getLength
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Sets the value of SQL template for the currently selected script.      */
name|void
name|setSQL
parameter_list|(
name|String
name|text
parameter_list|)
block|{
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
name|String
name|key
init|=
operator|(
name|String
operator|)
name|scripts
operator|.
name|getSelectedValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|text
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
block|}
comment|// Compare the value before modifying the query - text area
comment|// will call "verify" even if no changes have occured....
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|DEFAULT_LABEL
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|text
argument_list|,
name|query
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|)
condition|)
block|{
name|query
operator|.
name|setDefaultTemplate
argument_list|(
name|text
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
else|else
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|text
argument_list|,
name|query
operator|.
name|getTemplate
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
block|{
name|query
operator|.
name|setTemplate
argument_list|(
name|key
argument_list|,
name|text
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
specifier|public
name|void
name|insertUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|changedUpdate
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|changedUpdate
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|changedUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|updateDisabled
condition|)
block|{
name|setSQL
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|DbAdapterListRenderer
extends|extends
name|DefaultListCellRenderer
block|{
name|Map
name|adapterLabels
decl_stmt|;
name|DbAdapterListRenderer
parameter_list|(
name|Map
name|adapterLabels
parameter_list|)
block|{
name|this
operator|.
name|adapterLabels
operator|=
operator|(
name|adapterLabels
operator|!=
literal|null
operator|)
condition|?
name|adapterLabels
else|:
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
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
name|index
parameter_list|,
name|boolean
name|selected
parameter_list|,
name|boolean
name|hasFocus
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Class
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|Class
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|Object
name|label
init|=
name|adapterLabels
operator|.
name|get
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|==
literal|null
condition|)
block|{
name|label
operator|=
name|object
expr_stmt|;
block|}
name|Component
name|c
init|=
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|label
argument_list|,
name|index
argument_list|,
name|selected
argument_list|,
name|hasFocus
argument_list|)
decl_stmt|;
comment|// grey out keys that have no SQL
name|setForeground
argument_list|(
name|selected
operator|||
name|getSQLTemplate
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
operator|!=
literal|null
condition|?
name|Color
operator|.
name|BLACK
else|:
name|Color
operator|.
name|LIGHT_GRAY
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
block|}
specifier|public
name|int
name|getSelectedIndex
parameter_list|()
block|{
return|return
name|scripts
operator|.
name|getSelectedIndex
argument_list|()
return|;
block|}
specifier|public
name|void
name|setSelectedIndex
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|scripts
operator|.
name|setSelectedIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

