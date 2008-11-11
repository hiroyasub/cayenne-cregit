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
name|reflect
package|;
end_package

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
name|Persistent
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
name|EntityResolver
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
name|LifecycleEvent
import|;
end_import

begin_comment
comment|/**  * A registry of lifecycle callbacks for all callback event types. Valid event types are  * defined in {@link LifecycleEvent} enum.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|LifecycleCallbackRegistry
block|{
specifier|private
name|LifecycleCallbackEventHandler
index|[]
name|eventCallbacks
decl_stmt|;
comment|/**      * Creates an empty callback registry.      */
specifier|public
name|LifecycleCallbackRegistry
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|eventCallbacks
operator|=
operator|new
name|LifecycleCallbackEventHandler
index|[
name|LifecycleEvent
operator|.
name|values
argument_list|()
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
name|eventCallbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|eventCallbacks
index|[
name|i
index|]
operator|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Removes all listeners for all event types.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
for|for
control|(
name|LifecycleCallbackEventHandler
name|eventCallback
range|:
name|eventCallbacks
control|)
block|{
name|eventCallback
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Removes listeners for a single event type.      */
specifier|public
name|void
name|clear
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
index|]
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns true if there are no listeners for a specific event type.      */
specifier|public
name|boolean
name|isEmpty
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|)
block|{
return|return
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Registers a {@link LifecycleListener} for all events on all entities. Note that      * listeners are not required to implement {@link LifecycleListener} interface. Other      * methods in this class can be used to register arbitrary listeners.      */
specifier|public
name|void
name|addDefaultListener
parameter_list|(
name|LifecycleListener
name|listener
parameter_list|)
block|{
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|listener
argument_list|,
literal|"prePersist"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|,
name|listener
argument_list|,
literal|"postPersist"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_REMOVE
argument_list|,
name|listener
argument_list|,
literal|"preRemove"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|,
name|listener
argument_list|,
literal|"postRemove"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
name|listener
argument_list|,
literal|"preUpdate"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|,
name|listener
argument_list|,
literal|"postUpdate"
argument_list|)
expr_stmt|;
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|listener
argument_list|,
literal|"postLoad"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a callback method to be invoked on a provided non-entity object when a      * lifecycle event occurs on any entity that does not suppress default callbacks.      */
specifier|public
name|void
name|addDefaultListener
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|,
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|addDefaultListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a {@link LifecycleListener} for all events on all entities. Note that      * listeners are not required to implement {@link LifecycleListener} interface. Other      * methods in this class can be used to register arbitrary listeners.      */
specifier|public
name|void
name|addListener
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|,
name|LifecycleListener
name|listener
parameter_list|)
block|{
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"prePersist"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"postPersist"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_REMOVE
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"preRemove"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"postRemove"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"preUpdate"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"postUpdate"
argument_list|)
expr_stmt|;
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"postLoad"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers callback method to be invoked on a provided non-entity object when a      * lifecycle event occurs for a specific entity.      */
specifier|public
name|void
name|addListener
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|,
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|addListener
argument_list|(
name|entityClass
argument_list|,
name|listener
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a callback method to be invoked on an entity class instances when a      * lifecycle event occurs.      */
specifier|public
name|void
name|addListener
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|addListener
argument_list|(
name|entityClass
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invokes callbacks of a specific type for a given entity object.      */
specifier|public
name|void
name|performCallbacks
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|,
name|Persistent
name|object
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|performCallbacks
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invokes callbacks of a specific type for a collection of entity objects.      */
specifier|public
name|void
name|performCallbacks
parameter_list|(
name|LifecycleEvent
name|type
parameter_list|,
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
name|eventCallbacks
index|[
name|type
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|performCallbacks
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

