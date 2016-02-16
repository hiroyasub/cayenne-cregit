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
name|wocompat
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|DbRelationship
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
name|ObjAttribute
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
name|ObjEntity
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
name|ObjRelationship
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
name|QueryDescriptor
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
name|SelectQueryDescriptor
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|assertNotNull
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
name|assertNull
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
name|assertSame
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
specifier|public
class|class
name|EOModelProcessorTest
block|{
specifier|protected
name|EOModelProcessor
name|processor
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
name|processor
operator|=
operator|new
name|EOModelProcessor
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadModel
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"wotests/art.eomodeld/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
name|processor
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertLoaded
argument_list|(
literal|"art"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertLoadedQueries
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertOneWayRelationships
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertLoadedCustomTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadModelWithDependencies
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"wotests/cross-model-relationships.eomodeld/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
name|processor
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"CrossModelRelTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|ObjAttribute
name|a1
init|=
operator|(
name|ObjAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"testAttribute"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|DbAttribute
name|da1
init|=
name|a1
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|da1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|da1
argument_list|,
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"TEST_ATTRIBUTE"
argument_list|)
argument_list|)
expr_stmt|;
comment|// for now cross model relationships are simply ignored
comment|// eventually we must handle those...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"wotests/flattened.eomodeld/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// see CAY-1806
name|DataMap
name|map
init|=
name|processor
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ObjEntity
name|artistE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artistE
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artistE
operator|.
name|getRelationship
argument_list|(
literal|"exhibitArray"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artistE
operator|.
name|getRelationship
argument_list|(
literal|"artistExhibitArray"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadBrokenModel
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"art-with-errors.eomodeld/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
name|processor
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertLoaded
argument_list|(
literal|"art-with-errors"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertOneWayRelationships
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
comment|// assert that one way relationships are loaded properly
comment|// - Db loaded as two-way, obj - as one-way
name|ObjEntity
name|exhibitEntity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Exhibit"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|toTypeObject
init|=
operator|(
name|ObjRelationship
operator|)
name|exhibitEntity
operator|.
name|getRelationship
argument_list|(
literal|"toExhibitType"
argument_list|)
decl_stmt|;
name|DbRelationship
name|toTypeDB
init|=
operator|(
name|DbRelationship
operator|)
name|exhibitEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getRelationship
argument_list|(
literal|"toExhibitType"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toTypeObject
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|toTypeDB
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|toTypeObject
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|toTypeDB
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertLoadedQueries
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
comment|// queries
name|QueryDescriptor
name|query
init|=
name|map
operator|.
name|getQueryDescriptor
argument_list|(
literal|"ExhibitType_TestQuery"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QueryDescriptor
operator|.
name|SELECT_QUERY
argument_list|,
name|query
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|instanceof
name|SelectQueryDescriptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"ExhibitType"
argument_list|)
argument_list|,
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertLoadedCustomTypes
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
comment|// check obj entities
name|ObjEntity
name|customTypes
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"CustomTypes"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|customTypes
argument_list|)
expr_stmt|;
name|ObjAttribute
name|pk
init|=
operator|(
name|ObjAttribute
operator|)
name|customTypes
operator|.
name|getAttribute
argument_list|(
literal|"pk"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CustomType1"
argument_list|,
name|pk
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|other
init|=
operator|(
name|ObjAttribute
operator|)
name|customTypes
operator|.
name|getAttribute
argument_list|(
literal|"other"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CustomType2"
argument_list|,
name|other
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertLoaded
parameter_list|(
name|String
name|mapName
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mapName
argument_list|,
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// check obj entities
name|ObjEntity
name|artistE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|artistE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// check Db entities
name|DbEntity
name|artistDE
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|DbEntity
name|artistDE1
init|=
name|artistE
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|artistDE
argument_list|,
name|artistDE1
argument_list|)
expr_stmt|;
comment|// check attributes
name|ObjAttribute
name|a1
init|=
operator|(
name|ObjAttribute
operator|)
name|artistE
operator|.
name|getAttribute
argument_list|(
literal|"artistName"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|DbAttribute
name|da1
init|=
name|a1
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|da1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|da1
argument_list|,
name|artistDE
operator|.
name|getAttribute
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check ObjRelationships
name|ObjRelationship
name|rel
init|=
operator|(
name|ObjRelationship
operator|)
name|artistE
operator|.
name|getRelationship
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check DbRelationships
name|DbRelationship
name|drel
init|=
operator|(
name|DbRelationship
operator|)
name|artistDE
operator|.
name|getRelationship
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|drel
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|drel
argument_list|,
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// flattened relationships
name|ObjRelationship
name|frel
init|=
operator|(
name|ObjRelationship
operator|)
name|artistE
operator|.
name|getRelationship
argument_list|(
literal|"exhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|frel
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|frel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// storing data map may uncover some inconsistencies
name|PrintWriter
name|mockupWriter
init|=
operator|new
name|NullPrintWriter
argument_list|()
decl_stmt|;
name|map
operator|.
name|encodeAsXML
argument_list|(
operator|new
name|XMLEncoder
argument_list|(
name|mockupWriter
argument_list|)
argument_list|)
expr_stmt|;
block|}
class|class
name|NullPrintWriter
extends|extends
name|PrintWriter
block|{
specifier|public
name|NullPrintWriter
parameter_list|()
block|{
name|super
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|arg0
parameter_list|,
name|int
name|arg1
parameter_list|,
name|int
name|arg2
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|arg0
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|arg0
parameter_list|,
name|int
name|arg1
parameter_list|,
name|int
name|arg2
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

