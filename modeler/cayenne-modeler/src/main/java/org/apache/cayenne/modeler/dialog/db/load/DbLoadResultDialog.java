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
name|load
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
name|DefaultFormBuilder
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
name|map
operator|.
name|DataMap
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
name|JButton
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
name|EmptyBorder
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
name|DefaultTableModel
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DbLoadResultDialog
extends|extends
name|JDialog
block|{
specifier|private
specifier|static
specifier|final
name|int
name|TABLE_ROW_HIGH
init|=
literal|24
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TABLE_ROW_MARGIN
init|=
literal|3
decl_stmt|;
specifier|private
name|JButton
name|okButton
decl_stmt|;
specifier|private
name|JButton
name|revertButton
decl_stmt|;
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|DefaultFormBuilder
name|builder
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|JTable
argument_list|>
name|tableForMap
decl_stmt|;
specifier|private
name|JPanel
name|tablePanel
decl_stmt|;
specifier|private
name|JPanel
name|buttonPanel
decl_stmt|;
specifier|private
name|JScrollPane
name|scrollPane
decl_stmt|;
specifier|public
name|DbLoadResultDialog
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
name|this
operator|.
name|tableForMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|tablePanel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|this
operator|.
name|tablePanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BoxLayout
argument_list|(
name|this
operator|.
name|tablePanel
argument_list|,
name|BoxLayout
operator|.
name|Y_AXIS
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|scrollPane
operator|=
operator|new
name|JScrollPane
argument_list|(
name|tablePanel
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_ALWAYS
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
expr_stmt|;
name|this
operator|.
name|buttonPanel
operator|=
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
expr_stmt|;
name|initElements
argument_list|()
expr_stmt|;
name|buildElements
argument_list|()
expr_stmt|;
name|configureDialog
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|configureDialog
parameter_list|()
block|{
name|this
operator|.
name|setResizable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLocationRelativeTo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|setModal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAlwaysOnTop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|400
argument_list|,
literal|400
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|scrollPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|400
argument_list|,
literal|330
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initElements
parameter_list|()
block|{
name|revertButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Revert"
argument_list|)
expr_stmt|;
name|okButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|buildElements
parameter_list|()
block|{
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|okButton
argument_list|)
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"fill:200dlu"
argument_list|)
decl_stmt|;
name|builder
operator|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|scrollPane
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|revertButton
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|okButton
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|buttonPanel
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DefaultTableModel
name|prepareTable
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
if|if
condition|(
name|tableForMap
operator|.
name|containsKey
argument_list|(
name|dataMap
argument_list|)
condition|)
block|{
return|return
operator|(
name|DefaultTableModel
operator|)
name|tableForMap
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
operator|.
name|getModel
argument_list|()
return|;
block|}
name|DefaultTableModel
name|tokensTableModel
init|=
operator|new
name|DefaultTableModel
argument_list|()
block|{
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
name|col
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|JPanel
name|tablePane
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
name|dataMapLabel
init|=
operator|new
name|JLabel
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s"
argument_list|,
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|dataMapLabel
operator|.
name|setBorder
argument_list|(
operator|new
name|EmptyBorder
argument_list|(
literal|5
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|tablePane
operator|.
name|add
argument_list|(
name|dataMapLabel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|JTable
name|tokensTable
init|=
operator|new
name|JTable
argument_list|(
name|tokensTableModel
argument_list|)
decl_stmt|;
name|tokensTable
operator|.
name|setSelectionMode
argument_list|(
name|ListSelectionModel
operator|.
name|SINGLE_SELECTION
argument_list|)
expr_stmt|;
name|tokensTable
operator|.
name|setRowHeight
argument_list|(
name|TABLE_ROW_HIGH
argument_list|)
expr_stmt|;
name|tokensTable
operator|.
name|setRowMargin
argument_list|(
name|TABLE_ROW_MARGIN
argument_list|)
expr_stmt|;
name|tokensTableModel
operator|.
name|addColumn
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|tablePane
operator|.
name|add
argument_list|(
name|tokensTable
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|tableForMap
operator|.
name|put
argument_list|(
name|dataMap
argument_list|,
name|tokensTable
argument_list|)
expr_stmt|;
name|tablePanel
operator|.
name|add
argument_list|(
name|tablePane
argument_list|)
expr_stmt|;
return|return
name|tokensTableModel
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|addRowToOutput
parameter_list|(
name|String
name|output
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|prepareTable
argument_list|(
name|dataMap
argument_list|)
operator|.
name|addRow
argument_list|(
operator|new
name|Object
index|[]
block|{
name|output
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|addMsg
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|prepareTable
argument_list|(
name|dataMap
argument_list|)
operator|.
name|addRow
argument_list|(
operator|new
name|Object
index|[]
block|{
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s"
argument_list|,
literal|"No changes to import"
argument_list|)
block|}
argument_list|)
expr_stmt|;
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
name|JButton
name|getRevertButton
parameter_list|()
block|{
return|return
name|revertButton
return|;
block|}
specifier|public
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|JTable
argument_list|>
name|getTableForMap
parameter_list|()
block|{
return|return
name|tableForMap
return|;
block|}
specifier|public
name|JPanel
name|getTablePanel
parameter_list|()
block|{
return|return
name|tablePanel
return|;
block|}
block|}
end_class

end_unit

