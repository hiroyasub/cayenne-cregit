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
name|event
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
name|access
operator|.
name|event
operator|.
name|SnapshotEvent
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
name|test
operator|.
name|parallel
operator|.
name|ParallelTestContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|List
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|EventBridgeTest
block|{
specifier|private
name|List
argument_list|<
name|DefaultEventManager
argument_list|>
name|managersToClean
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|cleanEventManagers
parameter_list|()
block|{
for|for
control|(
name|DefaultEventManager
name|manager
range|:
name|managersToClean
control|)
block|{
name|manager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|managersToClean
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|local
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|EventBridgeTest
operator|.
name|class
argument_list|,
literal|"testInstall"
argument_list|)
decl_stmt|;
name|String
name|external
init|=
literal|"externalSubject"
decl_stmt|;
name|TestBridge
name|bridge
init|=
operator|new
name|TestBridge
argument_list|(
name|local
argument_list|,
name|external
argument_list|)
decl_stmt|;
name|Collection
name|subjects
init|=
name|bridge
operator|.
name|getLocalSubjects
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subjects
operator|.
name|contains
argument_list|(
name|local
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|external
argument_list|,
name|bridge
operator|.
name|getExternalSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStartup
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|local
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|EventBridgeTest
operator|.
name|class
argument_list|,
literal|"testInstall"
argument_list|)
decl_stmt|;
name|String
name|external
init|=
literal|"externalSubject"
decl_stmt|;
name|TestBridge
name|bridge
init|=
operator|new
name|TestBridge
argument_list|(
name|local
argument_list|,
name|external
argument_list|)
decl_stmt|;
name|DefaultEventManager
name|manager
init|=
operator|new
name|DefaultEventManager
argument_list|()
decl_stmt|;
name|managersToClean
operator|.
name|add
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|startup
argument_list|(
name|manager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|manager
argument_list|,
name|bridge
operator|.
name|eventManager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bridge
operator|.
name|startupCalls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bridge
operator|.
name|shutdownCalls
argument_list|)
expr_stmt|;
comment|// try startup again
name|DefaultEventManager
name|newManager
init|=
operator|new
name|DefaultEventManager
argument_list|()
decl_stmt|;
name|managersToClean
operator|.
name|add
argument_list|(
name|newManager
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|startup
argument_list|(
name|newManager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|newManager
argument_list|,
name|bridge
operator|.
name|eventManager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bridge
operator|.
name|startupCalls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bridge
operator|.
name|shutdownCalls
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|local
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|EventBridgeTest
operator|.
name|class
argument_list|,
literal|"testInstall"
argument_list|)
decl_stmt|;
name|String
name|external
init|=
literal|"externalSubject"
decl_stmt|;
name|TestBridge
name|bridge
init|=
operator|new
name|TestBridge
argument_list|(
name|local
argument_list|,
name|external
argument_list|)
decl_stmt|;
name|DefaultEventManager
name|manager
init|=
operator|new
name|DefaultEventManager
argument_list|()
decl_stmt|;
name|managersToClean
operator|.
name|add
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|startup
argument_list|(
name|manager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|bridge
operator|.
name|eventManager
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bridge
operator|.
name|startupCalls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bridge
operator|.
name|shutdownCalls
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendExternalEvent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|EventSubject
name|local
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|EventBridgeTest
operator|.
name|class
argument_list|,
literal|"testInstall"
argument_list|)
decl_stmt|;
name|String
name|external
init|=
literal|"externalSubject"
decl_stmt|;
specifier|final
name|TestBridge
name|bridge
init|=
operator|new
name|TestBridge
argument_list|(
name|local
argument_list|,
name|external
argument_list|)
decl_stmt|;
name|DefaultEventManager
name|manager
init|=
operator|new
name|DefaultEventManager
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|managersToClean
operator|.
name|add
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|startup
argument_list|(
name|manager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|)
expr_stmt|;
specifier|final
name|SnapshotEvent
name|eventWithNoSubject
init|=
operator|new
name|SnapshotEvent
argument_list|(
name|this
argument_list|,
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|eventWithNoSubject
argument_list|,
name|local
argument_list|)
expr_stmt|;
comment|// check that event was received and that subject was injected...
comment|// since bridge is notified asynchronously by default,
comment|// we must wait till notification is received
name|ParallelTestContainer
name|helper
init|=
operator|new
name|ParallelTestContainer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|bridge
operator|.
name|lastLocalEvent
operator|instanceof
name|SnapshotEvent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|local
argument_list|,
name|bridge
operator|.
name|lastLocalEvent
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|helper
operator|.
name|runTest
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
specifier|final
name|SnapshotEvent
name|eventWithSubject
init|=
operator|new
name|SnapshotEvent
argument_list|(
name|this
argument_list|,
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|eventWithSubject
operator|.
name|setSubject
argument_list|(
name|local
argument_list|)
expr_stmt|;
name|manager
operator|.
name|postEvent
argument_list|(
name|eventWithNoSubject
argument_list|,
name|local
argument_list|)
expr_stmt|;
comment|// check that event was received and that subject was injected...
comment|// since bridge is notified asynchronously by default,
comment|// we must wait till notification is received
name|ParallelTestContainer
name|helper1
init|=
operator|new
name|ParallelTestContainer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|bridge
operator|.
name|lastLocalEvent
operator|instanceof
name|SnapshotEvent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|local
argument_list|,
name|bridge
operator|.
name|lastLocalEvent
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|helper1
operator|.
name|runTest
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
class|class
name|TestBridge
extends|extends
name|EventBridge
block|{
name|CayenneEvent
name|lastLocalEvent
decl_stmt|;
name|int
name|startupCalls
decl_stmt|;
name|int
name|shutdownCalls
decl_stmt|;
specifier|public
name|TestBridge
parameter_list|(
name|EventSubject
name|localSubject
parameter_list|,
name|String
name|externalSubject
parameter_list|)
block|{
name|super
argument_list|(
name|localSubject
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sendExternalEvent
parameter_list|(
name|CayenneEvent
name|event
parameter_list|)
block|{
name|lastLocalEvent
operator|=
name|event
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdownCalls
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
block|{
name|startupCalls
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

