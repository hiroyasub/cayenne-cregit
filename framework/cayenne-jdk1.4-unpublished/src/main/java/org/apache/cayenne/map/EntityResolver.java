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
name|Iterator
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
name|ObjectId
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
name|ClassDescriptorMap
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
name|generic
operator|.
name|DataObjectDescriptorFactory
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
name|pojo
operator|.
name|EnhancedPojoDescriptorFactory
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
name|valueholder
operator|.
name|ValueHolderDescriptorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|collection
operator|.
name|CompositeCollection
import|;
end_import

begin_comment
comment|/**  * Represents a virtual shared namespace for zero or more DataMaps. Unlike DataMap,  * EntityResolver is intended to work as a runtime container of mapping. DataMaps can be  * added or removed dynamically at runtime.  *<p>  * EntityResolver is thread-safe.  *</p>  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EntityResolver
implements|implements
name|MappingNamespace
implements|,
name|Serializable
block|{
specifier|static
specifier|final
name|Object
name|DUPLICATE_MARKER
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|protected
name|boolean
name|indexedByClass
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|queryCache
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|embeddableCache
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|dbEntityCache
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|objEntityCache
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|procedureCache
decl_stmt|;
specifier|protected
name|List
name|maps
decl_stmt|;
specifier|protected
specifier|transient
name|Map
name|entityInheritanceCache
decl_stmt|;
specifier|protected
name|EntityResolver
name|clientEntityResolver
decl_stmt|;
comment|// must be transient, as resolver may get deserialized in another VM, and descriptor
comment|// recompilation will be desired.
specifier|protected
specifier|transient
name|ClassDescriptorMap
name|classDescriptorMap
decl_stmt|;
comment|// callbacks are not serializable
specifier|protected
specifier|transient
name|LifecycleEventCallbackMap
index|[]
name|lifecycleEventCallbacks
decl_stmt|;
comment|/**      * Creates new EntityResolver.      */
specifier|public
name|EntityResolver
parameter_list|()
block|{
name|this
operator|.
name|indexedByClass
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|maps
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|queryCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbEntityCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|objEntityCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|procedureCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|entityInheritanceCache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates new EntityResolver that indexes a collection of DataMaps.      */
specifier|public
name|EntityResolver
parameter_list|(
name|Collection
name|dataMaps
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|maps
operator|.
name|addAll
argument_list|(
name|dataMaps
argument_list|)
expr_stmt|;
comment|// Take a copy
name|this
operator|.
name|constructCache
argument_list|()
expr_stmt|;
block|}
specifier|synchronized
name|void
name|initCallbacks
parameter_list|()
block|{
if|if
condition|(
name|lifecycleEventCallbacks
operator|==
literal|null
condition|)
block|{
name|LifecycleEventCallbackMap
index|[]
name|lifecycleEventCallbacks
init|=
operator|new
name|LifecycleEventCallbackMap
index|[
literal|7
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lifecycleEventCallbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|lifecycleEventCallbacks
index|[
name|i
index|]
operator|=
operator|new
name|LifecycleEventCallbackMap
argument_list|()
block|{
specifier|protected
name|boolean
name|isExcludingDefaultListeners
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
return|return
name|excludingDefaultListeners
argument_list|(
name|objectClass
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isExcludingSuperclassListeners
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
return|return
name|excludingSuperclassListeners
argument_list|(
name|objectClass
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
name|this
operator|.
name|lifecycleEventCallbacks
operator|=
name|lifecycleEventCallbacks
expr_stmt|;
block|}
block|}
comment|/**      * @since 3.0      */
name|boolean
name|excludingDefaultListeners
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|lookupObjEntity
argument_list|(
name|objectClass
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
comment|/**      * @since 3.0      */
name|boolean
name|excludingSuperclassListeners
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|lookupObjEntity
argument_list|(
name|objectClass
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
comment|/**      * Returns a {@link LifecycleEventCallbackMap} for a given type of lifecycle events.      * Event types are defined as constants in LifecycleEventCallback interface. E.g.      * {@link LifecycleEventCallback#PRE_PERSIST}, etc.      *       * @since 3.0      */
specifier|public
name|LifecycleEventCallbackMap
name|getCallbacks
parameter_list|(
name|int
name|callbackType
parameter_list|)
block|{
if|if
condition|(
name|lifecycleEventCallbacks
operator|==
literal|null
condition|)
block|{
name|initCallbacks
argument_list|()
expr_stmt|;
block|}
return|return
name|lifecycleEventCallbacks
index|[
name|callbackType
index|]
return|;
block|}
comment|/**      * Returns ClientEntityResolver with mapping information that only includes entities      * available on CWS Client Tier.      *       * @since 1.2      */
specifier|public
name|EntityResolver
name|getClientEntityResolver
parameter_list|()
block|{
if|if
condition|(
name|clientEntityResolver
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
name|clientEntityResolver
operator|==
literal|null
condition|)
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|ClientEntityResolver
argument_list|()
decl_stmt|;
comment|// translate to client DataMaps
name|Iterator
name|it
init|=
name|getDataMaps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|DataMap
name|clientMap
init|=
name|map
operator|.
name|getClientDataMap
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientMap
operator|!=
literal|null
condition|)
block|{
name|resolver
operator|.
name|addDataMap
argument_list|(
name|clientMap
argument_list|)
expr_stmt|;
block|}
block|}
name|clientEntityResolver
operator|=
name|resolver
expr_stmt|;
block|}
block|}
block|}
return|return
name|clientEntityResolver
return|;
block|}
comment|/**      * Returns all DbEntities.      */
specifier|public
name|Collection
name|getDbEntities
parameter_list|()
block|{
name|CompositeCollection
name|c
init|=
operator|new
name|CompositeCollection
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|getDataMaps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|addComposited
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
name|Collection
name|getObjEntities
parameter_list|()
block|{
name|CompositeCollection
name|c
init|=
operator|new
name|CompositeCollection
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|getDataMaps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|addComposited
argument_list|(
name|map
operator|.
name|getObjEntities
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
name|Collection
name|getProcedures
parameter_list|()
block|{
name|CompositeCollection
name|c
init|=
operator|new
name|CompositeCollection
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|getDataMaps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|addComposited
argument_list|(
name|map
operator|.
name|getProcedures
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
name|Collection
name|getQueries
parameter_list|()
block|{
name|CompositeCollection
name|c
init|=
operator|new
name|CompositeCollection
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|getDataMaps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|addComposited
argument_list|(
name|map
operator|.
name|getQueries
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|_lookupDbEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|_lookupObjEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupProcedure
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Query
name|getQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupQuery
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|Embeddable
name|result
init|=
operator|(
name|Embeddable
operator|)
name|embeddableCache
operator|.
name|get
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// reconstruct cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|result
operator|=
operator|(
name|Embeddable
operator|)
name|embeddableCache
operator|.
name|get
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Returns ClassDescriptor for the ObjEntity matching the name. Returns null if no      * matching entity exists.      *       * @since 1.2      */
specifier|public
specifier|synchronized
name|ClassDescriptor
name|getClassDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
if|if
condition|(
name|entityName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null entityName"
argument_list|)
throw|;
block|}
return|return
name|getClassDescriptorMap
argument_list|()
operator|.
name|getDescriptor
argument_list|(
name|entityName
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|addDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
if|if
condition|(
operator|!
name|maps
operator|.
name|contains
argument_list|(
name|map
argument_list|)
condition|)
block|{
name|maps
operator|.
name|add
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|map
operator|.
name|setNamespace
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|clearCache
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Removes all entity mappings from the cache. Cache can be rebuilt either explicitly      * by calling<code>constructCache</code>, or on demand by calling any of the      *<code>lookup...</code> methods.      */
specifier|public
specifier|synchronized
name|void
name|clearCache
parameter_list|()
block|{
name|queryCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|dbEntityCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|objEntityCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|procedureCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|entityInheritanceCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|clientEntityResolver
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Creates caches of DbEntities by ObjEntity, DataObject class, and ObjEntity name      * using internal list of maps.      */
specifier|protected
specifier|synchronized
name|void
name|constructCache
parameter_list|()
block|{
name|clearCache
argument_list|()
expr_stmt|;
comment|// rebuild index
name|Iterator
name|mapIterator
init|=
name|maps
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|mapIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|mapIterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// index ObjEntities
name|Iterator
name|objEntities
init|=
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|objEntities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjEntity
name|oe
init|=
operator|(
name|ObjEntity
operator|)
name|objEntities
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// index by name
name|objEntityCache
operator|.
name|put
argument_list|(
name|oe
operator|.
name|getName
argument_list|()
argument_list|,
name|oe
argument_list|)
expr_stmt|;
comment|// index by class
if|if
condition|(
name|indexedByClass
condition|)
block|{
name|Class
name|entityClass
decl_stmt|;
try|try
block|{
name|entityClass
operator|=
name|oe
operator|.
name|getJavaClass
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// DataMaps can contain all kinds of garbage...
comment|// TODO (Andrus, 10/18/2005) it would be nice to log something
comment|// here, but since EntityResolver is used on the client, log4J is
comment|// a no-go...
continue|continue;
block|}
comment|// allow duplicates, but put a special marker indicating that this
comment|// entity can't be looked up by class
name|Object
name|existing
init|=
name|objEntityCache
operator|.
name|get
argument_list|(
name|entityClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|existing
operator|!=
name|DUPLICATE_MARKER
condition|)
block|{
name|objEntityCache
operator|.
name|put
argument_list|(
name|entityClass
argument_list|,
name|DUPLICATE_MARKER
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|objEntityCache
operator|.
name|put
argument_list|(
name|entityClass
argument_list|,
name|oe
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Andrus, 12/13/2005 - An invalid DbEntity name will cause
comment|// 'getDbEntity' to go into an
comment|// infinite loop as "getDbEntity" will try to resolve DbEntity via a
comment|// parent namespace (which will be this resolver).
if|if
condition|(
name|oe
operator|.
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|existingDB
init|=
name|dbEntityCache
operator|.
name|get
argument_list|(
name|entityClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingDB
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|existingDB
operator|!=
name|DUPLICATE_MARKER
condition|)
block|{
name|dbEntityCache
operator|.
name|put
argument_list|(
name|entityClass
argument_list|,
name|DUPLICATE_MARKER
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|dbEntityCache
operator|.
name|put
argument_list|(
name|entityClass
argument_list|,
name|oe
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// index ObjEntity inheritance
name|objEntities
operator|=
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|objEntities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjEntity
name|oe
init|=
operator|(
name|ObjEntity
operator|)
name|objEntities
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// build inheritance tree... include nodes that
comment|// have no children to avoid uneeded cache rebuilding on lookup...
name|EntityInheritanceTree
name|node
init|=
operator|(
name|EntityInheritanceTree
operator|)
name|entityInheritanceCache
operator|.
name|get
argument_list|(
name|oe
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|node
operator|=
operator|new
name|EntityInheritanceTree
argument_list|(
name|oe
argument_list|)
expr_stmt|;
name|entityInheritanceCache
operator|.
name|put
argument_list|(
name|oe
operator|.
name|getName
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|String
name|superOEName
init|=
name|oe
operator|.
name|getSuperEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|superOEName
operator|!=
literal|null
condition|)
block|{
name|EntityInheritanceTree
name|superNode
init|=
operator|(
name|EntityInheritanceTree
operator|)
name|entityInheritanceCache
operator|.
name|get
argument_list|(
name|superOEName
argument_list|)
decl_stmt|;
if|if
condition|(
name|superNode
operator|==
literal|null
condition|)
block|{
comment|// do direct entity lookup to avoid recursive cache rebuild
name|ObjEntity
name|superOE
init|=
operator|(
name|ObjEntity
operator|)
name|objEntityCache
operator|.
name|get
argument_list|(
name|superOEName
argument_list|)
decl_stmt|;
if|if
condition|(
name|superOE
operator|!=
literal|null
condition|)
block|{
name|superNode
operator|=
operator|new
name|EntityInheritanceTree
argument_list|(
name|superOE
argument_list|)
expr_stmt|;
name|entityInheritanceCache
operator|.
name|put
argument_list|(
name|superOEName
argument_list|,
name|superNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// bad mapping?
comment|// TODO (Andrus, 10/18/2005) it would be nice to log something
comment|// here, but since EntityResolver is used on the client, log4J
comment|// is a no-go...
continue|continue;
block|}
block|}
name|superNode
operator|.
name|addChildNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
comment|// index DbEntities
name|Iterator
name|dbEntities
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbEntities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|de
init|=
operator|(
name|DbEntity
operator|)
name|dbEntities
operator|.
name|next
argument_list|()
decl_stmt|;
name|dbEntityCache
operator|.
name|put
argument_list|(
name|de
operator|.
name|getName
argument_list|()
argument_list|,
name|de
argument_list|)
expr_stmt|;
block|}
comment|// index stored procedures
name|Iterator
name|procedures
init|=
name|map
operator|.
name|getProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|procedures
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Procedure
name|proc
init|=
operator|(
name|Procedure
operator|)
name|procedures
operator|.
name|next
argument_list|()
decl_stmt|;
name|procedureCache
operator|.
name|put
argument_list|(
name|proc
operator|.
name|getName
argument_list|()
argument_list|,
name|proc
argument_list|)
expr_stmt|;
block|}
comment|// index queries
name|Iterator
name|queries
init|=
name|map
operator|.
name|getQueries
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|queries
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Query
name|query
init|=
operator|(
name|Query
operator|)
name|queries
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|query
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|existingQuery
init|=
name|queryCache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingQuery
operator|!=
literal|null
operator|&&
name|query
operator|!=
name|existingQuery
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"More than one Query for name"
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Returns a DataMap matching the name.      */
specifier|public
specifier|synchronized
name|DataMap
name|getDataMap
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
if|if
condition|(
name|mapName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
name|it
init|=
name|maps
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
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapName
operator|.
name|equals
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|map
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setDataMaps
parameter_list|(
name|Collection
name|maps
parameter_list|)
block|{
name|this
operator|.
name|maps
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|maps
operator|.
name|addAll
argument_list|(
name|maps
argument_list|)
expr_stmt|;
name|clearCache
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an unmodifiable collection of DataMaps.      */
specifier|public
name|Collection
name|getDataMaps
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|maps
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the DbEntity that      * services the specified class      *       * @return the required DbEntity, or null if none matches the specifier      */
specifier|public
specifier|synchronized
name|DbEntity
name|lookupDbEntity
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
if|if
condition|(
operator|!
name|indexedByClass
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Class index is disabled."
argument_list|)
throw|;
block|}
return|return
name|this
operator|.
name|_lookupDbEntity
argument_list|(
name|aClass
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the DbEntity that      * services the specified data Object      *       * @return the required DbEntity, or null if none matches the specifier      */
specifier|public
specifier|synchronized
name|DbEntity
name|lookupDbEntity
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
return|return
name|this
operator|.
name|_lookupDbEntity
argument_list|(
name|dataObject
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns EntityInheritanceTree representing inheritance hierarchy that starts with a      * given ObjEntity as root, and includes all its subentities. If ObjEntity has no      * known subentities, null is returned.      */
specifier|public
name|EntityInheritanceTree
name|lookupInheritanceTree
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|EntityInheritanceTree
name|tree
init|=
operator|(
name|EntityInheritanceTree
operator|)
name|entityInheritanceCache
operator|.
name|get
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tree
operator|==
literal|null
condition|)
block|{
comment|// since we keep inheritance trees for all entities, null means
comment|// unknown entity...
comment|// rebuild cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|tree
operator|=
operator|(
name|EntityInheritanceTree
operator|)
name|entityInheritanceCache
operator|.
name|get
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// don't return "trivial" trees
return|return
operator|(
name|tree
operator|==
literal|null
operator|||
name|tree
operator|.
name|getChildrenCount
argument_list|()
operator|==
literal|0
operator|)
condition|?
literal|null
else|:
name|tree
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the ObjEntity that      * maps to the services the specified class      *       * @return the required ObjEntity or null if there is none that matches the specifier      */
specifier|public
specifier|synchronized
name|ObjEntity
name|lookupObjEntity
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
if|if
condition|(
operator|!
name|indexedByClass
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Class index is disabled."
argument_list|)
throw|;
block|}
return|return
name|this
operator|.
name|_lookupObjEntity
argument_list|(
name|aClass
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the ObjEntity that      * services the specified data Object      *       * @return the required ObjEntity, or null if none matches the specifier      */
specifier|public
specifier|synchronized
name|ObjEntity
name|lookupObjEntity
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|ObjectId
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|Persistent
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|object
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
name|Object
name|key
init|=
name|id
operator|!=
literal|null
condition|?
operator|(
name|Object
operator|)
name|id
operator|.
name|getEntityName
argument_list|()
else|:
name|object
operator|.
name|getClass
argument_list|()
decl_stmt|;
return|return
name|this
operator|.
name|_lookupObjEntity
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the ObjEntity that      * maps to the services the class with the given name      *       * @return the required ObjEntity or null if there is none that matches the specifier      */
specifier|public
specifier|synchronized
name|ObjEntity
name|lookupObjEntity
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
return|return
name|this
operator|.
name|_lookupObjEntity
argument_list|(
name|entityName
argument_list|)
return|;
block|}
specifier|public
name|Procedure
name|lookupProcedure
parameter_list|(
name|Query
name|q
parameter_list|)
block|{
return|return
name|q
operator|.
name|getMetaData
argument_list|(
name|this
argument_list|)
operator|.
name|getProcedure
argument_list|()
return|;
block|}
specifier|public
name|Procedure
name|lookupProcedure
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|Procedure
name|result
init|=
operator|(
name|Procedure
operator|)
name|procedureCache
operator|.
name|get
argument_list|(
name|procedureName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// reconstruct cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|result
operator|=
operator|(
name|Procedure
operator|)
name|procedureCache
operator|.
name|get
argument_list|(
name|procedureName
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Returns a named query or null if no query exists for a given name.      */
specifier|public
specifier|synchronized
name|Query
name|lookupQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Query
name|result
init|=
operator|(
name|Query
operator|)
name|queryCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// reconstruct cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|result
operator|=
operator|(
name|Query
operator|)
name|queryCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|removeDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
if|if
condition|(
name|maps
operator|.
name|remove
argument_list|(
name|map
argument_list|)
condition|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isIndexedByClass
parameter_list|()
block|{
return|return
name|indexedByClass
return|;
block|}
specifier|public
name|void
name|setIndexedByClass
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|indexedByClass
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * Internal usage only - provides the type-unsafe implementation which services the      * four typesafe public lookupDbEntity methods Looks in the DataMap's that this object      * was created with for the ObjEntity that maps to the specified object. Object may be      * a Entity name, ObjEntity, DataObject class (Class object for a class which      * implements the DataObject interface), or a DataObject instance itself      *       * @return the required DbEntity, or null if none matches the specifier      */
specifier|protected
name|DbEntity
name|_lookupDbEntity
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|DbEntity
condition|)
block|{
return|return
operator|(
name|DbEntity
operator|)
name|object
return|;
block|}
name|Object
name|result
init|=
name|dbEntityCache
operator|.
name|get
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// reconstruct cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|dbEntityCache
operator|.
name|get
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
name|DUPLICATE_MARKER
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't perform lookup. There is more than one DbEntity mapped to "
operator|+
name|object
argument_list|)
throw|;
block|}
return|return
operator|(
name|DbEntity
operator|)
name|result
return|;
block|}
comment|/**      * Internal usage only - provides the type-unsafe implementation which services the      * three typesafe public lookupObjEntity methods Looks in the DataMap's that this      * object was created with for the ObjEntity that maps to the specified object. Object      * may be a Entity name, DataObject instance or DataObject class (Class object for a      * class which implements the DataObject interface)      *       * @return the required ObjEntity or null if there is none that matches the specifier      */
specifier|protected
name|ObjEntity
name|_lookupObjEntity
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
operator|(
name|ObjEntity
operator|)
name|object
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Persistent
condition|)
block|{
name|object
operator|=
name|object
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
name|Object
name|result
init|=
name|objEntityCache
operator|.
name|get
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// reconstruct cache just in case some of the datamaps
comment|// have changed and now contain the required information
name|constructCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|objEntityCache
operator|.
name|get
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
name|DUPLICATE_MARKER
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't perform lookup. There is more than one ObjEntity mapped to "
operator|+
name|object
argument_list|)
throw|;
block|}
return|return
operator|(
name|ObjEntity
operator|)
name|result
return|;
block|}
comment|/**      * Returns an object that compiles and stores {@link ClassDescriptor} instances for      * all entities.      *       * @since 3.0      */
specifier|public
name|ClassDescriptorMap
name|getClassDescriptorMap
parameter_list|()
block|{
if|if
condition|(
name|classDescriptorMap
operator|==
literal|null
condition|)
block|{
name|ClassDescriptorMap
name|classDescriptorMap
init|=
operator|new
name|ClassDescriptorMap
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// add factories in reverse of the desired chain order
name|classDescriptorMap
operator|.
name|addFactory
argument_list|(
operator|new
name|ValueHolderDescriptorFactory
argument_list|(
name|classDescriptorMap
argument_list|)
argument_list|)
expr_stmt|;
name|classDescriptorMap
operator|.
name|addFactory
argument_list|(
operator|new
name|EnhancedPojoDescriptorFactory
argument_list|(
name|classDescriptorMap
argument_list|)
argument_list|)
expr_stmt|;
name|classDescriptorMap
operator|.
name|addFactory
argument_list|(
operator|new
name|DataObjectDescriptorFactory
argument_list|(
name|classDescriptorMap
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|classDescriptorMap
operator|=
name|classDescriptorMap
expr_stmt|;
block|}
return|return
name|classDescriptorMap
return|;
block|}
block|}
end_class

end_unit

