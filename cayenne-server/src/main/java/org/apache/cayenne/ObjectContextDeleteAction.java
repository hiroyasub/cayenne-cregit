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
name|Collections
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
name|graph
operator|.
name|ArcId
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
name|DeleteRule
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
name|ObjRelationship
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
name|ArcProperty
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
comment|/**  * A CayenneContext helper that processes object deletion.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|ObjectContextDeleteAction
block|{
specifier|private
name|ObjectContext
name|context
decl_stmt|;
name|ObjectContextDeleteAction
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
name|boolean
name|performDelete
parameter_list|(
name|Persistent
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
name|int
name|oldState
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldState
operator|==
name|PersistenceState
operator|.
name|TRANSIENT
operator|||
name|oldState
operator|==
name|PersistenceState
operator|.
name|DELETED
condition|)
block|{
comment|// Drop out... especially in case of DELETED we might be about to get
comment|// into a horrible recursive loop due to CASCADE delete rules.
comment|// Assume that everything must have been done correctly already
comment|// and *don't* do it again
return|return
literal|false
return|;
block|}
if|if
condition|(
name|object
operator|.
name|getObjectContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Attempt to delete unregistered non-TRANSIENT object: %s"
argument_list|,
name|object
argument_list|)
throw|;
block|}
if|if
condition|(
name|object
operator|.
name|getObjectContext
argument_list|()
operator|!=
name|context
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Attempt to delete object regsitered in a different ObjectContext. Object: %s, context: %s"
argument_list|,
name|object
argument_list|,
name|context
argument_list|)
throw|;
block|}
comment|// must resolve HOLLOW objects before delete... needed
comment|// to process relationships and optimistic locking...
name|context
operator|.
name|prepareForAccess
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldState
operator|==
name|PersistenceState
operator|.
name|NEW
condition|)
block|{
name|deleteNew
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deletePersistent
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|deleteNew
parameter_list|(
name|Persistent
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|)
expr_stmt|;
name|processDeleteRules
argument_list|(
name|object
argument_list|,
name|PersistenceState
operator|.
name|NEW
argument_list|)
expr_stmt|;
comment|// if an object was NEW, we must throw it out
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|unregisterNode
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|deletePersistent
parameter_list|(
name|Persistent
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
name|context
operator|.
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
name|PRE_REMOVE
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|int
name|oldState
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|)
expr_stmt|;
name|processDeleteRules
argument_list|(
name|object
argument_list|,
name|oldState
argument_list|)
expr_stmt|;
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|nodeRemoved
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Collection
argument_list|<
name|Persistent
argument_list|>
name|toCollection
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
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|// create copies of collections to avoid iterator exceptions
if|if
condition|(
name|object
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
operator|(
name|Collection
argument_list|<
name|Persistent
argument_list|>
operator|)
name|object
argument_list|)
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Map
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|Persistent
argument_list|>
operator|)
name|object
operator|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
return|;
block|}
block|}
specifier|private
name|void
name|processDeleteRules
parameter_list|(
specifier|final
name|Persistent
name|object
parameter_list|,
name|int
name|oldState
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
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
for|for
control|(
specifier|final
name|ObjRelationship
name|relationship
range|:
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|boolean
name|processFlattened
init|=
name|relationship
operator|.
name|isFlattened
argument_list|()
operator|&&
name|relationship
operator|.
name|isToDependentEntity
argument_list|()
operator|&&
operator|!
name|relationship
operator|.
name|isReadOnly
argument_list|()
decl_stmt|;
comment|// first check for no action... bail out if no flattened processing is needed
if|if
condition|(
name|relationship
operator|.
name|getDeleteRule
argument_list|()
operator|==
name|DeleteRule
operator|.
name|NO_ACTION
operator|&&
operator|!
name|processFlattened
condition|)
block|{
continue|continue;
block|}
name|ArcProperty
name|property
init|=
operator|(
name|ArcProperty
operator|)
name|descriptor
operator|.
name|getProperty
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|Persistent
argument_list|>
name|relatedObjects
init|=
name|toCollection
argument_list|(
name|property
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
argument_list|)
decl_stmt|;
comment|// no related object, bail out
if|if
condition|(
name|relatedObjects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
comment|// process DENY rule first...
if|if
condition|(
name|relationship
operator|.
name|getDeleteRule
argument_list|()
operator|==
name|DeleteRule
operator|.
name|DENY
condition|)
block|{
name|object
operator|.
name|setPersistenceState
argument_list|(
name|oldState
argument_list|)
expr_stmt|;
name|String
name|message
init|=
name|relatedObjects
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
literal|"1 related object"
else|:
name|relatedObjects
operator|.
name|size
argument_list|()
operator|+
literal|" related objects"
decl_stmt|;
throw|throw
operator|new
name|DeleteDenyException
argument_list|(
name|object
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|message
argument_list|)
throw|;
block|}
comment|// process flattened with dependent join tables...
comment|// joins must be removed even if they are non-existent or ignored in the
comment|// object graph
if|if
condition|(
name|processFlattened
condition|)
block|{
name|ArcId
name|arcId
init|=
operator|new
name|ArcId
argument_list|(
name|property
argument_list|)
decl_stmt|;
for|for
control|(
name|Persistent
name|relatedObject
range|:
name|relatedObjects
control|)
block|{
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|arcDeleted
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|relatedObject
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
block|}
comment|// process remaining rules
switch|switch
condition|(
name|relationship
operator|.
name|getDeleteRule
argument_list|()
condition|)
block|{
case|case
name|DeleteRule
operator|.
name|NO_ACTION
case|:
break|break;
case|case
name|DeleteRule
operator|.
name|NULLIFY
case|:
name|ArcProperty
name|reverseArc
init|=
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseArc
operator|==
literal|null
condition|)
block|{
comment|// nothing we can do here
break|break;
block|}
name|reverseArc
operator|.
name|visit
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
for|for
control|(
name|Persistent
name|relatedObject
range|:
name|relatedObjects
control|)
block|{
name|property
operator|.
name|removeTarget
argument_list|(
name|relatedObject
argument_list|,
name|object
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
comment|// Inverse is to-one - find all related objects and
comment|// nullify the reverse relationship
for|for
control|(
name|Persistent
name|relatedObject
range|:
name|relatedObjects
control|)
block|{
name|property
operator|.
name|setTarget
argument_list|(
name|relatedObject
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
break|break;
case|case
name|DeleteRule
operator|.
name|CASCADE
case|:
comment|// Delete all related objects
for|for
control|(
name|Persistent
name|relatedObject
range|:
name|relatedObjects
control|)
block|{
name|performDelete
argument_list|(
name|relatedObject
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|object
operator|.
name|setPersistenceState
argument_list|(
name|oldState
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid delete rule %s"
argument_list|,
name|relationship
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

