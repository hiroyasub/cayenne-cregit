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
name|BorderFactory
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
name|border
operator|.
name|CompoundBorder
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
name|Font
import|;
end_import

begin_class
specifier|public
class|class
name|SortButtonRenderer
extends|extends
name|DefaultTableCellRenderer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|NONE
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DOWN
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|UP
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Icon
name|ICON_DOWN
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-sort-desc.png"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Icon
name|ICON_UP
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-sort-asc.png"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Font
name|FONT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|CompoundBorder
name|BORDER
init|=
name|BorderFactory
operator|.
name|createCompoundBorder
argument_list|(
name|BorderFactory
operator|.
name|createMatteBorder
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
name|Color
operator|.
name|GRAY
argument_list|)
argument_list|,
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
decl_stmt|;
static|static
block|{
comment|// Get default font for current system
name|FONT
operator|=
operator|new
name|JLabel
argument_list|()
operator|.
name|getFont
argument_list|()
operator|.
name|deriveFont
argument_list|(
name|Font
operator|.
name|BOLD
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|sortingEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|int
name|currentState
decl_stmt|;
specifier|private
name|int
name|currentColumn
decl_stmt|;
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
if|if
condition|(
name|sortingEnabled
operator|&&
name|column
operator|==
name|currentColumn
condition|)
block|{
if|if
condition|(
name|currentState
operator|==
name|DOWN
condition|)
block|{
name|setIcon
argument_list|(
name|ICON_DOWN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setIcon
argument_list|(
name|ICON_UP
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|setIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|setText
argument_list|(
name|value
operator|==
literal|null
condition|?
literal|""
else|:
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|setFont
argument_list|(
name|FONT
argument_list|)
expr_stmt|;
name|setHorizontalTextPosition
argument_list|(
name|JLabel
operator|.
name|LEFT
argument_list|)
expr_stmt|;
name|setBorder
argument_list|(
name|BORDER
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setSelectedColumn
parameter_list|(
name|int
name|col
parameter_list|,
name|boolean
name|isAscOrder
parameter_list|)
block|{
if|if
condition|(
name|col
operator|<
literal|0
condition|)
block|{
return|return;
block|}
comment|//shows the direction of ordering
if|if
condition|(
name|isAscOrder
condition|)
block|{
name|currentState
operator|=
name|DOWN
expr_stmt|;
block|}
else|else
block|{
name|currentState
operator|=
name|UP
expr_stmt|;
block|}
name|currentColumn
operator|=
name|col
expr_stmt|;
block|}
specifier|public
name|int
name|getState
parameter_list|(
name|int
name|col
parameter_list|)
block|{
if|if
condition|(
name|col
operator|==
name|currentColumn
condition|)
block|{
return|return
name|currentState
return|;
block|}
return|return
name|NONE
return|;
block|}
specifier|public
name|boolean
name|isSortingEnabled
parameter_list|()
block|{
return|return
name|sortingEnabled
return|;
block|}
specifier|public
name|void
name|setSortingEnabled
parameter_list|(
name|boolean
name|sortingEnabled
parameter_list|)
block|{
name|this
operator|.
name|sortingEnabled
operator|=
name|sortingEnabled
expr_stmt|;
block|}
block|}
end_class

end_unit

