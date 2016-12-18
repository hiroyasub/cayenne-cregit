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
name|query
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
name|map
operator|.
name|EntityResolver
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
name|map
operator|.
name|ObjEntity
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
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|SelectQueryPrefetchRouterActionQualifiedEntityIT
extends|extends
name|PeopleProjectCase
block|{
annotation|@
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchEmployee
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|departmentEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Employee
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"abc"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Employee
operator|.
name|TO_DEPARTMENT
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQueryPrefetchRouterAction
name|action
init|=
operator|new
name|SelectQueryPrefetchRouterAction
argument_list|()
decl_stmt|;
name|MockQueryRouter
name|router
init|=
operator|new
name|MockQueryRouter
argument_list|()
decl_stmt|;
name|action
operator|.
name|route
argument_list|(
name|q
argument_list|,
name|router
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|router
operator|.
name|getQueryCount
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchSelectQuery
name|prefetch
init|=
operator|(
name|PrefetchSelectQuery
operator|)
name|router
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|departmentEntity
argument_list|,
name|prefetch
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"db:employees.NAME = 'abc' and (db:employees.PERSON_TYPE = 'EE' "
operator|+
literal|"or db:employees.PERSON_TYPE = 'EM')"
argument_list|)
argument_list|,
name|prefetch
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchManager
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|departmentEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|Department
operator|.
name|class
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Manager
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"abc"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Employee
operator|.
name|TO_DEPARTMENT
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQueryPrefetchRouterAction
name|action
init|=
operator|new
name|SelectQueryPrefetchRouterAction
argument_list|()
decl_stmt|;
name|MockQueryRouter
name|router
init|=
operator|new
name|MockQueryRouter
argument_list|()
decl_stmt|;
name|action
operator|.
name|route
argument_list|(
name|q
argument_list|,
name|router
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|router
operator|.
name|getQueryCount
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchSelectQuery
name|prefetch
init|=
operator|(
name|PrefetchSelectQuery
operator|)
name|router
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|departmentEntity
argument_list|,
name|prefetch
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:employees.NAME = 'abc' and db:employees.PERSON_TYPE = 'EM'"
argument_list|)
argument_list|,
name|prefetch
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

