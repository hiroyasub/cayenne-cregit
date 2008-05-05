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
name|util
operator|.
name|HashMap
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
name|cayenne
operator|.
name|CayenneDataObject
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
name|parser
operator|.
name|ASTObjPath
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

begin_class
specifier|public
class|class
name|ObjEntityTest
extends|extends
name|CayenneCase
block|{
comment|/**      * @deprecated since 3.0 as the method being tested is deprecated.      */
specifier|public
name|void
name|testLastPathComponentLegacy
parameter_list|()
block|{
name|ObjEntity
name|artistE
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|Object
name|lastAttribute
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastAttribute
operator|instanceof
name|ObjAttribute
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"paintingTitle"
argument_list|,
operator|(
operator|(
name|ObjAttribute
operator|)
name|lastAttribute
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|lastRelationship
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.toGallery"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastRelationship
operator|instanceof
name|ObjRelationship
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toGallery"
argument_list|,
operator|(
operator|(
name|ObjRelationship
operator|)
name|lastRelationship
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|lastLeftJoinRelationship
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"paintingArray+.toGallery+"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastLeftJoinRelationship
operator|instanceof
name|ObjRelationship
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toGallery"
argument_list|,
operator|(
operator|(
name|ObjRelationship
operator|)
name|lastLeftJoinRelationship
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLastPathComponent
parameter_list|()
block|{
name|ObjEntity
name|artistE
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|aliases
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"paintingArray.toGallery"
argument_list|)
expr_stmt|;
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|lastAttribute
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|)
argument_list|,
name|aliases
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastAttribute
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"paintingTitle"
argument_list|,
name|lastAttribute
operator|.
name|getAttribute
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|lastRelationship
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.toGallery"
argument_list|)
argument_list|,
name|aliases
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastRelationship
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toGallery"
argument_list|,
name|lastRelationship
operator|.
name|getRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|lastLeftJoinRelationship
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"paintingArray+.toGallery+"
argument_list|)
argument_list|,
name|aliases
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastLeftJoinRelationship
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toGallery"
argument_list|,
name|lastLeftJoinRelationship
operator|.
name|getRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|lastAliasedRelationship
init|=
name|artistE
operator|.
name|lastPathComponent
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|aliases
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lastAliasedRelationship
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toGallery"
argument_list|,
name|lastAliasedRelationship
operator|.
name|getRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGeneric
parameter_list|()
block|{
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"e1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e1
operator|.
name|isGeneric
argument_list|()
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setClassName
argument_list|(
literal|"SomeClass"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e1
operator|.
name|isGeneric
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|m
init|=
operator|new
name|DataMap
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|m
operator|.
name|setDefaultSuperclass
argument_list|(
literal|"SomeClass"
argument_list|)
expr_stmt|;
name|m
operator|.
name|addObjEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e1
operator|.
name|isGeneric
argument_list|()
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setClassName
argument_list|(
literal|"SomeOtherClass"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e1
operator|.
name|isGeneric
argument_list|()
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setClassName
argument_list|(
name|CayenneDataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e1
operator|.
name|isGeneric
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testServerOnly
parameter_list|()
block|{
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"e1"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|e1
operator|.
name|isServerOnly
argument_list|()
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setServerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e1
operator|.
name|isServerOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClientAllowed
parameter_list|()
block|{
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"e1"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"No parent DataMap should have automatically disabled client."
argument_list|,
name|e1
operator|.
name|isClientAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"m1"
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e1
operator|.
name|isClientAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setClientSupported
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e1
operator|.
name|isClientAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setServerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e1
operator|.
name|isClientAllowed
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetPrimaryKeyNames
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbentity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"dbe"
argument_list|)
decl_stmt|;
comment|// need a container
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
comment|// Test correctness with no mapped PK.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add a single column PK to the DB entity.
name|DbAttribute
name|pk
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|pk
operator|.
name|setName
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbentity
operator|.
name|addAttribute
argument_list|(
name|pk
argument_list|)
expr_stmt|;
comment|// Test correctness with a single column PK.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|contains
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add a multi-column PK to the DB entity.
name|DbAttribute
name|pk2
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|pk2
operator|.
name|setName
argument_list|(
literal|"id2"
argument_list|)
expr_stmt|;
name|pk2
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbentity
operator|.
name|addAttribute
argument_list|(
name|pk2
argument_list|)
expr_stmt|;
comment|// Test correctness with a multi-column PK.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|contains
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
operator|.
name|contains
argument_list|(
name|pk2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetClientEntity
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|setClientSupported
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|map
operator|.
name|addObjEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setClassName
argument_list|(
literal|"x.y.z"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setClientClassName
argument_list|(
literal|"a.b.c"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"A1"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"A2"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|DbEntity
name|dbentity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"dbe"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r1
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r1"
argument_list|)
block|{
annotation|@
name|Override
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
name|e1
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|ObjEntity
name|e2
init|=
name|e1
operator|.
name|getClientEntity
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
operator|.
name|getName
argument_list|()
argument_list|,
name|e2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
operator|.
name|getClientClassName
argument_list|()
argument_list|,
name|e2
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|e2
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|e2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
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
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|ObjEntity
name|d1
init|=
operator|(
name|ObjEntity
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|d1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDbEntityName
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"dbe"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dbe"
argument_list|,
name|entity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDbEntity
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|DbEntity
name|dbentity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"dbe"
argument_list|)
decl_stmt|;
comment|// need a container
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntity
argument_list|(
name|dbentity
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbentity
argument_list|,
name|entity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntity
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"dbe"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbentity
argument_list|,
name|entity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDbEntityNoContainer
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setDbEntityName
argument_list|(
literal|"dbe"
argument_list|)
expr_stmt|;
try|try
block|{
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Without a container ObjENtity shouldn't resolve DbEntity"
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
block|}
specifier|public
name|void
name|testClassName
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuperClassName
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|String
name|tstName
init|=
literal|"super_tst_name"
decl_stmt|;
name|entity
operator|.
name|setSuperClassName
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|entity
operator|.
name|getSuperClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAttributeForDbAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|ae
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DbEntity
name|dae
init|=
name|ae
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ae
operator|.
name|getAttributeForDbAttribute
argument_list|(
operator|(
name|DbAttribute
operator|)
name|dae
operator|.
name|getAttribute
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ae
operator|.
name|getAttributeForDbAttribute
argument_list|(
operator|(
name|DbAttribute
operator|)
name|dae
operator|.
name|getAttribute
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRelationshipForDbRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|ae
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DbEntity
name|dae
init|=
name|ae
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ae
operator|.
name|getRelationshipForDbRelationship
argument_list|(
operator|new
name|DbRelationship
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ae
operator|.
name|getRelationshipForDbRelationship
argument_list|(
operator|(
name|DbRelationship
operator|)
name|dae
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|entity
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setReadOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entity
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntityIndependentPath
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|artistE
operator|.
name|translateToRelatedEntity
argument_list|(
name|e1
argument_list|,
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure: "
operator|+
name|translated
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:toArtist.paintingArray"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntityTrimmedPath
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"artistExhibitArray.toExhibit"
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|artistE
operator|.
name|translateToRelatedEntity
argument_list|(
name|e1
argument_list|,
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure: "
operator|+
name|translated
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:toExhibit"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntitySplitHalfWay
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray.toPaintingInfo.textReview"
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|artistE
operator|.
name|translateToRelatedEntity
argument_list|(
name|e1
argument_list|,
literal|"paintingArray.toGallery"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure: "
operator|+
name|translated
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:paintingArray.toPaintingInfo.TEXT_REVIEW"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntityMatchingPath
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"artistExhibitArray.toExhibit"
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|artistE
operator|.
name|translateToRelatedEntity
argument_list|(
name|e1
argument_list|,
literal|"artistExhibitArray.toExhibit"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure: "
operator|+
name|translated
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:artistExhibitArray.toExhibit"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntityMultiplePaths
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"paintingArray = $p and artistExhibitArray.toExhibit.closingDate = $d"
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|artistE
operator|.
name|translateToRelatedEntity
argument_list|(
name|e1
argument_list|,
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure: "
operator|+
name|translated
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:toArtist.paintingArray = $p and db:toExhibit.CLOSING_DATE = $d"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

