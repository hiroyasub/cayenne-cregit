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
name|action
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
name|ProjectTreeView
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
name|FindDialog
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
name|CayenneAction
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
name|QueryDescriptor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
specifier|public
class|class
name|FindAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Result sort priority based on result type      */
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|PRIORITY_BY_TYPE
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|ObjEntity
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|ObjAttribute
operator|.
name|class
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|DbAttribute
operator|.
name|class
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|ObjRelationship
operator|.
name|class
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|QueryDescriptor
operator|.
name|class
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|PRIORITY_BY_TYPE
operator|.
name|put
argument_list|(
name|Embeddable
operator|.
name|class
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Find"
return|;
block|}
specifier|public
name|FindAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * All entities that contain a pattern substring (case-indifferent) in the name are produced.      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|JTextField
name|source
init|=
operator|(
name|JTextField
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|String
name|searchStr
init|=
name|source
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|searchStr
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|searchStr
operator|=
name|searchStr
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|searchStr
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|markEmptySearch
argument_list|(
name|source
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|searchResults
init|=
name|search
argument_list|(
name|searchStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|searchResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|markEmptySearch
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|searchResults
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|jumpToResult
argument_list|(
name|searchResults
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|new
name|FindDialog
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|,
name|searchResults
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|markEmptySearch
parameter_list|(
name|JTextField
name|source
parameter_list|)
block|{
name|source
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|pink
argument_list|)
expr_stmt|;
block|}
comment|/**      * Navigate to search result      * Used also in {@link org.apache.cayenne.modeler.graph.action.EntityDisplayAction}      */
specifier|public
specifier|static
name|void
name|jumpToResult
parameter_list|(
name|FindAction
operator|.
name|SearchResultEntry
name|searchResultEntry
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
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Entity
condition|)
block|{
name|jumpToEntityResult
argument_list|(
operator|(
name|Entity
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|editor
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|QueryDescriptor
condition|)
block|{
name|jumpToQueryResult
argument_list|(
operator|(
name|QueryDescriptor
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|editor
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Embeddable
condition|)
block|{
name|jumpToEmbeddableResult
argument_list|(
operator|(
name|Embeddable
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|editor
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|EmbeddableAttribute
condition|)
block|{
name|jumpToEmbeddableAttributeResult
argument_list|(
operator|(
name|EmbeddableAttribute
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|editor
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Attribute
operator|||
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Relationship
condition|)
block|{
name|jumpToAttributeResult
argument_list|(
name|searchResultEntry
argument_list|,
name|editor
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|search
parameter_list|(
name|String
name|searchStr
parameter_list|)
block|{
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|searchStr
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
operator|)
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|searchInQueryDescriptors
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|searchInEmbeddables
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|searchInDbEntities
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|searchInObjEntities
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|void
name|searchInQueryDescriptors
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|result
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
for|for
control|(
name|QueryDescriptor
name|q
range|:
name|dataMap
operator|.
name|getQueryDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|q
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|q
argument_list|,
name|q
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|searchInEmbeddables
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|result
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
for|for
control|(
name|Embeddable
name|emb
range|:
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|emb
operator|.
name|getClassName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|emb
argument_list|,
name|emb
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|EmbeddableAttribute
name|attr
range|:
name|emb
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|attr
argument_list|,
name|emb
operator|.
name|getClassName
argument_list|()
operator|+
literal|"."
operator|+
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|searchInObjEntities
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|result
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
for|for
control|(
name|ObjEntity
name|ent
range|:
name|dataMap
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|ent
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ObjAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|attr
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|ent
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|rel
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|searchInDbEntities
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|result
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
for|for
control|(
name|DbEntity
name|ent
range|:
name|dataMap
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|ent
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|attr
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|DbRelationship
name|rel
range|:
name|ent
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|SearchResultEntry
argument_list|(
name|rel
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|checkCatalogOrSchema
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|ent
argument_list|,
name|ent
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|checkCatalogOrSchema
argument_list|(
name|pattern
argument_list|,
name|result
argument_list|,
name|ent
argument_list|,
name|ent
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkCatalogOrSchema
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|List
argument_list|<
name|SearchResultEntry
argument_list|>
name|paths
parameter_list|,
name|DbEntity
name|ent
parameter_list|,
name|String
name|catalogOrSchema
parameter_list|)
block|{
if|if
condition|(
name|catalogOrSchema
operator|!=
literal|null
operator|&&
operator|!
name|catalogOrSchema
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|match
argument_list|(
name|catalogOrSchema
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|SearchResultEntry
name|entry
init|=
operator|new
name|SearchResultEntry
argument_list|(
name|ent
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|paths
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
condition|)
block|{
name|paths
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|match
parameter_list|(
name|String
name|entityName
parameter_list|,
name|Pattern
name|pattern
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|entityName
argument_list|)
operator|.
name|find
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|jumpToAttributeResult
parameter_list|(
name|SearchResultEntry
name|searchResultEntry
parameter_list|,
name|EditorView
name|editor
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|DataMap
name|map
decl_stmt|;
name|Entity
name|entity
decl_stmt|;
if|if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Attribute
condition|)
block|{
name|map
operator|=
operator|(
operator|(
name|Attribute
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|entity
operator|=
operator|(
operator|(
name|Attribute
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|map
operator|=
operator|(
operator|(
name|Relationship
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|entity
operator|=
operator|(
operator|(
name|Relationship
operator|)
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getSourceEntity
argument_list|()
expr_stmt|;
block|}
name|buildAndSelectTreePath
argument_list|(
name|map
argument_list|,
name|entity
argument_list|,
name|editor
argument_list|)
expr_stmt|;
if|if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Attribute
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
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|entity
argument_list|,
name|map
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
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DbAttribute
condition|)
block|{
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
else|else
block|{
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
block|}
if|else if
condition|(
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Relationship
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
name|searchResultEntry
operator|.
name|getObject
argument_list|()
argument_list|,
name|entity
argument_list|,
name|map
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
name|searchResultEntry
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DbRelationship
condition|)
block|{
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
else|else
block|{
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
specifier|static
name|void
name|jumpToEmbeddableAttributeResult
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|,
name|EditorView
name|editor
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|Embeddable
name|embeddable
init|=
name|attribute
operator|.
name|getEmbeddable
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|embeddable
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|buildAndSelectTreePath
argument_list|(
name|map
argument_list|,
name|embeddable
argument_list|,
name|editor
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
name|embeddable
argument_list|,
name|attribute
argument_list|,
name|map
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
specifier|private
specifier|static
name|void
name|jumpToEmbeddableResult
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|EditorView
name|editor
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|embeddable
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|buildAndSelectTreePath
argument_list|(
name|map
argument_list|,
name|embeddable
argument_list|,
name|editor
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
name|embeddable
argument_list|,
name|map
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
specifier|private
specifier|static
name|void
name|jumpToQueryResult
parameter_list|(
name|QueryDescriptor
name|queryDescriptor
parameter_list|,
name|EditorView
name|editor
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|queryDescriptor
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|buildAndSelectTreePath
argument_list|(
name|map
argument_list|,
name|queryDescriptor
argument_list|,
name|editor
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
name|queryDescriptor
argument_list|,
name|map
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
specifier|private
specifier|static
name|void
name|jumpToEntityResult
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|EditorView
name|editor
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|buildAndSelectTreePath
argument_list|(
name|map
argument_list|,
name|entity
argument_list|,
name|editor
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
name|entity
argument_list|,
name|map
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
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
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
block|}
if|else if
condition|(
name|entity
operator|instanceof
name|DbEntity
condition|)
block|{
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
block|}
comment|/**      * Builds a tree path for a given path and make selection in it      */
specifier|private
specifier|static
name|TreePath
name|buildAndSelectTreePath
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|Object
name|object
parameter_list|,
name|EditorView
name|editor
parameter_list|)
block|{
name|ProjectTreeView
name|projectTreeView
init|=
name|editor
operator|.
name|getProjectTreeView
argument_list|()
decl_stmt|;
name|ProjectTreeModel
name|treeModel
init|=
operator|(
name|ProjectTreeModel
operator|)
name|projectTreeView
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|DefaultMutableTreeNode
index|[]
name|mutableTreeNodes
init|=
operator|new
name|DefaultMutableTreeNode
index|[]
block|{
name|treeModel
operator|.
name|getRootNode
argument_list|()
block|,
name|treeModel
operator|.
name|getNodeForObjectPath
argument_list|(
operator|new
name|Object
index|[]
block|{
name|map
block|}
argument_list|)
block|,
name|treeModel
operator|.
name|getNodeForObjectPath
argument_list|(
operator|new
name|Object
index|[]
block|{
name|map
block|,
name|object
block|}
argument_list|)
block|}
decl_stmt|;
name|TreePath
name|treePath
init|=
operator|new
name|TreePath
argument_list|(
name|mutableTreeNodes
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projectTreeView
operator|.
name|isExpanded
argument_list|(
name|treePath
operator|.
name|getParentPath
argument_list|()
argument_list|)
condition|)
block|{
name|projectTreeView
operator|.
name|expandPath
argument_list|(
name|treePath
operator|.
name|getParentPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|projectTreeView
operator|.
name|getSelectionModel
argument_list|()
operator|.
name|setSelectionPath
argument_list|(
name|treePath
argument_list|)
expr_stmt|;
return|return
name|treePath
return|;
block|}
comment|/**      * Search result holder      */
specifier|public
specifier|static
class|class
name|SearchResultEntry
implements|implements
name|Comparable
argument_list|<
name|SearchResultEntry
argument_list|>
block|{
specifier|private
specifier|final
name|Object
name|object
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
name|SearchResultEntry
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
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
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|SearchResultEntry
name|entry
init|=
operator|(
name|SearchResultEntry
operator|)
name|o
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|name
argument_list|)
operator|&&
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|name
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|SearchResultEntry
name|o
parameter_list|)
block|{
name|int
name|res
init|=
name|PRIORITY_BY_TYPE
operator|.
name|get
argument_list|(
name|getObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
operator|-
name|PRIORITY_BY_TYPE
operator|.
name|get
argument_list|(
name|o
operator|.
name|getObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|!=
literal|0
condition|)
block|{
return|return
name|res
return|;
block|}
return|return
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

