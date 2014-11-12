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
name|dba
operator|.
name|DbAdapter
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
name|DataMap
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertSame
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
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DbGeneratorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|adapter
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
name|JdbcEventLogger
name|logger
decl_stmt|;
specifier|private
name|DbGenerator
name|generator
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
name|generator
operator|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
argument_list|,
name|logger
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSame
argument_list|(
name|adapter
argument_list|,
name|generator
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPkFilteringLogic
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|DbEntity
name|artistExhibit
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
decl_stmt|;
name|DbEntity
name|exhibit
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"EXHIBIT"
argument_list|)
decl_stmt|;
comment|// sanity check
name|assertNotNull
argument_list|(
name|artistExhibit
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exhibit
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|generator
operator|.
name|dbEntitiesRequiringAutoPK
argument_list|)
expr_stmt|;
comment|// real test
name|assertTrue
argument_list|(
name|generator
operator|.
name|dbEntitiesRequiringAutoPK
operator|.
name|contains
argument_list|(
name|exhibit
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|generator
operator|.
name|dbEntitiesRequiringAutoPK
operator|.
name|contains
argument_list|(
name|artistExhibit
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatePkSupport
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|generator
operator|.
name|shouldCreatePKSupport
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|generator
operator|.
name|shouldCreatePKSupport
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShouldCreateTables
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|generator
operator|.
name|shouldCreateTables
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|generator
operator|.
name|shouldCreateTables
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDropPkSupport
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|generator
operator|.
name|shouldDropPKSupport
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generator
operator|.
name|shouldDropPKSupport
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShouldDropTables
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|generator
operator|.
name|shouldDropTables
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generator
operator|.
name|shouldDropTables
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

