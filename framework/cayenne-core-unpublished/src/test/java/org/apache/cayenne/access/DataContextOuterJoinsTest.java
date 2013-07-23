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
name|Expression
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
name|map
operator|.
name|Entity
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
name|query
operator|.
name|SortOrder
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
name|DataContextOuterJoinsTest
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
name|artistHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|paintingHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|artgroupHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|artistGroupHelper
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
name|artistHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|,
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING_TITLE"
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
name|VARCHAR
argument_list|)
expr_stmt|;
name|artgroupHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTGROUP"
argument_list|,
literal|"GROUP_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|artistGroupHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST_GROUP"
argument_list|,
literal|"GROUP_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
expr_stmt|;
name|artistGroupHelper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|dbHelper
operator|.
name|update
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|set
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|NULL
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|artgroupHelper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|paintingHelper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|artistHelper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectWithOuterJoinFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"BB1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"BB2"
argument_list|)
expr_stmt|;
name|artgroupHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"G1"
argument_list|)
expr_stmt|;
name|artistGroupHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|33001
argument_list|)
expr_stmt|;
name|artistGroupHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|33002
argument_list|)
expr_stmt|;
name|artistGroupHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|33004
argument_list|)
expr_stmt|;
name|SelectQuery
name|missingToManyQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|missingToManyQuery
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|GROUP_ARRAY_PROPERTY
operator|+
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|missingToManyQuery
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|missingToManyQuery
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
literal|"BB1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectWithOuterJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"BB1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"BB2"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|33002
argument_list|,
literal|"P2"
argument_list|)
expr_stmt|;
name|SelectQuery
name|missingToManyQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|missingToManyQuery
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
operator|+
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|missingToManyQuery
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|missingToManyQuery
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
name|assertEquals
argument_list|(
literal|"BB1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|mixedConditionQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|mixedConditionQuery
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
operator|+
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|mixedConditionQuery
operator|.
name|orQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"AA1"
argument_list|)
argument_list|)
expr_stmt|;
name|mixedConditionQuery
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|artists
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|mixedConditionQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AA1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BB1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BB2"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectWithOuterJoinFromString
parameter_list|()
throws|throws
name|Exception
block|{
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|"BB1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|"BB2"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|33002
argument_list|,
literal|"P2"
argument_list|)
expr_stmt|;
name|SelectQuery
name|missingToManyQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|missingToManyQuery
operator|.
name|andQualifier
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray+ = null"
argument_list|)
argument_list|)
expr_stmt|;
name|missingToManyQuery
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|missingToManyQuery
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
name|assertEquals
argument_list|(
literal|"BB1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|mixedConditionQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|mixedConditionQuery
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
operator|+
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|mixedConditionQuery
operator|.
name|orQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"AA1"
argument_list|)
argument_list|)
expr_stmt|;
name|mixedConditionQuery
operator|.
name|addOrdering
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|artists
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|mixedConditionQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AA1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BB1"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BB2"
argument_list|,
name|artists
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectWithOuterOrdering
parameter_list|()
throws|throws
name|Exception
block|{
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"AA1"
argument_list|)
expr_stmt|;
name|artistHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"AA2"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|33002
argument_list|,
literal|"P2"
argument_list|)
expr_stmt|;
name|paintingHelper
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|null
argument_list|,
literal|"P3"
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
literal|"toArtist+.artistName"
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|paintings
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
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
