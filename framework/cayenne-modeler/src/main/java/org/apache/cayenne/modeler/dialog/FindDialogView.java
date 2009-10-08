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
name|FlowLayout
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
name|Comparator
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|BoxLayout
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
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
name|InputMap
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
name|JDialog
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
name|JTable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|KeyStroke
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
name|table
operator|.
name|DefaultTableCellRenderer
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
name|Embeddable
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
name|map
operator|.
name|ObjRelationship
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
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * Swing component displaying results produced by search feature.  */
end_comment

begin_class
specifier|public
class|class
name|FindDialogView
extends|extends
name|JDialog
block|{
specifier|private
name|JButton
name|okButton
decl_stmt|;
specifier|private
specifier|static
name|JScrollPane
name|scrollPane
decl_stmt|;
specifier|private
name|JTable
name|table
decl_stmt|;
specifier|private
specifier|static
name|Map
name|LabelAndObjectIndex
decl_stmt|;
specifier|public
specifier|static
name|Map
name|getLabelAndObjectIndex
parameter_list|()
block|{
return|return
name|LabelAndObjectIndex
return|;
block|}
specifier|public
name|JTable
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|FindDialogView
parameter_list|(
name|Map
name|objEntityNames
parameter_list|,
name|Map
name|dbEntityNames
parameter_list|,
name|Map
name|attrNames
parameter_list|,
name|Map
name|relatNames
parameter_list|,
name|Map
name|queryNames
parameter_list|,
name|Map
name|embeddableNames
parameter_list|,
name|Map
name|embeddableAttributeNames
parameter_list|)
block|{
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|panel
operator|.
name|setLayout
argument_list|(
operator|new
name|BoxLayout
argument_list|(
name|panel
argument_list|,
name|BoxLayout
operator|.
name|Y_AXIS
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|objEntityNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|dbEntityNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|attrNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|relatNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|queryNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|embeddableNames
operator|.
name|isEmpty
argument_list|()
operator|&&
name|embeddableAttributeNames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|panel
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"Nothing found!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|curentLineInTable
init|=
literal|0
decl_stmt|;
name|int
name|sizeDataVector
init|=
name|objEntityNames
operator|.
name|size
argument_list|()
operator|+
name|dbEntityNames
operator|.
name|size
argument_list|()
operator|+
name|attrNames
operator|.
name|size
argument_list|()
operator|+
name|relatNames
operator|.
name|size
argument_list|()
operator|+
name|queryNames
operator|.
name|size
argument_list|()
operator|+
name|embeddableNames
operator|.
name|size
argument_list|()
operator|+
name|embeddableAttributeNames
operator|.
name|size
argument_list|()
decl_stmt|;
name|Object
index|[]
index|[]
name|dataVector
init|=
operator|new
name|Object
index|[
name|sizeDataVector
index|]
index|[]
decl_stmt|;
name|TableModel
name|tableModel
init|=
operator|new
name|TableModel
argument_list|()
decl_stmt|;
name|LabelAndObjectIndex
operator|=
operator|new
name|HashMap
argument_list|<
name|JLabel
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|objEntityNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjEntity
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|objEntityNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|embeddableNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|Embeddable
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|curentLineInTable
operator|+
name|embeddableNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|dbEntityNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|DbEntity
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|curentLineInTable
operator|+
name|dbEntityNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|attrNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjAttribute
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|curentLineInTable
operator|+
name|attrNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|embeddableAttributeNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjAttribute
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|curentLineInTable
operator|+
name|embeddableAttributeNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|relatNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjRelationship
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|=
name|curentLineInTable
operator|+
name|relatNames
operator|.
name|size
argument_list|()
expr_stmt|;
name|dataVector
operator|=
name|createResultTable
argument_list|(
name|queryNames
argument_list|,
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|SelectQuery
argument_list|()
argument_list|)
argument_list|,
name|dataVector
argument_list|,
name|curentLineInTable
argument_list|)
expr_stmt|;
name|tableModel
operator|.
name|setDataVector
argument_list|(
name|dataVector
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|""
block|}
argument_list|)
expr_stmt|;
name|table
operator|=
operator|new
name|JTable
argument_list|(
name|tableModel
argument_list|)
expr_stmt|;
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
name|setCellRenderer
argument_list|(
operator|new
name|ImageRenderer
argument_list|()
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
name|InputMap
name|im
init|=
name|table
operator|.
name|getInputMap
argument_list|(
name|JComponent
operator|.
name|WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
argument_list|)
decl_stmt|;
name|InputMap
name|imParent
init|=
name|im
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|imParent
operator|.
name|remove
argument_list|(
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_ENTER
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|im
operator|.
name|setParent
argument_list|(
name|imParent
argument_list|)
expr_stmt|;
name|im
operator|.
name|remove
argument_list|(
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_ENTER
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setInputMap
argument_list|(
name|JComponent
operator|.
name|WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
argument_list|,
name|im
argument_list|)
expr_stmt|;
block|}
name|JPanel
name|okPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|okButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
name|okPanel
operator|.
name|add
argument_list|(
name|okButton
argument_list|)
expr_stmt|;
name|JComponent
name|contentPane
init|=
operator|(
name|JComponent
operator|)
name|getContentPane
argument_list|()
decl_stmt|;
name|contentPane
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|scrollPane
operator|=
operator|new
name|JScrollPane
argument_list|(
comment|// panel
name|table
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|add
argument_list|(
name|scrollPane
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|add
argument_list|(
name|okPanel
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|400
argument_list|,
literal|325
argument_list|)
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"Search results"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|createResultTable
parameter_list|(
name|Map
name|names
parameter_list|,
name|Icon
name|icon
parameter_list|,
name|Object
index|[]
index|[]
name|dataVector
parameter_list|,
name|int
name|curentLineInTable
parameter_list|)
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparer
init|=
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Map
name|sortedByNameMap
init|=
name|sortMapByValue
argument_list|(
name|names
argument_list|,
name|comparer
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|sortedByNameMap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Object
index|[]
name|objectHelper
init|=
operator|new
name|Object
index|[]
block|{}
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Integer
name|index
init|=
operator|(
name|Integer
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|JLabel
name|labelIcon
init|=
operator|new
name|JLabel
argument_list|()
decl_stmt|;
name|labelIcon
operator|.
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setText
argument_list|(
operator|(
name|String
operator|)
name|sortedByNameMap
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|objectHelper
operator|=
operator|new
name|Object
index|[]
block|{
name|labelIcon
block|}
expr_stmt|;
name|dataVector
index|[
name|curentLineInTable
index|]
operator|=
name|objectHelper
expr_stmt|;
name|LabelAndObjectIndex
operator|.
name|put
argument_list|(
name|labelIcon
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|curentLineInTable
operator|++
expr_stmt|;
block|}
return|return
name|dataVector
return|;
block|}
specifier|public
name|JButton
name|getOkButton
parameter_list|()
block|{
return|return
name|okButton
return|;
block|}
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|sortMapByValue
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|in
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|compare
parameter_list|)
block|{
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|swapped
init|=
operator|new
name|TreeMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|compare
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|in
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|swapped
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|entry
range|:
name|swapped
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

begin_class
class|class
name|ImageRenderer
extends|extends
name|DefaultTableCellRenderer
block|{
name|JLabel
name|lbl
init|=
operator|new
name|JLabel
argument_list|()
decl_stmt|;
name|ImageIcon
name|icon
init|=
literal|null
decl_stmt|;
name|ImageRenderer
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getTableCellRendererComponent
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
name|boolean
name|hasFocus
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|lbl
operator|.
name|setOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setText
argument_list|(
operator|(
operator|(
name|JLabel
operator|)
name|value
operator|)
operator|.
name|getText
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setIcon
argument_list|(
operator|(
operator|(
name|JLabel
operator|)
name|value
operator|)
operator|.
name|getIcon
argument_list|()
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createLineBorder
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setHorizontalAlignment
argument_list|(
name|JLabel
operator|.
name|LEFT
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|setFont
argument_list|(
name|isSelected
condition|?
name|FindDialog
operator|.
name|getFontSelected
argument_list|()
else|:
name|FindDialog
operator|.
name|getFont
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|lbl
return|;
block|}
block|}
end_class

begin_class
class|class
name|TableModel
extends|extends
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|DefaultTableModel
block|{
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

