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
name|util
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
name|dba
operator|.
name|TypesMapping
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
name|DataMap
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
name|DbAttribute
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
name|Entity
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
name|map
operator|.
name|naming
operator|.
name|LegacyNameGenerator
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
name|naming
operator|.
name|DefaultUniqueNameGenerator
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
name|naming
operator|.
name|NameCheckers
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
name|naming
operator|.
name|ObjectNameGenerator
import|;
end_import

begin_comment
comment|/**  * Implements methods for entity merging.  */
end_comment

begin_class
specifier|public
class|class
name|EntityMergeSupport
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CLASS_TO_PRIMITIVE
decl_stmt|;
static|static
block|{
name|CLASS_TO_PRIMITIVE
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"byte"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"long"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"double"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"boolean"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"float"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"short"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"int"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|DataMap
name|map
decl_stmt|;
specifier|protected
name|boolean
name|removeMeaningfulFKs
decl_stmt|;
specifier|protected
name|boolean
name|removeMeaningfulPKs
decl_stmt|;
specifier|protected
name|boolean
name|usePrimitives
decl_stmt|;
comment|/**      * Strategy for choosing names for entities, attributes and relationships      */
specifier|private
specifier|final
name|ObjectNameGenerator
name|nameGenerator
decl_stmt|;
comment|/**      * Listeners of merge process.      */
specifier|private
specifier|final
name|List
argument_list|<
name|EntityMergeListener
argument_list|>
name|listeners
init|=
operator|new
name|ArrayList
argument_list|<
name|EntityMergeListener
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EntityMergeSupport
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|this
argument_list|(
name|map
argument_list|,
operator|new
name|LegacyNameGenerator
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|EntityMergeSupport
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ObjectNameGenerator
name|nameGenerator
parameter_list|,
name|boolean
name|removeMeaningfulPKs
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|nameGenerator
operator|=
name|nameGenerator
expr_stmt|;
name|this
operator|.
name|removeMeaningfulFKs
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|removeMeaningfulPKs
operator|=
name|removeMeaningfulPKs
expr_stmt|;
comment|/**          * Adding a listener, so that all created ObjRelationships would have          * default delete rule          */
name|addEntityMergeListener
argument_list|(
name|DeleteRuleUpdater
operator|.
name|getEntityMergeListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates each one of the collection of ObjEntities, adding attributes and      * relationships based on the current state of its DbEntity.      *       * @return true if any ObjEntity has changed as a result of synchronization.      * @since 1.2 changed signature to use Collection instead of List.      */
specifier|public
name|boolean
name|synchronizeWithDbEntities
parameter_list|(
name|Iterable
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ObjEntity
name|nextEntity
range|:
name|objEntities
control|)
block|{
if|if
condition|(
name|synchronizeWithDbEntity
argument_list|(
name|nextEntity
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
comment|/**      * @since 4.0      */
specifier|protected
name|boolean
name|removePK
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
return|return
name|removeMeaningfulPKs
return|;
block|}
comment|/**      * @since 4.0      */
specifier|protected
name|boolean
name|removeFK
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
return|return
name|removeMeaningfulFKs
return|;
block|}
comment|/**      * Updates ObjEntity attributes and relationships based on the current state      * of its DbEntity.      *       * @return true if the ObjEntity has changed as a result of synchronization.      */
specifier|public
name|boolean
name|synchronizeWithDbEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbEntity
name|dbEntity
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
comment|// synchronization on DataMap is some (weak) protection
comment|// against simultaneous modification of the map (like double-clicking on sync button)
synchronized|synchronized
init|(
name|map
init|)
block|{
if|if
condition|(
name|removeFK
argument_list|(
name|dbEntity
argument_list|)
condition|)
block|{
name|changed
operator|=
name|getRidOfAttributesThatAreNowSrcAttributesForRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|changed
operator||=
name|addMissingAttributes
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|changed
operator||=
name|addMissingRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
specifier|private
name|boolean
name|addMissingRelationships
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dr
range|:
name|getRelationshipsToAdd
argument_list|(
name|entity
argument_list|)
control|)
block|{
name|DbEntity
name|targetEntity
init|=
name|dr
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|Entity
name|mappedTarget
range|:
name|map
operator|.
name|getMappedEntities
argument_list|(
name|targetEntity
argument_list|)
control|)
block|{
comment|// avoid duplicate names
name|String
name|relationshipName
init|=
name|nameGenerator
operator|.
name|createObjRelationshipName
argument_list|(
name|dr
argument_list|)
decl_stmt|;
name|relationshipName
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objRelationship
argument_list|,
name|entity
argument_list|,
name|relationshipName
argument_list|)
expr_stmt|;
name|ObjRelationship
name|or
init|=
operator|new
name|ObjRelationship
argument_list|(
name|relationshipName
argument_list|)
decl_stmt|;
name|or
operator|.
name|addDbRelationship
argument_list|(
name|dr
argument_list|)
expr_stmt|;
name|or
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|or
operator|.
name|setTargetEntity
argument_list|(
name|mappedTarget
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
name|or
argument_list|)
expr_stmt|;
name|fireRelationshipAdded
argument_list|(
name|or
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
specifier|private
name|boolean
name|addMissingAttributes
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|da
range|:
name|getAttributesToAdd
argument_list|(
name|entity
argument_list|)
control|)
block|{
name|String
name|attrName
init|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
argument_list|,
name|entity
argument_list|,
name|nameGenerator
operator|.
name|createObjAttributeName
argument_list|(
name|da
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|da
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|usePrimitives
condition|)
block|{
name|String
name|primitive
init|=
name|CLASS_TO_PRIMITIVE
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|primitive
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|primitive
expr_stmt|;
block|}
block|}
name|ObjAttribute
name|oa
init|=
operator|new
name|ObjAttribute
argument_list|(
name|attrName
argument_list|,
name|type
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|oa
operator|.
name|setDbAttributePath
argument_list|(
name|da
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|oa
argument_list|)
expr_stmt|;
name|fireAttributeAdded
argument_list|(
name|oa
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
specifier|private
name|boolean
name|getRidOfAttributesThatAreNowSrcAttributesForRelationships
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|da
range|:
name|getMeaningfulFKs
argument_list|(
name|entity
argument_list|)
control|)
block|{
name|ObjAttribute
name|oa
init|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
decl_stmt|;
while|while
condition|(
name|oa
operator|!=
literal|null
condition|)
block|{
name|String
name|attrName
init|=
name|oa
operator|.
name|getName
argument_list|()
decl_stmt|;
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrName
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
name|oa
operator|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
comment|/**      * Returns a list of DbAttributes that are mapped to foreign keys.      *       * @since 1.2      */
specifier|public
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|getMeaningfulFKs
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|fks
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|property
range|:
name|objEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|DbAttribute
name|column
init|=
name|property
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
comment|// check if adding it makes sense at all
if|if
condition|(
name|column
operator|!=
literal|null
operator|&&
name|column
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|fks
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fks
return|;
block|}
comment|/**      * Returns a list of attributes that exist in the DbEntity, but are missing      * from the ObjEntity.      */
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getAttributesToAdd
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|DbEntity
name|dbEntity
init|=
name|objEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|missing
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|rels
init|=
name|dbEntity
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incomingRels
init|=
name|getIncomingRelationships
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|dba
range|:
name|dbEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|dba
operator|.
name|getName
argument_list|()
operator|==
literal|null
operator|||
name|objEntity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|dba
argument_list|)
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|boolean
name|removeMeaningfulPKs
init|=
name|removePK
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|removeMeaningfulPKs
operator|&&
name|dba
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
continue|continue;
block|}
comment|// check FK's
name|boolean
name|isFK
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|rit
init|=
name|rels
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|isFK
operator|&&
name|rit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
name|rit
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|join
operator|.
name|getSource
argument_list|()
operator|==
name|dba
condition|)
block|{
name|isFK
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|removeMeaningfulPKs
condition|)
block|{
if|if
condition|(
operator|!
name|dba
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|isFK
condition|)
block|{
continue|continue;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isFK
condition|)
block|{
continue|continue;
block|}
block|}
comment|// check incoming relationships
name|rit
operator|=
name|incomingRels
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|isFK
operator|&&
name|rit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
name|rit
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|join
operator|.
name|getTarget
argument_list|()
operator|==
name|dba
condition|)
block|{
name|isFK
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|removeMeaningfulPKs
condition|)
block|{
if|if
condition|(
operator|!
name|dba
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|isFK
condition|)
block|{
continue|continue;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isFK
condition|)
block|{
continue|continue;
block|}
block|}
name|missing
operator|.
name|add
argument_list|(
name|dba
argument_list|)
expr_stmt|;
block|}
return|return
name|missing
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|getIncomingRelationships
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incoming
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|nextEntity
range|:
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|nextEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|entity
operator|==
name|relationship
operator|.
name|getTargetEntity
argument_list|()
condition|)
block|{
name|incoming
operator|.
name|add
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|incoming
return|;
block|}
specifier|protected
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getRelationshipsToAdd
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|missing
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dbRel
range|:
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
control|)
block|{
comment|// check if adding it makes sense at all
if|if
condition|(
name|dbRel
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|objEntity
operator|.
name|getRelationshipForDbRelationship
argument_list|(
name|dbRel
argument_list|)
operator|==
literal|null
condition|)
block|{
name|missing
operator|.
name|add
argument_list|(
name|dbRel
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|missing
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|boolean
name|isRemoveMeaningfulFKs
parameter_list|()
block|{
return|return
name|removeMeaningfulFKs
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setRemoveMeaningfulFKs
parameter_list|(
name|boolean
name|removeMeaningfulFKs
parameter_list|)
block|{
name|this
operator|.
name|removeMeaningfulFKs
operator|=
name|removeMeaningfulFKs
expr_stmt|;
block|}
comment|/**      * Registers new EntityMergeListener      */
specifier|public
name|void
name|addEntityMergeListener
parameter_list|(
name|EntityMergeListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unregisters an EntityMergeListener      */
specifier|public
name|void
name|removeEntityMergeListener
parameter_list|(
name|EntityMergeListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns registered listeners      */
specifier|public
name|EntityMergeListener
index|[]
name|getEntityMergeListeners
parameter_list|()
block|{
return|return
name|listeners
operator|.
name|toArray
argument_list|(
operator|new
name|EntityMergeListener
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|/**      * Notifies all listeners that an ObjAttribute was added      */
specifier|protected
name|void
name|fireAttributeAdded
parameter_list|(
name|ObjAttribute
name|attr
parameter_list|)
block|{
for|for
control|(
name|EntityMergeListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|objAttributeAdded
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Notifies all listeners that an ObjRelationship was added      */
specifier|protected
name|void
name|fireRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
for|for
control|(
name|EntityMergeListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|objRelationshipAdded
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return naming strategy for reverse engineering      */
specifier|public
name|ObjectNameGenerator
name|getNameGenerator
parameter_list|()
block|{
return|return
name|nameGenerator
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|boolean
name|isUsePrimitives
parameter_list|()
block|{
return|return
name|usePrimitives
return|;
block|}
comment|/**      * @since 4.0      * @param usePrimitives      */
specifier|public
name|void
name|setUsePrimitives
parameter_list|(
name|boolean
name|usePrimitives
parameter_list|)
block|{
name|this
operator|.
name|usePrimitives
operator|=
name|usePrimitives
expr_stmt|;
block|}
block|}
end_class

end_unit

