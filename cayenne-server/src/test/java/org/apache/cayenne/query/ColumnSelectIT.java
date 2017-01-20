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
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|CayenneRuntimeException
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
name|exp
operator|.
name|Property
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
name|PostgresUnitDbAdapter
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
name|UnitDbAdapter
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
name|Ignore
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
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|countExp
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|FunctionExpressionFactory
operator|.
name|substringExp
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
name|fail
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

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
name|ColumnSelectIT
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
name|Inject
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
comment|// Format: d/m/YY
specifier|private
name|DateFormat
name|dateFormat
init|=
name|DateFormat
operator|.
name|getDateInstance
argument_list|(
name|DateFormat
operator|.
name|SHORT
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
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
name|tArtist
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
name|DATE
argument_list|)
expr_stmt|;
name|java
operator|.
name|sql
operator|.
name|Date
index|[]
name|dates
init|=
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
index|[
literal|5
index|]
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|dates
index|[
name|i
operator|-
literal|1
index|]
operator|=
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/"
operator|+
name|i
operator|+
literal|"/17"
argument_list|)
operator|.
name|getTime
argument_list|()
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
name|dates
index|[
name|i
operator|%
literal|5
index|]
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
name|tPaintings
operator|.
name|insert
argument_list|(
literal|21
argument_list|,
literal|"painting21"
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectGroupBy
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|result
init|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|count
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/1/17"
argument_list|)
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|result
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
name|testSelectSimpleHaving
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|result
init|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|count
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|having
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|eq
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/2/17"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/2/17"
argument_list|)
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|Exception
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testHavingOnNonGroupByColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|nameSubstr
init|=
name|Property
operator|.
name|create
argument_list|(
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|6
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|q
init|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|nameSubstr
argument_list|,
name|count
argument_list|)
operator|.
name|having
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|like
argument_list|(
literal|"artist%"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|q
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20L
argument_list|,
name|q
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
name|testSelectRelationshipCount
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|Long
argument_list|>
name|paintingCount
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|path
argument_list|()
argument_list|)
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|result
init|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|paintingCount
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/1/17"
argument_list|)
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|result
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
name|testSelectHavingWithExpressionAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|nameSubstr
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"name_substr"
argument_list|,
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|6
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|q
init|=
literal|null
decl_stmt|;
try|try
block|{
name|q
operator|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|nameSubstr
argument_list|,
name|count
argument_list|)
operator|.
name|having
argument_list|(
name|count
operator|.
name|gt
argument_list|(
literal|10L
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsExpressionInHaving
argument_list|()
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|q
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20L
argument_list|,
name|q
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Need to figure out a better way to handle alias / no alias case for expression in having"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testSelectHavingWithExpressionNoAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|nameSubstr
init|=
name|Property
operator|.
name|create
argument_list|(
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|6
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|q
init|=
literal|null
decl_stmt|;
try|try
block|{
name|q
operator|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|nameSubstr
argument_list|,
name|count
argument_list|)
operator|.
name|having
argument_list|(
name|count
operator|.
name|gt
argument_list|(
literal|10L
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsExpressionInHaving
argument_list|()
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|q
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20L
argument_list|,
name|q
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
name|testSelectWhereAndHaving
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|nameFirstLetter
init|=
name|Property
operator|.
name|create
argument_list|(
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|nameSubstr
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"name_substr"
argument_list|,
name|substringExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|6
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|q
init|=
literal|null
decl_stmt|;
try|try
block|{
name|q
operator|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|nameSubstr
argument_list|,
name|count
argument_list|)
operator|.
name|where
argument_list|(
name|nameFirstLetter
operator|.
name|eq
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|having
argument_list|(
name|count
operator|.
name|gt
argument_list|(
literal|10L
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsExpressionInHaving
argument_list|()
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|q
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20L
argument_list|,
name|q
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
name|testSelectRelationshipCountHaving
parameter_list|()
throws|throws
name|Exception
block|{
name|Property
argument_list|<
name|Long
argument_list|>
name|paintingCount
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|path
argument_list|()
argument_list|)
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|paintingCount
argument_list|)
operator|.
name|having
argument_list|(
name|paintingCount
operator|.
name|gt
argument_list|(
literal|4L
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsExpressionInHaving
argument_list|()
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|result
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
name|testSelectWithQuoting
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|unitDbAdapter
operator|instanceof
name|PostgresUnitDbAdapter
condition|)
block|{
comment|// we need to convert somehow all names to lowercase on postgres, so skip it for now
return|return;
block|}
name|Property
argument_list|<
name|Long
argument_list|>
name|paintingCount
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|path
argument_list|()
argument_list|)
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
argument_list|,
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|paintingCount
argument_list|)
operator|.
name|having
argument_list|(
name|paintingCount
operator|.
name|gt
argument_list|(
literal|4L
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsExpressionInHaving
argument_list|()
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
finally|finally
block|{
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|result
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectGroupByWithQuoting
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|unitDbAdapter
operator|instanceof
name|PostgresUnitDbAdapter
condition|)
block|{
comment|// we need to convert somehow all names to lowercase on postgres, so skip it for now
return|return;
block|}
name|Property
argument_list|<
name|Long
argument_list|>
name|count
init|=
name|Property
operator|.
name|create
argument_list|(
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|Object
index|[]
name|result
init|=
name|ColumnSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|columns
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
argument_list|,
name|count
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|parse
argument_list|(
literal|"1/1/17"
argument_list|)
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4L
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

