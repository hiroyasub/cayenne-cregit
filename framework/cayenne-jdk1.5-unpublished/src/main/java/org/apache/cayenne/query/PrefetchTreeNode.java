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
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectStreamException
import|;
end_import

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
name|StringTokenizer
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
name|Entity
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

begin_comment
comment|/**  * Defines a node in a prefetch tree.  *  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|PrefetchTreeNode
implements|implements
name|Serializable
implements|,
name|XMLSerializable
block|{
specifier|public
specifier|static
specifier|final
name|int
name|UNDEFINED_SEMANTICS
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|JOINT_PREFETCH_SEMANTICS
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DISJOINT_PREFETCH_SEMANTICS
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
init|=
literal|3
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|boolean
name|phantom
decl_stmt|;
specifier|protected
name|int
name|semantics
decl_stmt|;
specifier|protected
name|String
name|ejbqlPathEntityId
decl_stmt|;
specifier|protected
name|String
name|entityName
decl_stmt|;
comment|// transient parent allows cloning parts of the tree via serialization
specifier|protected
specifier|transient
name|PrefetchTreeNode
name|parent
decl_stmt|;
comment|// Using Collection instead of Map for children storage (even though there cases of
comment|// lookup by segment) is a reasonable tradeoff considering that
comment|// each node has no more than a few children and lookup by name doesn't happen on
comment|// traversal, only during creation.
specifier|protected
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|children
decl_stmt|;
comment|/**      * Creates a root node of the prefetch tree. Children can be added to the parent by      * calling "addPath".      */
specifier|public
name|PrefetchTreeNode
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a phantom PrefetchTreeNode, initializing it with parent node and a name of      * a relationship segment connecting this node with the parent.      */
specifier|protected
name|PrefetchTreeNode
parameter_list|(
name|PrefetchTreeNode
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|phantom
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|semantics
operator|=
name|UNDEFINED_SEMANTICS
expr_stmt|;
block|}
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|traverse
argument_list|(
operator|new
name|XMLEncoderOperation
argument_list|(
name|encoder
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the root of the node tree. Root is the topmost parent node that itself has      * no parent set.      */
specifier|public
name|PrefetchTreeNode
name|getRoot
parameter_list|()
block|{
return|return
operator|(
name|parent
operator|!=
literal|null
operator|)
condition|?
name|parent
operator|.
name|getRoot
argument_list|()
else|:
name|this
return|;
block|}
comment|/**      * Returns full prefetch path, that is a dot separated String of node names starting      * from root and up to and including this node. Note that root "name" is considered to      * be an empty string.      */
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|getPath
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|(
name|PrefetchTreeNode
name|upTillParent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|==
literal|null
operator|||
name|upTillParent
operator|==
name|this
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|PrefetchTreeNode
name|node
init|=
name|this
operator|.
name|getParent
argument_list|()
decl_stmt|;
comment|// root node has no path
while|while
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
name|node
operator|!=
name|upTillParent
condition|)
block|{
name|path
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
name|node
operator|.
name|getName
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
name|node
operator|=
name|node
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a subset of nodes with "joint" semantics that are to be prefetched in the      * same query as the current node. Result excludes this node, regardless of its      * semantics.      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|adjacentJointNodes
parameter_list|()
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|()
decl_stmt|;
name|traverse
argument_list|(
operator|new
name|AdjacentJoinsOperation
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
comment|/**      * Returns a collection of PrefetchTreeNodes in this tree with joint semantics.      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|jointNodes
parameter_list|()
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|()
decl_stmt|;
name|traverse
argument_list|(
operator|new
name|CollectionBuilderOperation
argument_list|(
name|c
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
comment|/**      * Returns a collection of PrefetchTreeNodes with disjoint semantics.      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|disjointNodes
parameter_list|()
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|()
decl_stmt|;
name|traverse
argument_list|(
operator|new
name|CollectionBuilderOperation
argument_list|(
name|c
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
comment|/**      * Returns a collection of PrefetchTreeNodes with disjoint semantics      * @since 3.1      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|disjointByIdNodes
parameter_list|()
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|()
decl_stmt|;
name|traverse
argument_list|(
operator|new
name|CollectionBuilderOperation
argument_list|(
name|c
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
comment|/**      * Returns a collection of PrefetchTreeNodes that are not phantoms.      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|nonPhantomNodes
parameter_list|()
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|()
decl_stmt|;
name|traverse
argument_list|(
operator|new
name|CollectionBuilderOperation
argument_list|(
name|c
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
comment|/**      * Returns a clone of subtree that includes all joint children      * starting from this node itself and till the first occurrence of non-joint node      *      * @since 3.1      */
specifier|public
name|PrefetchTreeNode
name|cloneJointSubtree
parameter_list|()
block|{
return|return
name|cloneJointSubtree
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|private
name|PrefetchTreeNode
name|cloneJointSubtree
parameter_list|(
name|PrefetchTreeNode
name|parent
parameter_list|)
block|{
name|PrefetchTreeNode
name|cloned
init|=
operator|new
name|PrefetchTreeNode
argument_list|(
name|parent
argument_list|,
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|cloned
operator|.
name|setSemantics
argument_list|(
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setPhantom
argument_list|(
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|child
operator|.
name|isJointPrefetch
argument_list|()
condition|)
block|{
name|cloned
operator|.
name|addChild
argument_list|(
name|child
operator|.
name|cloneJointSubtree
argument_list|(
name|cloned
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|cloned
return|;
block|}
comment|/**      * Traverses the tree depth-first, invoking callback methods of the processor when      * passing through the nodes.      */
specifier|public
name|void
name|traverse
parameter_list|(
name|PrefetchProcessor
name|processor
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|isPhantom
argument_list|()
condition|)
block|{
name|result
operator|=
name|processor
operator|.
name|startPhantomPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|isDisjointPrefetch
argument_list|()
condition|)
block|{
name|result
operator|=
name|processor
operator|.
name|startDisjointPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|isDisjointByIdPrefetch
argument_list|()
condition|)
block|{
name|result
operator|=
name|processor
operator|.
name|startDisjointByIdPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|isJointPrefetch
argument_list|()
condition|)
block|{
name|result
operator|=
name|processor
operator|.
name|startJointPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|processor
operator|.
name|startUnknownPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|// process children unless processing is blocked...
if|if
condition|(
name|result
operator|&&
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|child
range|:
name|children
control|)
block|{
name|child
operator|.
name|traverse
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
comment|// call finish regardless of whether children were processed
name|processor
operator|.
name|finishPrefetch
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Looks up an existing node in the tree desribed by the dot-separated path. Will      * return null if no matching child exists.      */
specifier|public
name|PrefetchTreeNode
name|getNode
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty path: "
operator|+
name|path
argument_list|)
throw|;
block|}
name|PrefetchTreeNode
name|node
init|=
name|this
decl_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|path
argument_list|,
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
while|while
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
operator|&&
name|node
operator|!=
literal|null
condition|)
block|{
name|String
name|segment
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|node
operator|=
name|node
operator|.
name|getChild
argument_list|(
name|segment
argument_list|)
expr_stmt|;
block|}
return|return
name|node
return|;
block|}
comment|/**      * Adds a "path" with specified semantics to this prefetch node. All yet non-existent      * nodes in the created path will be marked as phantom.      *      * @return the last segment in the created path.      */
specifier|public
name|PrefetchTreeNode
name|addPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty path: "
operator|+
name|path
argument_list|)
throw|;
block|}
name|PrefetchTreeNode
name|node
init|=
name|this
decl_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|path
argument_list|,
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
while|while
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|segment
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|child
init|=
name|node
operator|.
name|getChild
argument_list|(
name|segment
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
name|child
operator|=
operator|new
name|PrefetchTreeNode
argument_list|(
name|node
argument_list|,
name|segment
argument_list|)
expr_stmt|;
name|node
operator|.
name|addChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
name|node
operator|=
name|child
expr_stmt|;
block|}
return|return
name|node
return|;
block|}
comment|/**      * Removes or makes phantom a node defined by this path. If the node for this path      * doesn't have any children, it is removed, otherwise it is made phantom.      */
specifier|public
name|void
name|removePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|PrefetchTreeNode
name|node
init|=
name|getNode
argument_list|(
name|path
argument_list|)
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|node
operator|.
name|children
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|setPhantom
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
name|String
name|segment
init|=
name|node
operator|.
name|getName
argument_list|()
decl_stmt|;
name|node
operator|=
name|node
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|removeChild
argument_list|(
name|segment
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addChild
parameter_list|(
name|PrefetchTreeNode
name|child
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|child
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Child has no segmentPath: "
operator|+
name|child
argument_list|)
throw|;
block|}
if|if
condition|(
name|child
operator|.
name|getParent
argument_list|()
operator|!=
name|this
condition|)
block|{
name|child
operator|.
name|getParent
argument_list|()
operator|.
name|removeChild
argument_list|(
name|child
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|child
operator|.
name|parent
operator|=
name|this
expr_stmt|;
block|}
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|ArrayList
argument_list|<
name|PrefetchTreeNode
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
name|children
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeChild
parameter_list|(
name|PrefetchTreeNode
name|child
parameter_list|)
block|{
if|if
condition|(
name|children
operator|!=
literal|null
operator|&&
name|child
operator|!=
literal|null
condition|)
block|{
name|children
operator|.
name|remove
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|child
operator|.
name|parent
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|removeChild
parameter_list|(
name|String
name|segment
parameter_list|)
block|{
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
name|PrefetchTreeNode
name|child
init|=
name|getChild
argument_list|(
name|segment
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|children
operator|.
name|remove
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|child
operator|.
name|parent
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|PrefetchTreeNode
name|getChild
parameter_list|(
name|String
name|segment
parameter_list|)
block|{
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|segment
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/**      * Returns an unmodifiable collection of children.      */
specifier|public
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
operator|==
literal|null
condition|?
name|Collections
operator|.
name|EMPTY_SET
else|:
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|children
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasChildren
parameter_list|()
block|{
return|return
name|children
operator|!=
literal|null
operator|&&
operator|!
name|children
operator|.
name|isEmpty
argument_list|()
return|;
block|}
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
name|boolean
name|isPhantom
parameter_list|()
block|{
return|return
name|phantom
return|;
block|}
specifier|public
name|void
name|setPhantom
parameter_list|(
name|boolean
name|phantom
parameter_list|)
block|{
name|this
operator|.
name|phantom
operator|=
name|phantom
expr_stmt|;
block|}
specifier|public
name|int
name|getSemantics
parameter_list|()
block|{
return|return
name|semantics
return|;
block|}
specifier|public
name|void
name|setSemantics
parameter_list|(
name|int
name|semantics
parameter_list|)
block|{
name|this
operator|.
name|semantics
operator|=
name|semantics
expr_stmt|;
block|}
specifier|public
name|boolean
name|isJointPrefetch
parameter_list|()
block|{
return|return
name|semantics
operator|==
name|JOINT_PREFETCH_SEMANTICS
return|;
block|}
specifier|public
name|boolean
name|isDisjointPrefetch
parameter_list|()
block|{
return|return
name|semantics
operator|==
name|DISJOINT_PREFETCH_SEMANTICS
return|;
block|}
specifier|public
name|boolean
name|isDisjointByIdPrefetch
parameter_list|()
block|{
return|return
name|semantics
operator|==
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
return|;
block|}
specifier|public
name|String
name|getEjbqlPathEntityId
parameter_list|()
block|{
return|return
name|ejbqlPathEntityId
return|;
block|}
specifier|public
name|void
name|setEjbqlPathEntityId
parameter_list|(
name|String
name|ejbqlPathEntityId
parameter_list|)
block|{
name|this
operator|.
name|ejbqlPathEntityId
operator|=
name|ejbqlPathEntityId
expr_stmt|;
block|}
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
specifier|public
name|void
name|setEntityName
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
block|}
comment|// **** custom serialization that supports serializing subtrees...
comment|// implementing 'readResolve' instead of 'readObject' so that this would work with
comment|// hessian
specifier|private
name|Object
name|readResolve
parameter_list|()
throws|throws
name|ObjectStreamException
block|{
if|if
condition|(
name|hasChildren
argument_list|()
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|child
range|:
name|children
control|)
block|{
name|child
operator|.
name|parent
operator|=
name|this
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
comment|// **** common tree operations
comment|// An operation that encodes prefetch tree as XML.
class|class
name|XMLEncoderOperation
implements|implements
name|PrefetchProcessor
block|{
name|XMLEncoder
name|encoder
decl_stmt|;
name|XMLEncoderOperation
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|this
operator|.
name|encoder
operator|=
name|encoder
expr_stmt|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
comment|// don't encode phantoms
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<prefetch type=\"disjoint\">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</prefetch>"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<prefetch type=\"disjointById\">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</prefetch>"
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
name|encoder
operator|.
name|print
argument_list|(
literal|"<prefetch type=\"joint\">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</prefetch>"
argument_list|)
expr_stmt|;
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
name|encoder
operator|.
name|print
argument_list|(
literal|"<prefetch>"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</prefetch>"
argument_list|)
expr_stmt|;
return|return
literal|true
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
comment|// noop
block|}
block|}
comment|// An operation that collects all nodes in a single collection.
class|class
name|CollectionBuilderOperation
implements|implements
name|PrefetchProcessor
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|nodes
decl_stmt|;
name|boolean
name|includePhantom
decl_stmt|;
name|boolean
name|includeDisjoint
decl_stmt|;
name|boolean
name|includeDisjointById
decl_stmt|;
name|boolean
name|includeJoint
decl_stmt|;
name|boolean
name|includeUnknown
decl_stmt|;
name|CollectionBuilderOperation
parameter_list|(
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|nodes
parameter_list|,
name|boolean
name|includeDisjoint
parameter_list|,
name|boolean
name|includeDisjointById
parameter_list|,
name|boolean
name|includeJoint
parameter_list|,
name|boolean
name|includeUnknown
parameter_list|,
name|boolean
name|includePhantom
parameter_list|)
block|{
name|this
operator|.
name|nodes
operator|=
name|nodes
expr_stmt|;
name|this
operator|.
name|includeDisjoint
operator|=
name|includeDisjoint
expr_stmt|;
name|this
operator|.
name|includeDisjointById
operator|=
name|includeDisjointById
expr_stmt|;
name|this
operator|.
name|includeJoint
operator|=
name|includeJoint
expr_stmt|;
name|this
operator|.
name|includeUnknown
operator|=
name|includeUnknown
expr_stmt|;
name|this
operator|.
name|includePhantom
operator|=
name|includePhantom
expr_stmt|;
block|}
specifier|public
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|includePhantom
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|includeDisjoint
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|includeDisjointById
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|includeJoint
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|includeUnknown
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
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
block|}
block|}
class|class
name|AdjacentJoinsOperation
implements|implements
name|PrefetchProcessor
block|{
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|nodes
decl_stmt|;
name|AdjacentJoinsOperation
parameter_list|(
name|Collection
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|nodes
parameter_list|)
block|{
name|this
operator|.
name|nodes
operator|=
name|nodes
expr_stmt|;
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
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
return|return
name|node
operator|==
name|PrefetchTreeNode
operator|.
name|this
return|;
block|}
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
specifier|public
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|!=
name|PrefetchTreeNode
operator|.
name|this
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
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
return|return
name|node
operator|==
name|PrefetchTreeNode
operator|.
name|this
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
block|}
block|}
block|}
end_class

end_unit

