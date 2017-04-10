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
name|ashwood
package|;
end_package

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
name|List
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
name|map
operator|.
name|DbEntity
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|WEIGHTED_SORT_PROJECT
argument_list|)
specifier|public
class|class
name|WeightedAshwoodEntitySorterIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|resolver
operator|=
name|context
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSortDbEntities
parameter_list|()
block|{
comment|// since it is impossible to ensure non-coincidental sort order of unrelated
comment|// DbEntities (without overriding DbEntity.hashCode()), we'll test on 2 entities
comment|// with a relationship, and reverse the topological order with SortWeight annotation.
name|List
argument_list|<
name|DbEntity
argument_list|>
name|eSorted
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_DEP"
argument_list|)
argument_list|,
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_ROOT"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|e1
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_ROOT"
argument_list|)
argument_list|,
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_DEP"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|e2
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_DEP"
argument_list|)
argument_list|,
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"SORT_ROOT"
argument_list|)
argument_list|)
decl_stmt|;
name|WeightedAshwoodEntitySorter
name|sorter
init|=
operator|new
name|WeightedAshwoodEntitySorter
argument_list|()
decl_stmt|;
name|sorter
operator|.
name|setEntityResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|sorter
operator|.
name|sortDbEntities
argument_list|(
name|e1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|eSorted
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|sorter
operator|.
name|sortDbEntities
argument_list|(
name|e2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|eSorted
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

