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
name|CardLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
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
name|beans
operator|.
name|PropertyChangeEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
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
name|JSplitPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
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
name|table
operator|.
name|AbstractTableModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeModel
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
name|EntityTreeModel
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
name|ModelerUtil
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
name|MultiColumnBrowser
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
name|UIUtil
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
name|Ordering
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
name|query
operator|.
name|SortOrder
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

begin_comment
comment|/**  * A panel for picking SelectQuery orderings.  *   */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryOrderingTab
extends|extends
name|JPanel
implements|implements
name|PropertyChangeListener
block|{
comment|// property for split pane divider size
specifier|private
specifier|static
specifier|final
name|String
name|SPLIT_DIVIDER_LOCATION_PROPERTY
init|=
literal|"query.orderings.divider.location"
decl_stmt|;
specifier|static
specifier|final
name|Dimension
name|BROWSER_CELL_DIM
init|=
operator|new
name|Dimension
argument_list|(
literal|150
argument_list|,
literal|100
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Dimension
name|TABLE_DIM
init|=
operator|new
name|Dimension
argument_list|(
literal|460
argument_list|,
literal|60
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PATH_HEADER
init|=
literal|"Path"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ASCENDING_HEADER
init|=
literal|"Ascending"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IGNORE_CASE_HEADER
init|=
literal|"Ignore Case"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REAL_PANEL
init|=
literal|"real"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PLACEHOLDER_PANEL
init|=
literal|"placeholder"
decl_stmt|;
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|selectQuery
decl_stmt|;
specifier|protected
name|MultiColumnBrowser
name|browser
decl_stmt|;
specifier|protected
name|JTable
name|table
decl_stmt|;
specifier|protected
name|CardLayout
name|cardLayout
decl_stmt|;
specifier|protected
name|JPanel
name|messagePanel
decl_stmt|;
specifier|public
name|SelectQueryOrderingTab
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
specifier|protected
name|void
name|initView
parameter_list|()
block|{
name|messagePanel
operator|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|cardLayout
operator|=
operator|new
name|CardLayout
argument_list|()
expr_stmt|;
name|Preferences
name|detail
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|int
name|defLocation
init|=
name|Application
operator|.
name|getFrame
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
decl_stmt|;
name|int
name|location
init|=
name|detail
operator|!=
literal|null
condition|?
name|detail
operator|.
name|getInt
argument_list|(
name|getDividerLocationProperty
argument_list|()
argument_list|,
name|defLocation
argument_list|)
else|:
name|defLocation
decl_stmt|;
comment|/**          * As of CAY-888 #3 main pane is now a JSplitPane. Top component is a bit larger.          */
name|JSplitPane
name|mainPanel
init|=
operator|new
name|JSplitPane
argument_list|(
name|JSplitPane
operator|.
name|VERTICAL_SPLIT
argument_list|)
decl_stmt|;
name|mainPanel
operator|.
name|addPropertyChangeListener
argument_list|(
name|JSplitPane
operator|.
name|DIVIDER_LOCATION_PROPERTY
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|mainPanel
operator|.
name|setDividerLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|mainPanel
operator|.
name|setTopComponent
argument_list|(
name|createEditorPanel
argument_list|()
argument_list|)
expr_stmt|;
name|mainPanel
operator|.
name|setBottomComponent
argument_list|(
name|createSelectorPanel
argument_list|()
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
name|cardLayout
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|mainPanel
argument_list|,
name|REAL_PANEL
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|messagePanel
argument_list|,
name|PLACEHOLDER_PANEL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
comment|// scroll to selected row whenever a selection even occurs
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|addListSelectionListener
argument_list|(
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
name|UIUtil
operator|.
name|scrollToSelectedRow
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
name|processInvalidModel
argument_list|(
literal|"Unknown query."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
operator|(
operator|(
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|query
operator|)
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|Entity
operator|)
condition|)
block|{
name|processInvalidModel
argument_list|(
literal|"SelectQuery has no root set."
argument_list|)
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|selectQuery
operator|=
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|query
expr_stmt|;
name|browser
operator|.
name|setModel
argument_list|(
name|createBrowserModel
argument_list|(
operator|(
name|Entity
operator|)
name|selectQuery
operator|.
name|getRoot
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setModel
argument_list|(
name|createTableModel
argument_list|()
argument_list|)
expr_stmt|;
comment|// init column sizes
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
literal|0
argument_list|)
operator|.
name|setPreferredWidth
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|cardLayout
operator|.
name|show
argument_list|(
name|this
argument_list|,
name|REAL_PANEL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processInvalidModel
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|JLabel
name|messageLabel
init|=
operator|new
name|JLabel
argument_list|(
name|message
argument_list|,
name|JLabel
operator|.
name|CENTER
argument_list|)
decl_stmt|;
name|messagePanel
operator|.
name|removeAll
argument_list|()
expr_stmt|;
name|messagePanel
operator|.
name|add
argument_list|(
name|messageLabel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|cardLayout
operator|.
name|show
argument_list|(
name|this
argument_list|,
name|PLACEHOLDER_PANEL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JPanel
name|createEditorPanel
parameter_list|()
block|{
name|table
operator|=
operator|new
name|JTable
argument_list|()
expr_stmt|;
name|table
operator|.
name|setRowHeight
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowMargin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|table
operator|.
name|setPreferredScrollableViewportSize
argument_list|(
name|TABLE_DIM
argument_list|)
expr_stmt|;
name|table
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|SINGLE_SELECTION
argument_list|)
expr_stmt|;
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
decl_stmt|;
name|panel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|table
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
specifier|protected
name|JPanel
name|createSelectorPanel
parameter_list|()
block|{
name|browser
operator|=
operator|new
name|MultiColumnBrowser
argument_list|()
expr_stmt|;
name|browser
operator|.
name|setPreferredColumnSize
argument_list|(
name|BROWSER_CELL_DIM
argument_list|)
expr_stmt|;
name|browser
operator|.
name|setDefaultRenderer
argument_list|()
expr_stmt|;
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
decl_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|createToolbar
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|browser
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_NEVER
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
comment|// setting minimal size, otherwise scrolling looks awful, because of
comment|// VERTICAL_SCROLLBAR_NEVER strategy
name|panel
operator|.
name|setMinimumSize
argument_list|(
name|panel
operator|.
name|getPreferredSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
specifier|protected
name|JComponent
name|createToolbar
parameter_list|()
block|{
name|JButton
name|add
init|=
operator|new
name|JButton
argument_list|(
literal|"Add Ordering"
argument_list|,
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-move_up.gif"
argument_list|)
argument_list|)
decl_stmt|;
name|add
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
name|e
parameter_list|)
block|{
name|addOrdering
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|JButton
name|remove
init|=
operator|new
name|JButton
argument_list|(
literal|"Remove Ordering"
argument_list|,
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-move_down.gif"
argument_list|)
argument_list|)
decl_stmt|;
name|remove
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
name|e
parameter_list|)
block|{
name|removeOrdering
argument_list|()
expr_stmt|;
block|}
block|}
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
name|add
argument_list|(
name|add
argument_list|)
expr_stmt|;
name|toolbar
operator|.
name|add
argument_list|(
name|remove
argument_list|)
expr_stmt|;
return|return
name|toolbar
return|;
block|}
specifier|protected
name|TreeModel
name|createBrowserModel
parameter_list|(
name|Entity
name|entity
parameter_list|)
block|{
return|return
operator|new
name|EntityTreeModel
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|protected
name|TableModel
name|createTableModel
parameter_list|()
block|{
return|return
operator|new
name|OrderingModel
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getSelectedPath
parameter_list|()
block|{
name|Object
index|[]
name|path
init|=
name|browser
operator|.
name|getSelectionPath
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
comment|// first item in the path is Entity, so we must have
comment|// at least two elements to constitute a valid ordering path
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|length
operator|<
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|// attribute or relationships
name|CayenneMapEntry
name|first
init|=
operator|(
name|CayenneMapEntry
operator|)
name|path
index|[
literal|1
index|]
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|first
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|path
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|CayenneMapEntry
name|pathEntry
init|=
operator|(
name|CayenneMapEntry
operator|)
name|path
index|[
name|i
index|]
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|pathEntry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
name|void
name|addOrdering
parameter_list|()
block|{
name|String
name|orderingPath
init|=
name|getSelectedPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|orderingPath
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check if such ordering already exists
for|for
control|(
name|Ordering
name|ord
range|:
name|selectQuery
operator|.
name|getOrderings
argument_list|()
control|)
block|{
if|if
condition|(
name|orderingPath
operator|.
name|equals
argument_list|(
name|ord
operator|.
name|getSortSpecString
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
name|selectQuery
operator|.
name|addOrdering
argument_list|(
operator|new
name|Ordering
argument_list|(
name|orderingPath
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|index
init|=
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
name|OrderingModel
name|model
init|=
operator|(
name|OrderingModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|fireTableRowsInserted
argument_list|(
name|index
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|SelectQueryOrderingTab
operator|.
name|this
argument_list|,
name|selectQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|removeOrdering
parameter_list|()
block|{
name|int
name|selection
init|=
name|table
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|selection
operator|<
literal|0
condition|)
block|{
return|return;
block|}
name|OrderingModel
name|model
init|=
operator|(
name|OrderingModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|Ordering
name|ordering
init|=
name|model
operator|.
name|getOrdering
argument_list|(
name|selection
argument_list|)
decl_stmt|;
name|selectQuery
operator|.
name|removeOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
name|model
operator|.
name|fireTableRowsDeleted
argument_list|(
name|selection
argument_list|,
name|selection
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|SelectQueryOrderingTab
operator|.
name|this
argument_list|,
name|selectQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A table model for the Ordering editing table.      */
specifier|final
class|class
name|OrderingModel
extends|extends
name|AbstractTableModel
block|{
name|Ordering
name|getOrdering
parameter_list|(
name|int
name|row
parameter_list|)
block|{
return|return
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|get
argument_list|(
name|row
argument_list|)
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
operator|(
name|selectQuery
operator|!=
literal|null
operator|)
condition|?
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|Ordering
name|ordering
init|=
name|getOrdering
argument_list|(
name|row
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|column
condition|)
block|{
case|case
literal|0
case|:
return|return
name|ordering
operator|.
name|getSortSpecString
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|ordering
operator|.
name|isAscending
argument_list|()
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
case|case
literal|2
case|:
return|return
name|ordering
operator|.
name|isCaseInsensitive
argument_list|()
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
default|default:
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Invalid column: "
operator|+
name|column
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Class
name|getColumnClass
parameter_list|(
name|int
name|column
parameter_list|)
block|{
switch|switch
condition|(
name|column
condition|)
block|{
case|case
literal|0
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
literal|1
case|:
case|case
literal|2
case|:
return|return
name|Boolean
operator|.
name|class
return|;
default|default:
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Invalid column: "
operator|+
name|column
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
switch|switch
condition|(
name|column
condition|)
block|{
case|case
literal|0
case|:
return|return
name|PATH_HEADER
return|;
case|case
literal|1
case|:
return|return
name|ASCENDING_HEADER
return|;
case|case
literal|2
case|:
return|return
name|IGNORE_CASE_HEADER
return|;
default|default:
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Invalid column: "
operator|+
name|column
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
return|return
name|column
operator|==
literal|1
operator|||
name|column
operator|==
literal|2
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setValueAt
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|Ordering
name|ordering
init|=
name|getOrdering
argument_list|(
name|row
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|column
condition|)
block|{
case|case
literal|1
case|:
if|if
condition|(
operator|(
operator|(
name|Boolean
operator|)
name|value
operator|)
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|ordering
operator|.
name|setAscending
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ordering
operator|.
name|setDescending
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
literal|2
case|:
if|if
condition|(
operator|(
operator|(
name|Boolean
operator|)
name|value
operator|)
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|ordering
operator|.
name|setCaseInsensitive
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ordering
operator|.
name|setCaseSensitive
argument_list|()
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Invalid editable column: "
operator|+
name|column
argument_list|)
throw|;
block|}
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|SelectQueryOrderingTab
operator|.
name|this
argument_list|,
name|selectQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates split pane divider location in properties      */
specifier|public
name|void
name|propertyChange
parameter_list|(
name|PropertyChangeEvent
name|evt
parameter_list|)
block|{
if|if
condition|(
name|JSplitPane
operator|.
name|DIVIDER_LOCATION_PROPERTY
operator|.
name|equals
argument_list|(
name|evt
operator|.
name|getPropertyName
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|value
init|=
operator|(
name|Integer
operator|)
name|evt
operator|.
name|getNewValue
argument_list|()
decl_stmt|;
name|Preferences
name|detail
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|detail
operator|.
name|putInt
argument_list|(
name|getDividerLocationProperty
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns name of a property for divider location.      */
specifier|protected
name|String
name|getDividerLocationProperty
parameter_list|()
block|{
return|return
name|SPLIT_DIVIDER_LOCATION_PROPERTY
return|;
block|}
block|}
end_class

end_unit

