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
name|DataChannel
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
name|MockDataChannel
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
name|ObjectId
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
name|QueryResponse
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
name|graph
operator|.
name|MockGraphDiff
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
name|graph
operator|.
name|NodeCreateOperation
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
name|EntityResolver
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
name|MockQuery
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
name|Query
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
name|remote
operator|.
name|QueryMessage
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
name|ClientMtTable1Subclass
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable3
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
name|MtTable1
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
name|EqualsBuilder
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
name|ClientServerChannelTest
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|serverContext
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ClientServerChannel
name|clientServerChannel
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable2
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable3
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
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE3"
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
name|tMtTable3
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE3"
argument_list|)
expr_stmt|;
name|tMtTable3
operator|.
name|setColumns
argument_list|(
literal|"TABLE3_ID"
argument_list|,
literal|"BINARY_COLUMN"
argument_list|,
literal|"CHAR_COLUMN"
argument_list|,
literal|"INT_COLUMN"
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|testGetEntityResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|clientServerChannel
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
operator|.
name|getClientEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSynchronizeCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// no changes...
name|clientServerChannel
operator|.
name|onSync
argument_list|(
name|serverContext
argument_list|,
operator|new
name|MockGraphDiff
argument_list|()
argument_list|,
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|serverContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// introduce changes
name|clientServerChannel
operator|.
name|onSync
argument_list|(
name|serverContext
argument_list|,
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|)
argument_list|,
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serverContext
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryObjectIDInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|55
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|Query
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
name|clientServerChannel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
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
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|clientObject
init|=
operator|(
name|ClientMtTable1
operator|)
name|result
decl_stmt|;
name|assertNotNull
argument_list|(
name|clientObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
literal|55
argument_list|)
argument_list|,
name|clientObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryValuePropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
decl_stmt|;
name|tMtTable3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
name|bytes
argument_list|,
literal|"abc"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Query
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable3"
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
name|clientServerChannel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
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
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is of wrong type: "
operator|+
name|result
argument_list|,
name|result
operator|instanceof
name|ClientMtTable3
argument_list|)
expr_stmt|;
name|ClientMtTable3
name|clientObject
init|=
operator|(
name|ClientMtTable3
operator|)
name|result
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|clientObject
operator|.
name|getCharColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
argument_list|,
name|clientObject
operator|.
name|getIntColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|clientObject
operator|.
name|getBinaryColumn
argument_list|()
argument_list|,
name|bytes
argument_list|)
operator|.
name|isEquals
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryPropagationInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|65
argument_list|,
literal|"sub1"
argument_list|,
literal|"xyz"
argument_list|)
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
name|QueryResponse
name|response
init|=
name|clientServerChannel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
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
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is of wrong type: "
operator|+
name|result
argument_list|,
name|result
operator|instanceof
name|ClientMtTable1Subclass
argument_list|)
expr_stmt|;
name|ClientMtTable1Subclass
name|clientObject
init|=
operator|(
name|ClientMtTable1Subclass
operator|)
name|result
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sub1"
argument_list|,
name|clientObject
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnQuery
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|genericDone
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|MockDataChannel
name|parent
init|=
operator|new
name|MockDataChannel
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|genericDone
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|onQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|DataContext
name|context
init|=
operator|(
name|DataContext
operator|)
name|runtime
operator|.
name|newContext
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|QueryMessage
name|message
init|=
operator|new
name|QueryMessage
argument_list|(
operator|new
name|MockQuery
argument_list|()
argument_list|)
decl_stmt|;
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|message
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|genericDone
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnQueryPrefetchingToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
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
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|clientServerChannel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|queryInterceptor
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
name|assertNull
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
name|children1
init|=
name|o1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|children1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ClientMtTable2
name|o
range|:
name|children1
control|)
block|{
name|assertNull
argument_list|(
name|o
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnQueryPrefetchingToManyEmpty
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
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|clientServerChannel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|q
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|queryInterceptor
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
name|assertNull
argument_list|(
name|o2
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|children2
init|=
name|o2
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|children2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|children2
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|children2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

