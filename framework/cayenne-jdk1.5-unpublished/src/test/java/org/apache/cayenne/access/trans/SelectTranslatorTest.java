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
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|art
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
name|art
operator|.
name|ArtistExhibit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|CompoundPainting
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|ExpressionException
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
name|DbAttribute
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
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Ordering
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
name|PrefetchTreeNode
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
name|Query
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|SelectTranslatorTest
extends|extends
name|CayenneCase
block|{
comment|/**      * Tests query creation with qualifier and ordering.      */
specifier|public
name|void
name|testCreateSqlString1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with qualifier and ordering
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"a%"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"dateOfBirth"
argument_list|,
name|Ordering
operator|.
name|ASC
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
operator|>
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" ORDER BY "
argument_list|)
operator|>
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests query creation with "distinct" specified.      */
specifier|public
name|void
name|testCreateSqlString2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with "distinct" set
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setDistinct
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT DISTINCT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test aliases when the same table used in more then 1 relationship. Check      * translation of relationship path "ArtistExhibit.toArtist.artistName" and      * "ArtistExhibit.toExhibit.toGallery.paintingArray.toArtist.artistName".      */
specifier|public
name|void
name|testCreateSqlString5
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with qualifier and ordering
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtistExhibit
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"toArtist.artistName"
argument_list|,
literal|"a%"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"toExhibit.toGallery.paintingArray.toArtist.artistName"
argument_list|,
literal|"a%"
argument_list|)
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// logObj.warn("Query: " + generatedSql);
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
operator|>
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that there are 2 distinct aliases for the ARTIST table
name|int
name|ind1
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST t"
argument_list|,
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ind1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|ind2
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST t"
argument_list|,
name|ind1
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ind2
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|charAt
argument_list|(
name|ind1
operator|+
literal|"ARTIST t"
operator|.
name|length
argument_list|()
argument_list|)
operator|!=
name|generatedSql
operator|.
name|charAt
argument_list|(
name|ind2
operator|+
literal|"ARTIST t"
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test aliases when the same table used in more then 1 relationship. Check      * translation of relationship path "ArtistExhibit.toArtist.artistName" and      * "ArtistExhibit.toArtist.paintingArray.paintingTitle".      */
specifier|public
name|void
name|testCreateSqlString6
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with qualifier and ordering
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ArtistExhibit
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"toArtist.artistName"
argument_list|,
literal|"a%"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"toArtist.paintingArray.paintingTitle"
argument_list|,
literal|"p%"
argument_list|)
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
operator|>
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that there is only one distinct alias for the ARTIST table
name|int
name|ind1
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST t"
argument_list|,
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ind1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|ind2
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST t"
argument_list|,
name|ind1
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ind2
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test query when qualifying on the same attribute more than once. Check translation      * "Artist.dateOfBirth> ? AND Artist.dateOfBirth< ?".      */
specifier|public
name|void
name|testCreateSqlString7
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"dateOfBirth"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
literal|"dateOfBirth"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// logObj.warn("Query: " + generatedSql);
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|i1
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i2
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i2
operator|>
name|i1
argument_list|)
expr_stmt|;
name|int
name|i3
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
name|i2
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i3
operator|>
name|i2
argument_list|)
expr_stmt|;
name|int
name|i4
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
name|i3
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No second DOB comparison: "
operator|+
name|i4
operator|+
literal|", "
operator|+
name|i3
argument_list|,
name|i4
operator|>
name|i3
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test query when qualifying on the same attribute accessed over relationship, more      * than once. Check translation "Painting.toArtist.dateOfBirth> ? AND      * Painting.toArtist.dateOfBirth< ?".      */
specifier|public
name|void
name|testCreateSqlString8
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|setRoot
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
expr_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
literal|"toArtist.dateOfBirth"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
literal|"toArtist.dateOfBirth"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|generatedSql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedSql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|i1
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" FROM "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i2
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|" WHERE "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i2
operator|>
name|i1
argument_list|)
expr_stmt|;
name|int
name|i3
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
name|i2
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i3
operator|>
name|i2
argument_list|)
expr_stmt|;
name|int
name|i4
init|=
name|generatedSql
operator|.
name|indexOf
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
name|i3
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No second DOB comparison: "
operator|+
name|i4
operator|+
literal|", "
operator|+
name|i3
argument_list|,
name|i4
operator|>
name|i3
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlString9
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query for a compound ObjEntity with qualifier
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundPainting
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"a%"
argument_list|)
argument_list|)
decl_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// do some simple assertions to make sure all parts are in
name|assertNotNull
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|" FROM "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i2
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i2
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i3
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i3
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i4
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"GALLERY"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i4
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i5
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i5
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i6
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i6
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i7
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ESTIMATED_PRICE"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i7
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i8
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"GALLERY_NAME"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i8
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i9
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i9
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i10
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"TEXT_REVIEW"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i10
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i11
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"PAINTING_ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i11
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i12
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i12
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i13
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"GALLERY_ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|i13
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlString10
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with to-many joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i2
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"FROM"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i2
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// make sure FK column is skipped
name|int
name|i3
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|i1
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i3
operator|<
literal|0
operator|||
name|i3
operator|>
name|i2
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
literal|"PAINTING_ID"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// assert we have one join
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transl
operator|.
name|dbRelList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlString11
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with joint prefetches and other joins
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.paintingTitle = 'a'"
argument_list|)
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|transl
operator|.
name|createSqlString
argument_list|()
expr_stmt|;
comment|// assert we only have one join
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transl
operator|.
name|dbRelList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlString12
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with to-one joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|transl
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sql
operator|.
name|startsWith
argument_list|(
literal|"SELECT "
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
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i1
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|i2
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"FROM"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i2
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// make sure FK column is skipped
name|int
name|i3
init|=
name|sql
operator|.
name|indexOf
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|i1
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sql
argument_list|,
name|i3
operator|<
literal|0
operator|||
name|i3
operator|>
name|i2
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
literal|"PAINTING_ID"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// assert we have one join
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transl
operator|.
name|dbRelList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlString13
parameter_list|()
throws|throws
name|Exception
block|{
comment|// query with invalid joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"invalid.invalid"
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|Template
name|test
init|=
operator|new
name|Template
argument_list|()
block|{
annotation|@
name|Override
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|transl
operator|.
name|createSqlString
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Invalid jointPrefetch must have thrown..."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
decl_stmt|;
name|test
operator|.
name|test
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests columns generated for a simple object query.      */
specifier|public
name|void
name|testBuildResultColumns1
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|SelectTranslator
name|tr
init|=
name|makeTranslator
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|List
name|columns
init|=
name|tr
operator|.
name|buildResultColumns
argument_list|()
decl_stmt|;
comment|// all DbAttributes must be included
name|DbEntity
name|entity
init|=
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|a
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|ColumnDescriptor
name|c
init|=
operator|new
name|ColumnDescriptor
argument_list|(
name|a
argument_list|,
literal|"t0"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No descriptor for "
operator|+
name|a
operator|+
literal|", columns: "
operator|+
name|columns
argument_list|,
name|columns
operator|.
name|contains
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests columns generated for an object query with joint prefetch.      */
specifier|public
name|void
name|testBuildResultColumns2
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|SelectTranslator
name|tr
init|=
name|makeTranslator
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|List
name|columns
init|=
name|tr
operator|.
name|buildResultColumns
argument_list|()
decl_stmt|;
comment|// assert root entity columns
name|DbEntity
name|entity
init|=
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|a
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|ColumnDescriptor
name|c
init|=
operator|new
name|ColumnDescriptor
argument_list|(
name|a
argument_list|,
literal|"t0"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No descriptor for "
operator|+
name|a
operator|+
literal|", columns: "
operator|+
name|columns
argument_list|,
name|columns
operator|.
name|contains
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// assert joined columns
name|DbEntity
name|joined
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|a
range|:
name|joined
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// skip ARTIST PK, it is joined from painting
if|if
condition|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|ColumnDescriptor
name|c
init|=
operator|new
name|ColumnDescriptor
argument_list|(
name|a
argument_list|,
literal|"t1"
argument_list|)
decl_stmt|;
name|c
operator|.
name|setLabel
argument_list|(
literal|"toArtist."
operator|+
name|a
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No descriptor for "
operator|+
name|a
operator|+
literal|", columns: "
operator|+
name|columns
argument_list|,
name|columns
operator|.
name|contains
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|SelectTranslator
name|makeTranslator
parameter_list|(
name|Query
name|q
parameter_list|)
throws|throws
name|Exception
block|{
name|SelectTranslator
name|translator
init|=
operator|new
name|SelectTranslator
argument_list|()
decl_stmt|;
name|translator
operator|.
name|setQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setAdapter
argument_list|(
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setEntityResolver
argument_list|(
name|getNode
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
comment|/**      * Helper class that serves as a template to streamline testing that requires an open      * connection.      */
specifier|abstract
class|class
name|Template
block|{
name|void
name|test
parameter_list|(
name|SelectQuery
name|q
parameter_list|)
throws|throws
name|Exception
block|{
name|SelectTranslator
name|transl
init|=
name|makeTranslator
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|Connection
name|c
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|transl
operator|.
name|setConnection
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|test
argument_list|(
name|transl
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
block|}
block|}
block|}
specifier|abstract
name|void
name|test
parameter_list|(
name|SelectTranslator
name|transl
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
block|}
end_class

end_unit

