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
name|CayenneContextDeletionIT
extends|extends
name|RemoteCayenneCase
block|{
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
name|CayenneContextDeletionIT
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
name|testDeletion
parameter_list|()
block|{
name|ClientMtTable1
name|object
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|clientContext
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// now check that the object is unregistered
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

