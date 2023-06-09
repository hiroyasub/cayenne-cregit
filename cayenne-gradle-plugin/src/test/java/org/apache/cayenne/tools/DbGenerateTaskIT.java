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
name|tools
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|BuildResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|GradleRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|TaskOutcome
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbGenerateTaskIT
extends|extends
name|BaseTaskIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|notConfiguredTaskFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"cdbgen_failure"
argument_list|,
literal|"cdbgen"
argument_list|,
literal|"--info"
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|buildAndFail
argument_list|()
decl_stmt|;
comment|// NOTE: There will be no result for the task, as build will fail earlier because
comment|// datamap is required parameter that is validated directly by Gradle before task execution.
comment|//assertNotNull(result.task(":cdbgen"));
comment|//assertEquals(TaskOutcome.FAILED, result.task(":cdbgen").getOutcome());
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No datamap configured in task or in cayenne.defaultDataMap"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|defaultConfigTaskSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dbUrl
init|=
literal|"jdbc:derby:build/testdb"
decl_stmt|;
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"cdbgen_simple"
argument_list|,
literal|"cdbgen"
argument_list|,
literal|"-PdbUrl="
operator|+
name|dbUrl
argument_list|,
literal|"-PdataMap=test_datamap.map.xml"
argument_list|,
literal|"--info"
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|task
argument_list|(
literal|":cdbgen"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cdbgen"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"generator options - [dropTables: false, dropPK: false, createTables: true, createPK: true, createFK: true]"
argument_list|)
argument_list|)
expr_stmt|;
comment|/* // check that DB is really created         try (Connection connection = DriverManager.getConnection(dbUrl)) {             try (ResultSet rs = connection.getMetaData()                     .getTables(null, null, "artist", new String[]{"TABLE"})) {                 assertTrue(rs.next());             }         } */
block|}
annotation|@
name|Test
specifier|public
name|void
name|customConfigTaskSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"cdbgen_custom"
argument_list|,
literal|"customCdbgen"
argument_list|,
literal|"-PdataMap=test_datamap.map.xml"
argument_list|,
literal|"--info"
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|task
argument_list|(
literal|":customCdbgen"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":customCdbgen"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"generator options - [dropTables: true, dropPK: true, createTables: false, createPK: false, createFK: false]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

