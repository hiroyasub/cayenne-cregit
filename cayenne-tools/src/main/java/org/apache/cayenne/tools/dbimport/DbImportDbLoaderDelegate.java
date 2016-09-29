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
name|tools
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
name|DefaultDbLoaderDelegate
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
name|ObjEntity
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
class|class
name|DbImportDbLoaderDelegate
extends|extends
name|DefaultDbLoaderDelegate
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|DbEntity
argument_list|>
name|addedDbEntities
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|DbEntity
argument_list|>
name|removedDbEntities
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|addedObjEntities
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|removedObjEntities
decl_stmt|;
name|DbImportDbLoaderDelegate
parameter_list|()
block|{
name|addedDbEntities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|removedDbEntities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|addedObjEntities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|removedObjEntities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|addDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|addedDbEntities
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeDbEntity
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|removedDbEntities
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|addObjEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|addedObjEntities
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeObjEntity
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|removedObjEntities
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|DbEntity
argument_list|>
name|getAddedDbEntities
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|addedDbEntities
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|DbEntity
argument_list|>
name|getRemovedDbEntities
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|removedDbEntities
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|getAddedObjEntities
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|addedObjEntities
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|getRemovedObjEntities
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|removedObjEntities
argument_list|)
return|;
block|}
block|}
end_class

end_unit

