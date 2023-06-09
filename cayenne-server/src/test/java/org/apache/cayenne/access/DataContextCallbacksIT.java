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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|map
operator|.
name|EntityResolver
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
name|map
operator|.
name|LifecycleEvent
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
name|reflect
operator|.
name|LifecycleCallbackRegistry
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
name|testdo
operator|.
name|testmap
operator|.
name|Painting
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
name|CayenneProjects
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextCallbacksIT
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
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostAddCallbacks
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
comment|// no callbacks
name|Artist
name|a1
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
name|assertNotNull
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postAddCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener2
init|=
operator|new
name|MockCallingBackListener
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener2
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a3
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a3
argument_list|,
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p3
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a3
argument_list|,
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrePersistCallbacks
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
comment|// no callbacks
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPrePersisted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPrePersisted
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"prePersistCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|isPrePersisted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|isPrePersisted
argument_list|()
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener2
init|=
operator|new
name|MockCallingBackListener
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener2
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|a3
argument_list|,
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPreRemoveCallbacks
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
comment|// no callbacks
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPreRemoved
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_REMOVE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"preRemoveCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|isPreRemoved
argument_list|()
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener2
init|=
operator|new
name|MockCallingBackListener
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_REMOVE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener2
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a3
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a3
operator|.
name|isPreRemoved
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a3
argument_list|,
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p3
operator|.
name|isPostAdded
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p3
operator|.
name|isPreRemoved
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a3
argument_list|,
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

