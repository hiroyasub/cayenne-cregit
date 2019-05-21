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
name|util
operator|.
name|HashCodeBuilder
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Compound {@link ObjectId}  * @since 4.2  */
end_comment

begin_class
class|class
name|ObjectIdCompound
implements|implements
name|ObjectId
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2265029098344119323L
decl_stmt|;
specifier|protected
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objectIdKeys
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
specifier|private
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
name|ObjectIdCompound
parameter_list|()
block|{
name|entityName
operator|=
literal|null
expr_stmt|;
name|objectIdKeys
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Creates a portable permanent ObjectId as a compound primary key. 	 *  	 * @param entityName 	 *            The entity name which this object id is for 	 * @param idMap 	 *            Keys are usually the attribute names for each part of the 	 *            primary key. Values are unique when taken as a whole. 	 * @since 1.2 	 */
name|ObjectIdCompound
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
name|this
operator|.
name|objectIdKeys
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|objectIdKeys
operator|=
name|wrapIdMap
argument_list|(
name|idMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|wrapIdMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|getClass
argument_list|()
operator|==
name|HashMap
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|m
return|;
block|}
else|else
block|{
comment|// we have to create a copy of the map, otherwise we may run into serialization problems with hessian
return|return
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|m
argument_list|)
return|;
block|}
block|}
comment|/** 	 * Is this is temporary object id (used for objects which are not yet 	 * persisted to the data store). 	 */
annotation|@
name|Override
specifier|public
name|boolean
name|isTemporary
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** 	 * @since 1.2 	 */
annotation|@
name|Override
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
comment|/** 	 * Get the binary temporary object id. Null if this object id is permanent 	 * (persisted to the data store). 	 */
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getKey
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** 	 * Returns an unmodifiable Map of persistent id values, essentially a 	 * primary key map. For temporary id returns replacement id, if it was 	 * already created. Otherwise returns an empty map. 	 */
annotation|@
name|Override
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
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|objectIdKeys
argument_list|)
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
name|ObjectIdCompound
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectIdCompound
name|id
init|=
operator|(
name|ObjectIdCompound
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
name|String
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
operator|&&
operator|(
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
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|else if
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
return|return
literal|true
return|;
block|}
specifier|private
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
name|o2
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|hashCode
operator|!=
literal|0
condition|)
block|{
return|return
name|hashCode
return|;
block|}
name|HashCodeBuilder
name|builder
init|=
operator|new
name|HashCodeBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|entityName
operator|.
name|hashCode
argument_list|()
argument_list|)
decl_stmt|;
comment|// handle multiple keys - must sort the keys to use with HashCodeBuilder
name|String
index|[]
name|keys
init|=
name|objectIdKeys
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
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
name|keys
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// HashCodeBuilder will take care of processing object if it
comment|// happens to be a primitive array such as byte[]
comment|// also we don't have to append the key hashcode, its index will work
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
return|return
name|hashCode
operator|=
name|builder
operator|.
name|toHashCode
argument_list|()
return|;
block|}
comment|/** 	 * Returns a non-null mutable map that can be used to append replacement id 	 * values. This allows to incrementally build a replacement GlobalID. 	 *  	 * @since 1.2 	 */
annotation|@
name|Override
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
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|replacementIdMap
return|;
block|}
comment|/** 	 * Creates and returns a replacement ObjectId. No validation of ID is done. 	 *  	 * @since 1.2 	 */
annotation|@
name|Override
specifier|public
name|ObjectId
name|createReplacementId
parameter_list|()
block|{
if|if
condition|(
name|replacementIdMap
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
comment|// merge existing and replaced ids to handle a replaced subset of a compound primary key
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
argument_list|<>
argument_list|(
name|objectIdKeys
argument_list|)
decl_stmt|;
name|newIdMap
operator|.
name|putAll
argument_list|(
name|replacementIdMap
argument_list|)
expr_stmt|;
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|entityName
argument_list|,
name|newIdMap
argument_list|)
return|;
block|}
comment|/** 	 * Returns true if there is full or partial replacement id attached to this 	 * id. This method is preferable to "!getReplacementIdMap().isEmpty()" as 	 * it avoids unneeded replacement id map creation. 	 */
annotation|@
name|Override
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
comment|/** 	 * A standard toString method used for debugging. It is guaranteed to 	 * produce the same string if two ObjectIds are equal. 	 */
annotation|@
name|Override
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
decl_stmt|;
comment|// ensure consistent order of the keys, so that toString could be
comment|// used as a unique key, just like id itself
name|String
index|[]
name|keys
init|=
name|objectIdKeys
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
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
name|String
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
operator|.
name|append
argument_list|(
name|key
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
return|return
name|buffer
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

