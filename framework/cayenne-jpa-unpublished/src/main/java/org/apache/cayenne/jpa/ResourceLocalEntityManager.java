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
name|jpa
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FlushModeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|LockModeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PersistenceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
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
name|DataChannel
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
name|DataObjectUtils
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
name|ObjectContext
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
name|PersistenceState
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
name|query
operator|.
name|EJBQLQuery
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceLocalEntityManager
implements|implements
name|EntityManager
implements|,
name|CayenneEntityManager
block|{
specifier|protected
name|EntityTransaction
name|transaction
decl_stmt|;
specifier|protected
name|ResourceLocalEntityManagerFactory
name|factory
decl_stmt|;
specifier|protected
name|FlushModeType
name|flushMode
decl_stmt|;
specifier|protected
name|boolean
name|open
decl_stmt|;
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|ResourceLocalEntityManager
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ResourceLocalEntityManagerFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null entity manager factory"
argument_list|)
throw|;
block|}
name|this
operator|.
name|open
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
comment|/**      * Returns a DataChannel of the peer ObjectContext.      */
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|context
operator|.
name|getChannel
argument_list|()
return|;
block|}
comment|/**      * Returns parent EntityManagerFactory.      */
specifier|protected
name|ResourceLocalEntityManagerFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
comment|/**      * Close an application-managed EntityManager. After an EntityManager has been closed,      * all methods on the EntityManager instance will throw the IllegalStateException      * except for isOpen, which will return false. This method can only be called when the      * EntityManager is not associated with an active transaction.      *       * @throws IllegalStateException if the EntityManager is associated with an active      *             transaction or if the EntityManager is container-managed.      */
specifier|public
name|void
name|close
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|transaction
operator|!=
literal|null
operator|&&
name|transaction
operator|.
name|isActive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Active transaction in progress"
argument_list|)
throw|;
block|}
name|open
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|open
operator|&&
operator|(
name|factory
operator|==
literal|null
operator|||
name|factory
operator|.
name|isOpen
argument_list|()
operator|)
return|;
block|}
specifier|public
name|Object
name|getDelegate
parameter_list|()
block|{
return|return
name|factory
operator|.
name|getProvider
argument_list|()
return|;
block|}
comment|/**      * Make an instance managed and persistent.      *       * @param entity an object to be made persistent      * @throws IllegalArgumentException if not an entity.      */
specifier|public
name|void
name|persist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Merge the state of the given entity into the current persistence context. Cayenne:      * Is this like localObject(s)?      *       * @param entity      * @return the instance that the state was merged to      * @throws IllegalArgumentException if instance is not an entity or is a removed      *             entity      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|merge
parameter_list|(
name|T
name|entity
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|checkNotRemoved
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|entity
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|context
operator|.
name|localObject
argument_list|(
name|persistent
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|persistent
argument_list|)
return|;
block|}
comment|/**      * Remove the entity instance.      *       * @param entity      * @throws IllegalArgumentException if not an entity or if a detached entity.      */
specifier|public
name|void
name|remove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|checkAttached
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
operator|(
name|Persistent
operator|)
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Find by primary key.      *       * @param entityClass      * @param primaryKey      * @return the found entity instance or null if the entity does not exist      * @throws IllegalArgumentException if the first argument does not denote an entity      *             type or the second argument is not a valid type for that      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|find
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entityClass
parameter_list|,
name|Object
name|primaryKey
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|entityClass
argument_list|,
name|primaryKey
argument_list|)
return|;
block|}
comment|/**      * Get an instance, whose state may be lazily fetched. If the requested instance does      * not exist in the database, throws EntityNotFoundException when the instance state      * is first accessed. (The persistence provider runtime is permitted to throw the      * EntityNotFoundException when getReference is called.) The application should not      * expect that the instance state will be available upon detachment, unless it was      * accessed by the application while the entity manager was open.      *       * @param entityClass      * @param primaryKey      * @return the found entity instance      * @throws IllegalArgumentException if the first argument does not denote an entity      *             type or the second argument is not a valid type for that entityÕs      *             primary key      * @throws EntityNotFoundException if the entity state cannot be accessed      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getReference
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entityClass
parameter_list|,
name|Object
name|primaryKey
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// TODO: force refresh?
name|T
name|ref
init|=
name|find
argument_list|(
name|entityClass
argument_list|,
name|primaryKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|EntityNotFoundException
argument_list|(
literal|"Could not find "
operator|+
name|entityClass
operator|.
name|toString
argument_list|()
operator|+
literal|" with primary key value "
operator|+
name|primaryKey
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ref
return|;
block|}
comment|/**      * Synchronize the persistence context to the underlying database.      *       * @throws PersistenceException if the flush fails      */
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PersistenceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Set the flush mode that applies to all objects contained in the persistence      * context.      *       * @param flushMode      */
specifier|public
name|void
name|setFlushMode
parameter_list|(
name|FlushModeType
name|flushMode
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|this
operator|.
name|flushMode
operator|=
name|flushMode
expr_stmt|;
block|}
comment|/**      * Get the flush mode that applies to all objects contained in the persistence      * context.      *       * @return flushMode      */
specifier|public
name|FlushModeType
name|getFlushMode
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|flushMode
return|;
block|}
comment|/**      * Refresh the state of the instance from the database, overwriting changes made to      * the entity, if any.      *       * @param entity      * @throws IllegalArgumentException if not an entity or entity is not managed      * @throws EntityNotFoundException if the entity no longer exists in the database      */
specifier|public
name|void
name|refresh
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// TODO: Andrus, 2/10/2006 - implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
comment|/**      * Clear the persistence context, causing all managed entities to become detached.      * Changes made to entities that have not been flushed to the database will not be      * persisted.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// TODO: Andrus, 2/10/2006 - implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
comment|/**      * Check if the instance belongs to the current persistence context.      *       * @throws IllegalArgumentException if not an entity      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|Persistent
name|p
init|=
operator|(
name|Persistent
operator|)
name|entity
decl_stmt|;
return|return
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|==
name|p
return|;
block|}
comment|/**      * Create an instance of Query for executing an EJB QL statement.      *       * @param ejbqlString an EJB QL query string      * @return the new query instance      * @throws IllegalArgumentException if query string is not valid      */
specifier|public
name|Query
name|createQuery
parameter_list|(
name|String
name|ejbqlString
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|JpaQuery
name|query
init|=
operator|new
name|JpaQuery
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQuery
argument_list|(
operator|new
name|EJBQLQuery
argument_list|(
name|ejbqlString
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
comment|/**      * Create an instance of Query for executing a named query (in EJB QL or native SQL).      *       * @param name the name of a query defined in metadata      * @return the new query instance      * @throws IllegalArgumentException if a query has not been defined with the given      *             name      */
specifier|public
name|Query
name|createNamedQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaQuery
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Query
name|createNativeQuery
parameter_list|(
name|String
name|sqlString
parameter_list|,
name|Class
name|resultClass
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaNativeQuery
argument_list|(
name|context
argument_list|,
name|sqlString
argument_list|,
name|resultClass
argument_list|)
return|;
block|}
comment|/**      * Create an instance of Query for executing a native SQL statement, e.g., for update      * or delete.      *       * @param sqlString a native SQL query string      * @return the new query instance      */
specifier|public
name|Query
name|createNativeQuery
parameter_list|(
name|String
name|sqlString
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|new
name|JpaNativeQuery
argument_list|(
name|context
argument_list|,
name|sqlString
argument_list|,
name|factory
operator|.
name|getPersistenceUnitInfo
argument_list|()
operator|.
name|getPersistenceUnitName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Create an instance of Query for executing a native SQL query.      *       * @param sqlString a native SQL query string      * @param resultSetMapping the name of the result set mapping      * @return the new query instance      */
specifier|public
name|Query
name|createNativeQuery
parameter_list|(
name|String
name|sqlString
parameter_list|,
name|String
name|resultSetMapping
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// TODO: Andrus, 2/10/2006 - implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
comment|/**      * Indicates to the EntityManager that a JTA transaction is active. This method should      * be called on a JTA application managed EntityManager that was created outside the      * scope of the active transaction to associate it with the current JTA transaction.      *<p>      * This implementation throws a JpaProviderException, as it only supports      * resource-local operation.      *       * @throws JpaProviderException as this impementation only supports resource-local      *             operation.      */
specifier|public
name|void
name|joinTransaction
parameter_list|()
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"'joinTransaction' is called on a RESOURCE_LOCAL EntityManager"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|lock
parameter_list|(
name|Object
name|entity
parameter_list|,
name|LockModeType
name|lockMode
parameter_list|)
block|{
comment|// TODO: andrus, 8/15/2006 - noop
block|}
comment|/**      * Return the resource-level transaction object. The EntityTransaction instance may be      * used serially to begin and commit multiple transactions.      *       * @return EntityTransaction instance      */
specifier|public
name|EntityTransaction
name|getTransaction
parameter_list|()
block|{
comment|// note - allowed to be called on a closed
if|if
condition|(
name|transaction
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|transaction
operator|=
operator|new
name|JpaTransaction
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|transaction
return|;
block|}
comment|/**      * Checks if an entity is attached to the current EntityManager, throwing      * IllegalArgumentException if not.      */
specifier|protected
name|void
name|checkAttached
parameter_list|(
name|Object
name|entity
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|Persistent
name|p
init|=
operator|(
name|Persistent
operator|)
name|entity
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|TRANSIENT
operator|||
name|p
operator|.
name|getObjectContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"entity is detached: "
operator|+
name|entity
argument_list|)
throw|;
block|}
block|}
comment|/**      * Checks if an entity is not removed in the current EntityManager, throwing      * IllegalArgumentException if it is.      */
specifier|protected
name|void
name|checkNotRemoved
parameter_list|(
name|Object
name|entity
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|Persistent
name|p
init|=
operator|(
name|Persistent
operator|)
name|entity
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|DELETED
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"entity is removed: "
operator|+
name|entity
argument_list|)
throw|;
block|}
block|}
comment|/**      * Throws an exception if called on closed factory.      */
specifier|protected
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IllegalStateException
block|{
if|if
condition|(
operator|!
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An attempt to access closed EntityManagerFactory."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

