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
name|LinkedList
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
name|ClassDescriptor
import|;
end_import

begin_class
specifier|final
class|class
name|PrefetchProcessorTreeBuilder
implements|implements
name|PrefetchProcessor
block|{
specifier|private
name|QueryMetadata
name|queryMetadata
decl_stmt|;
specifier|private
name|DataContext
name|context
decl_stmt|;
specifier|private
name|PrefetchProcessorNode
name|root
decl_stmt|;
specifier|private
name|LinkedList
argument_list|<
name|PrefetchProcessorNode
argument_list|>
name|nodeStack
decl_stmt|;
specifier|private
name|List
name|mainResultRows
decl_stmt|;
specifier|private
name|Map
name|extraResultsByPath
decl_stmt|;
name|PrefetchProcessorTreeBuilder
parameter_list|(
name|HierarchicalObjectResolver
name|objectTreeResolver
parameter_list|,
name|List
name|mainResultRows
parameter_list|,
name|Map
name|extraResultsByPath
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|objectTreeResolver
operator|.
name|context
expr_stmt|;
name|this
operator|.
name|queryMetadata
operator|=
name|objectTreeResolver
operator|.
name|queryMetadata
expr_stmt|;
name|this
operator|.
name|mainResultRows
operator|=
name|mainResultRows
expr_stmt|;
name|this
operator|.
name|extraResultsByPath
operator|=
name|extraResultsByPath
expr_stmt|;
block|}
name|PrefetchProcessorNode
name|buildTree
parameter_list|(
name|PrefetchTreeNode
name|tree
parameter_list|)
block|{
comment|// reset state
name|this
operator|.
name|nodeStack
operator|=
operator|new
name|LinkedList
argument_list|<
name|PrefetchProcessorNode
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|root
operator|=
literal|null
expr_stmt|;
name|tree
operator|.
name|traverse
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Failed to create prefetch processing tree."
argument_list|)
throw|;
block|}
return|return
name|root
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
comment|// root should be treated as disjoint
if|if
condition|(
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|startDisjointPrefetch
argument_list|(
name|node
argument_list|)
return|;
block|}
else|else
block|{
name|PrefetchProcessorNode
name|decorated
init|=
operator|new
name|PrefetchProcessorNode
argument_list|(
name|getParent
argument_list|()
argument_list|,
name|node
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|decorated
operator|.
name|setPhantom
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|addNode
argument_list|(
name|decorated
argument_list|)
return|;
block|}
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// look ahead for joint children as joint children will require a different
comment|// node type.
comment|// TODO, Andrus, 11/16/2005 - minor inefficiency: 'adjacentJointNodes' would
comment|// grab ALL nodes, we just need to find first and stop...
name|PrefetchProcessorNode
name|decorated
init|=
operator|!
name|node
operator|.
name|adjacentJointNodes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
operator|new
name|PrefetchProcessorJointNode
argument_list|(
name|getParent
argument_list|()
argument_list|,
name|node
operator|.
name|getName
argument_list|()
argument_list|)
else|:
operator|new
name|PrefetchProcessorNode
argument_list|(
name|getParent
argument_list|()
argument_list|,
name|node
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|decorated
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// semantics has to be "DISJOINT" even if the node is joint, as semantics
comment|// defines relationship with parent..
name|decorated
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
return|return
name|addNode
argument_list|(
name|decorated
argument_list|)
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
name|decorated
init|=
operator|new
name|PrefetchProcessorJointNode
argument_list|(
name|getParent
argument_list|()
argument_list|,
name|node
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|decorated
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|decorated
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|boolean
name|result
init|=
name|addNode
argument_list|(
name|decorated
argument_list|)
decl_stmt|;
comment|// set "jointChildren" flag on all nodes in the same "join group"
name|PrefetchProcessorNode
name|groupNode
init|=
name|decorated
decl_stmt|;
while|while
condition|(
name|groupNode
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|groupNode
operator|.
name|isDisjointPrefetch
argument_list|()
condition|)
block|{
name|groupNode
operator|=
operator|(
name|PrefetchProcessorNode
operator|)
name|groupNode
operator|.
name|getParent
argument_list|()
expr_stmt|;
name|groupNode
operator|.
name|setJointChildren
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|result
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
comment|// handle unknown as disjoint...
return|return
name|startDisjointPrefetch
argument_list|(
name|node
argument_list|)
return|;
block|}
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// pop stack...
name|nodeStack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
block|}
name|boolean
name|addNode
parameter_list|(
name|PrefetchProcessorNode
name|node
parameter_list|)
block|{
name|List
name|rows
decl_stmt|;
name|ArcProperty
name|arc
decl_stmt|;
name|ClassDescriptor
name|descriptor
decl_stmt|;
name|PrefetchProcessorNode
name|currentNode
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentNode
operator|!=
literal|null
condition|)
block|{
name|rows
operator|=
operator|(
name|List
operator|)
name|extraResultsByPath
operator|.
name|get
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|arc
operator|=
operator|(
name|ArcProperty
operator|)
name|currentNode
operator|.
name|getResolver
argument_list|()
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getProperty
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|arc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No relationship with name '"
operator|+
name|node
operator|.
name|getName
argument_list|()
operator|+
literal|"' found in entity "
operator|+
name|currentNode
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|descriptor
operator|=
name|arc
operator|.
name|getTargetDescriptor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|arc
operator|=
literal|null
expr_stmt|;
name|descriptor
operator|=
name|queryMetadata
operator|.
name|getClassDescriptor
argument_list|()
expr_stmt|;
name|rows
operator|=
name|mainResultRows
expr_stmt|;
block|}
name|node
operator|.
name|setDataRows
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|node
operator|.
name|setIncoming
argument_list|(
name|arc
argument_list|)
expr_stmt|;
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
name|isJointPrefetch
argument_list|()
condition|)
block|{
name|node
operator|.
name|setResolver
argument_list|(
operator|new
name|HierarchicalObjectResolverNode
argument_list|(
name|node
argument_list|,
name|context
argument_list|,
name|descriptor
argument_list|,
name|queryMetadata
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|.
name|setResolver
argument_list|(
operator|new
name|ObjectResolver
argument_list|(
name|context
argument_list|,
name|descriptor
argument_list|,
name|queryMetadata
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|==
literal|null
operator|||
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|isPhantom
argument_list|()
condition|)
block|{
name|node
operator|.
name|setParentAttachmentStrategy
argument_list|(
operator|new
name|NoopParentAttachmentStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|node
operator|.
name|isJointPrefetch
argument_list|()
condition|)
block|{
name|node
operator|.
name|setParentAttachmentStrategy
argument_list|(
operator|new
name|StackLookupParentAttachmentStrategy
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|node
operator|.
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
name|node
operator|.
name|setParentAttachmentStrategy
argument_list|(
operator|new
name|JoinedIdParentAttachementStrategy
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|.
name|setParentAttachmentStrategy
argument_list|(
operator|new
name|ResultScanParentAttachmentStrategy
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentNode
operator|!=
literal|null
condition|)
block|{
name|currentNode
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|afterInit
argument_list|()
expr_stmt|;
comment|// push node on stack
if|if
condition|(
name|nodeStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|root
operator|=
name|node
expr_stmt|;
block|}
name|nodeStack
operator|.
name|addLast
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|PrefetchProcessorNode
name|getParent
parameter_list|()
block|{
return|return
operator|(
name|nodeStack
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|null
else|:
name|nodeStack
operator|.
name|getLast
argument_list|()
return|;
block|}
block|}
end_class

end_unit

