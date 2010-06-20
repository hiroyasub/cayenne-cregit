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
name|unit
operator|.
name|di
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
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|Provider
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|di
operator|.
name|DataChannelInterceptor
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
name|unit
operator|.
name|di
operator|.
name|DataChannelSyncStats
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
name|unit
operator|.
name|di
operator|.
name|UnitTestClosure
import|;
end_import

begin_class
specifier|public
class|class
name|ClientServerDataChannelInterceptor
implements|implements
name|DataChannelInterceptor
block|{
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|ClientRuntime
argument_list|>
name|clientRuntimeProvider
decl_stmt|;
specifier|private
name|ClientServerDataChannelDecorator
name|getChannelDecorator
parameter_list|()
block|{
name|LocalConnection
name|connection
init|=
operator|(
name|LocalConnection
operator|)
name|clientRuntimeProvider
operator|.
name|get
argument_list|()
operator|.
name|getConnection
argument_list|()
decl_stmt|;
return|return
operator|(
name|ClientServerDataChannelDecorator
operator|)
name|connection
operator|.
name|getChannel
argument_list|()
return|;
block|}
specifier|public
name|void
name|runWithQueriesBlocked
parameter_list|(
name|UnitTestClosure
name|closure
parameter_list|)
block|{
name|ClientServerDataChannelDecorator
name|channel
init|=
name|getChannelDecorator
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setBlockingMessages
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|closure
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|channel
operator|.
name|setBlockingMessages
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|runWithQueryCounter
parameter_list|(
name|UnitTestClosure
name|closure
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO... unused for now"
argument_list|)
throw|;
block|}
specifier|public
name|DataChannelSyncStats
name|runWithSyncStatsCollection
parameter_list|(
name|UnitTestClosure
name|closure
parameter_list|)
block|{
name|ClientServerDataChannelDecorator
name|channel
init|=
name|getChannelDecorator
argument_list|()
decl_stmt|;
name|DataChannelSyncStats
name|stats
init|=
operator|new
name|DataChannelSyncStats
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setSyncStatsCounter
argument_list|(
name|stats
argument_list|)
expr_stmt|;
try|try
block|{
name|closure
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|channel
operator|.
name|setSyncStatsCounter
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|stats
return|;
block|}
block|}
end_class

end_unit

