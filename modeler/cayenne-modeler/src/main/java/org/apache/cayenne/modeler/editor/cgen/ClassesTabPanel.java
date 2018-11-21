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
operator|.
name|cgen
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
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
name|ScrollPaneConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|UIManager
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ClassesTabPanel
extends|extends
name|JPanel
block|{
specifier|protected
name|JTable
name|table
decl_stmt|;
specifier|private
name|JCheckBox
name|checkAll
decl_stmt|;
specifier|private
name|JLabel
name|checkAllLabel
decl_stmt|;
name|ClassesTabPanel
parameter_list|()
block|{
name|this
operator|.
name|table
operator|=
operator|new
name|JTable
argument_list|()
expr_stmt|;
name|this
operator|.
name|table
operator|.
name|setRowHeight
argument_list|(
literal|22
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkAll
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|checkAllLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|"Check All Classes"
argument_list|)
expr_stmt|;
name|checkAll
operator|.
name|addItemListener
argument_list|(
name|event
lambda|->
block|{
if|if
condition|(
name|checkAll
operator|.
name|isSelected
argument_list|()
condition|)
block|{
name|checkAllLabel
operator|.
name|setText
argument_list|(
literal|"Uncheck All Classess"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkAllLabel
operator|.
name|setText
argument_list|(
literal|"Check All Classes"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// assemble
name|JPanel
name|topPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEADING
argument_list|)
argument_list|)
decl_stmt|;
name|topPanel
operator|.
name|setBorder
argument_list|(
name|UIManager
operator|.
name|getBorder
argument_list|(
literal|"ToolBar.border"
argument_list|)
argument_list|)
expr_stmt|;
name|topPanel
operator|.
name|add
argument_list|(
name|checkAll
argument_list|)
expr_stmt|;
name|topPanel
operator|.
name|add
argument_list|(
name|checkAllLabel
argument_list|)
expr_stmt|;
name|JScrollPane
name|tablePanel
init|=
operator|new
name|JScrollPane
argument_list|(
name|table
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_NEVER
argument_list|)
decl_stmt|;
comment|// set some minimal preferred size, so that it is smaller than other forms used in
comment|// the dialog... this way we get the right automated overall size
name|tablePanel
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|450
argument_list|,
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|topPanel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|tablePanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
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
name|JCheckBox
name|getCheckAll
parameter_list|()
block|{
return|return
name|checkAll
return|;
block|}
block|}
end_class

end_unit

