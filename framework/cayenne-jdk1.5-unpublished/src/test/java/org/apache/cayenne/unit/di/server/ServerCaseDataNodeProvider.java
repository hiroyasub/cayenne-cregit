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
name|unit
operator|.
name|di
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
name|ConfigurationException
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
name|di
operator|.
name|Inject
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
name|di
operator|.
name|Provider
import|;
end_import

begin_class
specifier|public
class|class
name|ServerCaseDataNodeProvider
implements|implements
name|Provider
argument_list|<
name|DataNode
argument_list|>
block|{
annotation|@
name|Inject
specifier|protected
name|ServerRuntimeFactory
name|runtimeFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ServerCaseProperties
name|properties
decl_stmt|;
specifier|public
name|DataNode
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|DataDomain
name|channel
init|=
operator|(
name|DataDomain
operator|)
name|runtimeFactory
operator|.
name|get
argument_list|(
name|properties
operator|.
name|getConfigurationLocation
argument_list|()
argument_list|)
operator|.
name|getChannel
argument_list|()
decl_stmt|;
return|return
name|channel
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
end_class

end_unit

