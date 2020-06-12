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
name|access
package|;
end_package

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
name|DataRow
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
name|map
operator|.
name|DbJoin
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
name|ObjAttribute
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

begin_comment
comment|/**  * DataRowUtils contains a number of static methods to work with DataRows. This is a  * helper class for DataContext and ObjectStore.  *   * @since 1.1  */
end_comment

begin_class
class|class
name|DataRowUtils
block|{
comment|/**      * Merges changes reflected in snapshot map to the object. Changes made to attributes      * and to-one relationships will be merged. In case an object is already modified,      * modified properties will not be overwritten.      */
specifier|static
name|void
name|mergeObjectWithSnapshot
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
name|Persistent
name|object
parameter_list|,
name|DataRow
name|snapshot
parameter_list|)
block|{
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
name|HOLLOW
operator|||
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
name|refreshObjectWithSnapshot
argument_list|(
name|descriptor
argument_list|,
name|object
argument_list|,
name|snapshot
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|state
operator|!=
name|PersistenceState
operator|.
name|COMMITTED
condition|)
block|{
name|forceMergeWithSnapshot
argument_list|(
name|context
argument_list|,
name|descriptor
argument_list|,
name|object
argument_list|,
name|snapshot
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// do not invalidate to-many relationships, since they might have
comment|// just been prefetched...
name|refreshObjectWithSnapshot
argument_list|(
name|descriptor
argument_list|,
name|object
argument_list|,
name|snapshot
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Replaces all object attribute values with snapshot values. Sets object state to      * COMMITTED, unless the snapshot is partial in which case the state is set to HOLLOW      */
specifier|static
name|void
name|refreshObjectWithSnapshot
parameter_list|(
name|ClassDescriptor
name|descriptor
parameter_list|,
specifier|final
name|Persistent
name|object
parameter_list|,
specifier|final
name|DataRow
name|snapshot
parameter_list|,
specifier|final
name|boolean
name|invalidateToManyRelationships
parameter_list|)
block|{
specifier|final
name|boolean
index|[]
name|isPartialSnapshot
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|descriptor
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
name|AttributeProperty
name|property
parameter_list|)
block|{
name|ObjAttribute
name|attr
init|=
name|property
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|String
name|dbAttrPath
init|=
name|attr
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|snapshot
operator|.
name|get
argument_list|(
name|dbAttrPath
argument_list|)
decl_stmt|;
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// note that a check "snaphsot.get(..) == null" would be incorrect in this
comment|// case, as NULL value is entirely valid; still save a map lookup by
comment|// checking for the null value first
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
operator|!
name|snapshot
operator|.
name|containsKey
argument_list|(
name|dbAttrPath
argument_list|)
condition|)
block|{
if|if
condition|(
name|attr
operator|.
name|isLazy
argument_list|()
condition|)
block|{
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
operator|new
name|AttributeFault
argument_list|(
name|property
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isPartialSnapshot
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
literal|true
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
comment|// "to many" relationships have no information to collect from
comment|// snapshot
if|if
condition|(
name|invalidateToManyRelationships
condition|)
block|{
name|property
operator|.
name|invalidate
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
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
name|property
operator|.
name|invalidate
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|isPartialSnapshot
index|[
literal|0
index|]
condition|?
name|PersistenceState
operator|.
name|HOLLOW
else|:
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|forceMergeWithSnapshot
parameter_list|(
specifier|final
name|DataContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
specifier|final
name|Persistent
name|object
parameter_list|,
specifier|final
name|DataRow
name|snapshot
parameter_list|)
block|{
specifier|final
name|ObjectDiff
name|diff
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getChangesByObjectId
argument_list|()
operator|.
name|get
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|descriptor
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
name|AttributeProperty
name|property
parameter_list|)
block|{
name|String
name|dbAttrPath
init|=
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
comment|// supports merging of partial snapshots...
comment|// check for null is cheaper than double lookup
comment|// for a key... so check for partial snapshot
comment|// only if the value is null
name|Object
name|newValue
init|=
name|snapshot
operator|.
name|get
argument_list|(
name|dbAttrPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|newValue
operator|!=
literal|null
operator|||
name|snapshot
operator|.
name|containsKey
argument_list|(
name|dbAttrPath
argument_list|)
condition|)
block|{
name|Object
name|curValue
init|=
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|Object
name|oldValue
init|=
name|diff
operator|!=
literal|null
condition|?
name|diff
operator|.
name|getSnapshotValue
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
comment|// if value not modified, update it from snapshot,
comment|// otherwise leave it alone
if|if
condition|(
name|property
operator|.
name|isEqual
argument_list|(
name|curValue
argument_list|,
name|oldValue
argument_list|)
operator|&&
operator|!
name|property
operator|.
name|isEqual
argument_list|(
name|newValue
argument_list|,
name|curValue
argument_list|)
condition|)
block|{
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|object
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
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
comment|// noop - nothing to merge
return|return
literal|true
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
name|isToPK
argument_list|()
condition|)
block|{
comment|// TODO: will this work for flattened, how do we save snapshots for
comment|// them?
comment|// if value not modified, update it from snapshot,
comment|// otherwise leave it alone
if|if
condition|(
operator|!
name|isToOneTargetModified
argument_list|(
name|property
argument_list|,
name|object
argument_list|,
name|diff
argument_list|)
condition|)
block|{
name|DbRelationship
name|dbRelationship
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// must check before creating ObjectId because of partial
comment|// snapshots
if|if
condition|(
name|hasFK
argument_list|(
name|dbRelationship
argument_list|,
name|snapshot
argument_list|)
condition|)
block|{
name|ObjectId
name|id
init|=
name|snapshot
operator|.
name|createTargetObjectId
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|,
name|dbRelationship
argument_list|)
decl_stmt|;
if|if
condition|(
name|diff
operator|==
literal|null
operator|||
operator|!
name|diff
operator|.
name|containsArcSnapshot
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|id
argument_list|,
name|diff
operator|.
name|getArcSnapshotValue
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|property
operator|.
name|writeProperty
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we can't use 'localObject' if relationship is
comment|// optional or inheritance is involved
comment|// .. must turn to fault instead
if|if
condition|(
operator|!
name|relationship
operator|.
name|isSourceDefiningTargetPrecenseAndType
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
condition|)
block|{
name|property
operator|.
name|invalidate
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|property
operator|.
name|writeProperty
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|context
operator|.
name|findOrCreateObject
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|static
name|boolean
name|hasFK
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
parameter_list|)
block|{
for|for
control|(
specifier|final
name|DbJoin
name|join
range|:
name|relationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|snapshot
operator|.
name|containsKey
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Checks if an object has its to-one relationship target modified in memory.      */
specifier|static
name|boolean
name|isToOneTargetModified
parameter_list|(
name|ArcProperty
name|property
parameter_list|,
name|Persistent
name|object
parameter_list|,
name|ObjectDiff
name|diff
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|MODIFIED
operator|||
name|diff
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
name|property
operator|.
name|isFault
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Persistent
name|toOneTarget
init|=
operator|(
name|Persistent
operator|)
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|ObjectId
name|currentId
init|=
operator|(
name|toOneTarget
operator|!=
literal|null
operator|)
condition|?
name|toOneTarget
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
decl_stmt|;
comment|// if ObjectId is temporary, target is definitely modified...
comment|// this would cover NEW objects (what are the other cases of temp id??)
if|if
condition|(
name|currentId
operator|!=
literal|null
operator|&&
name|currentId
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|diff
operator|.
name|containsArcSnapshot
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectId
name|targetId
init|=
name|diff
operator|.
name|getArcSnapshotValue
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|currentId
argument_list|,
name|targetId
argument_list|)
return|;
block|}
comment|// not for instantiation
name|DataRowUtils
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

