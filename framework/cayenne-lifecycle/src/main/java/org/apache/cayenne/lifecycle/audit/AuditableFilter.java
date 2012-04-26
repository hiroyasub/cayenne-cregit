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
name|lifecycle
operator|.
name|audit
package|;
end_package

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
name|DataChannelFilter
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
name|DataChannelFilterChain
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
name|DataObject
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
name|QueryResponse
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
name|lifecycle
operator|.
name|changeset
operator|.
name|ChangeSetFilter
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

begin_comment
comment|/**  * A {@link DataChannelFilter} that enables audit of entities annotated with  * {@link Auditable} and {@link AuditableChild}. Note that this filter relies on  * {@link ChangeSetFilter} presence in the DataDomain filter chain to be able to analyze  * ignored properties.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|AuditableFilter
implements|implements
name|DataChannelFilter
block|{
specifier|private
name|ThreadLocal
argument_list|<
name|AuditableAggregator
argument_list|>
name|threadAggregator
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|AuditableEntityDescriptor
argument_list|>
name|entityDescriptors
decl_stmt|;
specifier|protected
name|AuditableProcessor
name|processor
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|public
name|AuditableFilter
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|,
name|AuditableProcessor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
name|this
operator|.
name|entityDescriptors
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AuditableEntityDescriptor
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|threadAggregator
operator|=
operator|new
name|ThreadLocal
argument_list|<
name|AuditableAggregator
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|,
name|DataChannelFilterChain
name|filterChain
parameter_list|)
block|{
return|return
name|filterChain
operator|.
name|onQuery
argument_list|(
name|originatingContext
argument_list|,
name|query
argument_list|)
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
parameter_list|,
name|DataChannelFilterChain
name|filterChain
parameter_list|)
block|{
name|GraphDiff
name|response
decl_stmt|;
try|try
block|{
name|response
operator|=
name|filterChain
operator|.
name|onSync
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
expr_stmt|;
if|if
condition|(
name|syncType
operator|==
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
operator|||
name|syncType
operator|==
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
condition|)
block|{
name|postSync
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|cleanupPostSync
argument_list|()
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/**      * A method called at the end of every      * {@link #onSync(ObjectContext, GraphDiff, int, DataChannelFilterChain)} invocation.      * This implementation uses it for cleaning up thread-local state of the filter.      * Subclasses may override it to do their own cleanup, and are expected to call super.      */
specifier|protected
name|void
name|cleanupPostSync
parameter_list|()
block|{
name|threadAggregator
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|void
name|postSync
parameter_list|()
block|{
name|AuditableAggregator
name|aggregator
init|=
name|threadAggregator
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|aggregator
operator|!=
literal|null
condition|)
block|{
comment|// must reset thread aggregator before processing the audit operations
comment|// to avoid an endless processing loop if audit processor commits
comment|// something
name|threadAggregator
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|aggregator
operator|.
name|postSync
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|AuditableAggregator
name|getAggregator
parameter_list|()
block|{
name|AuditableAggregator
name|aggregator
init|=
name|threadAggregator
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|aggregator
operator|==
literal|null
condition|)
block|{
name|aggregator
operator|=
operator|new
name|AuditableAggregator
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|threadAggregator
operator|.
name|set
argument_list|(
name|aggregator
argument_list|)
expr_stmt|;
block|}
return|return
name|aggregator
return|;
block|}
annotation|@
name|PostPersist
argument_list|(
name|entityAnnotations
operator|=
name|Auditable
operator|.
name|class
argument_list|)
name|void
name|insertAudit
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|getAggregator
argument_list|()
operator|.
name|audit
argument_list|(
name|object
argument_list|,
name|AuditableOperation
operator|.
name|INSERT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostRemove
argument_list|(
name|entityAnnotations
operator|=
name|Auditable
operator|.
name|class
argument_list|)
name|void
name|deleteAudit
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|getAggregator
argument_list|()
operator|.
name|audit
argument_list|(
name|object
argument_list|,
name|AuditableOperation
operator|.
name|DELETE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostUpdate
argument_list|(
name|entityAnnotations
operator|=
name|Auditable
operator|.
name|class
argument_list|)
name|void
name|updateAudit
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|isAuditableUpdate
argument_list|(
name|object
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|getAggregator
argument_list|()
operator|.
name|audit
argument_list|(
name|object
argument_list|,
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
block|}
block|}
comment|// only catching child updates... child insert/delete presumably causes an event on
comment|// the owner object
annotation|@
name|PostUpdate
argument_list|(
name|entityAnnotations
operator|=
name|AuditableChild
operator|.
name|class
argument_list|)
name|void
name|updateAuditChild
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|isAuditableUpdate
argument_list|(
name|object
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|Object
name|parent
init|=
name|getParent
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
comment|// not calling 'updateAudit' to skip checking 'isAuditableUpdate' on
comment|// parent
name|getAggregator
argument_list|()
operator|.
name|audit
argument_list|(
name|parent
argument_list|,
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: maybe log this fact... shouldn't normally happen, but I can
comment|// imagine certain combinations of object graphs, disconnected
comment|// relationships, delete rules, etc. may cause this
block|}
block|}
block|}
specifier|protected
name|Object
name|getParent
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null object"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|DataObject
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Object is not a DataObject: "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|DataObject
name|dataObject
init|=
operator|(
name|DataObject
operator|)
name|object
decl_stmt|;
name|AuditableChild
name|annotation
init|=
name|dataObject
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|AuditableChild
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No 'AuditableChild' annotation found"
argument_list|)
throw|;
block|}
name|String
name|propertyPath
init|=
name|annotation
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyPath
operator|==
literal|null
operator|||
name|propertyPath
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|propertyPath
operator|=
name|objectIdRelationshipName
argument_list|(
name|annotation
operator|.
name|objectIdRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyPath
operator|==
literal|null
operator|||
name|propertyPath
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Either 'value' or 'objectIdRelationship' of @AuditableChild must be set"
argument_list|)
throw|;
block|}
return|return
name|dataObject
operator|.
name|readNestedProperty
argument_list|(
name|propertyPath
argument_list|)
return|;
block|}
comment|// TODO: It's a temporary clone method of {@link
comment|// org.apache.cayenne.lifecycle.relationship.ObjectIdRelationshipHandler#objectIdRelationshipName(String)}.
comment|// Needs to be encapsulated to some separate class to avoid a code duplication
specifier|private
name|String
name|objectIdRelationshipName
parameter_list|(
name|String
name|uuidPropertyName
parameter_list|)
block|{
return|return
literal|"cay:related:"
operator|+
name|uuidPropertyName
return|;
block|}
specifier|protected
name|boolean
name|isAuditableUpdate
parameter_list|(
name|Object
name|object
parameter_list|,
name|boolean
name|child
parameter_list|)
block|{
name|AuditableEntityDescriptor
name|descriptor
init|=
name|getEntityDescriptor
argument_list|(
name|object
argument_list|,
name|child
argument_list|)
decl_stmt|;
return|return
name|descriptor
operator|.
name|auditableChange
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
return|;
block|}
specifier|private
name|AuditableEntityDescriptor
name|getEntityDescriptor
parameter_list|(
name|Object
name|object
parameter_list|,
name|boolean
name|child
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|entityResolver
operator|.
name|lookupObjEntity
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|AuditableEntityDescriptor
name|descriptor
init|=
name|entityDescriptors
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
name|descriptor
operator|==
literal|null
condition|)
block|{
name|String
index|[]
name|ignoredProperties
decl_stmt|;
if|if
condition|(
name|child
condition|)
block|{
name|AuditableChild
name|annotation
init|=
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|AuditableChild
operator|.
name|class
argument_list|)
decl_stmt|;
name|ignoredProperties
operator|=
name|annotation
operator|!=
literal|null
condition|?
name|annotation
operator|.
name|ignoredProperties
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
else|else
block|{
name|Auditable
name|annotation
init|=
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Auditable
operator|.
name|class
argument_list|)
decl_stmt|;
name|ignoredProperties
operator|=
name|annotation
operator|!=
literal|null
condition|?
name|annotation
operator|.
name|ignoredProperties
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
name|descriptor
operator|=
operator|new
name|AuditableEntityDescriptor
argument_list|(
name|entity
argument_list|,
name|ignoredProperties
argument_list|)
expr_stmt|;
name|AuditableEntityDescriptor
name|existingDescriptor
init|=
name|entityDescriptors
operator|.
name|putIfAbsent
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingDescriptor
operator|!=
literal|null
condition|)
block|{
name|descriptor
operator|=
name|existingDescriptor
expr_stmt|;
block|}
block|}
return|return
name|descriptor
return|;
block|}
block|}
end_class

end_unit

