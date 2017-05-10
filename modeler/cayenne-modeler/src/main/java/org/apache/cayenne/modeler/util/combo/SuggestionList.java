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
name|util
operator|.
name|combo
package|;
end_package

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
name|awt
operator|.
name|event
operator|.
name|MouseEvent
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
name|MouseListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultListModel
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
name|event
operator|.
name|MouseInputAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|plaf
operator|.
name|basic
operator|.
name|BasicComboPopup
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

begin_comment
comment|/**  * SuggestionList is a combo-popup displaying all items matching for  * autocompletion.  *   */
end_comment

begin_class
specifier|public
class|class
name|SuggestionList
extends|extends
name|BasicComboPopup
block|{
comment|/**      * 'Strict' matching, i.e. whether 'startWith' or 'contains' function      * should be used for checking match       */
specifier|protected
name|boolean
name|strict
decl_stmt|;
comment|/**      * Creates a strict suggestion-popup for a combobox      */
specifier|public
name|SuggestionList
parameter_list|(
name|JComboBox
name|cb
parameter_list|)
block|{
name|this
argument_list|(
name|cb
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a suggestion-popup for a combobox      */
specifier|public
name|SuggestionList
parameter_list|(
name|JComboBox
name|cb
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
name|super
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|this
operator|.
name|strict
operator|=
name|strict
expr_stmt|;
name|list
operator|.
name|addMouseListener
argument_list|(
operator|new
name|MouseHandler
argument_list|()
argument_list|)
expr_stmt|;
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createLineBorder
argument_list|(
name|Color
operator|.
name|LIGHT_GRAY
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * 'Filters' the list, leaving only matching items      * @param prefix user-typed string, used to filter      */
specifier|public
name|void
name|filter
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|ComboBoxModel
name|model
init|=
name|comboBox
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DefaultListModel
name|lm
init|=
operator|new
name|DefaultListModel
argument_list|()
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
name|model
operator|.
name|getSize
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|item
init|=
name|CellRenderers
operator|.
name|asString
argument_list|(
name|model
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
argument_list|(
name|item
argument_list|,
name|prefix
argument_list|)
condition|)
block|{
name|lm
operator|.
name|addElement
argument_list|(
name|model
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|list
operator|.
name|setModel
argument_list|(
name|lm
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks if an item matches input pattern      */
specifier|protected
name|boolean
name|matches
parameter_list|(
name|String
name|item
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|strict
condition|)
block|{
return|return
name|item
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
name|pattern
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|item
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
name|pattern
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Retrieves the height of the popup based on the current      * ListCellRenderer and the maximum row count.      *       * Overrriden to count for local list size      */
annotation|@
name|Override
specifier|protected
name|int
name|getPopupHeightForRowCount
parameter_list|(
name|int
name|maxRowCount
parameter_list|)
block|{
return|return
name|super
operator|.
name|getPopupHeightForRowCount
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|maxRowCount
argument_list|,
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @return selected index in popup      */
specifier|public
name|int
name|getSelectedIndex
parameter_list|()
block|{
return|return
name|list
operator|.
name|getSelectedIndex
argument_list|()
return|;
block|}
comment|/**      * @return selected item in popup      */
specifier|public
name|Object
name|getSelectedValue
parameter_list|()
block|{
return|return
name|list
operator|.
name|getSelectedValue
argument_list|()
return|;
block|}
comment|/**      * Selects an item in list      */
specifier|public
name|void
name|setSelectedIndex
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|list
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setSelectedItem
argument_list|(
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return current suggestions count      */
specifier|public
name|int
name|getItemCount
parameter_list|()
block|{
return|return
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
return|;
block|}
comment|/**      * @return an item from the list      */
specifier|public
name|Object
name|getItemAt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MouseListener
name|createListMouseListener
parameter_list|()
block|{
return|return
operator|new
name|MouseHandler
argument_list|()
return|;
block|}
comment|/**      * We don't want items in the list be automatically selected at all      */
annotation|@
name|Override
specifier|protected
name|ItemListener
name|createItemListener
parameter_list|()
block|{
return|return
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
block|}
block|}
return|;
block|}
comment|/**      * @return Whether match-check is 'strict'      */
specifier|public
name|boolean
name|isStrict
parameter_list|()
block|{
return|return
name|strict
return|;
block|}
specifier|protected
class|class
name|MouseHandler
extends|extends
name|MouseInputAdapter
block|{
annotation|@
name|Override
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|comboBox
operator|.
name|setSelectedItem
argument_list|(
name|list
operator|.
name|getSelectedValue
argument_list|()
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setPopupVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Workaround for cancelling an edited item (JVM bug 4530953).
if|if
condition|(
name|comboBox
operator|.
name|isEditable
argument_list|()
operator|&&
name|comboBox
operator|.
name|getEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|comboBox
operator|.
name|configureEditor
argument_list|(
name|comboBox
operator|.
name|getEditor
argument_list|()
argument_list|,
name|comboBox
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

