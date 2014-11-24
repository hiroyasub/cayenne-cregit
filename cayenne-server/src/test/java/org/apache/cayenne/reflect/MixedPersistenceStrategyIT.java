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
name|reflect
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
name|ValueHolder
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
name|mixed_persistence_strategy
operator|.
name|MixedPersistenceStrategy
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
name|mixed_persistence_strategy
operator|.
name|MixedPersistenceStrategy2
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests conflicts between field and map-based persistence.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MIXED_PERSISTENCE_STRATEGY_PROJECT
argument_list|)
specifier|public
class|class
name|MixedPersistenceStrategyIT
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
name|tMixedPersistenceStrategy
decl_stmt|;
specifier|protected
name|TableHelper
name|tMixedPersistenceStrategy2
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
name|tMixedPersistenceStrategy
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MIXED_PERSISTENCE_STRATEGY"
argument_list|)
expr_stmt|;
name|tMixedPersistenceStrategy
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"DESCRIPTION"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tMixedPersistenceStrategy2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MIXED_PERSISTENCE_STRATEGY2"
argument_list|)
expr_stmt|;
name|tMixedPersistenceStrategy2
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"MASTER_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createConflictingFieldDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMixedPersistenceStrategy
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"d1"
argument_list|,
literal|"n1"
argument_list|)
expr_stmt|;
name|tMixedPersistenceStrategy2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"dn1"
argument_list|)
expr_stmt|;
name|tMixedPersistenceStrategy2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"dn2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConflictingField1
parameter_list|()
throws|throws
name|Exception
block|{
name|createConflictingFieldDataSet
argument_list|()
expr_stmt|;
name|MixedPersistenceStrategy
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MixedPersistenceStrategy
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|object
operator|.
name|getDetails
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|.
name|getDetails
argument_list|()
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test case reproduces CAY-582 bug.      */
annotation|@
name|Test
specifier|public
name|void
name|testConflictingField2
parameter_list|()
throws|throws
name|Exception
block|{
name|createConflictingFieldDataSet
argument_list|()
expr_stmt|;
name|MixedPersistenceStrategy2
name|detail1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MixedPersistenceStrategy2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|MixedPersistenceStrategy2
name|detail2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MixedPersistenceStrategy2
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
comment|// resolve master (this is where CAY-582 exception happens)
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|detail1
operator|.
name|getMaster
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|detail2
operator|.
name|getMaster
argument_list|()
operator|.
name|getDetails
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|detail2
operator|.
name|getMaster
argument_list|()
operator|.
name|getDetails
argument_list|()
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

