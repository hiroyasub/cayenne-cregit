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
name|project2
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
name|project2
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * A ProjectUpgrader that handles project upgrades to version 6.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ProjectUpgrader_V6
implements|implements
name|ProjectUpgrader
block|{
specifier|public
name|UpgradeHandler
name|getUpgradeHandler
parameter_list|(
name|Resource
name|projectSource
parameter_list|)
block|{
return|return
operator|new
name|UpgradeHandler_V6
argument_list|(
name|projectSource
argument_list|)
return|;
block|}
block|}
end_class

end_unit

