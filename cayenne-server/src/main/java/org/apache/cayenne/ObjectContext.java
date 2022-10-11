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
package|;
end_package

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
name|Select
import|;
end_import

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
name|Collection
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

begin_comment
comment|/**  * A Cayenne object facade to a persistent store. Instances of ObjectContext are  * used in the application code to access Cayenne persistence features.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ObjectContext
extends|extends
name|DataChannel
extends|,
name|Serializable
block|{
comment|/**      * Returns EntityResolver that stores all mapping information accessible by      * this ObjectContext.      */
name|EntityResolver
name|getEntityResolver
parameter_list|()
function_decl|;
comment|/**      * Returns a collection of objects that are registered with this      * ObjectContext and have a state PersistenceState.NEW      */
name|Collection
argument_list|<
name|?
argument_list|>
name|newObjects
parameter_list|()
function_decl|;
comment|/**      * Returns a collection of objects that are registered with this      * ObjectContext and have a state PersistenceState.DELETED      */
name|Collection
argument_list|<
name|?
argument_list|>
name|deletedObjects
parameter_list|()
function_decl|;
comment|/**      * Returns a collection of objects that are registered with this      * ObjectContext and have a state PersistenceState.MODIFIED      */
name|Collection
argument_list|<
name|?
argument_list|>
name|modifiedObjects
parameter_list|()
function_decl|;
comment|/**      * Returns a collection of MODIFIED, DELETED or NEW objects.      */
name|Collection
argument_list|<
name|?
argument_list|>
name|uncommittedObjects
parameter_list|()
function_decl|;
comment|/**      * Returns a local copy of 'objectFromAnotherContext' object. "Local" means      * that the returned object is registered in this context. If the local      * object hasn't been previously cached in this context, a hollow object is      * created and returned to the caller. No DB query is performed to resolve      * an object.      *<p>      * Note that passing an object with a non-existing id, may later result in      * FaultFailureException on attempt to read returned object properties.      *       * @since 3.1      */
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|T
name|localObject
parameter_list|(
name|T
name|objectFromAnotherContext
parameter_list|)
function_decl|;
comment|/**      * Creates a new persistent object of a given class scheduled to be inserted      * to the database on next commit.      */
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
comment|/**      * Registers a transient object with the context. The difference with      * {@link #newObject(Class)} is that a user creates an object herself,      * before attaching it to the context, instead of relying on Cayenne to do      * that.      *       * @param object      *            new object that needs to be made persistent.      * @since 3.0      */
name|void
name|registerNewObject
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
comment|/** 	 * Schedules deletion of a persistent object. 	 *  	 * @throws DeleteDenyException 	 *             if a {@link org.apache.cayenne.map.DeleteRule#DENY} delete 	 *             rule is applicable for object deletion. 	 */
name|void
name|deleteObject
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
function_decl|;
comment|/**      * Schedules deletion of a collection of persistent objects.      *       * @throws DeleteDenyException      *             if a {@link org.apache.cayenne.map.DeleteRule#DENY} delete      *             rule is applicable for object deletion.      */
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
function_decl|;
comment|/**      * Schedules deletion of one or more persistent objects. Same as      * {@link #deleteObjects(Collection)} only with a vararg argument list for      * easier deletion of individual objects.      *       * @throws DeleteDenyException      *             if a {@link org.apache.cayenne.map.DeleteRule#DENY} delete      *             rule is applicable for object deletion.      * @since 3.1      */
parameter_list|<
name|T
parameter_list|>
name|void
name|deleteObjects
parameter_list|(
name|T
modifier|...
name|objects
parameter_list|)
throws|throws
name|DeleteDenyException
function_decl|;
comment|/**      * A callback method that child Persistent objects are expected to call      * before accessing property values. This callback allows ObjectContext to      * "inflate" unresolved objects on demand and also resolve properties that      * rely on lazy faulting.      *       * @since 3.0      */
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
function_decl|;
comment|/**      * A callback method that child Persistent objects are expected to call from      * inside the setter after modifying a value of a persistent property,      * including "simple" and "arc" properties.      */
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
comment|/**      * Flushes all changes to objects in this context to the parent DataChannel,      * cascading flush operation all the way through the stack, ultimately      * saving data in the database.      */
name|void
name|commitChanges
parameter_list|()
function_decl|;
comment|/**      * Flushes all changes to objects in this context to the parent DataChannel.      * Same as {@link #commitChanges()}, but no cascading flush occurs.      */
name|void
name|commitChangesToParent
parameter_list|()
function_decl|;
comment|/**      * Resets all uncommitted changes made to the objects in this ObjectContext,      * cascading rollback operation all the way through the stack.      */
name|void
name|rollbackChanges
parameter_list|()
function_decl|;
comment|/**      * Resets all uncommitted changes made to the objects in this ObjectContext.      * Same as {@link #rollbackChanges()}, but rollback is local to this      * context and no cascading changes undoing occurs.      */
name|void
name|rollbackChangesLocally
parameter_list|()
function_decl|;
comment|/**      * Executes a selecting query, returning a list of persistent objects or      * data rows.      */
name|List
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Executes a selecting query, returning a list of persistent objects or      * data rows.      *       * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Executes a selecting query, returning either NULL if query matched no      * objects, or a single object. If query matches more than one object,      * {@link CayenneRuntimeException} is thrown.      *       * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|T
name|selectOne
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Selects a single object using provided query. The query itself can      * match any number of objects, but will return only the first one. It      * returns null if no objects were matched.      *<p>      * If it matched more than one object, the first object from the list is      * returned. This makes 'selectFirst' different from      * {@link #selectOne(Select)}, which would throw in this situation.      * 'selectFirst' is useful e.g. when the query is ordered and we only want      * to see the first object (e.g. "most recent news article"), etc.      *<p>      * Selecting the first object via "Select.selectFirst(ObjectContext)"      * is more comprehensible than selecting via "ObjectContext.selectFirst(Select)",      * because implementations of "Select" set fetch size limit to one.      *      * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|T
name|selectFirst
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates a ResultIterator based on the provided query and passes it to a      * callback for processing. The caller does not need to worry about closing      * the iterator. This method takes care of it.      *       * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|void
name|iterate
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|,
name|ResultIteratorCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/**      * Creates a ResultIterator based on the provided query. It is usually      * backed by an open result set and is useful for processing of large data      * sets, preserving a constant memory footprint. The caller must wrap      * iteration in try/finally (or try-with-resources for Java 1.7 and higher) and      * close the ResultIterator explicitly.      * Or use {@link #iterate(Select, ResultIteratorCallback)} as an alternative.      *       * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates a ResultBatchIterator based on the provided query and batch size. It is usually      * backed by an open result set and is useful for processing of large data      * sets, preserving a constant memory footprint. The caller must wrap      * iteration in try/finally (or try-with-resources for Java 1.7 and higher) and      * close the ResultBatchIterator explicitly.      *      * @since 4.0      */
parameter_list|<
name|T
parameter_list|>
name|ResultBatchIterator
argument_list|<
name|T
argument_list|>
name|batchIterator
parameter_list|(
name|Select
argument_list|<
name|T
argument_list|>
name|query
parameter_list|,
name|int
name|size
parameter_list|)
function_decl|;
comment|/**      * Executes any kind of query providing the result in a form of      * QueryResponse.      */
name|QueryResponse
name|performGenericQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Returns GraphManager that manages object graph associated with this      * context.      */
name|GraphManager
name|getGraphManager
parameter_list|()
function_decl|;
comment|/**      * Returns an DataChannel used by this context.      */
name|DataChannel
name|getChannel
parameter_list|()
function_decl|;
comment|/**      * Returns<code>true</code> if there are any modified, deleted or new      * objects registered with this ObjectContext,<code>false</code> otherwise.      *       * @since 3.0      */
name|boolean
name|hasChanges
parameter_list|()
function_decl|;
comment|/**      * Invalidates a Collection of persistent objects. This operation only      * applies to the objects already committed to the database and does nothing      * to the NEW objects. It would remove each object's snapshot from caches      * and change object's state to HOLLOW. On the next access to this object,      * the object will be refetched.      */
name|void
name|invalidateObjects
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
function_decl|;
comment|/**      * Invalidates one or more persistent objects. Same as      * {@link #invalidateObjects(Collection)} only with a vararg argument list      * for easier invalidation of individual objects. If no arguments are passed      * to this method, it does nothing.      *       * @since 3.1      */
parameter_list|<
name|T
parameter_list|>
name|void
name|invalidateObjects
parameter_list|(
name|T
modifier|...
name|objects
parameter_list|)
function_decl|;
comment|/**      * Returns a user-defined property previously set via 'setUserProperty'.      * Concurrent access to properties does not require any special      * synchronization      *       * @since 3.0      */
name|Object
name|getUserProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Sets a user-defined property. Concurrent access to properties does not      * require any special synchronization      *       * @since 3.0      */
name|void
name|setUserProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Removes a user-defined property.      *      * @since 4.3      */
name|void
name|removeUserProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Removes all user-defined properties.      *      * @since 4.3      */
name|void
name|clearUserProperties
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

