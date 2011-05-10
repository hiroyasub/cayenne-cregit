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
name|math
operator|.
name|BigDecimal
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|SQLTemplate
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
name|DataContextPerformQueryAPITest
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
name|DataContext
name|context2
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
name|DataChannelInterceptor
name|queryInterceptor
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
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GALLERY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EXHIBIT"
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
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtists
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|21
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|201
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtistsAndTwoPaintingsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|101
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"p_artist3"
argument_list|,
literal|101
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"p_artist2"
argument_list|,
literal|11
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectQueryStringBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsAndTwoPaintingsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|paintings
init|=
name|context
operator|.
name|performQuery
argument_list|(
literal|"ObjectQuery"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|paintings
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectQueryStringMapBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsAndTwoPaintingsDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|localObject
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|11
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Artist
argument_list|>
name|parameters
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"artist"
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|paintings
init|=
name|context2
operator|.
name|performQuery
argument_list|(
literal|"ObjectQuery"
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|paintings
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
specifier|public
name|void
name|testProcedureQueryStringMapBoolean
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|canMakeObjectsOutOfProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTwoArtistsAndTwoPaintingsDataSet
argument_list|()
expr_stmt|;
comment|// fetch artist
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"aName"
argument_list|,
literal|"artist2"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
decl_stmt|;
comment|// Sybase blows whenever a transaction wraps a SP, so turn of transactions
name|boolean
name|transactionsFlag
init|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|isUsingExternalTransactions
argument_list|()
decl_stmt|;
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|setUsingExternalTransactions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|artists
operator|=
name|context
operator|.
name|performQuery
argument_list|(
literal|"ProcedureQuery"
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|setUsingExternalTransactions
argument_list|(
name|transactionsFlag
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|artists
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|artist
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNonSelectingQueryString
parameter_list|()
throws|throws
name|Exception
block|{
name|int
index|[]
name|counts
init|=
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
literal|"NonSelectingQuery"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|counts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counts
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|localObject
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Painting"
argument_list|,
name|Painting
operator|.
name|PAINTING_ID_PK_COLUMN
argument_list|,
literal|512
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"No Painting Like This"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNonSelectingQueryStringMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|300
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"title"
argument_list|,
literal|"Go Figure"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"price"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"22.01"
argument_list|)
argument_list|)
expr_stmt|;
name|int
index|[]
name|counts
init|=
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
literal|"ParameterizedNonSelectingQuery"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|counts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counts
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|localObject
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Painting"
argument_list|,
name|Painting
operator|.
name|PAINTING_ID_PK_COLUMN
argument_list|,
literal|300
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Go Figure"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerfomQueryNonSelecting
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SQLTemplate
name|q
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"DELETE FROM ARTIST"
argument_list|)
decl_stmt|;
comment|// this way of executing a query makes no sense, but it shouldn't blow either...
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
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectQueryWithLocalCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
literal|"QueryWithLocalCache"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|?
argument_list|>
name|artists1
init|=
name|context
operator|.
name|performQuery
argument_list|(
literal|"QueryWithLocalCache"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists1
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
specifier|public
name|void
name|testObjectQueryWithSharedCache
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtists
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
literal|"QueryWithSharedCache"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|?
argument_list|>
name|artists1
init|=
name|context2
operator|.
name|performQuery
argument_list|(
literal|"QueryWithSharedCache"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists1
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

