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
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFrame
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
name|project2
operator|.
name|validation
operator|.
name|ValidationInfo
import|;
end_import

begin_comment
comment|/**  * Attribute validation message.  *   */
end_comment

begin_class
specifier|public
class|class
name|AttributeErrorMsg
extends|extends
name|ValidationDisplayHandler
block|{
specifier|protected
name|DataMap
name|map
decl_stmt|;
specifier|protected
name|Entity
name|entity
decl_stmt|;
specifier|protected
name|Attribute
name|attribute
decl_stmt|;
comment|/**      * Constructor for AttributeErrorMsg.      *       * @param result      */
specifier|public
name|AttributeErrorMsg
parameter_list|(
name|ValidationInfo
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|result
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|attribute
operator|=
operator|(
name|Attribute
operator|)
name|object
expr_stmt|;
name|entity
operator|=
name|attribute
operator|.
name|getEntity
argument_list|()
expr_stmt|;
name|map
operator|=
name|entity
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
name|domain
operator|=
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
expr_stmt|;
block|}
specifier|public
name|void
name|displayField
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|JFrame
name|frame
parameter_list|)
block|{
name|AttributeDisplayEvent
name|event
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|frame
argument_list|,
name|attribute
argument_list|,
name|entity
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
decl_stmt|;
comment|// must first display entity, and then switch to relationship display .. so fire
comment|// twice
if|if
condition|(
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
name|mediator
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjAttributeDisplayEvent
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
name|mediator
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

