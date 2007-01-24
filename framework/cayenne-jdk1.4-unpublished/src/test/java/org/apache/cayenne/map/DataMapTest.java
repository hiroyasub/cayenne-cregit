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
name|project
operator|.
name|NamedObjectFactory
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
name|AbstractQuery
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
name|MockAbstractQuery
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * DataMap unit tests.  */
end_comment

begin_class
specifier|public
class|class
name|DataMapTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DataMap
name|d1
init|=
operator|(
name|DataMap
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|m1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|m1
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
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"oe1"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|DataMap
name|d2
init|=
operator|(
name|DataMap
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|m1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getObjEntity
argument_list|(
name|oe1
operator|.
name|getName
argument_list|()
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
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|DataMap
name|d1
init|=
operator|(
name|DataMap
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|m1
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|m1
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
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"oe1"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|DataMap
name|d2
init|=
operator|(
name|DataMap
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|m1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
operator|.
name|getObjEntity
argument_list|(
name|oe1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInitWithProperties
parameter_list|()
block|{
name|Map
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|DataMap
operator|.
name|CLIENT_SUPPORTED_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|DataMap
operator|.
name|DEFAULT_CLIENT_PACKAGE_PROPERTY
argument_list|,
literal|"aaaaa"
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isClientSupported
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aaaaa"
argument_list|,
name|map
operator|.
name|getDefaultClientPackage
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: test other defaults
block|}
specifier|public
name|void
name|testDefaultSchema
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstSchema
init|=
literal|"tst_schema"
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultSchema
argument_list|(
name|tstSchema
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstSchema
argument_list|,
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultSchema
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultClientPackage
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstPackage
init|=
literal|"tst.pkg"
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultClientPackage
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultClientPackage
argument_list|(
name|tstPackage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstPackage
argument_list|,
name|map
operator|.
name|getDefaultClientPackage
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultClientPackage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultClientPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultPackage
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstPackage
init|=
literal|"tst.pkg"
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultPackage
argument_list|(
name|tstPackage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstPackage
argument_list|,
name|map
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultPackage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultSuperclass
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstSuperclass
init|=
literal|"tst_superclass"
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultSuperclass
argument_list|(
name|tstSuperclass
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstSuperclass
argument_list|,
name|map
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultSuperclass
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultLockType
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ObjEntity
operator|.
name|LOCK_TYPE_NONE
argument_list|,
name|map
operator|.
name|getDefaultLockType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultLockType
argument_list|(
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
argument_list|,
name|map
operator|.
name|getDefaultLockType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultLockType
argument_list|(
name|ObjEntity
operator|.
name|LOCK_TYPE_NONE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjEntity
operator|.
name|LOCK_TYPE_NONE
argument_list|,
name|map
operator|.
name|getDefaultLockType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testName
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|map
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
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLocation
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|setLocation
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|map
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddObjEntity
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|e
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|e
operator|.
name|setClassName
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|map
operator|.
name|getObjEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|e
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddEntityWithSameName
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
comment|// Give them different class-names... we are only testing for the same entity name
comment|// being a problem
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"c"
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setClassName
argument_list|(
literal|"c1"
argument_list|)
expr_stmt|;
name|ObjEntity
name|e2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"c"
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setClassName
argument_list|(
literal|"c2"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
try|try
block|{
name|map
operator|.
name|addObjEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should not be able to add more than one entity with the same name"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
specifier|public
name|void
name|testRemoveThenAddNullClassName
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
comment|// It should be possible to cleanly remove and then add the same entity again.
comment|// Uncovered the need for this while testing modeller manually.
name|ObjEntity
name|e
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"f"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveObjEntity
parameter_list|()
block|{
comment|// make sure deleting an ObjEntity& other entity's relationships to it
comment|// works& does not cause a ConcurrentModificationException
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"2"
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
decl_stmt|;
name|r1
operator|.
name|setTargetEntityName
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r2
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r2"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|setTargetEntityName
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r3
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r3"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setTargetEntityName
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r4
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"r4"
argument_list|)
decl_stmt|;
name|r4
operator|.
name|setTargetEntityName
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|e2
operator|.
name|addRelationship
argument_list|(
name|r3
argument_list|)
expr_stmt|;
name|e2
operator|.
name|addRelationship
argument_list|(
name|r4
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
literal|"1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
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
name|map
operator|.
name|removeObjEntity
argument_list|(
literal|"2"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleNullClassNames
parameter_list|()
block|{
comment|// Now possible to have more than one objEntity with a null class name.
comment|// This test proves it
name|ObjEntity
name|e1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"g"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"h"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveThenAddRealClassName
parameter_list|()
block|{
name|ObjEntity
name|e
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"f"
argument_list|)
decl_stmt|;
name|e
operator|.
name|setClassName
argument_list|(
literal|"f"
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddEmbeddable
parameter_list|()
block|{
name|Embeddable
name|e
init|=
operator|new
name|Embeddable
argument_list|(
literal|"XYZ"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
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
name|map
operator|.
name|addEmbeddable
argument_list|(
name|e
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
name|assertTrue
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|contains
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveEmbeddable
parameter_list|()
block|{
name|Embeddable
name|e
init|=
operator|new
name|Embeddable
argument_list|(
literal|"XYZ"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addEmbeddable
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|contains
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeEmbeddable
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|contains
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeEmbeddable
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|contains
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddDbEntity
parameter_list|()
block|{
name|DbEntity
name|e
init|=
operator|new
name|DbEntity
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|map
operator|.
name|getDbEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|e
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddQuery
parameter_list|()
block|{
name|AbstractQuery
name|q
init|=
operator|new
name|MockAbstractQuery
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|q
argument_list|,
name|map
operator|.
name|getQuery
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveQuery
parameter_list|()
block|{
name|AbstractQuery
name|q
init|=
operator|new
name|MockAbstractQuery
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|q
argument_list|,
name|map
operator|.
name|getQuery
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeQuery
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getQuery
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQueryMap
parameter_list|()
block|{
name|AbstractQuery
name|q
init|=
operator|new
name|MockAbstractQuery
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|Map
name|queries
init|=
name|map
operator|.
name|getQueryMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|q
argument_list|,
name|queries
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// make sure deleting a DbEntity& other entity's relationships to it
comment|// works& does not cause a ConcurrentModificationException
specifier|public
name|void
name|testRemoveDbEntity
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
comment|// create a twisty maze of intermingled relationships.
name|DbEntity
name|e1
init|=
operator|(
name|DbEntity
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
literal|"e1"
argument_list|)
expr_stmt|;
name|DbEntity
name|e2
init|=
operator|(
name|DbEntity
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setName
argument_list|(
literal|"e2"
argument_list|)
expr_stmt|;
name|DbRelationship
name|r1
init|=
operator|(
name|DbRelationship
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
name|e1
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setName
argument_list|(
literal|"r1"
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|DbRelationship
name|r2
init|=
operator|(
name|DbRelationship
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
name|e2
argument_list|)
decl_stmt|;
name|r2
operator|.
name|setName
argument_list|(
literal|"r2"
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|DbRelationship
name|r3
init|=
operator|(
name|DbRelationship
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
name|e1
argument_list|)
decl_stmt|;
name|r3
operator|.
name|setName
argument_list|(
literal|"r3"
argument_list|)
expr_stmt|;
name|r3
operator|.
name|setTargetEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addRelationship
argument_list|(
name|r3
argument_list|)
expr_stmt|;
name|e2
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|e2
operator|.
name|addRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|e2
operator|.
name|addRelationship
argument_list|(
name|r3
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|e2
argument_list|)
expr_stmt|;
comment|// now actually test something
name|map
operator|.
name|removeDbEntity
argument_list|(
name|e1
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|e1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|e2
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|e2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChildProcedures
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|checkProcedures
argument_list|(
name|map
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|map
operator|.
name|addProcedure
argument_list|(
operator|new
name|Procedure
argument_list|(
literal|"proc1"
argument_list|)
argument_list|)
expr_stmt|;
name|checkProcedures
argument_list|(
name|map
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"proc1"
block|}
argument_list|)
expr_stmt|;
name|map
operator|.
name|addProcedure
argument_list|(
operator|new
name|Procedure
argument_list|(
literal|"proc2"
argument_list|)
argument_list|)
expr_stmt|;
name|checkProcedures
argument_list|(
name|map
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"proc1"
block|,
literal|"proc2"
block|}
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeProcedure
argument_list|(
literal|"proc2"
argument_list|)
expr_stmt|;
name|checkProcedures
argument_list|(
name|map
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"proc1"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|checkProcedures
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
index|[]
name|expectedNames
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|len
init|=
name|expectedNames
operator|.
name|length
decl_stmt|;
name|Map
name|proceduresMap
init|=
name|map
operator|.
name|getProcedureMap
argument_list|()
decl_stmt|;
name|Collection
name|proceduresCollection
init|=
name|map
operator|.
name|getProcedures
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|proceduresMap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|len
argument_list|,
name|proceduresMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|proceduresCollection
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|len
argument_list|,
name|proceduresCollection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Procedure
name|proc
init|=
name|map
operator|.
name|getProcedure
argument_list|(
name|expectedNames
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedNames
index|[
name|i
index|]
argument_list|,
name|proc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

