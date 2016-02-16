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
name|JToolBar
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
name|undo
operator|.
name|AddPrefetchUndoableEdit
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
name|EntityTreeFilter
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

begin_comment
comment|/**  * Subclass of the SelectQueryOrderingTab configured to work with prefetches.  *   */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryPrefetchTab
extends|extends
name|SelectQueryOrderingTab
block|{
specifier|public
name|SelectQueryPrefetchTab
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
literal|"Add Prefetch"
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
name|String
name|prefetch
init|=
name|getSelectedPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefetch
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|addPrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|AddPrefetchUndoableEdit
argument_list|(
name|prefetch
argument_list|,
name|SelectQueryPrefetchTab
operator|.
name|this
argument_list|)
argument_list|)
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
literal|"Remove Prefetch"
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
name|String
name|prefetch
init|=
operator|(
name|String
operator|)
name|table
operator|.
name|getModel
argument_list|()
operator|.
name|getValueAt
argument_list|(
name|selection
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|removePrefetch
argument_list|(
name|prefetch
argument_list|)
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
name|EntityTreeModel
name|treeModel
init|=
operator|new
name|EntityTreeModel
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|treeModel
operator|.
name|setFilter
argument_list|(
operator|new
name|EntityTreeFilter
argument_list|()
block|{
specifier|public
name|boolean
name|attributeMatch
parameter_list|(
name|Object
name|node
parameter_list|,
name|Attribute
name|attr
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|relationshipMatch
parameter_list|(
name|Object
name|node
parameter_list|,
name|Relationship
name|rel
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|treeModel
return|;
block|}
specifier|protected
name|TableModel
name|createTableModel
parameter_list|()
block|{
return|return
operator|new
name|PrefetchModel
argument_list|()
return|;
block|}
specifier|public
name|void
name|addPrefetch
parameter_list|(
name|String
name|prefetch
parameter_list|)
block|{
comment|// check if such prefetch already exists
if|if
condition|(
operator|!
name|selectQuery
operator|.
name|getPrefetches
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|selectQuery
operator|.
name|getPrefetches
argument_list|()
operator|.
name|contains
argument_list|(
name|prefetch
argument_list|)
condition|)
block|{
return|return;
block|}
name|selectQuery
operator|.
name|addPrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
comment|// reset the model, since it is immutable
name|table
operator|.
name|setModel
argument_list|(
name|createTableModel
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
name|selectQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetch
parameter_list|)
block|{
name|selectQuery
operator|.
name|removePrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
comment|// reset the model, since it is immutable
name|table
operator|.
name|setModel
argument_list|(
name|createTableModel
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
name|selectQuery
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isToMany
parameter_list|(
name|String
name|prefetch
parameter_list|)
block|{
if|if
condition|(
name|selectQuery
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|root
init|=
name|selectQuery
operator|.
name|getRoot
argument_list|()
decl_stmt|;
comment|// totally invalid path would result in ExpressionException
try|try
block|{
name|Expression
name|exp
init|=
name|Expression
operator|.
name|fromString
argument_list|(
name|prefetch
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|Relationship
condition|)
block|{
return|return
operator|(
operator|(
name|Relationship
operator|)
name|object
operator|)
operator|.
name|isToMany
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * A table model for the Ordering editing table.      */
specifier|final
class|class
name|PrefetchModel
extends|extends
name|AbstractTableModel
block|{
name|String
index|[]
name|prefetches
decl_stmt|;
name|PrefetchModel
parameter_list|()
block|{
if|if
condition|(
name|selectQuery
operator|!=
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|String
index|[
name|selectQuery
operator|.
name|getPrefetches
argument_list|()
operator|.
name|size
argument_list|()
index|]
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
name|prefetches
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|prefetches
index|[
name|i
index|]
operator|=
name|selectQuery
operator|.
name|getPrefetches
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|2
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
operator|(
name|prefetches
operator|!=
literal|null
operator|)
condition|?
name|prefetches
operator|.
name|length
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
switch|switch
condition|(
name|column
condition|)
block|{
case|case
literal|0
case|:
return|return
name|prefetches
index|[
name|row
index|]
return|;
case|case
literal|1
case|:
return|return
name|isToMany
argument_list|(
name|prefetches
index|[
name|row
index|]
argument_list|)
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
literal|"Prefetch Path"
return|;
case|case
literal|1
case|:
return|return
literal|"To Many"
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
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

