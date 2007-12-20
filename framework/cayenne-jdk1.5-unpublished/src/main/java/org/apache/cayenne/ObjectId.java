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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|IDUtil
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
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|EqualsBuilder
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
name|HashCodeBuilder
import|;
end_import

begin_comment
comment|/**  * A portable global identifier for persistent objects. ObjectId can be temporary (used  * for transient or new uncommitted objects) or permanent (used for objects that have been  * already stored in DB). A temporary ObjectId stores object entity name and a  * pseudo-unique binary key; permanent id stores a map of values from an external  * persistent store (aka "primary key").  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ObjectId
implements|implements
name|Serializable
block|{
specifier|protected
name|String
name|entityName
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objectIdKeys
decl_stmt|;
specifier|private
name|String
name|singleKey
decl_stmt|;
specifier|private
name|Object
name|singleValue
decl_stmt|;
comment|// key which is used for temporary ObjectIds only
specifier|protected
name|byte
index|[]
name|key
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacementIdMap
decl_stmt|;
comment|// hash code is transient to make sure id is portable across VM
specifier|transient
name|int
name|hashCode
decl_stmt|;
comment|// exists for deserialization with Hessian and similar
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|ObjectId
parameter_list|()
block|{
block|}
comment|/**      * Creates a TEMPORARY ObjectId. Assigns a generated unique key.      *       * @since 1.2      */
specifier|public
name|ObjectId
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|this
argument_list|(
name|entityName
argument_list|,
name|IDUtil
operator|.
name|pseudoUniqueByteSequence8
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a TEMPORARY id with a specified entity name and a binary key. It is a      * caller responsibility to provide a globally unique binary key.      *       * @since 1.2      */
specifier|public
name|ObjectId
parameter_list|(
name|String
name|entityName
parameter_list|,
name|byte
index|[]
name|key
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
comment|/**      * Creates a portable permanent ObjectId.      *       * @param entityName The entity name which this object id is for      * @param key A key describing this object id, usually the attribute name for the      *            primary key      * @param value The unique value for this object id      * @since 1.2      */
specifier|public
name|ObjectId
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
name|key
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|entityName
argument_list|,
name|key
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a portable permanent ObjectId.      *       * @param entityName The entity name which this object id is for      * @param key A key describing this object id, usually the attribute name for the      *            primary key      * @param value The unique value for this object id      * @since 1.2      */
specifier|public
name|ObjectId
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
name|this
operator|.
name|singleKey
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Creates a portable permanent ObjectId as a compound primary key.      *       * @param entityName The entity name which this object id is for      * @param idMap Keys are usually the attribute names for each part of the primary key.      *            Values are unique when taken as a whole.      * @since 1.2      */
specifier|public
name|ObjectId
parameter_list|(
name|String
name|entityName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|idMap
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
if|if
condition|(
name|idMap
operator|==
literal|null
operator|||
name|idMap
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
block|}
if|else if
condition|(
name|idMap
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|e
init|=
name|idMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|this
operator|.
name|singleKey
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|e
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// we have to create a copy of the map, otherwise we may run into
comment|// serialization
comment|// problems with hessian
name|this
operator|.
name|objectIdKeys
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|idMap
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Is this is temporary object id (used for objects which are not yet persisted to the      * data store).      */
specifier|public
name|boolean
name|isTemporary
parameter_list|()
block|{
return|return
name|key
operator|!=
literal|null
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
comment|/**      * Get the binary temporary object id. Null if this object id is permanent (persisted      * to the data store).      */
specifier|public
name|byte
index|[]
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * Returns an unmodifiable Map of persistent id values, essentially a primary key map.      * For temporary id returns replacement id, if it was already created. Otherwise      * returns an empty map.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getIdSnapshot
parameter_list|()
block|{
if|if
condition|(
name|isTemporary
argument_list|()
condition|)
block|{
return|return
operator|(
name|replacementIdMap
operator|==
literal|null
operator|)
condition|?
name|Collections
operator|.
name|EMPTY_MAP
else|:
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|replacementIdMap
argument_list|)
return|;
block|}
if|if
condition|(
name|singleKey
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|singleKey
argument_list|,
name|singleValue
argument_list|)
return|;
block|}
return|return
name|objectIdKeys
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|objectIdKeys
argument_list|)
else|:
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
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
name|object
operator|instanceof
name|ObjectId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectId
name|id
init|=
operator|(
name|ObjectId
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|entityName
argument_list|,
name|id
operator|.
name|entityName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|isTemporary
argument_list|()
condition|)
block|{
return|return
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|key
argument_list|,
name|id
operator|.
name|key
argument_list|)
operator|.
name|isEquals
argument_list|()
return|;
block|}
if|if
condition|(
name|singleKey
operator|!=
literal|null
condition|)
block|{
return|return
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|singleKey
argument_list|,
name|id
operator|.
name|singleKey
argument_list|)
operator|&&
name|valueEquals
argument_list|(
name|singleValue
argument_list|,
name|id
operator|.
name|singleValue
argument_list|)
return|;
block|}
if|if
condition|(
name|id
operator|.
name|objectIdKeys
operator|==
literal|null
condition|)
block|{
return|return
name|objectIdKeys
operator|==
literal|null
return|;
block|}
if|if
condition|(
name|id
operator|.
name|objectIdKeys
operator|.
name|size
argument_list|()
operator|!=
name|objectIdKeys
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|objectIdKeys
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|entryKey
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|entryValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|entryValue
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|id
operator|.
name|objectIdKeys
operator|.
name|get
argument_list|(
name|entryKey
argument_list|)
operator|!=
literal|null
operator|||
operator|!
name|id
operator|.
name|objectIdKeys
operator|.
name|containsKey
argument_list|(
name|entryKey
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|valueEquals
argument_list|(
name|entryValue
argument_list|,
name|id
operator|.
name|objectIdKeys
operator|.
name|get
argument_list|(
name|entryKey
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
specifier|final
name|boolean
name|valueEquals
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
name|o2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o1
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
name|o1
operator|instanceof
name|Number
condition|)
block|{
return|return
name|o2
operator|instanceof
name|Number
operator|&&
operator|(
operator|(
name|Number
operator|)
name|o1
operator|)
operator|.
name|longValue
argument_list|()
operator|==
operator|(
operator|(
name|Number
operator|)
name|o2
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
if|if
condition|(
name|o1
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
operator|.
name|isEquals
argument_list|()
return|;
block|}
return|return
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|hashCode
operator|==
literal|0
condition|)
block|{
name|HashCodeBuilder
name|builder
init|=
operator|new
name|HashCodeBuilder
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|entityName
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|singleKey
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|singleKey
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
comment|// must reconcile all possible numeric types
if|if
condition|(
name|singleValue
operator|instanceof
name|Number
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|singleValue
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|singleValue
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|objectIdKeys
operator|!=
literal|null
condition|)
block|{
name|int
name|len
init|=
name|objectIdKeys
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// handle multiple keys - must sort the keys to use with HashCodeBuilder
name|Object
index|[]
name|keys
init|=
name|objectIdKeys
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
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
comment|// HashCodeBuilder will take care of processing object if it
comment|// happens to be a primitive array such as byte[]
comment|// also we don't have to append the key hashcode, its index will
comment|// work
name|builder
operator|.
name|append
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|objectIdKeys
operator|.
name|get
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|)
decl_stmt|;
comment|// must reconcile all possible numeric types
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|hashCode
operator|=
name|builder
operator|.
name|toHashCode
argument_list|()
expr_stmt|;
assert|assert
name|hashCode
operator|!=
literal|0
operator|:
literal|"Generated zero hashCode"
assert|;
block|}
return|return
name|hashCode
return|;
block|}
comment|/**      * Returns a non-null mutable map that can be used to append replacement id values.      * This allows to incrementally build a replacement GlobalID.      *       * @since 1.2      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getReplacementIdMap
parameter_list|()
block|{
if|if
condition|(
name|replacementIdMap
operator|==
literal|null
condition|)
block|{
name|replacementIdMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|replacementIdMap
return|;
block|}
comment|/**      * Creates and returns a replacement ObjectId. No validation of ID is done.      *       * @since 1.2      */
specifier|public
name|ObjectId
name|createReplacementId
parameter_list|()
block|{
comment|// merge existing and replaced ids to handle a replaced subset of
comment|// a compound primary key
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newIdMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|getIdSnapshot
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacementIdMap
operator|!=
literal|null
condition|)
block|{
name|newIdMap
operator|.
name|putAll
argument_list|(
name|replacementIdMap
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ObjectId
argument_list|(
name|getEntityName
argument_list|()
argument_list|,
name|newIdMap
argument_list|)
return|;
block|}
comment|/**      * Returns true if there is full or partial replacement id attached to this id. This      * method is preferrable to "!getReplacementIdMap().isEmpty()" as it avoids unneeded      * replacement id map creation.      */
specifier|public
name|boolean
name|isReplacementIdAttached
parameter_list|()
block|{
return|return
name|replacementIdMap
operator|!=
literal|null
operator|&&
operator|!
name|replacementIdMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * A standard toString method used for debugging. It is guaranteed to produce the same      * string if two ObjectIds are equal.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"<ObjectId:"
argument_list|)
operator|.
name|append
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
if|if
condition|(
name|isTemporary
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", TEMP:"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|key
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
name|key
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|singleKey
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|singleKey
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|singleValue
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|objectIdKeys
operator|!=
literal|null
condition|)
block|{
comment|// ensure consistent order of the keys, so that toString could be used as a
comment|// unique key, just like id itself
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|objectIdKeys
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|keys
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|objectIdKeys
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

