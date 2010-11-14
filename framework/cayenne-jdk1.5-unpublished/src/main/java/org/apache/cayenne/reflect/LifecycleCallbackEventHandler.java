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
name|ArrayList
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
name|HashMap
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * A runtime callback processor for a single kind of lifecycle events.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|LifecycleCallbackEventHandler
block|{
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|AbstractCallback
argument_list|>
argument_list|>
name|listeners
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|AbstractCallback
argument_list|>
name|defaultListeners
decl_stmt|;
name|LifecycleCallbackEventHandler
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|AbstractCallback
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultListeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|AbstractCallback
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|excludingDefaultListeners
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
return|return
name|entity
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|isExcludingDefaultListeners
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|excludingSuperclassListeners
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
return|return
name|entity
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|isExcludingSuperclassListeners
argument_list|()
return|;
block|}
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|listeners
operator|.
name|isEmpty
argument_list|()
operator|&&
name|defaultListeners
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Removes all listeners.      */
name|void
name|clear
parameter_list|()
block|{
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
name|defaultListeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Registers a callback method to be invoked on a provided non-entity object when a      * lifecycle event occurs on any entity that does not suppress default callbacks.      */
name|void
name|addDefaultListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|CallbackOnListener
name|callback
init|=
operator|new
name|CallbackOnListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|)
decl_stmt|;
name|addDefaultCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a callback object to be invoked when a lifecycle event occurs.      */
specifier|private
name|void
name|addDefaultCallback
parameter_list|(
name|AbstractCallback
name|callback
parameter_list|)
block|{
name|defaultListeners
operator|.
name|add
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a callback method to be invoked on an entity class instances when a      * lifecycle event occurs.      */
name|void
name|addListener
parameter_list|(
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
name|addCallback
argument_list|(
name|entityClass
argument_list|,
operator|new
name|CallbackOnEntity
argument_list|(
name|entityClass
argument_list|,
name|methodName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers callback method to be invoked on a provided non-entity object when a      * lifecycle event occurs.      */
name|void
name|addListener
parameter_list|(
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
name|CallbackOnListener
name|callback
init|=
operator|new
name|CallbackOnListener
argument_list|(
name|listener
argument_list|,
name|methodName
argument_list|,
name|entityClass
argument_list|)
decl_stmt|;
name|addCallback
argument_list|(
name|entityClass
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
name|void
name|addListener
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|,
name|Object
name|listener
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|CallbackOnListener
name|callback
init|=
operator|new
name|CallbackOnListener
argument_list|(
name|listener
argument_list|,
name|method
argument_list|,
name|entityClass
argument_list|)
decl_stmt|;
name|addCallback
argument_list|(
name|entityClass
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a callback object to be invoked when a lifecycle event occurs.      */
specifier|private
name|void
name|addCallback
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|,
name|AbstractCallback
name|callback
parameter_list|)
block|{
name|Collection
argument_list|<
name|AbstractCallback
argument_list|>
name|entityListeners
init|=
name|listeners
operator|.
name|get
argument_list|(
name|entityClass
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entityListeners
operator|==
literal|null
condition|)
block|{
name|entityListeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|AbstractCallback
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|listeners
operator|.
name|put
argument_list|(
name|entityClass
operator|.
name|getName
argument_list|()
argument_list|,
name|entityListeners
argument_list|)
expr_stmt|;
block|}
name|entityListeners
operator|.
name|add
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invokes callbacks for a given entity object.      */
name|void
name|performCallbacks
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
comment|// default listeners are invoked first
if|if
condition|(
operator|!
name|defaultListeners
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|excludingDefaultListeners
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
specifier|final
name|AbstractCallback
name|listener
range|:
name|defaultListeners
control|)
block|{
name|listener
operator|.
name|performCallback
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|// apply per-entity listeners
name|performCallbacks
argument_list|(
name|object
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invokes callbacks for a collection of entity objects.      */
name|void
name|performCallbacks
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
for|for
control|(
name|Object
name|object
range|:
name|objects
control|)
block|{
name|performCallbacks
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Invokes callbacks for the class hierarchy, starting from the most generic      * superclass.      */
specifier|private
name|void
name|performCallbacks
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callbackEntityClass
parameter_list|)
block|{
if|if
condition|(
name|callbackEntityClass
operator|==
literal|null
operator|||
name|Object
operator|.
name|class
operator|.
name|equals
argument_list|(
name|callbackEntityClass
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// recursively perform super callbacks first
if|if
condition|(
operator|!
name|excludingSuperclassListeners
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|performCallbacks
argument_list|(
name|object
argument_list|,
name|callbackEntityClass
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// perform callbacks on provided class
name|String
name|key
init|=
name|callbackEntityClass
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|AbstractCallback
argument_list|>
name|entityListeners
init|=
name|listeners
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|entityListeners
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|AbstractCallback
name|listener
range|:
name|entityListeners
control|)
block|{
name|listener
operator|.
name|performCallback
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

