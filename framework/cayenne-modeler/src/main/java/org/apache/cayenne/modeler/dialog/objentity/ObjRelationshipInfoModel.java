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
name|modeler
operator|.
name|dialog
operator|.
name|objentity
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
name|map
operator|.
name|*
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
name|modeler
operator|.
name|util
operator|.
name|Comparators
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
name|modeler
operator|.
name|util
operator|.
name|DeleteRuleUpdater
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
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|ModelChangeTypes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|Selector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|model
operator|.
name|basic
operator|.
name|BasicModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * A Scope model for mapping an ObjRelationship to one or more DbRelationships.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ObjRelationshipInfoModel
extends|extends
name|BasicModel
block|{
specifier|static
specifier|final
name|String
name|COLLECTION_TYPE_MAP
init|=
literal|"java.util.Map"
decl_stmt|;
specifier|static
specifier|final
name|String
name|COLLECTION_TYPE_SET
init|=
literal|"java.util.Set"
decl_stmt|;
specifier|static
specifier|final
name|String
name|COLLECTION_TYPE_COLLECTION
init|=
literal|"java.util.Collection"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_MAP_KEY
init|=
literal|"ID (default)"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|DB_RELATIONSHIPS_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"dbRelationships"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|SOURCE_ENTITY_NAME_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"relationship.sourceEntity.name"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|OBJECT_TARGET_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"objectTarget"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|OBJECT_TARGETS_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"objectTargets"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|RELATIONSHIP_NAME_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"relationshipName"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|TARGET_COLLECTIONS_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"targetCollections"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|TARGET_COLLECTION_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"targetCollection"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|MAP_KEYS_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"mapKeys"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|MAP_KEY_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"mapKey"
argument_list|)
decl_stmt|;
specifier|protected
name|ObjRelationship
name|relationship
decl_stmt|;
comment|/**      * List of DB Relationships current ObjRelationship is mapped to      */
specifier|protected
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
decl_stmt|;
specifier|protected
name|ObjEntity
name|objectTarget
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|objectTargets
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|targetCollections
decl_stmt|;
specifier|protected
name|List
name|mapKeys
decl_stmt|;
specifier|protected
name|String
name|relationshipName
decl_stmt|;
specifier|protected
name|String
name|targetCollection
decl_stmt|;
specifier|protected
name|String
name|mapKey
decl_stmt|;
specifier|public
name|ObjRelationshipInfoModel
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|)
block|{
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|relationshipName
operator|=
name|relationship
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|mapKey
operator|=
name|relationship
operator|.
name|getMapKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|targetCollection
operator|=
name|relationship
operator|.
name|getCollectionType
argument_list|()
expr_stmt|;
if|if
condition|(
name|targetCollection
operator|==
literal|null
condition|)
block|{
name|targetCollection
operator|=
name|ObjRelationship
operator|.
name|DEFAULT_COLLECTION_TYPE
expr_stmt|;
block|}
comment|// prepare entities - copy those that have DbEntities mapped, and then sort
name|this
operator|.
name|objectTargets
operator|=
operator|new
name|ArrayList
argument_list|<
name|ObjEntity
argument_list|>
argument_list|(
name|objEntities
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ObjEntity
argument_list|>
name|entities
init|=
name|objEntities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjEntity
name|entity
init|=
name|entities
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|objectTargets
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|objectTarget
operator|=
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|objectTargets
argument_list|,
name|Comparators
operator|.
name|getNamedObjectComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// validate -
comment|// current limitation is that an ObjRelationship must have source
comment|// and target entities present, with DbEntities chosen.
name|validateCanMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|targetCollections
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|targetCollections
operator|.
name|add
argument_list|(
name|COLLECTION_TYPE_COLLECTION
argument_list|)
expr_stmt|;
name|targetCollections
operator|.
name|add
argument_list|(
name|ObjRelationship
operator|.
name|DEFAULT_COLLECTION_TYPE
argument_list|)
expr_stmt|;
name|targetCollections
operator|.
name|add
argument_list|(
name|COLLECTION_TYPE_MAP
argument_list|)
expr_stmt|;
name|targetCollections
operator|.
name|add
argument_list|(
name|COLLECTION_TYPE_SET
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapKeys
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|initMapKeys
argument_list|()
expr_stmt|;
comment|// setup path
name|dbRelationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
name|relationship
operator|.
name|getDbRelationships
argument_list|()
argument_list|)
expr_stmt|;
comment|// this sets the right enabled state of collection type selectors
name|fireModelChange
argument_list|(
name|ModelChangeTypes
operator|.
name|VALUE_CHANGED
argument_list|,
name|DB_RELATIONSHIPS_SELECTOR
argument_list|)
expr_stmt|;
comment|// add dummy last relationship if we are not connected
name|connectEnds
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ObjRelationship
name|getRelationship
parameter_list|()
block|{
return|return
name|relationship
return|;
block|}
comment|/**      * @return list of DB Relationships current ObjRelationship is mapped to       */
specifier|public
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getDbRelationships
parameter_list|()
block|{
return|return
name|dbRelationships
return|;
block|}
comment|/**      * @return last relationship in the path, or<code>null</code> if path is empty      */
specifier|public
name|DbRelationship
name|getLastRelationship
parameter_list|()
block|{
return|return
name|dbRelationships
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|dbRelationships
operator|.
name|get
argument_list|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Sets list of DB Relationships current ObjRelationship is mapped to      */
specifier|public
name|void
name|setDbRelationships
parameter_list|(
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|rels
parameter_list|)
block|{
name|this
operator|.
name|dbRelationships
operator|=
name|rels
expr_stmt|;
block|}
comment|/**      * Returns currently selected target of the ObjRelationship.      */
specifier|public
name|ObjEntity
name|getObjectTarget
parameter_list|()
block|{
return|return
name|objectTarget
return|;
block|}
comment|/**      * Sets a new target      */
specifier|public
name|void
name|setObjectTarget
parameter_list|(
name|ObjEntity
name|objectTarget
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|objectTarget
operator|!=
name|objectTarget
condition|)
block|{
name|unlistenOldSubmodel
argument_list|(
name|OBJECT_TARGET_SELECTOR
argument_list|)
expr_stmt|;
name|this
operator|.
name|objectTarget
operator|=
name|objectTarget
expr_stmt|;
name|listenNewSubmodel
argument_list|(
name|OBJECT_TARGET_SELECTOR
argument_list|)
expr_stmt|;
name|fireModelChange
argument_list|(
name|ModelChangeTypes
operator|.
name|VALUE_CHANGED
argument_list|,
name|OBJECT_TARGET_SELECTOR
argument_list|)
expr_stmt|;
comment|// init available map keys
name|initMapKeys
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initMapKeys
parameter_list|()
block|{
name|this
operator|.
name|mapKeys
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mapKeys
operator|.
name|add
argument_list|(
name|DEFAULT_MAP_KEY
argument_list|)
expr_stmt|;
comment|/**          * Object target can be null when selected target DbEntity has no ObjEntities          */
if|if
condition|(
name|objectTarget
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Iterator
name|attributes
init|=
name|this
operator|.
name|objectTarget
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|attributes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjAttribute
name|attribute
init|=
operator|(
name|ObjAttribute
operator|)
name|attributes
operator|.
name|next
argument_list|()
decl_stmt|;
name|mapKeys
operator|.
name|add
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fireModelChange
argument_list|(
name|ModelChangeTypes
operator|.
name|VALUE_CHANGED
argument_list|,
name|MAP_KEYS_SELECTOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|mapKey
operator|!=
literal|null
operator|&&
operator|!
name|mapKeys
operator|.
name|contains
argument_list|(
name|mapKey
argument_list|)
condition|)
block|{
name|mapKey
operator|=
name|DEFAULT_MAP_KEY
expr_stmt|;
name|fireModelChange
argument_list|(
name|ModelChangeTypes
operator|.
name|VALUE_CHANGED
argument_list|,
name|MAP_KEY_SELECTOR
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a list of ObjEntities available for target mapping.      */
specifier|public
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|getObjectTargets
parameter_list|()
block|{
return|return
name|objectTargets
return|;
block|}
specifier|public
name|String
name|getRelationshipName
parameter_list|()
block|{
return|return
name|relationshipName
return|;
block|}
specifier|public
name|void
name|setRelationshipName
parameter_list|(
name|String
name|relationshipName
parameter_list|)
block|{
name|this
operator|.
name|relationshipName
operator|=
name|relationshipName
expr_stmt|;
block|}
comment|/**      * Processes relationship path when path component at index was changed.      */
specifier|public
specifier|synchronized
name|void
name|relationshipChanged
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// strip everything starting from the index
name|breakChain
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// connect the ends
name|connectEnds
argument_list|()
expr_stmt|;
comment|// must fire with null selector, or refresh won't happen
name|fireModelChange
argument_list|(
name|VALUE_CHANGED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isToMany
parameter_list|()
block|{
comment|// copied algorithm from ObjRelationship.calculateToMany(), only iterating through
comment|// the unsaved dbrels selection.
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelIterator
init|=
name|dbRelationships
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbRelIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|relationship
init|=
name|dbRelIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Stores current state of the model in the internal ObjRelationship.      */
specifier|public
specifier|synchronized
name|boolean
name|savePath
parameter_list|()
block|{
name|boolean
name|hasChanges
init|=
literal|false
decl_stmt|;
name|boolean
name|oldToMany
init|=
name|relationship
operator|.
name|isToMany
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|relationshipName
argument_list|)
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|relationship
operator|.
name|setName
argument_list|(
name|relationshipName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|DbEntity
name|lastEntity
init|=
operator|(
name|DbEntity
operator|)
name|dbRelationships
operator|.
name|get
argument_list|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|objectTarget
operator|==
literal|null
operator|||
name|objectTarget
operator|.
name|getDbEntity
argument_list|()
operator|!=
name|lastEntity
condition|)
block|{
comment|/**                  * Entities in combobox and path browser do not match.                  * In this case, we rely on the browser and automatically select one                  * of lastEntity's ObjEntities                  */
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
init|=
name|lastEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getMappedEntities
argument_list|(
name|lastEntity
argument_list|)
decl_stmt|;
name|objectTarget
operator|=
name|objEntities
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|objEntities
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|objectTarget
operator|==
literal|null
operator|||
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|objectTarget
operator|.
name|getName
argument_list|()
argument_list|,
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
comment|// note on events notification - this needs to be propagated
comment|// via old modeler events, but we leave this to the controller
comment|// since model knows nothing about Modeler mediator.
name|relationship
operator|.
name|setTargetEntity
argument_list|(
name|objectTarget
argument_list|)
expr_stmt|;
block|}
comment|// check for path modifications
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|oldPath
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldPath
operator|.
name|size
argument_list|()
operator|!=
name|dbRelationships
operator|.
name|size
argument_list|()
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|updatePath
argument_list|()
expr_stmt|;
block|}
else|else
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
name|oldPath
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbRelationship
name|next
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldPath
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|!=
name|next
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|updatePath
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
name|String
name|collectionType
init|=
name|ObjRelationship
operator|.
name|DEFAULT_COLLECTION_TYPE
operator|.
name|equals
argument_list|(
name|targetCollection
argument_list|)
operator|||
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
condition|?
literal|null
else|:
name|targetCollection
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|collectionType
argument_list|,
name|relationship
operator|.
name|getCollectionType
argument_list|()
argument_list|)
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|relationship
operator|.
name|setCollectionType
argument_list|(
name|collectionType
argument_list|)
expr_stmt|;
block|}
comment|// map key only makes sense for Map relationships
name|String
name|mapKey
init|=
name|COLLECTION_TYPE_MAP
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
operator|&&
operator|!
name|DEFAULT_MAP_KEY
operator|.
name|equals
argument_list|(
name|this
operator|.
name|mapKey
argument_list|)
condition|?
name|this
operator|.
name|mapKey
else|:
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|mapKey
argument_list|,
name|relationship
operator|.
name|getMapKey
argument_list|()
argument_list|)
condition|)
block|{
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|relationship
operator|.
name|setMapKey
argument_list|(
name|mapKey
argument_list|)
expr_stmt|;
block|}
comment|/**          * As of CAY-436 here we check if to-many property has changed during the editing,          * and if so, delete rule must be reset to default value          */
if|if
condition|(
name|hasChanges
operator|&&
name|relationship
operator|.
name|isToMany
argument_list|()
operator|!=
name|oldToMany
condition|)
block|{
name|DeleteRuleUpdater
operator|.
name|updateObjRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
return|return
name|hasChanges
return|;
block|}
specifier|private
name|void
name|updatePath
parameter_list|()
block|{
name|relationship
operator|.
name|clearDbRelationships
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|it
init|=
name|dbRelationships
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
name|Relationship
name|nextPathComponent
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|nextPathComponent
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|relationship
operator|.
name|addDbRelationship
argument_list|(
operator|(
name|DbRelationship
operator|)
name|nextPathComponent
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|breakChain
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// strip everything starting from the index
while|while
condition|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|>
operator|(
name|index
operator|+
literal|1
operator|)
condition|)
block|{
comment|// remove last
name|dbRelationships
operator|.
name|remove
argument_list|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Connects last selected DbRelationship in the path to the
comment|// last DbEntity, creating a dummy relationship if needed.
specifier|private
name|void
name|connectEnds
parameter_list|()
block|{
name|Relationship
name|last
init|=
literal|null
decl_stmt|;
name|int
name|size
init|=
name|dbRelationships
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|last
operator|=
name|dbRelationships
operator|.
name|get
argument_list|(
name|size
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|Entity
name|target
init|=
name|getEndEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
operator|(
name|last
operator|==
literal|null
operator|||
name|last
operator|.
name|getTargetEntity
argument_list|()
operator|!=
name|target
operator|)
condition|)
block|{
comment|// try to connect automatically, if we can't use dummy connector
name|Entity
name|source
init|=
operator|(
name|last
operator|==
literal|null
operator|)
condition|?
name|getStartEntity
argument_list|()
else|:
name|last
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
name|Relationship
name|anyConnector
init|=
name|source
operator|!=
literal|null
condition|?
name|source
operator|.
name|getAnyRelationship
argument_list|(
name|target
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|anyConnector
operator|!=
literal|null
condition|)
block|{
name|dbRelationships
operator|.
name|add
argument_list|(
operator|(
name|DbRelationship
operator|)
name|anyConnector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Checks if the entity can be edited with this inspector.      * NOTE: As of CAY-1077, relationship inspector can be opened even if no target entity       * was set.      */
specifier|private
name|void
name|validateCanMap
parameter_list|()
block|{
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't map relationship without source entity."
argument_list|)
throw|;
block|}
if|if
condition|(
name|getStartEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't map relationship without source DbEntity."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|DbEntity
name|getStartEntity
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|DbEntity
name|getEndEntity
parameter_list|()
block|{
comment|/**          * Object target can be null when selected target DbEntity has no ObjEntities          */
if|if
condition|(
name|objectTarget
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|objectTarget
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|String
name|getMapKey
parameter_list|()
block|{
return|return
name|mapKey
return|;
block|}
specifier|public
name|void
name|setMapKey
parameter_list|(
name|String
name|mapKey
parameter_list|)
block|{
name|this
operator|.
name|mapKey
operator|=
name|mapKey
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetCollection
parameter_list|()
block|{
return|return
name|targetCollection
return|;
block|}
specifier|public
name|void
name|setTargetCollection
parameter_list|(
name|String
name|targetCollection
parameter_list|)
block|{
name|this
operator|.
name|targetCollection
operator|=
name|targetCollection
expr_stmt|;
block|}
specifier|public
name|List
name|getMapKeys
parameter_list|()
block|{
return|return
name|mapKeys
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTargetCollections
parameter_list|()
block|{
return|return
name|targetCollections
return|;
block|}
block|}
end_class

end_unit

