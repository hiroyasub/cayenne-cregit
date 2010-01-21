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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|EntityResolver
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_class
specifier|public
class|class
name|EJBQLQueryTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testParameters
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select a FROM Artist a WHERE a.artistName = ?1 OR a.artistName = :name"
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
literal|1
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"name"
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|Map
name|parameters
init|=
name|query
operator|.
name|getNamedParameters
argument_list|()
decl_stmt|;
name|Map
name|parameters1
init|=
name|query
operator|.
name|getPositionalParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|parameters1
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y"
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheParameters
parameter_list|()
block|{
name|String
name|ejbql1
init|=
literal|"select a FROM Artist a WHERE a.artistName = ?1 OR a.artistName = :name"
decl_stmt|;
name|EJBQLQuery
name|q1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setParameter
argument_list|(
literal|"name"
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"select a FROM Artist a WHERE a.artistName = ?1 OR a.artistName = :name"
decl_stmt|;
name|EJBQLQuery
name|q2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql2
argument_list|)
decl_stmt|;
name|q2
operator|.
name|setParameter
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setParameter
argument_list|(
literal|"name"
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|EntityResolver
name|resolver
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|insertValue
argument_list|()
expr_stmt|;
name|DataContext
name|contex
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
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
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artist1
init|=
name|contex
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artist2
decl_stmt|;
try|try
block|{
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|artist2
operator|=
name|contex
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|artist1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|artist2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataRows
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|insertValue
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
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
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|artistName
init|=
operator|(
name|String
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|row
operator|instanceof
name|DataRow
argument_list|)
expr_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|createDataContext
argument_list|()
operator|.
name|objectFromDataRow
argument_list|(
literal|"Artist"
argument_list|,
operator|(
name|DataRow
operator|)
name|row
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|artistName
argument_list|,
name|artist
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|insertValue
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|Artist
name|obj
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
name|obj
operator|.
name|setArtistName
argument_list|(
literal|"a"
operator|+
name|i
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|insertPaintValue
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|Artist
name|art
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
name|art
operator|.
name|setArtistName
argument_list|(
literal|"a"
operator|+
name|i
argument_list|)
expr_stmt|;
name|Painting
name|obj
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
name|obj
operator|.
name|setToArtist
argument_list|(
name|art
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setPaintingTitle
argument_list|(
literal|"title"
operator|+
name|i
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetExpression
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
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
name|EJBQLCompiledExpression
name|parsed
init|=
name|query
operator|.
name|getExpression
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|parsed
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ejbql
argument_list|,
name|parsed
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetName
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
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
name|assertNull
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setName
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XYZ"
argument_list|,
name|query
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUniqueKeyEntity
parameter_list|()
block|{
comment|// insertValue();
name|EntityResolver
name|resolver
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
decl_stmt|;
name|EJBQLQuery
name|q1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|q2
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|q2
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadata
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
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
name|QueryMetadata
name|md
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
argument_list|,
name|md
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md
operator|.
name|getObjEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
argument_list|,
name|md
operator|.
name|getObjEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|md
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|md
operator|.
name|isRefreshingObjects
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|md
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectRelationship
parameter_list|()
block|{
name|insertPaintValue
argument_list|()
expr_stmt|;
name|DataContext
name|contex
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT p.toArtist FROM Painting p"
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
name|List
name|result
init|=
name|contex
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|ejbql2
init|=
literal|"SELECT p.toArtist, p FROM Painting p"
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
name|result2
init|=
name|contex
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
index|[
literal|1
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|ejbql3
init|=
literal|"SELECT p.toArtist, p.paintingTitle FROM Painting p"
decl_stmt|;
name|EJBQLQuery
name|query3
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql3
argument_list|)
decl_stmt|;
name|List
name|result3
init|=
name|contex
operator|.
name|performQuery
argument_list|(
name|query3
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|(
operator|(
name|Object
index|[]
operator|)
name|result3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
index|[
literal|1
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEncodeAsXML
parameter_list|()
block|{
name|String
name|ejbql
init|=
literal|"select a FROM Artist a"
decl_stmt|;
name|String
name|name
init|=
literal|"Test"
decl_stmt|;
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLEncoder
name|e
init|=
operator|new
name|XMLEncoder
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|w
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|separator
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|StringBuffer
name|s
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"<query name=\""
argument_list|)
decl_stmt|;
name|s
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"\" factory=\""
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"org.apache.cayenne.map.EjbqlBuilder"
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|getEjbqlStatement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|append
argument_list|(
literal|"<ejbql><![CDATA["
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
name|query
operator|.
name|getEjbqlStatement
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"]]></ejbql>"
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"</query>"
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|query
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|query
operator|.
name|encodeAsXML
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|w
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|s
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullParameter
parameter_list|()
block|{
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select p from Painting p WHERE p.toArtist=:x"
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
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullAndNotNullParameter
parameter_list|()
block|{
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select p from Painting p WHERE p.toArtist=:x OR p.toArtist.artistName=:b"
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
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJoinToJoined
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select g from Gallery g inner join g.paintingArray p where p.toArtist.artistName like '%a%'"
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJoinAndCount
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p where p.toGallery.galleryName LIKE '%a%' AND ("
operator|+
literal|"p.paintingTitle like '%a%' or "
operator|+
literal|"p.toArtist.artistName like '%a%'"
operator|+
literal|")"
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
comment|//    SELECT COUNT(p) from Product p where p.vsCatalog.id = 1 and
comment|//    (
comment|//    p.displayName like '%rimadyl%'
comment|//    or p.manufacturer.name like '%rimadyl%'
comment|//    or p.description like '%rimadyl%'
comment|//    or p.longdescription like '%rimadyl%'
comment|//    or p.longdescription2 like '%rimadyl%'
comment|//    or p.manufacturerPartNumber like '%rimadyl%'
comment|//    or p.partNumber like '%rimadyl%'
comment|//    )
specifier|public
name|void
name|testRelationshipWhereClause
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
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
name|a
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|Painting
name|p
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
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToArtist
argument_list|(
name|a
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
literal|"select p from Painting p where p.toArtist=:a"
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"a"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
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
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|paintings
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRelationshipWhereClause2
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Painting
operator|.
name|TO_GALLERY_PROPERTY
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select p.toArtist from Painting p where "
operator|+
name|exp
operator|.
name|toEJBQL
argument_list|(
literal|"p"
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLikeIgnoreCase
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
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
name|a
operator|.
name|setArtistName
argument_list|(
literal|"testLikeIgnoreCase"
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
literal|"select a from Artist a where upper(a.artistName) "
operator|+
literal|"like upper('%likeignore%')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

