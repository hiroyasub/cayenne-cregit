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
name|rop
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Constants
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
name|MapBuilder
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
name|remote
operator|.
name|RemoteService
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianService
import|;
end_import

begin_comment
comment|/**  * A DI module that defines services for the server-side of an ROP application based on  * Caucho Hessian.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ROPServerModule
implements|implements
name|Module
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeProperties
decl_stmt|;
specifier|public
name|ROPServerModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeProperties
parameter_list|)
block|{
name|this
operator|.
name|eventBridgeProperties
operator|=
name|eventBridgeProperties
expr_stmt|;
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|MapBuilder
argument_list|<
name|String
argument_list|>
name|mapBuilder
init|=
name|binder
operator|.
name|bindMap
argument_list|(
name|Constants
operator|.
name|SERVER_ROP_EVENT_BRIDGE_PROPERTIES_MAP
argument_list|)
decl_stmt|;
name|mapBuilder
operator|.
name|putAll
argument_list|(
name|eventBridgeProperties
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|RemoteService
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|HessianService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

