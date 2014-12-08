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
name|lifecycle
operator|.
name|sort
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|junit
operator|.
name|After
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
specifier|public
class|class
name|WeightedAshwoodEntitySorterTest
block|{
specifier|private
name|ServerRuntime
name|runtime
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
name|runtime
operator|=
operator|new
name|ServerRuntime
argument_list|(
literal|"cayenne-lifecycle.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|runtime
operator|.
name|shutdown
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
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
comment|// since it is impossible to ensure non-coincidental sort order of
comment|// unrelated
comment|// DbEntities (without overriding DbEntity.hashCode()), we'll test on 2
comment|// entities
comment|// with a relationship, and reverse the topological order with
comment|// SortWeight
comment|// annotation.
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

