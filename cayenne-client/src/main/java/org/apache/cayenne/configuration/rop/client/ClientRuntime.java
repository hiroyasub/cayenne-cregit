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
name|configuration
operator|.
name|rop
operator|.
name|client
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
name|CayenneRuntime
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
name|ClientConnection
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
name|Collections
import|;
end_import

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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_comment
comment|/**  * A user application entry point to Cayenne stack on the ROP client.  *   * @since 3.1  * @since 4.0 preferred way to create this class is with {@link ClientRuntime#builder()} method.  */
end_comment

begin_class
specifier|public
class|class
name|ClientRuntime
extends|extends
name|CayenneRuntime
block|{
comment|/** 	 * @since 4.0 moved from deprecated ClientLocalRuntime class 	 */
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SERVER_CHANNEL_KEY
init|=
literal|"client-server-channel"
decl_stmt|;
comment|/** 	 * Creates new builder of client runtime 	 * @return client runtime builder 	 * 	 * @since 4.0 	 */
specifier|public
specifier|static
name|ClientRuntimeBuilder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|ClientRuntimeBuilder
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|ClientRuntime
parameter_list|(
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|)
block|{
name|super
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientConnection
name|getConnection
parameter_list|()
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

