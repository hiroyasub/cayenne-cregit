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
name|Map
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
name|DefaultButtonModel
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
name|SwingUtilities
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
name|EmptyBorder
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
name|java
operator|.
name|util
operator|.
name|List
name|entityButtons
decl_stmt|;
specifier|private
specifier|static
name|JScrollPane
name|scrollPane
decl_stmt|;
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
parameter_list|)
block|{
name|entityButtons
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
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
name|panel
operator|.
name|add
argument_list|(
name|createResultPanel
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
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|createResultPanel
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
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|createResultPanel
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
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|createResultPanel
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
argument_list|)
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
name|panel
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
name|JPanel
name|createResultPanel
parameter_list|(
name|Map
name|names
parameter_list|,
name|Icon
name|icon
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
name|Iterator
name|it
init|=
name|names
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
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
name|JButton
name|b
init|=
operator|new
name|JButton
argument_list|(
operator|(
name|String
operator|)
name|names
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|,
name|icon
argument_list|)
decl_stmt|;
name|b
operator|.
name|setBorder
argument_list|(
operator|new
name|EmptyBorder
argument_list|(
literal|2
argument_list|,
literal|10
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
comment|// top, left, bottom, right
name|b
operator|.
name|setModel
argument_list|(
operator|new
name|EntityButtonModel
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|entityButtons
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|panel
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
name|java
operator|.
name|util
operator|.
name|List
name|getEntityButtons
parameter_list|()
block|{
return|return
name|entityButtons
return|;
block|}
specifier|public
class|class
name|EntityButtonModel
extends|extends
name|DefaultButtonModel
block|{
specifier|private
name|Integer
name|index
decl_stmt|;
name|EntityButtonModel
parameter_list|(
name|Integer
name|index
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
specifier|public
name|Integer
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|scrollPaneToBottom
parameter_list|()
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|scrollPane
operator|.
name|getVerticalScrollBar
argument_list|()
operator|.
name|setValue
argument_list|(
name|scrollPane
operator|.
name|getVerticalScrollBar
argument_list|()
operator|.
name|getMaximum
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|scrollPaneToUp
parameter_list|()
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|scrollPane
operator|.
name|getVerticalScrollBar
argument_list|()
operator|.
name|setValue
argument_list|(
name|scrollPane
operator|.
name|getVerticalScrollBar
argument_list|()
operator|.
name|getMinimum
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|scrollPaneToPosition
parameter_list|(
specifier|final
name|int
name|position
parameter_list|)
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|scrollPane
operator|.
name|getVerticalScrollBar
argument_list|()
operator|.
name|setValue
argument_list|(
name|position
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

