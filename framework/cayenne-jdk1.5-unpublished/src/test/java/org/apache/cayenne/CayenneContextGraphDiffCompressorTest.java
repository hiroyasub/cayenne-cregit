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
name|access
operator|.
name|ClientServerChannel
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
name|access
operator|.
name|DataDomain
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
name|remote
operator|.
name|ClientChannel
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
name|ClientMtTable1
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
name|ClientMtTable2
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
name|AccessStack
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
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneContextGraphDiffCompressorTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|public
name|void
name|testMultipleSimpleProperties
parameter_list|()
block|{
name|DiffCounter
name|serverChannel
init|=
operator|new
name|DiffCounter
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|LocalConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
name|serverChannel
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"v1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"v2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serverChannel
operator|.
name|nodePropertiesChanged
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serverChannel
operator|.
name|nodesCreated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testComplimentaryArcs
parameter_list|()
block|{
name|DiffCounter
name|serverChannel
init|=
operator|new
name|DiffCounter
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|LocalConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
name|serverChannel
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|o2
operator|.
name|setTable1
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setTable1
argument_list|(
literal|null
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
name|serverChannel
operator|.
name|nodePropertiesChanged
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|serverChannel
operator|.
name|nodesCreated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|serverChannel
operator|.
name|arcsCreated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|serverChannel
operator|.
name|arcsDeleted
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDelete
parameter_list|()
block|{
name|DiffCounter
name|serverChannel
init|=
operator|new
name|DiffCounter
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|LocalConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
name|serverChannel
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"v1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|o1
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
name|serverChannel
operator|.
name|nodePropertiesChanged
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|serverChannel
operator|.
name|nodesCreated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|serverChannel
operator|.
name|nodesRemoved
argument_list|)
expr_stmt|;
block|}
specifier|final
class|class
name|DiffCounter
extends|extends
name|ClientServerChannel
implements|implements
name|GraphChangeHandler
block|{
name|int
name|arcsCreated
decl_stmt|;
name|int
name|arcsDeleted
decl_stmt|;
name|int
name|nodesCreated
decl_stmt|;
name|int
name|nodeIdsChanged
decl_stmt|;
name|int
name|nodePropertiesChanged
decl_stmt|;
name|int
name|nodesRemoved
decl_stmt|;
specifier|public
name|DiffCounter
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
name|super
argument_list|(
name|domain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
name|changes
operator|.
name|apply
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|onSync
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
return|;
block|}
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
name|Object
name|arcId
parameter_list|)
block|{
name|arcsCreated
operator|++
expr_stmt|;
block|}
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
name|Object
name|arcId
parameter_list|)
block|{
name|arcsDeleted
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|nodesCreated
operator|++
expr_stmt|;
block|}
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
name|nodeIdsChanged
operator|++
expr_stmt|;
block|}
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
name|nodePropertiesChanged
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|nodesRemoved
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

