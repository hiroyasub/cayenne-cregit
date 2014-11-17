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
name|PersistenceState
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
name|Persistent
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
name|query
operator|.
name|EJBQLQuery
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
name|query
operator|.
name|RefreshQuery
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
name|query
operator|.
name|SelectQuery
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
name|DataDomainCallbacksIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context1
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testPostLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
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
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetCallbackFlags
argument_list|()
expr_stmt|;
name|listener
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// post load must be called on rollback...
name|a1
operator|.
name|resetCallbackFlags
argument_list|()
expr_stmt|;
name|listener
operator|.
name|reset
argument_list|()
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// now change and rollback the artist - postLoad must be called
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// test invalidated
name|a1
operator|.
name|resetCallbackFlags
argument_list|()
expr_stmt|;
name|listener
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
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
name|testPostLoad_MixedResult
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
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
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|q
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select a, a.artistName from Artist a"
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
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
name|testPostLoad_Relationship
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
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
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
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
name|Painting
name|p1
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
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|a1
argument_list|,
name|p1
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|=
operator|(
name|Painting
operator|)
name|context1
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// this should be a hollow object, so no callback just yet
name|a1
operator|=
name|p1
operator|.
name|getToArtist
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
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
name|testPostLoad_Prefetch
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
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
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
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
name|Painting
name|p1
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
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
expr_stmt|;
name|p1
operator|=
operator|(
name|Painting
operator|)
name|context1
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// artist is prefetched here, and a callback must have been invoked
name|a1
operator|=
name|p1
operator|.
name|getToArtist
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
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
name|testPostLoad_LocalObject
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
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
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context1
operator|.
name|localObject
argument_list|(
name|a1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a2
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|a2
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a2
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a2
argument_list|,
name|listener
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
name|testPostLoad_ThatModifiesObject
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postLoadCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener
init|=
operator|new
name|MockCallingBackListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|publicCallback
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|super
operator|.
name|publicCallback
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|entity
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"Changed"
argument_list|)
expr_stmt|;
comment|// modify object during postLoad callback
block|}
block|}
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
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
name|Painting
name|p1
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
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|a1
argument_list|,
name|p1
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|=
operator|(
name|Painting
operator|)
name|context1
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// this should be a hollow object, so no callback just yet
name|a1
operator|=
name|p1
operator|.
name|getToArtist
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1
operator|.
name|getPostLoaded
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener
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
name|testPreUpdate
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPreUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"YY"
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
name|isPreUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"preUpdateCallback"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ZZ"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isPreUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetCallbackFlags
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPreUpdated
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
name|PRE_UPDATE
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"AA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isPreUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
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
name|testPostUpdate
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"YY"
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
name|isPostUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_UPDATE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postUpdateCallback"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ZZ"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isPostUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetCallbackFlags
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostUpdated
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
name|POST_UPDATE
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"AA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isPostUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
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
name|testPostRemove
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_REMOVE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postRemoveCallback"
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
name|POST_REMOVE
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
name|context
operator|.
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isPostRemoved
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
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
name|testPostRemove_UpdatedDeleted
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|MockCallingBackListener
name|listener1
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
name|POST_REMOVE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener1
argument_list|,
literal|"publicCallback"
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
name|POST_UPDATE
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
comment|// change before removing
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|listener2
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|listener1
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
name|testPostRemove_InsertedUpdatedDeleted
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|MockCallingBackListener
name|listener0
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
name|POST_PERSIST
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener0
argument_list|,
literal|"publicCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener1
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
name|POST_REMOVE
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|listener1
argument_list|,
literal|"publicCallback"
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
name|POST_UPDATE
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
name|deleteObjects
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|listener0
operator|.
name|getPublicCalledbackEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|listener1
operator|.
name|getPublicCalledbackEntity
argument_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostPersist
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|resolver
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
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
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostPersisted
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addCallback
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|"postPersistCallback"
argument_list|)
expr_stmt|;
name|MockCallingBackListener
name|listener2
init|=
operator|new
name|MockCallingBackListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|publicCallback
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|super
operator|.
name|publicCallback
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|Persistent
operator|)
name|entity
operator|)
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_PERSIST
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
name|assertFalse
argument_list|(
name|a1
operator|.
name|isPostPersisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|isPostPersisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a2
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

