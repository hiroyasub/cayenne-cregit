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
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEJBQLOrderByIT
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
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
name|createThreePaintings
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
literal|3000
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFourPaintings
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
literal|3000
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
name|tPainting
operator|.
name|insert
argument_list|(
literal|33003
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33004
argument_list|,
literal|null
argument_list|,
literal|"C"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoArtistsTwoPaintings
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
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33005
argument_list|,
literal|33001
argument_list|,
literal|"C"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33006
argument_list|,
literal|33002
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
name|testOrderByDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p ORDER BY p.paintingTitle"
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
name|results1
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
literal|3
argument_list|,
name|results1
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
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p FROM Painting p ORDER BY p.estimatedPrice"
decl_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|results2
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
name|results2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p ORDER BY p.paintingTitle ASC"
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
name|results1
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
literal|3
argument_list|,
name|results1
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
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p FROM Painting p ORDER BY p.estimatedPrice ASC"
decl_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|results2
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
name|results2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByDesc
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p ORDER BY p.paintingTitle DESC"
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
name|results1
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
literal|3
argument_list|,
name|results1
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
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p FROM Painting p ORDER BY p.estimatedPrice DESC"
decl_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|results2
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
name|results2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByQualified
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreePaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice> 1000 ORDER BY p.paintingTitle ASC"
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
name|results1
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
literal|2
argument_list|,
name|results1
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
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice> 1000 ORDER BY p.estimatedPrice ASC"
decl_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results2
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
name|results2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
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
name|results2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByMultiple
parameter_list|()
throws|throws
name|Exception
block|{
name|createFourPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p ORDER BY p.paintingTitle DESC, p.estimatedPrice DESC"
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
name|results1
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
literal|4
argument_list|,
name|results1
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
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33004
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
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
name|results1
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByPath
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoArtistsTwoPaintings
argument_list|()
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p ORDER BY p.toArtist.artistName ASC"
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
name|results1
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
literal|2
argument_list|,
name|results1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33005
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33006
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results1
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p FROM Painting p ORDER BY p.toArtist.artistName DESC"
decl_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33006
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33005
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

