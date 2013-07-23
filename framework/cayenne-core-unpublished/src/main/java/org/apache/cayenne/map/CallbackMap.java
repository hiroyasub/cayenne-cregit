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
comment|/**  * A generic descriptor of a set of standard lifecycle callbacks.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CallbackMap
implements|implements
name|Serializable
block|{
specifier|protected
name|CallbackDescriptor
index|[]
name|callbacks
init|=
operator|new
name|CallbackDescriptor
index|[
name|LifecycleEvent
operator|.
name|values
argument_list|()
operator|.
name|length
index|]
decl_stmt|;
specifier|public
name|CallbackMap
parameter_list|()
block|{
name|LifecycleEvent
index|[]
name|events
init|=
name|LifecycleEvent
operator|.
name|values
argument_list|()
decl_stmt|;
name|callbacks
operator|=
operator|new
name|CallbackDescriptor
index|[
name|events
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|events
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|callbacks
index|[
name|i
index|]
operator|=
operator|new
name|CallbackDescriptor
argument_list|(
name|events
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns all event callbacks as an array ordered by event type.      */
specifier|public
name|CallbackDescriptor
index|[]
name|getCallbacks
parameter_list|()
block|{
return|return
name|callbacks
return|;
block|}
comment|/**      * @param callbackType callback type id      * @return CallbackDescriptor for the specified callback type id      */
specifier|public
name|CallbackDescriptor
name|getCallbackDescriptor
parameter_list|(
name|LifecycleEvent
name|callbackType
parameter_list|)
block|{
return|return
name|callbacks
index|[
name|callbackType
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostLoad
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|POST_LOAD
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostPersist
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|POST_PERSIST
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostRemove
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|POST_REMOVE
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostUpdate
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|POST_UPDATE
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPostAdd
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|POST_ADD
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPrePersist
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|PRE_PERSIST
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPreRemove
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|PRE_REMOVE
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
specifier|public
name|CallbackDescriptor
name|getPreUpdate
parameter_list|()
block|{
return|return
name|callbacks
index|[
name|LifecycleEvent
operator|.
name|PRE_UPDATE
operator|.
name|ordinal
argument_list|()
index|]
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
name|getPostAdd
argument_list|()
argument_list|,
literal|"post-add"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPrePersist
argument_list|()
argument_list|,
literal|"pre-persist"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPostPersist
argument_list|()
argument_list|,
literal|"post-persist"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPreUpdate
argument_list|()
argument_list|,
literal|"pre-update"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPostUpdate
argument_list|()
argument_list|,
literal|"post-update"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPreRemove
argument_list|()
argument_list|,
literal|"pre-remove"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPostRemove
argument_list|()
argument_list|,
literal|"post-remove"
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|printMethods
argument_list|(
name|getPostLoad
argument_list|()
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
name|String
name|methodName
range|:
name|descriptor
operator|.
name|getCallbackMethods
argument_list|()
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
name|methodName
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
