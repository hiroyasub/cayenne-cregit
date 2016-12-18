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
name|project
operator|.
name|upgrade
operator|.
name|v6
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
name|configuration
operator|.
name|ConfigurationTree
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
name|project
operator|.
name|Project
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
name|project
operator|.
name|ProjectSaver
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
name|project
operator|.
name|upgrade
operator|.
name|BaseUpgradeHandler
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
name|project
operator|.
name|upgrade
operator|.
name|UpgradeMetaData
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|List
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|UpgradeHandler_V6
extends|extends
name|BaseUpgradeHandler
block|{
specifier|static
specifier|final
name|String
name|TO_VERSION
init|=
literal|"6"
decl_stmt|;
comment|/**      * Notice that the loader is statically typed, intentionally not using DI to ensure      * predictable behavior on legacy upgrades.      */
specifier|private
name|XMLDataChannelDescriptorLoader_V3_0_0_1
name|projectLoader
decl_stmt|;
comment|/**      * Unlike loader, saver is injected, so that we can save dynamically with the latest      * version. This may change once this upgrade handler becomes an intermediate handler.      */
annotation|@
name|Inject
specifier|private
name|ProjectSaver
name|projectSaver
decl_stmt|;
name|UpgradeHandler_V6
parameter_list|(
name|Resource
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectLoader
operator|=
operator|new
name|XMLDataChannelDescriptorLoader_V3_0_0_1
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Resource
name|doPerformUpgrade
parameter_list|(
name|UpgradeMetaData
name|metaData
parameter_list|)
throws|throws
name|ConfigurationException
block|{
name|List
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|domains
init|=
name|projectLoader
operator|.
name|load
argument_list|(
name|projectSource
argument_list|)
decl_stmt|;
if|if
condition|(
name|domains
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// create a single domain dummy project if a noop config is being upgraded
name|DataChannelDescriptor
name|descriptor
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setName
argument_list|(
literal|"DEFAULT"
argument_list|)
expr_stmt|;
name|domains
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
comment|// collect resources to delete before the upgrade...
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resourcesToDelete
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DataChannelDescriptor
name|descriptor
range|:
name|domains
control|)
block|{
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
name|Resource
name|nodeResource
init|=
name|node
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodeResource
operator|!=
literal|null
condition|)
block|{
name|resourcesToDelete
operator|.
name|add
argument_list|(
name|nodeResource
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// save in the new format
for|for
control|(
name|DataChannelDescriptor
name|descriptor
range|:
name|domains
control|)
block|{
name|Project
name|project
init|=
operator|new
name|Project
argument_list|(
operator|new
name|ConfigurationTree
argument_list|<>
argument_list|(
name|descriptor
argument_list|)
argument_list|)
decl_stmt|;
name|attachToNamespace
argument_list|(
operator|(
name|DataChannelDescriptor
operator|)
name|project
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// side effect of that is deletion of the common "cayenne.xml"
name|projectSaver
operator|.
name|save
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
comment|// delete all .driver.xml files
for|for
control|(
name|Resource
name|resource
range|:
name|resourcesToDelete
control|)
block|{
try|try
block|{
name|File
name|file
init|=
name|Util
operator|.
name|toFile
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".driver.xml"
argument_list|)
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore...
block|}
block|}
comment|// returns the first domain configuration out of possibly multiple new
comment|// configurations...
return|return
name|domains
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getConfigurationSource
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getToVersion
parameter_list|()
block|{
return|return
name|TO_VERSION
return|;
block|}
block|}
end_class

end_unit

