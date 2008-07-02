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
name|util
operator|.
name|CayenneController
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
name|DeleteRuleUpdater
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
name|EntityMergeSupport
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
specifier|public
class|class
name|EntitySyncController
extends|extends
name|CayenneController
block|{
specifier|protected
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|protected
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|protected
name|EntitySyncDialog
name|view
decl_stmt|;
comment|/**      * Creates a controller for synchronizing all ObjEntities mapped to a given DbEntity.      */
specifier|public
name|EntitySyncController
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|DbEntity
name|dbEntity
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
block|}
comment|/**      * Creates a controller for synchronizing a single ObjEntity with its parent DbEntity.      */
specifier|public
name|EntitySyncController
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|this
argument_list|(
name|parent
argument_list|,
name|objEntity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|objEntity
operator|=
name|objEntity
expr_stmt|;
block|}
specifier|public
name|EntityMergeSupport
name|createMerger
parameter_list|()
block|{
name|Collection
name|entities
init|=
name|getObjEntities
argument_list|()
decl_stmt|;
if|if
condition|(
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|EntityMergeSupport
name|merger
init|=
operator|new
name|EntityMergeSupport
argument_list|(
name|dbEntity
operator|.
name|getDataMap
argument_list|()
argument_list|)
decl_stmt|;
name|merger
operator|.
name|addEntityMergeListener
argument_list|(
name|DeleteRuleUpdater
operator|.
name|getEntityMergeListener
argument_list|()
argument_list|)
expr_stmt|;
comment|// see if we need to remove meaningful attributes...
name|boolean
name|showDialog
init|=
literal|false
decl_stmt|;
name|Iterator
name|it
init|=
name|entities
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
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|merger
operator|.
name|getMeaningfulFKs
argument_list|(
name|entity
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|showDialog
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
return|return
operator|(
name|showDialog
operator|)
condition|?
name|configureMerger
argument_list|(
name|merger
argument_list|)
else|:
name|merger
return|;
block|}
comment|/**      * Displays a nerger config dialog, returning a merger configured by the user. Returns      * null if the dialog was canceled.      */
specifier|protected
name|EntityMergeSupport
name|configureMerger
parameter_list|(
specifier|final
name|EntityMergeSupport
name|merger
parameter_list|)
block|{
specifier|final
name|boolean
index|[]
name|cancel
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|view
operator|=
operator|new
name|EntitySyncDialog
argument_list|()
expr_stmt|;
name|view
operator|.
name|getUpdateButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|merger
operator|.
name|setRemoveMeaningfulFKs
argument_list|(
name|view
operator|.
name|getRemoveFKs
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCancelButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|cancel
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|view
operator|.
name|show
argument_list|()
expr_stmt|;
return|return
name|cancel
index|[
literal|0
index|]
condition|?
literal|null
else|:
name|merger
return|;
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|Collection
name|getObjEntities
parameter_list|()
block|{
return|return
operator|(
name|objEntity
operator|!=
literal|null
operator|)
condition|?
name|Collections
operator|.
name|singleton
argument_list|(
name|objEntity
argument_list|)
else|:
name|dbEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getMappedEntities
argument_list|(
name|dbEntity
argument_list|)
return|;
block|}
block|}
end_class

end_unit

