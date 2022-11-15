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
name|ConfigurationNameMapper
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
name|DefaultConfigurationNameMapper
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
name|di
operator|.
name|Module
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
name|DefaultUpgradeService
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
name|UpgradeService
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
name|UpgradeHandler_V10
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
name|UpgradeHandler_V11
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
name|UpgradeHandler_V7
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
name|UpgradeHandler_V8
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
name|UpgradeHandler_V9
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
name|validation
operator|.
name|DefaultProjectValidator
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
name|validation
operator|.
name|ProjectValidator
import|;
end_import

begin_comment
comment|/**  * A dependency injection (DI) module contributing configuration related  * to Cayenne mapping project manipulation to a DI container.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModule
implements|implements
name|Module
block|{
comment|/**      * @since 5.0      */
specifier|public
specifier|static
name|ProjectModuleExtender
name|extend
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
operator|new
name|ProjectModuleExtender
argument_list|(
name|binder
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @deprecated in favor of {@link #extend(Binder)}      */
annotation|@
name|Deprecated
argument_list|(
name|since
operator|=
literal|"5.0"
argument_list|)
specifier|public
specifier|static
name|ListBuilder
argument_list|<
name|ProjectExtension
argument_list|>
name|contributeExtensions
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindList
argument_list|(
name|ProjectExtension
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @deprecated in favor of {@link #extend(Binder)}      */
annotation|@
name|Deprecated
argument_list|(
name|since
operator|=
literal|"5.0"
argument_list|)
specifier|public
specifier|static
name|ListBuilder
argument_list|<
name|UpgradeHandler
argument_list|>
name|contributeUpgradeHandler
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindList
argument_list|(
name|UpgradeHandler
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ProjectLoader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DataChannelProjectLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ProjectSaver
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FileProjectSaver
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ProjectValidator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultProjectValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNodeParentGetter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNodeParentGetter
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNameMapper
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|UpgradeService
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultUpgradeService
operator|.
name|class
argument_list|)
expr_stmt|;
name|extend
argument_list|(
name|binder
argument_list|)
operator|.
name|initAllExtensions
argument_list|()
comment|// Note: order is important
operator|.
name|addUpgradeHandler
argument_list|(
name|UpgradeHandler_V7
operator|.
name|class
argument_list|)
operator|.
name|addUpgradeHandler
argument_list|(
name|UpgradeHandler_V8
operator|.
name|class
argument_list|)
operator|.
name|addUpgradeHandler
argument_list|(
name|UpgradeHandler_V9
operator|.
name|class
argument_list|)
operator|.
name|addUpgradeHandler
argument_list|(
name|UpgradeHandler_V10
operator|.
name|class
argument_list|)
operator|.
name|addUpgradeHandler
argument_list|(
name|UpgradeHandler_V11
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

