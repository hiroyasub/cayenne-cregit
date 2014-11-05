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
name|reflect
operator|.
name|PersistentDescriptor
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
name|CompoundPainting
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
name|CompoundPaintingLongNames
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
name|assertNull
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
name|DataContextFlattenedAttributesIT
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
name|DBHelper
name|dbHelper
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
literal|"PAINTING1"
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
block|}
specifier|private
name|void
name|createTestDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tArtist
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|,
literal|"DATE_OF_BIRTH"
argument_list|)
expr_stmt|;
name|TableHelper
name|tPainting
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
decl_stmt|;
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
argument_list|,
literal|"GALLERY_ID"
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
name|VARCHAR
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
name|Types
operator|.
name|DECIMAL
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
name|TableHelper
name|tPaintingInfo
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING_INFO"
argument_list|)
decl_stmt|;
name|tPaintingInfo
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"TEXT_REVIEW"
argument_list|)
expr_stmt|;
name|TableHelper
name|tGallery
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"GALLERY"
argument_list|)
decl_stmt|;
name|tGallery
operator|.
name|setColumns
argument_list|(
literal|"GALLERY_ID"
argument_list|,
literal|"GALLERY_NAME"
argument_list|)
expr_stmt|;
name|long
name|dateBase
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|4
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
operator|+
literal|1
argument_list|,
literal|"artist"
operator|+
name|i
argument_list|,
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|dateBase
operator|+
literal|1000
operator|*
literal|60
operator|*
literal|60
operator|*
literal|24
operator|*
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|tGallery
operator|.
name|insert
argument_list|(
name|i
operator|+
literal|2
argument_list|,
literal|"gallery"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|galleryId
init|=
operator|(
name|i
operator|==
literal|3
operator|)
condition|?
literal|null
else|:
operator|(
name|i
operator|-
literal|1
operator|)
operator|%
literal|2
operator|+
literal|3
decl_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"painting"
operator|+
name|i
argument_list|,
operator|(
name|i
operator|-
literal|1
operator|)
operator|%
literal|4
operator|+
literal|2
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|1000d
argument_list|)
argument_list|,
name|galleryId
argument_list|)
expr_stmt|;
name|tPaintingInfo
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"painting review"
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
name|testSelectCompound1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundPainting
operator|.
name|class
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"CompoundPainting expected, got "
operator|+
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|CompoundPainting
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|CompoundPainting
name|painting
init|=
operator|(
name|CompoundPainting
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|Number
name|id
init|=
operator|(
name|Number
operator|)
name|painting
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"PAINTING_ID"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CompoundPainting.getPaintingTitle(): "
operator|+
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
literal|"painting"
operator|+
name|id
argument_list|,
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|painting
operator|.
name|getToPaintingInfo
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNull
argument_list|(
name|painting
operator|.
name|getTextReview
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"CompoundPainting.getTextReview(): "
operator|+
name|painting
operator|.
name|getTextReview
argument_list|()
argument_list|,
literal|"painting review"
operator|+
name|id
argument_list|,
name|painting
operator|.
name|getTextReview
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"CompoundPainting.getArtistName(): "
operator|+
name|painting
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|painting
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|painting
operator|.
name|getToGallery
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNull
argument_list|(
name|painting
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"CompoundPainting.getGalleryName(): "
operator|+
name|painting
operator|.
name|getGalleryName
argument_list|()
argument_list|,
name|painting
operator|.
name|getToGallery
argument_list|()
operator|.
name|getGalleryName
argument_list|()
argument_list|,
name|painting
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// TODO: andrus 1/5/2007 - CAY-952: SelectQuery uses INNER JOIN for flattened
comment|// attributes, while
comment|// EJBQLQuery does an OUTER JOIN... which seems like a better idea...
comment|// 14/01/2010 now it uses LEFT JOIN
annotation|@
name|Test
specifier|public
name|void
name|testSelectCompound2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundPainting
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist2"
argument_list|)
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
literal|"CompoundPainting expected, got "
operator|+
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|CompoundPainting
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|CompoundPainting
name|painting
init|=
operator|(
name|CompoundPainting
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|painting
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CompoundPainting.getArtistName(): "
operator|+
name|painting
operator|.
name|getArtistName
argument_list|()
argument_list|,
literal|"artist2"
argument_list|,
name|painting
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CompoundPainting.getArtistName(): "
operator|+
name|painting
operator|.
name|getGalleryName
argument_list|()
argument_list|,
name|painting
operator|.
name|getToGallery
argument_list|()
operator|.
name|getGalleryName
argument_list|()
argument_list|,
name|painting
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Emulates the situation when flattened attribute has unusual(long) name, that puts      * this attribute property to the top of PersistentDescriptor.declaredProperties map,      * {@link PersistentDescriptor}[105] That forced an error during the building of the      * SelectQuery statement, CAY-1484      */
annotation|@
name|Test
specifier|public
name|void
name|testSelectCompoundLongNames
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundPaintingLongNames
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the error was thrown on query execution
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQL
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM CompoundPainting a WHERE a.artistName = 'artist2'"
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
literal|"CompoundPainting expected, got "
operator|+
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|CompoundPainting
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CompoundPainting
name|painting
init|=
operator|(
name|CompoundPainting
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|painting
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQLCollectionTheta
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT DISTINCT a FROM CompoundPainting cp, Artist a "
operator|+
literal|"WHERE a.artistName=cp.artistName ORDER BY a.artistName"
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist"
operator|+
name|index
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQLLike
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM CompoundPainting a WHERE a.artistName LIKE 'artist%' "
operator|+
literal|"ORDER BY a.paintingTitle"
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CompoundPainting
name|painting
init|=
operator|(
name|CompoundPainting
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"painting"
operator|+
name|index
argument_list|,
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQLBetween
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM CompoundPainting a "
operator|+
literal|"WHERE a.artistName BETWEEN 'artist1' AND 'artist4' "
operator|+
literal|"ORDER BY a.paintingTitle"
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CompoundPainting
name|painting
init|=
operator|(
name|CompoundPainting
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"painting"
operator|+
name|index
argument_list|,
name|painting
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQLSubquery
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT g FROM Gallery g WHERE "
operator|+
literal|"(SELECT COUNT(cp) FROM CompoundPainting cp WHERE g.galleryName=cp.galleryName) = 4"
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|Gallery
name|gallery
init|=
operator|(
name|Gallery
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"gallery2"
argument_list|,
name|gallery
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectEJQBQLHaving
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT cp.galleryName, COUNT(a) from  Artist a, CompoundPainting cp "
operator|+
literal|"WHERE cp.artistName = a.artistName "
operator|+
literal|"GROUP BY cp.galleryName "
operator|+
literal|"HAVING cp.galleryName LIKE 'gallery1'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
index|[]
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
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|Object
index|[]
name|galleryItem
init|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"gallery1"
argument_list|,
name|galleryItem
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|galleryItem
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
name|testInsert
parameter_list|()
block|{
name|CompoundPainting
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPainting
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"A1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1.0d
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGalleryName
argument_list|(
literal|"G1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setPaintingTitle
argument_list|(
literal|"P1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTextReview
argument_list|(
literal|"T1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Number
name|artistCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Artist a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artistCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|paintingCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Painting a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|paintingCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|galleryCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Gallery a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|galleryCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDelete
parameter_list|()
throws|throws
name|Exception
block|{
comment|// throw in a bit of random overlapping data, to make sure FK/PK correspondence is
comment|// not purely coincidental
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
literal|"AX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|CompoundPainting
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPainting
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"A1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1.0d
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGalleryName
argument_list|(
literal|"G1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setPaintingTitle
argument_list|(
literal|"P1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTextReview
argument_list|(
literal|"T1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Number
name|artistCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Artist a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artistCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|paintingCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Painting a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|paintingCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|galleryCount
init|=
operator|(
name|Number
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(a) from Gallery a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|galleryCount
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpdate
parameter_list|()
block|{
name|CompoundPainting
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPainting
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"A1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1d
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGalleryName
argument_list|(
literal|"G1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setPaintingTitle
argument_list|(
literal|"P1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTextReview
argument_list|(
literal|"T1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"X1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|2d
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGalleryName
argument_list|(
literal|"X1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setPaintingTitle
argument_list|(
literal|"X1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTextReview
argument_list|(
literal|"X1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

