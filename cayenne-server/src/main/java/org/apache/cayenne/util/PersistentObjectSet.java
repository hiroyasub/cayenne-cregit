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
name|HashSet
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
name|ValueHolder
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|PersistentObjectSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|RelationshipFault
argument_list|<
name|E
argument_list|>
implements|implements
name|Set
argument_list|<
name|E
argument_list|>
implements|,
name|ValueHolder
argument_list|<
name|Object
argument_list|>
implements|,
name|PersistentObjectCollection
argument_list|<
name|E
argument_list|>
block|{
comment|// wrapped objects set
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|objectSet
decl_stmt|;
comment|// track additions/removals in unresolved...
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|addedToUnresolved
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|removedFromUnresolved
decl_stmt|;
comment|// exists for the benefit of manual serialization schemes such as the one in Hessian.
specifier|private
name|PersistentObjectSet
parameter_list|()
block|{
block|}
specifier|public
name|PersistentObjectSet
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|,
name|String
name|relationshipName
parameter_list|)
block|{
name|super
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns whether this list is not yet resolved and requires a fetch.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isFault
parameter_list|()
block|{
if|if
condition|(
name|objectSet
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
name|objectSet
operator|=
operator|new
name|HashSet
argument_list|<>
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
comment|/**      * Turns itself into a fault, thus forcing a refresh on the next access.      */
annotation|@
name|Override
specifier|public
name|void
name|invalidate
parameter_list|()
block|{
name|setObjectSet
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Set
argument_list|<
name|E
argument_list|>
name|old
init|=
name|this
operator|.
name|objectSet
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|instanceof
name|Set
condition|)
block|{
name|setObjectSet
argument_list|(
operator|(
name|Set
argument_list|<
name|E
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
comment|// we can wrap non-set collections on the fly - this is needed for prefetch handling...
comment|// although it seems to be breaking the contract for 'setValueDirectly' ???
name|setObjectSet
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|(
operator|(
name|Collection
argument_list|<
name|E
argument_list|>
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
literal|"Value must be a list, got: %s"
argument_list|,
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
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|getValue
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|resolvedObjectSet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|getValueDirectly
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|objectSet
return|;
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
name|resolvedObjectSet
argument_list|()
expr_stmt|;
return|return
name|setValueDirectly
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|void
name|setObjectSet
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|objectSet
parameter_list|)
block|{
name|this
operator|.
name|objectSet
operator|=
name|objectSet
expr_stmt|;
block|}
comment|// ====================================================
comment|// Standard Set Methods.
comment|// ====================================================
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|o
parameter_list|)
block|{
if|if
condition|(
operator|(
name|isFault
argument_list|()
operator|)
condition|?
name|addLocal
argument_list|(
name|o
argument_list|)
else|:
name|objectSet
operator|.
name|add
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|postprocessAdd
argument_list|(
name|o
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|resolvedObjectSet
argument_list|()
operator|.
name|addAll
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|// TODO: here we assume that all objects were added, while addAll may
comment|// technically return true and add only some objects... need a smarter
comment|// approach (maybe use "contains" in postprocessAdd"?)
name|postprocessAdd
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|resolved
init|=
name|resolvedObjectSet
argument_list|()
decl_stmt|;
name|postprocessRemove
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
name|resolved
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|contains
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
name|c
parameter_list|)
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|c
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
operator|!
operator|(
name|o
operator|instanceof
name|PersistentObjectSet
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|PersistentObjectSet
operator|)
name|o
operator|)
operator|.
name|resolvedObjectSet
argument_list|()
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
return|return
literal|53
operator|+
name|resolvedObjectSet
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|E
name|object
init|=
operator|(
name|E
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|(
name|isFault
argument_list|()
operator|)
condition|?
name|removeLocal
argument_list|(
name|object
argument_list|)
else|:
name|objectSet
operator|.
name|remove
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|postprocessRemove
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|resolvedObjectSet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|// TODO: here we assume that all objects were removed, while removeAll may
comment|// technically return true and remove only some objects... need a smarter
comment|// approach
name|postprocessRemove
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|c
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|toRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|resolvedObjectSet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|object
range|:
name|resolvedObjectSet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|c
operator|.
name|contains
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|toRemove
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|result
init|=
name|resolvedObjectSet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|postprocessRemove
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|a
parameter_list|)
block|{
return|return
name|resolvedObjectSet
argument_list|()
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
return|;
block|}
comment|// ====================================================
comment|// Tracking set modifications, and resolving it
comment|// on demand
comment|// ====================================================
comment|/**      * Returns internal objects list resolving it if needed.      */
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|resolvedObjectSet
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
comment|// if another thread just resolved the list
if|if
condition|(
name|isFault
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|localList
init|=
name|resolveFromDB
argument_list|()
decl_stmt|;
name|this
operator|.
name|objectSet
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|localList
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|objectSet
return|;
block|}
name|void
name|clearLocalChanges
parameter_list|()
block|{
name|addedToUnresolved
operator|=
literal|null
expr_stmt|;
name|removedFromUnresolved
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mergeLocalChanges
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|resolved
parameter_list|)
block|{
comment|// only merge if an object is in an uncommitted state
comment|// any other state means that our local tracking
comment|// is invalid...
if|if
condition|(
name|isUncommittedParent
argument_list|()
condition|)
block|{
if|if
condition|(
name|removedFromUnresolved
operator|!=
literal|null
condition|)
block|{
name|resolved
operator|.
name|removeAll
argument_list|(
name|removedFromUnresolved
argument_list|)
expr_stmt|;
block|}
comment|// add only those that are not already on the list
comment|// do not include transient objects...
if|if
condition|(
name|addedToUnresolved
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|E
name|next
range|:
name|addedToUnresolved
control|)
block|{
if|if
condition|(
name|next
operator|instanceof
name|Persistent
condition|)
block|{
name|Persistent
name|dataObject
init|=
operator|(
name|Persistent
operator|)
name|next
decl_stmt|;
if|if
condition|(
name|dataObject
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|TRANSIENT
condition|)
block|{
continue|continue;
block|}
block|}
if|if
condition|(
operator|!
name|resolved
operator|.
name|contains
argument_list|(
name|next
argument_list|)
condition|)
block|{
name|resolved
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// clear local information in any event
name|clearLocalChanges
argument_list|()
expr_stmt|;
block|}
name|boolean
name|addLocal
parameter_list|(
name|E
name|object
parameter_list|)
block|{
if|if
condition|(
name|removedFromUnresolved
operator|!=
literal|null
condition|)
block|{
name|removedFromUnresolved
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|addedToUnresolved
operator|==
literal|null
condition|)
block|{
name|addedToUnresolved
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|addedToUnresolved
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// this is really meaningless, since we don't know
comment|// if an object was present in the list
return|return
literal|true
return|;
block|}
name|boolean
name|removeLocal
parameter_list|(
name|E
name|object
parameter_list|)
block|{
if|if
condition|(
name|addedToUnresolved
operator|!=
literal|null
condition|)
block|{
name|addedToUnresolved
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|removedFromUnresolved
operator|==
literal|null
condition|)
block|{
name|removedFromUnresolved
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|shouldAddToRemovedFromUnresolvedSet
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|removedFromUnresolved
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|// this is really meaningless, since we don't know
comment|// if an object was present in the list
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|postprocessAdd
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
for|for
control|(
name|E
name|next
range|:
name|collection
control|)
block|{
name|postprocessAdd
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|postprocessRemove
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
for|for
control|(
name|E
name|next
range|:
name|collection
control|)
block|{
name|postprocessRemove
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|postprocessAdd
parameter_list|(
name|E
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
if|if
condition|(
name|addedObject
operator|instanceof
name|Persistent
condition|)
block|{
name|Util
operator|.
name|setReverse
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
operator|(
name|Persistent
operator|)
name|addedObject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|postprocessRemove
parameter_list|(
name|E
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
if|if
condition|(
name|removedObject
operator|instanceof
name|Persistent
condition|)
block|{
name|Util
operator|.
name|unsetReverse
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
operator|(
name|Persistent
operator|)
name|removedObject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|shouldAddToRemovedFromUnresolvedSet
parameter_list|(
name|E
name|object
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|objectSet
operator|!=
literal|null
operator|)
condition|?
name|objectSet
operator|.
name|toString
argument_list|()
else|:
literal|"[<unresolved>]"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addDirectly
parameter_list|(
name|E
name|target
parameter_list|)
block|{
if|if
condition|(
name|isFault
argument_list|()
condition|)
block|{
name|addLocal
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|objectSet
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeDirectly
parameter_list|(
name|E
name|target
parameter_list|)
block|{
if|if
condition|(
name|isFault
argument_list|()
condition|)
block|{
name|removeLocal
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|objectSet
operator|.
name|remove
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

