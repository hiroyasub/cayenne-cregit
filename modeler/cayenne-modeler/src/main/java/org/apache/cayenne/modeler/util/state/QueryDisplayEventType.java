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
name|util
operator|.
name|state
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
name|map
operator|.
name|QueryDescriptor
import|;
end_import

begin_class
class|class
name|QueryDisplayEventType
extends|extends
name|DisplayEventType
block|{
specifier|public
name|QueryDisplayEventType
parameter_list|(
name|ProjectController
name|controller
parameter_list|)
block|{
name|super
argument_list|(
name|controller
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|fireLastDisplayEvent
parameter_list|()
block|{
name|DataChannelDescriptor
name|dataChannel
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|controller
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dataChannel
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|preferences
operator|.
name|getDomain
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|DataMap
name|dataMap
init|=
name|dataChannel
operator|.
name|getDataMap
argument_list|(
name|preferences
operator|.
name|getDataMap
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataMap
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|QueryDescriptor
name|query
init|=
name|dataMap
operator|.
name|getQueryDescriptor
argument_list|(
name|preferences
operator|.
name|getQuery
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|QueryDisplayEvent
name|queryDisplayEvent
init|=
operator|new
name|QueryDisplayEvent
argument_list|(
name|this
argument_list|,
name|query
argument_list|,
name|dataMap
argument_list|,
name|dataChannel
argument_list|)
decl_stmt|;
name|controller
operator|.
name|fireQueryDisplayEvent
argument_list|(
name|queryDisplayEvent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveLastDisplayEvent
parameter_list|()
block|{
name|preferences
operator|.
name|setEvent
argument_list|(
name|QueryDisplayEvent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setDomain
argument_list|(
name|controller
operator|.
name|getCurrentDataChanel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setDataMap
argument_list|(
name|controller
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setQuery
argument_list|(
name|controller
operator|.
name|getCurrentQuery
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

