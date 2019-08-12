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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|ObjectSelect
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
name|relationships_clob
operator|.
name|ClobMaster
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
name|UnitDbAdapter
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

begin_comment
comment|/**  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_CLOB_PROJECT
argument_list|)
specifier|public
class|class
name|CAY_115IT
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
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tClobMaster
decl_stmt|;
specifier|protected
name|TableHelper
name|tClobDetail
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
name|tClobMaster
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CLOB_MASTER"
argument_list|)
expr_stmt|;
name|tClobMaster
operator|.
name|setColumns
argument_list|(
literal|"CLOB_MASTER_ID"
argument_list|,
literal|"CLOB_COLUMN"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CLOB_DETAIL"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|.
name|setColumns
argument_list|(
literal|"CLOB_DETAIL_ID"
argument_list|,
literal|"CLOB_MASTER_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createDistinctClobFetchDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"cm1 clob"
argument_list|,
literal|"cm1"
argument_list|)
expr_stmt|;
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"cm2 clob"
argument_list|,
literal|"cm2"
argument_list|)
expr_stmt|;
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"cm3 clob"
argument_list|,
literal|"cm3"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createDistinctClobFetchWithToManyJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"cm1 clob"
argument_list|,
literal|"cm1"
argument_list|)
expr_stmt|;
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"cm2 clob"
argument_list|,
literal|"cm2"
argument_list|)
expr_stmt|;
name|tClobMaster
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"cm3 clob"
argument_list|,
literal|"cm3"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"cd11"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|"cd21"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|,
literal|"cd22"
argument_list|)
expr_stmt|;
name|tClobDetail
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|3
argument_list|,
literal|"cd31"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Deprecated
specifier|public
name|void
name|testDistinctClobFetch
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsLobInsertsAsStrings
argument_list|()
condition|)
block|{
return|return;
block|}
name|createDistinctClobFetchDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClobMaster
argument_list|>
name|noDistinct
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|ClobMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|noDistinct
operator|.
name|addOrdering
argument_list|(
name|ClobMaster
operator|.
name|NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|ClobMaster
argument_list|>
name|distinct
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|ClobMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|distinct
operator|.
name|setDistinct
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|distinct
operator|.
name|addOrdering
argument_list|(
name|ClobMaster
operator|.
name|NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|noDistinctResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|noDistinct
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|distinctResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|distinct
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|noDistinctResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|noDistinctResult
argument_list|,
name|distinctResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistinctClobFetchWithToManyJoin
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsLobInsertsAsStrings
argument_list|()
condition|)
block|{
return|return;
block|}
name|createDistinctClobFetchWithToManyJoin
argument_list|()
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"details.name like 'cd%'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|ClobMaster
operator|.
name|class
argument_list|,
name|qual
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

