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
name|project
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
name|BaseConfigurationNodeVisitor
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
name|map
operator|.
name|DataMap
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|SaveableNodesGetter
extends|extends
name|BaseConfigurationNodeVisitor
argument_list|<
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|descriptor
parameter_list|)
block|{
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|nodes
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|nodes
operator|.
name|addAll
argument_list|(
name|descriptor
operator|.
name|getDataMaps
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|nodes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
block|}
end_class

end_unit

