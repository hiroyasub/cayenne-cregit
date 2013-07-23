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
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
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
name|TestCase
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

begin_class
specifier|public
class|class
name|DefaultEventManagerTest
extends|extends
name|TestCase
implements|implements
name|EventListener
block|{
comment|// used for counting received events on the class
specifier|public
specifier|static
name|int
name|numberOfReceivedEventsForClass
decl_stmt|;
comment|// used for counting received events per listener instance
specifier|public
name|int
name|numberOfReceivedEvents
decl_stmt|;
comment|// the event manager used for testing
specifier|private
name|EventManager
name|eventManager
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|eventManager
operator|=
operator|new
name|DefaultEventManager
argument_list|()
expr_stmt|;
name|numberOfReceivedEvents
operator|=
literal|0
expr_stmt|;
name|numberOfReceivedEventsForClass
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|void
name|testSubjectListenerWouldRegisterListener
parameter_list|()
block|{
name|MockListener
name|listener
init|=
operator|new
name|MockListener
argument_list|(
name|eventManager
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
literal|"processEvent"
argument_list|,
name|EventObject
operator|.
name|class
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|)
expr_stmt|;
comment|// test concurrent modification of the queue ... on event listener would attempt
comment|// adding another listener
comment|// add more than one listener to see that dispatch can proceed after one of the
comment|// listeners recats to event
name|eventManager
operator|.
name|addListener
argument_list|(
operator|new
name|MockListener
argument_list|(
name|eventManager
argument_list|)
argument_list|,
literal|"processEvent"
argument_list|,
name|EventObject
operator|.
name|class
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|EventObject
argument_list|(
name|this
argument_list|)
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectListenerWouldRegisterListener
parameter_list|()
block|{
name|MockListener
name|listener
init|=
operator|new
name|MockListener
argument_list|(
name|eventManager
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
literal|"processEvent"
argument_list|,
name|EventObject
operator|.
name|class
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// test concurrent modification of the queue ... on event listener would attempt
comment|// adding another listener
comment|// add more than one listener to see that dispatch can proceed after one of the
comment|// listeners recats to event
name|eventManager
operator|.
name|addListener
argument_list|(
operator|new
name|MockListener
argument_list|(
name|eventManager
argument_list|,
name|this
argument_list|)
argument_list|,
literal|"processEvent"
argument_list|,
name|EventObject
operator|.
name|class
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|EventObject
argument_list|(
name|this
argument_list|)
argument_list|,
name|MockListener
operator|.
name|mockSubject
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullListener
parameter_list|()
block|{
try|try
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"hansi"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ia
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testNullNotification
parameter_list|()
block|{
comment|// null notification
try|try
block|{
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"testNullObserver"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
comment|// invalid event class
try|try
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"testNullObserver"
argument_list|,
literal|null
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
comment|// empty string notification
try|try
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"testNullObserver"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testNonexistingMethod
parameter_list|()
block|{
try|try
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"hansi"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"thisMethodDoesNotExist"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testInvalidArgumentTypes
parameter_list|()
block|{
try|try
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"hansi"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeTheWrongMethod"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testNonretainedListener
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
operator|new
name|DefaultEventManagerTest
argument_list|()
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// (hopefully) make the listener go away
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|numberOfReceivedEventsForClass
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidSubclassOfRegisteredEventClass
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|MyCayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWrongRegisteredEventClass
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
comment|// we register a method that takes a CayenneEvent or subclass thereof..
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// ..but post a subclass of EventObject that is not compatible with CayenneEvent
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|EventObject
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|0
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessfulNotificationDefaultSender
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultEventManagerTest
name|listener1
init|=
name|this
decl_stmt|;
name|DefaultEventManagerTest
name|listener2
init|=
operator|new
name|DefaultEventManagerTest
argument_list|()
decl_stmt|;
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener1
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener2
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|listener1
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|listener2
argument_list|)
expr_stmt|;
name|assertReceivedEventsForClass
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessfulNotificationIndividualSender
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|assertReceivedEventsForClass
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessfulNotificationIndividualSenderTwice
parameter_list|()
throws|throws
name|Exception
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|2
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|assertReceivedEventsForClass
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessfulNotificationBothDefaultAndIndividualSender
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultEventManagerTest
name|listener1
init|=
name|this
decl_stmt|;
name|DefaultEventManagerTest
name|listener2
init|=
operator|new
name|DefaultEventManagerTest
argument_list|()
decl_stmt|;
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener1
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|listener1
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|listener2
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|)
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|listener1
argument_list|)
expr_stmt|;
name|assertReceivedEvents
argument_list|(
literal|1
argument_list|,
name|listener2
argument_list|)
expr_stmt|;
name|assertReceivedEventsForClass
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveOnEmptyList
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveOnNullSubject
parameter_list|()
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveFromDefaultQueue
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveSpecificQueue
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveSpecificSender
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveNullSender
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveNonexistingSender
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveAll
parameter_list|()
block|{
name|EventSubject
name|subject1
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX1"
argument_list|)
decl_stmt|;
name|EventSubject
name|subject2
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX2"
argument_list|)
decl_stmt|;
name|EventSubject
name|subject3
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX3"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject1
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject2
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject3
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject1
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject2
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|subject3
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSubjectGarbageCollection
parameter_list|()
block|{
name|EventSubject
name|subject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"XXX"
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"seeNotification"
argument_list|,
name|CayenneEvent
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// let go of the subject& (hopefully) release queue
name|subject
operator|=
literal|null
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// notification method
specifier|public
name|void
name|seeNotification
parameter_list|(
name|CayenneEvent
name|event
parameter_list|)
block|{
name|numberOfReceivedEvents
operator|++
expr_stmt|;
name|numberOfReceivedEventsForClass
operator|++
expr_stmt|;
block|}
comment|// allows just enough time for the event threads to run
specifier|private
specifier|static
name|void
name|assertReceivedEvents
parameter_list|(
specifier|final
name|int
name|expected
parameter_list|,
specifier|final
name|DefaultEventManagerTest
name|listener
parameter_list|)
throws|throws
name|Exception
block|{
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
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|listener
operator|.
name|numberOfReceivedEvents
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
block|}
comment|// allows just enough time for the event threads to run
specifier|private
specifier|static
name|void
name|assertReceivedEventsForClass
parameter_list|(
specifier|final
name|int
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
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
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|numberOfReceivedEventsForClass
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
block|}
block|}
end_class

begin_comment
comment|// dummy class to test for incompatible events
end_comment

begin_class
class|class
name|MyCayenneEvent
extends|extends
name|CayenneEvent
block|{
specifier|public
name|MyCayenneEvent
parameter_list|(
name|EventListener
name|l
parameter_list|)
block|{
name|super
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
