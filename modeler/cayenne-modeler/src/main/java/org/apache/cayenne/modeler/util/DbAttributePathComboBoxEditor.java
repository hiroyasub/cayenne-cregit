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
name|map
operator|.
name|DbEntity
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
name|DbRelationship
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
name|ObjAttribute
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
name|modeler
operator|.
name|editor
operator|.
name|ObjAttributeTableModel
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
name|JLabel
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
name|Collection
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

begin_class
specifier|public
class|class
name|DbAttributePathComboBoxEditor
extends|extends
name|PathChooserComboBoxCellEditor
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DB_ATTRIBUTE_PATH_COLUMN
init|=
name|ObjAttributeTableModel
operator|.
name|DB_ATTRIBUTE
decl_stmt|;
specifier|private
name|String
name|savePath
decl_stmt|;
specifier|private
name|ObjAttributeTableModel
name|model
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Component
name|getTableCellEditorComponent
parameter_list|(
name|JTable
name|table
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
operator|(
name|ObjAttributeTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
expr_stmt|;
name|this
operator|.
name|row
operator|=
name|row
expr_stmt|;
name|treeModel
operator|=
name|createTreeModelForComboBox
argument_list|(
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|treeModel
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|JLabel
argument_list|(
literal|"You should select table for this ObjectEntity"
argument_list|)
return|;
block|}
name|initializeCombo
argument_list|(
name|model
argument_list|,
name|row
argument_list|,
name|table
argument_list|)
expr_stmt|;
name|String
name|dbAttributePath
init|=
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
decl_stmt|;
name|previousEmbeddedLevel
operator|=
name|Util
operator|.
name|countMatches
argument_list|(
name|dbAttributePath
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
return|return
name|comboBoxPathChooser
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getCellEditorValue
parameter_list|()
block|{
return|return
name|model
operator|.
name|getValueAt
argument_list|(
name|row
argument_list|,
name|DB_ATTRIBUTE_PATH_COLUMN
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeCombo
parameter_list|(
name|CayenneTableModel
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
name|super
operator|.
name|initializeCombo
argument_list|(
name|model
argument_list|,
name|row
argument_list|,
name|table
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setSelectedItem
argument_list|(
operator|(
operator|(
name|ObjAttributeTableModel
operator|)
name|model
operator|)
operator|.
name|getAttribute
argument_list|(
name|row
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|savePath
operator|=
name|this
operator|.
name|model
operator|.
name|getAttribute
argument_list|(
name|row
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|getDbAttributePath
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getCurrentNodeToInitializeCombo
parameter_list|(
name|CayenneTableModel
name|model
parameter_list|,
name|int
name|row
parameter_list|)
block|{
return|return
name|getCurrentNode
argument_list|(
name|getPathToInitializeCombo
argument_list|(
name|model
argument_list|,
name|row
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getPathToInitializeCombo
parameter_list|(
name|CayenneTableModel
name|model
parameter_list|,
name|int
name|row
parameter_list|)
block|{
name|String
name|pathString
init|=
operator|(
operator|(
name|ObjAttributeTableModel
operator|)
name|model
operator|)
operator|.
name|getAttribute
argument_list|(
name|row
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathString
operator|==
literal|null
condition|)
block|{
return|return
literal|""
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
return|return
name|pathString
operator|.
name|replaceAll
argument_list|(
name|lastStringInPath
operator|+
literal|'$'
argument_list|,
literal|""
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|enterPressed
parameter_list|(
name|JTable
name|table
parameter_list|)
block|{
name|String
name|dbAttributePath
init|=
operator|(
operator|(
name|JTextComponent
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
name|getText
argument_list|()
decl_stmt|;
name|Object
name|currentNode
init|=
name|getCurrentNode
argument_list|(
name|dbAttributePath
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|currentNode
argument_list|)
operator|.
name|equals
argument_list|(
name|lastStringInPath
argument_list|)
operator|&&
name|currentNode
operator|instanceof
name|DbAttribute
condition|)
block|{
comment|// in this case choose is made.. we save data
if|if
condition|(
name|table
operator|.
name|getCellEditor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|getCellEditor
argument_list|()
operator|.
name|stopCellEditing
argument_list|()
expr_stmt|;
if|if
condition|(
name|dbAttributePath
operator|.
name|equals
argument_list|(
name|savePath
argument_list|)
condition|)
block|{
return|return;
block|}
name|model
operator|.
name|setUpdatedValueAt
argument_list|(
name|dbAttributePath
argument_list|,
name|row
argument_list|,
name|DB_ATTRIBUTE_PATH_COLUMN
argument_list|)
expr_stmt|;
name|model
operator|.
name|getAttribute
argument_list|(
name|row
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|setDbAttributePath
argument_list|(
name|dbAttributePath
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|currentNode
argument_list|)
operator|.
name|equals
argument_list|(
name|lastStringInPath
argument_list|)
operator|&&
name|currentNode
operator|instanceof
name|DbRelationship
condition|)
block|{
comment|// in this case we add dot  to pathString (if it is missing) and show variants for currentNode
if|if
condition|(
name|dbAttributePath
operator|.
name|charAt
argument_list|(
name|dbAttributePath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|!=
literal|'.'
condition|)
block|{
name|dbAttributePath
operator|=
name|dbAttributePath
operator|+
literal|'.'
expr_stmt|;
name|previousEmbeddedLevel
operator|=
name|Util
operator|.
name|countMatches
argument_list|(
name|dbAttributePath
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
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
name|dbAttributePath
argument_list|)
expr_stmt|;
block|}
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
name|dbAttributePath
argument_list|)
argument_list|,
name|dbAttributePath
argument_list|)
argument_list|)
decl_stmt|;
name|comboBoxPathChooser
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|currentNodeChildren
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setSelectedItem
argument_list|(
name|dbAttributePath
argument_list|)
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|showPopup
argument_list|()
expr_stmt|;
name|comboBoxPathChooser
operator|.
name|setPopupVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|EntityTreeModel
name|createTreeModelForComboBox
parameter_list|(
name|int
name|attributeIndexInTable
parameter_list|)
block|{
name|ObjAttribute
name|attribute
init|=
name|model
operator|.
name|getAttribute
argument_list|(
name|attributeIndexInTable
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Entity
name|firstEntity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getDbAttribute
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getParent
argument_list|()
operator|instanceof
name|ObjEntity
condition|)
block|{
name|DbEntity
name|dbEnt
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|attribute
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEnt
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
name|dbEnt
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|rel
init|=
name|dbEnt
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|attributes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|iterator
init|=
name|attributes
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|firstEntity
operator|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|rel
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|iterator
init|=
name|rel
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|firstEntity
operator|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getSourceEntity
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|firstEntity
operator|=
name|getFirstEntity
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstEntity
operator|!=
literal|null
condition|)
block|{
name|EntityTreeModel
name|treeModel
init|=
operator|new
name|EntityTreeModel
argument_list|(
name|firstEntity
argument_list|)
decl_stmt|;
name|treeModel
operator|.
name|setFilter
argument_list|(
operator|new
name|EntityTreeAttributeRelationshipFilter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|treeModel
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Entity
name|getFirstEntity
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|it
init|=
name|attribute
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
name|Entity
name|firstEnt
init|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|boolean
name|setEnt
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|ob
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ob
operator|instanceof
name|DbRelationship
condition|)
block|{
if|if
condition|(
operator|!
name|setEnt
condition|)
block|{
name|firstEnt
operator|=
operator|(
operator|(
name|DbRelationship
operator|)
name|ob
operator|)
operator|.
name|getSourceEntity
argument_list|()
expr_stmt|;
name|setEnt
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|ob
operator|instanceof
name|DbAttribute
condition|)
block|{
if|if
condition|(
operator|!
name|setEnt
condition|)
block|{
name|firstEnt
operator|=
operator|(
operator|(
name|DbAttribute
operator|)
name|ob
operator|)
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|firstEnt
return|;
block|}
block|}
end_class

end_unit

