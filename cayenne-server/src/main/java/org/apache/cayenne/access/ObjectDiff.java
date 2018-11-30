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
name|access
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
name|Fault
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
name|exp
operator|.
name|parser
operator|.
name|ASTDbPath
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
name|GraphChangeHandler
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
name|NodeDiff
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
name|DbEntity
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
name|DbRelationship
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
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

begin_comment
comment|/**  * A dynamic GraphDiff that represents a delta between object simple properties  * at diff creation time and its current state.  */
end_comment

begin_class
class|class
name|ObjectDiff
extends|extends
name|NodeDiff
block|{
specifier|private
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|private
specifier|transient
name|ClassDescriptor
name|classDescriptor
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|NodeDiff
argument_list|>
name|otherDiffs
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|arcSnapshot
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|currentArcSnapshot
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ArcOperation
argument_list|,
name|ArcOperation
argument_list|>
name|flatIds
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ArcOperation
argument_list|,
name|ArcOperation
argument_list|>
name|phantomFks
decl_stmt|;
specifier|private
name|Persistent
name|object
decl_stmt|;
name|ObjectDiff
parameter_list|(
specifier|final
name|Persistent
name|object
parameter_list|)
block|{
name|super
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
comment|// retain the object, as ObjectStore may have weak references to
comment|// registered
comment|// objects and we can't allow it to deallocate dirty objects.
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
name|EntityResolver
name|entityResolver
init|=
name|object
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|this
operator|.
name|entityName
operator|=
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
expr_stmt|;
name|this
operator|.
name|classDescriptor
operator|=
name|entityResolver
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
name|int
name|state
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
comment|// take snapshot of simple properties and arcs used for optimistic
comment|// locking..
if|if
condition|(
name|state
operator|==
name|PersistenceState
operator|.
name|COMMITTED
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|DELETED
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|MODIFIED
condition|)
block|{
specifier|final
name|ObjEntity
name|entity
init|=
name|entityResolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|lock
init|=
name|entity
operator|.
name|getLockType
argument_list|()
operator|==
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
decl_stmt|;
name|this
operator|.
name|snapshot
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|arcSnapshot
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|classDescriptor
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|snapshot
operator|.
name|put
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|property
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|boolean
name|isUsedForLocking
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isUsedForLocking
argument_list|()
decl_stmt|;
comment|// eagerly resolve optimistically locked relationships
name|Object
name|target
init|=
operator|(
name|lock
operator|&&
name|isUsedForLocking
operator|)
condition|?
name|property
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
else|:
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|Persistent
condition|)
block|{
name|target
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|target
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
comment|// else - null || Fault
name|arcSnapshot
operator|.
name|put
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
comment|// class descriptor is initiated in constructor, but is nullified on
comment|// serialization
if|if
condition|(
name|classDescriptor
operator|==
literal|null
condition|)
block|{
name|EntityResolver
name|entityResolver
init|=
name|object
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|this
operator|.
name|classDescriptor
operator|=
name|entityResolver
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
return|return
name|classDescriptor
return|;
block|}
name|Object
name|getSnapshotValue
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|snapshot
operator|!=
literal|null
condition|?
name|snapshot
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
else|:
literal|null
return|;
block|}
name|ObjectId
name|getArcSnapshotValue
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|Object
name|value
init|=
name|arcSnapshot
operator|!=
literal|null
condition|?
name|arcSnapshot
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Fault
condition|)
block|{
name|Persistent
name|target
init|=
operator|(
name|Persistent
operator|)
operator|(
operator|(
name|Fault
operator|)
name|value
operator|)
operator|.
name|resolveFault
argument_list|(
name|object
argument_list|,
name|propertyName
argument_list|)
decl_stmt|;
name|value
operator|=
name|target
operator|!=
literal|null
condition|?
name|target
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
expr_stmt|;
name|arcSnapshot
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ObjectId
operator|)
name|value
return|;
block|}
name|boolean
name|containsArcSnapshot
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|arcSnapshot
operator|!=
literal|null
operator|&&
name|arcSnapshot
operator|.
name|containsKey
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
comment|/**      * Appends individual diffs to collection.      */
name|void
name|appendDiffs
parameter_list|(
name|Collection
argument_list|<
name|NodeDiff
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|otherDiffs
operator|!=
literal|null
condition|)
block|{
name|collection
operator|.
name|addAll
argument_list|(
name|otherDiffs
argument_list|)
expr_stmt|;
block|}
name|collection
operator|.
name|add
argument_list|(
operator|new
name|NodeDiff
argument_list|(
name|nodeId
argument_list|,
name|diffId
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
name|applySimplePropertyChanges
argument_list|(
name|tracker
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|void
name|addDiff
parameter_list|(
name|NodeDiff
name|diff
parameter_list|,
name|ObjectStore
name|parent
parameter_list|)
block|{
name|boolean
name|addDiff
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|diff
operator|instanceof
name|ArcOperation
condition|)
block|{
name|ArcOperation
name|arcDiff
init|=
operator|(
name|ArcOperation
operator|)
name|diff
decl_stmt|;
name|Object
name|targetId
init|=
name|arcDiff
operator|.
name|getTargetNodeId
argument_list|()
decl_stmt|;
name|String
name|arcId
init|=
name|arcDiff
operator|.
name|getArcId
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArcProperty
name|property
init|=
operator|(
name|ArcProperty
operator|)
name|getClassDescriptor
argument_list|()
operator|.
name|getProperty
argument_list|(
name|arcId
argument_list|)
decl_stmt|;
comment|// note that some collection properties implement
comment|// 'SingleObjectArcProperty',
comment|// so we cant't do 'instanceof SingleObjectArcProperty'
comment|// TODO: andrus, 3.22.2006 - should we consider this a bug?
if|if
condition|(
name|property
operator|==
literal|null
operator|&&
name|arcId
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
name|addPhantomFkDiff
argument_list|(
name|arcDiff
argument_list|)
expr_stmt|;
name|addDiff
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|property
operator|instanceof
name|ToManyProperty
condition|)
block|{
comment|// record flattened op changes
name|ObjRelationship
name|relationship
init|=
name|property
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
if|if
condition|(
name|flatIds
operator|==
literal|null
condition|)
block|{
name|flatIds
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|ArcOperation
name|oldOp
init|=
name|flatIds
operator|.
name|put
argument_list|(
name|arcDiff
argument_list|,
name|arcDiff
argument_list|)
decl_stmt|;
comment|// "delete" cancels "create" and vice versa...
if|if
condition|(
name|oldOp
operator|!=
literal|null
operator|&&
name|oldOp
operator|.
name|isDelete
argument_list|()
operator|!=
name|arcDiff
operator|.
name|isDelete
argument_list|()
condition|)
block|{
name|addDiff
operator|=
literal|false
expr_stmt|;
name|flatIds
operator|.
name|remove
argument_list|(
name|arcDiff
argument_list|)
expr_stmt|;
if|if
condition|(
name|otherDiffs
operator|!=
literal|null
condition|)
block|{
name|otherDiffs
operator|.
name|remove
argument_list|(
name|oldOp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// register complimentary arc diff
name|String
name|arc
init|=
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|+
name|property
operator|.
name|getComplimentaryReverseDbRelationshipPath
argument_list|()
decl_stmt|;
name|ArcOperation
name|complimentartyOp
init|=
operator|new
name|ArcOperation
argument_list|(
name|targetId
argument_list|,
name|arcDiff
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|arc
argument_list|,
name|arcDiff
operator|.
name|isDelete
argument_list|()
argument_list|)
decl_stmt|;
name|parent
operator|.
name|registerDiff
argument_list|(
name|targetId
argument_list|,
name|complimentartyOp
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|property
operator|instanceof
name|ToOneProperty
condition|)
block|{
if|if
condition|(
name|currentArcSnapshot
operator|==
literal|null
condition|)
block|{
name|currentArcSnapshot
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|currentArcSnapshot
operator|.
name|put
argument_list|(
name|arcId
argument_list|,
name|targetId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|message
init|=
operator|(
name|property
operator|==
literal|null
operator|)
condition|?
literal|"No property for arcId "
operator|+
name|arcId
else|:
literal|"Unrecognized property for arcId "
operator|+
name|arcId
operator|+
literal|": "
operator|+
name|property
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|addDiff
condition|)
block|{
if|if
condition|(
name|otherDiffs
operator|==
literal|null
condition|)
block|{
name|otherDiffs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|otherDiffs
operator|.
name|add
argument_list|(
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addPhantomFkDiff
parameter_list|(
name|ArcOperation
name|arcDiff
parameter_list|)
block|{
name|String
name|arcId
init|=
name|arcDiff
operator|.
name|getArcId
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|classDescriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbRelationship
name|dbRelationship
init|=
name|dbEntity
operator|.
name|getRelationship
argument_list|(
name|arcId
operator|.
name|substring
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbRelationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentArcSnapshot
operator|==
literal|null
condition|)
block|{
name|currentArcSnapshot
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|currentArcSnapshot
operator|.
name|put
argument_list|(
name|arcId
argument_list|,
name|arcDiff
operator|.
name|getTargetNodeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|phantomFks
operator|==
literal|null
condition|)
block|{
name|phantomFks
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|ArcOperation
name|oldOp
init|=
name|phantomFks
operator|.
name|put
argument_list|(
name|arcDiff
argument_list|,
name|arcDiff
argument_list|)
decl_stmt|;
comment|// "delete" cancels "create" and vice versa...
if|if
condition|(
name|oldOp
operator|!=
literal|null
operator|&&
name|oldOp
operator|.
name|isDelete
argument_list|()
operator|!=
name|arcDiff
operator|.
name|isDelete
argument_list|()
condition|)
block|{
name|phantomFks
operator|.
name|remove
argument_list|(
name|arcDiff
argument_list|)
expr_stmt|;
if|if
condition|(
name|otherDiffs
operator|!=
literal|null
condition|)
block|{
name|otherDiffs
operator|.
name|remove
argument_list|(
name|oldOp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Checks whether at least a single property is modified.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
comment|// if we have no baseline to compare with, assume that there are changes
if|if
condition|(
name|snapshot
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|flatIds
operator|!=
literal|null
operator|&&
operator|!
name|flatIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|phantomFks
operator|!=
literal|null
operator|&&
operator|!
name|phantomFks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|state
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|PersistenceState
operator|.
name|NEW
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|DELETED
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// check phantom mods
specifier|final
name|boolean
index|[]
name|modFound
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|getClassDescriptor
argument_list|()
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|Object
name|oldValue
init|=
name|snapshot
operator|.
name|get
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|newValue
init|=
name|property
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
name|modFound
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
return|return
operator|!
name|modFound
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
comment|// flattened changes
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
if|if
condition|(
name|arcSnapshot
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Object
name|newValue
init|=
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|newValue
operator|instanceof
name|Fault
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Object
name|oldValue
init|=
name|arcSnapshot
operator|.
name|get
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldValue
argument_list|,
name|newValue
operator|!=
literal|null
condition|?
operator|(
operator|(
name|Persistent
operator|)
name|newValue
operator|)
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
argument_list|)
condition|)
block|{
name|modFound
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
return|return
operator|!
name|modFound
index|[
literal|0
index|]
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
operator|!
name|modFound
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|handler
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
specifier|final
name|GraphChangeHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|otherDiffs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|GraphDiff
name|diff
range|:
name|otherDiffs
control|)
block|{
name|diff
operator|.
name|apply
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
comment|// phantomFks are not in 'otherDiffs', while flattened diffs are
if|if
condition|(
name|phantomFks
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|GraphDiff
name|diff
range|:
name|phantomFks
operator|.
name|keySet
argument_list|()
control|)
block|{
name|diff
operator|.
name|apply
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
name|applySimplePropertyChanges
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|applySimplePropertyChanges
parameter_list|(
specifier|final
name|GraphChangeHandler
name|handler
parameter_list|)
block|{
name|getClassDescriptor
argument_list|()
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|Object
name|newValue
init|=
name|property
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
decl_stmt|;
comment|// no baseline to compare
if|if
condition|(
name|snapshot
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|newValue
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|// have baseline to compare
else|else
block|{
name|Object
name|oldValue
init|=
name|snapshot
operator|.
name|get
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is used to update faults.      */
name|void
name|updateArcSnapshot
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Persistent
name|object
parameter_list|)
block|{
if|if
condition|(
name|arcSnapshot
operator|==
literal|null
condition|)
block|{
name|arcSnapshot
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|arcSnapshot
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|object
operator|!=
literal|null
condition|?
name|object
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
class|class
name|ArcOperation
extends|extends
name|NodeDiff
block|{
specifier|private
name|Object
name|targetNodeId
decl_stmt|;
specifier|private
name|Object
name|arcId
decl_stmt|;
specifier|private
name|boolean
name|delete
decl_stmt|;
specifier|public
name|ArcOperation
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|,
name|boolean
name|delete
parameter_list|)
block|{
name|super
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|this
operator|.
name|targetNodeId
operator|=
name|targetNodeId
expr_stmt|;
name|this
operator|.
name|arcId
operator|=
name|arcId
expr_stmt|;
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
name|boolean
name|isDelete
parameter_list|()
block|{
return|return
name|delete
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// assuming String and ObjectId provide a good hashCode
return|return
name|arcId
operator|.
name|hashCode
argument_list|()
operator|+
name|targetNodeId
operator|.
name|hashCode
argument_list|()
operator|+
literal|5
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
comment|// compare ignoring op type...
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|ArcOperation
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArcOperation
name|other
init|=
operator|(
name|ArcOperation
operator|)
name|object
decl_stmt|;
return|return
name|arcId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|arcId
argument_list|)
operator|&&
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|targetNodeId
argument_list|,
name|other
operator|.
name|targetNodeId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
if|if
condition|(
name|delete
condition|)
block|{
name|tracker
operator|.
name|arcDeleted
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tracker
operator|.
name|arcCreated
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Object
name|getArcId
parameter_list|()
block|{
return|return
name|arcId
return|;
block|}
specifier|public
name|Object
name|getTargetNodeId
parameter_list|()
block|{
return|return
name|targetNodeId
return|;
block|}
block|}
block|}
end_class

end_unit

