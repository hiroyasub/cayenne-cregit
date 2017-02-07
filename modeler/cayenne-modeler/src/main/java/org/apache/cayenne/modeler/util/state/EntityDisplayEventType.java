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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|EntityDisplayEvent
import|;
end_import

begin_class
class|class
name|EntityDisplayEventType
extends|extends
name|DisplayEventType
block|{
name|EntityDisplayEventType
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
name|DataNodeDescriptor
name|dataNode
init|=
name|dataChannel
operator|.
name|getNodeDescriptor
argument_list|(
name|preferences
operator|.
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
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
name|Entity
name|entity
init|=
name|getLastEntity
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EntityDisplayEvent
name|entityDisplayEvent
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|dataMap
argument_list|,
name|dataNode
argument_list|,
name|dataChannel
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
name|controller
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
name|entityDisplayEvent
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
name|controller
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
name|entityDisplayEvent
argument_list|)
expr_stmt|;
block|}
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
name|EntityDisplayEvent
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
name|setNode
argument_list|(
name|controller
operator|.
name|getCurrentDataNode
argument_list|()
operator|!=
literal|null
condition|?
name|controller
operator|.
name|getCurrentDataNode
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|""
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
if|if
condition|(
name|controller
operator|.
name|getCurrentObjEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|preferences
operator|.
name|setObjEntity
argument_list|(
name|controller
operator|.
name|getCurrentObjEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setDbEntity
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|controller
operator|.
name|getCurrentDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|preferences
operator|.
name|setDbEntity
argument_list|(
name|controller
operator|.
name|getCurrentDbEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setObjEntity
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|Entity
name|getLastEntity
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
operator|!
name|preferences
operator|.
name|getObjEntity
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
name|dataMap
operator|.
name|getObjEntity
argument_list|(
name|preferences
operator|.
name|getObjEntity
argument_list|()
argument_list|)
else|:
name|dataMap
operator|.
name|getDbEntity
argument_list|(
name|preferences
operator|.
name|getDbEntity
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

