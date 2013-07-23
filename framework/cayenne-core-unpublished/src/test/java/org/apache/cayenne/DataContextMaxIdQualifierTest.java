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
name|ArrayList
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
name|query
operator|.
name|PrefetchTreeNode
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
name|testmap
operator|.
name|Bag
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
name|Box
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
name|DataContextMaxIdQualifierTest
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
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|protected
name|TableHelper
name|tBag
decl_stmt|;
specifier|protected
name|TableHelper
name|tBox
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
literal|"BALL"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BAG"
argument_list|)
expr_stmt|;
name|tBag
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BAG"
argument_list|)
expr_stmt|;
name|tBag
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tBox
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BOX"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BAG_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDisjointByIdPrefetch
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBag
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|"bag"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|i
operator|+
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDisjointByIdPrefetch_Zero
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBag
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|"bag"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|i
operator|+
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDisjointByIdPrefetch_Negative
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBag
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|"bag"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|i
operator|+
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIncrementalFaultList_Lower
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|50
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
for|for
control|(
name|Box
name|box
range|:
name|boxes
control|)
block|{
name|box
operator|.
name|getBag
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|21
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
name|queriesCount
operator|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<
name|Box
argument_list|>
argument_list|()
decl_stmt|;
name|tempList
operator|.
name|addAll
argument_list|(
name|boxes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|21
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIncrementalFaultList_Higher
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|1001
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
for|for
control|(
name|Box
name|box
range|:
name|boxes
control|)
block|{
name|box
operator|.
name|getBag
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
name|queriesCount
operator|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<
name|Box
argument_list|>
argument_list|()
decl_stmt|;
name|tempList
operator|.
name|addAll
argument_list|(
name|boxes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIncrementalFaultList_Zero
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<
name|Box
argument_list|>
argument_list|()
decl_stmt|;
name|tempList
operator|.
name|addAll
argument_list|(
name|boxes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIncrementalFaultList_Negative
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|tBox
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|1
argument_list|,
literal|1
argument_list|,
literal|"box"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setPageSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
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
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<
name|Box
argument_list|>
argument_list|()
decl_stmt|;
name|tempList
operator|.
name|addAll
argument_list|(
name|boxes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queriesCount
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
