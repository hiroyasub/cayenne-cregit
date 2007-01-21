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
name|Iterator
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
name|EntityTest
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
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|Entity
name|d1
init|=
operator|(
name|Entity
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
name|entity
operator|.
name|addAttribute
argument_list|(
operator|new
name|MockAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
operator|new
name|MockRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|Entity
name|d2
init|=
operator|(
name|Entity
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
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test that ref collection wrappers are still working
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|d2
operator|.
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getRelationships
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getRelationships
argument_list|()
operator|.
name|contains
argument_list|(
name|d2
operator|.
name|getRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getAttributeMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|d2
operator|.
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
name|d2
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getRelationshipMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getRelationshipMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|d2
operator|.
name|getRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|,
name|d2
operator|.
name|getRelationshipMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"xyz"
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
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|(
literal|"entity"
argument_list|)
decl_stmt|;
name|Entity
name|d1
init|=
operator|(
name|Entity
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
name|entity
operator|.
name|addAttribute
argument_list|(
operator|new
name|MockAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
operator|new
name|MockRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|Entity
name|d2
init|=
operator|(
name|Entity
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
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test that ref collection wrappers are still working
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|d2
operator|.
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getRelationships
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|d2
operator|.
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getAttributeMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|d2
operator|.
name|getAttribute
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
name|d2
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getRelationshipMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|d2
operator|.
name|getRelationshipMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|d2
operator|.
name|getRelationship
argument_list|(
literal|"xyz"
argument_list|)
argument_list|,
name|d2
operator|.
name|getRelationshipMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testName
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|entity
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
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAttribute
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|Attribute
name|attribute
init|=
operator|new
name|MockAttribute
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|attribute
argument_list|,
name|entity
operator|.
name|getAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// attribute must have its entity switched to our entity.
name|assertSame
argument_list|(
name|entity
argument_list|,
name|attribute
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove attribute
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRelationship
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rel
argument_list|,
name|entity
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
comment|// attribute must have its entity switched to our entity.
name|assertSame
argument_list|(
name|entity
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove attribute
name|entity
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
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
block|}
specifier|public
name|void
name|testAttributeClashWithRelationship
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
try|try
block|{
name|Attribute
name|attribute
init|=
operator|new
name|MockAttribute
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception should have been thrown due to clashing attribute and relationship names."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Exception expected.
block|}
block|}
specifier|public
name|void
name|testRelationshipClashWithAttribute
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|Attribute
name|attribute
init|=
operator|new
name|MockAttribute
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
try|try
block|{
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|(
literal|"tst_name"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception should have been thrown due to clashing attribute and relationship names."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Exception expected.
block|}
block|}
specifier|public
name|void
name|testResolveBadObjPath1
parameter_list|()
block|{
comment|// test invalid expression path
name|Expression
name|pathExpr
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|pathExpr
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"invalid.invalid"
argument_list|)
expr_stmt|;
comment|// itertator should be returned, but when trying to read 1st component,
comment|// it should throw an exception....
name|ObjEntity
name|galleryEnt
init|=
name|getObjEntity
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|galleryEnt
operator|.
name|resolvePathComponents
argument_list|(
name|pathExpr
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
specifier|public
name|void
name|testResolveBadObjPath2
parameter_list|()
block|{
comment|// test invalid expression type
name|Expression
name|badPathExpr
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|IN
argument_list|)
decl_stmt|;
name|badPathExpr
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"a.b.c"
argument_list|)
expr_stmt|;
name|ObjEntity
name|galleryEnt
init|=
name|getObjEntity
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
try|try
block|{
name|galleryEnt
operator|.
name|resolvePathComponents
argument_list|(
name|badPathExpr
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
specifier|public
name|void
name|testResolveObjPath1
parameter_list|()
block|{
name|Expression
name|pathExpr
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|pathExpr
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"galleryName"
argument_list|)
expr_stmt|;
name|ObjEntity
name|galleryEnt
init|=
name|getObjEntity
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|galleryEnt
operator|.
name|resolvePathComponents
argument_list|(
name|pathExpr
argument_list|)
decl_stmt|;
comment|// iterator must contain a single ObjAttribute
name|assertNotNull
argument_list|(
name|it
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|next
init|=
operator|(
name|ObjAttribute
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|galleryEnt
operator|.
name|getAttribute
argument_list|(
literal|"galleryName"
argument_list|)
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveAttribute
parameter_list|()
block|{
name|Entity
name|entity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|ObjAttribute
name|attribute1
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"a1"
argument_list|)
decl_stmt|;
name|ObjAttribute
name|attribute2
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"a2"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute1
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute2
argument_list|)
expr_stmt|;
name|Collection
name|attributes
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attributes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|removeAttribute
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|attributes
operator|=
name|entity
operator|.
name|getAttributes
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|attributes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

