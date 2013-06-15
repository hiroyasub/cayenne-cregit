begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
package|;
end_package

begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

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
name|sql
operator|.
name|Types
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
name|testdo
operator|.
name|testmap
operator|.
name|PaintingInfo
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

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneDOTestBase
extends|extends
name|ServerCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|artistName
init|=
literal|"artist with one painting"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|galleryName
init|=
literal|"my gallery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|textReview
init|=
literal|"this painting sucks..."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|paintingName
init|=
literal|"painting about nothing"
decl_stmt|;
specifier|static
specifier|final
name|byte
index|[]
name|paintingImage
init|=
operator|new
name|byte
index|[]
block|{
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|}
decl_stmt|;
annotation|@
name|Inject
specifier|protected
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
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
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
block|}
specifier|protected
name|Artist
name|newArtist
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
name|artistName
argument_list|)
expr_stmt|;
return|return
name|a1
return|;
block|}
specifier|protected
name|Painting
name|newPainting
parameter_list|()
block|{
name|Painting
name|p1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
name|paintingName
argument_list|)
expr_stmt|;
return|return
name|p1
return|;
block|}
specifier|protected
name|PaintingInfo
name|newPaintingInfo
parameter_list|()
block|{
name|PaintingInfo
name|p1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|PaintingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setTextReview
argument_list|(
name|textReview
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setImageBlob
argument_list|(
name|paintingImage
argument_list|)
expr_stmt|;
return|return
name|p1
return|;
block|}
specifier|protected
name|Painting
name|fetchPainting
parameter_list|()
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingTitle"
argument_list|,
name|paintingName
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|pts
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
operator|(
name|pts
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|Painting
operator|)
name|pts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|PaintingInfo
name|fetchPaintingInfo
parameter_list|()
block|{
comment|// we are using "LIKE" comparison, since Sybase does not allow
comment|// "=" comparisons on "text" columns
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|PaintingInfo
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"textReview"
argument_list|,
name|textReview
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|pts
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
operator|(
name|pts
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|PaintingInfo
operator|)
name|pts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

