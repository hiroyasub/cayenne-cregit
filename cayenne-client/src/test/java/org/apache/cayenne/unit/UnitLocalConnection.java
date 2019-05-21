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
name|unit
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|DataChannel
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
name|ClientMessage
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
name|service
operator|.
name|LocalConnection
import|;
end_import

begin_comment
comment|/**  * A ClientConnection that allows to block/unblock test client/server communications.  *   */
end_comment

begin_class
specifier|public
class|class
name|UnitLocalConnection
extends|extends
name|LocalConnection
block|{
specifier|protected
name|boolean
name|blockingMessages
decl_stmt|;
specifier|public
name|UnitLocalConnection
parameter_list|(
name|DataChannel
name|handler
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UnitLocalConnection
parameter_list|(
name|DataChannel
name|handler
parameter_list|,
name|int
name|serializationPolicy
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|,
name|serializationPolicy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|sendMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
name|checkMessageAllowed
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isBlockingMessages
parameter_list|()
block|{
return|return
name|blockingMessages
return|;
block|}
specifier|public
name|void
name|setBlockingMessages
parameter_list|(
name|boolean
name|blockingQueries
parameter_list|)
block|{
name|this
operator|.
name|blockingMessages
operator|=
name|blockingQueries
expr_stmt|;
block|}
specifier|public
name|void
name|checkMessageAllowed
parameter_list|()
throws|throws
name|AssertionFailedError
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"Message is unexpected."
argument_list|,
name|blockingMessages
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

