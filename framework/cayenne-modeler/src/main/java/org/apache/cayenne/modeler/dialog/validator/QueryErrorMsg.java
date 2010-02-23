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
name|query
operator|.
name|Query
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
name|validation
operator|.
name|ValidationFailure
import|;
end_import

begin_comment
comment|/**  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|QueryErrorMsg
extends|extends
name|ValidationDisplayHandler
block|{
specifier|public
name|QueryErrorMsg
parameter_list|(
name|ValidationFailure
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|result
argument_list|)
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
name|Object
name|object
init|=
name|super
operator|.
name|validationFailure
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|DataChannelDescriptor
name|domain
init|=
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
decl_stmt|;
name|Query
name|query
init|=
operator|(
name|Query
operator|)
name|object
decl_stmt|;
name|DataMap
name|map
init|=
name|query
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|QueryDisplayEvent
name|event
init|=
operator|new
name|QueryDisplayEvent
argument_list|(
name|frame
argument_list|,
name|query
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireQueryDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

