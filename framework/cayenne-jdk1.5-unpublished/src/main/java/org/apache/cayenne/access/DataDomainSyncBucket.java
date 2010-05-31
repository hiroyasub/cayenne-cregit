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
name|NodeIdChangeOperation
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
name|PropertyException
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
name|ToManyMapProperty
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
name|Factory
import|;
end_import

begin_comment
comment|/**  * A superclass of batch query wrappers.  *   * @since 1.2  */
end_comment

begin_class
specifier|abstract
class|class
name|DataDomainSyncBucket
block|{
specifier|final
name|Map
argument_list|<
name|ClassDescriptor
argument_list|,
name|List
argument_list|<
name|Persistent
argument_list|>
argument_list|>
name|objectsByDescriptor
decl_stmt|;
specifier|final
name|DataDomainFlushAction
name|parent
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
decl_stmt|;
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|Collection
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
argument_list|>
name|descriptorsByDbEntity
decl_stmt|;
name|DataDomainSyncBucket
parameter_list|(
name|DataDomainFlushAction
name|parent
parameter_list|)
block|{
name|this
operator|.
name|objectsByDescriptor
operator|=
operator|new
name|HashMap
argument_list|<
name|ClassDescriptor
argument_list|,
name|List
argument_list|<
name|Persistent
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|objectsByDescriptor
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|abstract
name|void
name|appendQueriesInternal
parameter_list|(
name|Collection
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|)
function_decl|;
comment|/**      * Appends all queries originated in the bucket to provided collection.      */
name|void
name|appendQueries
parameter_list|(
name|Collection
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
operator|!
name|objectsByDescriptor
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|groupObjEntitiesBySpannedDbEntities
argument_list|()
expr_stmt|;
name|appendQueriesInternal
argument_list|(
name|queries
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|checkReadOnly
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Entity must not be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|entity
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"Attempt to modify object(s) mapped to a read-only entity: "
argument_list|)
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|" '"
argument_list|)
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|". Can't commit changes."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|groupObjEntitiesBySpannedDbEntities
parameter_list|()
block|{
name|dbEntities
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|(
name|objectsByDescriptor
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|descriptorsByDbEntity
operator|=
operator|new
name|HashMap
argument_list|<
name|DbEntity
argument_list|,
name|Collection
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
argument_list|>
argument_list|(
name|objectsByDescriptor
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|ClassDescriptor
name|descriptor
range|:
name|objectsByDescriptor
operator|.
name|keySet
argument_list|()
control|)
block|{
comment|// root DbEntity
block|{
name|DbEntityClassDescriptor
name|dbEntityDescriptor
init|=
operator|new
name|DbEntityClassDescriptor
argument_list|(
name|descriptor
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|dbEntityDescriptor
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
name|descriptors
init|=
name|descriptorsByDbEntity
operator|.
name|get
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptors
operator|==
literal|null
condition|)
block|{
name|descriptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dbEntities
operator|.
name|add
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|descriptorsByDbEntity
operator|.
name|put
argument_list|(
name|dbEntity
argument_list|,
name|descriptors
argument_list|)
expr_stmt|;
block|}
name|descriptors
operator|.
name|add
argument_list|(
name|dbEntityDescriptor
argument_list|)
expr_stmt|;
block|}
comment|// secondary DbEntities...
comment|// Note that this logic won't allow flattened attributes to span multiple
comment|// databases...
for|for
control|(
name|ObjAttribute
name|objAttribute
range|:
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|objAttribute
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
name|DbEntityClassDescriptor
name|dbEntityDescriptor
init|=
operator|new
name|DbEntityClassDescriptor
argument_list|(
name|descriptor
argument_list|,
name|objAttribute
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|dbEntityDescriptor
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
name|descriptors
init|=
name|descriptorsByDbEntity
operator|.
name|get
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptors
operator|==
literal|null
condition|)
block|{
name|descriptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbEntityClassDescriptor
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dbEntities
operator|.
name|add
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|descriptorsByDbEntity
operator|.
name|put
argument_list|(
name|dbEntity
argument_list|,
name|descriptors
argument_list|)
expr_stmt|;
block|}
name|descriptors
operator|.
name|add
argument_list|(
name|dbEntityDescriptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|void
name|addDirtyObject
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
init|=
name|objectsByDescriptor
operator|.
name|get
argument_list|(
name|descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|==
literal|null
condition|)
block|{
name|objects
operator|=
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|()
expr_stmt|;
name|objectsByDescriptor
operator|.
name|put
argument_list|(
name|descriptor
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
name|objects
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
name|void
name|postprocess
parameter_list|()
block|{
if|if
condition|(
operator|!
name|objectsByDescriptor
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|CompoundDiff
name|result
init|=
name|parent
operator|.
name|getResultDiff
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|modifiedSnapshots
init|=
name|parent
operator|.
name|getResultModifiedSnapshots
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|deletedIds
init|=
name|parent
operator|.
name|getResultDeletedIds
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ClassDescriptor
argument_list|,
name|List
argument_list|<
name|Persistent
argument_list|>
argument_list|>
name|entry
range|:
name|objectsByDescriptor
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
for|for
control|(
name|Persistent
name|object
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|ObjectId
name|id
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|ObjectId
name|finalId
decl_stmt|;
comment|// record id change and update attributes for generated ids
if|if
condition|(
name|id
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacement
init|=
name|id
operator|.
name|getReplacementIdMap
argument_list|()
decl_stmt|;
for|for
control|(
name|AttributeProperty
name|property
range|:
name|descriptor
operator|.
name|getIdProperties
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|replacement
operator|.
name|get
argument_list|(
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 11/28/2006: this operation may be redundant
comment|// if the id wasn't generated. We may need to optimize it...
if|if
condition|(
name|value
operator|!=
literal|null
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
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectId
name|replacementId
init|=
name|id
operator|.
name|createReplacementId
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|NodeIdChangeOperation
argument_list|(
name|id
argument_list|,
name|replacementId
argument_list|)
argument_list|)
expr_stmt|;
comment|// classify replaced permanent ids as "deleted", as
comment|// DataRowCache has no notion of replaced id...
if|if
condition|(
operator|!
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
name|deletedIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|finalId
operator|=
name|replacementId
expr_stmt|;
block|}
if|else if
condition|(
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Temporary ID hasn't been replaced on commit: "
operator|+
name|object
argument_list|)
throw|;
block|}
else|else
block|{
name|finalId
operator|=
name|id
expr_stmt|;
block|}
comment|// do not take the snapshot until generated columns are processed (see
comment|// code above)
name|DataRow
name|dataRow
init|=
name|parent
operator|.
name|getContext
argument_list|()
operator|.
name|currentSnapshot
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DataObject
condition|)
block|{
name|DataObject
name|dataObject
init|=
operator|(
name|DataObject
operator|)
name|object
decl_stmt|;
name|dataRow
operator|.
name|setReplacesVersion
argument_list|(
name|dataObject
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|dataObject
operator|.
name|setSnapshotVersion
argument_list|(
name|dataRow
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|modifiedSnapshots
operator|.
name|put
argument_list|(
name|finalId
argument_list|,
name|dataRow
argument_list|)
expr_stmt|;
comment|// update Map reverse relationships
for|for
control|(
name|ArcProperty
name|arc
range|:
name|descriptor
operator|.
name|getMapArcProperties
argument_list|()
control|)
block|{
name|ToManyMapProperty
name|reverseArc
init|=
operator|(
name|ToManyMapProperty
operator|)
name|arc
operator|.
name|getComplimentaryReverseArc
argument_list|()
decl_stmt|;
comment|// must resolve faults... hopefully for to-one this will not cause
comment|// extra fetches...
name|Object
name|source
init|=
name|arc
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
operator|&&
operator|!
name|reverseArc
operator|.
name|isFault
argument_list|(
name|source
argument_list|)
condition|)
block|{
name|remapTarget
argument_list|(
name|reverseArc
argument_list|,
name|source
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
specifier|final
name|void
name|remapTarget
parameter_list|(
name|ToManyMapProperty
name|property
parameter_list|,
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|property
operator|.
name|readProperty
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Object
name|newKey
init|=
name|property
operator|.
name|getMapKey
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|Object
name|currentValue
init|=
name|map
operator|.
name|get
argument_list|(
name|newKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentValue
operator|==
name|target
condition|)
block|{
comment|// nothing to do
return|return;
block|}
comment|// else - do not check for conflicts here (i.e. another object mapped for the same
comment|// key), as we have no control of the order in which this method is called, so
comment|// another object may be remapped later by the caller
comment|// must do a slow map scan to ensure the object is not mapped under a different
comment|// key...
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|map
operator|.
name|entrySet
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
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|e
init|=
operator|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|target
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|map
operator|.
name|put
argument_list|(
name|newKey
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
comment|// a factory for extracting PKs generated on commit.
specifier|final
specifier|static
class|class
name|PropagatedValueFactory
implements|implements
name|Factory
block|{
name|ObjectId
name|masterID
decl_stmt|;
name|String
name|masterKey
decl_stmt|;
name|PropagatedValueFactory
parameter_list|(
name|ObjectId
name|masterID
parameter_list|,
name|String
name|masterKey
parameter_list|)
block|{
name|this
operator|.
name|masterID
operator|=
name|masterID
expr_stmt|;
name|this
operator|.
name|masterKey
operator|=
name|masterKey
expr_stmt|;
block|}
specifier|public
name|Object
name|create
parameter_list|()
block|{
name|Object
name|value
init|=
name|masterID
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|masterKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't extract a master key. "
operator|+
literal|"Missing key ("
operator|+
name|masterKey
operator|+
literal|"), master ID ("
operator|+
name|masterID
operator|+
literal|")"
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

