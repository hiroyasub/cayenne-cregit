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
name|modeler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultMutableTreeNode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultTreeModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|MutableTreeNode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeNode
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
name|access
operator|.
name|DataDomain
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
name|access
operator|.
name|DataNode
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
name|DataMap
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
name|project
operator|.
name|Project
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
name|project
operator|.
name|ProjectPath
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
name|project
operator|.
name|ProjectTraversal
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
name|project
operator|.
name|ProjectTraversalHandler
import|;
end_import

begin_comment
comment|/**  * ProjectTreeModel is a model of Cayenne project tree.  *   */
end_comment

begin_class
specifier|public
class|class
name|ProjectTreeModel
extends|extends
name|DefaultTreeModel
block|{
comment|/**      * Creates a tree of Swing TreeNodes wrapping Cayenne project. Returns the root node      * of the tree.      */
specifier|public
specifier|static
name|DefaultMutableTreeNode
name|wrapProject
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
return|return
name|wrapProjectNode
argument_list|(
name|project
argument_list|)
return|;
block|}
comment|/**      * Creates a tree of Swing TreeNodes wrapping Cayenne project object. Returns the root      * node of the tree.      */
specifier|public
specifier|static
name|DefaultMutableTreeNode
name|wrapProjectNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|TraversalHelper
name|helper
init|=
operator|new
name|TraversalHelper
argument_list|()
decl_stmt|;
operator|new
name|ProjectTraversal
argument_list|(
name|helper
argument_list|,
literal|true
argument_list|)
operator|.
name|traverse
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|helper
operator|.
name|getStartNode
argument_list|()
return|;
block|}
comment|/**      * Creates a tree of Swing TreeNodes wrapping Cayenne project object. Returns the root      * node of the tree.      */
specifier|public
specifier|static
name|DefaultMutableTreeNode
name|wrapProjectNode
parameter_list|(
name|Object
name|node
parameter_list|,
name|DefaultMutableTreeNode
name|parentPath
parameter_list|)
block|{
name|TraversalHelper
name|helper
init|=
operator|new
name|TraversalHelper
argument_list|()
decl_stmt|;
comment|// build a project path from tree node
name|ProjectPath
name|path
init|=
operator|new
name|ProjectPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentPath
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
name|helper
operator|.
name|registerNodes
argument_list|(
name|parentPath
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|new
name|ProjectTraversal
argument_list|(
name|helper
argument_list|)
operator|.
name|traverse
argument_list|(
name|node
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|helper
operator|.
name|getStartNode
argument_list|()
return|;
block|}
comment|/**      * Constructor for ProjectTreeModel.      *       * @param root      */
specifier|public
name|ProjectTreeModel
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|super
argument_list|(
name|wrapProject
argument_list|(
name|project
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Re-inserts a tree node to preserve the correct ordering of items. Assumes that the      * tree is already ordered, except for one node.      */
specifier|public
name|void
name|positionNode
parameter_list|(
name|MutableTreeNode
name|parent
parameter_list|,
name|DefaultMutableTreeNode
name|treeNode
parameter_list|,
name|Comparator
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|treeNode
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|parent
operator|==
literal|null
operator|&&
name|treeNode
operator|!=
name|getRoot
argument_list|()
condition|)
block|{
name|parent
operator|=
operator|(
name|MutableTreeNode
operator|)
name|treeNode
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
name|getRootNode
argument_list|()
expr_stmt|;
block|}
block|}
name|Object
name|object
init|=
name|treeNode
operator|.
name|getUserObject
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|parent
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
name|int
name|ins
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|rm
init|=
operator|-
literal|1
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DefaultMutableTreeNode
name|node
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|parent
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// remember to remove node
if|if
condition|(
name|node
operator|==
name|treeNode
condition|)
block|{
name|rm
operator|=
name|i
expr_stmt|;
continue|continue;
block|}
comment|// no more insert checks
if|if
condition|(
name|ins
operator|>=
literal|0
condition|)
block|{
continue|continue;
block|}
comment|// ObjEntities go before DbEntities
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|object
argument_list|,
name|node
operator|.
name|getUserObject
argument_list|()
argument_list|)
operator|<=
literal|0
condition|)
block|{
name|ins
operator|=
name|i
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ins
operator|<
literal|0
condition|)
block|{
name|ins
operator|=
name|len
expr_stmt|;
block|}
if|if
condition|(
name|rm
operator|==
name|ins
condition|)
block|{
return|return;
block|}
comment|// remove
if|if
condition|(
name|rm
operator|>=
literal|0
condition|)
block|{
name|removeNodeFromParent
argument_list|(
name|treeNode
argument_list|)
expr_stmt|;
if|if
condition|(
name|rm
operator|<
name|ins
condition|)
block|{
name|ins
operator|--
expr_stmt|;
block|}
block|}
comment|// insert
name|insertNodeInto
argument_list|(
name|treeNode
argument_list|,
name|parent
argument_list|,
name|ins
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns root node cast into DefaultMutableTreeNode.      */
specifier|public
name|DefaultMutableTreeNode
name|getRootNode
parameter_list|()
block|{
return|return
operator|(
name|DefaultMutableTreeNode
operator|)
name|super
operator|.
name|getRoot
argument_list|()
return|;
block|}
specifier|public
name|DefaultMutableTreeNode
name|getNodeForObjectPath
parameter_list|(
name|Object
index|[]
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DefaultMutableTreeNode
name|currentNode
init|=
name|getRootNode
argument_list|()
decl_stmt|;
comment|// adjust for root node being in the path
name|int
name|start
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|currentNode
operator|.
name|getUserObject
argument_list|()
operator|==
name|path
index|[
literal|0
index|]
condition|)
block|{
name|start
operator|=
literal|1
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|path
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DefaultMutableTreeNode
name|foundNode
init|=
literal|null
decl_stmt|;
name|Enumeration
name|children
init|=
name|currentNode
operator|.
name|children
argument_list|()
decl_stmt|;
while|while
condition|(
name|children
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|DefaultMutableTreeNode
name|child
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|children
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getUserObject
argument_list|()
operator|==
name|path
index|[
name|i
index|]
condition|)
block|{
name|foundNode
operator|=
name|child
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|foundNode
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|currentNode
operator|=
name|foundNode
expr_stmt|;
block|}
block|}
return|return
name|currentNode
return|;
block|}
specifier|static
class|class
name|TraversalHelper
implements|implements
name|ProjectTraversalHandler
block|{
specifier|protected
name|DefaultMutableTreeNode
name|startNode
decl_stmt|;
specifier|protected
name|Map
name|nodesMap
decl_stmt|;
specifier|public
name|TraversalHelper
parameter_list|()
block|{
name|this
operator|.
name|nodesMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DefaultMutableTreeNode
name|getStartNode
parameter_list|()
block|{
return|return
name|startNode
return|;
block|}
comment|/**          * Creates a starting point for tree traversal.          */
specifier|public
name|ProjectPath
name|registerNodes
parameter_list|(
name|TreeNode
index|[]
name|nodes
parameter_list|)
block|{
name|ProjectPath
name|path
init|=
operator|new
name|ProjectPath
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
name|nodes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DefaultMutableTreeNode
name|treeNode
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|nodes
index|[
name|i
index|]
decl_stmt|;
name|path
operator|=
name|path
operator|.
name|appendToPath
argument_list|(
name|treeNode
operator|.
name|getUserObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// register node with helper
name|registerNode
argument_list|(
name|treeNode
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
specifier|public
name|void
name|registerNode
parameter_list|(
name|DefaultMutableTreeNode
name|node
parameter_list|)
block|{
name|nodesMap
operator|.
name|put
argument_list|(
name|node
operator|.
name|getUserObject
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|projectNode
parameter_list|(
name|ProjectPath
name|nodePath
parameter_list|)
block|{
name|Object
name|parent
init|=
name|nodePath
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
name|Object
name|nodeObj
init|=
name|nodePath
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|DefaultMutableTreeNode
name|node
init|=
operator|new
name|DefaultMutableTreeNode
argument_list|(
name|nodeObj
argument_list|)
decl_stmt|;
if|if
condition|(
name|startNode
operator|==
literal|null
condition|)
block|{
name|startNode
operator|=
name|node
expr_stmt|;
block|}
else|else
block|{
name|DefaultMutableTreeNode
name|nodeParent
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|nodesMap
operator|.
name|get
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|nodeParent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|registerNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|shouldReadChildren
parameter_list|(
name|Object
name|node
parameter_list|,
name|ProjectPath
name|parentPath
parameter_list|)
block|{
comment|// do not read deatils of linked maps
if|if
condition|(
operator|(
name|node
operator|instanceof
name|DataMap
operator|)
operator|&&
name|parentPath
operator|!=
literal|null
operator|&&
operator|(
name|parentPath
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DataNode
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
name|node
operator|instanceof
name|Project
operator|)
operator|||
operator|(
name|node
operator|instanceof
name|DataDomain
operator|)
operator|||
operator|(
name|node
operator|instanceof
name|DataMap
operator|)
operator|||
operator|(
name|node
operator|instanceof
name|DataNode
operator|)
return|;
block|}
block|}
comment|/**      * Traversal hanlder that rebuilds the tree from another tree. Used to reorder tree      * nodes.      */
class|class
name|CopyTraversalHelper
extends|extends
name|TraversalHelper
block|{
specifier|public
name|void
name|projectNode
parameter_list|(
name|ProjectPath
name|nodePath
parameter_list|)
block|{
name|DefaultMutableTreeNode
name|node
decl_stmt|;
if|if
condition|(
name|startNode
operator|==
literal|null
condition|)
block|{
name|startNode
operator|=
operator|new
name|DefaultMutableTreeNode
argument_list|(
name|nodePath
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|=
name|startNode
expr_stmt|;
block|}
else|else
block|{
name|DefaultMutableTreeNode
name|original
init|=
name|ProjectTreeModel
operator|.
name|this
operator|.
name|getNodeForObjectPath
argument_list|(
name|nodePath
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultMutableTreeNode
name|nodeParent
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|nodesMap
operator|.
name|get
argument_list|(
name|nodePath
operator|.
name|getObjectParent
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|=
operator|new
name|DefaultMutableTreeNode
argument_list|(
name|original
operator|.
name|getUserObject
argument_list|()
argument_list|)
expr_stmt|;
name|nodeParent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|registerNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

