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
name|unit
operator|.
name|jira
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
name|relationships
operator|.
name|FkOfDifferentType
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|CAY_191IT
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
specifier|protected
name|TableHelper
name|tRelationshipHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tFkOfDifferentType
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
name|tRelationshipHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"RELATIONSHIP_HELPER"
argument_list|)
expr_stmt|;
name|tRelationshipHelper
operator|.
name|setColumns
argument_list|(
literal|"NAME"
argument_list|,
literal|"RELATIONSHIP_HELPER_ID"
argument_list|)
expr_stmt|;
name|tFkOfDifferentType
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FK_OF_DIFFERENT_TYPE"
argument_list|)
expr_stmt|;
name|tFkOfDifferentType
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"RELATIONSHIP_HELPER_FK"
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
name|tRelationshipHelper
operator|.
name|insert
argument_list|(
literal|"RH1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tFkOfDifferentType
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveToOneOverFKOfDifferentNumType
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this is mostly for legacy schemas, as on many dbs you won;t be able to even
comment|// create the FK constraint...
name|createTestDataSet
argument_list|()
expr_stmt|;
name|FkOfDifferentType
name|root
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FkOfDifferentType
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|root
operator|.
name|getRelationshipHelper
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RH1"
argument_list|,
name|root
operator|.
name|getRelationshipHelper
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

