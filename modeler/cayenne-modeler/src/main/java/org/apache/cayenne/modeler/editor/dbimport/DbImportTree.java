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
name|editor
operator|.
name|dbimport
package|;
end_package

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
name|event
operator|.
name|TreeExpansionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeExpansionListener
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
name|function
operator|.
name|BiFunction
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|FilterContainer
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|PatternParam
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Schema
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
name|Application
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
name|action
operator|.
name|LoadDbSchemaAction
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
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|TransferableNode
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DbImportTree
extends|extends
name|JTree
block|{
specifier|private
name|boolean
name|isTransferable
decl_stmt|;
specifier|private
name|ReverseEngineering
name|reverseEngineering
decl_stmt|;
specifier|public
name|DbImportTree
parameter_list|(
name|TreeNode
name|node
parameter_list|)
block|{
name|super
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|createTreeExpandListener
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|translateReverseEngineeringToTree
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|,
name|boolean
name|isTransferable
parameter_list|)
block|{
name|this
operator|.
name|isTransferable
operator|=
name|isTransferable
expr_stmt|;
name|this
operator|.
name|reverseEngineering
operator|=
name|reverseEngineering
expr_stmt|;
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|this
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DbImportTreeNode
name|root
init|=
operator|(
name|DbImportTreeNode
operator|)
name|model
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|root
operator|.
name|removeAllChildren
argument_list|()
expr_stmt|;
name|root
operator|.
name|setUserObject
argument_list|(
name|reverseEngineering
argument_list|)
expr_stmt|;
name|printCatalogs
argument_list|(
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printSchemas
argument_list|(
name|reverseEngineering
operator|.
name|getSchemas
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printIncludeTables
argument_list|(
name|reverseEngineering
operator|.
name|getIncludeTables
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|reverseEngineering
operator|.
name|getExcludeTables
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|reverseEngineering
operator|.
name|getIncludeColumns
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|reverseEngineering
operator|.
name|getExcludeColumns
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|reverseEngineering
operator|.
name|getIncludeProcedures
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|reverseEngineering
operator|.
name|getExcludeProcedures
argument_list|()
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|model
operator|.
name|reload
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|update
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|,
name|BiFunction
argument_list|<
name|FilterContainer
argument_list|,
name|DbImportTreeNode
argument_list|,
name|Void
argument_list|>
name|processor
parameter_list|)
block|{
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|this
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DbImportTreeNode
name|root
init|=
operator|(
name|DbImportTreeNode
operator|)
name|model
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
init|=
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|catalogs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|catalogs
operator|.
name|forEach
argument_list|(
name|catalog
lambda|->
block|{
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
name|catalog
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|schemas
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DbImportTreeNode
name|currentRoot
init|=
name|findNodeInParent
argument_list|(
name|root
argument_list|,
name|catalog
argument_list|)
decl_stmt|;
name|schemas
operator|.
name|forEach
argument_list|(
name|schema
lambda|->
name|packNextFilter
argument_list|(
name|schema
argument_list|,
name|currentRoot
argument_list|,
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|packNextFilter
argument_list|(
name|catalog
argument_list|,
name|root
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reverseEngineering
operator|.
name|getSchemas
argument_list|()
operator|.
name|forEach
argument_list|(
name|schema
lambda|->
name|packNextFilter
argument_list|(
name|schema
argument_list|,
name|root
argument_list|,
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|packNextFilter
parameter_list|(
name|FilterContainer
name|filterContainer
parameter_list|,
name|DbImportTreeNode
name|root
parameter_list|,
name|BiFunction
argument_list|<
name|FilterContainer
argument_list|,
name|DbImportTreeNode
argument_list|,
name|Void
argument_list|>
name|processor
parameter_list|)
block|{
name|DbImportTreeNode
name|container
init|=
name|findNodeInParent
argument_list|(
name|root
argument_list|,
name|filterContainer
argument_list|)
decl_stmt|;
if|if
condition|(
name|container
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|container
operator|.
name|setLoaded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|processor
operator|.
name|apply
argument_list|(
name|filterContainer
argument_list|,
name|container
argument_list|)
expr_stmt|;
block|}
name|void
name|packColumns
parameter_list|(
name|IncludeTable
name|includeTable
parameter_list|,
name|DbImportTreeNode
name|tableNode
parameter_list|)
block|{
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|forEach
argument_list|(
name|column
lambda|->
name|tableNode
operator|.
name|add
argument_list|(
operator|new
name|DbImportTreeNode
argument_list|(
name|column
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DbImportTreeNode
name|findNodeInParent
parameter_list|(
name|DbImportTreeNode
name|parent
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parent
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|(
name|DbImportTreeNode
operator|)
name|parent
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|userObject
init|=
name|node
operator|.
name|getUserObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|Catalog
condition|)
block|{
name|Catalog
name|catalog
init|=
operator|(
name|Catalog
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|userObject
operator|instanceof
name|Catalog
operator|)
condition|)
block|{
continue|continue;
block|}
name|Catalog
name|currentCatalog
init|=
operator|(
name|Catalog
operator|)
name|userObject
decl_stmt|;
if|if
condition|(
name|currentCatalog
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
if|if
condition|(
name|object
operator|instanceof
name|Schema
condition|)
block|{
name|Schema
name|schema
init|=
operator|(
name|Schema
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|userObject
operator|instanceof
name|Schema
operator|)
condition|)
block|{
continue|continue;
block|}
name|Schema
name|currentSchema
init|=
operator|(
name|Schema
operator|)
name|userObject
decl_stmt|;
if|if
condition|(
name|currentSchema
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
if|if
condition|(
name|object
operator|instanceof
name|IncludeTable
condition|)
block|{
name|IncludeTable
name|table
init|=
operator|(
name|IncludeTable
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|userObject
operator|instanceof
name|IncludeTable
operator|)
condition|)
block|{
continue|continue;
block|}
name|IncludeTable
name|currentTable
init|=
operator|(
name|IncludeTable
operator|)
name|userObject
decl_stmt|;
if|if
condition|(
name|currentTable
operator|.
name|getPattern
argument_list|()
operator|.
name|equals
argument_list|(
name|table
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
if|if
condition|(
name|object
operator|instanceof
name|ExcludeTable
condition|)
block|{
name|ExcludeTable
name|table
init|=
operator|(
name|ExcludeTable
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|userObject
operator|instanceof
name|ExcludeTable
operator|)
condition|)
block|{
continue|continue;
block|}
name|ExcludeTable
name|currentTable
init|=
operator|(
name|ExcludeTable
operator|)
name|userObject
decl_stmt|;
if|if
condition|(
name|currentTable
operator|.
name|getPattern
argument_list|()
operator|.
name|equals
argument_list|(
name|table
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|DbImportTreeNode
name|findNodeByParentsChain
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|,
name|DbImportTreeNode
name|movedNode
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|String
name|parentName
init|=
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|movedNode
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getSimpleNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|rootNode
operator|.
name|parentsIsEqual
argument_list|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|movedNode
operator|.
name|getParent
argument_list|()
operator|)
argument_list|)
operator|)
operator|&&
operator|(
name|rootNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|parentName
argument_list|)
operator|)
operator|&&
operator|(
operator|(
name|rootNode
operator|.
name|isCatalog
argument_list|()
operator|)
operator|||
operator|(
name|rootNode
operator|.
name|isSchema
argument_list|()
operator|)
operator|||
operator|(
name|rootNode
operator|.
name|isIncludeTable
argument_list|()
operator|)
operator|)
condition|)
block|{
return|return
name|rootNode
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rootNode
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|childNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|rootNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbImportTreeNode
name|node
init|=
name|findNodeByParentsChain
argument_list|(
name|childNode
argument_list|,
name|movedNode
argument_list|,
name|depth
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|DbImportTreeNode
name|findNode
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|,
name|DbImportTreeNode
name|movedNode
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|String
name|parentName
init|=
name|movedNode
operator|.
name|getSimpleNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|rootNode
operator|.
name|parentsIsEqual
argument_list|(
name|movedNode
argument_list|)
operator|)
operator|&&
operator|(
name|rootNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|parentName
argument_list|)
operator|)
condition|)
block|{
return|return
name|rootNode
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rootNode
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|childNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|rootNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbImportTreeNode
name|node
init|=
name|findNode
argument_list|(
name|childNode
argument_list|,
name|movedNode
argument_list|,
name|depth
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|node
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|// Create list of expanded elements
specifier|private
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|createTreeExpandList
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|,
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|resultList
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rootNode
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|childNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|rootNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|TreePath
name|childPath
init|=
operator|new
name|TreePath
argument_list|(
name|childNode
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isExpanded
argument_list|(
name|childPath
argument_list|)
condition|)
block|{
name|resultList
operator|.
name|add
argument_list|(
name|childNode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|childNode
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|createTreeExpandList
argument_list|(
name|childNode
argument_list|,
name|resultList
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultList
return|;
block|}
specifier|public
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|getTreeExpandList
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|resultList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
return|return
name|createTreeExpandList
argument_list|(
name|getRootNode
argument_list|()
argument_list|,
name|resultList
argument_list|)
return|;
block|}
specifier|private
name|void
name|expandBeginningWithNode
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|,
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rootNode
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|childNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|rootNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|list
operator|.
name|forEach
argument_list|(
parameter_list|(
name|element
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|element
operator|.
name|equals
argument_list|(
name|childNode
argument_list|)
condition|)
block|{
name|this
operator|.
name|expandPath
argument_list|(
operator|new
name|TreePath
argument_list|(
name|childNode
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|childNode
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|expandBeginningWithNode
argument_list|(
name|childNode
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|expandTree
parameter_list|(
name|ArrayList
argument_list|<
name|DbImportTreeNode
argument_list|>
name|expandIndexesList
parameter_list|)
block|{
name|expandBeginningWithNode
argument_list|(
name|getRootNode
argument_list|()
argument_list|,
name|expandIndexesList
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|PatternParam
parameter_list|>
name|void
name|printParams
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|collection
parameter_list|,
name|DbImportTreeNode
name|parent
parameter_list|)
block|{
for|for
control|(
name|T
name|element
range|:
name|collection
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|!
name|isTransferable
condition|?
operator|new
name|DbImportTreeNode
argument_list|(
name|element
argument_list|)
else|:
operator|new
name|TransferableNode
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|parent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|printIncludeTables
parameter_list|(
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|collection
parameter_list|,
name|DbImportTreeNode
name|parent
parameter_list|)
block|{
for|for
control|(
name|IncludeTable
name|includeTable
range|:
name|collection
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|!
name|isTransferable
condition|?
operator|new
name|DbImportTreeNode
argument_list|(
name|includeTable
argument_list|)
else|:
operator|new
name|TransferableNode
argument_list|(
name|includeTable
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
if|if
condition|(
name|isTransferable
operator|&&
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|printParams
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"Loading..."
argument_list|)
argument_list|)
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|printParams
argument_list|(
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|parent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|printChildren
parameter_list|(
name|FilterContainer
name|container
parameter_list|,
name|DbImportTreeNode
name|parent
parameter_list|)
block|{
name|printIncludeTables
argument_list|(
name|container
operator|.
name|getIncludeTables
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|container
operator|.
name|getExcludeTables
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|container
operator|.
name|getIncludeColumns
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|container
operator|.
name|getExcludeColumns
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|container
operator|.
name|getIncludeProcedures
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|printParams
argument_list|(
name|container
operator|.
name|getExcludeProcedures
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printSchemas
parameter_list|(
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
parameter_list|,
name|DbImportTreeNode
name|parent
parameter_list|)
block|{
for|for
control|(
name|Schema
name|schema
range|:
name|schemas
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|!
name|isTransferable
condition|?
operator|new
name|DbImportTreeNode
argument_list|(
name|schema
argument_list|)
else|:
operator|new
name|TransferableNode
argument_list|(
name|schema
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
if|if
condition|(
name|isTransferable
operator|&&
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|schema
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|printParams
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"Loading..."
argument_list|)
argument_list|)
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|printChildren
argument_list|(
name|schema
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|parent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|printCatalogs
parameter_list|(
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
parameter_list|,
name|DbImportTreeNode
name|parent
parameter_list|)
block|{
for|for
control|(
name|Catalog
name|catalog
range|:
name|catalogs
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|!
name|isTransferable
condition|?
operator|new
name|DbImportTreeNode
argument_list|(
name|catalog
argument_list|)
else|:
operator|new
name|TransferableNode
argument_list|(
name|catalog
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
if|if
condition|(
name|isTransferable
operator|&&
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|catalog
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|catalog
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|printParams
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"Loading..."
argument_list|)
argument_list|)
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|printSchemas
argument_list|(
name|catalog
operator|.
name|getSchemas
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|printChildren
argument_list|(
name|catalog
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|parent
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createTreeExpandListener
parameter_list|()
block|{
name|TreeExpansionListener
name|treeExpansionListener
init|=
operator|new
name|TreeExpansionListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|treeExpanded
parameter_list|(
name|TreeExpansionEvent
name|event
parameter_list|)
block|{
name|TreePath
name|path
init|=
name|event
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|Object
name|lastPathComponent
init|=
name|path
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|lastPathComponent
operator|instanceof
name|TransferableNode
operator|)
condition|)
block|{
return|return;
block|}
name|DbImportTreeNode
name|node
init|=
operator|(
name|DbImportTreeNode
operator|)
name|lastPathComponent
decl_stmt|;
if|if
condition|(
operator|(
name|node
operator|.
name|isIncludeTable
argument_list|()
operator|||
name|node
operator|.
name|isSchema
argument_list|()
operator|||
name|node
operator|.
name|isCatalog
argument_list|()
operator|)
operator|&&
operator|!
name|node
operator|.
name|isLoaded
argument_list|()
condition|)
block|{
comment|//reload tables and columns action.
name|LoadDbSchemaAction
name|action
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|LoadDbSchemaAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|action
operator|.
name|performAction
argument_list|(
literal|null
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|treeCollapsed
parameter_list|(
name|TreeExpansionEvent
name|event
parameter_list|)
block|{
block|}
block|}
decl_stmt|;
name|this
operator|.
name|addTreeExpansionListener
argument_list|(
name|treeExpansionListener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbImportTreeNode
name|getSelectedNode
parameter_list|()
block|{
return|return
operator|(
name|DbImportTreeNode
operator|)
name|this
operator|.
name|getSelectionPath
argument_list|()
operator|.
name|getLastPathComponent
argument_list|()
return|;
block|}
specifier|public
name|DbImportTreeNode
name|getRootNode
parameter_list|()
block|{
return|return
operator|(
name|DbImportTreeNode
operator|)
name|this
operator|.
name|getModel
argument_list|()
operator|.
name|getRoot
argument_list|()
return|;
block|}
specifier|public
name|ReverseEngineering
name|getReverseEngineering
parameter_list|()
block|{
return|return
name|reverseEngineering
return|;
block|}
specifier|public
name|void
name|setReverseEngineering
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|this
operator|.
name|reverseEngineering
operator|=
name|reverseEngineering
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTransferable
parameter_list|()
block|{
return|return
name|isTransferable
return|;
block|}
block|}
end_class

end_unit

