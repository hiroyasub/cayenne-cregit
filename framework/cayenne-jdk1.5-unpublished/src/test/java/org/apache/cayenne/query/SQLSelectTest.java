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
name|query
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
name|SQLSelectTest
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
specifier|protected
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
block|}
specifier|public
name|void
name|test_DataRows_DataMapNameRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"testmap"
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|context
operator|.
name|select
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataRow
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_DefaultRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|context
operator|.
name|select
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataRow
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_ClassRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|isFetchingDataRows
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
name|select
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_ClassRoot_Parameters
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = #bind($a)"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|getParameters
argument_list|()
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_ClassRoot_Bind
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = #bind($a) OR ARTIST_NAME = #bind($b)"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|bind
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
argument_list|)
operator|.
name|bind
argument_list|(
literal|"b"
argument_list|,
literal|"artist4"
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
name|select
argument_list|(
name|q1
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
block|}
specifier|public
name|void
name|test_DataRows_ColumnNameCaps
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = 'artist2'"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|upperColumnNames
argument_list|()
expr_stmt|;
name|SQLTemplate
name|r1
init|=
operator|(
name|SQLTemplate
operator|)
name|q1
operator|.
name|getReplacementQuery
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|,
name|r1
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
name|q1
operator|.
name|lowerColumnNames
argument_list|()
expr_stmt|;
name|SQLTemplate
name|r2
init|=
operator|(
name|SQLTemplate
operator|)
name|q1
operator|.
name|getReplacementQuery
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CapsStrategy
operator|.
name|LOWER
argument_list|,
name|r2
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_FetchLimit
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|limit
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|context
operator|.
name|select
argument_list|(
name|q1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_DataRows_FetchOffset
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|dataRowQuery
argument_list|(
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|offset
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|context
operator|.
name|select
argument_list|(
name|q1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_Append
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE ARTIST_NAME = #bind($a)"
argument_list|)
operator|.
name|bind
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|result
init|=
name|context
operator|.
name|select
argument_list|(
name|q1
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
block|}
specifier|public
name|void
name|test_Select
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|result
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = #bind($a)"
argument_list|)
operator|.
name|bind
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
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
block|}
specifier|public
name|void
name|test_SelectOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|Artist
name|a
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = #bind($a)"
argument_list|)
operator|.
name|bind
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist3"
argument_list|,
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_SelectLong
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|long
name|id
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|"SELECT ARTIST_ID FROM ARTIST WHERE ARTIST_NAME = #bind($a)"
argument_list|)
operator|.
name|bind
argument_list|(
literal|"a"
argument_list|,
literal|"artist3"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3l
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_SelectLongArray
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|ids
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|"SELECT ARTIST_ID FROM ARTIST ORDER BY ARTIST_ID"
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|ids
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2l
argument_list|,
name|ids
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test_SelectCount
parameter_list|()
throws|throws
name|Exception
block|{
name|createArtistsDataSet
argument_list|()
expr_stmt|;
name|int
name|c
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT COUNT(*) FROM ARTIST"
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

