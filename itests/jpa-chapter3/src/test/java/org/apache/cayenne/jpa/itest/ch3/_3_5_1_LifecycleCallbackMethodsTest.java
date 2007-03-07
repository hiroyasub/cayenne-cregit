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
name|jpa
operator|.
name|itest
operator|.
name|ch3
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
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
name|itest
operator|.
name|jpa
operator|.
name|EntityManagerCase
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
name|itest
operator|.
name|jpa
operator|.
name|ItestSetup
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|CallbackEntity
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|CallbackEntity2
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|EntityListener1
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|EntityListener2
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|EntityListenerState
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|ListenerEntity1
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
name|jpa
operator|.
name|itest
operator|.
name|ch3
operator|.
name|entity
operator|.
name|ListenerEntity2
import|;
end_import

begin_class
specifier|public
class|class
name|_3_5_1_LifecycleCallbackMethodsTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testPrePersist
parameter_list|()
block|{
comment|// regular entity
name|CallbackEntity
name|e
init|=
operator|new
name|CallbackEntity
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|isPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|EntityManager
name|em
init|=
name|getEntityManager
argument_list|()
decl_stmt|;
comment|// spec reqires the callback to be invoked as a part of persist, without waiting
comment|// for flush or commit.
name|em
operator|.
name|persist
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|isPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// entity with same callback method handling multiple callbacks
name|CallbackEntity2
name|e2
init|=
operator|new
name|CallbackEntity2
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|e2
operator|.
name|isMixedCallbackCalled
argument_list|()
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e2
operator|.
name|isMixedCallbackCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// external listeners
name|EntityListenerState
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityListenerState
operator|.
name|getPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|ListenerEntity1
name|e3
init|=
operator|new
name|ListenerEntity1
argument_list|()
decl_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|e3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
operator|+
name|EntityListener1
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|EntityListener2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|EntityListenerState
operator|.
name|getPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|EntityListenerState
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityListenerState
operator|.
name|getPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|ListenerEntity2
name|e4
init|=
operator|new
name|ListenerEntity2
argument_list|()
decl_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|e4
argument_list|)
expr_stmt|;
comment|// here annotations must be called in a different order from e3.
name|assertEquals
argument_list|(
literal|":"
operator|+
name|EntityListener2
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|EntityListener1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|EntityListenerState
operator|.
name|getPrePersistCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPostPersist
parameter_list|()
block|{
comment|// regular entity
name|CallbackEntity
name|e
init|=
operator|new
name|CallbackEntity
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|isPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// don't use super getEntityManager - it starts a tran
name|EntityManager
name|em
init|=
name|ItestSetup
operator|.
name|getInstance
argument_list|()
operator|.
name|createEntityManager
argument_list|()
decl_stmt|;
comment|// spec reqires the callback to be invoked as a part of persist, without waiting
comment|// for flush or commit.
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|isPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e
operator|.
name|getPostPersistedId
argument_list|()
argument_list|)
expr_stmt|;
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|isPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// Per spec, id must be availble during PostPersist
name|assertEquals
argument_list|(
name|e
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getPostPersistedId
argument_list|()
argument_list|)
expr_stmt|;
comment|// external listeners
name|EntityListenerState
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityListenerState
operator|.
name|getPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|ListenerEntity1
name|e3
init|=
operator|new
name|ListenerEntity1
argument_list|()
decl_stmt|;
comment|// reset EM
name|em
operator|=
name|ItestSetup
operator|.
name|getInstance
argument_list|()
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityListenerState
operator|.
name|getPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|e3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
operator|+
name|EntityListener2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|EntityListenerState
operator|.
name|getPostPersistCalled
argument_list|()
argument_list|,
name|EntityListenerState
operator|.
name|getPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
name|EntityListenerState
operator|.
name|reset
argument_list|()
expr_stmt|;
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|":"
operator|+
name|EntityListener1
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|EntityListener2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|EntityListenerState
operator|.
name|getPostPersistCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

