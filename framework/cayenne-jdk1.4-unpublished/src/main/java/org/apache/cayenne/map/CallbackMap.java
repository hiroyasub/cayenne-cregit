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
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * A generic descriptor of a set of standard lifecycle callbacks.  *   * @author Andrus Adamchik  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CallbackMap
implements|implements
name|Serializable
block|{
comment|/**      * An array containing all valid callbacks with each callback int value corresponding      * to its index in the array.      */
specifier|public
specifier|static
specifier|final
name|int
index|[]
name|CALLBACKS
init|=
operator|new
name|int
index|[]
block|{
name|LifecycleEventCallback
operator|.
name|PRE_PERSIST
block|,
name|LifecycleEventCallback
operator|.
name|PRE_REMOVE
block|,
name|LifecycleEventCallback
operator|.
name|PRE_UPDATE
block|,
name|LifecycleEventCallback
operator|.
name|POST_PERSIST
block|,
name|LifecycleEventCallback
operator|.
name|POST_REMOVE
block|,
name|LifecycleEventCallback
operator|.
name|POST_UPDATE
block|,
name|LifecycleEventCallback
operator|.
name|POST_LOAD
block|}
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|prePersist
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|postPersist
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|preUpdate
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|postUpdate
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|preRemove
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|postRemove
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|postLoad
decl_stmt|;
specifier|public
name|CallbackMap
parameter_list|()
block|{
name|this
operator|.
name|prePersist
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|PRE_PERSIST
argument_list|)
expr_stmt|;
name|this
operator|.
name|postPersist
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|POST_PERSIST
argument_list|)
expr_stmt|;
name|this
operator|.
name|preUpdate
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|PRE_UPDATE
argument_list|)
expr_stmt|;
name|this
operator|.
name|postUpdate
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|POST_UPDATE
argument_list|)
expr_stmt|;
name|this
operator|.
name|preRemove
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|PRE_REMOVE
argument_list|)
expr_stmt|;
name|this
operator|.
name|postRemove
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|POST_REMOVE
argument_list|)
expr_stmt|;
name|this
operator|.
name|postLoad
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|LifecycleEventCallback
operator|.
name|POST_LOAD
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns all event callbacks in a single array ordered by event type, following the      * order in {@link CallbackMap#CALLBACKS} array.      */
specifier|public
name|CallbackDescriptor
index|[]
name|getCallbacks
parameter_list|()
block|{
return|return
operator|new
name|CallbackDescriptor
index|[]
block|{
name|prePersist
block|,
name|preRemove
block|,
name|preUpdate
block|,
name|postPersist
block|,
name|postRemove
block|,
name|postUpdate
block|,
name|postLoad
block|}
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostLoad
parameter_list|()
block|{
return|return
name|postLoad
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostPersist
parameter_list|()
block|{
return|return
name|postPersist
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostRemove
parameter_list|()
block|{
return|return
name|postRemove
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostUpdate
parameter_list|()
block|{
return|return
name|postUpdate
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPrePersist
parameter_list|()
block|{
return|return
name|prePersist
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPreRemove
parameter_list|()
block|{
return|return
name|preRemove
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPreUpdate
parameter_list|()
block|{
return|return
name|preUpdate
return|;
block|}
block|}
end_class

end_unit

