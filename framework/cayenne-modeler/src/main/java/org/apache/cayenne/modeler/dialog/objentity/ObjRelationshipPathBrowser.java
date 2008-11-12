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
name|objentity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseAdapter
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
name|SwingUtilities
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
name|ListSelectionEvent
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
name|ListSelectionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|MultiColumnBrowser
import|;
end_import

begin_comment
comment|/**  * Multi-column browser for obj relationships  */
end_comment

begin_class
specifier|public
class|class
name|ObjRelationshipPathBrowser
extends|extends
name|MultiColumnBrowser
block|{
comment|/**      * Listener, which performs adding of new column      */
specifier|private
name|MouseListener
name|panelOpener
decl_stmt|;
comment|/**      * Listener, which performs removing of columns to the right of selected row      */
specifier|private
name|ListSelectionListener
name|panelRemover
decl_stmt|;
specifier|public
name|ObjRelationshipPathBrowser
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_MIN_COLUMNS_COUNT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjRelationshipPathBrowser
parameter_list|(
name|int
name|minColumns
parameter_list|)
block|{
name|super
argument_list|(
name|minColumns
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|installColumn
parameter_list|(
name|BrowserPanel
name|panel
parameter_list|)
block|{
if|if
condition|(
name|panelOpener
operator|==
literal|null
condition|)
block|{
name|panelOpener
operator|=
operator|new
name|PanelOpener
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|panelRemover
operator|==
literal|null
condition|)
block|{
name|panelRemover
operator|=
operator|new
name|PanelRemover
argument_list|()
expr_stmt|;
block|}
name|panel
operator|.
name|addMouseListener
argument_list|(
name|panelOpener
argument_list|)
expr_stmt|;
name|panel
operator|.
name|addListSelectionListener
argument_list|(
name|panelRemover
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setCellRenderer
argument_list|(
name|renderer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Selects one path component.      * Need to override this method, because list selection does not cause loading      * in this browser.      */
annotation|@
name|Override
specifier|protected
name|void
name|selectRow
parameter_list|(
name|Object
name|row
parameter_list|,
name|int
name|index
parameter_list|,
name|TreePath
name|path
parameter_list|)
block|{
if|if
condition|(
name|index
operator|>
literal|0
operator|&&
name|columns
operator|.
name|get
argument_list|(
name|index
operator|-
literal|1
argument_list|)
operator|.
name|getSelectedValue
argument_list|()
operator|!=
name|row
condition|)
block|{
name|columns
operator|.
name|get
argument_list|(
name|index
operator|-
literal|1
argument_list|)
operator|.
name|setSelectedValue
argument_list|(
name|row
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|index
operator|!=
name|path
operator|.
name|getPathCount
argument_list|()
operator|-
literal|1
condition|)
block|{
name|updateFromModel
argument_list|(
name|row
argument_list|,
name|index
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|uninstallColumn
parameter_list|(
name|BrowserPanel
name|panel
parameter_list|)
block|{
name|panel
operator|.
name|removeMouseListener
argument_list|(
name|panelOpener
argument_list|)
expr_stmt|;
name|panel
operator|.
name|removeListSelectionListener
argument_list|(
name|panelRemover
argument_list|)
expr_stmt|;
block|}
comment|/**      * Listener, which performs adding of new column at double-click      */
specifier|protected
class|class
name|PanelOpener
extends|extends
name|MouseAdapter
block|{
comment|/**          * Invoked when the mouse has been clicked on a component.          */
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
name|SwingUtilities
operator|.
name|isLeftMouseButton
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|process
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|process
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|BrowserPanel
name|panel
init|=
operator|(
name|BrowserPanel
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Object
name|selectedNode
init|=
name|panel
operator|.
name|getSelectedValue
argument_list|()
decl_stmt|;
comment|// ignore unselected
if|if
condition|(
name|selectedNode
operator|!=
literal|null
condition|)
block|{
name|updateFromModel
argument_list|(
name|selectedNode
argument_list|,
name|columns
operator|.
name|indexOf
argument_list|(
name|panel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Listener, which performs removing columns to the right of selected row      */
specifier|protected
class|class
name|PanelRemover
implements|implements
name|ListSelectionListener
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|ListSelectionEvent
name|e
parameter_list|)
block|{
comment|// ignore "adjusting"
if|if
condition|(
operator|!
name|e
operator|.
name|getValueIsAdjusting
argument_list|()
condition|)
block|{
name|BrowserPanel
name|panel
init|=
operator|(
name|BrowserPanel
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Object
name|selectedNode
init|=
name|panel
operator|.
name|getSelectedValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedNode
operator|!=
literal|null
condition|)
block|{
name|updateFromModel
argument_list|(
name|selectedNode
argument_list|,
name|columns
operator|.
name|indexOf
argument_list|(
name|panel
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

