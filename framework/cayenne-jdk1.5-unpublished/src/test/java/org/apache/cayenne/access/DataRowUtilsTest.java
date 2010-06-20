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
name|DataRow
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
name|reflect
operator|.
name|ArcProperty
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
name|ClassDescriptor
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
name|DataRowUtilsTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
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
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createOneArtist
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
block|}
specifier|protected
name|void
name|createOneArtistAndOnePainting
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"p_artist2"
argument_list|,
literal|11
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMerge
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|String
name|n1
init|=
literal|"changed"
decl_stmt|;
name|String
name|n2
init|=
literal|"changed again"
decl_stmt|;
name|SelectQuery
name|artistQ
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|artistQ
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
name|n1
argument_list|)
expr_stmt|;
name|DataRow
name|s2
init|=
operator|new
name|DataRow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|s2
operator|.
name|put
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
name|n2
argument_list|)
expr_stmt|;
name|s2
operator|.
name|put
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|ClassDescriptor
name|d
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DataRowUtils
operator|.
name|mergeObjectWithSnapshot
argument_list|(
name|context
argument_list|,
name|d
argument_list|,
name|a1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
comment|// name was modified, so it should not change during merge
name|assertEquals
argument_list|(
name|n1
argument_list|,
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// date of birth came from database, it should be updated during merge
name|assertEquals
argument_list|(
name|s2
operator|.
name|get
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|,
name|a1
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsToOneTargetModified
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtist
argument_list|()
expr_stmt|;
name|ClassDescriptor
name|d
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|ArcProperty
name|toArtist
init|=
operator|(
name|ArcProperty
operator|)
name|d
operator|.
name|getProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|Artist
name|artist2
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|painting
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
name|painting
operator|.
name|setPaintingTitle
argument_list|(
literal|"PX"
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
name|artist2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|119
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"artist3"
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|artist3
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|artist3
argument_list|,
name|painting
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectDiff
name|diff
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerDiff
argument_list|(
name|painting
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|DataRowUtils
operator|.
name|isToOneTargetModified
argument_list|(
name|toArtist
argument_list|,
name|painting
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
name|artist3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DataRowUtils
operator|.
name|isToOneTargetModified
argument_list|(
name|toArtist
argument_list|,
name|painting
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsToOneTargetModifiedWithNewTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneArtistAndOnePainting
argument_list|()
expr_stmt|;
comment|// add NEW gallery to painting
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
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
name|Painting
name|p1
init|=
name|paintings
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ClassDescriptor
name|d
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|ArcProperty
name|toGallery
init|=
operator|(
name|ArcProperty
operator|)
name|d
operator|.
name|getProperty
argument_list|(
literal|"toGallery"
argument_list|)
decl_stmt|;
name|ObjectDiff
name|diff
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registerDiff
argument_list|(
name|p1
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|DataRowUtils
operator|.
name|isToOneTargetModified
argument_list|(
name|toGallery
argument_list|,
name|p1
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
name|Gallery
name|g1
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|g1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DataRowUtils
operator|.
name|isToOneTargetModified
argument_list|(
name|toGallery
argument_list|,
name|p1
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

