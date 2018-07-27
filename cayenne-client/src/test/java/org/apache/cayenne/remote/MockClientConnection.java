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
name|remote
package|;
end_package

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
comment|/**  * A noop CayenneConnector used for unit testing. Accumulates commands sent via this  * connector without doing anything with them.  *   */
end_comment

begin_class
specifier|public
class|class
name|MockClientConnection
implements|implements
name|ClientConnection
block|{
specifier|protected
name|Collection
argument_list|<
name|ClientMessage
argument_list|>
name|commands
decl_stmt|;
specifier|protected
name|Object
name|fakeResponse
decl_stmt|;
specifier|public
name|MockClientConnection
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MockClientConnection
parameter_list|(
name|Object
name|defaultResponse
parameter_list|)
block|{
name|this
operator|.
name|commands
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|fakeResponse
operator|=
name|defaultResponse
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|commands
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fakeResponse
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|EventBridge
name|getServerEventBridge
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setResponse
parameter_list|(
name|Object
name|fakeResponse
parameter_list|)
block|{
name|this
operator|.
name|fakeResponse
operator|=
name|fakeResponse
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|ClientMessage
argument_list|>
name|getCommands
parameter_list|()
block|{
return|return
name|commands
return|;
block|}
specifier|public
name|Object
name|sendMessage
parameter_list|(
name|ClientMessage
name|command
parameter_list|)
block|{
name|commands
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return
name|fakeResponse
return|;
block|}
block|}
end_class

end_unit

