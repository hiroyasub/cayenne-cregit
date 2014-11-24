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
name|Cayenne
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
name|map
operator|.
name|LifecycleEvent
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
name|EJBQLQuery
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
name|reflect
operator|.
name|LifecycleCallbackRegistry
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
name|test
operator|.
name|junit
operator|.
name|AssertExtras
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
name|math
operator|.
name|BigDecimal
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
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
name|assertTrue
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
name|DataContextEJBQLQueryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
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
name|UnitDbAdapter
name|accessStackAdapter
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
argument_list|,
literal|"ESTIMATED_PRICE"
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
argument_list|,
name|Types
operator|.
name|DECIMAL
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFourArtistsTwoPaintings
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
literal|"AA1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"BB1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"BB2"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|33002
argument_list|,
literal|"P2"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
comment|/**      * CAY-899: Checks that aggregate results do not cause callbacks execution.      */
annotation|@
name|Test
specifier|public
name|void
name|testSelectAggregatePostLoadCallback
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|LifecycleCallbackRegistry
name|existingCallbacks
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
decl_stmt|;
name|LifecycleCallbackRegistry
name|testCallbacks
init|=
operator|new
name|LifecycleCallbackRegistry
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|DataContextEJBQLQueryCallback
name|listener
init|=
operator|new
name|DataContextEJBQLQueryCallback
argument_list|()
decl_stmt|;
name|testCallbacks
operator|.
name|addDefaultListener
argument_list|(
name|LifecycleEvent
operator|.
name|POST_LOAD
argument_list|,
name|listener
argument_list|,
literal|"postLoad"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setCallbackRegistry
argument_list|(
name|testCallbacks
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|ejbql
init|=
literal|"select count(p), count(distinct p.estimatedPrice), max(p.estimatedPrice), sum(p.estimatedPrice) from Painting p"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|data
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|listener
operator|.
name|postLoad
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setCallbackRegistry
argument_list|(
name|existingCallbacks
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectAggregate
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select count(p), count(distinct p.estimatedPrice), max(p.estimatedPrice), sum(p.estimatedPrice) from Painting p"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|data
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
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|aggregates
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|aggregates
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
argument_list|,
name|aggregates
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|5000d
argument_list|)
argument_list|,
name|aggregates
index|[
literal|2
index|]
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|8000d
argument_list|)
argument_list|,
name|aggregates
index|[
literal|3
index|]
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectAggregateNull
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportNullRowForAggregateFunctions
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|ejbql
init|=
literal|"select count(p), max(p.estimatedPrice), sum(p.estimatedPrice) "
operator|+
literal|"from Painting p WHERE p.paintingTitle = 'X'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|data
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
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|aggregates
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0
argument_list|)
argument_list|,
name|aggregates
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|aggregates
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|aggregates
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEntityPathsScalarResult
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select p.paintingTitle"
operator|+
literal|" from Painting p order by p.paintingTitle DESC"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|data
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
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"P2"
argument_list|,
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"P1"
argument_list|,
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEntityPathsArrayResult
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select p.estimatedPrice, p.toArtist.artistName "
operator|+
literal|"from Painting p order by p.estimatedPrice"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|data
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
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row0
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|row0
operator|.
name|length
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|3000d
argument_list|)
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AA1"
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|Object
index|[]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row1
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|row1
operator|.
name|length
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|5000d
argument_list|)
argument_list|,
name|row1
index|[
literal|0
index|]
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AA2"
argument_list|,
name|row1
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchLimit
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setFetchLimit
argument_list|(
literal|2
argument_list|)
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
name|query
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a from Artist a where a.artistName = 'AA2'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"AA2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereEqualReverseOrder
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsReverseComparison
argument_list|()
condition|)
block|{
return|return;
block|}
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a from Artist a where 'AA2' = a.artistName"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"AA2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNot
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a from Artist a where not a.artistName = 'AA2'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|artists
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"AA2"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNotEquals
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a from Artist a where a.artistName<> 'AA2'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|artists
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"AA2"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereOrEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a from Artist a where a.artistName = 'AA2' or a.artistName = 'BB1'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
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
name|query
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
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|artists
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"AA2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"BB1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereAndEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P where P.paintingTitle = 'P1' "
operator|+
literal|"AND p.estimatedPrice = 3000"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P1"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|3000d
argument_list|)
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereBetween
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice BETWEEN 2000 AND 3500"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P1"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|3000d
argument_list|)
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNotBetween
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice NOT BETWEEN 2000 AND 3500"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P2"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|5000d
argument_list|)
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereGreater
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice> 3000"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P2"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|5000d
argument_list|)
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereGreaterOrEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice>= 3000"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
literal|2
argument_list|,
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereLess
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice< 5000"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"P1"
argument_list|,
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|AssertExtras
operator|.
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|3000d
argument_list|)
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereLessOrEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice<= 5000"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
literal|2
argument_list|,
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereDecimalNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice<= 5000.00"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
literal|2
argument_list|,
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereDecimalNumberPositional
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice<= ?1"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|5000.00
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
literal|2
argument_list|,
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereDecimalNumberNamed
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.estimatedPrice<= :param"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"param"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|5000.00
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
literal|2
argument_list|,
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereMatchOnObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|Artist
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.toArtist = :param"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"param"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|33002
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereMatchRelationshipAndScalar
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select P from Painting P WHERE p.toArtist = 33002"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|ps
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
name|ps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|ps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|33002
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

