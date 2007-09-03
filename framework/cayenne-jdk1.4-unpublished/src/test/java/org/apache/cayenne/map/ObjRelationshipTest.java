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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|Util
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
name|ObjRelationshipTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DbEntity
name|artistDBEntity
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
specifier|protected
name|DbEntity
name|artistExhibitDBEntity
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
decl_stmt|;
specifier|protected
name|DbEntity
name|exhibitDBEntity
init|=
name|getDbEntity
argument_list|(
literal|"EXHIBIT"
argument_list|)
decl_stmt|;
specifier|protected
name|DbEntity
name|paintingDbEntity
init|=
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
specifier|protected
name|DbEntity
name|galleryDBEntity
init|=
name|getDbEntity
argument_list|(
literal|"GALLERY"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|testEncodeAsXML
parameter_list|()
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|XMLEncoder
name|encoder
init|=
operator|new
name|XMLEncoder
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"M"
argument_list|)
decl_stmt|;
name|ObjEntity
name|source
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"S"
argument_list|)
decl_stmt|;
name|ObjEntity
name|target
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"T"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|r
operator|.
name|setSourceEntity
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|r
operator|.
name|setTargetEntityName
argument_list|(
literal|"T"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setCollectionType
argument_list|(
literal|"java.util.Map"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setMapKey
argument_list|(
literal|"bla"
argument_list|)
expr_stmt|;
name|r
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<obj-relationship name=\"X\" source=\"S\" target=\"T\" "
operator|+
literal|"collection-type=\"java.util.Map\" map-key=\"bla\"/>\n"
argument_list|,
name|buffer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionType
parameter_list|()
block|{
name|ObjRelationship
name|r
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|r
operator|.
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setCollectionType
argument_list|(
literal|"java.util.Map"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.Map"
argument_list|,
name|r
operator|.
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjRelationship
name|r1
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r1"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"aaaa"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r2
init|=
operator|(
name|ObjRelationship
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|r1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getName
argument_list|()
argument_list|,
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|,
name|r2
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetClientRelationship
parameter_list|()
block|{
specifier|final
name|ObjEntity
name|target
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"te1"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|r1
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r1"
argument_list|)
block|{
specifier|public
name|Entity
name|getTargetEntity
parameter_list|()
block|{
return|return
name|target
return|;
block|}
block|}
decl_stmt|;
name|r1
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntityName
argument_list|(
literal|"te1"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r2
init|=
name|r1
operator|.
name|getClientRelationship
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getName
argument_list|()
argument_list|,
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getTargetEntityName
argument_list|()
argument_list|,
name|r2
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getDeleteRule
argument_list|()
argument_list|,
name|r2
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetReverseDbRelationshipPath
parameter_list|()
block|{
name|ObjEntity
name|artistObjEnt
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjEntity
name|paintingObjEnt
init|=
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
comment|// start with "to many"
name|ObjRelationship
name|r1
init|=
operator|(
name|ObjRelationship
operator|)
name|artistObjEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"toArtist"
argument_list|,
name|r1
operator|.
name|getReverseDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r2
init|=
operator|(
name|ObjRelationship
operator|)
name|paintingObjEnt
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"paintingArray"
argument_list|,
name|r2
operator|.
name|getReverseDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetDbRelationshipPath
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|dbRelationshipsRefreshNeeded
operator|=
literal|false
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy.path"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|dbRelationshipsRefreshNeeded
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dummy.path"
argument_list|,
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|dbRelationshipsRefreshNeeded
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefreshFromPath
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy.path"
argument_list|)
expr_stmt|;
comment|// attempt to resolve must fail - relationship is outside of context,
comment|// plus the path is random
try|try
block|{
name|relationship
operator|.
name|refreshFromPath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"refresh without source entity should have failed."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// attempt to resolve must fail - relationship is outside of context,
comment|// plus the path is random
try|try
block|{
name|relationship
operator|.
name|refreshFromPath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"refresh over a dummy path should have failed."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// finally assemble ObjEntity to make the path valid
name|DbEntity
name|dbEntity1
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST1"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity2
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST2"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity3
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST3"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity3
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"TEST1"
argument_list|)
expr_stmt|;
name|DbRelationship
name|dummyR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"dummy"
argument_list|)
decl_stmt|;
name|dummyR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST2"
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|DbRelationship
name|pathR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"path"
argument_list|)
decl_stmt|;
name|pathR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST3"
argument_list|)
expr_stmt|;
name|pathR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addRelationship
argument_list|(
name|dummyR
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addRelationship
argument_list|(
name|pathR
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|refreshFromPath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|dbRelationshipsRefreshNeeded
argument_list|)
expr_stmt|;
name|List
name|resolvedPath
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resolvedPath
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dummyR
argument_list|,
name|resolvedPath
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pathR
argument_list|,
name|resolvedPath
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCalculateToMany
parameter_list|()
block|{
comment|// assemble fixture....
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity1
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST1"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity2
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST2"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity3
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST3"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity3
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"TEST1"
argument_list|)
expr_stmt|;
name|DbRelationship
name|dummyR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"dummy"
argument_list|)
decl_stmt|;
name|dummyR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST2"
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|DbRelationship
name|pathR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"path"
argument_list|)
decl_stmt|;
name|pathR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST3"
argument_list|)
expr_stmt|;
name|pathR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addRelationship
argument_list|(
name|dummyR
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addRelationship
argument_list|(
name|pathR
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// test how toMany changes dependending on the underlying DbRelationships
comment|// add DbRelationships directly to avoid events to test "calculateToMany"
name|relationship
operator|.
name|dbRelationshipsRefreshNeeded
operator|=
literal|false
expr_stmt|;
name|relationship
operator|.
name|dbRelationships
operator|.
name|add
argument_list|(
name|dummyR
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|calculateToManyValue
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|calculateToManyValue
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
comment|// test chain
name|relationship
operator|.
name|dbRelationships
operator|.
name|add
argument_list|(
name|pathR
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|pathR
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|calculateToManyValue
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCalculateToManyFromPath
parameter_list|()
block|{
comment|// assemble fixture....
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity1
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST1"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity2
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST2"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity3
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST3"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity3
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"TEST1"
argument_list|)
expr_stmt|;
name|DbRelationship
name|dummyR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"dummy"
argument_list|)
decl_stmt|;
name|dummyR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST2"
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|DbRelationship
name|pathR
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"path"
argument_list|)
decl_stmt|;
name|pathR
operator|.
name|setTargetEntityName
argument_list|(
literal|"TEST3"
argument_list|)
expr_stmt|;
name|pathR
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addRelationship
argument_list|(
name|dummyR
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addRelationship
argument_list|(
name|pathR
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// test how toMany changes when the path is set as a string
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|dummyR
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
comment|// test chain
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy.path"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|pathR
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
literal|"dummy.path"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTargetEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"some_rel"
argument_list|)
decl_stmt|;
name|relationship
operator|.
name|setTargetEntityName
argument_list|(
literal|"targ"
argument_list|)
expr_stmt|;
try|try
block|{
name|relationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Without a container, getTargetEntity() must fail."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// assemble container
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|src
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"src"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|src
operator|.
name|addRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|target
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"targ"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|target
argument_list|,
name|relationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetReverseRel1
parameter_list|()
block|{
name|ObjEntity
name|artistObjEnt
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjEntity
name|paintingObjEnt
init|=
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
comment|// start with "to many"
name|ObjRelationship
name|r1
init|=
operator|(
name|ObjRelationship
operator|)
name|artistObjEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|r2
init|=
name|r1
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|paintingObjEnt
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetReverseRel2
parameter_list|()
block|{
name|ObjEntity
name|artistEnt
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjEntity
name|paintingEnt
init|=
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
comment|// start with "to one"
name|ObjRelationship
name|r1
init|=
operator|(
name|ObjRelationship
operator|)
name|paintingEnt
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|r2
init|=
name|r1
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSingleDbRelationship
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|relationship
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
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|isToMany
argument_list|()
argument_list|,
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|removeDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFlattenedRelationship
parameter_list|()
block|{
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setSourceEntity
argument_list|(
name|artistDBEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|artistExhibitDBEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setSourceEntity
argument_list|(
name|artistExhibitDBEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|exhibitDBEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
argument_list|,
name|relationship
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
name|assertEquals
argument_list|(
name|r2
argument_list|,
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|removeDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
comment|// only remaining rel is r2... a toOne
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r2
argument_list|,
name|relationship
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
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadOnlyMoreThan3DbRelsRelationship
parameter_list|()
block|{
comment|// Readonly is a flattened relationship that isn't over a single many->many link
comment|// table
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r3
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"Z"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setSourceEntity
argument_list|(
name|artistDBEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|artistExhibitDBEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setSourceEntity
argument_list|(
name|artistExhibitDBEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|exhibitDBEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r3
operator|.
name|setSourceEntity
argument_list|(
name|exhibitDBEntity
argument_list|)
expr_stmt|;
name|r3
operator|.
name|setTargetEntity
argument_list|(
name|galleryDBEntity
argument_list|)
expr_stmt|;
name|r3
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Test for a read-only flattened relationship that is readonly because it's dbrel
comment|// sequence is "incorrect" (or rather, unsupported)
specifier|public
name|void
name|testIncorrectSequenceReadOnlyRelationship
parameter_list|()
block|{
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setSourceEntity
argument_list|(
name|artistDBEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|paintingDbEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setSourceEntity
argument_list|(
name|paintingDbEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|galleryDBEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Test a relationship loaded from the test datamap that we know should be flattened
specifier|public
name|void
name|testKnownFlattenedRelationship
parameter_list|()
block|{
name|ObjEntity
name|artistEnt
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|theRel
init|=
operator|(
name|ObjRelationship
operator|)
name|artistEnt
operator|.
name|getRelationship
argument_list|(
literal|"groupArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|theRel
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|theRel
operator|.
name|isFlattened
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|theRel
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBadDeleteRuleValue
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
try|try
block|{
name|relationship
operator|.
name|setDeleteRule
argument_list|(
literal|999
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed with IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Good... it should throw an exception
block|}
block|}
specifier|public
name|void
name|testOkDeleteRuleValue
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
try|try
block|{
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|CASCADE
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have thrown an exception :"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testWatchesDbRelChanges
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|addDbRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
comment|// rel should be watching r1 (events) to see when that changes
name|r1
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

