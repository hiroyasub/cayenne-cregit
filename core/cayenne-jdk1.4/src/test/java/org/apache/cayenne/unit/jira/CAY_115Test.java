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
name|unit
operator|.
name|jira
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
name|testdo
operator|.
name|relationship
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
name|RelationshipCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CAY_115Test
extends|extends
name|RelationshipCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
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
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsLobInsertsAsStrings
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTestData
argument_list|(
literal|"testDistinctClobFetch"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|SelectQuery
name|noDistinct
init|=
operator|new
name|SelectQuery
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
name|NAME_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|SelectQuery
name|distinct
init|=
operator|new
name|SelectQuery
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
name|NAME_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|List
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
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsLobInsertsAsStrings
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTestData
argument_list|(
literal|"testDistinctClobFetchWithToManyJoin"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|qual
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"details.name like 'cd%'"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClobMaster
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
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

