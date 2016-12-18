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
name|graph
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
name|EntityInheritanceTree
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
name|EntityResolver
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
name|event
operator|.
name|AttributeEvent
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
name|event
operator|.
name|EntityEvent
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
name|event
operator|.
name|ObjAttributeListener
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
name|event
operator|.
name|ObjEntityListener
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
name|event
operator|.
name|ObjRelationshipListener
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
name|event
operator|.
name|RelationshipEvent
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
name|jgraph
operator|.
name|graph
operator|.
name|DefaultEdge
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|graph
operator|.
name|DefaultGraphCell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|graph
operator|.
name|GraphConstants
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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

begin_comment
comment|/**  * Builder of ObjEntity information-based graph (relative to UML class diagram)  */
end_comment

begin_class
class|class
name|ObjGraphBuilder
extends|extends
name|BaseGraphBuilder
implements|implements
name|ObjEntityListener
implements|,
name|ObjAttributeListener
implements|,
name|ObjRelationshipListener
block|{
specifier|static
specifier|final
name|Color
name|ENTITY_COLOR
init|=
operator|new
name|Color
argument_list|(
literal|255
argument_list|,
literal|255
argument_list|,
literal|185
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Entity
argument_list|,
name|DefaultEdge
argument_list|>
name|inheritanceEdges
decl_stmt|;
specifier|public
name|ObjGraphBuilder
parameter_list|()
block|{
name|inheritanceEdges
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|?
extends|extends
name|Entity
argument_list|>
name|getEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
return|return
name|map
operator|.
name|getObjEntities
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isIsolated
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|Entity
name|entity
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|isIsolated
argument_list|(
name|domain
argument_list|,
name|entity
argument_list|)
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
name|ObjEntity
operator|)
name|entity
operator|)
operator|.
name|getSuperEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// TODO: andrus 05/30/2010 - reindexing all DataMaps every time may be VERY slow
comment|// on large projects
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|mediator
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
argument_list|)
decl_stmt|;
name|EntityInheritanceTree
name|inheritanceTree
init|=
name|resolver
operator|.
name|lookupInheritanceTree
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|inheritanceTree
operator|==
literal|null
operator|||
name|inheritanceTree
operator|.
name|getChildren
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postProcessEntity
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|DefaultGraphCell
name|cell
parameter_list|)
block|{
name|super
operator|.
name|postProcessEntity
argument_list|(
name|entity
argument_list|,
name|cell
argument_list|)
expr_stmt|;
name|GraphConstants
operator|.
name|setBackground
argument_list|(
name|cell
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|ENTITY_COLOR
argument_list|)
expr_stmt|;
name|GraphConstants
operator|.
name|setOpaque
argument_list|(
name|cell
operator|.
name|getAttributes
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|DefaultEdge
name|edge
init|=
name|createInheritanceEdge
argument_list|(
operator|(
name|ObjEntity
operator|)
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|edge
operator|!=
literal|null
condition|)
block|{
name|createdObjects
operator|.
name|add
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
name|DefaultEdge
name|createInheritanceEdge
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
operator|!
name|inheritanceEdges
operator|.
name|containsKey
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|ObjEntity
name|superEntity
init|=
name|entity
operator|.
name|getSuperEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|superEntity
operator|!=
literal|null
condition|)
block|{
name|DefaultGraphCell
name|sourceCell
init|=
name|entityCells
operator|.
name|get
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultGraphCell
name|targetCell
init|=
name|entityCells
operator|.
name|get
argument_list|(
name|superEntity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultEdge
name|edge
init|=
operator|new
name|DefaultEdge
argument_list|()
decl_stmt|;
name|edge
operator|.
name|setSource
argument_list|(
name|sourceCell
operator|.
name|getChildAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|edge
operator|.
name|setTarget
argument_list|(
name|targetCell
operator|.
name|getChildAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|GraphConstants
operator|.
name|setDashPattern
argument_list|(
name|edge
operator|.
name|getAttributes
argument_list|()
argument_list|,
operator|new
name|float
index|[]
block|{
literal|5
block|,
literal|5
block|}
argument_list|)
expr_stmt|;
name|GraphConstants
operator|.
name|setLineEnd
argument_list|(
name|edge
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|GraphConstants
operator|.
name|ARROW_TECHNICAL
argument_list|)
expr_stmt|;
name|GraphConstants
operator|.
name|setSelectable
argument_list|(
name|edge
operator|.
name|getAttributes
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|inheritanceEdges
operator|.
name|put
argument_list|(
name|entity
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|edge
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|EntityCellMetadata
name|getCellMetadata
parameter_list|(
name|Entity
name|e
parameter_list|)
block|{
return|return
operator|new
name|ObjEntityCellMetadata
argument_list|(
name|this
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setProjectController
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|super
operator|.
name|setProjectController
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjEntityListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjAttributeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|addObjRelationshipListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|mediator
operator|.
name|removeObjEntityListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|removeObjAttributeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|removeObjRelationshipListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|insertEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityChanged
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|remapEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|updateEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// maybe super entity was changed
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|DefaultEdge
name|inheritanceEdge
init|=
name|inheritanceEdges
operator|.
name|get
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|inheritanceEdge
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entity
operator|.
name|getSuperEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|graph
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|remove
argument_list|(
operator|new
name|Object
index|[]
block|{
name|inheritanceEdge
block|}
argument_list|)
expr_stmt|;
name|inheritanceEdges
operator|.
name|remove
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inheritanceEdge
operator|.
name|setTarget
argument_list|(
name|entityCells
operator|.
name|get
argument_list|(
name|entity
operator|.
name|getSuperEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getChildAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Map
name|nested
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|nested
operator|.
name|put
argument_list|(
name|inheritanceEdge
argument_list|,
name|inheritanceEdge
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|graph
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|edit
argument_list|(
name|nested
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|entity
operator|.
name|getSuperEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DefaultEdge
name|edge
init|=
name|createInheritanceEdge
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|graph
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|insert
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|removeEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objAttributeAdded
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|updateEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objAttributeChanged
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|updateEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objAttributeRemoved
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
block|{
name|updateEntityCell
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipAdded
parameter_list|(
name|RelationshipEvent
name|e
parameter_list|)
block|{
comment|// nothing because relationship does not have target yet
block|}
specifier|public
name|void
name|objRelationshipChanged
parameter_list|(
name|RelationshipEvent
name|e
parameter_list|)
block|{
name|remapRelationship
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|updateRelationshipCell
argument_list|(
name|e
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipRemoved
parameter_list|(
name|RelationshipEvent
name|e
parameter_list|)
block|{
name|removeRelationshipCell
argument_list|(
name|e
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|removeEntityCell
parameter_list|(
name|Entity
name|e
parameter_list|)
block|{
name|super
operator|.
name|removeEntityCell
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|inheritanceEdges
operator|.
name|remove
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GraphType
name|getType
parameter_list|()
block|{
return|return
name|GraphType
operator|.
name|CLASS
return|;
block|}
block|}
end_class

end_unit

