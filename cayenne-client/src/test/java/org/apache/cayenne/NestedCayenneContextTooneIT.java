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
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|ArcId
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
name|GraphChangeHandler
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
name|GraphDiff
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
name|remote
operator|.
name|RemoteCayenneCase
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
name|toone
operator|.
name|ClientTooneDep
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
name|toone
operator|.
name|ClientTooneMaster
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
name|assertSame
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TOONE_PROJECT
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
name|NestedCayenneContextTooneIT
extends|extends
name|RemoteCayenneCase
block|{
annotation|@
name|Inject
specifier|private
name|ClientRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
annotation|@
name|Parameterized
operator|.
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
block|,}
argument_list|)
return|;
block|}
specifier|public
name|NestedCayenneContextTooneIT
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
comment|/*  * was added for CAY-1636  */
annotation|@
name|Test
specifier|public
name|void
name|testCAY1636
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientTooneMaster
name|A
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientTooneDep
name|B
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientTooneDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|A
operator|.
name|setToDependent
argument_list|(
name|B
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|child
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientTooneMaster
argument_list|>
name|objects
init|=
name|child
operator|.
name|select
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientTooneMaster
name|childDeleted
init|=
operator|(
name|ClientTooneMaster
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|child
operator|.
name|deleteObjects
argument_list|(
name|childDeleted
argument_list|)
expr_stmt|;
name|child
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|ClientTooneMaster
name|parentDeleted
init|=
operator|(
name|ClientTooneMaster
operator|)
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|childDeleted
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|parentDeleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|parentDeleted
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
name|query2
init|=
operator|new
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientTooneMaster
argument_list|>
name|objects2
init|=
name|child
operator|.
name|select
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCAY1636_2
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientTooneMaster
name|A
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ClientTooneDep
name|B
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientTooneDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|A
operator|.
name|setToDependent
argument_list|(
name|B
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|child
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|ClientTooneDep
argument_list|>
name|queryB
init|=
operator|new
name|SelectQuery
argument_list|<
name|ClientTooneDep
argument_list|>
argument_list|(
name|ClientTooneDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objectsB
init|=
name|child
operator|.
name|performQuery
argument_list|(
name|queryB
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objectsB
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientTooneDep
name|childBDeleted
init|=
operator|(
name|ClientTooneDep
operator|)
name|objectsB
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|child
operator|.
name|deleteObjects
argument_list|(
name|childBDeleted
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientTooneMaster
argument_list|>
name|objects
init|=
name|child
operator|.
name|select
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientTooneMaster
name|childDeleted
init|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|child
operator|.
name|deleteObjects
argument_list|(
name|childDeleted
argument_list|)
expr_stmt|;
name|child
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|ClientTooneMaster
name|parentDeleted
init|=
operator|(
name|ClientTooneMaster
operator|)
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|childDeleted
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|parentDeleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|parentDeleted
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
name|query2
init|=
operator|new
name|SelectQuery
argument_list|<
name|ClientTooneMaster
argument_list|>
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientTooneMaster
argument_list|>
name|objects2
init|=
name|child
operator|.
name|select
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitChangesToParentOneToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|child
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|ClientTooneMaster
name|master
init|=
name|child
operator|.
name|newObject
argument_list|(
name|ClientTooneMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientTooneDep
name|dep
init|=
name|child
operator|.
name|newObject
argument_list|(
name|ClientTooneDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|master
operator|.
name|setToDependent
argument_list|(
name|dep
argument_list|)
expr_stmt|;
name|child
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|ClientTooneMaster
name|masterParent
init|=
operator|(
name|ClientTooneMaster
operator|)
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|master
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|ClientTooneDep
name|depParent
init|=
operator|(
name|ClientTooneDep
operator|)
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|dep
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|masterParent
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|depParent
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|masterParent
argument_list|,
name|depParent
operator|.
name|getToMaster
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|depParent
argument_list|,
name|masterParent
operator|.
name|getToDependent
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that arc changes got recorded in the parent context
name|GraphDiff
name|diffs
init|=
name|clientContext
operator|.
name|internalGraphManager
argument_list|()
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
specifier|final
name|int
index|[]
name|arcDiffs
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|int
index|[]
name|newNodes
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|diffs
operator|.
name|apply
argument_list|(
operator|new
name|GraphChangeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
name|arcDiffs
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
block|{
name|arcDiffs
index|[
literal|0
index|]
operator|--
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|newNodes
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|newNodes
index|[
literal|0
index|]
operator|--
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|newNodes
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|arcDiffs
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

