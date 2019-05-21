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
name|dba
operator|.
name|oracle
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
name|AdhocObjectFactory
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
name|log
operator|.
name|JdbcEventLogger
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
name|DbKeyGenerator
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
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|OraclePkGeneratorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|private
name|OraclePkGenerator
name|pkGenerator
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
name|OracleAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|OracleAdapter
operator|.
name|class
argument_list|,
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|pkGenerator
operator|=
operator|new
name|OraclePkGenerator
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSequenceNameDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST_ENTITY"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pk_test_entity"
argument_list|,
name|pkGenerator
operator|.
name|sequenceName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSequenceNameCustom1
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST_ENTITY"
argument_list|)
decl_stmt|;
name|DbKeyGenerator
name|customGenerator
init|=
operator|new
name|DbKeyGenerator
argument_list|()
decl_stmt|;
name|customGenerator
operator|.
name|setGeneratorType
argument_list|(
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
argument_list|)
expr_stmt|;
name|customGenerator
operator|.
name|setGeneratorName
argument_list|(
literal|"CUSTOM_GENERATOR"
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setPrimaryKeyGenerator
argument_list|(
name|customGenerator
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"custom_generator"
argument_list|,
name|pkGenerator
operator|.
name|sequenceName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSequenceNameCustom2
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST_ENTITY"
argument_list|)
decl_stmt|;
name|DbKeyGenerator
name|customGenerator
init|=
operator|new
name|DbKeyGenerator
argument_list|()
decl_stmt|;
name|customGenerator
operator|.
name|setGeneratorType
argument_list|(
name|DbKeyGenerator
operator|.
name|NAMED_SEQUENCE_TABLE_TYPE
argument_list|)
expr_stmt|;
name|customGenerator
operator|.
name|setGeneratorName
argument_list|(
literal|"CUSTOM_GENERATOR"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pk_test_entity"
argument_list|,
name|pkGenerator
operator|.
name|sequenceName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

