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
name|assertNull
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|PersistentObjectIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testObjectContext
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPersistenceState
parameter_list|()
block|{
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectID
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|id
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

