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
name|Fault
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
name|PrefetchTreeNode
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
name|ToOneProperty
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

begin_comment
comment|/**  * A specialized PrefetchTreeNode used for disjoint prefetch resolving.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|PrefetchProcessorNode
extends|extends
name|PrefetchTreeNode
block|{
name|List
name|dataRows
decl_stmt|;
name|List
name|objects
decl_stmt|;
name|ArcProperty
name|incoming
decl_stmt|;
name|ObjectResolver
name|resolver
decl_stmt|;
name|Map
name|partitionByParent
decl_stmt|;
name|boolean
name|jointChildren
decl_stmt|;
specifier|private
name|Persistent
name|lastResolved
decl_stmt|;
specifier|private
name|ParentAttachmentStrategy
name|parentAttachmentStrategy
decl_stmt|;
name|PrefetchProcessorNode
parameter_list|(
name|PrefetchProcessorNode
name|parent
parameter_list|,
name|String
name|segmentPath
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|segmentPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets up derived flags and values for faster lookup during traversal. Called after      * all properties are initialized.      */
name|void
name|afterInit
parameter_list|()
block|{
if|if
condition|(
name|isPartitionedByParent
argument_list|()
condition|)
block|{
name|partitionByParent
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates a temporary association between child and parent objects. Permanent      * relationship is set using the information created here, by calling      * 'connectToParents'.      */
name|void
name|linkToParent
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|Persistent
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
comment|// if a relationship is to-one (i.e. flattened to-one), can connect right
comment|// away.... write directly to prevent changing persistence state.
if|if
condition|(
name|incoming
operator|instanceof
name|ToOneProperty
condition|)
block|{
name|incoming
operator|.
name|writePropertyDirectly
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
name|peers
init|=
operator|(
name|List
operator|)
name|partitionByParent
operator|.
name|get
argument_list|(
name|parent
argument_list|)
decl_stmt|;
comment|// wrap in a list even if relationship is to-one... will unwrap at the end
comment|// of the processing cycle.
if|if
condition|(
name|peers
operator|==
literal|null
condition|)
block|{
name|peers
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|partitionByParent
operator|.
name|put
argument_list|(
name|parent
argument_list|,
name|peers
argument_list|)
expr_stmt|;
block|}
comment|// checking for duplicates is needed in case of nested joint prefetches
comment|// when there is more than one row with the same combination of adjacent
comment|// parent and child...
if|else if
condition|(
name|peers
operator|.
name|contains
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return;
block|}
name|peers
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|connectToParents
parameter_list|()
block|{
comment|// to-one's were connected earlier...
if|if
condition|(
name|isPartitionedByParent
argument_list|()
condition|)
block|{
comment|// depending on whether parent is a "phantom" node,
comment|// use different strategy
name|PrefetchProcessorNode
name|parent
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|getParent
argument_list|()
decl_stmt|;
name|boolean
name|parentObjectsExist
init|=
name|parent
operator|.
name|getObjects
argument_list|()
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getObjects
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|incoming
operator|.
name|getRelationship
argument_list|()
operator|.
name|isToMany
argument_list|()
condition|)
block|{
if|if
condition|(
name|parentObjectsExist
condition|)
block|{
name|connectToNodeParents
argument_list|(
name|parent
operator|.
name|getObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connectToFaultedParents
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// optional to-one ... need to fill in unresolved relationships with
comment|// null...
if|if
condition|(
name|parentObjectsExist
condition|)
block|{
name|clearNullRelationships
argument_list|(
name|parent
operator|.
name|getObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|final
name|void
name|clearNullRelationships
parameter_list|(
name|List
name|parentObjects
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|parentObjects
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
name|object
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|incoming
operator|.
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
operator|instanceof
name|Fault
condition|)
block|{
name|incoming
operator|.
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|final
name|void
name|connectToNodeParents
parameter_list|(
name|List
name|parentObjects
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|parentObjects
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
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|related
init|=
operator|(
name|List
operator|)
name|partitionByParent
operator|.
name|get
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|connect
argument_list|(
name|object
argument_list|,
name|related
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|final
name|void
name|connectToFaultedParents
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|partitionByParent
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
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|List
name|related
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|connect
argument_list|(
name|object
argument_list|,
name|related
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|final
name|void
name|connect
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|List
name|related
parameter_list|)
block|{
if|if
condition|(
name|incoming
operator|.
name|getRelationship
argument_list|()
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|ValueHolder
name|toManyList
init|=
operator|(
name|ValueHolder
operator|)
name|incoming
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
decl_stmt|;
comment|// TODO, Andrus 11/15/2005 - if list is modified, shouldn't we attempt to
comment|// merge the changes instead of overwriting?
name|toManyList
operator|.
name|setValueDirectly
argument_list|(
name|related
operator|!=
literal|null
condition|?
name|related
else|:
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// this should've been handled elsewhere
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"To-one relationship wasn't handled properly: "
operator|+
name|incoming
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|List
name|getDataRows
parameter_list|()
block|{
return|return
name|dataRows
return|;
block|}
name|List
name|getObjects
parameter_list|()
block|{
return|return
name|objects
return|;
block|}
name|void
name|setResolver
parameter_list|(
name|ObjectResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
name|ObjectResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
name|ArcProperty
name|getIncoming
parameter_list|()
block|{
return|return
name|incoming
return|;
block|}
name|void
name|setIncoming
parameter_list|(
name|ArcProperty
name|incoming
parameter_list|)
block|{
name|this
operator|.
name|incoming
operator|=
name|incoming
expr_stmt|;
block|}
name|void
name|setDataRows
parameter_list|(
name|List
name|dataRows
parameter_list|)
block|{
name|this
operator|.
name|dataRows
operator|=
name|dataRows
expr_stmt|;
block|}
name|void
name|setObjects
parameter_list|(
name|List
name|objects
parameter_list|)
block|{
name|this
operator|.
name|objects
operator|=
name|objects
expr_stmt|;
block|}
name|boolean
name|isJointChildren
parameter_list|()
block|{
return|return
name|jointChildren
return|;
block|}
name|void
name|setJointChildren
parameter_list|(
name|boolean
name|jointChildren
parameter_list|)
block|{
name|this
operator|.
name|jointChildren
operator|=
name|jointChildren
expr_stmt|;
block|}
name|boolean
name|isPartitionedByParent
parameter_list|()
block|{
return|return
name|parent
operator|!=
literal|null
return|;
block|}
name|Persistent
name|getLastResolved
parameter_list|()
block|{
return|return
name|lastResolved
return|;
block|}
name|void
name|setLastResolved
parameter_list|(
name|Persistent
name|lastResolved
parameter_list|)
block|{
name|this
operator|.
name|lastResolved
operator|=
name|lastResolved
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|label
init|=
name|incoming
operator|!=
literal|null
condition|?
name|incoming
operator|.
name|getName
argument_list|()
else|:
literal|"<root>"
decl_stmt|;
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"incoming"
argument_list|,
name|label
argument_list|)
operator|.
name|append
argument_list|(
literal|"phantom"
argument_list|,
name|phantom
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
name|ParentAttachmentStrategy
name|getParentAttachmentStrategy
parameter_list|()
block|{
return|return
name|parentAttachmentStrategy
return|;
block|}
name|void
name|setParentAttachmentStrategy
parameter_list|(
name|ParentAttachmentStrategy
name|parentAttachmentStrategy
parameter_list|)
block|{
name|this
operator|.
name|parentAttachmentStrategy
operator|=
name|parentAttachmentStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

