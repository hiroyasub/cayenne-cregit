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
package|;
end_package

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
name|List
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
name|cache
operator|.
name|MapQueryCache
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
name|cache
operator|.
name|QueryCache
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
name|event
operator|.
name|EventManager
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
name|exp
operator|.
name|ValueInjector
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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|GraphDiff
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
name|graph
operator|.
name|GraphEvent
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
name|graph
operator|.
name|GraphManager
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
name|query
operator|.
name|ObjectIdQuery
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|RefreshQuery
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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|Property
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
import|;
end_import

begin_comment
comment|/**  * A common base superclass for Cayenne ObjectContext implementors.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseContext
implements|implements
name|ObjectContext
implements|,
name|DataChannel
block|{
comment|/**      * A holder of a ObjectContext bound to the current thread.      *       * @since 3.0      */
specifier|protected
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|ObjectContext
argument_list|>
name|threadObjectContext
init|=
operator|new
name|ThreadLocal
argument_list|<
name|ObjectContext
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Returns the ObjectContext bound to the current thread.      *       * @since 3.0      * @return the ObjectContext associated with caller thread.      * @throws IllegalStateException if there is no ObjectContext bound to the current      *             thread.      * @see org.apache.cayenne.conf.WebApplicationContextFilter      */
specifier|public
specifier|static
name|ObjectContext
name|getThreadObjectContext
parameter_list|()
throws|throws
name|IllegalStateException
block|{
name|ObjectContext
name|context
init|=
name|threadObjectContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current thread has no bound ObjectContext."
argument_list|)
throw|;
block|}
return|return
name|context
return|;
block|}
comment|/**      * Binds a ObjectContext to the current thread. ObjectContext can later be retrieved      * by users in the same thread by calling {@link BaseContext#getThreadObjectContext}.      * Using null parameter will unbind currently bound ObjectContext.      *       * @since 3.0      */
specifier|public
specifier|static
name|void
name|bindThreadObjectContext
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|threadObjectContext
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|// if we are to pass the context around, channel should be left alone and
comment|// reinjected later if needed
specifier|protected
specifier|transient
name|DataChannel
name|channel
decl_stmt|;
specifier|protected
name|QueryCache
name|queryCache
decl_stmt|;
comment|/**      * Stores user defined properties associated with this DataContext.      *       * @since 3.0      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|userProperties
decl_stmt|;
specifier|public
specifier|abstract
name|void
name|commitChanges
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|commitChangesToParent
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|?
argument_list|>
name|deletedObjects
parameter_list|()
function_decl|;
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
specifier|public
specifier|abstract
name|EntityResolver
name|getEntityResolver
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|GraphManager
name|getGraphManager
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Persistent
name|localObject
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|Object
name|prototype
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|?
argument_list|>
name|modifiedObjects
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|T
name|newObject
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|persistentClass
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|registerNewObject
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|?
argument_list|>
name|newObjects
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QueryResponse
name|performGenericQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|abstract
name|List
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
specifier|public
name|void
name|prepareForAccess
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|,
name|boolean
name|lazyFaulting
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
name|ObjectId
name|oid
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|performQuery
argument_list|(
operator|new
name|ObjectIdQuery
argument_list|(
name|oid
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|FaultFailureException
argument_list|(
literal|"Error resolving fault, no matching row exists in the database for ObjectId: "
operator|+
name|oid
argument_list|)
throw|;
block|}
if|else if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|FaultFailureException
argument_list|(
literal|"Error resolving fault, more than one row exists in the database for ObjectId: "
operator|+
name|oid
argument_list|)
throw|;
block|}
comment|// sanity check...
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|COMMITTED
condition|)
block|{
name|String
name|state
init|=
name|PersistenceState
operator|.
name|persistenceStateName
argument_list|(
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: andrus 4/13/2006, modified and deleted states are possible due to
comment|// a race condition, should we handle them here?
throw|throw
operator|new
name|FaultFailureException
argument_list|(
literal|"Error resolving fault for ObjectId: "
operator|+
name|oid
operator|+
literal|" and state ("
operator|+
name|state
operator|+
literal|"). Possible cause - matching row is missing from the database."
argument_list|)
throw|;
block|}
block|}
comment|// resolve relationship fault
if|if
condition|(
name|lazyFaulting
operator|&&
name|property
operator|!=
literal|null
condition|)
block|{
name|ClassDescriptor
name|classDescriptor
init|=
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|Property
name|propertyDescriptor
init|=
name|classDescriptor
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
comment|// If we don't have a property descriptor, there's not much we can do.
comment|// Let the caller know that the specified property could not be found and list
comment|// all of the properties that could be so the caller knows what can be used.
if|if
condition|(
name|propertyDescriptor
operator|==
literal|null
condition|)
block|{
specifier|final
name|StringBuilder
name|errorMessage
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|errorMessage
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Property '%s' is not declared for entity '%s'."
argument_list|,
name|property
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|errorMessage
operator|.
name|append
argument_list|(
literal|" Declared properties are: "
argument_list|)
expr_stmt|;
comment|// Grab each of the declared properties.
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|properties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|classDescriptor
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
specifier|final
name|AttributeProperty
name|property
parameter_list|)
block|{
name|properties
operator|.
name|add
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
specifier|final
name|ToOneProperty
name|property
parameter_list|)
block|{
name|properties
operator|.
name|add
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
specifier|final
name|ToManyProperty
name|property
parameter_list|)
block|{
name|properties
operator|.
name|add
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Now add the declared property names to the error message.
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|declaredProperty
range|:
name|properties
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|errorMessage
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"'%s'"
argument_list|,
name|declaredProperty
argument_list|)
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|errorMessage
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|", '%s'"
argument_list|,
name|declaredProperty
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|errorMessage
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|errorMessage
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
comment|// this should trigger fault resolving
name|propertyDescriptor
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|abstract
name|void
name|propertyChanged
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|rollbackChanges
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|rollbackChangesLocally
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|?
argument_list|>
name|uncommittedObjects
parameter_list|()
function_decl|;
comment|/**      * Returns {@link QueryCache}, creating it on the fly if needed.      */
specifier|public
name|QueryCache
name|getQueryCache
parameter_list|()
block|{
if|if
condition|(
name|queryCache
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|queryCache
operator|==
literal|null
condition|)
block|{
comment|// TODO: andrus, 7/27/2006 - figure out the factory stuff like we have
comment|// in DataContext
name|queryCache
operator|=
operator|new
name|MapQueryCache
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|queryCache
return|;
block|}
comment|/**      * Sets a QueryCache to be used for storing cached query results.      */
specifier|public
name|void
name|setQueryCache
parameter_list|(
name|QueryCache
name|queryCache
parameter_list|)
block|{
name|this
operator|.
name|queryCache
operator|=
name|queryCache
expr_stmt|;
block|}
comment|/**      * Returns EventManager associated with the ObjectStore.      *       * @since 1.2      */
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|channel
operator|!=
literal|null
condition|?
name|channel
operator|.
name|getEventManager
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
switch|switch
condition|(
name|syncType
condition|)
block|{
case|case
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
case|:
return|return
name|onContextRollback
argument_list|(
name|originatingContext
argument_list|)
return|;
case|case
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
case|:
return|return
name|onContextFlush
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
literal|false
argument_list|)
return|;
case|case
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
case|:
return|return
name|onContextFlush
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
literal|true
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unrecognized SyncMessage type: "
operator|+
name|syncType
argument_list|)
throw|;
block|}
block|}
name|GraphDiff
name|onContextRollback
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|)
block|{
name|rollbackChanges
argument_list|()
expr_stmt|;
return|return
operator|new
name|CompoundDiff
argument_list|()
return|;
block|}
specifier|protected
specifier|abstract
name|GraphDiff
name|onContextFlush
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|boolean
name|cascade
parameter_list|)
function_decl|;
comment|/**      * @since 1.2      */
specifier|protected
name|void
name|fireDataChannelCommitted
parameter_list|(
name|Object
name|postedBy
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|EventManager
name|manager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|GraphEvent
name|e
init|=
operator|new
name|GraphEvent
argument_list|(
name|this
argument_list|,
name|postedBy
argument_list|,
name|changes
argument_list|)
decl_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|e
argument_list|,
name|DataChannel
operator|.
name|GRAPH_FLUSHED_SUBJECT
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|protected
name|void
name|fireDataChannelRolledback
parameter_list|(
name|Object
name|postedBy
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|EventManager
name|manager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|GraphEvent
name|e
init|=
operator|new
name|GraphEvent
argument_list|(
name|this
argument_list|,
name|postedBy
argument_list|,
name|changes
argument_list|)
decl_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|e
argument_list|,
name|DataChannel
operator|.
name|GRAPH_ROLLEDBACK_SUBJECT
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|protected
name|void
name|fireDataChannelChanged
parameter_list|(
name|Object
name|postedBy
parameter_list|,
name|GraphDiff
name|changes
parameter_list|)
block|{
name|EventManager
name|manager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|GraphEvent
name|e
init|=
operator|new
name|GraphEvent
argument_list|(
name|this
argument_list|,
name|postedBy
argument_list|,
name|changes
argument_list|)
decl_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|e
argument_list|,
name|DataChannel
operator|.
name|GRAPH_CHANGED_SUBJECT
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|invalidateObjects
parameter_list|(
name|Collection
name|objects
parameter_list|)
block|{
name|performGenericQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|(
name|objects
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a map of user-defined properties associated with this DataContext.      *       * @since 3.0      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getUserProperties
parameter_list|()
block|{
comment|// as not all users will take advantage of properties, creating the
comment|// map on demand to keep DataContext lean...
if|if
condition|(
name|userProperties
operator|==
literal|null
condition|)
block|{
name|userProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|userProperties
return|;
block|}
comment|/**      * Returns a user-defined property previously set via 'setUserProperty'. Note that it      * is a caller responsibility to synchronize access to properties.      *       * @since 3.0      */
specifier|public
name|Object
name|getUserProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|getUserProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Sets a user-defined property. Note that it is a caller responsibility to      * synchronize access to properties.      *       * @since 3.0      */
specifier|public
name|void
name|setUserProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|getUserProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * If ObjEntity qualifier is set, asks it to inject initial value to an object.      * Also performs all Persistent initialization operations      */
specifier|protected
name|void
name|injectInitialValue
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
comment|// must follow this exact order of property initialization per CAY-653, i.e. have
comment|// the id and the context in place BEFORE setPersistence is called
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|obj
decl_stmt|;
name|object
operator|.
name|setObjectContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|GraphManager
name|graphManager
init|=
name|getGraphManager
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|graphManager
init|)
block|{
name|graphManager
operator|.
name|registerNode
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|graphManager
operator|.
name|nodeCreated
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ObjEntity
name|entity
decl_stmt|;
try|try
block|{
name|entity
operator|=
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|//ObjEntity cannot be fetched, ignored
name|entity
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
operator|instanceof
name|ValueInjector
condition|)
block|{
operator|(
operator|(
name|ValueInjector
operator|)
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
operator|)
operator|.
name|injectValue
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|// invoke callbacks
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|performCallbacks
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * Schedules an object for deletion on the next commit of this context. Object's      * persistence state is changed to PersistenceState.DELETED; objects related to this      * object are processed according to delete rules, i.e. relationships can be unset      * ("nullify" rule), deletion operation is cascaded (cascade rule).      *       * @param object a persistent object that we want to delete.      * @throws DeleteDenyException if a DENY delete rule is applicable for object      *             deletion.      * @throws NullPointerException if object is null.      */
specifier|public
name|void
name|deleteObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
operator|new
name|ObjectContextDeleteAction
argument_list|(
name|this
argument_list|)
operator|.
name|performDelete
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteObjects
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
if|if
condition|(
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
return|return;
comment|// Don't call deleteObject() directly since it would be less efficient.
name|ObjectContextDeleteAction
name|ocda
init|=
operator|new
name|ObjectContextDeleteAction
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// Make a copy to iterate over to avoid ConcurrentModificationException.
for|for
control|(
name|Persistent
name|object
range|:
operator|(
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
operator|)
operator|new
name|ArrayList
argument_list|(
name|objects
argument_list|)
control|)
name|ocda
operator|.
name|performDelete
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

