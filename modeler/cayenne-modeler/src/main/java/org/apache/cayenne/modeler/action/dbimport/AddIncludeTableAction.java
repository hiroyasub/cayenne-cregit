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
name|IncludeTable
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
name|AddIncludeTableAction
extends|extends
name|TreeManipulationAction
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_NAME
init|=
literal|"Add Include Table"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ICON_NAME
init|=
literal|"icon-dbi-includeTable.png"
decl_stmt|;
specifier|public
name|AddIncludeTableAction
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
name|insertableNodeClass
operator|=
name|IncludeTable
operator|.
name|class
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
name|ReverseEngineering
name|reverseEngineeringOldCopy
init|=
name|prepareElements
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseEngineeringIsEmpty
argument_list|()
condition|)
block|{
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|removeAllChildren
argument_list|()
expr_stmt|;
block|}
name|IncludeTable
name|newTable
init|=
operator|new
name|IncludeTable
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|canBeInserted
argument_list|(
name|selectedElement
argument_list|)
condition|)
block|{
operator|(
operator|(
name|FilterContainer
operator|)
name|selectedElement
operator|.
name|getUserObject
argument_list|()
operator|)
operator|.
name|addIncludeTable
argument_list|(
name|newTable
argument_list|)
expr_stmt|;
name|selectedElement
operator|.
name|add
argument_list|(
operator|new
name|DbImportTreeNode
argument_list|(
name|newTable
argument_list|)
argument_list|)
expr_stmt|;
name|updateSelected
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|parentElement
operator|==
literal|null
condition|)
block|{
name|parentElement
operator|=
name|tree
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
block|}
operator|(
operator|(
name|FilterContainer
operator|)
name|parentElement
operator|.
name|getUserObject
argument_list|()
operator|)
operator|.
name|addIncludeTable
argument_list|(
name|newTable
argument_list|)
expr_stmt|;
name|parentElement
operator|.
name|add
argument_list|(
operator|new
name|DbImportTreeNode
argument_list|(
name|newTable
argument_list|)
argument_list|)
expr_stmt|;
name|updateSelected
operator|=
literal|false
expr_stmt|;
block|}
name|completeInserting
argument_list|(
name|reverseEngineeringOldCopy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

