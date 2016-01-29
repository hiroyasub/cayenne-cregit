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
name|ResultBatchIterator
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
name|ResultIterator
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
name|ResultIteratorCallback
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
name|HashMap
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
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|SQLSelectIT
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
name|TableHelper
name|tPainting
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
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
name|VARCHAR
argument_list|,
name|Types
operator|.
name|DECIMAL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createPaintingsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|10000.
operator|*
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_DataMapNameRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
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
literal|"SELECT * FROM PAINTING"
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
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_DefaultRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
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
literal|"SELECT * FROM PAINTING"
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
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_ClassRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Painting
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
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
name|Painting
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
name|Painting
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_ClassRoot_Parameters
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Painting
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
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
name|Painting
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
literal|"painting3"
argument_list|,
name|a
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
name|test_DataRows_ClassRoot_Bind
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Painting
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING WHERE PAINTING_TITLE = #bind($a) OR PAINTING_TITLE = #bind($b)"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
decl_stmt|;
name|q1
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
argument_list|)
operator|.
name|params
argument_list|(
literal|"b"
argument_list|,
literal|"painting4"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Painting
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
annotation|@
name|Test
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
literal|"SELECT * FROM PAINTING WHERE PAINTING_TITLE = 'painting2'"
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
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_FetchLimit
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
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
literal|"SELECT * FROM PAINTING"
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
annotation|@
name|Test
specifier|public
name|void
name|test_DataRows_FetchOffset
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
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
literal|"SELECT * FROM PAINTING"
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
annotation|@
name|Test
specifier|public
name|void
name|test_Append
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Painting
argument_list|>
name|q1
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING"
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
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
annotation|@
name|Test
specifier|public
name|void
name|test_Select
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|result
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
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
annotation|@
name|Test
specifier|public
name|void
name|test_SelectOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|Painting
name|a
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"painting3"
argument_list|,
name|a
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
name|test_SelectFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|Painting
name|p
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING ORDER BY PAINTING_TITLE"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"painting1"
argument_list|,
name|p
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
name|test_SelectFirstByContext
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|SQLSelect
argument_list|<
name|Painting
argument_list|>
name|q
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING ORDER BY PAINTING_TITLE"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|context
operator|.
name|selectFirst
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"painting1"
argument_list|,
name|p
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
name|test_Iterate
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
specifier|final
name|int
index|[]
name|count
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
operator|.
name|iterate
argument_list|(
name|context
argument_list|,
operator|new
name|ResultIteratorCallback
argument_list|<
name|Painting
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|next
parameter_list|(
name|Painting
name|object
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|object
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|count
index|[
literal|0
index|]
operator|++
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_Iterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
try|try
init|(
name|ResultIterator
argument_list|<
name|Painting
argument_list|>
name|it
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
operator|.
name|iterator
argument_list|(
name|context
argument_list|)
init|;
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Painting
name|p
range|:
name|it
control|)
block|{
name|count
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_BatchIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
try|try
init|(
name|ResultBatchIterator
argument_list|<
name|Painting
argument_list|>
name|it
init|=
name|SQLSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"SELECT * FROM PAINTING"
argument_list|)
operator|.
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
operator|.
name|batchIterator
argument_list|(
name|context
argument_list|,
literal|5
argument_list|)
init|;
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Painting
argument_list|>
name|paintingList
range|:
name|it
control|)
block|{
name|count
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|paintingList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectLong
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|long
name|id
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
operator|.
name|params
argument_list|(
literal|"a"
argument_list|,
literal|"painting3"
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
annotation|@
name|Test
specifier|public
name|void
name|test_SelectLongArray
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ids
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING ORDER BY PAINTING_ID"
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
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectCount
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
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
literal|"SELECT #result('COUNT(*)' 'int') FROM PAINTING"
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
annotation|@
name|Test
specifier|public
name|void
name|test_ParamsArray_Single
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|Integer
name|id
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING WHERE PAINTING_TITLE = #bind($a)"
argument_list|)
operator|.
name|paramsArray
argument_list|(
literal|"painting3"
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
name|test_ParamsArray_Multiple
parameter_list|()
throws|throws
name|Exception
block|{
name|createPaintingsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ids
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING WHERE PAINTING_TITLE = #bind($a) OR PAINTING_TITLE = #bind($b) ORDER BY PAINTING_ID"
argument_list|)
operator|.
name|paramsArray
argument_list|(
literal|"painting3"
argument_list|,
literal|"painting2"
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2l
argument_list|,
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3l
argument_list|,
name|ids
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|test_ParamsArray_Multiple_OptionalChunks
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"painting1"
argument_list|,
literal|1.0
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"painting2"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ids
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING #chain('OR' 'WHERE') "
operator|+
literal|"#chunk($a) ESTIMATED_PRICE #bindEqual($a) #end "
operator|+
literal|"#chunk($b) PAINTING_TITLE #bindEqual($b) #end #end ORDER BY PAINTING_ID"
argument_list|)
operator|.
name|paramsArray
argument_list|(
literal|null
argument_list|,
literal|"painting1"
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
name|ids
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_Params_Multiple_OptionalChunks
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"painting1"
argument_list|,
literal|1.0
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"painting2"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"painting1"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ids
init|=
name|SQLSelect
operator|.
name|scalarQuery
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"SELECT PAINTING_ID FROM PAINTING #chain('OR' 'WHERE') "
operator|+
literal|"#chunk($a) ESTIMATED_PRICE #bindEqual($a) #end "
operator|+
literal|"#chunk($b) PAINTING_TITLE #bindEqual($b) #end #end ORDER BY PAINTING_ID"
argument_list|)
operator|.
name|params
argument_list|(
name|params
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
name|ids
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

