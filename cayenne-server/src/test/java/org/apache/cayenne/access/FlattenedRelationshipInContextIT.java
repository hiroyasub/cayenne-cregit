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
name|Fault
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
name|reflect
operator|.
name|ArcProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|relationship
operator|.
name|FlattenedTest3
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
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|FlattenedRelationshipInContextIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tFlattenedTest1
decl_stmt|;
specifier|private
name|TableHelper
name|tFlattenedTest2
decl_stmt|;
specifier|private
name|TableHelper
name|tFlattenedTest3
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
literal|"COMPLEX_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_4"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_3"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_1"
argument_list|)
expr_stmt|;
name|tFlattenedTest1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_1"
argument_list|)
expr_stmt|;
name|tFlattenedTest1
operator|.
name|setColumns
argument_list|(
literal|"FT1_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tFlattenedTest2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_2"
argument_list|)
expr_stmt|;
name|tFlattenedTest2
operator|.
name|setColumns
argument_list|(
literal|"FT2_ID"
argument_list|,
literal|"FT1_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tFlattenedTest3
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_3"
argument_list|)
expr_stmt|;
name|tFlattenedTest3
operator|.
name|setColumns
argument_list|(
literal|"FT3_ID"
argument_list|,
literal|"FT2_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createFlattenedTestDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tFlattenedTest1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"ft1"
argument_list|)
expr_stmt|;
name|tFlattenedTest1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"ft12"
argument_list|)
expr_stmt|;
name|tFlattenedTest2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"ft2"
argument_list|)
expr_stmt|;
name|tFlattenedTest3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"ft3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsToOneTargetModifiedFlattenedFault1
parameter_list|()
throws|throws
name|Exception
block|{
name|createFlattenedTestDataSet
argument_list|()
expr_stmt|;
comment|// fetch
name|List
argument_list|<
name|?
argument_list|>
name|ft3s
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ft3s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|ft3
init|=
operator|(
name|FlattenedTest3
operator|)
name|ft3s
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// mark as dirty for the purpose of the test...
name|ft3
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
comment|// test that checking for modifications does not trigger a fault, and generally
comment|// works well
name|ClassDescriptor
name|d
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"FlattenedTest3"
argument_list|)
decl_stmt|;
name|ArcProperty
name|flattenedRel
init|=
operator|(
name|ArcProperty
operator|)
name|d
operator|.
name|getProperty
argument_list|(
literal|"toFT1"
argument_list|)
decl_stmt|;
name|ObjectDiff
name|diff
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerDiff
argument_list|(
name|ft3
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|DataRowUtils
operator|.
name|isToOneTargetModified
argument_list|(
name|flattenedRel
argument_list|,
name|ft3
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

