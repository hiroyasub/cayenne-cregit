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
operator|.
name|translator
operator|.
name|ejbql
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|dba
operator|.
name|DbAdapter
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
name|ejbql
operator|.
name|EJBQLCompiledExpression
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
name|ejbql
operator|.
name|EJBQLParser
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
name|ejbql
operator|.
name|EJBQLParserFactory
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
name|EJBQLQuery
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
name|Collections
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
name|EJBQLSelectTranslatorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|adapter
decl_stmt|;
specifier|private
name|SQLTemplate
name|translateSelect
parameter_list|(
name|String
name|ejbql
parameter_list|)
block|{
return|return
name|translateSelect
argument_list|(
name|ejbql
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
return|;
block|}
specifier|private
name|SQLTemplate
name|translateSelect
parameter_list|(
name|String
name|ejbql
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|queryParameters
parameter_list|)
block|{
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
name|ejbql
argument_list|,
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|getPositionalParameters
parameter_list|()
block|{
return|return
name|queryParameters
return|;
block|}
block|}
decl_stmt|;
name|EJBQLTranslationContext
name|tr
init|=
operator|new
name|EJBQLTranslationContext
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
name|query
argument_list|,
name|select
argument_list|,
operator|new
name|JdbcEJBQLTranslatorFactory
argument_list|()
argument_list|,
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|select
operator|.
name|getExpression
argument_list|()
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLSelectTranslator
argument_list|(
name|tr
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|tr
operator|.
name|getQuery
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFrom
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
comment|// column order is unpredictable, just need to ensure that they are all
comment|// there
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"t0.ARTIST_ID"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"t0.ARTIST_NAME"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"t0.DATE_OF_BIRTH"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|" FROM ARTIST t0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectMultipleJoinsToTheSameTable
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"SELECT a "
operator|+
literal|"FROM Artist a JOIN a.paintingArray b JOIN a.paintingArray c "
operator|+
literal|"WHERE b.paintingTitle = 'P1' AND c.paintingTitle = 'P2'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"INNER JOIN PAINTING t1 ON (t0.ARTIST_ID = t1.ARTIST_ID)"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"INNER JOIN PAINTING t2 ON (t0.ARTIST_ID = t2.ARTIST_ID)"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectImplicitColumnJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"SELECT a.paintingArray.toGallery.galleryName "
operator|+
literal|"FROM Artist a JOIN a.paintingArray b"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that overlapping implicit and explicit joins did not result in
comment|// duplicates
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|contains
argument_list|(
literal|"INNER JOIN GALLERY"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|contains
argument_list|(
literal|"INNER JOIN PAINTING"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|i1
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"INNER JOIN PAINTING"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i1
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// TODO: andrus 1/6/2008 - this fails
comment|// int i2 = sql.indexOf("INNER JOIN PAINTING", i1 + 1);
comment|// assertTrue(sql, i2< 0);
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectDistinct
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select distinct a from Artist a"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT DISTINCT "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = 'Dali'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|" FROM ARTIST t0 WHERE t0.ARTIST_NAME ="
operator|+
literal|" #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereOrEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = 'Dali' "
operator|+
literal|"or a.artistName = 'Malevich'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|SQLTemplate
name|query1
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = 'Picasso' "
operator|+
literal|"or a.artistName = 'Malevich' "
operator|+
literal|"or a.artistName = 'Dali'"
argument_list|)
decl_stmt|;
name|String
name|sql1
init|=
name|query1
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|" FROM ARTIST t0 WHERE "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countDelimiters
argument_list|(
name|sql
argument_list|,
literal|" OR "
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql1
argument_list|,
name|sql1
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql1
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|" FROM ARTIST t0 WHERE "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|countDelimiters
argument_list|(
name|sql1
argument_list|,
literal|" OR "
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereAndEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = 'Dali' "
operator|+
literal|"and a.artistName = 'Malevich'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|SQLTemplate
name|query1
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = 'Picasso' "
operator|+
literal|"and a.artistName = 'Malevich' "
operator|+
literal|"and a.artistName = 'Dali'"
argument_list|)
decl_stmt|;
name|String
name|sql1
init|=
name|query1
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countDelimiters
argument_list|(
name|sql
argument_list|,
literal|" AND "
argument_list|,
name|sql
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql1
argument_list|,
name|sql1
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql1
argument_list|,
name|sql1
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|countDelimiters
argument_list|(
name|sql1
argument_list|,
literal|" AND "
argument_list|,
name|sql1
operator|.
name|indexOf
argument_list|(
literal|"WHERE "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNot
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where not (a.artistName = 'Dali')"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE NOT "
operator|+
literal|"t0.ARTIST_NAME = #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereGreater
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice> 1.0"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE> #bind($id0 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereGreaterOrEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice>= 2"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE>= #bind($id0 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereLess
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice< 1.0"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE< #bind($id0 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereLessOrEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice<= 1.0"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE<= #bind($id0 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNotEqual
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName<> 'Dali'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ARTIST_NAME<> #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereBetween
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice between 3 and 5"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE "
operator|+
literal|"BETWEEN #bind($id0 'INTEGER') AND #bind($id1 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNotBetween
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.estimatedPrice not between 3 and 5"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.ESTIMATED_PRICE "
operator|+
literal|"NOT BETWEEN #bind($id0 'INTEGER') AND #bind($id1 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereLike
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.paintingTitle like 'Stuff'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.PAINTING_TITLE "
operator|+
literal|"LIKE #bind('Stuff' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNotLike
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p from Painting p where p.paintingTitle NOT like 'Stuff'"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"WHERE t0.PAINTING_TITLE "
operator|+
literal|"NOT LIKE #bind('Stuff' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectPositionalParameters
parameter_list|()
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = ?1 or a.artistName = ?2"
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"t0.ARTIST_NAME = #bind($id0) OR t0.ARTIST_NAME = #bind($id1)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMax
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select max(p.estimatedPrice) from Painting p"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
operator|+
literal|"#result('MAX(t0.ESTIMATED_PRICE)' 'java.math.BigDecimal' 'sc0') "
operator|+
literal|"FROM PAINTING t0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistinctSum
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select sum( distinct p.estimatedPrice) from Painting p"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT #result('SUM(DISTINCT t0.ESTIMATED_PRICE)' 'java.math.BigDecimal' 'sc0') "
operator|+
literal|"FROM PAINTING t0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnPaths
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select p.estimatedPrice, p.toArtist.artistName from Painting p"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
name|query
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
operator|+
literal|"#result('t0.ESTIMATED_PRICE' 'java.math.BigDecimal' 'sc0' 'sc0' 3), "
operator|+
literal|"#result('t1.ARTIST_NAME' 'java.lang.String' 'sc1' 'sc1' 12) FROM"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|countDelimiters
parameter_list|(
name|String
name|string
parameter_list|,
name|String
name|delim
parameter_list|,
name|int
name|fromIndex
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|fromIndex
operator|=
name|string
operator|.
name|indexOf
argument_list|(
name|delim
argument_list|,
name|fromIndex
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|fromIndex
operator|+=
name|delim
operator|.
name|length
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
comment|// if parameter value is null (in this test x := null) we will generate
comment|// "IS NULL"
annotation|@
name|Test
specifier|public
name|void
name|testEqualsNullParameter
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select p from Painting p WHERE p.toArtist=:x"
decl_stmt|;
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
name|ejbql
argument_list|,
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|EJBQLTranslationContext
name|tr
init|=
operator|new
name|EJBQLTranslationContext
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
name|query
argument_list|,
name|select
argument_list|,
operator|new
name|JdbcEJBQLTranslatorFactory
argument_list|()
argument_list|,
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|select
operator|.
name|getExpression
argument_list|()
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLSelectTranslator
argument_list|(
name|tr
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|tr
operator|.
name|getQuery
argument_list|()
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"t0.ARTIST_ID IS NULL"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// if parameter value is null and more than one parameter in query
annotation|@
name|Test
specifier|public
name|void
name|testEqualsNullAndNotNullParameter
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select p from Painting p WHERE p.toArtist=:x OR p.toArtist.artistName=:b"
decl_stmt|;
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
name|ejbql
argument_list|,
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"b"
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|EJBQLTranslationContext
name|tr
init|=
operator|new
name|EJBQLTranslationContext
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
name|query
argument_list|,
name|select
argument_list|,
operator|new
name|JdbcEJBQLTranslatorFactory
argument_list|()
argument_list|,
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|select
operator|.
name|getExpression
argument_list|()
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLSelectTranslator
argument_list|(
name|tr
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|tr
operator|.
name|getQuery
argument_list|()
operator|.
name|getDefaultTemplate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|"t0.ARTIST_ID IS NULL OR t1.ARTIST_NAME = #bind($id0)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

