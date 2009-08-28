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
name|Collections
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
name|ListIterator
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
name|exp
operator|.
name|ExpressionException
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
name|map
operator|.
name|event
operator|.
name|RelationshipEvent
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
name|CayenneMapEntry
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
name|ToStringBuilder
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
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_comment
comment|/**  * Describes an association between two Java classes mapped as source and target  * ObjEntity. Maps to a path of DbRelationships.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjRelationship
extends|extends
name|Relationship
block|{
comment|/**      * Denotes a default type of to-many relationship collection which is a Java List.      *       * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_COLLECTION_TYPE
init|=
literal|"java.util.List"
decl_stmt|;
name|boolean
name|readOnly
decl_stmt|;
specifier|protected
name|int
name|deleteRule
init|=
name|DeleteRule
operator|.
name|NO_ACTION
decl_stmt|;
specifier|protected
name|boolean
name|usedForLocking
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|/**      * Stores the type of collection mapped by a to-many relationship. Null for to-one      * relationships.      *       * @since 3.0      */
specifier|protected
name|String
name|collectionType
decl_stmt|;
comment|/**      * Stores a property name of a target entity used to create a relationship map. Only      * has effect if collectionType property is set to "java.util.Map".      *       * @since 3.0      */
specifier|protected
name|String
name|mapKey
decl_stmt|;
specifier|public
name|ObjRelationship
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjRelationship
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|ObjEntity
name|source
init|=
operator|(
name|ObjEntity
operator|)
name|getSourceEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|encoder
operator|.
name|print
argument_list|(
literal|"<obj-relationship name=\""
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"\" source=\""
operator|+
name|source
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// looking up a target entity ensures that bogus names are not saved... whether
comment|// this is good or bad is debatable, as users may want to point to non-existent
comment|// entities on purpose.
name|ObjEntity
name|target
init|=
operator|(
name|ObjEntity
operator|)
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" target=\""
operator|+
name|target
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCollectionType
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|DEFAULT_COLLECTION_TYPE
operator|.
name|equals
argument_list|(
name|getCollectionType
argument_list|()
argument_list|)
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" collection-type=\""
operator|+
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getMapKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" map-key=\""
operator|+
name|getMapKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isUsedForLocking
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" lock=\"true"
argument_list|)
expr_stmt|;
block|}
name|String
name|deleteRule
init|=
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|getDeleteRule
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getDeleteRule
argument_list|()
operator|!=
name|DeleteRule
operator|.
name|NO_ACTION
operator|&&
name|deleteRule
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" deleteRule=\""
operator|+
name|deleteRule
argument_list|)
expr_stmt|;
block|}
comment|// quietly get rid of invalid path... this is not the best way of doing things,
comment|// but it is consistent across map package
name|String
name|path
init|=
name|getValidRelationshipPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" db-relationship-path=\""
operator|+
name|path
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a target ObjEntity of this relationship. Entity is looked up in the parent      * DataMap using "targetEntityName".      */
annotation|@
name|Override
specifier|public
name|Entity
name|getTargetEntity
parameter_list|()
block|{
name|String
name|targetName
init|=
name|getTargetEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getNonNullNamespace
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|targetName
argument_list|)
return|;
block|}
comment|/**      * Returns the name of a complimentary relationship going in the opposite direction or      * null if it doesn't exist.      *       * @since 1.2      */
specifier|public
name|String
name|getReverseRelationshipName
parameter_list|()
block|{
name|ObjRelationship
name|reverse
init|=
name|getReverseRelationship
argument_list|()
decl_stmt|;
return|return
operator|(
name|reverse
operator|!=
literal|null
operator|)
condition|?
name|reverse
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Returns a "complimentary" ObjRelationship going in the opposite direction. Returns      * null if no such relationship is found.      */
specifier|public
name|ObjRelationship
name|getReverseRelationship
parameter_list|()
block|{
comment|// reverse the list
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
name|getDbRelationships
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|reversed
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
name|relationships
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbRelationship
name|rel
range|:
name|relationships
control|)
block|{
name|DbRelationship
name|reverse
init|=
name|rel
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverse
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|reversed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|reverse
argument_list|)
expr_stmt|;
block|}
name|ObjEntity
name|target
init|=
operator|(
name|ObjEntity
operator|)
name|this
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|target
operator|.
name|getRelationships
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
name|ObjRelationship
name|rel
init|=
operator|(
name|ObjRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|.
name|isSubentityOf
argument_list|(
operator|(
name|ObjEntity
operator|)
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|otherRels
init|=
name|rel
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|reversed
operator|.
name|size
argument_list|()
operator|!=
name|otherRels
operator|.
name|size
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|int
name|len
init|=
name|reversed
operator|.
name|size
argument_list|()
decl_stmt|;
name|boolean
name|relsMatch
init|=
literal|true
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|otherRels
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|!=
name|reversed
operator|.
name|get
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|relsMatch
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|relsMatch
condition|)
block|{
return|return
name|rel
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Creates a complimentary reverse relationship from target entity to the source      * entity. A new relationship is created regardless of whether one already exists.      * Returned relationship is not attached to the source entity and has no name. Throws      * a {@link CayenneRuntimeException} if reverse DbRelationship is not mapped.      *       * @since 3.0      */
specifier|public
name|ObjRelationship
name|createReverseRelationship
parameter_list|()
block|{
name|ObjRelationship
name|reverse
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|reverse
operator|.
name|setSourceEntity
argument_list|(
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
name|reverse
operator|.
name|setTargetEntityName
argument_list|(
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|reverse
operator|.
name|setDbRelationshipPath
argument_list|(
name|getReverseDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reverse
return|;
block|}
comment|/**      * Returns an immutable list of underlying DbRelationships.      */
specifier|public
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getDbRelationships
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|dbRelationships
argument_list|)
return|;
block|}
comment|/**      * Appends a DbRelationship to the existing list of DbRelationships.      */
specifier|public
name|void
name|addDbRelationship
parameter_list|(
name|DbRelationship
name|dbRel
parameter_list|)
block|{
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DbRelationship has no name"
argument_list|)
throw|;
block|}
comment|// Adding a second is creating a flattened relationship.
comment|// Ensure that the new relationship properly continues
comment|// on the flattened path
name|int
name|numDbRelationships
init|=
name|dbRelationships
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|numDbRelationships
operator|>
literal|0
condition|)
block|{
name|DbRelationship
name|lastRel
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
name|numDbRelationships
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lastRel
operator|.
name|getTargetEntityName
argument_list|()
operator|.
name|equals
argument_list|(
name|dbRel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error adding db relationship "
operator|+
name|dbRel
operator|+
literal|" to ObjRelationship "
operator|+
name|this
operator|+
literal|" because the source of the newly added relationship "
operator|+
literal|"is not the target of the previous relationship "
operator|+
literal|"in the chain"
argument_list|)
throw|;
block|}
block|}
name|dbRelationships
operator|.
name|add
argument_list|(
name|dbRel
argument_list|)
expr_stmt|;
name|this
operator|.
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
name|this
operator|.
name|recalculateToManyValue
argument_list|()
expr_stmt|;
block|}
comment|/**      * Removes the relationship<code>dbRel</code> from the list of relationships.      */
specifier|public
name|void
name|removeDbRelationship
parameter_list|(
name|DbRelationship
name|dbRel
parameter_list|)
block|{
if|if
condition|(
name|dbRelationships
operator|.
name|remove
argument_list|(
name|dbRel
argument_list|)
condition|)
block|{
name|this
operator|.
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
name|this
operator|.
name|recalculateToManyValue
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clearDbRelationships
parameter_list|()
block|{
name|this
operator|.
name|dbRelationships
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|readOnly
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|toMany
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Returns a boolean indicating whether the presence of a non-null source key(s) will      * not guarantee a presence of a target record. PK..FK relationships are all optional,      * but there are other more subtle cases, such as PK..PK, etc.      *       * @since 3.0      */
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
if|if
condition|(
name|isToMany
argument_list|()
operator|||
name|isFlattened
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// entities with qualifiers may result in filtering even existing target rows, so
comment|// such relationships are optional
if|if
condition|(
name|isQualifiedEntity
argument_list|(
operator|(
name|ObjEntity
operator|)
name|getTargetEntity
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|DbRelationship
name|dbRelationship
init|=
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// to-one mandatory relationships are either from non-PK or to master pk
if|if
condition|(
name|dbRelationship
operator|.
name|isToPK
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|dbRelationship
operator|.
name|isFromPK
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbRelationship
name|reverseRelationship
init|=
name|dbRelationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseRelationship
operator|.
name|isToDependentPK
argument_list|()
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
comment|/**      * Returns true if the entity or its super entities have a limiting qualifier.      */
specifier|private
name|boolean
name|isQualifiedEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|entity
operator|=
name|entity
operator|.
name|getSuperEntity
argument_list|()
expr_stmt|;
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
return|return
name|isQualifiedEntity
argument_list|(
name|entity
argument_list|)
return|;
block|}
comment|/**      * Returns a boolean indicating whether modifying a target of such relationship in any      * way will not change the underlying table row of the source.      *       * @since 1.1      */
specifier|public
name|boolean
name|isSourceIndependentFromTargetChange
parameter_list|()
block|{
comment|// note - call "isToPK" at the end of the chain, since
comment|// if it is to a dependent PK, we still should return true...
return|return
name|isToMany
argument_list|()
operator|||
name|isFlattened
argument_list|()
operator|||
name|isToDependentEntity
argument_list|()
operator|||
operator|!
name|isToPK
argument_list|()
return|;
block|}
comment|/**      * Returns true if underlying DbRelationships point to dependent entity.      */
specifier|public
name|boolean
name|isToDependentEntity
parameter_list|()
block|{
return|return
operator|(
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|isToDependentPK
argument_list|()
return|;
block|}
comment|/**      * Returns true if the underlying DbRelationships point to a at least one of the      * columns of the target entity.      *       * @since 1.1      */
specifier|public
name|boolean
name|isToPK
parameter_list|()
block|{
return|return
operator|(
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|isToPK
argument_list|()
return|;
block|}
comment|/**      * Returns true if the relationship is a "flattened" relationship. A relationship is      * considered "flattened" if it maps to more than one DbRelationship. Such chain of      * DbRelationships is also called "relationship path". All flattened relationships are      * at least readable, but only those formed across a many-many join table (with no      * custom attributes other than foreign keys) can be automatically written.      *       * @see #isReadOnly      * @return flag indicating if the relationship is flattened or not.      */
specifier|public
name|boolean
name|isFlattened
parameter_list|()
block|{
return|return
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
return|;
block|}
comment|/**      * Returns true if the relationship is flattened, but is not of the single case that      * can have automatic write support. Otherwise, it returns false.      *       * @return flag indicating if the relationship is read only or not      */
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
return|return
name|readOnly
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isToMany
parameter_list|()
block|{
name|recalculateToManyValue
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|isToMany
argument_list|()
return|;
block|}
comment|/**      * Returns the deleteRule. The delete rule is a constant from the DeleteRule class,      * and specifies what should happen to the destination object when the source object      * is deleted.      *       * @return int a constant from DeleteRule      * @see #setDeleteRule      */
specifier|public
name|int
name|getDeleteRule
parameter_list|()
block|{
return|return
name|deleteRule
return|;
block|}
comment|/**      * Sets the delete rule of the relationship.      *       * @param value New delete rule. Must be one of the constants defined in DeleteRule      *            class.      * @see DeleteRule      * @throws IllegalArgumentException if the value is not a valid delete rule.      */
specifier|public
name|void
name|setDeleteRule
parameter_list|(
name|int
name|value
parameter_list|)
block|{
if|if
condition|(
operator|(
name|value
operator|!=
name|DeleteRule
operator|.
name|CASCADE
operator|)
operator|&&
operator|(
name|value
operator|!=
name|DeleteRule
operator|.
name|DENY
operator|)
operator|&&
operator|(
name|value
operator|!=
name|DeleteRule
operator|.
name|NULLIFY
operator|)
operator|&&
operator|(
name|value
operator|!=
name|DeleteRule
operator|.
name|NO_ACTION
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delete rule value "
operator|+
name|value
operator|+
literal|" is not a constant from the DeleteRule class"
argument_list|)
throw|;
block|}
name|this
operator|.
name|deleteRule
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.0 as ObjRelationship no longer reacts to DbRelationship events.      */
annotation|@
name|Deprecated
specifier|public
name|void
name|dbRelationshipDidChange
parameter_list|(
name|RelationshipEvent
name|event
parameter_list|)
block|{
name|recalculateToManyValue
argument_list|()
expr_stmt|;
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns whether this attribute should be used for locking.      *       * @since 1.1      */
specifier|public
name|boolean
name|isUsedForLocking
parameter_list|()
block|{
return|return
name|usedForLocking
return|;
block|}
comment|/**      * Sets whether this attribute should be used for locking.      *       * @since 1.1      */
specifier|public
name|void
name|setUsedForLocking
parameter_list|(
name|boolean
name|usedForLocking
parameter_list|)
block|{
name|this
operator|.
name|usedForLocking
operator|=
name|usedForLocking
expr_stmt|;
block|}
comment|/**      * Returns a dot-separated path over mapped DbRelationships.      *       * @since 1.1      */
specifier|public
name|String
name|getDbRelationshipPath
parameter_list|()
block|{
comment|// build path on the fly
if|if
condition|(
name|getDbRelationships
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|it
init|=
name|getDbRelationships
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
name|DbRelationship
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|next
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a reversed dbRelationship path.      *       * @since 1.2      */
specifier|public
name|String
name|getReverseDbRelationshipPath
parameter_list|()
throws|throws
name|ExpressionException
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationships
operator|==
literal|null
operator|||
name|relationships
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// iterate in reverse order
name|ListIterator
argument_list|<
name|DbRelationship
argument_list|>
name|it
init|=
name|relationships
operator|.
name|listIterator
argument_list|(
name|relationships
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|DbRelationship
name|relationship
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
name|DbRelationship
name|reverse
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
comment|// another sanity check
if|if
condition|(
name|reverse
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No reverse relationship exist for "
operator|+
name|relationship
argument_list|)
throw|;
block|}
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|reverse
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Sets mapped DbRelationships as a dot-separated path.      */
specifier|public
name|void
name|setDbRelationshipPath
parameter_list|(
name|String
name|relationshipPath
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getDbRelationshipPath
argument_list|()
argument_list|,
name|relationshipPath
argument_list|)
condition|)
block|{
name|refreshFromPath
argument_list|(
name|relationshipPath
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns dot-separated path over DbRelationships, only including components that      * have valid DbRelationships.      */
name|String
name|getValidRelationshipPath
parameter_list|()
block|{
name|String
name|path
init|=
name|getDbRelationshipPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|getSourceEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't resolve DbRelationships, null source ObjEntity"
argument_list|)
throw|;
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
literal|null
return|;
block|}
name|StringBuilder
name|validPath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|pathComponent
range|:
name|dbEntity
operator|.
name|resolvePath
argument_list|(
operator|new
name|ASTDbPath
argument_list|(
name|path
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|validPath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|validPath
operator|.
name|append
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|validPath
operator|.
name|append
argument_list|(
name|pathComponent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
block|}
return|return
name|validPath
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Rebuild a list of relationships if String relationshipPath has changed.      */
specifier|final
name|void
name|refreshFromPath
parameter_list|(
name|String
name|dbRelationshipPath
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|// remove existing relationships
name|dbRelationships
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|dbRelationshipPath
operator|!=
literal|null
condition|)
block|{
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|getSourceEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't resolve DbRelationships, null source ObjEntity"
argument_list|)
throw|;
block|}
try|try
block|{
comment|// add new relationships from path
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|it
init|=
name|entity
operator|.
name|resolvePathComponents
argument_list|(
operator|new
name|ASTDbPath
argument_list|(
name|dbRelationshipPath
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|dbRelationships
operator|.
name|add
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
name|recalculateToManyValue
argument_list|()
expr_stmt|;
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Recalculates whether a relationship is toMany or toOne, based on the underlying db      * relationships.      */
specifier|public
name|void
name|recalculateToManyValue
parameter_list|()
block|{
comment|// If there is a single toMany along the path, then the flattend
comment|// rel is toMany. If all are toOne, then the rel is toOne.
comment|// Simple (non-flattened) relationships form the degenerate case
comment|// taking the value of the single underlying dbrel.
for|for
control|(
name|DbRelationship
name|thisRel
range|:
name|this
operator|.
name|dbRelationships
control|)
block|{
if|if
condition|(
name|thisRel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|this
operator|.
name|toMany
operator|=
literal|true
expr_stmt|;
return|return;
block|}
block|}
name|this
operator|.
name|toMany
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Recalculates a new readonly value based on the underlying DbRelationships.      */
specifier|public
name|void
name|recalculateReadOnlyValue
parameter_list|()
block|{
comment|// not flattened, always read/write
if|if
condition|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
name|this
operator|.
name|readOnly
operator|=
literal|false
expr_stmt|;
return|return;
block|}
comment|// too long, can't handle this yet
if|if
condition|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|>
literal|2
condition|)
block|{
name|this
operator|.
name|readOnly
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|DbRelationship
name|firstRel
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DbRelationship
name|secondRel
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// only support many-to-many with single-step join
if|if
condition|(
operator|!
name|firstRel
operator|.
name|isToMany
argument_list|()
operator|||
name|secondRel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|this
operator|.
name|readOnly
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
name|firstRel
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" could not obtain a DataMap for the destination of "
operator|+
name|firstRel
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// allow modifications if the joins are from FKs
if|if
condition|(
operator|!
name|secondRel
operator|.
name|isToPK
argument_list|()
condition|)
block|{
name|this
operator|.
name|readOnly
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|DbRelationship
name|firstReverseRel
init|=
name|firstRel
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|firstReverseRel
operator|==
literal|null
operator|||
operator|!
name|firstReverseRel
operator|.
name|isToPK
argument_list|()
condition|)
block|{
name|this
operator|.
name|readOnly
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|readOnly
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"dbRelationshipPath"
argument_list|,
name|getDbRelationshipPath
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns an ObjAttribute stripped of any server-side information, such as      * DbAttribute mapping.      *       * @since 1.2      */
specifier|public
name|ObjRelationship
name|getClientRelationship
parameter_list|()
block|{
name|ObjRelationship
name|reverse
init|=
name|getReverseRelationship
argument_list|()
decl_stmt|;
name|String
name|reverseName
init|=
name|reverse
operator|!=
literal|null
condition|?
name|reverse
operator|.
name|getName
argument_list|()
else|:
literal|null
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ClientObjRelationship
argument_list|(
name|getName
argument_list|()
argument_list|,
name|reverseName
argument_list|,
name|isToMany
argument_list|()
argument_list|,
name|isReadOnly
argument_list|()
argument_list|)
decl_stmt|;
name|relationship
operator|.
name|setTargetEntityName
argument_list|(
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|getDeleteRule
argument_list|()
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setCollectionType
argument_list|(
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: copy locking flag...
return|return
name|relationship
return|;
block|}
comment|/**      * Returns the interface of collection mapped by a to-many relationship. Returns null      * for to-one relationships. Default for to-many is "java.util.List". Other possible      * values are "java.util.Set", "java.util.Collection", "java.util.Map".      *       * @since 3.0      */
specifier|public
name|String
name|getCollectionType
parameter_list|()
block|{
if|if
condition|(
name|collectionType
operator|!=
literal|null
condition|)
block|{
return|return
name|collectionType
return|;
block|}
return|return
name|isToMany
argument_list|()
condition|?
name|DEFAULT_COLLECTION_TYPE
else|:
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setCollectionType
parameter_list|(
name|String
name|collectionType
parameter_list|)
block|{
name|this
operator|.
name|collectionType
operator|=
name|collectionType
expr_stmt|;
block|}
comment|/**      * Returns a property name of a target entity used to create a relationship map. Only      * has effect if collectionType property is set to "java.util.Map".      *       * @return The attribute name used for the map key or<code>null</code> if the      *         default (PK) is used as the map key.      * @since 3.0      */
specifier|public
name|String
name|getMapKey
parameter_list|()
block|{
return|return
name|mapKey
return|;
block|}
comment|/**      * @since 3.0      */
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
block|}
end_class

end_unit

