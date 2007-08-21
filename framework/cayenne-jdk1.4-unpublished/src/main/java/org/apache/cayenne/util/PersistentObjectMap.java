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
name|java
operator|.
name|util
operator|.
name|Set
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
name|ValueHolder
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

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PersistentObjectMap
extends|extends
name|RelationshipFault
implements|implements
name|Map
implements|,
name|ValueHolder
block|{
specifier|protected
name|Map
name|objectMap
decl_stmt|;
specifier|protected
name|Expression
name|mapKeyExpression
decl_stmt|;
comment|// exists for the benefit of manual serialization schemes such as the one in Hessian.
specifier|private
name|PersistentObjectMap
parameter_list|()
block|{
block|}
comment|/**      * Creates PersistentObjectList initializing it with list owner persistent object and      * relationship name that this list maps to.      *       * @param relationshipOwner persistent object that owns this list.      * @param relationshipName a query used to resolve the list      * @param mapKeyExpression a Cayenne expression that resolves to a map key for the      *            target entity.      */
specifier|public
name|PersistentObjectMap
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|,
name|String
name|relationshipName
parameter_list|,
name|Expression
name|mapKeyExpression
parameter_list|)
block|{
name|super
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapKeyExpression
operator|=
name|mapKeyExpression
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|resolvedObjectMap
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getValueDirectly
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|objectMap
return|;
block|}
specifier|public
name|void
name|invalidate
parameter_list|()
block|{
name|setObjectMap
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFault
parameter_list|()
block|{
if|if
condition|(
name|objectMap
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// resolve on the fly if owner is transient... Can't do it in constructor, as
comment|// object may be in an inconsistent state during construction time
comment|// synchronize??
if|else if
condition|(
name|isTransientParent
argument_list|()
condition|)
block|{
name|objectMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
specifier|public
name|Object
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
name|resolvedObjectMap
argument_list|()
expr_stmt|;
return|return
name|setValueDirectly
argument_list|(
name|objectMap
argument_list|)
return|;
block|}
specifier|public
name|Object
name|setValueDirectly
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
name|Object
name|old
init|=
name|this
operator|.
name|objectMap
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|instanceof
name|Map
condition|)
block|{
name|setObjectMap
argument_list|(
operator|(
name|Map
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
comment|// we can index collections on the fly - this is needed for prefetch handling...
comment|// although it seems to be breaking the contract for 'setValueDirectly' ???
if|else if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|setObjectMap
argument_list|(
name|indexCollection
argument_list|(
operator|(
name|Collection
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Value must be a Map, a Collection or null, got: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|old
return|;
block|}
specifier|public
name|void
name|setObjectMap
parameter_list|(
name|Map
name|objectMap
parameter_list|)
block|{
name|this
operator|.
name|objectMap
operator|=
name|objectMap
expr_stmt|;
block|}
comment|// ====================================================
comment|// Tracking map modifications, and resolving it
comment|// on demand
comment|// ====================================================
comment|/**      * Returns internal objects list resolving it if needed.      */
specifier|protected
name|Map
name|resolvedObjectMap
parameter_list|()
block|{
if|if
condition|(
name|isFault
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|// now that we obtained the lock, check
comment|// if another thread just resolved the map
if|if
condition|(
name|isFault
argument_list|()
condition|)
block|{
name|List
name|localList
init|=
name|resolveFromDB
argument_list|()
decl_stmt|;
comment|// map objects by property
name|Map
name|localMap
init|=
name|indexCollection
argument_list|(
name|localList
argument_list|)
decl_stmt|;
comment|// TODO: andrus 8/20/2007 implement merging local changes like
comment|// PersistentObjectList does
comment|// mergeLocalChanges(localList);
name|this
operator|.
name|objectMap
operator|=
name|localMap
expr_stmt|;
block|}
block|}
block|}
return|return
name|objectMap
return|;
block|}
comment|/**      * Converts a collection into a map indexed by map key.      */
specifier|protected
name|Map
name|indexCollection
parameter_list|(
name|Collection
name|collection
parameter_list|)
block|{
comment|// map objects by property
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|(
operator|(
name|int
operator|)
operator|(
name|collection
operator|.
name|size
argument_list|()
operator|*
literal|1.33d
operator|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|collection
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Iterator
name|it
init|=
name|collection
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
name|Object
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|key
init|=
name|mapKeyExpression
operator|.
name|evaluate
argument_list|(
name|next
argument_list|)
decl_stmt|;
name|Object
name|previous
init|=
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
operator|&&
name|previous
operator|!=
name|next
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Duplicate key '"
operator|+
name|key
operator|+
literal|"' in relationship map. Relationship: "
operator|+
name|relationshipName
operator|+
literal|", source object: "
operator|+
name|relationshipOwner
operator|.
name|getObjectId
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|objectMap
operator|!=
literal|null
operator|)
condition|?
name|objectMap
operator|.
name|toString
argument_list|()
else|:
literal|"{<unresolved>}"
return|;
block|}
name|void
name|postprocessAdd
parameter_list|(
name|Object
name|addedObject
parameter_list|)
block|{
comment|// notify ObjectContext
if|if
condition|(
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|propertyChanged
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
literal|null
argument_list|,
name|addedObject
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|postprocessAdd
parameter_list|(
name|Collection
name|collection
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|collection
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
name|postprocessAdd
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|postprocessRemove
parameter_list|(
name|Object
name|removedObject
parameter_list|)
block|{
comment|// notify ObjectContext
if|if
condition|(
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|propertyChanged
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
name|removedObject
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|postprocessRemove
parameter_list|(
name|Collection
name|collection
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|collection
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
name|postprocessRemove
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ====================================================
comment|// Standard Map Methods.
comment|// ====================================================
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Map
name|resolved
init|=
name|resolvedObjectMap
argument_list|()
decl_stmt|;
name|postprocessRemove
argument_list|(
name|resolved
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|resolved
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Set
name|entrySet
parameter_list|()
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|Set
name|keySet
parameter_list|()
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|Object
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// TODO: andrus 8/20/2007 implement handling local changes without faulting like
comment|// PersistentObjectList does
name|Object
name|oldValue
init|=
name|resolvedObjectMap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|postprocessAdd
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|postprocessRemove
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
return|return
name|oldValue
return|;
block|}
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
name|map
parameter_list|)
block|{
name|resolvedObjectMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|postprocessAdd
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
comment|// TODO: andrus 8/20/2007 implement handling local changes without faulting like
comment|// PersistentObjectList does
name|Object
name|removed
init|=
name|resolvedObjectMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|postprocessRemove
argument_list|(
name|removed
argument_list|)
expr_stmt|;
return|return
name|removed
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Collection
name|values
parameter_list|()
block|{
return|return
name|resolvedObjectMap
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
block|}
end_class

end_unit

