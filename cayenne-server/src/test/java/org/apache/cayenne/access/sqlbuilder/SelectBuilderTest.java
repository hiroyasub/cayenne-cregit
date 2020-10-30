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
name|sqlbuilder
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SelectNode
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
name|DbEntity
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
name|access
operator|.
name|sqlbuilder
operator|.
name|SQLBuilder
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SelectBuilderTest
extends|extends
name|BaseSqlBuilderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSelect
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|()
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectColumns
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|table
argument_list|(
literal|"c"
argument_list|)
operator|.
name|column
argument_list|(
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a, c.b"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|assertQuotedSQL
argument_list|(
literal|"SELECT `a`, `c`.`b`"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFrom
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a FROM b"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|assertQuotedSQL
argument_list|(
literal|"SELECT `a` FROM `b`"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromDbEntity
parameter_list|()
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setSchema
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setCatalog
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
name|entity
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a FROM c.d.b"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|assertQuotedSQL
argument_list|(
literal|"SELECT `a` FROM `c`.`d`.`b`"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhere
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|where
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
literal|123
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a FROM b WHERE a = 123"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereNull
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|where
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a FROM b WHERE a IS NULL"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectFromWhereComplex
parameter_list|()
block|{
name|SelectBuilder
name|builder
init|=
operator|new
name|SelectBuilder
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|where
argument_list|(
name|column
argument_list|(
literal|"a"
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
literal|123
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|column
argument_list|(
literal|"c"
argument_list|)
operator|.
name|lt
argument_list|(
name|column
argument_list|(
literal|"d"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT a FROM b WHERE ( a = 123 ) AND ( c< d )"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexQuery
parameter_list|()
block|{
name|Node
name|node
init|=
name|select
argument_list|(
name|table
argument_list|(
literal|"a"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ARTIST_ID"
argument_list|)
operator|.
name|as
argument_list|(
literal|"a_id"
argument_list|)
argument_list|,
name|count
argument_list|(
name|table
argument_list|(
literal|"p"
argument_list|)
operator|.
name|column
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
operator|.
name|as
argument_list|(
literal|"p_count"
argument_list|)
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"ARTIST"
argument_list|)
operator|.
name|as
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|from
argument_list|(
name|leftJoin
argument_list|(
name|table
argument_list|(
literal|"PAINTING"
argument_list|)
operator|.
name|as
argument_list|(
literal|"p"
argument_list|)
argument_list|)
operator|.
name|on
argument_list|(
name|table
argument_list|(
literal|"a"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ARTIST_ID"
argument_list|)
operator|.
name|eq
argument_list|(
name|table
argument_list|(
literal|"p"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|table
argument_list|(
literal|"p"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ESTIMATED_PRICE"
argument_list|)
operator|.
name|gt
argument_list|(
name|value
argument_list|(
literal|10
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|where
argument_list|(
name|table
argument_list|(
literal|"a"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
literal|"Picasso"
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|exists
argument_list|(
name|select
argument_list|(
name|all
argument_list|()
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|(
literal|"GALLERY"
argument_list|)
operator|.
name|as
argument_list|(
literal|"g"
argument_list|)
argument_list|)
operator|.
name|where
argument_list|(
name|table
argument_list|(
literal|"g"
argument_list|)
operator|.
name|column
argument_list|(
literal|"GALLERY_ID"
argument_list|)
operator|.
name|eq
argument_list|(
name|table
argument_list|(
literal|"p"
argument_list|)
operator|.
name|column
argument_list|(
literal|"GALLERY_ID"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
name|value
argument_list|(
literal|1
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|value
argument_list|(
literal|false
argument_list|)
argument_list|)
argument_list|)
operator|.
name|groupBy
argument_list|(
name|table
argument_list|(
literal|"a"
argument_list|)
operator|.
name|column
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
operator|.
name|having
argument_list|(
name|not
argument_list|(
name|count
argument_list|(
name|table
argument_list|(
literal|"p"
argument_list|)
operator|.
name|column
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
operator|.
name|gt
argument_list|(
name|value
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|column
argument_list|(
literal|"p_count"
argument_list|)
operator|.
name|desc
argument_list|()
argument_list|,
name|column
argument_list|(
literal|"a_id"
argument_list|)
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|SelectNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"SELECT DISTINCT"
operator|+
literal|" a.ARTIST_ID a_id, COUNT( p.PAINTING_TITLE ) AS p_count"
operator|+
literal|" FROM ARTIST a"
operator|+
literal|" LEFT JOIN PAINTING p ON ( a.ARTIST_ID = p.ARTIST_ID ) AND ( p.ESTIMATED_PRICE> 10 )"
operator|+
literal|" WHERE ( ( ( a.ARTIST_NAME = 'Picasso' )"
operator|+
literal|" AND EXISTS (SELECT * FROM GALLERY g WHERE g.GALLERY_ID = p.GALLERY_ID) )"
operator|+
literal|" AND ( 1 = 1 ) ) OR false"
operator|+
literal|" GROUP BY a.ARTIST_ID"
operator|+
literal|" HAVING NOT ( COUNT( p.PAINTING_TITLE )> 3 )"
operator|+
literal|" ORDER BY p_count DESC, a_id"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

