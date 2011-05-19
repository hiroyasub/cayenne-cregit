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
name|event
operator|.
name|DefaultEventManager
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
name|UnitLocalConnection
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

begin_class
specifier|public
specifier|abstract
class|class
name|RemoteCayenneCase
extends|extends
name|ClientCase
block|{
specifier|protected
name|CayenneContext
name|clientContext
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataContext
name|serverContext
decl_stmt|;
comment|/**      * Used serialization policy. Per CAY-979 we're testing on all policies      */
specifier|private
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
name|runBareSimple
argument_list|()
expr_stmt|;
name|serializationPolicy
operator|=
name|LocalConnection
operator|.
name|JAVA_SERIALIZATION
expr_stmt|;
name|runBareSimple
argument_list|()
expr_stmt|;
name|serializationPolicy
operator|=
name|LocalConnection
operator|.
name|NO_SERIALIZATION
expr_stmt|;
name|runBareSimple
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|runBareSimple
parameter_list|()
throws|throws
name|Throwable
block|{
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
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|clientContext
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
name|serverContext
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
argument_list|,
literal|false
argument_list|,
comment|// we want events, but we don't want thread leaks, so creating single threaded EM.
comment|// TODO: replace with container managed ClientCase.
operator|new
name|DefaultEventManager
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
operator|new
name|MapQueryCache
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

