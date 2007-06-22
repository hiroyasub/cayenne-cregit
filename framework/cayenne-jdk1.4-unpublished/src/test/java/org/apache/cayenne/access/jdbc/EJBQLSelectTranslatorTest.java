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
operator|.
name|jdbc
package|;
end_package

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
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|EJBQLSelectTranslatorTest
extends|extends
name|CayenneCase
block|{
specifier|private
name|SQLTemplate
name|translateSelect
parameter_list|(
name|String
name|ejbql
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
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|EJBQLTranslationContext
name|tr
init|=
operator|new
name|EJBQLTranslationContext
argument_list|(
name|select
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
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
comment|// column order is unpredictable, just need to ensure that they are all there
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" FROM ARTIST AS t0${marker0}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"SELECT "
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
literal|"INNER JOIN PAINTING AS t1 ON (t0.ARTIST_ID = t1.ARTIST_ID)"
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
literal|"INNER JOIN PAINTING AS t2 ON (t0.ARTIST_ID = t2.ARTIST_ID)"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
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
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|sql
operator|.
name|endsWith
argument_list|(
literal|" FROM ARTIST AS t0${marker0}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"SELECT "
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
literal|" FROM ARTIST AS t0${marker0} WHERE t0.ARTIST_NAME ="
operator|+
literal|" #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"SELECT "
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
literal|" FROM ARTIST AS t0${marker0} WHERE "
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
literal|"SELECT "
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
literal|" FROM ARTIST AS t0${marker0} WHERE "
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
literal|"SELECT "
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
literal|" WHERE "
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
literal|"SELECT "
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
literal|" WHERE "
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
literal|"SELECT "
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
literal|" WHERE NOT "
operator|+
literal|"t0.ARTIST_NAME = #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE> #bind($id1 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE>= #bind($id1 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE< #bind($id1 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE<= #bind($id1 'DECIMAL')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ARTIST_NAME<> #bind('Dali' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE "
operator|+
literal|"BETWEEN #bind($id1 'INTEGER') AND #bind($id2 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.ESTIMATED_PRICE "
operator|+
literal|"NOT BETWEEN #bind($id1 'INTEGER') AND #bind($id2 'INTEGER')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.PAINTING_TITLE "
operator|+
literal|"LIKE #bind('Stuff' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" WHERE t0.PAINTING_TITLE "
operator|+
literal|"NOT LIKE #bind('Stuff' 'VARCHAR')"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectPositionalParameters
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
name|translateSelect
argument_list|(
literal|"select a from Artist a where a.artistName = ?1 or a.artistName = ?2"
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
literal|"t0.ARTIST_NAME = #bind($id1) OR t0.ARTIST_NAME = #bind($id2)"
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
block|}
end_class

end_unit

