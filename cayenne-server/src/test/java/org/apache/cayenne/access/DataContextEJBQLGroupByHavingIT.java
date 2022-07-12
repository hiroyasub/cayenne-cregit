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
name|testdo
operator|.
name|testmap
operator|.
name|Gallery
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEJBQLGroupByHavingIT
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
specifier|protected
name|TableHelper
name|tGallery
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
literal|"GALLERY_ID"
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
name|INTEGER
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
name|createFivePaintings
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
literal|null
argument_list|,
literal|"PX"
argument_list|,
literal|1
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
literal|null
argument_list|,
literal|"PY"
argument_list|,
literal|2
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
literal|null
argument_list|,
literal|"PY"
argument_list|,
literal|2
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
literal|null
argument_list|,
literal|"PZ"
argument_list|,
literal|1
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
literal|null
argument_list|,
literal|"PZ"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFourArtistsAndTwoPaintings
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
literal|33007
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
literal|33008
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
block|}
specifier|private
name|void
name|createArtistsPaintingGalleries
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
literal|33001
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"PX"
argument_list|,
literal|1
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
literal|null
argument_list|,
literal|"PY"
argument_list|,
literal|2
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
literal|null
argument_list|,
literal|"PY"
argument_list|,
literal|2
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
literal|"P1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33008
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
literal|33009
argument_list|,
literal|33002
argument_list|,
literal|33001
argument_list|,
literal|"P111"
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
literal|33002
argument_list|,
literal|"P112"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupBy
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" ORDER BY p.estimatedPrice"
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
literal|1d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row0
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|row0
index|[
literal|1
index|]
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
literal|2d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row1
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
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
name|testGroupByMultipleItems
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, p.paintingTitle, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice, p.paintingTitle"
operator|+
literal|" ORDER BY p.estimatedPrice, p.paintingTitle"
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
literal|3
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
literal|1d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row0
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PX"
argument_list|,
name|row0
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|row0
index|[
literal|2
index|]
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
literal|1d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row1
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PZ"
argument_list|,
name|row1
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|row1
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row2
init|=
operator|(
name|Object
index|[]
operator|)
name|data
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row2
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PY"
argument_list|,
name|row2
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|row2
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
name|testGroupByRelatedEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsAndTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT COUNT(p), a, a.artistName "
operator|+
literal|"FROM Painting p INNER JOIN p.toArtist a GROUP BY a, a.artistName "
operator|+
literal|"ORDER BY a.artistName"
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
literal|3
argument_list|,
name|row0
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|row0
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AA1"
argument_list|,
name|row0
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row0
index|[
literal|1
index|]
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByIdVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT count(p), p FROM Painting p GROUP BY p"
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
literal|5
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: andrus, 8/3/2007 the rest of the unit test fails as currently Cayenne
comment|// does not allow mixed object and scalar results (see CAY-839)
comment|// assertTrue(data.get(0) instanceof Object[]);
comment|//
comment|// for(int i = 0; i< data.size(); i++) {
comment|// Object[] row = (Object[]) data.get(i);
comment|// assertEquals(1L, row[0]);
comment|// }
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByHavingOnColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING p.estimatedPrice> 1"
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
literal|2d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row0
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|row0
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
name|testGroupByHavingOnAggregate
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING count(p)> 2"
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
literal|1d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row0
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|row0
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
name|testGroupByHavingOnAggregateMultipleConditions
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.estimatedPrice, count(p) FROM Painting p"
operator|+
literal|" GROUP BY p.estimatedPrice"
operator|+
literal|" HAVING count(p)> 2 AND p.estimatedPrice< 10"
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
literal|1d
argument_list|,
operator|(
operator|(
name|BigDecimal
operator|)
name|row0
index|[
literal|0
index|]
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.00001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|row0
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
name|testGroupByJoinedRelatedEntities
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourArtistsAndTwoPaintings
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT COUNT(p), p.toArtist FROM Painting p GROUP BY p.toArtist "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
index|[]
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
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|String
argument_list|>
name|expectedArtists
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedArtists
operator|.
name|add
argument_list|(
literal|"AA1"
argument_list|)
expr_stmt|;
name|expectedArtists
operator|.
name|add
argument_list|(
literal|"AA2"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|artistName
init|=
operator|(
operator|(
name|Artist
operator|)
name|row
index|[
literal|1
index|]
operator|)
operator|.
name|getArtistName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|row
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"error artistName:"
operator|+
name|artistName
argument_list|,
name|expectedArtists
operator|.
name|contains
argument_list|(
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|=
name|data
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|artistName
operator|=
operator|(
operator|(
name|Artist
operator|)
name|row
index|[
literal|1
index|]
operator|)
operator|.
name|getArtistName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|row
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"error artistName:"
operator|+
name|artistName
argument_list|,
name|expectedArtists
operator|.
name|contains
argument_list|(
name|artistName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByJoinedEntities
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsPaintingGalleries
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT COUNT(p), p.toArtist, p.toGallery FROM Painting p "
operator|+
literal|"GROUP BY p.toGallery, p.toArtist "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
index|[]
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
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
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
name|HashSet
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|expectedResults
init|=
operator|new
name|HashSet
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1L
argument_list|,
literal|"AA2"
argument_list|,
literal|"gallery1"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1L
argument_list|,
literal|"AA1"
argument_list|,
literal|"gallery2"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
index|[]
name|row
range|:
name|data
control|)
block|{
name|assertFalse
argument_list|(
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|row
index|[
literal|0
index|]
argument_list|,
name|row
index|[
literal|1
index|]
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
operator|(
name|Artist
operator|)
name|row
index|[
literal|1
index|]
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|row
index|[
literal|2
index|]
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
operator|(
name|Gallery
operator|)
name|row
index|[
literal|2
index|]
operator|)
operator|.
name|getGalleryName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByJoinedEntityInCount
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsPaintingGalleries
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT COUNT(p.toArtist), p.paintingTitle FROM Painting p "
operator|+
literal|"GROUP BY p.paintingTitle "
operator|+
literal|"HAVING p.paintingTitle LIKE 'P1%'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
index|[]
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
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|HashSet
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|expectedResults
init|=
operator|new
name|HashSet
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1L
argument_list|,
literal|"P1"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1L
argument_list|,
literal|"P111"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1L
argument_list|,
literal|"P112"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
index|[]
name|row
range|:
name|data
control|)
block|{
name|assertFalse
argument_list|(
name|expectedResults
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|row
index|[
literal|0
index|]
argument_list|,
name|row
index|[
literal|1
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByChainedJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createFivePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.painting.toArtist.paintingArray FROM PaintingInfo p"
operator|+
literal|" GROUP BY p.painting.toArtist.paintingArray"
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
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|ejbql
operator|=
literal|"SELECT p.painting.toArtist FROM PaintingInfo p"
operator|+
literal|" GROUP BY p.painting.toArtist"
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

