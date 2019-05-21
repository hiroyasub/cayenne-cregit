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
name|ExcludeColumn
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
name|ExcludeProcedure
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
name|IncludeProcedure
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
name|awt
operator|.
name|Color
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
class|class
name|DbImportNodeHandler
block|{
specifier|private
specifier|static
specifier|final
name|Color
name|ACCEPT_COLOR
init|=
operator|new
name|Color
argument_list|(
literal|60
argument_list|,
literal|179
argument_list|,
literal|113
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Color
name|EXCLUDE_COLOR
init|=
operator|new
name|Color
argument_list|(
literal|178
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Color
name|NON_INCLUDE_COLOR
init|=
name|Color
operator|.
name|LIGHT_GRAY
decl_stmt|;
specifier|static
specifier|final
name|Color
name|LABEL_COLOR
init|=
name|Color
operator|.
name|BLACK
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|EXCLUDE_TABLE_RATE
init|=
operator|-
literal|10000
decl_stmt|;
specifier|private
name|boolean
name|existFirstLevelIncludeTable
decl_stmt|;
specifier|private
name|boolean
name|existCatalogsOrSchemas
decl_stmt|;
specifier|private
name|boolean
name|hasEntitiesInEmptyContainer
decl_stmt|;
specifier|private
name|DbImportTreeNode
name|dbSchemaNode
decl_stmt|;
specifier|private
name|DbImportTree
name|reverseEngineeringTree
decl_stmt|;
specifier|private
name|boolean
name|namesIsEqual
parameter_list|(
name|DbImportTreeNode
name|reverseEngineeringNode
parameter_list|)
block|{
if|if
condition|(
name|isContainer
argument_list|(
name|reverseEngineeringNode
argument_list|)
condition|)
block|{
return|return
name|dbSchemaNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|reverseEngineeringNode
operator|.
name|getSimpleNodeName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|dbSchemaNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|matches
argument_list|(
name|reverseEngineeringNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
name|boolean
name|isContainer
parameter_list|(
name|DbImportTreeNode
name|node
parameter_list|)
block|{
return|return
operator|(
name|node
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|Schema
operator|.
name|class
operator|)
operator|||
operator|(
name|node
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|Catalog
operator|.
name|class
operator|)
return|;
block|}
specifier|private
name|boolean
name|isEmptyContainer
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|)
block|{
return|return
operator|(
operator|(
name|getChildIncludeTableCount
argument_list|(
name|rootNode
argument_list|)
operator|==
literal|0
operator|)
operator|&&
operator|(
operator|!
name|existFirstLevelIncludeTable
operator|)
operator|)
return|;
block|}
name|boolean
name|isParentIncluded
parameter_list|()
block|{
return|return
operator|(
operator|(
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|isColorized
argument_list|()
operator|)
operator|)
return|;
block|}
comment|// Compare node with current rendered node
specifier|public
name|boolean
name|nodesIsEqual
parameter_list|(
name|DbImportTreeNode
name|reverseEngineeringNode
parameter_list|)
block|{
name|TreePath
index|[]
name|paths
init|=
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
decl_stmt|;
for|for
control|(
name|TreePath
name|path
range|:
name|paths
operator|!=
literal|null
condition|?
name|paths
else|:
operator|new
name|TreePath
index|[
literal|0
index|]
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|(
name|DbImportTreeNode
operator|)
name|path
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|nodesClassesComparation
argument_list|(
name|node
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|dbSchemaNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
operator|)
operator|&&
name|namesIsEqual
argument_list|(
name|node
argument_list|)
operator|&&
operator|(
name|dbSchemaNode
operator|.
name|getLevel
argument_list|()
operator|>=
name|node
operator|.
name|getLevel
argument_list|()
operator|)
operator|&&
operator|(
name|dbSchemaNode
operator|.
name|parentsIsEqual
argument_list|(
name|node
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
operator|(
name|nodesClassesComparation
argument_list|(
name|reverseEngineeringNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|dbSchemaNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
operator|)
operator|&&
name|namesIsEqual
argument_list|(
name|reverseEngineeringNode
argument_list|)
operator|&&
operator|(
name|dbSchemaNode
operator|.
name|getLevel
argument_list|()
operator|>=
name|reverseEngineeringNode
operator|.
name|getLevel
argument_list|()
operator|)
operator|&&
operator|(
name|dbSchemaNode
operator|.
name|parentsIsEqual
argument_list|(
name|reverseEngineeringNode
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|checkTreesLevels
parameter_list|(
name|DbImportTree
name|dbTree
parameter_list|)
block|{
if|if
condition|(
name|dbTree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getChildCount
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbImportTreeNode
name|dbNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|dbTree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getChildAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|childCount
init|=
name|reverseEngineeringTree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getChildCount
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|reverseEngineeringTree
operator|.
name|getRootNode
argument_list|()
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|Catalog
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|dbNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|Catalog
operator|.
name|class
operator|||
name|dbNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|IncludeTable
operator|.
name|class
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// Compare reverseEngineeringNode with node.getParent()
specifier|private
name|boolean
name|compareWithParent
parameter_list|(
name|DbImportTreeNode
name|reverseEngineeringNode
parameter_list|)
block|{
if|if
condition|(
operator|(
name|reverseEngineeringNode
operator|==
literal|null
operator|)
operator|||
operator|(
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|reverseEngineeringNode
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|)
operator|&&
operator|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|reverseEngineeringNode
operator|.
name|getSimpleNodeName
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getLevel
argument_list|()
operator|>=
name|reverseEngineeringNode
operator|.
name|getLevel
argument_list|()
operator|)
operator|&&
operator|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|)
operator|)
operator|.
name|parentsIsEqual
argument_list|(
name|reverseEngineeringNode
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|// Get child IncludeTable's count in node, if exists
specifier|private
name|int
name|getChildIncludeTableCount
parameter_list|(
name|DbImportTreeNode
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|parentNode
operator|.
name|isIncludeTable
argument_list|()
condition|)
block|{
return|return
literal|1
return|;
block|}
name|int
name|childCount
init|=
name|parentNode
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
name|int
name|result
init|=
literal|0
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|tmpNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|parentNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpNode
operator|.
name|isIncludeTable
argument_list|()
condition|)
block|{
name|result
operator|++
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|// Find Exclude-node in configuration
specifier|private
name|boolean
name|foundExclude
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|)
block|{
name|int
name|childCount
init|=
name|rootNode
operator|.
name|getChildCount
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|tmpNode
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
if|if
condition|(
name|tmpNode
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|tmpNode
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|tmpNode
operator|.
name|isExcludeProcedure
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nodesIsEqual
argument_list|(
name|tmpNode
argument_list|)
condition|)
block|{
if|if
condition|(
name|tmpNode
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|tmpNode
operator|.
name|isExcludeProcedure
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/*     *  Recursively traverse DbImportTree,     *  Increment result if rendered node exists in configuration tree,     *  Subtract EXCLUDE_TABLE_RATE from result, if found Exclude-node for rendered node,     *  Return 0, if rendered node not found.     */
name|int
name|traverseTree
parameter_list|(
name|DbImportTreeNode
name|rootNode
parameter_list|)
block|{
name|hasEntitiesInEmptyContainer
operator|=
literal|false
expr_stmt|;
name|int
name|traverseResult
init|=
literal|0
decl_stmt|;
name|int
name|childCount
init|=
name|rootNode
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
name|boolean
name|hasProcedures
init|=
literal|false
decl_stmt|;
comment|// Case for empty reverse engineering, which has a include/exclude tables/procedures
if|if
condition|(
operator|(
name|childCount
operator|==
literal|0
operator|)
operator|&&
operator|(
name|nodesIsEqual
argument_list|(
name|rootNode
argument_list|)
operator|)
condition|)
block|{
name|traverseResult
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|nodesIsEqual
argument_list|(
name|rootNode
argument_list|)
condition|)
block|{
name|traverseResult
operator|++
expr_stmt|;
block|}
name|ReverseEngineering
name|reverseEngineering
init|=
name|reverseEngineeringTree
operator|.
name|getReverseEngineering
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getSchemas
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
operator|!
name|dbSchemaNode
operator|.
name|isIncludeProcedure
argument_list|()
operator|)
condition|)
block|{
name|traverseResult
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|nodesIsEqual
argument_list|(
name|rootNode
argument_list|)
operator|&&
name|isEmptyContainer
argument_list|(
name|rootNode
argument_list|)
condition|)
block|{
name|hasEntitiesInEmptyContainer
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|foundExclude
argument_list|(
name|rootNode
argument_list|)
condition|)
block|{
return|return
name|EXCLUDE_TABLE_RATE
return|;
block|}
return|return
literal|1
return|;
block|}
if|if
condition|(
name|compareWithParent
argument_list|(
name|rootNode
argument_list|)
operator|&&
operator|(
operator|!
name|rootNode
operator|.
name|isReverseEngineering
argument_list|()
operator|)
operator|&&
name|isEmptyContainer
argument_list|(
name|rootNode
argument_list|)
operator|&&
operator|(
name|dbSchemaNode
operator|.
name|isIncludeTable
argument_list|()
operator|)
condition|)
block|{
name|hasEntitiesInEmptyContainer
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|foundExclude
argument_list|(
name|rootNode
argument_list|)
condition|)
block|{
return|return
name|EXCLUDE_TABLE_RATE
return|;
block|}
return|return
literal|1
return|;
block|}
if|if
condition|(
name|hasEntitiesInEmptyContainer
condition|)
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|tmpNode
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
if|if
condition|(
name|dbSchemaNode
operator|.
name|isIncludeProcedure
argument_list|()
operator|&&
operator|(
name|nodesIsEqual
argument_list|(
name|tmpNode
argument_list|)
operator|)
condition|)
block|{
name|int
name|tmpNodeChildCount
init|=
name|tmpNode
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmpNodeChildCount
operator|>
literal|0
condition|)
block|{
name|traverseResult
operator|+=
name|traverseTree
argument_list|(
operator|(
name|DbImportTreeNode
operator|)
name|rootNode
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|traverseResult
operator|++
expr_stmt|;
name|hasProcedures
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
operator|!
name|rootNode
operator|.
name|isExcludeTable
argument_list|()
operator|)
operator|&&
operator|(
operator|!
name|nodesIsEqual
argument_list|(
name|rootNode
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|dbSchemaNode
operator|.
name|isIncludeProcedure
argument_list|()
operator|)
operator|&&
operator|(
operator|!
name|dbSchemaNode
operator|.
name|isIncludeColumn
argument_list|()
operator|)
condition|)
block|{
name|traverseResult
operator|++
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|(
operator|!
name|hasProcedures
operator|)
operator|&&
operator|(
operator|!
name|dbSchemaNode
operator|.
name|isIncludeProcedure
argument_list|()
operator|)
condition|)
block|{
name|traverseResult
operator|+=
name|EXCLUDE_TABLE_RATE
expr_stmt|;
block|}
block|}
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|tmpNode
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
if|if
condition|(
name|tmpNode
operator|.
name|getChildCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|traverseResult
operator|+=
name|traverseTree
argument_list|(
name|tmpNode
argument_list|)
expr_stmt|;
if|if
condition|(
name|tmpNode
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|tmpNode
operator|.
name|isExcludeProcedure
argument_list|()
condition|)
block|{
name|traverseResult
operator|+=
name|EXCLUDE_TABLE_RATE
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|compareWithParent
argument_list|(
name|tmpNode
argument_list|)
operator|&&
operator|!
operator|(
name|existFirstLevelIncludeTable
operator|)
condition|)
block|{
if|if
condition|(
operator|!
name|dbSchemaNode
operator|.
name|isIncludeProcedure
argument_list|()
condition|)
block|{
name|traverseResult
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dbSchemaNode
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nodesIsEqual
argument_list|(
name|tmpNode
argument_list|)
condition|)
block|{
if|if
condition|(
name|tmpNode
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|tmpNode
operator|.
name|isExcludeProcedure
argument_list|()
condition|)
block|{
name|traverseResult
operator|+=
name|EXCLUDE_TABLE_RATE
expr_stmt|;
block|}
name|traverseResult
operator|++
expr_stmt|;
block|}
block|}
block|}
return|return
name|traverseResult
return|;
block|}
name|Color
name|getColorByNodeType
parameter_list|(
name|DbImportTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
operator|(
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
operator|.
name|length
operator|>
literal|1
operator|)
condition|)
block|{
for|for
control|(
name|TreePath
name|path
range|:
name|reverseEngineeringTree
operator|.
name|getSelectionPaths
argument_list|()
control|)
block|{
name|DbImportTreeNode
name|pathNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|path
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathNode
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|dbSchemaNode
operator|.
name|getSimpleNodeName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|pathNode
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|pathNode
operator|.
name|isExcludeProcedure
argument_list|()
operator|||
name|node
operator|.
name|isExcludeColumn
argument_list|()
condition|)
block|{
return|return
name|EXCLUDE_COLOR
return|;
block|}
else|else
block|{
return|return
name|ACCEPT_COLOR
return|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|node
operator|.
name|isExcludeTable
argument_list|()
operator|||
name|node
operator|.
name|isExcludeProcedure
argument_list|()
operator|||
name|node
operator|.
name|isExcludeColumn
argument_list|()
condition|)
block|{
return|return
name|EXCLUDE_COLOR
return|;
block|}
else|else
block|{
return|return
name|ACCEPT_COLOR
return|;
block|}
block|}
name|void
name|findFirstLevelIncludeTable
parameter_list|()
block|{
name|DbImportTreeNode
name|root
init|=
name|reverseEngineeringTree
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|int
name|childCount
init|=
name|root
operator|.
name|getChildCount
argument_list|()
decl_stmt|;
name|existFirstLevelIncludeTable
operator|=
literal|false
expr_stmt|;
name|existCatalogsOrSchemas
operator|=
literal|false
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
name|childCount
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|tmpNode
init|=
operator|(
name|DbImportTreeNode
operator|)
name|root
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpNode
operator|.
name|isIncludeTable
argument_list|()
condition|)
block|{
name|existFirstLevelIncludeTable
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isContainer
argument_list|(
name|tmpNode
argument_list|)
condition|)
block|{
name|existCatalogsOrSchemas
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
comment|// Check, is DatabaseTree started with IncludeTable or IncludeProcedure
name|boolean
name|isFirstNodeIsPrimitive
parameter_list|(
name|DbImportTree
name|tree
parameter_list|)
block|{
specifier|final
name|int
name|firstChildIndex
init|=
literal|0
decl_stmt|;
name|DbImportTreeNode
name|root
init|=
name|tree
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|.
name|getChildCount
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbImportTreeNode
name|firstElement
init|=
operator|(
name|DbImportTreeNode
operator|)
name|root
operator|.
name|getChildAt
argument_list|(
name|firstChildIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstElement
operator|.
name|isIncludeTable
argument_list|()
operator|||
name|firstElement
operator|.
name|isIncludeProcedure
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|nodesClassesComparation
parameter_list|(
name|Class
name|firstClass
parameter_list|,
name|Class
name|secondClass
parameter_list|)
block|{
if|if
condition|(
name|firstClass
operator|.
name|equals
argument_list|(
name|secondClass
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|(
name|firstClass
operator|.
name|equals
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|)
operator|)
operator|&&
operator|(
name|secondClass
operator|.
name|equals
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isExistCatalogsOrSchemas
parameter_list|()
block|{
return|return
name|existCatalogsOrSchemas
return|;
block|}
specifier|public
name|boolean
name|getHasEntitiesInEmptyContainer
parameter_list|()
block|{
return|return
name|hasEntitiesInEmptyContainer
return|;
block|}
specifier|public
name|void
name|setHasEntitiesInEmptyContainer
parameter_list|(
name|boolean
name|newFlag
parameter_list|)
block|{
name|hasEntitiesInEmptyContainer
operator|=
name|newFlag
expr_stmt|;
block|}
specifier|public
name|void
name|setDbSchemaNode
parameter_list|(
name|DbImportTreeNode
name|dbSchemaNode
parameter_list|)
block|{
name|this
operator|.
name|dbSchemaNode
operator|=
name|dbSchemaNode
expr_stmt|;
block|}
specifier|public
name|void
name|setReverseEngineeringTree
parameter_list|(
name|DbImportTree
name|reverseEngineeringTree
parameter_list|)
block|{
name|this
operator|.
name|reverseEngineeringTree
operator|=
name|reverseEngineeringTree
expr_stmt|;
block|}
block|}
end_class

end_unit

