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
name|exp
package|;
end_package

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
name|ObjEntity
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
name|TranslateExpressionIT
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
name|Before
specifier|public
name|void
name|createArtistsDataSet
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
literal|20
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
literal|10000
operator|*
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|tGallery
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"tate modern"
argument_list|)
expr_stmt|;
name|TableHelper
name|tPaintings
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|tPaintings
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"GALLERY_ID"
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|tPaintings
operator|.
name|insert
argument_list|(
name|i
argument_list|,
literal|"painting"
operator|+
name|i
argument_list|,
name|i
operator|%
literal|5
operator|+
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchWithTranslatedExp
parameter_list|()
block|{
name|List
argument_list|<
name|Painting
argument_list|>
name|result
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
name|where
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|dot
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
argument_list|)
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|like
argument_list|(
literal|"painting7"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting2"
argument_list|)
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|disjoint
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getToArtist
argument_list|()
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
name|testPrefetchWithTheSamePrefetchAndQualifier
parameter_list|()
block|{
name|List
argument_list|<
name|Painting
argument_list|>
name|result
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
name|where
argument_list|(
name|Painting
operator|.
name|TO_GALLERY
operator|.
name|dot
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
argument_list|)
operator|.
name|dot
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
argument_list|)
operator|.
name|eq
argument_list|(
literal|"painting1"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|Painting
operator|.
name|PAINTING_TITLE
operator|.
name|like
argument_list|(
literal|"painting2"
argument_list|)
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Painting
operator|.
name|TO_GALLERY
operator|.
name|disjoint
argument_list|()
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Painting
operator|.
name|TO_GALLERY
operator|.
name|dot
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY
argument_list|)
operator|.
name|disjoint
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"painting2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTranslateExpression
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"toArtist.paintingArray"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"paintingArray.toArtist.paintingArray"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipPathEqualsToInput
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"paintingArray.toArtist"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipNoneLeadingParts
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"toGallery"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"paintingArray.toGallery"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipSomeLeadingParts
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"toGallery"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"toArtist.paintingArray.toGallery"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"paintingArray.toArtist.paintingArray.toGallery"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompQualifier
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"toArtist.artistExhibitArray.toExhibit"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"toGallery"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"paintingArray.toArtist.artistExhibitArray.toExhibit"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompQualifierAndPref
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
literal|"paintingArray.toGallery"
argument_list|)
decl_stmt|;
name|Expression
name|translatedExpression
init|=
name|entity
operator|.
name|translateToRelatedEntity
argument_list|(
name|expression
argument_list|,
literal|"artistExhibitArray.toExhibit"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"artistExhibitArray.toArtist.paintingArray.toGallery"
argument_list|)
argument_list|,
name|translatedExpression
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

