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
name|remote
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
name|LifecycleListener
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtLifecycles
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
name|client
operator|.
name|ClientCase
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|RemoteCallbacksIT
extends|extends
name|RemoteCayenneCase
implements|implements
name|LifecycleListener
block|{
specifier|private
name|int
name|added
decl_stmt|,
name|loaded
decl_stmt|,
name|prePersisted
decl_stmt|,
name|postPersisted
decl_stmt|,
name|preRemoved
decl_stmt|,
name|postRemoved
decl_stmt|,
name|preUpdated
decl_stmt|,
name|postUpdated
decl_stmt|;
annotation|@
name|Parameters
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
index|[]
block|{
block|{
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
block|}
block|,
block|{
name|LocalConnection
operator|.
name|JAVA_SERIALIZATION
block|}
block|,
block|{
name|LocalConnection
operator|.
name|NO_SERIALIZATION
block|}
block|,         }
argument_list|)
return|;
block|}
specifier|public
name|RemoteCallbacksIT
parameter_list|(
name|int
name|serializationPolicy
parameter_list|)
block|{
name|super
operator|.
name|serializationPolicy
operator|=
name|serializationPolicy
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUpAfterInjection
argument_list|()
expr_stmt|;
name|added
operator|=
literal|0
expr_stmt|;
name|loaded
operator|=
literal|0
expr_stmt|;
name|prePersisted
operator|=
literal|0
expr_stmt|;
name|postPersisted
operator|=
literal|0
expr_stmt|;
name|preRemoved
operator|=
literal|0
expr_stmt|;
name|postRemoved
operator|=
literal|0
expr_stmt|;
name|preUpdated
operator|=
literal|0
expr_stmt|;
name|postUpdated
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|ObjectContext
name|context
init|=
name|createROPContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|addListener
argument_list|(
name|ClientMtLifecycles
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|assertAll
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ClientMtLifecycles
name|l1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtLifecycles
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setName
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
argument_list|)
expr_stmt|;
comment|//until commit
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setName
argument_list|(
literal|"x2"
argument_list|)
expr_stmt|;
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
argument_list|)
expr_stmt|;
comment|//until commit
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|l1
argument_list|)
expr_stmt|;
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
argument_list|)
expr_stmt|;
comment|//until commit
name|assertAll
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAll
parameter_list|(
name|int
name|added
parameter_list|,
name|int
name|loaded
parameter_list|,
name|int
name|prePersisted
parameter_list|,
name|int
name|postPersisted
parameter_list|,
name|int
name|preUpdated
parameter_list|,
name|int
name|postUpdated
parameter_list|,
name|int
name|preRemoved
parameter_list|,
name|int
name|postRemoved
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|this
operator|.
name|added
argument_list|,
name|added
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|loaded
argument_list|,
name|loaded
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|prePersisted
argument_list|,
name|prePersisted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|postPersisted
argument_list|,
name|postPersisted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|preRemoved
argument_list|,
name|preRemoved
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|postRemoved
argument_list|,
name|postRemoved
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|preUpdated
argument_list|,
name|preUpdated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|postUpdated
argument_list|,
name|postUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|postAdd
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|added
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|postLoad
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|loaded
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|postPersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|postPersisted
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|postRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|postRemoved
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|postUpdated
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|prePersist
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|prePersisted
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|preRemove
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|preRemoved
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|preUpdate
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|preUpdated
operator|++
expr_stmt|;
block|}
block|}
end_class

end_unit

