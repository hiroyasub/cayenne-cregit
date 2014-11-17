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
name|query
operator|.
name|QueryChain
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
name|Test
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

begin_comment
comment|// TODO: renamed as it fails on DB's like Derby. See CAY-1480.
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
name|DataContextEJBQLFunctionalExpressionsIT
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
name|Test
specifier|public
name|void
name|testSIZE
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|Painting
name|p12
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p12
operator|.
name|setPaintingTitle
argument_list|(
literal|"p12"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToPaintingArray
argument_list|(
name|p12
argument_list|)
expr_stmt|;
name|Painting
name|p22
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p22
operator|.
name|setPaintingTitle
argument_list|(
literal|"p22"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToPaintingArray
argument_list|(
name|p22
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// this fails:
comment|// EJBQLQuery query = new EJBQLQuery(
comment|// "SELECT d FROM Artist d WHERE SIZE(d.paintingArray) = 2");
comment|// List<?> objects = context.performQuery(query);
comment|// assertEquals(1, objects.size());
comment|// assertTrue(objects.contains(a2));
comment|//
comment|// EJBQLQuery query2 = new EJBQLQuery(
comment|// "SELECT d FROM Artist d WHERE SIZE(d.paintingArray) = 0");
comment|// List<?> objects2 = context.performQuery(query2);
comment|// assertEquals(1, objects2.size());
comment|// assertTrue(objects2.contains(a1));
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCONCAT
parameter_list|()
block|{
name|Painting
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setPaintingTitle
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|Painting
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setPaintingTitle
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Painting a WHERE CONCAT(a.paintingTitle, a.paintingTitle) = 'a1a1'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSUBSTRING
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"12345678"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE SUBSTRING(a.artistName, 2, 3) = 'bcd'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLOWER
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ABCDEFG"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"Xabcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LOWER(a.artistName) = 'abcdefg'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUPPER
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ABCDEFG"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"Xabcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE UPPER(a.artistName) = UPPER('abcdefg')"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLENGTH
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"1234567"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"1234567890"
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"1234567890-="
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LENGTH(a.artistName)> 7"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLOCATE
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"___A___"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"_A_____"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LOCATE('A', a.artistName) = 2"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTRIM
parameter_list|()
block|{
comment|// insert via a SQL template to prevent adapter trimming and such...
name|QueryChain
name|inserts
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|inserts
operator|.
name|addQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID,ARTIST_NAME) VALUES(1, '  A')"
argument_list|)
argument_list|)
expr_stmt|;
name|inserts
operator|.
name|addQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID,ARTIST_NAME) VALUES(2, 'A  ')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|inserts
argument_list|)
expr_stmt|;
name|Artist
name|a1
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
literal|1
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
literal|2
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(a.artistName) = 'A'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(LEADING FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// this is fuzzy cause some DB trim trailing data by default
name|assertTrue
argument_list|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|||
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(TRAILING FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(BOTH FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTRIMChar
parameter_list|()
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsTrimChar
argument_list|()
condition|)
block|{
return|return;
block|}
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"XXXA"
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"AXXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM('X' FROM a.artistName) = 'A'"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(LEADING 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(TRAILING 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(BOTH 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

