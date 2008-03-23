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
name|java
operator|.
name|awt
operator|.
name|Toolkit
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
name|KeyEvent
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
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|KeyStroke
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
name|Procedure
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
name|ProcedureParameter
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
name|map
operator|.
name|event
operator|.
name|DataMapEvent
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
name|DataNodeEvent
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
name|DomainEvent
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
name|MapEvent
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
name|ProcedureEvent
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
name|QueryEvent
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
name|dialog
operator|.
name|ConfirmRemoveDialog
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
name|project
operator|.
name|ApplicationProject
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
name|Query
import|;
end_import

begin_comment
comment|/**  * Removes currently selected object from the project. This can be Domain, DataNode,  * Entity, Attribute or Relationship.  */
end_comment

begin_class
specifier|public
class|class
name|RemoveAction
extends|extends
name|CayenneAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Remove"
return|;
block|}
specifier|public
name|RemoveAction
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
specifier|protected
name|RemoveAction
parameter_list|(
name|String
name|actionName
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|actionName
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-trash.gif"
return|;
block|}
specifier|public
name|KeyStroke
name|getAcceleratorKey
parameter_list|()
block|{
return|return
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_D
argument_list|,
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getMenuShortcutKeyMask
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ConfirmRemoveDialog
name|getConfirmDeleteDialog
parameter_list|()
block|{
return|return
operator|new
name|ConfirmRemoveDialog
argument_list|()
return|;
block|}
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|ConfirmRemoveDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|()
decl_stmt|;
if|if
condition|(
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"ObjEntity"
argument_list|,
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeObjEntity
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"DbEntity"
argument_list|,
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeDbEntity
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"query"
argument_list|,
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeQuery
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentProcedure
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"procedure"
argument_list|,
name|mediator
operator|.
name|getCurrentProcedure
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeProcedure
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"data map"
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// In context of Data node just remove from Data Node
if|if
condition|(
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|removeDataMapFromDataNode
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Not under Data Node, remove completely
name|removeDataMap
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"data node"
argument_list|,
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeDataNode
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dialog
operator|.
name|shouldDelete
argument_list|(
literal|"data domain"
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeDomain
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|removeDomain
parameter_list|()
block|{
name|ApplicationProject
name|project
init|=
operator|(
name|ApplicationProject
operator|)
name|getCurrentProject
argument_list|()
decl_stmt|;
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
name|project
operator|.
name|getConfiguration
argument_list|()
operator|.
name|removeDomain
argument_list|(
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDomainEvent
argument_list|(
operator|new
name|DomainEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|domain
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeDataMap
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
name|domain
operator|.
name|removeMap
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataMapEvent
argument_list|(
operator|new
name|DataMapEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|map
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeDataNode
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataNode
name|node
init|=
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
name|domain
operator|.
name|removeDataNode
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|node
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes current DbEntity from its DataMap and fires "remove" EntityEvent.      */
specifier|protected
name|void
name|removeDbEntity
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DbEntity
name|ent
init|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes current Query from its DataMap and fires "remove" QueryEvent.      */
specifier|protected
name|void
name|removeQuery
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|removeQuery
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|query
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes current Procedure from its DataMap and fires "remove" ProcedureEvent.      */
specifier|protected
name|void
name|removeProcedure
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|Procedure
name|procedure
init|=
name|mediator
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|removeProcedure
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|procedure
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes current object entity from its DataMap.      */
specifier|protected
name|void
name|removeObjEntity
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove queries that depend on entity
comment|// TODO: (Andrus, 09/09/2005) show warning dialog?
comment|// clone to be able to remove within iterator...
name|Iterator
name|it
init|=
operator|new
name|ArrayList
argument_list|(
name|map
operator|.
name|getQueries
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AbstractQuery
name|next
init|=
operator|(
name|AbstractQuery
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|root
init|=
name|next
operator|.
name|getRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|==
name|entity
operator|||
operator|(
name|root
operator|instanceof
name|String
operator|&&
name|root
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|map
operator|.
name|removeQuery
argument_list|(
name|next
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|next
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|removeDataMapFromDataNode
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataNode
name|node
init|=
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|node
operator|.
name|removeDataMap
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Force reloading of the data node in the browse view
name|mediator
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable object.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|lastObject
init|=
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastObject
operator|instanceof
name|DataDomain
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|DataMap
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|DataNode
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|Entity
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|Attribute
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|Relationship
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|Procedure
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|ProcedureParameter
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
end_class

end_unit

