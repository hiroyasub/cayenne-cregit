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
name|art
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataDomainCallbacksTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|getDomain
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
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
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
comment|// TODO: andrus, 9/21/2006 - this fails as "postLoad" is called when query
comment|// refresh flag is set to false and object is already there.
comment|// q.setRefreshingObjects(false);
comment|//
comment|// assertFalse(a1.isPostLoaded());
comment|// context.performQuery(q);
comment|// assertFalse(a1.isPostLoaded());
comment|// assertNull(listener.getPublicCalledbackEntity());
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
specifier|public
name|void
name|testPreUpdate
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
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
specifier|public
name|void
name|testPostUpdate
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
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
specifier|public
name|void
name|testPostRemove
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
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
name|deleteObject
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
specifier|public
name|void
name|testPostPersist
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
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

