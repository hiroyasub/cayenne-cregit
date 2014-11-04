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
name|sql
operator|.
name|Timestamp
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
name|DataContextJoinAliasesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tExhibit
decl_stmt|;
specifier|protected
name|TableHelper
name|tGallery
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtistExhibit
decl_stmt|;
annotation|@
name|Override
specifier|public
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
name|tExhibit
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EXHIBIT"
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|setColumns
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|"GALLERY_ID"
argument_list|,
literal|"OPENING_DATE"
argument_list|,
literal|"CLOSING_DATE"
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
name|tArtistExhibit
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|setColumns
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createMatchAllDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"Picasso"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"Dali"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"G1"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"G2"
argument_list|)
expr_stmt|;
name|tGallery
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"G3"
argument_list|)
expr_stmt|;
name|Timestamp
name|now
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|1
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tExhibit
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|3
argument_list|,
name|now
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|tArtistExhibit
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchAll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// select all galleries that have exhibits by both Picasso and Dali...
name|createMatchAllDataSet
argument_list|()
expr_stmt|;
name|Artist
name|picasso
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
literal|1
argument_list|)
decl_stmt|;
name|Artist
name|dali
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
literal|2
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchAllExp
argument_list|(
literal|"|exhibitArray.artistExhibitArray.toArtist"
argument_list|,
name|picasso
argument_list|,
name|dali
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|galleries
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
name|galleries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"G1"
argument_list|,
name|galleries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGalleryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

