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
name|DataObject
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
name|PersistenceState
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
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SimpleIdIncrementalFaultListPrefetchIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
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
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPaining
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
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
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
name|tPaining
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createArtistsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist11"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist12"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"artist13"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"artist14"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|"artist15"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|"artist16"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|"artist21"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createArtistsAndPaintingsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"P_artist11"
argument_list|,
literal|33001
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"P_artist12"
argument_list|,
literal|33002
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"P_artist13"
argument_list|,
literal|33003
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"P_artist14"
argument_list|,
literal|33004
argument_list|,
literal|4000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|"P_artist15"
argument_list|,
literal|33005
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|"P_artist16"
argument_list|,
literal|33006
argument_list|,
literal|11000
argument_list|)
expr_stmt|;
name|tPaining
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|"P_artist21"
argument_list|,
literal|33007
argument_list|,
literal|21000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testListType
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|SimpleIdIncrementalFaultList
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that all queries specified in prefetch are executed with a single prefetch      * path.      */
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch1
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsAndPaintingsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
specifier|final
name|IncrementalFaultList
argument_list|<
name|?
argument_list|>
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// currently queries with prefetch do not resolve their first page
name|assertEquals
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|,
name|result
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
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
comment|// go through the second page objects and count queries
for|for
control|(
name|int
name|i
init|=
literal|3
init|;
name|i
operator|<
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
comment|// within the same page only one query should've been executed
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that a to-many relationship is initialized.      */
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch3
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsAndPaintingsDataSet
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|IncrementalFaultList
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// currently queries with prefetch do not resolve their first page
name|assertEquals
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|,
name|result
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
comment|// go through the second page objects and check their to many
for|for
control|(
name|int
name|i
init|=
literal|3
init|;
name|i
operator|<
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|result
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
name|paintings
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|paintings
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-one relationship is initialized.      */
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch4
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsAndPaintingsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|IncrementalFaultList
argument_list|<
name|?
argument_list|>
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
comment|// get an objects from the second page
specifier|final
name|DataObject
name|p1
init|=
operator|(
name|DataObject
operator|)
name|result
operator|.
name|get
argument_list|(
name|q
operator|.
name|getPageSize
argument_list|()
argument_list|)
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
name|Object
name|toOnePrefetch
init|=
name|p1
operator|.
name|readNestedProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toOnePrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected DataObject, got: "
operator|+
name|toOnePrefetch
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|toOnePrefetch
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|DataObject
name|a1
init|=
operator|(
name|DataObject
operator|)
name|toOnePrefetch
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
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

