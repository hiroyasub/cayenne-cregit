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
name|DataRow
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
name|query
operator|.
name|PrefetchProcessor
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
name|query
operator|.
name|QueryMetadata
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
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * Processes a number of DataRow sets corresponding to a given prefetch tree, resolving  * DataRows to an object tree. Can process any combination of joint and disjoint sets, per  * prefetch tree.  */
end_comment

begin_class
class|class
name|HierarchicalObjectResolver
block|{
name|DataContext
name|context
decl_stmt|;
name|QueryMetadata
name|queryMetadata
decl_stmt|;
name|DataRowStore
name|cache
decl_stmt|;
name|ClassDescriptor
name|descriptor
decl_stmt|;
name|boolean
name|needToSaveDuplicates
decl_stmt|;
name|HierarchicalObjectResolver
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|)
block|{
name|this
operator|.
name|queryMetadata
operator|=
name|queryMetadata
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
expr_stmt|;
block|}
name|HierarchicalObjectResolver
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|QueryMetadata
name|metadata
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
name|boolean
name|needToSaveDuplicates
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|needToSaveDuplicates
operator|=
name|needToSaveDuplicates
expr_stmt|;
block|}
comment|/**      * Properly synchronized version of 'resolveObjectTree'.      */
name|List
name|synchronizedObjectsFromDataRows
parameter_list|(
name|PrefetchTreeNode
name|tree
parameter_list|,
name|List
name|mainResultRows
parameter_list|,
name|Map
name|extraResultsByPath
parameter_list|)
block|{
synchronized|synchronized
init|(
name|context
operator|.
name|getObjectStore
argument_list|()
init|)
block|{
synchronized|synchronized
init|(
name|cache
init|)
block|{
return|return
name|resolveObjectTree
argument_list|(
name|tree
argument_list|,
name|mainResultRows
argument_list|,
name|extraResultsByPath
argument_list|)
return|;
block|}
block|}
block|}
specifier|private
name|List
name|resolveObjectTree
parameter_list|(
name|PrefetchTreeNode
name|tree
parameter_list|,
name|List
name|mainResultRows
parameter_list|,
name|Map
name|extraResultsByPath
parameter_list|)
block|{
comment|// create a copy of the tree using DecoratedPrefetchNodes and then traverse it
comment|// resolving objects...
name|PrefetchProcessorNode
name|decoratedTree
init|=
operator|new
name|PrefetchProcessorTreeBuilder
argument_list|(
name|this
argument_list|,
name|mainResultRows
argument_list|,
name|extraResultsByPath
argument_list|)
operator|.
name|buildTree
argument_list|(
name|tree
argument_list|)
decl_stmt|;
comment|// do a single path for disjoint prefetches, joint subtrees will be processed at
comment|// each disjoint node that is a parent of joint prefetches.
name|decoratedTree
operator|.
name|traverse
argument_list|(
operator|new
name|DisjointProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// connect related objects
name|decoratedTree
operator|.
name|traverse
argument_list|(
operator|new
name|PostProcessor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|decoratedTree
operator|.
name|getObjects
argument_list|()
operator|!=
literal|null
condition|?
name|decoratedTree
operator|.
name|getObjects
argument_list|()
else|:
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
return|;
block|}
specifier|final
class|class
name|DisjointProcessor
implements|implements
name|PrefetchProcessor
block|{
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
name|PrefetchProcessorNode
name|processorNode
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|node
decl_stmt|;
comment|// this means something bad happened during fetch
if|if
condition|(
name|processorNode
operator|.
name|getDataRows
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// ... continue with processing even if the objects list is empty to handle
comment|// multi-step prefetches.
if|if
condition|(
name|processorNode
operator|.
name|getDataRows
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
name|objects
init|=
name|processorNode
operator|.
name|getResolver
argument_list|()
operator|.
name|objectsFromDataRows
argument_list|(
name|processorNode
operator|.
name|getDataRows
argument_list|()
argument_list|)
decl_stmt|;
name|processorNode
operator|.
name|setObjects
argument_list|(
name|objects
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// delegate processing of the top level joint prefetch to a joint processor,
comment|// skip non-top joint nodes
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|isJointPrefetch
argument_list|()
condition|)
block|{
name|PrefetchProcessorJointNode
name|processorNode
init|=
operator|(
name|PrefetchProcessorJointNode
operator|)
name|node
decl_stmt|;
name|JointProcessor
name|subprocessor
init|=
operator|new
name|JointProcessor
argument_list|(
name|processorNode
argument_list|)
decl_stmt|;
name|PrefetchProcessorNode
name|parent
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|processorNode
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|isPhantom
argument_list|()
condition|)
block|{
name|parent
operator|=
operator|(
name|PrefetchProcessorNode
operator|)
name|parent
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
name|parentRows
init|=
name|parent
operator|.
name|getDataRows
argument_list|()
decl_stmt|;
comment|// phantom node?
if|if
condition|(
name|parentRows
operator|==
literal|null
operator|||
name|parentRows
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
name|List
name|parentObjects
init|=
name|parent
operator|.
name|getObjects
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|parentRows
operator|.
name|size
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|subprocessor
operator|.
name|setCurrentFlatRow
argument_list|(
operator|(
name|DataRow
operator|)
name|parentRows
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setLastResolved
argument_list|(
operator|(
name|Persistent
operator|)
name|parentObjects
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|processorNode
operator|.
name|traverse
argument_list|(
name|subprocessor
argument_list|)
expr_stmt|;
block|}
name|List
name|objects
init|=
name|processorNode
operator|.
name|getObjects
argument_list|()
decl_stmt|;
name|cache
operator|.
name|snapshotsUpdatedForObjects
argument_list|(
name|objects
argument_list|,
name|processorNode
operator|.
name|getResolvedRows
argument_list|()
argument_list|,
name|queryMetadata
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startUnknownPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown prefetch node: "
operator|+
name|node
argument_list|)
throw|;
block|}
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// now that all the children are processed, we can clear the dupes
comment|// TODO: see TODO in ObjectResolver.relatedObjectsFromDataRows
if|if
condition|(
name|node
operator|.
name|isDisjointPrefetch
argument_list|()
operator|&&
operator|!
name|needToSaveDuplicates
condition|)
block|{
name|PrefetchProcessorNode
name|processorNode
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|processorNode
operator|.
name|isJointChildren
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
init|=
name|processorNode
operator|.
name|getObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|objects
operator|!=
literal|null
operator|&&
name|objects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Set
argument_list|<
name|Persistent
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Persistent
argument_list|>
argument_list|(
name|objects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Persistent
argument_list|>
name|it
init|=
name|objects
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
if|if
condition|(
operator|!
name|seen
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
comment|// a processor of a single joint result set that walks a subtree of prefetch nodes
comment|// that use this result set.
specifier|final
class|class
name|JointProcessor
implements|implements
name|PrefetchProcessor
block|{
name|DataRow
name|currentFlatRow
decl_stmt|;
name|PrefetchProcessorNode
name|rootNode
decl_stmt|;
name|JointProcessor
parameter_list|(
name|PrefetchProcessorJointNode
name|rootNode
parameter_list|)
block|{
name|this
operator|.
name|rootNode
operator|=
name|rootNode
expr_stmt|;
block|}
name|void
name|setCurrentFlatRow
parameter_list|(
name|DataRow
name|currentFlatRow
parameter_list|)
block|{
name|this
operator|.
name|currentFlatRow
operator|=
name|currentFlatRow
expr_stmt|;
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// disjoint prefetch that is not the root terminates the walk...
comment|// don't process the root node itself..
return|return
name|node
operator|==
name|rootNode
return|;
block|}
specifier|public
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
name|PrefetchProcessorJointNode
name|processorNode
init|=
operator|(
name|PrefetchProcessorJointNode
operator|)
name|node
decl_stmt|;
name|Persistent
name|object
init|=
literal|null
decl_stmt|;
comment|// find existing object, if found skip further processing
name|Map
name|id
init|=
name|processorNode
operator|.
name|idFromFlatRow
argument_list|(
name|currentFlatRow
argument_list|)
decl_stmt|;
name|object
operator|=
name|processorNode
operator|.
name|getResolved
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|row
operator|=
name|processorNode
operator|.
name|rowFromFlatRow
argument_list|(
name|currentFlatRow
argument_list|)
expr_stmt|;
name|object
operator|=
name|processorNode
operator|.
name|getResolver
argument_list|()
operator|.
name|objectFromDataRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
comment|// LEFT OUTER JOIN produced no matches...
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|processorNode
operator|.
name|putResolved
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|processorNode
operator|.
name|addObject
argument_list|(
name|object
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
comment|// linking by parent needed even if an object is already there
comment|// (many-to-many case)
name|processorNode
operator|.
name|getParentAttachmentStrategy
argument_list|()
operator|.
name|linkToParent
argument_list|(
name|row
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|processorNode
operator|.
name|setLastResolved
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
name|processorNode
operator|.
name|isJointChildren
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PrefetchProcessorNode
operator|)
name|node
operator|)
operator|.
name|isJointChildren
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|startUnknownPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown prefetch node: "
operator|+
name|node
argument_list|)
throw|;
block|}
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// noop
block|}
block|}
comment|// processor that converts temporary associations between DataObjects to Cayenne
comment|// relationships and also fires snapshot update events
specifier|final
class|class
name|PostProcessor
implements|implements
name|PrefetchProcessor
block|{
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
operator|(
operator|(
name|PrefetchProcessorNode
operator|)
name|node
operator|)
operator|.
name|connectToParents
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
name|PrefetchProcessorJointNode
name|processorNode
init|=
operator|(
name|PrefetchProcessorJointNode
operator|)
name|node
decl_stmt|;
if|if
condition|(
operator|!
name|processorNode
operator|.
name|getObjects
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cache
operator|.
name|snapshotsUpdatedForObjects
argument_list|(
name|processorNode
operator|.
name|getObjects
argument_list|()
argument_list|,
name|processorNode
operator|.
name|getResolvedRows
argument_list|()
argument_list|,
name|queryMetadata
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// run 'connectToParents' even if the object list is empty. This is needed to
comment|// refresh stale relationships e.g. when some related objects got deleted.
name|processorNode
operator|.
name|connectToParents
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startUnknownPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown prefetch node: "
operator|+
name|node
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

