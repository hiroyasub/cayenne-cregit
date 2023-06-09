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
name|Artist
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
name|Painting
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
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

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
name|DataContextMaxIdQualifierIT
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
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
specifier|private
name|TableHelper
name|tPainting
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
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|insertData
parameter_list|()
throws|throws
name|SQLException
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
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|tArtist
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"AA"
operator|+
name|i
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
name|i
argument_list|,
name|i
argument_list|,
literal|"P"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|insertData_OneBag_100Boxes
parameter_list|()
throws|throws
name|SQLException
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|tPainting
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|1
argument_list|,
literal|"P"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDisjointByIdPrefetch
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData
argument_list|()
expr_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjointById
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
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
annotation|@
name|Test
specifier|public
name|void
name|testDisjointByIdPrefetch_Zero
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData
argument_list|()
expr_stmt|;
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
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjointById
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
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
annotation|@
name|Test
specifier|public
name|void
name|testDisjointByIdPrefetch_Negative
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData
argument_list|()
expr_stmt|;
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
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjointById
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
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
annotation|@
name|Test
specifier|public
name|void
name|testIncrementalFaultList_Lower
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData_OneBag_100Boxes
argument_list|()
expr_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Painting
name|box
range|:
name|boxes
control|)
block|{
name|box
operator|.
name|getToArtist
argument_list|()
expr_stmt|;
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
parameter_list|()
lambda|->
block|{
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|boxes
argument_list|)
decl_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testIncrementalFaultList_Higher
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData_OneBag_100Boxes
argument_list|()
expr_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|setMaxIdQualifierSize
argument_list|(
literal|101
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
specifier|final
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Painting
name|box
range|:
name|boxes
control|)
block|{
name|box
operator|.
name|getToArtist
argument_list|()
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
name|queriesCount
operator|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
specifier|final
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|boxes
argument_list|)
decl_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testIncrementalFaultList_Zero
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData_OneBag_100Boxes
argument_list|()
expr_stmt|;
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
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
specifier|final
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|boxes
argument_list|)
decl_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testIncrementalFaultList_Negative
parameter_list|()
throws|throws
name|Exception
block|{
name|insertData_OneBag_100Boxes
argument_list|()
expr_stmt|;
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
name|ObjectSelect
argument_list|<
name|Painting
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|int
name|queriesCount
init|=
name|queryInterceptor
operator|.
name|runWithQueryCounter
argument_list|(
parameter_list|()
lambda|->
block|{
specifier|final
name|List
argument_list|<
name|Painting
argument_list|>
name|boxes
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|tempList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|boxes
argument_list|)
decl_stmt|;
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

