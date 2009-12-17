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
name|project2
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * Updates ConfigurationNode's configuration sources.  *   * @since 3.1  */
end_comment

begin_class
class|class
name|ConfigurationSourceSetter
extends|extends
name|BaseConfigurationNodeVisitor
argument_list|<
name|Void
argument_list|>
block|{
specifier|private
name|Resource
name|configurationSource
decl_stmt|;
name|ConfigurationSourceSetter
parameter_list|(
name|Resource
name|configurationSource
parameter_list|)
block|{
name|this
operator|.
name|configurationSource
operator|=
name|configurationSource
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|node
parameter_list|)
block|{
name|node
operator|.
name|setConfigurationSource
argument_list|(
name|configurationSource
argument_list|)
expr_stmt|;
comment|// update child configurations
for|for
control|(
name|DataNodeDescriptor
name|childDescriptor
range|:
name|node
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
name|childDescriptor
operator|.
name|setConfigurationSource
argument_list|(
name|configurationSource
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDataMap
parameter_list|(
name|DataMap
name|node
parameter_list|)
block|{
name|node
operator|.
name|setConfigurationSource
argument_list|(
name|configurationSource
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

