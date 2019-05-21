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
name|Test
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
name|DataContextPrefetchQualifierOverlapIT
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
specifier|private
name|void
name|createTwoArtistsThreePaintingsDataSet
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
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"A1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"A2"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"ABC"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"ABD"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"ACC"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToManyDisjointOverlappingQualifierWithInnerJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsThreePaintingsDataSet
argument_list|()
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
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"AB%"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|result
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|result
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
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToManyJointOverlappingQualifierWithInnerJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsThreePaintingsDataSet
argument_list|()
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
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|,
literal|"AB%"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|result
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|result
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
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToManyJointOverlappingQualifierWithOuterJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsThreePaintingsDataSet
argument_list|()
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
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"paintingArray+.paintingTitle"
argument_list|,
literal|"AB%"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|orQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"A%"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|result
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|result
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
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

