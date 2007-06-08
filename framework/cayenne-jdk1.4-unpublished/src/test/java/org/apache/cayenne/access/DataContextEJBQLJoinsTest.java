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
name|DataContextEJBQLJoinsTest
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
name|testThetaJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testThetaJoins"
argument_list|)
expr_stmt|;
comment|// String ejbql = "SELECT DISTINCT a "
comment|// + "FROM Artist a, Painting b "
comment|// + "WHERE a.artistName = b.paintingTitle";
comment|//
comment|// EJBQLQuery query = new EJBQLQuery(ejbql);
comment|//
comment|// System.out.println(""
comment|// + query.getExpression(getDomain().getEntityResolver()).getExpression());
comment|// List artists = createDataContext().performQuery(query);
comment|// assertEquals(2, artists.size());
comment|//
comment|// Set names = new HashSet(2);
comment|// Iterator it = artists.iterator();
comment|// while (it.hasNext()) {
comment|// Artist a = (Artist) it.next();
comment|// names.add(a.getArtistName());
comment|// }
comment|//
comment|// assertTrue(names.contains("AA1"));
comment|// assertTrue(names.contains("BB2"));
block|}
specifier|public
name|void
name|testInnerJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testInnerJoins"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a INNER JOIN a.paintingArray p "
operator|+
literal|"WHERE a.artistName = 'AA1'"
decl_stmt|;
name|List
name|artists
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
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
literal|33001
argument_list|,
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOuterJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testInnerJoins"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a LEFT JOIN a.paintingArray p "
operator|+
literal|"WHERE a.artistName = 'AA1'"
decl_stmt|;
name|List
name|artists
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
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
name|ids
init|=
operator|new
name|HashSet
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Iterator
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
name|ids
operator|.
name|add
argument_list|(
name|DataObjectUtils
operator|.
name|pkForObject
argument_list|(
name|a
argument_list|)
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
literal|33005
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChainedJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testChainedJoins"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a JOIN a.paintingArray p JOIN p.toGallery g "
operator|+
literal|"WHERE g.galleryName = 'gallery2'"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
operator|+
name|query
operator|.
name|getExpression
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|artists
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
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33002
argument_list|,
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testImplicitJoins
parameter_list|()
throws|throws
name|Exception
block|{
comment|// createTestData("testChainedJoins");
comment|// String ejbql = "SELECT a "
comment|// + "FROM Artist a "
comment|// + "WHERE a.paintingArray.toGallery.galleryName = 'gallery2'";
comment|//
comment|// List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|// assertEquals(1, artists.size());
comment|//         assertEquals(33002, DataObjectUtils.intPKForObject((Artist) artists.get(0)));
block|}
specifier|public
name|void
name|testPartialImplicitJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testChainedJoins"
argument_list|)
expr_stmt|;
comment|// String ejbql = "SELECT a "
comment|// + "FROM Artist a JOIN a.paintingArray b "
comment|// + "WHERE a.paintingArray.toGallery.galleryName = 'gallery2'";
comment|//
comment|// List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|// assertEquals(1, artists.size());
comment|// assertEquals(33002, DataObjectUtils.intPKForObject((Artist) artists.get(0)));
block|}
specifier|public
name|void
name|testMultipleJoinsToTheSameTable
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testMultipleJoinsToTheSameTable"
argument_list|)
expr_stmt|;
comment|// String ejbql = "SELECT a "
comment|// + "FROM Artist a JOIN a.paintingArray b JOIN a.paintingArray c "
comment|// + "WHERE b.paintingTitle = 'P1' AND c.paintingTitle = 'P2'";
comment|//
comment|// List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|// assertEquals(1, artists.size());
comment|// assertEquals(33001, DataObjectUtils.intPKForObject((Artist) artists.get(0)));
block|}
block|}
end_class

end_unit

