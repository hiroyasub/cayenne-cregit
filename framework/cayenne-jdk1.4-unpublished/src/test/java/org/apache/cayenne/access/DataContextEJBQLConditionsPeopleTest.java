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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|query
operator|.
name|EJBQLQuery
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
name|Address
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
name|PeopleCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextEJBQLConditionsPeopleTest
extends|extends
name|PeopleCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Department
name|d1
init|=
operator|(
name|Department
operator|)
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
literal|"d1"
argument_list|)
expr_stmt|;
name|Department
name|d2
init|=
operator|(
name|Department
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|d2
operator|.
name|setName
argument_list|(
literal|"d2"
argument_list|)
expr_stmt|;
name|Department
name|d3
init|=
operator|(
name|Department
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|d3
operator|.
name|setName
argument_list|(
literal|"d3"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Manager
name|m1
init|=
operator|(
name|Manager
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|m1
operator|.
name|setName
argument_list|(
literal|"m1"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|setPersonType
argument_list|(
literal|"EM"
argument_list|)
expr_stmt|;
name|Manager
name|m2
init|=
operator|(
name|Manager
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|m2
operator|.
name|setName
argument_list|(
literal|"m2"
argument_list|)
expr_stmt|;
name|m2
operator|.
name|setPersonType
argument_list|(
literal|"EM"
argument_list|)
expr_stmt|;
name|Manager
name|m3
init|=
operator|(
name|Manager
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|m3
operator|.
name|setName
argument_list|(
literal|"m3"
argument_list|)
expr_stmt|;
name|m3
operator|.
name|setPersonType
argument_list|(
literal|"EM"
argument_list|)
expr_stmt|;
name|Address
name|a1
init|=
operator|(
name|Address
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addToAddresses
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|Address
name|a2
init|=
operator|(
name|Address
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
name|m2
operator|.
name|addToAddresses
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|Address
name|a3
init|=
operator|(
name|Address
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
name|m3
operator|.
name|addToAddresses
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addToEmployees
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|d3
operator|.
name|addToEmployees
argument_list|(
name|m3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|d1
operator|.
name|setToManager
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|d2
operator|.
name|setToManager
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|d3
operator|.
name|setToManager
argument_list|(
name|m3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionMemberOfId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ejbql
init|=
literal|"SELECT DISTINCT m FROM Manager m JOIN m.managedDepartments d"
operator|+
literal|" WHERE m MEMBER d.employees"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
name|ids
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Manager
name|m
init|=
operator|(
name|Manager
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|"m1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|"m3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionNotMemberOfId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ejbql
init|=
literal|"SELECT DISTINCT m FROM Manager m JOIN m.managedDepartments d"
operator|+
literal|" WHERE m NOT MEMBER d.employees"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
name|ids
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Manager
name|m
init|=
operator|(
name|Manager
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|"m2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionNotMemberOfToOne
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need a better test ... this query returns zero rows by definition
name|String
name|ejbql
init|=
literal|"SELECT a"
operator|+
literal|" FROM Address a JOIN a.toEmployee m JOIN m.toDepartment d"
operator|+
literal|" WHERE m NOT MEMBER d.employees"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

