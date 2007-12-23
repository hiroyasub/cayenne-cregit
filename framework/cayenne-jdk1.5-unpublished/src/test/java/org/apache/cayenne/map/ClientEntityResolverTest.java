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
name|Collections
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
import|;
end_import

begin_class
specifier|public
class|class
name|ClientEntityResolverTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSerializabilityWithHessian
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
literal|"test_entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
name|Artist
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|maps
argument_list|)
decl_stmt|;
comment|// 1. simple case
name|Object
name|c1
init|=
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|resolver
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c1
operator|instanceof
name|EntityResolver
argument_list|)
expr_stmt|;
name|EntityResolver
name|cr1
init|=
operator|(
name|EntityResolver
operator|)
name|c1
decl_stmt|;
name|assertNotSame
argument_list|(
name|resolver
argument_list|,
name|cr1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cr1
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cr1
operator|.
name|getObjEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// 2. with descriptors resolved...
name|assertNotNull
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EntityResolver
name|cr2
init|=
operator|(
name|EntityResolver
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|resolver
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cr2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cr2
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cr2
operator|.
name|getObjEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cr2
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test_entity"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setClassName
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|maps
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInheritance
parameter_list|()
block|{
name|ObjEntity
name|superEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"super_entity"
argument_list|)
decl_stmt|;
name|superEntity
operator|.
name|setClassName
argument_list|(
literal|"java.lang.Object"
argument_list|)
expr_stmt|;
name|ObjEntity
name|subEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"sub_entity"
argument_list|)
decl_stmt|;
name|subEntity
operator|.
name|setClassName
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
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
try|try
block|{
name|subEntity
operator|.
name|getSuperEntity
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"hmm... superentity can't possibly be resolved at this point."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|superEntity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|subEntity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
operator|new
name|EntityResolver
argument_list|(
name|maps
argument_list|)
expr_stmt|;
comment|// after registration with resolver super entity should resolve just fine
name|assertSame
argument_list|(
name|superEntity
argument_list|,
name|subEntity
operator|.
name|getSuperEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

