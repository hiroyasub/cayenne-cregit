begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * https://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|java
operator|.
name|rmi
operator|.
name|Remote
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_comment
comment|/**  * Interface of a Cayenne remote service.  *  * @since 1.2  * @see org.apache.cayenne.rop.ROPServlet  */
end_comment

begin_interface
specifier|public
interface|interface
name|RemoteService
extends|extends
name|Remote
block|{
comment|/**      * Establishes a dedicated session with Cayenne DataChannel, returning session id.      */
name|RemoteSession
name|establishSession
parameter_list|()
throws|throws
name|RemoteException
function_decl|;
comment|/**      * Creates a new session with the specified or joins an existing one. This method is      * used to bootstrap collaborating clients of a single "group chat".      */
name|RemoteSession
name|establishSharedSession
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RemoteException
function_decl|;
comment|/**      * Processes message on a remote server, returning the result of such processing.      */
name|Object
name|processMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|RemoteException
throws|,
name|Throwable
function_decl|;
comment|/**      * Close remote service resources.      * @sine 4.0      */
name|void
name|close
parameter_list|()
throws|throws
name|RemoteException
function_decl|;
block|}
end_interface

end_unit

