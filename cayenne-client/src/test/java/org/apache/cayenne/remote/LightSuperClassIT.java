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
name|persistent
operator|.
name|Continent
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
name|persistent
operator|.
name|Country
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
comment|/**  * Test for entities that are implemented in same class on client and server  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|PERSISTENT_PROJECT
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
name|LightSuperClassIT
extends|extends
name|RemoteCayenneCase
block|{
specifier|private
name|boolean
name|server
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
block|,
literal|true
block|}
block|,
block|{
name|LocalConnection
operator|.
name|JAVA_SERIALIZATION
block|,
literal|true
block|}
block|,
block|{
name|LocalConnection
operator|.
name|NO_SERIALIZATION
block|,
literal|true
block|}
block|,
block|{
name|LocalConnection
operator|.
name|NO_SERIALIZATION
block|,
literal|false
block|}
block|,         }
argument_list|)
return|;
block|}
specifier|public
name|LightSuperClassIT
parameter_list|(
name|int
name|serializationPolicy
parameter_list|,
name|boolean
name|server
parameter_list|)
block|{
name|super
operator|.
name|serializationPolicy
operator|=
name|serializationPolicy
expr_stmt|;
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
specifier|private
name|ObjectContext
name|createContext
parameter_list|()
block|{
if|if
condition|(
name|server
condition|)
block|{
return|return
name|serverContext
return|;
block|}
else|else
block|{
return|return
name|createROPContext
argument_list|()
return|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|createContext
argument_list|()
decl_stmt|;
name|Continent
name|continent
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Continent
operator|.
name|class
argument_list|)
decl_stmt|;
name|continent
operator|.
name|setName
argument_list|(
literal|"Europe"
argument_list|)
expr_stmt|;
name|Country
name|country
init|=
operator|new
name|Country
argument_list|()
decl_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|country
argument_list|)
expr_stmt|;
comment|// TODO: setting property before object creation does not work on ROP (CAY-1320)
name|country
operator|.
name|setName
argument_list|(
literal|"Russia"
argument_list|)
expr_stmt|;
name|country
operator|.
name|setContinent
argument_list|(
name|continent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|continent
operator|.
name|getCountries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|country
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|continent
operator|.
name|getCountries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|continent
operator|.
name|setName
argument_list|(
literal|"Australia"
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
name|assertEquals
argument_list|(
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Country
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Continent
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

