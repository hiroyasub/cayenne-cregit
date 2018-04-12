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
name|dialog
operator|.
name|db
operator|.
name|merge
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergeDirection
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|util
operator|.
name|CayenneController
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
name|swing
operator|.
name|BindingBuilder
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
name|ObjectBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultCellEditor
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
name|JTable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingConstants
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
name|DefaultTableCellRenderer
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
name|TableCellEditor
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
name|TableColumn
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
name|TableColumnModel
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|MergerTokenSelectorController
extends|extends
name|CayenneController
block|{
specifier|protected
name|MergerTokenSelectorView
name|view
decl_stmt|;
specifier|protected
name|ObjectBinding
name|tableBinding
decl_stmt|;
specifier|protected
name|MergerToken
name|token
decl_stmt|;
specifier|protected
name|int
name|permanentlyExcludedCount
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|MergerToken
argument_list|>
name|excludedTokens
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|MergerToken
argument_list|>
name|selectableTokensList
decl_stmt|;
specifier|protected
name|MergerTokenFactory
name|mergerTokenFactory
decl_stmt|;
specifier|protected
name|boolean
name|isReverse
decl_stmt|;
specifier|public
name|MergerTokenSelectorController
parameter_list|(
specifier|final
name|CayenneController
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|MergerTokenSelectorView
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludedTokens
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectableTokensList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setMergerTokenFactory
parameter_list|(
specifier|final
name|MergerTokenFactory
name|mergerTokenFactory
parameter_list|)
block|{
name|this
operator|.
name|mergerTokenFactory
operator|=
name|mergerTokenFactory
expr_stmt|;
block|}
specifier|public
name|void
name|setTokens
parameter_list|(
specifier|final
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|selectableTokensList
operator|.
name|clear
argument_list|()
expr_stmt|;
name|selectableTokensList
operator|.
name|addAll
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|excludedTokens
operator|.
name|addAll
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|getSelectedTokens
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|MergerToken
argument_list|>
name|t
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectableTokensList
argument_list|)
decl_stmt|;
name|t
operator|.
name|removeAll
argument_list|(
name|excludedTokens
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|t
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|getSelectableTokens
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|selectableTokensList
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeToken
parameter_list|(
specifier|final
name|MergerToken
name|token
parameter_list|)
block|{
name|selectableTokensList
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|excludedTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
specifier|final
name|AbstractTableModel
name|model
init|=
operator|(
name|AbstractTableModel
operator|)
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
comment|// ----- properties -----
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
comment|/**      * Called by table binding script to set current token.      */
specifier|public
name|void
name|setToken
parameter_list|(
specifier|final
name|MergerToken
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
comment|/**      * Returns {@link MergerToken}s that are excluded from DB generation.      */
comment|/*      * public Collection getExcludedTokens() { return excludedTokens; }      */
specifier|public
name|boolean
name|isIncluded
parameter_list|()
block|{
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|!
name|excludedTokens
operator|.
name|contains
argument_list|(
name|token
argument_list|)
return|;
block|}
specifier|public
name|void
name|setIncluded
parameter_list|(
specifier|final
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|b
condition|)
block|{
name|excludedTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|excludedTokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|tableSelectedAction
argument_list|()
expr_stmt|;
block|}
comment|/**      * A callback action that updates the state of Select All checkbox.      */
specifier|public
name|void
name|tableSelectedAction
parameter_list|()
block|{
specifier|final
name|int
name|unselectedCount
init|=
name|excludedTokens
operator|.
name|size
argument_list|()
operator|-
name|permanentlyExcludedCount
decl_stmt|;
if|if
condition|(
name|unselectedCount
operator|==
name|selectableTokensList
operator|.
name|size
argument_list|()
condition|)
block|{
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|unselectedCount
operator|==
literal|0
condition|)
block|{
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ------ other stuff ------
specifier|protected
name|void
name|initController
parameter_list|()
block|{
specifier|final
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCheckAll
argument_list|()
argument_list|,
literal|"checkAllAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getReverseAll
argument_list|()
argument_list|,
literal|"reverseAllAction()"
argument_list|)
expr_stmt|;
specifier|final
name|TableModel
name|model
init|=
operator|new
name|MergerTokenTableModel
argument_list|(
name|this
argument_list|)
decl_stmt|;
specifier|final
name|MergeDirection
index|[]
name|dirs
init|=
operator|new
name|MergeDirection
index|[]
block|{
name|MergeDirection
operator|.
name|TO_DB
block|,
name|MergeDirection
operator|.
name|TO_MODEL
block|}
decl_stmt|;
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
specifier|final
name|TableColumnModel
name|columnModel
init|=
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|getColumnModel
argument_list|()
decl_stmt|;
comment|// dropdown for direction column
specifier|final
name|JComboBox
name|directionCombo
init|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|dirs
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|directionCombo
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|TableColumn
name|directionColumn
init|=
name|columnModel
operator|.
name|getColumn
argument_list|(
name|MergerTokenTableModel
operator|.
name|COL_DIRECTION
argument_list|)
decl_stmt|;
name|directionColumn
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|DefaultTableCellRenderer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Component
name|getTableCellRendererComponent
parameter_list|(
specifier|final
name|JTable
name|table
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|,
specifier|final
name|boolean
name|isSelected
parameter_list|,
specifier|final
name|boolean
name|hasFocus
parameter_list|,
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|int
name|column
parameter_list|)
block|{
name|super
operator|.
name|getTableCellRendererComponent
argument_list|(
name|table
argument_list|,
name|value
argument_list|,
name|isSelected
argument_list|,
name|hasFocus
argument_list|,
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|setHorizontalTextPosition
argument_list|(
name|SwingConstants
operator|.
name|LEFT
argument_list|)
expr_stmt|;
name|setIcon
argument_list|(
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-arrow-open.png"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|directionColumn
operator|.
name|setCellEditor
argument_list|(
operator|new
name|DefaultCellEditor
argument_list|(
name|directionCombo
argument_list|)
argument_list|)
expr_stmt|;
name|columnModel
operator|.
name|getColumn
argument_list|(
name|MergerTokenTableModel
operator|.
name|COL_SELECT
argument_list|)
operator|.
name|setPreferredWidth
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|columnModel
operator|.
name|getColumn
argument_list|(
name|MergerTokenTableModel
operator|.
name|COL_DIRECTION
argument_list|)
operator|.
name|setPreferredWidth
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|columnModel
operator|.
name|getColumn
argument_list|(
name|MergerTokenTableModel
operator|.
name|COL_SELECT
argument_list|)
operator|.
name|setMaxWidth
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|columnModel
operator|.
name|getColumn
argument_list|(
name|MergerTokenTableModel
operator|.
name|COL_DIRECTION
argument_list|)
operator|.
name|setMaxWidth
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|(
specifier|final
name|MergerToken
name|token
parameter_list|)
block|{
return|return
operator|(
name|selectableTokensList
operator|.
name|contains
argument_list|(
name|token
argument_list|)
operator|&&
operator|!
name|excludedTokens
operator|.
name|contains
argument_list|(
name|token
argument_list|)
operator|)
return|;
block|}
specifier|public
name|void
name|select
parameter_list|(
specifier|final
name|MergerToken
name|token
parameter_list|,
specifier|final
name|boolean
name|select
parameter_list|)
block|{
if|if
condition|(
name|select
condition|)
block|{
name|excludedTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|excludedTokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setDirection
parameter_list|(
specifier|final
name|MergerToken
name|token
parameter_list|,
specifier|final
name|MergeDirection
name|direction
parameter_list|)
block|{
if|if
condition|(
name|token
operator|.
name|getDirection
argument_list|()
operator|.
name|equals
argument_list|(
name|direction
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|int
name|i
init|=
name|selectableTokensList
operator|.
name|indexOf
argument_list|(
name|token
argument_list|)
decl_stmt|;
specifier|final
name|MergerToken
name|reverse
init|=
name|token
operator|.
name|createReverse
argument_list|(
name|mergerTokenFactory
argument_list|)
decl_stmt|;
name|selectableTokensList
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|reverse
argument_list|)
expr_stmt|;
if|if
condition|(
name|excludedTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|excludedTokens
operator|.
name|add
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
comment|// Repaint, so that "Operation" column updates properly
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|checkAllAction
parameter_list|()
block|{
name|stopEditing
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|isCheckAllSelected
init|=
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|isSelected
argument_list|()
decl_stmt|;
if|if
condition|(
name|isCheckAllSelected
condition|)
block|{
name|excludedTokens
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|excludedTokens
operator|.
name|addAll
argument_list|(
name|selectableTokensList
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AbstractTableModel
name|model
init|=
operator|(
name|AbstractTableModel
operator|)
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReverse
parameter_list|()
block|{
return|return
name|isReverse
return|;
block|}
specifier|public
name|void
name|reverseAllAction
parameter_list|()
block|{
name|stopEditing
argument_list|()
expr_stmt|;
name|isReverse
operator|=
operator|!
name|isReverse
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
name|selectableTokensList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|MergerToken
name|token
init|=
name|selectableTokensList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|MergerToken
name|reverse
init|=
name|token
operator|.
name|createReverse
argument_list|(
name|mergerTokenFactory
argument_list|)
decl_stmt|;
name|selectableTokensList
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|reverse
argument_list|)
expr_stmt|;
if|if
condition|(
name|excludedTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|excludedTokens
operator|.
name|add
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|selectableTokensList
argument_list|)
expr_stmt|;
specifier|final
name|AbstractTableModel
name|model
init|=
operator|(
name|AbstractTableModel
operator|)
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|stopEditing
parameter_list|()
block|{
comment|// Stop cell editing before any action
specifier|final
name|TableCellEditor
name|cellEditor
init|=
name|view
operator|.
name|getTokens
argument_list|()
operator|.
name|getCellEditor
argument_list|()
decl_stmt|;
if|if
condition|(
name|cellEditor
operator|!=
literal|null
condition|)
block|{
name|cellEditor
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

