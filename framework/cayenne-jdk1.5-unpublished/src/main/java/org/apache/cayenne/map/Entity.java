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
name|Serializable
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Expression
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
name|XMLSerializable
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
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
import|;
end_import

begin_comment
comment|/**  * An Entity is an abstract descriptor for an entity mapping concept. Entity can represent  * either a descriptor of database table or a persistent object.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Entity
implements|implements
name|CayenneMapEntry
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PATH_SEPARATOR
init|=
literal|"."
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|attributes
decl_stmt|;
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Relationship
argument_list|>
name|relationships
decl_stmt|;
comment|/**      * Creates an unnamed Entity.      */
specifier|public
name|Entity
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a named Entity.      */
specifier|public
name|Entity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|attributes
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
argument_list|()
expr_stmt|;
name|relationships
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Relationship
argument_list|>
argument_list|()
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
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
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns entity name. Name is a unique identifier of the entity within its DataMap.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|getDataMap
argument_list|()
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|parent
operator|instanceof
name|DataMap
operator|)
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected null or DataMap, got: "
operator|+
name|parent
argument_list|)
throw|;
name|setDataMap
argument_list|(
operator|(
name|DataMap
operator|)
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return parent DataMap of this entity.      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * Sets parent DataMap of this entity.      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/**      * Returns attribute with name<code>attributeName</code> or null if no attribute      * with this name exists.      */
specifier|public
name|Attribute
name|getAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
comment|/**      * Adds new attribute to the entity, setting its parent entity to be this object. If      * attribute has no name, IllegalArgumentException is thrown.      */
specifier|public
name|void
name|addAttribute
parameter_list|(
name|Attribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to insert unnamed attribute."
argument_list|)
throw|;
comment|// block overrides
comment|// TODO: change method signature to return replaced attribute and make sure the
comment|// Modeler handles it...
name|Object
name|existingAttribute
init|=
name|attributes
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingAttribute
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|existingAttribute
operator|==
name|attribute
condition|)
return|return;
else|else
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"An attempt to override attribute '"
operator|+
name|attribute
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// Check that there aren't any relationships with the same name as the given
comment|// attribute.
name|Object
name|existingRelationship
init|=
name|relationships
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingRelationship
operator|!=
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attribute name conflict with existing relationship '"
operator|+
name|attribute
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
name|attributes
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setEntity
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/** Removes an attribute named<code>attrName</code>. */
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|attrName
parameter_list|)
block|{
name|attributes
operator|.
name|remove
argument_list|(
name|attrName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearAttributes
parameter_list|()
block|{
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns relationship with name<code>relName</code>. Will return null if no      * relationship with this name exists in the entity.      */
specifier|public
name|Relationship
name|getRelationship
parameter_list|(
name|String
name|relName
parameter_list|)
block|{
return|return
name|relationships
operator|.
name|get
argument_list|(
name|relName
argument_list|)
return|;
block|}
comment|/** Adds new relationship to the entity. */
specifier|public
name|void
name|addRelationship
parameter_list|(
name|Relationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to insert unnamed relationship."
argument_list|)
throw|;
comment|// block overrides
comment|// TODO: change method signature to return replaced attribute and make sure the
comment|// Modeler handles it...
name|Object
name|existingRelationship
init|=
name|relationships
operator|.
name|get
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingRelationship
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|existingRelationship
operator|==
name|relationship
condition|)
return|return;
else|else
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"An attempt to override relationship '"
operator|+
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// Check that there aren't any attributes with the same name as the given
comment|// relationship.
name|Object
name|existingAttribute
init|=
name|attributes
operator|.
name|get
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingAttribute
operator|!=
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Relationship name conflict with existing attribute '"
operator|+
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
name|relationships
operator|.
name|put
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/** Removes a relationship named<code>attrName</code>. */
specifier|public
name|void
name|removeRelationship
parameter_list|(
name|String
name|relName
parameter_list|)
block|{
name|relationships
operator|.
name|remove
argument_list|(
name|relName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearRelationships
parameter_list|()
block|{
name|relationships
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an unmodifiable map of relationships sorted by name.      */
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Relationship
argument_list|>
name|getRelationshipMap
parameter_list|()
block|{
comment|// create a new instance ... earlier attempts to cache it in the entity caused
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableSortedMap
argument_list|(
name|relationships
argument_list|)
return|;
block|}
comment|/**      * Returns a relationship that has a specified entity as a target. If there is more      * than one relationship for the same target, it is unpredictable which one will be      * returned.      *       * @since 1.1      */
specifier|public
name|Relationship
name|getAnyRelationship
parameter_list|(
name|Entity
name|targetEntity
parameter_list|)
block|{
if|if
condition|(
name|getRelationships
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
return|return
literal|null
return|;
for|for
control|(
name|Relationship
name|r
range|:
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|r
operator|.
name|getTargetEntity
argument_list|()
operator|==
name|targetEntity
condition|)
return|return
name|r
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns an unmodifiable collection of Relationships that exist in this entity.      */
specifier|public
name|Collection
argument_list|<
name|?
extends|extends
name|Relationship
argument_list|>
name|getRelationships
parameter_list|()
block|{
comment|// create a new instance ... earlier attempts to cache it in the entity caused
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|relationships
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns an unmodifiable sorted map of entity attributes.      */
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Attribute
argument_list|>
name|getAttributeMap
parameter_list|()
block|{
comment|// create a new instance ... earlier attempts to cache it in the entity caused
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableSortedMap
argument_list|(
name|attributes
argument_list|)
return|;
block|}
comment|/**      * Returns an unmodifiable collection of entity attributes.      */
specifier|public
name|Collection
argument_list|<
name|?
extends|extends
name|Attribute
argument_list|>
name|getAttributes
parameter_list|()
block|{
comment|// create a new instance ... earlier attempts to cache it in the entity caused
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|attributes
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Translates Expression rooted in this entity to an analogous expression rooted in      * related entity.      *       * @since 1.1      */
specifier|public
specifier|abstract
name|Expression
name|translateToRelatedEntity
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|String
name|relationshipPath
parameter_list|)
function_decl|;
comment|/**      * Convenience method returning the last component in the path iterator.      *       * @since 1.1      * @see #resolvePathComponents(Expression)      */
specifier|public
name|Object
name|lastPathComponent
parameter_list|(
name|Expression
name|pathExp
parameter_list|)
block|{
name|CayenneMapEntry
name|last
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|it
init|=
name|resolvePathComponents
argument_list|(
name|pathExp
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
name|last
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|last
return|;
block|}
comment|/**      * Processes expression<code>pathExp</code> and returns an Iterator of path      * components that contains a sequence of Attributes and Relationships. Note that if      * path is invalid and can not be resolved from this entity, this method will still      * return an Iterator, but an attempt to read the first invalid path component will      * result in ExpressionException.      */
specifier|public
specifier|abstract
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|resolvePathComponents
parameter_list|(
name|Expression
name|pathExp
parameter_list|)
throws|throws
name|ExpressionException
function_decl|;
comment|/**      * Returns an Iterator over the path components that contains a sequence of Attributes      * and Relationships. Note that if path is invalid and can not be resolved from this      * entity, this method will still return an Iterator, but an attempt to read the first      * invalid path component will result in ExpressionException.      */
specifier|public
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|resolvePathComponents
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ExpressionException
block|{
return|return
operator|new
name|PathIterator
argument_list|(
name|path
argument_list|)
return|;
block|}
comment|// An iterator resolving mapping components represented by the path string.
comment|// This entity is assumed to be the root of the path.
specifier|final
class|class
name|PathIterator
implements|implements
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
block|{
specifier|private
name|StringTokenizer
name|toks
decl_stmt|;
specifier|private
name|Entity
name|currentEnt
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
name|PathIterator
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|currentEnt
operator|=
name|Entity
operator|.
name|this
expr_stmt|;
name|toks
operator|=
operator|new
name|StringTokenizer
argument_list|(
name|path
argument_list|,
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|toks
operator|.
name|hasMoreTokens
argument_list|()
return|;
block|}
specifier|public
name|CayenneMapEntry
name|next
parameter_list|()
block|{
name|String
name|pathComp
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
comment|// see if this is an attribute
name|Attribute
name|attr
init|=
name|currentEnt
operator|.
name|getAttribute
argument_list|(
name|pathComp
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
comment|// do a sanity check...
if|if
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
condition|)
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Attribute must be the last component of the path: '"
operator|+
name|pathComp
operator|+
literal|"'."
argument_list|,
name|path
argument_list|,
literal|null
argument_list|)
throw|;
return|return
name|attr
return|;
block|}
name|Relationship
name|rel
init|=
name|currentEnt
operator|.
name|getRelationship
argument_list|(
name|pathComp
argument_list|)
decl_stmt|;
if|if
condition|(
name|rel
operator|!=
literal|null
condition|)
block|{
name|currentEnt
operator|=
name|rel
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
return|return
name|rel
return|;
block|}
comment|// build error message
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Can't resolve path component: ["
argument_list|)
operator|.
name|append
argument_list|(
name|currentEnt
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|pathComp
argument_list|)
operator|.
name|append
argument_list|(
literal|"]."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ExpressionException
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|,
name|path
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"'remove' operation is not supported."
argument_list|)
throw|;
block|}
block|}
specifier|final
name|MappingNamespace
name|getNonNullNamespace
parameter_list|()
block|{
name|MappingNamespace
name|parent
init|=
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Entity '"
operator|+
name|getName
argument_list|()
operator|+
literal|"' has no parent MappingNamespace (such as DataMap)"
argument_list|)
throw|;
return|return
name|parent
return|;
block|}
block|}
end_class

end_unit

