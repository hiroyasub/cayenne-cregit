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
name|configuration
operator|.
name|ConfigurationNode
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * A DbRelationship is a descriptor of a database inter-table relationship based  * on one or more primary key/foreign key pairs.  */
end_comment

begin_class
specifier|public
class|class
name|DbRelationship
extends|extends
name|Relationship
implements|implements
name|ConfigurationNode
block|{
comment|// The columns through which the join is implemented.
specifier|protected
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// Is relationship from source to target points to dependent primary
comment|// key (primary key column of destination table that is also a FK to the
comment|// source
comment|// column)
specifier|protected
name|boolean
name|toDependentPK
decl_stmt|;
specifier|public
name|DbRelationship
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DbRelationship
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
annotation|@
name|Override
specifier|public
name|DbEntity
name|getSourceEntity
parameter_list|()
block|{
return|return
operator|(
name|DbEntity
operator|)
name|super
operator|.
name|getSourceEntity
argument_list|()
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitDbRelationship
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"db-relationship"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"source"
argument_list|,
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getTargetEntityName
argument_list|()
operator|!=
literal|null
operator|&&
name|getTargetEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|attribute
argument_list|(
literal|"target"
argument_list|,
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|attribute
argument_list|(
literal|"toDependentPK"
argument_list|,
name|isToDependentPK
argument_list|()
operator|&&
name|isValidForDepPk
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|attribute
argument_list|(
literal|"toMany"
argument_list|,
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|nested
argument_list|(
name|getJoins
argument_list|()
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|visitDbRelationship
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns a target of this relationship. If relationship is not attached to      * a DbEntity, and DbEntity doesn't have a namespace, and exception is      * thrown.      */
annotation|@
name|Override
specifier|public
name|DbEntity
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
name|getDbEntity
argument_list|(
name|targetName
argument_list|)
return|;
block|}
comment|/**      * Returns a Collection of target attributes.      *       * @since 1.1      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|getTargetAttributes
parameter_list|()
block|{
if|if
condition|(
name|joins
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|joins
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|DbJoin
operator|::
name|getTarget
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns a Collection of source attributes.      *       * @since 1.1      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|getSourceAttributes
parameter_list|()
block|{
if|if
condition|(
name|joins
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|joins
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|DbJoin
operator|::
name|getSource
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates a new relationship with the same set of joins, but going in the      * opposite direction.      *       * @since 1.0.5      */
specifier|public
name|DbRelationship
name|createReverseRelationship
parameter_list|()
block|{
name|DbEntity
name|targetEntity
init|=
operator|(
name|DbEntity
operator|)
name|getTargetEntity
argument_list|()
decl_stmt|;
name|DbRelationship
name|reverse
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|reverse
operator|.
name|setSourceEntity
argument_list|(
name|targetEntity
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
comment|// TODO: andrus 12/24/2007 - one more case to handle - set reverse
comment|// toDepPK = true
comment|// if this relationship toDepPK is false, but the entities are joined on
comment|// a PK...
comment|// on the other hand, these can still be two independent entities...
if|if
condition|(
name|isToDependentPK
argument_list|()
operator|&&
operator|!
name|toMany
operator|&&
name|joins
operator|.
name|size
argument_list|()
operator|==
name|targetEntity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|reverse
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reverse
operator|.
name|setToMany
argument_list|(
operator|!
name|toMany
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
name|DbJoin
name|reverseJoin
init|=
name|join
operator|.
name|createReverseJoin
argument_list|()
decl_stmt|;
name|reverseJoin
operator|.
name|setRelationship
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
name|reverse
operator|.
name|addJoin
argument_list|(
name|reverseJoin
argument_list|)
expr_stmt|;
block|}
return|return
name|reverse
return|;
block|}
comment|/**      * Returns DbRelationship that is the opposite of this DbRelationship. This      * means a relationship from this target entity to this source entity with      * the same join semantics. Returns null if no such relationship exists.      */
specifier|public
name|DbRelationship
name|getReverseRelationship
parameter_list|()
block|{
name|DbEntity
name|target
init|=
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
name|Entity
name|src
init|=
name|this
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
comment|// special case - relationship to self with no joins...
if|if
condition|(
name|target
operator|==
name|src
operator|&&
name|joins
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TestJoin
name|testJoin
init|=
operator|new
name|TestJoin
argument_list|(
name|this
argument_list|)
decl_stmt|;
for|for
control|(
name|DbRelationship
name|rel
range|:
name|target
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|rel
operator|.
name|getTargetEntity
argument_list|()
operator|!=
name|src
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|DbJoin
argument_list|>
name|otherJoins
init|=
name|rel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
if|if
condition|(
name|otherJoins
operator|.
name|size
argument_list|()
operator|!=
name|joins
operator|.
name|size
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|boolean
name|joinsMatch
init|=
literal|true
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|otherJoins
control|)
block|{
comment|// flip join and try to find similar
name|testJoin
operator|.
name|setSourceName
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
name|testJoin
operator|.
name|setTargetName
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|joins
operator|.
name|contains
argument_list|(
name|testJoin
argument_list|)
condition|)
block|{
name|joinsMatch
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|joinsMatch
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
comment|/**      * Returns true if the relationship points to at least one of the PK columns      * of the target entity.      *       * @since 1.1      */
specifier|public
name|boolean
name|isToPK
parameter_list|()
block|{
for|for
control|(
name|DbJoin
name|join
range|:
name|getJoins
argument_list|()
control|)
block|{
name|DbAttribute
name|target
init|=
name|join
operator|.
name|getTarget
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
literal|false
return|;
block|}
if|if
condition|(
name|target
operator|.
name|isPrimaryKey
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
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|isFromPK
parameter_list|()
block|{
for|for
control|(
name|DbJoin
name|join
range|:
name|getJoins
argument_list|()
control|)
block|{
name|DbAttribute
name|source
init|=
name|join
operator|.
name|getSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|source
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
name|source
operator|.
name|isPrimaryKey
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
comment|/**      * Returns<code>true</code> if a method<code>isToDependentPK</code> of      * reverse relationship of this relationship returns<code>true</code>.      */
specifier|public
name|boolean
name|isToMasterPK
parameter_list|()
block|{
if|if
condition|(
name|isToMany
argument_list|()
operator|||
name|isToDependentPK
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbRelationship
name|revRel
init|=
name|getReverseRelationship
argument_list|()
decl_stmt|;
return|return
name|revRel
operator|!=
literal|null
operator|&&
name|revRel
operator|.
name|isToDependentPK
argument_list|()
return|;
block|}
comment|/**      * Returns a boolean indicating whether modifying a target of such      * relationship in any way will not change the underlying table row of the      * source.      *       * @since 4.0      */
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
name|isToDependentPK
argument_list|()
operator|||
operator|!
name|isToPK
argument_list|()
return|;
block|}
comment|/**      * Returns<code>true</code> if relationship from source to target points to      * dependent primary key. Dependent PK is a primary key column of the      * destination table that is also a FK to the source column.      */
specifier|public
name|boolean
name|isToDependentPK
parameter_list|()
block|{
return|return
name|toDependentPK
return|;
block|}
specifier|public
name|void
name|setToDependentPK
parameter_list|(
name|boolean
name|toDependentPK
parameter_list|)
block|{
name|this
operator|.
name|toDependentPK
operator|=
name|toDependentPK
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|boolean
name|isValidForDepPk
parameter_list|()
block|{
comment|// handle case with no joins
if|if
condition|(
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbJoin
name|join
range|:
name|getJoins
argument_list|()
control|)
block|{
name|DbAttribute
name|target
init|=
name|join
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|DbAttribute
name|source
init|=
name|join
operator|.
name|getSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
operator|!
name|target
operator|.
name|isPrimaryKey
argument_list|()
operator|||
name|source
operator|!=
literal|null
operator|&&
operator|!
name|source
operator|.
name|isPrimaryKey
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
comment|/**      * Returns a list of joins. List is returned by reference, so any      * modifications of the list will affect this relationship.      */
specifier|public
name|List
argument_list|<
name|DbJoin
argument_list|>
name|getJoins
parameter_list|()
block|{
return|return
name|joins
return|;
block|}
comment|/**      * Adds a join.      *       * @since 1.1      */
specifier|public
name|void
name|addJoin
parameter_list|(
name|DbJoin
name|join
parameter_list|)
block|{
if|if
condition|(
name|join
operator|!=
literal|null
condition|)
block|{
name|joins
operator|.
name|add
argument_list|(
name|join
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeJoin
parameter_list|(
name|DbJoin
name|join
parameter_list|)
block|{
name|joins
operator|.
name|remove
argument_list|(
name|join
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeAllJoins
parameter_list|()
block|{
name|joins
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setJoins
parameter_list|(
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|newJoins
parameter_list|)
block|{
name|this
operator|.
name|removeAllJoins
argument_list|()
expr_stmt|;
if|if
condition|(
name|newJoins
operator|!=
literal|null
condition|)
block|{
name|joins
operator|.
name|addAll
argument_list|(
name|newJoins
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a snapshot of primary key attributes of a target object of this      * relationship based on a snapshot of a source. Only "to-one" relationships      * are supported. Returns null if relationship does not point to an object.      * Throws CayenneRuntimeException if relationship is "to many" or if      * snapshot is missing id components.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|targetPkSnapshotWithSrcSnapshot
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|srcSnapshot
parameter_list|)
block|{
if|if
condition|(
name|isToMany
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only 'to one' relationships support this method."
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
decl_stmt|;
name|int
name|numJoins
init|=
name|joins
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|foundNulls
init|=
literal|0
decl_stmt|;
comment|// optimize for the most common single column join
if|if
condition|(
name|numJoins
operator|==
literal|1
condition|)
block|{
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|srcSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|foundNulls
operator|++
expr_stmt|;
name|idMap
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|idMap
operator|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
comment|// handle generic case: numJoins> 1
else|else
block|{
name|idMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|numJoins
operator|*
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
name|DbAttribute
name|source
init|=
name|join
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Object
name|val
init|=
name|srcSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
comment|// some keys may be nulls and some not in case of multi-key
comment|// relationships where PK and FK partially overlap (see
comment|// CAY-284)
if|if
condition|(
operator|!
name|source
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|foundNulls
operator|++
expr_stmt|;
block|}
else|else
block|{
name|idMap
operator|.
name|put
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|foundNulls
operator|==
literal|0
condition|)
block|{
return|return
name|idMap
return|;
block|}
if|else if
condition|(
name|foundNulls
operator|==
name|numJoins
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Some parts of FK are missing in snapshot, relationship: %s"
argument_list|,
name|this
argument_list|)
throw|;
block|}
block|}
comment|/**      * Common code to srcSnapshotWithTargetSnapshot. Both are functionally the      * same, except for the name, and whether they operate on a toMany or a      * toOne.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|srcSnapshotWithTargetSnapshot
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|targetSnapshot
parameter_list|)
block|{
name|int
name|len
init|=
name|joins
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// optimize for the most common single column join
if|if
condition|(
name|len
operator|==
literal|1
condition|)
block|{
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|targetSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|,
name|val
argument_list|)
return|;
block|}
comment|// general case
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|len
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
name|Object
name|val
init|=
name|targetSnapshot
operator|.
name|get
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
decl_stmt|;
name|idMap
operator|.
name|put
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|idMap
return|;
block|}
comment|/**      * Creates a snapshot of foreign key attributes of a source object of this      * relationship based on a snapshot of a target. Only "to-one" relationships      * are supported. Throws CayenneRuntimeException if relationship is      * "to many".      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|srcFkSnapshotWithTargetSnapshot
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|targetSnapshot
parameter_list|)
block|{
if|if
condition|(
name|isToMany
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only 'to one' relationships support this method."
argument_list|)
throw|;
block|}
return|return
name|srcSnapshotWithTargetSnapshot
argument_list|(
name|targetSnapshot
argument_list|)
return|;
block|}
comment|/**      * Creates a snapshot of primary key attributes of a source object of this      * relationship based on a snapshot of a target. Only "to-many"      * relationships are supported. Throws CayenneRuntimeException if      * relationship is "to one".      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|srcPkSnapshotWithTargetSnapshot
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|targetSnapshot
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isToMany
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only 'to many' relationships support this method."
argument_list|)
throw|;
block|}
return|return
name|srcSnapshotWithTargetSnapshot
argument_list|(
name|targetSnapshot
argument_list|)
return|;
block|}
comment|/**      * Sets relationship multiplicity.      */
specifier|public
name|void
name|setToMany
parameter_list|(
name|boolean
name|toMany
parameter_list|)
block|{
name|this
operator|.
name|toMany
operator|=
name|toMany
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isMandatory
parameter_list|()
block|{
for|for
control|(
name|DbJoin
name|join
range|:
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
operator|.
name|isMandatory
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
comment|// a join used for comparison
specifier|static
specifier|final
class|class
name|TestJoin
extends|extends
name|DbJoin
block|{
name|TestJoin
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|super
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
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
name|o
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
operator|!
operator|(
name|o
operator|instanceof
name|DbJoin
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbJoin
name|j
init|=
operator|(
name|DbJoin
operator|)
name|o
decl_stmt|;
return|return
name|j
operator|.
name|relationship
operator|==
name|this
operator|.
name|relationship
operator|&&
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|j
operator|.
name|sourceName
argument_list|,
name|this
operator|.
name|sourceName
argument_list|)
operator|&&
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|j
operator|.
name|targetName
argument_list|,
name|this
operator|.
name|targetName
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Db Relationship : "
argument_list|)
decl_stmt|;
name|res
operator|.
name|append
argument_list|(
name|toMany
condition|?
literal|"toMany"
else|:
literal|"toOne "
argument_list|)
expr_stmt|;
name|String
name|sourceEntityName
init|=
name|getSourceEntityName
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|sourceEntityName
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|targetEntityName
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|res
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getSourceEntityName
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|sourceEntity
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|this
operator|.
name|sourceEntity
operator|.
name|name
return|;
block|}
block|}
end_class

end_unit

