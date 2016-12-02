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
name|welcome
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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
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
name|JList
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
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|MouseInputListener
import|;
end_import

begin_comment
comment|/**  * Renderer for the list of last files. Ignores the selection, instead paints with  * ROLLOVER_BACKGROUND (currently red) the row mouse is hovering over  */
end_comment

begin_class
class|class
name|RecentFileListRenderer
extends|extends
name|DefaultListCellRenderer
implements|implements
name|MouseInputListener
block|{
interface|interface
name|OnFileClickListener
block|{
name|void
name|onFileSelect
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
block|}
comment|/**      * Color for background of row mouse is over      */
specifier|private
specifier|final
name|Color
name|ROLLOVER_BACKGROUND
init|=
operator|new
name|Color
argument_list|(
literal|223
argument_list|,
literal|223
argument_list|,
literal|223
argument_list|)
decl_stmt|;
comment|/**      * List which is rendered      */
specifier|private
specifier|final
name|JList
argument_list|<
name|String
argument_list|>
name|list
decl_stmt|;
comment|/**      * Row mouse is over      */
specifier|private
name|int
name|rolloverRow
decl_stmt|;
specifier|private
name|OnFileClickListener
name|listener
decl_stmt|;
name|RecentFileListRenderer
parameter_list|(
name|JList
argument_list|<
name|String
argument_list|>
name|list
parameter_list|,
name|OnFileClickListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"listener parameter is null"
argument_list|)
throw|;
block|}
name|list
operator|.
name|addMouseListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|list
operator|.
name|addMouseMotionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
name|setHorizontalTextPosition
argument_list|(
name|SwingConstants
operator|.
name|LEADING
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|value
argument_list|,
name|index
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setMargin
argument_list|(
literal|8
argument_list|,
literal|15
argument_list|,
literal|8
argument_list|,
literal|15
argument_list|)
expr_stmt|;
if|if
condition|(
name|rolloverRow
operator|==
name|index
condition|)
block|{
name|setOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setBackground
argument_list|(
name|ROLLOVER_BACKGROUND
argument_list|)
expr_stmt|;
name|setToolTipText
argument_list|(
name|getFullText
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setMargin
parameter_list|(
name|int
name|top
parameter_list|,
name|int
name|left
parameter_list|,
name|int
name|bottom
parameter_list|,
name|int
name|right
parameter_list|)
block|{
name|setBorder
argument_list|(
operator|new
name|EmptyBorder
argument_list|(
name|top
argument_list|,
name|left
argument_list|,
name|bottom
argument_list|,
name|right
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|mouseMoved
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
name|list
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|private
name|String
name|getFullText
parameter_list|()
block|{
if|if
condition|(
name|rolloverRow
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|RecentFileListModel
operator|)
name|list
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|getFullElementAt
argument_list|(
name|rolloverRow
argument_list|)
return|;
block|}
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|SwingUtilities
operator|.
name|isLeftMouseButton
argument_list|(
name|e
argument_list|)
operator|||
name|rolloverRow
operator|==
operator|-
literal|1
condition|)
block|{
return|return;
block|}
name|listener
operator|.
name|onFileSelect
argument_list|(
name|getFullText
argument_list|()
argument_list|)
expr_stmt|;
name|rolloverRow
operator|=
operator|-
literal|1
expr_stmt|;
comment|// clear selection
block|}
specifier|public
name|void
name|mouseDragged
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseMoved
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|int
name|newRow
decl_stmt|;
comment|// Check that a row boundary contains the mouse point, so that rolloverRow
comment|// would be -1 if we are below last row
name|Rectangle
name|bounds
init|=
name|list
operator|.
name|getCellBounds
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|getModel
argument_list|()
operator|.
name|getSize
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|bounds
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
condition|)
block|{
name|newRow
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|newRow
operator|=
name|list
operator|.
name|locationToIndex
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rolloverRow
operator|!=
name|newRow
condition|)
block|{
name|rolloverRow
operator|=
name|newRow
expr_stmt|;
name|list
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

