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
name|ConfigurationNode
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
name|configuration
operator|.
name|event
operator|.
name|ProcedureParameterEvent
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
name|naming
operator|.
name|NameBuilder
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
name|event
operator|.
name|ProcedureParameterDisplayEvent
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
name|modeler
operator|.
name|undo
operator|.
name|CreateProcedureParameterUndoableEdit
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

begin_class
specifier|public
class|class
name|CreateProcedureParameterAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Constructor for CreateProcedureParameterAction.      */
specifier|public
name|CreateProcedureParameterAction
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
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Create Parameter"
return|;
block|}
comment|/**      * Fires events when an proc parameter was added      */
specifier|static
name|void
name|fireProcedureParameterEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|Procedure
name|procedure
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|mediator
operator|.
name|fireProcedureParameterEvent
argument_list|(
operator|new
name|ProcedureParameterEvent
argument_list|(
name|src
argument_list|,
name|parameter
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireProcedureParameterDisplayEvent
argument_list|(
operator|new
name|ProcedureParameterDisplayEvent
argument_list|(
name|src
argument_list|,
name|parameter
argument_list|,
name|procedure
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-plus.png"
return|;
block|}
comment|/**      * Creates ProcedureParameter depending on context.      */
annotation|@
name|Override
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
if|if
condition|(
name|getProjectController
argument_list|()
operator|.
name|getCurrentProcedure
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Procedure
name|procedure
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
name|ProcedureParameter
name|parameter
init|=
operator|new
name|ProcedureParameter
argument_list|()
decl_stmt|;
name|parameter
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|parameter
argument_list|,
name|procedure
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|createProcedureParameter
argument_list|(
name|procedure
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateProcedureParameterUndoableEdit
argument_list|(
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
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|procedure
argument_list|,
name|parameter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|createProcedureParameter
parameter_list|(
name|Procedure
name|procedure
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|procedure
operator|.
name|addCallParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|fireProcedureParameterEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|procedure
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains a Procedure object.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
operator|(
name|ProcedureParameter
operator|)
name|object
operator|)
operator|.
name|getProcedure
argument_list|()
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

