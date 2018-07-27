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
name|ExpressionFactory
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
name|DbJoin
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
name|DbRelationship
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
name|ObjRelationship
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
name|PrefetchSelectQuery
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
name|PrefetchProcessorNode
name|synchronizedRootResultNodeFromDataRows
parameter_list|(
name|PrefetchTreeNode
name|tree
parameter_list|,
name|List
argument_list|<
name|DataRow
argument_list|>
name|mainResultRows
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
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
specifier|private
name|PrefetchProcessorNode
name|resolveObjectTree
parameter_list|(
name|PrefetchTreeNode
name|tree
parameter_list|,
name|List
argument_list|<
name|DataRow
argument_list|>
name|mainResultRows
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
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
return|;
block|}
specifier|final
class|class
name|DisjointProcessor
implements|implements
name|PrefetchProcessor
block|{
annotation|@
name|Override
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
comment|// continue with processing even if the objects list is empty to handle multi-step prefetches.
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
argument_list|<
name|Persistent
argument_list|>
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
annotation|@
name|Override
specifier|public
name|boolean
name|startDisjointByIdPrefetch
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
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|isPhantom
argument_list|()
condition|)
block|{
comment|// TODO: doing nothing in current implementation if parent node is phantom
return|return
literal|true
return|;
block|}
name|PrefetchProcessorNode
name|parentProcessorNode
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|processorNode
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
name|processorNode
operator|.
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
name|DbRelationship
name|lastDbRelationship
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|pathPrefix
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|dbRelationships
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// we need path prefix for flattened relationships
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|dbRelationships
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|1
condition|;
name|i
operator|--
control|)
block|{
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
literal|"."
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|dbRelationships
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|pathPrefix
operator|=
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|parentDataRows
decl_stmt|;
comment|// note that a disjoint prefetch that has adjacent joint prefetches
comment|// will be a PrefetchProcessorJointNode, so here check for semantics, not node type
if|if
condition|(
name|parentProcessorNode
operator|.
name|getSemantics
argument_list|()
operator|==
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
condition|)
block|{
name|parentDataRows
operator|=
operator|(
operator|(
name|PrefetchProcessorJointNode
operator|)
name|parentProcessorNode
operator|)
operator|.
name|getResolvedRows
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|parentDataRows
operator|=
name|parentProcessorNode
operator|.
name|getDataRows
argument_list|()
expr_stmt|;
block|}
name|int
name|maxIdQualifierSize
init|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getMaxIdQualifierSize
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PrefetchSelectQuery
argument_list|>
name|queries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|qualifiersCount
init|=
literal|0
decl_stmt|;
name|PrefetchSelectQuery
name|currentQuery
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|lastDbRelationship
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|values
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|dataRow
range|:
name|parentDataRows
control|)
block|{
comment|// handling too big qualifiers
if|if
condition|(
name|currentQuery
operator|==
literal|null
operator|||
operator|(
name|maxIdQualifierSize
operator|>
literal|0
operator|&&
name|qualifiersCount
operator|+
name|joins
operator|.
name|size
argument_list|()
operator|>
name|maxIdQualifierSize
operator|)
condition|)
block|{
name|createDisjointByIdPrefetchQualifier
argument_list|(
name|pathPrefix
argument_list|,
name|currentQuery
argument_list|,
name|joins
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|currentQuery
operator|=
operator|new
name|PrefetchSelectQuery
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
name|queries
operator|.
name|add
argument_list|(
name|currentQuery
argument_list|)
expr_stmt|;
name|qualifiersCount
operator|=
literal|0
expr_stmt|;
name|values
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|joinValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|joins
operator|.
name|size
argument_list|()
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
name|targetValue
init|=
operator|(
operator|(
name|DataRow
operator|)
name|dataRow
operator|)
operator|.
name|get
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
name|joinValues
operator|.
name|add
argument_list|(
name|targetValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|values
operator|.
name|add
argument_list|(
name|joinValues
argument_list|)
condition|)
block|{
name|qualifiersCount
operator|+=
name|joins
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
comment|// add final part of values
name|createDisjointByIdPrefetchQualifier
argument_list|(
name|pathPrefix
argument_list|,
name|currentQuery
argument_list|,
name|joins
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|jointSubtree
init|=
name|node
operator|.
name|cloneJointSubtree
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|dataRows
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PrefetchSelectQuery
name|query
range|:
name|queries
control|)
block|{
comment|// need to pass the remaining tree to make joint prefetches work
if|if
condition|(
name|jointSubtree
operator|.
name|hasChildren
argument_list|()
condition|)
block|{
name|query
operator|.
name|setPrefetchTree
argument_list|(
name|jointSubtree
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
comment|// setup extra result columns to be able to relate result rows to the
comment|// parent result objects.
name|query
operator|.
name|addResultPath
argument_list|(
literal|"db:"
operator|+
name|relationship
operator|.
name|getReverseDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|DataRow
argument_list|>
name|dataRowList
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|dataRows
operator|.
name|addAll
argument_list|(
name|dataRowList
argument_list|)
expr_stmt|;
block|}
name|processorNode
operator|.
name|setDataRows
argument_list|(
name|dataRows
argument_list|)
expr_stmt|;
return|return
name|startDisjointPrefetch
argument_list|(
name|node
argument_list|)
return|;
block|}
specifier|private
name|void
name|createDisjointByIdPrefetchQualifier
parameter_list|(
name|String
name|pathPrefix
parameter_list|,
name|PrefetchSelectQuery
name|currentQuery
parameter_list|,
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
parameter_list|,
name|Set
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|values
parameter_list|)
block|{
name|Expression
name|allJoinsQualifier
decl_stmt|;
if|if
condition|(
name|currentQuery
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|List
argument_list|<
name|Object
argument_list|>
name|joinValues
range|:
name|values
control|)
block|{
name|allJoinsQualifier
operator|=
literal|null
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
name|joins
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Expression
name|joinQualifier
init|=
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|pathPrefix
operator|+
name|joins
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|joinValues
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|allJoinsQualifier
operator|==
literal|null
condition|)
block|{
name|allJoinsQualifier
operator|=
name|joinQualifier
expr_stmt|;
block|}
else|else
block|{
name|allJoinsQualifier
operator|=
name|allJoinsQualifier
operator|.
name|andExp
argument_list|(
name|joinQualifier
argument_list|)
expr_stmt|;
block|}
block|}
name|currentQuery
operator|.
name|orQualifier
argument_list|(
name|allJoinsQualifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
argument_list|<
name|Persistent
argument_list|>
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
annotation|@
name|Override
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
annotation|@
name|Override
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
literal|"Unknown prefetch node: %s"
argument_list|,
name|node
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
operator|(
name|node
operator|.
name|isDisjointPrefetch
argument_list|()
operator|||
name|node
operator|.
name|isDisjointByIdPrefetch
argument_list|()
operator|)
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
argument_list|<>
argument_list|(
name|objects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|objects
operator|.
name|removeIf
argument_list|(
name|persistent
lambda|->
operator|!
name|seen
operator|.
name|add
argument_list|(
name|persistent
argument_list|)
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
name|startDisjointPrefetch
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Persistent
name|object
init|=
name|processorNode
operator|.
name|getResolved
argument_list|(
name|id
argument_list|)
decl_stmt|;
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
comment|// linking by parent needed even if an object is already there (many-to-many case)
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
annotation|@
name|Override
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
annotation|@
name|Override
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
literal|"Unknown prefetch node: %s"
argument_list|,
name|node
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
name|startDisjointPrefetch
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
literal|"Unknown prefetch node: %s"
argument_list|,
name|node
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

