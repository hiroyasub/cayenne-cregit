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
name|lifecycle
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|cache
operator|.
name|MapQueryCache
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
name|cache
operator|.
name|QueryCache
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
name|Binder
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
name|Module
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
name|lifecycle
operator|.
name|db
operator|.
name|E1
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
name|lifecycle
operator|.
name|db
operator|.
name|E2
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
name|lifecycle
operator|.
name|unit
operator|.
name|CacheInvalidationCase
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
name|ObjectSelect
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CacheInvalidationCacheGroupsHandlerIT
extends|extends
name|CacheInvalidationCase
block|{
specifier|private
name|AtomicInteger
name|removeGroupUntypedCounter
decl_stmt|;
specifier|private
name|AtomicInteger
name|removeGroupTypedCounter
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|resetCounters
parameter_list|()
block|{
name|removeGroupUntypedCounter
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|removeGroupTypedCounter
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Module
name|buildInvalidationModule
parameter_list|()
block|{
return|return
name|CacheInvalidationModuleBuilder
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Module
name|buildCustomModule
parameter_list|()
block|{
comment|// Proxy query cache that will count methods calls
specifier|final
name|QueryCache
name|cache
init|=
operator|new
name|MapQueryCache
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
name|removeGroupUntypedCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|super
operator|.
name|removeGroup
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeGroup
parameter_list|(
name|String
name|groupKey
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
parameter_list|)
block|{
name|removeGroupTypedCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|super
operator|.
name|removeGroup
argument_list|(
name|groupKey
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|QueryCache
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|invalidateE1
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E1
argument_list|>
name|g0
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E1
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E1
argument_list|>
name|g1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E1
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E1
argument_list|>
name|g2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E1
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// inserted via SQL... query results are still cached...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|E1
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|removeGroupUntypedCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|removeGroupTypedCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// inserted via Cayenne... "g1" and "g2" should get auto refreshed...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|invalidateE2
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E2
argument_list|>
name|g0
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E2
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|()
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E2
argument_list|>
name|g1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E2
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E2
argument_list|>
name|g2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E2
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g2"
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E2
argument_list|>
name|g3
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E2
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g3"
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|E2
argument_list|>
name|g5
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|E2
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g5"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g3
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g5
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|e2
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// inserted via SQL... query results are still cached...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g3
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g5
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|E2
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Typed remove will actually call untyped version, thus 4 + 2
name|assertEquals
argument_list|(
literal|4
operator|+
literal|2
argument_list|,
name|removeGroupUntypedCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|removeGroupTypedCounter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// inserted via Cayenne... "g1" and "g2" should get auto refreshed...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|g0
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g1
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g2
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g3
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|g5
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

