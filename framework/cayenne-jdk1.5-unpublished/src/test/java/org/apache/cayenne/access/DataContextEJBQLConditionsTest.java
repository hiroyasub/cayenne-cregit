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
name|art
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
name|DataObjectUtils
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextEJBQLConditionsTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testLike1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareLike"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareLike"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareLike"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareLike"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|testIn
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareIn"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareIn"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareInSubquery"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
block|}
specifier|public
name|void
name|testCollectionNotEmptyExplicitDistinct
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
block|}
specifier|public
name|void
name|testCollectionMemberOfParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE :x MEMBER OF a.paintingArray"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|DataObjectUtils
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
name|testCollectionNotMemberOfParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a FROM Artist a WHERE :x NOT MEMBER a.paintingArray"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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
name|DataObjectUtils
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
block|}
specifier|public
name|void
name|testCollectionMemberOfThetaJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareCollection"
argument_list|)
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
name|createDataContext
argument_list|()
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
name|DataObjectUtils
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

