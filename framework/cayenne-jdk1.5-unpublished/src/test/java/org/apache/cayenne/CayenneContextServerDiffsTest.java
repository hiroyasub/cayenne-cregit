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
name|ClientChannelServerDiffsListener1
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
name|testdo
operator|.
name|mt
operator|.
name|MtTable1
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
name|CayenneContextServerDiffsTest
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
name|testReturnDiffInPrePersist
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
try|try
block|{
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|ClientChannelServerDiffsListener1
argument_list|()
argument_list|,
literal|"prePersist"
argument_list|)
expr_stmt|;
name|ClientServerChannel
name|csChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
operator|new
name|LocalConnection
argument_list|(
name|csChannel
argument_list|)
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
name|o
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
name|o
operator|.
name|setServerAttribute1
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
name|o
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReturnDiffInPreUpdate
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
try|try
block|{
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_UPDATE
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|ClientChannelServerDiffsListener1
argument_list|()
argument_list|,
literal|"preUpdate"
argument_list|)
expr_stmt|;
name|ClientServerChannel
name|csChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
operator|new
name|LocalConnection
argument_list|(
name|csChannel
argument_list|)
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
name|o
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
name|o
operator|.
name|setServerAttribute1
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|o
operator|.
name|setServerAttribute1
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
name|o
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"111"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReturnDiffClientArcChanges
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
try|try
block|{
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|ClientChannelServerDiffsListener1
argument_list|()
argument_list|,
literal|"prePersist"
argument_list|)
expr_stmt|;
name|ClientServerChannel
name|csChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
operator|new
name|LocalConnection
argument_list|(
name|csChannel
argument_list|)
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
name|o
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
name|o1
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
name|o
operator|.
name|addToTable2Array
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|o
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|o
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReturnDiffServerArcChanges
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
try|try
block|{
name|registry
operator|.
name|addListener
argument_list|(
name|LifecycleEvent
operator|.
name|PRE_PERSIST
argument_list|,
name|MtTable1
operator|.
name|class
argument_list|,
operator|new
name|ClientChannelServerDiffsListener1
argument_list|()
argument_list|,
literal|"prePersistAddRelationship"
argument_list|)
expr_stmt|;
name|ClientServerChannel
name|csChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|ClientChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
operator|new
name|LocalConnection
argument_list|(
name|csChannel
argument_list|)
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
name|o
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
name|o1
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
name|o
operator|.
name|addToTable2Array
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|o
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o
operator|.
name|getTable2Array
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

