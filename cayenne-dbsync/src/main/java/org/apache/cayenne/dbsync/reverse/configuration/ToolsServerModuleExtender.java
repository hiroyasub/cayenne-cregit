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
name|dbsync
operator|.
name|reverse
operator|.
name|configuration
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
name|server
operator|.
name|ServerModuleExtender
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

begin_comment
comment|// this class exists so that ToolsModule can call "initAllExtensions()" that is protected in ServerModuleExtender.
end_comment

begin_class
class|class
name|ToolsServerModuleExtender
extends|extends
name|ServerModuleExtender
block|{
specifier|public
name|ToolsServerModuleExtender
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|super
argument_list|(
name|binder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ServerModuleExtender
name|initAllExtensions
parameter_list|()
block|{
return|return
name|super
operator|.
name|initAllExtensions
argument_list|()
return|;
block|}
block|}
end_class

end_unit

