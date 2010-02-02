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
name|query
operator|.
name|QueryTypeController
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|CreateQueryAction
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
literal|"Create Query"
return|;
block|}
comment|/**      * Constructor for CreateQueryAction.      */
specifier|public
name|CreateQueryAction
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
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-query.gif"
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
name|createQuery
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|createQuery
parameter_list|()
block|{
operator|new
name|QueryTypeController
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
operator|.
name|startup
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|createQuery
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|dataMap
operator|.
name|addQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// notify listeners
name|fireQueryEvent
argument_list|(
name|this
argument_list|,
name|getProjectController
argument_list|()
argument_list|,
name|domain
argument_list|,
name|dataMap
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fires events when a query was added      */
specifier|public
specifier|static
name|void
name|fireQueryEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|src
argument_list|,
name|query
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|,
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryDisplayEvent
argument_list|(
operator|new
name|QueryDisplayEvent
argument_list|(
name|src
argument_list|,
name|query
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

