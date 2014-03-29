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
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CapsStrategy
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
name|test
operator|.
name|file
operator|.
name|FileUtil
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
name|embeddable
operator|.
name|Embeddable1
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
name|Gallery
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_class
specifier|public
class|class
name|MapLoaderLoadTest
extends|extends
name|TestCase
block|{
specifier|private
name|InputSource
name|getMapXml
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
return|return
operator|new
name|InputSource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|mapName
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|testLoadEmbeddableMap
parameter_list|()
throws|throws
name|Exception
block|{
name|MapLoader
name|mapLoader
init|=
operator|new
name|MapLoader
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|getMapXml
argument_list|(
literal|"embeddable.map.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Embeddable
name|e
init|=
name|map
operator|.
name|getEmbeddable
argument_list|(
name|Embeddable1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Embeddable1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddableAttribute
name|ea1
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"embedded10"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ea1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"embedded10"
argument_list|,
name|ea1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|ea1
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EMBEDDED10"
argument_list|,
name|ea1
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddableAttribute
name|ea2
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"embedded20"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ea2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"embedded20"
argument_list|,
name|ea2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|ea2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EMBEDDED20"
argument_list|,
name|ea2
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|oe
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"EmbedEntity1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|oe
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|oe
operator|.
name|getDeclaredAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddedAttribute
name|oea1
init|=
operator|(
name|EmbeddedAttribute
operator|)
name|oe
operator|.
name|getAttribute
argument_list|(
literal|"embedded1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|oea1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Embeddable1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|oea1
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|oea1
operator|.
name|getAttributeOverrides
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddedAttribute
name|oea2
init|=
operator|(
name|EmbeddedAttribute
operator|)
name|oe
operator|.
name|getAttribute
argument_list|(
literal|"embedded2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|oea2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Embeddable1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|oea2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|oea2
operator|.
name|getAttributeOverrides
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadTestMap
parameter_list|()
throws|throws
name|Exception
block|{
name|MapLoader
name|mapLoader
init|=
operator|new
name|MapLoader
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|getMapXml
argument_list|(
literal|"tstmap.map.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// test procedures
name|Procedure
name|procedure
init|=
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_upd_proc"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|params
init|=
name|procedure
operator|.
name|getCallParameters
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ProcedureParameter
name|param
init|=
name|params
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"paintingPrice"
argument_list|,
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ProcedureParameter
operator|.
name|IN_PARAMETER
argument_list|,
name|param
operator|.
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
comment|// test super class name
comment|// We expect the artist entity to have a super class name... test map should be
comment|// set up in that way.
comment|// No other assertions can be made (the actual super class may change)
name|ObjEntity
name|ent
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ent
operator|.
name|getSuperClassName
argument_list|()
argument_list|)
expr_stmt|;
comment|//text exclude... parameters
name|ObjEntity
name|artistCallbackTestEntity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"ArtistCallback"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|artistCallbackTestEntity
operator|.
name|isExcludingDefaultListeners
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artistCallbackTestEntity
operator|.
name|isExcludingSuperclassListeners
argument_list|()
argument_list|)
expr_stmt|;
name|checkLoadedQueries
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEncodeAsXML
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
comment|// load map
name|MapLoader
name|mapLoader
init|=
operator|new
name|MapLoader
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|getMapXml
argument_list|(
literal|"tstmap.map.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// encode map
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|baseTestDirectory
argument_list|()
argument_list|,
literal|"testmap_generated.map.xml"
argument_list|)
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|encodeAsXML
argument_list|(
operator|new
name|XMLEncoder
argument_list|(
name|pw
argument_list|)
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|checkLoadedQueries
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|queryWithQualifier
init|=
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|map
operator|.
name|getQuery
argument_list|(
literal|"QueryWithQualifier"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|queryWithQualifier
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryWithQualifier
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|ObjEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
operator|(
operator|(
name|Entity
operator|)
name|queryWithQualifier
operator|.
name|getRoot
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|queryWithQualifier
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|queryWithOrdering
init|=
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|map
operator|.
name|getQuery
argument_list|(
literal|"QueryWithOrdering"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|queryWithOrdering
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryWithOrdering
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|ObjEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
operator|(
operator|(
name|Entity
operator|)
name|queryWithOrdering
operator|.
name|getRoot
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queryWithOrdering
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Ordering
name|artistNameOrdering
init|=
name|queryWithOrdering
operator|.
name|getOrderings
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|artistNameOrdering
operator|.
name|getSortSpecString
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistNameOrdering
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artistNameOrdering
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
name|Ordering
name|dobOrdering
init|=
name|queryWithOrdering
operator|.
name|getOrderings
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH_PROPERTY
argument_list|,
name|dobOrdering
operator|.
name|getSortSpecString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dobOrdering
operator|.
name|isAscending
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dobOrdering
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|queryWithPrefetch
init|=
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|map
operator|.
name|getQuery
argument_list|(
literal|"QueryWithPrefetch"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|queryWithPrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryWithPrefetch
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|ObjEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Gallery"
argument_list|,
operator|(
operator|(
name|Entity
operator|)
name|queryWithPrefetch
operator|.
name|getRoot
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|queryWithPrefetch
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queryWithPrefetch
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|nonPhantomNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|queryWithPrefetch
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|getNode
argument_list|(
name|Gallery
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|SQLTemplate
name|nonSelectingQuery
init|=
operator|(
name|SQLTemplate
operator|)
name|map
operator|.
name|getQuery
argument_list|(
literal|"NonSelectingQuery"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nonSelectingQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NonSelectingQuery"
argument_list|,
name|nonSelectingQuery
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|,
name|nonSelectingQuery
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|ejbqlQueryTest
init|=
operator|(
name|EJBQLQuery
operator|)
name|map
operator|.
name|getQuery
argument_list|(
literal|"EjbqlQueryTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ejbqlQueryTest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EjbqlQueryTest"
argument_list|,
name|ejbqlQueryTest
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ejbqlQueryTest
operator|.
name|getEjbqlStatement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SHARED_CACHE"
argument_list|,
name|ejbqlQueryTest
operator|.
name|getCacheStrategy
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

