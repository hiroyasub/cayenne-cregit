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
name|event
operator|.
name|ActionEvent
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
name|ConfirmDeleteDialog
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
name|ProjectUtil
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

begin_comment
comment|/**  * Removes currently selected attribute from either the DbEntity or ObjEntity.  *   * @author Garry Watkins  */
end_comment

begin_class
specifier|public
class|class
name|RemoveAttributeAction
extends|extends
name|RemoveAction
block|{
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Remove Attribute"
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
specifier|public
name|RemoveAttributeAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable      * attribute.      */
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
return|return
name|path
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Attribute
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
name|ConfirmDeleteDialog
name|dialog
init|=
name|getConfirmDeleteDialog
argument_list|()
decl_stmt|;
if|if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjAttribute
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
literal|"ObjAttribute"
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjAttribute
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeObjAttribute
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbAttribute
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
literal|"DbAttribute"
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbAttribute
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|removeDbAttribute
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|removeDbAttribute
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
name|DbAttribute
name|attrib
init|=
name|mediator
operator|.
name|getCurrentDbAttribute
argument_list|()
decl_stmt|;
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrib
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectUtil
operator|.
name|cleanObjMappings
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeEvent
name|e
init|=
operator|new
name|AttributeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|attrib
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbAttributeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeObjAttribute
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
name|ObjAttribute
name|attrib
init|=
name|mediator
operator|.
name|getCurrentObjAttribute
argument_list|()
decl_stmt|;
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrib
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeEvent
name|e
init|=
operator|new
name|AttributeEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|attrib
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

