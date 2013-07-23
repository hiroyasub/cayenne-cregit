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
name|Date
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
name|Persistent
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
name|DataContextEJBQLConditionsTest
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
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
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
specifier|protected
name|void
name|createCollectionDataSet
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
literal|"B"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33009
argument_list|,
literal|33001
argument_list|,
literal|"X"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33010
argument_list|,
literal|33001
argument_list|,
literal|"Y"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33011
argument_list|,
literal|33002
argument_list|,
literal|"Z"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createLikeDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|null
argument_list|,
literal|"ABAAC"
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
literal|null
argument_list|,
literal|"ADDDD"
argument_list|,
literal|4000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|null
argument_list|,
literal|"BDDDD"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|null
argument_list|,
literal|"BBDDDD"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|null
argument_list|,
literal|"_DDDD"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createGreaterThanDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|createInDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|null
argument_list|,
literal|"A"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33007
argument_list|,
literal|null
argument_list|,
literal|"B"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33008
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createInSubqueryDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|33012
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33013
argument_list|,
literal|null
argument_list|,
literal|"D"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33014
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDateParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|allArtists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|Date
name|dob
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|allArtists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setDateOfBirth
argument_list|(
name|dob
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE a.dateOfBirth = :x"
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
literal|"x"
argument_list|,
name|dob
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|allArtists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArithmetics
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
comment|// TODO: andrus 02/25/2008 - fails on HSQLDB / succeeds on MySQL. HSQLDB error is
comment|// "Unresolved parameter type : as both operands of aritmetic operator in
comment|// statement"
comment|// String ejbql = "SELECT p FROM Painting p WHERE p.estimatedPrice< (1 + - 4.0 *
comment|// - 1000.0)";
comment|//
comment|// EJBQLQuery query = new EJBQLQuery(ejbql);
comment|// List<?> objects = createDataContext().performQuery(query);
comment|// assertEquals(2, objects.size());
comment|//
comment|// Set<Object> ids = new HashSet<Object>();
comment|// Iterator<?> it = objects.iterator();
comment|// while (it.hasNext()) {
comment|// Object id = Cayenne.pkForObject((Persistent) it.next());
comment|// ids.add(id);
comment|// }
comment|//
comment|// assertTrue(ids.contains(new Integer(33001)));
comment|// assertTrue(ids.contains(new Integer(33002)));
block|}
specifier|public
name|void
name|testLike1
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle LIKE 'A%C'"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33001
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotLike
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle NOT LIKE 'A%C'"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33001
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLike2
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle LIKE '_DDDD'"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33003
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33005
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeEscape
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle LIKE 'X_DDDD' ESCAPE 'X'"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33005
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeEscape_LikeParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createLikeDataSet
argument_list|()
expr_stmt|;
comment|// test for CAY-1426
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle LIKE ?1 ESCAPE 'X'"
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
literal|"X_DDDD"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33005
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeNullParameter
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setDateOfBirth
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|eq1
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select a from Artist a where a.dateOfBirth like :param"
argument_list|)
decl_stmt|;
name|eq1
operator|.
name|setParameter
argument_list|(
literal|"param"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|eq1
argument_list|)
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|eq2
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select a from Artist a where a.dateOfBirth like ?1"
argument_list|)
decl_stmt|;
name|eq2
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|eq2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIn
parameter_list|()
throws|throws
name|Exception
block|{
name|createInDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle IN ('A', 'B')"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33006
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33007
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotIn
parameter_list|()
throws|throws
name|Exception
block|{
name|createInDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle NOT IN ('A', 'B')"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33008
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInSubquery
parameter_list|()
throws|throws
name|Exception
block|{
name|createInSubqueryDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.paintingTitle IN ("
operator|+
literal|"SELECT p1.paintingTitle FROM Painting p1 WHERE p1.paintingTitle = 'C'"
operator|+
literal|")"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33012
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33014
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE a.paintingArray IS EMPTY"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Long
argument_list|(
literal|33003
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionNotEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE a.paintingArray IS NOT EMPTY"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33001l
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33002l
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionNotEmptyExplicitDistinct
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT DISTINCT a FROM Artist a WHERE a.paintingArray IS NOT EMPTY"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33001l
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33002l
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionMemberOfParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE :x MEMBER OF a.paintingArray"
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
literal|"x"
argument_list|,
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
literal|33010
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33001l
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGreaterOrEquals
parameter_list|()
throws|throws
name|Exception
block|{
name|createGreaterThanDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice>= :estimatedPrice"
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
literal|"estimatedPrice"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|4000
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLessOrEquals
parameter_list|()
throws|throws
name|Exception
block|{
name|createGreaterThanDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice<= :estimatedPrice"
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
literal|"estimatedPrice"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|4000
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionNotMemberOfParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE :x NOT MEMBER a.paintingArray"
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
literal|"x"
argument_list|,
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Painting
operator|.
name|class
argument_list|,
literal|33010
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33002l
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|33003l
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionMemberOfThetaJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createCollectionDataSet
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p FROM Painting p, Artist a "
operator|+
literal|"WHERE p MEMBER OF a.paintingArray AND a.artistName = 'B'"
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
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
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
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33009
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33010
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
