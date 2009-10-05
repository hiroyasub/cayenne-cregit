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
name|undo
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotRedoException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotUndoException
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
name|modeler
operator|.
name|action
operator|.
name|PasteAction
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
name|RemoveAction
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
name|RemoveAttributeAction
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
name|RemoveProcedureParameterAction
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
name|RemoveRelationshipAction
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

begin_class
specifier|public
class|class
name|PasteUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|DataDomain
name|domain
decl_stmt|;
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|private
name|Object
name|where
decl_stmt|;
specifier|private
name|Object
name|content
decl_stmt|;
specifier|public
name|PasteUndoableEdit
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Object
name|where
parameter_list|,
name|Object
name|content
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|where
operator|=
name|where
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
name|String
name|className
init|=
name|this
operator|.
name|content
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|String
name|contentName
init|=
name|className
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
literal|"Paste "
operator|+
name|contentName
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|PasteAction
name|action
init|=
operator|(
name|PasteAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|PasteAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|action
operator|.
name|paste
argument_list|(
name|where
argument_list|,
name|content
argument_list|,
name|domain
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
name|RemoveAttributeAction
name|rAttributeAction
init|=
operator|(
name|RemoveAttributeAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|RemoveAction
name|rAction
init|=
operator|(
name|RemoveAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|RemoveRelationshipAction
name|rRelationShipAction
init|=
operator|(
name|RemoveRelationshipAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveRelationshipAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
name|RemoveProcedureParameterAction
name|rProcedureParamAction
init|=
operator|(
name|RemoveProcedureParameterAction
operator|)
name|actionManager
operator|.
name|getAction
argument_list|(
name|RemoveProcedureParameterAction
operator|.
name|getActionName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|DataMap
condition|)
block|{
if|if
condition|(
name|where
operator|instanceof
name|DataDomain
condition|)
block|{
name|rAction
operator|.
name|removeDataMap
argument_list|(
operator|(
name|DataDomain
operator|)
name|where
argument_list|,
operator|(
name|DataMap
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|where
operator|instanceof
name|DataNode
condition|)
block|{
name|rAction
operator|.
name|removeDataMapFromDataNode
argument_list|(
operator|(
name|DataNode
operator|)
name|where
argument_list|,
operator|(
name|DataMap
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|where
operator|instanceof
name|DataMap
condition|)
block|{
if|if
condition|(
name|content
operator|instanceof
name|DbEntity
condition|)
block|{
name|rAction
operator|.
name|removeDbEntity
argument_list|(
name|map
argument_list|,
operator|(
name|DbEntity
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|content
operator|instanceof
name|ObjEntity
condition|)
block|{
name|rAction
operator|.
name|removeObjEntity
argument_list|(
name|map
argument_list|,
operator|(
name|ObjEntity
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|content
operator|instanceof
name|Query
condition|)
block|{
name|rAction
operator|.
name|removeQuery
argument_list|(
name|map
argument_list|,
operator|(
name|Query
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|content
operator|instanceof
name|Procedure
condition|)
block|{
name|rAction
operator|.
name|removeProcedure
argument_list|(
name|map
argument_list|,
operator|(
name|Procedure
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|where
operator|instanceof
name|DbEntity
condition|)
block|{
if|if
condition|(
name|content
operator|instanceof
name|DbAttribute
condition|)
block|{
name|rAttributeAction
operator|.
name|removeDbAttributes
argument_list|(
name|map
argument_list|,
operator|(
name|DbEntity
operator|)
name|where
argument_list|,
operator|new
name|DbAttribute
index|[]
block|{
operator|(
name|DbAttribute
operator|)
name|content
block|}
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|content
operator|instanceof
name|DbRelationship
condition|)
block|{
name|rRelationShipAction
operator|.
name|removeDbRelationships
argument_list|(
operator|(
name|DbEntity
operator|)
name|where
argument_list|,
operator|new
name|DbRelationship
index|[]
block|{
operator|(
name|DbRelationship
operator|)
name|content
block|}
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|where
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|content
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|rAttributeAction
operator|.
name|removeObjAttributes
argument_list|(
operator|(
name|ObjEntity
operator|)
name|where
argument_list|,
operator|new
name|ObjAttribute
index|[]
block|{
operator|(
name|ObjAttribute
operator|)
name|content
block|}
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|content
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|rRelationShipAction
operator|.
name|removeObjRelationships
argument_list|(
operator|(
name|ObjEntity
operator|)
name|where
argument_list|,
operator|new
name|ObjRelationship
index|[]
block|{
operator|(
name|ObjRelationship
operator|)
name|content
block|}
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|where
operator|instanceof
name|Procedure
condition|)
block|{
specifier|final
name|Procedure
name|procedure
init|=
operator|(
name|Procedure
operator|)
name|where
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|ProcedureParameter
condition|)
block|{
name|rProcedureParamAction
operator|.
name|removeProcedureParameters
argument_list|(
name|procedure
argument_list|,
operator|new
name|ProcedureParameter
index|[]
block|{
operator|(
name|ProcedureParameter
operator|)
name|content
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

