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
name|di
operator|.
name|spi
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|CopyOnWriteArrayList
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
name|di
operator|.
name|Provider
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
name|di
operator|.
name|Scope
import|;
end_import

begin_comment
comment|/**  * A base superclass of DI scopes that send scope events.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|EventfulScope
implements|implements
name|Scope
block|{
specifier|protected
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
argument_list|>
name|listeners
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|eventAnnotations
decl_stmt|;
specifier|public
name|EventfulScope
parameter_list|()
block|{
name|this
operator|.
name|listeners
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventAnnotations
operator|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Adds an annotation type that marks handler methods for a specific event type.      * Subclasses of EventfulScope should use this method to configure supported      * annotation types. This method should be invoked before any calls to      * {@link #addScopeEventListener(Object)}. Often it is invoked form the scope      * constructor.      */
specifier|protected
name|void
name|addEventAnnotation
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
name|eventAnnotations
operator|.
name|add
argument_list|(
name|annotationType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers annotated methods of an arbitrary object for this scope lifecycle events.      */
specifier|public
name|void
name|addScopeEventListener
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
comment|// not caching metadata for now, as all services in SingletonScope are unique...
comment|// If we start using RequestScope or similar, may need to figure out per-class
comment|// metadata cache.
comment|// 'getMethods' grabs public method from the class and its superclasses...
for|for
control|(
name|Method
name|method
range|:
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
range|:
name|eventAnnotations
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|annotationType
argument_list|)
condition|)
block|{
name|String
name|typeName
init|=
name|annotationType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
name|newListeners
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|ScopeEventBinding
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
name|eventListeners
init|=
name|listeners
operator|.
name|putIfAbsent
argument_list|(
name|typeName
argument_list|,
name|newListeners
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventListeners
operator|==
literal|null
condition|)
block|{
name|eventListeners
operator|=
name|newListeners
expr_stmt|;
block|}
name|eventListeners
operator|.
name|add
argument_list|(
operator|new
name|ScopeEventBinding
argument_list|(
name|object
argument_list|,
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Posts a scope event to all registered listeners. There's no predetermined order of      * event dispatching. An exception thrown by any of the listeners stops further event      * processing and is rethrown.      */
specifier|protected
name|void
name|postScopeEvent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|type
parameter_list|,
name|Object
modifier|...
name|eventParameters
parameter_list|)
block|{
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
name|eventListeners
init|=
name|listeners
operator|.
name|get
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventListeners
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ScopeEventBinding
name|listener
range|:
name|eventListeners
control|)
block|{
name|listener
operator|.
name|onScopeEvent
argument_list|(
name|eventParameters
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|scope
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|unscoped
parameter_list|)
function_decl|;
block|}
end_class

end_unit

