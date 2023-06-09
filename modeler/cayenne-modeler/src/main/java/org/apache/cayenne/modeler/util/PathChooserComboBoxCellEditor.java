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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|AbstractCellEditor
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
name|ImageIcon
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
name|JTable
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
name|event
operator|.
name|PopupMenuEvent
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
name|PopupMenuListener
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
name|text
operator|.
name|JTextComponent
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
name|KeyAdapter
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
name|KeyEvent
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * This class used as cell editor, when you need to  * choose path in comboBox and use autocompletion.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PathChooserComboBoxCellEditor
parameter_list|<
name|T
extends|extends
name|CayenneTableModel
parameter_list|<
name|?
parameter_list|>
parameter_list|>
extends|extends
name|AbstractCellEditor
implements|implements
name|TableCellEditor
implements|,
name|ActionListener
implements|,
name|PopupMenuListener
block|{
specifier|protected
name|JComboBox
argument_list|<
name|String
argument_list|>
name|comboBoxPathChooser
decl_stmt|;
specifier|protected
name|int
name|previousEmbeddedLevel
init|=
literal|0
decl_stmt|;
specifier|protected
name|EntityTreeModel
name|treeModel
decl_stmt|;
specifier|protected
name|int
name|row
decl_stmt|;
specifier|private
name|JTable
name|table
decl_stmt|;
specifier|protected
specifier|abstract
name|void
name|enterPressed
parameter_list|(
name|JTable
name|table
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|EntityTreeModel
name|createTreeModelForComboBox
parameter_list|(
name|int
name|indexInTable
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|Object
name|getCurrentNodeToInitializeCombo
parameter_list|(
name|T
name|model
parameter_list|,
name|int
name|row
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|String
name|getPathToInitializeCombo
parameter_list|(
name|T
name|model
parameter_list|,
name|int
name|row
parameter_list|)
function_decl|;
specifier|protected
name|void
name|initializeCombo
parameter_list|(
name|T
name|model
parameter_list|,
name|int
name|row
parameter_list|,
specifier|final
name|JTable
name|table
parameter_list|)
block|{
name|Object
name|currentNode
init|=
name|getCurrentNodeToInitializeCombo
argument_list|(
name|model
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|String
name|dbAttributePath
init|=
name|getPathToInitializeCombo
argument_list|(
name|model
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|nodeChildren
init|=
name|getChildren
argument_list|(
name|currentNode
argument_list|,
name|dbAttributePath
argument_list|)
decl_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|comboBoxPathChooser
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|nodeChildren
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|.
name|addKeyListener
argument_list|(
operator|new
name|KeyAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_ENTER
condition|)
block|{
name|enterPressed
argument_list|(
name|table
argument_list|)
expr_stmt|;
return|return;
block|}
name|parsePathString
argument_list|(
name|event
operator|.
name|getKeyChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|comboBoxPathChooser
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JComponent
operator|)
name|comboBoxPathChooser
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|)
operator|.
name|setBorder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setRenderer
argument_list|(
operator|new
name|PathChooserComboBoxCellRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|addPopupMenuListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setComboModelAccordingToPath
parameter_list|(
name|String
name|pathString
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|currentNodeChildren
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getChildren
argument_list|(
name|getCurrentNode
argument_list|(
name|pathString
argument_list|)
argument_list|,
name|pathString
argument_list|)
argument_list|)
decl_stmt|;
name|comboBoxPathChooser
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|currentNodeChildren
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setSelectedItem
argument_list|(
name|pathString
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|pathString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|comboBoxPathChooser
operator|.
name|showPopup
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|parsePathString
parameter_list|(
name|char
name|lastEnteredCharacter
parameter_list|)
block|{
name|JTextComponent
name|editorComponent
init|=
operator|(
name|JTextComponent
operator|)
operator|(
name|comboBoxPathChooser
operator|)
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
decl_stmt|;
name|String
name|pathString
init|=
name|editorComponent
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathString
operator|!=
literal|null
operator|&&
name|pathString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setComboModelAccordingToPath
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|previousEmbeddedLevel
operator|=
literal|0
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|lastEnteredCharacter
operator|==
literal|'.'
condition|)
block|{
name|processDotEntered
argument_list|()
expr_stmt|;
name|previousEmbeddedLevel
operator|=
name|Util
operator|.
name|countMatches
argument_list|(
name|pathString
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|currentEmbeddedLevel
init|=
name|Util
operator|.
name|countMatches
argument_list|(
name|pathString
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousEmbeddedLevel
operator|!=
name|currentEmbeddedLevel
condition|)
block|{
name|previousEmbeddedLevel
operator|=
name|currentEmbeddedLevel
expr_stmt|;
name|String
index|[]
name|pathStrings
init|=
name|pathString
operator|.
name|split
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|lastStringInPath
init|=
name|pathStrings
index|[
name|pathStrings
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|String
name|saveDbAttributePath
init|=
name|pathString
decl_stmt|;
name|pathString
operator|=
name|pathString
operator|.
name|replaceAll
argument_list|(
name|lastStringInPath
operator|+
literal|"$"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|currentNodeChildren
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getChildren
argument_list|(
name|getCurrentNode
argument_list|(
name|pathString
argument_list|)
argument_list|,
name|pathString
argument_list|)
argument_list|)
decl_stmt|;
name|comboBoxPathChooser
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|currentNodeChildren
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setSelectedItem
argument_list|(
name|saveDbAttributePath
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|processDotEntered
parameter_list|()
block|{
name|JTextComponent
name|editorComponent
init|=
operator|(
name|JTextComponent
operator|)
operator|(
name|comboBoxPathChooser
operator|)
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
decl_stmt|;
name|String
name|dbAttributePath
init|=
name|editorComponent
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"."
operator|.
name|equals
argument_list|(
name|dbAttributePath
argument_list|)
condition|)
block|{
name|setComboModelAccordingToPath
argument_list|(
literal|""
argument_list|)
expr_stmt|;
return|return;
block|}
name|char
name|secondFromEndCharacter
init|=
name|dbAttributePath
operator|.
name|charAt
argument_list|(
name|dbAttributePath
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|secondFromEndCharacter
operator|==
literal|'.'
condition|)
block|{
comment|// two dots entered one by one , we replace it by one dot
name|editorComponent
operator|.
name|setText
argument_list|(
name|dbAttributePath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dbAttributePath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
index|[]
name|pathStrings
init|=
name|dbAttributePath
operator|.
name|split
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|lastStringInPath
init|=
name|pathStrings
index|[
name|pathStrings
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
comment|//we will check if lastStringInPath is correct name of DbAttribute or DbRelationship
comment|//for appropriate previous node in path. if it is not we won't add entered dot to dbAttributePath
name|String
name|dbAttributePathForPreviousNode
decl_stmt|;
if|if
condition|(
name|pathStrings
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|//previous root is treeModel.getRoot()
name|dbAttributePathForPreviousNode
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|dbAttributePathForPreviousNode
operator|=
name|dbAttributePath
operator|.
name|replaceAll
argument_list|(
literal|'.'
operator|+
name|lastStringInPath
operator|+
literal|".$"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|potentialVariantsToChoose
init|=
name|getChildren
argument_list|(
name|getCurrentNode
argument_list|(
name|dbAttributePathForPreviousNode
argument_list|)
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|potentialVariantsToChoose
operator|.
name|contains
argument_list|(
name|lastStringInPath
argument_list|)
operator|&&
operator|!
operator|(
name|getCurrentNode
argument_list|(
name|dbAttributePath
argument_list|)
operator|instanceof
name|DbAttribute
operator|)
condition|)
block|{
name|setComboModelAccordingToPath
argument_list|(
name|dbAttributePath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|editorComponent
operator|.
name|setText
argument_list|(
name|dbAttributePath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dbAttributePath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * find current node by path      *      * @param pathString db path      * @return last node in path which matches DbRelationship or DbAttribute      */
specifier|protected
name|Object
name|getCurrentNode
parameter_list|(
name|String
name|pathString
parameter_list|)
block|{
comment|//case for new attribute
if|if
condition|(
name|pathString
operator|==
literal|null
operator|||
name|pathString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|treeModel
operator|.
name|getRoot
argument_list|()
return|;
block|}
name|String
index|[]
name|pathStrings
init|=
name|pathString
operator|.
name|split
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|root
init|=
name|treeModel
operator|.
name|getRoot
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|rootChildText
range|:
name|pathStrings
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|treeModel
operator|.
name|getChildCount
argument_list|(
name|root
argument_list|)
condition|;
name|j
operator|++
control|)
block|{
name|Object
name|child
init|=
name|treeModel
operator|.
name|getChild
argument_list|(
name|root
argument_list|,
name|j
argument_list|)
decl_stmt|;
name|String
name|objectName
init|=
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|child
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectName
operator|.
name|equals
argument_list|(
name|rootChildText
argument_list|)
condition|)
block|{
name|root
operator|=
name|child
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|root
return|;
block|}
comment|/**      * @param node       for which we will find children      * @param pathString string which will be added to each child to make right autocomplete      * @return list with children , which will be used to autocomplete      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getChildren
parameter_list|(
name|Object
name|node
parameter_list|,
name|String
name|pathString
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|currentNodeChildren
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|treeModel
operator|.
name|getChildCount
argument_list|(
name|node
argument_list|)
condition|;
name|j
operator|++
control|)
block|{
name|Object
name|child
init|=
name|treeModel
operator|.
name|getChild
argument_list|(
name|node
argument_list|,
name|j
argument_list|)
decl_stmt|;
name|String
name|relationshipName
init|=
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|child
argument_list|)
decl_stmt|;
name|currentNodeChildren
operator|.
name|add
argument_list|(
name|pathString
operator|+
name|relationshipName
argument_list|)
expr_stmt|;
block|}
return|return
name|currentNodeChildren
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
comment|//for some reason comboBoxPathChooser don't load selected item text, so we made it by hand
if|if
condition|(
name|comboBoxPathChooser
operator|.
name|getSelectedIndex
argument_list|()
operator|!=
operator|(
operator|-
literal|1
operator|)
condition|)
block|{
operator|(
operator|(
name|JTextComponent
operator|)
operator|(
name|comboBoxPathChooser
operator|)
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|)
operator|.
name|setText
argument_list|(
name|comboBoxPathChooser
operator|.
name|getSelectedItem
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|popupMenuWillBecomeInvisible
parameter_list|(
name|PopupMenuEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|comboBoxPathChooser
operator|.
name|getSelectedIndex
argument_list|()
operator|!=
operator|-
literal|1
operator|&&
operator|!
operator|(
operator|(
name|JTextComponent
operator|)
operator|(
name|comboBoxPathChooser
operator|)
operator|.
name|getEditor
argument_list|()
operator|.
name|getEditorComponent
argument_list|()
operator|)
operator|.
name|getText
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|enterPressed
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|popupMenuWillBecomeVisible
parameter_list|(
name|PopupMenuEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|popupMenuCanceled
parameter_list|(
name|PopupMenuEvent
name|e
parameter_list|)
block|{
block|}
specifier|private
specifier|final
class|class
name|PathChooserComboBoxCellRenderer
extends|extends
name|DefaultListCellRenderer
block|{
specifier|private
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
annotation|@
name|Override
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
argument_list|<
name|?
argument_list|>
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
name|JLabel
name|label
init|=
operator|new
name|JLabel
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
decl_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|Object
name|currentNode
init|=
name|getCurrentNode
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|treeModel
operator|.
name|isLeaf
argument_list|(
name|currentNode
argument_list|)
condition|)
block|{
name|ListCellRenderer
argument_list|<
name|Object
argument_list|>
name|leafRenderer
init|=
name|CellRenderers
operator|.
name|listRenderer
argument_list|()
decl_stmt|;
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
else|else
block|{
name|DefaultListCellRenderer
name|nonLeafTextRenderer
init|=
operator|new
name|DefaultListCellRenderer
argument_list|()
decl_stmt|;
name|Component
name|text
init|=
name|nonLeafTextRenderer
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
decl_stmt|;
name|panel
operator|.
name|setBackground
argument_list|(
name|text
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|panel
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
return|return
name|panel
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

