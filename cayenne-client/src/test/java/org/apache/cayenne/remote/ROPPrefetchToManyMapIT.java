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
name|remote
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Cayenne
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
name|query
operator|.
name|ObjectSelect
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
name|PrefetchTreeNode
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
name|map_to_many
operator|.
name|ClientIdMapToMany
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
name|map_to_many
operator|.
name|ClientIdMapToManyTarget
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
name|DataChannelInterceptor
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MAP_TO_MANY_PROJECT
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
name|ROPPrefetchToManyMapIT
extends|extends
name|RemoteCayenneCase
block|{
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|queryInterceptor
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
name|ROPPrefetchToManyMapIT
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
name|Test
specifier|public
name|void
name|test
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createROPContext
argument_list|()
decl_stmt|;
name|ClientIdMapToMany
name|map
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientIdMapToMany
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientIdMapToManyTarget
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientIdMapToManyTarget
operator|.
name|class
argument_list|)
decl_stmt|;
name|target
operator|.
name|setMapToMany
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|ClientIdMapToMany
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|ClientIdMapToMany
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
literal|"targets"
argument_list|,
name|PrefetchTreeNode
operator|.
name|UNDEFINED_SEMANTICS
argument_list|)
decl_stmt|;
specifier|final
name|ClientIdMapToMany
name|mapToMany
init|=
operator|(
name|ClientIdMapToMany
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
parameter_list|()
lambda|->
name|assertEquals
argument_list|(
name|mapToMany
operator|.
name|getTargets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

