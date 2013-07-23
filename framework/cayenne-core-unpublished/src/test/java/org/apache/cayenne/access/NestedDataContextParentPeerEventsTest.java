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
name|test
operator|.
name|parallel
operator|.
name|ParallelTestContainer
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
name|relationship
operator|.
name|Child
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
name|relationship
operator|.
name|Master
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|NestedDataContextParentPeerEventsTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|parentContext1
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|parentContext2
decl_stmt|;
specifier|public
name|void
name|testPeerObjectUpdatedSimpleProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|Master
name|a
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Master
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|parentContext1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Master
name|a1
init|=
name|parentContext2
operator|.
name|localObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
specifier|final
name|ObjectContext
name|child
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|parentContext1
argument_list|)
decl_stmt|;
specifier|final
name|Master
name|a2
init|=
name|child
operator|.
name|localObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|parentContext2
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
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
literal|"Y"
argument_list|,
name|a2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|child
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPeerObjectUpdatedToOneRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|Master
name|a
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Master
operator|.
name|class
argument_list|)
decl_stmt|;
name|Master
name|altA
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Master
operator|.
name|class
argument_list|)
decl_stmt|;
name|Child
name|p
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Child
operator|.
name|class
argument_list|)
decl_stmt|;
name|p
operator|.
name|setMaster
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|altA
operator|.
name|setName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|parentContext1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Child
name|p1
init|=
name|parentContext2
operator|.
name|localObject
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|Master
name|altA1
init|=
name|parentContext2
operator|.
name|localObject
argument_list|(
name|altA
argument_list|)
decl_stmt|;
specifier|final
name|ObjectContext
name|childContext1
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|parentContext1
argument_list|)
decl_stmt|;
specifier|final
name|Child
name|p2
init|=
name|childContext1
operator|.
name|localObject
argument_list|(
name|p
argument_list|)
decl_stmt|;
specifier|final
name|Master
name|altA2
init|=
name|childContext1
operator|.
name|localObject
argument_list|(
name|altA
argument_list|)
decl_stmt|;
name|Master
name|a2
init|=
name|childContext1
operator|.
name|localObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setMaster
argument_list|(
name|altA1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a2
argument_list|,
name|p2
operator|.
name|getMaster
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|altA2
argument_list|,
name|p2
operator|.
name|getMaster
argument_list|()
argument_list|)
expr_stmt|;
name|parentContext2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
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
name|assertSame
argument_list|(
name|altA2
argument_list|,
name|p2
operator|.
name|getMaster
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|childContext1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPeerObjectUpdatedToManyRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|Master
name|a
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Master
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|Child
name|px
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Child
operator|.
name|class
argument_list|)
decl_stmt|;
name|px
operator|.
name|setMaster
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|Child
name|py
init|=
name|parentContext1
operator|.
name|newObject
argument_list|(
name|Child
operator|.
name|class
argument_list|)
decl_stmt|;
name|parentContext1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Child
name|py1
init|=
name|parentContext2
operator|.
name|localObject
argument_list|(
name|py
argument_list|)
decl_stmt|;
name|Master
name|a1
init|=
name|parentContext2
operator|.
name|localObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
specifier|final
name|ObjectContext
name|peer2
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|parentContext1
argument_list|)
decl_stmt|;
specifier|final
name|Child
name|py2
init|=
name|peer2
operator|.
name|localObject
argument_list|(
name|py
argument_list|)
decl_stmt|;
specifier|final
name|Master
name|a2
init|=
name|peer2
operator|.
name|localObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToChildren
argument_list|(
name|py1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a2
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|getChildren
argument_list|()
operator|.
name|contains
argument_list|(
name|py2
argument_list|)
argument_list|)
expr_stmt|;
name|parentContext2
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
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
literal|2
argument_list|,
name|a2
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|getChildren
argument_list|()
operator|.
name|contains
argument_list|(
name|py2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|peer2
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
