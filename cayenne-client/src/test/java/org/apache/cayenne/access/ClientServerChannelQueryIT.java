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
name|cache
operator|.
name|QueryCache
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
name|query
operator|.
name|SortOrder
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable2
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|PersistentObjectHolder
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
name|util
operator|.
name|PersistentObjectList
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
name|assertTrue
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
name|ClientServerChannelQueryIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
argument_list|(
name|ClientCase
operator|.
name|ROP_CLIENT_KEY
argument_list|)
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ClientServerChannel
name|serverChannel
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable2
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
name|tMtTable2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|setColumns
argument_list|(
literal|"TABLE2_ID"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSevenMtTable1sDataSet
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|7
condition|;
name|i
operator|++
control|)
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"g"
operator|+
name|i
argument_list|,
literal|"s"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|createTwoMtTable1sAnd2sDataSet
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
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"g1"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPaginatedQueryServerCacheOverflow
parameter_list|()
throws|throws
name|Exception
block|{
name|createSevenMtTable1sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
comment|// read page 1
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
comment|// now kick out the server-side list from local cache, and see if the query would
comment|// recover...
name|QueryCache
name|qc
init|=
name|serverChannel
operator|.
name|getQueryCache
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|qc
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|qc
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|qc
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParameterizedMappedToEJBQLQueries
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
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
name|assertTrue
argument_list|(
name|r1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamedQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|NamedQuery
name|q
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"AllMtTable1"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryEntityNameRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryClientClassRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQuerySimpleQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"globalAttribute1 = 'g1'"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryToManyRelationshipQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"table2Array.globalAttribute = 'g1'"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryOrdering
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|getGlobalAttribute1
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
comment|// now run the same query with reverse ordering to check that the first ordering
comment|// result wasn't coincidental.
name|q
operator|.
name|clearOrderings
argument_list|()
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o3
init|=
operator|(
name|ClientMtTable1
operator|)
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|o4
init|=
operator|(
name|ClientMtTable1
operator|)
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|o3
operator|.
name|getGlobalAttribute1
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o4
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryPrefetchToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"globalAttribute = 'g1'"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable2
operator|.
name|TABLE1_PROPERTY
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|result
init|=
operator|(
name|ClientMtTable2
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ValueHolder
name|holder
init|=
name|result
operator|.
name|getTable1Direct
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|holder
operator|instanceof
name|PersistentObjectHolder
argument_list|)
expr_stmt|;
name|PersistentObjectHolder
name|objectHolder
init|=
operator|(
name|PersistentObjectHolder
operator|)
name|holder
decl_stmt|;
name|assertFalse
argument_list|(
name|objectHolder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|target
init|=
operator|(
name|ClientMtTable1
operator|)
name|objectHolder
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryPrefetchToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"globalAttribute1 = 'g1'"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|result
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|holder
init|=
name|result
operator|.
name|getTable2ArrayDirect
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|holder
operator|instanceof
name|PersistentObjectList
argument_list|)
expr_stmt|;
name|PersistentObjectList
name|objectHolder
init|=
operator|(
name|PersistentObjectList
operator|)
name|holder
decl_stmt|;
name|assertFalse
argument_list|(
name|objectHolder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objectHolder
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

