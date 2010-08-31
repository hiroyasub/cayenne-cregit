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
name|DataContextEJBQLJoinsTest
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
specifier|protected
name|TableHelper
name|tGallery
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
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GALLERY"
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
literal|"GALLERY_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
name|tGallery
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"GALLERY"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|setColumns
argument_list|(
literal|"GALLERY_ID"
argument_list|,
literal|"GALLERY_NAME"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFourArtistsFourPaintings
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
literal|null
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
literal|null
argument_list|,
literal|"P2"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|33001
argument_list|,
literal|null
argument_list|,
literal|"AA1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|33002
argument_list|,
literal|null
argument_list|,
literal|"BB2"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtistsOnePainting
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
literal|33005
argument_list|,
literal|"AA1"
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
literal|null
argument_list|,
literal|"P1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtistsTwoPaintingsTwoGalleries
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
name|tGallery
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"gallery1"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"gallery2"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"CC1"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|33002
argument_list|,
literal|33002
argument_list|,
literal|"CC2"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtistsThreePaintings
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|null
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
literal|null
argument_list|,
literal|"P2"
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
literal|33001
argument_list|,
literal|null
argument_list|,
literal|"P2"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testThetaJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsFourPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT DISTINCT a "
operator|+
literal|"FROM Artist a, Painting b "
operator|+
literal|"WHERE a.artistName = b.paintingTitle"
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
argument_list|(
literal|2
argument_list|)
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
name|names
operator|.
name|add
argument_list|(
name|a
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
literal|"AA1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"BB2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInnerJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsOnePainting
argument_list|()
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
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|context
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
name|Cayenne
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
name|createTwoArtistsOnePainting
argument_list|()
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
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|context
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
argument_list|(
literal|2
argument_list|)
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
name|Cayenne
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
literal|33005l
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
name|createTwoArtistsTwoPaintingsTwoGalleries
argument_list|()
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
literal|33002
argument_list|,
name|Cayenne
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
name|createTwoArtistsTwoPaintingsTwoGalleries
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a "
operator|+
literal|"WHERE a.paintingArray.toGallery.galleryName = 'gallery2'"
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
name|context
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
literal|33002
argument_list|,
name|Cayenne
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
name|testPartialImplicitJoins1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsTwoPaintingsTwoGalleries
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a JOIN a.paintingArray b "
operator|+
literal|"WHERE a.paintingArray.toGallery.galleryName = 'gallery2'"
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
literal|33002
argument_list|,
name|Cayenne
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
name|testPartialImplicitJoins2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsTwoPaintingsTwoGalleries
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a JOIN a.paintingArray b "
operator|+
literal|"WHERE a.paintingArray.paintingTitle = 'CC2'"
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
literal|33002
argument_list|,
name|Cayenne
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
name|testMultipleJoinsToTheSameTable
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsThreePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT a "
operator|+
literal|"FROM Artist a JOIN a.paintingArray b JOIN a.paintingArray c "
operator|+
literal|"WHERE b.paintingTitle = 'P1' AND c.paintingTitle = 'P2'"
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
name|Cayenne
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
block|}
end_class

end_unit

