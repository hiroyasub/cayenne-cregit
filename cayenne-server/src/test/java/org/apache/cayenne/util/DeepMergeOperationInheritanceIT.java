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
name|util
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
name|PersistenceState
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
name|testdo
operator|.
name|inherit
operator|.
name|Department
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
name|inherit
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
name|inherit
operator|.
name|Manager
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|PEOPLE_PROJECT
argument_list|)
specifier|public
class|class
name|DeepMergeOperationInheritanceIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context1
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testDeepMergeExistingSubclass
parameter_list|()
block|{
specifier|final
name|Department
name|d1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|d1
operator|.
name|setName
argument_list|(
literal|"D1"
argument_list|)
expr_stmt|;
comment|// need to do double commit as Ashwood sorter blows on Employees/Departments
comment|// ordering...
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Employee
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Employee
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
literal|"E1"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setPersonType
argument_list|(
literal|"EE"
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|Manager
name|e2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setName
argument_list|(
literal|"E2"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|setPersonType
argument_list|(
literal|"EM"
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// need to make sure source relationship is resolved as a result of some Ashwood
comment|// strangeness...
name|d1
operator|.
name|getEmployees
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
comment|// resolve Employees
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Employee
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|DeepMergeOperation
name|op
init|=
operator|new
name|DeepMergeOperation
argument_list|(
name|context1
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
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Department
name|d2
init|=
operator|(
name|Department
operator|)
name|op
operator|.
name|merge
argument_list|(
name|d1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|d2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Employee
name|ex
range|:
name|d2
operator|.
name|getEmployees
argument_list|()
control|)
block|{
if|if
condition|(
literal|"E2"
operator|.
name|equals
argument_list|(
name|ex
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|instanceof
name|Manager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|ex
operator|instanceof
name|Manager
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeepMergeNonExistentSubclass
parameter_list|()
block|{
specifier|final
name|Department
name|d1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|d1
operator|.
name|setName
argument_list|(
literal|"D1"
argument_list|)
expr_stmt|;
comment|// need to do double commit as Ashwood sorter blows on Employees/Departments
comment|// ordering...
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Employee
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Employee
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
literal|"E1"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setPersonType
argument_list|(
literal|"EE"
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|Manager
name|e2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setName
argument_list|(
literal|"E2"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|setPersonType
argument_list|(
literal|"EM"
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// need to make sure source relationship is resolved as a result of some Ashwood
comment|// strangeness...
name|d1
operator|.
name|getEmployees
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
specifier|final
name|DeepMergeOperation
name|op
init|=
operator|new
name|DeepMergeOperation
argument_list|(
name|context1
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
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Department
name|d2
init|=
operator|(
name|Department
operator|)
name|op
operator|.
name|merge
argument_list|(
name|d1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|d2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Employee
name|ex
range|:
name|d2
operator|.
name|getEmployees
argument_list|()
control|)
block|{
if|if
condition|(
literal|"E2"
operator|.
name|equals
argument_list|(
name|ex
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|instanceof
name|Manager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|ex
operator|instanceof
name|Manager
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

