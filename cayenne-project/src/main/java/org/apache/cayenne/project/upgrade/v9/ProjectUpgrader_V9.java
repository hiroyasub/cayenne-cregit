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
name|v9
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
name|Injector
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
name|ProjectUpgrader
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * A ProjectUpgrader that handles project upgrades from version 4.0.M4 and 8  * to version 9.  */
end_comment

begin_class
specifier|public
class|class
name|ProjectUpgrader_V9
implements|implements
name|ProjectUpgrader
block|{
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Override
specifier|public
name|UpgradeHandler
name|getUpgradeHandler
parameter_list|(
name|Resource
name|projectSource
parameter_list|)
block|{
name|UpgradeHandler_V9
name|handler
init|=
operator|new
name|UpgradeHandler_V9
argument_list|(
name|projectSource
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|handler
return|;
block|}
block|}
end_class

end_unit

