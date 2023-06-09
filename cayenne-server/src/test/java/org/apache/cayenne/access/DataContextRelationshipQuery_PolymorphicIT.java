begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|Cayenne
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
name|jdbc
operator|.
name|TableHelper
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
name|inheritance_people
operator|.
name|Employee
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
name|inheritance_people
operator|.
name|PersonNotes
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
name|UnitTestClosure
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
name|PeopleProjectCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextRelationshipQuery_PolymorphicIT
extends|extends
name|PeopleProjectCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context1
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context2
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
specifier|private
name|TableHelper
name|tPerson
decl_stmt|;
specifier|private
name|TableHelper
name|tPersonNotes
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|tPerson
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PERSON"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"PERSON_ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"PERSON_TYPE"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|)
expr_stmt|;
name|tPersonNotes
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PERSON_NOTES"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"PERSON_ID"
argument_list|,
literal|"NOTES"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPolymorphicSharedCache
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// see CAY-2101... we are trying to get a snapshot from a new object in the shared cache, and then read this
comment|// object via a relationship, so that shared cache is consulted
name|Employee
name|e
init|=
name|context1
operator|.
name|newObject
argument_list|(
name|Employee
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|setName
argument_list|(
literal|"E1"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setSalary
argument_list|(
literal|1234.01f
argument_list|)
expr_stmt|;
name|PersonNotes
name|n
init|=
name|context1
operator|.
name|newObject
argument_list|(
name|PersonNotes
operator|.
name|class
argument_list|)
decl_stmt|;
name|n
operator|.
name|setNotes
argument_list|(
literal|"N1"
argument_list|)
expr_stmt|;
name|n
operator|.
name|setPerson
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|context1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// use different context to ensure we hit shared cache for relationship resolving
specifier|final
name|PersonNotes
name|nPeer
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context2
argument_list|,
name|PersonNotes
operator|.
name|class
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|n
argument_list|)
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|nPeer
operator|.
name|getPerson
argument_list|()
operator|instanceof
name|Employee
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

