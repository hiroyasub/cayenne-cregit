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
operator|.
name|dialog
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseListener
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
name|javax
operator|.
name|swing
operator|.
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|Attribute
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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|Embeddable
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
name|EmbeddableAttribute
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
name|map
operator|.
name|ObjAttribute
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
name|ObjEntity
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
name|map
operator|.
name|Relationship
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
name|CayenneModelerFrame
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
name|editor
operator|.
name|EditorView
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
name|event
operator|.
name|AttributeDisplayEvent
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
name|event
operator|.
name|EmbeddableAttributeDisplayEvent
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
name|event
operator|.
name|EmbeddableDisplayEvent
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
name|event
operator|.
name|EntityDisplayEvent
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
name|event
operator|.
name|QueryDisplayEvent
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
name|event
operator|.
name|RelationshipDisplayEvent
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
name|CayenneController
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
name|AbstractQuery
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
name|EJBQLQuery
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
name|Query
import|;
end_import

begin_comment
comment|/**  * An instance of this class is responsible for displaying search results and navigating  * to the selected entity's representation.  */
end_comment

begin_class
specifier|public
class|class
name|FindDialog
extends|extends
name|CayenneController
block|{
specifier|private
name|FindDialogView
name|view
decl_stmt|;
specifier|private
name|List
name|paths
decl_stmt|;
specifier|private
specifier|static
name|Font
name|font
decl_stmt|;
specifier|private
specifier|static
name|Font
name|fontSelected
decl_stmt|;
specifier|public
specifier|static
name|Font
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
specifier|public
specifier|static
name|Font
name|getFontSelected
parameter_list|()
block|{
return|return
name|fontSelected
return|;
block|}
specifier|public
name|FindDialog
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|List
name|paths
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|paths
operator|=
name|paths
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|objEntityNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|dbEntityNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|attrNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|relatNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|queryNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|embeddableNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|,
name|embeddableAttributeNames
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|paths
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
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
name|path
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|ObjEntity
condition|)
block|{
name|objEntityNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
operator|(
operator|(
name|ObjEntity
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|DbEntity
condition|)
block|{
name|dbEntityNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
operator|(
operator|(
name|DbEntity
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Query
condition|)
block|{
name|queryNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
operator|(
operator|(
name|Query
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Embeddable
condition|)
block|{
name|String
name|name
init|=
operator|(
operator|(
name|Embeddable
operator|)
name|path
operator|)
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|embeddableNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|EmbeddableAttribute
condition|)
block|{
name|Embeddable
name|parentObject
init|=
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getEmbeddable
argument_list|()
decl_stmt|;
name|embeddableAttributeNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
name|parentObject
operator|.
name|getClassName
argument_list|()
operator|+
literal|"."
operator|+
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Attribute
condition|)
block|{
name|Object
name|parentObject
init|=
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|attrNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
name|getParentName
argument_list|(
name|parentObject
argument_list|)
operator|+
literal|"."
operator|+
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Relationship
condition|)
block|{
name|Object
name|parentObject
init|=
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getParent
argument_list|()
decl_stmt|;
comment|/*                  * relationships are different from attributes in that they do not                  * correctly return the owning entity when inheritance is involved.                  * Hopefully this will be reconciled in the future relases                  */
name|String
name|parentName
init|=
name|getParentName
argument_list|(
name|parentObject
argument_list|)
decl_stmt|;
comment|// if (!parentObject.equals(path)) {
comment|// parentName = ((ObjEntity) path[path.length - 2]).getName();
comment|// }
name|relatNames
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
name|parentName
operator|+
literal|"."
operator|+
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|view
operator|=
operator|new
name|FindDialogView
argument_list|(
name|objEntityNames
argument_list|,
name|dbEntityNames
argument_list|,
name|attrNames
argument_list|,
name|relatNames
argument_list|,
name|queryNames
argument_list|,
name|embeddableNames
argument_list|,
name|embeddableAttributeNames
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|view
operator|.
name|setDefaultCloseOperation
argument_list|(
name|JDialog
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|view
operator|.
name|getOkButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|font
operator|=
name|view
operator|.
name|getOkButton
argument_list|()
operator|.
name|getFont
argument_list|()
expr_stmt|;
name|fontSelected
operator|=
operator|new
name|Font
argument_list|(
name|font
operator|.
name|getFamily
argument_list|()
argument_list|,
name|font
operator|.
name|BOLD
argument_list|,
name|font
operator|.
name|getSize
argument_list|()
operator|+
literal|2
argument_list|)
expr_stmt|;
name|JTable
name|table
init|=
name|view
operator|.
name|getTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|setRowHeight
argument_list|(
name|fontSelected
operator|.
name|getSize
argument_list|()
operator|+
literal|6
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowMargin
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|table
operator|.
name|addKeyListener
argument_list|(
operator|new
name|JumpToResultsKeyListener
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|addMouseListener
argument_list|(
operator|new
name|JumpToResultActionListener
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionInterval
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|jumpToResult
parameter_list|(
name|Object
name|path
parameter_list|)
block|{
name|EditorView
name|editor
init|=
operator|(
operator|(
name|CayenneModelerFrame
operator|)
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getView
argument_list|()
operator|)
operator|.
name|getView
argument_list|()
decl_stmt|;
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|Entity
condition|)
block|{
name|Object
index|[]
name|o
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|o
index|[
literal|0
index|]
operator|=
name|domain
expr_stmt|;
name|o
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|Entity
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
name|Entity
operator|)
name|path
expr_stmt|;
comment|/** Make selection in a project tree, open correspondent entity tab */
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|buildTreePath
argument_list|(
name|o
argument_list|,
name|editor
argument_list|)
argument_list|)
expr_stmt|;
name|EntityDisplayEvent
name|event
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Entity
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Entity
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|ObjEntity
condition|)
name|editor
operator|.
name|getObjDetailView
argument_list|()
operator|.
name|currentObjEntityChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|DbEntity
condition|)
name|editor
operator|.
name|getDbDetailView
argument_list|()
operator|.
name|currentDbEntityChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Query
condition|)
block|{
name|DataMap
name|dmForQuery
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|EJBQLQuery
condition|)
block|{
name|dmForQuery
operator|=
operator|(
operator|(
name|EJBQLQuery
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|instanceof
name|AbstractQuery
condition|)
block|{
name|dmForQuery
operator|=
operator|(
operator|(
name|AbstractQuery
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
name|Object
index|[]
name|o
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|o
index|[
literal|0
index|]
operator|=
name|domain
expr_stmt|;
name|o
index|[
literal|1
index|]
operator|=
name|dmForQuery
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
name|Query
operator|)
name|path
expr_stmt|;
comment|/** Make selection in a project tree, open correspondent entity tab */
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|buildTreePath
argument_list|(
name|o
argument_list|,
name|editor
argument_list|)
argument_list|)
expr_stmt|;
name|QueryDisplayEvent
name|event
init|=
operator|new
name|QueryDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Query
operator|)
name|path
argument_list|,
operator|(
name|DataMap
operator|)
name|dmForQuery
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|editor
operator|.
name|currentQueryChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Embeddable
condition|)
block|{
name|Object
index|[]
name|o
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|o
index|[
literal|0
index|]
operator|=
name|domain
expr_stmt|;
name|o
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|Embeddable
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
name|Embeddable
operator|)
name|path
expr_stmt|;
comment|/** Make selection in a project tree, open correspondent entity tab */
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|buildTreePath
argument_list|(
name|o
argument_list|,
name|editor
argument_list|)
argument_list|)
expr_stmt|;
name|EmbeddableDisplayEvent
name|event
init|=
operator|new
name|EmbeddableDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Embeddable
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Embeddable
operator|)
name|path
operator|)
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|currentEmbeddableChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|EmbeddableAttribute
condition|)
block|{
comment|/** Make selection in a project tree, open correspondent embeddable tab */
name|Object
index|[]
name|o
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|o
index|[
literal|0
index|]
operator|=
name|domain
expr_stmt|;
name|o
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getEmbeddable
argument_list|()
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getEmbeddable
argument_list|()
expr_stmt|;
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|buildTreePath
argument_list|(
name|o
argument_list|,
name|editor
argument_list|)
argument_list|)
expr_stmt|;
name|EmbeddableAttributeDisplayEvent
name|event
init|=
operator|new
name|EmbeddableAttributeDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getEmbeddable
argument_list|()
argument_list|,
operator|(
name|EmbeddableAttribute
operator|)
name|path
argument_list|,
operator|(
operator|(
name|EmbeddableAttribute
operator|)
name|path
operator|)
operator|.
name|getEmbeddable
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getEmbeddableView
argument_list|()
operator|.
name|currentEmbeddableAttributeChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|instanceof
name|Attribute
operator|||
name|path
operator|instanceof
name|Relationship
condition|)
block|{
comment|/** Make selection in a project tree, open correspondent attributes tab */
name|Object
index|[]
name|o
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|o
index|[
literal|0
index|]
operator|=
name|domain
expr_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|Attribute
condition|)
block|{
name|o
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|o
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|o
index|[
literal|2
index|]
operator|=
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
expr_stmt|;
block|}
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|buildTreePath
argument_list|(
name|o
argument_list|,
name|editor
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|instanceof
name|DbAttribute
condition|)
block|{
name|AttributeDisplayEvent
name|event
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Attribute
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getDbDetailView
argument_list|()
operator|.
name|currentDbAttributeChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|AttributeDisplayEvent
name|event
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Attribute
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|Attribute
operator|)
name|path
operator|)
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getObjDetailView
argument_list|()
operator|.
name|currentObjAttributeChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|instanceof
name|DbRelationship
condition|)
block|{
name|RelationshipDisplayEvent
name|event
init|=
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Relationship
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getDbDetailView
argument_list|()
operator|.
name|currentDbRelationshipChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|RelationshipDisplayEvent
name|event
init|=
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|editor
operator|.
name|getProjectTreeView
argument_list|()
argument_list|,
operator|(
name|Relationship
operator|)
name|path
argument_list|,
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|Relationship
operator|)
name|path
operator|)
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|event
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getObjDetailView
argument_list|()
operator|.
name|currentObjRelationshipChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|JumpToResultActionListener
implements|implements
name|MouseListener
block|{
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|JTable
name|table
init|=
operator|(
name|JTable
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Integer
name|selectedLine
init|=
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|getLeadSelectionIndex
argument_list|()
decl_stmt|;
name|JLabel
name|label
init|=
operator|(
name|JLabel
operator|)
name|table
operator|.
name|getModel
argument_list|()
operator|.
name|getValueAt
argument_list|(
name|selectedLine
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Integer
name|index
init|=
operator|(
name|Integer
operator|)
name|FindDialogView
operator|.
name|getLabelAndObjectIndex
argument_list|()
operator|.
name|get
argument_list|(
name|label
argument_list|)
decl_stmt|;
name|Object
name|path
init|=
name|paths
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|jumpToResult
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
block|}
specifier|private
class|class
name|JumpToResultsKeyListener
implements|implements
name|KeyListener
block|{
specifier|public
name|void
name|keyPressed
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyEvent
operator|.
name|VK_ENTER
condition|)
block|{
name|JTable
name|table
init|=
operator|(
name|JTable
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Integer
name|selectedLine
init|=
name|table
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|getLeadSelectionIndex
argument_list|()
decl_stmt|;
name|JLabel
name|label
init|=
operator|(
name|JLabel
operator|)
name|table
operator|.
name|getModel
argument_list|()
operator|.
name|getValueAt
argument_list|(
name|selectedLine
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Integer
name|index
init|=
operator|(
name|Integer
operator|)
name|FindDialogView
operator|.
name|getLabelAndObjectIndex
argument_list|()
operator|.
name|get
argument_list|(
name|label
argument_list|)
decl_stmt|;
name|Object
index|[]
name|path
init|=
operator|(
name|Object
index|[]
operator|)
name|paths
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|jumpToResult
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|keyReleased
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|keyTyped
parameter_list|(
name|KeyEvent
name|e
parameter_list|)
block|{
block|}
block|}
comment|/**      * Builds a tree path for a given path. Urgent for later selection.      *       * @param path      * @return tree path      */
specifier|private
specifier|static
name|TreePath
name|buildTreePath
parameter_list|(
name|Object
index|[]
name|path
parameter_list|,
name|EditorView
name|editor
parameter_list|)
block|{
name|Object
index|[]
name|mutableTreeNodes
init|=
operator|new
name|Object
index|[
name|path
operator|.
name|length
index|]
decl_stmt|;
name|mutableTreeNodes
index|[
literal|0
index|]
operator|=
operator|(
operator|(
name|ProjectTreeModel
operator|)
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
name|Object
index|[]
name|helper
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
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
name|helper
operator|=
operator|new
name|Object
index|[
name|i
index|]
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
control|)
block|{
name|helper
index|[
name|j
index|]
operator|=
name|path
index|[
operator|++
name|j
index|]
expr_stmt|;
block|}
name|mutableTreeNodes
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|ProjectTreeModel
operator|)
name|editor
operator|.
name|getProjectTreeView
argument_list|()
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|getNodeForObjectPath
argument_list|(
name|helper
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|TreePath
argument_list|(
name|mutableTreeNodes
argument_list|)
return|;
block|}
specifier|private
name|String
name|getParentName
parameter_list|(
name|Object
name|parentObject
parameter_list|)
block|{
name|String
name|nameParent
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parentObject
operator|instanceof
name|ObjEntity
condition|)
block|{
name|ObjEntity
name|objEntity
init|=
operator|(
name|ObjEntity
operator|)
name|parentObject
decl_stmt|;
name|nameParent
operator|=
name|objEntity
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parentObject
operator|instanceof
name|DbEntity
condition|)
block|{
name|DbEntity
name|dbEntity
init|=
operator|(
name|DbEntity
operator|)
name|parentObject
decl_stmt|;
name|nameParent
operator|=
name|dbEntity
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parentObject
operator|instanceof
name|Embeddable
condition|)
block|{
name|Embeddable
name|embeddable
init|=
operator|(
name|Embeddable
operator|)
name|parentObject
decl_stmt|;
name|nameParent
operator|=
name|embeddable
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
return|return
name|nameParent
return|;
block|}
block|}
end_class

end_unit

