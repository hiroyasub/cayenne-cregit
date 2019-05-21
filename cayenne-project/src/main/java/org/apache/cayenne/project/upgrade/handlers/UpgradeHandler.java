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
operator|.
name|upgrade
operator|.
name|handlers
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
name|project
operator|.
name|upgrade
operator|.
name|UpgradeUnit
import|;
end_import

begin_comment
comment|/**  * Interface that upgrade handlers should implement.  * Implementation also should be injected into DI stack in right order.  *  * @since 4.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|UpgradeHandler
block|{
comment|/**      * @return target version for this handler      */
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Process DOM for the project root file (e.g. cayenne-project.xml)      */
name|void
name|processProjectDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
function_decl|;
comment|/**      * Process DOM for the data map file (e.g. datamap.map.xml)      */
name|void
name|processDataMapDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
function_decl|;
comment|/**      * This method should be avoided as much as possible, as      * using this method will make upgrade process not future proof and      * will require refactoring if model should change.      */
specifier|default
name|void
name|processModel
parameter_list|(
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|)
block|{
block|}
block|}
end_interface

end_unit

