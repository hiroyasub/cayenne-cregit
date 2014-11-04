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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|inheritance_flat
operator|.
name|Group
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
name|inheritance_flat
operator|.
name|Role
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
name|inheritance_flat
operator|.
name|User
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

begin_comment
comment|/**  * Special test cases per CAY-1378, CAY-1379.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|INHERTITANCE_SINGLE_TABLE1_PROJECT
argument_list|)
specifier|public
class|class
name|SingleTableInheritance1IT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GROUP_MEMBERS"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"USER_PROPERTIES"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GROUP_PROPERTIES"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ROLES"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupActions
parameter_list|()
throws|throws
name|Exception
block|{
name|User
name|user
init|=
name|context
operator|.
name|newObject
argument_list|(
name|User
operator|.
name|class
argument_list|)
decl_stmt|;
name|user
operator|.
name|setName
argument_list|(
literal|"test_user"
argument_list|)
expr_stmt|;
name|Group
name|group1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Group
operator|.
name|class
argument_list|)
decl_stmt|;
name|group1
operator|.
name|setName
argument_list|(
literal|"test_group1"
argument_list|)
expr_stmt|;
name|Group
name|group2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Group
operator|.
name|class
argument_list|)
decl_stmt|;
name|group2
operator|.
name|setName
argument_list|(
literal|"test_group2"
argument_list|)
expr_stmt|;
name|group1
operator|.
name|addToGroupMembers
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|group2
operator|.
name|addToGroupMembers
argument_list|(
name|group1
argument_list|)
expr_stmt|;
name|group2
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Per CAY-1379 removing user and then refetching resulted in a FFE downstream
name|group1
operator|.
name|removeFromGroupMembers
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Role
operator|.
name|ROLE_GROUPS_PROPERTY
argument_list|,
name|group2
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Group
operator|.
name|class
argument_list|,
name|exp
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
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
name|group1
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|group2
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFlattenedNullifyNullifyDeleteRules
parameter_list|()
throws|throws
name|Exception
block|{
name|User
name|user
init|=
name|context
operator|.
name|newObject
argument_list|(
name|User
operator|.
name|class
argument_list|)
decl_stmt|;
name|user
operator|.
name|setName
argument_list|(
literal|"test_user"
argument_list|)
expr_stmt|;
name|Group
name|group
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Group
operator|.
name|class
argument_list|)
decl_stmt|;
name|group
operator|.
name|setName
argument_list|(
literal|"test_group"
argument_list|)
expr_stmt|;
name|group
operator|.
name|addToGroupMembers
argument_list|(
name|user
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
name|user
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|group
operator|.
name|getGroupMembers
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// here Cayenne would throw per CAY-1378 on an attempt to delete a previously
comment|// related transient object
name|context
operator|.
name|deleteObjects
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

