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
name|validation
operator|.
name|ValidationFailure
import|;
end_import

begin_class
specifier|public
class|class
name|EmbeddableErrorMsg
extends|extends
name|ValidationDisplayHandler
block|{
specifier|protected
name|DataMap
name|map
decl_stmt|;
specifier|protected
name|Embeddable
name|embeddable
decl_stmt|;
specifier|public
name|EmbeddableErrorMsg
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
name|Object
name|object
init|=
name|result
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|embeddable
operator|=
operator|(
name|Embeddable
operator|)
name|object
expr_stmt|;
name|map
operator|=
name|embeddable
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
annotation|@
name|Override
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
name|EmbeddableDisplayEvent
name|event
init|=
operator|new
name|EmbeddableDisplayEvent
argument_list|(
name|frame
argument_list|,
name|embeddable
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireEmbeddableDisplayEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

