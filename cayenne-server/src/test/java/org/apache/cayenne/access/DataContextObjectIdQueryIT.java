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
name|ObjectId
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
name|ObjectIdQuery
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
name|SQLTemplate
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|DataContextObjectIdQueryIT
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefreshNullifiedValuesNew
parameter_list|()
block|{
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
literal|"X"
argument_list|)
expr_stmt|;
name|a
operator|.
name|setDateOfBirth
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"UPDATE ARTIST SET DATE_OF_BIRTH = NULL"
argument_list|)
argument_list|)
expr_stmt|;
name|long
name|id
init|=
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
name|id
argument_list|)
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a1
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a1
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
name|testNoRefreshValuesNew
parameter_list|()
block|{
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
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"UPDATE ARTIST SET ARTIST_NAME = 'Y'"
argument_list|)
argument_list|)
expr_stmt|;
name|long
name|id
init|=
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
name|id
argument_list|)
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a1
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
name|testRefreshNullifiedValuesExisting
parameter_list|()
block|{
name|SQLTemplate
name|insert
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) VALUES (44, 'X', #bind($date 'DATE'))"
argument_list|)
decl_stmt|;
name|insert
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"date"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|insert
argument_list|)
expr_stmt|;
name|Artist
name|a
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
literal|44l
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"UPDATE ARTIST SET DATE_OF_BIRTH = NULL"
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|44l
argument_list|)
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a1
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

