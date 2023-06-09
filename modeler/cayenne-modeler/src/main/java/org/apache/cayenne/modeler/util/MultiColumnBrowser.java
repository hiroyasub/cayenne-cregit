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
name|util
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|AbstractListModel
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
name|ImageIcon
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
name|JViewport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ListCellRenderer
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
name|border
operator|.
name|Border
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
name|event
operator|.
name|TreeSelectionEvent
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
name|TreeSelectionListener
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
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|Component
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
name|GridLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|List
import|;
end_import

begin_comment
comment|/**  * A simple non-editable tree browser with multiple columns for display and  * navigation of a tree structure. This type of browser is ideal for showing  * deeply (or infinitely) nested trees/graphs. The most famous example of its  * use is Mac OS X Finder column view.  *   *<p>  * MultiColumnBrowser starts at the root of the tree and automatically expands  * to the right as navigation goes deeper. MultiColumnBrowser uses the same  * TreeModel as a regular JTree for its navigation model.  *</p>  *   *<p>  * Users are notified of selection changes via a TreeSelectionEvents.  *</p>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|MultiColumnBrowser
extends|extends
name|JPanel
block|{
specifier|protected
specifier|static
specifier|final
name|ImageIcon
name|rightArrow
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-arrow-closed.png"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_MIN_COLUMNS_COUNT
init|=
literal|3
decl_stmt|;
specifier|protected
name|int
name|minColumns
decl_stmt|;
specifier|protected
name|ListCellRenderer
name|renderer
decl_stmt|;
specifier|protected
name|TreeModel
name|model
decl_stmt|;
specifier|protected
name|Object
index|[]
name|selectionPath
decl_stmt|;
specifier|protected
name|Dimension
name|preferredColumnSize
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|BrowserPanel
argument_list|>
name|columns
decl_stmt|;
specifier|protected
name|ListSelectionListener
name|browserSelector
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|TreeSelectionListener
argument_list|>
name|treeSelectionListeners
decl_stmt|;
comment|/**      * Whether firing of TreeSelectionListeners is disabled now      */
specifier|private
name|boolean
name|fireDisabled
decl_stmt|;
specifier|public
name|MultiColumnBrowser
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_MIN_COLUMNS_COUNT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultiColumnBrowser
parameter_list|(
name|int
name|minColumns
parameter_list|)
block|{
if|if
condition|(
name|minColumns
operator|<
name|DEFAULT_MIN_COLUMNS_COUNT
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected "
operator|+
name|DEFAULT_MIN_COLUMNS_COUNT
operator|+
literal|" or more columns, got: "
operator|+
name|minColumns
argument_list|)
throw|;
block|}
name|this
operator|.
name|minColumns
operator|=
name|minColumns
expr_stmt|;
name|this
operator|.
name|browserSelector
operator|=
operator|new
name|PanelController
argument_list|()
expr_stmt|;
name|this
operator|.
name|treeSelectionListeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|TreeSelectionListener
argument_list|>
argument_list|()
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addTreeSelectionListener
parameter_list|(
name|TreeSelectionListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|!=
literal|null
operator|&&
operator|!
name|treeSelectionListeners
operator|.
name|contains
argument_list|(
name|listener
argument_list|)
condition|)
block|{
name|treeSelectionListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeTreeSelectionListener
parameter_list|(
name|TreeSelectionListener
name|listener
parameter_list|)
block|{
name|treeSelectionListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Notifies listeners of a tree selection change.      */
specifier|protected
name|void
name|fireTreeSelectionEvent
parameter_list|(
name|Object
index|[]
name|selectionPath
parameter_list|)
block|{
if|if
condition|(
name|fireDisabled
condition|)
block|{
return|return;
block|}
name|TreeSelectionEvent
name|e
init|=
operator|new
name|TreeSelectionEvent
argument_list|(
name|this
argument_list|,
operator|new
name|TreePath
argument_list|(
name|selectionPath
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|TreeSelectionListener
name|listener
range|:
name|treeSelectionListeners
control|)
block|{
name|listener
operator|.
name|valueChanged
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns current selection path or null if no selection is made.      */
specifier|public
name|TreePath
name|getSelectionPath
parameter_list|()
block|{
return|return
name|selectionPath
operator|!=
literal|null
condition|?
operator|new
name|TreePath
argument_list|(
name|selectionPath
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Sets new selection path and fires an event      */
specifier|public
name|void
name|setSelectionPath
parameter_list|(
name|TreePath
name|path
parameter_list|)
block|{
try|try
block|{
name|fireDisabled
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|getPathCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|selectRow
argument_list|(
name|path
operator|.
name|getPathComponent
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|fireDisabled
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|/**      * Selects one path component      */
specifier|protected
name|void
name|selectRow
parameter_list|(
name|Object
name|row
parameter_list|,
name|int
name|index
parameter_list|,
name|TreePath
name|path
parameter_list|)
block|{
if|if
condition|(
name|index
operator|>
literal|0
operator|&&
name|columns
operator|.
name|get
argument_list|(
name|index
operator|-
literal|1
argument_list|)
operator|.
name|getSelectedValue
argument_list|()
operator|!=
name|row
condition|)
block|{
name|columns
operator|.
name|get
argument_list|(
name|index
operator|-
literal|1
argument_list|)
operator|.
name|setSelectedValue
argument_list|(
name|row
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// update
name|updateFromModel
argument_list|(
name|row
argument_list|,
name|index
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the minumum number of displayed columns.      */
specifier|public
name|int
name|getMinColumns
parameter_list|()
block|{
return|return
name|minColumns
return|;
block|}
comment|/**      * Sets the minumum number of displayed columns.      */
specifier|public
name|void
name|setMinColumns
parameter_list|(
name|int
name|minColumns
parameter_list|)
block|{
name|this
operator|.
name|minColumns
operator|=
name|minColumns
expr_stmt|;
block|}
comment|/**      * Returns prefrred size of a single browser column.      */
specifier|public
name|Dimension
name|getPreferredColumnSize
parameter_list|()
block|{
return|return
name|preferredColumnSize
return|;
block|}
specifier|public
name|void
name|setPreferredColumnSize
parameter_list|(
name|Dimension
name|preferredColumnSize
parameter_list|)
block|{
name|this
operator|.
name|preferredColumnSize
operator|=
name|preferredColumnSize
expr_stmt|;
name|refreshPreferredSize
argument_list|()
expr_stmt|;
block|}
comment|/**      * Resets currently used renderer to default one that will use the "name"      * property of objects and display a small arrow to the right of all      * non-leaf nodes.      */
specifier|public
name|void
name|setDefaultRenderer
parameter_list|()
block|{
if|if
condition|(
operator|!
operator|(
name|renderer
operator|instanceof
name|MultiColumnBrowserRenderer
operator|)
condition|)
block|{
name|setRenderer
argument_list|(
operator|new
name|MultiColumnBrowserRenderer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns ListCellRenderer for individual elements of each column.      */
specifier|public
name|ListCellRenderer
name|getRenderer
parameter_list|()
block|{
return|return
name|renderer
return|;
block|}
comment|/**      * Initializes the renderer of column cells.      */
specifier|public
name|void
name|setRenderer
parameter_list|(
name|ListCellRenderer
name|renderer
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|renderer
operator|!=
name|renderer
condition|)
block|{
name|this
operator|.
name|renderer
operator|=
name|renderer
expr_stmt|;
comment|// update existing browser
if|if
condition|(
name|columns
operator|!=
literal|null
operator|&&
name|columns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|JList
name|column
range|:
name|columns
control|)
name|column
operator|.
name|setCellRenderer
argument_list|(
name|renderer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Initializes browser model.      */
specifier|public
name|void
name|setModel
parameter_list|(
name|TreeModel
name|model
parameter_list|)
block|{
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null tree model."
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|model
operator|!=
name|model
condition|)
block|{
name|this
operator|.
name|model
operator|=
name|model
expr_stmt|;
comment|// display first column
name|updateFromModel
argument_list|(
name|model
operator|.
name|getRoot
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns browser model.      */
specifier|public
name|TreeModel
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
block|}
comment|/**      * Returns a current number of columns.      */
specifier|public
name|int
name|getColumnsCount
parameter_list|()
block|{
return|return
name|columns
operator|.
name|size
argument_list|()
return|;
block|}
comment|// ====================================================
comment|// Internal private methods
comment|// ====================================================
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|BrowserPanel
argument_list|>
argument_list|(
name|minColumns
argument_list|)
expr_stmt|;
name|adjustViewColumns
argument_list|(
name|minColumns
argument_list|)
expr_stmt|;
block|}
comment|/**      * Expands or contracts the view by<code>delta</code> columns. Never      * contracts the view below<code>minColumns</code>.      */
specifier|private
name|void
name|adjustViewColumns
parameter_list|(
name|int
name|delta
parameter_list|)
block|{
if|if
condition|(
name|delta
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|setLayout
argument_list|(
operator|new
name|GridLayout
argument_list|(
literal|1
argument_list|,
name|columns
operator|.
name|size
argument_list|()
operator|+
name|delta
argument_list|,
literal|3
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|delta
operator|>
literal|0
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
name|delta
condition|;
name|i
operator|++
control|)
block|{
name|appendColumn
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
name|delta
init|;
name|i
operator|>
literal|0
operator|&&
name|columns
operator|.
name|size
argument_list|()
operator|>
name|minColumns
condition|;
name|i
operator|--
control|)
block|{
name|removeLastColumn
argument_list|()
expr_stmt|;
block|}
block|}
name|refreshPreferredSize
argument_list|()
expr_stmt|;
name|revalidate
argument_list|()
expr_stmt|;
block|}
specifier|private
name|BrowserPanel
name|appendColumn
parameter_list|()
block|{
name|BrowserPanel
name|panel
init|=
operator|new
name|BrowserPanel
argument_list|()
decl_stmt|;
name|installColumn
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|columns
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|JScrollPane
name|scroller
init|=
operator|new
name|JScrollPane
argument_list|(
name|panel
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
decl_stmt|;
comment|// note - it is important to set prefrred size on scroller,
comment|// not on the component itself... otherwise resizing
comment|// will be very ugly...
if|if
condition|(
name|preferredColumnSize
operator|!=
literal|null
condition|)
block|{
name|scroller
operator|.
name|setPreferredSize
argument_list|(
name|preferredColumnSize
argument_list|)
expr_stmt|;
block|}
name|add
argument_list|(
name|scroller
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
comment|/**      * Installs all needed columns and renderers to a new column      */
specifier|protected
name|void
name|installColumn
parameter_list|(
name|BrowserPanel
name|panel
parameter_list|)
block|{
name|panel
operator|.
name|addListSelectionListener
argument_list|(
name|browserSelector
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setCellRenderer
argument_list|(
name|renderer
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BrowserPanel
name|removeLastColumn
parameter_list|()
block|{
if|if
condition|(
name|columns
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|index
init|=
name|columns
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
name|BrowserPanel
name|panel
init|=
name|columns
operator|.
name|remove
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|uninstallColumn
argument_list|(
name|panel
argument_list|)
expr_stmt|;
comment|// remove ansestor of the column (JScrollPane)
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
comment|/**      * Clears selection in browser and removes all unnessesary columns      */
specifier|public
name|void
name|clearSelection
parameter_list|()
block|{
name|updateFromModel
argument_list|(
name|model
operator|.
name|getRoot
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes all local listeners from the column      */
specifier|protected
name|void
name|uninstallColumn
parameter_list|(
name|BrowserPanel
name|panel
parameter_list|)
block|{
name|panel
operator|.
name|removeListSelectionListener
argument_list|(
name|browserSelector
argument_list|)
expr_stmt|;
block|}
comment|/**      * Refreshes preferred size of the browser to reflect current number of      * columns.      */
specifier|private
name|void
name|refreshPreferredSize
parameter_list|()
block|{
if|if
condition|(
name|preferredColumnSize
operator|!=
literal|null
condition|)
block|{
name|int
name|w
init|=
name|getColumnsCount
argument_list|()
operator|*
operator|(
name|preferredColumnSize
operator|.
name|width
operator|+
literal|3
operator|)
operator|+
literal|3
decl_stmt|;
name|int
name|h
init|=
name|preferredColumnSize
operator|.
name|height
operator|+
literal|6
decl_stmt|;
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Makes numbered column visible if the browser parent allows scrolling.      */
specifier|private
name|void
name|scrollToColumn
parameter_list|(
name|int
name|column
parameter_list|)
block|{
if|if
condition|(
name|getParent
argument_list|()
operator|instanceof
name|JViewport
condition|)
block|{
name|JViewport
name|viewport
init|=
operator|(
name|JViewport
operator|)
name|getParent
argument_list|()
decl_stmt|;
comment|// find a rectangle in the middle of the browser
comment|// and scroll it...
name|double
name|x
init|=
name|getWidth
argument_list|()
operator|*
name|column
operator|/
operator|(
operator|(
name|double
operator|)
name|getMinColumns
argument_list|()
operator|)
decl_stmt|;
name|double
name|y
init|=
name|getHeight
argument_list|()
operator|/
literal|2
decl_stmt|;
if|if
condition|(
name|preferredColumnSize
operator|!=
literal|null
condition|)
block|{
name|x
operator|-=
name|preferredColumnSize
operator|.
name|width
operator|/
literal|2
expr_stmt|;
if|if
condition|(
name|x
operator|<
literal|0
condition|)
block|{
name|x
operator|=
literal|0
expr_stmt|;
block|}
block|}
name|Rectangle
name|rectangle
init|=
operator|new
name|Rectangle
argument_list|(
operator|(
name|int
operator|)
name|x
argument_list|,
operator|(
name|int
operator|)
name|y
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
comment|// Scroll the area into view.
name|viewport
operator|.
name|scrollRectToVisible
argument_list|(
name|rectangle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Rebuilds view for the new object selection.      */
specifier|protected
name|void
name|updateFromModel
parameter_list|(
name|Object
name|selectedNode
parameter_list|,
name|int
name|panelIndex
parameter_list|)
block|{
name|updateFromModel
argument_list|(
name|selectedNode
argument_list|,
name|panelIndex
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Rebuilds view for the new object selection.      *       * @param load      *            Whether children are loaded automatically      */
specifier|protected
name|void
name|updateFromModel
parameter_list|(
name|Object
name|selectedNode
parameter_list|,
name|int
name|panelIndex
parameter_list|,
name|boolean
name|load
parameter_list|)
block|{
if|if
condition|(
name|selectionPath
operator|==
literal|null
condition|)
block|{
name|selectionPath
operator|=
operator|new
name|Object
index|[
literal|0
index|]
expr_stmt|;
block|}
comment|// clean up extra columns
name|int
name|lastIndex
init|=
name|selectionPath
operator|.
name|length
decl_stmt|;
comment|// check array range to handle race conditions
for|for
control|(
name|int
name|i
init|=
name|panelIndex
operator|+
literal|1
init|;
name|i
operator|<=
name|lastIndex
operator|&&
name|i
operator|>=
literal|0
operator|&&
name|i
operator|<
name|columns
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|BrowserPanel
name|column
init|=
name|columns
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|column
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|clearSelection
argument_list|()
expr_stmt|;
name|column
operator|.
name|setRootNode
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// build path to selected node
name|this
operator|.
name|selectionPath
operator|=
name|rebuildPath
argument_list|(
name|selectionPath
argument_list|,
name|selectedNode
argument_list|,
name|panelIndex
argument_list|)
expr_stmt|;
if|if
condition|(
name|load
condition|)
block|{
comment|// a selectedNode is contained in "panelIndex" column,
comment|// but its children are in the next column.
name|panelIndex
operator|++
expr_stmt|;
block|}
comment|// expand/contract columns as needed
name|adjustViewColumns
argument_list|(
name|panelIndex
operator|+
literal|1
operator|-
name|columns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|load
condition|)
block|{
comment|// selectedNode becomes the root of columns[panelIndex]
if|if
condition|(
operator|!
name|model
operator|.
name|isLeaf
argument_list|(
name|selectedNode
argument_list|)
condition|)
block|{
name|BrowserPanel
name|lastPanel
init|=
name|columns
operator|.
name|get
argument_list|(
name|panelIndex
argument_list|)
decl_stmt|;
name|lastPanel
operator|.
name|setRootNode
argument_list|(
name|selectedNode
argument_list|)
expr_stmt|;
name|scrollToColumn
argument_list|(
name|panelIndex
argument_list|)
expr_stmt|;
block|}
block|}
name|fireTreeSelectionEvent
argument_list|(
name|selectionPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Builds a TreePath to the new node, that is known to be a peer or a child      * of one of the path components. As the method walks the current path      * backwards, it cleans columns that are not common with the new path.      */
specifier|private
name|Object
index|[]
name|rebuildPath
parameter_list|(
name|Object
index|[]
name|path
parameter_list|,
name|Object
name|node
parameter_list|,
name|int
name|panelIndex
parameter_list|)
block|{
name|Object
index|[]
name|newPath
init|=
operator|new
name|Object
index|[
name|panelIndex
operator|+
literal|2
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|path
argument_list|,
literal|0
argument_list|,
name|newPath
argument_list|,
literal|0
argument_list|,
name|panelIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|newPath
index|[
name|panelIndex
operator|+
literal|1
index|]
operator|=
name|node
expr_stmt|;
return|return
name|newPath
return|;
block|}
comment|// ====================================================
comment|// Helper classes
comment|// ====================================================
comment|// ====================================================
comment|// Represents a browser column list model. This is an
comment|// adapter on top of the TreeModel node, showing the branch
comment|// containing node children
comment|// ====================================================
specifier|final
class|class
name|ColumnListModel
extends|extends
name|AbstractListModel
block|{
name|Object
name|treeNode
decl_stmt|;
name|int
name|children
decl_stmt|;
name|void
name|setTreeNode
parameter_list|(
name|Object
name|treeNode
parameter_list|)
block|{
name|int
name|oldChildren
init|=
name|children
decl_stmt|;
name|this
operator|.
name|treeNode
operator|=
name|treeNode
expr_stmt|;
name|this
operator|.
name|children
operator|=
operator|(
name|treeNode
operator|!=
literal|null
operator|)
condition|?
name|model
operator|.
name|getChildCount
argument_list|(
name|treeNode
argument_list|)
else|:
literal|0
expr_stmt|;
comment|// must fire an event to refresh the view
name|super
operator|.
name|fireContentsChanged
argument_list|(
name|MultiColumnBrowser
operator|.
name|this
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|max
argument_list|(
name|oldChildren
argument_list|,
name|children
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getElementAt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|model
operator|.
name|getChild
argument_list|(
name|treeNode
argument_list|,
name|index
argument_list|)
return|;
block|}
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|children
return|;
block|}
block|}
comment|// ====================================================
comment|// Represents a single browser column
comment|// ====================================================
specifier|protected
specifier|final
class|class
name|BrowserPanel
extends|extends
name|JList
block|{
name|BrowserPanel
parameter_list|()
block|{
name|BrowserPanel
operator|.
name|this
operator|.
name|setModel
argument_list|(
operator|new
name|ColumnListModel
argument_list|()
argument_list|)
expr_stmt|;
name|BrowserPanel
operator|.
name|this
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|SINGLE_SELECTION
argument_list|)
expr_stmt|;
block|}
name|void
name|setRootNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
operator|(
operator|(
name|ColumnListModel
operator|)
name|BrowserPanel
operator|.
name|this
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|setTreeNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|Object
name|getTreeNode
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ColumnListModel
operator|)
name|BrowserPanel
operator|.
name|this
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|treeNode
return|;
block|}
block|}
comment|// ====================================================
comment|// Processes selection events in each column
comment|// ====================================================
specifier|final
class|class
name|PanelController
implements|implements
name|ListSelectionListener
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
comment|// ignore "adjusting"
if|if
condition|(
operator|!
name|e
operator|.
name|getValueIsAdjusting
argument_list|()
condition|)
block|{
name|BrowserPanel
name|panel
init|=
operator|(
name|BrowserPanel
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Object
name|selectedNode
init|=
name|panel
operator|.
name|getSelectedValue
argument_list|()
decl_stmt|;
comment|// ignore unselected
if|if
condition|(
name|selectedNode
operator|!=
literal|null
condition|)
block|{
name|updateFromModel
argument_list|(
name|selectedNode
argument_list|,
name|columns
operator|.
name|indexOf
argument_list|(
name|panel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// ====================================================
comment|// Default renderer that shows non-leaf nodes with a
comment|// small right arrow. Unfortunately we can't subclass
comment|// DefaultListCellRenerer since it extends JLabel that
comment|// does not allow the layout that we need.
comment|// ====================================================
specifier|final
class|class
name|MultiColumnBrowserRenderer
implements|implements
name|ListCellRenderer
implements|,
name|Serializable
block|{
name|ListCellRenderer
name|leafRenderer
decl_stmt|;
name|JPanel
name|nonLeafPanel
decl_stmt|;
name|ListCellRenderer
name|nonLeafTextRenderer
decl_stmt|;
name|MultiColumnBrowserRenderer
parameter_list|()
block|{
name|leafRenderer
operator|=
name|CellRenderers
operator|.
name|listRenderer
argument_list|()
expr_stmt|;
name|nonLeafTextRenderer
operator|=
operator|new
name|DefaultListCellRenderer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Border
name|getBorder
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
name|nonLeafPanel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|nonLeafPanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|nonLeafPanel
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
name|rightArrow
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|EAST
argument_list|)
expr_stmt|;
name|nonLeafPanel
operator|.
name|add
argument_list|(
operator|(
name|Component
operator|)
name|nonLeafTextRenderer
argument_list|,
name|BorderLayout
operator|.
name|WEST
argument_list|)
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
name|value
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|cellHasFocus
parameter_list|)
block|{
if|if
condition|(
name|getModel
argument_list|()
operator|.
name|isLeaf
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|leafRenderer
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|value
argument_list|,
name|index
argument_list|,
name|isSelected
argument_list|,
name|cellHasFocus
argument_list|)
return|;
block|}
name|Object
name|renderedValue
init|=
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|renderedValue
operator|==
literal|null
condition|)
block|{
comment|// render NULL as empty string
name|renderedValue
operator|=
literal|" "
expr_stmt|;
block|}
name|Component
name|text
init|=
name|nonLeafTextRenderer
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|renderedValue
argument_list|,
name|index
argument_list|,
name|isSelected
argument_list|,
name|cellHasFocus
argument_list|)
decl_stmt|;
name|nonLeafPanel
operator|.
name|setComponentOrientation
argument_list|(
name|text
operator|.
name|getComponentOrientation
argument_list|()
argument_list|)
expr_stmt|;
name|nonLeafPanel
operator|.
name|setBackground
argument_list|(
name|text
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|nonLeafPanel
operator|.
name|setForeground
argument_list|(
name|text
operator|.
name|getForeground
argument_list|()
argument_list|)
expr_stmt|;
name|nonLeafPanel
operator|.
name|setEnabled
argument_list|(
name|text
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|nonLeafPanel
return|;
block|}
block|}
block|}
end_class

end_unit

