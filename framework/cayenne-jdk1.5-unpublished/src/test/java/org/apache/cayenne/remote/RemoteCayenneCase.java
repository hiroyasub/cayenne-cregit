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
name|DataContext
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
name|UnitLocalConnection
import|;
end_import

begin_comment
comment|/**  * JUnit case to test ROP functionality  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|RemoteCayenneCase
extends|extends
name|CayenneCase
block|{
specifier|protected
name|CayenneContext
name|context
decl_stmt|;
specifier|protected
name|DataContext
name|parentDataContext
decl_stmt|;
comment|/**      * Used serialization policy. Per CAY-979 we're testing on all policies      */
specifier|protected
name|int
name|serializationPolicy
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|runBare
parameter_list|()
throws|throws
name|Throwable
block|{
name|serializationPolicy
operator|=
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
expr_stmt|;
name|super
operator|.
name|runBare
argument_list|()
expr_stmt|;
name|serializationPolicy
operator|=
name|LocalConnection
operator|.
name|JAVA_SERIALIZATION
expr_stmt|;
name|super
operator|.
name|runBare
argument_list|()
expr_stmt|;
name|serializationPolicy
operator|=
name|LocalConnection
operator|.
name|NO_SERIALIZATION
expr_stmt|;
name|super
operator|.
name|runBare
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|parentDataContext
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|context
operator|=
name|createROPContext
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|CayenneContext
name|createROPContext
parameter_list|()
block|{
name|ClientServerChannel
name|clientServerChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|parentDataContext
argument_list|)
decl_stmt|;
name|UnitLocalConnection
name|connection
init|=
operator|new
name|UnitLocalConnection
argument_list|(
name|clientServerChannel
argument_list|,
name|serializationPolicy
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
return|return
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
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
block|}
end_class

end_unit

