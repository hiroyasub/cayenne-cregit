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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
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
name|FaultFactory
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
name|LifecycleCallbackRegistry
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
name|SingletonFaultFactory
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Represents a virtual shared namespace for zero or more DataMaps. Unlike  * DataMap, EntityResolver is intended to work as a runtime container of  * mapping. DataMaps can be added or removed dynamically at runtime.  *<p>  * EntityResolver is thread-safe.  *</p>  *   * @since 1.1  */
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
specifier|protected
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EntityResolver
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Deprecated
specifier|protected
name|boolean
name|indexedByClass
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
decl_stmt|;
specifier|protected
specifier|transient
name|MappingNamespace
name|mappingCache
decl_stmt|;
specifier|protected
name|EntityResolver
name|clientEntityResolver
decl_stmt|;
comment|// must be transient, as resolver may get deserialized in another VM, and
comment|// descriptor recompilation will be desired.
specifier|protected
specifier|transient
specifier|volatile
name|ClassDescriptorMap
name|classDescriptorMap
decl_stmt|;
comment|// callbacks are not serializable
specifier|protected
specifier|transient
name|LifecycleCallbackRegistry
name|callbackRegistry
decl_stmt|;
comment|/**      * Creates new empty EntityResolver.      */
specifier|public
name|EntityResolver
parameter_list|()
block|{
name|this
argument_list|(
name|Collections
operator|.
expr|<
name|DataMap
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates new EntityResolver that indexes a collection of DataMaps.      */
specifier|public
name|EntityResolver
parameter_list|(
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
parameter_list|)
block|{
name|this
operator|.
name|maps
operator|=
operator|new
name|ArrayList
argument_list|<
name|DataMap
argument_list|>
argument_list|(
name|dataMaps
argument_list|)
expr_stmt|;
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
comment|/**      * Updates missing mapping artifacts that can be guessed from other mapping      * information. This implementation creates missing reverse relationships,      * marking newly created relationships as "runtime".      *       * @since 3.0      */
specifier|public
name|void
name|applyDBLayerDefaults
parameter_list|()
block|{
comment|// connect DB layer
for|for
control|(
name|DataMap
name|map
range|:
name|getDataMaps
argument_list|()
control|)
block|{
for|for
control|(
name|DbEntity
name|entity
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
comment|// iterate by copy to avoid concurrency modification errors on
comment|// reflexive
comment|// relationships
name|Object
index|[]
name|relationships
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|toArray
argument_list|()
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
name|relationships
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
name|relationships
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
operator|==
literal|null
condition|)
block|{
name|DbRelationship
name|reverse
init|=
name|relationship
operator|.
name|createReverseRelationship
argument_list|()
decl_stmt|;
name|Entity
name|targetEntity
init|=
name|reverse
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|reverse
operator|.
name|setName
argument_list|(
name|makeUniqueRelationshipName
argument_list|(
name|targetEntity
argument_list|)
argument_list|)
expr_stmt|;
name|reverse
operator|.
name|setRuntime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|targetEntity
operator|.
name|addRelationship
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"added runtime complimentary DbRelationship from "
operator|+
name|targetEntity
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|reverse
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Updates missing mapping artifacts that can be guessed from other mapping      * information. This implementation creates missing reverse relationships,      * marking newly created relationships as "runtime".      *       * @since 3.0      */
specifier|public
name|void
name|applyObjectLayerDefaults
parameter_list|()
block|{
comment|// connect object layer
for|for
control|(
name|DataMap
name|map
range|:
name|getDataMaps
argument_list|()
control|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|map
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
comment|// iterate by copy to avoid concurrency modification errors on
comment|// reflexive
comment|// relationships
name|Object
index|[]
name|relationships
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|toArray
argument_list|()
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
name|relationships
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|relationships
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ObjRelationship
name|reverse
init|=
name|relationship
operator|.
name|createReverseRelationship
argument_list|()
decl_stmt|;
name|Entity
name|targetEntity
init|=
name|reverse
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|reverse
operator|.
name|setName
argument_list|(
name|makeUniqueRelationshipName
argument_list|(
name|targetEntity
argument_list|)
argument_list|)
expr_stmt|;
name|reverse
operator|.
name|setRuntime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|targetEntity
operator|.
name|addRelationship
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"added runtime complimentary ObjRelationship from "
operator|+
name|targetEntity
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|reverse
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|String
name|makeUniqueRelationshipName
parameter_list|(
name|Entity
name|entity
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
literal|"runtimeRelationship"
operator|+
name|i
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getRelationship
argument_list|(
name|name
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Could not come up with a unique relationship name"
argument_list|)
throw|;
block|}
comment|/**      * Compiles internal callback registry.      */
specifier|synchronized
name|void
name|initCallbacks
parameter_list|()
block|{
if|if
condition|(
name|callbackRegistry
operator|==
literal|null
condition|)
block|{
name|LifecycleCallbackRegistry
name|callbackRegistry
init|=
operator|new
name|LifecycleCallbackRegistry
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// load entity callbacks
for|for
control|(
name|ObjEntity
name|entity
range|:
name|getObjEntities
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
init|=
name|entity
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
name|CallbackDescriptor
index|[]
name|callbacks
init|=
name|entity
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getCallbacks
argument_list|()
decl_stmt|;
for|for
control|(
name|CallbackDescriptor
name|callback
range|:
name|callbacks
control|)
block|{
for|for
control|(
name|String
name|method
range|:
name|callback
operator|.
name|getCallbackMethods
argument_list|()
control|)
block|{
name|callbackRegistry
operator|.
name|addCallback
argument_list|(
name|callback
operator|.
name|getCallbackType
argument_list|()
argument_list|,
name|entityClass
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|callbackRegistry
operator|=
name|callbackRegistry
expr_stmt|;
block|}
block|}
comment|/**      * Returns a {@link LifecycleCallbackRegistry} for handling callbacks.      * Registry is lazily initialized on first call.      *       * @since 3.0      */
specifier|public
name|LifecycleCallbackRegistry
name|getCallbackRegistry
parameter_list|()
block|{
if|if
condition|(
name|callbackRegistry
operator|==
literal|null
condition|)
block|{
name|initCallbacks
argument_list|()
expr_stmt|;
block|}
return|return
name|callbackRegistry
return|;
block|}
comment|/**      * Sets a lifecycle callbacks registry of the EntityResolver. Users rarely      * if ever need to call this method as Cayenne would instantiate a registry      * itself as needed based on mapped configuration.      *       * @since 3.0      */
specifier|public
name|void
name|setCallbackRegistry
parameter_list|(
name|LifecycleCallbackRegistry
name|callbackRegistry
parameter_list|)
block|{
name|this
operator|.
name|callbackRegistry
operator|=
name|callbackRegistry
expr_stmt|;
block|}
comment|/**      * Returns ClientEntityResolver with mapping information that only includes      * entities available on CWS Client Tier.      *       * @since 1.2      */
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
for|for
control|(
name|DataMap
name|map
range|:
name|getDataMaps
argument_list|()
control|)
block|{
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
argument_list|<
name|DbEntity
argument_list|>
name|getDbEntities
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getDbEntities
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|getObjEntities
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getObjEntities
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Collection
argument_list|<
name|Embeddable
argument_list|>
name|getEmbeddables
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getEmbeddables
argument_list|()
return|;
block|}
comment|/**      * @deprecated since 3.2 use {@link #getResults()}.      */
annotation|@
name|Deprecated
specifier|public
name|Collection
argument_list|<
name|SQLResult
argument_list|>
name|getResultSets
parameter_list|()
block|{
return|return
name|getResults
argument_list|()
return|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|Collection
argument_list|<
name|SQLResult
argument_list|>
name|getResults
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getResults
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Procedure
argument_list|>
name|getProcedures
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getProcedures
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Query
argument_list|>
name|getQueries
parameter_list|()
block|{
return|return
name|mappingCache
operator|.
name|getQueries
argument_list|()
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
name|DbEntity
name|result
init|=
name|mappingCache
operator|.
name|getDbEntity
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getDbEntity
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
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ObjEntity
name|result
init|=
name|mappingCache
operator|.
name|getObjEntity
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getObjEntity
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
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|Procedure
name|result
init|=
name|mappingCache
operator|.
name|getProcedure
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getProcedure
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
name|Query
name|getQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Query
name|result
init|=
name|mappingCache
operator|.
name|getQuery
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getQuery
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
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
name|mappingCache
operator|.
name|getEmbeddable
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getEmbeddable
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|SQLResult
name|getResult
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|SQLResult
name|result
init|=
name|mappingCache
operator|.
name|getResult
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getResult
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Returns ClassDescriptor for the ObjEntity matching the name. Returns null      * if no matching entity exists.      *       * @since 1.2      */
specifier|public
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Removes all entity mappings from the cache.      *       * @deprecated since 3.2 in favor of {@link #refreshMappingCache()}.      */
annotation|@
name|Deprecated
specifier|public
name|void
name|clearCache
parameter_list|()
block|{
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
comment|/**      * Refreshes entity cache to reflect the current state of the DataMaps in      * the EntityResolver.      *       * @since 3.2      */
specifier|public
name|void
name|refreshMappingCache
parameter_list|()
block|{
name|mappingCache
operator|=
operator|new
name|ProxiedMappingNamespace
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|MappingCache
name|createDelegate
parameter_list|()
block|{
return|return
operator|new
name|MappingCache
argument_list|(
name|maps
argument_list|)
return|;
block|}
block|}
expr_stmt|;
name|clientEntityResolver
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Returns a DataMap matching the name.      */
specifier|public
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
for|for
control|(
name|DataMap
name|map
range|:
name|maps
control|)
block|{
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
argument_list|<
name|DataMap
argument_list|>
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an unmodifiable collection of DataMaps.      */
specifier|public
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|getDataMaps
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|maps
argument_list|)
return|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|EntityInheritanceTree
name|getInheritanceTree
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|EntityInheritanceTree
name|tree
init|=
name|mappingCache
operator|.
name|getInheritanceTree
argument_list|(
name|entityName
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|tree
operator|=
name|mappingCache
operator|.
name|getInheritanceTree
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
return|return
name|tree
return|;
block|}
comment|/**      * @deprecated since 3.2 use {@link #getInheritanceTree(String)}.      */
annotation|@
name|Deprecated
specifier|public
name|EntityInheritanceTree
name|lookupInheritanceTree
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
return|return
name|getInheritanceTree
argument_list|(
name|entityName
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the      * ObjEntity that maps to the services the specified class      *       * @return the required ObjEntity or null if there is none that matches the      *         specifier      *       * @since 3.2      */
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
block|{
name|ObjEntity
name|result
init|=
name|mappingCache
operator|.
name|getObjEntity
argument_list|(
name|entityClass
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|result
operator|=
name|mappingCache
operator|.
name|getObjEntity
argument_list|(
name|entityClass
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * @deprecated since 3.2, use {@link #getObjEntity(Class)}.      */
specifier|public
name|ObjEntity
name|lookupObjEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
block|{
return|return
name|getObjEntity
argument_list|(
name|entityClass
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
return|return
name|mappingCache
operator|.
name|getObjEntity
argument_list|(
name|object
argument_list|)
return|;
block|}
comment|/**      * Looks in the DataMap's that this object was created with for the      * ObjEntity that services the specified data Object      *       * @return the required ObjEntity, or null if none matches the specifier      * @since 3.2 a corresponding getObjEntity method should be used.      */
annotation|@
name|Deprecated
specifier|public
name|ObjEntity
name|lookupObjEntity
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
name|ObjectId
name|id
init|=
operator|(
operator|(
name|Persistent
operator|)
name|object
operator|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
return|return
name|getObjEntity
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
return|;
block|}
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Class
condition|)
block|{
return|return
name|getObjEntity
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|object
argument_list|)
return|;
block|}
return|return
name|getObjEntity
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @deprecated since 3.2. Use q.getMetaData(resolver).getProcedure()      */
annotation|@
name|Deprecated
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
comment|/**      * @deprecated since 3.2 use {@link #getProcedure(String)}.      */
annotation|@
name|Deprecated
specifier|public
name|Procedure
name|lookupProcedure
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
return|return
name|getProcedure
argument_list|(
name|procedureName
argument_list|)
return|;
block|}
comment|/**      * @deprecated since 3.2 use {@link #getQuery(String)}.      */
annotation|@
name|Deprecated
specifier|public
name|Query
name|lookupQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getQuery
argument_list|(
name|name
argument_list|)
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
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @deprecated since 3.2. There's no replacement. This property is      *             meaningless and is no longer respected by the code.      */
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isIndexedByClass
parameter_list|()
block|{
return|return
name|indexedByClass
return|;
block|}
comment|/**      * @deprecated since 3.2. There's no replacement. This property is      *             meaningless.      */
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
comment|/**      * Returns an object that compiles and stores {@link ClassDescriptor}      * instances for all entities.      *       * @since 3.0      */
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
synchronized|synchronized
init|(
name|this
init|)
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
name|FaultFactory
name|faultFactory
init|=
operator|new
name|SingletonFaultFactory
argument_list|()
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
name|DataObjectDescriptorFactory
argument_list|(
name|classDescriptorMap
argument_list|,
name|faultFactory
argument_list|)
argument_list|)
expr_stmt|;
comment|// since ClassDescriptorMap is not synchronized, we need to
comment|// prefill
comment|// it with entity proxies here.
for|for
control|(
name|DataMap
name|map
range|:
name|maps
control|)
block|{
for|for
control|(
name|String
name|entityName
range|:
name|map
operator|.
name|getObjEntityMap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|classDescriptorMap
operator|.
name|getDescriptor
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|classDescriptorMap
operator|=
name|classDescriptorMap
expr_stmt|;
block|}
block|}
block|}
return|return
name|classDescriptorMap
return|;
block|}
comment|/**      * @since 3.0      * @deprecated since 3.2 this method does nothing, as EntityResolver no      *             longer loads listeners from its DataMaps.      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setEntityListenerFactory
parameter_list|(
name|EntityListenerFactory
name|entityListenerFactory
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Java default deserialization seems not to invoke constructor by default -      * invoking it manually      */
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|in
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

