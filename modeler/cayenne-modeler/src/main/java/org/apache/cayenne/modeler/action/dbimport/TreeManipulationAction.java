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
name|action
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
name|editor
operator|.
name|dbimport
operator|.
name|DbImportModel
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
name|editor
operator|.
name|dbimport
operator|.
name|DbImportTree
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
name|undo
operator|.
name|DbImportTreeUndoableEdit
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
name|util
operator|.
name|CayenneAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Arrays
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
name|HashMap
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TreeManipulationAction
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TreeManipulationAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|EMPTY_NAME
init|=
literal|""
decl_stmt|;
specifier|protected
name|DbImportTree
name|tree
decl_stmt|;
specifier|protected
name|DbImportTreeNode
name|selectedElement
decl_stmt|;
name|DbImportTreeNode
name|parentElement
decl_stmt|;
name|DbImportTreeNode
name|foundNode
decl_stmt|;
name|String
name|insertableNodeName
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|insertableNodeClass
decl_stmt|;
name|boolean
name|isMultipleAction
decl_stmt|;
specifier|private
name|boolean
name|movedFromDbSchema
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|levels
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|boolean
name|updateSelected
decl_stmt|;
specifier|public
name|TreeManipulationAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|application
argument_list|)
expr_stmt|;
name|initLevels
argument_list|()
expr_stmt|;
block|}
name|void
name|completeInserting
parameter_list|(
name|ReverseEngineering
name|reverseEngineeringOldCopy
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isMultipleAction
condition|)
block|{
name|updateAfterInsert
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
name|isMultipleAction
operator|)
operator|&&
operator|(
operator|!
name|insertableNodeName
operator|.
name|equals
argument_list|(
name|EMPTY_NAME
argument_list|)
operator|)
condition|)
block|{
name|putReverseEngineeringToUndoManager
argument_list|(
name|reverseEngineeringOldCopy
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getNodeName
parameter_list|()
block|{
return|return
name|insertableNodeName
operator|!=
literal|null
condition|?
name|insertableNodeName
else|:
name|EMPTY_NAME
return|;
block|}
specifier|protected
name|ReverseEngineering
name|prepareElements
parameter_list|()
block|{
name|name
operator|=
name|getNodeName
argument_list|()
expr_stmt|;
name|tree
operator|.
name|stopEditing
argument_list|()
expr_stmt|;
if|if
condition|(
name|tree
operator|.
name|getSelectionPath
argument_list|()
operator|==
literal|null
condition|)
block|{
name|TreePath
name|root
init|=
operator|new
name|TreePath
argument_list|(
name|tree
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
name|tree
operator|.
name|setSelectionPath
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|foundNode
operator|==
literal|null
condition|)
block|{
name|selectedElement
operator|=
name|tree
operator|.
name|getSelectedNode
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|selectedElement
operator|=
name|foundNode
expr_stmt|;
block|}
name|parentElement
operator|=
name|selectedElement
operator|.
name|getParent
argument_list|()
expr_stmt|;
if|if
condition|(
name|parentElement
operator|==
literal|null
condition|)
block|{
name|parentElement
operator|=
name|selectedElement
expr_stmt|;
block|}
if|if
condition|(
name|reverseEngineeringIsEmpty
argument_list|()
condition|)
block|{
name|tree
operator|.
name|getRootNode
argument_list|()
operator|.
name|removeAllChildren
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|ReverseEngineering
argument_list|(
name|tree
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|putReverseEngineeringToUndoManager
parameter_list|(
name|ReverseEngineering
name|reverseEngineeringOldCopy
parameter_list|)
block|{
name|ReverseEngineering
name|reverseEngineeringNewCopy
init|=
operator|new
name|ReverseEngineering
argument_list|(
name|tree
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
decl_stmt|;
name|DbImportTreeUndoableEdit
name|undoableEdit
init|=
operator|new
name|DbImportTreeUndoableEdit
argument_list|(
name|reverseEngineeringOldCopy
argument_list|,
name|reverseEngineeringNewCopy
argument_list|,
name|tree
argument_list|,
name|getProjectController
argument_list|()
argument_list|)
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|getApplication
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
name|undoableEdit
argument_list|)
expr_stmt|;
block|}
name|boolean
name|reverseEngineeringIsEmpty
parameter_list|()
block|{
name|ReverseEngineering
name|reverseEngineering
init|=
name|tree
operator|.
name|getReverseEngineering
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getSchemas
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|reverseEngineering
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
operator|)
return|;
block|}
specifier|private
name|void
name|initLevels
parameter_list|()
block|{
name|levels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|schemaChildren
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
name|ExcludeTable
operator|.
name|class
argument_list|,
name|IncludeColumn
operator|.
name|class
argument_list|,
name|ExcludeColumn
operator|.
name|class
argument_list|,
name|IncludeProcedure
operator|.
name|class
argument_list|,
name|ExcludeProcedure
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|rootChildren
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|schemaChildren
argument_list|)
decl_stmt|;
name|rootChildren
operator|.
name|add
argument_list|(
name|Schema
operator|.
name|class
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ReverseEngineering
operator|.
name|class
argument_list|,
name|rootChildren
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
name|rootChildren
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
name|schemaChildren
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|,
name|ExcludeColumn
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|levels
operator|.
name|put
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTree
parameter_list|(
name|DbImportTree
name|tree
parameter_list|)
block|{
name|this
operator|.
name|tree
operator|=
name|tree
expr_stmt|;
block|}
specifier|public
name|JTree
name|getTree
parameter_list|()
block|{
return|return
name|tree
return|;
block|}
name|boolean
name|canBeInserted
parameter_list|(
name|DbImportTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|selectedObjectClass
init|=
name|node
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|levels
operator|.
name|get
argument_list|(
name|selectedObjectClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|classes
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Trying to insert node of the unknown class '"
operator|+
name|selectedObjectClass
operator|.
name|getName
argument_list|()
operator|+
literal|"' to the dbimport tree."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
name|classes
operator|.
name|contains
argument_list|(
name|insertableNodeClass
argument_list|)
return|;
block|}
name|boolean
name|canInsert
parameter_list|()
block|{
if|if
condition|(
name|selectedElement
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|parentElement
operator|!=
literal|null
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
name|parentElement
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbImportTreeNode
name|child
init|=
operator|(
name|DbImportTreeNode
operator|)
name|parentElement
operator|.
name|getChildAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getSimpleNodeName
argument_list|()
operator|.
name|equals
argument_list|(
name|insertableNodeName
argument_list|)
operator|&&
operator|(
name|child
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|insertableNodeClass
operator|)
condition|)
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
specifier|protected
name|void
name|updateModel
parameter_list|(
name|boolean
name|updateSelected
parameter_list|)
block|{
name|insertableNodeName
operator|=
literal|null
expr_stmt|;
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|tree
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|TreePath
name|savedPath
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|updateSelected
condition|)
block|{
name|savedPath
operator|=
operator|new
name|TreePath
argument_list|(
name|parentElement
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|model
operator|.
name|reload
argument_list|(
name|updateSelected
condition|?
name|selectedElement
else|:
name|parentElement
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|savedPath
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|parentElement
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
operator|!=
name|ReverseEngineering
operator|.
name|class
operator|)
condition|)
block|{
name|tree
operator|.
name|setSelectionPath
argument_list|(
name|savedPath
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|updateAfterInsert
parameter_list|()
block|{
name|updateModel
argument_list|(
name|updateSelected
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|movedFromDbSchema
condition|)
block|{
if|if
condition|(
name|updateSelected
condition|)
block|{
name|tree
operator|.
name|startEditingAtPath
argument_list|(
operator|new
name|TreePath
argument_list|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|selectedElement
operator|.
name|getLastChild
argument_list|()
operator|)
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tree
operator|.
name|startEditingAtPath
argument_list|(
operator|new
name|TreePath
argument_list|(
operator|(
operator|(
name|DbImportTreeNode
operator|)
name|parentElement
operator|.
name|getLastChild
argument_list|()
operator|)
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|resetActionFlags
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|resetActionFlags
parameter_list|()
block|{
name|movedFromDbSchema
operator|=
literal|false
expr_stmt|;
name|isMultipleAction
operator|=
literal|false
expr_stmt|;
name|insertableNodeName
operator|=
literal|""
expr_stmt|;
block|}
name|void
name|setInsertableNodeName
parameter_list|(
name|String
name|nodeName
parameter_list|)
block|{
name|this
operator|.
name|insertableNodeName
operator|=
name|nodeName
expr_stmt|;
block|}
name|void
name|setMultipleAction
parameter_list|(
name|boolean
name|multipleAction
parameter_list|)
block|{
name|isMultipleAction
operator|=
name|multipleAction
expr_stmt|;
block|}
name|void
name|setMovedFromDbSchema
parameter_list|(
name|boolean
name|movedFromDbSchema
parameter_list|)
block|{
name|this
operator|.
name|movedFromDbSchema
operator|=
name|movedFromDbSchema
expr_stmt|;
block|}
name|void
name|setFoundNode
parameter_list|(
name|DbImportTreeNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|foundNode
operator|=
name|node
expr_stmt|;
block|}
block|}
end_class

end_unit

