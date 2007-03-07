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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PostLoad
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PostPersist
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PostRemove
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PostUpdate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PrePersist
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PreRemove
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PreUpdate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Transient
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|CallbackEntity
block|{
annotation|@
name|Id
specifier|protected
name|int
name|id
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|prePersistCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|postPersistCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|int
name|postPersistedId
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|preRemoveCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|postRemoveCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|preUpdateCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|postUpdateCalled
decl_stmt|;
annotation|@
name|Transient
specifier|protected
specifier|transient
name|boolean
name|postLoadCalled
decl_stmt|;
annotation|@
name|PrePersist
specifier|public
name|void
name|prePersistMethod
parameter_list|()
block|{
name|prePersistCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PostPersist
specifier|public
name|void
name|postPersistMethod
parameter_list|()
block|{
name|postPersistCalled
operator|=
literal|true
expr_stmt|;
name|postPersistedId
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|PreRemove
specifier|public
name|void
name|preRemoveMethod
parameter_list|()
block|{
name|preRemoveCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PostRemove
specifier|public
name|void
name|postRemoveMethod
parameter_list|()
block|{
name|postRemoveCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PreUpdate
specifier|public
name|void
name|preUpdateMethod
parameter_list|()
block|{
name|preUpdateCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PostUpdate
specifier|public
name|void
name|postUpdateMethod
parameter_list|()
block|{
name|postUpdateCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PostLoad
specifier|public
name|void
name|postLoadMethod
parameter_list|()
block|{
name|postLoadCalled
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|boolean
name|isPostLoadCalled
parameter_list|()
block|{
return|return
name|postLoadCalled
return|;
block|}
specifier|public
name|boolean
name|isPostPersistCalled
parameter_list|()
block|{
return|return
name|postPersistCalled
return|;
block|}
specifier|public
name|int
name|getPostPersistedId
parameter_list|()
block|{
return|return
name|postPersistedId
return|;
block|}
specifier|public
name|boolean
name|isPostRemoveCalled
parameter_list|()
block|{
return|return
name|postRemoveCalled
return|;
block|}
specifier|public
name|boolean
name|isPostUpdateCalled
parameter_list|()
block|{
return|return
name|postUpdateCalled
return|;
block|}
specifier|public
name|boolean
name|isPrePersistCalled
parameter_list|()
block|{
return|return
name|prePersistCalled
return|;
block|}
specifier|public
name|boolean
name|isPreRemoveCalled
parameter_list|()
block|{
return|return
name|preRemoveCalled
return|;
block|}
specifier|public
name|boolean
name|isPreUpdateCalled
parameter_list|()
block|{
return|return
name|preUpdateCalled
return|;
block|}
block|}
end_class

end_unit

