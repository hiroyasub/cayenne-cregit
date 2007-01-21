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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|DbEntityTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|DbAttribute
name|pk
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"pk"
argument_list|)
decl_stmt|;
name|pk
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|DbAttribute
name|generated
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"generated"
argument_list|)
decl_stmt|;
name|generated
operator|.
name|setGenerated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|DbEntity
name|d2
init|=
operator|(
name|DbEntity
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|pk2
init|=
operator|(
name|DbAttribute
operator|)
name|d2
operator|.
name|getAttribute
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|pk2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|generated2
init|=
operator|(
name|DbAttribute
operator|)
name|d2
operator|.
name|getAttribute
argument_list|(
name|generated
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|generated2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializabilityWithHessian
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|DbAttribute
name|pk
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"pk"
argument_list|)
decl_stmt|;
name|pk
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|DbAttribute
name|generated
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"generated"
argument_list|)
decl_stmt|;
name|generated
operator|.
name|setGenerated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|DbEntity
name|d2
init|=
operator|(
name|DbEntity
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|entity
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|pk2
init|=
operator|(
name|DbAttribute
operator|)
name|d2
operator|.
name|getAttribute
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|pk2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|generated2
init|=
operator|(
name|DbAttribute
operator|)
name|d2
operator|.
name|getAttribute
argument_list|(
name|generated
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getGeneratedAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|generated2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructor1
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructor2
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCatalog
parameter_list|()
block|{
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|ent
operator|.
name|setCatalog
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|ent
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSchema
parameter_list|()
block|{
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|ent
operator|.
name|setSchema
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|ent
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFullyQualifiedName
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|String
name|schemaName
init|=
literal|"tst_schema_name"
decl_stmt|;
name|ent
operator|.
name|setName
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|setSchema
argument_list|(
name|schemaName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|schemaName
operator|+
literal|"."
operator|+
name|tstName
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetPrimaryKey
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|DbAttribute
name|a2
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a2
operator|.
name|setName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|List
name|pk
init|=
name|ent
operator|.
name|getPrimaryKey
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a2
argument_list|,
name|pk
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
name|testAddPKAttribute
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeAttributeToPK
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangePKAttribute
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ent
operator|.
name|getPrimaryKey
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveAttribute
parameter_list|()
block|{
name|DbEntity
name|ent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
decl_stmt|;
name|ent
operator|.
name|setName
argument_list|(
literal|"ent"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|DbEntity
name|otherEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"22ent1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|otherEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|otherEntity
argument_list|)
expr_stmt|;
name|DbAttribute
name|a11
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a11
operator|.
name|setName
argument_list|(
literal|"a11"
argument_list|)
expr_stmt|;
name|a11
operator|.
name|setPrimaryKey
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|otherEntity
operator|.
name|addAttribute
argument_list|(
name|a11
argument_list|)
expr_stmt|;
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"relfrom"
argument_list|)
decl_stmt|;
name|ent
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setTargetEntity
argument_list|(
name|otherEntity
argument_list|)
expr_stmt|;
name|rel
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rel
argument_list|,
literal|"a1"
argument_list|,
literal|"a11"
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|rel1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"relto"
argument_list|)
decl_stmt|;
name|otherEntity
operator|.
name|addRelationship
argument_list|(
name|rel1
argument_list|)
expr_stmt|;
name|rel1
operator|.
name|setTargetEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|rel1
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rel1
argument_list|,
literal|"a11"
argument_list|,
literal|"a1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that the test case is working
name|assertSame
argument_list|(
name|a1
argument_list|,
name|ent
operator|.
name|getAttribute
argument_list|(
name|a1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rel
argument_list|,
name|ent
operator|.
name|getRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// test removal
name|ent
operator|.
name|removeAttribute
argument_list|(
name|a1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ent
operator|.
name|getAttribute
argument_list|(
name|a1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|rel1
operator|.
name|getJoins
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
name|rel
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTranslateToRelatedEntityIndependentPath
parameter_list|()
block|{
name|DbEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
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
literal|"db:paintingArray"
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
block|{
name|DbEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
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
literal|"db:artistExhibitArray.toExhibit"
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
block|{
name|DbEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
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
literal|"db:paintingArray.toPaintingInfo.TEXT_REVIEW"
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
block|{
name|DbEntity
name|artistE
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
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
literal|"db:artistExhibitArray.toExhibit"
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
block|}
end_class

end_unit

