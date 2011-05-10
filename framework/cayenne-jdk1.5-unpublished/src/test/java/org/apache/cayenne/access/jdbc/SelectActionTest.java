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
operator|.
name|jdbc
package|;
end_package

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
name|testdo
operator|.
name|testmap
operator|.
name|ClobTestEntity
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
name|testmap
operator|.
name|ClobTestRelation
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
name|AccessStackAdapter
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
name|SelectActionTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AccessStackAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
literal|"CLOB_TEST_RELATION"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_TEST"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFetchLimit_DistinctResultIterator
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|insertClobDb
argument_list|()
expr_stmt|;
name|Expression
name|qual
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"clobValue.value = 100"
argument_list|)
decl_stmt|;
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|select
operator|.
name|setFetchLimit
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultRows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|resultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|insertClobDb
parameter_list|()
block|{
name|ClobTestEntity
name|obj
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|80
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|<
literal|20
condition|)
block|{
name|obj
operator|=
operator|(
name|ClobTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ClobTestEntity"
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setClobCol
argument_list|(
literal|"a1"
operator|+
name|i
argument_list|)
expr_stmt|;
name|insetrClobRel
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
operator|(
name|ClobTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ClobTestEntity"
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setClobCol
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|insetrClobRel
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|insetrClobRel
parameter_list|(
name|ClobTestEntity
name|clobId
parameter_list|)
block|{
name|ClobTestRelation
name|obj
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|obj
operator|=
operator|(
name|ClobTestRelation
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"ClobTestRelation"
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setClobId
argument_list|(
name|clobId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

