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
name|action
operator|.
name|dbimport
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|FilterContainer
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|PatternParam
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|EditNodeAction
extends|extends
name|TreeManipulationAction
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Rename"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ICON_NAME
init|=
literal|"icon-edit.png"
decl_stmt|;
specifier|private
name|String
name|actionName
decl_stmt|;
specifier|public
name|EditNodeAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
name|ICON_NAME
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|tree
operator|.
name|isEditing
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|tree
operator|.
name|getSelectionPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tree
operator|.
name|startEditingAtPath
argument_list|(
name|tree
operator|.
name|getSelectionPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|actionName
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|tree
operator|.
name|getSelectionPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|selectedElement
operator|=
name|tree
operator|.
name|getSelectedNode
argument_list|()
expr_stmt|;
name|parentElement
operator|=
operator|(
name|DbImportTreeNode
operator|)
name|selectedElement
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
name|parentElement
operator|!=
literal|null
condition|)
block|{
name|Object
name|selectedObject
init|=
name|selectedElement
operator|.
name|getUserObject
argument_list|()
decl_stmt|;
name|ReverseEngineering
name|reverseEngineeringOldCopy
init|=
operator|new
name|ReverseEngineering
argument_list|(
name|tree
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|actionName
argument_list|)
condition|)
block|{
if|if
condition|(
name|selectedObject
operator|instanceof
name|FilterContainer
condition|)
block|{
operator|(
operator|(
name|FilterContainer
operator|)
name|selectedObject
operator|)
operator|.
name|setName
argument_list|(
name|actionName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|selectedObject
operator|instanceof
name|PatternParam
condition|)
block|{
operator|(
operator|(
name|PatternParam
operator|)
name|selectedObject
operator|)
operator|.
name|setPattern
argument_list|(
name|actionName
argument_list|)
expr_stmt|;
block|}
name|updateModel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|selectedElement
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|actionName
operator|.
name|equals
argument_list|(
name|EMPTY_NAME
argument_list|)
condition|)
block|{
name|putReverseEngineeringToUndoManager
argument_list|(
name|reverseEngineeringOldCopy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|setActionName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|actionName
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

