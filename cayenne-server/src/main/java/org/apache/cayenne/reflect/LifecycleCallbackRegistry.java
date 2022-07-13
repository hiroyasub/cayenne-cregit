begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|CayenneRuntimeException
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
name|annotation
operator|.
name|PostAdd
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
name|annotation
operator|.
name|PostLoad
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
name|annotation
operator|.
name|PostPersist
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
name|annotation
operator|.
name|PostRemove
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
name|annotation
operator|.
name|PostUpdate
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
name|annotation
operator|.
name|PrePersist
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
name|annotation
operator|.
name|PreRemove
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
name|annotation
operator|.
name|PreUpdate
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A registry of lifecycle callbacks for all callback event types. Valid event  * types are defined in {@link LifecycleEvent} enum.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|LifecycleCallbackRegistry
block|{
specifier|private
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|private
name|LifecycleCallbackEventHandler
index|[]
name|eventCallbacks
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AnnotationReader
argument_list|>
name|annotationsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|entitiesByAnnotation
decl_stmt|;
comment|/** 	 * Creates an empty callback registry. 	 */
specifier|public
name|LifecycleCallbackRegistry
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|resolver
expr_stmt|;
comment|// initialize callbacks map in constructor to avoid synchronization issues downstream.
name|this
operator|.
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
argument_list|()
expr_stmt|;
block|}
comment|// other "static" lookup maps are initialized on-demand
name|this
operator|.
name|entitiesByAnnotation
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Removes all listeners for all event types. 	 */
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
comment|/** 	 * Removes listeners for a single event type. 	 */
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
comment|/** 	 * Returns true if there are no listeners for a specific event type. 	 */
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
comment|/** 	 * Registers a {@link LifecycleListener} for all events on all entities. 	 * Note that listeners are not required to implement 	 * {@link LifecycleListener} interface. Other methods in this class can be 	 * used to register arbitrary listeners. 	 */
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
name|POST_ADD
argument_list|,
name|listener
argument_list|,
literal|"postAdd"
argument_list|)
expr_stmt|;
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
comment|/** 	 * Registers a callback method to be invoked on a provided non-entity object 	 * when a lifecycle event occurs on any entity that does not suppress 	 * default callbacks. 	 */
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
comment|/** 	 * Registers a {@link LifecycleListener} for all events on all entities. 	 * Note that listeners are not required to implement 	 * {@link LifecycleListener} interface. Other methods in this class can be 	 * used to register arbitrary listeners. 	 */
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
name|POST_ADD
argument_list|,
name|entityClass
argument_list|,
name|listener
argument_list|,
literal|"postAdd"
argument_list|)
expr_stmt|;
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
comment|/** 	 * Registers callback method to be invoked on a provided non-entity object 	 * when a lifecycle event occurs for a specific entity. 	 */
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
comment|/** 	 * Registers a callback method to be invoked on an entity class instances 	 * when a lifecycle event occurs. 	 *  	 * @since 4.0 	 */
specifier|public
name|void
name|addCallback
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
comment|/** 	 * Registers a callback method to be invoked on an entity class instances 	 * when a lifecycle event occurs. 	 * 	 * @param type of the lifecycle event 	 * @param entityClass type of the entity 	 * @param method callback method reference 	 * 	 * @since 4.2 	 */
specifier|public
name|void
name|addCallback
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
name|Method
name|method
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
name|method
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Adds a listener, mapping its methods to events based on annotations. 	 *  	 * @since 3.1 	 */
specifier|public
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null listener"
argument_list|)
throw|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|listenerType
init|=
name|listener
operator|.
name|getClass
argument_list|()
decl_stmt|;
do|do
block|{
for|for
control|(
name|Method
name|m
range|:
name|listenerType
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|m
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
name|AnnotationReader
name|reader
init|=
name|getAnnotationsMap
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|types
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
init|=
name|reader
operator|.
name|entities
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
init|=
name|reader
operator|.
name|entityAnnotations
argument_list|(
name|a
argument_list|)
decl_stmt|;
comment|// TODO: ignoring entity subclasses?
comment|// whenever we add those, take into account "exlcudeSuperclassListeners" flag
name|Collections
operator|.
name|addAll
argument_list|(
name|types
argument_list|,
name|entities
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|type
range|:
name|entityAnnotations
control|)
block|{
name|types
operator|.
name|addAll
argument_list|(
name|getAnnotatedEntities
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|types
control|)
block|{
name|eventCallbacks
index|[
name|reader
operator|.
name|eventType
argument_list|()
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|addListener
argument_list|(
name|type
argument_list|,
name|listener
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
comment|// if no entities specified then adding global callback
if|if
condition|(
name|entities
operator|.
name|length
operator|==
literal|0
operator|&&
name|entityAnnotations
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|eventCallbacks
index|[
name|reader
operator|.
name|eventType
argument_list|()
operator|.
name|ordinal
argument_list|()
index|]
operator|.
name|addDefaultListener
argument_list|(
name|listener
argument_list|,
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|listenerType
operator|=
name|listenerType
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|listenerType
operator|!=
literal|null
operator|&&
operator|!
name|listenerType
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
do|;
block|}
comment|/** 	 * Invokes callbacks of a specific type for a given entity object. 	 */
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
comment|/** 	 * Invokes callbacks of a specific type for a collection of entity objects. 	 */
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
comment|// used by unit tests to poke inside the registry
name|LifecycleCallbackEventHandler
name|getHandler
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
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AnnotationReader
argument_list|>
name|getAnnotationsMap
parameter_list|()
block|{
if|if
condition|(
name|annotationsMap
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|AnnotationReader
argument_list|>
name|annotationsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PostAdd
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|POST_ADD
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostAdd
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostAdd
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PrePersist
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|PRE_PERSIST
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PrePersist
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PrePersist
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PreRemove
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|PRE_REMOVE
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PreRemove
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PreRemove
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PreUpdate
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|PRE_UPDATE
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PreUpdate
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PreUpdate
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PostLoad
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|POST_LOAD
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostLoad
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostLoad
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PostPersist
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|POST_PERSIST
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostPersist
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostPersist
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PostUpdate
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|POST_UPDATE
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostUpdate
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostUpdate
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|annotationsMap
operator|.
name|put
argument_list|(
name|PostRemove
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AnnotationReader
argument_list|()
block|{
annotation|@
name|Override
name|LifecycleEvent
name|eventType
parameter_list|()
block|{
return|return
name|LifecycleEvent
operator|.
name|POST_REMOVE
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostRemove
operator|)
name|a
operator|)
operator|.
name|entityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PostRemove
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|annotationsMap
operator|=
name|annotationsMap
expr_stmt|;
block|}
return|return
name|annotationsMap
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getAnnotatedEntities
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|)
block|{
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|entities
init|=
name|entitiesByAnnotation
operator|.
name|get
argument_list|(
name|annotationType
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entities
operator|==
literal|null
condition|)
block|{
comment|// ensure no dupes
name|entities
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entityResolver
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
init|=
name|entityResolver
operator|.
name|getObjectFactory
argument_list|()
operator|.
name|getJavaClass
argument_list|(
name|entity
operator|.
name|getJavaClassName
argument_list|()
argument_list|)
decl_stmt|;
comment|// ensure that we don't register the same callback for multiple
comment|// classes in the same hierarchy, so find the topmost type using
comment|// a given annotation and register it once
comment|// TODO: This ignores "excludeSuperclassListeners" setting,
comment|// which is not possible with annotations anyways
while|while
condition|(
name|entityType
operator|!=
literal|null
operator|&&
name|entityType
operator|.
name|isAnnotationPresent
argument_list|(
name|annotationType
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|superType
init|=
name|entityType
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superType
operator|==
literal|null
operator|||
operator|!
name|superType
operator|.
name|isAnnotationPresent
argument_list|(
name|annotationType
argument_list|)
condition|)
block|{
name|entities
operator|.
name|add
argument_list|(
name|entityType
argument_list|)
expr_stmt|;
break|break;
block|}
name|entityType
operator|=
name|superType
expr_stmt|;
block|}
block|}
name|entitiesByAnnotation
operator|.
name|put
argument_list|(
name|annotationType
operator|.
name|getName
argument_list|()
argument_list|,
name|entities
argument_list|)
expr_stmt|;
block|}
return|return
name|entities
return|;
block|}
specifier|abstract
class|class
name|AnnotationReader
block|{
specifier|abstract
name|LifecycleEvent
name|eventType
parameter_list|()
function_decl|;
specifier|abstract
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|entities
parameter_list|(
name|Annotation
name|a
parameter_list|)
function_decl|;
specifier|abstract
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|entityAnnotations
parameter_list|(
name|Annotation
name|a
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

