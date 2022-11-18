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
name|di
operator|.
name|Binder
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
name|ListBuilder
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
name|extension
operator|.
name|ProjectExtension
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
name|handlers
operator|.
name|UpgradeHandler
import|;
end_import

begin_comment
comment|/**  * @since 5.0  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModuleExtender
block|{
specifier|private
specifier|final
name|Binder
name|binder
decl_stmt|;
specifier|private
name|ListBuilder
argument_list|<
name|UpgradeHandler
argument_list|>
name|upgradeHandlers
decl_stmt|;
specifier|private
name|ListBuilder
argument_list|<
name|ProjectExtension
argument_list|>
name|extensions
decl_stmt|;
specifier|public
name|ProjectModuleExtender
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|this
operator|.
name|binder
operator|=
name|binder
expr_stmt|;
block|}
specifier|protected
name|ProjectModuleExtender
name|initAllExtensions
parameter_list|()
block|{
name|contributeExtensions
argument_list|()
expr_stmt|;
name|contributeUpgradeHandler
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProjectModuleExtender
name|addUpgradeHandler
parameter_list|(
name|UpgradeHandler
name|handler
parameter_list|)
block|{
name|contributeUpgradeHandler
argument_list|()
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProjectModuleExtender
name|addUpgradeHandler
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|UpgradeHandler
argument_list|>
name|handler
parameter_list|)
block|{
name|contributeUpgradeHandler
argument_list|()
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProjectModuleExtender
name|addExtension
parameter_list|(
name|ProjectExtension
name|extension
parameter_list|)
block|{
name|contributeExtensions
argument_list|()
operator|.
name|add
argument_list|(
name|extension
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ProjectModuleExtender
name|addExtension
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ProjectExtension
argument_list|>
name|extension
parameter_list|)
block|{
name|contributeExtensions
argument_list|()
operator|.
name|add
argument_list|(
name|extension
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|ListBuilder
argument_list|<
name|ProjectExtension
argument_list|>
name|contributeExtensions
parameter_list|()
block|{
if|if
condition|(
name|extensions
operator|==
literal|null
condition|)
block|{
name|extensions
operator|=
name|binder
operator|.
name|bindList
argument_list|(
name|ProjectExtension
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|extensions
return|;
block|}
specifier|private
name|ListBuilder
argument_list|<
name|UpgradeHandler
argument_list|>
name|contributeUpgradeHandler
parameter_list|()
block|{
if|if
condition|(
name|upgradeHandlers
operator|==
literal|null
condition|)
block|{
name|upgradeHandlers
operator|=
name|binder
operator|.
name|bindList
argument_list|(
name|UpgradeHandler
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|upgradeHandlers
return|;
block|}
block|}
end_class

end_unit

