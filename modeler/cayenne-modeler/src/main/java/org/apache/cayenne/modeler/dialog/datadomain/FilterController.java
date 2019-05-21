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
name|modeler
operator|.
name|dialog
operator|.
name|datadomain
package|;
end_package

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
name|JTree
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
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|modeler
operator|.
name|ProjectController
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
name|modeler
operator|.
name|ProjectTreeModel
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
name|modeler
operator|.
name|ProjectTreeView
import|;
end_import

begin_class
specifier|public
class|class
name|FilterController
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|filterMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|ProjectTreeView
name|tree
decl_stmt|;
specifier|private
name|ProjectController
name|eventController
decl_stmt|;
specifier|private
name|ProjectTreeModel
name|treeModel
decl_stmt|;
specifier|public
name|ProjectTreeView
name|getTree
parameter_list|()
block|{
return|return
name|tree
return|;
block|}
specifier|public
name|ProjectTreeModel
name|getTreeModel
parameter_list|()
block|{
return|return
name|treeModel
return|;
block|}
specifier|public
name|ProjectController
name|getEventController
parameter_list|()
block|{
return|return
name|eventController
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|getFilterMap
parameter_list|()
block|{
return|return
name|filterMap
return|;
block|}
specifier|public
name|FilterController
parameter_list|(
name|ProjectController
name|eventController
parameter_list|,
name|ProjectTreeView
name|treePanel
parameter_list|)
block|{
name|this
operator|.
name|eventController
operator|=
name|eventController
expr_stmt|;
name|this
operator|.
name|tree
operator|=
name|treePanel
expr_stmt|;
name|this
operator|.
name|treeModel
operator|=
name|tree
operator|.
name|getProjectModel
argument_list|()
expr_stmt|;
name|filterMap
operator|.
name|put
argument_list|(
literal|"dbEntity"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|filterMap
operator|.
name|put
argument_list|(
literal|"objEntity"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|filterMap
operator|.
name|put
argument_list|(
literal|"embeddable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|filterMap
operator|.
name|put
argument_list|(
literal|"procedure"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|filterMap
operator|.
name|put
argument_list|(
literal|"query"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|treeExpOrCollPath
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|TreeNode
name|root
init|=
operator|(
name|TreeNode
operator|)
name|treeModel
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|expandAll
argument_list|(
name|tree
argument_list|,
operator|new
name|TreePath
argument_list|(
name|root
argument_list|)
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|expandAll
parameter_list|(
name|JTree
name|tree
parameter_list|,
name|TreePath
name|parent
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|TreeNode
name|node
init|=
operator|(
name|TreeNode
operator|)
name|parent
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getChildCount
argument_list|()
operator|>=
literal|0
condition|)
block|{
for|for
control|(
name|Enumeration
name|e
init|=
name|node
operator|.
name|children
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|TreeNode
name|n
init|=
operator|(
name|TreeNode
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|TreePath
name|path
init|=
name|parent
operator|.
name|pathByAddingChild
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|expandAll
argument_list|(
name|tree
argument_list|,
name|path
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|"expand"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|tree
operator|.
name|expandPath
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"collapse"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|treeModel
operator|.
name|reload
argument_list|(
name|treeModel
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

