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
name|Collection
import|;
end_import

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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ObjEntityTest
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
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
block|}
specifier|public
name|void
name|testGetAttributeWithOverrides
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"dm"
argument_list|)
decl_stmt|;
name|ObjEntity
name|superEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"super"
argument_list|)
decl_stmt|;
name|superEntity
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"a1"
argument_list|,
literal|"int"
argument_list|,
name|superEntity
argument_list|)
argument_list|)
expr_stmt|;
name|superEntity
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"a2"
argument_list|,
literal|"int"
argument_list|,
name|superEntity
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|superEntity
argument_list|)
expr_stmt|;
name|ObjEntity
name|subEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"sub"
argument_list|)
decl_stmt|;
name|subEntity
operator|.
name|setSuperEntityName
argument_list|(
name|superEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|subEntity
operator|.
name|addAttributeOverride
argument_list|(
literal|"a1"
argument_list|,
literal|"overridden.path"
argument_list|)
expr_stmt|;
name|subEntity
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"a3"
argument_list|,
literal|"int"
argument_list|,
name|subEntity
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|subEntity
argument_list|)
expr_stmt|;
name|ObjAttribute
name|a1
init|=
operator|(
name|ObjAttribute
operator|)
name|subEntity
operator|.
name|getAttribute
argument_list|(
literal|"a1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|subEntity
argument_list|,
name|a1
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"overridden.path"
argument_list|,
name|a1
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int"
argument_list|,
name|a1
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|a2
init|=
operator|(
name|ObjAttribute
operator|)
name|subEntity
operator|.
name|getAttribute
argument_list|(
literal|"a2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|subEntity
argument_list|,
name|a2
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|a2
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|a3
init|=
operator|(
name|ObjAttribute
operator|)
name|subEntity
operator|.
name|getAttribute
argument_list|(
literal|"a3"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|subEntity
argument_list|,
name|a3
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetPrimaryKeys
parameter_list|()
block|{
name|ObjEntity
name|artistE
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|pks
init|=
name|artistE
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|pk
init|=
name|pks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Long"
argument_list|,
name|pk
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|pk
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artistId"
argument_list|,
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|pk
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistE
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|pk
argument_list|)
argument_list|)
expr_stmt|;
name|ObjEntity
name|clientArtistE
init|=
name|artistE
operator|.
name|getClientEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|clientpks
init|=
name|clientArtistE
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|clientpks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|clientPk
init|=
name|clientpks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Long"
argument_list|,
name|clientPk
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|clientPk
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artistId"
argument_list|,
name|clientPk
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|clientPk
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|clientArtistE
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|pk
argument_list|)
argument_list|)
expr_stmt|;
name|ObjEntity
name|meaningfulPKE
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|mpks
init|=
name|meaningfulPKE
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mpks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|mpk
init|=
name|mpks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|meaningfulPKE
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|mpk
argument_list|)
argument_list|)
expr_stmt|;
name|ObjEntity
name|clientMeaningfulPKE
init|=
name|meaningfulPKE
operator|.
name|getClientEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|clientmpks
init|=
name|clientMeaningfulPKE
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|clientmpks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|clientmpk
init|=
name|clientmpks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|clientmpk
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clientMeaningfulPKE
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|clientmpk
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAttributes
parameter_list|()
block|{
comment|// ObjEntity artistE = getObjEntity("Artist");
name|ObjEntity
name|artistE
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjAttribute
name|attr
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
name|assertEquals
argument_list|(
name|attr
operator|.
name|getMaxLength
argument_list|()
argument_list|,
name|attr
operator|.
name|getDbAttribute
argument_list|()
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attr
operator|.
name|isMandatory
argument_list|()
argument_list|,
name|attr
operator|.
name|getDbAttribute
argument_list|()
operator|.
name|isMandatory
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
literal|"db:toArtist.artistExhibitArray.toExhibit"
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
literal|"db:paintingArray.toArtist.paintingArray.toPaintingInfo.TEXT_REVIEW"
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
literal|"db:artistExhibitArray.toArtist.artistExhibitArray.toExhibit"
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
literal|"db:toArtist.paintingArray = $p "
operator|+
literal|"and db:toArtist.artistExhibitArray.toExhibit.CLOSING_DATE = $d"
argument_list|)
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateNullArg
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
literal|"dateOfBirth"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Expression
name|translated
init|=
name|entity
operator|.
name|translateToDbPath
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|translated
operator|.
name|match
argument_list|(
operator|new
name|Artist
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

