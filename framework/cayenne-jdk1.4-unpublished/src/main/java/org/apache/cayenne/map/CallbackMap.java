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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|LifecycleListener
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
name|XMLEncoder
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
comment|// these int constants correspond to indexes in array in LifecycleCallbackRegistry, so
comment|// they must start with 0 and increment by 1.
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
name|LifecycleListener
operator|.
name|PRE_PERSIST
block|,
name|LifecycleListener
operator|.
name|PRE_REMOVE
block|,
name|LifecycleListener
operator|.
name|PRE_UPDATE
block|,
name|LifecycleListener
operator|.
name|POST_PERSIST
block|,
name|LifecycleListener
operator|.
name|POST_REMOVE
block|,
name|LifecycleListener
operator|.
name|POST_UPDATE
block|,
name|LifecycleListener
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
comment|/**      * map for quick access to a callback descriptor by type      */
specifier|private
name|Map
name|callbacksMap
init|=
operator|new
name|HashMap
argument_list|()
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
name|LifecycleListener
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
name|LifecycleListener
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
name|LifecycleListener
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
name|LifecycleListener
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
name|LifecycleListener
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
name|LifecycleListener
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
name|LifecycleListener
operator|.
name|POST_LOAD
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|PRE_PERSIST
argument_list|)
argument_list|,
name|prePersist
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|POST_PERSIST
argument_list|)
argument_list|,
name|postPersist
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|PRE_UPDATE
argument_list|)
argument_list|,
name|preUpdate
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|POST_UPDATE
argument_list|)
argument_list|,
name|postUpdate
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|PRE_REMOVE
argument_list|)
argument_list|,
name|preRemove
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|POST_REMOVE
argument_list|)
argument_list|,
name|postRemove
argument_list|)
expr_stmt|;
name|callbacksMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|LifecycleListener
operator|.
name|POST_LOAD
argument_list|)
argument_list|,
name|postLoad
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return all event callbacks in a single array ordered by event type, following the      * order in {@link CallbackMap#CALLBACKS} array.      */
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
comment|/**      * @param callbackType callback type id      * @return CallbackDescriptor for the specified callback type id      */
specifier|public
name|CallbackDescriptor
name|getCallbackDescriptor
parameter_list|(
name|int
name|callbackType
parameter_list|)
block|{
return|return
operator|(
name|CallbackDescriptor
operator|)
name|callbacksMap
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
name|callbackType
argument_list|)
argument_list|)
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
specifier|public
name|void
name|encodeCallbacksAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|printMethods
argument_list|(
name|prePersist
argument_list|,
literal|"pre-persist"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|postPersist
argument_list|,
literal|"post-persist"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|preUpdate
argument_list|,
literal|"pre-update"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|postUpdate
argument_list|,
literal|"post-update"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|preRemove
argument_list|,
literal|"pre-remove"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|postRemove
argument_list|,
literal|"post-remove"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|postLoad
argument_list|,
literal|"post-load"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|printMethods
parameter_list|(
name|CallbackDescriptor
name|descriptor
parameter_list|,
name|String
name|stringCallbackName
parameter_list|,
name|XMLEncoder
name|encoder
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|descriptor
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|stringCallbackName
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|" method-name=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

