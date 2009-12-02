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
name|graph
operator|.
name|action
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
name|ActionEvent
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
name|action
operator|.
name|RemoveAction
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
name|ConfirmRemoveDialog
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
name|graph
operator|.
name|GraphBuilder
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
name|undo
operator|.
name|RemoveUndoableEdit
import|;
end_import

begin_comment
comment|/**  * Action for removing entities from the graph  */
end_comment

begin_class
specifier|public
class|class
name|RemoveEntityAction
extends|extends
name|RemoveAction
block|{
name|GraphBuilder
name|builder
decl_stmt|;
specifier|public
name|RemoveEntityAction
parameter_list|(
name|GraphBuilder
name|builder
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|,
name|boolean
name|allowAsking
parameter_list|)
block|{
name|ConfirmRemoveDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|(
name|allowAsking
argument_list|)
decl_stmt|;
name|Entity
name|entity
init|=
name|builder
operator|.
name|getSelectedEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"ObjEntity"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveUndoableEdit
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|ObjEntity
operator|)
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|removeObjEntity
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|ObjEntity
operator|)
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"DbEntity"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|RemoveUndoableEdit
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|DbEntity
operator|)
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|removeDbEntity
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|DbEntity
operator|)
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

