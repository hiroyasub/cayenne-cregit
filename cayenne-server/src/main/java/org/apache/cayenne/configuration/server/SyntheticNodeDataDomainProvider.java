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
name|configuration
operator|.
name|server
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
name|access
operator|.
name|DataDomain
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
name|access
operator|.
name|DataNode
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
class|class
name|SyntheticNodeDataDomainProvider
extends|extends
name|DataDomainProvider
block|{
annotation|@
name|Override
specifier|protected
name|DataDomain
name|createAndInitDataDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|dataDomain
init|=
name|super
operator|.
name|createAndInitDataDomain
argument_list|()
decl_stmt|;
comment|// no nodes... add a synthetic node... it will become the default
if|if
condition|(
name|dataDomain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DataChannelDescriptor
name|channelDescriptor
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|channelDescriptor
operator|.
name|setName
argument_list|(
name|DEFAULT_NAME
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|(
name|DEFAULT_NAME
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|setDataChannelDescriptor
argument_list|(
name|channelDescriptor
argument_list|)
expr_stmt|;
name|DataNode
name|node
init|=
name|addDataNode
argument_list|(
name|dataDomain
argument_list|,
name|nodeDescriptor
argument_list|)
decl_stmt|;
name|dataDomain
operator|.
name|setDefaultNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
name|dataDomain
return|;
block|}
block|}
end_class

end_unit

