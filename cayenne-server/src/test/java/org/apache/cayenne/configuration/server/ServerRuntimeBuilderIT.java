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
name|configuration
operator|.
name|server
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
name|DataRow
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
name|conn
operator|.
name|DataSourceInfo
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
name|SQLSelect
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ServerRuntimeBuilderIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataSourceInfo
name|dsi
decl_stmt|;
specifier|private
name|ServerRuntime
name|localRuntime
decl_stmt|;
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|stopLocalRuntime
parameter_list|()
block|{
comment|// even though we don't supply real configs here, we sometimes access
comment|// DataDomain, and this starts EventManager threads that need to be
comment|// shutdown
if|if
condition|(
name|localRuntime
operator|!=
literal|null
condition|)
block|{
name|localRuntime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tArtist
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|runtime
operator|.
name|getDataSource
argument_list|(
literal|"testmap"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigFree_WithDataSource
parameter_list|()
block|{
name|localRuntime
operator|=
operator|new
name|ServerRuntimeBuilder
argument_list|()
operator|.
name|dataSource
argument_list|(
name|dataSource
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
operator|.
name|select
argument_list|(
name|localRuntime
operator|.
name|newContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigFree_WithDBParams
parameter_list|()
block|{
name|localRuntime
operator|=
operator|new
name|ServerRuntimeBuilder
argument_list|()
operator|.
name|jdbcDriver
argument_list|(
name|dsi
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
operator|.
name|url
argument_list|(
name|dsi
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|)
operator|.
name|password
argument_list|(
name|dsi
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|user
argument_list|(
name|dsi
operator|.
name|getUserName
argument_list|()
argument_list|)
operator|.
name|minConnections
argument_list|(
literal|1
argument_list|)
operator|.
name|maxConnections
argument_list|(
literal|2
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
operator|.
name|select
argument_list|(
name|localRuntime
operator|.
name|newContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_UnnamedDomain_MultiLocation
parameter_list|()
block|{
name|localRuntime
operator|=
operator|new
name|ServerRuntimeBuilder
argument_list|()
operator|.
name|addConfigs
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|,
name|CayenneProjects
operator|.
name|EMBEDDABLE_PROJECT
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cayenne"
argument_list|,
name|localRuntime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_NamedDomain_MultiLocation
parameter_list|()
block|{
name|localRuntime
operator|=
operator|new
name|ServerRuntimeBuilder
argument_list|(
literal|"myd"
argument_list|)
operator|.
name|addConfigs
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|,
name|CayenneProjects
operator|.
name|EMBEDDABLE_PROJECT
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myd"
argument_list|,
name|localRuntime
operator|.
name|getDataDomain
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

