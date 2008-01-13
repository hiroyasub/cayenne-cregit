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
name|conn
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|PooledConnectionImplTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConnectionErrorNotificationConcurrency
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test a case when error notification is sent to connection
comment|// that has been removed from the pool, but when pool is still a
comment|// listener for its events.
name|PoolManager
name|pm
init|=
operator|new
name|PoolManager
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|startMaintenanceThread
parameter_list|()
block|{
block|}
block|}
decl_stmt|;
name|PooledConnectionImpl
name|con
init|=
operator|new
name|PooledConnectionImpl
argument_list|()
decl_stmt|;
name|con
operator|.
name|addConnectionEventListener
argument_list|(
name|pm
argument_list|)
expr_stmt|;
name|con
operator|.
name|connectionErrorNotification
argument_list|(
operator|new
name|java
operator|.
name|sql
operator|.
name|SQLException
argument_list|(
literal|"Bad SQL Exception.."
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConnectionClosedNotificationConcurrency
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test a case when closed notification is sent to connection
comment|// that has been removed from the pool, but when pool is still a
comment|// listener for its events.
name|PoolManager
name|pm
init|=
operator|new
name|PoolManager
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|startMaintenanceThread
parameter_list|()
block|{
block|}
block|}
decl_stmt|;
name|PooledConnectionImpl
name|con
init|=
operator|new
name|PooledConnectionImpl
argument_list|()
decl_stmt|;
name|con
operator|.
name|addConnectionEventListener
argument_list|(
name|pm
argument_list|)
expr_stmt|;
name|con
operator|.
name|connectionClosedNotification
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

