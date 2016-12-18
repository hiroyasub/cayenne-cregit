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
name|map_to_many
operator|.
name|MapToMany
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
name|relationships_set_to_many
operator|.
name|SetToMany
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
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_SET_TO_MANY_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneDataObjectSetToManySetIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tSetToMany
decl_stmt|;
specifier|protected
name|TableHelper
name|tSetToManyTarget
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
name|tSetToMany
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"SET_TO_MANY"
argument_list|)
expr_stmt|;
name|tSetToMany
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"SET_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"SET_TO_MANY_ID"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTestDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tSetToMany
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tSetToMany
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tSetToManyTarget
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Testing if collection type is set, everything should work fine without an 	 * runtimexception 	 *  	 * @throws Exception 	 */
annotation|@
name|Test
specifier|public
name|void
name|testRelationCollectionTypeMap
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|SetToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|SetToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|readProperty
argument_list|(
name|SetToMany
operator|.
name|TARGETS
operator|.
name|getName
argument_list|()
argument_list|)
operator|instanceof
name|Set
argument_list|)
expr_stmt|;
name|boolean
name|catchedSomething
init|=
literal|false
decl_stmt|;
try|try
block|{
name|o1
operator|.
name|setToManyTarget
argument_list|(
name|SetToMany
operator|.
name|TARGETS
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|MapToMany
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|catchedSomething
operator|=
literal|true
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|catchedSomething
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

