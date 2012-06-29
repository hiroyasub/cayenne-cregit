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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|NamedQuery
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
name|DataChannelInterceptor
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
name|UnitTestClosure
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
name|client
operator|.
name|ClientCase
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
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextNamedQueryCachingTest
extends|extends
name|ClientCase
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
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Inject
argument_list|(
name|ClientCase
operator|.
name|ROP_CLIENT_KEY
argument_list|)
specifier|private
name|DataChannelInterceptor
name|clientServerInterceptor
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
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
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|setColumns
argument_list|(
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"SERVER_ATTRIBUTE1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createThreeMtTable1sDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"g2"
argument_list|,
literal|"s2"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"g3"
argument_list|,
literal|"s3"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLocalCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreeMtTable1sDataSet
argument_list|()
expr_stmt|;
specifier|final
name|NamedQuery
name|q1
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"MtQueryWithLocalCache"
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|result1
argument_list|,
name|result2
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// refresh
name|q1
operator|.
name|setForceNoCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|result1
argument_list|,
name|result3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLocalCacheParameterized
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreeMtTable1sDataSet
argument_list|()
expr_stmt|;
specifier|final
name|NamedQuery
name|q1
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"ParameterizedMtQueryWithLocalCache"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"g"
argument_list|,
literal|"g1"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|NamedQuery
name|q2
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"ParameterizedMtQueryWithLocalCache"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"g"
argument_list|,
literal|"g2"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|result1
argument_list|,
name|result2
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|result1
argument_list|,
name|result3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|clientServerInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|result4
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|result3
argument_list|,
name|result4
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result5
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|result1
argument_list|,
name|result5
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParameterizedMappedToEJBQLQueries
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreeMtTable1sDataSet
argument_list|()
expr_stmt|;
name|NamedQuery
name|query
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"ParameterizedEJBQLMtQuery"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"g"
argument_list|,
literal|"g1"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|r1
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
literal|1
argument_list|,
name|r1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

