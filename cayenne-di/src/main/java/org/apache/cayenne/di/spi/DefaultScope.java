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
operator|.
name|Entry
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
name|ConcurrentLinkedQueue
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|BeforeScopeEnd
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
comment|/**  * An implementation of a DI scopes with support scope events.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultScope
implements|implements
name|Scope
block|{
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
name|eventTypes
decl_stmt|;
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
specifier|private
specifier|static
specifier|final
name|String
name|SPECIAL_EVENT
init|=
name|AfterScopeEnd
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|SafeVarargs
specifier|public
name|DefaultScope
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|customEventTypes
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventTypes
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
comment|// initialize the event listener data structures in constructor to avoid
comment|// synchronization concerns on everything but per-event lists.
comment|// standard event types
name|eventTypes
operator|.
name|add
argument_list|(
name|BeforeScopeEnd
operator|.
name|class
argument_list|)
expr_stmt|;
name|eventTypes
operator|.
name|add
argument_list|(
name|AfterScopeEnd
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// custom event types
if|if
condition|(
name|customEventTypes
operator|!=
literal|null
condition|)
block|{
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
name|customEventTypes
control|)
block|{
name|eventTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
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
name|eventTypes
control|)
block|{
name|listeners
operator|.
name|put
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|ScopeEventBinding
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Shuts down this scope, posting {@link BeforeScopeEnd} and {@link AfterScopeEnd}      * events.      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|postScopeEvent
argument_list|(
name|BeforeScopeEnd
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// this will notify providers that they should reset their state and unregister
comment|// object event listeners that just went out of scope
name|postScopeEvent
argument_list|(
name|AfterScopeEnd
operator|.
name|class
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
comment|// TODO: cache metadata for non-singletons scopes for performance
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
name|eventTypes
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
name|eventListeners
init|=
name|listeners
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
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
specifier|public
name|void
name|removeScopeEventListener
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
comment|// TODO: 2 level-deep full scan will not be very efficient for short scopes. Right
comment|// now this would only affect the unit test scope, but if we start creating the
comment|// likes of HTTP request scope, we may need to create a faster listener
comment|// removal algorithm.
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|ScopeEventBinding
argument_list|>
argument_list|>
name|entry
range|:
name|listeners
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|SPECIAL_EVENT
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
comment|// no scanning and removal of Scope providers ...
comment|// for faster scan skip those
continue|continue;
block|}
name|Iterator
argument_list|<
name|ScopeEventBinding
argument_list|>
name|it
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ScopeEventBinding
name|binding
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|binding
operator|.
name|getObject
argument_list|()
operator|==
name|object
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Posts a scope event to all registered listeners. There's no predetermined order of      * event dispatching. An exception thrown by any of the listeners stops further event      * processing and is rethrown.      */
specifier|public
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
name|Iterator
argument_list|<
name|ScopeEventBinding
argument_list|>
name|it
init|=
name|eventListeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ScopeEventBinding
name|listener
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|listener
operator|.
name|onScopeEvent
argument_list|(
name|eventParameters
argument_list|)
condition|)
block|{
comment|// remove listeners that were garbage collected
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
return|return
operator|new
name|DefaultScopeProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
name|unscoped
argument_list|)
return|;
block|}
block|}
end_class

end_unit

