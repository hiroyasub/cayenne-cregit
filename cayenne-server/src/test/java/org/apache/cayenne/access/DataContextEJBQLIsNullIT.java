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
name|Persistent
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
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEJBQLIsNullIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
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
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
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
name|BIGINT
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
specifier|private
name|void
name|createTwoPaintings
parameter_list|()
throws|throws
name|Exception
block|{
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|null
argument_list|,
literal|"A"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|null
argument_list|,
literal|"B"
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoPaintingsAndOneArtist
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|null
argument_list|,
literal|"A"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|33001
argument_list|,
literal|"C"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareToNull
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the query below can blow up on FrontBase. See CAY-819 for details.
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice = :x"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// unlike SelectQuery or SQLTemplate, EJBQL nulls are handled just like SQL.
comment|// note that some databases (notably Sybase) actually allow = NULL comparison,
comment|// most do not; per JPA spec the result is undefined.. so we can't make any
comment|// assertions about the result. Just making sure the query doesn't blow up
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareToNull2
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist.artistName = :x"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareToNull3
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|createTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE :x = p.toArtist.artistName"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice IS NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33001
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice IS NOT NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33002
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToOneIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoPaintingsAndOneArtist
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist IS NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33001
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToOneIsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoPaintingsAndOneArtist
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist IS NOT NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33003
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

