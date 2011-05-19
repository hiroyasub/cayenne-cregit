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
name|access
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
name|DataChannelListener
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
name|ObjectContext
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
name|graph
operator|.
name|GraphEvent
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|server
operator|.
name|ServerCase
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
name|server
operator|.
name|UseServerRuntime
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
name|util
operator|.
name|ThreadedTestHelper
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
name|util
operator|.
name|EventUtil
import|;
end_import

begin_comment
comment|/**  * Tests that DataContext sends DataChannel events.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextDataChannelEventsTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|peer
decl_stmt|;
specifier|public
name|void
name|testCommitEvent
parameter_list|()
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|MockChannelListener
name|listener
init|=
operator|new
name|MockChannelListener
argument_list|()
decl_stmt|;
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
name|context
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|graphCommitted
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphChanged
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphRolledBack
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRollbackEvent
parameter_list|()
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|MockChannelListener
name|listener
init|=
operator|new
name|MockChannelListener
argument_list|()
decl_stmt|;
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
name|context
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphCommitted
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphChanged
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|graphRolledBack
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeEventOnChildChange
parameter_list|()
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|MockChannelListener
name|listener
init|=
operator|new
name|MockChannelListener
argument_list|()
decl_stmt|;
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
name|context
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|ObjectContext
name|child
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|child
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|child
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphCommitted
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|graphChanged
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphRolledBack
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeEventOnPeerChange
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
specifier|final
name|MockChannelListener
name|listener
init|=
operator|new
name|MockChannelListener
argument_list|()
decl_stmt|;
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
name|context
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|peer
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|peer
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
operator|new
name|ThreadedTestHelper
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
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphCommitted
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|graphChanged
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphRolledBack
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeEventOnPeerChangeSecondNestingLevel
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|childPeer1
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|childPeer1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|childPeer1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
specifier|final
name|MockChannelListener
name|listener
init|=
operator|new
name|MockChannelListener
argument_list|()
decl_stmt|;
name|EventUtil
operator|.
name|listenForChannelEvents
argument_list|(
operator|(
name|DataChannel
operator|)
name|childPeer1
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|ObjectContext
name|childPeer2
init|=
name|context
operator|.
name|createChildContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|childPeer2
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|childPeer2
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
operator|new
name|ThreadedTestHelper
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
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphCommitted
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|graphChanged
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|graphRolledBack
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
class|class
name|MockChannelListener
implements|implements
name|DataChannelListener
block|{
name|boolean
name|graphChanged
decl_stmt|;
name|boolean
name|graphCommitted
decl_stmt|;
name|boolean
name|graphRolledBack
decl_stmt|;
specifier|public
name|void
name|graphChanged
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
block|{
name|graphChanged
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|graphFlushed
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
block|{
name|graphCommitted
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|graphRolledback
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
block|{
name|graphRolledBack
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

