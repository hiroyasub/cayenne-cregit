begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|map
operator|.
name|DbAttribute
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
name|DbRelationship
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
name|ObjAttribute
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
name|map
operator|.
name|ObjRelationship
import|;
end_import

begin_comment
comment|/**  * A default noop implementation of {@link ModelMergeDelegate}.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultModelMergeDelegate
implements|implements
name|ModelMergeDelegate
block|{
annotation|@
name|Override
specifier|public
name|void
name|dbAttributeAdded
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbAttributeModified
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbAttributeRemoved
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbRelationshipRemoved
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|objAttributeAdded
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|objAttributeModified
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|objAttributeRemoved
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|objRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|objRelationshipRemoved
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

