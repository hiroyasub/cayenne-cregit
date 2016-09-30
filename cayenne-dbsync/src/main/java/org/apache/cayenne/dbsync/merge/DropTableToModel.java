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
name|dbsync
operator|.
name|merge
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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

begin_comment
comment|/**  * A {@link MergerToken} to remove a {@link DbEntity} from a {@link DataMap}. Any  * {@link ObjEntity} mapped to the {@link DbEntity} will also be removed.  *   */
end_comment

begin_class
specifier|public
class|class
name|DropTableToModel
extends|extends
name|AbstractToModelToken
operator|.
name|Entity
block|{
specifier|public
name|DropTableToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
literal|"Drop Table"
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createCreateTableToDb
argument_list|(
name|getEntity
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|getEntity
argument_list|()
operator|.
name|mappedObjEntities
argument_list|()
control|)
block|{
name|objEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeObjEntity
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getDelegate
argument_list|()
operator|.
name|objEntityRemoved
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeDbEntity
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getDelegate
argument_list|()
operator|.
name|dbEntityRemoved
argument_list|(
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

