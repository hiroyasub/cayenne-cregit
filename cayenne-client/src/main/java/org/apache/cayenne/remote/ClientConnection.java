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
name|remote
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
name|CayenneRuntimeException
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
name|event
operator|.
name|EventBridge
import|;
end_import

begin_comment
comment|/**  * A connection object used to interact with a remote Cayenne server. Connection supports  * synchronous interaction via {@link #sendMessage(ClientMessage)} and asynchronous  * listening for server events.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ClientConnection
block|{
comment|/**      * Returns an EventBridge that receives remote server events. Caller would normally      * register returned bridge with a local EventManager, thus allowing local listeners      * to receive server events.      *       * @return An EventBridge or null if server events are not supported.      */
name|EventBridge
name|getServerEventBridge
parameter_list|()
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * Sends a synchronous ClientMessage to the server, returning a reply.      */
name|Object
name|sendMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|CayenneRuntimeException
function_decl|;
block|}
end_interface

end_unit

