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
name|CayenneContext
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
name|ObjectId
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
name|ClientChannelServerDiffsTest
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
name|testReturnIdDiff
parameter_list|()
block|{
specifier|final
name|Object
index|[]
name|ids
init|=
operator|new
name|Object
index|[
literal|2
index|]
decl_stmt|;
specifier|final
name|GraphChangeHandler
name|diffReader
init|=
operator|new
name|NoopGraphChangeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|oldId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
name|ids
index|[
literal|0
index|]
operator|=
name|oldId
expr_stmt|;
name|ids
index|[
literal|1
index|]
operator|=
name|newId
expr_stmt|;
block|}
block|}
decl_stmt|;
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
block|{
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
name|GraphDiff
name|serverDiff
init|=
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
decl_stmt|;
name|assertNotNull
argument_list|(
name|serverDiff
argument_list|)
expr_stmt|;
name|serverDiff
operator|.
name|apply
argument_list|(
name|diffReader
argument_list|)
expr_stmt|;
return|return
name|serverDiff
return|;
block|}
block|}
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
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
index|[
literal|0
index|]
operator|instanceof
name|ObjectId
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|ids
index|[
literal|0
index|]
operator|)
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
index|[
literal|1
index|]
operator|instanceof
name|ObjectId
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|ids
index|[
literal|1
index|]
operator|)
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|NoopGraphChangeHandler
implements|implements
name|GraphChangeHandler
block|{
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
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
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
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

